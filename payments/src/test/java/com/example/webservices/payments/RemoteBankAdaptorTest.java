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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;



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
        merchantDto = new AccountDto(UUID.randomUUID(),"Bob Bobby", "020202-0202", AccountType.MERCHANT);

        try {
            String customerAccountId = bank.addAccount(customerDto, startingBalance);
            customerDto.setBankAccountId(customerAccountId);
            String merchantAccountId = bank.addAccount(merchantDto, startingBalance);
            merchantDto.setBankAccountId(merchantAccountId);
        } catch (BankException e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void teardown() {
        try {
            bank.retireAccount(customerDto);
            bank.retireAccount(merchantDto);
        } catch (BankException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void CheckBalance(AccountDto customer, BigDecimal customerBalance, AccountDto merchant, BigDecimal merchantBalance) {
        try {
            BigDecimal currentBalance = bank.getBalance(customer);
            Assert.assertEquals(customerBalance, currentBalance);
            currentBalance = bank.getBalance(merchant);
            Assert.assertEquals(merchantBalance, currentBalance);
        } catch (BankException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void TestBalanceWasSetup() {
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
            Assert.fail(e.getMessage());
        }
    }
}
