package com.skilrock.lms.coreEngine.ola;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SendSMS extends Thread{
	private String message ;
	private String phoneNumber;
	private boolean isMultiple;
	private ArrayList<String> msgList;
	private ArrayList<String> phNbrList;
	public SendSMS(String msg,String plrPhoneNumber){
		this.message=msg;
		this.phoneNumber=plrPhoneNumber;
		this.isMultiple=false;
	}
	public SendSMS(ArrayList<String> msgList,ArrayList<String> phNbrList){
		this.msgList=msgList;
		this.phNbrList=phNbrList;
		this.isMultiple=true;
	}
	
	private String sendSMS(String msg,String phoneNumber){
		try
		{ 
			URL url = new URL("http://www.unicel.in/SendSMS/sendmsg.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			String urlStr ="uname=Serntytr&pass=Stpl2011&send=1234&dest="+phoneNumber+"&msg="+msg+"&concat=1"; 
			System.out.println("Arguments: "+urlStr);
			wr.write(urlStr);
			wr.flush();
			InputStream iStream =conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
			StringBuilder sb =new StringBuilder();
			String line =null;
					while ((line = reader.readLine()) != null) {
							sb.append(line);
							}
					
			  String deliveryMsg = sb.toString();
			  return  deliveryMsg;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}	
		
public void run(){
	
	try{
		if(isMultiple){
			for(int i=0;i<phNbrList.size();i++){
				System.out.println(" Send msg on number :"+phNbrList.get(i)+" msg is:"+msgList.get(i));
				System.out.println("Msg Length"+msgList.get(i).length());
				String deliveryMsg = sendSMS(msgList.get(i),phNbrList.get(i));
				System.out.println("Delivery Msg :"+deliveryMsg);
			}
		}
		else{
			System.out.println("Message"+message);
			System.out.println("Msg Length"+message.length());
			String deliveryMsg = sendSMS(message,phoneNumber);
			System.out.println("Delivery Msg :"+deliveryMsg);
		}
		
		
	}
	catch(Exception e){
		
		e.printStackTrace();
	}
	
	
}

}
