package com.skilrock.lms.rest.services.common.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.db.DBConnect;


public class TpSchedulerDaoImpl {
	
	private static Logger logger = LoggerFactory.getLogger(TpSchedulerDaoImpl.class);
	private volatile static TpSchedulerDaoImpl tpSchedulerDaoImpl = null;

	private TpSchedulerDaoImpl(){}
	public static TpSchedulerDaoImpl getInstance() {
		if (tpSchedulerDaoImpl == null) {
			synchronized (TpSchedulerDaoImpl.class) {
				if (tpSchedulerDaoImpl == null) {
					logger.info("getInstance(): First time getInstance invoked!");
					tpSchedulerDaoImpl = new TpSchedulerDaoImpl();
				}
			}
		}
		return tpSchedulerDaoImpl;
	}
	
	/**
	 * This function is used to fetch draw schedule from lms sikkim table
	 * @return
	 */
	
	public String[][] fetchDrawTime(boolean isScreenTime){
		
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		List<String> drawGameList = new ArrayList<String>();
		List<String> virtualSportList = new ArrayList<String>();
		String[][] arr = new String[2][];
				
		try{
		con = DBConnect.getConnection();
		if(isScreenTime){
			ps = con.prepareStatement("Select screen_time drawTime,screen_type from st_lms_draw_schedule_master");		
		}else {
			ps = con.prepareStatement("Select draw_time  drawTime,screen_type from st_lms_draw_schedule_master");
		}
	
		rs = ps.executeQuery();
		
		while(rs.next()){
			if(rs.getString("screen_type").equalsIgnoreCase("draw_game"))
			{
				drawGameList.add(rs.getString("drawTime"));
			}else{
				virtualSportList.add(rs.getString("drawTime"));
			}
		}
		arr[0] = drawGameList.toArray(new String[drawGameList.size()]);
		arr[1] = virtualSportList.toArray(new String[virtualSportList.size()]);
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		return arr;
	}
	
	/*
	 * this function is used to get some number of upcoming draws based on the current time
	 */
	
	public String[] getCurrentSchedule(String[] drawArray,int noOfUpcomingDraw){
		List<String> timeList = new ArrayList<String>();
		//String[] drawArray = drawstr.split(", ");
		String[] timeStr = null;
		Calendar cal2 = Calendar.getInstance();
		int j=1;
		for (int i = 0; (i < drawArray.length&&j<=noOfUpcomingDraw); i++) {
			String time = drawArray[i];
			//time = time.replace("[", "");
			//time = time.replace("]", "");
			Calendar cal1 = Calendar.getInstance();
			cal1.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(time.split(":")[0]));
			cal1.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
			cal1.set(Calendar.SECOND, 0);
			if (cal1.after(cal2)) {
				timeList.add(time);
				j++;
			}
		}
		
		timeStr = timeList.toArray(new String[timeList.size()]);
		return timeStr;	
	}

}
