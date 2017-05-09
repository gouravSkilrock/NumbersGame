package com.skilrock.lms.web.drawGames.pwtMgmt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.PlayerVerifyHelperForApp;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.RetailerPwtProcessHelper;

public class RetailerPwtProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 8715168560239123800L;

	/**
	 * This Method is used to verify PWT Ticket and VIRN Entries
	 * 
	 * @throws LMSException
	 * @throws
	 */

	/*
	 * public Map<String, Object> verifyAndSaveDrawTicket(PrizeWinningTicketB
	 * winningBean){ try {
	 * 
	 * String highPrizeCriteria =
	 * (String)ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
	 * String highPrizeAmt =
	 * (String)ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
	 * session = request.getSession(); UserInfoBean userInfoBean =
	 * (UserInfoBean) session.getAttribute("USER_INFO"); // if HIGH
	 * HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
	 * if((highPrizeCriteria==null) || (highPrizeAmt == null) || userInfoBean ==
	 * null){
	 * 
	 * logger.debug("highPrizeCriteria = "+highPrizeCriteria+" or highPrizeAmt = "
	 * +null+" or userInfoBean = "+userInfoBean); Map<String, Object> detailMap =
	 * new TreeMap<String, Object>(); detailMap.put("returnType", "error");
	 * return detailMap; } // game id from database using game number int
	 * gameId=1; int gameNbr=winningBean.getGameNo(); Long
	 * ticketNumber=Long.parseLong(winningBean.getTicketNo());
	 * 
	 * List<DrawTicketWinningBean> drawWinningList =
	 * winningBean.getDrawWinningList(); RetailerPwtProcessHelper helper = new
	 * RetailerPwtProcessHelper(); return
	 * helper.verifyTicket(ticketNumber,gameId, gameNbr,userInfoBean,
	 * highPrizeCriteria, highPrizeAmt,drawWinningList);
	 * 
	 * }catch (Exception e) { e.printStackTrace(); Map<String, Object>
	 * detailMap = new TreeMap<String, Object>(); detailMap.put("returnType",
	 * "error"); return detailMap; } }
	 */

	private String firstName;
	private String idNumber;
	private String idType;

	private String lastName;
	Log logger = LogFactory.getLog(RetailerPwtProcessAction.class);
	/*
	 * private String emailId; private String phone; private String plrAddr1;
	 * private String plrAddr2; private String state; private String plrCity;
	 * private String plrCountry; private String country; private String plrPin;
	 * private String plrAlreadyReg;
	 */
	private int playerId;
	private String playerType;
	private Map pwtAppMap;

	/*
	 * //by Arun Added field in DB private String bankName; private String
	 * bankBranch; private String locationCity; private String bankAccNbr;
	 */

	/*
	 * public String fetchPlayerDetails() throws LMSException { Map<String,String>
	 * searchMap = new HashMap<String,String>();
	 * searchMap.put(TableConstants.PLAYER_FIRSTNAME, firstName);
	 * searchMap.put(TableConstants.PLAYER_LASTNAME, lastName);
	 * searchMap.put(TableConstants.PLAYER_IDNUMBER, idNumber);
	 * searchMap.put(TableConstants.PLAYER_IDTYPE, idType); logger.debug("player
	 * details = "+searchMap);
	 * 
	 * PlayerVerifyHelperForApp searchPlayerHelper = new
	 * PlayerVerifyHelperForApp(); Map<String, Object> playerBeanMap =
	 * searchPlayerHelper.searchPlayer(firstName, lastName, idNumber, idType);
	 * PlayerBean plrBean = (PlayerBean)playerBeanMap.get("plrBean"); session =
	 * request.getSession(); session.setAttribute("playerBean", plrBean); String
	 * plrAlreadyReg = "NO"; if (plrBean!=null) { plrAlreadyReg = "YES"; } List<String>
	 * countryList = (ArrayList<String>)playerBeanMap.get("countryList");
	 * if(countryList== null) countryList = new ArrayList<String>();
	 * session.setAttribute("countryList", countryList);
	 * session.setAttribute("plrAlreadyReg", plrAlreadyReg); return SUCCESS; }
	 */

	private HttpServletRequest request;
	private HttpSession session;

	public String getFirstName() {
		return firstName;
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

	public String getPlayerType() {
		return playerType;
	}

	public Map getPwtAppMap() {
		return pwtAppMap;
	}

	/**
	 * Player registration process
	 * 
	 * @param idType
	 * @param idNumber
	 * @param lastName
	 * @param firstName
	 * @param connection
	 * @return
	 * @throws LMSException
	 */
	public String plrRegistrationAndApproval(String firstName, String lastName,
			String idNumber, String idType, Connection con) throws LMSException {
		logger.debug("Inside plrRegistrationAndApproval");
		PlayerBean plrBean = null;
		session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		/*
		 * firstName="yogesh"; lastName="bansal"; idNumber="12345";
		 * idType="xyz";
		 */

		// check if player is registered or not
		PlayerVerifyHelperForApp verifyHelper = new PlayerVerifyHelperForApp();
		playerId = verifyHelper.verifyPlayer(firstName, lastName, idNumber,
				idType);

		if (playerId == 0) {// means player is not registered its need to
			// register
			plrBean = new PlayerBean();

			Statement stmt;
			String countryName = "";
			String stateName = "";
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt
						.executeQuery("select  a.name country,b.name state from st_country_master a,st_state_master b ,st_lms_organization_master c where c.organization_id="
								+ userInfoBean.getUserOrgId()
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
			logger.debug("Inside player registration 11111 & plrBean is "
					+ plrBean.toString());
			// logger.debug("Inside player registration 11111 & plrBean is
			// "+plrBean);
		} else {
			logger
					.debug("Player is already registered with plr id "
							+ playerId);
			// logger.debug("Player is already registered with plr id " +
			// playerId);
		}

		// session = request.getSession();
		Map pwtDetails = (Map) session.getAttribute("pwtDetailMap");

		// UserInfoBean userInfoBean = (UserInfoBean)
		// session.getAttribute("USER_INFO");
		// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}

		playerType = "PLAYER";
		// player registration and approval process
		RetailerPwtProcessHelper helper = new RetailerPwtProcessHelper();
		// this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
		// pwtDetails, playerType, playerId, plrBean,
		// (String)session.getAttribute("ROOT_PATH"));

		if (plrBean == null) {
			plrBean = (PlayerBean) session.getAttribute("playerBean");
			session.setAttribute("playerBean", null);
		}

		pwtAppMap.put("plrBean", plrBean);

		session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		return SUCCESS;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public void setPwtAppMap(Map pwtAppMap) {
		this.pwtAppMap = pwtAppMap;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {

	}

}
