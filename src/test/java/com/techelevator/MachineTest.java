package com.techelevator;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

public class MachineTest {
    File file = new File("vendingmachine.csv");
    Inventory inventory = new Inventory(file);
    Machine machine = new Machine(inventory);
    Slot slot = new Slot();



    @Test
    public void getFunds() {
        machine.getFunds();
        assertEquals(0.0,machine.getFunds(),0.0);

    }

    @Test
    public void getTotal() {
        machine.getTotal();
        assertEquals(0.0, machine.getTotal(), 0.0);
    }


    @Test
    public void feedMoney_testToSeeThatMoneyIsAddToFunds_shouldReturnBalancePlusMoney() {
        machine.feedMoney(5.00);
        assertEquals(5.00, machine.getFunds(), 0.0);
        machine.feedMoney(5.00);
        assertEquals(10.00, machine.getFunds(), 0.0);
        machine.feedMoney(2);
        assertEquals(12.00, machine.getFunds(), 0.0);
    }

    @Test
    public void purchaseProduct_testToSeeIfItReturnsProduct_expectProductNameToBeCorrect() {
        machine = new Machine(inventory);
        inventory.loadInventoryMap();
        assertEquals("Crunchie", machine.purchaseProduct("B4").getProductName());
    }

    @Test
    public void purchaseProduct_testToSeeIfFileWasCreate_ShouldContainNewEntry(){
        machine = new Machine(inventory);
        inventory.loadInventoryMap();
        boolean fileContains = false;
        machine.purchaseProduct("B4");
        File file = new File("log.txt");
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                String currentLine = scanner.nextLine();
                if (currentLine.contains("B4")){
                    fileContains = true;
                }
            }
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        assertTrue(fileContains);
    }


    @Test
    public void returnChange_testToSeeIfFundsReducedToZeroCoinsReturned_shouldReturnZeroBalance() {
        machine.feedMoney(5.00);
        machine.returnChange();
        assertEquals(0.0, machine.getFunds(), 0.0);
    }

    @Test
    public void getCurrentTimeAsString() {
        String format = "MM/dd/yyyy HH:mm:ss a";
        machine.getCurrentTimeAsString(format);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        assertEquals(formatter.format(now), machine.getCurrentTimeAsString(format));
    }

    @Test
    public void getSalesReport_testIfReportWasReported_fileShouldExist() {
        String fileDate = "MM-dd-yyyy-HHmma";
        String filePath = machine.getCurrentTimeAsString(fileDate);
        File salesFile = new File(filePath +"salesReport.txt");
        machine.getSalesReport();
        assertTrue(salesFile.exists());
    }

    @Test
    public void getSalesReport_testIfPurchasesAreOnReport(){
        inventory.loadInventoryMap();
        machine.feedMoney(5.00);
        machine.purchaseProduct("B4");
        String fileDate = "MM-dd-yyyy-HHmma";
        String filePath = machine.getCurrentTimeAsString(fileDate);
        File salesFile = new File(filePath +"salesReport.txt");
        machine.getSalesReport();
        assertEquals(1, machine.salesReportMap.size());
    }

}