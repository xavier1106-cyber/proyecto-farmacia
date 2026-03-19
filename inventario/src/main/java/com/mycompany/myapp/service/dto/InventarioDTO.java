package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Inventario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventarioDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 50)
    private String claveMedicamento;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 150)
    private String nombre;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 100)
    private String presentacion;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 50)
    private String lote;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer cantidad;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    private Integer cantidadMinima;

    @NotNull(message = "must not be null")
    private LocalDate fechaCaducidad;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 100)
    private String ubicacion;

    @NotNull(message = "must not be null")
    private Boolean controlado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaveMedicamento() {
        return claveMedicamento;
    }

    public void setClaveMedicamento(String claveMedicamento) {
        this.claveMedicamento = claveMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(Integer cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getControlado() {
        return controlado;
    }

    public void setControlado(Boolean controlado) {
        this.controlado = controlado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventarioDTO)) {
            return false;
        }

        InventarioDTO inventarioDTO = (InventarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventarioDTO{" +
            "id=" + getId() +
            ", claveMedicamento='" + getClaveMedicamento() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", presentacion='" + getPresentacion() + "'" +
            ", lote='" + getLote() + "'" +
            ", cantidad=" + getCantidad() +
            ", cantidadMinima=" + getCantidadMinima() +
            ", fechaCaducidad='" + getFechaCaducidad() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", controlado='" + getControlado() + "'" +
            "}";
    }
}
