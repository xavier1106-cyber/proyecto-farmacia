package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InventarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Inventario getInventarioSample1() {
        return new Inventario()
            .id(1L)
            .claveMedicamento("claveMedicamento1")
            .nombre("nombre1")
            .presentacion("presentacion1")
            .lote("lote1")
            .cantidad(1)
            .cantidadMinima(1)
            .ubicacion("ubicacion1");
    }

    public static Inventario getInventarioSample2() {
        return new Inventario()
            .id(2L)
            .claveMedicamento("claveMedicamento2")
            .nombre("nombre2")
            .presentacion("presentacion2")
            .lote("lote2")
            .cantidad(2)
            .cantidadMinima(2)
            .ubicacion("ubicacion2");
    }

    public static Inventario getInventarioRandomSampleGenerator() {
        return new Inventario()
            .id(longCount.incrementAndGet())
            .claveMedicamento(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .presentacion(UUID.randomUUID().toString())
            .lote(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .cantidadMinima(intCount.incrementAndGet())
            .ubicacion(UUID.randomUUID().toString());
    }
}
