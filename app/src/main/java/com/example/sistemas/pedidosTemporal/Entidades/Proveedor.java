package com.example.sistemas.pedidosTemporal.Entidades;

import java.io.Serializable;

public class Proveedor implements Serializable {

    private String codProveedor;
    private String nombreProveedor;
    private String direccionProveedor;
    private String codDpto;
    private String departamento;
    private String codProv;
    private String provincia;
    private String codDistrito;
    private String Distrito;

    public Proveedor(String codProveedor, String nombreProveedor, String direccionProveedor,
                     String codDpto, String departamento, String codProv, String provincia,
                     String codDistrito, String distrito) {
        this.codProveedor = codProveedor;
        this.nombreProveedor = nombreProveedor;
        this.direccionProveedor = direccionProveedor;
        this.codDpto = codDpto;
        this.departamento = departamento;
        this.codProv = codProv;
        this.provincia = provincia;
        this.codDistrito = codDistrito;
        Distrito = distrito;
    }

    public Proveedor() {
    }

    public String getCodProveedor() { return codProveedor; }

    public void setCodProveedor(String codProveedor) { this.codProveedor = codProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }

    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    public String getDireccionProveedor() { return direccionProveedor; }

    public void setDireccionProveedor(String direccionProveedor) { this.direccionProveedor = direccionProveedor; }

    public String getCodDpto() { return codDpto; }

    public void setCodDpto(String codDpto) { this.codDpto = codDpto; }

    public String getDepartamento() { return departamento; }

    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getCodProv() { return codProv; }

    public void setCodProv(String codProv) { this.codProv = codProv; }

    public String getProvincia() { return provincia; }

    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCodDistrito() { return codDistrito; }

    public void setCodDistrito(String codDistrito) { this.codDistrito = codDistrito; }

    public String getDistrito() { return Distrito; }

    public void setDistrito(String distrito) { Distrito = distrito; }
}
