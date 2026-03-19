package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class InventarioSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("clave_medicamento", table, columnPrefix + "_clave_medicamento"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("presentacion", table, columnPrefix + "_presentacion"));
        columns.add(Column.aliased("lote", table, columnPrefix + "_lote"));
        columns.add(Column.aliased("cantidad", table, columnPrefix + "_cantidad"));
        columns.add(Column.aliased("cantidad_minima", table, columnPrefix + "_cantidad_minima"));
        columns.add(Column.aliased("fecha_caducidad", table, columnPrefix + "_fecha_caducidad"));
        columns.add(Column.aliased("ubicacion", table, columnPrefix + "_ubicacion"));
        columns.add(Column.aliased("controlado", table, columnPrefix + "_controlado"));

        return columns;
    }
}
