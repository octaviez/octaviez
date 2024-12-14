package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class RequestBucksDTO {
    private String requestFromUsername;
    private BigDecimal amount;

    public String getRequestFromUsername() {
        return requestFromUsername;
    }

    public void setRequestFromUsername(String requestFromUsername) {
        this.requestFromUsername = requestFromUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}