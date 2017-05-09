package com.skilrock.lms.coreEngine.inventoryMgmt.DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;

public class BODirectInvAssignNReturnDaoImpl extends MyTextProvider{
	private static final Logger logger = LoggerFactory.getLogger(BODirectInvAssignNReturnDaoImpl.class);
	private BODirectInvAssignNReturnDaoImpl(){}

	private static BODirectInvAssignNReturnDaoImpl classInstance;

	public static synchronized BODirectInvAssignNReturnDaoImpl getInstance() {
		if(classInstance == null)
			classInstance = new BODirectInvAssignNReturnDaoImpl();
		return classInstance;
	}
	
	public String retailerToAgentInvReturn(int retOrgId, String invSrNo,
			int agtOrgId,String modelId, String userType, int agtId,Connection con) throws LMSException {
		
		PreparedStatement psmt = null;
		PreparedStatement insIntoInvDet=null;
		ResultSet rs = null;
		String status = "error";
		String srNo = invSrNo;// serial no of inventory

		int  model  = 0;
		//String deviceType =null;
		String invColName =null;
		try {
			
			model  = Integer.parseInt(modelId.split("-")[0]);// device type of inventory
			
			String modelDeatials = "select  inv_column_name from st_lms_inv_model_master where model_id=?  " ;
			String assignSerNoQuery = "update st_lms_inv_status set current_owner_type = ?, current_owner_id = ? where serial_no =  ? and current_owner_id = ?";
			String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id, inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id) select ?,?,?,inv_model_id,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where serial_no = ?";
			psmt =con.prepareStatement(modelDeatials);
			psmt.setInt(1, model);
			rs =psmt.executeQuery();
			if(rs.next()){
				invColName =rs.getString("inv_column_name");
			}else{
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,getText("msg.some.internal.error.try.after.some.time"));
			}
			psmt = con.prepareStatement(assignSerNoQuery);
			psmt.setString(1, userType);
			psmt.setInt(2, agtOrgId);
			psmt.setString(3, srNo);
			psmt.setInt(4, retOrgId);
			logger.debug("update query for inventory::::::" + psmt);
			psmt.executeUpdate();
			insIntoInvDet = con.prepareStatement(insIntoInvDetQuery);
			insIntoInvDet.setInt(1, agtId);
			insIntoInvDet.setString(2, userType);
			insIntoInvDet.setInt(3, agtOrgId);
			// insIntoInvDet.setInt(4, inv_modelId);
			insIntoInvDet.setString(4, srNo);
			insIntoInvDet.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			insIntoInvDet.setString(6, userType);
			insIntoInvDet.setInt(7, agtOrgId);
			insIntoInvDet.setString(8, srNo);
			logger.debug("insert query for inventory::::::"+ insIntoInvDet);
			insIntoInvDet.executeUpdate();
			String offLoginStatus = "update st_lms_ret_offline_master set "+invColName+" = -1 , device_type = -1 where  organization_id=?";
			psmt = con.prepareStatement(offLoginStatus);
			psmt.setInt(1,retOrgId );
			psmt.executeUpdate();
			status = "success";

		}catch (Exception e) {
			logger.error("Exception",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error("Exception",e1);
			}
			return "error";
		}finally{
			
			DBConnect.closePstmt(psmt);
			DBConnect.closePstmt(insIntoInvDet);
			
		}
		return status;
	}
	
	public int agentToBoInventoryReturn(int userOrgId, int userId,
			String ownerType, int agtOrgId, int retOrgId, int nonConsInvId,
			int  nonConsModelId, int  nonConsBrandId, String serNo,
			String userType,Connection conn)
			throws LMSException {

		int DNID=0;
		PreparedStatement boReceiptNoGenStmt=null;
		PreparedStatement boReceiptStmt=null;
		try {
			String lastDCNoGenerated = null, autoGeneDCNo = null;
			
			boReceiptNoGenStmt = conn.prepareStatement("SELECT * from st_lms_inv_dl_detail where dl_owner_type=?  ORDER BY generated_dl_id DESC LIMIT 1");
			boReceiptNoGenStmt.setString(1, "BO");
			ResultSet DCRs = boReceiptNoGenStmt.executeQuery();

			while (DCRs.next()) {
				lastDCNoGenerated = DCRs.getString("generated_dl_id");
			}
			autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DNCHALLAN",
					lastDCNoGenerated, "BO");
			System.out.println("lastDCNoGenerated " + lastDCNoGenerated
					+ "autoGeneDCNo " + autoGeneDCNo);
			
			// insert into lms dl inv detail table for delivery challan
			 boReceiptStmt = conn.prepareStatement("insert into st_lms_inv_dl_detail(dl_owner_type,generated_dl_id) values(?,?)");
			
			boReceiptStmt.setString(1, "BO");
			boReceiptStmt.setString(2, autoGeneDCNo);
			System.out.println("bo DL receipt = " + boReceiptStmt);
			boReceiptStmt.execute();
			//find dlID
			boReceiptNoGenStmt = conn.prepareStatement("SELECT * from st_lms_inv_dl_detail where generated_dl_id=?  ORDER BY generated_dl_id DESC LIMIT 1");
			boReceiptNoGenStmt.setString(1, autoGeneDCNo);
			 DCRs = boReceiptNoGenStmt.executeQuery();

			while (DCRs.next()) {
				DNID = DCRs.getInt("dl_id");
			}
			
			// assign non consumable inventory
			returnAgentToBONonConsInv(userOrgId, userId, ownerType, agtOrgId, retOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo, conn,
					userType,DNID);
			// assign consumable inventory

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closePstmt(boReceiptNoGenStmt);
			DBConnect.closePstmt(boReceiptStmt);
		}

		return DNID;
	}
	
	private void returnAgentToBONonConsInv(int userOrgId, int userId, String ownerType,int agtOrgId, int retOrgId, int nonConsInvId,int nonConsModelId, int nonConsBrandId, String serNo,
			Connection conn, String userType,int DNID) throws LMSException {
		PreparedStatement assignSerNoPstmt=null;
		PreparedStatement insIntoInvDet=null;
		
			try{

				String assignSerNoQuery = "update st_lms_inv_status set user_id = ?, user_org_type = ?, user_org_id = ?, "
						+ "current_owner_type = ?, current_owner_id = ? where inv_model_id =? and serial_no = "
						+ " ? and current_owner_id = ?";
				assignSerNoPstmt = conn.prepareStatement(assignSerNoQuery);
				assignSerNoPstmt.setInt(1, userId);
				assignSerNoPstmt.setString(2, userType);
				assignSerNoPstmt.setInt(3, userOrgId);
				assignSerNoPstmt.setString(4, userType);
				assignSerNoPstmt.setInt(5, userOrgId);
				assignSerNoPstmt.setInt(8,agtOrgId);		
				logger.debug(assignSerNoQuery);
				// insert into st_lms_inv_detail table to add inventory quantity
				String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id,"
						+ " inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id)"
						+ " select ?,?,?,?,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where inv_model_id =? "
						+ " and serial_no = ?";
				insIntoInvDet = conn.prepareStatement(insIntoInvDetQuery);
				insIntoInvDet.setInt(1, userId);
				insIntoInvDet.setString(2, userType);
				insIntoInvDet.setInt(3, userOrgId);
				insIntoInvDet.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
				insIntoInvDet.setString(7, userType);
				insIntoInvDet.setInt(8, userOrgId);
					
				logger.debug(insIntoInvDetQuery);		
				int fstRowUpd = -1, scdRowUpd = -1;
				if (nonConsInvId > 0 && nonConsModelId > 0 && nonConsBrandId> 0 && !"".equals(serNo)) {
					assignSerNoPstmt.setInt(6, nonConsModelId);
					insIntoInvDet.setInt(4, nonConsModelId);
					insIntoInvDet.setInt(9, nonConsModelId);
					if (!"".equals(serNo.trim())) {
						// update st_lms_inv_status
							assignSerNoPstmt.setString(7, serNo);
							logger.debug("updPstmt = " + assignSerNoPstmt);
							fstRowUpd = assignSerNoPstmt.executeUpdate();
							if (fstRowUpd == 1) {
								// insert data into st_lms_inv_detail
								insIntoInvDet.setString(5, serNo);
								insIntoInvDet.setString(10, serNo);
								logger.debug("insPstmt = " + insIntoInvDet);
								scdRowUpd = insIntoInvDet.executeUpdate();
								if (scdRowUpd > 0) {
									ResultSet rsTask=insIntoInvDet.getGeneratedKeys();
									if(rsTask.next()){
										int taskId=rsTask.getInt(1);
										String insQuery="insert into st_lms_inv_dl_task_mapping values("+DNID+","+taskId+",'NON_CONS')";
										PreparedStatement	pstmt=conn.prepareStatement(insQuery);
										pstmt.executeUpdate();
									}
								}
								else{
									throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,LMSErrors.INVALID_INV_ERROR_MESSAGE);
								}
							} else {
								throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,LMSErrors.INVALID_INV_ERROR_MESSAGE);
							}
						}
					
	
					} else {
						// values are not in valid format
						logger.debug("inside else ==== " + nonConsInvId + " == "
								+ nonConsModelId + " === " + nonConsBrandId
								+ "==" + serNo.trim());
						throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
	

			}catch (SQLException s) {
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}catch (LMSException l) {
				throw l;
			}catch (Exception e) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			} finally{
				DBConnect.closePstmt(assignSerNoPstmt);
				DBConnect.closePstmt(insIntoInvDet);
			}
		
	}
	public boolean verifyAssignNonConsInv(int userOrgId, int invId,
			int modelId, int brandId, String serNo, Connection conn)
			throws SQLException, LMSException {

		boolean isValid = false;
		String assignSerNoQuery = null;
		Statement assignSerNoPstmt = conn.createStatement();
		ResultSet rs = null;
		try{
			List<String> dbEntries = new ArrayList<String>();
			if (invId > 0 && modelId > 0 && brandId > 0 && !"".equals(serNo.trim())) {
	
				assignSerNoQuery = "select serial_no from st_lms_inv_status  where inv_model_id = "
						+ modelId
						+ " and serial_no = '"
						+ serNo
						+ "' and  current_owner_type !='REMOVED' and current_owner_id = "
						+ userOrgId;
				logger.debug("fetch nonCons inv Pstmt for validate= "
						+ assignSerNoQuery);
				rs = assignSerNoPstmt.executeQuery(assignSerNoQuery);
				dbEntries.clear();
				while (rs.next()) {
					dbEntries.add(rs.getString("serial_no"));
				}
	
				if (dbEntries.contains(serNo.trim().toUpperCase()))
					isValid = true;
			}
			return isValid;
		}catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closeConnection(assignSerNoPstmt, rs);
		}
		
	}
	
	public boolean verifyAssignedInvForRetailer(int retOrgId, int invId,
			int modelId, int brandId, String serNo, Connection conn)
			throws LMSException {

		boolean isValid = true;
		String invCol = null;
		Statement psmt = null;
		String isInvInRetOffline = null;
		ResultSet rs = null;
		try {
			psmt = conn.createStatement();
			String modelDeatials = "SELECT inv_column_name, model_name FROM st_lms_inv_model_master WHERE model_id="
					+ modelId + " ";
			rs = psmt.executeQuery(modelDeatials);
			if (rs.next())
				invCol = rs.getString("inv_column_name");
			else
				throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,
						LMSErrors.INVALID_INV_ERROR_MESSAGE);

			isInvInRetOffline = "SELECT organization_id FROM st_lms_inv_status ivs INNER JOIN st_lms_ret_offline_master rom on ivs.current_owner_id = rom.organization_id AND ivs.serial_no = rom."
				+ invCol
				+ " WHERE current_owner_type <>'REMOVED'"
				+ " AND organization_id="
				+ retOrgId
				+ " ";

			rs = psmt.executeQuery(isInvInRetOffline);
			if (rs.next())
				throw new LMSException(
						LMSErrors.INV_ALREADY_ASSIGNED_ERROR_CODE,
						LMSErrors.INV_ALREADY_ASSIGNED_ERROR_MESSAGE);

			return isValid;
		} catch (LMSException e) {
			throw e;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally {
			DBConnect.closeConnection(psmt, rs);
		}

	}
	
	public int assignNonConsInvToRet(UserInfoBean boInfoBean,
			UserInfoBean agentBean, int retOrgId, int invId, int modelId,
			int brandId, String serNo, Connection conn) throws LMSException {

		int DNID = 0;
		String lastDCNoGenerated = null, autoGeneDCNo = null;
		PreparedStatement pstmt = null, insIntoInvDet = null;
		int fstRowUpd = -1, scdRowUpd = -1, trdRowUpd = -1;
		try {
			conn.setAutoCommit(false);

			pstmt = conn
					.prepareStatement("SELECT * from st_lms_inv_dl_detail where dl_owner_type=?  ORDER BY generated_dl_id DESC LIMIT 1");
			pstmt.setString(1, "BO");
			ResultSet DCRs = pstmt.executeQuery();

			while (DCRs.next()) {
				lastDCNoGenerated = DCRs.getString("generated_dl_id");
			}
			autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DNCHALLAN",
					lastDCNoGenerated, "BO");
			System.out.println("lastDCNoGenerated " + lastDCNoGenerated
					+ "autoGeneDCNo " + autoGeneDCNo);

			// insert into lms dl inv detail table for delivery challan
			pstmt = conn
					.prepareStatement("insert into st_lms_inv_dl_detail(dl_owner_type,generated_dl_id) values(?,?)");

			pstmt.setString(1, "BO");
			pstmt.setString(2, autoGeneDCNo);
			System.out.println("bo DL receipt = " + pstmt);
			pstmt.execute();
			// find dlID
			pstmt = conn
					.prepareStatement("SELECT * from st_lms_inv_dl_detail where generated_dl_id=?  ORDER BY generated_dl_id DESC LIMIT 1");
			pstmt.setString(1, autoGeneDCNo);
			DCRs = pstmt.executeQuery();

			while (DCRs.next()) {
				DNID = DCRs.getInt("dl_id");
			}

			String assignSerNoQuery = "update st_lms_inv_status set user_id = ?, user_org_type = ?, user_org_id = ?, "
					+ "current_owner_type = ?, current_owner_id = ? where inv_model_id =? and serial_no = "
					+ " ? and current_owner_id = ?";
			pstmt = conn.prepareStatement(assignSerNoQuery);
			pstmt.setInt(1, agentBean.getUserId());
			pstmt.setString(2, agentBean.getUserType());
			pstmt.setInt(3, agentBean.getUserOrgId());
			pstmt.setString(4, "RETAILER");
			pstmt.setInt(5, retOrgId);
			pstmt.setInt(6, modelId);
			pstmt.setString(7, serNo.trim());
			pstmt.setInt(8, boInfoBean.getUserOrgId());

			fstRowUpd = pstmt.executeUpdate();

			if (fstRowUpd == 1) {
				// insert data into st_lms_inv_detail
				String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id,"
						+ " inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id)"
						+ " select ?,?,?,?,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where inv_model_id =? "
						+ " and serial_no = ?";
				insIntoInvDet = conn.prepareStatement(insIntoInvDetQuery);

				// Insert inv detail for BO to AGENT
				insIntoInvDet.setInt(1, boInfoBean.getUserId());
				insIntoInvDet.setString(2, boInfoBean.getUserType());
				insIntoInvDet.setInt(3, boInfoBean.getUserOrgId());
				insIntoInvDet.setInt(4, modelId);
				insIntoInvDet.setString(5, serNo.trim());
				insIntoInvDet.setTimestamp(6, new Timestamp(
						new java.util.Date().getTime()));
				insIntoInvDet.setString(7, agentBean.getUserType());
				insIntoInvDet.setInt(8, agentBean.getUserOrgId());
				insIntoInvDet.setInt(9, modelId);
				insIntoInvDet.setString(10, serNo.trim());

				scdRowUpd = insIntoInvDet.executeUpdate();

				// Insert detail for AGENT to RETAILER
				insIntoInvDet.setInt(1, agentBean.getUserId());
				insIntoInvDet.setString(2, agentBean.getUserType());
				insIntoInvDet.setInt(3, agentBean.getUserOrgId());
				insIntoInvDet.setInt(4, modelId);
				insIntoInvDet.setString(5, serNo.trim());
				insIntoInvDet.setTimestamp(6, new Timestamp(
						new java.util.Date().getTime()));
				insIntoInvDet.setString(7, "RETAILER");
				insIntoInvDet.setInt(8, retOrgId);
				insIntoInvDet.setInt(9, modelId);
				insIntoInvDet.setString(10, serNo.trim());

				trdRowUpd = insIntoInvDet.executeUpdate();

				// Update inv for retailer in st_lms_ret_offline_master
				String invCol = null;
				String deviceType=null;
				String invColQuery = "SELECT inv_column_name,model_name FROM st_lms_inv_model_master WHERE model_id = "
						+ modelId + " ";
				pstmt = conn.prepareStatement(invColQuery);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()){
					invCol = rs.getString("inv_column_name");
					deviceType=rs.getString("model_name");
				}else{
					throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,LMSErrors.INVALID_INV_ERROR_MESSAGE);
				}
				String updateRetOfflineMst = "UPDATE st_lms_ret_offline_master SET "
						+ invCol
						+ "=?,device_type=? WHERE organization_id=?";
				pstmt = conn.prepareStatement(updateRetOfflineMst);
				pstmt.setString(1, serNo.trim());
				pstmt.setString(2, deviceType.trim());
				pstmt.setInt(3, retOrgId);

				int romUpdate = pstmt.executeUpdate();

				if (scdRowUpd > 0 && trdRowUpd > 0 && romUpdate > 0) {
					ResultSet rsTask = insIntoInvDet.getGeneratedKeys();
					if (rsTask.next()) {
						int taskId = rsTask.getInt(1);
						String insQuery = "insert into st_lms_inv_dl_task_mapping values("
								+ DNID + "," + taskId + ",'NON_CONS')";
						pstmt = conn.prepareStatement(insQuery);
						pstmt.executeUpdate();
						conn.commit();
					}
				}
			}
			return DNID;
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
			DBConnect.closePstmt(insIntoInvDet);
		}

	}


}