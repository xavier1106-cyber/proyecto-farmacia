package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.repository.HistoricoRepository;
import com.mycompany.myapp.service.HistoricoService;
import com.mycompany.myapp.service.dto.HistoricoDTO;
import com.mycompany.myapp.service.mapper.HistoricoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Historico}.
 */
@Service
@Transactional
public class HistoricoServiceImpl implements HistoricoService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricoServiceImpl.class);

    private final HistoricoRepository historicoRepository;

    private final HistoricoMapper historicoMapper;

    public HistoricoServiceImpl(HistoricoRepository historicoRepository, HistoricoMapper historicoMapper) {
        this.historicoRepository = historicoRepository;
        this.historicoMapper = historicoMapper;
    }

    @Override
    public Mono<HistoricoDTO> save(HistoricoDTO historicoDTO) {
        LOG.debug("Request to save Historico : {}", historicoDTO);
        return historicoRepository.save(historicoMapper.toEntity(historicoDTO)).map(historicoMapper::toDto);
    }

    @Override
    public Mono<HistoricoDTO> update(HistoricoDTO historicoDTO) {
        LOG.debug("Request to update Historico : {}", historicoDTO);
        return historicoRepository.save(historicoMapper.toEntity(historicoDTO)).map(historicoMapper::toDto);
    }

    @Override
    public Mono<HistoricoDTO> partialUpdate(HistoricoDTO historicoDTO) {
        LOG.debug("Request to partially update Historico : {}", historicoDTO);

        return historicoRepository
            .findById(historicoDTO.getId())
            .map(existingHistorico -> {
                historicoMapper.partialUpdate(existingHistorico, historicoDTO);

                return existingHistorico;
            })
            .flatMap(historicoRepository::save)
            .map(historicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HistoricoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Historicos");
        return historicoRepository.findAllBy(pageable).map(historicoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return historicoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HistoricoDTO> findOne(Long id) {
        LOG.debug("Request to get Historico : {}", id);
        return historicoRepository.findById(id).map(historicoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Historico : {}", id);
        return historicoRepository.deleteById(id);
    }
}
