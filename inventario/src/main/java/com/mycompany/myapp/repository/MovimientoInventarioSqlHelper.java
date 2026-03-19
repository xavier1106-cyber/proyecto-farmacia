package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MovimientoInventarioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("tipo_movimiento", table, columnPrefix + "_tipo_movimiento"));
        columns.add(Column.aliased("cantidad", table, columnPrefix + "_cantidad"));
        columns.add(Column.aliased("fecha", table, columnPrefix + "_fecha"));
        columns.add(Column.aliased("observacion", table, columnPrefix + "_observacion"));

        columns.add(Column.aliased("inventario_id", table, columnPrefix + "_inventario_id"));
        return columns;
    }
}
