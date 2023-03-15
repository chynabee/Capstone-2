package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;


import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

        Account getAcctByUserId(int userid);

        BigDecimal getBalanceByAcctId( int accountId);

        BigDecimal getBalanceByUserId( int userId);



}
