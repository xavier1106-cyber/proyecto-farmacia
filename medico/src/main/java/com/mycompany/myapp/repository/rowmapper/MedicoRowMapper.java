package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Medico;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Medico}, with proper type conversions.
 */
@Service
public class MedicoRowMapper implements BiFunction<Row, String, Medico> {

    private final ColumnConverter converter;

    public MedicoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Medico} stored in the database.
     */
    @Override
    public Medico apply(Row row, String prefix) {
        Medico entity = new Medico();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setEspecialidad(converter.fromRow(row, prefix + "_especialidad", String.class));
        entity.setTelefono(converter.fromRow(row, prefix + "_telefono", String.class));
        entity.setTurno(converter.fromRow(row, prefix + "_turno", String.class));
        entity.setActivo(converter.fromRow(row, prefix + "_activo", Boolean.class));
        return entity;
    }
}
