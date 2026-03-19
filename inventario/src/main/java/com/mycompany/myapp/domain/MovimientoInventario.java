package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.TipoMovimiento;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MovimientoInventario.
 */
@Table("movimiento_inventario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovimientoInventario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("tipo_movimiento")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Column("cantidad")
    private Integer cantidad;

    @NotNull(message = "must not be null")
    @Column("fecha")
    private LocalDate fecha;

    @Size(max = 250)
    @Column("observacion")
    private String observacion;

    @Transient
    private Inventario inventario;

    @Column("inventario_id")
    private Long inventarioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MovimientoInventario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMovimiento getTipoMovimiento() {
        return this.tipoMovimiento;
    }

    public MovimientoInventario tipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.setTipoMovimiento(tipoMovimiento);
        return this;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public MovimientoInventario cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public MovimientoInventario fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public MovimientoInventario observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
        this.inventarioId = inventario != null ? inventario.getId() : null;
    }

    public MovimientoInventario inventario(Inventario inventario) {
        this.setInventario(inventario);
        return this;
    }

    public Long getInventarioId() {
        return this.inventarioId;
    }

    public void setInventarioId(Long inventario) {
        this.inventarioId = inventario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovimientoInventario)) {
            return false;
        }
        return getId() != null && getId().equals(((MovimientoInventario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovimientoInventario{" +
            "id=" + getId() +
            ", tipoMovimiento='" + getTipoMovimiento() + "'" +
            ", cantidad=" + getCantidad() +
            ", fecha='" + getFecha() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
