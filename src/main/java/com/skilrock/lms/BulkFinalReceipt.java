package com.skilrock.lms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.common.db.DBConnect;

public class BulkFinalReceipt {

	public static void main(String[] args) {

		 
		Connection con = DBConnect.getConnection();

		new BulkFinalReceipt().updateTemp(con);

		/*
		 * String arr[] = tiket.split("','"); for(int i = 0; i<arr.length; i++) {
		 * 
		 * //System.out.println(); arr[i] = arr[i].trim();
		 * 
		 * String newtkt =
		 * arr[i].substring(0,3)+"-"+arr[i].substring(3,9)+"-"+arr[i].substring(9,12);
		 * 
		 * System.out.print("'"+newtkt+"',"); }
		 */
		System.out.println("ENd Main");

	}

	void updateTemp(Connection con) {

		try {
			Statement stmt = con.createStatement();
			Statement stmt1 = con.createStatement();
			// con.setAutoCommit(false);

			int gameId = 48;

			ResultSet rs = stmt
					.executeQuery("select tp.virn_code, final_receipt_num,"
							+ " bp.transaction_id , tm.receipt_id from st_lms_bo_receipts_trn_mapping"
							+ " tm, st_tmp_pwt_inv tp, st_se_bo_pwt bp where tp.game_id = "
							+ gameId
							+ " and"
							+ " tp.virn_code = bp.virn_code and tp.game_id = bp.game_id and"
							+ " bp.transaction_id = tm.transaction_id and final_receipt_num != tm.receipt_id "
							+ "order by receipt_id desc");

			while (rs.next()) {
				int finalReceiptOld = rs.getInt("final_receipt_num");
				int newReceipt = rs.getInt("receipt_id");
				String virnCode = rs.getString("virn_code");
				String updateQuery = "update st_tmp_pwt_inv set final_receipt_num = "
						+ newReceipt
						+ " where virn_code =  '"
						+ virnCode
						+ "' and game_id = "
						+ gameId
						+ " and final_receipt_num = " + finalReceiptOld;

				System.out.println(updateQuery);

				int row = stmt1.executeUpdate(updateQuery);
				System.out.println();

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
