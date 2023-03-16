package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.token.Token;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated")
@RestController
public class TransferController {
    /// setup transfer parameters for clients#5
    //will include all Daos, restcontrollers, and preauthorize
    private final UserDao userDao;
    private final AccountDao accountDao;
    private final TransferDao transferDao;

    public TransferController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    //get Transfer
    //request transfer
    //
    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int transferId) {

        Transfer transfer = null;
        transfer = transferDao.getTransferByTransferId(transferId);


        return transferDao.getTransferByTransferId(transferId);

    }

    @RequestMapping(path = "/gettransfertype", method = RequestMethod.GET)
    public Transfer getTransferByTypeId(int typeId) {

        return transferDao.getTransferByTypeId(typeId);
    }

    @RequestMapping(path = "/mytransfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersForUser(Principal principal) {
        return transferDao.getTransferByUserId(userDao.findIdByUsername(principal.getName()));

    }

    @RequestMapping(path = "/transferstatus", method = RequestMethod.POST)
    public Transfer getTransferByStatus(int statusId) {
        return transferDao.getTransferByStatus(statusId);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public ResponseEntity<String> sendTransfer(@Valid @RequestBody Transfer transfer) {
        Transfer updatedTransfer = null;

        if(transfer.getAccountTo()==transfer.getAccountFrom()){return new ResponseEntity<String>
                ("Please try again. Unable to complete transfer into the same account!", HttpStatus.BAD_REQUEST);}

        BigDecimal accountFrom = accountDao.getBalanceByAcctId(transfer.getAccountFrom());
        int compare = accountFrom.compareTo(transfer.getAmount());

        if (compare >= 0) {
            updatedTransfer = transferDao.createTransfer(transfer);
        }
        if (updatedTransfer != null) {
            accountDao.addToAcctBalance(transfer.getAmount(), transfer.getAccountTo());
            accountDao.subtractFromAcctBalance(transfer.getAmount(), transfer.getAccountFrom());

        } else {
            return new ResponseEntity<String>("Insufficient Funds can not process", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Transfer complete", HttpStatus.CREATED);
    }




}
