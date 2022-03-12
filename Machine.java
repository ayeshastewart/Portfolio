package com.techelevator;

import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Machine {
    private double funds = 0;
    private double total = 0;
    private Inventory inventory;

    Money moneyObject = new Money(funds);
    Map<String, Integer> salesReportMap = new TreeMap<>();
    public Machine(Inventory inventory){
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public double getFunds() {
        BigDecimal bigDecimal = new BigDecimal(funds);
        bigDecimal = moneyObject.getMoneyBigDecimal(funds).setScale(2, RoundingMode.HALF_UP);
        funds = bigDecimal.doubleValue();
        return funds;
    }

    public double getTotal(){
        return total;
    }


    File newFile = new File("log.txt");

    public double feedMoney(double money){
        funds += money;
        String message = getCurrentTimeAsString(format) + " FEED MONEY: " + String.format("$%,.2f", money) + " " + String.format("$%,.2f", funds) + "\n";
        boolean append = newFile.exists() ? true : false;
        try (PrintWriter writer =
                     new PrintWriter(new FileOutputStream(newFile, append))) {
            writer.append(message);
        }catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return funds;
    }

    public Product purchaseProduct(String slotNum){
        Slot slot = inventory.getInventoryMap().get(slotNum);
        Product product = slot.getProduct();
        funds -= product.price;
        total += product.price;
        slot.reduceQuantity();
        product.productSold();
        double priorBalance = funds + product.price;
        salesReportMap.put(product.productName, product.getSales());
        String message = getCurrentTimeAsString(format) + " " + product.productName + " " + slot.getSlotNumber() + " " + String.format("$%,.2f", priorBalance) + " " + String.format("$%,.2f", getFunds()) + "\n";
        boolean append = newFile.exists() ? true : false;
        try (PrintWriter writer =
                new PrintWriter(new FileOutputStream(newFile, append))) {
            writer.append(message);
        }catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return product;
    }

    public void returnChange(){
        int change = moneyObject.getPennies(funds);
        moneyObject.getCoins(change);
        double dollarAmount = funds;
        funds = 0;
        System.out.println("Balance remaining: " +  String.format("$%,.2f", funds));
        String message = getCurrentTimeAsString(format) + " GIVE CHANGE: " + String.format("$%,.2f", dollarAmount) + " " + String.format("$%,.2f", funds) + "\n";
        boolean append = newFile.exists() ? true : false;
        try (PrintWriter writer =
                     new PrintWriter(new FileOutputStream(newFile, append))) {
            writer.append(message);
        }catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    String format = "MM/dd/yyyy HH:mm:ss a";
    String fileDate = "MM-dd-yyyy-HHmma";
    public String getCurrentTimeAsString(String format) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(now);
    }


    public void getSalesReport() {
        String filePath = getCurrentTimeAsString(fileDate);
        File salesFile = new File(filePath +"salesReport.txt");
        try (PrintWriter writer = new PrintWriter(salesFile)){
            for (Map.Entry<String, Integer> entry : salesReportMap.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                writer.print(key + "|" + value + "\n");
            }
            writer.print("\n" +"**TOTAL SALES** " + String.format("$%,.2f", getTotal()));
        }catch (IOException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

}
