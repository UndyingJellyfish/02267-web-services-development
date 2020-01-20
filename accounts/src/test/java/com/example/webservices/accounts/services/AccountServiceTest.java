package com.example.webservices.accounts.services;

import com.example.webservices.accounts.interfaces.IAccountDatastore;
import com.example.webservices.accounts.models.Customer;
import com.example.webservices.accounts.models.Merchant;
import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.ITokenManager;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;


public class AccountServiceTest {

    private ITokenManager tokenManager;
    private IAccountDatastore store;
    private AccountService service;
    private Merchant merchant = new Merchant("name","cvr");
    private UUID merchantId = merchant.getAccountId();
    private Customer customer = new Customer("customer","123");
    private UUID customerId = customer.getAccountId();
    private SignupDto signupDto = new SignupDto("customer","123", "bankId");


    @Before
    public void setup(){
        this.tokenManager = mock(ITokenManager.class);
        this.store = mock(IAccountDatastore.class);
        this.service = new AccountService(store, tokenManager);
    }

    @Test
    public void changeName() throws EntryNotFoundException {
        String newName = "newName";
        when(store.getAccount(customerId)).thenReturn(customer);
        ChangeNameDto dto = new ChangeNameDto(customerId,newName);
        service.changeName(dto);
        assertEquals(newName, customer.getName());
    }


    @Test
    public void addCustomer() throws DuplicateEntryException {
        when(store.addAccount(any())).thenReturn(customer);
        service.addCustomer(signupDto);
        verify(store, times(1)).addAccount(argThat(acc -> acc.getIdentifier().equals(signupDto.getIdentifier())));
    }

    @Test
    public void addMerchant() throws DuplicateEntryException  {
        when(store.addAccount(any())).thenReturn(merchant);
        service.addMerchant(signupDto);
        verify(store, times(1)).addAccount(argThat(acc -> acc.getIdentifier().equals(signupDto.getIdentifier())));

    }

    @Test
    public void delete() {
        try {
            service.delete(merchantId);
            verify(store,times(1)).deleteAccount(argThat(id -> id.equals(merchantId)));
            verify(tokenManager,times(1)).retireAll(argThat(id -> id.equals(merchantId)));
        } catch (EntryNotFoundException e) {
            fail();
        }
    }

    @Test
    public void getCustomer() throws EntryNotFoundException {
        when(store.getAccount(customerId)).thenReturn(customer);
        AccountDto dto = service.getCustomer(customerId);
        assertEquals(customerId,dto.getAccountId());

    }


    @Test
    public void getMerchant() throws EntryNotFoundException {
        when(store.getAccount(merchantId)).thenReturn(merchant);
        AccountDto dto = service.getMerchant(merchantId);
        assertEquals(merchantId, dto.getAccountId());
    }

    @Test
    public void getAccountException()  {
        try {
            when(store.getAccount(any())).thenThrow(EntryNotFoundException.class);
            service.getAccount(customerId);
            fail();
        } catch (EntryNotFoundException ignored) {
        }

    }
}