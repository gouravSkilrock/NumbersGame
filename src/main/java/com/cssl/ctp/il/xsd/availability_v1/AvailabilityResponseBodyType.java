package com.cssl.ctp.il.xsd.availability_v1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.cssl.ctp.il.xsd.infra_v1.ResponseBodyType;
import com.cssl.ctp.il.xsd.infra_v1.SystemType;

/**
 * The body of the response.
 * 
 * <p>
 * Java class for AvailabilityResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AvailabilityResponseBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ResponseBodyType">
 *       &lt;sequence>
 *         &lt;element name="System" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}SystemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailabilityResponseBodyType", propOrder = { "system" })
public class AvailabilityResponseBodyType extends ResponseBodyType {

	@XmlElement(name = "System")
	protected List<SystemType> system;

	/**
	 * Gets the value of the system property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the system property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSystem().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SystemType }
	 * 
	 * 
	 */
	public List<SystemType> getSystem() {
		if (system == null) {
			system = new ArrayList<SystemType>();
		}
		return this.system;
	}

}
