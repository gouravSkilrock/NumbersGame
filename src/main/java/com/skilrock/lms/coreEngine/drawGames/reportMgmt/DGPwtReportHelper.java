package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.DirPlrPwtRepBean;
import com.skilrock.lms.beans.PwtDetailsBean;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawWinTktsBean;
import com.skilrock.lms.dge.beans.GameWinTktDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class DGPwtReportHelper  extends MyTextProvider {
	static Log logger = LogFactory.getLog(DGPwtReportHelper.class);
	private Connection con = null;
	private Timestamp end;
	private Date endDate;
	private int OrgId;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Timestamp start;
	private Date startDate;

	public DGPwtReportHelper() {

	}

	public DGPwtReportHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		start = new Timestamp(startDate.getTime());
		this.endDate = dateBeans.getLastdate();
		end = new Timestamp(endDate.getTime());
		this.OrgId = userInfoBean.getUserOrgId();
	}

	public Map<Integer, String> ajaxAgentListForPwt() throws LMSException {
		Map<Integer, String> map = new TreeMap<Integer, String>();
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select name, organization_id from st_lms_organization_master where organization_type='AGENT'");

			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				map.put(rs.getInt("organization_id"), rs.getString("name"));
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
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
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<PwtDetailsBean> fetchClaimTicketData(String agtOrgName,
			String filter) {
		con = DBConnect.getConnection();
		StringBuilder orgIdList = null;
		String query = null;
		List<PwtDetailsBean> pwtDetailsBean = new ArrayList<PwtDetailsBean>();
		List<PwtDetailsBean> dirPlrBean = new ArrayList<PwtDetailsBean>();
		PwtDetailsBean detailBean = null;
		String subQuery = "";
		List<Integer> gameNumList = Util.getGameNumberList();
		try {
			if (!"Game Wise".equalsIgnoreCase(filter)) {
				String selQry = "";
				orgIdList = new StringBuilder("");
				if ("Agent Wise".equalsIgnoreCase(filter)) {
					selQry = "select organization_id from st_lms_organization_master where parent_id =(select organization_id from st_lms_organization_master where name=?)";

				} else if ("Retailer Wise".equalsIgnoreCase(filter)) {
					selQry = "select organization_id from st_lms_organization_master where name=?";
				}
				pstmt = con.prepareStatement(selQry);
				pstmt.setString(1, agtOrgName);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					orgIdList.append(rs.getInt("organization_id") + ",");
				}
				if (orgIdList.length() > 1) {
					orgIdList.deleteCharAt(orgIdList.length() - 1);
				}
				subQuery = "and retailer_org_id in (" + orgIdList + ")";

			}

			for (int i = 0; i < gameNumList.size(); i++) {
				if ("Agent Wise".equalsIgnoreCase(filter)) {
					query = "select sum(amount) amount, sum(totTkt) totTkt,prize,game_id from (select sum(pwt_amt) amount,count(pwt_amt) totTkt,pwt_amt prize,game_id,' ' from st_dg_ret_pwt_"
							+ gameNumList.get(i)
							+ " sdrp where sdrp.transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
							+ new java.sql.Date(startDate.getTime())
							+ "'and transaction_date<'"
							+ new java.sql.Date(endDate.getTime())
							+ "' "
							+ subQuery
							+ ") and status='DONE_CLM' group by game_id,pwt_amt union select sum(pwt_amt) amount,count(pwt_amt) totTkt,pwt_amt prize,game_id,'DP' from  st_dg_agt_direct_plr_pwt sdrp where sdrp.transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='"
							+ new java.sql.Date(startDate.getTime())
							+ "' and transaction_date<'"
							+ new java.sql.Date(endDate.getTime())
							+ "' ) and game_id="
							+ gameNumList.get(i)
							+ " and sdrp.agent_org_id=(select organization_id from st_lms_organization_master where name='"
							+ agtOrgName
							+ "') group by game_id,pwt_amt) a  group by game_id,prize order by prize";
				} else {
					query = "select sum(pwt_amt) amount,count(pwt_amt) totTkt,pwt_amt prize,game_id from st_dg_ret_pwt_"
							+ gameNumList.get(i)
							+ " sdrp where sdrp.transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
							+ new java.sql.Date(startDate.getTime())
							+ "'and transaction_date<'"
							+ new java.sql.Date(endDate.getTime())
							+ "' "
							+ subQuery
							+ ") and status='DONE_CLM' group by game_id,pwt_amt order by prize";
				}
				pstmt = con.prepareStatement(query);
				logger.debug("Pwt Details****" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					detailBean = new PwtDetailsBean();
					detailBean.setAmount(rs.getString("amount"));
					detailBean.setNoOfTkt(rs.getInt("totTkt"));
					detailBean.setPrize(rs.getString("prize"));
					detailBean.setGameName(Util.getGameDisplayName(rs
							.getInt("game_id")));
					detailBean.setPlayerName(agtOrgName);
					pwtDetailsBean.add(detailBean);
				}

			}

			/*
			 * if ("Agent Wise".equalsIgnoreCase(filter)) { dirPlrQry = "select
			 * sum(pwt_amt) amount,count(pwt_amt) totTkt,pwt_amt
			 * prize,game_id,agent_org_id from st_dg_agt_direct_plr_pwt sdrp
			 * where sdrp.transaction_id in (select transaction_id from
			 * st_lms_agent_transaction_master where transaction_date>='" +
			 * startDate + "'and transaction_date<'" + endDate + "' and
			 * sdrp.agent_org_id in (" + orgIdList + ")) group by
			 * game_id,pwt_amt order by prize"; } else if ("Retailer
			 * Wise".equalsIgnoreCase(filter)) { //selQry = "select
			 * organization_id from st_lms_organization_master where name=?"; }
			 * pstmt = con.prepareStatement(dirPlrQry); rs =
			 * pstmt.executeQuery(); while (rs.next()) { detailBean = new
			 * PwtDetailsBean(); detailBean.setAmount(rs.getString("amount"));
			 * detailBean.setNoOfTkt(rs.getInt("totTkt"));
			 * detailBean.setPrize(rs.getString("prize"));
			 * detailBean.setGameName(Util.getGameName(rs .getInt("game_id")));
			 * detailBean.setPlayerName(agtOrgName); dirPlrBean.add(detailBean); }
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

		return pwtDetailsBean;
	}

	public List<DirPlrPwtRepBean> getDirPlrPwtDetailAgentWise() {
		List<DirPlrPwtRepBean> amtList = new ArrayList<DirPlrPwtRepBean>();
		con = DBConnect.getConnection();
		try {
			String dirPwtRepQuery = QueryManager
					.getST_DG_DIR_PLR_PWT_REPORT_AGENT_WISE_BO();
			PreparedStatement pstmt = con.prepareStatement(dirPwtRepQuery);
			/*
			 * pstmt.setDate(1, ); ResultSet rs1 = pstmt.executeQuery();
			 * 
			 * while(rs1.next()){ bean = new DirPlrPwtRepBean();
			 * bean.setRetName(rs1.getString(""));
			 * bean.setPwtAmtClm(rs1.getDouble("")); }
			 */
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
			}
		}
		return amtList;
	}

	public List<DirPlrPwtRepBean> getDirPlrPwtDetailGameWise() {
		List<DirPlrPwtRepBean> amtList = new ArrayList<DirPlrPwtRepBean>();
		con = DBConnect.getConnection();
		DirPlrPwtRepBean bean = null;
		DirPlrPwtRepBean totalBean = null;
		double totPwtAmt = 0.0;
		try {
			String dirPwtRepQuery = QueryManager
					.getST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_BO();
			PreparedStatement pstmt = con.prepareStatement(dirPwtRepQuery);
			pstmt.setDate(1, new java.sql.Date(start.getTime()));
			pstmt.setDate(2, new java.sql.Date(end.getTime()));
			ResultSet rs1 = pstmt.executeQuery();
			logger.debug("get Direct Plr PWT Rep Query Game wise for BO -- "
					+ pstmt);
			while (rs1.next()) {
				bean = new DirPlrPwtRepBean();
				bean.setGameName(rs1.getString("game_name"));
				bean.setPwtAmtClm(rs1.getDouble("total_pwt"));
				totPwtAmt += rs1.getDouble("total_pwt");
				amtList.add(bean);
			}
			totalBean = new DirPlrPwtRepBean();
			totalBean.setGameName("Total");
			totalBean.setPwtAmtClm(totPwtAmt);
			amtList.add(totalBean);
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
			}
		}
		return amtList;
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
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
		return orgAdd;
	}

	public List<PwtReportBean> getPwtDetailAgentWise() {

		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		PwtReportBean totalBean = null;
		double totPwtAmt = 0.0;
		con = DBConnect.getConnection();

		try {

			// pstmt=con.prepareStatement(QueryManager.getST_PWT_REPORT_BO());
			String orgCodeQry = " name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}	
			pstmt = con
					.prepareStatement("select sum(pwt_amt) pwt_amt,"+orgCodeQry+" from st_dg_bo_pwt sdbp,st_lms_bo_transaction_master slbtm,st_lms_organization_master slom where sdbp.transaction_id=slbtm.transaction_id and (slbtm.transaction_date>=? and slbtm.transaction_date<?) and slom.organization_id=sdbp.agent_org_id group by agent_org_id order by "+QueryManager.getAppendOrgOrder());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			rs = pstmt.executeQuery();
			logger.debug(" get pwt details query- ==== -" + pstmt);
			while (rs.next()) {
				reportbean = new PwtReportBean();
				reportbean.setClmMrp(FormatNumber.formatNumber(rs
						.getDouble("pwt_amt")));
				totPwtAmt += rs.getDouble("pwt_amt");
				reportbean.setAgentName(rs.getString("orgCode"));
				list.add(reportbean);
			}
			totalBean = new PwtReportBean();
			totalBean.setAgentName("Total");
			totalBean.setClmMrp(FormatNumber.formatNumber(totPwtAmt));
			list.add(totalBean);

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
			}
		}
		return list;
	}

	/**
	 * It is called when BO want to get the PWT details 'Game Wise'
	 * 
	 * @param gameStatus
	 * @return List<GameWisePWTBean>
	 */

	public List<PwtReportBean> getPwtDetailGameWise() {
		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		con = DBConnect.getConnection();
		PwtReportBean reportbean = null;
		PwtReportBean totalBean = null;
		double totPwtAmt = 0.0;
		try {
			PreparedStatement pst = con.prepareStatement(QueryManager
					.getST_DG_PWT_REPORT_GAME_WISE_BO());
			pst.setDate(1, startDate);
			pst.setDate(2, endDate);
			pst.setDate(3, startDate);
			pst.setDate(4, endDate);
			pst.setDate(5, startDate);
			pst.setDate(6, endDate);
			pst.setDate(7, startDate);
			pst.setDate(8, endDate);
			ResultSet rss = pst.executeQuery();
			logger.debug("get Player PWT query : == " + pst);
			while (rss.next()) {
				reportbean = new PwtReportBean();
				reportbean.setClmMrp(FormatNumber.formatNumber(rss
						.getDouble("agtMrpClaimed")));
				totPwtAmt += rss.getDouble("agtMrpClaimed");
				reportbean.setUnclmMrp(FormatNumber.formatNumber(rss
						.getDouble("agtMrpUnclm")));
				reportbean.setGameName(rss.getString("gamename"));
				list.add(reportbean);
			}
			totalBean = new PwtReportBean();
			totalBean.setGameName("Total");
			totalBean.setClmMrp(FormatNumber.formatNumber(totPwtAmt));
			list.add(totalBean);

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
			}
		}

		// logger.debug(list);
		return list;
	}

	public List<PwtReportBean> getPwtDetailRetailerWise(int agtId) {
		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		PwtReportBean totalBean = null;
		double totPwtAmt = 0.0;
		con = DBConnect.getConnection();
		try {
			String orgCodeQry = " name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}	
			PreparedStatement pst = con.prepareStatement("select top.orgCode,  myn.pwtMrpClm as pwtMrpClm, myn.pwtMrpUnclm as pwtMrpUnclm from (select lef.retailer_org_id, ifnull(lef.pwtMrpClm,0) as pwtMrpClm, ifnull(righ.pwtMrpUnclm,0) as pwtMrpUnclm from (select dgpclm.retailer_org_id, ifnull(sum(pwtMrpClm),0) as pwtMrpClm from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrpClm from st_dg_agt_pwt where status='DONE_CLM') as dgpclm where main.transaction_id = dgpclm.transaction_id  and main.user_org_id = dgpclm.agent_org_id and dgpclm.agent_org_id = ? group by dgpclm.retailer_org_id) as lef left outer join (select dgpUnclm.retailer_org_id, ifnull(sum(pwtMrpUnclm),0) as pwtMrpUnclm from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrpUnclm from st_dg_agt_pwt where status='CLAIM_BAL' or status = 'UNCLAIM_BAL') as dgpUnclm where main.transaction_id = dgpUnclm.transaction_id  and main.user_org_id = dgpUnclm.agent_org_id and dgpUnclm.agent_org_id = ? group by dgpUnclm.retailer_org_id)as righ on lef.retailer_org_id = righ.retailer_org_id)as myn,(select organization_id, "+orgCodeQry+" from st_lms_organization_master where organization_type='RETAILER')as top where top.organization_id = myn.retailer_org_id");
			pst.setDate(1, startDate);
			pst.setDate(2, endDate);
			pst.setInt(3, agtId);
			pst.setDate(4, startDate);
			pst.setDate(5, endDate);
			pst.setInt(6, agtId);
			ResultSet rss = pst.executeQuery();
			logger.debug("get Player PWT query : == " + pst);
			while (rss.next()) {
				reportbean = new PwtReportBean();
				reportbean.setClmMrp(FormatNumber.formatNumber(rss
						.getDouble("pwtMrpClm")));
				totPwtAmt += rss.getDouble("pwtMrpClm");
				reportbean.setUnclmMrp(FormatNumber.formatNumber(rss
						.getDouble("pwtMrpUnclm")));
				reportbean.setRetName(rss.getString("orgCode"));
				list.add(reportbean);
			}
			totalBean = new PwtReportBean();
			totalBean.setRetName("Total");
			totalBean.setClmMrp(FormatNumber.formatNumber(totPwtAmt));
			list.add(totalBean);
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}
		DBConnect.closeCon(con);
		return list;
	}
	public Map<Integer, DrawWinTktsBean> fetchRetailerWiseWinningTicketsInfo(DrawDataBean drawDataBean,Map<String,String> orgCodeNameMap,Map<String,String> paymentDateMap,Map<String,String> parentOrgNameMap) 
	throws LMSException{
		ArrayList<Integer> retUserIdList=null;
		Connection con=null;
		Map<Integer,DrawWinTktsBean> drawBeanMap = null;
		GameWinTktDataBean gameWinTktDataBean=null;
		try{
			con=DBConnect.getConnection();
			retUserIdList=fetchRetailirUserIdList(drawDataBean.getAgentOrgId(), con);
			drawDataBean.setRetailerUserIdList(retUserIdList);
			ServiceRequest serReq = new ServiceRequest();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_WINNING_TICKET_DATA);
			serReq.setServiceData(drawDataBean);
			IServiceDelegate delegate =ServiceDelegate.getInstance(); 
			ServiceResponse serResp=delegate.getResponse(serReq);
			if(serResp.getIsSuccess()){
				gameWinTktDataBean  = new Gson().fromJson(serResp.getResponseData().toString(), GameWinTktDataBean.class);
				drawBeanMap=gameWinTktDataBean.getDrawMap();
				if(!drawBeanMap.isEmpty() && gameWinTktDataBean.getUserIdDetailList().size()>0){
					fetchOrgCodeName(gameWinTktDataBean.getUserIdDetailList(),con,orgCodeNameMap,parentOrgNameMap);
					fetchClamiemedAtOrgCodeMap(gameWinTktDataBean.getClaimetAtBoTickets(),gameWinTktDataBean.getClaimetAtAgtTickets(),gameWinTktDataBean.getClaimetAtRetTickets(),drawDataBean.getGameNo(),con,paymentDateMap);
				}else{
					throw new LMSException(LMSErrors.NO_RECORD_FOUND_ERROR_CODE,getText("msg.no.record"));
				}	
			}else{
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} catch (LMSException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
		}
		return drawBeanMap;
	}
	

	public ArrayList<Integer> fetchRetailirUserIdList(int agentOrgId,Connection con) throws LMSException{
		ArrayList<Integer> retUserIdList=new ArrayList<Integer>();
		Statement stmt=null;
		ResultSet rs=null;
		String qry=null;
		try{
			if(agentOrgId > 0){
				con =DBConnect.getConnection();
				qry="SELECT user_id FROM st_lms_user_master um INNER JOIN st_lms_organization_master om  ON om.organization_id=um.organization_id WHERE parent_id="+agentOrgId+" AND om.organization_type='RETAILER'";
				stmt=con.createStatement();
				rs=stmt.executeQuery(qry);
				if(rs.next()) {
					rs.beforeFirst();
					while(rs.next()){
						retUserIdList.add(rs.getInt("user_id"));
					}
				} else {
					throw new LMSException(LMSErrors.NO_RECORD_FOUND_ERROR_CODE,getText("msg.no.record"));
				}
				logger.info("Retailers For Agent: "+agentOrgId+":- "+retUserIdList);
			
				}	
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
		return retUserIdList;
	}
	
	public void fetchOrgCodeName(Set<String> retUserIdSet,Connection con, Map<String,String> orgCodeNameMap,Map<String,String> parentOrgNameMap) throws LMSException {
		Statement stmt=null;
		ResultSet rs=null;
		String qry=null;
		List<String> parentUserIdlist=new ArrayList<String>();
		try{
			qry= "SELECT user_id,concat(org_code,'_',om.name) orgCode,parent_user_id FROM  st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id  where um.user_id  in ("+ retUserIdSet.toString().substring(1,retUserIdSet.toString().lastIndexOf("]")) + ")";
			stmt=con.createStatement();
			rs=stmt.executeQuery(qry);
			while(rs.next()){
				orgCodeNameMap.put(rs.getString("user_id"),rs.getString("orgCode"));
				parentUserIdlist.add(rs.getString("parent_user_id"));
			}
			qry=" SELECT user_id,concat(ch.org_code,'_',ch.name) orgCode,parent_user_id FROM  st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id  inner join st_lms_organization_master ch on ch.organization_id=.om.parent_id where om.organization_type='RETAILER'  and parent_user_id  IN ("+ parentUserIdlist.toString().substring(1,parentUserIdlist.toString().lastIndexOf("]")) + ")";
			stmt=con.createStatement();
			rs=stmt.executeQuery(qry);
			while(rs.next()){
				parentOrgNameMap.put(rs.getString("user_id"), rs.getString("orgCode"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}
	
	public void fetchClamiemedAtOrgCodeMap(Set<String> claimedByBoTktSet,Set<String> claimedByAgtTktSet,Set<String> claimedByRetTktSet,int gameId,Connection con,Map<String, String> paymentDateMap) throws LMSException{
		Statement stmt=null;
		ResultSet rs=null;
		String boPaymentateDateQry=null;
		String agtPaymentDateQry=null;
		String retPaymentDateQry=null;
		try {
			stmt=con.createStatement();
			if(claimedByBoTktSet.size()>0){
				boPaymentateDateQry="SELECT ticket_nbr,transaction_date from st_dg_pwt_inv_"+gameId+"  dpi INNER JOIN  st_lms_bo_transaction_master btm ON dpi.bo_transaction_id=btm.transaction_id where ticket_nbr IN("+ claimedByBoTktSet.toString().substring(1,claimedByBoTktSet.toString().lastIndexOf("]"))+")";
				rs=stmt.executeQuery(boPaymentateDateQry);
				while(rs.next()){
					paymentDateMap.put(rs.getString("ticket_nbr"), rs.getString("transaction_date").replace(".0", ""));
				}
			}
			if(claimedByAgtTktSet.size()>0){
				agtPaymentDateQry="SELECT ticket_nbr,transaction_date from st_dg_pwt_inv_"+gameId+"  dpi INNER JOIN  st_lms_agent_transaction_master atm ON dpi.agent_transaction_id=atm.transaction_id where ticket_nbr IN("+ claimedByAgtTktSet.toString().substring(1,claimedByAgtTktSet.toString().lastIndexOf("]"))+")";
				rs=stmt.executeQuery(agtPaymentDateQry);
				while(rs.next()){
					paymentDateMap.put(rs.getString("ticket_nbr"), rs.getString("transaction_date").replace(".0", ""));
				}
			}
			if(claimedByRetTktSet.size()>0){
				retPaymentDateQry="SELECT ticket_nbr,transaction_date from st_dg_pwt_inv_"+gameId+"  dpi INNER JOIN  st_lms_retailer_transaction_master rtm ON dpi.retailer_transaction_id=rtm.transaction_id where ticket_nbr IN("+ claimedByRetTktSet.toString().substring(1,claimedByRetTktSet.toString().lastIndexOf("]"))+")";
				rs=stmt.executeQuery(retPaymentDateQry);
				while(rs.next()){
					paymentDateMap.put(rs.getString("ticket_nbr"), rs.getString("transaction_date").replace(".0", ""));
				}
			}
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}

}
