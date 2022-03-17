package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferAccount;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {
    private JdbcTransferDao jdbcTransferDao;
    private JdbcAccountDao jdbcAccountDao;

    public TransferService(JdbcTransferDao jdbcTransferDao, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTransferDao = jdbcTransferDao;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    public void addTransfer(Transfer transfer, int accountId){
        int accountFrom = transfer.getAccountFrom();
        int accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();
        BigDecimal accountToNewBalance = jdbcAccountDao.getAccount(accountTo).getBalance().add(amount);
        BigDecimal accountFromNewBalance = jdbcAccountDao.getAccount(accountFrom).getBalance().subtract(amount);
        // the 1 represents the transfer_type_id number for a request
        if (transfer.getTransferTypeId() == 1){
            jdbcTransferDao.createRequestTransfer(transfer, accountId);
        }else if (transfer.getTransferTypeId() == 2) {
        jdbcTransferDao.createSendTransfer(transfer, accountFrom, accountTo, amount, accountFromNewBalance, accountToNewBalance);
    }
    }

    public void updateTransfer(Transfer transfer){
        jdbcTransferDao.updateTransfer(transfer);
        //If the updated transfer has a transfer_status_id of 2(approved) then transfer the money requested
        if (transfer.getTransferStatusId() == 2){
            int accountFrom = transfer.getAccountFrom();
            int accountTo = transfer.getAccountTo();
            BigDecimal amount = transfer.getAmount();
            BigDecimal accountToNewBalance = jdbcAccountDao.getAccount(accountTo).getBalance().subtract(amount);
            BigDecimal accountFromNewBalance = jdbcAccountDao.getAccount(accountFrom).getBalance().add(amount);
            jdbcTransferDao.transfer(accountFrom, accountTo, accountFromNewBalance, accountToNewBalance);
        }
    }
}


