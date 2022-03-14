package com.techelevator;

public abstract class Product {

    public String productName;
    public String type;
    public double price;
    public int sales = 0;



    public Product(String productName, String type, double price){
        this.price = price;
        this.productName= productName;
        this.type = type;

    }

    public double getPrice() {
        return price;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }
    public abstract String getMessage();

    public void productSold(){
        sales++;
    }

    public int getSales() {
        return sales;
    }

}
