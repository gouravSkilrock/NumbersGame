package com.skilrock.lms.web.reportsMgmt.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

/**
 * This class is for Generating the Reports in Pdf format using Jasper Reports
 * 
 * @author Skilrock Technologies
 * 
 */
public class GraphReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	String file = null;
	private String fromDate;
	private String gameStatus = null;
	GraphReportHelper graphHelper = new GraphReportHelper();
	Log logger = LogFactory.getLog(GraphReportAction.class);
	byte[] path = null;
	String query = null;
	private String reportType = "";
	HttpServletRequest request;
	HttpServletResponse response;
	private List selectedGames;

	private List selectGames;
	private String selectMonth = null;
	private String selectYear = null;
	String singleGame = null;
	private String status = null;
	private String toDate;
	private String reportData;
	private String reportName;
	/**
	 * This method is for Generating the PDF Reports
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String createReport() throws Exception {
		reportType = getReportType();
		HttpSession session = getRequest().getSession();
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formatString = (String) session.getAttribute("date_format");
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
		Date sqlfmtFromDate = null;
		Date sqlfmtToDate = null;
		Timestamp fmtFromDate = null;
		Timestamp fmtToDate = null;
		Calendar calendar = Calendar.getInstance();
		HashMap parameterMap = new HashMap();
		UserInfoBean userBean = null;
		String orgName = null;
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		orgName = userBean.getOrgName();
		logger.debug("orgName" + orgName);
		System.out.println("{{{{{{{{{{{{{{{{{{{{{  " + orgName);
		parameterMap.put("orgName", orgName);
		// calendar.set(arg0, arg1, arg2);

		// System.out.println("hello");
		logger.debug("SelectGames " + getSelectGames() + "  Selected Games "
				+ getSelectedGames() + "Report Type" + getReportType()
				+ "Single Game" + getSingleGame());
		logger.debug("Selected Month " + getSelectMonth() + "From Date"
				+ getFromDate() + "TO Date" + getToDate() + "Year "
				+ getSelectYear());
		// System.out.println("SelectGames "+getSelectGames()+" Selected Games
		// "+getSelectedGames()+"Report Type"+getReportType()+"Single
		// Game"+getSingleGame());
		// System.out.println("Selected Month "+getSelectMonth()+"From
		// Date"+getFromDate()+"TO Date"+getToDate()+"Year "+getSelectYear());
		if (getSelectedGames() != null) {
			selectedGames = new ArrayList(getSelectedGames());
			logger.debug("Size : " + selectedGames.size());
			// System.out.println("Size : "+selectedGames.size());
		}
		if (!getSelectMonth().equals("--Please Select--")) {
			// System.out.println("<<<<<<<<<<<<<<<<<<");
			String day = "";
			// try{
			/*
			 * if(getSelectMonth().equals("01")||getSelectMonth().equals("03")||getSelectMonth().equals("05")||getSelectMonth().equals("07")||getSelectMonth().equals("08")||getSelectMonth().equals("10")||getSelectMonth().equals("12")){
			 * day = "31"; } else
			 * if(getSelectMonth().equals("04")||getSelectMonth().equals("06")||getSelectMonth().equals("09")||getSelectMonth().equals("11")){
			 * day = "30"; } else if(getSelectMonth().equals("02")){ day = "28"; }
			 */

			// System.out.println("dateFormat "+dateFormat.toPattern()+"
			// pppppp");
			Calendar calendar2 = Calendar.getInstance();

			calendar2.set(Integer.parseInt(getSelectYear()), Integer
					.parseInt(getSelectMonth()) - 1, Integer.parseInt("1"), 0,
					0, 0);
			sqlfmtFromDate = calendar2.getTime();

			calendar2.add(Calendar.MONTH, 1);
			sqlfmtToDate = calendar2.getTime();
			/*
			 * int dayNo=calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
			 * 
			 * 
			 * 
			 * sqlfmtToDate =
			 * dateFormat.parse("1"+"-"+getSelectMonth()+"-"+getSelectYear() );
			 * sqlfmtFromDate =
			 * dateFormat.parse("1"+"-"+getSelectMonth()+"-"+getSelectYear() );
			 */
			fmtFromDate = new Timestamp(sqlfmtFromDate.getTime());
			fmtToDate = new Timestamp(sqlfmtToDate.getTime());
			logger.debug(sqlfmtToDate + "    sqlfmtFromDate ??????????? "
					+ sqlfmtFromDate);
			logger.debug(fmtToDate + "    fmtFromDate ??????????? "
					+ fmtFromDate);
			System.out.println(sqlfmtToDate + "    sqlfmtFromDate ??????????? "
					+ sqlfmtFromDate);
			System.out.println(fmtToDate + "    fmtFromDate ??????????? "
					+ fmtFromDate);
			/*
			 * } catch (ParseException e){ System.out.println("ParseException " ); }
			 */

		} else {
			try {

				if (!getToDate().equals("")) {
					sqlfmtToDate = dateFormat.parse(getToDate());
					calendar.setTime(sqlfmtToDate);
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					sqlfmtToDate = calendar.getTime();
				} else {
					// Date date = new Date();
					// sqlfmtToDate= new java.sql.Date(date.getTime());
					Calendar calendar2 = Calendar.getInstance();

					calendar.set(calendar2.get(Calendar.YEAR), calendar2
							.get(Calendar.MONTH), calendar2.get(Calendar.DATE),
							0, 0, 0);
					calendar.add(Calendar.DAY_OF_MONTH, 1);

					sqlfmtToDate = calendar.getTime();
				}
				logger.debug("dateFormat  " + dateFormat);
				System.out.println("dateFormat  " + dateFormat);
				sqlfmtFromDate = dateFormat.parse(getFromDate());

				logger.debug(sqlfmtToDate + "    sqlfmtFromDate  "
						+ sqlfmtFromDate);
				System.out.println(sqlfmtToDate + "    sqlfmtFromDate  "
						+ sqlfmtFromDate);
				fmtFromDate = new Timestamp(sqlfmtFromDate.getTime());
				fmtToDate = new Timestamp(sqlfmtToDate.getTime());
			} catch (ParseException e) {
				System.out.println("hello3333");
			}

		}

		logger.debug("Changed From Date" + fmtFromDate + "Changed TO Date"
				+ fmtToDate);
		System.out.println("Changed From Date" + fmtFromDate
				+ "Changed TO Date" + fmtToDate);

		String root = ServletActionContext.getServletContext().getRealPath("/");
		File directory = new File("" + root + "/REPORTS");
		boolean exists = directory.exists();
		if (!exists) {
			directory.mkdirs();
		}

		if (reportType.equalsIgnoreCase("Monthly Sale Gamewise")) {
			System.out.println("hello4444444");
			String queryToAppend = "(";
			int j = 1;
			for (int i = 0; i < selectedGames.size() - 1; i++) {
				queryToAppend = queryToAppend + "b.game_id = '"
						+ selectedGames.get(i) + "' or ";
				j++;
				if (j == selectedGames.size()) {
					break;
				}
			}
			queryToAppend = queryToAppend + " b.game_id = '"
					+ selectedGames.get(j - 1) + "' )";

			query = "select sgm1.game_nbr, netAmount,month from st_se_game_master sgm1,(select game_id ,ifnull((mrp_amt - mrp_amt2),mrp_amt) as netAmount,month from (select sale_table.game_id,mrp_amt,mrp_amt2,month from (select b.game_id,sum(b.mrp_amt) as mrp_amt,b.transaction_type, b.transaction_id as id2, month from st_se_bo_agent_transaction b, (select  transaction_id, monthname(transaction_date)as month  from st_lms_bo_transaction_master where transaction_type='sale' and transaction_date > '"
					+ fmtFromDate
					+ "'       and transaction_date < '"
					+ fmtToDate
					+ "')        a where a.transaction_id=b.transaction_id and "
					+ queryToAppend
					+ " group by game_id) sale_table , (select mrp_amt2 , game_id from (select mrp_amt2, temp1.game_id,temp2.id2,temp2.transaction_type,temp2.month from ( select b.game_id,sum(b.mrp_amt) as mrp_amt1,b.transaction_type, b.transaction_id as id2, a.month from st_se_bo_agent_transaction b, (select  transaction_id, month(transaction_date)as month  from st_lms_bo_transaction_master where transaction_type='sale' and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "' )a where a.transaction_id=b.transaction_id and "
					+ queryToAppend
					+ " group by game_id ) temp1 left join (select b.game_id,sum(b.mrp_amt) as mrp_amt2,b.transaction_type, b.transaction_id as id2, a.month from st_se_bo_agent_transaction b, (select  transaction_id, month(transaction_date)as month  from st_lms_bo_transaction_master where transaction_type='sale_ret' and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "')a where a.transaction_id=b.transaction_id  and "
					+ queryToAppend
					+ " group by game_id ) temp2 on temp1.game_id=temp2.game_id) sale_return) tb2 where sale_table.game_id =  tb2.game_id) tb3) table4 where table4.game_id = sgm1.game_id order by game_nbr ";
			logger.debug(query);
			System.out.println(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/NetSaleGameWiseBar.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;
		} else if (reportType.equalsIgnoreCase("GameWise Sale")) {
			String queryToAppend = "(";
			int j = 1;
			for (int i = 0; i < selectedGames.size() - 1; i++) {
				queryToAppend = queryToAppend + "game_id = '"
						+ selectedGames.get(i) + "' or ";
				j++;
				if (j == selectedGames.size()) {
					break;
				}
			}
			queryToAppend = queryToAppend + " game_id = '"
					+ selectedGames.get(j - 1) + "' )";

			query = "select game_nbr , netAmount,month from st_se_game_master , (select ifnull((mrp_amt1 - mrp_amt2),mrp_amt1) as netAmount , game_id,month from (select mrp_amt2, temp1.game_id,temp2.id2,temp2.transaction_type,temp1.month,temp1.mrp_amt1 from ( select b.game_id,sum(b.mrp_amt) as mrp_amt1,b.transaction_type, b.transaction_id as id2, a.month from st_se_bo_agent_transaction b, (select  transaction_id, MONTHNAME(transaction_date)as month  from st_lms_bo_transaction_master where transaction_type='sale' and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "' )a where a.transaction_id=b.transaction_id  and "
					+ queryToAppend
					+ " group by game_id ) temp1 left join (select b.game_id,sum(b.mrp_amt) as mrp_amt2,b.transaction_type, b.transaction_id as id2, a.month from st_se_bo_agent_transaction b, (select  transaction_id, MONTHNAME(transaction_date)as month  from st_lms_bo_transaction_master where transaction_type='sale_ret' and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "' )a where a.transaction_id=b.transaction_id and "
					+ queryToAppend
					+ " group by game_id ) temp2 on temp1.game_id=temp2.game_id) sale_return) sale_net where st_se_game_master.game_id = sale_net.game_id order by game_nbr ";
			logger.debug(query);
			System.out.println(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/MonthlySaleGameWiseLine.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;

		} else if (reportType
				.equalsIgnoreCase("Sale of Game Monthly PriceWise")) {
			query = "select ticket_price , MONTHNAME(transaction_date)as month, SUM(netAmount) Amount from st_se_game_master sgm , (select sale.game_id,sale.transaction_date,ifnull((saleAmt - saleretAmt),saleAmt) as netAmount from (select game_id,transaction_date,SUM(mrp_amt) as saleAmt from (select game_id , transaction_date, transaction_type,mrp_amt from st_se_bo_agent_transaction sbat, (select transaction_id , transaction_date from st_lms_bo_transaction_master where (transaction_type='SALE' or transaction_type='SALE_RET') and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "') trans_id_tab where sbat.transaction_id=trans_id_tab.transaction_id) trans_both where transaction_type = 'sale' group by game_id) sale left join (select game_id,transaction_date,SUM(mrp_amt) as saleretAmt from (select game_id , transaction_date, transaction_type,mrp_amt from st_se_bo_agent_transaction sbat, (select transaction_id , transaction_date from st_lms_bo_transaction_master where (transaction_type='SALE' or transaction_type='SALE_RET') and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "') trans_id_tab where sbat.transaction_id=trans_id_tab.transaction_id) trans_both where transaction_type = 'sale_ret' group by game_id) sale_ret on sale_ret.game_id = sale.game_id) netSale where sgm.game_id = netSale.game_id group by ticket_price order by ticket_price ";
			System.out.println(query);
			logger.debug(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/MonthlySalePricewisePie.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;
		} else if (reportType.equalsIgnoreCase("Game Pwt Status")) {
			query = "select game_nbr as game_id,pwt_amt,status from st_se_game_master sgm , (select game_id,sum(pwt_amt) as pwt_amt,status from st_pwt_inv  where game_id= '"
					+ getSingleGame()
					+ "' group by status ) result where sgm.game_id = result.game_id order by status";
			System.out.println(query);
			logger.debug(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/GamePWTStatusPie.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;
		} else if (reportType.equalsIgnoreCase("PWT Status Gamewise")) {

			String queryToAppend = "(";
			int j = 1;
			for (int i = 0; i < selectedGames.size() - 1; i++) {
				queryToAppend = queryToAppend + "game_id = '"
						+ selectedGames.get(i) + "' or ";
				j++;
				if (j == selectedGames.size()) {
					break;
				}
			}
			queryToAppend = queryToAppend + " game_id = '"
					+ selectedGames.get(j - 1) + "' )";

			query = "select game_nbr as game_id , pwt_amt,status from st_se_game_master sgm , (select game_id,sum(pwt_amt) as pwt_amt,status from st_pwt_inv where "
					+ queryToAppend
					+ " group by game_id,status) result where sgm.game_id = result.game_id order by game_id,status";
			System.out.println(query);
			logger.debug(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/PWTStatusGameWiseBar.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;
		}

		else if (reportType.equalsIgnoreCase("Inventory Status")) {
			String queryToAppend = "and (";
			int j = 1;
			for (int i = 0; i < selectedGames.size() - 1; i++) {
				queryToAppend = queryToAppend + "game_id = '"
						+ selectedGames.get(i) + "' or ";
				j++;
				if (j == selectedGames.size()) {
					break;
				}
			}
			queryToAppend = queryToAppend + " game_id = '"
					+ selectedGames.get(j - 1) + "' )";
			query = "select total_tick, current_owner , game_nbr from ( select (COUNT(book_nbr)*nbr_of_tickets_per_book) as total_tick, game_id , current_owner,nbr_of_tickets_per_book,game_nbr from( select game_master.game_nbr , sgis.game_id, book_nbr , current_owner,nbr_of_tickets_per_book from st_se_game_inv_status sgis , (select game_nbr, game_id , nbr_of_tickets_per_book from st_se_game_master where game_status='OPEN' "
					+ queryToAppend
					+ " ) game_master where  sgis.game_id = game_master.game_id) agent where current_owner='BO' group by game_id union select * from ( select (COUNT(book_nbr)*nbr_of_tickets_per_book) as total_tick, game_id , current_owner,nbr_of_tickets_per_book,game_nbr from( select game_master.game_nbr , sgis.game_id, book_nbr , current_owner,nbr_of_tickets_per_book from st_se_game_inv_status sgis , (select game_nbr, game_id , nbr_of_tickets_per_book from st_se_game_master where game_status='OPEN' "
					+ queryToAppend
					+ " ) game_master where  sgis.game_id = game_master.game_id) agent where current_owner='AGENT' group by game_id ) agent1 union select (COUNT(book_nbr)*nbr_of_tickets_per_book) as total_tick, game_id , current_owner,nbr_of_tickets_per_book,game_nbr from( select game_master.game_nbr , sgis.game_id, book_nbr , current_owner,nbr_of_tickets_per_book from st_se_game_inv_status sgis , (select game_nbr, game_id , nbr_of_tickets_per_book from st_se_game_master where game_status='OPEN' "
					+ queryToAppend
					+ " ) game_master where  sgis.game_id = game_master.game_id) agent where current_owner='RETAILER' group by game_id) inventory order by game_nbr,current_owner";
			System.out.println(query);
			logger.debug(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/InventoryStatusGameWiseBar.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;
		}

		else if (reportType.equalsIgnoreCase("Yearly Sale Pricewise")) {
			query = "select ticket_price , MONTHNAME(transaction_date) as month, SUM(netAmount) Amount from st_se_game_master sgm , (select sale.game_id,sale.transaction_date,ifnull((saleAmt - saleretAmt),saleAmt) as netAmount from (select game_id,transaction_date,SUM(mrp_amt) as saleAmt from (select game_id , transaction_date, transaction_type,mrp_amt from st_se_bo_agent_transaction sbat, (select transaction_id , transaction_date from st_lms_bo_transaction_master where (transaction_type='SALE' or transaction_type='SALE_RET') and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "') trans_id_tab where sbat.transaction_id=trans_id_tab.transaction_id) trans_both where transaction_type = 'sale' group by game_id) sale left join (select game_id,transaction_date,SUM(mrp_amt) as saleretAmt from (select game_id , transaction_date, transaction_type,mrp_amt from st_se_bo_agent_transaction sbat, (select transaction_id , transaction_date from st_lms_bo_transaction_master where (transaction_type='SALE' or transaction_type='SALE_RET') and transaction_date > '"
					+ fmtFromDate
					+ "' and transaction_date < '"
					+ fmtToDate
					+ "') trans_id_tab where sbat.transaction_id=trans_id_tab.transaction_id) trans_both where transaction_type = 'sale_ret' group by game_id) sale_ret on sale_ret.game_id = sale.game_id) netSale where sgm.game_id = netSale.game_id group by ticket_price order by month  DESC ";
			System.out.println(query);
			logger.debug(query);
			file = root
					+ "com/skilrock/lms/web/reportsMgmt/compiledReports/MonthlySalebyPriceLine22.jasper";
			path = graphHelper.generateReport(query, file, parameterMap);
			// session.setAttribute("GENERATED_REPORT_PATH", path) ;
		}

		// session.setAttribute("GENERATED_BYTES", path) ;
		// if(path.length>800){
		// getResponsePDF(path);
		// }
		return getResponsePDF(path);
	}

	public String displayReport() {
		logger.info("inside displayReport");
		HttpSession session = getRequest().getSession();
		session.setAttribute("graphDate", new java.sql.Date(new Date()
				.getTime()).toString());
		Map m = new LinkedHashMap();
		m.putAll(graphHelper.getDefaultSelectGames("--Please Select--"));
		// System.out.println(m);
		logger.debug("Map: " + m);
		session.setAttribute("defaultGames", m);

		return SUCCESS;
	}

	/**
	 * This method send the currently present Games for the User Interface
	 * 
	 * @return Map of Games
	 */
	public void getDefaultGames() throws Exception {
		logger.info("Inside getDefaultGames");
		HttpSession session = getRequest().getSession();
		Map m = new LinkedHashMap();
		m.putAll(graphHelper.getDefaultSelectGames(getGameStatus()));
		// System.out.println(m);
		logger.debug("Map:  " + m);
		session.setAttribute("defaultGames", m);

		PrintWriter out = getResponse().getWriter();
		String html = "";
		html = "<select name=\"selectOptionManual\"><OPTION VALUE='---Please Select---'>---Please Select---<OPTION VALUE=''> ";

		int i = 0;
		for (Iterator it = m.keySet().iterator(); it.hasNext();) {
			int key = (Integer) it.next();
			int val = (Integer) m.get(key);
			i++;
			html += "<option value=\"" + key + "\">" + val + "</option>";
			logger.debug("key " + key + "  value " + val);
			// System.out.println("key "+key +" value "+val);
		}
		html += "</select>";
		// System.out.println(html);
		logger.debug(html);
		response.setContentType("text/html");

		out.print(html);
	}

	public void getDefaultGames2() throws Exception {
		HttpSession session = getRequest().getSession();
		Map m = new LinkedHashMap();
		m.putAll(graphHelper.getDefaultSelectGames(getGameStatus()));
		// System.out.println(m);
		logger.debug("Map:  " + m);
		session.setAttribute("defaultGames", m);

	}

	public String getFile() {
		return file;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getGameStatus() {
		if (gameStatus.equals("Open")) {

			gameStatus = "OPEN";
		} else if (gameStatus.equals("Close")) {

			gameStatus = "CLOSE";
		} else if (gameStatus.equals("Sale Hold")) {

			gameStatus = "SALE_HOLD";
		} else if (gameStatus.equals("Sale Closed")) {

			gameStatus = "SALE_CLOSE";
		} else if (gameStatus.equals("Terminated")) {

			gameStatus = "TERMINATE";
		}

		return gameStatus;
	}

	/**
	 * This method is for Generating the PDF Reports
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String getPDF() throws Exception {
		logger.debug("In PDF Generation in Graph Action   getPDF");
		// System.out.println("In PDF Generation in Graph Action getPDF");
		HttpSession session = getRequest().getSession();
		try {

			response.setContentType("application/pdf");
			OutputStream OutStrm = response.getOutputStream();
			OutStrm.write((byte[]) session.getAttribute("GENERATED_BYTES"));
			OutStrm.flush();
			return null;
			// OutStrm.close();
		} catch (Exception e) {
			logger
					.error("Error in Output Stream in Graph Report Action getPDF() Method");
			// System.out.println("Error in Output Stream in Graph Report Action
			// getPDF() Method");
			// e.printStackTrace();
		}
		/*
		 * PrintWriter out = getResponse().getWriter();
		 * response.setContentType("application/pdf"); out.print(data);
		 * out.flush();
		 */
		return SUCCESS;
	}

	public void getPDFReport(byte[] dataSession) {
		logger.info("inside getPDFReport   " + dataSession.length);
		// System.out.println("inside getPDFReport "+dataSession.length);
		HttpServletRequest sc = ServletActionContext.getRequest();

		HttpSession session = sc.getSession();
		// System.out.println("I am in Action66666666666666666666666666666");
		session.setAttribute("GENERATED_BYTES", dataSession);
	}

	public String getQuery() {
		return query;
	}

	public String getReportType() {
		return reportType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * This method is for Generating the PDF Reports
	 * 
	 * @param data
	 *            Byte Array generated by Jasper Compiler
	 * @return String
	 * @throws Exception
	 */
	public String getResponsePDF(byte[] data) {
		logger
				.debug("In PDF Generation in Graph Action    getResponsePDF graph action");
		// System.out.println("In PDF Generation in Graph Action getResponsePDF
		// graph action");
		try {

			if (data.length > 800) {
				response.setContentType("application/pdf");
				OutputStream OutStrm = response.getOutputStream();
				OutStrm.write(data);
				OutStrm.flush();
				return null;
			}
			// OutStrm.close();
		} catch (java.lang.IllegalStateException e) {
			logger
					.error("Error in Output1 Stream in Graph Report Action getResponsePDF Method");
			// System.out.println("Error in Output1 Stream in Graph Report
			// Action getResponsePDF Method");
			// e.printStackTrace();
		} catch (IOException e2) {
			logger
					.error("Error in Output2 Stream in Graph Report Action getResponsePDF Method");
			// System.out.println("Error in Output2 Stream in Graph Report
			// Action getResponsePDF Method");
			e2.printStackTrace();
		}

		/*
		 * PrintWriter out = getResponse().getWriter();
		 * response.setContentType("application/pdf"); out.print(data);
		 * out.flush();
		 */
		/*
		 * if(data.length>0){ return null; }
		 */
		return SUCCESS;
	}

	public List getSelectedGames() {
		return selectedGames;
	}

	public List getSelectGames() {
		return selectGames;
	}

	public String getSelectMonth() {
		return selectMonth;
	}

	public String getSelectYear() {
		return selectYear;
	}

	public String getSingleGame() {
		return singleGame;
	}

	public String getStatus() {
		return status;
	}

	public String getToDate() {
		return toDate;
	}

	/**
	 * This method send the List of year for selecting on Graph Report Page.
	 * 
	 * @return Map
	 */
	public Map getYear() {

		String depDate = (String) ServletActionContext.getServletContext()
				.getAttribute("DEPLOYMENT_DATE");
		final int GRAPH_STARTING_YEAR = Integer.parseInt(depDate
				.substring(0, 4));

		Calendar rightNow = Calendar.getInstance();
		int YR = rightNow.get(Calendar.YEAR);
		logger.debug("Date in Map" + YR + "Star  " + GRAPH_STARTING_YEAR);
		// System.out.println("Date in Map"+YR+"Star "+GRAPH_STARTING_YEAR);
		Map m = new LinkedHashMap();
		for (int k = GRAPH_STARTING_YEAR; k <= YR; k++) {
			m.put(new Integer(k), new Integer(k));
		}
		logger.debug("Map: " + m);
		// System.out.println(m);
		return m;
	}

	private String startDate;
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename="+reportName+".xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll(
					"</tbody>", "").trim();
			//out.write("<table border='1' width='100%' >" + reportData
					//+ "</table>");
			String appender = (startDate == null || endDate == null) ? "" : "<table border='1' width='100%' ><tr><td>Start Date</td><td>"+startDate+"</td></tr><tr><td>End Date</td><td>"+endDate+"</td></tr></table>";
			out.write(appender + reportData);
		}
	}

	public void exportAsExcelWithoutTable() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename="+reportName+".xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll(
					"</tbody>", "").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setSelectedGames(List selectedGames) {
		this.selectedGames = selectedGames;
	}

	public void setSelectGames(List selectGames) {
		this.selectGames = selectGames;
	}

	public void setSelectMonth(String selectMonth) {
		this.selectMonth = selectMonth;
	}

	public void setSelectYear(String selectYear) {
		this.selectYear = selectYear;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setSingleGame(String singleGame) {
		this.singleGame = singleGame;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
