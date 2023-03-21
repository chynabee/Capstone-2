package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/accounts")
public class AccountController {

    @Autowired
    private JdbcAccountDao accountDao;

    @Autowired
    private UserDao userDao;

    public AccountController(JdbcAccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/{accountId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByAcctId (@PathVariable int accountId) {
        return accountDao.getBalanceByAcctId(accountId);
    }

    @RequestMapping(path = "/user/{userId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId (@PathVariable int userId) {
        return accountDao.getBalanceByUserId(userId);
    }


}
