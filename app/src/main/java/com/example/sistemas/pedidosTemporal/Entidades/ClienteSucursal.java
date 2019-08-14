package com.example.sistemas.pedidosTemporal.Entidades;

import java.io.Serializable;

public class ClienteSucursal implements Serializable {

    private String codSucursal;
    private String nombreSucursal;
    private String direccionSucursal;
    private String distrito;
    private String provincia;
    private String departamento;
    private String lineaCredito;
    private String creditoDisponible;
    private String canal;

    public ClienteSucursal(String codSucursal, String nombreSucursal, String direccionSucursal,
                           String distrito, String provincia, String departamento, String lineaCredito,
                           String creditoDisponible, String canal) {
        this.codSucursal = codSucursal;
        this.nombreSucursal = nombreSucursal;
        this.direccionSucursal = direccionSucursal;
        this.distrito = distrito;
        this.provincia = provincia;
        this.departamento = departamento;
        this.lineaCredito = lineaCredito;
        this.creditoDisponible = creditoDisponible;
        this.canal = canal;
    }

    public ClienteSucursal() {
    }

    public String getLineaCredito() {
        return lineaCredito;
    }

    public void setLineaCredito(String lineaCredito) {
        this.lineaCredito = lineaCredito;
    }

    public String getCreditoDisponible() {
        return creditoDisponible;
    }

    public void setCreditoDisponible(String creditoDisponible) {
        this.creditoDisponible = creditoDisponible;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getNombreSucursal() { return nombreSucursal; }

    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }

    public String getCodSucursal() {
        return codSucursal;
    }

    public void setCodSucursal(String codSucursal) {
        this.codSucursal = codSucursal;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) { this.direccionSucursal = direccionSucursal; }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
