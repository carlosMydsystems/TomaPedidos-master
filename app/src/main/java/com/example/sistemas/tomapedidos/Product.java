package com.example.sistemas.tomapedidos;

import org.json.JSONObject;

public class Product {

    public String ProductName;
    public Double ProductPrice;
    public String ProductImage;
    public int    CartQuantity=0;

    public Product(String productName, Double productPrice, String productImage) {
        ProductName = productName;
        ProductPrice = productPrice;
        ProductImage = productImage;
    }

    public String getJsonObject() {
        JSONObject cartItems = new JSONObject();
        try
        {
            cartItems.put("ProductName", ProductName);
            cartItems.put("ProductPrice", ProductPrice);
            cartItems.put("ProductImage",ProductImage);
            cartItems.put("CartQuantity",CartQuantity);
        }
        catch (Exception e) {}
        return cartItems.toString();
    }


}
