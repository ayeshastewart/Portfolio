package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferAccount;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountUserService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("account/")
//@PreAuthorize("isAuthorized()")
public class TenmoController {

    private JdbcTransferDao transferDao;
    private JdbcAccountDao accountDao;


    private AccountUserService accountUserService;
    private TransferService transferService;



    public TenmoController(AccountUserService accountUserService, TransferService transferService, JdbcTransferDao transferDao, JdbcAccountDao accountDao){
        this.accountUserService = accountUserService;
        this.transferService = transferService;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "{accountId}/transfer", method = RequestMethod.POST)
    public void addTransfer(@RequestBody Transfer transfer, @PathVariable ("accountId") int accountId)
            throws AccountNotFoundException{
        transferService.addTransfer(transfer, accountId);
    }

    @RequestMapping(path = "{accountId}/transferAccounts", method = RequestMethod.GET)
    public List<TransferAccount> getTransferAccountList(@PathVariable ("accountId") int accountId) throws AccountNotFoundException{
        return transferDao.transferAccountList(accountId);
    }

    @RequestMapping(path = "{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@PathVariable ("accountId") int accountId) throws AccountNotFoundException{
        return transferDao.getTransferHistoryList(accountId);
    }
    @RequestMapping(path = "{accountId}/pendingtransfers", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable ("accountId") int accountId) throws AccountNotFoundException{
        return transferDao.getPendingTransfers(accountId);
    }


    @RequestMapping(path = "{userId}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable("userId") int userId) throws UserNotFoundException {
        return accountDao.getAccountByUserId(userId);
    }

    @RequestMapping(path = "username/{username}", method = RequestMethod.GET)
    public BigDecimal getAccountByUserName(@PathVariable ("username") String username ){
        return accountDao.getBalanceByUsername(username);
    }

    @RequestMapping(path = "user/{accountId}", method = RequestMethod.GET)
    public String getUserByAccountId(@PathVariable ("accountId") int accountId){
        return accountUserService.getUsername(accountId);
    }

    @RequestMapping (path = "transfer/{id}", method = RequestMethod.PUT)
    public void updateTransfer (@RequestBody Transfer transfer, @PathVariable ("id") int id){
        transferService.updateTransfer(transfer);
    }

}
