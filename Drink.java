package com.techelevator;

public class Drink extends Product {


    public Drink(String productName, String type, double price) {
        super(productName, type, price);
    }

   @Override
    public String getMessage() {
        return "Glug Glug, Yum!";
    }
}
