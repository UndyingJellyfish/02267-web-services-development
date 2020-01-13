package main.controllers;

import main.exceptions.DuplicateEntryException;
import main.services.AccountService;
import main.models.Customer;
import main.models.Merchant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/signup")
public class AccountController {

    private AccountService accountService;


    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    public static class SignupDto{
        private String name;
        private String cpr;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCpr() {
            return cpr;
        }

        public void setCpr(String cpr) {
            this.cpr = cpr;
        }
    }

    @PutMapping(value={"/merchant","/customer"})
    @ResponseStatus(HttpStatus.OK)
    public void changeName(@RequestBody SignupDto dto){

    }

    @PostMapping("/merchant")
    @ResponseStatus(HttpStatus.OK)
    public void signupMerchant(@RequestBody SignupDto merchant){
        try {
            accountService.addAccount(new Merchant(merchant.name,"123"));
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate key from random UUID generation");
        }
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    public void signupCustomer(@RequestBody SignupDto customer){
        try {
            accountService.addAccount( new Customer(customer.name,customer.cpr));
        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}
