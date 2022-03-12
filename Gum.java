package com.techelevator;

public class Gum extends Product{

    public Gum(String productName, String type, double price) {
        super(productName, type, price);
    }


    @Override
    public String getMessage() {
        return "Chew Chew, yum!";
    }
}
