package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.InventarioAsserts.*;
import static com.mycompany.myapp.domain.InventarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventarioMapperTest {

    private InventarioMapper inventarioMapper;

    @BeforeEach
    void setUp() {
        inventarioMapper = new InventarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInventarioSample1();
        var actual = inventarioMapper.toEntity(inventarioMapper.toDto(expected));
        assertInventarioAllPropertiesEquals(expected, actual);
    }
}
