package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private String approvedStatus = "Approved";
    private String transferRequest = "Request";


    private final JdbcTemplate jdbcTemplate;

    private final JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
    }


   @Override
    public Transfer createTransfer(int accountTo, int accountFrom, BigDecimal amount, BigDecimal balance) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, accountTo, accountFrom, amount, 2, 1);

        return null;

    }

    @Override
    public Transfer getTransferByAccountId(int accountId) {
        return null;
    }

    @Override
    public Transfer insertTransfer(Transfer transfer) {
        Transfer transfer1 = null;
        String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(2,2, ?, ?, ?) RETURNING transfer_id";

        int transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        //transfer1 = mapResultToTransfer(results);
        return getTransferByTransferId(transferId);
    }

    @Override
    public List<Transfer> getTransferByUserId(int userId) {
        return null;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, amount, account_from, account_to\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,transferId);

        if(results.next()){
            transfer = mapRowToTransfer(results);
        }
        return transfer;

    }

    @Override
    public Transfer getTransferByTypeId(int typeId) {
        Transfer transfer = new Transfer();

        String sql = "Select transfer_type_id\n" +
                "From transfer\n" +
                "where transfer_type_id = ?;\n";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,typeId);

        return transfer;
    }

    @Override
    public Transfer getTransferByStatus(int statusId) {

        Transfer transfer = new Transfer();

        String sql = "Select transfer_status_id\n" +
                "From transfer\n" +
                "where transfer_status_id = ?;\n";

        SqlRowSet  results = jdbcTemplate.queryForRowSet(sql,statusId);

        return transfer;

    }

    @Override
    public BigDecimal getTransferAmount(int amount) {

        String sql = "Select amount\n" +
                "From transfer\n" +
                "where amount = ?;";
        BigDecimal updateAmount = jdbcTemplate.queryForObject(sql,BigDecimal.class, amount);

        return updateAmount;
    }

    @Override
    public Transfer transferAccountFrom(int accountFrom) {
        Transfer transfer = new Transfer();

        String sql ="Select account_from\n" +
                "From transfer\n" +
                "where account_from = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountFrom);


        return transfer;
    }

    public List<Transfer> getTransferListByAccountId(int accountId){

        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, amount, account_from, account_to\n" +
                "FROM transfer \n" +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer transferAccountTo(int accountTo) {
        Transfer transfer = new Transfer();
        String sql ="Select account_to\n" +
                "From transfer\n" +
                "where account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountTo);

        return transfer;
    }

    public String sendTransfer(int accountFrom, int accountTo, BigDecimal amount) {
        if(accountFrom == accountTo) {
            return "Please Try Again, unable to send transfer to same account!";
        }
        if ((amount.compareTo(jdbcAccountDao.getBalanceByAcctId(accountFrom)) == 1 || amount.compareTo(jdbcAccountDao.getBalanceByAcctId(accountFrom)) == 0) && amount.compareTo(new BigDecimal(0)) == -1) {


            jdbcAccountDao.addToAcctBalance(amount, accountTo);
            jdbcAccountDao.subtractFromAcctBalance(amount, accountFrom);
            Transfer transferToTrack = new Transfer();
            transferToTrack.setAccountFrom(accountFrom);
            transferToTrack.setAccountTo(accountTo);
            transferToTrack.setAmount(amount);
            transferToTrack.setTransferStatusId(2);
            transferToTrack.setTransferTypeId(1);

            return "Your transfer is complete!";

        } else {

            return "Transfer cannot be completed!";
        }

    }

    private Transfer mapRowToTransfer(SqlRowSet results){
        Transfer transfer = new Transfer();

        int transferId = results.getInt("transfer_id");
        transfer.setTransferId(transferId);

        int transferTypeId = results.getInt("transfer_type_id");
        transfer.setTransferTypeId(transferTypeId);

        int transferStatusId = results.getInt("transfer_status_id");
        transfer.setTransferStatusId(transferStatusId);

        BigDecimal amount = results.getBigDecimal("amount");
        transfer.setAmount(amount);

        int transferAccountFrom = results.getInt("account_from");
        transfer.setAccountFrom(transferAccountFrom);

        int transferAccountTo = results.getInt("account_to");
        transfer.setAccountTo(transferAccountTo);


        return transfer;
    }


}
