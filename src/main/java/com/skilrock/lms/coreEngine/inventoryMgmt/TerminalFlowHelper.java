package com.skilrock.lms.coreEngine.inventoryMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.InvTransitionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class TerminalFlowHelper {

	
	static Log logger = LogFactory.getLog(TerminalFlowHelper.class);
	
	public List<InvTransitionBean> getTermFlow(String termNumber, StringBuilder isValidTerminal, int modelId) throws LMSException {
		List<InvTransitionBean> transitionList = new ArrayList<InvTransitionBean>();
		Connection con = DBConnect.getConnection();
		//String modelNo = termNumber.split("-")[0];
		//String serialNo = termNumber.split("-")[1];
		
		String serialNo = termNumber;
		
		List<String> currOwnerList = new ArrayList<String>();
		List<String> currOwnerNameList = new ArrayList<String>();
		List<String> txTimeList = new ArrayList<String>();

		String currOwner = null;
		String nextOwner = null;
		Timestamp txTime = null;

		String currOwnerName = null;
		String nextOwnerName = null;

		InvTransitionBean invTransitionBean = null;
		transitionList = new ArrayList<InvTransitionBean>();
		String time = null;
		
		PreparedStatement pstmt = null;
		
		try {
			//PreparedStatement pstmt = con.prepareStatement("select date,user_org_type,current_owner_type,name from st_lms_inv_detail, st_lms_inv_model_master,st_lms_organization_master where serial_no=? and inv_model_id = model_id and model_name = ? and organization_id = current_owner_id");
			
			String orgCodeQry = " name orgCode ";

			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode  ";
			

			}			
			
			pstmt = con.prepareStatement("select date,inv.user_org_type,inv.current_owner_type,"+orgCodeQry+" from st_lms_inv_detail inv , st_lms_organization_master,st_lms_inv_status sts where inv.serial_no=? and inv.inv_model_id = ? and sts.inv_model_id = ? and sts.serial_no=inv.serial_no and organization_id = inv.current_owner_id and sts.current_owner_type <> 'REMOVED'");
			pstmt.setString(1, serialNo);
			pstmt.setInt(2,	modelId);
			pstmt.setInt(3,	modelId);
			//pstmt.setString(2, modelNo);
			System.out.println("query:" + pstmt);

			ResultSet rs = pstmt.executeQuery();
			
			SimpleDateFormat sdt = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

			while (rs.next()) {
				currOwnerList.add(rs.getString("current_owner_type"));
				currOwnerNameList.add(rs.getString("orgCode"));
				txTime = rs.getTimestamp("date");
				txTimeList.add(sdt.format(txTime));
			}
			if (currOwnerList != null && currOwnerList.size() > 1) {

				for (int i = 0; i < currOwnerList.size() - 1; i++) {

					currOwner = currOwnerList.get(i);
					nextOwner = currOwnerList.get(i + 1);
					time = txTimeList.get(i + 1);

					currOwnerName = currOwnerNameList.get(i);
					nextOwnerName = currOwnerNameList.get(i + 1);

					invTransitionBean = new InvTransitionBean(currOwnerName,
							nextOwnerName, currOwner, nextOwner, time);
					transitionList.add(invTransitionBean);

					if (currOwner.equals("BO")) {
						invTransitionBean.setBOToAgent(true);
					} else if (currOwner.equals("AGENT")) {

						if (nextOwner.equals("RETAILER")) {
							invTransitionBean.setAgentToRetailer(true);
						} else {
							invTransitionBean.setAgentToBO(true);
						}
					} else if (currOwner.equals("RETAILER")) {
						invTransitionBean.setRetailerToAgent(true);
					}
				}
			} else if(currOwnerList.size() == 0){
				isValidTerminal.append("Invalid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			DBConnect.closeCon(con);
		}
		
		return transitionList;
	}
	
	
	public String fetchSerialNo(int modelId, int bindLen, String termNumber) throws LMSException {		
		String validSerNo = null;			
		Connection con = null;		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			
				con = DBConnect.getConnection();	
				pstmt = con.prepareStatement("select serial_no from st_lms_inv_status where inv_model_id = "+modelId+" and substr(serial_no, length(serial_no)-("+bindLen+"-1), length(serial_no)) = '"+termNumber+"'");
				rs = pstmt.executeQuery();		
				while(rs.next()){
					validSerNo = rs.getString("serial_no");
				}	
			}catch (SQLException e) {
				logger.error("Exception:"+e);	
			}catch (Exception e) {
				logger.error("Exception:"+e);	
			} finally {
				DBConnect.closeConnection(con, pstmt, rs);
			}
			return validSerNo;
	}
}