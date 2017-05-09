package com.skilrock.lms.scheduler;


import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.CreateTransactionForCommissionHelper;
import com.skilrock.lms.coreEngine.ola.NetGamingForRummyHelper;


public class OlaCommUpdateWeekly implements Job {
	private static	Log logger = LogFactory.getLog(OlaCommUpdateWeekly.class);
	public OlaCommUpdateWeekly() {
	}
		
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		 
		
		try {	
			
			
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("OLA_Comm_Update_Weekly_SCHEDULER").isActive()){
					
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("OLA_Comm_Update_Weekly_SCHEDULER").getJobId());
						
						Calendar calStart=Calendar.getInstance();
						ServletContext sc = AutoQuartzMain.scx;
						String commUpdateType = (String) sc
						.getAttribute("OLA_COMM_UPDATE_TYPE");
						int settlementAfter=Integer.parseInt((String) sc
						.getAttribute("OLA_NETGAMING_SETTLEMENT_AFTER"));
						String olaNetGamingUpdateMode=(String) sc.getAttribute("approveNetGamingUpdateMode");
						String olaNetGamingDistributionModel=(String) sc.getAttribute("OLA_NETGAMING_DISTRIBUTION_MODEL");
						System.out.println("Net Gaming Model:"+olaNetGamingDistributionModel);
						  if("WEEKLY".equalsIgnoreCase(commUpdateType)){
								
								System.out.println("Day Of Week:"+calStart.get(Calendar.DAY_OF_WEEK));
								int dayOfWeek=calStart.get(Calendar.DAY_OF_WEEK);
								if(dayOfWeek == 2){
									NetGamingForRummyHelper helper=new NetGamingForRummyHelper();
									
									
									//Calendar calStart=Calendar.getInstance();
									calStart.add(Calendar.DAY_OF_MONTH, -7);
									Date startDate=new Date(calStart.getTime().getTime());
									System.out.println("start_date:"+startDate);
									
									calStart.add(Calendar.DAY_OF_MONTH,6);
									Date endDate=new Date(calStart.getTime().getTime());
									System.out.println("end_date:"+endDate);
									Date lastDate=helper.getLastDateForPlayerCommission();
									Calendar cal = Calendar.getInstance();
									cal.setTime(lastDate);
									//int dayofDate=cal.get(Calendar.DAY_OF_MONTH);// lastDate.getDate();
									cal.add(Calendar.DAY_OF_MONTH,1);
									lastDate=new Date(cal.getTime().getTime());//setDate(dayofDate+1);

									if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDate)) < 0){
										return;
									}else if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDate))==0){
										helper.updateNetGamingDataWeeklyWise(1,2,olaNetGamingDistributionModel,settlementAfter,startDate,endDate);
										
										
										if("AUTO".equalsIgnoreCase(olaNetGamingUpdateMode)){
										/*Calendar calender=Calendar.getInstance();
										calender.add(Calendar.DAY_OF_MONTH, -7);
										Date startDate=new Date(calender.getTime().getTime());
										System.out.println("start_date:"+startDate);
										
										calender.add(Calendar.DAY_OF_MONTH,6);
										Date endDate=new Date(calender.getTime().getTime());
										System.out.println("end_date:"+endDate);*/
										//CreateTransactionForCommissionHelper transHelper=new CreateTransactionForCommissionHelper();
										CreateTransactionForCommissionHelper.retOlaCommissionTransaction(startDate,endDate, commUpdateType,olaNetGamingUpdateMode);
										}
									}else{
										Calendar calendar = Calendar.getInstance();

									    calendar.setTime(lastDate);
									    calendar.add(Calendar.DAY_OF_MONTH, 6);
										Date transactionLastDate=new Date(calendar.getTime().getTime());
										while(getZeroTimeDate(endDate).compareTo(getZeroTimeDate(transactionLastDate)) >=0 ){

											helper.updateNetGamingDataWeeklyWise(1,2,olaNetGamingDistributionModel,settlementAfter,lastDate,transactionLastDate);
											
											
											if("AUTO".equalsIgnoreCase(olaNetGamingUpdateMode)){
											/*Calendar calender=Calendar.getInstance();
											calender.add(Calendar.DAY_OF_MONTH, -7);
											Date startDate=new Date(calender.getTime().getTime());
											System.out.println("start_date:"+startDate);
											
											calender.add(Calendar.DAY_OF_MONTH,6);
											Date endDate=new Date(calender.getTime().getTime());
											System.out.println("end_date:"+endDate);*/
											 //CreateTransactionForCommissionHelper transHelper=new CreateTransactionForCommissionHelper();
											CreateTransactionForCommissionHelper.retOlaCommissionTransaction(lastDate,transactionLastDate, commUpdateType, olaNetGamingUpdateMode);
											}
										
											calendar.add(Calendar.DAY_OF_MONTH, 1);
											lastDate=new Date(calendar.getTime().getTime());
											calendar.add(Calendar.DAY_OF_MONTH, 6);
											transactionLastDate=new Date(calendar.getTime().getTime());
										}
										
										
									}
									
								}
						      }else if("MONTHLY".equalsIgnoreCase(commUpdateType))			      
						      {
						    	  System.out.println("Day of Month:"+calStart.get(Calendar.DAY_OF_MONTH));
						    	  int dayOfMonth=calStart.get(Calendar.DAY_OF_MONTH);
						    	  if(dayOfMonth ==1){
						    		  Calendar calendar = Calendar.getInstance();
						    		  int year = calendar.get(Calendar.YEAR);
						    		  System.out.println(year);
						    		  int month = calendar.get(Calendar.MONTH);
						    		  System.out.println(month);
						    		  int date = 1;
						    		  
						    		 
						    		  calendar.set(year, month-1, date);
						    		  
						    		  Date startDate=new Date(calendar.getTime().getTime());
						    	 		System.out.println("start_date:"+startDate);
						    	 		int noOfdays=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						    			  System.out.println(noOfdays);
						    			  calendar.set(year, month-1, noOfdays);
						    			  Date endDate=new Date(calendar.getTime().getTime());
						    		 		System.out.println("end_date:"+endDate);
						    		  NetGamingForRummyHelper helper=new NetGamingForRummyHelper();
						  			helper.updateNetGamingDataMonthlyWise(1,2,olaNetGamingDistributionModel,settlementAfter,startDate,endDate);
						    	  
						    		if("AUTO".equalsIgnoreCase(olaNetGamingUpdateMode)){
						    			/*Calendar calendar = Calendar.getInstance();
						    			  int year = calendar.get(Calendar.YEAR);
						    			  System.out.println(year);
						    			  int month = calendar.get(Calendar.MONTH);
						    			  System.out.println(month);
						    			  int date = 1;
						    			  
						    			 
						    			  calendar.set(year, month-1, date);
						    			  
						    			  Date startDate=new Date(calendar.getTime().getTime());
						    		 		System.out.println("start_date:"+startDate);
						    		 		int noOfdays=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						    				  System.out.println(noOfdays);
						    				  calendar.set(year, month-1, noOfdays);
						    				  Date endDate=new Date(calendar.getTime().getTime());
						    			 		System.out.println("end_date:"+endDate);*/
										 //CreateTransactionForCommissionHelper transHelper=new CreateTransactionForCommissionHelper();
										CreateTransactionForCommissionHelper.retOlaCommissionTransaction(startDate,endDate, commUpdateType, olaNetGamingUpdateMode);
										}
						    	  }
						      }
					
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("OLA_Comm_Update_Weekly_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in OLA_Comm_Update_Weekly_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("OLA_Comm_Update_Weekly_SCHEDULER").getJobId(), errorMsg);
					}
					
					
					
				}
			}
			
			
			
			
			
			
			
		
			}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Date getZeroTimeDate(Date fecha) {
	    Date res = fecha;
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime( fecha );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    res = new Date(calendar.getTime().getTime());

	    return res;
	}

	
	public static void main(String[] args) throws LMSException{
		try {		
			
			String olaNetGamingDistributionModel="CUMMULATIVE";
			System.out.println("Net Gaming Model:"+olaNetGamingDistributionModel);
			
			int settlementAfter=1;
			
			Calendar calStart=Calendar.getInstance();
			// ServletContext sc = AutoQuartzMain.scx;
			String commUpdateType = "WEEKLY";
			String OlaNetGamingUpdateMode="MANUAL";
			  if("WEEKLY".equalsIgnoreCase(commUpdateType)){
					System.out.println("Day Of Week:"+calStart.get(Calendar.DAY_OF_WEEK));
					int dayOfWeek=calStart.get(Calendar.DAY_OF_WEEK);
					if(dayOfWeek == 2){
						NetGamingForRummyHelper helper=new NetGamingForRummyHelper();
						
						
						//Calendar calStart=Calendar.getInstance();
						calStart.add(Calendar.DAY_OF_MONTH, -7);
						Date startDate=new Date(calStart.getTime().getTime());
						System.out.println("start_date:"+startDate);
						
						calStart.add(Calendar.DAY_OF_MONTH,6);
						Date endDate=new Date(calStart.getTime().getTime());
						System.out.println("end_date:"+endDate);
						Date lastDate=helper.getLastDateForPlayerCommission();
						int dayofDate=lastDate.getDate();
						lastDate.setDate(dayofDate+1);
						

						if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDate)) < 0){
							return;
						}else if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDate))==0){
							helper.updateNetGamingDataWeeklyWise(1,2,olaNetGamingDistributionModel,settlementAfter,startDate,endDate);
							
							
							if("AUTO".equalsIgnoreCase(OlaNetGamingUpdateMode)){
							/*Calendar calender=Calendar.getInstance();
							calender.add(Calendar.DAY_OF_MONTH, -7);
							Date startDate=new Date(calender.getTime().getTime());
							System.out.println("start_date:"+startDate);
							
							calender.add(Calendar.DAY_OF_MONTH,6);
							Date endDate=new Date(calender.getTime().getTime());
							System.out.println("end_date:"+endDate);*/
							//CreateTransactionForCommissionHelper transHelper=new CreateTransactionForCommissionHelper();
							CreateTransactionForCommissionHelper.retOlaCommissionTransaction(startDate,endDate, commUpdateType, OlaNetGamingUpdateMode);
							}
						}else{
							Calendar calendar = Calendar.getInstance();

						    calendar.setTime(lastDate);
						    calendar.add(Calendar.DAY_OF_MONTH, 6);
							Date transactionLastDate=new Date(calendar.getTime().getTime());
							while(getZeroTimeDate(endDate).compareTo(getZeroTimeDate(transactionLastDate)) >=0 ){

								helper.updateNetGamingDataWeeklyWise(1,2,olaNetGamingDistributionModel,settlementAfter,lastDate,transactionLastDate);
								
								
								if("AUTO".equalsIgnoreCase(OlaNetGamingUpdateMode)){
								/*Calendar calender=Calendar.getInstance();
								calender.add(Calendar.DAY_OF_MONTH, -7);
								Date startDate=new Date(calender.getTime().getTime());
								System.out.println("start_date:"+startDate);
								
								calender.add(Calendar.DAY_OF_MONTH,6);
								Date endDate=new Date(calender.getTime().getTime());
								System.out.println("end_date:"+endDate);*/
								//CreateTransactionForCommissionHelper transHelper=new CreateTransactionForCommissionHelper();
								CreateTransactionForCommissionHelper.retOlaCommissionTransaction(lastDate,transactionLastDate, commUpdateType, OlaNetGamingUpdateMode);
								}
							
								calendar.add(Calendar.DAY_OF_MONTH, 1);
								lastDate=new Date(calendar.getTime().getTime());
								calendar.add(Calendar.DAY_OF_MONTH, 6);
								transactionLastDate=new Date(calendar.getTime().getTime());
							}
							
							
						}
						
					
						
					}
			      }else{
			    	  System.out.println("Day of Month:"+calStart.get(Calendar.DAY_OF_MONTH));
			    	  int dayOfMonth=calStart.get(Calendar.DAY_OF_MONTH);
			    	  if(dayOfMonth ==1){
			    		  Calendar calendar = Calendar.getInstance();
			    		  int year = calendar.get(Calendar.YEAR);
			    		  System.out.println(year);
			    		  int month = calendar.get(Calendar.MONTH);
			    		  System.out.println(month);
			    		  int date = 1;
			    		  
			    		 
			    		  calendar.set(year, month-1, date);
			    		  
			    		  Date startDate=new Date(calendar.getTime().getTime());
			    	 		System.out.println("start_date:"+startDate);
			    	 		int noOfdays=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			    			  System.out.println(noOfdays);
			    			  calendar.set(year, month-1, noOfdays);
			    			  Date endDate=new Date(calendar.getTime().getTime());
			    		 		System.out.println("end_date:"+endDate);
			    		  NetGamingForRummyHelper helper=new NetGamingForRummyHelper();
			  			helper.updateNetGamingDataMonthlyWise(1,2,olaNetGamingDistributionModel,settlementAfter,startDate,endDate);
			    	  
			    		if("AUTO".equalsIgnoreCase(OlaNetGamingUpdateMode)){
			    			/*Calendar calendar = Calendar.getInstance();
			    			  int year = calendar.get(Calendar.YEAR);
			    			  System.out.println(year);
			    			  int month = calendar.get(Calendar.MONTH);
			    			  System.out.println(month);
			    			  int date = 1;
			    			  
			    			 
			    			  calendar.set(year, month-1, date);
			    			  
			    			  Date startDate=new Date(calendar.getTime().getTime());
			    		 		System.out.println("start_date:"+startDate);
			    		 		int noOfdays=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			    				  System.out.println(noOfdays);
			    				  calendar.set(year, month-1, noOfdays);
			    				  Date endDate=new Date(calendar.getTime().getTime());
			    			 		System.out.println("end_date:"+endDate);*/
							//CreateTransactionForCommissionHelper transHelper=new CreateTransactionForCommissionHelper();
							CreateTransactionForCommissionHelper.retOlaCommissionTransaction(startDate,endDate, commUpdateType, OlaNetGamingUpdateMode);
							}
			    	  }
			      }
			}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
