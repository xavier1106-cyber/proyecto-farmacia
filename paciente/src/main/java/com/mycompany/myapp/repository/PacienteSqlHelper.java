package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PacienteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("curp", table, columnPrefix + "_curp"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("fecha_nacimiento", table, columnPrefix + "_fecha_nacimiento"));
        columns.add(Column.aliased("sexo", table, columnPrefix + "_sexo"));
        columns.add(Column.aliased("numero_seguro_social", table, columnPrefix + "_numero_seguro_social"));

        return columns;
    }
}
