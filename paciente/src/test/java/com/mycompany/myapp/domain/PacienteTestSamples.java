package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente().id(1L).curp("curp1").nombre("nombre1").sexo("sexo1").numeroSeguroSocial("numeroSeguroSocial1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente().id(2L).curp("curp2").nombre("nombre2").sexo("sexo2").numeroSeguroSocial("numeroSeguroSocial2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente()
            .id(longCount.incrementAndGet())
            .curp(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .sexo(UUID.randomUUID().toString())
            .numeroSeguroSocial(UUID.randomUUID().toString());
    }
}
