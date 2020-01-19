package com.example.webservices.payments;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.payments.services.RemoteBankAdaptor;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.fail;


public class RemoteBankAdaptorTest {

    private static final BigDecimal startingBalance = new BigDecimal("100.");

    private BankService bankService = new BankServiceService().getBankServicePort();

    private RemoteBankAdaptor bank;
    private AccountDto customerDto;
    private AccountDto merchantDto;

    @Before
    public void Setup() {
        bank = new RemoteBankAdaptor(this.bankService);

        customerDto = new AccountDto(UUID.randomUUID(),"Bob Bobby", "010101-0101", AccountType.CUSTOMER);
        merchantDto = new AccountDto(UUID.randomUUID(),"Alice Alicy", "020202-0202", AccountType.MERCHANT);

        try {
            String customerAccountId = bank.addAccount(customerDto, startingBalance);
            customerDto.setBankAccountId(customerAccountId);
            String merchantAccountId = bank.addAccount(merchantDto, startingBalance);
            merchantDto.setBankAccountId(merchantAccountId);
        } catch (BankException e) {
            fail();
        }
    }

    @After
    public void teardown() {
        try {
            bank.retireAccount(customerDto);
            bank.retireAccount(merchantDto);
        } catch (BankException e) {
            fail();
        }
    }

    private void CheckBalance(AccountDto customer, BigDecimal customerBalance, AccountDto merchant, BigDecimal merchantBalance) {
        try {
            BigDecimal currentBalance = bank.getBalance(customer);
            Assert.assertEquals(customerBalance, currentBalance);
            currentBalance = bank.getBalance(merchant);
            Assert.assertEquals(merchantBalance, currentBalance);
        } catch (BankException e) {
            fail();
        }
    }

    @Test
    public void TestBalanceWasSetup() {
        CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
    }

    @Test
    public void TestTransfer() {
        BigDecimal amount = new BigDecimal(50);
        try {
            CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
            bank.transferMoney(customerDto, merchantDto, amount, "Test transfer");
            CheckBalance(customerDto, startingBalance.subtract(amount), merchantDto, startingBalance.add(amount));
        } catch (BankException e) {
            fail();
        }
    }

    @Test
    public void TestDuplicate() {
        try {
            bank.addAccount(customerDto, startingBalance);
            fail();
        } catch (BankException e) {
            // Success (was duplicate)
        }
    }

    @Test
    public void RetireNonExisting() {
        try {
            bank.retireAccount(customerDto);
        } catch (BankException e) {
            fail();
        }

        try {
            bank.retireAccount(customerDto);
        } catch (BankException e) {
            // Success (was not found)
        }
        try {
            // Is added again to allow teardown to run successfully
            String newBankAccount = bank.addAccount(customerDto, startingBalance);
            customerDto.setBankAccountId(newBankAccount);
        } catch (BankException e) {
            fail();
        }
    }

    @Test
    public void TestTransferInsufficientFunds() {
        BigDecimal amount = new BigDecimal(150);
        try {
            CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
            bank.transferMoney(customerDto, merchantDto, amount, "Test insufficient fund transfer");
            fail();
        } catch (BankException e) {
            CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
        }
    }

    @Test
    public void TestTransferNegativeAmount() {
        BigDecimal amount = new BigDecimal(-100);
        try {
            CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
            bank.transferMoney(customerDto, merchantDto, amount, "Test negative amount transfer");
            fail();
        } catch (BankException e) {
            CheckBalance(customerDto, startingBalance, merchantDto, startingBalance);
        }
    }

    @Test
    public void TestTransferNonExistingBankAccount() {
        BigDecimal amount = new BigDecimal(50);
        AccountDto accountDto = new AccountDto(UUID.randomUUID(),
                "I dont exist",
                "Im not a bank account. Banky McBankFace",
                "Cpr McCprFace",
                AccountType.CUSTOMER);
        try {
            bank.getBalance(accountDto);
            fail();
        } catch (BankException ignored) {
            // Success (not found)
        }
        try {
            bank.transferMoney(accountDto, merchantDto, amount, "Test transfer");
            fail();
        } catch (BankException ignored) {
            // Success
        }
    }
}
