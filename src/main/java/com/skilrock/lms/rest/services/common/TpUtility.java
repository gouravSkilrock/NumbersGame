package com.skilrock.lms.rest.services.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.skilrock.lms.beans.AvailableServiceBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.userMgmt.common.CountryOrgHelper;

public class TpUtility {
	
	private CountryOrgHelper cntryOrgHlpr;
	
	public TpUtility() {
 		this.cntryOrgHlpr = new CountryOrgHelper();
 	}
 	
 	public TpUtility(CountryOrgHelper cntryOrgHlpr){
	
 		this.cntryOrgHlpr = cntryOrgHlpr;
 	}

	public static void validateSessions(UserInfoBean userInfoBean, String userSessionId, String serviceCode) throws LMSException{

		if (userInfoBean == null || !userInfoBean.getUserSession().equals(userSessionId)) {
			if ("IW".equals(serviceCode))
				throw new LMSException(01, SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
			else
				throw new LMSException(SLEErrors.SESSION_TIME_OUT_ERROR_CODE , SLEErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		}

	}
	
	public static UserInfoBean createParentUserBean(int agtUserId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		UserInfoBean agtInfoBean = new UserInfoBean();
		try {
			selectQuery = "select um.organization_id,um.user_name,om.organization_type,om.pwt_scrap from st_lms_organization_master om,st_lms_user_master um where um.user_id=? and om.organization_id=um.organization_id";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setInt(1, agtUserId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtInfoBean.setUserId(agtUserId);
				agtInfoBean.setUserType(rs.getString("organization_type"));
				agtInfoBean.setPwtSacrap(rs.getString("pwt_scrap"));
				agtInfoBean.setUserOrgId(rs.getInt("organization_id"));
				agtInfoBean.setUserName(rs.getString("user_name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}

		return agtInfoBean;
	}

	public static int getUserIdForOrgCode(String orgCode) {
		// TODO Auto-generated method stub
		
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		int userId = 0;
		try {
			selectQuery = "select user_id from st_lms_organization_master om INNER JOIN  st_lms_user_master um ON om.organization_id = um.organization_id where om.org_code = ?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, orgCode);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				userId = rs.getInt("user_id");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		
		return userId;
	}
	
	public int[] getServiceList() {
		
		ArrayList<Integer> mappingId=new ArrayList<Integer>();
		@SuppressWarnings("unchecked")
		List<AvailableServiceBean> serviceList = cntryOrgHlpr.getAvlSerInterface("RETAILER");
		
		for (Iterator<AvailableServiceBean> iterator = serviceList.iterator(); iterator.hasNext();) {
			AvailableServiceBean serviceBean = (AvailableServiceBean) iterator.next();
			mappingId.add(serviceBean.getMappingId());			
		}
		
		int s = mappingId.size();
	    int[] intArray = new int[s];
	    for (int i = 0; i < s; i++) {
	        intArray[i] = mappingId.get(i).intValue();
	    }
		
		return intArray;
	}

	public String[] getStatusTableValues() {
		
		ArrayList<String> serviceAttr=new ArrayList<String>();
		@SuppressWarnings("unchecked")
		List<AvailableServiceBean> serviceList = cntryOrgHlpr.getAvlSerInterface("RETAILER");
		
		for (Iterator<AvailableServiceBean> iterator = serviceList.iterator(); iterator.hasNext();) {
			AvailableServiceBean serviceBean = (AvailableServiceBean) iterator.next();
			
			StringBuilder sb= new StringBuilder();
			sb.append(serviceBean.getStatusInterface()).append("-").append(serviceBean.getPrivRepTable());
			serviceAttr.add(sb.toString());
		}
		
		int s = serviceAttr.size();
	    String[] servArray = new String[s];
	    for (int i = 0; i < s; i++) {
	    	servArray[i] = serviceAttr.get(i);
	    }
		return servArray;
	}

	public static boolean checkForDuplicateTpUserId(String tpUserId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		try {
			selectQuery = "select user_id from st_lms_user_master where tp_user_id = ?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, tpUserId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}

		return true;
	}

}
