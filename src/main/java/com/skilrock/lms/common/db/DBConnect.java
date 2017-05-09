/*
 * ? copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an ?AS IS?
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.common.db;


import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.utility.PropertyLoader;

public class DBConnect {

	private static String databaseName;

	static Log logger = LogFactory.getLog(DBConnect.class);
	private static DataSource ds = null;
	private static String userName = null;
	private static String hostAddress = null;
	private static String password = null;
	private static String url = null;
	static {
		try {
			//ds = (DataSource) new InitialContext().lookup("java:/MyDataSource");
			PropertyLoader.loadProperties("RMS/LMS.properties");

			databaseName = PropertyLoader.getProperty("databaseName");
			userName = PropertyLoader.getProperty("userName");
			hostAddress = PropertyLoader.getProperty("hostAddress");
			password = PropertyLoader.getProperty("password");
			url = "jdbc:mysql://" + hostAddress + "/" + databaseName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getDatabaseName() {
		return databaseName;
	}

	public static void setDatabaseName(String databaseName) {
		DBConnect.databaseName = databaseName;
	}

	public static Connection getConnection() {

		Connection connection = null;
		try {
			/*ds = null;
			if (ds != null) {

				connection = ds.getConnection();
				System.out
						.println("**********Connection Created by container****************************");

			} else {*/
				connection = getPropFileCon();
			//}
		} catch (Exception se) {
			 se.printStackTrace();
		}
		return connection;
	}

	public static Connection getPropFileCon() {

		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, userName, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return connection;
	}

/*	public static void closeCon(Connection con) {
		try {
			if(con != null)
			con.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
	}
*/	
	public static void closeCon(Connection con) {
		try {
			if (con == null || con.isClosed())
				logger.info("Connection Already Closed Or Empty");
			else
				con.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing Connection");
			ex.printStackTrace();
		}
	}

	public static void closePstmt(PreparedStatement pstm) {
		try {
			if (pstm == null)
				logger.info("PreparedStatement Already Closed Or Empty");
			else
				pstm.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing PreparedStatement");
			ex.printStackTrace();

		}
	}

	public static void closeStmt(Statement stmt) {
		try {
			if (stmt == null)
				logger.info("Statement Already Closed Or Empty");
			else
				stmt.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing Statement");
			ex.printStackTrace();
		}
	}

	public static void closeCstmt(CallableStatement cstmt) {
		try {
			if (cstmt == null)
				logger.info("Callable Statement Already Closed Or Empty");
			else
				cstmt.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing Callable Statement");
			ex.printStackTrace();
		}
	}

	public static void closeRs(ResultSet rs) {
		try {
			if (rs == null)
				logger.info("ResultSet Already Closed Or Empty");
			else
				rs.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing ResultSet");
			ex.printStackTrace();
		}
	}

	public static void closeConnection(Connection con, PreparedStatement pstm,
			Statement stmt, ResultSet rs) {
		closeRs(rs);
		closePstmt(pstm);
		closeStmt(stmt);
		closeCon(con);
	}

	public static void closeConnection(Connection con, PreparedStatement pstm,
			ResultSet rs) {
		closeRs(rs);
		closePstmt(pstm);
		closeCon(con);
	}

	public static void closeConnection(Connection con, Statement stmt,
			ResultSet rs) {
		closeRs(rs);
		closeStmt(stmt);
		closeCon(con);
	}
	public static void closeConnection(Connection con,PreparedStatement pstmt) {
		closePstmt(pstmt);
		closeCon(con);
	}
	
	public static void closeConnection(PreparedStatement pstmt ,Statement stmt ,ResultSet rs) {
		closeRs(rs);
		closePstmt(pstmt);
		closeStmt(stmt);
	}
	
	public static void closeConnection(PreparedStatement pstmt ,ResultSet rs) {
		closeRs(rs);
		closePstmt(pstmt);
	}

	public static void closeConnection(Statement stmt ,ResultSet rs) {
		closeRs(rs);
		closeStmt(stmt);
	}

	public static void closeResource(Object... resources) {
		for(Object resource : resources) {
			if(resource instanceof Connection)
				closeCon((Connection) resource);

			if(resource instanceof Statement)
				closeStmt((Statement) resource);

			if(resource instanceof PreparedStatement)
				closePstmt((PreparedStatement) resource);

			if(resource instanceof CallableStatement)
				closeCstmt((CallableStatement) resource);

			if(resource instanceof ResultSet)
				closeRs((ResultSet) resource);
		}
	}
}
