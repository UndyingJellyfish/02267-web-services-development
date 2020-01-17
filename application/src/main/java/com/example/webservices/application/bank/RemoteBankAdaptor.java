package com.example.webservices.application.bank;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
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
    public String addAccount(AccountDto account) throws BankException, ClassNotFoundException {
        return addAccount(account, BigDecimal.ZERO);
    }

    public String addAccount(AccountDto account, BigDecimal balance) throws BankException, ClassNotFoundException {
        try{
            if (account.getType() == AccountType.CUSTOMER) {
                return addAccountCustomer(account, balance);
            } else if (account.getType() == AccountType.MERCHANT) {
                return addAccountMerchant(account, balance);
            }
        }catch(BankServiceException_Exception e){
            throw new BankException(e.getMessage());
        }
        throw new ClassNotFoundException();
    }

    private String addAccountCustomer(AccountDto customer, BigDecimal balance) throws BankServiceException_Exception {
        return addAccountCommon(customer, customer.getIdentifier(), balance);
    }

    private String addAccountMerchant(AccountDto merchant, BigDecimal balance) throws BankServiceException_Exception {
        return addAccountCommon(merchant, merchant.getIdentifier(), balance);
    }

    private String addAccountCommon(AccountDto account, String cpr, BigDecimal balance) throws BankServiceException_Exception {
        BankService bankService = bankServiceService.getPort(BankService.class);
        User user = new User();
        setupUserName(account.getName(), user);
        user.setCprNumber(cpr);
        return bankService.createAccountWithBalance(user, balance);
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
