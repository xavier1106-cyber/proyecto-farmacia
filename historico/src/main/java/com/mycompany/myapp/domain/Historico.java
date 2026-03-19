package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Historico.
 */
@Table("historico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Historico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("fecha_emision")
    private Instant fechaEmision;

    @NotNull(message = "must not be null")
    @Column("folio")
    private String folio;

    @NotNull(message = "must not be null")
    @Column("paciente_id")
    private Long pacienteId;

    @NotNull(message = "must not be null")
    @Column("paciente_nombre")
    private String pacienteNombre;

    @Column("paciente_curp")
    private String pacienteCurp;

    @NotNull(message = "must not be null")
    @Column("medico_id")
    private Long medicoId;

    @Column("medico_nombre")
    private String medicoNombre;

    @Column("medico_especialidad")
    private String medicoEspecialidad;

    @NotNull(message = "must not be null")
    @Column("usuario_que_registro")
    private String usuarioQueRegistro;

    @Column("medicamentos")
    private String medicamentos;

    @Column("autorizo")
    private String autorizo;

    @Column("observaciones")
    private String observaciones;

    @Column("cantidad")
    private Integer cantidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Historico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaEmision() {
        return this.fechaEmision;
    }

    public Historico fechaEmision(Instant fechaEmision) {
        this.setFechaEmision(fechaEmision);
        return this;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFolio() {
        return this.folio;
    }

    public Historico folio(String folio) {
        this.setFolio(folio);
        return this;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Long getPacienteId() {
        return this.pacienteId;
    }

    public Historico pacienteId(Long pacienteId) {
        this.setPacienteId(pacienteId);
        return this;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNombre() {
        return this.pacienteNombre;
    }

    public Historico pacienteNombre(String pacienteNombre) {
        this.setPacienteNombre(pacienteNombre);
        return this;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getPacienteCurp() {
        return this.pacienteCurp;
    }

    public Historico pacienteCurp(String pacienteCurp) {
        this.setPacienteCurp(pacienteCurp);
        return this;
    }

    public void setPacienteCurp(String pacienteCurp) {
        this.pacienteCurp = pacienteCurp;
    }

    public Long getMedicoId() {
        return this.medicoId;
    }

    public Historico medicoId(Long medicoId) {
        this.setMedicoId(medicoId);
        return this;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public String getMedicoNombre() {
        return this.medicoNombre;
    }

    public Historico medicoNombre(String medicoNombre) {
        this.setMedicoNombre(medicoNombre);
        return this;
    }

    public void setMedicoNombre(String medicoNombre) {
        this.medicoNombre = medicoNombre;
    }

    public String getMedicoEspecialidad() {
        return this.medicoEspecialidad;
    }

    public Historico medicoEspecialidad(String medicoEspecialidad) {
        this.setMedicoEspecialidad(medicoEspecialidad);
        return this;
    }

    public void setMedicoEspecialidad(String medicoEspecialidad) {
        this.medicoEspecialidad = medicoEspecialidad;
    }

    public String getUsuarioQueRegistro() {
        return this.usuarioQueRegistro;
    }

    public Historico usuarioQueRegistro(String usuarioQueRegistro) {
        this.setUsuarioQueRegistro(usuarioQueRegistro);
        return this;
    }

    public void setUsuarioQueRegistro(String usuarioQueRegistro) {
        this.usuarioQueRegistro = usuarioQueRegistro;
    }

    public String getMedicamentos() {
        return this.medicamentos;
    }

    public Historico medicamentos(String medicamentos) {
        this.setMedicamentos(medicamentos);
        return this;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getAutorizo() {
        return this.autorizo;
    }

    public Historico autorizo(String autorizo) {
        this.setAutorizo(autorizo);
        return this;
    }

    public void setAutorizo(String autorizo) {
        this.autorizo = autorizo;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Historico observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Historico cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historico)) {
            return false;
        }
        return getId() != null && getId().equals(((Historico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historico{" +
            "id=" + getId() +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", folio='" + getFolio() + "'" +
            ", pacienteId=" + getPacienteId() +
            ", pacienteNombre='" + getPacienteNombre() + "'" +
            ", pacienteCurp='" + getPacienteCurp() + "'" +
            ", medicoId=" + getMedicoId() +
            ", medicoNombre='" + getMedicoNombre() + "'" +
            ", medicoEspecialidad='" + getMedicoEspecialidad() + "'" +
            ", usuarioQueRegistro='" + getUsuarioQueRegistro() + "'" +
            ", medicamentos='" + getMedicamentos() + "'" +
            ", autorizo='" + getAutorizo() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", cantidad=" + getCantidad() +
            "}";
    }
}
