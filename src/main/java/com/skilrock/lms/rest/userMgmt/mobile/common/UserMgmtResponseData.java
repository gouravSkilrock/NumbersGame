package com.skilrock.lms.rest.userMgmt.mobile.common;

import java.util.List;

import com.skilrock.lms.userMgmt.javaBeans.LmsCityDataBean;
import com.skilrock.lms.userMgmt.javaBeans.LmsStateDataBean;
import com.skilrock.lms.userMgmt.javaBeans.LmsUserDataBean;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class UserMgmtResponseData {
	public static JSONObject getRetailerDataJson(
			List<LmsUserDataBean> lmsUserInfoList) {
		JSONObject finalJsonObject = new JSONObject();
		if (lmsUserInfoList.size() == 0) {
			finalJsonObject.put("isSuccess", false);
			finalJsonObject.put("errorMsg", "No Retailers Available");
		} else {
			JSONArray retailerDataArray = new JSONArray();
			JSONObject retailerData = null;
			LmsUserDataBean lmsUserDataBean = null;
			for (int iLoop = 0; iLoop < lmsUserInfoList.size(); iLoop++) {
				retailerData = new JSONObject();
				lmsUserDataBean = lmsUserInfoList.get(iLoop);

				retailerData.put("firstName", lmsUserDataBean.getFirstName());
				retailerData.put("lastName", lmsUserDataBean.getLastName());
				retailerData.put("email_id", lmsUserDataBean.getEmailId());
				retailerData.put("addr_1", lmsUserDataBean.getAddress_1());
				retailerData.put("addr_2", lmsUserDataBean.getAddress_2());
				retailerData.put("latitude", lmsUserDataBean.getLatitude());
				retailerData.put("longitude", lmsUserDataBean.getLongitude());
				retailerData.put("phoneNbr", lmsUserDataBean.getPhoneNbr());
				retailerData.put("mobileNbr", lmsUserDataBean.getMobileNbr());

				retailerDataArray.add(retailerData);
			}
			finalJsonObject.put("isSuccess", true);
			finalJsonObject.put("errorMsg", "");
			finalJsonObject.put("retailerList", retailerDataArray);
		}
		return finalJsonObject;
	}

	public static JSONObject getStateDataJson(
			List<LmsStateDataBean> lmsStateInfoList) {
		JSONObject finalJsonObject = new JSONObject();
		if (lmsStateInfoList.size() == 0) {
			finalJsonObject.put("isSuccess", false);
			finalJsonObject.put("errorMsg", "No State Available");
		} else {
			JSONArray stateDataArray = new JSONArray();
			JSONObject stateData = null;
			for (int iLoop = 0; iLoop < lmsStateInfoList.size(); iLoop++) {
				stateData = new JSONObject();

				stateData.put("stateCode", lmsStateInfoList.get(iLoop)
						.getStateCode());
				stateData.put("stateName", lmsStateInfoList.get(iLoop)
						.getStateName());
				stateDataArray.add(stateData);
			}
			finalJsonObject.put("isSuccess", true);
			finalJsonObject.put("errorMsg", "");
			finalJsonObject.put("stateList", stateDataArray);
		}
		return finalJsonObject;
	}

	public static JSONObject getCityDataJson(
			List<LmsCityDataBean> lmsCityInfoList) {
		JSONObject finalJsonObject = new JSONObject();
		if (lmsCityInfoList.size() == 0) {
			finalJsonObject.put("isSuccess", false);
			finalJsonObject.put("errorMsg", "No City Available");
		} else {
			JSONArray cityDataArray = new JSONArray();
			JSONObject cityData = null;
			for (int iLoop = 0; iLoop < lmsCityInfoList.size(); iLoop++) {
				cityData = new JSONObject();

				cityData.put("cityCode", lmsCityInfoList.get(iLoop)
						.getCityCode());
				cityData.put("cityName", lmsCityInfoList.get(iLoop)
						.getCityName());
				cityDataArray.add(cityData);
			}
			finalJsonObject.put("isSuccess", true);
			finalJsonObject.put("errorMsg", "");
			finalJsonObject.put("cityList", cityDataArray);
		}
		return finalJsonObject;
	}
}
