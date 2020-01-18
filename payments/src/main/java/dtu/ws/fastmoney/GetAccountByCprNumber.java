
package dtu.ws.fastmoney;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAccountByCprNumber", propOrder = {
    "cpr"
})
public class GetAccountByCprNumber {

    protected String cpr;

    /**
     * Gets the value of the cpr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpr() {
        return cpr;
    }

    /**
     * Sets the value of the cpr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpr(String value) {
        this.cpr = value;
    }

}
