package com.techelevator;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class MoneyTest {

    Money money = new Money(10.00);

    @Test
    public void getMoneyBigDecimal() {
        Double dollars = 10.0;
        BigDecimal expected = new BigDecimal(dollars);
        assertEquals(expected, money.getMoneyBigDecimal(dollars));
    }

    @Test
    public void getPennies() {
        double pennies = 1.00;
        assertEquals(100, money.getPennies(1.00));
    }

    @Test
    public void getCoins() {

    }
}