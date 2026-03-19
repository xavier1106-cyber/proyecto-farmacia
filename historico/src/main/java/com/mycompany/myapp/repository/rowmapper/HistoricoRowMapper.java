package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Historico;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Historico}, with proper type conversions.
 */
@Service
public class HistoricoRowMapper implements BiFunction<Row, String, Historico> {

    private final ColumnConverter converter;

    public HistoricoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Historico} stored in the database.
     */
    @Override
    public Historico apply(Row row, String prefix) {
        Historico entity = new Historico();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setFechaEmision(converter.fromRow(row, prefix + "_fecha_emision", Instant.class));
        entity.setFolio(converter.fromRow(row, prefix + "_folio", String.class));
        entity.setPacienteId(converter.fromRow(row, prefix + "_paciente_id", Long.class));
        entity.setPacienteNombre(converter.fromRow(row, prefix + "_paciente_nombre", String.class));
        entity.setPacienteCurp(converter.fromRow(row, prefix + "_paciente_curp", String.class));
        entity.setMedicoId(converter.fromRow(row, prefix + "_medico_id", Long.class));
        entity.setMedicoNombre(converter.fromRow(row, prefix + "_medico_nombre", String.class));
        entity.setMedicoEspecialidad(converter.fromRow(row, prefix + "_medico_especialidad", String.class));
        entity.setUsuarioQueRegistro(converter.fromRow(row, prefix + "_usuario_que_registro", String.class));
        entity.setMedicamentos(converter.fromRow(row, prefix + "_medicamentos", String.class));
        entity.setAutorizo(converter.fromRow(row, prefix + "_autorizo", String.class));
        entity.setObservaciones(converter.fromRow(row, prefix + "_observaciones", String.class));
        entity.setCantidad(converter.fromRow(row, prefix + "_cantidad", Integer.class));
        return entity;
    }
}
