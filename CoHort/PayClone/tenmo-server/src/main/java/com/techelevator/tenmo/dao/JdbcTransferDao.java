package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private String senderUsername;

    private Principal principal;


    @Autowired
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public List<Transfer> findTransferHistoryByUsername(String username) {
        return null;
    }

    @Override
    public List<Transfer> findPendingTransfersByUsername(String username) {
        return null;
    }



    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.queryForRowSet(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                    transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating transfer", e);
        }
    }






    @Override
    public void sendTransfer( Transfer transfer) {
               String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?,?,?)";
        try {
            jdbcTemplate.update(sql,transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


   @Override
public List<Transfer> viewTransferHistory(String username) throws SQLException {
    List<Transfer> viewTransfers = new ArrayList<>();

    String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                 "FROM transfer " +
                 "INNER JOIN account ON transfer.account_from = account.account_id " +
                 "INNER JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                 "WHERE tenmo_user.username = ?";

    SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
    while (rs.next()) {
        Transfer transfer = mapRowToTransfer(rs);
        viewTransfers.add(transfer);
    }
    return viewTransfers;
}


    @Override
    public List<Transfer> findPendingTransfersByUsername(Transfer transfer, Principal principal) throws SQLException {
        String username = principal.getName();
        List<Transfer> pendingTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "INNER JOIN account ON transfer.account_from = account.account_id " +
                "INNER JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username ILIKE ? AND transfer_status_id = 1";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        while (rs.next()) {
            transfer = mapRowToTransfer(rs);
            findPendingTransfersByUsername(username).add(transfer);
            pendingTransfers.add(transfer);
        }
        return pendingTransfers;
    }

    @Override
    public Transfer findTransferById(long id)  {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            return mapRowToTransfer(rs);
        } else {
            return null;
        }
    }

    @Override
    public void approveTransfer(long id) {

        String sql = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = 1";
        jdbcTemplate.update(sql, id);

    }

    @Override
    public void rejectTransfer(long id) {
        String sql = "UPDATE transfer SET transfer_status_id = 3 WHERE transfer_id = 1";
        jdbcTemplate.update(sql, id);
    }

 public List<Transfer> RequestTransfersByUsername(String username, int statusId) throws SQLException {
    List<Transfer> requestTransfers = new ArrayList<>();
    String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                 "FROM transfer " +
                 "INNER JOIN account ON transfer.account_from = account.account_id " +
                 "INNER JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                 "WHERE tenmo_user.username ILIKE ? AND transfer_status_id = ?";

    SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username, statusId);
    while (rs.next()) {
        Transfer transfer = mapRowToTransfer(rs);
        requestTransfers.add(transfer);
    }
    return requestTransfers;
}


    @Override
    public List<Transfer> SendTransfersByUsername(String username) throws SQLException {
        List<Transfer> sendTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "INNER JOIN account ON transfer.account_from = account.account_id " +
                "INNER JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username ILIKE ? AND transfer_status_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);
        while (rs.next()) {
            Transfer transfer = mapRowToTransfer(rs);
            sendTransfers.add(transfer);
        }
        return sendTransfers;
    }


    @Override
    public List<Transfer> getTransfersByUsername(String username) throws SQLException {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "INNER JOIN account ON transfer.account_from = account.account_id " +
                "INNER JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE tenmo_user.username ILIKE ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, username);

        while (rs.next()) {
            Transfer transfer = mapRowToTransfer(rs);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> updateTransferStatus(int id) {
        return null;
    }



    @Override
    public Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        try{
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));

            transfer.setAmount(rs.getBigDecimal("amount"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transfer;
    }
}
