package com.skilrock.lms.coreEngine.scratchService.gameMgmt.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This helper class provides methods to calculate unclaimed pwt , Govt
 * Commission for the terminated game
 * 
 * @author Skilrock Technologies
 * 
 */
public class CalculateUnclaimedHelper {

	/**
	 * This method calculate unclaimed pwt , Govt Commission for the terminated
	 * game
	 * 
	 * @param gameid
	 *            is Id of game for which calculation is done
	 * @throws LMSException
	 */

	public void calculateUnclaimed(int gameid) throws LMSException {

		Connection con = null;
		try {
			 

			Statement stmt1 = null;
			Statement stmt2 = null;
			Statement stmt3 = null;
			Statement stmt4 = null;
			// Statement stmt5 = null;
			// Statement stmt6 = null;
			// Statement stmt7 = null;
			Statement stmt8 = null;

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			stmt4 = con.createStatement();
			// stmt5 = con.createStatement();
			// stmt6 = con.createStatement();
			// stmt7 = con.createStatement();
			stmt8 = con.createStatement();

			int gameId = gameid;
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			nf.setMinimumFractionDigits(2);

			// This code is blocked because concept of unclaimed calculation is
			// removed from the project

			/*
			 * String getMrpPWt = QueryManager.getST3PwtMrp() + " where
			 * game_id='" + gameId + "' and status='UNCLM_PWT' ";
			 * 
			 * rs = stmt1.executeQuery(getMrpPWt);
			 * 
			 * //rs=stmt1.executeQuery(" select SUM(pwt_amt) from st_pwt_inv
			 * where game_id='"+y+"' and status='UNCLM_PWT'");
			 * 
			 * while (rs.next()) {
			 * 
			 * amount = rs.getDouble(1); logger.debug("amount calculated " +
			 * amount); } // create task for unclaimed prize
			 * 
			 * String createTask = QueryManager.createST3Task() + " values(" +
			 * amount + "," + gameId + ",'UNCLM_PWT','APPROVED',CURRENT_DATE) ";
			 * stmt2.executeUpdate(createTask);
			 * 
			 */
			// stmt2.executeUpdate("insert into
			// st_lms_bo_tasks(amount,game_id,transaction_type,status)
			// values("+x+","+y+",'UNCLM_PWT','APPROVED')");
			// stmt3.executeQuery("select govt_comm_rate from st_se_game_master
			// where game_id='"+y+"'");
			// get govt comm rate,comm type,fixed amount from game_master
			// this code is commented because fovt comm is calculated separatly
			// by Arun on approve govt commission click
			/*
			 * String query = QueryManager.getST3GovtCommRate() + " where
			 * game_id = " + gameId + " "; rs1 = stmt3.executeQuery(query);
			 * 
			 * while (rs1.next()) {
			 * 
			 * govtRate = rs1.getDouble(TableConstants.GOVT_COMM_RATE);
			 * govtCommType = rs1.getString(TableConstants.GOVT_COMM_TYPE);
			 * minAssuredProfit = rs1.getDouble(TableConstants.FIXED_AMT);
			 * //govtRate=rs1.getDouble(TableConstants.GOVT_COMM_RATE);
			 * logger.debug("govt rate is " + govtRate); logger.debug("govt comm
			 * type is " + govtCommType); logger.debug("fixed amount is " +
			 * minAssuredProfit); }
			 * 
			 * 
			 * String query1 = QueryManager.getST3MrpForGovtComm() + " where
			 * game_id = " + gameId + " and transaction_type='SALE' "; String
			 * query2 = QueryManager.getST3MrpForGovtComm() + " where game_id = " +
			 * gameId + " and transaction_type='SALE_RET' "; rs2 =
			 * stmt4.executeQuery(query1); rs3 = stmt8.executeQuery(query2);
			 * while (rs2.next()) { mrpAmtSale = rs2.getDouble(1);
			 * logger.debug("mrp from bo_agent is " + mrpAmtSale); } while
			 * (rs3.next()) { mrpAmtSaleRet = rs3.getDouble(1);
			 * logger.debug("mrp from bo_agent is " + mrpAmtSaleRet); } mrpAmt =
			 * mrpAmtSale - mrpAmtSaleRet; logger.debug("net mrp is " + mrpAmt);
			 * govtShare = (Math.round(((mrpAmt * govtRate) / 100) * 100)) /
			 * 100.0;
			 * 
			 * if (govtCommType.equals("FIXED_PER") && govtCommType != null) {
			 * logger.debug("inside fixed percentage of sale");
			 * //govtShare=(Math.round(((mrpAmt*govtRate)/100)*100))/100.0;
			 * //govtShare=Double.parseDouble(nf.format(
			 * (mrpAmt*govtRate)/100)); logger.debug("govt share is " +
			 * govtShare); } else if (govtCommType.equals("MIN_PROFIT") &&
			 * govtCommType != null) { logger.debug("inside minimun assured
			 * profit"); if (minAssuredProfit > govtShare) govtShare =
			 * minAssuredProfit; } else if (govtCommType.equals("MAP_FP") &&
			 * govtCommType != null) { logger.debug("inside map + fp ");
			 * govtShare = minAssuredProfit + govtShare; } else govtShare = 0.0;
			 * //Get MRP rate From ageent_transaction
			 * 
			 * String createTaskforgov = QueryManager.createST3Task() + "
			 * values(" + govtShare + "," + gameId +
			 * ",'GOVT_COMM','APPROVED',CURRENT_DATE) ";
			 * 
			 * stmt2.executeUpdate(createTaskforgov);
			 * 
			 */

			// stmt5.executeUpdate("insert into
			// st_lms_bo_tasks(amount,game_id,transaction_type,status)
			// values("+govtShare+","+y+",'GOVT_COMM','APPROVED')");
			// update unclaimed to submitted to govt..
			/*
			 * String updatePwtinv = QueryManager.updateST3PwtInv() + " where
			 * game_id=" + gameId + " and status='UNCLM_PWT'";
			 * stmt2.executeUpdate(updatePwtinv);
			 */
			// stmt6.executeUpdate("update st_pwt_inv set status='SUB_GOV' where
			// game_id="+y+"");
			String updateQueryManager = QueryManager.updateST3QueryManager()
					+ " where game_id=" + gameId + " ";
			stmt2.executeUpdate(updateQueryManager);
			// stmt7.executeUpdate("update st_se_game_master set
			// game_status='TERMINATE' where game_id="+y+"");

			con.commit();
			con.close();
		} catch (SQLException e) {

			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException(e);

			}
		}

	}

}
