package com.skilrock.lms.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class PoolledDBConnection {
	static Log logger = LogFactory.getLog(PoolledDBConnection.class);

	public static void main(String[] args) {
		// "jdbc:oracle:thin:scott/tiger@myhost:1521:mysid"
		// "SELECT * FROM DUAL"

		String url = "jdbc:mysql://192.168.123.108/lms080303";

		// First we load the underlying JDBC driver.
		// You need this if you don't use the jdbc.drivers
		// system property.
		//
		logger.debug("Loading underlying JDBC driver.");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		logger.debug("Done.");

		//
		// Then we set up and register the PoolingDriver.
		// Normally this would be handled auto-magically by
		// an external configuration, but in this example we'll
		// do it manually.
		//
		logger.debug("Setting up driver.");
		try {
			setupDriver(url, "root", "ibm@123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Done.");

		//
		// Now, we can use JDBC as we normally would.
		// Using the connect string
		// jdbc:apache:commons:dbcp:example
		// The general form being:
		// jdbc:apache:commons:dbcp:<name-of-pool>
		//

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		try {
			logger.debug("Creating connection.");
			conn = DriverManager
					.getConnection("jdbc:apache:commons:dbcp:example");
			logger.debug("Creating statement.");
			stmt = conn.createStatement();
			logger.debug("Executing statement.");
			rset = stmt.executeQuery("select * from st_se_game_master");
			logger.debug("Results:");
			int numcols = rset.getMetaData().getColumnCount();
			while (rset.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print("\t" + rset.getString(i));
				}
				logger.debug("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}

		// Display some pool statistics
		try {
			printDriverStats();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// closes the pool
		try {
			shutdownDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printDriverStats() throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		ObjectPool connectionPool = driver.getConnectionPool("example");

		logger.debug("NumActive: " + connectionPool.getNumActive());
		logger.debug("NumIdle: " + connectionPool.getNumIdle());
	}

	public static void setupDriver(String connectURI, String user, String pass)
			throws Exception {
		//
		// First, we'll need a ObjectPool that serves as the
		// actual pool of connections.
		//
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
		ObjectPool connectionPool = new GenericObjectPool(null);

		//
		// Next, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string passed in the command line
		// arguments.
		//

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				connectURI, user, pass);

		//
		// Now we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);

		//
		// Finally, we create the PoolingDriver itself...
		//
		Class.forName("org.apache.commons.dbcp.PoolingDriver");
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");

		//
		// ...and register our pool with it.
		//
		driver.registerPool("example", connectionPool);

		//
		// Now we can just use the connect string
		// "jdbc:apache:commons:dbcp:example"
		// to access our pool of Connections.
		//
	}

	public static void shutdownDriver() throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		driver.closePool("example");
	}
}
