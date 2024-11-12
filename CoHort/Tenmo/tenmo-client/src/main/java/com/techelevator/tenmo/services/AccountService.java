package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/account/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticatedUser currentUser;
    private final Account account = new Account();


    public AccountService(AuthenticatedUser currentUser) {

        this.currentUser = currentUser;
    }

    private String currentToken() {
        return currentUser.getToken();
    }

    private HttpEntity<Void> authToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentToken());
        return new HttpEntity<>(headers);
    }
    private HttpEntity<Account> accountBody(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentToken());
        return new HttpEntity<>(account, headers);
    }

    // Method to get account balance for the authenticated user
    //TODO ::: DONT TOUCH THIS
    public BigDecimal viewBalance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "balance"  , HttpMethod.GET, authToken(), Account.class);
            Account account = response.getBody();
            if (account != null) {
                return account.getBalance();
            }
            else {
                BasicLogger.log("Failed to retrieve balance. HTTP response code: " + response.getStatusCodeValue()+ "  "+ response.getStatusCode());
                return null;
            }
        } catch (RestClientException e) {
            BasicLogger.log("Error retrieving balance: " + e.getMessage());
            return null;
        }
    }

public int getAccountByUserId(int userId) {
     userId = currentUser.getUser().getId();
    int accountId = 0;
    try {
        ResponseEntity<Account> response = restTemplate.exchange(
                API_BASE_URL + userId , HttpMethod.GET, authToken(), Account.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
           accountId = response.getBody().getAccountId();
            return accountId;
        } else {
            BasicLogger.log("Failed to retrieve account details. HTTP response code: " + response.getStatusCodeValue());
            return 0;
        }
    } catch (RestClientResponseException | ResourceAccessException e) {
        BasicLogger.log("Error: Unable to retrieve account details. " + e.getMessage());
        return 0;
    }
}


}