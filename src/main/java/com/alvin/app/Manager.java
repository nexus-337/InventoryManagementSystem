package com.alvin.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.alvin.app.Exceptions.IdNotFoundException;
import com.alvin.app.Exceptions.InsufficientStockException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Manager {
            private HashMap<Integer,Product> products;
            private HashMap<Integer,Order> orders;
            private static final int default_low_stock = 10;
            private ProductSortOption product_sort_option = ProductSortOption.DEFAULT;
            private OrderSortOption order_sort_option = OrderSortOption.DEFAULT;
            public Manager() throws IOException {
                products = new HashMap<>();
                orders = new HashMap<>();
                loadFromFile();
            }
            public enum ProductSortOption {
                NAME , PRICE , STOCK , DEFAULT
            }
            public enum OrderSortOption{
                NAME, NO_ITEMS , DATE , DEFAULT
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
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                Files.createDirectories(file_paths.PRODUCT.getFilePath().getParent());
                Files.createDirectories(file_paths.ORDER.getFilePath().getParent());
                String mapped = mapper.writeValueAsString(products);
                Files.writeString(file_paths.PRODUCT.getFilePath() , mapped);
                mapped = mapper.writeValueAsString(orders);
                Files.writeString(file_paths.ORDER.getFilePath(), mapped);
            }
            private void loadFromFile() throws  IOException{
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                String temp;
                if(Files.exists(file_paths.PRODUCT.getFilePath())){
                    temp = Files.readString(file_paths.PRODUCT.getFilePath());
                    products =  mapper.readValue(temp, new TypeReference<HashMap<Integer, Product>>() {});
                }
                if(Files.exists(file_paths.ORDER.getFilePath())){
                    temp = Files.readString(file_paths.ORDER.getFilePath());
                    orders = mapper.readValue(temp, new TypeReference<HashMap<Integer, Order>>() {});
                }
            }
            public String productsToString(){
                StringBuilder builder = new StringBuilder();
                if(product_sort_option == ProductSortOption.DEFAULT) {
                    for (Product temp_product : products.values()) {
                        builder.append(temp_product.toString()).append("\n");
                    }
                }
                else if(product_sort_option == ProductSortOption.NAME){
                    for (Product temp_product : products.values().stream().sorted(Comparator.comparing(Product::getName)).toList()){
                        builder.append(temp_product.toString()).append("\n");
                    }
                }
                else if (product_sort_option == ProductSortOption.PRICE){
                    for (Product temp_product : products.values().stream().sorted(Comparator.comparing(Product::getUnit_price)).toList()){
                        builder.append(temp_product.toString()).append("\n");
                    }
                }
                else if (product_sort_option == ProductSortOption.STOCK){
                    for (Product temp_product : products.values().stream().sorted(Comparator.comparing(Product::getUnit_price)).toList()){
                        builder.append(temp_product.toString()).append("\n");
                    }
                }
                return builder.toString();
            }

            public String productToString(int id ){
                Product temp_product = products.get(id);
                return "\nId: " + temp_product.getId() +
                        "\nName: " + temp_product.getName() +
                        "\nCategory: " + temp_product.getCategory().toString() +
                        "\nPrice: " + temp_product.getUnit_price() +
                        "\nStock: " + temp_product.getStock_quantity();
            }
            public String orderToString(int id){
                Order temp_order = orders.get(id);
                List<OrderItem> temp_list = temp_order.getItems();
                StringBuilder builder = new StringBuilder();
                for(OrderItem item : temp_list){
                    builder.append(item.toString()).append("\n");
                }
                return "\nId: " + temp_order.getId() +
                        "\nCustomer Name: " + temp_order.getCustomer_name() +
                        "\nOrder Time: " + temp_order.getOrder_date_time().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a")) +
                        "\nNo of Items: " + temp_list.size() +
                        "\n----Items-----\n" + builder;
            }

            public String ordersToString(){
                StringBuilder builder = new StringBuilder();
                if(product_sort_option == ProductSortOption.DEFAULT) {
                    for (Order temp_order : orders.values()) {
                        builder.append(temp_order.toString()).append("\n");
                    }
                    return builder.toString();
                }
                else if (product_sort_option == ProductSortOption.NAME){

                    for (Order temp_order : orders.values().stream().sorted(Comparator.comparing(Order::getCustomer_name)).toList()){
                        builder.append(temp_order.toString()).append("\n");
                    }
                }
                return builder.toString();
            }
            public String zeroStockProductToString(){
                StringBuilder builder = new StringBuilder();
                for (Product temp_product : products.values()){
                    if (temp_product.getStock_quantity() == 0){
                        builder.append(temp_product).append("\n");
                    }
                }
                return builder.toString();
            }
            public String lowStockProductToString() throws IOException {
                StringBuilder builder = new StringBuilder();
                for (Product temp_product : products.values()){
                    if (temp_product.getStock_quantity() < getLowStockConstant()){
                        builder.append(temp_product).append("\n");
                    }
                }
                return builder.toString();
            }
            public void setLowStockConstant(int low_const) throws IOException {
                Files.writeString(Paths.get("config/lowStock.conf") , Integer.toString(low_const));
            }

            private int getLowStockConstant() throws IOException {
                if(Files.exists(Paths.get("config/lowStock.conf"))){
                    setLowStockConstant(default_low_stock);
                    return default_low_stock;
                }
                return Integer.parseInt(Files.readString(Paths.get("config/lowStock.conf")));
            }


            public OrderItem createOrderItem(int id , int quantity) throws InsufficientStockException , IdNotFoundException{
                Product temp_product = products.get(id);
                if (temp_product == null){
                    throw new IdNotFoundException("Id not found in the hashmap");
                }
                if (temp_product.getStock_quantity() < quantity){
                    throw new InsufficientStockException("Insufficient stock for the specified product");
                }
                return OrderItem.createNew(id , temp_product.getName(), quantity ,temp_product.getUnit_price());
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
            public void cancelOrder(int id) throws  IOException{
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
            public void editNameofProduct(int id , String name) throws IOException, IdNotFoundException {
                Product temp_product = products.get(id);
                if (temp_product == null){
                    throw new IdNotFoundException("Id not found in the hashmap");
                }
                temp_product.setName(name);
                saveToFile();
            }
            public void editUnitPriceofProduct(int id , int unit_price) throws IOException, IdNotFoundException {
                Product temp_product = products.get(id);
                if (temp_product == null){
                    throw new IdNotFoundException("Id not found in the hashmap");
                }
                temp_product.setUnit_price(unit_price);
                saveToFile();
            }
            public void editStockofProduct(int id , int stock) throws IOException, IdNotFoundException {
                Product temp_product = products.get(id);
                if (temp_product == null){
                    throw new IdNotFoundException("Id not found in the hashmap");
                }
                temp_product.setStock_quantity(stock);
                saveToFile();
            }
            public void updateStockOfProduct(int id , String choice , int value) throws IdNotFoundException {
                Product temp_product = products.get(id);
                if(temp_product == null){
                    throw new IdNotFoundException("Given product id not found for update stock of product");
                }

                if(choice.equals("add")){
                    temp_product.setStock_quantity(temp_product.getStock_quantity() + value);
                }
                else if (choice.equals("remove")){
                    temp_product.setStock_quantity(temp_product.getStock_quantity() - value);
                }
            }
            public void sortProduct(ProductSortOption option){
                product_sort_option = option;
            }
            public void sortOrder(OrderSortOption option){
                order_sort_option = option;
            }
            public boolean isProduct(int id){
                return products.containsKey(id);
            }
            public boolean isOrder(int id){
                return orders.containsKey(id);
            }

}
