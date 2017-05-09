package com.skilrock.lms.coreEngine.commercialService.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CSSaleBean;
import com.skilrock.lms.beans.CSUserBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class CSUtil {
	static Log logger = LogFactory.getLog(CSUtil.class);

	public static CSUserBean fetchUserInfo(String userName) {
		Connection con = DBConnect.getConnection();
		CSUserBean userBean = new CSUserBean();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select retslom.retUserId as userId, retslom.retOrgId as userOrgId, retslom.retOrgName as userOrgName, retslom.retUserName as userName,  agtslum.user_id as parentId, agtslom.organization_id as parentOrgId, agtslom.name as parentOrgName, agtslum.user_name as parentUserName,retslom.retStatus as userStatus, agtslom.organization_status as parentStatus, retslom.retBal as userBal, (agtslom.available_credit - agtslom.claimable_bal) as parentBal from st_lms_organization_master agtslom,(select slum.user_id as retUserId, slum.organization_id as retOrgId, slom.name as retOrgName, slum.user_name as retUserName,slom.parent_id as agtOrgId, slom.organization_status as retStatus, (slom.available_credit - slom.claimable_bal) as retBal  from st_lms_organization_master slom, st_lms_user_master slum where slum.organization_id = slom.organization_id and slom.organization_type = 'RETAILER' and slum.user_name = ?) retslom, st_lms_user_master agtslum where retslom.agtOrgId = agtslom.organization_id and retslom.agtOrgId = agtslum.organization_id and agtslom.organization_type = 'AGENT'");
			pstmt.setString(1, userName);
			logger.debug("userInfo " + pstmt);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				userBean.setUserId(rs.getInt("userId"));
				userBean.setUserOrgId(rs.getInt("userOrgId"));
				userBean.setUserName(rs.getString("userName"));
				userBean.setOrgName(rs.getString("userOrgName"));
				userBean.setUserOrgStatus(rs.getString("userStatus"));
				userBean.setUserOrgBalance(rs.getDouble("userBal"));

				userBean.setParentOrgUserId(rs.getInt("parentId"));
				userBean.setParentOrgId(rs.getInt("parentOrgId"));
				userBean.setParentUserName(rs.getString("parentUserName"));
				userBean.setParentOrgName(rs.getString("parentOrgName"));
				userBean.setParentOrgStatus(rs.getString("parentStatus"));
				userBean.setParentBalance(rs.getDouble("parentBal"));

			} else {
				userBean = null;
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return userBean;
	}

	public static Map<String, Double> fetchCommisions(String prodCode,
			String operatorCode, String circleCode, double denomination,
			String merchant, Connection con) {
		Map<String, Double> commMap = new HashMap<String, Double>();
		PreparedStatement pstmt = null;
		try {
			/*
			 * if("PAYWORLD".equalsIgnoreCase(merchant)){ pstmt = con
			 * .prepareStatement("select category_id, product_id, retailer_comm,
			 * agent_comm, jv_comm,unit_price, denomination from
			 * st_cs_product_master where product_code = ? and status =
			 * 'ACTIVE'"); pstmt.setString(1, prodCode); }
			 * if("CAMLOT".equalsIgnoreCase(merchant)){ pstmt = con
			 * .prepareStatement("select category_id, product_id, retailer_comm,
			 * agent_comm, jv_comm,unit_price, denomination from
			 * st_cs_product_master where product_code = ? and operator_code = ?
			 * and circle_code = ? and denomination = ? and status = 'ACTIVE'");
			 * pstmt.setString(1, prodCode); pstmt.setString(2, operatorCode);
			 * pstmt.setString(3, circleCode); pstmt.setDouble(4, denomination); }
			 */
			
			//This condition is plugged here to continue sale in kenya with OLd structure
			if("CAMLOT".equalsIgnoreCase(merchant)){
				pstmt = con
					.prepareStatement("select category_id, product_id, retailer_comm, agent_comm, jv_comm,unit_price, denomination from st_cs_product_master where product_code = ? and status = 'ACTIVE'");
				pstmt.setString(1, prodCode);
			}else{
			
			pstmt = con
					.prepareStatement("select category_id, product_id, retailer_comm, agent_comm, jv_comm,unit_price, denomination from st_cs_product_master where product_code = ? and operator_code = ? and circle_code = ? and denomination = ? and status = 'ACTIVE'");
			pstmt.setString(1, prodCode);
			pstmt.setString(2, operatorCode);
			pstmt.setString(3, circleCode);
			pstmt.setDouble(4, denomination);
			}
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				commMap.put("prodId", rs.getDouble("product_id"));
				commMap.put("retailerComm", rs.getDouble("retailer_comm"));
				commMap.put("agentComm", rs.getDouble("agent_comm"));
				commMap.put("jvComm", rs.getDouble("jv_comm"));
				commMap.put("unit_price", rs.getDouble("unit_price"));
				commMap.put("category_id", rs.getDouble("category_id"));
				if (Double.compare(rs.getDouble("denomination"), 0.00D) == 0) {
					commMap.put("is_flexi", 1.0);
				} else {
					commMap.put("is_flexi", 0.0);
				}
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return commMap;
	}

	public static int fetchRetParentId(int retOrgId) {
		int agtOrgId = 0;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select parent_id from st_lms_organization_master where organization_id = ? and organization_type = 'RETAILER'");
			pstmt.setInt(1, retOrgId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				agtOrgId = rs.getInt("parent_id");
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return agtOrgId;
	}

	public static double fetchUserBalance(String userName) {
		double balance = -1.0;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement(""
							+ "select (avaliable_credit - claimable_bal)as balance from st_lms_organization_master where name = ? and organization_type = 'RETAILER'");
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				balance = rs.getDouble("balance");
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return balance;
	}

	public static Map<String, Integer> fetchUserOrgId(String userName,
			int userOrgId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;

		try {
			pstmt = con
					.prepareStatement("select organization_id, user_id from st_lms_user_master where user_name = ? or organization_id=?  and organization_type = 'RETAILER'");
			pstmt.setString(1, userName);
			pstmt.setInt(2, userOrgId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				map.put("OrgId", rs.getInt("organization_id"));
				map.put("UserId", rs.getInt("user_id"));
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
		return map;
	}

	public static void insertIntoCamlotTansactionLog(int RMSTransId,
			String CSRefTransId, String providerRefId, String pin,
			String expiryDate, String mobileNum) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("insert into st_cs_camlot_transaction_log values (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, RMSTransId);
			pstmt.setString(2, CSRefTransId);
			pstmt.setString(3, providerRefId);
			if (pin == null) {
				pin = "NA";
			}
			pstmt.setString(4, pin);
			if (expiryDate == null) {
				expiryDate = "NA";
			}
			pstmt.setString(5, expiryDate);
			if (mobileNum == null) {
				mobileNum = "NA";
			}
			pstmt.setString(6, mobileNum);
			pstmt.executeUpdate();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}
	}

	public static void fetchRetCommVar(CSUserBean userBean, int productId,
			Connection con) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select sale_comm_variance from st_cs_agent_retailer_sale_comm_variance where retailer_org_id = ? and product_id =?");
			pstmt.setInt(1, userBean.getUserOrgId());
			pstmt.setInt(2, productId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				userBean.setUserSaleCommVar(rs.getDouble("sale_comm_variance"));
			}
			pstmt = con
					.prepareStatement("select sale_comm_variance from st_cs_bo_agent_sale_comm_variance where agent_org_id = ? and product_id =?");
			pstmt.setInt(1, userBean.getParentOrgId());
			pstmt.setInt(2, productId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				userBean.setParentSaleCommVar(rs
						.getDouble("sale_comm_variance"));
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		}

	}

	public static double fmtToFourDecimal(double number) {
		return Math.round((number * 10000)) / 10000.0;

	}

	public static Map<String, String> fetchStates() {
		Map<String, String> stateMap = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select circle_code, circle_name from st_cs_circle_master where status = 'ACTIVE'");
			ResultSet rs = pstmt.executeQuery();
			System.out.println("fetchStates query:" + pstmt);

			while (rs.next()) {
				stateMap.put(rs.getString("circle_code"), rs
						.getString("circle_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		System.out.println("StateMap in fetchStates(): " + stateMap);
		return stateMap;
	}

	public static Map<String, String> fetchOperatorMap() {

		Map<String, String> returnMap = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select distinct(om.operator_code), om.operator_name from st_cs_operator_master om, st_cs_product_master pm where om.operator_code=pm.operator_code and om.status='ACTIVE' AND pm.status='ACTIVE'");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				returnMap.put(rs.getString("operator_code"), rs
						.getString("operator_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnMap;
	}

	public static Map<String, String> fetchCircleMap(String operatorCode) {
		Map<String, String> returnMap = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select distinct(cm.circle_code), cm.circle_name from st_cs_circle_master cm, st_cs_product_master pm where pm.operator_code=? and cm.status='ACTIVE' AND pm.status='ACTIVE'");
			pstmt.setString(1, operatorCode);
			System.out.println("**fetchCircleMap query:" + pstmt + "*****");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				returnMap.put(rs.getString("circle_code"), rs
						.getString("circle_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnMap;
	}

	public static Map<String, String> fetchCategoryMap(String operatorCode,
			String state) {
		Map<String, String> returnMap = new HashMap<String, String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select distinct(pcm.category_code), pcm.description from st_cs_product_category_master pcm,  st_cs_product_master pm where operator_code=? and circle_code=? and pcm.category_id=pm.category_id;");
			pstmt.setString(1, operatorCode);
			pstmt.setString(2, state);
			System.out.println("***fetchCategoryMap query:" + pstmt + "*****");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String categoryCode = rs.getString("category_code");
				String description = rs.getString("description");
				returnMap.put(categoryCode, description);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnMap;
	}

	public static Map<String, String> fetchDenominationsMap(
			String operatorCode, String state, String voucherType) {
		Map<String, String> returnMap = new HashMap<String, String>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select pm.denomination from st_cs_product_category_master pcm, st_cs_product_master pm where operator_code=? and circle_code=? and pcm.category_code=? and pcm.category_id=pm.category_id;");
			pstmt.setString(1, operatorCode);
			pstmt.setString(2, state);
			pstmt.setString(3, voucherType);
			System.out.println("**fetchDenominationsMap query:" + pstmt + "**");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String denomination = rs.getString("denomination");
				returnMap.put(denomination, denomination);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnMap;
	}

	public static String fetchLastCSTransId(int retOrgId) throws LMSException {
		String lastTransId = "";
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		try {
			pstmt1 = con
					.prepareStatement("select CSLastSaleTransId from st_lms_last_sale_transaction where organization_id = ?");
			pstmt1.setInt(1, retOrgId);
			rs1 = pstmt1.executeQuery();
			if (rs1.next()) {
				lastTransId = rs1.getString("CSLastSaleTransId");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return lastTransId;
	}

	public static String fetchCSSaleData(CSSaleBean saleBean) {
		Connection con = DBConnect.getConnection();
		StringBuilder returnData = new StringBuilder("");
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select cpm.product_id, cpm.product_code, cpm.description, cpm.operator_code, com.operator_name, cpm.circle_code, cpm.category_id, ccm.circle_name, cpm.denomination, cpd.talktime, cpd.validity, cpd.admin_fee, cpd.service_tax, cpd.recharge_instructions from st_cs_product_master cpm, st_cs_operator_master com , st_cs_circle_master ccm, st_cs_product_category_master ccatm, st_cs_product_details cpd where com.operator_code = cpm.operator_code and ccm.circle_code = cpm.circle_code and cpm.category_id = ccatm.category_id and cpd.product_id = cpm.product_id and cpm.status = 'ACTIVE' and com.operator_code=? and cpm.denomination=?");
			pstmt.setString(1, saleBean.getOperatorCode());
			pstmt.setDouble(2, saleBean.getDenomination());
			System.out.println("fetchCSSaleData query:" + pstmt);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				saleBean.setProdId(rs.getInt("product_id"));
				saleBean.setProdCode(rs.getString("product_code"));
				returnData.append("description=" + rs.getString("description"));
				saleBean.setOperatorCode(rs.getString("operator_code"));
				returnData.append("|operator_name="
						+ rs.getString("operator_name"));
				saleBean.setCircleCode(rs.getString("circle_code"));
				returnData
						.append("|circle_name=" + rs.getString("circle_name"));
				saleBean.setDenomination(rs.getDouble("denomination"));
				saleBean.setCategoryId(rs.getInt("category_id"));

				String talkTime = rs.getString("talktime");
				double talkTimeVal = Double.parseDouble(talkTime);
				if (talkTimeVal < 0) {
					talkTime = "NA"; // For Applet Printing Purpose
				}

				String validity = rs.getString("validity");
				double validityVal = Double.parseDouble(validity);
				if (validityVal < 0) {
					validity = "NA";
				}

				String adminFee = rs.getString("admin_fee");
				double adminFeeVal = Double.parseDouble(adminFee);
				if (adminFeeVal < 0) {
					adminFee = "NA";
				}

				String serviceTax = rs.getString("service_tax");
				double serviceTaxVal = Double.parseDouble(serviceTax);
				if (serviceTaxVal < 0) {
					serviceTax = "NA";
				}

				String rechargeInstruction = "Dial "
						+ rs.getString("recharge_instructions")
						+ " & Press OK to recharge";

				returnData.append("|talktime=" + talkTime);
				returnData.append("|validity=" + validity);
				returnData.append("|admin_fee=" + adminFee);
				returnData.append("|service_tax=" + serviceTax);
				returnData.append("|instruction=" + rechargeInstruction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnData.toString();
	}

	public static String fetchCSReprintData(CSSaleBean saleBean, int lmsTxnId) {
		Connection con = DBConnect.getConnection();
		StringBuilder returnData = new StringBuilder("");
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select cpm.product_id, cpm.product_code, cpm.description, cpm.operator_code, com.operator_name, cpm.circle_code, ccm.circle_name, cpm.denomination, cpd.talktime, cpd.validity, cpd.admin_fee, cpd.service_tax, cpd.recharge_instructions from st_cs_product_master cpm, st_cs_operator_master com , st_cs_circle_master ccm, st_cs_product_category_master ccatm, st_cs_product_details cpd where com.operator_code = cpm.operator_code and ccm.circle_code = cpm.circle_code and cpm.category_id = ccatm.category_id and cpd.product_id = cpm.product_id and cpm.status = 'ACTIVE' and cpm.product_id=(select game_id as prodId from st_lms_retailer_transaction_master where transaction_id = ?)");
			pstmt.setInt(1, lmsTxnId);

			System.out.println("fetchCSReprintData query:" + pstmt);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				saleBean.setProdId(rs.getInt("product_id"));
				saleBean.setProdCode(rs.getString("product_code"));
				returnData.append("description=" + rs.getString("description"));
				saleBean.setOperatorCode(rs.getString("operator_code"));
				returnData.append("|operator_name="
						+ rs.getString("operator_name"));
				saleBean.setCircleCode(rs.getString("circle_code"));
				returnData
						.append("|circle_name=" + rs.getString("circle_name"));
				saleBean.setDenomination(rs.getDouble("denomination"));
				saleBean.setMrpAmt(saleBean.getDenomination());
				//returnData.append("|talktime=" + rs.getString("talktime"));
				//returnData.append("|validity=" + rs.getString("validity"));
				//returnData.append("|admin_fee=" + rs.getString("admin_fee"));
				//returnData.append("|service_tax=" + rs.getString("service_tax"));
				
				String talkTime = rs.getString("talktime");
				double talkTimeVal = Double.parseDouble(talkTime);
				if (talkTimeVal < 0) {
					talkTime = "NA"; // For Applet Printing Purpose
				}

				String validity = rs.getString("validity");
				double validityVal = Double.parseDouble(validity);
				if (validityVal < 0) {
					validity = "NA";
				}

				String adminFee = rs.getString("admin_fee");
				double adminFeeVal = Double.parseDouble(adminFee);
				if (adminFeeVal < 0) {
					adminFee = "NA";
				}

				String serviceTax = rs.getString("service_tax");
				double serviceTaxVal = Double.parseDouble(serviceTax);
				if (serviceTaxVal < 0) {
					serviceTax = "NA";
				}

				String rechargeInstruction = "Dial "
						+ rs.getString("recharge_instructions")
						+ " & Press OK to recharge";

				returnData.append("|talktime=" + talkTime);
				returnData.append("|validity=" + validity);
				returnData.append("|admin_fee=" + adminFee);
				returnData.append("|service_tax=" + serviceTax);
				returnData.append("|instruction=" + rechargeInstruction);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return returnData.toString();
	}

	/*
	 * public static Map<String, String> fetchOprNameIdMap(){ Map<String,
	 * String> returnMap = new HashMap<String, String>(); Connection con =
	 * DBConnect.getConnection(); try{ PreparedStatement pstmt =
	 * con.prepareStatement("select operator_name, operator_code from
	 * st_cs_operator_master where status = 'ACTIVE'");
	 * System.out.println("fetchOprNameIdMap query:" + pstmt); ResultSet rs =
	 * pstmt.executeQuery(); while (rs.next()) {
	 * returnMap.put(rs.getString("operator_name"),
	 * rs.getString("operator_code")); } } catch (Exception e) {
	 * e.printStackTrace(); } finally { DBConnect.closeCon(con); } return
	 * returnMap; }
	 */

	public static void updateLastCSTransId(int retOrgId, long newTransId)
			throws LMSException {
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			stmt
					.executeUpdate("update st_lms_last_sale_transaction set CSLastSaleTransId = "
							+ newTransId
							+ " where organization_id = "
							+ retOrgId);
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}

	public static List<Integer> fetchCSGameNumList() {
		List<Integer> csGameNumList = new ArrayList<Integer>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select category_id from st_cs_product_category_master where status = 'ACTIVE'");
			System.out.println("**fetchCSGameNumList query:" + pstmt);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				csGameNumList.add(rs.getInt("category_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return csGameNumList;
	}

}
