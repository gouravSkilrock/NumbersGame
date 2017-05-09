package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PlayerVerifyHelperForApp;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.RetailerPwtProcessHelper;

public class RetailerPwtProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(RetailerPwtProcessAction.class);

	private static final long serialVersionUID = 8715168560239123800L;
	private String bankAccNbr;
	private String bankBranch;
	// by Arun Added field in DB
	private String bankName;
	private String country;
	private String emailId;
	private String firstName;

	private String gameIdNbrName;

	private String idNumber;

	private String idType;

	private String lastName;
	private String locationCity;
	private String phone;
	private String playerId;
	private String playerType;
	private String plrAddr1;
	private String plrAddr2;
	private String plrAlreadyReg;
	private String plrCity;
	private String plrCountry;
	private String plrPin;
	private Map pwtAppMap;
	private Map pwtAppMapRPOS;
	Map<String, Object> pwtDetailMapRpos;
	/**
	 * This Method is used to verify PWT Ticket and VIRN Entries
	 * 
	 * @throws LMSException
	 * @throws
	 */

	Map<String, Object> pwtErrorMap;

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private String state;

	private String ticketNbr;

	private String virnNbr;

	public String fetchPlayerDetails() throws LMSException {
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(TableConstants.PLAYER_FIRSTNAME, firstName);
		searchMap.put(TableConstants.PLAYER_LASTNAME, lastName);
		searchMap.put(TableConstants.PLAYER_IDNUMBER, idNumber);
		searchMap.put(TableConstants.PLAYER_IDTYPE, idType);
		System.out.println("player details = " + searchMap);

		PlayerVerifyHelperForApp searchPlayerHelper = new PlayerVerifyHelperForApp();
		Map<String, Object> playerBeanMap = searchPlayerHelper.searchPlayer(
				firstName, lastName, idNumber, idType);
		PlayerBean plrBean = (PlayerBean) playerBeanMap.get("plrBean");
		session = request.getSession();
		session.setAttribute("playerBean", plrBean);
		String plrAlreadyReg = "NO";
		if (plrBean != null) {
			plrAlreadyReg = "YES";
		}
		List<String> countryList = (ArrayList<String>) playerBeanMap.get("countryList");
		if (countryList == null) {
			countryList = new ArrayList<String>();
		}
		session.setAttribute("countryList", countryList);
		session.setAttribute("plrAlreadyReg", plrAlreadyReg);
		return SUCCESS;
	}

	// common function to be called
	public void fetchPwtGameDetails() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			String gameDetails = helper.fetchPwtGameDetails();
			System.out.println("game details String on retailer PWT == "+ gameDetails);
			pw.print(gameDetails);
		} catch (LMSException e) {
			e.printStackTrace();
			if (pw != null) {
				pw.print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*public void fetchPwtGameDetailsNewWinning() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			String gameDetails = helper.fetchPwtGameDetailsNewWinning( ticketNbr,  virnNbr);
			System.out.println("game details String on retailer PWT == "+ gameDetails);
			pw.print(gameDetails);
		} catch (LMSException e) {
			e.printStackTrace();
			if (pw != null) {
				pw.print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	
	public String getBankAccNbr() {
		return bankAccNbr;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public String getBankName() {
		return bankName;
	}

	public String getCountry() {
		return country;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getGameIdNbrName() {
		return gameIdNbrName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public String getPhone() {
		return phone;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getPlayerType() {
		return playerType;
	}

	public String getPlrAddr1() {
		return plrAddr1;
	}

	public String getPlrAddr2() {
		return plrAddr2;
	}

	public String getPlrAlreadyReg() {
		return plrAlreadyReg;
	}

	public String getPlrCity() {
		return plrCity;
	}

	public String getPlrCountry() {
		return plrCountry;
	}

	public String getPlrPin() {
		return plrPin;
	}

	public Map getPwtAppMap() {
		return pwtAppMap;
	}

	public Map<String, Object> getPwtErrorMap() {
		return pwtErrorMap;
	}

	public String getState() {
		return state;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String getVirnNbr() {
		return virnNbr;
	}

	/**
	 * Player registration process
	 * 
	 * @return
	 * @throws LMSException
	 */
	public String plrRegistrationAndApproval() throws LMSException {
		PlayerBean plrBean = null;
		this.pwtAppMap = null;
		System.out.println("plrAlreadyReg = " + plrAlreadyReg
				+ "  , playerType = " + playerType + "  , playerId = "
				+ playerId + "  ");

		if ("player".equalsIgnoreCase(playerType.trim())
				&& !"YES".equalsIgnoreCase(plrAlreadyReg.trim())) {
			plrBean = new PlayerBean();
			plrBean.setFirstName(firstName);
			plrBean.setLastName(lastName);
			plrBean.setIdType(idType);
			plrBean.setIdNumber(idNumber);
			plrBean.setEmailId(emailId);
			plrBean.setPhone(phone);
			plrBean.setPlrAddr1(plrAddr1);
			plrBean.setPlrAddr2(plrAddr2);
			plrBean.setPlrState(state);
			plrBean.setPlrCity(plrCity);
			plrBean.setPlrCountry(plrCountry);
			plrBean.setPlrPin(Long.parseLong(plrPin));
			plrBean.setBankName(bankName);
			plrBean.setBankBranch(bankBranch);
			plrBean.setLocationCity(locationCity);
			plrBean.setBankAccNbr(bankAccNbr);
			System.out.println("Inside player registration 11111 & plrBean is "
					+ plrBean);
		}

		session = request.getSession();
		Map pwtDetails = (Map) session.getAttribute("pwtDetailMap");

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}

		userInfoBean.setChannel("RETAIL");
		userInfoBean.setInterfaceType((String) request
				.getAttribute("interfaceType"));

		// player registration and approval process
		RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
		this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDetails, playerType, playerId, plrBean, (String) session
						.getAttribute("ROOT_PATH"));

		if (plrBean == null) {
			plrBean = (PlayerBean) session.getAttribute("playerBean");
			session.setAttribute("playerBean", null);
		}

		pwtAppMap.put("plrBean", plrBean);

		session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		return SUCCESS;
	}

	public String plrRegistrationAndApprovalRPOS() throws LMSException {
		try {
			PlayerBean plrBean = null;
			this.pwtAppMapRPOS = null;
			System.out.println("plrAlreadyReg = " + plrAlreadyReg
					+ "  , playerType = " + playerType + "  , playerId = "
					+ playerId + "  ");
			// String plrName="Anonymous";
			// PrintWriter out = response.getWriter();

			session = request.getSession();
			Map pwtDetails = (Map) session.getAttribute("pwtDetailMapRpos");

			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (userInfoBean == null) {
				throw new LMSException("userInfoBean = " + userInfoBean);
			}
			String PlayerName = "Anonymous";

			if ("player".equalsIgnoreCase(playerType.trim())) {
				// plrName = firstName+" "+lastName;
				PlayerVerifyHelperForApp verifyHelper = new PlayerVerifyHelperForApp();
				playerId = verifyHelper.verifyPlayer(firstName, lastName,
						idNumber, idType)
						+ "";
				PlayerName = firstName + " " + lastName;
				if (playerId == "0") {
					String orgLocationDtls = verifyHelper
							.getCountryNStateForPlrRpos(userInfoBean
									.getUserOrgId());

					plrBean = new PlayerBean();
					plrBean.setFirstName(firstName);
					plrBean.setLastName(lastName);
					plrBean.setIdType(idType);
					plrBean.setIdNumber(idNumber);
					plrBean.setEmailId("NA");
					plrBean.setPhone("NA");
					plrBean.setPlrAddr1("NA");
					plrBean.setPlrAddr2("NA");
					plrBean.setPlrState(orgLocationDtls.split("::")[1]);
					plrBean.setPlrCity(orgLocationDtls.split("::")[2]);
					plrBean.setPlrCountry(orgLocationDtls.split("::")[0]);
					plrBean.setPlrPin(0);
					plrBean.setBankName(null);
					plrBean.setBankBranch(null);
					plrBean.setLocationCity(null);
					plrBean.setBankAccNbr(null);
					System.out
							.println("Inside player registration 11111 & plrBean is "
									+ plrBean);
				} else {
					System.out
							.println("Player is already registered with player id  "
									+ playerId);
				}
			} else {
				System.out.println("player is anonymous");
			}

			userInfoBean.setChannel("RETAIL");
			userInfoBean.setInterfaceType((String) request
					.getAttribute("interfaceType"));

			// player registration and approval process
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			this.pwtAppMapRPOS = helper.plrRegistrationAndApproval(
					userInfoBean, pwtDetails, playerType, playerId, plrBean,
					(String) session.getAttribute("ROOT_PATH"));

			/*
			 * if(plrBean==null) { plrBean =
			 * (PlayerBean)session.getAttribute("playerBean");
			 * session.setAttribute("playerBean", null); }
			 */

			// pwtAppMap.put("plrBean", plrBean);
			pwtAppMapRPOS.put("playerName", PlayerName);
			pwtAppMapRPOS.put("idNumber", idNumber);
			pwtAppMapRPOS.put("idType", idType);
			pwtAppMapRPOS.put("playerType", playerType.trim());

			session.setAttribute("plrPwtAppDetMapRPOS", pwtAppMapRPOS);
			// out.print("success"+"::"+plrName+"::"+pwtAppMap.get("recId"));
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public void setBankAccNbr(String bankAccNbr) {
		this.bankAccNbr = bankAccNbr;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGameIdNbrName(String gameIdNbrName) {
		this.gameIdNbrName = gameIdNbrName;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public void setPlrAddr1(String plrAddr1) {
		this.plrAddr1 = plrAddr1;
	}

	public void setPlrAddr2(String plrAddr2) {
		this.plrAddr2 = plrAddr2;
	}

	public void setPlrAlreadyReg(String plrAlreadyReg) {
		this.plrAlreadyReg = plrAlreadyReg;
	}

	public void setPlrCity(String plrCity) {
		this.plrCity = plrCity;
	}

	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}

	public void setPlrPin(String plrPin) {
		this.plrPin = plrPin;
	}

	public void setPwtAppMap(Map pwtAppMap) {
		this.pwtAppMap = pwtAppMap;
	}

	public void setPwtErrorMap(Map<String, Object> pwtErrorMap) {
		this.pwtErrorMap = pwtErrorMap;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setVirnNbr(String virnNbr) {
		this.virnNbr = virnNbr;
	}

	public void verifyTicketAndVirnNumber() throws LMSException {
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = null;
		response.setContentType("application/json");
		try {
			out = response.getWriter();
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			gameIdNbrName = helper.fetchPwtGameDetailsNewWinning( ticketNbr,  virnNbr);
			if (!gameIdNbrName.trim().isEmpty()) {
				String highPrizeCriteria = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
    			String highPrizeAmount = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
    			logger.info("gameIdNbrName= " + gameIdNbrName + " ticketNbr = " + ticketNbr
    					+ " virnNbr = " + virnNbr + " high prize amount = " + highPrizeAmount
    					+ " and  highPrizeCriteria = " + highPrizeCriteria);
    			session = request.getSession();
    			UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
    			if (highPrizeCriteria == null || highPrizeAmount == null || userInfoBean == null) {
    				throw new LMSException("highPrizeCriteria = "
    						+ highPrizeCriteria + " or highPrizeAmount = " + null
    						+ " or userInfoBean = " + userInfoBean);
    			}
    			String gameArr[] = gameIdNbrName.split("-");
    			userInfoBean.setChannel("RETAIL");
    			userInfoBean.setInterfaceType((String) request.getAttribute("interfaceType"));
    			Map<String, Object> pwtDetailMap = helper.verifyTicketAndVirnNumber(
    					ticketNbr, virnNbr, Integer.parseInt(gameArr[0]),
    					gameArr[1], userInfoBean, highPrizeCriteria, highPrizeAmount);
    			session.setAttribute("pwtDetailMap", pwtDetailMap);
    			pwtErrorMap = pwtDetailMap;
    			if (pwtDetailMap != null && pwtDetailMap.containsKey("returnType")) {
    				//String returnType = (String) pwtDetailMap.get("returnType");
    				jsonObject.put("isSuccess", true);
    				jsonObject.put("responseCode", 0);
    				jsonObject.put("responseMsg", "Success");
    				jsonObject.put("pwtDetailMap", pwtDetailMap);
    			}else{
    				jsonObject.put("isSuccess", false);
    				jsonObject.put("responseCode", 500);
    				jsonObject.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
    			}
			}
		}catch (LMSException e) {
			jsonObject.put("isSuccess", false);
			jsonObject.put("responseCode", e.getErrorCode());
			jsonObject.put("responseMsg", e.getErrorMessage());
		}catch(Exception e){
			e.printStackTrace();
			jsonObject.put("isSuccess", false);
			jsonObject.put("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonObject.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(jsonObject);
		out.flush();
		out.close();
		
	}
	public void verifyAndSaveTicketNVirn() throws LMSException {
		  JSONObject js = new JSONObject();
		  PrintWriter out = null;
		  response.setContentType("application/json");
		try{
            out=response.getWriter();
            RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
            gameIdNbrName = helper.fetchPwtGameDetailsNewWinning( ticketNbr,  virnNbr);
            if(!gameIdNbrName.trim().isEmpty()){
    			String highPrizeCriteria = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
    			String highPrizeAmt = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
    			System.out.println("g= " + gameIdNbrName + " t = " + ticketNbr
    					+ " virn = " + virnNbr + "high prize amt = " + highPrizeAmt
    					+ " and  highPrizeCriteria = " + highPrizeCriteria);
    			session = request.getSession();
    			UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
    			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
    			if (highPrizeCriteria == null || highPrizeAmt == null || userInfoBean == null) {
    				throw new LMSException("highPrizeCriteria = "
    						+ highPrizeCriteria + " or highPrizeAmt = " + null
    						+ " or userInfoBean = " + userInfoBean);
    			}
    			String gameArr[] = gameIdNbrName.split("-"); //	
    			userInfoBean.setChannel("RETAIL");
    			userInfoBean.setInterfaceType((String) request.getAttribute("interfaceType"));
    			Map<String, Object> pwtDetailMap = helper.verifyAndSaveTicketNVirn(
    					ticketNbr, virnNbr, Integer.parseInt(gameArr[0]),
    					gameArr[1], userInfoBean, highPrizeCriteria, highPrizeAmt);
    			session.setAttribute("pwtDetailMap", pwtDetailMap);
    			pwtErrorMap = pwtDetailMap;
    			if (pwtDetailMap != null && pwtDetailMap.containsKey("returnType")) {
    				//String returnType = (String) pwtDetailMap.get("returnType");
    					js.put("isSuccess", true);
    					js.put("responseCode", 0);
    					js.put("responseMsg", "Success");
    	            	js.put("pwtDetailMap", pwtDetailMap);
    			}else{
    				js.put("isSuccess", false);
    				js.put("responseCode", 500);
    				js.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
    			}
            }
		}catch (LMSException e) {
			js.put("isSuccess", false);
			js.put("responseCode", e.getErrorCode());
			js.put("responseMsg", e.getErrorMessage());
		}catch(Exception e){
			e.printStackTrace();
			js.put("isSuccess", false);
			js.put("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			js.put("responseMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		out.print(js);
		out.flush();
		out.close();
	}

	// this method will be called through AJAX so will return void
	public String verifyAndSaveTicketNVirnRPOS() throws LMSException {
		try {
			// PrintWriter out = response.getWriter();

			String highPrizeCriteria = (String) ServletActionContext
					.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
			String highPrizeAmt = (String) ServletActionContext
					.getServletContext().getAttribute("HIGH_PRIZE_AMT");
			System.out.println("g= " + gameIdNbrName + " t = " + ticketNbr
					+ " virn = " + virnNbr + "high prize amt = " + highPrizeAmt
					+ " and  highPrizeCriteria = " + highPrizeCriteria);
			session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			userInfoBean.setChannel("RETAIL");
			userInfoBean.setInterfaceType((String) request
					.getAttribute("interfaceType"));
			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (highPrizeCriteria == null || highPrizeAmt == null
					|| userInfoBean == null) {
				throw new LMSException("highPrizeCriteria = "
						+ highPrizeCriteria + " or highPrizeAmt = " + null
						+ " or userInfoBean = " + userInfoBean);
			}

			String gameArr[] = gameIdNbrName.split("-"); //
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			Map<String, Object> pwtDetailMap = helper.verifyAndSaveTicketNVirn(
					ticketNbr, virnNbr, Integer.parseInt(gameArr[0]),
					gameArr[1], userInfoBean, highPrizeCriteria, highPrizeAmt);
			pwtDetailMap.put("virn", virnNbr);
			session.setAttribute("pwtDetailMapRpos", pwtDetailMap);
			pwtDetailMapRpos = pwtDetailMap;

			return SUCCESS;
			/*
			 * if(pwtDetailMap!=null && pwtDetailMap.containsKey("returnType")) {
			 * String returnType = (String)pwtDetailMap.get("returnType");
			 * System.out.println("pwt type return = "+returnType);
			 * //session.setAttribute("", arg1) String ticketMessage=""; String
			 * virnMessage=""; String ticketNumbr=""; String virnNumber="";
			 * String winningAmt="0.0"; if(pwtDetailMap.get("tktBean") !=null){
			 * ticketMessage =
			 * ((TicketBean)pwtDetailMap.get("tktBean")).getStatus();
			 * ticketNumbr =
			 * ((TicketBean)pwtDetailMap.get("tktBean")).getTicketNumber(); }
			 * if(pwtDetailMap.get("pwtBean") != null){ virnMessage =
			 * ((PwtBean)pwtDetailMap.get("pwtBean")).getMessage(); virnNumber =
			 * ((PwtBean)pwtDetailMap.get("pwtBean")).getVirnCode(); }
			 * 
			 * if("success".equalsIgnoreCase(returnType)) winningAmt =
			 * ((PwtBean)pwtDetailMap.get("pwtBean")).getPwtAmount();
			 * 
			 * //returnrype::ticketerror::virnerror
			 * out.print(returnType+"::"+ticketMessage+"::"+virnMessage+"::"+ticketNumbr+"::"+virnNumber+"::"+winningAmt);
			 * return returnType; } //out.print("pwtDetailMap"); return INPUT;
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

}
