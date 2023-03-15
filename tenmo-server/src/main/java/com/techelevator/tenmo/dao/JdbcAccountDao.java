package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Account getAcctByUserId(int userid) {
        return null;
    }

    @Override
    public BigDecimal getBalanceByAcctId(int accountId) {
        return null;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        return null;
    }
}
