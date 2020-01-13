package main.adaptors;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import main.interfaces.IBank;
import main.models.Account;
import main.models.Customer;
import main.models.Merchant;

import javax.xml.ws.Service;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class RemoteBankAdaptor implements IBank {

    private Service bankServiceService;

    public RemoteBankAdaptor() {
        this.bankServiceService = new BankServiceService();
    }

    @Override
    public void transferMoney(Customer customer, Merchant merchant, BigDecimal amount, String description) throws BankServiceException_Exception {
            BankService bankService = bankServiceService.getPort(BankService.class);
            bankService.transferMoneyFromTo(
                    customer.getBankAccountId(),
                    merchant.getBankAccountId(),
                    amount,
                    description);
    }

    @Override
    public String addAccount(Account account) throws BankServiceException_Exception, ClassNotFoundException {
        return addAccount(account, BigDecimal.ZERO);
    }

    @Override
    public String addAccount(Account account, BigDecimal balance) throws BankServiceException_Exception, ClassNotFoundException {
        if (account instanceof Customer) {
            return addAccountCustomer((Customer)account, balance);
        } else if (account instanceof Merchant) {
            return addAccountMerchant((Merchant)account, balance);
        }
        throw new ClassNotFoundException();
    }

    private String addAccountCustomer(Customer customer, BigDecimal balance) throws BankServiceException_Exception {
        return addAccountCommon(customer, customer.getCpr(), balance);
    }

    private String addAccountMerchant(Merchant merchant, BigDecimal balance) throws BankServiceException_Exception {
        return addAccountCommon(merchant, merchant.getCvr(), balance);
    }

    private String addAccountCommon(Account account, String cpr, BigDecimal balance) throws BankServiceException_Exception {
        BankService bankService = bankServiceService.getPort(BankService.class);
        User user = new User();
        setupUserName(account.getName(), user);
        user.setCprNumber(cpr);
        return bankService.createAccountWithBalance(user, balance);
    }

    @Override
    public void retireAccount(Account account) throws BankServiceException_Exception {
        BankService bankService = bankServiceService.getPort(BankService.class);
        bankService.retireAccount(account.getBankAccountId());
    }

    @Override
    public BigDecimal getBalance(Account account) throws BankServiceException_Exception {
        BankService bankService = bankServiceService.getPort(BankService.class);
        dtu.ws.fastmoney.Account bankAccount = bankService.getAccount(account.getBankAccountId());
        return bankAccount.getBalance();
    }

    private void setupUserName(String name, User user) {
        List<String> names = Arrays.asList(name.split(" "));
        List<String> firstNames = names.subList(0, names.size() - 1);
        String firstName = String.join(" ", firstNames);
        String lastName = names.get(names.size() - 1);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }
}
