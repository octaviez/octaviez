package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {
    AuthenticatedUser currentUser = new AuthenticatedUser();

    public UserService(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    private String currentToken() {
        return currentUser.getToken();
    }

    private static final String API_BASE_URL = "http://localhost:8080/tenmo_user/";
    private RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<Void> authToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentToken());
        return new HttpEntity<>(headers);
    }
    private HttpEntity<User> userBody(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentToken());
        return new HttpEntity<>(user, headers);
    }


    public List<User> getUserList() {
        User user = new User();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());


        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL+ "users", HttpMethod.GET, userBody(user), User[].class);
            return Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            BasicLogger.log("Error retrieving user list: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public int getUserById(int id) {
        User user = null;
        ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "{user_id}/" + id, HttpMethod.GET, authToken(), User.class);
        user = response.getBody();
        return user.getId();
    }


    public int findIdByUsername(String recipientUsername) {
        User[] users = getUserList().toArray(new User[0]);
        int recipientId = 0;
        for (User user : users) {
            if (user.getUsername().equals(recipientUsername)) {
                recipientId = user.getId();
            }
        }
        return recipientId;
    }
}