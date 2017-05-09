package com.skilrock.lms.embedded.ola.olaMgmt;



import java.util.Map;



import com.skilrock.lms.coreEngine.ola.common.OLAUtility;

public class OlaRposHelper {

public String walletData(){
	

	StringBuilder sb = new StringBuilder();
	boolean isFirst =true;
	Map<Integer,String> map = OLAUtility.getOlaWalletDataMap();
	for(Map.Entry<Integer,String> entry : map.entrySet()){
		if(isFirst){
			sb.append(entry.getKey()+","+entry.getValue());
			isFirst=false;
		}else{
			sb.append("|"+entry.getKey()+","+entry.getValue());
		}
		
	}
	
	
	return sb.toString();
}	
}
