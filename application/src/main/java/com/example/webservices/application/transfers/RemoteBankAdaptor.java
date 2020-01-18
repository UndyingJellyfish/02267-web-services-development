package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.interfaces.IBank;
import dtu.ws.fastmoney.*;

import javax.xml.ws.Service;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
public class RemoteBankAdaptor implements IBank {

    private Service bankServiceService;

    public RemoteBankAdaptor() {
        this.bankServiceService = new BankServiceService();
    }

    @Override
    public void transferMoney(AccountDto customer, AccountDto merchant, BigDecimal amount, String description) throws BankException {
            BankService bankService = bankServiceService.getPort(BankService.class);
        try {
            bankService.transferMoneyFromTo(
                    customer.getBankAccountId(),
                    merchant.getBankAccountId(),
                    amount,
                    description);
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
    }

    @Override
    public String addAccount(AccountDto account) throws BankException {
        return addAccount(account, BigDecimal.ZERO);
    }
    @Override
    public String addAccount(AccountDto account, BigDecimal balance) throws BankException{
        BankService bankService = bankServiceService.getPort(BankService.class);
        User user = new User();
        setupUserName(account.getName(), user);
        user.setCprNumber(account.getIdentifier());
        try {
            return bankService.createAccountWithBalance(user, balance);
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
    }

    @Override
    public void retireAccount(AccountDto account) throws BankException {
        BankService bankService = bankServiceService.getPort(BankService.class);
        try {
            bankService.retireAccount(account.getBankAccountId());
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getBalance(AccountDto account) throws BankException {
        BankService bankService = bankServiceService.getPort(BankService.class);
        Account bankAccount = null;
        try {
            bankAccount = bankService.getAccount(account.getBankAccountId());
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
        return bankAccount.getBalance();
    }

    private void setupUserName(String name, User user) {
        List<String> names = Arrays.asList(name.split(" "));
        List<String> firstNames = names.subList(0, names.size() - 1);
        String firstName = String.join(" ", firstNames);
        String lastName = names.get(names.size() - 1);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }
}
