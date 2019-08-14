package com.example.sistemas.pedidosTemporal.Entidades;

import java.io.Serializable;

public class ConsultaPromocion implements Serializable {



    private String nroPromocion;
    private String glosa;
    private String fechaEmision;
    private String fechaFinVigencia;
    private String moneda;
    private String precioregular;
    private String preciopaquete;
    private String ahorro;
    private String formaPromocion;
    private String importecantidad;
    private String nomCanal;
    private String codTipoListapre;
    private String codCanal;
    private String codArticulo;
    private String descripcion;
    private String desMarca;
    private String undVenta;
    private String flgRegalo;
    private String cantidad;
    private String tasadescuento;
    private String subtotal;

    public ConsultaPromocion() {
    }

    public String getTasadescuento() {
        return tasadescuento;
    }

    public void setTasadescuento(String tasadescuento) {
        this.tasadescuento = tasadescuento;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getPrecioregular() {
        return precioregular;
    }

    public void setPrecioregular(String precioregular) {
        this.precioregular = precioregular;
    }

    public String getPreciopaquete() {
        return preciopaquete;
    }

    public void setPreciopaquete(String preciopaquete) {
        this.preciopaquete = preciopaquete;
    }

    public String getAhorro() {
        return ahorro;
    }

    public void setAhorro(String ahorro) {
        this.ahorro = ahorro;
    }

    public String getNroPromocion() {
        return nroPromocion;
    }

    public void setNroPromocion(String nroPromocion) {
        this.nroPromocion = nroPromocion;
    }

    public String getGlosa() {
        return glosa;
    }

    public void setGlosa(String glosa) {
        this.glosa = glosa;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setFechaFinVigencia(String fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getFormaPromocion() {
        return formaPromocion;
    }

    public void setFormaPromocion(String formaPromocion) {
        this.formaPromocion = formaPromocion;
    }

    public String getImportecantidad() {
        return importecantidad;
    }

    public void setImportecantidad(String importecantidad) {
        this.importecantidad = importecantidad;
    }

    public String getNomCanal() {
        return nomCanal;
    }

    public void setNomCanal(String nomCanal) {
        this.nomCanal = nomCanal;
    }

    public String getCodTipoListapre() {
        return codTipoListapre;
    }

    public void setCodTipoListapre(String codTipoListapre) {
        this.codTipoListapre = codTipoListapre;
    }

    public String getCodCanal() {
        return codCanal;
    }

    public void setCodCanal(String codCanal) {
        this.codCanal = codCanal;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDesMarca() {
        return desMarca;
    }

    public void setDesMarca(String desMarca) {
        this.desMarca = desMarca;
    }

    public String getUndVenta() {
        return undVenta;
    }

    public void setUndVenta(String undVenta) {
        this.undVenta = undVenta;
    }

    public String getFlgRegalo() {
        return flgRegalo;
    }

    public void setFlgRegalo(String flgRegalo) {
        this.flgRegalo = flgRegalo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
