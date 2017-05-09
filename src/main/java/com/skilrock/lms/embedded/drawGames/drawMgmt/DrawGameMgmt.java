package com.skilrock.lms.embedded.drawGames.drawMgmt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.DrawScheduleBean;
import com.skilrock.lms.dge.beans.SchedulerBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

public class DrawGameMgmt extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	private static String host = LMSFilterDispatcher.getServDelegateUrl();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		DrawGameMgmt drawGameMgmt = new DrawGameMgmt();
		try {
			drawGameMgmt.setGameNo(104);

			Integer[] drawId = new Integer[1];
			drawId[0] = 2;
			// drawId[1] = 118;
			// drawId[2] = 119;
			// drawId[3] = 120;

			drawGameMgmt.setDrawId(drawId);
			drawGameMgmt.setPostponedMin(20);
			drawGameMgmt.setFreezeTime(120);
			// drawGameMgmt.checkNextDraw();
			// drawGameMgmt.changeFreezeTime();
			// drawGameMgmt.postponeDraw();
			drawGameMgmt.cancelDraw();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String cancelMoneyStatus;
	private String draw_day;
	private Integer[] drawId;
	private int freezeTime;
	private String fromDate;
	private String fromHour;
	private String fromMin;
	private String fromSec;
	private int gameNo;

	private String postpone_single_postponedDate;
	private String postpone_singleFromHour;
	private String postpone_singleFromMin;
	private String postpone_singleFromSec;
	private int postponedMin;
	private HttpServletRequest servletRequest;
	private HttpServletResponse response;

	HttpSession session = null;
	private String toDate;
	private String toHour;
	private String toMin;

	private String toSec;

	private String type;
	
	private String isNewURL;
	private long LSTktNo;
	private String userName;

	public String cancelDraw() throws Exception {
		System.out.println("Before--" + new Date());
		// HttpSession session = servletRequest.getSession();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				System.out.println("len-----" + fromDate.length());
			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		} catch (Exception e) {

		}
		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		List<Integer> drawIdList = new ArrayList<Integer>(Arrays.asList(drawId));
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(gameNo);
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setType(type);
		drawScheduleBean.setStatus(cancelMoneyStatus);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = helper.cancelDraw(drawScheduleBean,userInfoBean);
		System.out.println("msg--" + schedulerBean.getResponseMessage());
		System.out.println("msg--" + schedulerBean.getGameNo());
		System.out.println("msg--"
				+ schedulerBean.getFreezeOrPerform().toString());

		// code to change scheduler if any draw already in scheduler
		if (schedulerBean.getGameNo() != 0) {
			gameNo = schedulerBean.getGameNo();
			try {
				System.out.println(" calling url for scheduler---in cancel");
				URL url = new URL("http://" + host
						+ "/DrawGameWeb/RescheduleJob?gameNo=" + gameNo
						+ "&freezeOrPerform="
						+ schedulerBean.getFreezeOrPerform().toString());
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		System.out.println("After--" + new Date());
		System.out.println(" before successsssssss----- in cancel");
		return SUCCESS;
	}

	public String changeFreezeTime() throws Exception {
		System.out.println("in change freeze---------------");
		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		// HttpSession session = servletRequest.getSession();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		System.out.println("from date---" + fromDate + "--toDate---" + toDate
				+ "----draw id---" + drawId + "----g no--" + gameNo
				+ "   freeze time---" + freezeTime);
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				System.out.println("len-----" + fromDate.length());
			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		} catch (Exception e) {

		}
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		List<Integer> drawIdList = new ArrayList<Integer>(Arrays.asList(drawId));
		System.out
				.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<-inseide change freeze time "
						+ drawIdList);
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(gameNo);
		drawScheduleBean.setFreezeTime(freezeTime);
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setType(type);
		System.out.println("Before--" + new Date());
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = helper.changeFreezeTime(drawScheduleBean,userInfoBean);
		System.out.println("msg--" + schedulerBean.getResponseMessage());
		System.out.println("msg--" + schedulerBean.getGameNo());
		System.out.println("msg--"
				+ schedulerBean.getFreezeOrPerform().toString());
		System.out.println("After--" + new Date());

		// code to change scheduler if any draw already in scheduler
		if (schedulerBean.getGameNo() != 0) {
			gameNo = schedulerBean.getGameNo();
			try {
				System.out
						.println(" calling url for scheduler---in change freeze time");
				URL url = new URL("http://" + host
						+ "/DrawGameWeb/RescheduleJob?gameNo=" + gameNo
						+ "&freezeOrPerform="
						+ schedulerBean.getFreezeOrPerform().toString());
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println(" before---------------- successss");
		return SUCCESS;
	}

	public String checkNextDraw() throws Exception {
		// HttpSession session = servletRequest.getSession();
		System.out.println("Before--" + new Date());
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		List<Integer> drawIdList = new ArrayList<Integer>(Arrays.asList(drawId));
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(gameNo);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		Object message = helper.checkNextDraw(drawScheduleBean);
		System.out.println("msg---------" + message);
		System.out.println("After--" + new Date());
		return SUCCESS;
	}

	public String getCancelMoneyStatus() {
		return cancelMoneyStatus;
	}

	public String getDraw_day() {
		return draw_day;
	}

	public Integer[] getDrawId() {
		return drawId;
	}

	public String getDrawSchdule() throws Exception {
		System.out.println("Before--" + new Date());
		HttpSession session = servletRequest.getSession();
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return null;
		}
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;

		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				System.out.println("len-----" + fromDate.length());
			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		System.out.println("from date---" + fromDate + "--toDate---" + toDate
				+ "----draw id---" + drawId + "----g no--" + gameNo);
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_day(draw_day);
		List<Integer> drawIdList = new ArrayList<Integer>(Arrays.asList(drawId));

		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(gameNo);
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("DRAW_SCH_LIST", helper
				.getDrawSchdule(drawScheduleBean));
		System.out.println("---response----"
				+ helper.getDrawSchdule(drawScheduleBean));
		System.out.println("After--" + new Date());
		return SUCCESS;

	}

	public int getFreezeTime() {
		return freezeTime;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getFromHour() {
		return fromHour;
	}

	public String getFromMin() {
		return fromMin;
	}

	public String getFromSec() {
		return fromSec;
	}

	public int getGameNo() {
		return gameNo;
	}

	public String getPostpone_single_postponedDate() {
		return postpone_single_postponedDate;
	}

	public String getPostpone_singleFromHour() {
		return postpone_singleFromHour;
	}

	public String getPostpone_singleFromMin() {
		return postpone_singleFromMin;
	}

	public String getPostpone_singleFromSec() {
		return postpone_singleFromSec;
	}

	public int getPostponedMin() {
		return postponedMin;
	}

	public String getToDate() {
		return toDate;
	}

	public String getToHour() {
		return toHour;
	}

	public String getToMin() {
		return toMin;
	}

	public String getToSec() {
		return toSec;
	}

	public String getType() {
		return type;
	}

	public String initiateDrawSchedule() throws Exception {
		HttpSession session = servletRequest.getSession();
		System.out.println("Before--" + new Date());
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setGameNo(101);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		helper.initiateDrawSchdule(drawScheduleBean);
		System.out.println("After--" + new Date());
		return SUCCESS;

	}

	public String postponeDraw() throws Exception {
		System.out.println("Before--" + new Date());
		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		// HttpSession session = servletRequest.getSession();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		Date single_frdate = null;
		Timestamp single_postponed_timestamp = null;
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		List<Integer> drawIdList = new ArrayList<Integer>(Arrays.asList(drawId));
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(gameNo);

		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				System.out.println("len-----" + fromDate.length());
			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
			if (postpone_single_postponedDate != null) {
				single_frdate = format.parse(postpone_single_postponedDate);
				fromDrawCal.setTime(single_frdate);
				fromDrawCal.set(Calendar.HOUR, Integer
						.parseInt(postpone_singleFromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer
						.parseInt(postpone_singleFromMin));
				fromDrawCal.set(Calendar.SECOND, Integer
						.parseInt(postpone_singleFromSec));
				single_postponed_timestamp = new Timestamp(fromDrawCal
						.getTime().getTime());
			}
		} catch (Exception e) {

		}
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setType(type);
		drawScheduleBean.setPostponeMin(postponedMin);
		drawScheduleBean.setPostponeDate(single_postponed_timestamp);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = helper.postponeDraw(drawScheduleBean,userInfoBean);

		System.out.println("msg--" + schedulerBean.getResponseMessage());
		System.out.println("msg--" + schedulerBean.getGameNo());
		System.out.println("msg--"
				+ schedulerBean.getFreezeOrPerform().toString());

		// code to change scheduler if any draw already in scheduler
		if (schedulerBean.getGameNo() != 0) {
			gameNo = schedulerBean.getGameNo();
			try {
				System.out.println(" calling url for scheduler---in postpone");
				URL url = new URL("http://" + host
						+ "/DrawGameWeb/RescheduleJob?gameNo=" + gameNo
						+ "&freezeOrPerform="
						+ schedulerBean.getFreezeOrPerform().toString());
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				in.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		System.out.println("After--" + new Date());
		System.out.println(" after success-------------in postpone");
		return SUCCESS;
	}
	/**
	 * This Method Get Winning Results oF Game On For Terminal
	 * @throws Exception
	 */
	
	public void fetchWinningResultDateWise() throws Exception{
		Connection con = DBConnect.getConnection();
		StringBuilder sb = new StringBuilder("");
		try{
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
				return;
			}

			session = (HttpSession) currentUserSessionMap.get(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
				return;
			}

			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			long lastPrintedTicket=0;
			int gameId = 0;
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}

			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);

			DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
			Map<String, LinkedHashMap<String, DrawDetailsBean>> resultMap = helper.fetchWinningResultDateWise(fromDate, toDate);
			if(resultMap.containsKey("RaffleGame1"))
			resultMap.remove("RaffleGame1");
			Iterator iter = resultMap.entrySet().iterator();
			PreparedStatement pstmt=con.prepareStatement("select game_name_dev from st_dg_game_master where is_sale_allowed_through_terminal='Y' and game_status='OPEN'");
			ResultSet rs = pstmt.executeQuery();
			Set <String> gameSet = new HashSet<String>();
			while (rs.next()) {
				gameSet.add(rs.getString("game_name_dev").trim().toLowerCase());
				
			}
			while(iter.hasNext()){
				Map.Entry<String, LinkedHashMap<String, DrawDetailsBean>> pair = (Map.Entry<String, LinkedHashMap<String, DrawDetailsBean>>)iter.next();
				String gameNameDev = pair.getKey();
				// Remove Game Results which are closed on terminal 
               if(!gameSet.contains(gameNameDev.trim().toLowerCase()))
				 continue;
				sb.append("devGName:" + gameNameDev + ",");
				Iterator<DrawDetailsBean> innerIter = pair.getValue().values().iterator();
				while(innerIter.hasNext()){
					DrawDetailsBean drawBean = innerIter.next();
					int drawId = drawBean.getDrawId();
					String drawName = drawBean.getDrawName();
					Timestamp drawDateTime = drawBean.getDrawDateTime();
					String winningResult = drawBean.getWinningResult();
					String machineResult = drawBean.getMachineResult();
					
					if("null".equalsIgnoreCase(winningResult)){
						winningResult = null;
					}
					
					if("null".equalsIgnoreCase(machineResult)){
						machineResult = null;
					}
					
					if(winningResult == null && !"YES".equalsIgnoreCase(isNewURL)){
						continue;
					}
					
					if(winningResult != null || machineResult != null){
						sb.append("DrawTime:");
						
						sb.append(drawDateTime.toString().split("\\.")[0]);
						
						if("YES".equalsIgnoreCase(isNewURL)){
							if(drawName != null && !"null".equalsIgnoreCase(drawName.trim()) && !"".equalsIgnoreCase(drawName.trim())){
								sb.append("*" + drawName);
							}
							sb.append(",");
							if(machineResult != null && !"null".equalsIgnoreCase(machineResult.trim()) && !"".equalsIgnoreCase(machineResult.trim())){
								sb.append("Machine:" + machineResult + ";");
							}
						} else {
							sb.append(",");
						}
						
						if(winningResult != null && !"null".equalsIgnoreCase(winningResult.trim()) && !"".equalsIgnoreCase(winningResult.trim())){
							if("FortuneTwo".equalsIgnoreCase(gameNameDev)){
								String[] winResArr = winningResult.split(",");
								StringBuilder sbResult = new StringBuilder("");
								for (int j = 0; j < winResArr.length; j++) {
									winResArr[j] = winResArr[j].substring(0,3).toUpperCase();
									sbResult.append(winResArr[j] + ",");
								}
								
								if(sbResult.length() > 0){
									sbResult.deleteCharAt(sbResult.length() - 1);
								}
								
								winningResult = sbResult.toString();
							}
							
							sb.append("Result:" + winningResult);
						}
						
						sb.append("#");
					}
				}
				
				sb.deleteCharAt(sb.length() -1);
				sb.append("|");
			}
			
			if(sb.length() == 0){
				sb.append("ErrorMsg:"+EmbeddedErrors.NO_RESULT_AVAILABLE_ERROR_MSG+"ErrorCode:"+EmbeddedErrors.NO_RESULT_AVAILABLE_ERROR_CODE+"|");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		System.out.println("**fetchWinningResultDateWise**"+sb.toString()+"*");
		response.getOutputStream().write(sb.toString().getBytes());
	}

	public void setCancelMoneyStatus(String cancelMoneyStatus) {
		this.cancelMoneyStatus = cancelMoneyStatus;
	}

	public void setDraw_day(String draw_day) {
		this.draw_day = draw_day;
	}

	public void setDrawId(Integer[] drawId) {
		this.drawId = drawId;
	}

	public void setFreezeTime(int freezeTime) {
		this.freezeTime = freezeTime;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}

	public void setFromMin(String fromMin) {
		this.fromMin = fromMin;
	}

	public void setFromSec(String fromSec) {
		this.fromSec = fromSec;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setPostpone_single_postponedDate(
			String postpone_single_postponedDate) {
		this.postpone_single_postponedDate = postpone_single_postponedDate;
	}

	public void setPostpone_singleFromHour(String postpone_singleFromHour) {
		this.postpone_singleFromHour = postpone_singleFromHour;
	}

	public void setPostpone_singleFromMin(String postpone_singleFromMin) {
		this.postpone_singleFromMin = postpone_singleFromMin;
	}

	public void setPostpone_singleFromSec(String postpone_singleFromSec) {
		this.postpone_singleFromSec = postpone_singleFromSec;
	}

	public void setPostponedMin(int postponedMin) {
		this.postponedMin = postponedMin;
	}

	

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setToHour(String toHour) {
		this.toHour = toHour;
	}

	public void setToMin(String toMin) {
		this.toMin = toMin;
	}

	public void setToSec(String toSec) {
		this.toSec = toSec;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}
	
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}
	
	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public String getIsNewURL() {
		return isNewURL;
	}

	public void setIsNewURL(String isNewURL) {
		this.isNewURL = isNewURL;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}