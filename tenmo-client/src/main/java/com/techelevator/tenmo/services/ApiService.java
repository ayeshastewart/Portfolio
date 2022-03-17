package com.techelevator.tenmo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiService {

    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticationService authenticationService;
    private AuthenticatedUser authenticatedUser;

    int requestTransferTypeId = 1;
    int sendTrasferTypeId = 2;


    public BigDecimal getCurrentBalance(String username){
        BigDecimal accountBalance = restTemplate.getForObject(API_BASE_URL + "account/username/" + username, BigDecimal.class);
        return accountBalance;
    }

    public Transfer createTransfer(int userId, BigDecimal amount, AuthenticatedUser currentUser){
        Transfer transfer = new Transfer();
        Account currentUserAccount = getAccount(currentUser.getUser().getId().intValue());
        Account receiverAccount = getAccount(userId);
        transfer.setAccountTo(receiverAccount.getAccountId());
        transfer.setAccountFrom(currentUserAccount.getAccountId());
        transfer.setAmount(amount);
        transfer.setTransferTypeId(sendTrasferTypeId);
        return transfer;
    }
    public void updateTransfer(Transfer transfer, int approveOrReject){
        int accountId = transfer.getAccountTo();
        String username = getUsername(accountId);
        BigDecimal accountBalance = getCurrentBalance(username);
        // 1 is the menu selection number for the approval option
        if(approveOrReject == 1){
            if (accountBalance.subtract(transfer.getAmount()).compareTo(BigDecimal.valueOf(0)) < 0){
                System.out.println("\nTransfer cannot be approved. Amount exceeds your current balance.");
            } else {
        // 2 is the transfer_status_id for approved
            transfer.setTransferStatusId(2);
            }
        // else if user selects reject
        } else if (approveOrReject == 2){
        // set transfer_status_id to rejected
            transfer.setTransferStatusId(3);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        try{
            restTemplate.put(API_BASE_URL + "account/transfer/" + transfer.getTransferId(),  entity);
        }
        catch (RestClientResponseException ex){
            BasicLogger.log("error: " + ex.getRawStatusCode() + " " + ex.getStatusText());
        }
        catch (ResourceAccessException ex){
            BasicLogger.log("error : " + ex.getMessage());
        }

    }

    public Transfer createRequest(int userId, BigDecimal amount, AuthenticatedUser currentUser){
        Transfer transfer = new Transfer();
        Account currentUserAccount = getAccount(currentUser.getUser().getId().intValue());
        Account receiverAccount = getAccount(userId);
        transfer.setAccountTo(receiverAccount.getAccountId());
        transfer.setAccountFrom(currentUserAccount.getAccountId());
        transfer.setAmount(amount);
        transfer.setTransferTypeId(requestTransferTypeId);
        return transfer;
    }


    public Transfer transferMethod(Transfer newTransfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);

        try{
            Transfer saveTransfer = restTemplate.postForObject(API_BASE_URL + "account/" + newTransfer.getAccountFrom() + "/transfer",  entity, Transfer.class);
            return saveTransfer;
        }
        catch (RestClientResponseException ex){
            BasicLogger.log("error: " + ex.getRawStatusCode() + " " + ex.getStatusText());
        }
        catch (ResourceAccessException ex){
            BasicLogger.log("error : " + ex.getMessage());
        }
        return null;

    }


    public Account getAccount(int userId){
        Account account = restTemplate.getForObject(API_BASE_URL + "account/" + userId, Account.class);
        return account;
    }

    public String getUsername(int accountId){
        String username= restTemplate.getForObject(API_BASE_URL + "account/user/" + accountId, String.class);
        return username;
    }

    public List<TransferAccount> transferAccountList(int accountId){
        List<TransferAccount> transferAccountsList = restTemplate.getForObject(API_BASE_URL + "account/" + accountId + "/transferAccounts", List.class);
        return transferAccountsList;
    }

    public List<Transfer> transferHistoryList(int accountId){
        List<Transfer> transferHistoryList = restTemplate.getForObject(API_BASE_URL + "account/" + accountId + "/transfers",
                List.class);
           return transferHistoryList;

    }
    public List<Transfer> pendingTransferList(int accountId){
        List<Transfer> pendingTransferList = restTemplate.getForObject(API_BASE_URL + "account/" + accountId + "/pendingtransfers",
                List.class);
        return pendingTransferList;

    }
    public Transfer getTransfer (List<Transfer> list, int transferId){
        ObjectMapper mapper = new ObjectMapper();
//        int currentUserId = user.getUser().getId().intValue();
        list = mapper.convertValue(list,
                new TypeReference<List<Transfer>>() {});
        for(Transfer transfer : list){
            if(transfer.getTransferId() == transferId){
                return transfer;
            }
        }
        return null;
    }

    public boolean canTransferToThisAccount(AuthenticatedUser user, int userId){
        ObjectMapper mapper = new ObjectMapper();
        List<TransferAccount> list = transferAccountList(getAccount(user.getUser().getId().intValue()).getAccountId());
        list = mapper.convertValue(list,
                new TypeReference<List<TransferAccount>>() {});
        for(TransferAccount transferAccount : list){
            if(transferAccount.getUserId() == userId){
                return true;
            } else return false;
        } return false;
        }




    public void printTransferList(AuthenticatedUser user){
        ObjectMapper mapper = new ObjectMapper();
        int userId = user.getUser().getId().intValue();
        List<TransferAccount> transferAccounts = mapper.convertValue(transferAccountList(getAccount(userId).getAccountId()),
                new TypeReference<List<TransferAccount>>() {});
        for(TransferAccount transferAccount : transferAccounts){
            System.out.println(transferAccount.getUserId() + "     " + transferAccount.getUsername());
        }

    }


    public boolean isTransferValid(BigDecimal amount, AuthenticatedUser user){
        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            return false;
        } if (amount.compareTo(getCurrentBalance(user.getUser().getUsername())) > 0) {
            System.out.println("\nInsufficient funds");
            return false;
        }
        return true;
    }


    public void viewList(AuthenticatedUser user, List<Transfer> list) {
        String userNameOfOtherAccount;
        ObjectMapper mapper = new ObjectMapper();
        int userId = user.getUser().getId().intValue();
        list = mapper.convertValue(list,
                new TypeReference<List<Transfer>>() {});
        for(Transfer transfer : list) {
            if (transfer.getAccountFrom() == getAccount(userId).getAccountId()){
                userNameOfOtherAccount = getUsername(transfer.getAccountTo());
            } else {
                userNameOfOtherAccount = getUsername(transfer.getAccountFrom());
            }
            if (transfer.getAccountFrom() == getAccount(userId).getAccountId()) {
                System.out.println(transfer.getTransferId() + "       To : " + userNameOfOtherAccount +
                        "        " + String.format("$%,.2f", transfer.getAmount()));
            }else if (transfer.getAccountTo() == getAccount(userId).getAccountId()){
            System.out.println(transfer.getTransferId() + "       From : " + userNameOfOtherAccount + "        " + String.format("$%,.2f", transfer.getAmount())  );
        }
    }
        }

    public void viewDetails(AuthenticatedUser user, int transferId, List<Transfer> list) {
        ObjectMapper mapper = new ObjectMapper();
        list = mapper.convertValue(list,
                new TypeReference<List<Transfer>>() {});
        for(Transfer transfer : list) {
            if (transfer.getTransferId() == transferId){
            String recipientUsername = getUsername(transfer.getAccountTo());
            System.out.println("----------------------------------------");
            System.out.println("Transfer Details");
            System.out.println("----------------------------------------");
            System.out.println("Id: " + transfer.getTransferId());
            System.out.println("From: " + getUsername(transfer.getAccountFrom()));
            System.out.println("To:  " + recipientUsername);
            System.out.print("Type:  " );
            printTransferType(transfer);
            System.out.print("Status:  ");
            printTransferStatus(transfer);
            }

    }
    }
//    public void approveRequest(int transferId){
//        restTemplate.getForObject()
//
//    }

    public void printTransferType(Transfer transfer){
        if (transfer.getTransferTypeId() == 1){
            System.out.println("Request");
        } else if (transfer.getTransferTypeId() == 2){
            System.out.println("Send");
        }
    }

    public void printTransferStatus(Transfer transfer){
        if (transfer.getTransferStatusId() == 1){
            System.out.println("Pending");
        } else if (transfer.getTransferStatusId() == 2){
        System.out.println("Approved");
        }else if (transfer.getTransferStatusId() == 3){
            System.out.println("Rejected");
        }
    }

    public void Display(int selectionNumber){
        //These numbers are the same as the menuSelection numbers in App
        if (selectionNumber == 2){
            System.out.println("Transfers");
            System.out.println("ID       From/To              Amount");
        }else if (selectionNumber == 3){
            System.out.println("Pending Transfers");
            System.out.println("ID       From/To              Amount");
        }else if (selectionNumber == 4 || selectionNumber == 5){
            System.out.println("Users");
            System.out.println("ID       Name");
        }



    }

 /*User accounts are TransferAccount type and display differently than Transfer objects, this method figures out
 whether a list of transfers or a list of TransferAccount needs to be displayed, numbers correlate with menu selection */
    public void accountsOrTransfers(AuthenticatedUser user, int selectionNumber){
        int accountId = getAccount(user.getUser().getId().intValue()).getAccountId();
        if (selectionNumber == 2){
            viewList(user, getTransferLists(2, user));
        } else if (selectionNumber == 3){
            viewList(user, getTransferLists(3, user));
        } else if (selectionNumber == 4 || selectionNumber == 5){
            printTransferList(user);
        }
    }
    // gets list of all transfer or all pending transfer depending on menu selection number
    public List<Transfer> getTransferLists(int selectionNumber, AuthenticatedUser user){
        int accountId = getAccount(user.getUser().getId().intValue()).getAccountId();
        if (selectionNumber == 2){
            return transferHistoryList(accountId);
        } else {
           return pendingTransferList(accountId);
    }
    }


    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        // TODO Auto-generated method stub

    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }
}
