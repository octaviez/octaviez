package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Util.BasicLogger;
import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;


    private TransferDao transferDao;

    //TODO: REMOVE COMMENTED OUT BROKEN CODE
    //private final TokenProvider TokenProvider;


    public AccountController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) throws SQLException {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;

    }

    //TODO: DONT TOUCH THIS
    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public Account viewBalance() throws SQLException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountDao.findBal(username);

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }


        return account;
    }


    /*
    @GetMapping("/addBalance")
    @PreAuthorize("permitAll")
    public BigDecimal addBalance(BigDecimal newBal) throws SQLException {
        try {
            return accountDao.addBalance(newBal);
        } catch (ResponseStatusException e) {
            BasicLogger.log("Failed to send transfer: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

    }


     */

    /*
    @GetMapping("/subtractBalance")
    @PreAuthorize("permitAll")
    public long subtractBalance(Principal principal, BigDecimal amount) throws SQLException {
        String username = principal.getName();
        try {
            return accountDao.subtractBalance(username, amount);
        } catch (ResponseStatusException e) {
            BasicLogger.log("Failed to send transfer: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);

        }

        //TODO: REMOVE COMMENTED OUT BROKEN CODE
        //    private boolean isTokenValid(String token) {
        //        return TokenProvider.validateToken(token);
        //    }

    }

     */
    @GetMapping("/{userId}")
    @PreAuthorize("permitAll")
    public Account getAccountByUserId(int userId) {
        return accountDao.getAccountByUserId(userId);
    }
}