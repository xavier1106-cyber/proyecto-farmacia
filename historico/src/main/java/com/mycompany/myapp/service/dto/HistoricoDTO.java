package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Historico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoricoDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private Instant fechaEmision;

    @NotNull(message = "must not be null")
    private String folio;

    @NotNull(message = "must not be null")
    private Long pacienteId;

    @NotNull(message = "must not be null")
    private String pacienteNombre;

    private String pacienteCurp;

    @NotNull(message = "must not be null")
    private Long medicoId;

    private String medicoNombre;

    private String medicoEspecialidad;

    @NotNull(message = "must not be null")
    private String usuarioQueRegistro;

    @Lob
    private String medicamentos;

    private String autorizo;

    private String observaciones;

    private Integer cantidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getPacienteCurp() {
        return pacienteCurp;
    }

    public void setPacienteCurp(String pacienteCurp) {
        this.pacienteCurp = pacienteCurp;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }

    public String getMedicoNombre() {
        return medicoNombre;
    }

    public void setMedicoNombre(String medicoNombre) {
        this.medicoNombre = medicoNombre;
    }

    public String getMedicoEspecialidad() {
        return medicoEspecialidad;
    }

    public void setMedicoEspecialidad(String medicoEspecialidad) {
        this.medicoEspecialidad = medicoEspecialidad;
    }

    public String getUsuarioQueRegistro() {
        return usuarioQueRegistro;
    }

    public void setUsuarioQueRegistro(String usuarioQueRegistro) {
        this.usuarioQueRegistro = usuarioQueRegistro;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getAutorizo() {
        return autorizo;
    }

    public void setAutorizo(String autorizo) {
        this.autorizo = autorizo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoricoDTO)) {
            return false;
        }

        HistoricoDTO historicoDTO = (HistoricoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoricoDTO{" +
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
