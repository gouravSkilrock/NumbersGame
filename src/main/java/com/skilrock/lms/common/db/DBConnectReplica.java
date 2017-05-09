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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.utility.PropertyLoader;

public class DBConnectReplica {

	private static String databaseName;

	static Log logger = LogFactory.getLog(DBConnectReplica.class);
	private static DataSource ds = null;
	private static String userName = null;
	private static String hostAddress = null;
	private static String password = null;
	private static String url = null;
	static {
		try {
			//ds = (DataSource) new InitialContext().lookup("java:/MyDataSource");
		        PropertyLoader.loadProperties("RMS/LMS.properties");

			databaseName = PropertyLoader.getProperty("replicaDatabaseName");
			userName = PropertyLoader.getProperty("replicaUserName");
			hostAddress = PropertyLoader.getProperty("replicaHostAddress");
			password = PropertyLoader.getProperty("replicaPassword");
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
		DBConnectReplica.databaseName = databaseName;
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

	public static void closeCon(Connection con) {
		try {
			con.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
	}
}
