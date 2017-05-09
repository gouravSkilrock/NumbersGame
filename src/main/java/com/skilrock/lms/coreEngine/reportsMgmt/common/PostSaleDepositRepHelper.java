package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.beans.PostSaleDepositAgentBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class PostSaleDepositRepHelper
{
	static Log logger = LogFactory.getLog(PostSaleDepositRepHelper.class);

	private static final PostSaleDepositRepHelper instance;

	static
	{
		instance = new PostSaleDepositRepHelper();
	}

	private PostSaleDepositRepHelper()
	{
	}

	public List<PostSaleDepositAgentBean> getReportData(int month, int year,String repType)
	{
		List<PostSaleDepositAgentBean> list = new ArrayList<PostSaleDepositAgentBean>();

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String appendQry =" ";
		if(!repType.equalsIgnoreCase("ALL")){
		   appendQry ="where bank_type='"+repType+"'";	
		}
		String orgCodeQry="om.name orgCode";
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = "om.org_code orgCode ";
		

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(om.org_code,'_',om.name)  orgCode  ";
		
			
			
		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(om.name,'_',om.org_code)  orgCode  ";
		
			
		}
		String query = " select "+orgCodeQry+",depositAmt,depCommAmt,taxAmt,charge1Amt,netAmt,ifnull(branch_name,'NA') branchName,ifnull(bank_name,'NA') bankName,ifnull(bank_type,'NA') bankType ,ifnull(account_nbr,'NA') account_nbr,ifnull(branch_sort_code,'NA') branch_sort_code from (select agt_org_id,sum(deposit_amount) depositAmt,sum(deposit_comm_amount) depCommAmt,sum(tax_amount) taxAmt,sum(charges_1) charge1Amt,sum(net_amount_to_pay) netAmt from st_lms_agent_post_deposit_datewise_commission    where month(start_date)=?  and year(start_date)=? group by agt_org_id) postDate  left join  st_lms_organization_master om  on  om.organization_id=postDate.agt_org_id left join st_lms_agent_bank_details bd On  bd.agent_org_id=postDate.agt_org_id left join st_lms_agent_bank_master bm on bm.bank_id=bd.bank_id left join st_lms_agent_branch_master  brm on brm.branch_id=bd.branch_id "+appendQry;

		try
		{
			connection =DBConnect.getConnection();	
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, month);
			pstmt.setInt(2,year);
			logger.info("post sale rep "+pstmt);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				PostSaleDepositAgentBean depositAgentBean = new PostSaleDepositAgentBean();
				depositAgentBean.setAgentName(rs.getString("orgCode"));
				depositAgentBean.setBankName(rs.getString("bankName"));
				depositAgentBean.setBankType(rs.getString("bankType"));
				depositAgentBean.setBranchName(rs.getString("branchName"));
				depositAgentBean.setCharge1Amount(rs.getDouble("charge1Amt"));
				depositAgentBean.setDepositAmount(rs.getDouble("depositAmt"));
				depositAgentBean.setDepositCommAmount(rs.getDouble("depCommAmt"));
				depositAgentBean.setNetAmount(rs.getDouble("netAmt"));
				depositAgentBean.setTaxAmount(rs.getDouble("taxAmt"));
				depositAgentBean.setAccountNumber(rs.getString("account_nbr"));
				depositAgentBean.setBranchSortCode(rs.getString("branch_sort_code"));
				list.add(depositAgentBean);
			}
		}catch (Exception ex){
			ex.printStackTrace();
			list.clear();
		}finally{
			DBConnect.closeCon(connection);
		}

		return list;
	}

	public static synchronized PostSaleDepositRepHelper getInstance()
	{
		return instance;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("Can't Create Clone");
	}
}