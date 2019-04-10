package com.example.sistemas.tomapedidos.Entidades;

import java.io.Serializable;

public class Stock implements Serializable {

    private String codalmacen;
    private String stockDisponible;

    public Stock(String codalmacen, String stockDisponible) {
        this.codalmacen = codalmacen;
        this.stockDisponible = stockDisponible;
    }

    public Stock() {
    }

    public String getCodalmacen() {
        return codalmacen;
    }

    public void setCodalmacen(String codalmacen) {
        this.codalmacen = codalmacen;
    }

    public String getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(String stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
}
