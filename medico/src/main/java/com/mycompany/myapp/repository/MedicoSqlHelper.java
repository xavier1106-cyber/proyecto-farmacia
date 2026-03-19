package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MedicoSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("especialidad", table, columnPrefix + "_especialidad"));
        columns.add(Column.aliased("telefono", table, columnPrefix + "_telefono"));
        columns.add(Column.aliased("turno", table, columnPrefix + "_turno"));
        columns.add(Column.aliased("activo", table, columnPrefix + "_activo"));

        return columns;
    }
}
