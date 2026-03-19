package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Paciente.
 */
@Table("paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 18, max = 18)
    @Column("curp")
    private String curp;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 100)
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotNull(message = "must not be null")
    @Size(min = 1, max = 20)
    @Column("sexo")
    private String sexo;

    @Size(min = 8, max = 20)
    @Column("numero_seguro_social")
    private String numeroSeguroSocial;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurp() {
        return this.curp;
    }

    public Paciente curp(String curp) {
        this.setCurp(curp);
        return this;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Paciente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Paciente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return this.sexo;
    }

    public Paciente sexo(String sexo) {
        this.setSexo(sexo);
        return this;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNumeroSeguroSocial() {
        return this.numeroSeguroSocial;
    }

    public Paciente numeroSeguroSocial(String numeroSeguroSocial) {
        this.setNumeroSeguroSocial(numeroSeguroSocial);
        return this;
    }

    public void setNumeroSeguroSocial(String numeroSeguroSocial) {
        this.numeroSeguroSocial = numeroSeguroSocial;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", curp='" + getCurp() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", numeroSeguroSocial='" + getNumeroSeguroSocial() + "'" +
            "}";
    }
}
