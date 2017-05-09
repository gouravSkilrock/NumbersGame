package com.skilrock.lms.web.scratchService.gameMgmt.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class GenerateTicketFormat extends ActionSupport implements
		ServletResponseAware {
	static Log logger = LogFactory.getLog(GenerateTicketFormat.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int findDigit(int k) {
		int count = 0;
		while (k != 0) {
			k = k / 10;
			count++;
		}
		return count;
	}

	private int bookaNbr;

	private HttpServletResponse response;

	private int ticketNbr;

	@Override
	public String execute() throws Exception {
		logger.debug("jjjjjjjjj");
		logger.debug("hh " + getBookaNbr());
		logger.debug("gg " + getTicketNbr());
		PrintWriter out = getResponse().getWriter();
		int nbr_books = getBookaNbr();
		int nbr_ticket = getTicketNbr();
		logger.debug("" + nbr_books);
		// int nbr_books=1000;
		// int nbr_tickets;
		int pdigits = 0;
		int tdigits = findDigit(nbr_ticket);
		int bdigits = findDigit(nbr_books);
		if (bdigits >= 4) {
			bdigits = bdigits;
			pdigits = 2;
		} else if (bdigits <= 3) {
			bdigits = 3;
			pdigits = 3;

		}

		String values = "";
		String pdigitsstr = "" + pdigits + ":";
		String bdigitsstr = "" + bdigits + ":";
		String tdigitsstr = "" + tdigits;
		values = values
				.concat(bdigitsstr.concat(pdigitsstr).concat(tdigitsstr));
		logger.debug("values are " + values);
		out.print(values);
		return null;
	}

	public int getBookaNbr() {
		return bookaNbr;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getTicketNbr() {
		return ticketNbr;
	}

	public void setBookaNbr(int bookaNbr) {
		this.bookaNbr = bookaNbr;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setTicketNbr(int ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

}
