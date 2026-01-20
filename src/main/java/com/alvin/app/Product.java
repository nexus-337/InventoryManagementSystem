package com.alvin.app;
import com.alvin.app.utils.IdGenerator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;

public class Product {
    private final int id;
    private Category category;
    private String name;
    private int unit_price;
    private int stock_quantity;

    public enum Category{LAPTOP , SMARTPHONE , HEADPHONE , LAPTOP_ACCESSORY
        , MOBILE_ACCESSORY, SPEAKER , OTHER}

    @JsonCreator
    private Product(@JsonProperty("id") int id,@JsonProperty("category") Category category ,@JsonProperty("unit_price") int unit_price ,@JsonProperty("stock_quantity") int stock_quantity , @JsonProperty("name") String name) {
        this.id = id;
        this.category = category;
        this.unit_price = unit_price;
        this.stock_quantity = stock_quantity;
        this.name = name;
    }
    public static Product createNew(Category category , String name ,  int unit_price , int init_stock_quantity ) throws IOException {
        IdGenerator gen = new IdGenerator(IdGenerator.IdOption.PRODUCT);
        return new Product(gen.getId() , category , unit_price , init_stock_quantity , name);
    }
    @Override
    public String toString(){
        return String.format("%-10d %-40s %-20s %-15d %-15d" , this.id , this.name , this.category.toString() , this.unit_price , this.stock_quantity);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public Category getCategory() {
        return category;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public int getId() {
        return id;
    }
}
