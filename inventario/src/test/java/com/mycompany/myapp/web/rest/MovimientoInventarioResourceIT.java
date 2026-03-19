package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MovimientoInventarioAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MovimientoInventario;
import com.mycompany.myapp.domain.enumeration.TipoMovimiento;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.MovimientoInventarioRepository;
import com.mycompany.myapp.service.MovimientoInventarioService;
import com.mycompany.myapp.service.dto.MovimientoInventarioDTO;
import com.mycompany.myapp.service.mapper.MovimientoInventarioMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link MovimientoInventarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MovimientoInventarioResourceIT {

    private static final TipoMovimiento DEFAULT_TIPO_MOVIMIENTO = TipoMovimiento.ENTRADA;
    private static final TipoMovimiento UPDATED_TIPO_MOVIMIENTO = TipoMovimiento.SALIDA;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/movimiento-inventarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepositoryMock;

    @Autowired
    private MovimientoInventarioMapper movimientoInventarioMapper;

    @Mock
    private MovimientoInventarioService movimientoInventarioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MovimientoInventario movimientoInventario;

    private MovimientoInventario insertedMovimientoInventario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovimientoInventario createEntity() {
        return new MovimientoInventario()
            .tipoMovimiento(DEFAULT_TIPO_MOVIMIENTO)
            .cantidad(DEFAULT_CANTIDAD)
            .fecha(DEFAULT_FECHA)
            .observacion(DEFAULT_OBSERVACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MovimientoInventario createUpdatedEntity() {
        return new MovimientoInventario()
            .tipoMovimiento(UPDATED_TIPO_MOVIMIENTO)
            .cantidad(UPDATED_CANTIDAD)
            .fecha(UPDATED_FECHA)
            .observacion(UPDATED_OBSERVACION);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MovimientoInventario.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        movimientoInventario = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMovimientoInventario != null) {
            movimientoInventarioRepository.delete(insertedMovimientoInventario).block();
            insertedMovimientoInventario = null;
        }
        deleteEntities(em);
    }

    @Test
    void createMovimientoInventario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);
        var returnedMovimientoInventarioDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MovimientoInventarioDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the MovimientoInventario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMovimientoInventario = movimientoInventarioMapper.toEntity(returnedMovimientoInventarioDTO);
        assertMovimientoInventarioUpdatableFieldsEquals(
            returnedMovimientoInventario,
            getPersistedMovimientoInventario(returnedMovimientoInventario)
        );

        insertedMovimientoInventario = returnedMovimientoInventario;
    }

    @Test
    void createMovimientoInventarioWithExistingId() throws Exception {
        // Create the MovimientoInventario with an existing ID
        movimientoInventario.setId(1L);
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTipoMovimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movimientoInventario.setTipoMovimiento(null);

        // Create the MovimientoInventario, which fails.
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movimientoInventario.setCantidad(null);

        // Create the MovimientoInventario, which fails.
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        movimientoInventario.setFecha(null);

        // Create the MovimientoInventario, which fails.
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllMovimientoInventarios() {
        // Initialize the database
        insertedMovimientoInventario = movimientoInventarioRepository.save(movimientoInventario).block();

        // Get all the movimientoInventarioList
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
            .value(hasItem(movimientoInventario.getId().intValue()))
            .jsonPath("$.[*].tipoMovimiento")
            .value(hasItem(DEFAULT_TIPO_MOVIMIENTO.toString()))
            .jsonPath("$.[*].cantidad")
            .value(hasItem(DEFAULT_CANTIDAD))
            .jsonPath("$.[*].fecha")
            .value(hasItem(DEFAULT_FECHA.toString()))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMovimientoInventariosWithEagerRelationshipsIsEnabled() {
        when(movimientoInventarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(movimientoInventarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMovimientoInventariosWithEagerRelationshipsIsNotEnabled() {
        when(movimientoInventarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(movimientoInventarioRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMovimientoInventario() {
        // Initialize the database
        insertedMovimientoInventario = movimientoInventarioRepository.save(movimientoInventario).block();

        // Get the movimientoInventario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, movimientoInventario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(movimientoInventario.getId().intValue()))
            .jsonPath("$.tipoMovimiento")
            .value(is(DEFAULT_TIPO_MOVIMIENTO.toString()))
            .jsonPath("$.cantidad")
            .value(is(DEFAULT_CANTIDAD))
            .jsonPath("$.fecha")
            .value(is(DEFAULT_FECHA.toString()))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION));
    }

    @Test
    void getNonExistingMovimientoInventario() {
        // Get the movimientoInventario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMovimientoInventario() throws Exception {
        // Initialize the database
        insertedMovimientoInventario = movimientoInventarioRepository.save(movimientoInventario).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movimientoInventario
        MovimientoInventario updatedMovimientoInventario = movimientoInventarioRepository.findById(movimientoInventario.getId()).block();
        updatedMovimientoInventario
            .tipoMovimiento(UPDATED_TIPO_MOVIMIENTO)
            .cantidad(UPDATED_CANTIDAD)
            .fecha(UPDATED_FECHA)
            .observacion(UPDATED_OBSERVACION);
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(updatedMovimientoInventario);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, movimientoInventarioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMovimientoInventarioToMatchAllProperties(updatedMovimientoInventario);
    }

    @Test
    void putNonExistingMovimientoInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoInventario.setId(longCount.incrementAndGet());

        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, movimientoInventarioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMovimientoInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoInventario.setId(longCount.incrementAndGet());

        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMovimientoInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoInventario.setId(longCount.incrementAndGet());

        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMovimientoInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedMovimientoInventario = movimientoInventarioRepository.save(movimientoInventario).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movimientoInventario using partial update
        MovimientoInventario partialUpdatedMovimientoInventario = new MovimientoInventario();
        partialUpdatedMovimientoInventario.setId(movimientoInventario.getId());

        partialUpdatedMovimientoInventario
            .tipoMovimiento(UPDATED_TIPO_MOVIMIENTO)
            .cantidad(UPDATED_CANTIDAD)
            .observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMovimientoInventario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMovimientoInventario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MovimientoInventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMovimientoInventarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMovimientoInventario, movimientoInventario),
            getPersistedMovimientoInventario(movimientoInventario)
        );
    }

    @Test
    void fullUpdateMovimientoInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedMovimientoInventario = movimientoInventarioRepository.save(movimientoInventario).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the movimientoInventario using partial update
        MovimientoInventario partialUpdatedMovimientoInventario = new MovimientoInventario();
        partialUpdatedMovimientoInventario.setId(movimientoInventario.getId());

        partialUpdatedMovimientoInventario
            .tipoMovimiento(UPDATED_TIPO_MOVIMIENTO)
            .cantidad(UPDATED_CANTIDAD)
            .fecha(UPDATED_FECHA)
            .observacion(UPDATED_OBSERVACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMovimientoInventario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedMovimientoInventario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MovimientoInventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMovimientoInventarioUpdatableFieldsEquals(
            partialUpdatedMovimientoInventario,
            getPersistedMovimientoInventario(partialUpdatedMovimientoInventario)
        );
    }

    @Test
    void patchNonExistingMovimientoInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoInventario.setId(longCount.incrementAndGet());

        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, movimientoInventarioDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMovimientoInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoInventario.setId(longCount.incrementAndGet());

        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMovimientoInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        movimientoInventario.setId(longCount.incrementAndGet());

        // Create the MovimientoInventario
        MovimientoInventarioDTO movimientoInventarioDTO = movimientoInventarioMapper.toDto(movimientoInventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(movimientoInventarioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MovimientoInventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMovimientoInventario() {
        // Initialize the database
        insertedMovimientoInventario = movimientoInventarioRepository.save(movimientoInventario).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the movimientoInventario
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, movimientoInventario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return movimientoInventarioRepository.count().block();
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

    protected MovimientoInventario getPersistedMovimientoInventario(MovimientoInventario movimientoInventario) {
        return movimientoInventarioRepository.findById(movimientoInventario.getId()).block();
    }

    protected void assertPersistedMovimientoInventarioToMatchAllProperties(MovimientoInventario expectedMovimientoInventario) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMovimientoInventarioAllPropertiesEquals(expectedMovimientoInventario, getPersistedMovimientoInventario(expectedMovimientoInventario));
        assertMovimientoInventarioUpdatableFieldsEquals(
            expectedMovimientoInventario,
            getPersistedMovimientoInventario(expectedMovimientoInventario)
        );
    }

    protected void assertPersistedMovimientoInventarioToMatchUpdatableProperties(MovimientoInventario expectedMovimientoInventario) {
        // Test fails because reactive api returns an empty object instead of null
        // assertMovimientoInventarioAllUpdatablePropertiesEquals(expectedMovimientoInventario, getPersistedMovimientoInventario(expectedMovimientoInventario));
        assertMovimientoInventarioUpdatableFieldsEquals(
            expectedMovimientoInventario,
            getPersistedMovimientoInventario(expectedMovimientoInventario)
        );
    }
}
