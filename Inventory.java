package com.techelevator;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Inventory {
    private File inventoryList;

    private Map<String, Slot> inventoryMap = new TreeMap<>();

    public Inventory(File inventoryList) {
        this.inventoryList = inventoryList;


    }

    public File getInventoryList() {
        return inventoryList;
    }
    public Map<String, Slot> getInventoryMap() {
        return inventoryMap;
    }
    public Map<String, Slot> loadInventoryMap() {
        try (Scanner scanner = new Scanner(inventoryList)) {
            while (scanner.hasNextLine()) {
                String inventoryOutput = scanner.nextLine();
                String[] line = inventoryOutput.split("\\|");
                double price = Double.parseDouble(line[2]);
                Product product;
                if(line[3].equalsIgnoreCase("chip")){
                    product = new Chip(line[1], line[3], price);
                    Slot snackSlot = new Slot(line[1], line[0], 5, price, product);
                    inventoryMap.put(line[0], snackSlot);
                } else if (line[3].equalsIgnoreCase("candy")){
                    product = new Candy(line[1], line[3], price);
                    Slot snackSlot = new Slot(line[1], line[0], 5, price, product);
                    inventoryMap.put(line[0], snackSlot);
                }else if (line[3].equalsIgnoreCase("drink")){
                    product = new Drink(line[1], line[3], price);
                    Slot snackSlot = new Slot(line[1], line[0], 5, price, product);
                    inventoryMap.put(line[0], snackSlot);
                }else if (line[3].equalsIgnoreCase("gum")){
                    product = new Gum(line[1], line[3], price);
                    Slot snackSlot = new Slot(line[1], line[0], 5, price, product);
                    inventoryMap.put(line[0], snackSlot);
                }
            }
           return inventoryMap;

        } catch (Exception ex) {
            ex.getMessage();
//        }return inventoryMap;
        }

        return inventoryMap;

    }

//    public void getDisplay(){
//        for (Map.Entry < String, Slot > entry:inventoryMap.entrySet()){
//            String key = entry.getKey();
//            Slot value = entry.getValue();
//            System.out.println(key+"|"+value.getProduct()+ "|"+ value.getPrice()+"|"+ value.getQuantity());
        }

