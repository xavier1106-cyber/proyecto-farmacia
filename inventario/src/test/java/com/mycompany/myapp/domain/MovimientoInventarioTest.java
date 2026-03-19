package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InventarioTestSamples.*;
import static com.mycompany.myapp.domain.MovimientoInventarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovimientoInventarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimientoInventario.class);
        MovimientoInventario movimientoInventario1 = getMovimientoInventarioSample1();
        MovimientoInventario movimientoInventario2 = new MovimientoInventario();
        assertThat(movimientoInventario1).isNotEqualTo(movimientoInventario2);

        movimientoInventario2.setId(movimientoInventario1.getId());
        assertThat(movimientoInventario1).isEqualTo(movimientoInventario2);

        movimientoInventario2 = getMovimientoInventarioSample2();
        assertThat(movimientoInventario1).isNotEqualTo(movimientoInventario2);
    }

    @Test
    void inventarioTest() {
        MovimientoInventario movimientoInventario = getMovimientoInventarioRandomSampleGenerator();
        Inventario inventarioBack = getInventarioRandomSampleGenerator();

        movimientoInventario.setInventario(inventarioBack);
        assertThat(movimientoInventario.getInventario()).isEqualTo(inventarioBack);

        movimientoInventario.inventario(null);
        assertThat(movimientoInventario.getInventario()).isNull();
    }
}
