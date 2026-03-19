package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoricoDTO.class);
        HistoricoDTO historicoDTO1 = new HistoricoDTO();
        historicoDTO1.setId(1L);
        HistoricoDTO historicoDTO2 = new HistoricoDTO();
        assertThat(historicoDTO1).isNotEqualTo(historicoDTO2);
        historicoDTO2.setId(historicoDTO1.getId());
        assertThat(historicoDTO1).isEqualTo(historicoDTO2);
        historicoDTO2.setId(2L);
        assertThat(historicoDTO1).isNotEqualTo(historicoDTO2);
        historicoDTO1.setId(null);
        assertThat(historicoDTO1).isNotEqualTo(historicoDTO2);
    }
}
