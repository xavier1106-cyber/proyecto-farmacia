package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Paciente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 18, max = 18)
    private String curp;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotNull(message = "must not be null")
    private LocalDate fechaNacimiento;

    @NotNull(message = "must not be null")
    @Size(min = 1, max = 20)
    private String sexo;

    @Size(min = 8, max = 20)
    private String numeroSeguroSocial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNumeroSeguroSocial() {
        return numeroSeguroSocial;
    }

    public void setNumeroSeguroSocial(String numeroSeguroSocial) {
        this.numeroSeguroSocial = numeroSeguroSocial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteDTO)) {
            return false;
        }

        PacienteDTO pacienteDTO = (PacienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteDTO{" +
            "id=" + getId() +
            ", curp='" + getCurp() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", numeroSeguroSocial='" + getNumeroSeguroSocial() + "'" +
            "}";
    }
}
