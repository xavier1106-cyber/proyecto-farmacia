package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InventarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventario.class);
        Inventario inventario1 = getInventarioSample1();
        Inventario inventario2 = new Inventario();
        assertThat(inventario1).isNotEqualTo(inventario2);

        inventario2.setId(inventario1.getId());
        assertThat(inventario1).isEqualTo(inventario2);

        inventario2 = getInventarioSample2();
        assertThat(inventario1).isNotEqualTo(inventario2);
    }
}
