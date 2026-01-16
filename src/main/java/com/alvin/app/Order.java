package com.alvin.app;

import com.alvin.app.Exceptions.InsufficientStockException;
import com.alvin.app.utils.DeSerializer;
import com.alvin.app.utils.IdGenerator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int id;
    private String customer_name;
    private final LocalDateTime order_date_time;
    private final List<OrderItem> items;
    @JsonCreator
    private Order(@JsonProperty("id") int id,@JsonProperty("customer_name") String customer_name, @JsonProperty("order_date_time") LocalDateTime order_date_time,@JsonProperty("list") List<OrderItem> items) {
        this.id = id;
        this.customer_name = customer_name;
        this.order_date_time = order_date_time;
        this.items = items;
    }

    public static Order createNew(String customer_name) throws IOException{
        IdGenerator gen = new IdGenerator(IdGenerator.IdOption.ORDER);
        return new Order(gen.getId(), customer_name, LocalDateTime.now(), new ArrayList<>());
    }
    public void addProduct (int product_id, int quantity) throws InsufficientStockException , IOException {
        Product temp = DeSerializer.readClass(Paths.get("Paths/" + product_id + ".json") , DeSerializer.class_type.PRODUCT);
        if(temp == null){
            throw new NullPointerException("Deserialized product is a null from add product");
        }
        if (temp.getStock_quantity() < quantity){
            throw new InsufficientStockException("Insufficient stock for the specified product");
        }
        items.add(OrderItem.createNew(temp.getId() , quantity , temp.getUnit_price()));

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