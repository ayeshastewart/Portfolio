package com.techelevator;

import java.math.BigDecimal;

public class Money {
    private double money = 0;

    public Money(double money){
        this.money = money;

    }

    public BigDecimal getMoneyBigDecimal(double money) {
        BigDecimal bd = new BigDecimal(money);
        return bd;
    }

    public int getPennies(double money){
        int pennies = (int) Math.ceil(money * 100);
        return pennies;
    }

    public void getCoins(int change){
        int quarters = 0;
        int dimes = 0;
        int nickles =0;
        quarters = change / 25;
        change -= quarters * 25;
        dimes = change / 10;
        change -= dimes * 10;
        nickles = change / 5;

        System.out.println ("Your change is, quarters: " + quarters + "," + " dimes: " + dimes + "," + " nickles: "+ nickles);
    }
}
