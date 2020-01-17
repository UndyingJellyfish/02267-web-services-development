package com.example.webservices.application.accounts;

import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping(value={"/{accountId}"})
    @ResponseStatus(HttpStatus.OK)
    public void changeName(@PathVariable UUID accountId, @RequestBody ChangeNameDto newName){
        try {
            accountService.changeName(accountId, newName.getNewName());
        } catch (EntryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/merchant")
    @ResponseStatus(HttpStatus.OK)
    public UUID signupMerchant(@RequestBody SignupDto merchant){
        try {
            return accountService.addAccount(new Merchant(merchant.getName(), merchant.getIdentifier(), merchant.getBankAccountId())).getAccountId();
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    public UUID signupCustomer(@RequestBody SignupDto customer){
        try {
            return accountService.addAccount( new Customer(customer.getName(), customer.getIdentifier(), customer.getBankAccountId())).getAccountId();
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping(value={"/{accountId}"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID accountId){
        try {
            accountService.delete(accountId);
        } catch (EntryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}
