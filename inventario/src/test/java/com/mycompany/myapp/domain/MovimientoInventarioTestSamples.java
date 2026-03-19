package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MovimientoInventarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MovimientoInventario getMovimientoInventarioSample1() {
        return new MovimientoInventario().id(1L).cantidad(1).observacion("observacion1");
    }

    public static MovimientoInventario getMovimientoInventarioSample2() {
        return new MovimientoInventario().id(2L).cantidad(2).observacion("observacion2");
    }

    public static MovimientoInventario getMovimientoInventarioRandomSampleGenerator() {
        return new MovimientoInventario()
            .id(longCount.incrementAndGet())
            .cantidad(intCount.incrementAndGet())
            .observacion(UUID.randomUUID().toString());
    }
}
