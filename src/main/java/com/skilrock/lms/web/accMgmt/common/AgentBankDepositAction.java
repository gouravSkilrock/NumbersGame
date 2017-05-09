
package com.skilrock.lms.web.accMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.MultiBankDepositBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryHelper;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.accMgmt.common.AgentBankDepositHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgentBankDepositAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String orgType;
	private String orgName;
	private String retOrgName;
	private String bankName;
	private String receiptNum;
	private String branchName;
	private String depositDate;
	private double totalAmount; 
	private String receiptId;
	private Map<String, String> bankMap;
	private Map<String, String> agtMap;
	private String finalPaymentData;
	
	private String agentOrgId;
	private List<MultiBankDepositBean> bdList = null;
	private String currentDate=null;
	
	Map<String, byte[]> receiptByteMap= null;
	
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getAgentOrgId() {
		return agentOrgId;
	}
	public void setAgentOrgId(String agentOrgId) {
		this.agentOrgId = agentOrgId;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	
	
	public String start() throws LMSException {
		QueryHelper qp = new QueryHelper();
		bankMap = qp.searchBanks();
		return SUCCESS;
	}
	
	public String save(){
		String autoGeneRecieptNo=null;
	    String rootPath=null;
		AgentBankDepositHelper helper=null;
		HttpSession session=null;
		UserInfoBean userBean=null,agentInfoBean=null;
		HttpServletRequest request=null;
	    Connection con=null;
		int agentOrgId=0;
		int retOrgId=0;
		try{
			helper=new AgentBankDepositHelper();
			request=ServletActionContext.getRequest();
			session=request.getSession();
			userBean=(UserInfoBean)session.getAttribute("USER_INFO");
			rootPath=(String) session.getAttribute("ROOT_PATH");
			agentOrgId=Integer.parseInt(getOrgName());
			retOrgId=Integer.parseInt(getRetOrgName());
			agentInfoBean=CommonMethods.fetchUserData(agentOrgId);
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			if(totalAmount>0.0){
				autoGeneRecieptNo=helper.submitBankDepositAmt(agentOrgId, "AGENT",  totalAmount,
					receiptNum, bankName, branchName, depositDate, userBean,con);
				if(orgType.equalsIgnoreCase("RETAILER")){
					autoGeneRecieptNo=helper.submitBankDepositAmtForRetailer(retOrgId,agentOrgId, agentInfoBean.getUserId(),orgType,  totalAmount,
							receiptNum, bankName, branchName, depositDate,con);	
				}
			    con.commit();	
			}else{
				return NONE;
			}
			GraphReportHelper graphReportHelper = new GraphReportHelper();
			int userOrgID = userBean.getUserOrgId();
			if(orgType.equalsIgnoreCase("AGENT")){
				String parentOrgName = null;
				parentOrgName = userBean.getOrgName();
				graphReportHelper.createTextReportBO(Integer.parseInt(autoGeneRecieptNo.split("Nxt")[0]), parentOrgName, userOrgID,
						rootPath);
			}
			else{
				graphReportHelper.createTextReportAgent(Integer.parseInt(autoGeneRecieptNo.split("Nxt")[0]), rootPath, agentOrgId, agentInfoBean.getOrgName());
			}	
			receiptId=autoGeneRecieptNo.split("Nxt")[1];
		}catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}finally{
				try{
					if(con!=null){
						con.close();
					}
				}catch(SQLException se){
					se.printStackTrace();
				}
		  }
		return SUCCESS;
	}
	
	public String fetchAgentAndBankDetails() throws LMSException {
		
		HttpSession session = request.getSession();
		UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
		int userOrgId = 0;
		if (uib != null)
			userOrgId = uib.getUserOrgId();

		bankMap = new QueryHelper().searchBanks();
		agtMap = AjaxRequestHelper.getOrgIdMap(userOrgId, "AGENT");
		currentDate = Util.getCurrentTimeString().substring(0, 10);
		return SUCCESS;
	}
	
	public void fetchAvlCredit() throws IOException{
		UserInfoBean bean = new UserInfoBean();
		bean.setUserOrgId(Integer.parseInt(agentOrgId));
		JSONObject obj = new JSONObject();
		obj.put("avlBal", new AjaxRequestHelper().getAvlblCreditAmt(bean));
		
		response.setContentType("application/json");
		PrintWriter out = null;
		out = response.getWriter();
		out.print(obj.toString());
		out.flush();
		out.close(); 
	}
	
	public String savePaymentData(){
		
		MultiBankDepositBean depositBean = null;
		UserInfoBean userBean=null;
		HttpSession session=null;
		String rootPath=null;
		
		try{
				System.out.println(finalPaymentData);
				
				request=ServletActionContext.getRequest();
				session=request.getSession();
				userBean=(UserInfoBean)session.getAttribute("USER_INFO");
				rootPath=(String) session.getAttribute("ROOT_PATH");
			
				JSONObject jObject = new JSONObject();
				jObject.put("finalData", finalPaymentData);
				JSONArray finalData = jObject.getJSONArray("finalData");
				
				bdList = new ArrayList<MultiBankDepositBean>();
				
				for (int i=0; i<finalData.size(); i++) {
				    JSONObject item = finalData.getJSONObject(i);
				    depositBean = new MultiBankDepositBean();
				    depositBean.setAgentOrgId(item.getInt("agtId"));
				    depositBean.setAgtName(item.getString("agtName"));
				    depositBean.setOrgType("AGENT");
				    depositBean.setTotalAmt(item.getDouble("depositAmt"));
				    depositBean.setReceiptNo(item.getString("receiptNo"));
				    depositBean.setBankName(item.getString("bankName"));
				    depositBean.setBranchName(item.getString("branchName"));
				    depositBean.setDepositDate(item.getString("depositDate"));
				    bdList.add(depositBean);				    
				}
				
				new AgentBankDepositHelper().submitBankDepositAmt(bdList, userBean);
				
				/*Generate Text Reports*/
				Iterator<MultiBankDepositBean> itr = bdList.iterator();
				receiptByteMap = new HashMap<String, byte[]>();
				while(itr.hasNext()){
					depositBean = itr.next();					
					GraphReportHelper graphReportHelper = new GraphReportHelper();
					int userOrgID = userBean.getUserOrgId();
					
					if("AGENT".equalsIgnoreCase(depositBean.getOrgType())){
						String parentOrgName = userBean.getOrgName();
						byte[] repBytes = graphReportHelper.createMultiTextReportBO(Integer.parseInt(depositBean.getAutoGeneratedReceiptId().split("Nxt")[0]), parentOrgName, userOrgID, rootPath);
						receiptByteMap.put(depositBean.getAutoGeneratedReceiptId().split("Nxt")[1],repBytes);
					}
					depositBean.setRefNo(depositBean.getAutoGeneratedReceiptId().split("Nxt")[1]);
				}
				session.setAttribute("RECEIPT_BYTE_MAP", receiptByteMap);
				setBdList(bdList);
			}catch(Exception e){
				e.printStackTrace();
				return ERROR;
			}
			return SUCCESS;	
		}
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRetOrgName() {
		return retOrgName;
	}
	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(String depositDate) {
		this.depositDate = depositDate;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	public String getReceiptId() {
		return receiptId;	
	}	
	public Map<String, String> getBankMap() {
		return bankMap;
	}
	public void setBankMap(Map<String, String> bankMap) {
		this.bankMap = bankMap;
	}
	
	public void setAgtMap(Map<String, String> agtMap) {
		this.agtMap = agtMap;
	}
	public Map<String, String> getAgtMap() {
		return agtMap;
	}
	public void setFinalPaymentData(String finalPaymentData) {
		this.finalPaymentData = finalPaymentData;
	}
	public String getFinalPaymentData() {
		return finalPaymentData;
	}
	public void setBdList(List<MultiBankDepositBean> bdList) {
		this.bdList = bdList;
	}
	public List<MultiBankDepositBean> getBdList() {
		return bdList;
	}
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	public String getCurrentDate() {
		return currentDate;
	}
}