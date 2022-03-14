package com.techelevator;

import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.*;

public class InventoryTest {

    @Test
    public void getInventoryList() {
    }

    @Test
    public void getInventoryMap() {
        File file = new File("vendingmachine.csv");
        Inventory inventory = new Inventory(file);
        Map<String, Slot> map = inventory.loadInventoryMap();
        assertEquals(16, map.size());
        assertEquals("Potato Crisps", map.get("A1").getProduct().productName);
        assertEquals("Moonpie", map.get("B1").getProduct().productName);
        assertEquals("Cola", map.get("C1").getProduct().productName);

    }
    @Test
    public void getDisplay(){
        File file = new File("vendingmachine.csv");
        Inventory inventory = new Inventory(file);
        Map<String, Slot> map = inventory.getInventoryMap();
//        inventory.getDisplay();
    }
}