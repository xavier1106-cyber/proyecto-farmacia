package com.mycompany.myapp.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Inventario.
 */
@Table("inventario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 50)
    @Column("clave_medicamento")
    private String claveMedicamento;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 150)
    @Column("nombre")
    private String nombre;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 100)
    @Column("presentacion")
    private String presentacion;

    @NotNull(message = "must not be null")
    @Size(min = 2, max = 50)
    @Column("lote")
    private String lote;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("cantidad")
    private Integer cantidad;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Column("cantidad_minima")
    private Integer cantidadMinima;

    @NotNull(message = "must not be null")
    @Column("fecha_caducidad")
    private LocalDate fechaCaducidad;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 100)
    @Column("ubicacion")
    private String ubicacion;

    @NotNull(message = "must not be null")
    @Column("controlado")
    private Boolean controlado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaveMedicamento() {
        return this.claveMedicamento;
    }

    public Inventario claveMedicamento(String claveMedicamento) {
        this.setClaveMedicamento(claveMedicamento);
        return this;
    }

    public void setClaveMedicamento(String claveMedicamento) {
        this.claveMedicamento = claveMedicamento;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Inventario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return this.presentacion;
    }

    public Inventario presentacion(String presentacion) {
        this.setPresentacion(presentacion);
        return this;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getLote() {
        return this.lote;
    }

    public Inventario lote(String lote) {
        this.setLote(lote);
        return this;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Inventario cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadMinima() {
        return this.cantidadMinima;
    }

    public Inventario cantidadMinima(Integer cantidadMinima) {
        this.setCantidadMinima(cantidadMinima);
        return this;
    }

    public void setCantidadMinima(Integer cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public LocalDate getFechaCaducidad() {
        return this.fechaCaducidad;
    }

    public Inventario fechaCaducidad(LocalDate fechaCaducidad) {
        this.setFechaCaducidad(fechaCaducidad);
        return this;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Inventario ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getControlado() {
        return this.controlado;
    }

    public Inventario controlado(Boolean controlado) {
        this.setControlado(controlado);
        return this;
    }

    public void setControlado(Boolean controlado) {
        this.controlado = controlado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventario)) {
            return false;
        }
        return getId() != null && getId().equals(((Inventario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventario{" +
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
