package com.skilrock.lms.web.reportsMgmt.drawables;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.web.common.drawables.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.drawables.PrepareGraphsAndChartsDashBoardForBoHelper;

public class PrepareGraphsAndChartsDashBoardForBoAction extends ActionSupport implements ServletRequestAware , ServletResponseAware{

	Log logger = LogFactory.getLog(PrepareGraphsAndChartsDashBoardForBoAction.class);
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private double height;
	private double width;
	private String csvPath;
	private String currencySymbol;
	private String country;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String prepareDashBordForBo() {

		String result = null;
		setCountry((String)LMSUtility.sc.getAttribute("COUNTRY_DEPLOYED"));
		if(!checkActionList()){
			result = "success";
		}else{
		if (isNewCallForDashBord())
			prepareDashBordGraph();
		setCsvPath(Utility.getPropertyValue("CHARTS_GRAPHS_PATH_IN_LMS"));
		setCurrencySymbol(Utility.getPropertyValue("CURRENCY_SYMBOL"));
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");
		response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.addHeader("Access-Control-Allow-Origin", "*");
		result = "dashboard";
		}
		return result;
	}

	private void prepareDashBordGraph(){
		setPath();
		new PrepareGraphsAndChartsDashBoardForBoHelper();
		 /* ExecutorService executor = Executors.newFixedThreadPool(5);
	         for (int i = 1; i < 6; i++) {
	            Runnable worker = new PrepareGraphsAndChartsDashBoardForBoHelper(i);
	            executor.execute(worker);
	          }
	        executor.shutdown();
	        while (!executor.isTerminated()) {
	        }
	        logger.info("Finished all threads");*/
	}
	
	public void setPath(){
		if(CommonMethods.path == null){
			//CommonMethods.path = Utility.getPropertyValue("CHARTS_GRAPHS_PATH_IN_LMS");
			//CommonMethods.path = "/"+System.getProperty("jboss.server.home.dir")+"/deploy/"+request.getContextPath()+".war"+Utility.getPropertyValue("CHARTS_GRAPHS_PATH_IN_LMS");
			String warFolder = Utility.getPropertyValue("CHARTS_GRAPHS_PATH_IN_LMS").split("/")[1];
			CommonMethods.path = Utility.getPropertyValue("CHARTS_GRAPHS_PATH_IN_LMS").replace(warFolder, warFolder+".war");
		}
		logger.info(CommonMethods.path);
	} 	
	private boolean isNewCallForDashBord(){
		long timeDiff = 0;
		boolean flagForNewCall = true;
		ServletContext sc = null;
		try{
		sc= request.getSession().getServletContext();
		if(sc.getAttribute("DASHBOARD_SEEN_TIME_IN_MILI")!=null){
			timeDiff= System.currentTimeMillis()-Long.parseLong(sc.getAttribute("DASHBOARD_SEEN_TIME_IN_MILI").toString());
			if(timeDiff<0)
					flagForNewCall = false;
		}
		}catch (Exception e) {
			logger.error("EXCEPTION :- " , e);
		}
		
		if(flagForNewCall)
			sc.setAttribute("DASHBOARD_SEEN_TIME_IN_MILI" ,CommonMethods.getCurrentTimeInMilis());
		return flagForNewCall;
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkActionList(){
		return ((ArrayList<String>)request.getSession().getAttribute("ACTION_LIST")).contains(getActionName());
		}
	
	public String getActionName() {
	    return ActionContext.getContext().getName();
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public String getCsvPath() {
		return csvPath;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	
}
