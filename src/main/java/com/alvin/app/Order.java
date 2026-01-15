package com.alvin.app;

import com.alvin.app.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private String customer_name;
    private LocalDateTime order_date_time;
    private List<OrderItem> items = new ArrayList<OrderItem>();

    private Order(int id, String customer_name, LocalDateTime order_date_time, List<OrderItem> items) {
        this.id = id;
        this.customer_name = customer_name;
        this.order_date_time = order_date_time;
        this.items = items;
    }

    public static Order createNew(String customer_name) {
        IdGenerator gen = new IdGenerator(IdGenerator.IdOption.ORDER);
        return new Order(gen.getId(), customer_name, LocalDateTime.now(), new ArrayList<OrderItem>());
    }

    public static Order reCreate(int id, String customer_name, LocalDateTime order_date_time, List<OrderItem> items) {
        return new Order(id, customer_name, order_date_time, items);
    }
    public void addProduct(int product_id, int quantity){


    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public LocalDateTime getOrder_date_time() {
        return order_date_time;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public int getId() {
        return id;
    }
}