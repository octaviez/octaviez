package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    private TransferService transferService;
    private UserService userService = new UserService(currentUser);
    private AccountService accountService = new AccountService(currentUser);
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            accountService = new AccountService(currentUser);
            transferService = new TransferService(currentUser);
            userService = new UserService(currentUser);
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }

    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    //TODO ::: DONT TOUCH THIS
    private void viewCurrentBalance() {

        System.out.println("Your current account balance is: $" + accountService.viewBalance());
    }

    //TODO :: PRINT FORMAT ALSO DONT TOUCH THIS
    private void viewTransferHistory() {
    List<Transfer> transferHistory = transferService.viewTransferHistory();

    if (transferHistory.isEmpty()) {
        System.out.println("There are no transfers to display.");
        BasicLogger.log("Empty Transfers.");
    } else {
        System.out.println("Transfer History:");
        for (Transfer transfer : transferHistory) {
            System.out.println(transfer);
        }
    }
}



    private void viewPendingRequests() {
       Transfer[] pendingRequests = transferService.viewPendingTransfers( currentUser);
       if (pendingRequests != null) {
           System.out.println("Pending Requests: " + pendingRequests.toString());
       } else {
           System.out.println("Failed to retrieve pending requests.");
              BasicLogger.log("Failed to retrieve pending requests.");
       }
   }

    private void sendBucks() {
        List<User> users = userService.getUserList();

        if (users.isEmpty()) {
            System.out.println("No users available to send bucks.");
            return;
        }

        System.out.println("Choose a user to send bucks to:");
        for (User user : users) {
            System.out.printf("User ID: %d, Username: %s\n", user.getId(), user.getUsername());
        }

        int recipientId = consoleService.promptForInt("Enter the recipient's user ID: ");

        BigDecimal amount = consoleService.promptForBigDecimal("Enter the amount to send: ");

        boolean success = transferService.sendBucks(recipientId, amount);
        if (success) {
            System.out.println("Transfer successful!");
        } else {
            System.out.println("Transfer failed. Please check the details and try again.");
            BasicLogger.log("Transfer failed. User ID: " + recipientId + ", Amount: " + amount);
        }
    }


    private void requestBucks() {
         User[] users = userService.getUserList().toArray(new User[0]);

        for (User user : users) {
            System.out.println(user);
        }
        int requestFromUserId = consoleService.promptForInt("Enter userID to request from: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to request: ");

        Transfer requested = transferService.requestBucks(requestFromUserId, amount);
        if (requested != null) {
            System.out.println("Request successful!");
            BasicLogger.log("Request successful!");
        } else {
            System.out.println("Failed to request TE Bucks.");
            BasicLogger.log("Failed to request TE Bucks.");
        }
    }

//TODO MAKE SERVICE CLASSESS WITH THESE METHODS, THEY WORK!!!!!
    //TODO REQUESTS ANDSEND REJECTING POST AND PUT, NEED TO FIX
    //TODO FIGURE OUT BODIES FOR METHODS AND DO TRANSFER SERVICE
    // TODO MIGRATE ALL HTTP STUFF TO BE HANDLED THERE, AND MAKE SURE TO USE THE CORRECT METHODS
    @Override
    public String toString() {

        return super.toString();
    }
}
