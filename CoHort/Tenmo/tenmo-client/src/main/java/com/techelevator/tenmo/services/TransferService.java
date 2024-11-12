package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/transfer/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private final UserService userService = new UserService(currentUser);
    private final AccountService accountService = new AccountService( currentUser);

    private final Account account = new Account();

    public TransferService( AuthenticatedUser currentUser) {

        this.currentUser = currentUser;
    }
    private String currentToken() {
        return currentUser.getToken();
    }
    private HttpEntity<Void> authToken() {
        HttpHeaders headers = new HttpHeaders();
        String token = currentToken();
        BasicLogger.log("Token: " + token);
        headers.setBearerAuth(currentToken());
        return new HttpEntity<>(headers);
    }
    private HttpEntity<Transfer> transferBody(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentToken());
        return new HttpEntity<>(transfer, headers);
    }

    public List<Transfer> viewTransferHistory() {

    try {
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "history", HttpMethod.GET, authToken(), Transfer[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Transfer[] transfers = response.getBody();
            if (transfers != null && transfers.length > 0) {
                return Arrays.asList(transfers);
            } else {
                BasicLogger.log("No transfers found.");
                return new ArrayList<>();
            }
        } else {
            BasicLogger.log("Failed to retrieve transfer history. HTTP response code: " + response.getStatusCodeValue());
            return new ArrayList<>();
        }
    } catch (RestClientException e) {
        BasicLogger.log("Error retrieving transfer history: " + e.getMessage());
        return new ArrayList<>();
    }
}




    //TODO ::::::: DONT TOUCH THIS METHOD
    public boolean sendBucks(int recipientId, BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());

        Transfer transfer = new Transfer();
        transfer.setAccountFrom(currentUser.getUser().getId());
        transfer.setAccountTo(recipientId);
        transfer.setAmount(amount);
        transfer.setTransferStatusId(2);
        transfer.setTransferTypeId(2);




        try {
            restTemplate.exchange(API_BASE_URL + "send", HttpMethod.POST, transferBody(transfer), Transfer.class);
            return true;
        } catch (RestClientException e) {
            BasicLogger.log("Error sending bucks: " + e.getMessage());
            return false;
        }
    }

public Transfer requestBucks(int accountTo, BigDecimal amount) {
    try {
        Transfer newRequest = new Transfer();
        newRequest.setTransferTypeId(1);
        newRequest.setTransferStatusId(1);
        newRequest.setAccountFrom(accountService.getAccountByUserId(currentUser.getUser().getId()));
        newRequest.setAccountTo(accountTo);
        newRequest.setAmount(amount);



        ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "request", HttpMethod.POST, transferBody(newRequest), Transfer.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return newRequest;
        } else {
            BasicLogger.log("Failed to request TE Bucks. HTTP response code: " + response.getStatusCodeValue());
            BasicLogger.log("Error response: " + response.getBody());
            return null;
        }
    } catch (HttpClientErrorException | HttpServerErrorException e) {
        BasicLogger.log("HTTP Status Code: " + e.getStatusCode());
        BasicLogger.log("HTTP Response Body: " + e.getResponseBodyAsString());
        return null;
    } catch (RestClientException e) {
        BasicLogger.log("Error: Unable to request TE Bucks. " + e.getMessage());
        return null;
    }
}


    public Transfer[] viewPendingTransfers(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/pending" + authenticatedUser.getUser().getId() + "/pending", HttpMethod.GET, authToken(), Transfer[].class);
        transfers = response.getBody();
        return transfers;
    }




}

