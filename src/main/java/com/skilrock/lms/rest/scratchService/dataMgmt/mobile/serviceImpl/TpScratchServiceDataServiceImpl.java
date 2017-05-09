package com.skilrock.lms.rest.scratchService.dataMgmt.mobile.serviceImpl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.scratchService.dataMgmt.controllerImpl.ScratchGameDataControllerImpl;

@Path("/scratchService/dataMgmt")
public class TpScratchServiceDataServiceImpl {
	
	
	@Path("/getScratchGameData")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScratchGameData(String requestData){
		
		List<ScratchGameDataBean> scratchGameList = null;
		ScratchGameDataControllerImpl scratchGameDataControllerImpl = null;
		try{
			scratchGameDataControllerImpl = ScratchGameDataControllerImpl.getSingleInstance();
			scratchGameList = scratchGameDataControllerImpl.getScratchGameList();
			
		}catch (GenericException e) {
			e.printStackTrace();
			return Response.status(e.getErrorCode()).entity(LMSErrorProperty.getPropertyValue(e.getErrorCode())).build();
		}
		return Response.ok().entity(scratchGameList).build();
	}

	
}
