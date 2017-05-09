package com.skilrock.lms.web.common.drawables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;

public class CommonMethods {
	
	static Log logger = LogFactory.getLog(CommonMethods.class);
	public static String path = null;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat dateFormatForGraph = new SimpleDateFormat("dd-MMM");

	public static String getListFromData(List<String> list) {
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < list.size(); i++) 
			builder.append("'" + list.get(i) + "',");

		return builder.substring(0, builder.lastIndexOf(",")).concat("]");
	}

	public static String getListFromDataForDouble(List<Double> list) {
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < list.size(); i++) 
			builder.append("" + list.get(i) + ",");

		return builder.substring(0, builder.lastIndexOf(",")).concat("]");
	}

	public static String getArrayAsString(String [] itemArray){
		StringBuilder items=new StringBuilder();
		if(itemArray==null)
			return items.append("").toString();
		
		for(int i=0; i<itemArray.length ;i++)
			items.append("'").append(itemArray[i].split(",")[0]).append("',");	
		
		return items.replace(items.lastIndexOf(","), items.length(), "").toString();
	}
	
	public static void DeleteFiles(String filePath , boolean isSpecific , String fileName) {
		File file = new File(filePath);
		
		if(isSpecific){
			if(file.isDirectory()) {
				for (File f : file.listFiles()) {
					logger.info("Is file named " + f.getName());
					if (!f.isDirectory() && f.getName().equals(fileName))
						logger.info("Is file named " + f.getName() + " deleted " + f.delete());
				}
			}
		}else if(file.isDirectory()) {
			for (File f : file.listFiles()) {
				if (!f.isDirectory())
					logger.info("Is file named " + f.getName() + " deleted " + f.delete());
			}
		}
	}
	
	public static void prepareFiles(String fileName, String fileContent,
			String filePath) {

		FileWriter file = null;
		try {
			file = new FileWriter(filePath + fileName + ".html");
			file.write(fileContent);
		}catch (IOException e) {
			logger.error("IOEXCEPTION  : -  " , e);
		}catch (Exception e) {
			logger.error("EXCEPTION  : -  " , e);
		} finally {
			if (file != null) {
				try {
					file.flush();
					file.close();
				} catch (IOException e) {
					logger.error("IOEXCEPTION  : -  " , e);
				}catch (Exception e) {
					logger.error("EXCEPTION  : -  " , e);
				}

			}
		}
	}
	
	
	public static synchronized void prepareCSVFile(String fileName, Map<String ,String> fileContentMap,
			String filePath) {

		File file = null;
		PrintStream out = null;
		try {
			file = new File(filePath);
			if (!file.exists()) 
				file.mkdirs(); // for multiple directories
			
			file = new File(filePath + fileName + ".csv");
			if (file.exists()) {
				file.delete();
				file = new File(filePath+fileName);
			}
			
			out = new PrintStream(file);
			for(Map.Entry<String, String> entry : fileContentMap.entrySet()){
			out.println(entry.getKey()+','+entry.getValue());
			}
			
		}catch (IOException e) {
			logger.error("IOEXCEPTION  : -  " , e);
		}catch (Exception e) {
			logger.error("EXCEPTION  : -  " , e);
		} finally {
			if (file != null) {
				try {
					out.flush();
					out.close();
				}catch (Exception e) {
					logger.error("EXCEPTION  : -  " , e);
				}

			}
		}
	}
	
	public static String getDateForDrawables(String date) throws ParseException{
		Timestamp t1 = new Timestamp(sdf.parse(date).getTime());
		return dateFormatForGraph.format(t1);
	}
	
	public static String perpDateForDashBoardChartsAndGraphs(boolean isDayWise , boolean isWeekWise, int gapValue, int noOfWksOrDys , boolean isForPeakDay){

		Calendar c=Calendar.getInstance();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(isForPeakDay || isDayWise){
			c.set(Calendar.HOUR_OF_DAY,00);
			c.set(Calendar.MINUTE,00);
			c.set(Calendar.SECOND,00);
		}else{
			c.set(Calendar.HOUR_OF_DAY,23);
			c.set(Calendar.MINUTE,59);
			c.set(Calendar.SECOND,59);
		}

		logger.info(df.format(c.getTime()));   

		if(isForPeakDay)
			c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		
		c.set(Calendar.HOUR_OF_DAY,00);
		c.set(Calendar.MINUTE,00);
		c.set(Calendar.SECOND,00);
		
		StringBuilder datesString = new StringBuilder("").append(df.format(c.getTime()).toString()).append("#");
		
		if(isWeekWise)
			c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
			
		
		if(!isDayWise)
		datesString.append(df.format(c.getTime())).append("#");
		
		for(int i=2;i<=noOfWksOrDys ; i++){
			c.set(Calendar.HOUR_OF_DAY,0);
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);
			c.add(Calendar.DATE,gapValue);
			datesString.append(df.format(c.getTime())).append("#");
		}
		logger.info(datesString);
		return datesString.substring(0, datesString.length()-1);
	}
	
	public static boolean isArchTablesRequired(String lastDate,String archDate){
		boolean isArchTablesReq = false;
		if(lastDate.trim().compareTo(archDate)<=0)
			isArchTablesReq=true;
		return isArchTablesReq;
	}
	
	public static synchronized long getCurrentTimeInMilis(){
		return (System.currentTimeMillis()+Long.parseLong(com.skilrock.lms.common.Utility.getPropertyValue("TIME_ELAPSE_FOR_DASHBOARD_NEWCALL").toString())*60*1000);
	}

	public static String  perpareCaptionsForDrawables(String chartTitle  ,String chartSubTitle , String xAxisTitle, String yAxisTitle ){
		StringBuilder builder =  new StringBuilder();
		return builder.append((chartTitle==null?"":chartTitle)).append(",").append((chartSubTitle==null?"":chartSubTitle)).append(",").append((xAxisTitle==null?"":xAxisTitle)).append(",").append((yAxisTitle==null?"":yAxisTitle)).append(",").toString();
	}
	
	public static boolean isArchData(String startDate,StringBuilder archivingDate) {
		boolean isArchData = true;
		SimpleDateFormat sdf1 =new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		Calendar calStart = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		Connection con = null;
		PreparedStatement getPergDate = null;
		ResultSet rs2 = null;
		try {
			con = DBConnect.getConnection();
			Date  lastArchDate =new Date(sdf.parse("2000-01-01").getTime());
			 getPergDate = con.prepareStatement("select alldate from datestore order by alldate desc limit 1");
			 rs2 = getPergDate.executeQuery();
			if (rs2.next()) {
								lastArchDate = rs2.getDate("alldate");
							}
			
			if(lastArchDate!=null){
									calStart.setTime(lastArchDate);
								  }
			calLast.setTime(sdf1.parse(startDate));
			archivingDate.append(lastArchDate.toString());
			if (calStart.compareTo(calLast)>=0) {
				isArchData = true;
			} else {
				isArchData = false;
			}
			

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			DBConnect.closeResource(con,getPergDate,rs2);
		}
		return isArchData;

	}
}

