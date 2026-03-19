package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.HistoricoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Historico;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.HistoricoRepository;
import com.mycompany.myapp.service.dto.HistoricoDTO;
import com.mycompany.myapp.service.mapper.HistoricoMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link HistoricoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HistoricoResourceIT {

    private static final Instant DEFAULT_FECHA_EMISION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EMISION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FOLIO = "AAAAAAAAAA";
    private static final String UPDATED_FOLIO = "BBBBBBBBBB";

    private static final Long DEFAULT_PACIENTE_ID = 1L;
    private static final Long UPDATED_PACIENTE_ID = 2L;

    private static final String DEFAULT_PACIENTE_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_PACIENTE_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PACIENTE_CURP = "AAAAAAAAAA";
    private static final String UPDATED_PACIENTE_CURP = "BBBBBBBBBB";

    private static final Long DEFAULT_MEDICO_ID = 1L;
    private static final Long UPDATED_MEDICO_ID = 2L;

    private static final String DEFAULT_MEDICO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_MEDICO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICO_ESPECIALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_MEDICO_ESPECIALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_USUARIO_QUE_REGISTRO = "AAAAAAAAAA";
    private static final String UPDATED_USUARIO_QUE_REGISTRO = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICAMENTOS = "AAAAAAAAAA";
    private static final String UPDATED_MEDICAMENTOS = "BBBBBBBBBB";

    private static final String DEFAULT_AUTORIZO = "AAAAAAAAAA";
    private static final String UPDATED_AUTORIZO = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final String ENTITY_API_URL = "/api/historicos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private HistoricoMapper historicoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Historico historico;

    private Historico insertedHistorico;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historico createEntity() {
        return new Historico()
            .fechaEmision(DEFAULT_FECHA_EMISION)
            .folio(DEFAULT_FOLIO)
            .pacienteId(DEFAULT_PACIENTE_ID)
            .pacienteNombre(DEFAULT_PACIENTE_NOMBRE)
            .pacienteCurp(DEFAULT_PACIENTE_CURP)
            .medicoId(DEFAULT_MEDICO_ID)
            .medicoNombre(DEFAULT_MEDICO_NOMBRE)
            .medicoEspecialidad(DEFAULT_MEDICO_ESPECIALIDAD)
            .usuarioQueRegistro(DEFAULT_USUARIO_QUE_REGISTRO)
            .medicamentos(DEFAULT_MEDICAMENTOS)
            .autorizo(DEFAULT_AUTORIZO)
            .observaciones(DEFAULT_OBSERVACIONES)
            .cantidad(DEFAULT_CANTIDAD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historico createUpdatedEntity() {
        return new Historico()
            .fechaEmision(UPDATED_FECHA_EMISION)
            .folio(UPDATED_FOLIO)
            .pacienteId(UPDATED_PACIENTE_ID)
            .pacienteNombre(UPDATED_PACIENTE_NOMBRE)
            .pacienteCurp(UPDATED_PACIENTE_CURP)
            .medicoId(UPDATED_MEDICO_ID)
            .medicoNombre(UPDATED_MEDICO_NOMBRE)
            .medicoEspecialidad(UPDATED_MEDICO_ESPECIALIDAD)
            .usuarioQueRegistro(UPDATED_USUARIO_QUE_REGISTRO)
            .medicamentos(UPDATED_MEDICAMENTOS)
            .autorizo(UPDATED_AUTORIZO)
            .observaciones(UPDATED_OBSERVACIONES)
            .cantidad(UPDATED_CANTIDAD);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Historico.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        historico = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHistorico != null) {
            historicoRepository.delete(insertedHistorico).block();
            insertedHistorico = null;
        }
        deleteEntities(em);
    }

    @Test
    void createHistorico() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);
        var returnedHistoricoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(HistoricoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Historico in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistorico = historicoMapper.toEntity(returnedHistoricoDTO);
        assertHistoricoUpdatableFieldsEquals(returnedHistorico, getPersistedHistorico(returnedHistorico));

        insertedHistorico = returnedHistorico;
    }

    @Test
    void createHistoricoWithExistingId() throws Exception {
        // Create the Historico with an existing ID
        historico.setId(1L);
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkFechaEmisionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historico.setFechaEmision(null);

        // Create the Historico, which fails.
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFolioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historico.setFolio(null);

        // Create the Historico, which fails.
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPacienteIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historico.setPacienteId(null);

        // Create the Historico, which fails.
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPacienteNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historico.setPacienteNombre(null);

        // Create the Historico, which fails.
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkMedicoIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historico.setMedicoId(null);

        // Create the Historico, which fails.
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUsuarioQueRegistroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historico.setUsuarioQueRegistro(null);

        // Create the Historico, which fails.
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllHistoricos() {
        // Initialize the database
        insertedHistorico = historicoRepository.save(historico).block();

        // Get all the historicoList
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
            .value(hasItem(historico.getId().intValue()))
            .jsonPath("$.[*].fechaEmision")
            .value(hasItem(DEFAULT_FECHA_EMISION.toString()))
            .jsonPath("$.[*].folio")
            .value(hasItem(DEFAULT_FOLIO))
            .jsonPath("$.[*].pacienteId")
            .value(hasItem(DEFAULT_PACIENTE_ID.intValue()))
            .jsonPath("$.[*].pacienteNombre")
            .value(hasItem(DEFAULT_PACIENTE_NOMBRE))
            .jsonPath("$.[*].pacienteCurp")
            .value(hasItem(DEFAULT_PACIENTE_CURP))
            .jsonPath("$.[*].medicoId")
            .value(hasItem(DEFAULT_MEDICO_ID.intValue()))
            .jsonPath("$.[*].medicoNombre")
            .value(hasItem(DEFAULT_MEDICO_NOMBRE))
            .jsonPath("$.[*].medicoEspecialidad")
            .value(hasItem(DEFAULT_MEDICO_ESPECIALIDAD))
            .jsonPath("$.[*].usuarioQueRegistro")
            .value(hasItem(DEFAULT_USUARIO_QUE_REGISTRO))
            .jsonPath("$.[*].medicamentos")
            .value(hasItem(DEFAULT_MEDICAMENTOS.toString()))
            .jsonPath("$.[*].autorizo")
            .value(hasItem(DEFAULT_AUTORIZO))
            .jsonPath("$.[*].observaciones")
            .value(hasItem(DEFAULT_OBSERVACIONES))
            .jsonPath("$.[*].cantidad")
            .value(hasItem(DEFAULT_CANTIDAD));
    }

    @Test
    void getHistorico() {
        // Initialize the database
        insertedHistorico = historicoRepository.save(historico).block();

        // Get the historico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, historico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(historico.getId().intValue()))
            .jsonPath("$.fechaEmision")
            .value(is(DEFAULT_FECHA_EMISION.toString()))
            .jsonPath("$.folio")
            .value(is(DEFAULT_FOLIO))
            .jsonPath("$.pacienteId")
            .value(is(DEFAULT_PACIENTE_ID.intValue()))
            .jsonPath("$.pacienteNombre")
            .value(is(DEFAULT_PACIENTE_NOMBRE))
            .jsonPath("$.pacienteCurp")
            .value(is(DEFAULT_PACIENTE_CURP))
            .jsonPath("$.medicoId")
            .value(is(DEFAULT_MEDICO_ID.intValue()))
            .jsonPath("$.medicoNombre")
            .value(is(DEFAULT_MEDICO_NOMBRE))
            .jsonPath("$.medicoEspecialidad")
            .value(is(DEFAULT_MEDICO_ESPECIALIDAD))
            .jsonPath("$.usuarioQueRegistro")
            .value(is(DEFAULT_USUARIO_QUE_REGISTRO))
            .jsonPath("$.medicamentos")
            .value(is(DEFAULT_MEDICAMENTOS.toString()))
            .jsonPath("$.autorizo")
            .value(is(DEFAULT_AUTORIZO))
            .jsonPath("$.observaciones")
            .value(is(DEFAULT_OBSERVACIONES))
            .jsonPath("$.cantidad")
            .value(is(DEFAULT_CANTIDAD));
    }

    @Test
    void getNonExistingHistorico() {
        // Get the historico
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHistorico() throws Exception {
        // Initialize the database
        insertedHistorico = historicoRepository.save(historico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historico
        Historico updatedHistorico = historicoRepository.findById(historico.getId()).block();
        updatedHistorico
            .fechaEmision(UPDATED_FECHA_EMISION)
            .folio(UPDATED_FOLIO)
            .pacienteId(UPDATED_PACIENTE_ID)
            .pacienteNombre(UPDATED_PACIENTE_NOMBRE)
            .pacienteCurp(UPDATED_PACIENTE_CURP)
            .medicoId(UPDATED_MEDICO_ID)
            .medicoNombre(UPDATED_MEDICO_NOMBRE)
            .medicoEspecialidad(UPDATED_MEDICO_ESPECIALIDAD)
            .usuarioQueRegistro(UPDATED_USUARIO_QUE_REGISTRO)
            .medicamentos(UPDATED_MEDICAMENTOS)
            .autorizo(UPDATED_AUTORIZO)
            .observaciones(UPDATED_OBSERVACIONES)
            .cantidad(UPDATED_CANTIDAD);
        HistoricoDTO historicoDTO = historicoMapper.toDto(updatedHistorico);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoricoToMatchAllProperties(updatedHistorico);
    }

    @Test
    void putNonExistingHistorico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historico.setId(longCount.incrementAndGet());

        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historicoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchHistorico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historico.setId(longCount.incrementAndGet());

        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamHistorico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historico.setId(longCount.incrementAndGet());

        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateHistoricoWithPatch() throws Exception {
        // Initialize the database
        insertedHistorico = historicoRepository.save(historico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historico using partial update
        Historico partialUpdatedHistorico = new Historico();
        partialUpdatedHistorico.setId(historico.getId());

        partialUpdatedHistorico
            .folio(UPDATED_FOLIO)
            .pacienteId(UPDATED_PACIENTE_ID)
            .pacienteNombre(UPDATED_PACIENTE_NOMBRE)
            .medicoId(UPDATED_MEDICO_ID)
            .medicoNombre(UPDATED_MEDICO_NOMBRE)
            .medicoEspecialidad(UPDATED_MEDICO_ESPECIALIDAD)
            .usuarioQueRegistro(UPDATED_USUARIO_QUE_REGISTRO)
            .medicamentos(UPDATED_MEDICAMENTOS)
            .cantidad(UPDATED_CANTIDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistorico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedHistorico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Historico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoricoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistorico, historico),
            getPersistedHistorico(historico)
        );
    }

    @Test
    void fullUpdateHistoricoWithPatch() throws Exception {
        // Initialize the database
        insertedHistorico = historicoRepository.save(historico).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historico using partial update
        Historico partialUpdatedHistorico = new Historico();
        partialUpdatedHistorico.setId(historico.getId());

        partialUpdatedHistorico
            .fechaEmision(UPDATED_FECHA_EMISION)
            .folio(UPDATED_FOLIO)
            .pacienteId(UPDATED_PACIENTE_ID)
            .pacienteNombre(UPDATED_PACIENTE_NOMBRE)
            .pacienteCurp(UPDATED_PACIENTE_CURP)
            .medicoId(UPDATED_MEDICO_ID)
            .medicoNombre(UPDATED_MEDICO_NOMBRE)
            .medicoEspecialidad(UPDATED_MEDICO_ESPECIALIDAD)
            .usuarioQueRegistro(UPDATED_USUARIO_QUE_REGISTRO)
            .medicamentos(UPDATED_MEDICAMENTOS)
            .autorizo(UPDATED_AUTORIZO)
            .observaciones(UPDATED_OBSERVACIONES)
            .cantidad(UPDATED_CANTIDAD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistorico.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedHistorico))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Historico in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoricoUpdatableFieldsEquals(partialUpdatedHistorico, getPersistedHistorico(partialUpdatedHistorico));
    }

    @Test
    void patchNonExistingHistorico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historico.setId(longCount.incrementAndGet());

        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, historicoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchHistorico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historico.setId(longCount.incrementAndGet());

        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamHistorico() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historico.setId(longCount.incrementAndGet());

        // Create the Historico
        HistoricoDTO historicoDTO = historicoMapper.toDto(historico);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(historicoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Historico in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteHistorico() {
        // Initialize the database
        insertedHistorico = historicoRepository.save(historico).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historico
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, historico.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historicoRepository.count().block();
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

    protected Historico getPersistedHistorico(Historico historico) {
        return historicoRepository.findById(historico.getId()).block();
    }

    protected void assertPersistedHistoricoToMatchAllProperties(Historico expectedHistorico) {
        // Test fails because reactive api returns an empty object instead of null
        // assertHistoricoAllPropertiesEquals(expectedHistorico, getPersistedHistorico(expectedHistorico));
        assertHistoricoUpdatableFieldsEquals(expectedHistorico, getPersistedHistorico(expectedHistorico));
    }

    protected void assertPersistedHistoricoToMatchUpdatableProperties(Historico expectedHistorico) {
        // Test fails because reactive api returns an empty object instead of null
        // assertHistoricoAllUpdatablePropertiesEquals(expectedHistorico, getPersistedHistorico(expectedHistorico));
        assertHistoricoUpdatableFieldsEquals(expectedHistorico, getPersistedHistorico(expectedHistorico));
    }
}
