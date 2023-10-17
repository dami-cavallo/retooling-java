package com.retooling.accenture.msfarmservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "transaccion")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_TRANSACCION")
    private TipoTransaccionProducto tipoTransaccion;
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Column(name = "PRECIO_VENTA_UNITARIO")
    private double precioUnitario;

    @Column(name = "PRECIO_TOTAL")
    private double precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_PRODUCTO")
    private TipoTransaccionProducto tipoProducto;
    @Column(name = "FECHA")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "FK_FARMER")
    @JsonBackReference
    private Farmer farmer;


    protected Transaccion(){

    }

    public Transaccion(TipoTransaccionProducto tipoTransaccion, int cantidad, double precioUnitario, double precioTotal, TipoTransaccionProducto tipoProducto, LocalDateTime fecha) {
        this.tipoTransaccion = tipoTransaccion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
        this.tipoProducto = tipoProducto;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoTransaccionProducto getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccionProducto tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public TipoTransaccionProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoTransaccionProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }


}
