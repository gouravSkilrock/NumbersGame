package com.skilrock.lms.coreEngine.payWorld.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PWSaleDataBean;
import com.skilrock.lms.beans.PWSaleRespBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.commercialService.common.CSPWSaleHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class PayWorldHelper {
	static Log logger = LogFactory.getLog(CSPWSaleHelper.class);
	
	public String transactionRequest(String agentid,int transId, String retailerid,  String operatorcode,String circode,String product,double denomination,int bulkqty, String narration, String agentpwd, String loginstatus,  String appver){
		logger.debug("PayWorld Sale Request:  agentid:"+agentid+",transId:"+transId+",retailerid:"+retailerid+",operatorCode:"+operatorcode+",circleCode:"+circode+",productCode:"+product+",denomination:"+denomination+",bulkqty:"+bulkqty+",narration:"+narration+",agentpwd:"+agentpwd+",loginstatus:"+loginstatus+",appver:"+appver);
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("agentid", agentid);
		paramMap.put("transid",transId+"");
		paramMap.put("retailerid", retailerid);
		paramMap.put("operatorcode", operatorcode);
		paramMap.put("product",product);
		paramMap.put("circode", circode);
		paramMap.put("denomination", denomination+"");
		paramMap.put("bulkqty",bulkqty+"");
		paramMap.put("narration",narration);
		paramMap.put("loginstatus",loginstatus);
		paramMap.put("agentpwd",agentpwd);
		paramMap.put("appver", appver);
		String respData = callPayWorld("pw_trans.php3", paramMap);
		logger.debug("PayWorld Sale Response: "+respData); 
		return respData;//"845%$2011-07-28 00:00:00%$32452452%$48767864%$2011-12-31 00:00:00<br>%$32452423%$48734534%$2011-12-31 00:00:00<br>%$32457556%$4873245321%$2011-12-31 00:00:00<br>%$3223452123%$482345232213%$2011-12-31 00:00:00$$$";
	}
	
	public PWSaleDataBean parseSaleResponseData(String respData, PWSaleDataBean termResp)throws Exception{
		List<PWSaleRespBean> dataList = new ArrayList<PWSaleRespBean>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String[] strArr = respData.split("<BR>");
		if(strArr.length < dataList.size()){
			termResp.setNarration(respData);
			return termResp;
		}
		if(respData.indexOf("#ERROR")!=-1){
			termResp.setNarration(respData);
			return termResp;
		}
		  for(int i=0 ;i<strArr.length; i++){
			  PWSaleRespBean data = new PWSaleRespBean();
			  String[] prodDataArr = strArr[i].split("%\\$");
			  logger.debug(prodDataArr[i]+",");
			  if(i==0){
				  termResp.setPWtransId(prodDataArr[0]);
				  termResp.setTransDateTime(sf.parse(prodDataArr[1]));
				  data.setCardSerialNum(prodDataArr[2]);
				  data.setCardPinNum(prodDataArr[3]);
				  data.setExpiryDate(sf.parse(prodDataArr[4]));
			  }else{
				  data.setCardSerialNum(prodDataArr[0]);
				  data.setCardPinNum(prodDataArr[1]);
				  data.setExpiryDate(sf.parse(prodDataArr[2]));
			  }
			  dataList.add(data);
		  }
		  logger.debug(dataList.size());
		  termResp.setRespDataList(dataList);
		  boolean success = updatePWTransId(termResp.getRMStransId(),termResp.getPWtransId(), termResp.getProdId(), termResp.getCategoryId()  );
		  return termResp;
	}
	
	public String getTerminalFormatSaleData(PWSaleDataBean dataBean){
		if(dataBean.getRespDataList() == null){
			return dataBean.getNarration();
		}else{
			StringBuilder st = new StringBuilder();
			st.append("TrnId:"+dataBean.getPWtransId());
			st.append("|TrnDateTime:"+new java.sql.Date(dataBean.getTransDateTime().getTime())+"$");
			for(PWSaleRespBean temp : dataBean.getRespDataList() ){
				st.append("|CardSrNum:"+temp.getCardSerialNum());
				st.append("|CardPin:"+temp.getCardPinNum());
				st.append("|CardExp:"+new java.sql.Date(temp.getExpiryDate().getTime()));
			}
			return st.toString();
		}
	}
	
	public String parseAndFormatTransStatusData(String data){
		return data.toString().substring(0,data.toString().length()-3);
	}
												//operator -> circle -> product -> denomination -> detailsMap[Keys]('talktime', 'validity', 'adminFee', 'serviceTax')
	public String getTerminalFormatServiceData(Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> dataMap, String cs_isVoucherPrintON){
		StringBuilder termData = new StringBuilder();
		if(dataMap.size() == 0){
			return "#Error:No Products on Server|";
		}else{
			termData.append("cs_isShowCircle:"+LMSFilterDispatcher.cs_isShowCircle+"|cs_isVoucherPrintOn:"+cs_isVoucherPrintON+"|PINServiceData:");
			Iterator<Map.Entry<String, Map<String, Map<String, Map<String, Map<String, String>>>>>> dataIt = dataMap.entrySet().iterator();
			while(dataIt.hasNext()){
				Map.Entry<String, Map<String, Map<String, Map<String, Map<String, String>>>>> dataPair = (Map.Entry<String, Map<String, Map<String, Map<String, Map<String, String>>>>>)dataIt.next();
				termData.append(dataPair.getKey().replace("_", ","));
				Iterator<Map.Entry<String, Map<String, Map<String, Map<String, String>>>>> cirIt = dataPair.getValue().entrySet().iterator();
				while(cirIt.hasNext()){
					Map.Entry<String, Map<String, Map<String, Map<String, String>>>> cirPair = (Map.Entry<String, Map<String, Map<String, Map<String, String>>>>)cirIt.next();
					termData.append("%$"+cirPair.getKey().replace("_", ","));
					Iterator<Map.Entry<String, Map<String, Map<String, String>>>> prodIt = cirPair.getValue().entrySet().iterator();
					while(prodIt.hasNext()){
						Map.Entry<String, Map<String, Map<String, String>>> prodPair = (Map.Entry<String, Map<String, Map<String, String>>>)prodIt.next();
						termData.append("%:"+prodPair.getKey().replace("_", ","));
						Iterator<Map.Entry<String, Map<String, String>>> denoIt = prodPair.getValue().entrySet().iterator();
						while(denoIt.hasNext()){
							Map.Entry<String, Map<String, String>> denoPair = (Map.Entry<String, Map<String, String>>)denoIt.next();
							termData.append(":"+denoPair.getKey()+",");
							Iterator<Map.Entry<String, String>> detIt = denoPair.getValue().entrySet().iterator();
							while(detIt.hasNext()){
								Map.Entry<String, String> detPair = detIt.next();
								termData.append(detPair.getValue()+",");
							}
							termData.replace(termData.length()-1, termData.length(), "");
						}
					}
				}
				termData.append("%#");
			}
			termData.delete(termData.length()-2, termData.length());
			termData.append("|");
		}
		return termData.toString();
	}
	
	
	public String getTerminalFormatServiceDataNew(String cs_isVoucherPrintON){
		StringBuilder termData = new StringBuilder();
		termData.append("cs_isShowCircle:"+(String)LMSUtility.sc.getAttribute("IS_CS_SHOW_CIRCLE")+"|cs_isVoucherPrintOn:"+cs_isVoucherPrintON+"|PINServiceData:");
	try {
			Connection con=DBConnect.getConnection();
			String operatorQuery="select  * from st_cs_operator_master where status ='ACTIVE'";
			PreparedStatement pstmt = con.prepareStatement(operatorQuery);
			ResultSet rsOp = pstmt.executeQuery();
			
				while(rsOp.next()){					
					String operatorCode=rsOp.getString("operator_code");
					
					termData.append(operatorCode+",");					
					termData.append(rsOp.getString("operator_name")+"%$");
				
					
					String circleQuery="select distinct(a.circle_code),a.circle_name from st_cs_circle_master a,st_cs_product_master b where  b.operator_code='"+operatorCode+"' and b.status='ACTIVE' and a.circle_code=b.circle_code";
					PreparedStatement pstmtCircle = con.prepareStatement(circleQuery);
					ResultSet rsCircle = pstmtCircle.executeQuery();
					
					while(rsCircle.next()){
					  String circleCode=rsCircle.getString("circle_code");
					  termData.append(circleCode+","+rsCircle.getString("circle_name")+"%:");					  
					  
					    String categoryQuery="select distinct(a.category_id),b.category_code,b.description  from st_cs_product_master a, st_cs_product_category_master b  where circle_code='"+circleCode+"' and operator_code='"+operatorCode+"' and a.category_id=b.category_id and a.status='ACTIVE'";
						PreparedStatement pstmtcategory = con.prepareStatement(categoryQuery);
						ResultSet rscategory = pstmtcategory.executeQuery();
					    while(rscategory.next()){					    	
						    	int categoryId=rscategory.getInt("category_id");
						    	termData.append(rscategory.getString("category_code")+",");
						    	termData.append(rscategory.getString("description")+":");  
							    String productQuery="select cpm.product_id,cpm.denomination, cpm.product_code, cpm.description, cpm.operator_code, cpm.circle_code,cpd.talktime, cpd.validity, cpd.admin_fee, cpd.service_tax,cpd.recharge_instructions from st_cs_product_master cpm, st_cs_product_details cpd where cpm.status='ACTIVE' and cpm.product_id=cpd.product_id and cpm.operator_code='"+operatorCode+"' and cpm.circle_code='"+circleCode+"' and cpm.category_id="+categoryId;
								PreparedStatement pstmtProduct = con.prepareStatement(productQuery);
								ResultSet rsproduct = pstmtProduct.executeQuery();
								while(rsproduct.next()){									
									termData.append(rsproduct.getString("denomination")+",");
									termData.append(rsproduct.getString("talktime")+",");
									termData.append(rsproduct.getString("validity")+",");
									termData.append(rsproduct.getString("admin_fee")+",");
									termData.append(rsproduct.getString("service_tax")+",");									
									termData.append(rsproduct.getString("product_code")+",");
									termData.append(rsproduct.getString("recharge_instructions"));
									termData.append(":");
								}
								termData.deleteCharAt(termData.length()-1);
								termData.append("%:");
					    }
					    termData.delete(termData.length()-2, termData.length()); 
					    termData.append("%$");
					}
					termData.delete(termData.length()-2, termData.length()); 
					termData.append("%#");
				}
				termData.delete(termData.length()-2, termData.length());
				termData.append("|");
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(termData.toString());
		
		return termData.toString();
	}
	
	
	
	public boolean updatePWTransId(String RMSTxId, String PWTxId, int prodId, int categoryId)throws LMSException{
		boolean status = false;
		Connection con = DBConnect.getConnection();
		try{
			Statement stmt = con.createStatement();
			String query = "update st_cs_sale_"+categoryId+" set cs_ref_transaction_id='"+PWTxId+"' where transaction_id="+RMSTxId;
			logger.debug("update transaction id got from payworld: "+query);
			int rows = stmt.executeUpdate(query);
			if(rows == 1){
				status = true;
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return status;
	}
	
	public String serviceDataRequest(String merchantid, String data, String circode, String loginstatus, String appver){
		Map<String,String> paramMap = new HashMap<String,String>();
		
		paramMap.put("agentid",merchantid);
		paramMap.put("data",data);
		paramMap.put("circode",circode);
		paramMap.put("loginstatus",loginstatus);
		paramMap.put("appver",appver);
		String respData = callPayWorld("pw_servicedata.php3", paramMap);
		return respData;
	}
	
	public String transStatusRequest(String spId, String clientId, String agentid, String agentPwd, String loginstatus, String service, String appver){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("agentid", agentid);
		paramMap.put("sp_transid", spId);
		paramMap.put("client_transid", clientId);
		paramMap.put("loginstatus", loginstatus);
		paramMap.put("service", service);
		paramMap.put("appver", appver);
		paramMap.put("sp_transid", spId+"");
		String respData = callPayWorld("pw_gettransstatus.php3", paramMap);
		logger.debug("PayWorld Sale status Response: "+respData);
		return respData;
	}
	
	/*public Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> fetchServiceDataForTerminal()throws LMSException{
		Connection con = DBConnect.getConnection();
		//operator -> circle -> product -> denomination -> detailsMap[Keys]('talktime', 'validity', 'adminFee', 'serviceTax')
		Map<String, Map<String, Map<String, Map<String, Map<String, Double>>>>> dataMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, Double>>>>>();
		String prodQuery = "select cpm.product_id, cpm.product_code, cpm.description, cpm.operator_code, com.operator_name, cpm.circle_code, ccm.circle_name, cpm.denomination, cpd.talktime, cpd.validity, cpd.admin_fee, cpd.service_tax from st_cs_product_master cpm, st_cs_operator_master com , st_cs_circle_master ccm, st_cs_product_category_master ccatm, st_cs_product_details cpd where com.operator_code = cpm.operator_code and ccm.circle_code = cpm.circle_code and cpm.category_id = ccatm.category_id and cpd.product_id = cpm.product_id and cpm.status = 'ACTIVE' and cpm.product_code not like '%0%'";
		try{
			PreparedStatement pstmt = con.prepareStatement(prodQuery);
			ResultSet rs = pstmt.executeQuery();
			Map<String, Map<String, Map<String, Map<String, Double>>>> cirMap = new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
			Map<String, Map<String, Map<String, Double>>> prodMap = new HashMap<String, Map<String, Map<String, Double>>>();
			Map<String, Map<String, Double>> denoMap = new HashMap<String, Map<String, Double>>();
			Map<String, Double> detailMap = new HashMap<String, Double>();
			Map<String, Map<String, Map<String, Map<String, Double>>>> cirMap = null;
			Map<String, Map<String, Map<String, Double>>> prodMap = null;
			Map<String, Map<String, Double>> denoMap = null;
			Map<String, Double> detailMap = null;
			while(rs.next()){
				detailMap.put("talktime", rs.getDouble("talktime"));
				detailMap.put("validity", rs.getDouble("validity"));
				detailMap.put("adminFee", rs.getDouble("admin_fee"));
				detailMap.put("serviceTax", rs.getDouble("service_tax"));
				denoMap.put(rs.getString("denomination"), detailMap);
				prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
				cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"),prodMap);
				dataMap.put(rs.getString("operator_code")+"_"+rs.getString("operator_name"), cirMap);
				detailMap = new HashMap<String, Double>();
				detailMap.put("talktime", rs.getDouble("talktime"));
				detailMap.put("validity", rs.getDouble("validity"));
				detailMap.put("adminFee", rs.getDouble("admin_fee"));
				detailMap.put("serviceTax", rs.getDouble("service_tax"));
				if(dataMap.get(rs.getString("operator_code")+"_"+rs.getString("operator_name")) == null){
					denoMap = new HashMap<String, Map<String, Double>>();
					prodMap = new HashMap<String, Map<String, Map<String, Double>>>();
					cirMap = new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
					
					denoMap.put(rs.getString("denomination"), detailMap);
					prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
					cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"), prodMap);
					dataMap.put(rs.getString("operator_code")+"_"+rs.getString("operator_name"), cirMap);
				}else{
						denoMap = new HashMap<String, Map<String, Double>>();
					if(cirMap.get(rs.getString("circle_code")+"_"+rs.getString("circle_name")) == null){
						prodMap = new HashMap<String, Map<String, Map<String, Double>>>();
						
						denoMap.put(rs.getString("denomination"), detailMap);
						prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
						cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"), prodMap);
						dataMap.put(rs.getString("operator_code")+"_"+rs.getString("operator_name"), cirMap);
					}else{
						prodMap = new HashMap<String, Map<String, Map<String, Double>>>();
						if(prodMap.get(rs.getString("product_code")+"_"+rs.getString("description")) == null){
							
							denoMap.put(rs.getString("denomination"), detailMap);
							prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
							cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"), prodMap);
							dataMap.put(rs.getString("operator_code")+"_"+rs.getString("operator_name"), cirMap);
						}else{
							
							cirMap = new HashMap<String, Map<String, Map<String, Map<String, Double>>>>();
							if(denoMap.get(rs.getString("denomination")) == null){
								
								denoMap.put(rs.getString("denomination"), detailMap);
								prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
								cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"), prodMap);
								dataMap.put(rs.getString("operator_code")+"_"+rs.getString("operator_name"), cirMap);
							}
						}
					}
					
				}
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		logger.debug("the service data map: "+dataMap);
		return dataMap;
	}*/
	
	public Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> fetchServiceDataForTerminal()throws LMSException{
		Connection con = DBConnect.getConnection();
		//operator -> circle -> product -> denomination -> detailsMap[Keys]('talktime', 'validity', 'adminFee', 'serviceTax')
		Map<String, Map<String, Map<String, Map<String, Map<String, String>>>>> dataMap = new HashMap<String, Map<String, Map<String, Map<String, Map<String, String>>>>>();
		String prodQuery = "select cpm.product_id, cpm.product_code, cpm.description, cpm.operator_code, com.operator_name, cpm.circle_code, ccm.circle_name, cpm.denomination, cpd.talktime, cpd.validity, cpd.admin_fee, cpd.service_tax from st_cs_product_master cpm, st_cs_operator_master com , st_cs_circle_master ccm, st_cs_product_category_master ccatm, st_cs_product_details cpd where com.operator_code = cpm.operator_code and ccm.circle_code = cpm.circle_code and cpm.category_id = ccatm.category_id and cpd.product_id = cpm.product_id and cpm.status = 'ACTIVE'";
		try{
			PreparedStatement pstmt = con.prepareStatement(prodQuery);
			ResultSet rs = pstmt.executeQuery();
			Map<String, Map<String, Map<String, Map<String, String>>>> cirMap = null;
			Map<String, Map<String, Map<String, String>>> prodMap = null;
			Map<String, Map<String, String>> denoMap = null;
			Map<String, String> detailMap = null;
			while(rs.next()){
				detailMap = new LinkedHashMap<String, String>();
				detailMap.put("talktime", rs.getString("talktime"));
				detailMap.put("validity", rs.getString("validity"));
				detailMap.put("adminFee", rs.getString("admin_fee"));
				detailMap.put("serviceTax", rs.getString("service_tax"));
				if((cirMap = dataMap.get(rs.getString("operator_code")+"_"+rs.getString("operator_name"))) == null){
					denoMap = new HashMap<String, Map<String, String>>();
					prodMap = new HashMap<String, Map<String, Map<String, String>>>();
					cirMap = new HashMap<String, Map<String, Map<String, Map<String, String>>>>();
					
					dataMap.put(rs.getString("operator_code")+"_"+rs.getString("operator_name"), cirMap);
					cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"), prodMap);
					prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
					denoMap.put(rs.getString("denomination"), detailMap);
				}else{					
					if((prodMap = cirMap.get(rs.getString("circle_code")+"_"+rs.getString("circle_name"))) == null){
						denoMap = new HashMap<String, Map<String, String>>();
						prodMap = new HashMap<String, Map<String, Map<String, String>>>();
						
						cirMap.put(rs.getString("circle_code")+"_"+rs.getString("circle_name"), prodMap);
						prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
						denoMap.put(rs.getString("denomination"), detailMap);				
												
					}else{
						
						if((denoMap = prodMap.get(rs.getString("product_code")+"_"+rs.getString("description"))) == null){
							denoMap = new HashMap<String, Map<String, String>>();
							
							prodMap.put(rs.getString("product_code")+"_"+rs.getString("description"), denoMap);
							denoMap.put(rs.getString("denomination"), detailMap);
						}else{
							if(denoMap.get(rs.getString("denomination")) == null){								
								denoMap.put(rs.getString("denomination"), detailMap);								
							}
						}
					}
					
				}
			}
		}catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		logger.debug("the service data map: "+dataMap);
		return dataMap;
	}
	
	public static void serviceDataScheduler(String merchantId, String loginStatus, String appVer)throws LMSException{
		Connection con = DBConnect.getConnection();
		//To Fill Operator Table and Circle Table
		PayWorldHelper pwhelper = new PayWorldHelper();
		Map<String, String> operatorMap = new TreeMap<String, String>();
		Map<String, String> circleMap = new TreeMap<String, String>();
		Map<String ,String> prodMap = new TreeMap<String, String>();
		Map<String, Map<String, String>> allMap = new TreeMap<String, Map<String, String>>();
		
		String operatorData = pwhelper.serviceDataRequest(merchantId, "operator", "*", loginStatus, appVer);
		String circleData = pwhelper.serviceDataRequest(merchantId, "circle", "*", loginStatus, appVer);
		String prodData = pwhelper.serviceDataRequest(merchantId, "product", "*", loginStatus, appVer);
		String allData = pwhelper.serviceDataRequest(merchantId, "all", "*", loginStatus, appVer);
		if((operatorData.indexOf("ERROR")!= -1)||(circleData.indexOf("ERROR")!= -1)||(prodData.indexOf("ERROR")!= -1)||(allData.indexOf("ERROR")!= -1)){
			return;
		}
		String[] strArr = operatorData.split("<BR>");
		  for(int i=0 ;i<strArr.length; i++){
			  String[] optrDataArr = strArr[i].split("%\\$");
			  if(i==strArr.length-1){
				  operatorMap.put(optrDataArr[0],optrDataArr[1].substring(0, optrDataArr[1].length()-3));
			  }else{
				  operatorMap.put(optrDataArr[0],optrDataArr[1]);
			  }
		  }
		  if("Y".equalsIgnoreCase(circleData.split("\\*!")[0])){
			  LMSFilterDispatcher.cs_isShowCircle = "YES";
		  }
		  else if("N".equalsIgnoreCase(circleData.split("\\*!")[0])){
			  LMSFilterDispatcher.cs_isShowCircle = "NO";
		  }
		strArr = circleData.split("\\*!")[1].split("<BR>");
		  for(int i=0 ;i<strArr.length; i++){
			  String[] cirDataArr = strArr[i].split("%\\$");
			  if(i==strArr.length-1){
				  circleMap.put(cirDataArr[0],cirDataArr[1].substring(0, cirDataArr[1].length()-3));
			  }else{
				  circleMap.put(cirDataArr[0],cirDataArr[1]);
			  }
		  }
		  strArr = prodData.split("<BR>");
		  for(int i=0 ;i<strArr.length; i++){
			  String[] prodDataArr = strArr[i].split("%\\$");
			  if(i==strArr.length-1){
				  prodMap.put(prodDataArr[1],prodDataArr[2]);
			  }else{
				  prodMap.put(prodDataArr[1],prodDataArr[2]);
			  }
		  }
		  strArr = allData.split("<BR>");
		  for(int i=0 ;i<strArr.length; i++){
			  String[] allDataArr = strArr[i].split("%\\$");
			  Map<String, String> tempMap = new HashMap<String, String>();
			  tempMap.put("talktime", allDataArr[4]);
			  tempMap.put("validity", allDataArr[5]);
			  tempMap.put("adminfee", allDataArr[6]);
			  if(i==strArr.length-1){
				  tempMap.put("servicetax", allDataArr[7].substring(0, allDataArr[7].length()-3));
			  }else{
				  tempMap.put("servicetax", allDataArr[7]);
			  }
			  allMap.put(allDataArr[0]+"_"+allDataArr[1]+"_"+allDataArr[2]+"_"+allDataArr[3],tempMap);
			  //operatorcode_circode_product_denomination
		  }
		  try{
			  con.setAutoCommit(false);
			  //update circle master
			  Statement stmt = con.createStatement();
			  stmt.executeUpdate(new StringBuilder("update st_cs_circle_master set status = 'INACTIVE' where circle_code not in ("+ Util.convertCollToStr(circleMap.keySet()) +")").toString().replaceAll("\\*", "'*'"));
			  //select from circle master
			  PreparedStatement pstmt = con.prepareStatement("select circle_code from st_cs_circle_master where status = 'ACTIVE'");
			  ResultSet rs = pstmt.executeQuery();
			  List<String> cirCodeList = new ArrayList<String>();
			  while(rs.next()){
				  cirCodeList.add(rs.getString("circle_code"));
			  }
			  pstmt.close();
			  rs.close();
			  //insert into circle master
			  StringBuilder insCircleMaster =  new StringBuilder("insert into st_cs_circle_master values");
			  int i = 0;
			  for(String cirCode : circleMap.keySet()){
				  if(!cirCodeList.contains(cirCode) ){
					  i++;
					  insCircleMaster.append("('"+cirCode+"','"+circleMap.get(cirCode)+"','ACTIVE'),");
				  }
			  }
			  if(i>0){
				  stmt.executeUpdate(insCircleMaster.substring(0, insCircleMaster.length()-1));
			  }
			  
			  //update operator master
			  stmt.executeUpdate(new StringBuilder("update st_cs_operator_master set status = 'INACTIVE' where operator_code not in("+ Util.convertCollToStr(operatorMap.keySet()) +")").toString().replaceAll("\\*", "'*'"));
			  //select from operator master
			  pstmt = con.prepareStatement("select operator_code from st_cs_operator_master where status = 'ACTIVE'");
			  rs = pstmt.executeQuery();
			  List<String> optrCodeList = new ArrayList<String>();
			  while(rs.next()){
				  optrCodeList.add(rs.getString("operator_code"));
			  }
			  pstmt.close();
			  rs.close();
			  //insert into circle master
			  StringBuilder insOptrMaster =  new StringBuilder("insert into st_cs_operator_master values");
			  i = 0;
			  for(String optrCode : operatorMap.keySet()){
				  if(!optrCodeList.contains(optrCode) ){
					  i++;
					  insOptrMaster.append("('"+optrCode+"','"+operatorMap.get(optrCode)+"','ACTIVE'),");
				  }
			  }
			  if(i>0){
				  stmt.executeUpdate(insOptrMaster.substring(0, insOptrMaster.length()-1));
			  }
			  //This code below is for filling st_cs_product_master automatically from payworld 
			/*//update product name master
			  stmt = con.createStatement();
			  StringBuilder temp = new StringBuilder("update st_cs_product_name_master set status = 'INACTIVE' where product_code not in (");
			  for(String tt : prodMap.keySet()){
				  temp.append("'"+ tt +"',");
			  }
			  stmt.executeUpdate(temp.toString().substring(0,temp.toString().length()-1)+")");
			  //select from product name master
			  pstmt = con.prepareStatement("select product_code from st_cs_product_name_master where status = 'ACTIVE'");
			  rs = pstmt.executeQuery();
			  List<String> prodCodeList = new ArrayList<String>();
			  while(rs.next()){
				  prodCodeList.add(rs.getString("product_code"));
			  }
			  pstmt.close();
			  rs.close();
			  //insert into product name master
			  StringBuilder insProdNameMaster =  new StringBuilder("insert into st_cs_product_name_master values ");
			  i = 0;
			  for(String prodCode : prodMap.keySet()){
				  if(!prodCodeList.contains(prodCode) ){
					  i++;
					  insProdNameMaster.append("('"+prodCode+"','"+prodMap.get(prodCode)+"','ACTIVE'),");
				  }
			  }
			  if(i>0){
				  stmt.executeUpdate(insProdNameMaster.substring(0, insProdNameMaster.length()-1));
			  }
			  
			  //update product master
			 StringBuilder updtProdMaster = new StringBuilder("update st_cs_product_master set status='INACTIVE' where ");
			  Set<String> prCd = new TreeSet<String>();
			  Set<String> optrCd = new TreeSet<String>();
			  Set<String> cirCd = new TreeSet<String>();
			  Set<String> deno = new TreeSet<String>();
			  StringBuilder prCdStr = new StringBuilder();
			  StringBuilder denoStr = new StringBuilder();
			  StringBuilder cirCdStr = new StringBuilder();
			  for(String prodCandidKey : allMap.keySet()){
				  String arr[] = prodCandidKey.split("_");
				  optrCd.add(arr[0]);
				  cirCd.add(arr[1]);
				  prCd.add(arr[2]);
				  deno.add(arr[3]);
			  }
			  for(String cirCode: cirCd){
				  cirCdStr.append("'"+ cirCode +"',");
			  }
			  for(String prcdCode: prCd){
				  prCdStr.append("'"+ prcdCode +"',");
			  }
			  for(String denoCode: deno){
				  denoStr.append("'"+ denoCode +"',");
			  }
			  updtProdMaster.append("product_code not in ("+ prCdStr.substring(0, prCdStr.length()-1) +")");
			  updtProdMaster.append(" and ");
			  updtProdMaster.append("operator_code not in ("+ Util.convertCollToStr(optrCd) +")");
			  updtProdMaster.append(" and ");
			  updtProdMaster.append("circle_code not in ("+ cirCdStr.substring(0, cirCdStr.length()-1) +")");
			  updtProdMaster.append(" and ");
			  updtProdMaster.append("denomination not in ("+ denoStr.substring(0, denoStr.length()-1) +")");
			  System.out.println("update product status query: "+updtProdMaster.toString());
			  stmt.executeUpdate(updtProdMaster.toString());
			  
			  //select product master
			  pstmt = con.prepareStatement("select product_id, operator_code, circle_code, product_code, denomination from st_cs_product_master where status = 'ACTIVE'");
			  rs = pstmt.executeQuery();
			  List<String> tempProdList = new ArrayList<String>();
			  while(rs.next()){
				  tempProdList.add(rs.getString("operator_code")+"_"+rs.getString("circle_code")+"_"+rs.getString("product_code")+"_"+rs.getString("denomination"));
			  }
			  pstmt.close();
			  rs.close();
			  System.out.println("All Map is: "+allMap.size());
			//insert into product master
			  StringBuilder insProdMaster =  new StringBuilder("insert into st_cs_product_master (product_code,category_id,description,operator_code,circle_code,denomination,country_code,supplier_name,unit_price,retailer_comm,agent_comm,jv_comm,good_cause_comm,vat_comm,status) values");
			  StringBuilder insProdDetails = new StringBuilder("insert into st_cs_product_details (product_id,talktime,validity,admin_fee,service_tax) values ");
			  i = 0;
			  int j=0;
			  for(String prodCandidKey : allMap.keySet()){
				  if(!tempProdList.contains(prodCandidKey) ){
					  i++;
					  insProdMaster.append("('"+prodCandidKey.split("_")[2]+"',7,'"+ prodMap.get(prodCandidKey.split("_")[2]) +"','"+ prodCandidKey.split("_")[0] +"','"+ prodCandidKey.split("_")[1] +"','"+ prodCandidKey.split("_")[3] +"','KEN','PW','"+ prodCandidKey.split("_")[3] +"',0,0,0,0,0,'ACTIVE'),");
				  }
			  }
			  insOptrMaster.length();
			  if(i>0){
				  System.out.println("product master insert query: "+insProdMaster.substring(0, insProdMaster.length()-1));
				  stmt.executeUpdate(insProdMaster.substring(0, insProdMaster.length()-1));
				  rs = stmt.getGeneratedKeys();
				  Set<String> s = allMap.keySet();
				  Iterator<String> it = s.iterator();
				  while(rs.next() && it.hasNext()){
					  j++;
					  String oldKey = it.next(); 
					  insProdDetails.append("("+ rs.getInt(1) +","+ allMap.get(oldKey).get("talktime") +","+ allMap.get(oldKey).get("validity") +","+ allMap.get(oldKey).get("adminfee") +","+ allMap.get(oldKey).get("servicetax") +"),");
				  }
				  System.out.println("product details query: "+insProdDetails.substring(0, insProdDetails.length()-1));
				  stmt.executeUpdate(insProdDetails.substring(0, insProdDetails.length()-1));
			  }*/
			  con.commit();
			  
		  }catch (SQLException e) {
				logger.error("Exception: " + e);
				try{
					con.rollback();
				}catch(SQLException er){
					er.printStackTrace();
				}
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				try {
					if (con != null && !con.isClosed()) {
						con.close();
					}
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
	}
	
	public static String callPayWorld(String phpName,Map<String,String> paramMap)
    {
	 try {					
		
		 StringBuilder urlStr = new StringBuilder("");
		 Set<String> paramSet =paramMap.keySet();
		 for (String paramName : paramSet) {
			 urlStr.append(paramName+"="+paramMap.get(paramName)+"&");
		}
		 urlStr.deleteCharAt(urlStr.length()-1);
		 	String address = (String)LMSUtility.sc.getAttribute("PW_PAYWORLD_SERVER_PATH");
		 	//String address = "http://192.168.126.7:8181/zim/mainlinkpos/purchase/";
			//String encodedAsdress = URLEncoder.encode(address, "UTF-8");
			 address = address+phpName;
			 URL url = new URL(address);
			logger.debug("Calling Payword URL: "+url); 			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//URLConnection conn = url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();
			
			if(conn.getResponseCode() == 408 || conn.getResponseCode() == 404){
				return "#ERROR:No Response From Host";
			}
						
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			// Get the response				
			StringBuffer result = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null)
			{
				result.append(line);
			}		
			in.close();			
			return result.toString();

		} catch (Exception e) {
			System.out.println("inside exception ");
			e.printStackTrace();
			return "ERROR";
		}
    }
	
	public String formatRespForSMS(String rawResp){
		String formatedResp = rawResp;
		return formatedResp;
	}
	
	public static void main(String[] agrs)throws LMSException{
		/*String str = new PayWorldHelper().serviceDataRequest("1", "all", "3", "LIVE", "3.33");
		String[] strArr = str.split("<BR>");
		  for(int i=0 ;i<strArr.length; i++){
			  String[] prodDataArr = strArr[i].split("%\\$");
			  for(int j=0; j<prodDataArr.length; j++){
				  System.out.print(" "+ prodDataArr[j]+ " ");
			  }
			  System.out.println("");
		  }*/
		//System.out.println((new PayWorldHelper()).transStatusRequest("", "62", "1", "", "LIVE", "PIN", "3.33"));
		serviceDataScheduler("1", "LIVE", "3.34");
	}
}
