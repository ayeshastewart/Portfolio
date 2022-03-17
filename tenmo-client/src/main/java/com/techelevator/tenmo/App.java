package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.ApiService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final ApiService apiService = new ApiService();

    private AuthenticatedUser currentUser;

    int selectionTransfersView = 2;
    int selectionUsersView = 4;
    int selectionPendingView = 3;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            //passing the menuSelectionNumber to use in a method that prints different displays depending on the selection to reduce repetitive code
            if (menuSelection == 1) {
                viewCurrentBalance(1);
            } else if (menuSelection == 2) {
                viewTransferHistory(2);
            } else if (menuSelection == 3) {
                viewPendingRequests(3);
            } else if (menuSelection == 4) {
                sendBucks(4);
            } else if (menuSelection == 5) {
                requestBucks(5);
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

//    private void requestMenu(int transferId, AuthenticatedUser user){
//        int accountId = apiService.getAccount(user.getUser().getId().intValue()).getAccountId();
//        int menuSelection = -1;
//        while (menuSelection != 0) {
//            consoleService.printRequestMenu();
//            menuSelection = consoleService.promptForMenuSelection("Please select an option: ");
//            if (menuSelection == 1) {
//                apiService.updateTransfer(apiService.getTransfer(apiService.pendingTransferList(accountId), transferId));
//            }
//            if (menuSelection == 2) {
////            apiService.rejectRequest();
//            }
//            if (menuSelection == 0) {
//                continue;
//            } else {
//                System.out.println("Invalid selection");
//            }
//        }
//    }


    public void createAccount(AuthenticatedUser user){
        int userIdAsInt = currentUser.getUser().getId().intValue();
        apiService.getAccount(userIdAsInt);
    }

	private void viewCurrentBalance(int selectionNumber) {
		BigDecimal balance = apiService.getCurrentBalance(currentUser.getUser().getUsername());
        System.out.println( "\nYour current account balance is: " + balance);
		
	}

	private void viewTransferHistory(int selectionNumber) {
        consoleService.printScreen(currentUser, selectionNumber);
        consoleService.promptForTransferNumber(currentUser, selectionNumber);
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests(int selectionNumber) {
        consoleService.printScreen(currentUser, selectionNumber);
        consoleService.promptForTransferNumber(currentUser, selectionNumber);
		// TODO Auto-generated method stub
	}

	private void sendBucks(int selectionNumber) {
        consoleService.printScreen(currentUser, selectionNumber);
        consoleService.promptForTransferData(currentUser);
		// TODO Auto-generated method stub
		
	}

	private void requestBucks(int selectionNumber) {
        consoleService.printScreen(currentUser, selectionNumber);
        consoleService.promptForRequestData(currentUser);
		// TODO Auto-generated method stub
		
	}

}
