package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public JdbcAccountDao(){
//
//    }

    public Account getAccount(int accountId){
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if(rowSet.next()){
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    public Account getAccountByUserId(int userId){
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if(rowSet.next()){
            account = mapRowToAccount(rowSet);
        }
        return account;
    }


    public Account getAccountByUsername(String username){
        Account account = null;
        String sql = "SELECT * FROM account a JOIN tenmo_user tu ON a.user_id = tu.user_id WHERE username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if(rowSet.next()){
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    public BigDecimal getBalance(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?; ";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,accountId);
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account.getBalance();
    }

    public BigDecimal getBalanceByUsername(String username) {
        Account account = null;
        String sql = "SELECT * FROM account a JOIN tenmo_user tu ON a.user_id = tu.user_id WHERE username = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,username);
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account.getBalance();
    }


    public Account mapRowToAccount (SqlRowSet rowSet){
        Account account = new Account();

            account.setAccountId(rowSet.getInt("account_id"));
            account.setUserId(rowSet.getInt("user_id"));
            account.setBalance(rowSet.getBigDecimal("balance"));
            return account;
    }


}
