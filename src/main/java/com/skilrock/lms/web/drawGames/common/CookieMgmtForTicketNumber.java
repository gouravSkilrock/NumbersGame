package com.skilrock.lms.web.drawGames.common;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.utility.EncpDecpUtil;

/**
 * 
 * @author Mandeep
 *<pre>
 * 09th April 2014    Mandeep    CR NO # :Implement Last Sold Ticket concept , like embedded , on web for sales only. 
 * 										  Serves cookie related services with the name "TICKET_COOKIE".
 * </pre>
 */
public class CookieMgmtForTicketNumber {

	static Log logger = LogFactory.getLog(CookieMgmtForTicketNumber.class);
	public synchronized static void checkAndUpdateTicketsDetails(HttpServletRequest request, HttpServletResponse response, String userName , String newTicketNumber)
			throws ServletException, IOException {

		Cookie ticketCookie = checkIfCookiePresent(request);
		if (ticketCookie != null) {
			updateOldCookie(ticketCookie , response, newTicketNumber , userName);
		} else {
			createNewCookie(userName, newTicketNumber ,request, response);
		}
	}
	
	private static Cookie checkIfCookiePresent(HttpServletRequest request){

		Cookie ticketCookie = null;
		Cookie tempCookie=null;
		boolean isCookieFound = false;
		if (request.getCookies() != null) {
			Cookie[] cookies = request.getCookies();
			for (Cookie element : cookies) {
					tempCookie = element;
					if (tempCookie.getName().equals("TICKET_COOKIE")) {
						ticketCookie=element;
						logger.info("TICKET_COOKIE Found  --->"+ ticketCookie.getValue());
						isCookieFound = true;
						break;
					}
				}
			if(!isCookieFound){
				logger.info("TICKET_COOKIE NOT Found");
			}
		}
		return ticketCookie;
		}
	
	public synchronized static long getTicketNumberFromCookie(HttpServletRequest request, String userName) throws Exception{
		long ticketNumber=0;
		try{		
		Cookie ticketCookie=checkIfCookiePresent(request);
		if(ticketCookie !=null ){
			ticketNumber=manageCookieCreation(ticketCookie, request, userName);
		}		
	 }catch(Exception e){
		 e.printStackTrace();
		 throw e;
	 }
	 return ticketNumber;
	}
	
	private static  void createNewCookie(String userName ,String ticketNumber ,HttpServletRequest request , HttpServletResponse response){
		Cookie ticketCookie = new Cookie("TICKET_COOKIE",EncpDecpUtil.encryptString((userName+":"+ticketNumber) , "" , ""));
		ticketCookie.setPath("/");
		ticketCookie.setMaxAge(48*60*60); // Auto cancel allowed for N*24 hours (where n is no. of days).   
		response.addCookie(ticketCookie);
		logger.info("New COOKIE"	+ ticketCookie);
	}
	
	private static void updateOldCookie(Cookie ticketCookie ,HttpServletResponse response , String newTicketNumber , String userName ){
		ticketCookie.setPath("/");
		ticketCookie.setMaxAge(48*60*60); // Auto cancel allowed for N*24 hours (where n is no. of days).   
		ticketCookie.setValue(EncpDecpUtil.encryptString(((userName+":"+newTicketNumber)) , "" ,""));
		response.addCookie(ticketCookie);
	}

	public static long manageCookieCreation(Cookie ticketCookie ,HttpServletRequest request , String userName) throws Exception{
		long ticktNumber=0;
		String enycryptedValue =  ticketCookie.getValue().toString();
		String dycryptedValue = EncpDecpUtil.decryptString(enycryptedValue ,"" ,"");
		String[] userNameNTicketNumber=dycryptedValue.split(":");
		if(userNameNTicketNumber[0].equals(userName)){
			ticktNumber= Long.parseLong(userNameNTicketNumber[1]);
		}		
		return ticktNumber;
	}
}
