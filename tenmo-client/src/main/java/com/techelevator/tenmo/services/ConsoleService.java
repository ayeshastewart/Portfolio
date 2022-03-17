package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    ApiService apiService = new ApiService();

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }
    public void printRequestMenu(){
        System.out.println("1 : Approve");
        System.out.println("2 : Reject");
        System.out.println("0 : Don't approve or reject");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    private void requestMenu(int transferId, AuthenticatedUser user){
        int accountId = apiService.getAccount(user.getUser().getId().intValue()).getAccountId();
        int menuSelection = -1;
        BigDecimal balanceBeforeTransfer = apiService.getCurrentBalance(user.getUser().getUsername());
        String prompt = "Please choose an option: ";
            printRequestMenu();
            menuSelection = promptForMenuSelection(prompt);
         if (menuSelection == 1){
            apiService.updateTransfer(apiService.getTransfer(apiService.pendingTransferList(accountId), transferId), 1);
            if (balanceBeforeTransfer.compareTo(apiService.getCurrentBalance(user.getUser().getUsername())) != 0){
             System.out.println("\nTransfer approved. Your new balance is: " + apiService.getCurrentBalance(user.getUser().getUsername()));
            }
        } else if (menuSelection == 2){
                apiService.updateTransfer(apiService.getTransfer(apiService.pendingTransferList(accountId), transferId), 2);
             System.out.println("\nTransfer rejected.");
        } else if (menuSelection == 0){
        } else {
            System.out.println("Invalid selection");
        }
        }


// This method displays differently depending on the selectionNumber from App
    public void printScreen(AuthenticatedUser user, int selectionNumber){
        System.out.println("----------------------------------------");
        apiService.Display(selectionNumber);
        System.out.println("----------------------------------------");
        apiService.accountsOrTransfers(user, selectionNumber);
        System.out.println("----------");
        System.out.println("\n");
    }

    public void promptForTransferData(AuthenticatedUser currentUser) {
            String prompt = "Enter ID of user you are sending to (0 to cancel): ";
            int userId = promptForInt(prompt);
            if (!apiService.canTransferToThisAccount(currentUser, userId) && userId != 0){
                System.out.println("\nMoney cannot be sent to this account.");
            } else if ( userId != 0) {
            prompt = "Enter amount: ";
            BigDecimal amount = promptForBigDecimal(prompt);
            prompt = "\nAre you sure you want to send " + String.format("$%,.2f", amount) +" to user " + userId + "? (Y/N): ";
            String yesOrNo = promptForString(prompt).toLowerCase(Locale.ROOT);
            if (yesOrNo.equals("y")){
            if (!apiService.isTransferValid(amount, currentUser)){
                System.out.println("Not a valid transfer amount.");
            } else {
                try {
                apiService.transferMethod(apiService.createTransfer(userId, amount, currentUser));
                    System.out.println("\nMoney transfer complete! Your new balance is: " + apiService.getCurrentBalance(currentUser.getUser().getUsername()));
                } catch (Exception ex) {
                    BasicLogger.log("Error: " + ex.getMessage());
        }
            }
            } else if ( !yesOrNo.equals("n")){
                    System.out.println("Please enter Y or N");

                }
    }
    }

    public void promptForRequestData(AuthenticatedUser currentUser) {
        String prompt = "Enter ID of user you are requesting from (0 to cancel): ";
        int userId = promptForInt(prompt);
        if (!apiService.canTransferToThisAccount(currentUser, userId) && userId != 0){
            System.out.println("\nA request cannot be made to this account.");
        } else if (userId != 0){
        prompt = "Enter amount: ";
        BigDecimal amount = promptForBigDecimal(prompt);
        if(amount.compareTo(BigDecimal.valueOf(0)) <=  0){
            System.out.println("Not a valid request amount");
        } else {
        try{ apiService.transferMethod(apiService.createRequest(userId, amount, currentUser));
            System.out.println("\n Request pending");
        } catch (Exception ex) {
            BasicLogger.log("Error: " + ex.getMessage());

        }
        }
    }
    }

    public void promptForTransferNumber(AuthenticatedUser user, int selectionNumber) {
        String prompt;
        if (selectionNumber == 3){
            prompt = "Please enter transfer ID to approve/reject (0 to cancel): ";
            int transferId = promptForInt(prompt);
            try{
                // if the transfer ID number is on the pending transfer list
                if (apiService.getTransfer(apiService.pendingTransferList(apiService.getAccount(user.getUser().getId().intValue()).getAccountId()),
                        transferId) != null){
                    requestMenu(transferId, user);
                // if they didn't press 0 to continue
                } else if (transferId != 0) {
                    System.out.println("Not a valid pending transfer ID number.");
                }
            } catch (Exception ex){
                BasicLogger.log("Error: " + ex.getMessage());
            }
        } else {
            prompt = "Please enter transfer ID to view details (0 to cancel): ";
        int transferId = promptForInt(prompt);
        try {
            apiService.viewDetails(user, transferId, apiService.transferHistoryList(apiService.getAccount(user.getUser().getId().intValue()).getAccountId()));
        }catch (Exception ex){
            BasicLogger.log("Error:  " + ex.getMessage());
        }
    }
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
