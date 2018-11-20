package com.revolut.resources.transform;

import com.revolut.domain.*;
import com.revolut.resources.dto.AccountDTO;
import com.revolut.resources.dto.BankDTO;
import com.revolut.resources.dto.TransactionDTO;
import com.revolut.resources.dto.UserDTO;

import java.util.Date;
import java.util.UUID;

public class Transformer {

    public static Account toEntity(AccountDTO dto, User owner){
        Account account=new Account();
        account.setBalance(dto.getBalance());
        account.setAccountName(dto.getBankAccountName());
        account.setAccountNumber(dto.getAccountNumber());
        account.setSortCode(dto.getSortCode());
        account.setCurrency(dto.getCurrency());
        account.setOwner(owner);
        account.setCreationDate(new Date());
        return account;
    }

    public static Bank toEntity(BankDTO dto) {
        return new Bank(dto.getName());
    }

    public static User toEntity(UserDTO dto, Bank bank) {
        return new User(bank, dto.getName());
    }

    public static Transaction toEntity(TransactionDTO dto, Account from, Account to) {
        Transaction transaction=new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setType(dto.getType());
        transaction.setDateAccepted(new Date());
        transaction.setStatus(TransactionStatus.ACCEPTED);
        transaction.setUuid(UUID.randomUUID());
        return transaction;
    }

    public static UserDTO toDto(User user) {
        return new UserDTO(user.getName(), user.getBank().getName());
    }

}
