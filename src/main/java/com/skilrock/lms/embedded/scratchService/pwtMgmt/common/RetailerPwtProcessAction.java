package com.skilrock.lms.embedded.scratchService.pwtMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.RetailerPwtProcessHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class RetailerPwtProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 8715168560239123800L;

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		String month = ("" + (cal.get(Calendar.MONTH) + 1)).length() == 2 ? ""
				+ (cal.get(Calendar.MONTH) + 1) : "0"
				+ (cal.get(Calendar.MONTH) + 1);
		String date = ("" + cal.get(Calendar.DATE)).length() == 2 ? ""
				+ cal.get(Calendar.DATE) : "0" + cal.get(Calendar.DATE);

		String vDate = date + "-" + month + "-" + cal.get(Calendar.YEAR);

		String hour = ("" + cal.get(Calendar.HOUR)).length() == 2 ? ""
				+ cal.get(Calendar.HOUR) : "0" + cal.get(Calendar.HOUR);
		String min = ("" + cal.get(Calendar.MINUTE)).length() == 2 ? ""
				+ cal.get(Calendar.MINUTE) : "0" + cal.get(Calendar.MINUTE);
		String sec = ("" + cal.get(Calendar.SECOND)).length() == 2 ? ""
				+ cal.get(Calendar.SECOND) : "0" + cal.get(Calendar.SECOND);

		String time = hour + "-" + min + "-" + sec;
		System.out.println(vDate);
		System.out.println(time);
	}

	private String firstName;
	private String gameNo;
	private String idNumber;
	private String idType;
	private String lastName;

	private String playerId = null;

	private String playerType;

	private String plrAlreadyReg;

	private Map pwtAppMap;
	/**
	 * This Method is used to verify PWT Ticket and VIRN Entries
	 * 
	 * @throws LMSException
	 * @throws
	 */

	Map<String, Object> pwtErrorMap;
	private HttpServletResponse response;
	private HttpSession session;

	private String ticketNbr;
	private String userName;

	private String virnNbr;

	// common function to be called
	public void fetchPwtGameDetails() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			String gameDetails = helper.fetchPwtGameDetails();
			System.out.println("game details String on retailer PWT == "
					+ gameDetails);
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

	public String getFirstName() {
		return firstName;
	}

	public String getGameNo() {
		return gameNo;
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

	public String getPlayerId() {
		return playerId;
	}

	public String getPlayerType() {
		return playerType;
	}

	public String getPlrAlreadyReg() {
		return plrAlreadyReg;
	}

	public Map getPwtAppMap() {
		return pwtAppMap;
	}

	public Map<String, Object> getPwtErrorMap() {
		return pwtErrorMap;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String getUserName() {
		return userName;
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
	public void plrRegistrationAndApproval() throws LMSException {
		PlayerBean plrBean = null;
		this.pwtAppMap = null;
		System.out.println("plrAlreadyReg = " + plrAlreadyReg
				+ "  , playerType = " + playerType + "  , playerId = "
				+ playerId + "  ");

		// code to get logged in users country state information
		ServletContext sc = ServletActionContext.getServletContext();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// System.out.println(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);

		System.out.println(" user name is " + userName);

		session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Connection con = DBConnect.getConnection();
		Statement stmt;
		String countryName = "";
		String stateName = "";
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select  a.name country,b.name state from st_lms_country_master a,st_lms_state_master b ,st_lms_organization_master c where c.organization_id="
							+ userBean.getUserOrgId()
							+ " and c.country_code=a.country_code and c.state_code=b.state_code");
			if (rs.next()) {
				countryName = rs.getString("country");
				stateName = rs.getString("state");
			} else {
				throw new LMSException();
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new LMSException();
		}

		if ("player".equalsIgnoreCase(playerType.trim())
				&& !"YES".equalsIgnoreCase(plrAlreadyReg.trim())) {
			plrBean = new PlayerBean();
			plrBean.setFirstName(firstName);
			plrBean.setLastName(lastName);
			plrBean.setIdType(idType);
			plrBean.setIdNumber(idNumber);
			plrBean.setEmailId("NA");
			plrBean.setPhone("NA");
			plrBean.setPlrAddr1("NA");
			plrBean.setPlrAddr2("NA");
			plrBean.setPlrState(stateName);
			plrBean.setPlrCity("NA");
			plrBean.setPlrCountry(countryName);
			plrBean.setPlrPin(0);
			plrBean.setBankName("NA");
			plrBean.setBankBranch("NA");
			plrBean.setLocationCity("NA");
			plrBean.setBankAccNbr("000");
			System.out.println("Inside player registration 11111 & plrBean is "
					+ plrBean);
		}
		System.out.println(" LOGGED_IN_USERS--------");
		// System.out.println(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);

		System.out.println(" user name is " + userName);

		Map pwtDetails = (Map) session.getAttribute("pwtDetailMap");

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}

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
		currentUserSessionMap.put(userName, session);
		sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
		try {
			Calendar cal = Calendar.getInstance();
			String month = ("" + (cal.get(Calendar.MONTH) + 1)).length() == 2 ? ""
					+ (cal.get(Calendar.MONTH) + 1)
					: "0" + (cal.get(Calendar.MONTH) + 1);
			String date = ("" + cal.get(Calendar.DATE)).length() == 2 ? ""
					+ cal.get(Calendar.DATE) : "0" + cal.get(Calendar.DATE);

			String vDate = date + "-" + month + "-" + cal.get(Calendar.YEAR);

			String hour = ("" + cal.get(Calendar.HOUR)).length() == 2 ? ""
					+ cal.get(Calendar.HOUR) : "0" + cal.get(Calendar.HOUR);
			String min = ("" + cal.get(Calendar.MINUTE)).length() == 2 ? ""
					+ cal.get(Calendar.MINUTE) : "0" + cal.get(Calendar.MINUTE);
			String sec = ("" + cal.get(Calendar.SECOND)).length() == 2 ? ""
					+ cal.get(Calendar.SECOND) : "0" + cal.get(Calendar.SECOND);

			String time = hour + ":" + min + ":" + sec;

			TicketBean tktBean = (TicketBean) pwtDetails.get("tktBean");
			PwtBean pwtBean = (PwtBean) pwtDetails.get("pwtBean");
			String gameName = (String) session.getAttribute("GAME_NAME");
			String returnData = "RetName:" + userInfoBean.getOrgName()
					+ "|FirstName:" + plrBean.getFirstName() + "|LastName:"
					+ plrBean.getLastName() + "|VoucherNo:"
					+ pwtAppMap.get("recId") + "|VoucherDate:" + vDate
					+ "|IDType:" + plrBean.getIdType() + "|IDNumber:"
					+ plrBean.getIdNumber() + "|AmountType:PWT Amt.|GameNo:"
					+ tktBean.getGameNbr() + "|GameName:" + gameName
					+ "|TktNo:" + tktBean.getTicketNumber() + "|VIRNNO:"
					+ pwtBean.getVirnCode() + "|Amount:"
					+ pwtBean.getPwtAmount() + "|GenDate:" + vDate
					+ "|GenTime:" + time + "|Message:Go For Payments|";
			System.out.println("RETUNR STRING AFTER PLAYER REGISTRATION-----"
					+ returnData);
			response.getOutputStream().write(returnData.getBytes());
		} catch (IOException e) {
			System.out.println("=---------ERROR IN REGISTRATION-----------=");
			e.printStackTrace();
			throw new LMSException();
		}
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
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

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public void setPlrAlreadyReg(String plrAlreadyReg) {
		this.plrAlreadyReg = plrAlreadyReg;
	}

	public void setPwtAppMap(Map pwtAppMap) {
		this.pwtAppMap = pwtAppMap;
	}

	public void setPwtErrorMap(Map<String, Object> pwtErrorMap) {
		this.pwtErrorMap = pwtErrorMap;
	}

	public void setServletRequest(HttpServletRequest req) {
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setVirnNbr(String virnNbr) {
		this.virnNbr = virnNbr;
	}

	public void verifyAndSaveTicketNVirn() throws LMSException {
		try {
			ServletContext sc = ServletActionContext.getServletContext();
			String isDraw = (String) sc.getAttribute("IS_SCRATCH");
			if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(
						"Scratch Game Not Avaialbe".getBytes());
				return;
			}

			// get game name and id for given game number

			String query = " select game_name,game_id from st_se_game_master where game_nbr=? and game_status in('SALE_CLOSE','OPEN','SALE_HOLD')";
					

			Connection con = DBConnect.getConnection();
			String gameName = null;
			int gameId = 0;

			PreparedStatement pStatement = con.prepareStatement(query);
			pStatement.setInt(1,Integer.parseInt(gameNo.trim()));
			ResultSet rs = pStatement.executeQuery();
			System.out.println(" before rs----------" + pStatement);
			while (rs.next()) {
				System.out.println(" in rs ");
				gameName = rs.getString("game_name");
				gameId = rs.getInt("game_id");
			}
			System.out.println(" game name and id " + gameName + " " + gameId);
			if (gameName == null) {
				response.getOutputStream().write(
						"Game number is not Valid".getBytes());
				return;
			}

			System.out.println(" game id number name before appending is "
					+ gameNo);
			gameNo = gameId + "-" + gameNo + "-" + gameName;
			System.out.println(" game id number name after appending is "
					+ gameNo);
			//

			System.out.println(" inside verification of virn ");
			System.out.println(" input data is " + gameNo + " " + ticketNbr
					+ " " + virnNbr);
			if (ticketNbr.indexOf("-") == -1) {
				query = " select game_nbr_digits,pack_nbr_digits,book_nbr_digits,ticket_nbr_digits from st_se_game_ticket_nbr_format where game_id="
						+ gameId;
				pStatement = con.prepareStatement(query);

				rs = pStatement.executeQuery();
				while (rs.next()) {

					int gameNoDigit = rs.getInt("game_nbr_digits");
					int packDigit = rs.getInt("pack_nbr_digits");
					int bookDigit = rs.getInt("book_nbr_digits");
					int tktDigit = rs.getInt("ticket_nbr_digits");

					ticketNbr = ticketNbr.substring(0, gameNoDigit)
							+ "-"
							+ ticketNbr.substring(gameNoDigit, (gameNoDigit
									+ packDigit + bookDigit))
							+ "-"
							+ ticketNbr
									.substring((gameNoDigit + packDigit + bookDigit));
				}
				con.close();

			}
			String highPrizeCriteria = (String) ServletActionContext
					.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
			String highPrizeAmt = (String) ServletActionContext
					.getServletContext().getAttribute("HIGH_PRIZE_AMT");
			System.out.println("g= " + gameNo + " t = " + ticketNbr
					+ " virn = " + virnNbr + "high prize amt = " + highPrizeAmt
					+ " and  highPrizeCriteria = " + highPrizeCriteria);

			// ServletContext sc = ServletActionContext.getServletContext();

			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			/*
			 * System.out.println(" LOGGED_IN_USERS maps is " +
			 * currentUserSessionMap);
			 */

			System.out.println(" user name is " + userName);

			session = (HttpSession) currentUserSessionMap.get(userName);
			// session = (HttpSession) sc.getAttribute(userName);

			System.out.println(" session is in PWT RETAILER " + session);

			if (!CommonFunctionsHelper.isSessionValid(session)) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}

			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			
			//added for interface and channel 
			userInfoBean.setChannel("RETAILER");
			userInfoBean.setInterfaceType("TERMINAL");
			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (highPrizeCriteria == null || highPrizeAmt == null
					|| userInfoBean == null) {
				throw new LMSException("highPrizeCriteria = "
						+ highPrizeCriteria + " or highPrizeAmt = " + null
						+ " or userInfoBean = " + userInfoBean);
			}

			/*new AjaxRequestHelper().getAvlblCreditAmt(userInfoBean);
			double bal1 = userInfoBean.getAvailableCreditLimit()
					- userInfoBean.getClaimableBal();
*/
			String gameArr[] = gameNo.split("-"); //
			RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
			Map<String, Object> pwtDetailMap = helper.verifyAndSaveTicketNVirn(
					ticketNbr, virnNbr, Integer.parseInt(gameArr[0]),
					gameArr[1], userInfoBean, highPrizeCriteria, highPrizeAmt);

			// /change by mukul
			// PwtBean pwtBean = new PwtBean();
			// pwtBean.setAppReq(true);
			// pwtBean.setHighLevel(true);
			// pwtBean.setPwtAmount("100000.01");
			// pwtBean.setEncVirnCode("2457238hjs");
			// pwtBean.setInUnclmed("YES");
			// pwtBean.setValid(true);
			// pwtBean.setVirnCode(virnNbr);
			// pwtDetailMap.put("pwtBean", pwtBean);
			// TicketBean tktBean1 = (TicketBean) pwtDetailMap.get("tktBean");
			// tktBean1.setUpdateTicketType("UPDATE");
			// tktBean1.setBook_nbr("049");
			// tktBean1.setBookStatus("OPEN");
			// tktBean1.setGameNbr(101);
			// tktBean1.setMessageCode("sdfd");
			// tktBean1.setStatus("aa");
			// tktBean1.setTicketGameId(10);
			// tktBean1.setTicketNumber("101-082049-001");
			// tktBean1.setValid(true);
			// tktBean1.setValidity("true");
			// tktBean1.set

			// pwtDetailMap.put("tktBean", tktBean1);
			// pwtBean.set

			session.setAttribute("pwtDetailMap", pwtDetailMap);
			session.setAttribute("GAME_NAME", gameName);
			pwtErrorMap = pwtDetailMap;

			new AjaxRequestHelper().getAvlblCreditAmt(userInfoBean);
			double bal2 = userInfoBean.getAvailableCreditLimit()
					- userInfoBean.getClaimableBal();
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			
			String balance = nf.format(bal2).replaceAll(",", "");
			
		/*	double bal = bal2 - bal1;
			String balance = bal + "00";
			balance = balance.substring(0, balance.indexOf(".") + 3);*/

			if (pwtDetailMap != null && pwtDetailMap.containsKey("returnType")) {
				String returnType = (String) pwtDetailMap.get("returnType");
				TicketBean tktBean = (TicketBean) pwtDetailMap.get("tktBean");
				PwtBean pwtBean = (PwtBean) pwtDetailMap.get("pwtBean");

				System.out.println("pwt type return = " + returnType);

				if (returnType.equals("registration")) {
					response.getOutputStream()
							.write("Register User".getBytes());
					System.out.println("Register User");
				} else if (returnType.equals("success")) {

					returnType = "Ticket Validity:" + tktBean.getValidity()
							+ "\n" + "Ticket Message:" + tktBean.getStatus()
							+ "\n" + "Virn Validity:"
							+ pwtBean.getVerificationStatus() + "\n"
							+ "Virn Message:" + pwtBean.getMessage() + "\n"
							+ "Winning Amount:" + pwtBean.getPwtAmount() + "\n"
							+ "balance:" + balance;

					// response.getOutputStream()
					// .write("VIRN is Valid".getBytes());

					response.getOutputStream().write(returnType.getBytes());
				} else {
					if (pwtBean != null) {

						returnType = "Ticket Validity:" + tktBean.getValidity()
								+ "\n" + "Ticket Message:"
								+ tktBean.getStatus() + "\n" + "Virn Validity:"
								+ pwtBean.getVerificationStatus() + "\n"
								+ "Virn Message:" + pwtBean.getMessage();
					} else {
						returnType = "Ticket Validity:" + tktBean.getValidity()
								+ "\n" + "Ticket Message:"
								+ tktBean.getStatus();

					}

					response.getOutputStream().write(returnType.getBytes());
				}
			} else {
				response.getOutputStream().write("Error in input".getBytes());
			}
			currentUserSessionMap.put(userName, session);
			sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}
}
