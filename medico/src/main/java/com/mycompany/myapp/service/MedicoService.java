package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MedicoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Medico}.
 */
public interface MedicoService {
    /**
     * Save a medico.
     *
     * @param medicoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MedicoDTO> save(MedicoDTO medicoDTO);

    /**
     * Updates a medico.
     *
     * @param medicoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MedicoDTO> update(MedicoDTO medicoDTO);

    /**
     * Partially updates a medico.
     *
     * @param medicoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MedicoDTO> partialUpdate(MedicoDTO medicoDTO);

    /**
     * Get all the medicos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MedicoDTO> findAll(Pageable pageable);

    /**
     * Returns the number of medicos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" medico.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MedicoDTO> findOne(Long id);

    /**
     * Delete the "id" medico.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
