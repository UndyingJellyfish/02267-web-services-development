
package dtu.ws.fastmoney;

import javax.xml.ws.WebFault;

@WebFault(name = "BankServiceException", targetNamespace = "http://fastmoney.ws.dtu/")
public class BankServiceException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private BankServiceException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public BankServiceException_Exception(String message, BankServiceException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public BankServiceException_Exception(String message, BankServiceException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: dtu.ws.fastmoney.BankServiceException
     */
    public BankServiceException getFaultInfo() {
        return faultInfo;
    }

}
