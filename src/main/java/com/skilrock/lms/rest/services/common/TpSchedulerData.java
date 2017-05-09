package com.skilrock.lms.rest.services.common;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.rest.services.common.daoImpl.TpSchedulerDaoImpl;

/*
 * API to fetch full draw schedule timings of draw game and virtual sports
 */
@Path("/schedulerData")

public class TpSchedulerData {
	
	@Path("/fetchSchedule")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchDrawSchedule(){
		JSONObject responseObj = new JSONObject();
		String responseString = null;
		String[] drawGameArr = null;
		String[] goldenRaceArr = null;
		String drawgameURL = null;
		String goldenraceURL = null;
		String[][] getDrawTime = new String[2][];
  		PropertyLoader.loadProperties("RMS/LMS.properties");
		getDrawTime = TpSchedulerDaoImpl.getInstance().fetchDrawTime(true);
		drawGameArr = getDrawTime[0];
		goldenRaceArr = getDrawTime[1];
		
		drawgameURL = PropertyLoader.getProperty("drawGameURL");
		goldenraceURL = PropertyLoader.getProperty("goldenRaceURL");
		responseObj.put("drawGame", drawGameArr);
		responseObj.put("goldenRace", goldenRaceArr);
		responseObj.put("drawGameURL", drawgameURL);
		responseObj.put("goldenRaceURL", goldenraceURL);
		responseString = new Gson().toJson(responseObj);
		
		return responseString;
	}
	/*
	 * API to fetch limited draw schedule timings of draw game and virtual sports from current time 
	 */
	@Path("/fetchUpcomingDrawSchedule")
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String fetchCurrentDrawSchedule(){
		JSONObject responseObj = new JSONObject();
		String responseString = null;
		String[] drawGameArr = null;
		String[] goldenRaceArr = null;
		String[] curDrawGameArr = null;
		String[] curGoldenRaceArr = null;
		String drawgameURL = null;
		String goldenraceURL = null;
		String[][] getDrawTime = new String[2][];
		PropertyLoader.loadProperties("RMS/LMS.properties");
		getDrawTime = TpSchedulerDaoImpl.getInstance().fetchDrawTime(false);
		drawGameArr = getDrawTime[0];
		goldenRaceArr = getDrawTime[1];
		int noOfupcomingDraw=5;
		curDrawGameArr = TpSchedulerDaoImpl.getInstance().getCurrentSchedule(drawGameArr,noOfupcomingDraw);
		curGoldenRaceArr = TpSchedulerDaoImpl.getInstance().getCurrentSchedule(goldenRaceArr,noOfupcomingDraw);
		
		drawgameURL = PropertyLoader.getProperty("drawGameURL");
		goldenraceURL = PropertyLoader.getProperty("goldenRaceURL");
		responseObj.put("drawGame", curDrawGameArr);
		responseObj.put("goldenRace", curGoldenRaceArr);
		responseObj.put("drawGameURL", drawgameURL);
		responseObj.put("goldenRaceURL", goldenraceURL);
		responseString = new Gson().toJson(responseObj);
		
		return responseString;
	}
	
}
