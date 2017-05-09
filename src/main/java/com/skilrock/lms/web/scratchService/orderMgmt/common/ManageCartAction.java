/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.ManageCartHelper;

/**
 * This class provides methods for managing the cart used in the order creation
 * 
 * @author Skilrock Technologies
 * 
 */
public class ManageCartAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(ManageCartAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String agtOrgName;
	private String cartEmpty;
	private int gameId;
	private String gameName;
	private int quantity;
	private HttpServletRequest request;

	private String retOrgName;
	private String searchResultsAvailable;

	private String totalAmount;

	/**
	 * This method is used for adding game to the cart
	 * 
	 * @return String
	 */
	public String addToCart() {

		/*
		 * System.out.println("----------Inisedeeeeeee ----Add to cart");
		 * System.out.println("ReqGameId:" + request.getAttribute("gameId"));
		 * System.out.println("ReqQuan:" + request.getAttribute("quantity"));
		 * 
		 * System.out.println("GameId:" + getGameId()); System.out.println("Game
		 * Name---:" + getGameName()); System.out.println("Quantity---:" +
		 * getQuantity());
		 */

		setSearchResultsAvailable("Yes");

		HttpSession session = getRequest().getSession();

		if (session.getAttribute("CART") == null) {
			session.setAttribute("CART", new ArrayList());

		}

		List cartList = (List) session.getAttribute("CART");
		List searchResultList = (List) session.getAttribute("SEARCH_RESULTS");

		ManageCartHelper cartHelper = new ManageCartHelper();

		cartHelper.addGameToCart(searchResultList, cartList, getGameId(),
				getQuantity());
		/*
		 * if (cartList != null && cartList.size() > 0) {
		 * 
		 * Iterator it= cartList.iterator(); while(it.hasNext()){ GameBean
		 * gameBean=(GameBean)it.next(); int id=gameBean.getGameId();
		 * if(id==gameId){
		 * 
		 * if(session.getAttribute("TOTAL")!=null &&
		 * session.getAttribute("BALANCE")!=null){
		 * 
		 * totalAmount=((Double)session.getAttribute("TOTAL")).doubleValue()+(quantity*gameBean.getTicketPrice()*gameBean.getTicketsPerBook());
		 * if(((Double)session.getAttribute("BALANCE")).doubleValue()>0.0){
		 * balance=((Double)session.getAttribute("BALANCE")).doubleValue()+(quantity*gameBean.getTicketPrice()*gameBean.getTicketsPerBook());
		 * System.out.println("Total is not null and it is "+totalAmount); }
		 * else{
		 * balance=((Double)session.getAttribute("CREDIT_AMT")).doubleValue()+(quantity*gameBean.getTicketPrice()*gameBean.getTicketsPerBook()); } }
		 * else{
		 * totalAmount=quantity*gameBean.getTicketPrice()*gameBean.getTicketsPerBook();
		 * System.out.println("Total is null && and first time adding and it is
		 * "+totalAmount); } } break; } }
		 * //session.setAttribute("TOTAL",totalAmount);
		 * //session.setAttribute("BALANCE",balance);
		 */
		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		if (agtOrgName != null) {
			setAgtOrgName(agtOrgName);
		}

		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		if (retOrgName != null) {
			setRetOrgName(retOrgName);
		}

		session.setAttribute("SEARCH_RESULTS1", null);
		session.setAttribute("SEARCH_RESULTS", null);
		setSearchResultsAvailable("No");

		return SUCCESS;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getCartEmpty() {
		return cartEmpty;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getQuantity() {
		return quantity;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	/**
	 * This method is used for processing the cart
	 * 
	 * @return String
	 */
	public String processCart() {

		HttpSession session = getRequest().getSession();
		// List cartList = (List) session.getAttribute("CART");

		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		if (agtOrgName != null) {
			setAgtOrgName(agtOrgName);
		}

		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		if (retOrgName != null) {
			setRetOrgName(retOrgName);
		}

		System.out.println("available credirt for retailer is  "
				+ session.getAttribute("AVAIL_AMT_RET"));

		System.out.println("total sale amount is " + getTotalAmount());

		return SUCCESS;
	}

	/**
	 * This method is used for removing game from the cart
	 * 
	 * @return String
	 */
	public String removeFromCart() {

		HttpSession session = getRequest().getSession();
		List cartList = (List) session.getAttribute("CART");

		ManageCartHelper cartHelper = new ManageCartHelper();
		cartHelper.removeFromCart(cartList, getGameId());

		if (cartList != null && cartList.size() > 0) {
			setCartEmpty("No");
		}

		String agtOrgName = (String) session.getAttribute("AGT_ORG_NAME");
		if (agtOrgName != null) {
			setAgtOrgName(agtOrgName);
		}

		String retOrgName = (String) session.getAttribute("RET_ORG_NAME");
		if (retOrgName != null) {
			setRetOrgName(retOrgName);
		}

		return SUCCESS;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setCartEmpty(String cartEmpty) {
		this.cartEmpty = cartEmpty;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
		// this.totalAmount=this.totalAmount.replace(",", "");
	}

}
