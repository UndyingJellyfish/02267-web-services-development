
package dtu.ws.fastmoney;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "BankServiceService", targetNamespace = "http://fastmoney.ws.dtu/", wsdlLocation = "http://fastmoney-00.compute.dtu.dk/BankService.wsdl.xml")
public class BankServiceService
    extends Service
{

    private final static URL BANKSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException BANKSERVICESERVICE_EXCEPTION;
    private final static QName BANKSERVICESERVICE_QNAME = new QName("http://fastmoney.ws.dtu/", "BankServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://fastmoney-00.compute.dtu.dk/BankService.wsdl.xml");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        BANKSERVICESERVICE_WSDL_LOCATION = url;
        BANKSERVICESERVICE_EXCEPTION = e;
    }

    public BankServiceService() {
        super(__getWsdlLocation(), BANKSERVICESERVICE_QNAME);
    }

    public BankServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), BANKSERVICESERVICE_QNAME, features);
    }

    public BankServiceService(URL wsdlLocation) {
        super(wsdlLocation, BANKSERVICESERVICE_QNAME);
    }

    public BankServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BANKSERVICESERVICE_QNAME, features);
    }

    public BankServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BankServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BankService
     */
    @WebEndpoint(name = "BankServicePort")
    public BankService getBankServicePort() {
        return super.getPort(new QName("http://fastmoney.ws.dtu/", "BankServicePort"), BankService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BankService
     */
    @WebEndpoint(name = "BankServicePort")
    public BankService getBankServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://fastmoney.ws.dtu/", "BankServicePort"), BankService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BANKSERVICESERVICE_EXCEPTION!= null) {
            throw BANKSERVICESERVICE_EXCEPTION;
        }
        return BANKSERVICESERVICE_WSDL_LOCATION;
    }

}
