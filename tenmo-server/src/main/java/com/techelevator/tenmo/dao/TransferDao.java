package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TransferDao {

    public void createSendTransfer(Transfer transfer, int accountFrom, int accountTo, BigDecimal amount, BigDecimal accountFromNewBalance, BigDecimal accountToNewBalance) throws AccountNotFoundException;
}
