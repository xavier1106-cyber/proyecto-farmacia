package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Medico.
 */
@Table("medico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 100)
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 100)
    @Column("especialidad")
    private String especialidad;

    @Size(min = 10, max = 15)
    @Column("telefono")
    private String telefono;

    @Size(min = 3, max = 50)
    @Column("turno")
    private String turno;

    @NotNull(message = "must not be null")
    @Column("activo")
    private Boolean activo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Medico nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    public Medico especialidad(String especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Medico telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTurno() {
        return this.turno;
    }

    public Medico turno(String turno) {
        this.setTurno(turno);
        return this;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Medico activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return getId() != null && getId().equals(((Medico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", especialidad='" + getEspecialidad() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", turno='" + getTurno() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
