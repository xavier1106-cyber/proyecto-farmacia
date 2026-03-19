package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MovimientoInventarioAsserts.*;
import static com.mycompany.myapp.domain.MovimientoInventarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovimientoInventarioMapperTest {

    private MovimientoInventarioMapper movimientoInventarioMapper;

    @BeforeEach
    void setUp() {
        movimientoInventarioMapper = new MovimientoInventarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMovimientoInventarioSample1();
        var actual = movimientoInventarioMapper.toEntity(movimientoInventarioMapper.toDto(expected));
        assertMovimientoInventarioAllPropertiesEquals(expected, actual);
    }
}
