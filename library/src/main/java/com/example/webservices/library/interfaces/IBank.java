package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.exceptions.BankException;
import java.math.BigDecimal;

public interface IBank {
    void transferMoney(AccountDto customerId, AccountDto merchantId, BigDecimal amount, String description) throws BankException;
    String addAccount(AccountDto account) throws BankException;
    String addAccount(AccountDto accountDto, BigDecimal startingBalance) throws BankException;
    void retireAccount(AccountDto account) throws BankException;
    BigDecimal getBalance(AccountDto account) throws BankException;
}
