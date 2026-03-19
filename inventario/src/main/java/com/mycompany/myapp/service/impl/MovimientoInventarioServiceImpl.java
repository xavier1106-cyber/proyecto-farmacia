package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.repository.MovimientoInventarioRepository;
import com.mycompany.myapp.service.MovimientoInventarioService;
import com.mycompany.myapp.service.dto.MovimientoInventarioDTO;
import com.mycompany.myapp.service.mapper.MovimientoInventarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.MovimientoInventario}.
 */
@Service
@Transactional
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private static final Logger LOG = LoggerFactory.getLogger(MovimientoInventarioServiceImpl.class);

    private final MovimientoInventarioRepository movimientoInventarioRepository;

    private final MovimientoInventarioMapper movimientoInventarioMapper;

    public MovimientoInventarioServiceImpl(
        MovimientoInventarioRepository movimientoInventarioRepository,
        MovimientoInventarioMapper movimientoInventarioMapper
    ) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.movimientoInventarioMapper = movimientoInventarioMapper;
    }

    @Override
    public Mono<MovimientoInventarioDTO> save(MovimientoInventarioDTO movimientoInventarioDTO) {
        LOG.debug("Request to save MovimientoInventario : {}", movimientoInventarioDTO);
        return movimientoInventarioRepository
            .save(movimientoInventarioMapper.toEntity(movimientoInventarioDTO))
            .map(movimientoInventarioMapper::toDto);
    }

    @Override
    public Mono<MovimientoInventarioDTO> update(MovimientoInventarioDTO movimientoInventarioDTO) {
        LOG.debug("Request to update MovimientoInventario : {}", movimientoInventarioDTO);
        return movimientoInventarioRepository
            .save(movimientoInventarioMapper.toEntity(movimientoInventarioDTO))
            .map(movimientoInventarioMapper::toDto);
    }

    @Override
    public Mono<MovimientoInventarioDTO> partialUpdate(MovimientoInventarioDTO movimientoInventarioDTO) {
        LOG.debug("Request to partially update MovimientoInventario : {}", movimientoInventarioDTO);

        return movimientoInventarioRepository
            .findById(movimientoInventarioDTO.getId())
            .map(existingMovimientoInventario -> {
                movimientoInventarioMapper.partialUpdate(existingMovimientoInventario, movimientoInventarioDTO);

                return existingMovimientoInventario;
            })
            .flatMap(movimientoInventarioRepository::save)
            .map(movimientoInventarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovimientoInventarioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MovimientoInventarios");
        return movimientoInventarioRepository.findAllBy(pageable).map(movimientoInventarioMapper::toDto);
    }

    public Flux<MovimientoInventarioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return movimientoInventarioRepository.findAllWithEagerRelationships(pageable).map(movimientoInventarioMapper::toDto);
    }

    public Mono<Long> countAll() {
        return movimientoInventarioRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MovimientoInventarioDTO> findOne(Long id) {
        LOG.debug("Request to get MovimientoInventario : {}", id);
        return movimientoInventarioRepository.findOneWithEagerRelationships(id).map(movimientoInventarioMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete MovimientoInventario : {}", id);
        return movimientoInventarioRepository.deleteById(id);
    }
}
