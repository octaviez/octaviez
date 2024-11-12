package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private Principal principal;



    //TODO: REMOVE COMMENTED OUT BROKEN CODE
    @Autowired
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }


    @Override
    public Account findBal(String username) throws SQLException {
        Account account = null;
        String sql = "SELECT account.account_id AS account_id, account.user_id AS user_id, account.balance AS balance " +
                "FROM account " +
                "JOIN tenmo_user u ON account.user_id = u.user_id " +
                "WHERE u.username = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
        if (rs.next()) {

            account = mapRowToAccount(rs);
        } else {
            throw new DaoException("Balance not found");
        }
        return account;
    }




 @Override
public BigDecimal addBalance(int userId, BigDecimal newBalance) {

     String sqlIncreaseBalance = "UPDATE account " +
             "SET balance = balance + ? " +
             "WHERE user_id = ?";

    try {
        int rowsAffected = jdbcTemplate.update(sqlIncreaseBalance, newBalance, userId);

        if (rowsAffected == 0) {
            throw new DaoException("Zero rows affected, expected at least one");
        }
    } catch (CannotGetJdbcConnectionException e) {
        throw new DaoException("Unable to connect to server or database", e);
    } catch (DataIntegrityViolationException e) {
        throw new DaoException("Data integrity violation", e);
    }

    return getBalanceByUserId(userId);
}



    public BigDecimal subtractBalance(int userId, BigDecimal newBalance) {
        String sqlDecreaseBalance = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?";


        try {
            int rowsAffected = jdbcTemplate.update(sqlDecreaseBalance, newBalance, userId);

            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return getBalanceByUserId(userId);
    }

    public Account getAccountByUserId ( int userId) {
            Account account = null;
            String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);

                while (rs.next()) {
                    try {
                        account = mapRowToAccount(rs);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }


            } return account;
        }
        public BigDecimal getBalanceByUsername (String username) {
            String sql = "SELECT balance FROM account " +
                    "JOIN tenmo_user u ON account.user_id = u.user_id " +
                    "WHERE u.username = ?";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
            if (rs.next()) {
                rs.getBigDecimal("balance");
            } else {
                throw new DaoException("Balance not found");
            }
            return rs.getBigDecimal("balance");
        }

        public BigDecimal getBalanceByUserId (int userId) {
            String sql = "SELECT balance FROM account WHERE user_id = ?";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
            if (rs.next()) {
                rs.getBigDecimal("balance");
            } else {
                throw new DaoException("Balance not found");
            }
            return rs.getBigDecimal("balance");
        }

    public void updateAccountBalance(Account account) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, account.getBalance(), account.getUserId());
    }


    private Account mapRowToAccount (SqlRowSet rs) throws SQLException {
            Account account = new Account();
            account.setAccountId(rs.getInt("account_id"));
            account.setUserId(rs.getInt("user_id"));
            account.setBalance(rs.getBigDecimal("balance"));
            return account;
        }


    }


