package main;

import Interfaces.IAccountDatastore;
import Interfaces.IBank;
import Interfaces.ITokenManager;
import dtu.ws.fastmoney.Bank;
import exceptions.PayException;
import models.Customer;
import models.Merchant;
import models.Token;
import models.Transaction;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentService {
    private ITokenManager tokenManager;
    private IAccountDatastore accountDatastore;
    private IBank bank;

    public PaymentService(ITokenManager tokenManager, IAccountDatastore accountDatastore, IBank bank) {
        this.tokenManager = tokenManager;
        this.accountDatastore = accountDatastore;
        this.bank = bank;
    }

    public Transaction pay(UUID tokenId, UUID merchantId, BigDecimal amount) {
        if(!isGreaterThanZero(amount)){
            throw new IllegalArgumentException();
        }
        Token token = tokenManager.GetToken(tokenId);
        Merchant merchant = accountDatastore.getMerchant(merchantId);
        Customer customer = token.getCustomer();
        tokenManager.UseToken(tokenId);
        bank.transferMoney(customer, merchant, amount);
        return new Transaction(merchant, customer, amount, token);
    }

    private boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) > 0;
    }
}
