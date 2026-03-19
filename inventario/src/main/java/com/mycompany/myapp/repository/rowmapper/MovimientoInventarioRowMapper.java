package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.MovimientoInventario;
import com.mycompany.myapp.domain.enumeration.TipoMovimiento;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link MovimientoInventario}, with proper type conversions.
 */
@Service
public class MovimientoInventarioRowMapper implements BiFunction<Row, String, MovimientoInventario> {

    private final ColumnConverter converter;

    public MovimientoInventarioRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link MovimientoInventario} stored in the database.
     */
    @Override
    public MovimientoInventario apply(Row row, String prefix) {
        MovimientoInventario entity = new MovimientoInventario();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTipoMovimiento(converter.fromRow(row, prefix + "_tipo_movimiento", TipoMovimiento.class));
        entity.setCantidad(converter.fromRow(row, prefix + "_cantidad", Integer.class));
        entity.setFecha(converter.fromRow(row, prefix + "_fecha", LocalDate.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        entity.setInventarioId(converter.fromRow(row, prefix + "_inventario_id", Long.class));
        return entity;
    }
}
