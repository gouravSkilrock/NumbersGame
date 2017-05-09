package com.skilrock.lms.web.reportsMgmt.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.TallyXMLFilesBean;
import com.skilrock.lms.beans.TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries;
import com.skilrock.lms.beans.TallyXmlVariablesBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class TallyXMLFilesMonthlyScheduleHelper{
	private static Log logger = LogFactory.getLog(TallyXMLFilesMonthlyScheduleHelper.class);
	
	/*public static String perform(int jobId) throws JobExecutionException {
		Connection con=null;	
		SimpleDateFormat formet = new SimpleDateFormat("yyyy/MM/dd");
		try{
			con=DBConnect.getConnection();

			Calendar start = Calendar.getInstance();
			start.add(Calendar.MONTH, -1);
			start.set(Calendar.DAY_OF_MONTH, 1);
			String startDate=formet.format(start.getTime()).replace("/", "-");

			Calendar end = Calendar.getInstance();
			end.add(Calendar.MONTH, -1);
		  	end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
		  	String endDate=formet.format(end.getTime()).replace("/", "-");

			SchedulerCommonFuntionsHelper.updateSchedulerStart(jobId, con);
			logger.info("Tally Xml Sale Pwt Training Exp Files for date:"+startDate+" to "+endDate);
			saleXMLFilesCreation(startDate,endDate,con);
			pwtXMLFilesCreation(startDate,endDate,con);
			trainingExpensesXMLFilesCreation(startDate,endDate,con);
			
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
		finally{
			DBConnect.closeCon(con);
		}
		return null;
	}*/

	public static void trainingExpensesXMLFilesCreation(String startDate, String endDate, Connection con) {
		Statement stmt=null;
		ResultSet rs=null;
		String query=null;
		TallyXmlVariablesBean bean=null;
		try{
			bean=new TallyXmlVariablesBean();
			bean.setEndDate(endDate);
			bean.setStartDate(startDate);
			bean.setTrainingBean();
			stmt=con.createStatement();
//			query="select kk.Name name,sum(Amt) amt from(select sum(amount) Amt, agent_org_id from  (select amount ,a.agent_org_id  as agent_org_id from st_lms_bo_credit_note a inner join st_lms_bo_transaction_master b on a.transaction_id =b.transaction_id where  date(b.transaction_date) between '"+startDate+"' and '"+endDate+"' and b.transaction_type in('CR_NOTE_CASH') and a.reason in ('CR_WEEKLY_EXP','CR_DAILY_EXP') group by  agent_org_id union all select -amount ,a.agent_org_id  as agent_org_id  from st_lms_bo_debit_note a inner join st_lms_bo_transaction_master b on a.transaction_id =b.transaction_id where  date(b.transaction_date) between '"+startDate+"' and '"+endDate+"' and b.transaction_type in('DR_NOTE_CASH') and a.reason in ('DR_DAILY_TE_RETURN','DR_WEEKLY_TE_RETURN') group by  agent_org_id)s group by agent_org_id)kb inner join st_lms_organization_master kk on kb.agent_org_id =kk.organization_id group by kk.Name;";
			query="select kk.Name name,amt from(select sum(amt) amt, agent_org_id from (select sum(amount) amt ,a.agent_org_id  as agent_org_id from st_lms_bo_credit_note a inner join st_lms_bo_transaction_master b on a.transaction_id =b.transaction_id where  date(b.transaction_date) between '"+startDate+"' and '"+endDate+"' and b.transaction_type in('CR_NOTE_CASH') and a.reason in ('CR_WEEKLY_EXP','CR_DAILY_EXP') group by  agent_org_id union all select -sum(amount) amt,a.agent_org_id  as agent_org_id  from st_lms_bo_debit_note a inner join st_lms_bo_transaction_master b on a.transaction_id =b.transaction_id where  date(b.transaction_date) between '"+startDate+"' and '"+endDate+"' and b.transaction_type in('DR_NOTE_CASH') and a.reason in ('DR_DAILY_TE_RETURN','DR_WEEKLY_TE_RETURN') group by  agent_org_id)s group by agent_org_id)kb inner join st_lms_organization_master kk on kb.agent_org_id =kk.organization_id group by kk.Name order by kk.organization_id;";
			logger.info("Training Exp query:"+query);
			rs=stmt.executeQuery(query);
			xmlFileCreation(bean,rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public static void pwtXMLFilesCreation(String startDate, String endDate, Connection con) {
		Statement stmt=null;
		ResultSet rs=null;
		String query=null;
		TallyXmlVariablesBean bean=null;
		Map<Integer, GameMasterLMSBean>lmsGameMap=null;
		try{
			bean=new TallyXmlVariablesBean();
			bean.setEndDate(endDate);
			bean.setStartDate(startDate);
			bean.setPwtBean();
			lmsGameMap=Util.getLmsGameMap();
			stmt=con.createStatement();
			Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameMap = lmsGameMap.entrySet().iterator();
			while (gameMap.hasNext()) {
				Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameMap.next();
				{
					String gameName = gameNumpair.getValue().getGameNameDev();
					int gameId = gameNumpair.getKey();
					if ("TwelveByTwentyFour".equalsIgnoreCase(gameName)|| "KenoFour".equalsIgnoreCase(gameName)|| "KenoFive".equalsIgnoreCase(gameName)|| "KenoNine".equalsIgnoreCase(gameName)) {
						query = "select name,sum(pwt_amt) as amt from (select parent_id,sum(pwt_amt) as pwt_amt from (select drs.retailer_org_id,sum(pwt_amt + agt_claim_comm) as pwt_amt from st_dg_ret_pwt_"+gameId+" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and date(transaction_date)>='"+startDate+"' and date(transaction_date)<='"+endDate+"' group by drs.retailer_org_id )kb inner join st_lms_organization_master om on om.organization_id=kb.retailer_org_id group by parent_id union all select agent_org_id,sum(net_amt) as pwt_amt from st_dg_agt_direct_plr_pwt  where date(transaction_date)>='"+startDate+"' and date(transaction_date)<='"+endDate+"' and game_id="+gameId+" group by agent_org_id)k inner join st_lms_organization_master om on om.organization_id=k.parent_id group by name;";
						logger.info("Pwt query for game "+gameName+" :"+query);
						rs = stmt.executeQuery(query);
						bean.setGameName(gameName);
						xmlFileCreation(bean, rs);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public static void saleXMLFilesCreation(String startDate,String endDate, Connection con) {
		Statement stmt=null;
		ResultSet rs=null;
		String query=null;
		TallyXmlVariablesBean bean=null;
		Map<Integer, GameMasterLMSBean>lmsGameMap=null;
		try{
			bean=new TallyXmlVariablesBean();
			bean.setSaleBean();
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			lmsGameMap=Util.getLmsGameMap();
			stmt=con.createStatement();
			Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameMap = lmsGameMap.entrySet().iterator();
			while (gameMap.hasNext()) {
				Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameMap.next();
				{
					String gameName = gameNumpair.getValue().getGameNameDev();
					int gameId = gameNumpair.getKey();
					if ("TwelveByTwentyFour".equalsIgnoreCase(gameName)|| "KenoFour".equalsIgnoreCase(gameName)|| "KenoFive".equalsIgnoreCase(gameName)|| "KenoNine".equalsIgnoreCase(gameName)) {
						query="select name,mrp_amt as amt from (select parent_id,sum(mrp_amt) as mrp_amt from (select sum(mrp_amt) as mrp_amt,retailer_org_id from (select drs.retailer_org_id as retailer_org_id, sum(agent_net_amt) as mrp_amt from st_dg_ret_sale_"+gameId+" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and date(rtm.transaction_date)>='"+startDate+"' and date(rtm.transaction_date)<='"+endDate+"' group by drs.retailer_org_id union all select drs.retailer_org_id as retailer_org_id,-sum(agent_net_amt) as mrp_amt from st_dg_ret_sale_refund_"+gameId+" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and date(transaction_date)>='"+startDate+"' and date(transaction_date)<='"+endDate+"' group by drs.retailer_org_id)s group by retailer_org_id)kb inner join st_lms_organization_master om on om.organization_id=kb.retailer_org_id group by parent_id)k inner join st_lms_organization_master om on om.organization_id=k.parent_id;";
						logger.info("Sale query for game "+gameName+" :"+query);
						rs=stmt.executeQuery(query);
						bean.setGameName(gameName);
						xmlFileCreation(bean,rs);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(stmt, rs);
		}
	}
	
	
	public static void saleConsolidatedXMLFilesCreation(String startDate, String endDate, Connection con) {
		Statement stmt = null;
		ResultSet rs = null;
		TallyXmlVariablesBean bean = null;
		Map<Integer, GameMasterLMSBean> lmsGameMap = null;
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder saleBuilder = new StringBuilder();
		StringBuilder pwtBuilder = new StringBuilder();
		try {
			bean = new TallyXmlVariablesBean();
			bean.setSaleConsolidatedBean();
			bean.setStartDate(startDate);
			bean.setEndDate(endDate);
			lmsGameMap = Util.getLmsGameMap();
			stmt = con.createStatement();
			Iterator<Map.Entry<Integer, GameMasterLMSBean>> gameMap = lmsGameMap.entrySet().iterator();
			queryBuilder = queryBuilder.append("select name, sum(amt) amt from (");
			saleBuilder = saleBuilder.append("select name, sum(amt) amt from (");
			pwtBuilder = pwtBuilder.append("select name, -sum(amt) amt from (");
			while (gameMap.hasNext()) {
				Map.Entry<Integer, GameMasterLMSBean> gameNumpair = gameMap.next();
				int gameId = gameNumpair.getKey();
				saleBuilder = saleBuilder.append("select name, net_amt as amt from (select parent_id,sum(net_amt) as net_amt from (select sum(net_amt) as net_amt,retailer_org_id  from (select drs.retailer_org_id as retailer_org_id,sum(agent_net_amt) as net_amt from st_dg_ret_sale_").append(gameId).append(" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and date(rtm.transaction_date)>='").append(startDate).append("' and date(rtm.transaction_date)<='").append(endDate).append("' group by drs.retailer_org_id union all select drs.retailer_org_id as retailer_org_id,-sum(agent_net_amt) as net_amt from st_dg_ret_sale_refund_").append(gameId).append(" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and date(transaction_date)>='").append(startDate).append("' and date(transaction_date)<='").append(endDate).append("' group by drs.retailer_org_id)s group by retailer_org_id)kb inner join st_lms_organization_master om on om.organization_id=kb.retailer_org_id group by parent_id)k inner join st_lms_organization_master om on om.organization_id=k.parent_id");
				saleBuilder.append(" union all ");
				
				pwtBuilder = pwtBuilder.append("select name,sum(pwt_amt) as amt from (select parent_id, sum(pwt_amt) as pwt_amt from (select drs.retailer_org_id, sum(pwt_amt + agt_claim_comm) as pwt_amt from st_dg_ret_pwt_").append(gameId).append(" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and date(transaction_date)>='").append(startDate).append("' and date(transaction_date)<='").append(endDate).append("' group by drs.retailer_org_id )kb inner join st_lms_organization_master om on om.organization_id=kb.retailer_org_id group by parent_id union all select agent_org_id, sum(net_amt) as pwt_amt from st_dg_agt_direct_plr_pwt  where date(transaction_date)>='").append(startDate).append("' and date(transaction_date)<='").append(endDate).append("' and game_id=").append(gameId).append(" group by agent_org_id)k inner join st_lms_organization_master om on om.organization_id=k.parent_id group by name");
				pwtBuilder.append(" union all ");
			}
			saleBuilder.append("select om.name name, sum(rep.sale_net - rep.ref_net_amt) amt from st_rep_dg_agent rep inner join st_lms_organization_master om on rep.organization_id = om.organization_id where finaldate >= '").append(startDate).append("' and finaldate <= '").append(endDate).append("' group by name");
			saleBuilder.append(" union all ");
			
			saleBuilder.append("select name,agent_net_amt as amt from (select parent_id,sum(agent_net_amt) as agent_net_amt from (select sum(agent_net_amt) as agent_net_amt,retailer_org_id  from (select drs.retailer_org_id as retailer_org_id,sum(agent_net_amt) as agent_net_amt from st_iw_ret_sale drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('IW_SALE') and date(rtm.transaction_date)>='").append(startDate).append("' and date(rtm.transaction_date)<='").append(endDate).append("' group by drs.retailer_org_id union all select drs.retailer_org_id as retailer_org_id,-sum(agent_net_amt) as agent_net_amt from st_iw_ret_sale_refund drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('IW_REFUND_CANCEL') and date(rtm.transaction_date)>='").append(startDate).append("' and date(rtm.transaction_date)<='").append(endDate).append("' group by drs.retailer_org_id)s group by retailer_org_id)kb inner join st_lms_organization_master om on om.organization_id=kb.retailer_org_id group by parent_id)k inner join st_lms_organization_master om on om.organization_id=k.parent_id");
			saleBuilder.append(" union all ");			
			
			saleBuilder.append("select om.name name, sum(rep.sale_net - rep.ref_net_amt) amt from st_rep_iw_agent rep inner join st_lms_organization_master om on rep.organization_id = om.organization_id where finaldate >= '").append(startDate).append("' and finaldate <= '").append(endDate).append("' group by name");
			
			saleBuilder.append(")main1 group by name");
			
			pwtBuilder.append("select om.name, sum(rep.pwt_net_amt) amt from st_rep_dg_agent rep inner join st_lms_organization_master om on rep.organization_id = om.organization_id where rep.pwt_net_amt > 0  and finaldate >= '").append(startDate).append("'  and finaldate <= '").append(endDate).append("' group by name");
			pwtBuilder.append(" union all ");
			
			pwtBuilder.append("select name,sum(pwt_amt) as amt from (select parent_id,sum(pwt_amt) as pwt_amt from (select drs.retailer_org_id,sum(agent_net_amt) as pwt_amt from st_iw_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('IW_PWT') and date(rtm.transaction_date)>='").append(startDate).append("' and date(rtm.transaction_date)<='").append(endDate).append("' group by drs.retailer_org_id )kb inner join st_lms_organization_master om on om.organization_id=kb.retailer_org_id group by parent_id union all select agent_org_id,sum(net_amt) as pwt_amt from st_iw_agent_direct_plr_pwt  where date(transaction_date)>='").append(startDate).append("' and date(transaction_date)<='").append(endDate).append("' group by agent_org_id)k inner join st_lms_organization_master om on om.organization_id=k.parent_id group by name");
			pwtBuilder.append(" union all ");
			
			pwtBuilder.append("select om.name, sum(rep.pwt_net_amt) amt from st_rep_iw_agent rep inner join st_lms_organization_master om on rep.organization_id = om.organization_id where rep.pwt_net_amt > 0  and finaldate >= '").append(startDate).append("'  and finaldate <= '").append(endDate).append("' group by name");
			pwtBuilder.append(" union all ");
			
			pwtBuilder.append("select om.name name, sum(rep.direct_pwt_net_amt) amt from st_rep_dg_agent rep inner join st_lms_organization_master om on rep.organization_id = om.organization_id where rep.direct_pwt_net_amt > 0  and finaldate >= '").append(startDate).append("' and finaldate <= '").append(endDate).append("' group by name");
			pwtBuilder.append(" union all ");
			
			pwtBuilder.append("select om.name name, sum(rep.direct_pwt_net_amt) amt from st_rep_iw_agent rep inner join st_lms_organization_master om on rep.organization_id = om.organization_id where rep.direct_pwt_net_amt > 0  and finaldate >= '").append(startDate).append("'  and finaldate <= '").append(endDate).append("' group by name");
			
			pwtBuilder.append(")main2 group by name");

			queryBuilder.append(saleBuilder).append(" union all ").append(pwtBuilder).append(") main group by name");
			System.out.println(queryBuilder.toString());
			rs = stmt.executeQuery(queryBuilder.toString());
			bean.setGameName("ALL");
			xmlFileCreation(bean, rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	private static void xmlFileCreation(TallyXmlVariablesBean variablesBean, ResultSet rs) {
		 
		String start=null;
		String end=null;
		double amt=0.0;
		String firstName = null;	
		String totalName=null;
		boolean flag=true;
		String narration=null;
		try{
			NumberFormat formatter = new DecimalFormat("#0.00");
			LinkedList<AllLedgerEntries> allLedgerList=new LinkedList<AllLedgerEntries>();
			start=Util.changeFormat("yyyy-MM-dd","dd.MM.yy",variablesBean.getStartDate());
			end=Util.changeFormat("yyyy-MM-dd","dd.MM.yy",variablesBean.getEndDate());
			
			if("Sale".equalsIgnoreCase(variablesBean.getXmlFileType()) && "TwelveByTwentyFour".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="Sales 12/24 Game";
				narration="Being sales done for the period: "+start+" to "+end+".";
			}else if("Sale".equalsIgnoreCase(variablesBean.getXmlFileType()) && "KenoFive".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="Sales-5/90-Ghana";
				narration="Being sales done for the period: "+start+" to "+end+".";
			}else if("Sale".equalsIgnoreCase(variablesBean.getXmlFileType()) && "KenoFour".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="Sales 5/90 INDOOR Game";
				narration="Being sales done for the period: "+start+" to "+end+".";
			}else if("Sale".equalsIgnoreCase(variablesBean.getXmlFileType()) && "KenoNine".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="Sales 6/36 Lagos Game";
				narration="Being sales done for the period: "+start+" to "+end+".";
			}else if("Pwt".equalsIgnoreCase(variablesBean.getXmlFileType()) && "TwelveByTwentyFour".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="PWT-12/24 POOL A/C";
				narration="Being payout amount paid  from prize payout float account for the period of: "+start+" to "+end+".";
			}else if("Pwt".equalsIgnoreCase(variablesBean.getXmlFileType()) && "KenoFive".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="PWT-5/90 Ghana POOL A/C";
				narration="Being payout amount paid  from prize payout float account for the period of: "+start+" to "+end+".";
			}else if("Pwt".equalsIgnoreCase(variablesBean.getXmlFileType()) && "KenoFour".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="PWT-5/90 Indoor POOL A/C";
				narration="Being payout amount paid  from prize payout float account for the period of: "+start+" to "+end+".";
			}else if("Pwt".equalsIgnoreCase(variablesBean.getXmlFileType()) && "KenoNine".equalsIgnoreCase(variablesBean.getGameName())){
				totalName="PWT-6/36 Lagos POOL A/C";
				narration="Being payout amount paid  from prize payout float account for the period of: "+start+" to "+end+".";
			}else if("Training".equalsIgnoreCase(variablesBean.getXmlFileType())){
				totalName="PA/SA Training Expenses-5/90 (W)";
				narration="Being provision made for weekly training expenses for period of "+start+" to "+end+".";
			} else if ("SaleConsolidated".equalsIgnoreCase(variablesBean.getXmlFileType()) && "ALL".equalsIgnoreCase(variablesBean.getGameName())) {
				totalName = "Sales - Pwt For All Games";
				narration = "Being sales done for the period: " + start + " to " + end + ".";
			}

			TallyXMLFilesBean bean = new TallyXMLFilesBean();
			TallyXMLFilesBean.Header header=new TallyXMLFilesBean.Header();
			bean.setHeader(header);
			TallyXMLFilesBean.Body body=new TallyXMLFilesBean.Body();
			TallyXMLFilesBean.Body.ImportData importData=new TallyXMLFilesBean.Body.ImportData();
			TallyXMLFilesBean.Body.ImportData.RequestDesc requestDesc=new TallyXMLFilesBean.Body.ImportData.RequestDesc();
			TallyXMLFilesBean.Body.ImportData.RequestDesc.StaticVariables staticVariables = new TallyXMLFilesBean.Body.ImportData.RequestDesc.StaticVariables();   
			requestDesc.setStaticVariables(staticVariables);
			importData.setRequestDesc(requestDesc);
			TallyXMLFilesBean.Body.ImportData.RequestData requestData=new TallyXMLFilesBean.Body.ImportData.RequestData();
			TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage tallyMessage=new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage();
			TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher voucher =new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher();
			
			//different for bank deposit
			voucher.setVchtype(variablesBean.getVoucherType());
			
			//toDate
			voucher.setDate(variablesBean.getEndDate().replace("-", ""));
			
			voucher.setNarration(narration);
			voucher.setVoucherTypeName(variablesBean.getVoucherType());
			
			//toDate
			voucher.setEffectiveDate(variablesBean.getEndDate().replace("-", ""));
			String regex = "^[0-9]*$";
			while(rs.next()){
				if(flag){
					String data = rs.getString("name");
					if(data.length()>8 && data.substring(data.length()-8, data.length()).matches(regex))
						data=data.substring(0,data.length()-8);
					
					firstName=data;
					flag=false;
				}
				amt+=rs.getDouble("amt");
				
				TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries allLedgerEntries = new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries();
				allLedgerEntries.setIsDeemedPositive(variablesBean.getIsDeemedForLedger());
				if("No".equalsIgnoreCase(variablesBean.getIsLedgerAmountPositive()))
					allLedgerEntries.setAmount(formatter.format(-1*(rs.getDouble("amt"))));
				else
					allLedgerEntries.setAmount(formatter.format(rs.getDouble("amt")));
				String data = rs.getString("name");
				if(data.length()>8 && data.substring(data.length()-8, data.length()).matches(regex))
					data=data.substring(0,data.length()-8);
				
				allLedgerEntries.setLedgerName(data);
				allLedgerEntries.setIsPartyLedger(variablesBean.getIsPartyForLedger());
				allLedgerList.add(allLedgerEntries);
			}
			
			TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries allLedgerEntries = new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries();
			if ("No".equalsIgnoreCase(variablesBean.getIsLedgerAmountPositive()))
				allLedgerEntries.setAmount(formatter.format(amt));
			else
				allLedgerEntries.setAmount(formatter.format(-1 * amt));
			allLedgerEntries.setLedgerName(totalName);
			allLedgerEntries.setIsDeemedPositive(variablesBean.getIsDeemedForTotal());
			allLedgerEntries.setIsPartyLedger(variablesBean.getIsPartyForTotal());
			
			if("Top".equalsIgnoreCase(variablesBean.getLocationOfTotal()))
				allLedgerList.addFirst(allLedgerEntries);
			else 
				allLedgerList.add(allLedgerEntries);
			
			voucher.setPartyLedgerName(firstName);
			voucher.setAllLedger(allLedgerList);
			tallyMessage.setVoucher(voucher);
			requestData.setTallyMessage(tallyMessage);
			importData.setRequestData(requestData);
			body.setImportData(importData);
			bean.setBody(body);
			jaxbObjectToXML(bean,variablesBean.getXmlFileType(),variablesBean.getEndDate().replace("-", ""),variablesBean.getGameName());
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void jaxbObjectToXML(Object emp, String type, String date, Object object) {
		try {
			JAXBContext context = null;
			context = JAXBContext.newInstance(TallyXMLFilesBean.class);

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(emp, System.out);

			File file = null;
			if (object == null)
				file = new File(type + date + ".xml");
			else
				file = new File(type + "-" + object + date + ".xml");

			m.marshal(emp, new FileOutputStream(file));

			String OS = System.getProperty("os.name").toLowerCase();
			String root = "/tmp";
			if (OS.indexOf("win") >= 0) {
				root = "D:\\";
			} else {
				root = "/home/stpl/";
			}

			File folder = new File(root + "upload/");
			if (!folder.exists() || !folder.isDirectory()) {
				folder.mkdir();
			}

			String fileName = null;
			if (object == null)
				fileName = type + date + ".xml";
			else
				fileName = type + "-" + object + date + ".xml";

			file = new File(folder, fileName);

			m.marshal(emp, new FileOutputStream(file));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}