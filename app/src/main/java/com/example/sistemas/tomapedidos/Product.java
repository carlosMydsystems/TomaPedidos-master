package com.example.sistemas.tomapedidos;

import org.json.JSONObject;

public class Product {

    public String ProductName;
    public Double ProductPrice;
    public String ProductImage;
    public int    CartQuantity=0;
    public String ProductIdArticulo;
    public String UnidadProducto;

    /*
    public Product(String productName, Double productPrice, String productImage) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductImage = productImage;
    }

    public Product(String productName, Double productPrice, String productImage, String productIdArticulo) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductImage = productImage;
        ProductIdArticulo = productIdArticulo;
    }
*/
    public Product(String productName, Double productPrice, String productImage, String productIdArticulo, String unidadProducto) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductImage = productImage;
        ProductIdArticulo = productIdArticulo;
        UnidadProducto = unidadProducto;
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
        }
        catch (Exception e) {}
        return cartItems.toString();
    }
}
