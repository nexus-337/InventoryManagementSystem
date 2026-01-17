package com.alvin.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        OrderItem order = OrderItem.createNew(234323, 234, 39999);
        System.out.println(order.toString());
    }
}