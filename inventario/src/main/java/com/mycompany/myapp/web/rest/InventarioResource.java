package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.InventarioRepository;
import com.mycompany.myapp.service.InventarioService;
import com.mycompany.myapp.service.dto.InventarioDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Inventario}.
 */
@RestController
@RequestMapping("/api/inventarios")
public class InventarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioResource.class);

    private static final String ENTITY_NAME = "inventarioInventario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventarioService inventarioService;

    private final InventarioRepository inventarioRepository;

    public InventarioResource(InventarioService inventarioService, InventarioRepository inventarioRepository) {
        this.inventarioService = inventarioService;
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * {@code POST  /inventarios} : Create a new inventario.
     *
     * @param inventarioDTO the inventarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventarioDTO, or with status {@code 400 (Bad Request)} if the inventario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InventarioDTO>> createInventario(@Valid @RequestBody InventarioDTO inventarioDTO) throws URISyntaxException {
        LOG.debug("REST request to save Inventario : {}", inventarioDTO);
        if (inventarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new inventario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return inventarioService
            .save(inventarioDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/inventarios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /inventarios/:id} : Updates an existing inventario.
     *
     * @param id the id of the inventarioDTO to save.
     * @param inventarioDTO the inventarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventarioDTO,
     * or with status {@code 400 (Bad Request)} if the inventarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<InventarioDTO>> updateInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InventarioDTO inventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Inventario : {}, {}", id, inventarioDTO);
        if (inventarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inventarioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return inventarioService
                    .update(inventarioDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /inventarios/:id} : Partial updates given fields of an existing inventario, field will ignore if it is null
     *
     * @param id the id of the inventarioDTO to save.
     * @param inventarioDTO the inventarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventarioDTO,
     * or with status {@code 400 (Bad Request)} if the inventarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InventarioDTO>> partialUpdateInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InventarioDTO inventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Inventario partially : {}, {}", id, inventarioDTO);
        if (inventarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inventarioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InventarioDTO> result = inventarioService.partialUpdate(inventarioDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /inventarios} : get all the inventarios.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventarios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<InventarioDTO>>> getAllInventarios(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Inventarios");
        return inventarioService
            .countAll()
            .zipWith(inventarioService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /inventarios/:id} : get the "id" inventario.
     *
     * @param id the id of the inventarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InventarioDTO>> getInventario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Inventario : {}", id);
        Mono<InventarioDTO> inventarioDTO = inventarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventarioDTO);
    }

    /**
     * {@code DELETE  /inventarios/:id} : delete the "id" inventario.
     *
     * @param id the id of the inventarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInventario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Inventario : {}", id);
        return inventarioService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
