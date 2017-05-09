package com.skilrock.lms.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OlaNetGamingRowList {

	static Log logger = LogFactory.getLog(OlaNetGamingRowList.class);
	
	List<OlaNetGamingRetailerData> columnList = null;

	public List<OlaNetGamingRetailerData> getColumnList() {
		return columnList;
	}

	public void setColumnList(OlaNetGamingRetailerData column) {
		if(columnList == null){
			columnList = new ArrayList<OlaNetGamingRetailerData>();
		}
		columnList.add(column);
	}
	
	
}
