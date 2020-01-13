package interfaces;

import dtu.ws.fastmoney.BankServiceException_Exception;
import models.Customer;
import models.Merchant;

import java.math.BigDecimal;

public interface IBank {

    void transferMoney(Customer customer, Merchant merchant, BigDecimal amount, String description) throws BankServiceException_Exception;
}
