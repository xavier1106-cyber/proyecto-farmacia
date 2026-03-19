package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MovimientoInventario;
import com.mycompany.myapp.repository.rowmapper.InventarioRowMapper;
import com.mycompany.myapp.repository.rowmapper.MovimientoInventarioRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the MovimientoInventario entity.
 */
@SuppressWarnings("unused")
class MovimientoInventarioRepositoryInternalImpl
    extends SimpleR2dbcRepository<MovimientoInventario, Long>
    implements MovimientoInventarioRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final InventarioRowMapper inventarioMapper;
    private final MovimientoInventarioRowMapper movimientoinventarioMapper;

    private static final Table entityTable = Table.aliased("movimiento_inventario", EntityManager.ENTITY_ALIAS);
    private static final Table inventarioTable = Table.aliased("inventario", "inventario");

    public MovimientoInventarioRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        InventarioRowMapper inventarioMapper,
        MovimientoInventarioRowMapper movimientoinventarioMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(MovimientoInventario.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.inventarioMapper = inventarioMapper;
        this.movimientoinventarioMapper = movimientoinventarioMapper;
    }

    @Override
    public Flux<MovimientoInventario> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<MovimientoInventario> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = MovimientoInventarioSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(InventarioSqlHelper.getColumns(inventarioTable, "inventario"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(inventarioTable)
            .on(Column.create("inventario_id", entityTable))
            .equals(Column.create("id", inventarioTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, MovimientoInventario.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<MovimientoInventario> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<MovimientoInventario> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<MovimientoInventario> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<MovimientoInventario> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<MovimientoInventario> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private MovimientoInventario process(Row row, RowMetadata metadata) {
        MovimientoInventario entity = movimientoinventarioMapper.apply(row, "e");
        entity.setInventario(inventarioMapper.apply(row, "inventario"));
        return entity;
    }

    @Override
    public <S extends MovimientoInventario> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
