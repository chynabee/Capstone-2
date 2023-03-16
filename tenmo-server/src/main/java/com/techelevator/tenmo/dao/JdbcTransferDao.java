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

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }





    @Override
    public List<Transfer> getTransferByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, amount, account_from, account_to\n" +
                "FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);

        }
        return transfers;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = new Transfer();

        String sql = " SELECT transfer_id, transfer_type_id, transfer_status_id, amount, account_from, account_to\n" +
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

    @Override
    public Transfer transferAccountTo(int accountTo) {
        Transfer transfer = new Transfer();
        String sql ="Select account_to\n" +
                "From transfer\n" +
                "where account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountTo);

        return transfer;
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
