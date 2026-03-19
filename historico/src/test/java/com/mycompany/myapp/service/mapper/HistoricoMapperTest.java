package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.HistoricoAsserts.*;
import static com.mycompany.myapp.domain.HistoricoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoricoMapperTest {

    private HistoricoMapper historicoMapper;

    @BeforeEach
    void setUp() {
        historicoMapper = new HistoricoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoricoSample1();
        var actual = historicoMapper.toEntity(historicoMapper.toDto(expected));
        assertHistoricoAllPropertiesEquals(expected, actual);
    }
}
