
package dtu.ws.fastmoney;

import java.math.BigDecimal;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "BankService", targetNamespace = "http://fastmoney.ws.dtu/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BankService {


    /**
     * 
     * @param accountId id of the account
     * @return
     *     returns dtu.ws.fastmoney.Account
     * @throws BankServiceException_Exception Is thrown if an account could not be found
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAccount", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.GetAccount")
    @ResponseWrapper(localName = "getAccountResponse", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.GetAccountResponse")
    public Account getAccount(
        @WebParam(name = "account_id", targetNamespace = "")
        String accountId)
        throws BankServiceException_Exception
    ;

    /**
     * 
     * @param cpr identifying CPR of the account
     * @return
     *     returns dtu.ws.fastmoney.Account
     * @throws BankServiceException_Exception Is thrown if an account could not be found
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAccountByCprNumber", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.GetAccountByCprNumber")
    @ResponseWrapper(localName = "getAccountByCprNumberResponse", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.GetAccountByCprNumberResponse")
    public Account getAccountByCprNumber(
        @WebParam(name = "cpr", targetNamespace = "")
        String cpr)
        throws BankServiceException_Exception
    ;

    /**
     *
     * @param user the user to create
     * @param balance balance accredited to the new user
     * @return
     *     returns java.lang.String
     * @throws BankServiceException_Exception Is thrown if the account could not be created
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createAccountWithBalance", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.CreateAccountWithBalance")
    @ResponseWrapper(localName = "createAccountWithBalanceResponse", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.CreateAccountWithBalanceResponse")
    public String createAccountWithBalance(
        @WebParam(name = "user", targetNamespace = "")
        User user,
        @WebParam(name = "balance", targetNamespace = "")
        BigDecimal balance)
        throws BankServiceException_Exception
    ;

    /**
     * 
     * @param accountId id of the account
     * @throws BankServiceException_Exception Is thrown if an account could not be found
     */
    @WebMethod
    @RequestWrapper(localName = "retireAccount", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.RetireAccount")
    @ResponseWrapper(localName = "retireAccountResponse", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.RetireAccountResponse")
    public void retireAccount(
        @WebParam(name = "account_id", targetNamespace = "")
        String accountId)
        throws BankServiceException_Exception
    ;

    /**
     * 
     * @return
     *     returns {@literal (java.util.List<dtu.ws.fastmoney.AccountInfo>)}
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAccounts", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.GetAccounts")
    @ResponseWrapper(localName = "getAccountsResponse", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.GetAccountsResponse")
    public List<AccountInfo> getAccounts();

    /**
     * 
     * @param amount amount to transfer
     * @param creditor recipient of money
     * @param debtor sender of money
     * @param description the description to show in bank information
     * @throws BankServiceException_Exception Is thrown if the transfer could not be completed
     */
    @WebMethod
    @RequestWrapper(localName = "transferMoneyFromTo", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.TransferMoneyFromTo")
    @ResponseWrapper(localName = "transferMoneyFromToResponse", targetNamespace = "http://fastmoney.ws.dtu/", className = "dtu.ws.fastmoney.TransferMoneyFromToResponse")
    public void transferMoneyFromTo(
        @WebParam(name = "debtor", targetNamespace = "")
        String debtor,
        @WebParam(name = "creditor", targetNamespace = "")
        String creditor,
        @WebParam(name = "amount", targetNamespace = "")
        BigDecimal amount,
        @WebParam(name = "description", targetNamespace = "")
        String description)
        throws BankServiceException_Exception
    ;

}
