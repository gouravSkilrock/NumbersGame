package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.ResponsibleGamingBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.drawGames.common.Util;

public class ResponsibleGaming {
    static Log logger=LogFactory.getLog(ResponsibleGaming.class);
	public static Set<String> chkRGCriteOnLogIn(int userId, int orgId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		boolean isExecuteble = false;
		Set<String> actionSet = new HashSet<String>();
		try {
			con.setAutoCommit(false);
			selectQuery = "select criteria,criteria_limit,crit_action,criteria_type from st_lms_rg_criteria_limit where crit_status='ACTIVE' and organization_type='RETAILER' and criteria_type='DAILY' and service_code in (select service_code from st_lms_service_master where status='ACTIVE')";
			pstmt = con.prepareStatement(selectQuery);
			logger.info("Criteria Limit Query ::" + pstmt);
			rs = pstmt.executeQuery();
			StringBuilder dailyQuery = new StringBuilder("select concat(");
			String actionStr = null, actionQuery = null;

			while (rs.next()) {
				isExecuteble = true;
				dailyQuery.append("\"'\",if(" + rs.getString("criteria") + "<"
						+ rs.getString("criteria_limit") + ",'','"
						+ rs.getString("criteria") + "'),\"'\",\",\",");
			}
			if (isExecuteble) {
				dailyQuery.delete(dailyQuery.length() - 5, dailyQuery.length());
				dailyQuery
						.append(") from st_lms_rg_org_daily_tx where user_id=? and organization_id=?");
				// logger.info(dailyQuery);
				pstmt = con.prepareStatement(dailyQuery.toString());
				pstmt.setInt(1, userId);
				pstmt.setInt(2, orgId);
				logger.info("*******" + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					actionStr = rs.getString(1);
				}

				actionQuery = "select distinct(crit_action) from st_lms_rg_criteria_limit where crit_status='ACTIVE' and organization_type='RETAILER' and criteria_type='DAILY' and criteria in ("
						+ actionStr + ")";
				pstmt = con.prepareStatement(actionQuery);
				logger.info("*******" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					actionSet.add(rs.getString("crit_action"));
				}
			}
			isExecuteble = false;
			selectQuery = "select criteria,criteria_limit,crit_action,criteria_type from st_lms_rg_criteria_limit where crit_status='ACTIVE' and organization_type='RETAILER' and criteria_type='WEEKLY' and service_code in (select service_code from st_lms_service_master where status='ACTIVE')";
			pstmt = con.prepareStatement(selectQuery);
			logger.info("Criteria Limit Query ::" + pstmt);
			rs = pstmt.executeQuery();
			dailyQuery = new StringBuilder("select concat(");
			actionStr = null;
			actionQuery = null;
			while (rs.next()) {
				isExecuteble = true;
				dailyQuery.append("\"'\",if(" + rs.getString("criteria") + "<"
						+ rs.getString("criteria_limit") + ",'','"
						+ rs.getString("criteria") + "'),\"'\",\",\",");
			}
			if (isExecuteble) {
				dailyQuery.delete(dailyQuery.length() - 5, dailyQuery.length());
				dailyQuery
						.append(") from st_lms_rg_org_weekly_tx where user_id=? and organization_id=?");
				// logger.info(dailyQuery);
				pstmt = con.prepareStatement(dailyQuery.toString());
				pstmt.setInt(1, userId);
				pstmt.setInt(2, orgId);
				logger.info("*******" + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					actionStr = rs.getString(1);
				}

				actionQuery = "select distinct(crit_action) from st_lms_rg_criteria_limit where crit_status='ACTIVE' and organization_type='RETAILER' and criteria_type='WEEKLY' and criteria in ("
						+ actionStr + ")";
				pstmt = con.prepareStatement(actionQuery);
				logger.info("*******" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					actionSet.add(rs.getString("crit_action"));
				}
			}
			logger.info(actionSet);
			/*
			 * for (String action : actionSet) { actionOnFraudDetection(action,
			 * userId, orgId); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return actionSet;
	}

	public static String chkRGCriteOnLogIn(UserInfoBean userBean) {
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(userBean
				.getUserName());
		Set<String> actionSet = chkRGCriteOnLogIn(userBean.getUserId(),
				userBean.getUserOrgId());
		if (actionSet.contains("INACTIVE_USER")) {
			currentUserSessionMap.remove(userBean.getUserName());
			return "Your Responsible Gaming Limits are exceed!!";
		}

	/*	List actionList = (List) session.getAttribute("ACTION_LIST");
		logger.info("************Action_List**" + actionList);
		logger.info("************actionSet**" + actionSet);
		for (String str : actionSet) {
			actionList = removeAction(actionList, str);
		}
		logger.info("After RG Check ACTION_LIST" + actionList);
		session.setAttribute("ACTION_LIST", actionList);*/
		return "SUCCESS";
	}

	public static void insertDailyHistory() throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String insertQuery = null, updateQuery = null;
		try {
			boolean isSLE = ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE()));
			boolean isIW = ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW()));
			boolean isVS = ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS()));

			String sleString  = isSLE?", sle_sale_amt,sle_pwt_amt,sle_reprint_limit,sle_cancel_limit,sle_invalid_pwt_limit ":"";
			String sleUpdateString  = isSLE?", sle_sale_amt = 0.0 ,sle_pwt_amt = 0.0 ,sle_reprint_limit = 0 ,sle_cancel_limit = 0,sle_invalid_pwt_limit = 0 ":"";
			
			String iwString  = isIW?", iw_sale_amt, iw_pwt_amt, iw_reprint_limit, iw_invalid_pwt_limit ":"";
			String iwUpdateString  = isIW?", iw_sale_amt = 0.0, iw_pwt_amt = 0.0, iw_reprint_limit = 0, iw_invalid_pwt_limit = 0 ":"";
			
			String vsString  = isVS?", vs_sale_amt, vs_pwt_amt, vs_reprint_limit, vs_invalid_pwt_limit ":"";
			String vsUpdateString  = isVS?", vs_sale_amt = 0.0, vs_pwt_amt = 0.0, vs_reprint_limit = 0, vs_invalid_pwt_limit = 0 ":"";

			con.setAutoCommit(false);
			insertQuery = "insert into st_lms_rg_org_daily_tx_history (organization_id, user_id,"
					+ " date, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, dg_invalid_pwt_limit,"
					+ " se_sale_amt, se_pwt_amt, se_invalid_pwt_limit "+sleString+"  " +iwString+"  " +vsString+") select organization_id, user_id"
					+ ",CURRENT_TIMESTAMP,dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, "
					+ "dg_invalid_pwt_limit, se_sale_amt, se_pwt_amt, se_invalid_pwt_limit "+sleString+ " " +iwString+ " " +vsString+ " from st_lms_rg_org_daily_tx";
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();

			updateQuery = "update st_lms_rg_org_daily_tx set dg_sale_amt=0.0,dg_pwt_amt=0.0,dg_reprint_limit=0,dg_cancel_limit=0,dg_invalid_pwt_limit=0,se_sale_amt=0.0,se_pwt_amt=0.0,se_invalid_pwt_limit=0.0 "+ sleUpdateString + " " + iwUpdateString + " " + vsUpdateString;
			pstmt = con.prepareStatement(updateQuery);
			pstmt.executeUpdate();

			con.commit();
		} /*catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
		 catch (SQLException e) {
				logger.info("SQL Exception ",e);
				throw new LMSException("SQL Exception "+e.getMessage());
		 }catch (Exception e) {
				logger.info("Exception ",e);
				throw new LMSException("Exception"+e.getMessage());
			} finally {
				DBConnect.closeCon(con);
		}
	}

	public static void insertWeeklyHistory() throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String insertQuery = null, updateQuery = null;
		try {
			boolean isSLE = ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE()));
			boolean isIW = ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW()));
			boolean isVS = ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS()));

			String sleString  = isSLE?", sle_sale_amt,sle_pwt_amt,sle_reprint_limit,sle_cancel_limit,sle_invalid_pwt_limit ":"";
			String sleUpdateString  = isSLE?", sle_sale_amt = 0.0 ,sle_pwt_amt = 0.0 ,sle_reprint_limit = 0 ,sle_cancel_limit = 0,sle_invalid_pwt_limit = 0 ":"";

			String iwString  = isIW?", iw_sale_amt, iw_pwt_amt, iw_reprint_limit, iw_invalid_pwt_limit ":"";
			String iwUpdateString  = isIW?", iw_sale_amt = 0.0, iw_pwt_amt = 0.0, iw_reprint_limit = 0, iw_invalid_pwt_limit = 0 ":"";

			String vsString  = isVS?", vs_sale_amt, vs_pwt_amt, vs_reprint_limit, vs_invalid_pwt_limit ":"";
			String vsUpdateString  = isVS?", vs_sale_amt = 0.0, vs_pwt_amt = 0.0, vs_reprint_limit = 0, vs_invalid_pwt_limit = 0 ":"";

			con.setAutoCommit(false);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(new Date().getTime());
			cal.add(Calendar.DAY_OF_MONTH, -7);
			logger.info("*********7 day before date "
					+ new Timestamp(cal.getTimeInMillis()));
			insertQuery = "insert into st_lms_rg_org_weakly_tx_history (organization_id, user_id, startdate, "
					+ "enddate, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, dg_invalid_pwt_limit,"
					+ " se_sale_amt, se_pwt_amt, se_invalid_pwt_limit " + sleString + iwString + vsString + " )select organization_id, user_id,"
					+ " startdate, CURRENT_TIMESTAMP, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, "
					+ "dg_invalid_pwt_limit, se_sale_amt, se_pwt_amt, se_invalid_pwt_limit " + sleString + iwString + vsString + " from "
					+ "st_lms_rg_org_weekly_tx";
			pstmt = con.prepareStatement(insertQuery);
			pstmt.executeUpdate();

			updateQuery = "update st_lms_rg_org_weekly_tx set startdate=CURRENT_TIMESTAMP,dg_sale_amt=0.0,dg_pwt_amt=0.0,dg_reprint_limit=0,dg_cancel_limit=0,dg_invalid_pwt_limit=0,se_sale_amt=0.0,se_pwt_amt=0.0,se_invalid_pwt_limit=0.0 " +sleUpdateString + iwUpdateString + vsUpdateString;
			pstmt = con.prepareStatement(updateQuery);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public static void main(String[] args) {
		ResponsibleGaming rg = new ResponsibleGaming();
		List list = new ArrayList();
		list.addAll(Arrays.asList("ret_rep_sale_Navigate",
				"ret_rep_cashChq_Menu", "ret_rep_cashChq_Search",
				"ret_rep_ledger_Menu", "ret_rep_ledger_Generate",
				"ret_um_addSubUser_Menu", "ret_um_editSubUserPriv_Menu",
				"rep_common_openPDF", "bo_rep_showReceipt",
				"bo_rep_showDeliveryChallen", "agt_rep_showDeliveryChallen",
				"generateDeliveryChallanToPDF", "ret_rep_sale_Menu",
				"ret_rep_sale_Search", "ret_im_bookWiseInvDet_Menu",
				"ret_im_bookWiseInvDetAjx_Detail", "ret_rep_pwt_Menu",
				"ret_rep_pwt_Search", "ret_om_quickOrder_Menu",
				"ret_om_quickOrder_Save", "ret_im_soldTktEntry_Menu",
				"ret_im_soldTktEntry_save",
				"ret_im_soldTktEntry_fetchBooksAjax", "verifyTicket",
				"fortuneBuy", "fortuneReprintTicket", "reprintTicket",
				"fortuneInfo", "card12Buy", "card12ReprintTicket",
				"card12Info", "jreVersion", "card16Buy", "card16ReprintTicket",
				"card16Info", "getServerTime", "lottoBuy",
				"lottoReprintTicket", "lottoInfo", "kenoBuy",
				"kenoReprintTicket", "kenoInfo", "fastLottoBuy",
				"fastlottoReprintTicket", "fastLottoInfo", "zeroToNineBuy",
				"zimLottoBuy", "zimlottoReprintTicket", "zimLottoInfo",
				"zimLottotwoBuy", "zimlottotwoReprintTicket",
				"zimLottotwoInfo", "cancelTicket", "rpos", "updatedData",
				"registerPlayer", "ret_dg_rep_pwt_Search",
				"ret_dg_rep_sale_Menu", "ret_dg_rep_sale_Search",
				"ret_dg_rep_sale_Navigate", "ret_dg_rep_pwt_Menu"));
		// logger.info(list);
		rg.removeAction(list, "SALE_HOLD");
		// logger.info(list);
	}

	public static List removeAction(List actionList, String action) {
		String actType = "NO_ACTION";
		if ("INACTIVE_USER".equals(action)) {
			actionList = new ArrayList();
			return actionList;
		} else if ("PWT_HOLD".equals(action)) {
			actType = "verifyTicket";
		} else if ("SALE_HOLD".equals(action)) {
			actType = "Buy";
		} else if ("REPRINT_HOLD".equals(action)) {
			actType = "reprintTicket";
		} else if ("CANCEL_HOLD".equals(action)) {
			actType = "cancelTicket";
		}
		for (int i = 0; i < actionList.size(); i++) {
			String str = (String) actionList.get(i);
			logger.info("*******str" + str);
			if (str.contains(actType)) {
				logger.info("*******strMatch" + str);
				actionList.remove(str);
				logger.info(str);
				i--;
			}
		}
		return actionList;
	}

	public static boolean respGaming(UserInfoBean userBean, String criTpye,
			String limit) {
		Connection con = DBConnect.getConnection();
		boolean result = false;
		try {
			con.setAutoCommit(false);
			result = respGaming(userBean, criTpye, limit, con);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static boolean respGaming(UserInfoBean userBean, String criTpye,
			String limit, Connection con) {
		int orgId = userBean.getUserOrgId();
		int userId = userBean.getUserId();
		//logger.info("RG Counter for Criteria::" + criTpye+ " and limit::" + limit);
		List<String> criTpyeList = Arrays.asList("DG_SALE", "DG_PWT",
				"DG_REPRINT", "DG_CANCEL", "DG_PWT_INVALID", "SE_PWT",
				"SE_PWT_INVALID", "DG_OFFLINE_LATEUPLOAD",
				"DG_OFFLINE_ERRORFILE", "CS_REPRINT_LIMIT","DG_CANCEL_AMOUNT","DG_PWT_COUNT","AGENT_DG_PWT",
				"SLE_SALE","SLE_CANCEL","SLE_CANCEL_AMOUNT","SLE_REPRINT", "SLE_PWT", "SLE_INVALID_PWT", "IW_SALE", "IW_PWT", "IW_INVALID_PWT", "IW_REPRINT_LIMIT","VS_SALE","VS_PWT");
		ResponsibleGaming rg = new ResponsibleGaming();
		boolean isFraud = false;
		switch (criTpyeList.indexOf(criTpye)) {
		case 0: // DG_SALE
			isFraud = rg.rgCounter(orgId, userId, "dg_sale_amt", Double
					.parseDouble(limit),con);
			break;
		case 1: // DG_PWT
			isFraud = rg.rgCounter(orgId, userId, "dg_pwt_amt", Double
					.parseDouble(limit));
			break;
		case 2: // DG_REPRINT
			isFraud = rg.rgCounter(orgId, userId, "dg_reprint_limit", Integer
					.parseInt(limit), con);
			break;
		case 3: // DG_CANCEL
			isFraud = rg.rgCounter(orgId, userId, "dg_cancel_limit", Integer
					.parseInt(limit), con);
			break;
		case 4: // DG_PWT_INVALID
			isFraud = rg.rgCounter(orgId, userId, "dg_invalid_pwt_limit",
					Integer.parseInt(limit), con);
			break;
		case 5:
			isFraud = rg.rgCounter(orgId, userId, "se_pwt_amt", Double
					.parseDouble(limit));
			break;
		case 6:
			isFraud = rg.rgCounter(orgId, userId, "se_invalid_pwt_limit",
					Integer.parseInt(limit), con);
			break;
		case 7:
			isFraud = rg.rgCounter(orgId, userId, "dg_offline_lateupload",
					Integer.parseInt(limit), con);
			break;
		case 8:
			isFraud = rg.rgCounter(orgId, userId, "dg_offline_errorfile",
					Integer.parseInt(limit), con);
			break;
		case 9:
			isFraud = rg.rgCounter(orgId, userId, "cs_reprint_limit", Integer
					.parseInt(limit), con);
			break;
			//subtract cancel amount from sale
		case 10:
			isFraud = rg.rgCounterCancel(orgId, userId, "dg_sale_amt",  Double
					.parseDouble(limit), con);
			break;
		case 11:
			isFraud = rg.rgCounterCheck(orgId, userId, "dg_invalid_pwt_limit",
					Integer.parseInt(limit), con);
			break;
		case 12:
			isFraud = rg.rgCounterCheck(orgId, userId,"dg_pwt_amt", Double.parseDouble(limit), con,0);
			break;
		case 13:
			isFraud = rg.rgCounter(orgId, userId,"sle_sale_amt", Double.parseDouble(limit), con);
			break;
		case 14:
			isFraud = rg.rgCounter(orgId, userId,"sle_cancel_limit", Integer.parseInt(limit), con);
			break;
		case 15:
			isFraud = rg.rgCounterCancel(orgId, userId, "sle_sale_amt",  Double.parseDouble(limit), con);
			break;
		case 16:
			isFraud = rg.rgCounter(orgId, userId, "sle_reprint_limit",  Integer.parseInt(limit), con);
			break;
		case 17:
			isFraud = rg.rgCounter(orgId, userId, "sle_pwt_amt",  Double.parseDouble(limit), con);
			break;
		case 18:
			isFraud = rg.rgCounter(orgId, userId, "sle_invalid_pwt_limit",  Integer.parseInt(limit), con);
			break;
		case 19:
			isFraud = rg.rgCounter(orgId, userId, "iw_sale_amt", Double.parseDouble(limit), con);
			break;
		case 20:
			isFraud = rg.rgCounter(orgId, userId, "iw_pwt_amt", Double.parseDouble(limit), con);
			break;
		case 21:
			isFraud = rg.rgCounter(orgId, userId, "iw_invalid_pwt_limit", Integer.parseInt(limit), con);
			break;
		/*case 22:
			isFraud = rg.rgCounter(orgId, userId, "iw_cancel_limit",  Integer.parseInt(limit), con);
			break;*/
		case 22:
			isFraud = rg.rgCounter(orgId, userId, "iw_reprint_limit",  Integer.parseInt(limit), con);
			break;
		case 23:
			isFraud = rg.rgCounter(orgId, userId, "vs_sale_amt", Double.parseDouble(limit), con);
			break;	
		case 24:
			isFraud = rg.rgCounter(orgId, userId, "vs_pwt_amt", Double.parseDouble(limit), con);
			break;
		default:
			break;
		}
		/*if (isFraud) {
			rg.logOutUser(userBean);
		}*/
		return isFraud;
	}

	@Deprecated
	public static boolean rgInsertion(int userId, int OrgId) {

		logger.info("RG Insert for UserID::" + userId + "  and OrgId::"
				+ OrgId);
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String updateQuery = null;
		boolean chkDone = false;
		try {
			con.setAutoCommit(false);

			// for daily table
			updateQuery = "insert into st_lms_rg_org_daily_tx (organization_id,user_id) values(?,?)";
			pstmt = con.prepareStatement(updateQuery);
			pstmt.setInt(1, OrgId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
			pstmt.close();
			// for weekly table
			updateQuery = "insert into st_lms_rg_org_weekly_tx (organization_id,user_id,startdate) values (?,?,?)";
			pstmt = con.prepareStatement(updateQuery);
			pstmt.setInt(1, OrgId);
			pstmt.setInt(2, userId);
			pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			pstmt.executeUpdate();
			pstmt.close();
			con.commit();
			chkDone = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return chkDone;
	}

	public static boolean rgInsertion(int userId, int OrgId , Connection con)throws SQLException {

		logger.info("RG Insert for UserID::" + userId + "  and OrgId::"	+ OrgId +" With Connection");
		boolean chkDone = false;
		String updateQuery = null;
		PreparedStatement pstmt = null;
		// for daily table
			updateQuery = "insert into st_lms_rg_org_daily_tx (organization_id,user_id) values(?,?)";
			pstmt = con.prepareStatement(updateQuery);
			pstmt.setInt(1, OrgId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
			DBConnect.closePstmt(pstmt);

			// for weekly table
			updateQuery = "insert into st_lms_rg_org_weekly_tx (organization_id,user_id,startdate) values (?,?,?)";
			pstmt = con.prepareStatement(updateQuery);
			pstmt.setInt(1, OrgId);
			pstmt.setInt(2, userId);
			pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			pstmt.executeUpdate();
			DBConnect.closePstmt(pstmt);
			chkDone = true;
			
		return chkDone;
	}
	
	private String criteriaAction;

	public List fetchRGEnumList() {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		List<List> mainList = new ArrayList<List>();
		try {
			selectQuery = "select SUBSTRING(COLUMN_TYPE,5) as list from information_schema.COLUMNS where TABLE_SCHEMA='"
					+ DBConnect.getDatabaseName()
					+ "' and TABLE_NAME='st_lms_rg_criteria_limit' and COLUMN_TYPE like 'enum%'";
			pstmt = con.prepareStatement(selectQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				List tempList = Arrays.asList(rs.getString("list").replace("(",
						"").replace(")", "").replaceAll("'", "").split(","));
				logger.info("******" + tempList);
				mainList.add(tempList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mainList;
	}

	public List fetchRGLimits(String criType) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		List<ResponsibleGamingBean> criList = new ArrayList<ResponsibleGamingBean>();
		ResponsibleGamingBean bean = null;
		try {
			selectQuery = "select crit_id,criteria_desc,criteria_limit,crit_status,crit_action,organization_type from st_lms_rg_criteria_limit where criteria_type='"
					+ criType
					+ "' and service_code in (select service_code from st_lms_service_master where status='ACTIVE')";
			pstmt = con.prepareStatement(selectQuery);
			logger.info("***fetchRGLimits****" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new ResponsibleGamingBean();
				bean.setCriteriaId(rs.getInt("crit_id"));
				bean.setCriteria(rs.getString("criteria_desc"));
				bean.setCriLimit(FormatNumber.formatNumber(rs.getDouble("criteria_limit")));
				bean.setCriAction(rs.getString("crit_action"));
				bean.setOrgType(rs.getString("organization_type"));
				bean.setStatus(rs.getString("crit_status"));
				criList.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return criList;
	}

	public String getCriteriaAction() {
		return criteriaAction;
	}

	public void logOutUser(UserInfoBean userBean) {
		//ServletContext sc = ServletActionContext.getServletContext();
		ServletContext sc = LMSUtility.sc;
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session = (HttpSession) currentUserSessionMap.get(userBean
				.getUserName());
        //if session is null then skip log out function
		if(session !=null){
		String critAction = getCriteriaAction();
		if ("INACTIVE_USER".equals(critAction)) { // user logout only in case
			// of invalid pwt
			currentUserSessionMap.remove(userBean.getUserName());
			userBean.setUserSession(null);
			return;
		}

		List actionList = (List) session.getAttribute("ACTION_LIST");
		logger.info("************Action_List**" + actionList);
		actionList = removeAction(actionList, critAction);

		logger.info("After RG Check ACTION_LIST" + actionList);
		session.setAttribute("ACTION_LIST", actionList);
		}
	}
	
	/**
	 * This function is used to cancel the ticket amount from sale
	 * @param orgId
	 * @param userId
	 * @param criteriaType
	 * @param amount
	 * @param con
	 * @return
	 */
	public boolean rgCounterCancel(int orgId, int userId, String criteriaType,
			double amount,Connection con) {
		logger.info("RG Counter for Criteria::" + criteriaType
				+ " and amount::" + amount);
		
		PreparedStatement pstmt = null;
		
		String  updateQuery = null;
		
		try {
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getStatus())){
				
						updateQuery = "update st_lms_rg_org_daily_tx set "
								+ criteriaType + "=" + criteriaType
								+ "-? where organization_id=? and user_id=?";
						pstmt = con.prepareStatement(updateQuery);
						pstmt.setDouble(1, amount);
						pstmt.setInt(2, orgId);
						pstmt.setInt(3, userId);
						logger.info("Update Query ::" + pstmt);
						pstmt.executeUpdate();
					
			}
					
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getStatus())){
				
				updateQuery = "update st_lms_rg_org_weekly_tx set "
							+ criteriaType + "=" + criteriaType
							+ "-? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setDouble(1, amount);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

		return false;
	}
	

	public boolean rgCounter(int orgId, int userId, String criteriaType,
			double amount,Connection con) {
		//logger.info("RG Counter for Criteria::" + criteriaType	+ " and amount::" + amount);
		
		PreparedStatement pstmt = null;
		ResultSet rsUser = null;
		String selectQuery = null, updateQuery = null, critAction = null;
		double maxLimit = 0.0, currentAmount = 0.0;
		boolean isFraud = false;
		try {
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getStatus())){
				maxLimit = Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_daily_tx where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(selectQuery);
					pstmt.setInt(1, orgId);
					pstmt.setInt(2, userId);
					logger.info("Select Query Retailer::" + pstmt);
					rsUser = pstmt.executeQuery();
					if (rsUser.next()) {
						currentAmount = rsUser.getDouble(criteriaType);
					}
					logger.info("check " + (currentAmount + amount)+ " *** maxLimit" + maxLimit + " **"+ (currentAmount + amount < maxLimit));
					if (currentAmount + amount <= maxLimit) {
						updateQuery = "update st_lms_rg_org_daily_tx set "
								+ criteriaType + "=" + criteriaType
								+ "+? where organization_id=? and user_id=?";
						pstmt = con.prepareStatement(updateQuery);
						pstmt.setDouble(1, amount);
						pstmt.setInt(2, orgId);
						pstmt.setInt(3, userId);
						logger.info("Update Query ::" + pstmt);
						pstmt.executeUpdate();
					} else {
						logger.info("************RG Criteria fails");
						isFraud = true;
						setCriteriaAction(critAction);
						return isFraud;
					}
			}
			
				
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getStatus())){
				maxLimit = Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_weekly_tx where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					currentAmount = rsUser.getDouble(criteriaType);
				}
				logger.info("check " + (currentAmount + amount)
						+ " *** maxLimit" + maxLimit + " **"
						+ (currentAmount + amount < maxLimit));
				if (currentAmount + amount < maxLimit) {
					updateQuery = "update st_lms_rg_org_weekly_tx set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setDouble(1, amount);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					setCriteriaAction(critAction);
					return isFraud;
				}
			}
			setCriteriaAction(critAction);
			
	/*		selectQuery = "select criteria_limit,crit_action,criteria_type from st_lms_rg_criteria_limit where criteria=? and  crit_status='ACTIVE' and organization_type='RETAILER' order by criteria_type";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, criteriaType);
			logger.info("Criteria Limit Query ::" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				logger.info("******for criteria_type"
						+ rs.getString("criteria_type"));
				if ("DAILY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getDouble("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_daily_tx";
				} else if ("WEEKLY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getDouble("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_weekly_tx";
				}
				selectQuery = "select " + criteriaType + " from " + tableName
						+ " where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					currentAmount = rsUser.getDouble(criteriaType);
				}
				logger.info("check " + (currentAmount + amount)
						+ " *** maxLimit" + maxLimit + " **"
						+ (currentAmount + amount < maxLimit));
				if (currentAmount + amount < maxLimit) {
					updateQuery = "update " + tableName + " set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setDouble(1, amount);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					break;
				}
			}
			setCriteriaAction(critAction);*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isFraud;
	}

	public boolean rgCounter(int orgId, int userId, String criteriaType,
			double amount) {
		logger.info("RG Counter for Criteria::" + criteriaType
				+ " and amount::" + amount);
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null, rsUser = null;
		String selectQuery = null, updateQuery = null, critAction = null, tableName = null;
		double maxLimit = 0.0, currentAmount = 0.0;
		boolean isFraud = false;
		try {
			con.setAutoCommit(false);
			
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getStatus())){
				maxLimit = Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_daily_tx where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(selectQuery);
					pstmt.setInt(1, orgId);
					pstmt.setInt(2, userId);
					logger.info("Select Query Retailer::" + pstmt);
					rsUser = pstmt.executeQuery();
					if (rsUser.next()) {
						currentAmount = rsUser.getDouble(criteriaType);
					}
					logger.info("check " + (currentAmount + amount)+ " *** maxLimit" + maxLimit + " **"+ (currentAmount + amount < maxLimit));
					if (currentAmount + amount < maxLimit) {
						updateQuery = "update st_lms_rg_org_daily_tx set "
								+ criteriaType + "=" + criteriaType
								+ "+? where organization_id=? and user_id=?";
						pstmt = con.prepareStatement(updateQuery);
						pstmt.setDouble(1, amount);
						pstmt.setInt(2, orgId);
						pstmt.setInt(3, userId);
						logger.info("Update Query ::" + pstmt);
						pstmt.executeUpdate();
					} else {
						logger.info("************RG Criteria fails");
						isFraud = true;
						setCriteriaAction(critAction);
						return isFraud;
					}
			}
			
				
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getStatus())){
				maxLimit = Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_weekly_tx where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					currentAmount = rsUser.getDouble(criteriaType);
				}
				logger.info("check " + (currentAmount + amount)
						+ " *** maxLimit" + maxLimit + " **"
						+ (currentAmount + amount < maxLimit));
				if (currentAmount + amount < maxLimit) {
					updateQuery = "update st_lms_rg_org_weekly_tx set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setDouble(1, amount);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					setCriteriaAction(critAction);
					return isFraud;
				}
			}
			setCriteriaAction(critAction);
			
			
			/*selectQuery = "select criteria_limit,crit_action,criteria_type from st_lms_rg_criteria_limit where criteria=? and  crit_status='ACTIVE' and organization_type='RETAILER' order by criteria_type";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, criteriaType);
			logger.info("Criteria Limit Query ::" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				logger.info("******for criteria_type"
						+ rs.getString("criteria_type"));
				if ("DAILY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getDouble("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_daily_tx";
				} else if ("WEEKLY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getDouble("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_weekly_tx";
				}
				selectQuery = "select " + criteriaType + " from " + tableName
						+ " where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					currentAmount = rsUser.getDouble(criteriaType);
				}
				logger.info("check " + (currentAmount + amount)
						+ " *** maxLimit" + maxLimit + " **"
						+ (currentAmount + amount < maxLimit));
				if (currentAmount + amount < maxLimit) {
					updateQuery = "update " + tableName + " set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setDouble(1, amount);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					break;
				}
			}
			setCriteriaAction(critAction);*/
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isFraud;
	}

	public boolean rgCounter(int orgId, int userId, String criteriaType,
			int count, Connection con) {
		logger.info("RG Counter for Criteria::" + criteriaType
				+ " and count::" + count);
		PreparedStatement pstmt = null;
		ResultSet rs = null, rsUser = null;
		String selectQuery = null, updateQuery = null, critAction = null;
		int maxLimit = 0, curCount = 0;
		boolean isFraud = false;
		try {
			
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getStatus())){
				maxLimit = (int) Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_daily_tx where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(selectQuery);
					pstmt.setInt(1, orgId);
					pstmt.setInt(2, userId);
					logger.info("Select Query Retailer::" + pstmt);
					rsUser = pstmt.executeQuery();
					if (rsUser.next()) {
						curCount = rsUser.getInt(criteriaType);
					}
					logger.info("check " + (curCount + count)
							+ " *** maxLimit" + maxLimit + " **"
							+ (curCount + count < maxLimit));
					if (curCount < maxLimit) {
						updateQuery = "update st_lms_rg_org_daily_tx set "
								+ criteriaType + "=" + criteriaType
								+ "+? where organization_id=? and user_id=?";
						pstmt = con.prepareStatement(updateQuery);
						pstmt.setInt(1, count);
						pstmt.setInt(2, orgId);
						pstmt.setInt(3, userId);
						logger.info("Update Query ::" + pstmt);
						pstmt.executeUpdate();
					} else {
						logger.info("************RG Criteria fails");
						isFraud = true;
						setCriteriaAction(critAction);
						return isFraud;
					}
			}
			
				
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getStatus())){
				maxLimit = (int) Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_weekly_tx where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					curCount = rsUser.getInt(criteriaType);
				}
				logger.info("check " + (curCount + count)
						+ " *** maxLimit" + maxLimit + " **"
						+ (curCount + count < maxLimit));
				if (curCount < maxLimit) {
					updateQuery = "update st_lms_rg_org_weekly_tx set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setInt(1, count);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				}else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					setCriteriaAction(critAction);
					return isFraud;
				}
			}
			setCriteriaAction(critAction);
			
			
			/*
			selectQuery = "select criteria_limit,crit_action,criteria_type from st_lms_rg_criteria_limit where criteria=? and  crit_status='ACTIVE' and organization_type='RETAILER' order by criteria_type";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, criteriaType);
			logger.info("Criteria Limit Query ::" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if ("DAILY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getInt("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_daily_tx";
				} else if ("WEEKLY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getInt("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_weekly_tx";
				}
				selectQuery = "select " + criteriaType + " from " + tableName
						+ " where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					curCount = rsUser.getInt(criteriaType);
				}
				logger.info("check " + (curCount + count)
						+ " *** maxLimit" + maxLimit + " **"
						+ (curCount + count < maxLimit));
				if (curCount < maxLimit) {
					updateQuery = "update " + tableName + " set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setInt(1, count);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					break;
				}
			}
			setCriteriaAction(critAction);*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isFraud;
	}

	public boolean rgCounterCheck(int orgId, int userId, String criteriaType,
			int count, Connection con) {
		logger.info("RG Counter for Criteria::" + criteriaType
				+ " and count::" + count);
		PreparedStatement pstmt = null;
		ResultSet rs = null, rsUser = null;
		String selectQuery = null, critAction = null;
		int maxLimit = 0, curCount = 0;
		boolean isFraud = false;
		try {
			
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getStatus())){
				maxLimit = (int) Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"DAILY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_daily_tx where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(selectQuery);
					pstmt.setInt(1, orgId);
					pstmt.setInt(2, userId);
					logger.info("Select Query Retailer::" + pstmt);
					rsUser = pstmt.executeQuery();
					if (rsUser.next()) {
						curCount = rsUser.getInt(criteriaType);
					}
					
					if (curCount < maxLimit) {
						logger.info("check " + (curCount + count)
								+ " *** maxLimit" + maxLimit + " **"
								+ (curCount + count < maxLimit));
					} else {
						logger.info("************RG Criteria fails");
						isFraud = true;
						setCriteriaAction(critAction);
						return isFraud;
					}
			}
			
				
			if("ACTIVE".equals(Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getStatus())){
				maxLimit = (int) Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriteriaLimit();
				critAction = Util.respGamingCriteriaStatusMap.get(criteriaType+"WEEKLY").getCriAction();
				
				selectQuery = "select " + criteriaType + " from st_lms_rg_org_weekly_tx where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					curCount = rsUser.getInt(criteriaType);
				}
				
				if (curCount < maxLimit) {
					logger.info("check " + (curCount + count)
						+ " *** maxLimit" + maxLimit + " **"
						+ (curCount + count < maxLimit));
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					setCriteriaAction(critAction);
					return isFraud;
				}
			}
			setCriteriaAction(critAction);
			
			
			/*
			selectQuery = "select criteria_limit,crit_action,criteria_type from st_lms_rg_criteria_limit where criteria=? and  crit_status='ACTIVE' and organization_type='RETAILER' order by criteria_type";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, criteriaType);
			logger.info("Criteria Limit Query ::" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if ("DAILY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getInt("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_daily_tx";
				} else if ("WEEKLY".equals(rs.getString("criteria_type"))) {
					maxLimit = rs.getInt("criteria_limit");
					critAction = rs.getString("crit_action");
					tableName = "st_lms_rg_org_weekly_tx";
				}
				selectQuery = "select " + criteriaType + " from " + tableName
						+ " where organization_id=? and user_id=?";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, orgId);
				pstmt.setInt(2, userId);
				logger.info("Select Query Retailer::" + pstmt);
				rsUser = pstmt.executeQuery();
				if (rsUser.next()) {
					curCount = rsUser.getInt(criteriaType);
				}
				logger.info("check " + (curCount + count)
						+ " *** maxLimit" + maxLimit + " **"
						+ (curCount + count < maxLimit));
				if (curCount < maxLimit) {
					updateQuery = "update " + tableName + " set "
							+ criteriaType + "=" + criteriaType
							+ "+? where organization_id=? and user_id=?";
					pstmt = con.prepareStatement(updateQuery);
					pstmt.setInt(1, count);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, userId);
					logger.info("Update Query ::" + pstmt);
					pstmt.executeUpdate();
				} else {
					logger.info("************RG Criteria fails");
					isFraud = true;
					break;
				}
			}
			setCriteriaAction(critAction);*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isFraud;
	}
	
	
	public boolean rgCounterCheck(int orgId, int userId,String criteriaType , double amount, Connection con,int zero) {
		logger.info("RG Counter for Criteria::" + criteriaType
				+ " and amount::" + amount);
	
		PreparedStatement pstmt = null;
		ResultSet rsUser = null;
		String selectQuery = null,updateQuery = null, critAction = null;
		
		boolean pwtCheck=false;
		boolean isFraud = false;
		try {
			con.setAutoCommit(false);
			
			
				selectQuery = "select if(dg_pwt_amt>max_daily_claim_amt,true,false) pwt_check from (select  organization_id,dg_pwt_amt+? dg_pwt_amt from st_lms_rg_org_daily_tx where organization_id=?) a inner join (select organization_id, max_daily_claim_amt from st_lms_oranization_limits where organization_id=?) b on a.organization_id=b.organization_id";
					pstmt = con.prepareStatement(selectQuery);
					pstmt.setDouble(1, amount);
					pstmt.setInt(2, orgId);
					pstmt.setInt(3, orgId);
					logger.info("Select Query Retailer::" + pstmt);
					rsUser = pstmt.executeQuery();
					if (rsUser.next()) {
						pwtCheck = rsUser.getBoolean("pwt_check");
					}
					
					if (!pwtCheck) {
						updateQuery = "update st_lms_rg_org_daily_tx set "+ criteriaType + "=" + criteriaType
								+ "+? where organization_id=?";
						pstmt = con.prepareStatement(updateQuery);
						pstmt.setDouble(1, amount);
						pstmt.setInt(2, orgId);
						logger.info("Update Query ::" + pstmt);
						pstmt.executeUpdate();
					} else {
						logger.info("************RG Criteria fails");
						isFraud = true;
						//setCriteriaAction(critAction);
						return isFraud;
					}
			
			//setCriteriaAction(critAction);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
				DBConnect.closePstmt(pstmt);
				DBConnect.closeRs(rsUser);
			}
		return isFraud;
	} 

	
	public StringBuilder saveRGLimits(int criteriaId[], String criLimit[],
			String criAction[], String status[], String criType) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String updateQuery = null;
		String insertQuery = null;
		String selectQuery = null;
		Map<Integer, Double> critIdLimitMap = new HashMap<Integer, Double>();
		Map<Integer, String> cirtIdNameMap = new HashMap<Integer, String>();
		StringBuilder returnStr = new StringBuilder("");
		try {
			con.setAutoCommit(false);
			selectQuery = "select rg2.crit_id,rg1.criteria,rg1.criteria_limit from st_lms_rg_criteria_limit rg1,st_lms_rg_criteria_limit rg2 where rg1.criteria=rg2.criteria and rg1.criteria_type=? and rg2.criteria_type=?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, "WEEKLY".equalsIgnoreCase(criType) ? "DAILY"
					: "WEEKLY");
			pstmt.setString(2, criType);
			logger.info("***selectQuery::" + pstmt);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				critIdLimitMap.put(rs.getInt("crit_id"), rs
						.getDouble("criteria_limit"));
				cirtIdNameMap.put(rs.getInt("crit_id"), rs
						.getString("criteria"));
			}
			logger.info("*****critIdLimitMap::" + critIdLimitMap);
			logger.info("*****cirtIdNameMap::" + cirtIdNameMap);
			pstmt.close();
			// insert into st_lms_rg_criteria_limit_history table
			insertQuery = "insert into st_lms_rg_criteria_limit_history (crit_id,date, criteria, criteria_type, criteria_limit, crit_status, organization_type, crit_action, criteria_desc,service_code) select crit_id,?,criteria,criteria_type,criteria_limit,crit_status,organization_type,crit_action,criteria_desc,service_code from st_lms_rg_criteria_limit where criteria_type='"
					+ criType + "'";
			pstmt = con.prepareStatement(insertQuery);
			pstmt.setTimestamp(1, Util.getCurrentTimeStamp());
			pstmt.executeUpdate();
			pstmt.close();

			// update st_lms_rg_criteria_limit table
			updateQuery = "update st_lms_rg_criteria_limit set criteria_limit=?,crit_status=?,crit_action=? where crit_id=?";
			pstmt = con.prepareStatement(updateQuery);
			for (int i = 0; i < criteriaId.length; i++) {
				pstmt.setDouble(1, Double.parseDouble(criLimit[i]));
				pstmt.setString(2, status[i]);
				pstmt.setString(3, criAction[i]);
				pstmt.setInt(4, criteriaId[i]);

				if ("WEEKLY".equalsIgnoreCase(criType)) {
					if (Double.parseDouble(criLimit[i]) >= critIdLimitMap
							.get(criteriaId[i])) {
						pstmt.executeUpdate();
						returnStr
								.append(cirtIdNameMap.get(criteriaId[i]) + ",");
					} else {
						cirtIdNameMap.remove(criteriaId[i]);
					}
				} else { // In case DAILY
					if (Double.parseDouble(criLimit[i]) <= critIdLimitMap
							.get(criteriaId[i])) {
						pstmt.executeUpdate();
						returnStr
								.append(cirtIdNameMap.get(criteriaId[i]) + ",");
					} else {
						cirtIdNameMap.remove(criteriaId[i]);
					}
				}
			}
			pstmt.close();
			con.commit();
			if (returnStr.indexOf(",") == -1) {
				returnStr.append("No Limits");
			} else {
				returnStr.deleteCharAt(returnStr.lastIndexOf(","));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return returnStr;
	}

	public void setCriteriaAction(String criteriaAction) {
		this.criteriaAction = criteriaAction;
	}

}