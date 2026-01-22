package com.alvin.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {
    private final int productId;
    private final String productName;
    private final int quantity;
    private final int unit_price;
    private final int total_price;
    @JsonCreator





    private OrderItem(@JsonProperty("productId") int productId , @JsonProperty("quantity") int quantity , @JsonProperty("unit_price") int unit_price , @JsonProperty("productName") String productName){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.total_price = unit_price * quantity;
    }
    public static OrderItem createNew(int productId , String productName , int quantity , int unit_price){
        return new OrderItem(productId , quantity , unit_price , productName);
    }
    public int getProductId(){
        return this.productId;
    }
    @Override
    public String toString(){
        return String.format("%-10d %-40s %-15d %-15d %-15d" , this.productId , this.productName , this.quantity , this.unit_price, this.total_price);
    }
    public String getProductName(){return this.productName;}
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


