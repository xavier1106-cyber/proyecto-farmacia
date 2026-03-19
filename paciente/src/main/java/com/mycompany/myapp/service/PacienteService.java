package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.PacienteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Paciente}.
 */
public interface PacienteService {
    /**
     * Save a paciente.
     *
     * @param pacienteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PacienteDTO> save(PacienteDTO pacienteDTO);

    /**
     * Updates a paciente.
     *
     * @param pacienteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PacienteDTO> update(PacienteDTO pacienteDTO);

    /**
     * Partially updates a paciente.
     *
     * @param pacienteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO);

    /**
     * Get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PacienteDTO> findAll(Pageable pageable);

    /**
     * Returns the number of pacientes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" paciente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PacienteDTO> findOne(Long id);

    /**
     * Delete the "id" paciente.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
