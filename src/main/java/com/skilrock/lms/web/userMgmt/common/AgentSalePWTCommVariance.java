package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.SalePwtCommVarBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentSalePWTCommVarianceHelper;

public class AgentSalePWTCommVariance extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String agentOrgName;

	private String agtOrgName;
	private String buttonValue;

	private String defPwtCommRate;

	private String defSaleCommRate;
	
	private String defWidCommRate;
	private String defDepCommRate;
	private String defNetGCommRate;
	
	private String gameName;
	private String gamestatus;
	private String gameStatus;
	private String gametype;
	private String gname;
	Log logger = LogFactory.getLog(AgentSalePWTCommVariance.class);
	private String orgType;

	private String pwtCommVar;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String retailerOrgName;

	private String saleCommVar;
	
	private String widCommVar;
	private String depCommVar;
	private String netGCommVar;

	private String serviceName;

	public String agentSalePwtCommVarianceDetail() {
		logger.debug("clicked button value is : === " + buttonValue
				+ "  game status : " + gamestatus);
		AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
		logger.debug("serviceName " + serviceName);
		logger.debug("orgType " + orgType);
		if (serviceName.equalsIgnoreCase("SE")) {
			List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
			salePwtCommVarList = helper.salePwtCommVarList(agentOrgName,
					gamestatus, orgType);
			HttpSession session = request.getSession();
			session
					.setAttribute("salePwtCommVarSearchList",
							salePwtCommVarList);
			return SUCCESS;
		} else if (serviceName.equalsIgnoreCase("DG")) {
			List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
			salePwtCommVarList = helper.salePwtCommVarListForDg(agentOrgName,
					gamestatus, orgType);
			HttpSession session = request.getSession();
			session
					.setAttribute("salePwtCommVarSearchList",
							salePwtCommVarList);
			return SUCCESS;
		}else if (serviceName.equalsIgnoreCase("OLA")) {
			List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
			salePwtCommVarList = helper.salePwtCommVarListOLA(agentOrgName,
					gamestatus, orgType);
			HttpSession session = request.getSession();
			session
					.setAttribute("salePwtCommVarSearchList",
							salePwtCommVarList);
			return SUCCESS;
		}else if(serviceName.equalsIgnoreCase("CS")){
			List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
			salePwtCommVarList = helper.saleCommVarListForCS(agentOrgName,
					gamestatus, orgType);
			HttpSession session = request.getSession();
			session
					.setAttribute("salePwtCommVarSearchList",
							salePwtCommVarList);
			return SUCCESS;
		} else if ("IW".equalsIgnoreCase(serviceName)) {
			List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
			salePwtCommVarList = helper.salePwtCommVarListForIW(agentOrgName, gamestatus, orgType);
			HttpSession session = request.getSession();
			session.setAttribute("salePwtCommVarSearchList", salePwtCommVarList);
			return SUCCESS;
       }  else if ("VS".equalsIgnoreCase(serviceName)) {
			List<SalePwtCommVarBean> salePwtCommVarList = new ArrayList<SalePwtCommVarBean>();
			salePwtCommVarList = helper.salePwtCommVarListForVS(agentOrgName, gamestatus, orgType);
			HttpSession session = request.getSession();
			session.setAttribute("salePwtCommVarSearchList", salePwtCommVarList);
			return SUCCESS;
     	}
		return ERROR;

	}

	public String chackOrgType() {
		String returnVal = "error";
		if ("AGENT".equals(orgType)) {
			returnVal = agentSalePwtCommVarianceDetail();
			if ("success".equalsIgnoreCase(returnVal) && "CS".equalsIgnoreCase(serviceName)) {
				returnVal = "agentCS";
			} else if ("success".equalsIgnoreCase(returnVal) && "OLA".equalsIgnoreCase(serviceName)) {
				returnVal = "agentOLA";
			} else if ("success".equalsIgnoreCase(returnVal) && "IW".equalsIgnoreCase(serviceName)) {
				returnVal = "agentIW";
			} else if ("success".equalsIgnoreCase(returnVal) && "VS".equalsIgnoreCase(serviceName)) {
				returnVal = "agentVS";
			}
		} else if ("RETAILER".equals(orgType)) {
			RetailerSalePWTCommVariance retAction = new RetailerSalePWTCommVariance();
			retAction.setOrgType(orgType);
			retAction.setServiceName(serviceName);
			retAction.setAgentOrgName(retailerOrgName); // retailer org Name
			retAction.setGamestatus(gamestatus);
			retAction.setServletRequest(request);
			returnVal = retAction.retailerSalePwtCommVarianceDetail();
			if ("success".equalsIgnoreCase(returnVal) && "OLA".equalsIgnoreCase(serviceName)) {
				returnVal = "retailerOLA";
			} else if ("success".equalsIgnoreCase(returnVal) && "CS".equalsIgnoreCase(serviceName)) {
				returnVal = "retailerCS";
			} else if ("success".equalsIgnoreCase(returnVal) && "IW".equalsIgnoreCase(serviceName)) {
				returnVal = "retailerIW";
			} else if ("success".equalsIgnoreCase(returnVal) && "VS".equalsIgnoreCase(serviceName)) {
				returnVal = "retailerVS";
			} else if ("success".equalsIgnoreCase(returnVal)) {
				returnVal = "retailer";
			}
		}
		return returnVal;
	}

	public String execute() {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
		// List<String> agentNameList=new ArrayList<String>();
		logger.info("in side execute ====== ");
		System.out.println("in side execute ====== ");
		// agentNameList=helper.getAgentNameList(userInfoBean.getUserType());
		/*
		 * if(agentNameList.size()<1) return ERROR;
		 */
		// System.out.println("111111111");
		session.setAttribute("agentNameList", new ArrayList());

		Map<String, String> serviceNameMap = helper.getServiceList();
		session.setAttribute("serviceNameMap", serviceNameMap);
		int tierId = userInfoBean.getTierId();
		Map<String, String> organizationTypeMap = helper
				.getOrganizationType(tierId);
		session.setAttribute("organizationTypeMap", organizationTypeMap);

		return SUCCESS;
	}

	public String getAgentName() {
		return agentOrgName;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public String getDefPwtCommRate() {
		return defPwtCommRate;
	}

	public String getDefSaleCommRate() {
		return defSaleCommRate;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGamestatus() {
		return gamestatus;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public String getGametype() {
		return gametype;
	}

	public String getGname() {
		return gname;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getPwtCommVar() {
		return pwtCommVar;
	}

	public String getRetailerOrgName() {
		return retailerOrgName;
	}

	public String getSaleCommVar() {
		return saleCommVar;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setAgentName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}

	public void setDefPwtCommRate(String defPwtCommRate) {
		this.defPwtCommRate = defPwtCommRate;
	}

	public void setDefSaleCommRate(String defSaleCommRate) {
		this.defSaleCommRate = defSaleCommRate;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGamestatus(String gamestatus) {
		this.gamestatus = gamestatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setPwtCommVar(String pwtCommVar) {
		this.pwtCommVar = pwtCommVar;
		// if("".equalsIgnoreCase(this.pwtCommVar.trim()))
		// this.pwtCommVar="0.00";
	}

	public void setRetailerOrgName(String retailerOrgName) {
		this.retailerOrgName = retailerOrgName;
	}

	public void setSaleCommVar(String saleCommVar) {
		this.saleCommVar = saleCommVar;
		// if("".equalsIgnoreCase(this.saleCommVar.trim()))
		// this.saleCommVar="0.00";
	}

	public String getDefWidCommRate() {
		return defWidCommRate;
	}

	public void setDefWidCommRate(String defWidCommRate) {
		this.defWidCommRate = defWidCommRate;
	}

	public String getDefDepCommRate() {
		return defDepCommRate;
	}

	public void setDefDepCommRate(String defDepCommRate) {
		this.defDepCommRate = defDepCommRate;
	}

	public String getDefNetGCommRate() {
		return defNetGCommRate;
	}

	public void setDefNetGCommRate(String defNetGCommRate) {
		this.defNetGCommRate = defNetGCommRate;
	}

	public String getDepCommVar() {
		return depCommVar;
	}

	public void setDepCommVar(String depCommVar) {
		this.depCommVar = depCommVar;
	}

	public String getNetGCommVar() {
		return netGCommVar;
	}

	public void setNetGCommVar(String netGCommVar) {
		this.netGCommVar = netGCommVar;
	}

	public String getWidCommVar() {
		return widCommVar;
	}

	public void setWidCommVar(String widCommVar) {
		this.widCommVar = widCommVar;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void submitUpdatedAllValue() {
		logger.debug(" agtOrgName=" + agtOrgName + "  gameStatus=" + gameStatus
				+ "  saleCommVar=" + saleCommVar + " pwtCommVar=" + pwtCommVar);
		System.out.println(" agtOrgName=" + agtOrgName + "  gameStatus="
				+ gameStatus + "  saleCommVar=" + saleCommVar + " pwtCommVar="
				+ pwtCommVar);
		PrintWriter out = null;
		try {
			AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
			System.out.println("called......");
			Map<String, Double> map = helper.getDefaultCommRateForAll();
			String flag = null;
			double maxSaleValue = map.get("maxSaleVariance");
			double minSaleRate = map.get("minSaleCommVar");
			double minSaleCommValue = minSaleRate
					+ Double.parseDouble(saleCommVar);
			logger.debug("max value is : " + maxSaleValue);
			System.out.println("max value is : " + maxSaleValue);

			if (Double.parseDouble(saleCommVar) < maxSaleValue
					&& minSaleCommValue >= 0) {
				flag = helper.submitUpdatedAllValue(agtOrgName.replace("amp",
						"&"), gameStatus, saleCommVar, pwtCommVar);
			}

			out = response.getWriter();
			if (flag == null) {
				out.print("pbm:Sale Commission Variance must be less than "
						+ maxSaleValue);
			} else if (minSaleCommValue < 0) {
				out
						.print("Calculated Net Comm Variance is Negetive. Please Enter Another Value in Sale Comm Variance.");
			} else if ("SUCCESS".equalsIgnoreCase(flag)) {
				out.print("successfully updated");
			} else if ("SAME_VALUES".equalsIgnoreCase(flag)) {
				out
						.print("You Must Upadate Sale Or PWT Commission Variance Value");
			} else {
				out.print("Some Internal Error occurred");
			}
		} catch (Exception e) {
			if (out != null) {
				out.print("Some Internal Error occurred");
			}
			e.printStackTrace();
		}
	}

	public void submitUpdatedValue() {
		logger.info("Inside submitUpdatedValue()");
		logger.debug("agtOrgName=" + agtOrgName.replace("'", "") + " gname="
				+ gname + "  defSaleCommRate=" + defSaleCommRate
				+ "saleCommVar=" + saleCommVar + " defPwtCommRate="
				+ defPwtCommRate + " pwtCommVar=" + pwtCommVar
				+ " serviceName =" + serviceName + " orgType =" + orgType);
		System.out.println("agtOrgName=" + agtOrgName.replace("'", "")
				+ " gname=" + gname + "  defSaleCommRate=" + defSaleCommRate
				+ "saleCommVar=" + saleCommVar + " defPwtCommRate="
				+ defPwtCommRate + " pwtCommVar=" + pwtCommVar
				+ " serviceName =" + serviceName + " orgType =" + orgType);
		PrintWriter out = null;
		try {
			AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
			System.out.println("called......");
			String game[] = null;

			if (serviceName.equalsIgnoreCase("SE")) {
				 game = gname.split("-");
				Map<String, Double> map = helper.getDefaultCommRate(game[0],
						game[1]);
				double maxSaleValue = map.get("maxSaleVariance");
				double maxPwtVariance = map.get("maxPwtVariance");
				logger.debug("max value is : " + maxSaleValue);
				System.out.println("max value is : " + maxSaleValue);
				logger.debug("serviceName  " + serviceName);
				System.out.println("serviceName  " + serviceName);
				String flag = null;
				if (!(Double.parseDouble(saleCommVar) < maxSaleValue)) {
					flag = "SALE";
				}
				if (!(Double.parseDouble(pwtCommVar) < maxPwtVariance)) {
					flag = "PWT";
				}

				if (Double.parseDouble(saleCommVar) < maxSaleValue
						&& Double.parseDouble(pwtCommVar) < maxPwtVariance) {
					flag = helper.submitUpdatedValue(agtOrgName, gname, defSaleCommRate, saleCommVar,
							defPwtCommRate, pwtCommVar);
				}

				out = response.getWriter();
				if ("SALE".equalsIgnoreCase(flag)) {
					out.print("SALE;" + maxSaleValue);
				} else if ("PWT".equalsIgnoreCase(flag)) {
					out.print("PWT;" + maxSaleValue);
				} else if ("SUCCESS".equalsIgnoreCase(flag)) {
					out.print("successfully updated");
				} else if ("SAME_VALUES".equalsIgnoreCase(flag)) {
					out
							.print("You Must Upadate Sale Or PWT Commission Variance Value");
				} else {
					out.print("Some Internal Error occurred");
				}
			} else if (serviceName.equalsIgnoreCase("DG")) {
				game = gname.split("-");
				Map<String, Double> map = helper.getDefaultCommRateDg(game[0],
						game[1]);
				double maxSaleValue = map.get("maxSaleVariance");
				double maxPwtVariance = map.get("maxPwtVariance");

				String flag = null;
				if (!(Double.parseDouble(saleCommVar) < maxSaleValue)) {
					flag = "SALE";
				}
				if (!(Double.parseDouble(pwtCommVar) < maxPwtVariance)) {
					flag = "PWT";
				}

				if (Double.parseDouble(saleCommVar) < maxSaleValue
						&& Double.parseDouble(pwtCommVar) < maxPwtVariance) {
					flag = helper.submitUpdatedValueDg(agtOrgName, gname, defSaleCommRate, saleCommVar,
							defPwtCommRate, pwtCommVar);
				}

				out = response.getWriter();
				if ("SALE".equalsIgnoreCase(flag)) {
					out.print("SALE;" + maxSaleValue);
				} else if ("PWT".equalsIgnoreCase(flag)) {
					out.print("PWT;" + maxPwtVariance);
				} else if ("SUCCESS".equalsIgnoreCase(flag)) {
					out.print("successfully updated");
				} else if ("SAME_VALUES".equalsIgnoreCase(flag)) {
					out
							.print("You Must Upadate Sale Or PWT Commission Variance Value");
				} else {
					out.print("Some Internal Error occurred");
				}

			}else if (serviceName.equalsIgnoreCase("CS")) {
				game = gname.split("_");
				Map<String, Double> map = helper.getDefaultCommRateCS(game[0],
						game[1], game[2]);
				double maxSaleValue = map.get("maxSaleVariance");
				logger.debug("max value is : " + maxSaleValue);
				logger.debug("serviceName  " + serviceName);
				String flag = null;
				if (!(Double.parseDouble(saleCommVar) < maxSaleValue)) {
					flag = "SALE";
				}

				if (Double.parseDouble(saleCommVar) < maxSaleValue) {
					flag = helper.submitUpdatedValueCS(agtOrgName, gname, defSaleCommRate, saleCommVar);
				}

				out = response.getWriter();
				if ("SALE".equalsIgnoreCase(flag)) {
					out.print("SALE;" + maxSaleValue);
				} else if ("SUCCESS".equalsIgnoreCase(flag)) {
					out.print("successfully updated");
				} else if ("SAME_VALUES".equalsIgnoreCase(flag)) {
					out.print("You Must Upadate Sale Commission Variance Value");
				} else {
					out.print("Some Internal Error occurred");
				}
			}else if(serviceName.equalsIgnoreCase("OLA")){

				game = gname.split("-");
				Map<String, Double> map = helper.getDefaultCommRateForOLA(game[0],
						game[1]);
				double maxWidVariance = map.get("maxWidVariance");
				double maxDepVariance = map.get("maxDepVariance");
				double maxNetGVariance = map.get("maxNetGVariance");

				String flag = null;
				if (!(Double.parseDouble(widCommVar) < maxWidVariance)) {
					flag = "WITHDRAWAL";
				}
				if (!(Double.parseDouble(depCommVar) < maxDepVariance)) {
					flag = "DEPOSIT";
				}
				if (!(Double.parseDouble(netGCommVar) < maxNetGVariance)) {
					flag = "NET GAMING";
				}

				if (Double.parseDouble(widCommVar) < maxWidVariance
						&& Double.parseDouble(depCommVar) < maxDepVariance
						&& Double.parseDouble(netGCommVar) < maxNetGVariance) {
					flag = helper.submitUpdatedValueOLA(agtOrgName, gname, defWidCommRate, widCommVar,
							defDepCommRate, depCommVar, defNetGCommRate, netGCommVar);
				}

				out = response.getWriter();
				if ("WITHDRAWAL".equalsIgnoreCase(flag)) {
					out.print("WITHDRAWAL;" + maxWidVariance);
				} else if ("DEPOSIT".equalsIgnoreCase(flag)) {
					out.print("DEPOSIT;" + maxDepVariance);
				} else if ("NET GAMING".equalsIgnoreCase(flag)){
					out.print("NET GAMING;" + maxNetGVariance);
				} else if ("SUCCESS".equalsIgnoreCase(flag)) {
					out.print("successfully updated");
				} else if ("SAME_VALUES".equalsIgnoreCase(flag)) {
					out
							.print("You Must Upadate Any One Of Commission Variance Value");
				} else {
					out.print("Some Internal Error occurred");
				}

			
			} else if ("IW".equals(serviceName)) {
				game = gname.split("-");
				Map<String, Double> map = helper.getDefaultCommRateIW(game[0], game[1]);
				double maxSaleValue = map.get("maxSaleVariance");
				double maxPwtVariance = map.get("maxPwtVariance");

				String flag = null;
				if (!(Double.parseDouble(saleCommVar) < maxSaleValue)) {
					flag = "SALE";
				}
				if (!(Double.parseDouble(pwtCommVar) < maxPwtVariance)) {
					flag = "PWT";
				}

				if (Double.parseDouble(saleCommVar) < maxSaleValue && Double.parseDouble(pwtCommVar) < maxPwtVariance) {
					flag = helper.submitUpdatedValueIW(agtOrgName, gname, defSaleCommRate, saleCommVar, defPwtCommRate, pwtCommVar);
				}

				out = response.getWriter();
				if ("SALE".equalsIgnoreCase(flag)) {
					out.print("SALE;" + maxSaleValue);
				} else if ("PWT".equalsIgnoreCase(flag)) {
					out.print("PWT;" + maxPwtVariance);
				} else if ("SUCCESS".equalsIgnoreCase(flag)) {
					out.print("successfully updated");
				} else if ("SAME_VALUES".equalsIgnoreCase(flag)) {
					out.print("You Must Upadate Sale Or PWT Commission Variance Value");
				} else {
					out.print("Some Internal Error occurred");
				}

			}

		} catch (Exception e) {
			if (out != null) {
				out.print("Some Internal Error occurred");
			}
			e.printStackTrace();
		}
	}

}
