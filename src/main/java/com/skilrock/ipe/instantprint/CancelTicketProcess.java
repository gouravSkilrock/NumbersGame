package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for cancelTicketProcess complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="cancelTicketProcess">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticketBean" type="{http://instantPrint.ipe.skilrock.com/}ticketPurchaseBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelTicketProcess", propOrder = { "ticketBean" })
public class CancelTicketProcess {

	protected TicketPurchaseBean ticketBean;

	/**
	 * Gets the value of the ticketBean property.
	 * 
	 * @return possible object is {@link TicketPurchaseBean }
	 * 
	 */
	public TicketPurchaseBean getTicketBean() {
		return ticketBean;
	}

	/**
	 * Sets the value of the ticketBean property.
	 * 
	 * @param value
	 *            allowed object is {@link TicketPurchaseBean }
	 * 
	 */
	public void setTicketBean(TicketPurchaseBean value) {
		this.ticketBean = value;
	}

}
