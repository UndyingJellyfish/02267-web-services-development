package com.example.webservices.application.accounts;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.ChangeNameDto;
import com.example.webservices.library.dataTransferObjects.SignupDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @author s164434
     * @param newName {@inheritDoc}
     */
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public void changeName(@RequestBody ChangeNameDto newName){
        try {
            accountService.changeName(newName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * @author s164424
     * @param merchant {@inheritDoc}
     * @return id of the newly signed up merchant
     */
    @PostMapping("/merchant")
    @ResponseStatus(HttpStatus.OK)
    public UUID signupMerchant(@RequestBody SignupDto merchant){
        try {
            return accountService.addMerchant(merchant).getAccountId();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @author s164407
     * @param accountId id of the account to fetch
     */
    @GetMapping(value={"/{accountId}"})
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccount(@PathVariable UUID accountId){
        try {
            return accountService.getAccount(accountId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    /**
     * @author s164398
     * @param customer {@inheritDoc}
     * @return id of the newly sign up customer
     */
    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    public UUID signupCustomer(@RequestBody SignupDto customer){
        try {
            AccountDto dto = accountService.addCustomer(customer);
            return dto.getAccountId();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * @author s164410
     * @param accountId id of the account to delete
     */
    @DeleteMapping(value={"/{accountId}"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID accountId){
        try {
            accountService.delete(accountId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
