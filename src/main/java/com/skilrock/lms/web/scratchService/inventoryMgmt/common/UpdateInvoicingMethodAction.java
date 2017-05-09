package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.web.scratchService.inventoryMgmt.javaBeans.AgentInvoicingMethodBean;
import com.skilrock.lms.web.scratchService.inventoryMgmt.serviceImpl.UpdateInvoicingMethodServiceImpl;

public class UpdateInvoicingMethodAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private int gameId;
	private Map<Integer, String> gameMap;
	private List<AgentInvoicingMethodBean> agentDetailList;
	private int agentOrgId;
	private String jsonParamData;
	private Map<Integer, String> invoiceMap;
	private Map<Integer, String> methodIdMap;

	public UpdateInvoicingMethodAction() {
		super("UpdateInvoicingMethodAction");
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public Map<Integer, String> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, String> gameMap) {
		this.gameMap = gameMap;
	}

	public List<AgentInvoicingMethodBean> getAgentDetailList() {
		return agentDetailList;
	}

	public void setAgentDetailList(List<AgentInvoicingMethodBean> agentDetailList) {
		this.agentDetailList = agentDetailList;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String getJsonParamData() {
		return jsonParamData;
	}

	public void setJsonParamData(String jsonParamData) {
		this.jsonParamData = jsonParamData;
	}

	public Map<Integer, String> getInvoiceMap() {
		return invoiceMap;
	}

	public void setInvoiceMap(Map<Integer, String> invoiceMap) {
		this.invoiceMap = invoiceMap;
	}

	public Map<Integer, String> getMethodIdMap() {
		return methodIdMap;
	}

	public void setMethodIdMap(Map<Integer, String> methodIdMap) {
		this.methodIdMap = methodIdMap;
	}

	public String updateInvoicingMethodMenu() {
		try {
			gameMap = UpdateInvoicingMethodServiceImpl.getInstance().activeGameMap();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public String updateInvoicingMethodSearch() {
		try {
			agentDetailList = UpdateInvoicingMethodServiceImpl.getInstance().getAgentInvoicingMethod(gameId);
			invoiceMap = UpdateInvoicingMethodServiceImpl.getInstance().getInvoicingMethods();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public void methodIdMap(){
		try {
			JSONObject Obj = new JSONObject();
			PrintWriter out = getResponse().getWriter();
			response.setContentType("application/json");
			methodIdMap = UpdateInvoicingMethodServiceImpl.getInstance().getMethodIdMap();
			Obj.put("arg0", methodIdMap);
			out.print(Obj);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void updateInvoicingMethod() {
		agentDetailList = new ArrayList<AgentInvoicingMethodBean>();
		try {
			JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();
			AgentInvoicingMethodBean methodBean = null;
			JsonObject updateData = null;
			for(int i=0; i<dataArray.size(); i++) {
        		updateData = dataArray.get(i).getAsJsonObject();
        		methodBean = new AgentInvoicingMethodBean();
				methodBean.setOrgId(updateData.get("orgId").getAsInt());
				methodBean.setMethodId(updateData.get("methodId").getAsInt());
				methodBean.setMethodValue(updateData.get("methodValue").getAsString());
				agentDetailList.add(methodBean);
        	}

			UpdateInvoicingMethodServiceImpl.getInstance().updateInvoicingMethod(gameId, agentDetailList, getUserBean().getUserId(), request.getRemoteAddr());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}