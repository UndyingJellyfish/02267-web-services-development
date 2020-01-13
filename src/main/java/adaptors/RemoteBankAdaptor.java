package adaptors;

import adaptors.dtu.ws.fastmoney.BankService;
import adaptors.dtu.ws.fastmoney.BankServiceException_Exception;
import adaptors.dtu.ws.fastmoney.BankServiceService;
import interfaces.IBank;
import models.Customer;
import models.Merchant;

import javax.xml.ws.Service;
import java.math.BigDecimal;

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
