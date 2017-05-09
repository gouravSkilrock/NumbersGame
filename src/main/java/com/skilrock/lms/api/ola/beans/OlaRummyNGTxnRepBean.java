package com.skilrock.lms.api.ola.beans;


import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.OlaNetGamingRowList;




public class OlaRummyNGTxnRepBean {
	

	String domainName;
	List<OlaRummyNGPlrTxnRepBean> rummyngplrTxnList;
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public List<OlaRummyNGPlrTxnRepBean> getRummyngplrTxnList() {
		return rummyngplrTxnList;
	}
	public void setRummyngplrTxnList(OlaRummyNGPlrTxnRepBean rummyngplrTxn) {
		if(rummyngplrTxnList == null){
			rummyngplrTxnList = new ArrayList<OlaRummyNGPlrTxnRepBean>();
		}
		
		rummyngplrTxnList.add(rummyngplrTxn);
	}
	
	
	
	
	

	

}
