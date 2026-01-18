package com.alvin.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import com.alvin.app.Exceptions.InsufficientStockException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;

public class Manager {
            private HashMap<Integer,Product> products;
            private HashMap<Integer,Order> orders;
            public Manager() throws IOException {
                products = new HashMap<>();
                orders = new HashMap<>();
                loadFromFile();
            }
            private enum file_paths{
                PRODUCT(Paths.get("productData/products.json")) , ORDER(Paths.get("orderData/orders.json"));

                private final Path file_path;
                file_paths(Path file_path){
                    this.file_path = file_path;
                }
                public Path getFilePath(){
                    return file_path;
                }
            }
            private void saveToFile() throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                Files.createDirectories(file_paths.PRODUCT.getFilePath());
                Files.createDirectories(file_paths.ORDER.getFilePath());
                String mapped = mapper.writeValueAsString(products);
                Files.writeString(file_paths.PRODUCT.getFilePath() , mapped);
                mapped = mapper.writeValueAsString(orders);
                Files.writeString(file_paths.ORDER.getFilePath(), mapped);
            }
            private void loadFromFile() throws  IOException{
                ObjectMapper mapper = new ObjectMapper();
                String temp;
                if(Files.exists(file_paths.PRODUCT.getFilePath().getParent())){
                    temp = Files.readString(file_paths.PRODUCT.getFilePath());
                    products =  mapper.readValue(temp, new TypeReference<HashMap<Integer, Product>>() {});
                }
                if(Files.exists(file_paths.ORDER.getFilePath().getParent())){
                    temp = Files.readString(file_paths.ORDER.getFilePath());
                    orders = mapper.readValue(temp, new TypeReference<HashMap<Integer, Order>>() {});
                }
            }
            public String productsToString(){
                StringBuilder builder = new StringBuilder();
                for (Product temp_product:products.values()){
                    builder.append(temp_product.toString()).append("\n");
                }
                return builder.toString();
            }
            public String productToString(int id ){
                Product temp_product = products.get(id);
                String str = "\nId: " + temp_product.getId() +
                        "\nName: " + temp_product.getName() +
                        "\nCategory: " + temp_product.getCategory().toString() +
                        "\nPrice: " + temp_product.getUnit_price() +
                        "\nStock: " + temp_product.getStock_quantity();
                return str;
            }
            public String orderToString(int id){
                Order temp_order = orders.get(id);
                List<OrderItem> temp_list = temp_order.getItems();
                StringBuilder builder = new StringBuilder();
                for(OrderItem item : temp_list){
                    builder.append(item.toString()).append("\n");
                }
                String str = "\nId: " + temp_order.getId() +
                        "\nCustomer Name: " + temp_order.getCustomer_name() +
                        "\nOrder Time: " + temp_order.getOrder_date_time().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a")) +
                        "\nNo of Items: " + temp_list.size() +
                        "\n----Items-----\n" + builder.toString();
                return str;
            }
            public String zeroStockProductToString(){
                StringBuilder builder = new StringBuilder();
                for (Product temp_product : products.values()){
                    if (temp_product.getStock_quantity() == 0){
                        builder.append(temp_product.toString()).append("\n");
                    }
                }
                return builder.toString();
            }
            public String lowStockProductToString() throws IOException {
                StringBuilder builder = new StringBuilder();
                for (Product temp_product : products.values()){
                    if (temp_product.getStock_quantity() < getLowStockConstant()){
                        builder.append(temp_product.toString()).append("\n");
                    }
                }
                return builder.toString();
            }
            public void setLowStockConstant(int low_const) throws IOException {
                Files.writeString(Paths.get("config/lowStock.conf") , Integer.toString(low_const));
            }

            private int getLowStockConstant() throws IOException {
                return Integer.parseInt(Files.readString(Paths.get("config/lowStock.conf")));
            }

            public String ordersToString(){

                StringBuilder builder = new StringBuilder();
                for (Order temp_order:orders.values()){
                    builder.append(temp_order.toString()).append("\n");
                }
                return builder.toString();
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
            public void editCategoryofProduct(int id , Product.Category category) throws IOException {
                Product temp_product = products.get(id);
                temp_product.setCategory(category);
                saveToFile();
            }
            public void editNameofProduct(int id , String name) throws IOException {
                Product temp_product = products.get(id);
                temp_product.setName(name);
                saveToFile();
            }
            public void editUnitPriceofProduct(int id , int unit_price) throws IOException {
                Product temp_product = products.get(id);
                temp_product.setUnit_price(unit_price);
                saveToFile();
            }
            public void editStockofProduct(int id , int stock) throws IOException {
                Product temp_product = products.get(id);
                temp_product.setStock_quantity(stock);
                saveToFile();
            }
            public void sort
}
