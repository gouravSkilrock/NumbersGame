package com.skilrock.lms.controller.invMgmtController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.MySqlQueries;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.inventoryMgmt.DaoImpl.BODirectInvAssignNReturnDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;




public class BODirectInvAssignNReturnControllerImpl extends MyTextProvider{

	private BODirectInvAssignNReturnControllerImpl(){}

	private static BODirectInvAssignNReturnControllerImpl classInstance;

	public static synchronized BODirectInvAssignNReturnControllerImpl getInstance() {
		if(classInstance == null)
			classInstance = new BODirectInvAssignNReturnControllerImpl();
		return classInstance;
	}
	
	public int retailerToBOInvReturnProcess(UserInfoBean userBean,int retOrgId, String invSrNo,
			int agtOrgId,String modelId,String brandId,int invId) throws LMSException{
		String status="failed";
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;
		int agtId=0;
		int DNID=0;
		try{
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			if (invSrNo != null && retOrgId != 0 && agtOrgId!=0 ) {
				String query=MySqlQueries.ST3_GET_USER_ID+" where organization_id="+agtOrgId;
				stmt=con.createStatement();
				rs=stmt.executeQuery(query);
				if(rs.next()){
					agtId=rs.getInt("user_id");
				}else{
					throw new LMSException(LMSErrors.INVALID_AGENT_ERROR_CODE,getText("error.invalid.agent"));
				}

				status = BODirectInvAssignNReturnDaoImpl.getInstance().retailerToAgentInvReturn(retOrgId, invSrNo,
						agtOrgId, modelId,"AGENT",agtId,con);
				if("success".equalsIgnoreCase(status)){
					DNID=BODirectInvAssignNReturnDaoImpl.getInstance().agentToBoInventoryReturn(userBean.getUserOrgId(), userBean.getUserId(), "AGENT", agtOrgId, retOrgId, invId,Integer.parseInt(modelId.split("-")[0]), Integer.parseInt(brandId.split("-")[0]), invSrNo, userBean.getUserType(), con);
					con.commit();
				}
			} else {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,getText("msg.some.internal.error.try.after.some.time"));
			}
		}catch (LMSException e) {
			throw e;
		}catch (Exception e1) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,getText("msg.some.internal.error.try.after.some.time"));
		}finally{
			DBConnect.closeConnection(con, stmt, rs);
		}
		
		return DNID;
		
	}
	
	public int assignInvBoToRetailer(int agtOrgId, int retOrgId, int invId,
			String modelName, String brandName, String invSrNo,
			UserInfoBean boInfoBean) throws LMSException {
		Connection con = null;
		int DNID = 0;
		try {
			int modelId = Integer.parseInt(modelName.split("-")[0]);
			int brandId = Integer.parseInt(brandName.split("-")[0]);
			con = DBConnect.getConnection();
			// Validate if the valid BO inventory to assign
			if (!BODirectInvAssignNReturnDaoImpl.getInstance()
					.verifyAssignNonConsInv(boInfoBean.getUserOrgId(), invId,
							modelId, brandId, invSrNo, con))
				throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,
						getText("inv.detail.error.msg"));

			// Validate if the retailer already have this type of invetory
			if (!BODirectInvAssignNReturnDaoImpl.getInstance()
					.verifyAssignedInvForRetailer(retOrgId, invId, modelId,
							brandId, invSrNo, con))
				throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,
						getText("inv.detail.error.msg"));

			UserInfoBean agentBean = new OrgNUserRegHelper()
					.createAgtBean(agtOrgId);

			DNID = BODirectInvAssignNReturnDaoImpl.getInstance()
					.assignNonConsInvToRet(boInfoBean, agentBean, retOrgId,
							invId, modelId, brandId, invSrNo, con);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					getText("msg.some.internal.error.try.after.some.time"));
		} finally {
			DBConnect.closeCon(con);
		}

		return DNID;
	}
}