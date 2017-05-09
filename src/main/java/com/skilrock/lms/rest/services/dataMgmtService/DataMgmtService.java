package com.skilrock.lms.rest.services.dataMgmtService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.controller.dataMgmtController.ReconcileMgmtController;
import com.skilrock.lms.controllerImpl.ReconcileMgmtControllerImplSLE;
import com.skilrock.lms.rest.services.bean.TPRequestBean;

@Path("/dataMgmt")
public class DataMgmtService {
	
	@Path("reconcileTransactions")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response reconcileTransactions(String reqData) {
		JsonObject respObj = null;
		TPRequestBean requestBean = null;
		ReconcileMgmtController controllerImpl = null;
		try {
			requestBean = new Gson().fromJson(new JsonParser().parse(reqData).getAsJsonObject(), TPRequestBean.class);
			if("SLE".equals(requestBean.getServiceCode())) {
				controllerImpl = ReconcileMgmtControllerImplSLE.Single.INSTANCE.getInstance();
			}
			
			controllerImpl.reconcileSLETransactions(requestBean);
			respObj = new JsonObject();
			respObj.addProperty("reconcile", new Gson().toJson(requestBean.getRequestData()));
			respObj.addProperty("responseCode", 0);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return Response.ok().entity(new Gson().toJson(respObj)).build();
	}
}
