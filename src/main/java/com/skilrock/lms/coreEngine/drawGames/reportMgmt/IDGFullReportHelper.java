package com.skilrock.lms.coreEngine.drawGames.reportMgmt;


import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSException;


public interface IDGFullReportHelper {
	
	public Map<String, String> fetchDGFullReport(String cityCode, String stateCode) throws LMSException;

}