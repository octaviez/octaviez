package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.tenmo.model.User;
import org.springframework.web.server.ResponseStatusException;
import com.techelevator.tenmo.Util.BasicLogger;

@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")

public class TransferController {

    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final UserDao userDao;
    private Transfer transfer;
    private AccountController accountController;


    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao ;
        this.userDao = userDao;

    }

    //TODO :: DONT TOUCH THIS METHOD
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll")
    public void sendTransfer(@RequestBody Transfer transfer) throws SQLException {
        Account sender = accountDao.getAccountByUserId(transfer.getAccountFrom());
        Account recipient = accountDao.getAccountByUserId(transfer.getAccountTo());

        if (sender == null || recipient == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender or recipient account does not exist");
        }

        if (sender.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        transfer.setAccountFrom(sender.getAccountId());
        transfer.setAccountTo(recipient.getAccountId());

        transfer.setTransferStatusId(2);
        transfer.setTransferTypeId(2);

        sender.setBalance(sender.getBalance().subtract(transfer.getAmount()));
        recipient.setBalance(recipient.getBalance().add(transfer.getAmount()));

        accountDao.updateAccountBalance(sender);
        accountDao.updateAccountBalance(recipient);

        transferDao.sendTransfer(transfer);
    }


    @GetMapping("/history")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    //TODO: USER THE PRINCIPAL TO EXTRACT AUTH'D USER NAME SILLY GEESES
    //TODO: REFACTOR *NEW* EXISTING CONTROLLER METHODS TO THIS PATTERN-ISH
    public List<Transfer> viewTransferHistory(Principal principal) throws SQLException {

            String username = principal.getName();
            List<Transfer> transfers = transferDao.viewTransferHistory(username);
            if (transfers == null || transfers.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found");
            }
            return transfers;
        }

    @GetMapping("/pending")
    @PreAuthorize("permitAll")
    //TODO: USER THE PRINCIPAL TO EXTRACT AUTH'D USER NAME SILLY GEESES
    public List<Transfer> viewPendingTransfers(Principal principal) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        transferDao.findPendingTransfersByUsername(principal.getName());
        try{
            return  pendingTransfers;
        } catch (DataAccessException e) {
            BasicLogger.log("Failed to retrieve pending transfers: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

    }


    @PostMapping("/pending/{id}/approve")
    @PreAuthorize("permitAll")
    public void approveTransfer(@PathVariable int id) {
        try {
            transferDao.approveTransfer(id);
        } catch (DaoException e) {
            BasicLogger.log("Failed to approve transfer: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping ("/pending/{id}/reject")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    public void rejectPendingTransfer(Principal principal, @PathVariable int id) {
        String username = principal.getName();
        id = transfer.getTransferId(id);

        try {
            transferDao.rejectTransfer(id);
        } catch (DaoException e) {
            BasicLogger.log("Failed to reject transfer: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }


    @PostMapping("/request")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        String username = principal.getName();
        try {
        int id = transfer.getAccountFrom();

        transfer.setTransferTypeId(1);
        transfer.setTransferStatusId(1);
        transfer.setAccountFrom(id);
        transfer.setAccountTo(transfer.getAccountTo());
        transfer.setAmount(transfer.getAmount());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            transferDao.createTransfer(transfer);
            return transfer;
        } catch (DaoException e) {
            BasicLogger.log("Failed to create transfer request: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create transfer request");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}
