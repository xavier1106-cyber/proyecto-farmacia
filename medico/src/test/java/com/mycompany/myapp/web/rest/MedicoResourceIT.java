package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MedicoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Medico;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.MedicoRepository;
import com.mycompany.myapp.service.dto.MedicoDTO;
import com.mycompany.myapp.service.mapper.MedicoMapper;
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
 * Integration tests for the {@link MedicoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MedicoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECIALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_TURNO = "AAAAAAAAAA";
    private static final String UPDATED_TURNO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/medicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private MedicoMapper medicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Medico medico;

    private Medico insertedMedico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createEntity() {
        return new Medico()
            .nombre(DEFAULT_NOMBRE)
            .especialidad(DEFAULT_ESPECIALIDAD)
            .telefono(DEFAULT_TELEFONO)
            .turno(DEFAULT_TURNO)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medico createUpdatedEntity() {
        return new Medico()
            .nombre(UPDATED_NOMBRE)
            .especialidad(UPDATED_ESPECIALIDAD)
            .telefono(UPDATED_TELEFONO)
            .turno(UPDATED_TURNO)
            .activo(UPDATED_ACTIVO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Medico.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        medico = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMedico != null) {
            medicoRepository.delete(insertedMedico).block();
            insertedMedico = null;
        }
        deleteEntities(em);
    }

    @Test
    void createMedico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);
        var returnedMedicoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MedicoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Medico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedico = medicoMapper.toEntity(returnedMedicoDTO);
        assertMedicoUpdatableFieldsEquals(returnedMedico, getPersistedMedico(returnedMedico));

        insertedMedico = returnedMedico;
    }

    @Test
    void createMedicoWithExistingId() throws Exception {
        // Create the Medico with an existing ID
        medico.setId(1L);
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setNombre(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEspecialidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setEspecialidad(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medico.setActivo(null);

        // Create the Medico, which fails.
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMedicos() {
        // Initialize the database
        insertedMedico = medicoRepository.save(medico).block();

        // Get all the medicoList
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
            .value(hasItem(medico.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].especialidad")
            .value(hasItem(DEFAULT_ESPECIALIDAD))
            .jsonPath("$.[*].telefono")
            .value(hasItem(DEFAULT_TELEFONO))
            .jsonPath("$.[*].turno")
            .value(hasItem(DEFAULT_TURNO))
            .jsonPath("$.[*].activo")
            .value(hasItem(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    void getMedico() {
        // Initialize the database
        insertedMedico = medicoRepository.save(medico).block();

        // Get the medico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, medico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(medico.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.especialidad")
            .value(is(DEFAULT_ESPECIALIDAD))
            .jsonPath("$.telefono")
            .value(is(DEFAULT_TELEFONO))
            .jsonPath("$.turno")
            .value(is(DEFAULT_TURNO))
            .jsonPath("$.activo")
            .value(is(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    void getNonExistingMedico() {
        // Get the medico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMedico() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.save(medico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico
        Medico updatedMedico = medicoRepository.findById(medico.getId()).block();
        updatedMedico
            .nombre(UPDATED_NOMBRE)
            .especialidad(UPDATED_ESPECIALIDAD)
            .telefono(UPDATED_TELEFONO)
            .turno(UPDATED_TURNO)
            .activo(UPDATED_ACTIVO);
        MedicoDTO medicoDTO = medicoMapper.toDto(updatedMedico);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicoToMatchAllProperties(updatedMedico);
    }

    @Test
    void putNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, medicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.save(medico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMedico, medico), getPersistedMedico(medico));
    }

    @Test
    void fullUpdateMedicoWithPatch() throws Exception {
        // Initialize the database
        insertedMedico = medicoRepository.save(medico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medico using partial update
        Medico partialUpdatedMedico = new Medico();
        partialUpdatedMedico.setId(medico.getId());

        partialUpdatedMedico
            .nombre(UPDATED_NOMBRE)
            .especialidad(UPDATED_ESPECIALIDAD)
            .telefono(UPDATED_TELEFONO)
            .turno(UPDATED_TURNO)
            .activo(UPDATED_ACTIVO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMedico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMedico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Medico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicoUpdatableFieldsEquals(partialUpdatedMedico, getPersistedMedico(partialUpdatedMedico));
    }

    @Test
    void patchNonExistingMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, medicoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMedico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medico.setId(longCount.incrementAndGet());

        // Create the Medico
        MedicoDTO medicoDTO = medicoMapper.toDto(medico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(medicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Medico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMedico() {
        // Initialize the database
        insertedMedico = medicoRepository.save(medico).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medico
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, medico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicoRepository.count().block();
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

    protected Medico getPersistedMedico(Medico medico) {
        return medicoRepository.findById(medico.getId()).block();
    }

    protected void assertPersistedMedicoToMatchAllProperties(Medico expectedMedico) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicoAllPropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
        assertMedicoUpdatableFieldsEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }

    protected void assertPersistedMedicoToMatchUpdatableProperties(Medico expectedMedico) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMedicoAllUpdatablePropertiesEquals(expectedMedico, getPersistedMedico(expectedMedico));
        assertMedicoUpdatableFieldsEquals(expectedMedico, getPersistedMedico(expectedMedico));
    }
}
