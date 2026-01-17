package com.alvin.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import com.alvin.app.Exceptions.InsufficientStockException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Manager {
            private HashMap<Integer,Product> products;
            private HashMap<Integer,Order> orders;
            public Manager(){
                products = new HashMap<>();
                orders = new HashMap<>();
            }
            private void saveToFile() throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                String mapped = mapper.writeValueAsString(products);
                Files.writeString(Paths.get("productData/products.json") , mapped);
                mapped = mapper.writeValueAsString(orders);
                Files.writeString(Paths.get("orderData/orders.json") , mapped);
            }
            private void loadFromFile() throws  IOException{
                ObjectMapper mapper = new ObjectMapper();
                String temp = Files.readString(Paths.get("productData/products.json"));
                products =  mapper.readValue(temp, new TypeReference<HashMap<Integer, Product>>() {});
                temp = Files.readString((Paths.get("orderData/orders.json")));
                orders = mapper.readValue(temp, new TypeReference<HashMap<Integer, Order>>() {});
            }
            public OrderItem createOrderItem(int id , int quantity) throws InsufficientStockException {
                Product temp_product = products.get(id);
                if (temp_product.getStock_quantity() < quantity){
                    throw new InsufficientStockException("Insufficient stock for the specified product");
                }
                return OrderItem.createNew(id , quantity ,temp_product.getUnit_price());
            }

            public void newOrder(String customer_name , List<OrderItem> items) throws IOException{
                Order temp = Order.createNew(customer_name , items);
                orders.put(temp.getId(), temp);
                saveToFile();
            }
            public void deleteOrder(int id) throws IOException {
                orders.remove(id);
                saveToFile();
            }
            public void newProduct(Product.Category category , int unit_price , int init_stock , String name) throws IOException {
                Product temp_product = Product.createNew(category , name , unit_price , init_stock);
                products.put(temp_product.getId() , temp_product);
                saveToFile();
            }
            public void deleteProduct(int id) throws IOException {
                products.remove(id);
                saveToFile();
            }
            public void editProduct(int id , Product.Category category , int unit_price , int stock) throws IOException {
                Product temp_product = products.get(id);
                temp_product.setCategory(category);
                temp_product.setUnit_price(unit_price);
                temp_product.setStock_quantity(unit_price);
                saveToFile();
            }
}
