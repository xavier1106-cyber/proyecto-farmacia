package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.repository.InventarioRepository;
import com.mycompany.myapp.service.InventarioService;
import com.mycompany.myapp.service.dto.InventarioDTO;
import com.mycompany.myapp.service.mapper.InventarioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Inventario}.
 */
@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioServiceImpl.class);

    private final InventarioRepository inventarioRepository;

    private final InventarioMapper inventarioMapper;

    public InventarioServiceImpl(InventarioRepository inventarioRepository, InventarioMapper inventarioMapper) {
        this.inventarioRepository = inventarioRepository;
        this.inventarioMapper = inventarioMapper;
    }

    @Override
    public Mono<InventarioDTO> save(InventarioDTO inventarioDTO) {
        LOG.debug("Request to save Inventario : {}", inventarioDTO);
        return inventarioRepository.save(inventarioMapper.toEntity(inventarioDTO)).map(inventarioMapper::toDto);
    }

    @Override
    public Mono<InventarioDTO> update(InventarioDTO inventarioDTO) {
        LOG.debug("Request to update Inventario : {}", inventarioDTO);
        return inventarioRepository.save(inventarioMapper.toEntity(inventarioDTO)).map(inventarioMapper::toDto);
    }

    @Override
    public Mono<InventarioDTO> partialUpdate(InventarioDTO inventarioDTO) {
        LOG.debug("Request to partially update Inventario : {}", inventarioDTO);

        return inventarioRepository
            .findById(inventarioDTO.getId())
            .map(existingInventario -> {
                inventarioMapper.partialUpdate(existingInventario, inventarioDTO);

                return existingInventario;
            })
            .flatMap(inventarioRepository::save)
            .map(inventarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<InventarioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Inventarios");
        return inventarioRepository.findAllBy(pageable).map(inventarioMapper::toDto);
    }

    public Mono<Long> countAll() {
        return inventarioRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<InventarioDTO> findOne(Long id) {
        LOG.debug("Request to get Inventario : {}", id);
        return inventarioRepository.findById(id).map(inventarioMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Inventario : {}", id);
        return inventarioRepository.deleteById(id);
    }
}
