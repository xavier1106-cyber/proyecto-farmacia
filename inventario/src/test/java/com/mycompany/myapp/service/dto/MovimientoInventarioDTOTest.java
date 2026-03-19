package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovimientoInventarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovimientoInventarioDTO.class);
        MovimientoInventarioDTO movimientoInventarioDTO1 = new MovimientoInventarioDTO();
        movimientoInventarioDTO1.setId(1L);
        MovimientoInventarioDTO movimientoInventarioDTO2 = new MovimientoInventarioDTO();
        assertThat(movimientoInventarioDTO1).isNotEqualTo(movimientoInventarioDTO2);
        movimientoInventarioDTO2.setId(movimientoInventarioDTO1.getId());
        assertThat(movimientoInventarioDTO1).isEqualTo(movimientoInventarioDTO2);
        movimientoInventarioDTO2.setId(2L);
        assertThat(movimientoInventarioDTO1).isNotEqualTo(movimientoInventarioDTO2);
        movimientoInventarioDTO1.setId(null);
        assertThat(movimientoInventarioDTO1).isNotEqualTo(movimientoInventarioDTO2);
    }
}
