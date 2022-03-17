package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTransferDao implements  TransferDao{

    private final JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(DataSource dataSource, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcAccountDao = jdbcAccountDao;
    }
    @Transactional
    public void transfer(int accountFrom, int accountTo, BigDecimal accountFromNewBalance, BigDecimal accountToNewBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, accountFromNewBalance, accountFrom);
        jdbcTemplate.update(sql, accountToNewBalance, accountTo);

    }

    public SqlRowSet transferAccounts (int accountId){
        String sql = "SELECT account_id, a.user_id, username FROM account a " +
                "JOIN tenmo_user tu ON a.user_id = tu.user_id WHERE account_id != ?";
        SqlRowSet rowSet= jdbcTemplate.queryForRowSet(sql, accountId);
         return rowSet;
    }

    public List<TransferAccount> transferAccountList(int accountId){
        List<TransferAccount> transferAccounts = new ArrayList<>();
        SqlRowSet rowSet = transferAccounts(accountId);
        while (rowSet.next()){
            TransferAccount transferAccount = mapRowToTransferAccount(rowSet);
            transferAccounts.add(transferAccount);
        }
        return transferAccounts;
    }

    public TransferAccount mapRowToTransferAccount (SqlRowSet rowSet){
        TransferAccount transferAccount = new TransferAccount();
        transferAccount.setAccountId(rowSet.getInt("account_id"));
        transferAccount.setUsername(rowSet.getString("username"));
        transferAccount.setUserId(rowSet.getInt("user_id"));
        return transferAccount;
    }


    @Transactional
    @Override
    public void createSendTransfer(Transfer transfer, int accountFrom, int accountTo, BigDecimal amount, BigDecimal accountFromNewBalance, BigDecimal accountToNewBalance){
        String sql = "INSERT INTO transfer (transfer_status_id, transfer_type_id, account_from, account_to, amount) values" +
                "((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc LIKE 'Send')," +
                "(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc LIKE 'Approved') , ?, ?, ?);";

        jdbcTemplate.update(sql, accountFrom, accountTo,
                amount);
        transfer(accountFrom, accountTo,accountFromNewBalance, accountToNewBalance);

    }

    public void createRequestTransfer(Transfer transfer, int accountId){
        String sql = "INSERT INTO transfer (transfer_status_id, transfer_type_id, account_from, account_to, amount) values" +
                "((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc LIKE 'Request')," +
                "(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc LIKE 'Pending') , ?, ?, ?);";

        jdbcTemplate.update(sql, transfer.getAccountFrom(), transfer.getAccountTo(),
                transfer.getAmount());
    }



    public List<Transfer> getTransferHistoryList (int accountId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        } return transfers;
    }


    public List<Transfer> getPendingTransfers (int accountId){
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer t JOIN transfer_status ts ON " +
        "t.transfer_status_id = ts.transfer_status_id WHERE (account_from = ? OR " +
                "account_to = ?) AND transfer_status_desc LIKE 'Pending'";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        } return transfers;

    }
    public void updateTransfer(Transfer transfer){
        String sql = "UPDATE transfer SET transfer_status_id = ?, transfer_type_id = ?, account_from = ?, account_to = ?, amount = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferTypeId(), transfer.getAccountFrom(),
                transfer.getAccountTo(), transfer.getAmount(), transfer.getTransferId());
    }



     public Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount( rowSet.getBigDecimal("amount"));
         return transfer;
     }

}


