package com.techelevator.view;

import com.techelevator.*;
import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;


	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
		this.inventory.loadInventoryMap();
	}

	double money;
	File inventoryList = new File("vendingmachine.csv");
	Inventory inventory = new Inventory(inventoryList);
	Machine machine = new Machine(inventory);

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
			System.out.println(e.getMessage());
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			if (i == 3){
				int optionNum = i;
			} else {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
			}
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public Scanner getIn() {
		return in;
	}

	public void getDisplay() {
		for (Map.Entry<String, Slot> entry : inventory.getInventoryMap().entrySet()) {
			String key = entry.getKey();
			Slot value = entry.getValue();
			if (value.getQuantity() == 0){
				System.out.println(key + "|" + value.getProduct().getProductName() + "|" + value.getProduct().getPrice() + "| SOLD OUT");
			} else {
			System.out.println(key + "|" + value.getProduct().getProductName() + "|" + value.getProduct().getPrice() + "|" + value.getQuantity());
			}

		}
	}


	public void feedMoney() {
		System.out.println("Insert Money");
		String inputMoney = in.nextLine();
		money = Double.parseDouble(inputMoney);
		if (money == 1 || money == 5 || money == 10){
		machine.feedMoney(money);
		System.out.printf("Current Money Provided: $%,.2f", machine.getFunds());
		} else {
			System.out.println("Please insert $1, $5, or $10");
		}

	}


	public void purchase() {
		getDisplay();
		System.out.println("\nSelect Slot");
		String inputSlot = in.nextLine();
		if (inventory.getInventoryMap().containsKey(inputSlot)) {
			if (machine.getFunds() < inventory.getInventoryMap().get(inputSlot).getPrice()) {
				System.out.println("Insufficient Funds, Feed in money");
			} else if (inventory.getInventoryMap().get(inputSlot).getQuantity() == 0) {
				System.out.println("SOLD OUT");
			} else {
				Product product = machine.purchaseProduct(inputSlot);
				System.out.println("You just bought: " + product.productName + " " + product.getMessage());
				System.out.println("Balance remaining: " + String.format("$%,.2f", machine.getFunds()));

			}

		} else {
			System.out.println("Product does not exist");
		}
	}

	public void finish() {
		machine.returnChange();

	}
	public void writeSalesReport() {
		machine.getSalesReport();
	}
}
