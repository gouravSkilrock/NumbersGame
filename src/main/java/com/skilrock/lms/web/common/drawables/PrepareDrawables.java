package com.skilrock.lms.web.common.drawables;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.skilrock.lms.beans.TitleBeanForDrawables;

public class PrepareDrawables {
	
	public static String  prepareDrawablesJSON(TitleBeanForDrawables titleBeanForDrawables , String drawableType , LinkedHashMap<String, List<Object>> seriesMap ,LinkedHashMap<String, Object> pieSeriesMap){
		
		List<Object> objectList = new ArrayList<Object>();
		JSONArray responseJSONArray = null;
		objectList.add(titleBeanForDrawables);
		
		if (DrawablesPermittedType.contains(drawableType)) {
			switch (DrawablesPermittedType.valueOf(drawableType)
					.getStatus()) {
			case 1:
				prepareColumnJSON(objectList, seriesMap);
				break;
			case 2:
				prepareLineJSON(objectList , seriesMap);
				break;
			case 3:
				preparePieJSON(objectList , pieSeriesMap);
				break;
			default:
				// NOT REQUIRED BUT PLACED
				break;
			}
		} else {
			//
		}
		responseJSONArray = JSONArray.fromObject(objectList);
		return responseJSONArray.toString();
	}
	
	private static void prepareColumnJSON(List<Object> objectList , LinkedHashMap<String, List<Object>> seriesMap){

		JSONObject seriesData = null;
		for(Map.Entry<String, List<Object>> entry : seriesMap.entrySet()){
			seriesData = new JSONObject();	
			seriesData.put("name", entry.getKey());
			seriesData.put("data", entry.getValue());
			objectList.add(seriesData);
		}
	}

	private static void prepareLineJSON(List<Object> objectList ,LinkedHashMap<String, List<Object>> seriesMap){

		JSONObject seriesData = null;
		for(Map.Entry<String, List<Object>> entry : seriesMap.entrySet()){
			seriesData = new JSONObject();	
			seriesData.put("name", entry.getKey());
			seriesData.put("data", entry.getValue());
			objectList.add(seriesData);
		}
	}

	private static void preparePieJSON(List<Object> objectList , LinkedHashMap<String, Object> pieSeriesMap){
		
		int count  = 0;
		JSONObject outerJson = null;
		JSONObject innerJson = null;
		List<JSONObject> innerJsonList=  new ArrayList<JSONObject>();
		for(Map.Entry<String, Object> entry :  pieSeriesMap.entrySet()){
			innerJson = new JSONObject();
			innerJson.put("name",entry.getKey());
			innerJson.put("y",entry.getValue());
			if(count==0){
				innerJson.put("sliced", true);
				//innerJson.put("selected", true);
			}
			innerJsonList.add(innerJson);
			count++;
		}
		
		outerJson = new JSONObject();
		outerJson.put("type", "pie");
		outerJson.put("data", innerJsonList);
		objectList.add(outerJson);
		}
}
