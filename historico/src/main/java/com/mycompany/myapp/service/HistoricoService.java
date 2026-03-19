package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.HistoricoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Historico}.
 */
public interface HistoricoService {
    /**
     * Save a historico.
     *
     * @param historicoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HistoricoDTO> save(HistoricoDTO historicoDTO);

    /**
     * Updates a historico.
     *
     * @param historicoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HistoricoDTO> update(HistoricoDTO historicoDTO);

    /**
     * Partially updates a historico.
     *
     * @param historicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HistoricoDTO> partialUpdate(HistoricoDTO historicoDTO);

    /**
     * Get all the historicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HistoricoDTO> findAll(Pageable pageable);

    /**
     * Returns the number of historicos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" historico.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HistoricoDTO> findOne(Long id);

    /**
     * Delete the "id" historico.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
