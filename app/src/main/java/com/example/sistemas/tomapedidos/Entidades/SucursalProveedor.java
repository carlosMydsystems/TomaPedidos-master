package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class SucursalProveedor implements Serializable {

    private String codigoSucursalProveedor;
    private String nombreSucursalProveedor;
    private String direccionSucursalProveedor;

    public SucursalProveedor(String codigoSucursalProveedor, String nombreSucursalProveedor,
                             String direccionSucursalProveedor) {
        this.codigoSucursalProveedor = codigoSucursalProveedor;
        this.nombreSucursalProveedor = nombreSucursalProveedor;
        this.direccionSucursalProveedor = direccionSucursalProveedor;
    }

    public SucursalProveedor() {
    }

    public String getCodigoSucursalProveedor() {
        return codigoSucursalProveedor;
    }

    public void setCodigoSucursalProveedor(String codigoSucursalProveedor) {
        this.codigoSucursalProveedor = codigoSucursalProveedor;
    }

    public String getNombreSucursalProveedor() {
        return nombreSucursalProveedor;
    }

    public void setNombreSucursalProveedor(String nombreSucursalProveedor) {
        this.nombreSucursalProveedor = nombreSucursalProveedor;
    }

    public String getDireccionSucursalProveedor() {
        return direccionSucursalProveedor;
    }

    public void setDireccionSucursalProveedor(String direccionSucursalProveedor) {
        this.direccionSucursalProveedor = direccionSucursalProveedor;
    }
}
