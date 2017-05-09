package com.skilrock.lms.coreEngine.scratchService.gameMgmt.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This class provides methods to extend PWT End Date and Sale End Date for the
 * game
 * 
 * @author Skilrock Technologies
 * 
 */
public class ExtendPwtDateHelper {
	Log logger = LogFactory.getLog(ExtendPwtDateHelper.class);

	/**
	 * This method is used for extend PWT End Date
	 * 
	 * @param gameid
	 *            is Id of the game
	 * @param date
	 *            is Extended date
	 * @throws LMSException
	 */
	public void extendDate(int gameid, String date) throws LMSException {

		String Date = date;

		Connection con = null;
		try {
			 

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt1 = null;
			stmt1 = con.createStatement();

			int gameId = gameid;

			String updateStatus = QueryManager.updateST3GameStatus()
					+ "  pwt_end_date ='" + Date + "'where game_id = " + gameId
					+ "   ";
			stmt1.executeUpdate(updateStatus);
			// stmt1.executeUpdate("update st_se_game_master set
			// game_status='OPEN', pwt_end_date ='"+Date+"'where game_id =
			// "+gameId+"");
			// addActionError("New Pwt End Date IS:: " + getPwtEndDate());

			con.commit();

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
			}
		}

	}

	/**
	 * This method is used to extend Sale End Date and Pwt End Date for the game
	 * 
	 * @param gameid
	 *            id of the Game
	 * @param date
	 *            extended Sale End Date
	 * @param pwtdate
	 *            extended PWT End Date
	 * @param pwtDateDb
	 *            current PWT End Date
	 * @param saleDateDb
	 *            current Sale End Date
	 * @param presentdate
	 *            current date from system
	 * @return String
	 * @throws LMSException
	 */

	// all the update queries in thios method are written in helper classes at
	// the time of self review
	public String extendSaleDate(int gameid, String date, String pwtdate,
			String pwtDateDb, String saleDateDb, String presentdate)
			throws LMSException {

		String presentDate = presentdate;
		String Date = date;
		String pwtDate = pwtdate;
		String pwtDateDB = pwtDateDb;
		String saleDateDB = saleDateDb;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date pwtDt = null;
		Date saleDt = null;
		Date pwtDtDB = null;
		Date saleDtDB = null;
		Date presentDt = null;

		try {
			if (!pwtDate.equals("")) {
				pwtDt = dateFormat.parse(pwtDate);
			}
			if (!Date.equals("")) {
				saleDt = dateFormat.parse(Date);
			}
			pwtDtDB = dateFormat.parse(pwtDateDB);
			saleDtDB = dateFormat.parse(saleDateDB);
			presentDt = dateFormat.parse(presentDate);
			logger.debug("present date is  " + presentDt);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection con = null;
		try {
			 

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt1 = null;
			stmt1 = con.createStatement();

			int gameId = gameid;

			if (saleDt.equals(saleDtDB) && !pwtDt.equals(pwtDtDB)) {
				if (pwtDt.after(presentDt)) {
					System.out
							.println("only pwt to be extended  set status to sale_hold");
					// stmt1.executeUpdate("update st_se_game_master set
					// pwt_end_date='"+pwtDate+"' where game_id = "+gameId+"") ;
					stmt1.executeUpdate(QueryManager.extendPwtEndDate() + "'"
							+ pwtDate + "' where game_id = " + gameId + "");
					con.commit();
					con.close();
					return "SUCCESS";
					// alert("only pwt to be extended set status to slae_hold");
				} else {
					logger.debug("Enter valid pwt greater then current");
					// alert("Enter valid pwt greater then current");
					return "ERROR";
				}
			}

			if (pwtDt.equals(pwtDtDB) && !saleDt.equals(saleDtDB))

			{

				if (saleDt.after(presentDt) && saleDt.before(pwtDt)
						&& pwtDt.after(presentDt)) {

					System.out
							.println("only sale date to be extend set status to open");
					// stmt1.executeUpdate("update st_se_game_master set
					// game_status='OPEN', sale_end_date ='"+Date+"'where
					// game_id = "+gameId+"");
					stmt1.executeUpdate(QueryManager.extendSaleEndDate() + "'"
							+ Date + "'where game_id = " + gameId + "");
					con.commit();
					con.close();
					// alert("only sale date to be extend set status to open");
					return "SUCCESS";
				} else {

					System.out
							.println("Enter valid Sale date greater then current or less then pwt");
					// alert("Enter valid Sale date greater then current or less
					// then pwt");
					return "ERROR";
				}
			}

			if (!pwtDt.equals(pwtDtDB) && !saleDt.equals(saleDtDB)) {
				if (saleDt.after(presentDt) && pwtDt.after(presentDt)) {

					if (saleDt.after(pwtDt)) {

						System.out
								.println("sale can not be greater then pwt  here pwt< sed");
						// alert("sale can not be greater then pwt here pwt<
						// sed");
						return "ERROR";
					} else {
						System.out
								.println("valid sale  and pwt date and sed < pwt  set status to open ");

						// stmt1.executeUpdate("update st_se_game_master set
						// game_status='OPEN', pwt_end_date
						// ='"+pwtDate+"',sale_end_date ='"+Date+"' where
						// game_id = "+gameId+"");

						stmt1.executeUpdate(QueryManager.extendSalePwtEndDate()
								+ "'" + pwtDate + "',sale_end_date ='" + Date
								+ "' where game_id = " + gameId + "");
						// alert("valid sale and pwt date and sed < pwt set
						// status to open ");
						con.commit();
						con.close();
						return "SUCCESS";
					}
				} else {
					System.out
							.println("please check for the valid dates  should be greater then current");
					// alert("please check for the valid dates should be greater
					// then current");
					return "ERROR";
				}

			}

			/*
			 * if(pwtDate.equals("") && date.equals("")) { return "ERROR"; }
			 * 
			 * if(pwtDate.equals("")) { if(saleDt.before(pwtDtDB) &&
			 * saleDt.after(presentDt)) { logger.debug("pwt date is null");
			 * String updateStatus= QueryManager.updateST3SaleDateStatus() + "
			 * sale_end_date ='"+Date+"' where game_id = "+gameId+" " ;
			 * stmt1.executeUpdate(updateStatus); // stmt1.executeUpdate("update
			 * st_se_game_master set game_status='OPEN', pwt_end_date
			 * ='"+Date+"'where game_id = "+gameId+""); //addActionError("New
			 * Pwt End Date IS:: " + getPwtEndDate()); con.commit(); return
			 * "SUCCESS"; }
			 * 
			 * return "ERROR1"; } else if(date.equals("") ) {
			 * if(pwtDt.after(presentDt)) { logger.debug("sale date is null");
			 * stmt1.executeUpdate("update st_se_game_master set
			 * pwt_end_date='"+pwtDate+"' where game_id = "+gameId+"") ;
			 * con.commit(); return "SUCCESS"; } else return "ERROR1"; } else
			 * 
			 * if(pwtDt.after(saleDt)) {
			 * 
			 * if(saleDt.equals(saleDtDB)&& pwtDt.equals(pwtDtDB)) { return
			 * "NOCHANGE"; }
			 * 
			 * if(pwtDt.after(presentDt) && saleDt.before(presentDt)) {
			 * logger.debug("inside when sale is less then current or pwt is
			 * greater thhen current"); stmt1.executeUpdate("update
			 * st_se_game_master set
			 * sale_end_date='"+Date+"',pwt_end_date='"+pwtDate+"' where game_id =
			 * "+gameId+"") ; con.commit(); return "SUCCESS"; }
			 * 
			 * if(pwtDt.after(presentDt) && saleDt.after(presentDt)) {
			 * logger.debug("none is null"); String updateStatus=
			 * QueryManager.updateST3SalePwtStatus() + " sale_end_date
			 * ='"+Date+"',pwt_end_date='"+pwtDate+"' where game_id = "+gameId+" " ;
			 * stmt1.executeUpdate(updateStatus); // stmt1.executeUpdate("update
			 * st_se_game_master set game_status='OPEN', pwt_end_date
			 * ='"+Date+"'where game_id = "+gameId+""); //addActionError("New
			 * Pwt End Date IS:: " + getPwtEndDate());
			 * 
			 * con.commit(); return "SUCCESS"; } else return "ERROR1"; }
			 * 
			 * 
			 */
			return "ERROR1";

		} catch (SQLException e) {

			try {
				con.rollback();
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);
			// return null;
		}

	}

}
