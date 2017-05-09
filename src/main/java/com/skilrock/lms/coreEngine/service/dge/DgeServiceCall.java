package com.skilrock.lms.coreEngine.service.dge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;

public class DgeServiceCall {/*
	
	public static ServiceResponse callDgServiceResponse(ServiceRequest sReq){
		
		 String urlString="http://localhost:8080/DrawGameEngine_Scheduler/services/"+sReq.getServiceName()+"/"+sReq.getServiceMethod();
		 ServiceResponse obj=new ServiceResponse();
		 try{
	        URL url=new URL(urlString);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoOutput(true);
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setRequestProperty("principal", "E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
	        connection.setRequestProperty("credentials", "p@55w0rd");
	        connection.setRequestProperty(
	        	    "Cookie","JSESSIONID=" + "12ECA807E0C31320DE59FD6E355369A");
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	        out.write("{\"clientName\": \"Mr.Client\",\"name\":\"smuit\"}");
	        out.close();

	        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String decodedString;
	        while ((decodedString = in.readLine()) != null) {
	        System.out.println(decodedString);
	        Gson gson = new Gson();
	        
	        
	        JsonParser parser = new JsonParser();
	        JsonObject o = (JsonObject)parser.parse(decodedString);
	        obj.setIsSuccess(o.get("isSuccess").getAsBoolean());
	        obj.setResponseData(o.get("responseData"));
	        
	        
	         //obj = gson.fromJson(decodedString, ServiceResponse.class);
	        System.out.println(obj);
	        }
	        in.close();
}catch (MalformedURLException e) {
	// TODO: handle exception
}catch (ProtocolException e) {
	// TODO: handle exception
}catch (IOException e) {
	// TODO: handle exception
}
		return obj;
		
	}
	
    public static void main(String[] args) throws Exception{

        String urlString="http://localhost:8080/DrawGameEngine_Scheduler/services/myresource/sendReceiveJson";

        URL url=new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("principal", "E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
        connection.setRequestProperty("credentials", "p@55w0rd");
        connection.setRequestProperty(
        	    "Cookie","JSESSIONID=" + "12ECA807E0C31320DE59FD6E355369A");
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write("{\"clientName\": \"Mr.Client\",\"name\":\"smuit\"}");
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
        System.out.println(decodedString);
        }
        in.close();
}
*/}
