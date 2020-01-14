package com.example.webservices.application.accounts;

import com.example.webservices.application.exceptions.DuplicateEntryException;
import com.example.webservices.library.models.Customer;
import com.example.webservices.library.models.Merchant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/signup")
public class AccountController {

    private AccountService accountService;


    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }



    @PutMapping(value={"/merchant","/customer"})
    @ResponseStatus(HttpStatus.OK)
    public void changeName(@RequestBody SignupDto dto){

    }

    @PostMapping("/merchant")
    @ResponseStatus(HttpStatus.OK)
    public UUID signupMerchant(@RequestBody SignupDto merchant){
        try {
            return accountService.addAccount(new Merchant(merchant.getName(),"123")).getAccountId();
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate key from random UUID generation");
        }
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    public UUID signupCustomer(@RequestBody SignupDto customer){
        try {
            return accountService.addAccount( new Customer(customer.getName(),customer.getCpr())).getAccountId();
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
