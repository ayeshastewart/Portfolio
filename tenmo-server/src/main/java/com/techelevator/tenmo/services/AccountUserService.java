package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Service
public class AccountUserService  {

    private JdbcAccountDao jdbcAccountDao ;
    private JdbcTransferDao jdbcTransferDao;;
    private JdbcTemplate jdbcTemplate;

//    public TransferService(JdbcTransferDao jdbcTransferDao, JdbcAccountDao jdbcAccountDao) {
//        this.jdbcTransferDao = jdbcTransferDao;
//        this.jdbcAccountDao = jdbcAccountDao;
//    }

    public AccountUserService(JdbcAccountDao jdbcAccountDao, DataSource dataSource) {

        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public BigDecimal showBalance (int accountId){
        return jdbcAccountDao.getBalance(accountId);

    }

    public String getUsername(int accountId){
    String username = null;
    String sql = "SELECT username FROM tenmo_user tu JOIN account a ON tu.user_id = a.user_id WHERE account_id = ?;";
    username = jdbcTemplate.queryForObject(sql, String.class, accountId);
    return username;
    }

//    public User mapRowToUser(SqlRowSet rowSet){
//        User user = new User ();
//        user.setId(rowSet.getLong("user_id"));
//        user.setUsername(rowSet.getString("username"));
//        return user;
//    }

//    public Transfer mapRowToTransfer(SqlRowSet rowSet){
//        Transfer transfer = new Transfer();
//        transfer.setTransferId(rowSet.getInt("transfer_id"));
//        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
//        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
//        transfer.setAccountFrom(rowSet.getInt("account_from"));
//        transfer.setAccountTo(rowSet.getInt("account_to"));
//        transfer.setAmount( rowSet.getBigDecimal("amount"));
//        return transfer;
//    }

}
