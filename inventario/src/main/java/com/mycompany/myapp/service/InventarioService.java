package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.InventarioDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Inventario}.
 */
public interface InventarioService {
    /**
     * Save a inventario.
     *
     * @param inventarioDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InventarioDTO> save(InventarioDTO inventarioDTO);

    /**
     * Updates a inventario.
     *
     * @param inventarioDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InventarioDTO> update(InventarioDTO inventarioDTO);

    /**
     * Partially updates a inventario.
     *
     * @param inventarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InventarioDTO> partialUpdate(InventarioDTO inventarioDTO);

    /**
     * Get all the inventarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<InventarioDTO> findAll(Pageable pageable);

    /**
     * Returns the number of inventarios available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" inventario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InventarioDTO> findOne(Long id);

    /**
     * Delete the "id" inventario.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
