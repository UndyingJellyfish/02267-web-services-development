package adaptors;

import Interfaces.IBank;
import dtu.ws.fastmoney.Bank;
import models.Customer;
import models.Merchant;

import java.math.BigDecimal;

public class LocalBankAdaptor implements IBank {
    private Bank bank;

    public LocalBankAdaptor() {
        this.bank = new Bank();
    }


    @Override
    public void transferMoney(Customer customer, Merchant merchant, BigDecimal amount) {

    }
}
