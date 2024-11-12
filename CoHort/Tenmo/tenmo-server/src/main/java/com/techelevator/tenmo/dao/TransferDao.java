package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

public interface TransferDao {

    List<Transfer> findTransferHistoryByUsername(String username);




    List<Transfer> findPendingTransfersByUsername(String username);


    void createTransfer(Transfer transfer) throws SQLException;

    void sendTransfer(Transfer transfer);

    List<Transfer> viewTransferHistory(String username) throws SQLException;

    List<Transfer> findPendingTransfersByUsername(Transfer transfer, Principal principal) throws SQLException;

    Transfer findTransferById(long id) throws SQLException;

    void approveTransfer(long id);

    void rejectTransfer(long id);

    List<Transfer> RequestTransfersByUsername(String username, int StatusId) throws SQLException;

    List<Transfer> SendTransfersByUsername(String username) throws SQLException;

    List<Transfer> getTransfersByUsername(String username) throws SQLException;

    List <Transfer> updateTransferStatus(int id);



    Transfer mapRowToTransfer(SqlRowSet rs) throws SQLException;


}
