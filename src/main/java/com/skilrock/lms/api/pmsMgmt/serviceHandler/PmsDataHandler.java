package com.skilrock.lms.api.pmsMgmt.serviceHandler;

import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

public class PmsDataHandler implements RequestHandler {

	public String[] handledRequests() {
		return new String[] { "fetchUserInfo", "fetchScratchGameData",
				"fetchStateList", "fetchCityList", "validateScratchTicket" };
	}

	public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {
		List<String> lmsApiList = Arrays.asList(handledRequests());

		switch (lmsApiList.indexOf(req.getMethod())) {
		case -1:
			return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req
					.getID());
		case 0:
			JSONObject jsonObject = null;
			PmsDataHandlerHelper pmsDataHandlerHelper = null;

			pmsDataHandlerHelper = new PmsDataHandlerHelper();
			jsonObject = pmsDataHandlerHelper.getLmsRetailerJson(req);

			return new JSONRPC2Response(jsonObject, req.getID());
		case 1:
			jsonObject = null;

			pmsDataHandlerHelper = new PmsDataHandlerHelper();
			jsonObject = pmsDataHandlerHelper.getScratchGameJson(req);

			return new JSONRPC2Response(jsonObject, req.getID());
		case 2:
			jsonObject = null;

			pmsDataHandlerHelper = new PmsDataHandlerHelper();
			jsonObject = pmsDataHandlerHelper.getLmsStateDataJson(req);
			return new JSONRPC2Response(jsonObject, req.getID());
		case 3:
			jsonObject = null;

			pmsDataHandlerHelper = new PmsDataHandlerHelper();
			jsonObject = pmsDataHandlerHelper.getLmsCityDataJson(req);
			return new JSONRPC2Response(jsonObject, req.getID());
		case 4:
			jsonObject = null;

			pmsDataHandlerHelper = new PmsDataHandlerHelper();
			jsonObject = pmsDataHandlerHelper.validateScratchTicket(req);
			return new JSONRPC2Response(jsonObject, req.getID());
		default:
			break;
		}
		return null;
	}

	public static JSONRPC2Response getDispatcher(JSONRPC2Request reqIn) {
		Dispatcher dispatcher = new Dispatcher();
		dispatcher.register(new PmsDataHandler());
		return dispatcher.process(reqIn, null);
	}
}
