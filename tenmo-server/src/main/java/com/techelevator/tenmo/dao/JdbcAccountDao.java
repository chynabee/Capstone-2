package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public Account getAcctByUserId(int userId) {

        Account account = null;

        String sql = "select account_id, user_id, balance\n" +
                "from account\n" +
                "where user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if(results.next()) {
            account = mapRowToAccountBalance(results);
        }

        return account;
    }


    public BigDecimal getBalanceByAcctId(int accountId) {

        String sql = "select balance\n" +
                "from account\n" +
                "where account_id = ?;";

        SqlRowSet results = null;
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);

        return balance;
    }


    public BigDecimal getBalanceByUserId(int userId) {


        String sql = "select balance\n" +
                "from account\n" +
                "where user_id = ?;";

        SqlRowSet results = null;
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);


        return balance;
    }

    public BigDecimal addToAcctBalance (BigDecimal amountAdded, int accountId){

        String sql = "UPDATE account\n" +
                "SET balance = balance + ?\n" +
                "WHERE account_id = ? RETURNING balance;";

        BigDecimal updatedBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class,amountAdded, accountId);


        return updatedBalance;
    }

    public BigDecimal subtractFromAcctBalance (BigDecimal amountSubtracted, int accountId) {

        String sql = "UPDATE account\n" +
                "SET balance = balance - ?\n" +
                "WHERE account_id = ? RETURNING balance;";

        BigDecimal updatedBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class,amountSubtracted, accountId);


        return updatedBalance;
    }

    private Account mapRowToAccountBalance (SqlRowSet results) {

        Account userAccount = new Account();

        int accountId = results.getInt("account_id");
        userAccount.setAccountId(accountId);

        int userId = results.getInt("user_id");
        userAccount.setUserId(userId);

        BigDecimal balance = results.getBigDecimal("balance");
        userAccount.setBalance(balance);

        return userAccount;
    }
}
