package adaptors;

import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import interfaces.IBank;
import models.Account;
import models.Customer;
import models.Merchant;

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
                    customer.getAccountId().toString(),
                    merchant.getAccountId().toString(),
                    amount,
                    description);
    }

    @Override
    public String addAccount(Account account) throws BankServiceException_Exception {
        return addAccount(account, BigDecimal.ZERO);
    }

    @Override
    public String addAccount(Account account, BigDecimal balance) throws BankServiceException_Exception {
        BankService bankService = bankServiceService.getPort(BankService.class);
        User user = new User();
        setupUserName(account.getName(), user);
        user.setCprNumber(account.getCpr());
        return bankService.createAccountWithBalance(user, balance);
    }

    @Override
    public void retireAccount(Account account) throws BankServiceException_Exception {
        BankService bankService = bankServiceService.getPort(BankService.class);
        bankService.retireAccount(account.getBankAccountId());
    }

    private void setupUserName(String name, User user) {
        List<String> names = Arrays.asList(name.split(" "));
        List<String> firstNames = names.subList(0, names.size() - 2);
        String firstName = String.join(" ", firstNames);
        String lastName = names.get(names.size() - 1);
        user.setFirstName(firstName);
        user.setLastName(lastName);
    }
}
