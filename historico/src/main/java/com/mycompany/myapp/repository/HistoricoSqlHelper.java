package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HistoricoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("fecha_emision", table, columnPrefix + "_fecha_emision"));
        columns.add(Column.aliased("folio", table, columnPrefix + "_folio"));
        columns.add(Column.aliased("paciente_id", table, columnPrefix + "_paciente_id"));
        columns.add(Column.aliased("paciente_nombre", table, columnPrefix + "_paciente_nombre"));
        columns.add(Column.aliased("paciente_curp", table, columnPrefix + "_paciente_curp"));
        columns.add(Column.aliased("medico_id", table, columnPrefix + "_medico_id"));
        columns.add(Column.aliased("medico_nombre", table, columnPrefix + "_medico_nombre"));
        columns.add(Column.aliased("medico_especialidad", table, columnPrefix + "_medico_especialidad"));
        columns.add(Column.aliased("usuario_que_registro", table, columnPrefix + "_usuario_que_registro"));
        columns.add(Column.aliased("medicamentos", table, columnPrefix + "_medicamentos"));
        columns.add(Column.aliased("autorizo", table, columnPrefix + "_autorizo"));
        columns.add(Column.aliased("observaciones", table, columnPrefix + "_observaciones"));
        columns.add(Column.aliased("cantidad", table, columnPrefix + "_cantidad"));

        return columns;
    }
}
