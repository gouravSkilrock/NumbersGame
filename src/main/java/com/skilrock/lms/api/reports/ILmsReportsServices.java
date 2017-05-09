package com.skilrock.lms.api.reports;

import com.skilrock.lms.api.reports.beans.LmsApiReportConsolidateGameDataBean;
import com.skilrock.lms.api.reports.beans.LmsApiReportRequestDataBean;

public interface ILmsReportsServices {
	LmsApiReportConsolidateGameDataBean getDrawGameReports(LmsApiReportRequestDataBean lmsApiReportRequestDataBean);
	
}
