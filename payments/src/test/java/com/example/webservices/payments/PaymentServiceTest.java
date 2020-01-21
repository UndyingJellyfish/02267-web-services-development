package com.example.webservices.payments;

import com.example.webservices.library.dataTransferObjects.*;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.*;
import com.example.webservices.payments.services.PaymentService;
import dtu.ws.fastmoney.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/** @author s164395 */
public class PaymentServiceTest {

    private IPaymentService paymentService;
    private AccountDto merchant;
    private AccountDto customer;
    private UUID tokenId;
    private IBank bank = mock(IBank.class);
    private IAccountService accountService = mock(IAccountService.class);
    private ITokenManager tokenManager = mock(ITokenManager.class);
    private ITransactionService transactionService = mock(ITransactionService.class);
    private AccountDto customerDto = null;
    private AccountDto merchantDto = null;

    public PaymentServiceTest(){
        this.paymentService = new PaymentService(tokenManager, accountService, transactionService, bank);
    }

    @Before
    public void setup(){
        SignupDto customerSignup = new SignupDto("yoink", "12345678", UUID.randomUUID().toString());
        SignupDto merchantSignup = new SignupDto("doink", "12345678", UUID.randomUUID().toString());
        customerDto = new AccountDto(UUID.randomUUID(),
                customerSignup.getName(),
                customerSignup.getBankAccountId(),
                customerSignup.getIdentifier(),
                AccountType.CUSTOMER);
        merchantDto = new AccountDto(UUID.randomUUID(),
                merchantSignup.getName(),
                merchantSignup.getBankAccountId(),
                merchantSignup.getIdentifier(),
                AccountType.MERCHANT);

        try {
            when(accountService.addCustomer(customerSignup)).thenReturn(customerDto);
            when(accountService.addMerchant(merchantSignup)).thenReturn(merchantDto);
            this.customer = accountService.addCustomer(customerSignup);
            this.merchant = accountService.addMerchant(merchantSignup);
        } catch (DuplicateEntryException e) {
            fail();
        }
        try {
            when(tokenManager.RequestToken(this.customer.getAccountId())).thenReturn(UUID.randomUUID());
            this.tokenId = tokenManager.RequestToken(this.customer.getAccountId());
        } catch (EntryNotFoundException | TokenQuantityException e) {
            fail();
        }
    }

    private Exception exception;
    private TransactionDto transaction = null;
    @Test
    public void negativeTransferAmount(){
        try{
            BigDecimal amount = new BigDecimal(-23);
            when(accountService.getCustomer(customer.getAccountId())).thenReturn(customerDto);
            when(accountService.getMerchant(merchant.getAccountId())).thenReturn(merchantDto);
            TransactionDto dto = new TransactionDto(tokenId, merchant.getAccountId(), UUID.randomUUID(), amount, "", false, new Date());
            transaction = paymentService.transfer(dto);
            fail();
        }
        catch(InvalidTransferAmountException e){
            exception = e;
        } catch (BankException | TokenException | EntryNotFoundException | DuplicateEntryException ignored) {
            fail();
        }
        assertNull(transaction);
        assertNotNull(exception);
    }

    @Test
    public void transferAmount(){
        BigDecimal amount = new BigDecimal("100");
        String description = "Im a transaction";
        try {
            when(accountService.getCustomer(customer.getAccountId())).thenReturn(customerDto);
            when(accountService.getMerchant(merchant.getAccountId())).thenReturn(merchantDto);
            when(tokenManager.GetToken(tokenId)).thenReturn(new TokenDto(tokenId, false, customerDto.getAccountId()));
            TransactionDto dto = new TransactionDto(tokenId, merchant.getAccountId(), UUID.randomUUID(), amount, description, false, new Date());
            transaction = paymentService.transfer(dto);
        } catch (EntryNotFoundException | TokenException | BankException | InvalidTransferAmountException | DuplicateEntryException e) {
            fail();
        }
        assertEquals(merchant.getAccountId(),transaction.getCreditorId());
        assertEquals(customer.getAccountId(), transaction.getDebtorId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(tokenId, transaction.getTokenId());
    }

    @Test
    public void refundAmount() {
        try {
            transactionService.refundTransaction(UUID.randomUUID());
            verify(transactionService,times(1)).refundTransaction(any());
        } catch (EntryNotFoundException | DuplicateEntryException e) {
            fail();
        }
    }

    @Test
    public void refundAmountException() throws EntryNotFoundException, DuplicateEntryException {
        Mockito.doThrow(EntryNotFoundException.class).when(transactionService).refundTransaction(any());
        try {
            transactionService.refundTransaction(UUID.randomUUID());
            fail();
        } catch (EntryNotFoundException e) {
            verify(transactionService,times(1)).refundTransaction(any());
        }
    }
}
