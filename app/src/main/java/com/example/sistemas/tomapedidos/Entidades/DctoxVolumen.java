package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class DctoxVolumen implements Serializable {

    private String rango;
    private String desde;
    private String hasta;
    private String descuento;
    private String precio;
    private String moneda;
    public DctoxVolumen() {
    }

    public String getMoneda() { return moneda; }

    public void setMoneda(String moneda) { this.moneda = moneda; }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
