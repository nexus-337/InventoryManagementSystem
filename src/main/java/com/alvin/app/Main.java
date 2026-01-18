package com.alvin.app;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        printHeader();
        printHelp();
        String input;
        Manager manager = null;
        try {
            manager = new Manager();
        } catch (IOException e) {
            System.out.println("Error loading user files. Exiting application");
            return;
        }
        while (true){
            input = getStringFromUser(">>").toLowerCase().trim();
            switch (input){
                case "view products" , "vp" , "1":
                    System.out.println(manager.productsToString());
                    break;
                case "view products by id" ,"vpbi" , "vpi" , "2":


                case "view orders", "vo" , "3":
                    System.out.println(manager.ordersToString());
                    break;
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
        System.out.println("[10]  delete product      - Remove a product");
        System.out.println("[11]  delete order        - Remove an order");
        System.out.println("[12]  cancel order        - cancel an order");
        //cancel mean delete but add the items which was substracted for the order
        System.out.println("[13]  update stock        - Update the stock of a Product");

        System.out.println("\n--- TOOLS & UTILITIES ---");
        System.out.println("[14] sort products       - Sort products");
        System.out.println("[15] sort orders         - Sort orders");
        System.out.println("[16] backup              - Backup data to local storage");
        System.out.println("[17] restore             - restore data from local storage");
        System.out.println("[18] edit low stock      - restore data from local storage");
        System.out.println("[0]  exit                - Close the application");
        System.out.println("==================================================");
    }
}