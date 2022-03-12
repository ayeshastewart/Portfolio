package com.techelevator;

public class Slot {
    private String productName;
    private int quantity = 5;
    private double price;
    private String slotNumber;
    private Product product;

    public Slot(String productName, String slotNumber, int quantity, double price, Product product){
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.slotNumber = slotNumber;
        this.product = product;
    }
    public Slot(){

    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void reduceQuantity() {
        quantity--;
    }
}

