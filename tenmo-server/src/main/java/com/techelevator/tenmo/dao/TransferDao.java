package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransferByUserId(int userId);

    Transfer getTransferByTransferId(int transferId);

    Transfer getTransferByTypeId(int typeId);

    Transfer getTransferByStatus(int statusId);

    BigDecimal getTransferAmount(int amount);

    Transfer transferAccountFrom( int accountFrom);

    Transfer transferAccountTo( int accountTo);

    Transfer createTransfer (Transfer transfer);




}
