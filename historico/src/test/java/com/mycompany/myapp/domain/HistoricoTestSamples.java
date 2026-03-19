package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HistoricoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Historico getHistoricoSample1() {
        return new Historico()
            .id(1L)
            .folio("folio1")
            .pacienteId(1L)
            .pacienteNombre("pacienteNombre1")
            .pacienteCurp("pacienteCurp1")
            .medicoId(1L)
            .medicoNombre("medicoNombre1")
            .medicoEspecialidad("medicoEspecialidad1")
            .usuarioQueRegistro("usuarioQueRegistro1")
            .autorizo("autorizo1")
            .observaciones("observaciones1")
            .cantidad(1);
    }

    public static Historico getHistoricoSample2() {
        return new Historico()
            .id(2L)
            .folio("folio2")
            .pacienteId(2L)
            .pacienteNombre("pacienteNombre2")
            .pacienteCurp("pacienteCurp2")
            .medicoId(2L)
            .medicoNombre("medicoNombre2")
            .medicoEspecialidad("medicoEspecialidad2")
            .usuarioQueRegistro("usuarioQueRegistro2")
            .autorizo("autorizo2")
            .observaciones("observaciones2")
            .cantidad(2);
    }

    public static Historico getHistoricoRandomSampleGenerator() {
        return new Historico()
            .id(longCount.incrementAndGet())
            .folio(UUID.randomUUID().toString())
            .pacienteId(longCount.incrementAndGet())
            .pacienteNombre(UUID.randomUUID().toString())
            .pacienteCurp(UUID.randomUUID().toString())
            .medicoId(longCount.incrementAndGet())
            .medicoNombre(UUID.randomUUID().toString())
            .medicoEspecialidad(UUID.randomUUID().toString())
            .usuarioQueRegistro(UUID.randomUUID().toString())
            .autorizo(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet());
    }
}
