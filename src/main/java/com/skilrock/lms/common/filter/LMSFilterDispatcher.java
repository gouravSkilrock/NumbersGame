package com.skilrock.lms.common.filter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.FilterDispatcher;

import com.skilrock.lms.common.LSControllerImpl;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.GarbageCollector;
import com.skilrock.lms.common.utility.InitializeAfterDGServerStartUp;
import com.skilrock.lms.common.utility.InitializeAfterServerStartUp;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ServerStartUpData;
import com.skilrock.lms.web.drawGames.common.Util;

public class LMSFilterDispatcher extends FilterDispatcher {

	public static String claimByClick = "";

	// static final Logger logger = Logger.getLogger(LMSFilterDispatcher.class);

	// Log logger = LogFactory.getLog(AutoQuartzMainForMail.class);

	public static String currencySymbol = "";

	private static String HOST = "";

	private static String isDraw = "NO";

	private static String isCS = "NO";

	private static String isOLA = "NO";

	private static String isSE = "NO";

	private static String isSLE = "NO";

	public static String csProvider = "";

	public static String cs_isShowCircle = "NO";

	public static String isMailSend = "";

	private static String isScratch = "NO";

	private static String isIPE = "NO";
	
	private static String isIW = "NO";
	
	private static String isVS = "NO";

	public static Log logger = LogFactory.getLog(LMSFilterDispatcher.class);

	public static String loginMailAlert = "";

	public static String mailProjName = "";

	public static String mailSmtpIPAddress = "";

	public static String orgName = "";

	public static boolean isBarCodeRequired;
	public static boolean isByPassDatesRequired;

	private static String PORT = "";
	public static boolean isDGActiveAtRetWeb = false;
	public static String projectName = "";
	private static String servDelegateUrl = "";
	public static String seSaleReportType = "";
	public static String ipeSaleReportType = "";
	public static String webLink = "";
	public static boolean stopLogInUsers = false;
	public static boolean isMachineEnabled = false;
	public static boolean isOfflineFileApproval = false;
	public static boolean isRepFrmSP = false;
	public static String pmsWebLink = "";
	public static String rummyCashierWebLink = "";
	public static String orgFieldType =" ";
	public static String orgOrderBy=" ";
	public static int newUseridAdditionValue=0;
	public static boolean IS_POST_COMMISSION_SCHEDULED=false;
	public static String getHOST() {
		return HOST;
	}

	public static String getIsDraw() {
		return isDraw;
	}

	public static String getIsScratch() {
		return isScratch;
	}

	public static String getPORT() {
		return PORT;
	}

	public static String getServDelegateUrl() {
		return servDelegateUrl;
	}

	/*
	 * To load the driver
	 */
	public static void loadDriver(String databaseName, String password,
			String userName, String hostAddress) {

		try {

			ServletContext sc = ServletActionContext.getServletContext();

			// databaseName=(String)sc.getAttribute("DATABASE_NAME");
			// userName=(String)sc.getAttribute("DATABASE_USER_NAME");
			// password=(String)sc.getAttribute("DATABASE_PASSWORD");
			// hostAddress=(String)sc.getAttribute("DATABASE_HOST_ADDRESS");

			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://" + hostAddress + "/" + databaseName;

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
				// setupDriver(url,userName,password);
				DataSource ds = setupDataSource(url, userName, password);

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.debug("Done.");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method basically used to know status of the connection pool.
	 * 
	 * @throws Exception
	 */
	public static void printDriverStats() throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		ObjectPool connectionPool = driver.getConnectionPool("example");

		logger.debug("NumActive: " + connectionPool.getNumActive());
		logger.debug("NumIdle: " + connectionPool.getNumIdle());
	}

	public static DataSource setupDataSource(String connectURI, String user,
			String pass) {
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

		logger.debug("Number of Already Active Connection"
				+ connectionPool.getNumActive());
		logger.debug("Number of Already Idle Connection"
				+ connectionPool.getNumIdle());
		;
		//
		// Finally, we create the PoolingDriver itself,
		// passing in the object pool we created.
		//
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

		return dataSource;
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

	/**
	 * Shut down the loaded driver
	 * 
	 * @throws Exception
	 */
	public static void shutdownDriver() throws Exception {
		PoolingDriver driver = (PoolingDriver) DriverManager
				.getDriver("jdbc:apache:commons:dbcp:");
		driver.closePool("example");
	}

	String databaseName;
	String hostAddress;
	String password;
	String userName;

	public String getDatabaseName() {
		return databaseName;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public void init(FilterConfig filterConfig) throws ServletException {

		super.init(filterConfig);
	        PropertyLoader.loadProperties("RMS/LMS.properties");
		Map<String, String> tierMap = new HashMap<String, String>();
		ServletContext servletContext = getServletContext();
		LMSUtility.sc = servletContext;
		System.out.println(LMSUtility.sc
				+ "***********************************");
		Connection con = DBConnect.getConnection();
		String query = "select ref_merchant_id from st_lms_service_master where service_code='DG' and status='ACTIVE'";
		String ipeQuery = "select ref_merchant_id from st_lms_service_master where service_code='IPE' and status='ACTIVE'";
		String serQuery = "select service_display_name, service_code from st_lms_service_master where status='ACTIVE'";
		String tierQuery = "select tier_name,tier_code from st_lms_tier_master";
		String dgQuery = "select sm.service_id,sdm.status from st_lms_service_master sm,st_lms_service_delivery_master sdm,st_lms_tier_master tm where sm.service_id=sdm.service_id and sdm.tier_id=tm.tier_id and tier_code='RETAILER' and sm.service_code='DG' and interface='WEB'";
		String refMerchantId = "";
		String refMerchantId_IPE = "";

		Map<String, String> serviceCodeNameMap = new HashMap<String, String>();
		Map<String, Integer> serviceCodeIdMap = new HashMap<String, Integer>();
		ServerStartUpData.setServicesMaps(serviceCodeNameMap, serviceCodeIdMap);

		servletContext.setAttribute("SERVICES_CODE_NAME_MAP",
				serviceCodeNameMap);
		servletContext.setAttribute("SERVICES_CODE_ID_MAP", serviceCodeIdMap);

		try {
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(query);
			while (rs.next()) {
				refMerchantId = rs.getString("ref_merchant_id");
			}

			rs = pstmt.executeQuery(ipeQuery);
			while (rs.next()) {
				refMerchantId_IPE = rs.getString("ref_merchant_id");
			}

			rs = pstmt.executeQuery(serQuery);
			while (rs.next()) {
				String serviceType = rs.getString("service_display_name");
				String serviceCode = rs.getString("service_code");
				if ("SE".equalsIgnoreCase(serviceCode)) {
					isScratch = "YES";
				}
				if ("DG".equalsIgnoreCase(serviceCode)) {
					isDraw = "YES";
				}
				if ("CS".equalsIgnoreCase(serviceCode)) {

					isCS = "YES";
				}
				if ("OLA".equalsIgnoreCase(serviceCode)) {

					isOLA = "YES";
				}

				if ("SLE".equalsIgnoreCase(serviceCode)) {
					isSLE = "YES";
				}

				if ("IPE".equalsIgnoreCase(serviceCode)) {
					isIPE = "YES";
				}
				
				if("IW".equalsIgnoreCase(serviceCode)) {
					isIW = "YES";
				}

				if ("VS".equalsIgnoreCase(serviceCode)) {
					isVS = "YES";
				}
			}

			rs = pstmt.executeQuery(tierQuery);

			while (rs.next()) {
				tierMap.put(rs.getString("tier_code"), rs
						.getString("tier_name"));
			}

			rs = pstmt.executeQuery(dgQuery);
			while (rs.next()) {
				isDGActiveAtRetWeb = rs.getString("status").equals("ACTIVE") ? true
						: false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		servletContext.setAttribute("REF_MERCHANT_ID", refMerchantId);
		servletContext.setAttribute("REF_MERCHANT_ID_IPE", refMerchantId_IPE);

		servletContext.setAttribute("IS_DRAW", isDraw);
		servletContext.setAttribute("IS_SCRATCH", isScratch);
		servletContext.setAttribute("IS_CS", isCS);
		servletContext.setAttribute("IS_OLA", isOLA);
		servletContext.setAttribute("IS_IPE", isIPE);
		servletContext.setAttribute("IS_SLE", isSLE);
		servletContext.setAttribute("IS_IW", isIW);
		servletContext.setAttribute("IS_VS", isVS);

		System.out
				.println("--------Initializng custom filter displatcher filter---");

		servletContext.setAttribute("TIER_MAP", tierMap);

		servletContext.setAttribute("DATABASE_NAME", PropertyLoader
				.getProperty("databaseName"));
		servletContext.setAttribute("DATABASE_USER_NAME", PropertyLoader
				.getProperty("userName"));
		servletContext.setAttribute("DATABASE_HOST_ADDRESS", PropertyLoader
				.getProperty("hostAddress"));
		servletContext.setAttribute("DATABASE_PASSWORD", PropertyLoader
				.getProperty("password"));

		updateProperties();
		if ("YES".equalsIgnoreCase(isCS)) {
			csProvider = (String) servletContext.getAttribute("CS_PROVIDER");
			if ("PAYWORLD".equalsIgnoreCase(csProvider)) {
				String agtid = (String) servletContext
						.getAttribute("PW_MERCHANT_ID");
				String loginstatus = (String) servletContext
						.getAttribute("PW_MERCHANT_LOGIN_STATUS");
				String appver = (String) servletContext
						.getAttribute("PW_PAYWORLD_API_VERSION");
				/*
				 * try { PayWorldHelper.serviceDataScheduler(agtid, loginstatus,
				 * appver); } catch (LMSException e) { e.printStackTrace(); }
				 */
			}
		}

		// For setting the cached files details in the servlet context
		/*com.skilrock.lms.common.utility.CachedFilesDetails
				.getCachedFiles(servletContext);*/

		
		
/*  The below code(457-461) is used for setting game map on server start up.
 *  
 */
		
		
		if ("YES".equals(isDraw)) {
			InitializeAfterDGServerStartUp dgStartUp = new InitializeAfterDGServerStartUp(
					servletContext);
			dgStartUp.setDaemon(true);
			dgStartUp.start();
		}
		/*
		 * ServerStartUpData serverStartUpData=new ServerStartUpData();
		 * serverStartUpData .onStartGameData(servletContext);
		 * 
		 * // For setting thr organization data for draw games serverStartUpData
		 * .onStartOrganizationData();
		 * 
		 * // For setting advertisement messages. serverStartUpData
		 * .onStartAdvMessageData();
		 */

		// For Setting Priviledge and Report Map.
		com.skilrock.lms.web.reportsMgmt.common.ReportUtility.onStartPriviledgeReportMap();

		// set Wrapper Authentication data in authentication Map
		if("YES".equalsIgnoreCase((String) servletContext.getAttribute("IS_WRAPPER_ENABLED"))){		
		com.skilrock.lms.api.lmsWrapper.LmsWrapperServiceApiHelper.onStartWrapperData();
		}
		// Set IPE Game Data
		if ("YES".equals(isIPE)) {
			InitializeAfterServerStartUp gameMap = new InitializeAfterServerStartUp();
			gameMap.setDaemon(true);
			gameMap.start();
		}
		// run the garbage collector after 10 Minute periodically
		GarbageCollector coll = new GarbageCollector();
		coll.setDaemon(true);
		coll.start();

//		Licensing Server Validation
		LSControllerImpl.getInstance().clientValidation();

		// To load driver.
		/*
		 * logger.debug("Creating connection."); try {
		 * Class.forName("com.mysql.jdbc.Driver"); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); }
		 * 
		 * String url = "jdbc:mysql://"+hostAddress+"/"+databaseName; DataSource
		 * ds=setupDataSource(url,userName,password);
		 * servletContext.setAttribute("DATA_SOURCE",ds); //
		 * loadDriver(databaseName,password,userName,hostAddress);
		 */

	}

	public static void updateProperties() {
		String propertyFile = "select property_dev_name,value,editable from st_lms_property_master where status='ACTIVE'";
		Connection con = DBConnect.getConnection();
		ServletContext servletContext = LMSUtility.sc;
		Map<String, String> lmsPropertyMap = new HashMap<String, String>();
		try {
			Statement pstmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery(propertyFile);
			while (rs.next()) {
				servletContext.setAttribute(rs.getString("property_dev_name"),
						rs.getString("value"));
				lmsPropertyMap.put(rs.getString("property_dev_name"),
						rs.getString("value"));
			}
			Utility.setLmsPropertyMap(lmsPropertyMap);// set property map
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}

		if (((String) servletContext.getAttribute("ON_FREEZE_SALE"))
				.equalsIgnoreCase("NO")) {
			Util.onfreezeSale = false;
		}
		isMailSend = (String) servletContext.getAttribute("IS_MAIL_SEND");
		loginMailAlert = (String) servletContext
				.getAttribute("LOGIN_MAIL_ALERT");
		claimByClick = (String) servletContext.getAttribute("CLAIM_BY_CLICK");
		webLink = (String) servletContext.getAttribute("WEB_LINK");
		mailProjName = (String) servletContext.getAttribute("MAIL_PROJ_NAME");
		currencySymbol = (String) servletContext
				.getAttribute("CURRENCY_SYMBOL");
		mailSmtpIPAddress = (String) servletContext
				.getAttribute("MAIL_SMTP_HOST_IP");
		seSaleReportType = (String) servletContext
				.getAttribute("SE_SALE_REP_TYPE");
		ipeSaleReportType = (String) servletContext
				.getAttribute("IPE_SALE_REP_TYPE");
		servDelegateUrl = (String) servletContext
				.getAttribute("SERVICE_DELEGATE_URL");

		isMachineEnabled = "YES".equalsIgnoreCase((String) servletContext
				.getAttribute("IS_MACHINE_ENABLED"));

		PORT = (String) servletContext.getAttribute("PORT");
		HOST = (String) servletContext.getAttribute("HOST");
		isOfflineFileApproval = servletContext.getAttribute(
				"isOfflineFileApproval").toString().equals("true");
		isRepFrmSP = servletContext.getAttribute("isRepFrmSP").toString()
				.equals("true");
		newUseridAdditionValue=Integer.parseInt((String) servletContext.getAttribute("NEW_USERID_ADDITION_VALUE"));
		isBarCodeRequired  = "true".equalsIgnoreCase((String) servletContext.getAttribute("IS_BARCODE_REQUIRED"));
		isByPassDatesRequired="true".equalsIgnoreCase((String) servletContext.getAttribute("IS_BYPASSDATESFORPWT_REQUIRED"));
		if ("https".equalsIgnoreCase(HOST.trim())) {
			PORT = "";
		}
		servletContext.setAttribute("PORT", PORT);
		// new CreateSysInfo().validateCheckSum();
		logger.debug("HOST = " + HOST + "  port = " + PORT
				+ "  servDelegateUrl = " + servDelegateUrl);

		projectName = servletContext.getContextPath();
		pmsWebLink = (String) servletContext.getAttribute("PMS_WebLink");
		rummyCashierWebLink = (String) servletContext
				.getAttribute("RummyCashier_WebLink");

		orgFieldType = ((String) servletContext.getAttribute("ORG_LIST_TYPE"))
				.trim();
		orgOrderBy = ((String) servletContext.getAttribute("ORG_LIST_ORDER"))
				.trim();
		IS_POST_COMMISSION_SCHEDULED = "YES"
				.equalsIgnoreCase((String) servletContext
						.getAttribute("IS_POST_COMMISSION_SCHEDULED"));
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static String getIsCS() {
		return isCS;
	}

	public static void setIsCS(String isCS) {
		LMSFilterDispatcher.isCS = isCS;
	}

	public static String getIsOLA() {
		return isOLA;
	}

	public static void setIsOLA(String isOLA) {
		LMSFilterDispatcher.isOLA = isOLA;
	}

	public static String getIsIPE() {
		return isIPE;
	}

	public static void setIsIPE(String isIPE) {
		LMSFilterDispatcher.isIPE = isIPE;
	}

	public static String getIsSE() {
		return isSE;
	}

	public static void setIsSE(String isSE) {
		LMSFilterDispatcher.isSE = isSE;
	}

	public static String getIsSLE() {
		return isSLE;
	}

	public static void setIsSLE(String isSLE) {
		LMSFilterDispatcher.isSLE = isSLE;
	}
	
	public static String getIsIW() {
		return isIW;
	}

	public static void setIsIW(String isIW) {
		LMSFilterDispatcher.isIW = isIW;
	}

	public static String getIsVS() {
		return isVS;
	}

	public static void setIsVS(String isVS) {
		LMSFilterDispatcher.isVS = isVS;
	}
	
}
