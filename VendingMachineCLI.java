package com.techelevator;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,MAIN_MENU_OPTION_EXIT };

	private static final String SECOND_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String SECOND_MENU_OPTION_SELECT = "Select Product";
	private static final String SECOND_MENU_OPTION_FINISH = "Finish Transaction";
	private static final String SECOND_MENU_MONEY_DISPLAY = "Current Money Provided:";
	private static final String SECOND_MENU_SALES_REPORT = " ";
	private static final String[] SECOND_MENU_OPTIONS = { SECOND_MENU_OPTION_FEED_MONEY, SECOND_MENU_OPTION_SELECT,SECOND_MENU_OPTION_FINISH, SECOND_MENU_SALES_REPORT};

	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				menu.getDisplay();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				while (true) {
					String secondChoice = (String) menu.getChoiceFromOptions(SECOND_MENU_OPTIONS);

					if (secondChoice.equals(SECOND_MENU_OPTION_FEED_MONEY)) {
						// display vending machine items
						menu.feedMoney();
					} else if (secondChoice.equals(SECOND_MENU_OPTION_SELECT)) {
						menu.purchase();
					}else if (secondChoice.equals(SECOND_MENU_OPTION_FINISH)){
						menu.finish();
						run();
					}else if (secondChoice.equals(SECOND_MENU_SALES_REPORT)){
						menu.writeSalesReport();
					}
					}
				}
			else if(choice.equals(MAIN_MENU_OPTION_EXIT));{
//				System.exit(0);// do purchase
			}
			}

		}


	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
