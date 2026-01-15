package com.alvin.app;
import com.alvin.app.utils.IdGenerator;

public class Product {
    public final int id;
    Category category;
    int unit_price;
    int stock_quantity;

    public Product(int id, Category category , int unit_price , int stock_quantity) {
        this.id = id;
        this.category = category;
        this.unit_price = unit_price;
        this.stock_quantity = stock_quantity;
    }
    public static Product createNew(Category category , int unit_price , int init_stock_quantity){
        IdGenerator gen = new IdGenerator(IdGenerator.IdOption.PRODUCT);
        return new Product(gen.getId() , category , unit_price , init_stock_quantity);
    }
    public static Product reCreate(int id , Category category , int unit_price , int init_stock_quantity){
        return new Product(id, category, unit_price , init_stock_quantity);
    }
    public enum Category{LAPTOP , SMARTPHONE , HEADPHONE , LAPTOP_ACCESSORY
                         , MOBILE_ACCESSORY, SPEAKER , BLUETOOTH_SPEAKER}
}
