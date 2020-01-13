package main.interfaces;

import dtu.ws.fastmoney.BankServiceException_Exception;
import main.models.Customer;
import main.models.Merchant;

import java.math.BigDecimal;

public interface IBank {

    void transferMoney(Customer customer, Merchant merchant, BigDecimal amount, String description) throws BankServiceException_Exception;
}
