package com.techelevator;

public class Chip extends Product{
    private String message;


    public Chip(String productName, String type, double price) {
        super(productName, type, price);
    }


    @Override
    public String getMessage() {
        return "Crunch Crunch, yum!";
    }

}
