package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class Productos implements Serializable {

    private String descripcion;
    private String nombre;
    private String codigo;
    private String stock;
    private String precio;
    private String cantidad;
    private String flete;
    private String idProducto;
    private String marca;
    private String unidad;
    private String estado;
    private String precioAcumulado;
    private String almacen;
    private String observacion;
    private String distrito;
    private String numPromocion;
    private Integer indice;


    public Productos(String descripcion, String nombre, String codigo, String stock, String precio,
                     String cantidad, String flete, String idProducto, String marca, String unidad,
                     String estado, String precioAcumulado, String almacen, String observacion,
                     String distrito, String numPromocion, Integer indice) {
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.codigo = codigo;
        this.stock = stock;
        this.precio = precio;
        this.cantidad = cantidad;
        this.flete = flete;
        this.idProducto = idProducto;
        this.marca = marca;
        this.unidad = unidad;
        this.estado = estado;
        this.precioAcumulado = precioAcumulado;
        this.almacen = almacen;
        this.observacion = observacion;
        this.distrito = distrito;
        this.numPromocion = numPromocion;
        this.indice = indice;
    }

    public Productos() {
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getNumPromocion() {
        return numPromocion;
    }

    public void setNumPromocion(String numPromocion) {
        this.numPromocion = numPromocion;
    }

    public String getDistrito() { return distrito; }

    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getObservacion() { return observacion; }

    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getAlmacen() { return almacen; }

    public void setAlmacen(String almacen) {this.almacen = almacen; }

    public String getNombre() { return nombre; }

    public String getPrecioAcumulado() { return precioAcumulado; }

    public void setPrecioAcumulado(String precioAcumulado) { this.precioAcumulado = precioAcumulado; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFlete() {
        return flete;
    }

    public void setFlete(String flete) {
        this.flete = flete;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
