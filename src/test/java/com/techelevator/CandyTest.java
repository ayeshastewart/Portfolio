package com.techelevator;

import org.junit.Test;

import static org.junit.Assert.*;

public class CandyTest {

    @Test
    public void getMessage() {
        Candy candy = new Candy("lollipop", "Candy", 1);

        Slot slot = new Slot("Lays", "B3", 5, 3.50, candy);

        assertEquals("Munch, Munch, Yum!", candy.getMessage());

    }
}