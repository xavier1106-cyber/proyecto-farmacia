package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.HistoricoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoricoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historico.class);
        Historico historico1 = getHistoricoSample1();
        Historico historico2 = new Historico();
        assertThat(historico1).isNotEqualTo(historico2);

        historico2.setId(historico1.getId());
        assertThat(historico1).isEqualTo(historico2);

        historico2 = getHistoricoSample2();
        assertThat(historico1).isNotEqualTo(historico2);
    }
}
