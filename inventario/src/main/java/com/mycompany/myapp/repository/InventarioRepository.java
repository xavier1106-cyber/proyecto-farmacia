package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Inventario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Inventario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventarioRepository extends ReactiveCrudRepository<Inventario, Long>, InventarioRepositoryInternal {
    Flux<Inventario> findAllBy(Pageable pageable);

    @Override
    <S extends Inventario> Mono<S> save(S entity);

    @Override
    Flux<Inventario> findAll();

    @Override
    Mono<Inventario> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface InventarioRepositoryInternal {
    <S extends Inventario> Mono<S> save(S entity);

    Flux<Inventario> findAllBy(Pageable pageable);

    Flux<Inventario> findAll();

    Mono<Inventario> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Inventario> findAllBy(Pageable pageable, Criteria criteria);
}
