package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.Connection;
import java.util.List;
import com.skilrock.lms.beans.PwtReportBean;

public interface IPwtReportsAgentHelper {

	public PwtReportBean getBOPwtDetail(Connection conn); 

	public List<PwtReportBean> getPwtDetail();

}
