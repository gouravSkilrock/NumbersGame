package com.skilrock.lms.embedded.ola.olaMgmt;

import java.io.IOException;
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

import com.skilrock.lms.beans.FlexiCardPurchaseBean;
import com.skilrock.lms.beans.OlaPlayerDetailsBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.CreateNewPlayerHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.ola.CreateNewPlayerAction;

public class CreateNewPlayerRpos implements ServletRequestAware,
		ServletResponseAware {
	static Log logger = LogFactory.getLog(CreateNewPlayerAction.class);
	private String fName;
	private String lName;
	private String gender;
	private String dob;
	private String userName;
	private String password1;
	private String email;
	private String userPhone;
	private String address;
	private String city;
	private String state;
	private String country;
	private double amount;
	private HttpServletRequest servletRequest;
	private HttpServletResponse response;
	private HttpSession session = null;
	private int walletId;
	private String plrId;
	

	public void createNewPlayer() throws LMSException {
		String walletName = null;
		try {
			if (walletId > 0) {
				walletName = OLAUtility.getWalletName(walletId);
			}
			long rnumber;

			if (walletName == null) {
				response.getOutputStream().write(
						("ErrorMsg: " + EmbeddedErrors.PURCHSE_INVALID_DATA)
								.getBytes());
			}

			CreateNewPlayerHelper helper = new CreateNewPlayerHelper();
			ServletContext sc = ServletActionContext.getServletContext();
			String depositAnyWhere = (String) sc
					.getAttribute("OLA_DEP_ANYWHERE");
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			session =  (HttpSession)currentUserSessionMap.get(userName);
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			FlexiCardPurchaseBean flexiCardPurchaseBean = new FlexiCardPurchaseBean();
			OlaPlayerDetailsBean playerBean = new OlaPlayerDetailsBean();
			playerBean.setFirstName(fName);
			playerBean.setWalletId(walletId);
			playerBean.setLastName(lName);
			playerBean.setGender(gender);
			playerBean.setDateOfBirth(dob);
			playerBean.setUsername(plrId.trim());
			playerBean.setEmail(email);
			playerBean.setPhone(userPhone);
			playerBean.setAddress(address);
			playerBean.setCity(city);
			playerBean.setState(state);
			playerBean.setCountry(country);

			if (walletName.equalsIgnoreCase("RUMMY")) {
				rnumber = RNGUtilities.generateRandomNumber(1000000000l,
						9999999999l);
				setPassword1(((Long) rnumber).toString());
				playerBean.setPassword(password1);
				int validMonths = Integer.parseInt((String) sc
						.getAttribute("olaDepositExpiry"));
				String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
				String propKey = (String) sc.getAttribute("OLA_PIN_AES_KEY");
				flexiCardPurchaseBean = helper.registerPlayer(walletId,
						walletName, userBean, depositAnyWhere, playerBean,
						amount, null, validMonths, desKey, propKey);
				if (flexiCardPurchaseBean.isSuccess()) {
					response
							.getOutputStream()
							.write(
									("SuccessMsg: Player  Registered Successfully With Info User Name : "
											+ flexiCardPurchaseBean
													.getPlayerName()
											+ "Deposit Amount: "
											+ flexiCardPurchaseBean.getAmount()
											+ "Password :  "
											+ playerBean.getPassword()
											+ "Deposit RefCode:" + flexiCardPurchaseBean
											.getSerialNumber()).getBytes());

				} else {
					response.getOutputStream().write(
							("ErrorMsg: " + flexiCardPurchaseBean
									.getReturnType()).getBytes());

				}
			} else if (walletName.equalsIgnoreCase("PLAYER_LOTTERY")) {
				playerBean.setPassword("");
				flexiCardPurchaseBean = helper.registerPlayerForPMS(walletId,
						walletName, userBean, depositAnyWhere, playerBean,
						amount, null);
				if (flexiCardPurchaseBean.isSuccess()) {
					response.getOutputStream().write(
							("SuccessMsg: Player  Registered Successfully")
									.getBytes());
				} else {
					response.getOutputStream().write(
							("ErrorMsg: " + flexiCardPurchaseBean
									.getReturnType()).getBytes());

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write(
						("ErrorMsg: " + EmbeddedErrors.PURCHSE_INVALID_DATA)
								.getBytes());
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}

	}

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.response = servletResponse;

	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlrId() {
		return plrId;
	}

	public void setPlrId(String plrId) {
		this.plrId = plrId;
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

	public int getWalletId() {
		return walletId;
	}

	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
