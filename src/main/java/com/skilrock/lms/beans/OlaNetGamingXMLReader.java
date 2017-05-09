package com.skilrock.lms.beans;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OlaNetGamingXMLReader {
 
	static Log logger = LogFactory.getLog(OlaNetGamingXMLReader.class);
	
	List<OlaNetGamingRowList> rowList = null;
	
	public List<OlaNetGamingRowList> getRowList() {
		return rowList;
	}

	public void setRowList(OlaNetGamingRowList row) {
		if(rowList == null){
			rowList = new ArrayList<OlaNetGamingRowList>();
		}
		rowList.add(row);
	}
	
		
}
