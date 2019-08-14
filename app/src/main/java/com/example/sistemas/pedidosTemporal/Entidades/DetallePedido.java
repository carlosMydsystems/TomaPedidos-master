package com.example.sistemas.pedidosTemporal.Entidades;

import java.io.Serializable;

public class DetallePedido implements Serializable {

    private String nroPedido;
    private String codArticulo;
    private String articulo;
    private String undMedida;
    private String cantidad;
    private String precio;
    private String subtotal;


    public DetallePedido(String nroPedido, String codArticulo, String articulo, String undMedida,
                         String cantidad, String precio, String subtotal) {
        this.nroPedido = nroPedido;
        this.codArticulo = codArticulo;
        this.articulo = articulo;
        this.undMedida = undMedida;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    public DetallePedido() {
    }

    public String getNroPedido() {
        return nroPedido;
    }

    public void setNroPedido(String nroPedido) {
        this.nroPedido = nroPedido;
    }

    public String getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(String codArticulo) {
        this.codArticulo = codArticulo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getUndMedida() {
        return undMedida;
    }

    public void setUndMedida(String undMedida) {
        this.undMedida = undMedida;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
