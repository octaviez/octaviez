package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface AccountDao {



    Account findBal(String username) throws SQLException;



    BigDecimal addBalance(int userId, BigDecimal newBalance);

    BigDecimal subtractBalance(int userId, BigDecimal amount);

    Account getAccountByUserId(int userId);
    public void updateAccountBalance(Account account);
}

