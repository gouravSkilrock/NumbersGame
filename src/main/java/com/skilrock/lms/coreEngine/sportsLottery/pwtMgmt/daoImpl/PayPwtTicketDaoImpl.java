package com.skilrock.lms.coreEngine.sportsLottery.pwtMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketDrawDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryPayPwtBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.BoardTicketDataBean;


public class PayPwtTicketDaoImpl {

	public static synchronized Map<Integer, Map<Integer, String>> sportsLotteryPayPwtTicketDaoImpl(PwtVerifyTicketBean pwtVerifyTicketBean,SportsLotteryPayPwtBean payPwtTicketBean,UserInfoBean userBean,Connection con) throws SLEException{
		 PreparedStatement insertPstmt=null;
		 ResultSet insertRs=null;
		 long transId=0;
		 int pwtInvId=0;
		PreparedStatement insertDraw=null;
		PreparedStatement insertTicket=null;
		ResultSet ticketRs=null;
		Timestamp transactionTime=null;
		Map<Integer, Map<Integer, String>> refDrawTransmap=null;
		Map<Integer, String> boardTransmap=null;
		try{
			// insert in main transaction table
			
			int gameId=pwtVerifyTicketBean.getGameId();
			int gameTypeId=pwtVerifyTicketBean.getGameTypeId();
			refDrawTransmap=new HashMap<Integer, Map<Integer,String>>();
			for(int i=0;i<pwtVerifyTicketBean.getVerifyTicketDrawDataBeanArray().length;i++){
				PwtVerifyTicketDrawDataBean pwtVerifyTicketDrawBean=pwtVerifyTicketBean.getVerifyTicketDrawDataBeanArray()[i];
				int drawId=pwtVerifyTicketDrawBean.getDrawId();
				boardTransmap=new HashMap<Integer, String>();
				for(int j=0;j<pwtVerifyTicketDrawBean.getBoardTicketBeanArray().length;j++){
					BoardTicketDataBean boardTicketBean=pwtVerifyTicketDrawBean.getBoardTicketBeanArray()[j];
					
					transactionTime=Util.getCurrentTimeStamp();

					insertPstmt = con.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
					insertPstmt.setString(1, userBean.getUserType());
					insertPstmt.setString(2, payPwtTicketBean.getServiceCode());
					insertPstmt.setString(3, payPwtTicketBean.getInterfaceType());
					insertPstmt.executeUpdate();
					        
					insertRs = insertPstmt.getGeneratedKeys();
							if (insertRs.next()) {
								transId = insertRs.getLong(1);
								
								// insert into retailer transaction master
								insertPstmt = con
											.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
								insertPstmt.setLong(1, transId);
								insertPstmt.setInt(2, userBean.getUserId()); 
								insertPstmt.setInt(3, userBean.getUserOrgId());
								insertPstmt.setInt(4,gameId);
								insertPstmt.setTimestamp(5,transactionTime);
								insertPstmt.setString(6, "SE_PWT");
								insertPstmt.executeUpdate();
					
									
								double pwtAmt=boardTicketBean.getWinningAmt();
								double retCommAmt=pwtAmt*SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getRetailetPwtComm()*.01;
								double retNetAmt=pwtAmt-retCommAmt;
								
								double agtCommAmt=pwtAmt*SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getAgentPwtComm()*.01;
								double agentNetAmt=pwtAmt-agtCommAmt;
								
								boolean isValid=false;
								insertTicket=con.prepareStatement("insert into st_sle_pwt_inv(ticket_nbr,game_id,game_type_id,draw_id,board_id,pwt_amt,status,is_direct_plr)values(?,?,?,?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
								insertTicket.setLong(1,pwtVerifyTicketBean.getTicketNumber());
								insertTicket.setInt(2,gameId);
								insertTicket.setInt(3, gameTypeId);
								insertTicket.setInt(4, drawId);
								insertTicket.setInt(5,boardTicketBean.getBoardId());
								insertTicket.setDouble(6,  CommonMethods.fmtToTwoDecimal(pwtAmt));
								insertTicket.setString(7, "CLAIM_AT_RETAILER");
								insertTicket.setString(8,payPwtTicketBean.getDirectPlrPwt());
								insertTicket.executeUpdate();
								
								ticketRs = insertTicket.getGeneratedKeys();
								if (ticketRs.next()) {
									pwtInvId = ticketRs.getInt(1);
									
								insertDraw = con
										.prepareStatement("insert into st_sle_ret_pwt_?(transaction_id,pwt_inv_id,game_id,game_type_id,retailer_org_id,retailer_user_id,pwt_amt,retailer_claim_comm,retailer_net_amt,agt_claim_comm,agent_net_amt,transaction_date,status)values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
								
								insertDraw.setInt(1, gameId);
								insertDraw.setLong(2,transId);
								insertDraw.setInt(3,pwtInvId);
								insertDraw.setInt(4, gameId);
								insertDraw.setInt(5,gameTypeId);
								insertDraw.setInt(6, userBean.getUserOrgId());
								insertDraw.setInt(7, userBean.getUserId());
								insertDraw.setDouble(8, CommonMethods.fmtToTwoDecimal(pwtAmt));
								insertDraw.setDouble(9, CommonMethods.fmtToTwoDecimal(retCommAmt));
								insertDraw.setDouble(10, CommonMethods.fmtToTwoDecimal(retNetAmt));
								insertDraw.setDouble(11, CommonMethods.fmtToTwoDecimal(agtCommAmt));
								insertDraw.setDouble(12, CommonMethods.fmtToTwoDecimal(agentNetAmt));
								insertDraw.setTimestamp(13,transactionTime);
								insertDraw.setString(14, "CLAIM_BAL");
								insertDraw.executeUpdate();
				}
				
								isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNetAmt, "CLAIM_BAL", "DEBIT", userBean
										.getUserOrgId(), userBean
										.getParentOrgId(), "RETAILER", 0, con);
								if(!isValid){
									throw new SLEException(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);

								}
								isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "DEBIT", userBean
										.getParentOrgId(),0, "AGENT", 0, con);
								if(!isValid){
									throw new SLEException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE,SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);

								}		
				
			}else{
						throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
							boardTransmap.put(boardTicketBean.getBoardId(),String.valueOf(transId));
		}
				refDrawTransmap.put(drawId, boardTransmap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);

		}
		
		return refDrawTransmap;
		
	}

}
