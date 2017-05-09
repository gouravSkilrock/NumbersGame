package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;

public class CashDenominationExchangeHelper {
	
	public void saveExchangeMoneyData(String[] iMultiples,String[] eMultiples,String[] denoArray,UserInfoBean userBean,String[] iDenoType)
	{
		Connection con = null;
		PreparedStatement ipstmt = null;
		PreparedStatement epstmt = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			Statement stmt1 = con.createStatement();
			ResultSet rs = stmt1.executeQuery("select cashier_id,drawer_id,denomination,nbrOfNotes from st_lms_bo_cash_drawer_status  where cashier_id="+userBean.getUserId()+"");
			while(rs.next())
			{
					PreparedStatement pstmtHistory = con.prepareStatement("insert into st_lms_bo_cash_drawer_status_history(cashier_id, drawer_id, denomination, nbrOfNotes, date, update_mode, doneBy)values(?, ?, ?, ?, ?, ?, ?)");
					pstmtHistory.setInt(1, rs.getInt("cashier_id"));
					pstmtHistory.setInt(2, rs.getInt("drawer_id"));
					pstmtHistory.setString(3,rs.getString("denomination"));
					pstmtHistory.setInt(4, rs.getInt("nbrOfNotes"));
					pstmtHistory.setTimestamp(5, new Timestamp(new Date().getTime()));
					pstmtHistory.setString(6,"CHANGE");
					pstmtHistory.setInt(7, userBean.getUserId());
					pstmtHistory.executeUpdate();
			}
			if (iMultiples != null) {
				for (int i = 0; i < iDenoType.length; i++) {
					if (!iMultiples[i].equalsIgnoreCase("")
							&& !iMultiples[i].equalsIgnoreCase("0")) {
						ipstmt = con
								.prepareStatement("update st_lms_bo_cash_drawer_status set nbrOfNotes=nbrOfNotes-"
										+ iMultiples[i]
										+ " where cashier_id=? and denomination=?");
						ipstmt.setInt(1, userBean.getUserId());
						ipstmt.setString(2, iDenoType[i].trim());
						ipstmt.executeUpdate();
					}
				}
			}
			if (eMultiples != null) {
				for (int i = 0; i < denoArray.length; i++) {
					if (!eMultiples[i].equalsIgnoreCase("")
							&& !eMultiples[i].equalsIgnoreCase("0")) {
						epstmt = con
								.prepareStatement("update st_lms_bo_cash_drawer_status set nbrOfNotes=nbrOfNotes+"
										+ eMultiples[i]
										+ " where cashier_id=? and denomination=?");
						epstmt.setInt(1, userBean.getUserId());
						epstmt.setString(2, denoArray[i]);
						epstmt.executeUpdate();
					}
				}
			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
