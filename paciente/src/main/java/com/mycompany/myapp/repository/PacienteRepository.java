package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Paciente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Paciente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PacienteRepository extends ReactiveCrudRepository<Paciente, Long>, PacienteRepositoryInternal {
    Flux<Paciente> findAllBy(Pageable pageable);

    @Override
    <S extends Paciente> Mono<S> save(S entity);

    @Override
    Flux<Paciente> findAll();

    @Override
    Mono<Paciente> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PacienteRepositoryInternal {
    <S extends Paciente> Mono<S> save(S entity);

    Flux<Paciente> findAllBy(Pageable pageable);

    Flux<Paciente> findAll();

    Mono<Paciente> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Paciente> findAllBy(Pageable pageable, Criteria criteria);
}
