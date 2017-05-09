package com.skilrock.lms.keba.reportMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;

public class RetailerGraphReportAction extends BaseAction{
	private static final long serialVersionUID = 1L;

	public RetailerGraphReportAction() {
		super(RetailerGraphReportAction.class);
	}

	private String requestData;

	@SuppressWarnings("unchecked")
	public void retailerGraphReport(){
		ServletContext sc = ServletActionContext.getServletContext();
		JSONObject responseObject = new JSONObject();
		JSONArray pieChartArray = new JSONArray();
		JSONObject pieChartBean = null;
		JSONArray barChartArray = new JSONArray();
		JSONObject barChartBean = null;
		PrintWriter out = null;
		try {
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(request.getParameter("requestData"));
			String userName = (String) requestData.get("userName");

			String[] days = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
			int dateRange = 4;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, -dateRange);
			Date earlierDate = calendar.getTime();
			String fromDate = dateFormat.format(currentDate);
			String toDate = dateFormat.format(earlierDate);

			calendar = Calendar.getInstance();
			String[][] dates = new String[dateRange][2];
			for(int i=0; i<dateRange; i++) {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				dates[i][0] = days[calendar.get(Calendar.DAY_OF_WEEK)];
				dates[i][1] = dateFormat.format(calendar.getTime());
			}

			response.setContentType("application/json");
			out = response.getWriter();

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				throw new LMSException(LMSErrors.SESSION_EXPIRED_ERROR_CODE,LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
			}

			HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				throw new LMSException(LMSErrors.SESSION_EXPIRED_ERROR_CODE,LMSErrors.SESSION_EXPIRED_ERROR_MESSAGE);
			}
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			double mrpSale = 0.0;
			double netSale = 0.0;
			double mrpPwt = 0.0;
			double netPwt = 0.0;

			StringBuilder saleQueryBuilder = new StringBuilder("SELECT IFNULL(SUM(mrpSale),0) mrpSale, IFNULL(SUM(netSale),0) netSale FROM (");
			StringBuilder pwtQueryBuilder = new StringBuilder("SELECT IFNULL(SUM(mrpPwt),0)AS mrpPwt, IFNULL(SUM(netPwt),0)AS netPwt FROM (");

			Connection connection = DBConnect.getConnection();
			String activeGamesQry = "select game_id, game_name from st_dg_game_master where game_status='OPEN';";
			Statement stmt;
			Statement gameStmt = connection.createStatement();
			ResultSet rs;
			ResultSet gameRs = gameStmt.executeQuery(activeGamesQry);
			int gameCount = 0;
			while(gameRs.next()) {
				int gameId = gameRs.getInt("game_id");
				String gameName = gameRs.getString("game_name");

				stmt = connection.createStatement();
				rs = stmt.executeQuery("SELECT IFNULL(SUM(mrp_amt),0)mrpSale, IFNULL(SUM(net_amt),0)netSale " +
						"FROM st_dg_ret_sale_"+gameId+" WHERE transaction_id IN (" +
						"SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE transaction_date>='"+fromDate+" 00:00:00' " +
						"AND transaction_date<='"+toDate+" 23:59:59' AND transaction_type IN ('DG_SALE','DG_SALE_OFFLINE')) " +
						"AND retailer_org_id="+userBean.getUserOrgId()+";");
				while(rs.next()) {
					mrpSale = rs.getDouble("mrpSale");
					netSale = rs.getDouble("netSale");
				}
				pieChartBean = new JSONObject();
				pieChartBean.put("gameId", gameId);
				pieChartBean.put("gameName", gameName);
				pieChartBean.put("netSale", netSale);
				pieChartArray.add(pieChartBean);

				saleQueryBuilder.append("SELECT IFNULL(SUM(mrp_amt),0) mrpSale, IFNULL(SUM(net_amt),0) netSale FROM st_dg_ret_sale_"+gameId+" " +
						"WHERE transaction_id IN (" +
						"SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE DATE(transaction_date)=? " +
						"AND transaction_type IN ('DG_SALE','DG_SALE_OFFLINE')) AND retailer_org_id="+userBean.getUserOrgId()+" UNION ALL ");

				pwtQueryBuilder.append("SELECT IFNULL(SUM(pwt_amt),0)AS mrpPwt, IFNULL(SUM(pwt_amt+retailer_claim_comm),0)AS netPwt " +
						"FROM st_dg_ret_pwt_"+gameId+" WHERE transaction_id IN (" +
						"SELECT transaction_id FROM st_lms_retailer_transaction_master WHERE DATE(transaction_date)=? " +
						"AND transaction_type IN ('DG_PWT','DG_PWT_AUTO')) AND retailer_org_id="+userBean.getUserOrgId()+" UNION ALL ");

				gameCount++;
			}

			saleQueryBuilder.delete(saleQueryBuilder.lastIndexOf(" UNION ALL "), saleQueryBuilder.length()-1);
			pwtQueryBuilder.delete(pwtQueryBuilder.lastIndexOf(" UNION ALL "), pwtQueryBuilder.length()-1);
			saleQueryBuilder.append(")aa;");
			pwtQueryBuilder.append(")aa;");

			System.out.println(saleQueryBuilder);
			System.out.println(pwtQueryBuilder);

			for(int i=0; i<dateRange; i++) {

				PreparedStatement pstmtSale = connection.prepareStatement(saleQueryBuilder.toString());
				PreparedStatement pstmtPwt = connection.prepareStatement(pwtQueryBuilder.toString());
				for(int j=0; j<gameCount; j++) {
					pstmtSale.setString(j+1, dates[i][1]);
					pstmtPwt.setString(j+1, dates[i][1]);
				}

				rs = pstmtSale.executeQuery();
				while(rs.next()) {
					mrpSale = rs.getDouble("mrpSale");
					netSale = rs.getDouble("netSale");
				}

				rs = pstmtPwt.executeQuery();
				while(rs.next()) {
					mrpPwt = rs.getDouble("mrpPwt");
					netPwt = rs.getDouble("netPwt");
				}

				barChartBean = new JSONObject();
				barChartBean.put("dayName", dates[i][0]);
				barChartBean.put("netSale", netSale);
				barChartBean.put("netWinning", netPwt);
				barChartBean.put("netProfit", mrpSale-netSale);
				barChartArray.add(barChartBean);
			}

			responseObject.put("isSuccess", true);
			responseObject.put("errorMsg", "");
			responseObject.put("errorCode", 0);
			
			responseObject.put("pieChartData", pieChartArray);
			responseObject.put("barChartData", barChartArray);
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "IOException Occured.");
			responseObject.put("isSuccess", false);
			return;
		} catch (LMSException e) {
			e.printStackTrace();
			if(e.getErrorCode() == 2013){
				responseObject.put("errorMsg", e.getErrorMessage());
			}else{
			responseObject.put("errorMsg", "LMSException Occured.");
			}
			responseObject.put("isSuccess", false);
			return;
		}  catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("ZimLottoBonus Sale Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}
	}
	
	
	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
}
