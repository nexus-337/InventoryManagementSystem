package com.alvin.app;

import com.alvin.app.Exceptions.IdNotFoundException;
import com.alvin.app.Exceptions.InsufficientStockException;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        printHeader();
        printHelp();
        String input;
        Manager manager;
        try {
            manager = new Manager();
        } catch (IOException e) {
            System.out.println("Error loading user files. Exiting application");
            return;
        }


        while (true){
            input = getStringFromUser(">>").toLowerCase().trim();
            switch (input){
                case "view products" , "vp" , "1" , "view product":
                    System.out.println();
                    System.out.printf("\u001B[1m\u001B[34m%-10s %-40s %-20s %-15s %-15s\u001B[0m\u001B[1m%n", "ID", "NAME", "CATEGORY" ,"UNIT PRICE" , "STOCK");
                    System.out.println(manager.productsToString());
                    break;

                case "view product by id" ,"vpbi" , "vpi" , "2":
                    int productid = getIntFromUser("Enter the id of the product: ");
                    System.out.println();
                    System.out.println(manager.productToString(productid));
                    System.out.println();

                case "view orders", "vo" , "3" , "view order" :
                    System.out.println();
                    System.out.printf("\u001B[1m\u001B[34m%-10s %-40s %-15s %-20s\u001B[0m\u001B[1m\n" , "ID" , "Customer Name" , "No of items" , "Order Date & Time");
                    System.out.println(manager.ordersToString());
                    break;
                case "view order by id", "vobi" , "4":
                    int orderid = getIntFromUser("Enter the id of the order: ");
                    System.out.println();
                    System.out.println(manager.orderToString(orderid));
                    break;

                case "view zero stock" , "vzs" , "5":
                    System.out.println();
                    System.out.printf("\u001B[1m\u001B[34m%-10s %-40s %-20s %-15s %-15s\u001B[0m\u001B[1m%n", "ID", "NAME", "CATEGORY" ,"UNIT PRICE" , "STOCK");
                    System.out.println(manager.zeroStockProductToString());
                    break;
                case "view low stock" , "vls" , "6":
                    System.out.printf("\u001B[1m\u001B[34m%-10s %-40s %-20s %-15s %-15s\u001B[0m\u001B[1m%n", "ID", "NAME", "CATEGORY" ,"UNIT PRICE" , "STOCK");
                    try {
                        System.out.println(manager.lowStockProductToString());
                    }
                    catch (IOException e) {
                        System.err.println("Error in reading/writing files");
                        continue;
                    }
                    break;
                case "new product" , "np" , "7":
                    Product.Category inp_category = Product.Category.OTHER;
                    String temp_name;
                    int inp_unitprice;
                    int inp_stock_quantity;
                    int inp;
                    do {
                        inp = getIntFromUser("Enter the category of the product:\n" +
                                "1 = Laptop\n" +
                                "2 = Smartphone\n" +
                                "3 = Headphone\n" +
                                "4 = Laptop Accessory\n" +
                                "5 = Mobile Accessory\n" +
                                "6 = Speaker\n" +
                                "7 = Other ");
                        switch (inp){
                            case 1:
                                inp_category = Product.Category.LAPTOP;
                                break;
                            case 2:
                                inp_category = Product.Category.SMARTPHONE;
                                break;
                            case 3:
                                inp_category = Product.Category.HEADPHONE;
                                break;
                            case 4:
                                inp_category = Product.Category.LAPTOP_ACCESSORY;
                                break;
                            case 5:
                                inp_category = Product.Category.MOBILE_ACCESSORY;
                                break;
                            case 6:
                                inp_category = Product.Category.SPEAKER;
                                break;
                            case 7:
                                inp_category = Product.Category.OTHER;
                                break;
                            default:
                                System.out.println("Enter integer from 1 to 7");
                                continue;
                        }
                    }while(inp >= 7 || inp <= 1);
                    temp_name = getStringFromUser("Enter the name of the product: ");
                    inp_unitprice = getIntFromUser("Enter the unit price of product: ");
                    inp_stock_quantity = getIntFromUser("Enter the initial stock quantity of the product: ");
                    try {
                        manager.newProduct(inp_category , inp_unitprice , inp_stock_quantity , temp_name);
                        System.out.println("New product added");
                    } catch (IOException e) {
                        System.err.println("Error in reading/writin files");
                        continue;
                    }
                case "new order" , "no" , "8":
                    String inp_cust_name = getStringFromUser("Enter the customer name: ");
                    List<OrderItem> inp_order_items = new ArrayList<>();
                    OrderItem temp_order_item;
                    int temp_productid , temp_quantity;
                    boolean n = true;
                    while(n){
                        System.out.println("Enter the product item details to add\nput 0 to stop adding products");
                        System.out.println();
                        while(true){
                            temp_productid = getIntFromUser("Enter the product id: ");
                            if (temp_productid == 0)break;
                            temp_quantity = getIntFromUser("Enter the quantity: ");
                            try {
                                temp_order_item = manager.createOrderItem(temp_productid , temp_quantity);
                                inp_order_items.add(temp_order_item);
                                n =false;
                                break;
                            } catch (InsufficientStockException e) {
                                System.err.println("The given product have insufficient stock");
                            } catch (IdNotFoundException e) {
                                System.err.println("The given product id not found");
                            }
                        }
                    }
                    try {
                        manager.newOrder(inp_cust_name , inp_order_items);
                    } catch (IOException e) {
                        System.err.println("Error in reading/writing files");
                    }
                    break;
                case "edit product" , "ep" , "9":
                    int input_n , input_id;
                    String name;
                    input_id = getIntFromUser("Enter the id of the product you want to edit: ");
                    if (!manager.isProduct(input_id)){
                        System.err.println("Product doesnt exist");
                        continue;
                    }
                    do {
                        input_n = getIntFromUser("Enter which property to edit\n(1 - Name | 2 - Category | 3 - Unit Price | 4 - Stock Quantity): ");
                    }while(input_n >= 4 || input_n <= 1);
                    switch (input_n){
                        case 1:
                           name = getStringFromUser("Enter the new name of product: ");
                            try {
                                manager.editNameofProduct(input_n , name);
                            } catch (IOException e) {
                                System.err.println("Error in reading/writing files");
                            } catch (IdNotFoundException e) {
                                System.err.println("Product doesnt exist");
                            }
                            break;
                        case 2:
                            Product.Category input_category = Product.Category.OTHER;
                            input_n = getIntFromUser("Enter the id of the product: ");
                            if (!manager.isProduct(input_id)){
                                System.err.println("Product doesnt exist");
                                continue;
                            }
                            do {
                                inp = getIntFromUser("Enter the new category of the product:\n" +
                                        "1 = Laptop\n" +
                                        "2 = Smartphone\n" +
                                        "3 = Headphone\n" +
                                        "4 = Laptop Accessory\n" +
                                        "5 = Mobile Accessory\n" +
                                        "6 = Speaker\n" +
                                        "7 = Other ");
                                switch (inp){
                                    case 1:
                                        input_category = Product.Category.LAPTOP;
                                        break;
                                    case 2:
                                        input_category = Product.Category.SMARTPHONE;
                                        break;
                                    case 3:
                                        input_category = Product.Category.HEADPHONE;
                                        break;
                                    case 4:
                                        input_category = Product.Category.LAPTOP_ACCESSORY;
                                        break;
                                    case 5:
                                        input_category = Product.Category.MOBILE_ACCESSORY;
                                        break;
                                    case 6:
                                        input_category = Product.Category.SPEAKER;
                                        break;
                                    case 7:
                                        input_category = Product.Category.OTHER;
                                        break;
                                    default:
                                        System.out.println("Enter integer from 1 to 7");
                                        break;
                                }
                            }while(inp >= 7 || inp <= 1);
                            try {
                                manager.editCategoryofProduct(input_n , input_category);
                            } catch (IOException e) {
                                System.err.println("Error in reading/writing files");
                            }
                            break;

                        case 3:
                            input_n = getIntFromUser("Enter the id of product you want to edit: ");
                            if(!manager.isProduct(input_n)){
                                System.err.println("Product doesnt exist");
                                continue;
                            }
                            int inp_n = getIntFromUser("Enter the value of new Unit price: ");
                            try {
                                manager.editUnitPriceofProduct(input_n , inp_n);
                            } catch (IOException e) {
                                System.err.println("Error in reading/writing files");
                            } catch (IdNotFoundException e) {
                                System.err.println("Product doesnt exist");
                            }


                        case 4:
                            input_n = getIntFromUser("Enter the id of product you want to edit: ");
                            if(!manager.isProduct(input_n)){
                                System.err.println("Product doesnt exist");
                                continue;
                            }
                            inp_n = getIntFromUser("Enter the value of new Stock quantity: ");
                            try {
                                manager.editStockofProduct(input_n , n);
                            } catch (IOException e) {
                                System.err.println("Error in reading/writing files");
                            } catch (IdNotFoundException e) {
                                System.err.println("Product doesnt exist");
                            }
                    }
                case "delete product" , "dp" , "10":
                    input_n = getIntFromUser("Enter the id of the product you want to delete: ");
                    if(!manager.isProduct(input_n)){
                        System.err.println("Product doesnt exist");
                        continue;
                    }
                    if(getIntFromUser("Press 1 for confirm deletion of specified product: ") == 1){
                        try {
                            manager.deleteProduct(input_n);
                        } catch (IOException e) {
                            System.err.println("Error in reading/writing files");
                        }
                    }
                    else{
                        System.out.println("Cancelled deletion of Product");
                    }
                    break;
                case "delete order" , "do" , "11":
                    input_n = getIntFromUser("Enter the id of the order you want to delete: ");
                    if(!manager.isOrder(input_n)){
                        System.err.println("order doesnt exist");
                        continue;
                    }
                    if(getIntFromUser("Press 1 for confirm deletion of specified product: ") == 1){
                        try {
                            manager.deleteOrder(input_n);
                            System.out.println("Deleted specified Product");
                        } catch (IOException e) {
                            System.err.println("Error in reading/writing files");
                        }
                    }
                    else{
                        System.out.println("Cancelled deletion of Order");
                    }
                    break;


                case "cancel order" , "co" , "12":
                    input_n = getIntFromUser("Enter the id of the order you want to delete: ");
                    if(!manager.isOrder(input_n)){
                        System.err.println("order doesnt exist");
                        continue;
                    }
                    if(getIntFromUser("Press 1 for confirm deletion of specified product: ") == 1){
                        try {
                            manager.cancelOrder(input_n);
                            System.out.println("Cancelled specified Product");
                        } catch (IOException e) {
                            System.err.println("Error in reading/writing files");
                        }
                    }
                    else{
                        System.out.println("Cancelled cancellation of Order");
                    }
                    break;

                case "update stock" , "us" , "13":
                    input_n = getIntFromUser("Enter the id of the order you want to delete: ");
                    String inp_str;
                    if(!manager.isOrder(input_n)){
                        System.err.println("order doesnt exist");
                        continue;
                    }
                    while (true){
                        inp_str = getStringFromUser("Enter if you want to add or remove stock: ").toLowerCase().trim();
                        if (inp_str == "add"){
                            inp = getIntFromUser("Enter the amount you want to add: ");
                            if (inp < 0){
                                System.err.println("Cannot add negative amount");
                                continue;
                            }
                            manager.
                        }

                    }

                case "sort products" , "sp" , "14":

                case "sort orders" , "so" , "15":
                case "edit low stock" , "els" , "16":
                case "exit" , "e" , "0":
            }
        }
    }
    private static int getIntFromUser(String message){
        Scanner scanner = new Scanner(System.in);
        int input;
        while(true) {
            try {
                System.out.print(message);
                input = scanner.nextInt();
                scanner.nextLine();
                return input;
            }
            catch (InputMismatchException e) {
                System.out.println("Enter an integer");
            }
        }

    }
    private static String getStringFromUser(String message){
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true) {
            System.out.print(message);
            input = scanner.nextLine();
            if (input.isEmpty()){
                System.out.println("Input cannot be blank.");
                continue;
            }
            return input;
        }
    }
















    private static void printHeader(){

        System.out.println("==================================================");
        System.out.println("       INVENTORY MANAGEMENT SYSTEM - v1.0         ");
        System.out.println("               By Alvin                           ");
        System.out.println("==================================================");
    }

    public static void printHelp(){
//        System.out.println("==================================================");
//        System.out.println("                     HELP                         ");
//        System.out.println("==================================================");
        System.out.println("Usage:Enter the [Code],[Command] or [Initials]");
        System.out.println("\n--- VIEW COMMANDS ---");
        System.out.println("[1]  view products       - Display all Products");
        System.out.println("[2]  view product by id  - Display all Products");
        System.out.println("[3]  view orders         - Display all Orders");
        System.out.println("[4]  view order by id    - Display a specific order by ID");
        System.out.println("[5]  view zero stock     - Show products with 0 stock");
        System.out.println("[6]  view low stock      - Show products with low(configurable) stock");

        System.out.println("\n--- MANAGEMENT COMMANDS ---");
        System.out.println("[7]  new product         - Add a new item");
        System.out.println("[8]  new order           - Create a new order");
        System.out.println("[9]  edit product        - Modify existing product details");
        System.out.println("[10]  delete product     - Remove a product");
        System.out.println("[11]  delete order       - Remove an order");
        System.out.println("[12]  cancel order       - cancel an order");
        //cancel mean delete but add the items which was substracted for the order
        System.out.println("[13]  update stock        - Update the stock of a Product");

        System.out.println("\n--- TOOLS & UTILITIES ---");
        System.out.println("[14] sort products       - Sort products");
        System.out.println("[15] sort orders         - Sort orders");
        System.out.println("[16] backup              - Backup data to local storage");
        System.out.println("[17] restore             - restore data from local storage");
        System.out.println("[18] edit low stock      - Edit low stock constant");
        System.out.println("[0]  exit                - Close the application");
        System.out.println("==================================================");
    }
}