package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.InvTransitionBean;
import com.skilrock.lms.beans.InvTransitionWarehouseWiseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookFlowHelper;

public class BookFlow extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(BookFlow.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private String bookNumber;
	private boolean bookValidity = false;
	private String gameNameNbr;
	private HttpServletResponse response;
	private String status;
	private List<InvTransitionBean> transitionList;
	private InvTransitionWarehouseWiseBean invTransitionWarehouseWiseBean;

	public String getBookNumber() {
		return bookNumber;
	}
	
	public InvTransitionWarehouseWiseBean getInvTransitionWarehouseWiseBean() {
		return invTransitionWarehouseWiseBean;
	}

	public void setInvTransitionWarehouseWiseBean(
			InvTransitionWarehouseWiseBean invTransitionWarehouseWiseBean) {
		this.invTransitionWarehouseWiseBean = invTransitionWarehouseWiseBean;
	}

	private ArrayList getCharacters() throws Exception {
		System.out.println("inside get characters ");
		 
		ArrayList arrlist = new ArrayList();
		Connection con;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		con = DBConnect.getConnection();
		stmt = con.createStatement();
		stmt2 = con.createStatement();

		rs = stmt
				.executeQuery("select game_name,game_nbr from st_se_game_master where game_status='"
						+ status + "'");
		System.out
				.println("query is "
						+ "select game_name,game_nbr from st_se_game_master where game_status='"
						+ status + "'");
		while (rs.next()) {

			System.out.println("hhhhhhhhhhhhhh");
			arrlist.add(rs.getString("game_nbr") + "-"
					+ rs.getString("game_name"));
			System.out.println("helllo n");
		}
		if (con != null) {
			con.close();
		}
		return arrlist;

	}

	public String getFlow() throws LMSException {
		int gameNbr = 0;
		StringTokenizer stringtoken = new StringTokenizer(getGameNameNbr(), "-");
		while (stringtoken.hasMoreTokens()) {
			gameNbr = Integer.parseInt(stringtoken.nextToken());
			break;
		}
		System.out.println("game number is  " + gameNbr);
		int gameId = getGameIdfromgameNbr(gameNbr);
		System.out.println("game id is " + gameId);

		BookFlowHelper bookHelper = new BookFlowHelper();
		Map bookFlowDetailMap = bookHelper.getBookFlow(gameId, getBookNumber());

		bookValidity = (Boolean) bookFlowDetailMap.get("bookValidity");
		transitionList = (List<InvTransitionBean>) bookFlowDetailMap
				.get("transitionList");
		return SUCCESS;
		/*
		 *   Connection connection =
		 * DBConnect.getConnection(); PreparedStatement statement = null; ResultSet rs =
		 * null;
		 * 
		 * List<String> currOwnerList = new ArrayList<String>(); List<String>
		 * currOwnerNameList = new ArrayList<String>(); List<String>
		 * txTimeList = new ArrayList<String>();
		 * 
		 * String currOwner = null; String nextOwner = null; Timestamp txTime =
		 * null;
		 * 
		 * String currOwnerName = null; String nextOwnerName = null;
		 * 
		 * InvTransitionBean invTransitionBean = null; transitionList = new
		 * ArrayList<InvTransitionBean>(); SimpleDateFormat sdt = new
		 * SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		 * 
		 * String time = null; String bookNbr=getBookNumber();
		 * GameTicketFormatBean ticketformatBean=getGameTicketinfo(gameId);
		 * if(getBookNumber().indexOf("-") == -1 && getBookNumber().length() >=
		 * (ticketformatBean.getGameNbrDigits()+ticketformatBean.getBookDigits()+ticketformatBean.getPackDigits())){
		 * 
		 * bookNbr = bookNbr.substring(0,ticketformatBean.getGameNbrDigits()) +
		 * "-" + bookNbr.substring(ticketformatBean.getGameNbrDigits());
		 * 
		 * System.out.println("New book nbr:::" + bookNbr); }
		 * 
		 * try {
		 * 
		 * 
		 * statement = connection.prepareStatement("select
		 * a.current_owner,a.current_owner_id,a.transaction_date,b.name from
		 * st_se_game_inv_detail a, st_lms_organization_master b where game_id =
		 * "+gameId+" and book_nbr = '"+bookNbr+"' and a.current_owner_id =
		 * b.organization_id order by transaction_date");
		 * System.out.println("query :" + "select
		 * a.current_owner,a.current_owner_id,a.transaction_date,b.name from
		 * st_se_game_inv_detail a, st_lms_organization_master b where game_id =
		 * "+gameId+" and book_nbr = '"+bookNbr+"' and a.current_owner_id =
		 * b.organization_id order by transaction_date");
		 * 
		 * rs = statement.executeQuery();
		 * 
		 * while(rs.next()){
		 * 
		 * 
		 * currOwnerList.add(rs.getString("current_owner"));
		 * currOwnerNameList.add(rs.getString("name"));
		 * 
		 * txTime = rs.getTimestamp("transaction_date");
		 * txTimeList.add(sdt.format(txTime)); bookValidity=true; }
		 * System.out.println("book validity flag is " + bookValidity);
		 * 
		 * if(currOwnerList != null && currOwnerList.size() > 1){
		 * 
		 * for(int i=0 ; i<currOwnerList.size() -1 ; i++){
		 * 
		 * currOwner = currOwnerList.get(i); nextOwner = currOwnerList.get(i+1);
		 * time = txTimeList.get(i+1);
		 * 
		 * currOwnerName = currOwnerNameList.get(i); nextOwnerName =
		 * currOwnerNameList.get(i+1);
		 * 
		 * invTransitionBean = new
		 * InvTransitionBean(currOwnerName,nextOwnerName,currOwner,nextOwner,time);
		 * transitionList.add(invTransitionBean);
		 * 
		 * if(currOwner.equals("BO")){ invTransitionBean.setBOToAgent(true); }
		 * else if(currOwner.equals("AGENT")){
		 * 
		 * if(nextOwner.equals("RETAILER")){
		 * invTransitionBean.setAgentToRetailer(true); } else {
		 * invTransitionBean.setAgentToBO(true); } } else
		 * if(currOwner.equals("RETAILER")){
		 * invTransitionBean.setRetailerToAgent(true); } }
		 * 
		 * //this.transitionList = transitionList; }
		 * 
		 * System.out.println(); for(InvTransitionBean i : transitionList)
		 * System.out.println(i);
		 * 
		 */

	}

	public String getFlowNew() throws LMSException {
		int gameNbr = 0;
		HttpSession session = request.getSession();
		int roleId=((UserInfoBean)session.getAttribute("USER_INFO")).getRoleId();
		StringTokenizer stringtoken = new StringTokenizer(getGameNameNbr(), "-");
		while (stringtoken.hasMoreTokens()) {
			gameNbr = Integer.parseInt(stringtoken.nextToken());
			break;
		}
		System.out.println("game number is  " + gameNbr);
		int gameId = getGameIdfromgameNbr(gameNbr);
		System.out.println("game id is " + gameId);

		BookFlowHelper bookHelper = new BookFlowHelper();
		
		//Tracking book for perticular user
		if(bookHelper.isValidUserForTrackingBook(getBookNumber(), roleId)){
			invTransitionWarehouseWiseBean = bookHelper.getBookFlowNew(gameId, getBookNumber());	
		}else{
			invTransitionWarehouseWiseBean = new InvTransitionWarehouseWiseBean();
			invTransitionWarehouseWiseBean.setBookLocation(0);
		}
		return SUCCESS;
	}

	/*
	 * public String getFlow() throws LMSException{ int gameNbr=0;
	 * StringTokenizer stringtoken=new StringTokenizer(getGameNameNbr(),"-");
	 * while(stringtoken.hasMoreTokens()) {
	 * gameNbr=Integer.parseInt(stringtoken.nextToken()); break; }
	 * System.out.println("game number is " + gameNbr); int
	 * gameId=getGameIdfromgameNbr(gameNbr); System.out.println("game id is " +
	 * gameId);   Connection connection =
	 * DBConnect.getConnection(); PreparedStatement statement = null; ResultSet rs =
	 * null;
	 * 
	 * List<String> currOwnerList = new ArrayList<String>(); List<String>
	 * currOwnerNameList = new ArrayList<String>(); List<String> txTimeList =
	 * new ArrayList<String>();
	 * 
	 * String currOwner = null; String nextOwner = null; Timestamp txTime =
	 * null;
	 * 
	 * String currOwnerName = null; String nextOwnerName = null;
	 * 
	 * InvTransitionBean invTransitionBean = null; transitionList = new
	 * ArrayList<InvTransitionBean>(); SimpleDateFormat sdt = new
	 * SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	 * 
	 * String time = null; String bookNbr=getBookNumber(); GameTicketFormatBean
	 * ticketformatBean=getGameTicketinfo(gameId);
	 * if(getBookNumber().indexOf("-") == -1 && getBookNumber().length() >=
	 * (ticketformatBean.getGameNbrDigits()+ticketformatBean.getBookDigits()+ticketformatBean.getPackDigits())){
	 * 
	 * bookNbr = bookNbr.substring(0,ticketformatBean.getGameNbrDigits()) + "-" +
	 * bookNbr.substring(ticketformatBean.getGameNbrDigits());
	 * 
	 * System.out.println("New book nbr:::" + bookNbr); }
	 * 
	 * try {
	 * 
	 * 
	 * statement = connection.prepareStatement("select
	 * a.current_owner,a.current_owner_id,a.transaction_date,b.name from
	 * st_se_game_inv_detail a, st_lms_organization_master b where game_id =
	 * "+gameId+" and book_nbr = '"+bookNbr+"' and a.current_owner_id =
	 * b.organization_id order by transaction_date"); System.out.println("query :" +
	 * "select a.current_owner,a.current_owner_id,a.transaction_date,b.name from
	 * st_se_game_inv_detail a, st_lms_organization_master b where game_id =
	 * "+gameId+" and book_nbr = '"+bookNbr+"' and a.current_owner_id =
	 * b.organization_id order by transaction_date");
	 * 
	 * rs = statement.executeQuery();
	 * 
	 * while(rs.next()){
	 * 
	 * 
	 * currOwnerList.add(rs.getString("current_owner"));
	 * currOwnerNameList.add(rs.getString("name"));
	 * 
	 * txTime = rs.getTimestamp("transaction_date");
	 * txTimeList.add(sdt.format(txTime)); bookValidity=true; }
	 * System.out.println("book validity flag is " + bookValidity);
	 * 
	 * if(currOwnerList != null && currOwnerList.size() > 1){
	 * 
	 * for(int i=0 ; i<currOwnerList.size() -1 ; i++){
	 * 
	 * currOwner = currOwnerList.get(i); nextOwner = currOwnerList.get(i+1);
	 * time = txTimeList.get(i+1);
	 * 
	 * currOwnerName = currOwnerNameList.get(i); nextOwnerName =
	 * currOwnerNameList.get(i+1);
	 * 
	 * invTransitionBean = new
	 * InvTransitionBean(currOwnerName,nextOwnerName,currOwner,nextOwner,time);
	 * transitionList.add(invTransitionBean);
	 * 
	 * if(currOwner.equals("BO")){ invTransitionBean.setBOToAgent(true); } else
	 * if(currOwner.equals("AGENT")){
	 * 
	 * if(nextOwner.equals("RETAILER")){
	 * invTransitionBean.setAgentToRetailer(true); } else {
	 * invTransitionBean.setAgentToBO(true); } } else
	 * if(currOwner.equals("RETAILER")){
	 * invTransitionBean.setRetailerToAgent(true); } }
	 * 
	 * //this.transitionList = transitionList; }
	 * 
	 * System.out.println(); for(InvTransitionBean i : transitionList)
	 * System.out.println(i); } catch (SQLException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } finally{ try{ if(connection!=null)
	 * connection.close(); }catch(SQLException se){ se.printStackTrace(); } }
	 * 
	 * return SUCCESS; }
	 */

	public int getGameIdfromgameNbr(int gameNbr) throws LMSException {
		int gameId = 0;
		 
		Connection connection = DBConnect.getConnection();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt
					.executeQuery("select game_id from st_se_game_master where game_nbr="
							+ gameNbr + "");
			while (rs.next()) {
				gameId = rs.getInt("game_id");
				System.out.println("gfhhhhhhhh " + gameId);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return gameId;

	}

	/*
	 * public String getFlow() throws LMSException{ int gameNbr=0;
	 * StringTokenizer stringtoken=new StringTokenizer(getGameNameNbr(),"-");
	 * while(stringtoken.hasMoreTokens()) {
	 * gameNbr=Integer.parseInt(stringtoken.nextToken()); break; }
	 * System.out.println("game number is " + gameNbr); int
	 * gameId=getGameIdfromgameNbr(gameNbr); System.out.println("game id is " +
	 * gameId);   Connection connection =
	 * DBConnect.getConnection(); PreparedStatement statement = null; ResultSet rs =
	 * null;
	 * 
	 * List<String> currOwnerList = new ArrayList<String>(); List<Integer>
	 * currOwnerIdList = new ArrayList<Integer>(); List<Timestamp> txTimeList =
	 * new ArrayList<Timestamp>();
	 * 
	 * String currOwner = null; String nextOwner = null; Timestamp txTime =
	 * null;
	 * 
	 * int currOwnerId = -1; int nextOwnerId = -1;
	 * 
	 * InvTransitionBean invTransitionBean = null; transitionList = new
	 * ArrayList<InvTransitionBean>();
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * statement = connection.prepareStatement("select
	 * a.current_owner,a.current_owner_id,a.transaction_date,b.name from
	 * st_se_game_inv_detail a, st_lms_organization_master b where game_id =
	 * "+gameId+" and book_nbr = '"+getBookNumber()+"' and a.current_owner_id =
	 * b.organization_id order by transaction_id"); System.out.println("query :" +
	 * "select a.current_owner,a.current_owner_id,a.transaction_date,b.name from
	 * st_se_game_inv_detail a, st_lms_organization_master b where game_id =
	 * "+gameId+" and book_nbr = '"+getBookNumber()+"' and a.current_owner_id =
	 * b.organization_id order by transaction_id"); //statement.setInt(1,53);
	 * //statement.setString(2,"1111-001001"); rs = statement.executeQuery();
	 * 
	 * while(rs.next()){
	 * 
	 * currOwnerList.add(rs.getString("current_owner"));
	 * currOwnerIdList.add(rs.getInt("current_owner_id"));
	 * txTimeList.add(rs.getTimestamp("transaction_date")); }
	 * 
	 * 
	 * if(currOwnerList != null && currOwnerList.size() > 1){
	 * 
	 * for(int i=0 ; i<currOwnerList.size() -1 ; i++){
	 * 
	 * currOwner = currOwnerList.get(i); nextOwner = currOwnerList.get(i+1);
	 * txTime = txTimeList.get(i+1);
	 * 
	 * currOwnerId = currOwnerIdList.get(i); nextOwnerId =
	 * currOwnerIdList.get(i+1);
	 * 
	 * invTransitionBean = new
	 * InvTransitionBean(currOwnerId,nextOwnerId,currOwner,nextOwner,txTime);
	 * transitionList.add(invTransitionBean);
	 * 
	 * if(currOwner.equals("BO")){ invTransitionBean.setBOToAgent(true); } else
	 * if(currOwner.equals("AGENT")){
	 * 
	 * if(nextOwner.equals("RETAILER")){
	 * invTransitionBean.setAgentToRetailer(true); } else {
	 * invTransitionBean.setAgentToBO(true); } } else
	 * if(currOwner.equals("RETAILER")){
	 * invTransitionBean.setRetailerToAgent(true); } } }
	 * 
	 * System.out.println(); for(InvTransitionBean i : transitionList)
	 * System.out.println(i); } catch (SQLException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * 
	 * return SUCCESS; }
	 */

	public String getGameNameNbr() {
		return gameNameNbr;
	}

	public String getGames() throws Exception {
		PrintWriter out = getResponse().getWriter();
		String gameStatus = getStatus();
		System.out.println("game status is " + gameStatus);
		if (gameStatus == null) {
			gameStatus = "";
		}
		ArrayList characters = getCharacters();
		String html = "";
		html = "<select class=\"option\" name=\"gameNameNbr\" id=\"gameNameId\" onclick=\"clearDiv(this.id)\"><option value=-1>--PleaseSelect--</option>";
		int i = 0;
		for (Iterator it = characters.iterator(); it.hasNext();) {
			String name = (String) it.next();
			i++;
			html += "<option class=\"option\" value=\"" + name + "\">" + name
					+ "</option>";
		}
		html += "</select>";
		response.setContentType("text/html");
		out.print(html);
		return null;
	}

	public GameTicketFormatBean getGameTicketinfo(int gameId)
			throws LMSException {
		GameTicketFormatBean ticketformatBean = null;
		 
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();

			String ticketinfoQuery = QueryManager.getGameFormatInformation()
					+ gameId + ")";
			System.out.println("query for ticket format :: " + ticketinfoQuery);
			ResultSet rs = stmt.executeQuery(ticketinfoQuery);
			while (rs.next()) {
				ticketformatBean = new GameTicketFormatBean();
				ticketformatBean.setBookDigits(rs.getInt("book_nbr_digits"));
				ticketformatBean.setGameNbrDigits(rs.getInt("game_nbr_digits"));
				ticketformatBean.setPackDigits(rs.getInt("pack_nbr_digits"));
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During closing connection", se);
			}
		}
		return ticketformatBean;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStatus() {
		return status;
	}

	public List<InvTransitionBean> getTransitionList() {
		return transitionList;
	}

	public boolean isBookValidity() {
		return bookValidity;
	}

	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}

	public void setBookValidity(boolean bookValidity) {
		this.bookValidity = bookValidity;
	}

	public void setGameNameNbr(String gameNameNbr) {
		this.gameNameNbr = gameNameNbr;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTransitionList(List<InvTransitionBean> transitionList) {
		this.transitionList = transitionList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
