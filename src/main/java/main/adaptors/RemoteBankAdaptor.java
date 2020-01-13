package main.adaptors;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import main.interfaces.IBank;
import main.models.Customer;
import main.models.Merchant;

import javax.xml.ws.Service;
import java.math.BigDecimal;

@org.springframework.stereotype.Service
public class RemoteBankAdaptor implements IBank {

    private Service bankServiceService;

    public RemoteBankAdaptor() {
        this.bankServiceService = new BankServiceService();
    }

    @Override
    public void transferMoney(Customer customer, Merchant merchant, BigDecimal amount, String description) throws BankServiceException_Exception {

            BankService bankService = bankServiceService.getPort(BankService.class);
            bankService.transferMoneyFromTo(
                    customer.getAccountId().toString(),
                    merchant.getAccountId().toString(),
                    amount,
                    description);


    }
}
