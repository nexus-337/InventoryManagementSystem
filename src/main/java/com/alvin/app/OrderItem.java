package com.alvin.app;

public class OrderItem {
    private final int productId;
    private final int quantity;
    private final int unit_price;
    private final int total_price;

    private OrderItem(int productId , int quantity , int unit_price){
        this.productId = productId;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_price = unit_price * quantity;
    }
    public static OrderItem createNew(int productId , int quantity , int unit_price){
        return new OrderItem(productId , quantity , unit_price);
    }
    public int getProductId(){
        return this.productId;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public int getUnit_price(){
        return this.unit_price;
    }
    public int getTotal_price(){
        return this.total_price;
    }

}


