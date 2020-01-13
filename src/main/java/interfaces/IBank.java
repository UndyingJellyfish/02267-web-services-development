package interfaces;

import dtu.ws.fastmoney.BankServiceException_Exception;
import models.Account;
import models.Customer;
import models.Merchant;

import java.math.BigDecimal;

public interface IBank {

    void transferMoney(Customer customer, Merchant merchant, BigDecimal amount, String description) throws BankServiceException_Exception;

    String addAccount(Account account) throws BankServiceException_Exception, ClassNotFoundException;
    String addAccount(Account account, BigDecimal amount) throws BankServiceException_Exception, ClassNotFoundException;

    void retireAccount(Account account) throws BankServiceException_Exception;

    BigDecimal getBalance(Account customer) throws BankServiceException_Exception;
}
