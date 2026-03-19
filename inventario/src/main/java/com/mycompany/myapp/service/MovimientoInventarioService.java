package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MovimientoInventarioDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.MovimientoInventario}.
 */
public interface MovimientoInventarioService {
    /**
     * Save a movimientoInventario.
     *
     * @param movimientoInventarioDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MovimientoInventarioDTO> save(MovimientoInventarioDTO movimientoInventarioDTO);

    /**
     * Updates a movimientoInventario.
     *
     * @param movimientoInventarioDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MovimientoInventarioDTO> update(MovimientoInventarioDTO movimientoInventarioDTO);

    /**
     * Partially updates a movimientoInventario.
     *
     * @param movimientoInventarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MovimientoInventarioDTO> partialUpdate(MovimientoInventarioDTO movimientoInventarioDTO);

    /**
     * Get all the movimientoInventarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MovimientoInventarioDTO> findAll(Pageable pageable);

    /**
     * Get all the movimientoInventarios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MovimientoInventarioDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of movimientoInventarios available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" movimientoInventario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MovimientoInventarioDTO> findOne(Long id);

    /**
     * Delete the "id" movimientoInventario.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
