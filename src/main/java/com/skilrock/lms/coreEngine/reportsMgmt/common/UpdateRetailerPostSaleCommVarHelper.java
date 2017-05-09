package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.beans.PostSalePwtCommissionVarianceBean;
import com.skilrock.lms.common.db.DBConnect;

public class UpdateRetailerPostSaleCommVarHelper {

	public Map<Integer, PostSalePwtCommissionVarianceBean> fetchRetailerDepositCommExp(int agentOrgId) {

		Connection con = DBConnect.getConnection();
		PostSalePwtCommissionVarianceBean bean = null;
		Map<Integer, PostSalePwtCommissionVarianceBean> agentDepositCommMap = new HashMap<Integer, PostSalePwtCommissionVarianceBean>();
		try {
			
			PreparedStatement pstmt = con.prepareStatement("select organization_id,name,deposit_default_comm_rate,ifnull(deposit_comm_var,0.0)deposit_comm_var,ifnull(deposit_comm_var,0.0)+deposit_default_comm_rate total_comm_var from( " 
					                                     +" select organization_id,name,deposit_default_comm_rate from st_lms_organization_master om,st_lms_retailer_post_deposit_commission_details com where com.status='ACTIVE' and om.organization_type='RETAILER' and parent_id="+agentOrgId+")aa left join st_lms_retailer_post_deposit_commission_variance var on aa.organization_id=var.ret_org_id");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PostSalePwtCommissionVarianceBean();
				bean.setOrgId(rs.getInt("organization_id"));
				bean.setOrgName(rs.getString("name"));
				bean.setDefaultCommVar(rs.getDouble("deposit_default_comm_rate"));
				bean.setCommVar(rs.getDouble("deposit_comm_var"));
				bean.setTotalCommVar(rs.getDouble("total_comm_var"));
				agentDepositCommMap.put(rs.getInt("organization_id"), bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return agentDepositCommMap;
	
	}

	public void updateRetailerDepositVar(int[] retId, double[] depositVar, int userId) {
		Connection con = DBConnect.getConnection();
		try {
			
			StringBuilder agtIdStr = new StringBuilder("");
			StringBuilder agtIdUpdateTable=new StringBuilder("");
			StringBuilder agtIdUpdate=new StringBuilder("update st_lms_retailer_post_deposit_commission_variance com inner join (");
			for (int i = 0; i < retId.length; i++) {
				agtIdStr.append(retId[i] + ",");
				agtIdUpdateTable.append("select "+retId[i]+" ret_id,"+depositVar[i]+" deposit_var union ");
			}
			agtIdStr.delete(agtIdStr.lastIndexOf(","), agtIdStr.length());
			agtIdUpdateTable.delete(agtIdUpdateTable.lastIndexOf("union"), agtIdUpdateTable.length());
			agtIdUpdate.append(agtIdUpdateTable);
			agtIdUpdate.append(")var set com.deposit_comm_var=var.deposit_var where com.ret_org_id=var.ret_id");
			con.setAutoCommit(false);
			
			Statement stmt=con.createStatement();
			String insertQry="insert into st_lms_retailer_post_deposit_commission_variance_history(ret_org_id,change_comm_rate,commission_type,updated_by_user_id,updated_date) " 
                +	" select ret_org_id,deposit_comm_var,'DEPOSIT' comm_type,"+userId+",now() from st_lms_retailer_post_deposit_commission_variance where ret_org_id in("+agtIdStr.toString()+")";
			stmt.executeUpdate(insertQry);
			
			int update=stmt.executeUpdate(agtIdUpdate.toString());
			
			if(update != retId.length){
				String insertVarQry="insert into st_lms_retailer_post_deposit_commission_variance(ret_org_id,deposit_comm_var,tax_var,charges_1_var,charges_2_var,status) select ret_id,deposit_var,0 tax_default_comm_rate,0 charges_1,0 charges_2,status from( select ret_id,deposit_var from( select ret_id,ret_org_id,var.deposit_var from st_lms_retailer_post_deposit_commission_variance com right join ("+agtIdUpdateTable.toString()+") var on com.ret_org_id=var.ret_id)bb where ret_org_id is NULL)cc inner join st_lms_retailer_post_deposit_commission_details com";
				stmt.executeUpdate(insertVarQry);
			}
			
			
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}

	
	
}
