package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.TipoMovimiento;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MovimientoInventario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovimientoInventarioDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    private Integer cantidad;

    @NotNull(message = "must not be null")
    private LocalDate fecha;

    @Size(max = 250)
    private String observacion;

    private InventarioDTO inventario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public InventarioDTO getInventario() {
        return inventario;
    }

    public void setInventario(InventarioDTO inventario) {
        this.inventario = inventario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovimientoInventarioDTO)) {
            return false;
        }

        MovimientoInventarioDTO movimientoInventarioDTO = (MovimientoInventarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, movimientoInventarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovimientoInventarioDTO{" +
            "id=" + getId() +
            ", tipoMovimiento='" + getTipoMovimiento() + "'" +
            ", cantidad=" + getCantidad() +
            ", fecha='" + getFecha() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", inventario=" + getInventario() +
            "}";
    }
}
