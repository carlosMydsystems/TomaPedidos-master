package com.example.sistemas.pedidosTemporal;

import org.json.JSONObject;

public class Product {

    public String ProductName;
    public Double ProductPrice;
    public String ProductImage;
    public int    CartQuantity=0;
    public String ProductIdArticulo;
    public String UnidadProducto;
    public String TasaDescuento;
    public String Presentacion;
    public String Equivalencia;
    public String PrecioUni;
    public String ObservacionSeleccion;
    public String Stock;

    public Product(String productName, Double productPrice, String productImage,
                   String productIdArticulo, String unidadProducto, String tasaDescuento,
                   String presentacion, String equivalencia, String precioUni,
                   String observacionSeleccion, String stock) {

        ProductName = productName;
        ProductPrice = productPrice;
        ProductImage = productImage;
        ProductIdArticulo = productIdArticulo;
        UnidadProducto = unidadProducto;
        TasaDescuento = tasaDescuento;
        Presentacion = presentacion;
        Equivalencia = equivalencia;
        PrecioUni = precioUni;
        ObservacionSeleccion = observacionSeleccion;
        Stock = stock;

    }

    public Product() {

    }

    public String getJsonObject() {
        JSONObject cartItems = new JSONObject();
        try
        {

            cartItems.put("ProductName", ProductName);
            cartItems.put("ProductPrice", ProductPrice);
            cartItems.put("ProductImage",ProductImage);
            cartItems.put("CartQuantity",CartQuantity);
            cartItems.put("ProductIdArticulo",ProductIdArticulo);
            cartItems.put("unidadproducto",UnidadProducto);
            cartItems.put("tasadescuento",TasaDescuento);
            cartItems.put("Presentacion",Presentacion);
            cartItems.put("Equivalencia",Equivalencia);
            cartItems.put("PrecioUni",PrecioUni);
            cartItems.put("ObservacionSeleccion",ObservacionSeleccion);
            cartItems.put("Stock",Stock);

        }

        catch (Exception e) {}
        return cartItems.toString();
    }
}
