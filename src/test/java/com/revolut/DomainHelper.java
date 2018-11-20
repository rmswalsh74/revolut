package com.revolut;

import com.revolut.domain.*;
import com.revolut.event.command.CreateAccountCommandEvent;
import com.revolut.event.command.CreateBankCommandEvent;
import com.revolut.event.command.CreateUserCommandEvent;
import com.revolut.event.command.IntraBankTransferCommandEvent;
import com.revolut.resources.dto.BankDTO;
import com.revolut.resources.dto.UserDTO;

import java.util.*;

public class DomainHelper {

    public static BankDTO bankDTO(String name) {
        return new BankDTO(name);
    }

    public static UserDTO userDTO(String name, String bankName){
        return new UserDTO(name, bankName);
    }

    public static Bank bank(String name) {
        return new Bank(name);
    }

    public static User user(Bank bank, String name) {
        return new User(bank, name);
    }

    public static Account account(Bank bank, User owner, Integer accountNumber, String sortCode) {
        return new Account(accountNumber, "Savings", sortCode, Currency.getInstance(Locale.UK), owner);
    }

    public static Transaction transaction(Account from, Account to, double amount) {
        return new Transaction(UUID.randomUUID(), from, to, amount, TransactionType.TRANSFER, TransactionStatus.ACCEPTED, new Date());
    }

    public static CreateBankCommandEvent bankCommandEvent(Bank bank) {
        return new CreateBankCommandEvent(bank);
    }

    public static CreateUserCommandEvent userCommandEvent(User user) {
        return new CreateUserCommandEvent(user);
    }

    public static CreateAccountCommandEvent accountCommandEvent(Account account) {
        return new CreateAccountCommandEvent(account);
    }

//    public static IntraBankTransferCommandEvent transferCommandEvent(Integer from, Integer to, double amount, Optional<Date> dateToTransfer) {
//        return new IntraBankTransferCommandEvent(new Transaction(UUID.randomUUID(), from, to, amount, dateToTransfer));
//    }
}
