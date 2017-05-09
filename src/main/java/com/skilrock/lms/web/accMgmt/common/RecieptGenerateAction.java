package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.LedgerHelper;

public class RecieptGenerateAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orgName;
	private String orgType;
	private String ownerName;
	private String parentOrgId;
	private int receiptId;
	HttpServletRequest request = null;
	private String serviceCode;

	private int getOrgId(String ownerName) throws LMSException {
		int orgId = -1;
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement("select organization_id from st_lms_organization_master where name=?");
			pstmt.setString(1, ownerName.trim());
			ResultSet result = pstmt.executeQuery();
			while (result.next()) {
				orgId = result.getInt("organization_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println(" closing connection  ");
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	private Map<String, Object> getPlayerValues(int rId, String serviceCode)
			throws LMSException {
		Map<String, Object> playerMap = new TreeMap<String, Object>();
		Connection conn = DBConnect.getConnection();
		try {
			String getReceiptType = "select user_type from st_lms_receipts_master where receipt_id = "
					+ rId;
			PreparedStatement pstmtt = conn.prepareStatement(getReceiptType);
			ResultSet rs = pstmtt.executeQuery();
			if (rs.next()) {
				String getTaskId = "";
				if ("BO".equalsIgnoreCase(rs.getString("user_type"))) {
					if ("SE".equalsIgnoreCase(serviceCode)) {
						getTaskId = "select cc.task_id, aa.receipt_id, aa.transaction_id, bb.party_id, cc.pwt_amt, cc.tax_amt  from st_lms_bo_receipts_trn_mapping aa, st_lms_bo_transaction_master bb, st_se_direct_player_pwt cc where aa.receipt_id = "
								+ rId
								+ " and aa.transaction_id = bb.transaction_id and bb.transaction_id = cc.transaction_id";
					} else if ("DG".equalsIgnoreCase(serviceCode)) {
						getTaskId = "select cc.task_id, aa.receipt_id, aa.transaction_id, bb.party_id, cc.pwt_amt, cc.tax_amt  from st_lms_bo_receipts_trn_mapping aa, st_lms_bo_transaction_master bb, st_dg_bo_direct_plr_pwt cc where aa.receipt_id = "
								+ rId
								+ " and aa.transaction_id = bb.transaction_id and bb.transaction_id = cc.transaction_id";
					}
				} else if ("AGENT".equalsIgnoreCase(rs.getString("user_type"))) {
					if ("SE".equalsIgnoreCase(serviceCode)) {
						getTaskId = "select cc.task_id, aa.receipt_id, aa.transaction_id, bb.party_id, cc.pwt_amt, cc.tax_amt  from st_lms_agent_receipts_trn_mapping aa, st_lms_agent_transaction_master bb, st_se_agt_direct_player_pwt cc where aa.receipt_id = "
								+ rId
								+ " and aa.transaction_id = bb.transaction_id and bb.transaction_id = cc.transaction_id";
					} else if ("DG".equalsIgnoreCase(serviceCode)) {
						getTaskId = "select cc.task_id, aa.receipt_id, aa.transaction_id, bb.party_id, cc.pwt_amt, cc.tax_amt  from st_lms_agent_receipts_trn_mapping aa, st_lms_agent_transaction_master bb, st_dg_agt_direct_plr_pwt cc where aa.receipt_id = "
								+ rId
								+ " and aa.transaction_id = bb.transaction_id and bb.transaction_id = cc.transaction_id";
					}
				}
				PreparedStatement pstmt = conn.prepareStatement(getTaskId);
				ResultSet result = pstmt.executeQuery();
				while (result.next()) {
					System.out.println(pstmt + "\n-------TASK ID="
							+ result.getInt("task_id"));
					playerMap.put("playerId", "" + result.getInt("party_id"));
					playerMap.put("pwt_amt", "" + result.getDouble("pwt_amt"));
					playerMap.put("tax_amt", "" + result.getDouble("tax_amt"));
					playerMap.put("task_id", result.getInt("task_id"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println(" closing connection  ");
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return playerMap;
	}

	public int getReceiptId() {
		return receiptId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String receiptGenerate() throws Exception {
		HttpSession session = request.getSession();
		GraphReportHelper graphReportHelper = new GraphReportHelper();
		UserInfoBean userBean = null;
		String parentOrgName = null;
		int userOrgID = 0;
		LedgerHelper ledger = new LedgerHelper();
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		parentOrgName = userBean.getOrgName();
		String agentOrgId = (String) session.getAttribute("agentOrgIdSearch");

		if (agentOrgId != null) {
			userOrgID = Integer.parseInt(agentOrgId);
			// userOrgID=userBean.getUserOrgId();
			System.out.println("getAgentOrgId not null userOrgID" + userOrgID
					+ "  agentOrgId " + agentOrgId);
		} else if ("RETAILER".equalsIgnoreCase(orgType.trim())
				&& "BO".equalsIgnoreCase(userBean.getUserType())) {
			userOrgID = getOrgId(ownerName);
			System.out.println("getAgentOrgId  null userOrgID" + userOrgID
					+ "uyuy " + agentOrgId);
		} else if ("RETAILER".equalsIgnoreCase(orgType.trim())
				&& "AGENT".equalsIgnoreCase(userBean.getUserType())) {
			userOrgID = getOrgId(ownerName);
			System.out.println("getAgentOrgId  null userOrgID" + userOrgID
					+ "uyuy " + agentOrgId);
		} else {
			userOrgID = userBean.getUserOrgId();
			System.out.println("getAgentOrgId  null userOrgID" + userOrgID
					+ "uyuy " + agentOrgId);
		}
		System.out.println("receipt no : " + receiptId + ",  Org type : "
				+ orgType + ", parentOrgName : " + parentOrgName
				+ " userOrgID@@ " + userOrgID + " Orgname " + getOrgName());

		// generate pdf report
		if ("PLAYER".equalsIgnoreCase(orgType.trim())) {
			Map<String, Object> playerMap = getPlayerValues(receiptId,
					serviceCode);
			if (serviceCode.equalsIgnoreCase("SE")) {
				graphReportHelper.createTextReportPlayer((Integer) playerMap
						.get("task_id"), (String) session
						.getAttribute("ROOT_PATH"), "SCRATCH_GAME");
			} else if (serviceCode.equalsIgnoreCase("DG")) {
				graphReportHelper.createTextReportPlayer((Integer) playerMap
						.get("task_id"), (String) session
						.getAttribute("ROOT_PATH"), "DRAW_GAME");
			}
		} 
		// added check for OLA_DISTRIBUTOR organization type
		else if ("AGENT".equalsIgnoreCase(orgType.trim())
				|| "GOVT".equalsIgnoreCase(orgType.trim())
				|| "OLA_DISTRIBUTOR".equalsIgnoreCase(orgType.trim())
				&& "BO".equalsIgnoreCase(userBean.getUserType())) {
			graphReportHelper.createTextReportBO(receiptId, parentOrgName,
					userOrgID, (String) session.getAttribute("ROOT_PATH"));
		} else if ("RETAILER".equalsIgnoreCase(orgType.trim())
				|| "GOVT".equalsIgnoreCase(orgType.trim())
				&& "BO".equalsIgnoreCase(userBean.getUserType())) {
			orgName = ledger.getOrganizationName(userOrgID);
			graphReportHelper.createTextReportAgent(receiptId, (String) session
					.getAttribute("ROOT_PATH"), userOrgID, orgName);
		} else if ("RETAILER".equalsIgnoreCase(orgType.trim())
				|| "GOVT".equalsIgnoreCase(orgType.trim())
				&& "AGENT".equalsIgnoreCase(userBean.getUserType())) {
			orgName = ledger.getOrganizationName(userOrgID);
			graphReportHelper.createTextReportAgent(receiptId, (String) session
					.getAttribute("ROOT_PATH"), userOrgID, orgName);
		}

		return SUCCESS;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public void setReceiptId(int receiptId) {
		this.receiptId = receiptId;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

}
