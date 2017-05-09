package com.skilrock.lms.web.ola;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import rng.RNGUtilities;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.OlaPlayerDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.CreateNewPlayerHelper;
import com.skilrock.lms.coreEngine.ola.OlaHelper;


public class CreateNewPlayerAction extends ActionSupport implements
ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(CreateNewPlayerAction.class);
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String gender;
	private String dateOfBirth;
	private String username;
	private String password1;
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String country;
	private double deposit;
	private String walletName;

	private HttpServletRequest request;
	private HttpServletResponse response;

	public String createNewPlayer() throws LMSException{
		int walletId = 0;
		long rnumber;
		String devWalletName = null;
		if(walletName.equalsIgnoreCase("-1")||walletName.equalsIgnoreCase("null")||walletName.equalsIgnoreCase(null)){
					return ERROR;
		}
		else{
			String[] walletArr = walletName.split(":");
			walletId = Integer.parseInt(walletArr[0]);
			devWalletName = walletArr[1];
		}
		if(devWalletName.equalsIgnoreCase("RUMMY")){
			rnumber = RNGUtilities.generateRandomNumber(1000000000l,
					9999999999l);
			setPassword1(((Long)rnumber).toString());
		}
		CreateNewPlayerHelper helper = new CreateNewPlayerHelper();
		ServletContext sc = ServletActionContext.getServletContext();
		String depositAnyWhere = (String) sc.getAttribute("OLA_DEP_ANYWHERE");
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		FlexiCardPurchaseBean flexiCardPurchaseBean = new 	FlexiCardPurchaseBean();
		OlaPlayerDetailsBean playerBean = new OlaPlayerDetailsBean();
		playerBean.setFirstName(firstName);
		playerBean.setWalletId(walletId);
		playerBean.setLastName(lastName);
		playerBean.setGender(gender);
		if( dateOfBirth == null || dateOfBirth.trim().isEmpty()){
			dateOfBirth="0000-00-00";
		}
		playerBean.setDateOfBirth(dateOfBirth);
		playerBean.setUsername(username);
		playerBean.setPassword(password1);
		playerBean.setEmail(email);
		playerBean.setPhone(phone);
		playerBean.setAddress(address);
		playerBean.setCity(city);
		playerBean.setState(state);
		playerBean.setCountry(country);
		playerBean.setRequestIp(getRequest().getRemoteHost());
		String result ;
		if(devWalletName.equalsIgnoreCase("KhelPlayRUMMY")){
			
			
			flexiCardPurchaseBean = helper.registerPlayerForKpRummy(walletId,devWalletName,userBean,depositAnyWhere,playerBean,deposit);
			if(flexiCardPurchaseBean.isSuccess()){
				
				session.setAttribute("cashCardList",flexiCardPurchaseBean);
				return SUCCESS;
			}
			else{
				addActionMessage(flexiCardPurchaseBean.getReturnType());
				return ERROR;
			}
		}else	if(devWalletName.equalsIgnoreCase("RUMMY")){
			int   validMonths= Integer.parseInt((String) sc.getAttribute("olaDepositExpiry"));
			String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
			String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
			flexiCardPurchaseBean = helper.registerPlayer(walletId,devWalletName,userBean,depositAnyWhere,playerBean,deposit,rootPath,validMonths,desKey,propKey);
			if(flexiCardPurchaseBean.isSuccess()){
				
				session.setAttribute("cashCardList",flexiCardPurchaseBean);
				return SUCCESS;
			}
			else{
				addActionMessage(flexiCardPurchaseBean.getReturnType());
				return ERROR;
			}
		}else if(devWalletName.equalsIgnoreCase("PLAYTECH_CASINO")) {
			result = helper.savePlayerDetails(walletId,devWalletName,userBean,depositAnyWhere,playerBean,deposit,rootPath);
			
			if(result.equalsIgnoreCase("true")){
				return SUCCESS;
			}
			else{
				addActionMessage(result);
				return ERROR;
			}
		}else if(devWalletName.equalsIgnoreCase("PLAYER_LOTTERY")){
			playerBean.setPassword("");
			flexiCardPurchaseBean = helper.registerPlayerForPMS(walletId,devWalletName,userBean,depositAnyWhere,playerBean,deposit,rootPath);
			if(flexiCardPurchaseBean.isSuccess()){
				
				session.setAttribute("cashCardList",flexiCardPurchaseBean);
				return SUCCESS;
			}
			else{
				addActionMessage(flexiCardPurchaseBean.getReturnType());
				return ERROR;
			}
			
		}
		
		return ERROR;
	}
	public void checkAvlOrgUser() throws LMSException {
		int  walletId=0;
		try {
			CreateNewPlayerHelper helper = new CreateNewPlayerHelper();
			Map<String, String> errorMap=null;
			PrintWriter pw = response.getWriter();
			String devWalletName = null;
			if(walletName.equalsIgnoreCase("-1")||walletName.equalsIgnoreCase("null")||walletName==null){
				pw.print("");
			}
			else{
				String[] walletArr = walletName.split(":");
				walletId = Integer.parseInt(walletArr[0]);
				devWalletName = walletArr[1];
			}
			if(devWalletName.equalsIgnoreCase("KhelPlayRummy")){
				OlaHelper olaHelper = new OlaHelper();
				if(username!=null){
					errorMap=olaHelper.verifyPlrName(username,walletId,"USER_AVAILABILITY");
				}
				if(email!=null){
					 errorMap = helper.verifyEmailForKpRummy(email,walletId);
				}
				if(phone!=null){
					 errorMap = helper.verifyPhoneForKpRummy(phone, walletId);
				}
			}else if(devWalletName.equalsIgnoreCase("RUMMY")){
				if(username!=null){
					errorMap = helper.verifyOrgName(username);
				}
				if(email!=null){
					 errorMap = helper.verifyEmail(email);
				}
				
			}else if(devWalletName.equalsIgnoreCase("PLAYER_LOTTERY")){
				errorMap=helper.verifyPlrName(username);
			}
			
			
			pw.print(errorMap.toString().replace("{", "").replace("}", ""));
			} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException();
		} 
	}
	
	

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;		
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;		
	}
	public String getWalletName() {
		return walletName;
	}
	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

}