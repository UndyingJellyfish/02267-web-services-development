package main.controllers;

import dtu.ws.fastmoney.BankServiceException_Exception;
import exceptions.TokenException;
import interfaces.ITokenManager;
import main.PaymentService;
import main.UserService;
import models.Customer;
import models.Merchant;
import models.Token;
import models.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final ITokenManager tokenManager;
    private final UserService userService;


    public PaymentController(PaymentService paymentService, ITokenManager tokenManager, UserService userService){
        this.paymentService = paymentService;
        this.tokenManager = tokenManager;
        this.userService = userService;
    }

    @GetMapping
    public Transaction pay() throws TokenException, BankServiceException_Exception {
        Customer customer = userService.addAccount(new Customer("TestCustomer"));
        List<Token> tokens = this.tokenManager.RequestTokens(customer, 5);
        Merchant merchant = userService.addAccount(new Merchant("TestMerchant"));
        return paymentService.transfer(tokens.get(0).getTokenId(),merchant.getAccountId(), new BigDecimal(10), "");
    }

}
