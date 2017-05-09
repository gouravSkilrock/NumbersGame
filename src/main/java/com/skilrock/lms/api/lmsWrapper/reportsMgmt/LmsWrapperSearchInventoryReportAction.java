package com.skilrock.lms.api.lmsWrapper.reportsMgmt;

import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperConsNNonConsDetailBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperSearchInventoryRequestDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperSearchInventoryResponseDataBean;
import com.skilrock.lms.api.lmsWrapper.common.InventoryHelper;
import com.skilrock.lms.api.lmsWrapper.common.WrapperUtility;
import com.skilrock.lms.beans.ConsNNonConsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.inventoryMgmt.ConsNNonConsInvHelper;

public class LmsWrapperSearchInventoryReportAction {

	public LmsWrapperSearchInventoryResponseDataBean consNonConsSearchInvDetail(LmsWrapperSearchInventoryRequestDataBean searchInventoryReqBean) throws LMSException{
		
		LmsWrapperSearchInventoryResponseDataBean searchInvResponseBean=new LmsWrapperSearchInventoryResponseDataBean();
		
		InventoryHelper.setInventoryDataToMap();
		int invId=-1;
		int brandId=-1;
		String modelId="-1";
		int agentUserId=-1;
		if(searchInventoryReqBean.getAgtOrgId() >0)
		 agentUserId=Integer.parseInt(WrapperUtility.getAgentOrgIdFromAgentUserId(String.valueOf(searchInventoryReqBean.getAgtOrgId())));
		
		if("NON_CONS".equals(searchInventoryReqBean.getInvType())){
		if(!"NA".equals(searchInventoryReqBean.getInvName())){
			 invId=Integer.parseInt(InventoryHelper.inventoryNameMap.get(searchInventoryReqBean.getInvName()));
		}
       if(!"NA".equals(searchInventoryReqBean.getBrandName())){
    	   brandId=Integer.parseInt(InventoryHelper.brandNameMap.get(searchInventoryReqBean.getBrandName()));
		}
      if(!"NA".equals(searchInventoryReqBean.getModelName())){
    	  modelId=InventoryHelper.modelNameMap.get(searchInventoryReqBean.getModelName());
        }
			 
		}else{
			if(!"NA".equals(searchInventoryReqBean.getInvName())){
				 invId=Integer.parseInt(InventoryHelper.consInventoryNameMap.get(searchInventoryReqBean.getInvName()));
			}
	       if(!"NA".equals(searchInventoryReqBean.getBrandName())){
	    	   brandId=Integer.parseInt(InventoryHelper.brandNameMap.get(searchInventoryReqBean.getBrandName()));
			}
	      if(!"NA".equals(searchInventoryReqBean.getModelName())){
	    	  modelId=InventoryHelper.consModelIdMap.get(searchInventoryReqBean.getModelName());
	        }
		}
		
		 List<ConsNNonConsBean> invDetList = null;
		 ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
		 
		 
		 if("SEARCH_INVENTORY".equals(searchInventoryReqBean.getReportType())){
			invDetList = helper.fetchInvntoryCount(searchInventoryReqBean.getOwnerType(), agentUserId, searchInventoryReqBean.getRetOrgId(),
					searchInventoryReqBean.getInvType(), invId, brandId, modelId, searchInventoryReqBean.getSign(), searchInventoryReqBean.getCount());
		 }else{
			 int ownerId=-1;
			 if("AGENT".equals(searchInventoryReqBean.getOwnerType())){
			
			 if(searchInventoryReqBean.getOwnerId()>0){
				 ownerId=Integer.parseInt(WrapperUtility.getAgentOrgIdFromAgentUserId(String.valueOf(searchInventoryReqBean.getOwnerId())));
			 }
			 }else{
				 ownerId=WrapperUtility.getOrgIdFromOrgType(searchInventoryReqBean.getOwnerType());
			 }
			 
			 invDetList = helper.fetchInvntoryWiseDetail(ownerId, searchInventoryReqBean.getInvType(), invId,
						brandId, modelId);
		 }
			ConsNNonConsBean consNNonConsBean=null;
			List<LmsWrapperConsNNonConsDetailBean> wrapperConsNNonConsList=new ArrayList<LmsWrapperConsNNonConsDetailBean>();
			for(int i=0;i<invDetList.size();i++){
				consNNonConsBean=invDetList.get(i);
				LmsWrapperConsNNonConsDetailBean wrapperConsNNonConsBean=new LmsWrapperConsNNonConsDetailBean();
				wrapperConsNNonConsBean.setBrandId(consNNonConsBean.getBrandId());
				wrapperConsNNonConsBean.setBrandName(consNNonConsBean.getBrandName());
				wrapperConsNNonConsBean.setCost(consNNonConsBean.getCost());
				wrapperConsNNonConsBean.setCount(consNNonConsBean.getCount());
				wrapperConsNNonConsBean.setInvId(consNNonConsBean.getInvId());
				wrapperConsNNonConsBean.setInvName(consNNonConsBean.getInvName());
				wrapperConsNNonConsBean.setInvType(consNNonConsBean.getInvType());
				wrapperConsNNonConsBean.setModelId(consNNonConsBean.getModelId());
				wrapperConsNNonConsBean.setModelName(consNNonConsBean.getModelName());
				wrapperConsNNonConsBean.setOwnerId(consNNonConsBean.getOwnerId());
				wrapperConsNNonConsBean.setOwnerName(consNNonConsBean.getOwnerName());
				wrapperConsNNonConsBean.setOwnerType(consNNonConsBean.getOwnerType());
				wrapperConsNNonConsBean.setSerialNo(consNNonConsBean.getSerialNo());
				
				wrapperConsNNonConsList.add(wrapperConsNNonConsBean);
			}
			
			searchInvResponseBean.setConsNNonConsDataBeanList(wrapperConsNNonConsList);
			return searchInvResponseBean;
		
	}
}

