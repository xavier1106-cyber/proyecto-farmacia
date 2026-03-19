package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Inventario;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Inventario}, with proper type conversions.
 */
@Service
public class InventarioRowMapper implements BiFunction<Row, String, Inventario> {

    private final ColumnConverter converter;

    public InventarioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Inventario} stored in the database.
     */
    @Override
    public Inventario apply(Row row, String prefix) {
        Inventario entity = new Inventario();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setClaveMedicamento(converter.fromRow(row, prefix + "_clave_medicamento", String.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setPresentacion(converter.fromRow(row, prefix + "_presentacion", String.class));
        entity.setLote(converter.fromRow(row, prefix + "_lote", String.class));
        entity.setCantidad(converter.fromRow(row, prefix + "_cantidad", Integer.class));
        entity.setCantidadMinima(converter.fromRow(row, prefix + "_cantidad_minima", Integer.class));
        entity.setFechaCaducidad(converter.fromRow(row, prefix + "_fecha_caducidad", LocalDate.class));
        entity.setUbicacion(converter.fromRow(row, prefix + "_ubicacion", String.class));
        entity.setControlado(converter.fromRow(row, prefix + "_controlado", Boolean.class));
        return entity;
    }
}
