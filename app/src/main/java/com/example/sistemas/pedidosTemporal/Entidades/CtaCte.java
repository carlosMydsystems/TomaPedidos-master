package com.example.sistemas.pedidosTemporal.Entidades;

import java.io.Serializable;

public class CtaCte implements Serializable {

    private String tipoDocumento;
    private String numeroDocumento;
    private String fecha;
    private String moneda;
    private String importe;
    private String tienda;

    public CtaCte() {
    }

    public String getTipoDocumento() { return tipoDocumento; }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
}
