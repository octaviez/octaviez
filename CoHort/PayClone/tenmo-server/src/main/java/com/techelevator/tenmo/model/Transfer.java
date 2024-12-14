package com.techelevator.tenmo.model;

import com.techelevator.tenmo.dao.JdbcAccountDao;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Transfer {

    private int transferId;
    private String senderUsername;
    private String recipientUsername;
    private BigDecimal amount;
    private JdbcAccountDao jdbcAccountDao;
    private int transferStatusId;
    private int transferTypeId;
    private int accountFrom;
    private int accountTo;


    public Transfer() {

    }

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferId =  transferId;
        this.transferTypeId =  transferTypeId;
        this.transferStatusId =  transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    // Getters and setters
    public int getTransferId(int id) {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }



    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public BigDecimal getAmount() throws SQLException {
        setAmount(amount);
    return amount;
}

    public void setAmount(BigDecimal amount) throws SQLException {
        this.amount = amount;
    }

    public int getTransferStatusId() {
        return this.transferId;
    }

    public int getTransferId() {
        return transferId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public JdbcAccountDao getJdbcAccountDao() {
        return jdbcAccountDao;
    }


    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    @Override
    public String toString() {

        return "Transfer{" +
                "transfer_id=" + transferId +
                ", transfer_type_id=" + transferTypeId +
                ", transfer_status_id=" + transferStatusId +
                ", account_from=" + accountFrom +
                ", account_to=" + accountTo+
                ", amount=" + amount +
                '}';
    }
}
