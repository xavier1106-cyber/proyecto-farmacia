package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MovimientoInventario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MovimientoInventario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovimientoInventarioRepository
    extends ReactiveCrudRepository<MovimientoInventario, Long>, MovimientoInventarioRepositoryInternal {
    Flux<MovimientoInventario> findAllBy(Pageable pageable);

    @Override
    Mono<MovimientoInventario> findOneWithEagerRelationships(Long id);

    @Override
    Flux<MovimientoInventario> findAllWithEagerRelationships();

    @Override
    Flux<MovimientoInventario> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM movimiento_inventario entity WHERE entity.inventario_id = :id")
    Flux<MovimientoInventario> findByInventario(Long id);

    @Query("SELECT * FROM movimiento_inventario entity WHERE entity.inventario_id IS NULL")
    Flux<MovimientoInventario> findAllWhereInventarioIsNull();

    @Override
    <S extends MovimientoInventario> Mono<S> save(S entity);

    @Override
    Flux<MovimientoInventario> findAll();

    @Override
    Mono<MovimientoInventario> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MovimientoInventarioRepositoryInternal {
    <S extends MovimientoInventario> Mono<S> save(S entity);

    Flux<MovimientoInventario> findAllBy(Pageable pageable);

    Flux<MovimientoInventario> findAll();

    Mono<MovimientoInventario> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MovimientoInventario> findAllBy(Pageable pageable, Criteria criteria);

    Mono<MovimientoInventario> findOneWithEagerRelationships(Long id);

    Flux<MovimientoInventario> findAllWithEagerRelationships();

    Flux<MovimientoInventario> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
