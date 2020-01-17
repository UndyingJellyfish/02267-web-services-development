package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.exceptions.BankException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class RemoteBankAdaptorTest {

    private static final BigDecimal startingBalance = new BigDecimal("100.");

    private RemoteBankAdaptor bank;
    private AccountDto customerDto;
    private AccountDto merchantDto;

    @Before
    public void Setup() {
        bank = new RemoteBankAdaptor();

        customerDto = new AccountDto(UUID.randomUUID(),"Bob Bobby", "010101-0101", AccountType.CUSTOMER);
        merchantDto = new AccountDto(UUID.randomUUID(),"Bob Bobby", "020202-0202", AccountType.MERCHANT);

        try {
            String customerAccountId = bank.addAccount(customerDto);
            customerDto.setBankAccountId(customerAccountId);
            String merchantAccountId = bank.addAccount(merchantDto);
            merchantDto.setBankAccountId(merchantAccountId);
        } catch (BankException | ClassNotFoundException e) {
            Assert.fail();
        }
    }

    @After
    public void teardown() {
        try {
            bank.retireAccount(customerDto);
            bank.retireAccount(merchantDto);
        } catch (BankException e) {
            Assert.fail();
        }
    }

    private void CheckBalance(AccountDto customer, BigDecimal customerBalance, AccountDto merchant, BigDecimal merchantBalance) {
        try {
            BigDecimal currentBalance = bank.getBalance(customer);
            Assert.assertEquals(customerBalance, currentBalance);
            currentBalance = bank.getBalance(merchant);
            Assert.assertEquals(merchantBalance, currentBalance);
        } catch (BankException e) {
            Assert.fail();
        }
    }

    @Test
    public void TestBalanceWasSetup() {

        // TODO this won't work as there is no public method for setting starting balance currently
        CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
    }

    @Test
    public void TestTransfer() {
        BigDecimal amount = new BigDecimal("50.0");
        try {
            CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
            bank.transferMoney(customerDto, merchantDto, amount, "Test transfer");
            CheckBalance(customerDto, startingBalance.subtract(amount), merchantDto, startingBalance.add(amount));
        } catch (BankException e) {
            Assert.fail();
        }
    }
}
