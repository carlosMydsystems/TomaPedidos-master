package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class Clientes implements Serializable {

    private String idCliente;
    private String nombre;
    private String codCliente;
    private String direccion;
    private String giro;
    private String tipoCliente;
    private String deuda;
    private String estado;
    private String fechaultpedido;
    private String usuarioultpedido;

    public Clientes(String idCliente, String nombre, String codCliente, String direccion, String giro,
                    String tipoCliente, String deuda, String estado, String fechaultpedido,
                    String usuarioultpedido) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.codCliente = codCliente;
        this.direccion = direccion;
        this.giro = giro;
        this.tipoCliente = tipoCliente;
        this.deuda = deuda;
        this.estado = estado;
        this.fechaultpedido = fechaultpedido;
        this.usuarioultpedido = usuarioultpedido;
    }

    public Clientes() {
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getDeuda() {
        return deuda;
    }

    public void setDeuda(String deuda) {
        this.deuda = deuda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaultpedido() {
        return fechaultpedido;
    }

    public void setFechaultpedido(String fechaultpedido) {
        this.fechaultpedido = fechaultpedido;
    }

    public String getUsuarioultpedido() {
        return usuarioultpedido;
    }

    public void setUsuarioultpedido(String usuarioultpedido) {
        this.usuarioultpedido = usuarioultpedido;
    }
}
