package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.exceptions.BankException;
import java.math.BigDecimal;

public interface IBank {
    /**
     * @param debtor account who owes money to the creditor
     * @param creditor account who is owed money by the debtor
     * @param amount amount of money owed
     * @param description message describing the money transfer
     * @throws BankException when the transfer could not be processed
     */
    void transferMoney(AccountDto debtor, AccountDto creditor, BigDecimal amount, String description) throws BankException;

    /**
     * @param accountDto the account creation information
     * @param startingBalance positive integer denoting the money the account has
     * @return creates a new account in the bank and returns the ID of that new user
     * @throws BankException when the balance is illegal or the CPR is duplicated
     */
    String addAccount(AccountDto accountDto, BigDecimal startingBalance) throws BankException;

    /**
     * @param account account to retire
     * @throws BankException when no such account exists
     */
    void retireAccount(AccountDto account) throws BankException;

    /**
     * @param account account to search for
     * @return the balance of the given account
     * @throws BankException when no such account exists
     */
    BigDecimal getBalance(AccountDto account) throws BankException;
}
