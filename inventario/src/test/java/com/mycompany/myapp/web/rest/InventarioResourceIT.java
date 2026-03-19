package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.InventarioAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Inventario;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.InventarioRepository;
import com.mycompany.myapp.service.dto.InventarioDTO;
import com.mycompany.myapp.service.mapper.InventarioMapper;
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
 * Integration tests for the {@link InventarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InventarioResourceIT {

    private static final String DEFAULT_CLAVE_MEDICAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_CLAVE_MEDICAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENTACION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTACION = "BBBBBBBBBB";

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 0;
    private static final Integer UPDATED_CANTIDAD = 1;

    private static final Integer DEFAULT_CANTIDAD_MINIMA = 0;
    private static final Integer UPDATED_CANTIDAD_MINIMA = 1;

    private static final LocalDate DEFAULT_FECHA_CADUCIDAD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CADUCIDAD = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONTROLADO = false;
    private static final Boolean UPDATED_CONTROLADO = true;

    private static final String ENTITY_API_URL = "/api/inventarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private InventarioMapper inventarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Inventario inventario;

    private Inventario insertedInventario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventario createEntity() {
        return new Inventario()
            .claveMedicamento(DEFAULT_CLAVE_MEDICAMENTO)
            .nombre(DEFAULT_NOMBRE)
            .presentacion(DEFAULT_PRESENTACION)
            .lote(DEFAULT_LOTE)
            .cantidad(DEFAULT_CANTIDAD)
            .cantidadMinima(DEFAULT_CANTIDAD_MINIMA)
            .fechaCaducidad(DEFAULT_FECHA_CADUCIDAD)
            .ubicacion(DEFAULT_UBICACION)
            .controlado(DEFAULT_CONTROLADO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventario createUpdatedEntity() {
        return new Inventario()
            .claveMedicamento(UPDATED_CLAVE_MEDICAMENTO)
            .nombre(UPDATED_NOMBRE)
            .presentacion(UPDATED_PRESENTACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .cantidadMinima(UPDATED_CANTIDAD_MINIMA)
            .fechaCaducidad(UPDATED_FECHA_CADUCIDAD)
            .ubicacion(UPDATED_UBICACION)
            .controlado(UPDATED_CONTROLADO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Inventario.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        inventario = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInventario != null) {
            inventarioRepository.delete(insertedInventario).block();
            insertedInventario = null;
        }
        deleteEntities(em);
    }

    @Test
    void createInventario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);
        var returnedInventarioDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(InventarioDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Inventario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventario = inventarioMapper.toEntity(returnedInventarioDTO);
        assertInventarioUpdatableFieldsEquals(returnedInventario, getPersistedInventario(returnedInventario));

        insertedInventario = returnedInventario;
    }

    @Test
    void createInventarioWithExistingId() throws Exception {
        // Create the Inventario with an existing ID
        inventario.setId(1L);
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkClaveMedicamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setClaveMedicamento(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setNombre(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkPresentacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setPresentacion(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLoteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setLote(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setCantidad(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkCantidadMinimaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setCantidadMinima(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaCaducidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setFechaCaducidad(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUbicacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setUbicacion(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkControladoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setControlado(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllInventarios() {
        // Initialize the database
        insertedInventario = inventarioRepository.save(inventario).block();

        // Get all the inventarioList
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
            .value(hasItem(inventario.getId().intValue()))
            .jsonPath("$.[*].claveMedicamento")
            .value(hasItem(DEFAULT_CLAVE_MEDICAMENTO))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].presentacion")
            .value(hasItem(DEFAULT_PRESENTACION))
            .jsonPath("$.[*].lote")
            .value(hasItem(DEFAULT_LOTE))
            .jsonPath("$.[*].cantidad")
            .value(hasItem(DEFAULT_CANTIDAD))
            .jsonPath("$.[*].cantidadMinima")
            .value(hasItem(DEFAULT_CANTIDAD_MINIMA))
            .jsonPath("$.[*].fechaCaducidad")
            .value(hasItem(DEFAULT_FECHA_CADUCIDAD.toString()))
            .jsonPath("$.[*].ubicacion")
            .value(hasItem(DEFAULT_UBICACION))
            .jsonPath("$.[*].controlado")
            .value(hasItem(DEFAULT_CONTROLADO.booleanValue()));
    }

    @Test
    void getInventario() {
        // Initialize the database
        insertedInventario = inventarioRepository.save(inventario).block();

        // Get the inventario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inventario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inventario.getId().intValue()))
            .jsonPath("$.claveMedicamento")
            .value(is(DEFAULT_CLAVE_MEDICAMENTO))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.presentacion")
            .value(is(DEFAULT_PRESENTACION))
            .jsonPath("$.lote")
            .value(is(DEFAULT_LOTE))
            .jsonPath("$.cantidad")
            .value(is(DEFAULT_CANTIDAD))
            .jsonPath("$.cantidadMinima")
            .value(is(DEFAULT_CANTIDAD_MINIMA))
            .jsonPath("$.fechaCaducidad")
            .value(is(DEFAULT_FECHA_CADUCIDAD.toString()))
            .jsonPath("$.ubicacion")
            .value(is(DEFAULT_UBICACION))
            .jsonPath("$.controlado")
            .value(is(DEFAULT_CONTROLADO.booleanValue()));
    }

    @Test
    void getNonExistingInventario() {
        // Get the inventario
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.save(inventario).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario
        Inventario updatedInventario = inventarioRepository.findById(inventario.getId()).block();
        updatedInventario
            .claveMedicamento(UPDATED_CLAVE_MEDICAMENTO)
            .nombre(UPDATED_NOMBRE)
            .presentacion(UPDATED_PRESENTACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .cantidadMinima(UPDATED_CANTIDAD_MINIMA)
            .fechaCaducidad(UPDATED_FECHA_CADUCIDAD)
            .ubicacion(UPDATED_UBICACION)
            .controlado(UPDATED_CONTROLADO);
        InventarioDTO inventarioDTO = inventarioMapper.toDto(updatedInventario);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inventarioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventarioToMatchAllProperties(updatedInventario);
    }

    @Test
    void putNonExistingInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inventarioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.save(inventario).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario using partial update
        Inventario partialUpdatedInventario = new Inventario();
        partialUpdatedInventario.setId(inventario.getId());

        partialUpdatedInventario
            .claveMedicamento(UPDATED_CLAVE_MEDICAMENTO)
            .presentacion(UPDATED_PRESENTACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .cantidadMinima(UPDATED_CANTIDAD_MINIMA)
            .fechaCaducidad(UPDATED_FECHA_CADUCIDAD)
            .ubicacion(UPDATED_UBICACION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInventario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedInventario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventario, inventario),
            getPersistedInventario(inventario)
        );
    }

    @Test
    void fullUpdateInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.save(inventario).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario using partial update
        Inventario partialUpdatedInventario = new Inventario();
        partialUpdatedInventario.setId(inventario.getId());

        partialUpdatedInventario
            .claveMedicamento(UPDATED_CLAVE_MEDICAMENTO)
            .nombre(UPDATED_NOMBRE)
            .presentacion(UPDATED_PRESENTACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .cantidadMinima(UPDATED_CANTIDAD_MINIMA)
            .fechaCaducidad(UPDATED_FECHA_CADUCIDAD)
            .ubicacion(UPDATED_UBICACION)
            .controlado(UPDATED_CONTROLADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInventario.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedInventario))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventarioUpdatableFieldsEquals(partialUpdatedInventario, getPersistedInventario(partialUpdatedInventario));
    }

    @Test
    void patchNonExistingInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inventarioDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(inventarioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInventario() {
        // Initialize the database
        insertedInventario = inventarioRepository.save(inventario).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventario
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inventario.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventarioRepository.count().block();
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

    protected Inventario getPersistedInventario(Inventario inventario) {
        return inventarioRepository.findById(inventario.getId()).block();
    }

    protected void assertPersistedInventarioToMatchAllProperties(Inventario expectedInventario) {
        // Test fails because reactive api returns an empty object instead of null
        // assertInventarioAllPropertiesEquals(expectedInventario, getPersistedInventario(expectedInventario));
        assertInventarioUpdatableFieldsEquals(expectedInventario, getPersistedInventario(expectedInventario));
    }

    protected void assertPersistedInventarioToMatchUpdatableProperties(Inventario expectedInventario) {
        // Test fails because reactive api returns an empty object instead of null
        // assertInventarioAllUpdatablePropertiesEquals(expectedInventario, getPersistedInventario(expectedInventario));
        assertInventarioUpdatableFieldsEquals(expectedInventario, getPersistedInventario(expectedInventario));
    }
}
