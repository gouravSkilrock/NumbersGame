package com.skilrock.lms.rest.scratchService.orderMgmt.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.OrderRequestBean;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GenerateRetailerOrderHelper;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.ProcessRetailerRequestHelper;
import com.skilrock.lms.rest.scratchService.BaseController.BaseController;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.scratchService.orderMgmt.serviceImpl.ScratchServiceImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;
import com.skilrock.lms.scratch.orderBeans.OrderCartBean;
import com.skilrock.lms.scratch.orderBeans.OrderListBean;
import com.skilrock.lms.web.drawGames.common.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;



@Provider
@Path("orderMgmt")
public class ScratchController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(ScratchController.class);
	
	private ScratchService scracthService;
	private GenerateRetailerOrderHelper orderHelper;
	private JsonObject requestObj = null;
	private OrderCartBean orderCartBean = null;
	private JsonObject responseDataOfOrder = null;
	private int orderId = 0;

	public ScratchController() {
		this.scracthService = new ScratchServiceImpl();
		this.orderHelper =  new GenerateRetailerOrderHelper();
	}

	public ScratchController(ScratchService scracthService){
		this.scracthService = scracthService;
	}
	
	public ScratchController(GenerateRetailerOrderHelper orderHelper, ScratchService scracthService){
		this.orderHelper = orderHelper;
		this.scracthService = scracthService;
	}
	
	@Path("/itemReceive")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getUpdateRecievedBookNbrStatus(String requestData) {
		try {
			ScracthMgmtBean bean = getScracthManagementBean(requestData);
			if (isValidRequest(bean)) {
				return getResponseForValidBookNumbers(bean);
			} else {
				return new Gson().toJson(getFailureJsonResponse(101, "Mandatory parameteres are not provided"));
			}
		} catch (Exception e) {
			logger.error("Some Internal Error Occured");
			return new Gson().toJson(getFailureJsonResponse(102, "Some Internal Error Occured"));
		}
	}
	
	@Path("/getGameList")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getGameList (String requestData) throws JSONException{
		JSONObject resObj = new JSONObject();
	    int requestId=0;
		try{
			requestId = makeGameListPanel(requestData, resObj);
		}catch(ScratchException e ){
			resObj.put("requestId", requestId);
			resObj.put("responseCode", e.getErrorCode());
			resObj.put("responseData",e.getErrorMessage());
		}catch(Exception e){
			getErrorWhileGetGameList(resObj, requestId, e);
		}
		return resObj.toString();
	}

	private void getErrorWhileGetGameList(JSONObject resObj, int requestId, Exception e) throws JSONException {
		resObj.put("requestId", requestId);
		resObj.put("responseCode", ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE);
		resObj.put("responseData",ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		e.printStackTrace();
	}

	private int makeGameListPanel(String requestData, JSONObject resObj)
			throws ScratchException, JSONException, Exception {
		JsonObject reqObj;
		int requestId;
		String tpUserId;
		reqObj = new JsonObject();
		if(requestData == null || requestData.trim().length() < 1 ){
			throw new ScratchException(ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
		}
		reqObj=new JsonParser().parse(requestData).getAsJsonObject();
		try{
			requestId=reqObj.get("requestId").getAsInt();
			tpUserId=reqObj.get("tpUserId").getAsString();
		}
		catch(NullPointerException e){
			throw new ScratchException(ScratchErrors.INVALID_REQUEST_DATA_ERROR_CODE, ScratchErrors.INVALID_REQUEST_DATA_ERROR_MESSAGE);
		}
		
		if(tpUserId==null ||requestId==0){
			throw new ScratchException(ScratchErrors.NO_REQUEST_ID_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_ID_PROVIDED_ERROR_MESSAGE);
		}
		
		resObj.put("requestId",requestId);
		resObj.put("responseCode", ScratchErrors.SUCCESS_CODE);
		resObj.put("responseData",getScracthService().getGameList());
		return requestId;
	}

	private String getResponseForValidBookNumbers(ScracthMgmtBean orderMgmtBean) throws JSONException {
		if (scracthService.isBookNumberListValid(orderMgmtBean)) {
			return markBookNumbersReceived(orderMgmtBean);
		} else {
			return new Gson().toJson(getFailureJsonResponse(103, "Book nBrs are incorrect."));
		}
	}

	private boolean isValidRequest(ScracthMgmtBean orderMgmtBean) {
		return orderMgmtBean.getBookList() != null && orderMgmtBean.getDlNumber() != null
				&& !orderMgmtBean.getDlNumber().equals("");
	}

	private String markBookNumbersReceived(ScracthMgmtBean orderMgmtBean) throws JSONException {
		String getStatus = scracthService.updateBookListStatus(orderMgmtBean);
		String response = "";
		if ("SUCCESS".equals(getStatus)) {
			response = generateResponseForSuccess(orderMgmtBean);
			return response.toString();
		}
		return new Gson().toJson(getFailureJsonResponse(102,"Some Internal Error Occured"));
	}

	private String generateResponseForSuccess(ScracthMgmtBean orderMgmtBean) throws JSONException {
		JSONObject responseObject = new JSONObject();
		responseObject.put("requestId", orderMgmtBean.getRequestId());
		responseObject.put("responseCode", 100);
		JSONArray reponseDataArray = new JSONArray();
		createJsonArrayForResposeData(orderMgmtBean, reponseDataArray);
		responseObject.put("responseData", reponseDataArray);
		return responseObject.toString();
	}

	private void createJsonArrayForResposeData(ScracthMgmtBean orderMgmtBean, JSONArray reponseDataArray)
			throws JSONException {
		for (String bookNbr : orderMgmtBean.getBookList()) {
			JSONObject responseDataObject = new JSONObject();
			responseDataObject.put("bookNumber", bookNbr);
			responseDataObject.put("status", "MARKED_AS_RECEIVED");
			reponseDataArray.add(responseDataObject);
		}
	}
	
	public ScratchService getScracthService() {
		return scracthService;
	}
	
	@Path("/orderHistory")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getOrderHistory(String requestData) throws Exception {
		JSONObject resObj = new JSONObject();
		int requestId = 0;
		try{
			requestId = buildOrderHistory(requestData, resObj);
		}catch(ScratchException e){
			resObj.put("requestId", requestId);
			resObj.put("responseCode", e.getErrorCode());
			resObj.put("responseData",e.getErrorMessage());
		}catch(Exception e){
			buildErrorResponse(resObj, requestId, e);
		}
		return  resObj.toString();
	}

	private void buildErrorResponse(JSONObject resObj, int requestId, Exception e) throws JSONException {
		e.printStackTrace();
		resObj.put("requestId", requestId);
		resObj.put("responseCode", ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE);
		resObj.put("responseData",ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}

	private int buildOrderHistory(String requestData, JSONObject resObj)
			throws ScratchException, SQLException, Exception, JSONException {
		JsonObject reqObj;
		String tpUserId;
		int requestId;
		String orderReferenceId = null;
		String orderStatus = null;
		ProcessRetailerRequestHelper processRetailerRequestHelper;
		reqObj = new JsonObject();
		
		try{
			reqObj=new JsonParser().parse(requestData).getAsJsonObject();
			tpUserId=reqObj.get("tpUserId").getAsString();
			requestId=reqObj.get("requestId").getAsInt();
			
		}catch(NullPointerException e){
			throw new ScratchException(ScratchErrors.INVALID_REQUEST_DATA_ERROR_CODE, ScratchErrors.INVALID_REQUEST_DATA_ERROR_MESSAGE);
		}
		if(reqObj.get("orderReferenceId")==null || reqObj.get("orderReferenceId").getAsString().trim().isEmpty()){
			orderReferenceId=null;
		}
		else{
			orderReferenceId=reqObj.get("orderReferenceId").getAsString();
		}
		if(reqObj.get("orderStatus")==null || reqObj.get("orderStatus").getAsString().trim().isEmpty()){
			orderStatus=null;
		}else{
			orderStatus=reqObj.get("orderStatus").getAsString();
		}
		
		if(orderReferenceId==null && orderStatus==null){
			throw new ScratchException(ScratchErrors.INVALID_REQUEST_DATA_ERROR_CODE, ScratchErrors.INVALID_REQUEST_DATA_ERROR_MESSAGE);
		}
		validateRequestData(requestData, tpUserId, requestId, orderReferenceId, orderStatus);
		processRetailerRequestHelper=new ProcessRetailerRequestHelper();
		ScratchDaoImpl scratchDaoImpl=new ScratchDaoImpl();
		DaoBean daoBean=scratchDaoImpl.getUserOrgIdAndUserIdFromTpUserId(tpUserId);
		invokeScratchCoreEngineForOrderHistory(resObj, requestId, orderReferenceId, orderStatus,
				processRetailerRequestHelper, daoBean);
		return requestId;
	}

	private void invokeScratchCoreEngineForOrderHistory(JSONObject resObj, int requestId, String orderReferenceId,
			String orderStatus, ProcessRetailerRequestHelper processRetailerRequestHelper,DaoBean daoBean)
			throws Exception, JSONException {
		List<OrderRequestBean> listOfOrder;
		int retOrgId = 0;
		String endDate=Util.getCurrentTimeString();
		Calendar calendar= Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);
		String startDate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
		try{
			retOrgId=daoBean.getUserOrgId();
		}
		catch(NullPointerException e){
			throw new ScratchException(ScratchErrors.INVALID_USER_NAME_CODE,ScratchErrors.INVALID_USER_NAME_MESSAGE);
		}
		listOfOrder=processRetailerRequestHelper.getRetailerRequestedOrders(retOrgId, null, null, orderReferenceId, orderStatus, startDate, endDate);
		if(listOfOrder.size()!=0){
			List<OrderHistory> list=new ArrayList<OrderHistory>();
			for(OrderRequestBean order : listOfOrder){
				OrderHistory orderHistory = new OrderHistory();
				orderHistory.setNoOfBookApproved(order.getNbrAppBooks());
				orderHistory.setNoOfBookDispatched(order.getNbrOfBooksDlvrd());
				orderHistory.setNoOfBookOrdered(order.getNbrOfBooksReq());
				orderHistory.setOrderReferenceId(order.getOrderId());
				orderHistory.setGameName(order.getGameName());
				orderHistory.setOrderStatus(order.getName());
				list.add(orderHistory);
			} 
			String json=new Gson().toJson(listOfOrder);
			resObj.put("requestId", requestId);
			resObj.put("responseCode", ScratchErrors.SUCCESS_CODE);
			resObj.put("responseData",list);
		}
		else{
			resObj.put("requestId", requestId);
			resObj.put("responseCode", ScratchErrors.SUCCESS_CODE);
			resObj.put("responseData","Ordered detail not found ! ");
		}
		
	}

	private void validateRequestData(String requestData, String tpUserId, int requestId, String orderReferenceId,
			String orderStatus) throws ScratchException {
		if(requestData == null || requestData.trim().length() < 1 ){
			throw new ScratchException(ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE, ScratchErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
		}
		else if(requestId==0 || tpUserId==null){
			throw new ScratchException(ScratchErrors.INVALID_REQUEST_DATA_ERROR_CODE, ScratchErrors.INVALID_REQUEST_DATA_ERROR_MESSAGE);
		}
	}
	
	
	@Path("/orderInventory")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String orderInventory(String reqData) {
		responseDataOfOrder = new JsonObject();
		try {
			isRequestedDataExisting(reqData);
			requestObj = new JsonParser().parse(reqData).getAsJsonObject();
			orderCartBean = new Gson().fromJson(requestObj, OrderCartBean.class);
			validateMandatoryParamsOfRequest();
			generateOrderOfRequest();
		} catch (LMSException e) {
			setRespCodeOfLMSExceptionForOrderCart(e);
		} catch (Exception e) {
			setRespCodeOfGeneralExceptionForOrderCart();
		}
		return new Gson().toJson(responseDataOfOrder);
	}

	private void isRequestedDataExisting(String reqData) throws LMSException {
		if (reqData == null || reqData.trim().length() < 1) {
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
		}
	}

	private void generateOrderOfRequest() throws LMSException, JSONException {
		DaoBean daoBean = getRetailerIdAndOrgId();
		int agentOrgID = scracthService.getAgentOrganizationId(orderCartBean.getTpUserId());
		List<GameBean> cartList= createCartOfOrder(orderCartBean.getOrderList());
		orderId = orderHelper.generateOrder(agentOrgID, daoBean.getUserId(), daoBean.getUserOrgId(), cartList);
		if(orderId!=-1){
			setSuccessResponseData();
		}else{
			setFailureResponseData();
		}
	}

	private DaoBean getRetailerIdAndOrgId() throws JSONException, LMSException {
		DaoBean daoBean = scracthService.getRetailerData(orderCartBean.getTpUserId());
		if(daoBean ==null){
		  setFailureResponseData();
		}
		return daoBean;
	}
	
	public List<GameBean> createCartOfOrder(List<OrderListBean> orderListBean) {
		List<GameBean> gameCart = new ArrayList<GameBean>();
		GameBean cartBean = null;
		for (Iterator<OrderListBean> iterator = orderListBean.iterator(); iterator.hasNext();) {
			OrderListBean orderList = (OrderListBean) iterator.next();
			cartBean = new GameBean();
			cartBean.setGameId(orderList.getGameId());
			cartBean.setOrderedQty(orderList.getNoOfItems());
			gameCart.add(cartBean);
		}		
		System.out.println("size of gameCart = " + gameCart.size());
		if (gameCart.isEmpty()) {
			return null;
		} else {
			return gameCart;
		}
	}

	private void validateMandatoryParamsOfRequest() throws LMSException {
		orderCartBean.validateRequestId();
		orderCartBean.validateTpUserId();
		orderCartBean.validateOrderList();
	}

	private void setRespCodeOfLMSExceptionForOrderCart(LMSException e) {
		responseDataOfOrder.addProperty("responseCode", e.getErrorCode());
		responseDataOfOrder.addProperty("responseMsj", e.getErrorMessage());
	}

	private void setRespCodeOfGeneralExceptionForOrderCart() {
		responseDataOfOrder.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		responseDataOfOrder.addProperty("responseMsj", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}
	
	private void setSuccessResponseData() throws JSONException{
		JsonObject responseData = new JsonObject();
		responseData.addProperty("orderReferenceId", orderId);
		responseDataOfOrder.addProperty("requestId", orderCartBean.getRequestId());
		responseDataOfOrder.addProperty("responseCode", 200);
		responseDataOfOrder.addProperty("responseMsj", "Order Created Successfully");
		responseDataOfOrder.add("responseData", responseData);
	}
	
    private void setFailureResponseData() throws JSONException{
    	responseDataOfOrder.addProperty("requestId", orderCartBean.getRequestId());
		responseDataOfOrder.addProperty("responseCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
		responseDataOfOrder.addProperty("responseMsj", "Requested data is not existing");
	}
}
