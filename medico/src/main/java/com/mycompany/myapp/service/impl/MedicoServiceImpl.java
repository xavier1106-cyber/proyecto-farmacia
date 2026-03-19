package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.repository.MedicoRepository;
import com.mycompany.myapp.service.MedicoService;
import com.mycompany.myapp.service.dto.MedicoDTO;
import com.mycompany.myapp.service.mapper.MedicoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Medico}.
 */
@Service
@Transactional
public class MedicoServiceImpl implements MedicoService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicoServiceImpl.class);

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    public MedicoServiceImpl(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Override
    public Mono<MedicoDTO> save(MedicoDTO medicoDTO) {
        LOG.debug("Request to save Medico : {}", medicoDTO);
        return medicoRepository.save(medicoMapper.toEntity(medicoDTO)).map(medicoMapper::toDto);
    }

    @Override
    public Mono<MedicoDTO> update(MedicoDTO medicoDTO) {
        LOG.debug("Request to update Medico : {}", medicoDTO);
        return medicoRepository.save(medicoMapper.toEntity(medicoDTO)).map(medicoMapper::toDto);
    }

    @Override
    public Mono<MedicoDTO> partialUpdate(MedicoDTO medicoDTO) {
        LOG.debug("Request to partially update Medico : {}", medicoDTO);

        return medicoRepository
            .findById(medicoDTO.getId())
            .map(existingMedico -> {
                medicoMapper.partialUpdate(existingMedico, medicoDTO);

                return existingMedico;
            })
            .flatMap(medicoRepository::save)
            .map(medicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MedicoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Medicos");
        return medicoRepository.findAllBy(pageable).map(medicoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return medicoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MedicoDTO> findOne(Long id) {
        LOG.debug("Request to get Medico : {}", id);
        return medicoRepository.findById(id).map(medicoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        LOG.debug("Request to delete Medico : {}", id);
        return medicoRepository.deleteById(id);
    }
}
