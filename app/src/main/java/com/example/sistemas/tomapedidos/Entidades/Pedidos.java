package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class Pedidos implements Serializable {

    private String idPedido;
    private String cliente;
    private String fecha;
    private String sucursalCliente;

    public Pedidos(String idPedido, String cliente, String fecha, String sucursalCliente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fecha = fecha;
        this.sucursalCliente = sucursalCliente;
    }

    public Pedidos() {
    }

    public String getSucursalCliente() {
        return sucursalCliente;
    }

    public void setSucursalCliente(String sucursalCliente) {
        this.sucursalCliente = sucursalCliente;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
