package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PacienteAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.PacienteRepository;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.mapper.PacienteMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_CURP = "AAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CURP = "BBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SEXO = "AAAAAAAAAA";
    private static final String UPDATED_SEXO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_SEGURO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_SEGURO_SOCIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PacienteMapper pacienteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Paciente paciente;

    private Paciente insertedPaciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity() {
        return new Paciente()
            .curp(DEFAULT_CURP)
            .nombre(DEFAULT_NOMBRE)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .sexo(DEFAULT_SEXO)
            .numeroSeguroSocial(DEFAULT_NUMERO_SEGURO_SOCIAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity() {
        return new Paciente()
            .curp(UPDATED_CURP)
            .nombre(UPDATED_NOMBRE)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .numeroSeguroSocial(UPDATED_NUMERO_SEGURO_SOCIAL);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Paciente.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaciente != null) {
            pacienteRepository.delete(insertedPaciente).block();
            insertedPaciente = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPaciente() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);
        var returnedPacienteDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PacienteDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Paciente in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaciente = pacienteMapper.toEntity(returnedPacienteDTO);
        assertPacienteUpdatableFieldsEquals(returnedPaciente, getPersistedPaciente(returnedPaciente));

        insertedPaciente = returnedPaciente;
    }

    @Test
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCurpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setCurp(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setNombre(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaNacimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setFechaNacimiento(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkSexoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paciente.setSexo(null);

        // Create the Paciente, which fails.
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPacientes() {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        // Get all the pacienteList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(paciente.getId().intValue()))
            .jsonPath("$.[*].curp")
            .value(hasItem(DEFAULT_CURP))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].fechaNacimiento")
            .value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.[*].sexo")
            .value(hasItem(DEFAULT_SEXO))
            .jsonPath("$.[*].numeroSeguroSocial")
            .value(hasItem(DEFAULT_NUMERO_SEGURO_SOCIAL));
    }

    @Test
    void getPaciente() {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        // Get the paciente
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paciente.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paciente.getId().intValue()))
            .jsonPath("$.curp")
            .value(is(DEFAULT_CURP))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.fechaNacimiento")
            .value(is(DEFAULT_FECHA_NACIMIENTO.toString()))
            .jsonPath("$.sexo")
            .value(is(DEFAULT_SEXO))
            .jsonPath("$.numeroSeguroSocial")
            .value(is(DEFAULT_NUMERO_SEGURO_SOCIAL));
    }

    @Test
    void getNonExistingPaciente() {
        // Get the paciente
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaciente() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).block();
        updatedPaciente
            .curp(UPDATED_CURP)
            .nombre(UPDATED_NOMBRE)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .numeroSeguroSocial(UPDATED_NUMERO_SEGURO_SOCIAL);
        PacienteDTO pacienteDTO = pacienteMapper.toDto(updatedPaciente);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPacienteToMatchAllProperties(updatedPaciente);
    }

    @Test
    void putNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente.curp(UPDATED_CURP).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPaciente))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaciente, paciente), getPersistedPaciente(paciente));
    }

    @Test
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .curp(UPDATED_CURP)
            .nombre(UPDATED_NOMBRE)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .sexo(UPDATED_SEXO)
            .numeroSeguroSocial(UPDATED_NUMERO_SEGURO_SOCIAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPaciente))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Paciente in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPacienteUpdatableFieldsEquals(partialUpdatedPaciente, getPersistedPaciente(partialUpdatedPaciente));
    }

    @Test
    void patchNonExistingPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pacienteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaciente() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paciente.setId(longCount.incrementAndGet());

        // Create the Paciente
        PacienteDTO pacienteDTO = pacienteMapper.toDto(paciente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(pacienteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Paciente in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaciente() {
        // Initialize the database
        insertedPaciente = pacienteRepository.save(paciente).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paciente
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paciente.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pacienteRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Paciente getPersistedPaciente(Paciente paciente) {
        return pacienteRepository.findById(paciente.getId()).block();
    }

    protected void assertPersistedPacienteToMatchAllProperties(Paciente expectedPaciente) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAllPropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
        assertPacienteUpdatableFieldsEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }

    protected void assertPersistedPacienteToMatchUpdatableProperties(Paciente expectedPaciente) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPacienteAllUpdatablePropertiesEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
        assertPacienteUpdatableFieldsEquals(expectedPaciente, getPersistedPaciente(expectedPaciente));
    }
}
