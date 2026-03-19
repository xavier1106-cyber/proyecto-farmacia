package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MovimientoInventarioRepository;
import com.mycompany.myapp.service.MovimientoInventarioService;
import com.mycompany.myapp.service.dto.MovimientoInventarioDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MovimientoInventario}.
 */
@RestController
@RequestMapping("/api/movimiento-inventarios")
public class MovimientoInventarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(MovimientoInventarioResource.class);

    private static final String ENTITY_NAME = "inventarioMovimientoInventario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovimientoInventarioService movimientoInventarioService;

    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventarioResource(
        MovimientoInventarioService movimientoInventarioService,
        MovimientoInventarioRepository movimientoInventarioRepository
    ) {
        this.movimientoInventarioService = movimientoInventarioService;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    /**
     * {@code POST  /movimiento-inventarios} : Create a new movimientoInventario.
     *
     * @param movimientoInventarioDTO the movimientoInventarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movimientoInventarioDTO, or with status {@code 400 (Bad Request)} if the movimientoInventario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<MovimientoInventarioDTO>> createMovimientoInventario(
        @Valid @RequestBody MovimientoInventarioDTO movimientoInventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MovimientoInventario : {}", movimientoInventarioDTO);
        if (movimientoInventarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new movimientoInventario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return movimientoInventarioService
            .save(movimientoInventarioDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/movimiento-inventarios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /movimiento-inventarios/:id} : Updates an existing movimientoInventario.
     *
     * @param id the id of the movimientoInventarioDTO to save.
     * @param movimientoInventarioDTO the movimientoInventarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movimientoInventarioDTO,
     * or with status {@code 400 (Bad Request)} if the movimientoInventarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movimientoInventarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MovimientoInventarioDTO>> updateMovimientoInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MovimientoInventarioDTO movimientoInventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MovimientoInventario : {}, {}", id, movimientoInventarioDTO);
        if (movimientoInventarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movimientoInventarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return movimientoInventarioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return movimientoInventarioService
                    .update(movimientoInventarioDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /movimiento-inventarios/:id} : Partial updates given fields of an existing movimientoInventario, field will ignore if it is null
     *
     * @param id the id of the movimientoInventarioDTO to save.
     * @param movimientoInventarioDTO the movimientoInventarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movimientoInventarioDTO,
     * or with status {@code 400 (Bad Request)} if the movimientoInventarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the movimientoInventarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the movimientoInventarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MovimientoInventarioDTO>> partialUpdateMovimientoInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MovimientoInventarioDTO movimientoInventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MovimientoInventario partially : {}, {}", id, movimientoInventarioDTO);
        if (movimientoInventarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, movimientoInventarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return movimientoInventarioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MovimientoInventarioDTO> result = movimientoInventarioService.partialUpdate(movimientoInventarioDTO);

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
     * {@code GET  /movimiento-inventarios} : get all the movimientoInventarios.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movimientoInventarios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<MovimientoInventarioDTO>>> getAllMovimientoInventarios(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of MovimientoInventarios");
        return movimientoInventarioService
            .countAll()
            .zipWith(movimientoInventarioService.findAll(pageable).collectList())
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
     * {@code GET  /movimiento-inventarios/:id} : get the "id" movimientoInventario.
     *
     * @param id the id of the movimientoInventarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movimientoInventarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovimientoInventarioDTO>> getMovimientoInventario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MovimientoInventario : {}", id);
        Mono<MovimientoInventarioDTO> movimientoInventarioDTO = movimientoInventarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movimientoInventarioDTO);
    }

    /**
     * {@code DELETE  /movimiento-inventarios/:id} : delete the "id" movimientoInventario.
     *
     * @param id the id of the movimientoInventarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMovimientoInventario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MovimientoInventario : {}", id);
        return movimientoInventarioService
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
