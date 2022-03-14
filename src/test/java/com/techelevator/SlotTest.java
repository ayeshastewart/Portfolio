package com.techelevator;

import org.junit.Test;

import static org.junit.Assert.*;

public class SlotTest {

    private Candy candy = new Candy("lollipop", "Candy", 1);
    @Test
    public void getPrice() {
        Slot slot = new Slot("Lays", "B3", 5, 3.50, candy);
        assertEquals(3.50, slot.getPrice(), 0.0);
    }

    @Test
    public void getQuantity() {
        Slot slot = new Slot("Lays", "B3", 5, 3.50, candy);
        assertEquals(5, slot.getQuantity());
    }

    @Test
    public void getProduct() {
        Slot slot = new Slot("Lays", "B3", 5, 3.50, candy);
        assertEquals("Lays", slot.getProduct());
    }

    @Test
    public void getSlotNumber() {
        Slot slot = new Slot("Lays", "B3", 5, 3.50, candy);
        assertEquals("B3", slot.getSlotNumber());
    }
}