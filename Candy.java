package com.techelevator;

public class Candy extends Product {

    public Candy(String productName, String type, double price) {
        super(productName, type, price);
    }

    @Override
    public String getMessage() {
        return "Munch, Munch, Yum!";
    }
}
