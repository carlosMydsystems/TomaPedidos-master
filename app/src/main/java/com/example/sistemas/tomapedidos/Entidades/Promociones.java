package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class Promociones implements Serializable {

    private String numeroPromocion;
    private String codArticulo;
    private String descripcionPromocion;
    private String marcaPromocion;
    private String unidad;
    private String cantidadPedida;
    private String flgGraba;
    private String tasaDescuento;
    private String codPresentacion;
    private String precioSoles;
    private String precioDolares;
    private String stkDisponible;
    private String stkFisico;
    private String cantidadBonificada;
    private String factor;
    private String formaPromocion;
    private String codDocumento;
    private String opcionSeleccion;
    private String precioRegularSoles;
    private String precioRegularDolares;
    private String valido;
    private String equivalencia;

    public Promociones(String numeroPromocion, String codArticulo, String descripcionPromocion,
                       String marcaPromocion, String unidad, String cantidadPedida, String flgGraba,
                       String tasaDescuento, String codPresentacion, String precioSoles,
                       String precioDolares, String stkDisponible, String stkFisico,
                       String cantidadBonificada, String factor, String formaPromocion,
                       String codDocumento, String opcionSeleccion, String precioRegularSoles,
                       String precioRegularDolares, String valido, String equivalencia) {

        this.numeroPromocion = numeroPromocion;
        this.codArticulo = codArticulo;
        this.descripcionPromocion = descripcionPromocion;
        this.marcaPromocion = marcaPromocion;
        this.unidad = unidad;
        this.cantidadPedida = cantidadPedida;
        this.flgGraba = flgGraba;
        this.tasaDescuento = tasaDescuento;
        this.codPresentacion = codPresentacion;
        this.precioSoles = precioSoles;
        this.precioDolares = precioDolares;
        this.stkDisponible = stkDisponible;
        this.stkFisico = stkFisico;
        this.cantidadBonificada = cantidadBonificada;
        this.factor = factor;
        this.formaPromocion = formaPromocion;
        this.codDocumento = codDocumento;
        this.opcionSeleccion = opcionSeleccion;
        this.precioRegularSoles = precioRegularSoles;
        this.precioRegularDolares = precioRegularDolares;
        this.valido = valido;
        this.equivalencia = equivalencia;
    }

    public Promociones() {
    }

    public String getNumeroPromocion() {
        return numeroPromocion;
    }

    public void setNumeroPromocion(String numeroPromocion) {
        this.numeroPromocion = numeroPromocion;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getDescripcionPromocion() {
        return descripcionPromocion;
    }

    public void setDescripcionPromocion(String descripcionPromocion) {
        this.descripcionPromocion = descripcionPromocion;
    }

    public String getMarcaPromocion() {
        return marcaPromocion;
    }

    public void setMarcaPromocion(String marcaPromocion) {
        this.marcaPromocion = marcaPromocion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getCantidadPedida() {
        return cantidadPedida;
    }

    public void setCantidadPedida(String cantidadPedida) {
        this.cantidadPedida = cantidadPedida;
    }

    public String getFlgGraba() {
        return flgGraba;
    }

    public void setFlgGraba(String flgGraba) {
        this.flgGraba = flgGraba;
    }

    public String getTasaDescuento() {
        return tasaDescuento;
    }

    public void setTasaDescuento(String tasaDescuento) {
        this.tasaDescuento = tasaDescuento;
    }

    public String getCodPresentacion() {
        return codPresentacion;
    }

    public void setCodPresentacion(String codPresentacion) {
        this.codPresentacion = codPresentacion;
    }

    public String getPrecioSoles() {
        return precioSoles;
    }

    public void setPrecioSoles(String precioSoles) {
        this.precioSoles = precioSoles;
    }

    public String getPrecioDolares() {
        return precioDolares;
    }

    public void setPrecioDolares(String precioDolares) {
        this.precioDolares = precioDolares;
    }

    public String getStkDisponible() {
        return stkDisponible;
    }

    public void setStkDisponible(String stkDisponible) {
        this.stkDisponible = stkDisponible;
    }

    public String getStkFisico() {
        return stkFisico;
    }

    public void setStkFisico(String stkFisico) {
        this.stkFisico = stkFisico;
    }

    public String getCantidadBonificada() {
        return cantidadBonificada;
    }

    public void setCantidadBonificada(String cantidadBonificada) {
        this.cantidadBonificada = cantidadBonificada;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getFormaPromocion() {
        return formaPromocion;
    }

    public void setFormaPromocion(String formaPromocion) {
        this.formaPromocion = formaPromocion;
    }

    public String getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(String codDocumento) {
        this.codDocumento = codDocumento;
    }

    public String getOpcionSeleccion() {
        return opcionSeleccion;
    }

    public void setOpcionSeleccion(String opcionSeleccion) {
        this.opcionSeleccion = opcionSeleccion;
    }

    public String getPrecioRegularSoles() {
        return precioRegularSoles;
    }

    public void setPrecioRegularSoles(String precioRegularSoles) {
        this.precioRegularSoles = precioRegularSoles;
    }

    public String getPrecioRegularDolares() {
        return precioRegularDolares;
    }

    public void setPrecioRegularDolares(String precioRegularDolares) {
        this.precioRegularDolares = precioRegularDolares;
    }

    public String getValido() {
        return valido;
    }

    public void setValido(String valido) {
        this.valido = valido;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }
}
