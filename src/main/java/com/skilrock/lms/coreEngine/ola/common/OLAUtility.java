package com.skilrock.lms.coreEngine.ola.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSONObject;




import org.apache.commons.digester.Digester;
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.commons.digester.Rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



import com.skilrock.lms.api.ola.beans.OlaRummyNGDepositRepBean;
import com.skilrock.lms.api.ola.beans.OlaRummyNGPlrTxnRepBean;
import com.skilrock.lms.api.ola.beans.OlaRummyNGTxnRepBean;
import com.skilrock.lms.api.ola.beans.OlaWalletIntegrationBean;
import com.skilrock.lms.beans.OlaGetCancelWithdrawalDetailsBean;
import com.skilrock.lms.beans.OlaGetPendingWithdrawalDetailsBean;
import com.skilrock.lms.beans.OlaGetPlayerBindingInfoBean;
import com.skilrock.lms.beans.OlaGetPlayerInfoBean;
import com.skilrock.lms.beans.OlaNetGamingRetailerData;
import com.skilrock.lms.beans.OlaNetGamingRowList;
import com.skilrock.lms.beans.OlaNetGamingXMLReader;
import com.skilrock.lms.beans.OlaPTResponseBean;
import com.skilrock.lms.beans.OlaPendingWithdrawalBean;
import com.skilrock.lms.beans.OlaPendingWithdrawalDataBean;
import com.skilrock.lms.beans.OlaPlayerDetailsBean;
import com.skilrock.lms.beans.OlaWalletBean;
import com.skilrock.lms.beans.RetailerRegistrationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;

public class OLAUtility {
	
	private static Map<Integer,String> olaWalletMap =null;
	static Log  logger = LogFactory.getLog(OLAUtility.class);
	public static Map<Integer, OlaWalletBean> olaWalletDataMap=null;
	public static Map<String,OlaWalletIntegrationBean>  walletIntBeanMap =null;
	
	//Need to set while server startup
	static{
		setWalletInfoMap(); // Wallet Details and Integration Details
	}
	
	public static void setWalletInfoMap() {
		Connection con = DBConnect.getConnection();
		Statement pstmt = null;
		ResultSet rs = null;
		olaWalletDataMap = new HashMap<Integer, OlaWalletBean>();
		walletIntBeanMap =new HashMap<String, OlaWalletIntegrationBean>();
		OlaWalletBean olaWalletBean=null;
		try {
	
				logger.info("setting wallet  Bean");
				String walletQuery = "select wallet_id,wallet_name,wallet_category,wallet_display_name,wallet_status from st_ola_wallet_master";
				pstmt = con.createStatement();
				rs = pstmt.executeQuery(walletQuery);
				while (rs.next()) {
					olaWalletBean=new OlaWalletBean();
					olaWalletBean.setWalletId(rs.getInt("wallet_id"));
					olaWalletBean.setWalletDevName(rs.getString("wallet_name"));
					olaWalletBean.setWalletDispName(rs.getString("wallet_display_name"));
					olaWalletBean.setWalletCategory(rs.getString("wallet_category"));
					olaWalletBean.setWalletStatus(rs.getString("wallet_status"));
					
					olaWalletDataMap.put(rs.getInt("wallet_id"), olaWalletBean);

				}
			
				logger.info("setting wallet Integration Bean");
				walletIntBeanMap=new HashMap<String,OlaWalletIntegrationBean> ();
				String walletIntQuery = "select wallet_id,int_ip,int_walletCode,int_userName,int_password from st_ola_wallet_integration_master";
				pstmt = con.createStatement();
				rs = pstmt.executeQuery(walletIntQuery);
			while(rs.next()){
					OlaWalletIntegrationBean  walletIntBean = new OlaWalletIntegrationBean();
					walletIntBean.setTpIp(rs.getString("int_ip"));
					walletIntBean.setTpPassword(rs.getString("int_password"));
					walletIntBean.setTpUserName(rs.getString("int_userName"));
					walletIntBean.setWalletId(rs.getInt("wallet_id"));
					walletIntBean.setTpWalletCode(rs.getString("int_walletCode"	));
					walletIntBeanMap.put(rs.getInt("wallet_id")+"", walletIntBean);
					walletIntBeanMap.put(rs.getString("int_walletCode"), walletIntBean);// added to get Wallet Integration Bean from wallet Integration Code
				}
				logger.info("setting wallet Integration Bean done"+walletIntBeanMap.size());
		

		}catch(SQLException e){
			logger.error("SQL Exception ",e);
		}catch (Exception e) {
			logger.error(" Exception ",e);
			//e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}

				if (con != null) {
					con.close();
				}

			} catch (Exception e) {
				logger.error(" Exception ",e);
			}

		}
	}
	public static boolean checkWalletStatus(String walletDevName){
		for(java.util.Map.Entry<Integer, OlaWalletBean> entry:olaWalletDataMap.entrySet()){
			if(walletDevName.equalsIgnoreCase(entry.getValue().getWalletDevName())){
				if("ACTIVE".equals(entry.getValue().getWalletStatus())){
					return true;
				}
			}
		}
		return false;
	}
	public static void main(String[] args) {
	
		getPlayerInfo2Response("SAURABH", "/home/gauravk/yogesh/");
		
	}
	
	public static boolean createAffiliate(RetailerRegistrationBean orgUserData,
			Map<String, String> errorMap,String rootPath) {

		Map<String, String> createAffilateParamMap = new HashMap<String, String>();

		try {
			createAffilateParamMap.put("address", URLEncoder.encode(orgUserData
					.getAddrLine1()
					+ " " + orgUserData.getAddrLine2(), "UTF-8"));// address
			createAffilateParamMap.put("city", URLEncoder.encode(orgUserData
					.getCity(), "UTF-8"));// city
			createAffilateParamMap.put("company", URLEncoder.encode(orgUserData
					.getOrgName(), "UTF-8"));// campany
			createAffilateParamMap.put("country", URLEncoder.encode("IN",
					"UTF-8"));// country code //has to be changed
			createAffilateParamMap.put("email", URLEncoder.encode(orgUserData
					.getEmail(), "UTF-8"));// email
			createAffilateParamMap.put("emailconfirm", URLEncoder.encode(
					orgUserData.getEmail(), "UTF-8"));// emailconfirm
			createAffilateParamMap.put("firstname", URLEncoder.encode(
					orgUserData.getFirstName(), "UTF-8"));// firstname
			createAffilateParamMap.put("lastname", URLEncoder.encode(
					orgUserData.getLastName(), "UTF-8"));// lastName
			createAffilateParamMap.put("mobile", URLEncoder.encode(orgUserData
					.getPhone()
					+ "", "UTF-8"));// mobile
			createAffilateParamMap.put("occupation", URLEncoder.encode(
					"Lottery", "UTF-8"));// occuption
			createAffilateParamMap.put("password1", URLEncoder.encode("12345",
					"UTF-8"));// password1
			createAffilateParamMap.put("password2", URLEncoder.encode("12345",
					"UTF-8"));// password2
			createAffilateParamMap.put("paymentmethod", URLEncoder.encode(
					"BD", "UTF-8"));// paymentmethod
			createAffilateParamMap.put("phone", URLEncoder.encode(orgUserData
					.getPhone()
					+ "", "UTF-8"));// phone
		
			createAffilateParamMap.put("salesman", URLEncoder.encode("15256",
					"UTF-8"));// salesman //has to be updated by parent
								// affiliate user id
			createAffilateParamMap.put("state", URLEncoder.encode(orgUserData
					.getState(), "UTF-8"));// state
			createAffilateParamMap.put("zip", URLEncoder.encode(orgUserData
					.getPin()
					+ "", "UTF-8"));// zip
			createAffilateParamMap.put("username", URLEncoder.encode(
					orgUserData.getUserName(), "UTF-8"));// username
			createAffilateParamMap.put("bname", URLEncoder.encode("OLAMS",
					"UTF-8"));
			createAffilateParamMap.put("bstate", URLEncoder.encode(
					"GANGTOK", "UTF-8"));
			createAffilateParamMap.put("baddress", URLEncoder.encode(
					"SIKKIM", "UTF-8"));
			createAffilateParamMap.put("bcity", URLEncoder.encode("SIKKIM",
					"UTF-8"));
			
			createAffilateParamMap.put("bzip", URLEncoder.encode("1234567",
					"UTF-8"));
		
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream iStream = OLAClient.callPlaytechForNewAffiliate(OLAConstants.CREATE_AFFILIATE_PHP, createAffilateParamMap,OLAConstants.CREATE_AFFILIATE_URL,rootPath);
			
			if (iStream == null) {
				// some internal error has occured may be at the time of
				// communication with playtech system
				errorMap.put("returnTypeError", "input");
				errorMap.put("orgError"," some Internal error has occured during communication with playTech");
				return false;
			} else {
				Document doc = docBuilder.parse(iStream);
				// here parse the xml and read the messages
				NodeList nl = doc.getElementsByTagName("newaffiliate");
				Element newaffiliate = (Element) nl.item(0);
				NodeList traList = newaffiliate
						.getElementsByTagName("transaction");
				Element tran = (Element) traList.item(0);
				String result = tran.getAttribute("result");
				// String result = "ERROR";
				if ("OK".equalsIgnoreCase(result)) {
					// communication successful affilaite id created at
					return true;
				} else if ("ERROR".equalsIgnoreCase(result)) {

					NodeList errList = newaffiliate
							.getElementsByTagName("error");
					Element err = (Element) errList.item(0);
					String errorCode = err.getAttribute("nr");

					Element errorElement = (Element) errList.item(0);
					NodeList textErrorList = errorElement.getChildNodes();
					String errorMessage = ((Node) textErrorList.item(0))
							.getNodeValue().trim();
					System.out.println("error is " + errorMessage);
					System.out.println("error is:" + errorCode);
					System.out.println("ERROR return form playtech");
					errorMap.put("returnTypeError", "input");
					errorMap.put("orgError", errorMessage
							+ " during communication with playTech"); // customize
																		// error
					return false;
				} else {
					errorMap.put("returnTypeError", "input");
					errorMap.put("orgError",
							"Undefined Error type from PlayTech System");
					System.out
							.println("Undefined Error type from PlayTech System");
					return false;
				}

			}
		

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * This method is used to create binding between player and affiliate
	 * 
	 * @param depositAnyWhere
	 *            , can player deposit money any where
	 * @return boolean whether eligible to make transaction or not
	 * @throws LMSException
	 * @throws SQLException
	 */

	/*public static boolean affiliatePlrBinding(String depositAnyWhere,
			String plrId, String affiliateId, Connection con)
			throws LMSException {

		// first check whether player axists in the player affiliate mapping
		// tablr or not
		String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(
				con, plrId);
		System.out.println("refAffiliateId" + refAffiliateId);
		boolean PlrExist = false;
		if (refAffiliateId == null) {
			// player in not registered in OLA
			PlrExist = false;
		} else {
			// player is mapped with some affiliate
			PlrExist = true;
		}

		if (PlrExist) {
			if (depositAnyWhere.equalsIgnoreCase("YES")) {
				return true;
			} else {
				// check mapping
				boolean isMapped = affiliateId.equalsIgnoreCase(refAffiliateId);
				if (isMapped) {
					return true;
				} else {
					return false;
				}
			}
		} else {// means player does not exists in OLA System
			// here send the request to PlayTech to get the Player Info with the
			// Affiliate details
			// call function to communicate with PT to get the player Info
			String affiliateIdFrmPT = "default"; // possible values are
													// default,NOT_IN_OLA,IN_OLA
			if ("default".equalsIgnoreCase(affiliateIdFrmPT)) {
				// in this case again send the request to PT for player
				// affiliate binding
				// call function to communicate PT to binding with parameter
				// plrId and ref_user_id(affiliate id)
				String isBindFromPT = "SUCCESS"; // success or error
				if ("SUCCESS".equalsIgnoreCase(isBindFromPT)) {
					// here bind the player in OLA DB
					CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
							affiliateId);
					return true;
				} else {
					System.out
							.println("ERROR returned from playtech system during player affiliate binding");
					return false;
				}
			} else {
				// player is registered in Playtech but not registered in OLA
				// system
				// now we need to check affiliate id in OLA system

			}
			if ("NOT_IN_OLA".equalsIgnoreCase(affiliateIdFrmPT)) {
				// means player is bind with some online affiliate so no neeed
				// to bind in LMS
				// transaction can not be done in offline affiliate
				// if deposit anyehere is true then only we can make transaction
				// through offline affiliate
				if (depositAnyWhere.equalsIgnoreCase("YES")) {
					return true;
				} else {
					return false;
				}

			} else if ("IN_OLA".equalsIgnoreCase(affiliateIdFrmPT)) {
				// means data is not in synch with playtech system
				// as per the response from playtech this player should be bind
				// in OLA system(LMS)
				System.out
						.println("Data conflict between playtech and OLA, during player INFO");
				// need to take some action send mail to admin to get it correct
				return false;
			} else {
				System.out
						.println("some undefined status from Play tech system "
								+ affiliateIdFrmPT);
				return false;
			}
		}
	}*/
	public static String affiliatePlrBinding(String depositAnyWhere,
			String plrId, String affiliateId,int affiliateOrgId,Connection con,String rootPath,int walletId)
			throws LMSException {

		// first check whether player axists in the player affiliate mapping
		// tablr or not
		PreparedStatement pStatement = null;
		try
		{
		String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(
				con, plrId,walletId);
		System.out.println("refAffiliateId" + refAffiliateId);
		boolean PlrExist = false;
		if (refAffiliateId == null) {
			// player in not registered in OLA
			PlrExist = false;
		} else {
			// player is mapped with some affiliate
			PlrExist = true;
		}

		if (PlrExist) {
			if (depositAnyWhere.equalsIgnoreCase("YES")) {
				return "OK";
			} else {
				// check mapping
				boolean isMapped = affiliateId.equalsIgnoreCase(refAffiliateId);
				if (isMapped) {
					return "OK";
				} else {
					return "Player is not mapped With this Affiliate";
				}
			}
		} else {// means player does not exists in OLA System
			// here send the request to PlayTech to get the Player Info with the
			// Affiliate details
			// call function to communicate with PT to get the player Info
			OlaGetPlayerInfoBean respBean = new OlaGetPlayerInfoBean();
			respBean = callGetPlayerInfoApi(plrId,respBean,rootPath);
			if(respBean!=null)
			{
				if(respBean.getErrorCode().equalsIgnoreCase("0"))
				{
					if(respBean.getPlayerDataMap()!=null)
					{
						if(!(respBean.getPlayerDataMap().get(0).getValue()).equalsIgnoreCase("default03"))
						{
							pStatement = con.prepareStatement("select ref_user_id from st_ola_org_affiliate_mapping where ref_user_id='"+respBean.getPlayerDataMap().get(0).getValue()+"'");
							ResultSet rs = pStatement.executeQuery();
							if(rs.next())
							{
								// here bind the player in OLA DB
								//CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,respBean.getPlayerDataMap().get(1).getValue());
								return "OK";
							}
							else
							{
								System.out.println("affiliates does not  exist in the LMS Database but exists in Playetech");
								return "Some Internal ERROR";
							}
						}
						else if((respBean.getPlayerDataMap().get(0).getValue()).equalsIgnoreCase("default03"))
						{

							boolean resp = callChangePlayerApi(affiliateId,plrId,rootPath);
							if(resp)
							{
								// here bind the player in OLA DB
								CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
										affiliateId,affiliateOrgId,walletId);
								return "OK";
							}
							else
							{
								System.out.println("ERROR returned from playtech system during player affiliate binding");
								return "Some Internal ERROR";
							}
						}
						else if((respBean.getPlayerDataMap().get(0).getValue()).equalsIgnoreCase("ONLINE"))
						{
							if (depositAnyWhere.equalsIgnoreCase("YES")) {
								return "OK";
							} else {
								return "Player is already mapped with some ONLINE Affiliate";
							}
						}
						else
						{
							return "Problem in Player Mapping";
						}
					}
					else
					{
						System.out.println("Error Occured"+respBean.getErrorText());
						return respBean.getErrorText();
					}
				}
				else{
					System.out.println("error Occured"+respBean.getErrorText());
					return respBean.getErrorText();
				}
				
			/*String affiliateIdFrmPT = "default"; // possible values are
													// default,NOT_IN_OLA,IN_OLA
			if ("default10".equalsIgnoreCase(respBean.getAffiliateName())) {
				boolean resp = callChangePlayerApi(affiliateId,plrId,password);
				if(resp)
				{
					// here bind the player in OLA DB
					CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
							affiliateId);
			Digester  digester = new Digester();
					return true;
				}
				else
				{
					System.out.println("ERROR returned from playtech system during player affiliate binding");
					return false;
				}
				// in this case again send the request to PT for player
				// affiliate binding
				// call function to communicate PT to binding with parameter
				// plrId and ref_user_id(affiliate id)
				String isBindFromPT = "SUCCESS"; // success or error
				if ("SUCCESS".equalsIgnoreCase(isBindFromPT)) {
					// here bind the player in OLA DB
					CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
							affiliateId);
					return true;
				} else {
					System.out
							.println("ERROR returned from playtech system during player affiliate binding");
					return false;
				}
			} else if ("offline".equalsIgnoreCase(affiliateIdFrmPT)) {
				// player is registered in Playtech but not registered in OLA
				// system
				// now we need to check affiliate id in OLA system
				pStatement = con.prepareStatement("select ref_user_id from st_ola_org_affiliate_mapping where ref_user_id='"+respBean.getAffiliateName()+"'");
				ResultSet rs = pStatement.executeQuery();
				if(rs.next())
				{
					// here bind the player in OLA DB
					CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,respBean.getAffiliateName());
					return true;
				}
				else
				{
					System.out.println("affiliates is not exit in the LMS and ");
				}
			}
			else if ("online".equalsIgnoreCase(respBean.getPlayerStatus())) {
				// means player is bind with some online affiliate so no neeed
				// to bind in LMS
				// transaction can not be done in offline affiliate
				// if deposit anyehere is true then only we can make transaction
				// through offline affiliate
				if (depositAnyWhere.equalsIgnoreCase("YES")) {
					return true;
				} else {
					return false;
				}

			} else if ("IN_OLA".equalsIgnoreCase(affiliateIdFrmPT)) {
				// means data is not in synch with playtech system
				// as per the response from playtech this player should be bind
				// in OLA system(LMS)
				System.out
						.println("Data conflict between playtech and OLA, during player INFO");
				// need to take some action send mail to admin to get it correct
				return false;
			} else {
				System.out
						.println("some undefined status from Play tech system "
								+ affiliateIdFrmPT);
				return false;
			}
		}
			else{
				System.out.println("internal error occurs during communication with playtech");
				return false;
			}
		}*/
		}
			else{
				System.out.println("Some Internal Error Occured");
				return "Some Internal error occured";
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Some Internal error occured";
		}
	}

	public static boolean affiliatePlrBindingForWithdrawl(String withdrawlAnyWhere,String plrId, String affiliateId, Connection con,int walletId)
			throws LMSException {

		// first check whether player axists in the player affiliate mapping
		// tablr or not
		String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(
				con, plrId,walletId);
		System.out.println("refAffiliateId" + refAffiliateId);
		boolean PlrExist = false;
		if (refAffiliateId == null) {
			// player in not registered in OLA
			PlrExist = false;
		} else {
			// player is mapped with some affiliate
			PlrExist = true;
		}

		if (PlrExist) {
			if (withdrawlAnyWhere.equalsIgnoreCase("YES")) {
				return true;
			} else {
				// check mapping
				boolean isMapped = affiliateId.equalsIgnoreCase(refAffiliateId);
				if (isMapped) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			
			//check withdrawl any where  parameter if yes then ok 
			if (withdrawlAnyWhere.equalsIgnoreCase("NO")) {
				//Exit or show message
				return false;
			}else if (withdrawlAnyWhere.equalsIgnoreCase("YES")) {
				return true;				
			}								
			
		}
		return true;
	}

	
	 public static List<OlaNetGamingRowList> getPlrsCommissionOfRetailer(String refUserId,String startDate,String endDate,String rootPath) throws ParserConfigurationException, SAXException, IOException{
		 Map<String,String> getPlrCommParamMap = new HashMap<String,String>();
		 Digester digester = null;
		 OlaNetGamingXMLReader netGamingXMLReader = new OlaNetGamingXMLReader(); 
	  try {
	  getPlrCommParamMap.put("advertiser", URLEncoder.encode(refUserId,"UTF-8"));
	 getPlrCommParamMap.put("startdate", URLEncoder.encode(startDate,"UTF-8"));
	 getPlrCommParamMap.put("enddate",  URLEncoder.encode(endDate,"UTF-8")); 
	  DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	  DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	  InputStream iStream = getNetGamingResponseXML(OLAConstants.NETGAMING_PHP, getPlrCommParamMap,OLAConstants.NETGAMING_URL,rootPath);
	  if(iStream != null)
	  {
		  File file1 = new File("netGaming.xml");
			file1.createNewFile();
			FileWriter file = new FileWriter(file1);
			BufferedReader reader = new BufferedReader(new InputStreamReader(iStream)); 
			 PrintWriter outputFile = null;
			 outputFile = new PrintWriter(file);
			 String result ;
			while((result=reader.readLine())!=null)
			{
				System.out.println(result);
				outputFile.println(result);
			}
			outputFile.flush();
		
	  Document document = docBuilder.parse("netGaming.xml");
	  NodeList list = document.getElementsByTagName("AdvertiserStatsResponse");
	  Node node = list.item(0);
	  if(node == null)
	  {
		 digester = new Digester();
	 	digester.addObjectCreate("AdvertiserStats", OlaNetGamingXMLReader.class);
		digester.addObjectCreate("AdvertiserStats/row", OlaNetGamingRowList.class);
		digester.addSetNext("AdvertiserStats/row/","setRowList");
		
		digester.addObjectCreate("AdvertiserStats/row/column", OlaNetGamingRetailerData.class);
		digester.addSetNext("AdvertiserStats/row/column","setColumnList");
		
		digester.addSetProperties("AdvertiserStats/row/column/", "name", "name");
		digester.addBeanPropertySetter("AdvertiserStats/row/column/");
		netGamingXMLReader = (OlaNetGamingXMLReader) digester.parse("netGaming.xml");
		file1.deleteOnExit();
		return netGamingXMLReader.getRowList();
		 
	  }
	  else
	  {
		  file1.deleteOnExit();
		  if((node.toString().split(":")[0]).equals("[AdvertiserStatsResponse"))
		  {
			  System.out.println("error occurs");
			  return null;
		  }
		  else
		  {
			  System.out.println("no XML returned from playtech");
			  return null;
		  }
		  
	  }
	
	  }
	  else
	  {
		  System.out.println("error occurs during communication with the playtech");
		  return null;
	  }
	  
	  }
	  catch(UnsupportedEncodingException e)
	  { 
		  e.printStackTrace();
	  }
	  return netGamingXMLReader.getRowList();
	  }
	  

	public static OlaPTResponseBean callDepositApi(long transactionId,double amount,String userName,OlaPTResponseBean bean) {
		Map<String, String> DepositParamMap = new HashMap<String, String>();
		try {

	
			DepositParamMap.put("casino", URLEncoder.encode(OLAConstants.CASINO_NAME,"UTF-8"));
			DepositParamMap.put("username", URLEncoder.encode(userName.toUpperCase(),"UTF-8"));
			DepositParamMap.put("amount", URLEncoder.encode(Double.toString(amount),"UTF-8"));
			DepositParamMap.put("currency", URLEncoder.encode(OLAConstants.currency,"UTF-8"));
			DepositParamMap.put("externaltranid", URLEncoder.encode(Long.toString(transactionId),"UTF-8"));
			DepositParamMap.put("secretkey", URLEncoder.encode(OLAConstants.Depositsecretkey,"UTF-8"));
			DepositParamMap.put("casinosecret", URLEncoder.encode(OLAConstants.casinosecret,"UTF-8"));
		
			InputStream iStream = OLAClient.callPlaytech(OLAConstants.DEPOSIT_PHP, DepositParamMap,OLAConstants.DEPOSIT_URL);
			if (iStream != null) {
				Digester digester = new Digester();
				digester.addObjectCreate("fundtransfer", OlaPTResponseBean.class);
				digester.addBeanPropertySetter("fundtransfer/status", "depositStatus");
				digester.addBeanPropertySetter("fundtransfer/tranid", "imsDepositTransactionId");
				digester.addBeanPropertySetter("fundtransfer/error", "depositError");
				bean = (OlaPTResponseBean) digester.parse(iStream);
				return bean;
				} else {
				bean.setImsDepositTransactionId(0);
				bean.setDepositStatus("null");
				//error number 4141 -  network error occur during communication with playtech
				bean.setDepositError("4141");
				return bean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean.setDepositStatus("null");
			bean.setDepositError("4141");
			return bean;
		}
		
	}
	
	public static OlaGetPlayerInfoBean callGetPlayerInfoApi(String userName,OlaGetPlayerInfoBean bean,String rootPath) {
		 Digester digester = null;
			try
			{
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				
				InputStream stream = getPlayerInfo2Response(userName,rootPath);
				if(stream!=null)
				{
				File file1 = new File("getPlayerInfo.xml");
				file1.createNewFile();
				FileWriter file = new FileWriter(file1);
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); 
				 PrintWriter outputFile = null;
				 outputFile = new PrintWriter(file);
				 String result ;
				while((result=reader.readLine())!=null)
				{
					System.out.println(result);
					outputFile.println(result);
				}
				outputFile.flush();
				Document d = builder.parse("getPlayerInfo.xml");
				NodeList nl = d.getElementsByTagName("*");
				String errorElementTagName = null;
				String messageElementTagName = null;
				String errorTextElementTagName = null;
				for(int i=0;i<nl.getLength();i++)
				{
					Element element = (Element)nl.item(i);
					String getElementName  = element.toString().split(":")[1];
					if(getElementName.equalsIgnoreCase("errorCode"))
					{
						errorElementTagName = element.getNodeName();
						System.out.println("error"+errorElementTagName);
					}
					if(getElementName.equalsIgnoreCase("messageId"))
					{
						messageElementTagName = element.getNodeName();
						System.out.println("message"+messageElementTagName);
					}
					if(getElementName.equalsIgnoreCase("errorText"))
					{
						errorTextElementTagName = element.getNodeName();
						System.out.println("message"+errorTextElementTagName);
					}
				}
				digester = new Digester();
				 digester.addObjectCreate("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2", OlaGetPlayerInfoBean.class);
				 digester.setValidating( false );
					
				digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/"+messageElementTagName,"messageId");
				digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/"+errorElementTagName,"errorCode");
				digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/"+errorTextElementTagName,"errorText");
			
				digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/ns3:playerDataMap", "playerDataMap");
				
				digester.addObjectCreate("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/ns3:playerDataMap/", OlaGetPlayerBindingInfoBean.class);
				digester.addSetNext("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/ns3:playerDataMap","setPlayerDataMap");
				
				digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/ns3:playerDataMap/ns1:key", "key");
				digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns3:GetPlayerInfoResponse2/ns3:playerDataMap/ns1:value", "value");
				 bean = (OlaGetPlayerInfoBean)digester.parse("getPlayerInfo.xml");
				System.out.println("error1"+bean.getErrorCode());
				System.out.println("message1"+bean.getMessageId());
				System.out.println(bean.getErrorText());
				
				file1.deleteOnExit();
				return bean;
			}
				else
				{
					return null;
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return bean;
	}
	public static boolean callChangePlayerApi(String affiliateId,String userName,String rootPath) {
	
		Map<String, String> plrBindingMap = new HashMap<String, String>();
		try {

			plrBindingMap.put("casino", URLEncoder.encode(OLAConstants.CASINO_NAME,"UTF-8"));
			plrBindingMap.put("sync_username", URLEncoder.encode(userName.toUpperCase(),"UTF-8"));
			plrBindingMap.put("secretkey", URLEncoder.encode(OLAConstants.secretkey,"UTF-8"));
			plrBindingMap.put("responsetype", URLEncoder.encode("xml","UTF-8"));
			plrBindingMap.put("advertiser", URLEncoder.encode(affiliateId,"UTF-8"));
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream iStream = OLAClient.callPlaytechForChangePlayer(OLAConstants.cahngePlayer_PHP, plrBindingMap,OLAConstants.PLAYERINFO_URL,rootPath);

			if (iStream == null) {
				System.out.println("some internal error has occured may be at the time of communication with playtech system");
				return false;
			} else {
				Document doc = docBuilder.parse(iStream);
				// here parse the xml and read the messages
				NodeList nl = doc.getElementsByTagName("changeplayer");
				Element newaffiliate = (Element) nl.item(0);
				NodeList traList = newaffiliate
						.getElementsByTagName("transaction");
				Element tran = (Element) traList.item(0);
				String result = tran.getAttribute("result");
				// String result = "ERROR";
				if ("OK".equalsIgnoreCase(result)) {
					// communication successful affilaite id created at
					return true;
				} else if ("ERROR".equalsIgnoreCase(result)) {

					NodeList errList = newaffiliate
							.getElementsByTagName("error");
					Element err = (Element) errList.item(0);
					String errorCode = err.getAttribute("nr");

					Element errorElement = (Element) errList.item(0);
					NodeList textErrorList = errorElement.getChildNodes();
					String errorMessage = ((Node) textErrorList.item(0))
							.getNodeValue().trim();
					System.out.println("error is " + errorMessage);
					System.out.println("error is:" + errorCode);
					System.out.println("ERROR return form playtech during player binding");
					return false;
				} else {
					System.out
							.println("Undefined Error type from PlayTech System");
					return false;
				}

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static OlaPTResponseBean callWithdrawlApi(long transactionId,double amount,OlaPTResponseBean bean,String aunthticationCode) {
		Map<String, String> WithdrawlParamMap = new HashMap<String, String>();
	
		try {
			WithdrawlParamMap.put("casino", URLEncoder.encode(OLAConstants.CASINO_CODE,"UTF-8"));
			WithdrawlParamMap.put("currency", URLEncoder.encode(OLAConstants.currency,"UTF-8"));
			WithdrawlParamMap.put("amount", URLEncoder.encode(String.format("%.2f", amount),"UTF-8"));
			WithdrawlParamMap.put("secretkey", URLEncoder.encode(OLAConstants.secretkey,"UTF-8"));
			WithdrawlParamMap.put("referencecode", URLEncoder.encode(aunthticationCode,"UTF-8"));
			WithdrawlParamMap.put("externaltranid", URLEncoder.encode(Long.toString(transactionId),"UTF-8"));
			WithdrawlParamMap.put("olakey", URLEncoder.encode(OLAConstants.olakey,"UTF-8"));
			
			InputStream iStream = OLAClient.callPlaytech(OLAConstants.WITHDRAWAL_PHP, WithdrawlParamMap,OLAConstants.WITHDRAWL_URL);
			if (iStream != null) {
				
				Digester digester = new Digester();
				digester.addObjectCreate("olawithdraw", OlaPTResponseBean.class);
				digester.addBeanPropertySetter("olawithdraw/status","withdrawalStatus");
				digester.addBeanPropertySetter("olawithdraw/transactionid","imsWithdrawalTransactionId");
				digester.addBeanPropertySetter("olawithdraw/error","withdrawalError");
				bean = (OlaPTResponseBean) digester.parse(iStream);
				return bean;
			} else {
				bean.setImsWithdrawalTransactionId(0);
				bean.setWithdrawalStatus("null");
				bean.setWithdrawalError("4141");
				return bean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean.setDepositStatus("null");
			bean.setDepositError("4141");
			return bean;
			
		}
	}

	public static OlaGetPendingWithdrawalDetailsBean parsePendingWithdrawalXML(String userName,OlaGetPendingWithdrawalDetailsBean bean,String rootPath){
		 Digester digester = null;
		try
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			
			InputStream stream = getPendingWithdrawalResponse(userName,rootPath);
			if(stream!=null)
			{
			File file1 = new File("pending.xml");
			file1.createNewFile();
			FileWriter file = new FileWriter(file1);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); 
			 PrintWriter outputFile = null;
			 outputFile = new PrintWriter(file);
			 String result ;
			while((result=reader.readLine())!=null)
			{
				System.out.println(result);
				outputFile.println(result);
			}
			outputFile.flush();
			Document d = builder.parse("pending.xml");
			NodeList nl = d.getElementsByTagName("*");
			String errorElementTagName = null;
			String messageElementTagName = null;
			String errorTextElementTagName = null;
			for(int i=0;i<nl.getLength();i++)
			{
				Element element = (Element)nl.item(i);
				String getElementName  = element.toString().split(":")[1];
				if(getElementName.equalsIgnoreCase("errorCode"))
				{
					errorElementTagName = element.getNodeName();
					System.out.println("error"+errorElementTagName);
				}
				if(getElementName.equalsIgnoreCase("messageId"))
				{
					messageElementTagName = element.getNodeName();
					System.out.println("message"+messageElementTagName);
				}
				if(getElementName.equalsIgnoreCase("errorText"))
				{
					errorTextElementTagName = element.getNodeName();
					System.out.println("message"+errorTextElementTagName);
				}
			}
			digester = new Digester();
			 digester.addObjectCreate("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse", OlaGetPendingWithdrawalDetailsBean.class);
			 digester.setValidating( false );
				
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/"+messageElementTagName,"messageId");
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/"+errorElementTagName,"errorCode");
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/"+errorTextElementTagName,"errorText");
		
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals", "pendingWithdrawalList");
			
			digester.addObjectCreate("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals", OlaPendingWithdrawalDataBean.class);
			digester.addSetNext("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals","setPendingWithdrawalList");
			digester.addSetNext("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals/ns9:pendingWithdrawalCode","setPendingWithdrawalCodeList");
			
			digester.addSetNext("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals/ns9:amount/amount/number","setAmountList");
			digester.addSetNext("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals/ns9:withdrawRequestDate/ns1:date","setDateList");
			digester.addObjectCreate("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals", OlaPendingWithdrawalBean.class);
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals/ns9:pendingWithdrawalCode", "pendingWithdrawalCode");
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals/ns9:amount/amount/number", "amount");
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:GetPendingWithdrawalsResponse/ns9:pendingWithdrawals/ns9:withdrawRequestDate/ns1:date","withdrawRequestDate");
			
			 bean = (OlaGetPendingWithdrawalDetailsBean)digester.parse("pending.xml");
			System.out.println("error1"+bean.getErrorCode());
			System.out.println("message1"+bean.getMessageId());
			System.out.println(bean.getErrorText());
			file1.deleteOnExit();
			return bean;
			}
			else
			{
				bean.setErrorCode("null");
				return bean;
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			bean.setErrorCode("null");
			return bean;
		}
		
	}
	public static InputStream getPendingWithdrawalResponse(String userName,String rootPath)
	{
		String address = null;
		try{
			rootPath = rootPath.replace("\\", "/");
			 address ="https://umsgateway.sugal:4915/axis2/services/PlayerPaymentService";
			 URL url = new URL(address);		
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			 File keyFile = new File(rootPath+"certificate_sha1.pfx");
		      ((HttpsURLConnection) con).setSSLSocketFactory(getFactory(keyFile, "SkilRock@123"));
			con.setRequestProperty("SOAPAction", "getPendingWithdrawals");
			con.setRequestProperty("Content-Type", "text/xml");
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			
			OutputStreamWriter wr = new OutputStreamWriter(con
					.getOutputStream());
			
			wr.write("<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns9:GetPendingWithdrawalsRequest xmlns:ns9=\"http://www.playtech.com/Services/PlayerPaymentMessages\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns9:GetPendingWithdrawalsRequestType\"><s1:messageId xmlns:s1=\"http://www.playtech.com/Services/CommonTypes\">"+OLAConstants.messageId+"</s1:messageId><authentication xmlns=\"http://www.playtech.com/Services/CommonTypes\" xsi:type=\"AuthenticationByTrustedConnectionType\"><systemId>"+OLAConstants.systemId+"</systemId></authentication><objectIdentity xmlns=\"http://www.playtech.com/Services/CommonTypes\" xmlns:ns2=\"http://www.playtech.com/Services/PlayerCommonTypes\" xsi:type=\"ns2:PlayerIdentityByCasinoAndUsernameType\"><ns2:casinoname><casinoName>"+OLAConstants.CASINO_NAME+"</casinoName></ns2:casinoname><ns2:username><username>"+userName.toUpperCase()+"</username></ns2:username></objectIdentity></ns9:GetPendingWithdrawalsRequest></soapenv:Body></soapenv:Envelope> ");
			wr.flush();
			
			return con.getInputStream();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	public static InputStream getPlayerInfo2Response(String userName,String rootPath)
	{
		String address = null;
		try{
			rootPath = rootPath.replace("\\", "/");
			 address ="https://umsgateway.sugal:4915/axis2/services/ExternalAccountService";
			 URL url = new URL(address);		
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			 File keyFile = new File(rootPath+"certificate_sha1.pfx");
		      ((HttpsURLConnection) con).setSSLSocketFactory(getFactory(keyFile, "SkilRock@123"));
			con.setRequestProperty("SOAPAction", "getPlayerInfo2");
			con.setRequestProperty("Content-Type", "text/xml");
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			
			OutputStreamWriter wr = new OutputStreamWriter(con
					.getOutputStream());
			
		wr.write("<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns3:GetPlayerInfoRequest2 xmlns:ns3=\"http://www.playtech.com/Services/ExternalAccountMessages\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns3:GetPlayerInfoRequestType2\"><s1:messageId xmlns:s1=\"http://www.playtech.com/Services/CommonTypes\">"+OLAConstants.messageId+"</s1:messageId><authentication xmlns=\"http://www.playtech.com/Services/CommonTypes\" xsi:type=\"AuthenticationByTrustedConnectionType\"><systemId>"+OLAConstants.systemId+"</systemId></authentication><objectIdentity xmlns=\"http://www.playtech.com/Services/CommonTypes\" xmlns:ns2=\"http://www.playtech.com/Services/PlayerCommonTypes\" xsi:type=\"ns2:PlayerIdentityByCasinoAndUsernameType\"><ns2:casinoname><casinoName>"+OLAConstants.CASINO_NAME+"</casinoName></ns2:casinoname><ns2:username><username>"+userName.toUpperCase()+"</username></ns2:username></objectIdentity><ns3:requestType>marked</ns3:requestType><ns3:requestedPlayerData>11</ns3:requestedPlayerData><ns3:requestedPlayerData>35</ns3:requestedPlayerData><ns3:requestedPlayerData>2</ns3:requestedPlayerData></ns3:GetPlayerInfoRequest2></soapenv:Body></soapenv:Envelope>");
		System.out.println("<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns3:GetPlayerInfoRequest2 xmlns:ns3=\"http://www.playtech.com/Services/ExternalAccountMessages\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns3:GetPlayerInfoRequestType2\"><s1:messageId xmlns:s1=\"http://www.playtech.com/Services/CommonTypes\">"+OLAConstants.messageId+"</s1:messageId><authentication xmlns=\"http://www.playtech.com/Services/CommonTypes\" xsi:type=\"AuthenticationByTrustedConnectionType\"><systemId>"+OLAConstants.systemId+"</systemId></authentication><objectIdentity xmlns=\"http://www.playtech.com/Services/CommonTypes\" xmlns:ns2=\"http://www.playtech.com/Services/PlayerCommonTypes\" xsi:type=\"ns2:PlayerIdentityByCasinoAndUsernameType\"><ns2:casinoname><casinoName>"+OLAConstants.CASINO_NAME+"</casinoName></ns2:casinoname><ns2:username><username>"+userName.toUpperCase()+"</username></ns2:username></objectIdentity><ns3:requestType>marked</ns3:requestType><ns3:requestedPlayerData>11</ns3:requestedPlayerData><ns3:requestedPlayerData>35</ns3:requestedPlayerData><ns3:requestedPlayerData>2</ns3:requestedPlayerData></ns3:GetPlayerInfoRequest2></soapenv:Body></soapenv:Envelope>");
			wr.flush();
			
			return con.getInputStream();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static InputStream getNetGamingResponseXML(String phpName,Map<String,String> paramMap,String addr,String rootPath)
	{
		  try
	        {
		    rootPath = rootPath.replace("\\", "/");
			StringBuilder urlStr = new StringBuilder("");
			Set<String> paramSet = paramMap.keySet();
			for (String paramName : paramSet) {
				urlStr.append(paramName + "=" + paramMap.get(paramName) + "&");
			}
			urlStr.deleteCharAt(urlStr.length() - 1);
			System.out.println(urlStr);

			String address = null;
			address = addr + phpName;
			System.out.println(address);
			URL url = new URL(address);	
	        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
	        con.setDoOutput(true);
	        System.out.println("rootPath"+rootPath);
	        File keyFile = new File(rootPath+"advapi.sugal-prod.4601.p12");
	        con.setSSLSocketFactory(getFactory(keyFile, "TZefDhG1Cv"));
	        OutputStreamWriter wr = new OutputStreamWriter(con
					.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();
			return con.getInputStream();
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        	return null;
	        }
	}
	 public static SSLSocketFactory getFactory( File pKeyFile, String pKeyPassword ) throws Exception
	 {
	        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
	        KeyStore keyStore = KeyStore.getInstance("PKCS12");

	        InputStream keyInput = new FileInputStream(pKeyFile);
	        keyStore.load(keyInput, pKeyPassword.toCharArray());
	        keyInput.close();

	        keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

	        SSLContext context = SSLContext.getInstance("TLS");
	        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

	        return context.getSocketFactory();
	    }
	 
	 
	 public static  OlaGetCancelWithdrawalDetailsBean parseCancelWithdrawalXML(String withdrawalCode,OlaGetCancelWithdrawalDetailsBean bean1,String userName,String rootPath){
		 Digester digester = null;
		try
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			
			InputStream stream = getCancelWithdrawalResponse(withdrawalCode,userName,rootPath);
			if(stream!=null)
			{
			File file1 = new File("cancel.xml");
			file1.createNewFile();
			FileWriter file = new FileWriter(file1);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); 
			 PrintWriter outputFile = null;
			 outputFile = new PrintWriter(file);
			 String result ;
			while((result=reader.readLine())!=null)
			{
				System.out.println(result);
				outputFile.println(result);
			}
			outputFile.flush();
			
			Document d = builder.parse("cancel.xml");
			
			NodeList nl = d.getElementsByTagName("*");
			String errorElementTagName = null;
			String messageElementTagName = null;
			String errorTextElementTagName = null;
			for(int i=0;i<nl.getLength();i++)
			{
				Element element = (Element)nl.item(i);
				String getElementName  = element.toString().split(":")[1];
				if(getElementName.equalsIgnoreCase("errorCode"))
				{
					errorElementTagName = element.getNodeName();
					System.out.println("error"+errorElementTagName);
				}
				if(getElementName.equalsIgnoreCase("messageId"))
				{
					messageElementTagName = element.getNodeName();
					System.out.println("message"+messageElementTagName);
				}
				if(getElementName.equalsIgnoreCase("errorText"))
				{
					errorTextElementTagName = element.getNodeName();
					System.out.println("Error message"+errorTextElementTagName);
				}
			}
			digester = new Digester();
			digester.addObjectCreate("soapenv:Envelope/soapenv:Body/ns9:CancelPendingWithdrawalResponse", OlaGetCancelWithdrawalDetailsBean.class);
			 digester.setValidating( false );
			
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:CancelPendingWithdrawalResponse/"+messageElementTagName,"messageId");
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:CancelPendingWithdrawalResponse/"+errorElementTagName,"errorCode");
			digester.addBeanPropertySetter("soapenv:Envelope/soapenv:Body/ns9:CancelPendingWithdrawalResponse/"+errorTextElementTagName,"errorText");
			
			bean1 = (OlaGetCancelWithdrawalDetailsBean)digester.parse("cancel.xml");
			System.out.println("error1"+bean1.getErrorCode());
			System.out.println("message1"+bean1.getMessageId());
			System.out.println(bean1.getErrorText());
			file1.deleteOnExit();
			return bean1;
		}
			else
			{
				return null;
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	 public static InputStream getCancelWithdrawalResponse(String withdrawalCode,String userName,String rootPath)
		{
			String address = null;
	
			try{
				rootPath = rootPath.replace("\\", "/");
				 address ="https://umsgateway.sugal:4915/axis2/services/PlayerPaymentService";
				 URL url = new URL(address);		
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				 File keyFile = new File(rootPath+"certificate_sha1.pfx");
			      ((HttpsURLConnection) conn).setSSLSocketFactory(getFactory(keyFile, "SkilRock@123"));
				conn.setRequestProperty("SOAPAction", "cancelPendingWithdrawal");
				conn.setRequestProperty("Content-Type", "text/xml");
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				
				OutputStreamWriter wr = new OutputStreamWriter(conn
						.getOutputStream());
				
				wr.write("<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><ns9:CancelPendingWithdrawalRequest xmlns:ns9=\"http://www.playtech.com/Services/PlayerPaymentMessages\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns9:CancelPendingWithdrawalRequestType\"><s2:messageId xmlns:s2=\"http://www.playtech.com/Services/CommonTypes\">"+OLAConstants.messageId+"</s2:messageId><authentication xmlns=\"http://www.playtech.com/Services/CommonTypes\" xsi:type=\"AuthenticationByTrustedConnectionType\"><systemId>"+OLAConstants.CancelWithdrawalsystemId+"</systemId></authentication><objectIdentity xmlns=\"http://www.playtech.com/Services/CommonTypes\" xmlns:ns2=\"http://www.playtech.com/Services/PlayerCommonTypes\" xsi:type=\"ns2:PlayerIdentityByCasinoAndUsernameType\"><ns2:casinoname><casinoName>"+OLAConstants.CASINO_NAME+"</casinoName></ns2:casinoname><ns2:username><username>"+userName.toUpperCase()+"</username></ns2:username></objectIdentity><ns9:pendingWithdrawalCode>"+withdrawalCode+"</ns9:pendingWithdrawalCode></ns9:CancelPendingWithdrawalRequest></soapenv:Body></soapenv:Envelope>");
				wr.flush();
				
				return conn.getInputStream();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			
		}
		public static String newPlayerRegistration(OlaPlayerDetailsBean playerBean,String countryCode) {
			Map<String, String> plrMap = new HashMap<String, String>();
				try{	
					plrMap.put("birthdate", URLEncoder.encode(playerBean.getDateOfBirth(),"UTF-8"));
					plrMap.put("firstname", URLEncoder.encode(playerBean.getFirstName(),"UTF-8"));
					plrMap.put("lastname", URLEncoder.encode(playerBean.getLastName(),"UTF-8"));
					plrMap.put("password1", URLEncoder.encode(playerBean.getPassword(),"UTF-8"));
					plrMap.put("password2", URLEncoder.encode(playerBean.getPassword(),"UTF-8"));
					plrMap.put("email", URLEncoder.encode(playerBean.getEmail(),"UTF-8"));
					plrMap.put("countrycode", URLEncoder.encode(countryCode,"UTF-8"));
					plrMap.put("address", URLEncoder.encode(playerBean.getAddress(),"UTF-8"));
					plrMap.put("city", URLEncoder.encode(playerBean.getCity(),"UTF-8"));
					plrMap.put("language", URLEncoder.encode("English","UTF-8"));
					plrMap.put("phone", URLEncoder.encode(playerBean.getPhone(),"UTF-8"));
					plrMap.put("gender", URLEncoder.encode(playerBean.getGender(),"UTF-8"));
					plrMap.put("state", URLEncoder.encode(playerBean.getState(),"UTF-8"));
					plrMap.put("username", URLEncoder.encode(playerBean.getUsername().toUpperCase(),"UTF-8"));
					plrMap.put("zip", URLEncoder.encode("0","UTF-8"));
					plrMap.put("remotecreate", URLEncoder.encode("1","UTF-8"));
					plrMap.put("casino", URLEncoder.encode(OLAConstants.CASINO_NAME,"UTF-8"));
					plrMap.put("responsetype", URLEncoder.encode("xml","UTF-8"));
					plrMap.put("currency", URLEncoder.encode(OLAConstants.currency,"UTF-8"));
					plrMap.put("custom01", URLEncoder.encode("OFFLINE","UTF-8"));
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				InputStream iStream = OLAClient.callPlaytech(OLAConstants.NEW_PLAYER_PHP, plrMap,OLAConstants.NEW_PLAYER_URL);

				if (iStream == null) {
					System.out.println("some internal error has occured may be at the time of communication with playtech system");
					return "some internal error has occured may be at the time of communication with playtech system";
				} else {
					Document doc = docBuilder.parse(iStream);
					// here parse the xml and read the messages
					NodeList nl = doc.getElementsByTagName("newplayer");
					Element newaffiliate = (Element) nl.item(0);
					NodeList traList = newaffiliate
							.getElementsByTagName("transaction");
					Element tran = (Element) traList.item(0);
					String result = tran.getAttribute("result");
					if ("OK".equalsIgnoreCase(result)) {
						return "OK";
					} else if ("ERROR".equalsIgnoreCase(result)) {

						NodeList errList = newaffiliate
								.getElementsByTagName("error");
						Element err = (Element) errList.item(0);
						String errorCode = err.getAttribute("nr");

						Element errorElement = (Element) errList.item(0);
						NodeList textErrorList = errorElement.getChildNodes();
						String errorMessage = ((Node) textErrorList.item(0))
								.getNodeValue().trim();
						System.out.println("error is " + errorMessage);
						System.out.println("error is:" + errorCode);
						System.out.println("ERROR return form playtech during player binding");
						return errorMessage;
					} else {
						System.out
								.println("Undefined Error type from PlayTech System");
						return "Undefined Error type from PlayTech System";
					}

				}
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "OK";
		}
		public static InputStream  parseCheckUserNameAvailabilityApi(String userName)
		{
			try
			{ 
				URL url = new URL("http://27.251.125.195:6380/services/check_username.php");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setInstanceFollowRedirects(true);
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn
						.getOutputStream());
				String urlStr ="alias="+userName+"&PF=JSON"; 
				wr.write(urlStr);
				System.out.println(url+"?"+urlStr);
				wr.flush();
				return conn.getInputStream();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		public static InputStream  checkUserNameAvailabilityAtKhelPlayApi(String userName)
		{
			try
			{ 
				URL url = new URL("https://www.khelplayrummy.com/services/check_username_khelplay.php");
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setInstanceFollowRedirects(true);
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn
						.getOutputStream());
				String urlStr ="alias="+userName; 
				wr.write(urlStr);
				System.out.println(url+"?"+urlStr);
				wr.flush();
				return conn.getInputStream();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		public static OlaPTResponseBean callCheckTransactionIdForDeposit(OlaPTResponseBean bean,long LmsTransactionId)
		{
			Map<String,String> checkTransactionMap = new HashMap<String, String>();
			
			try{
				checkTransactionMap.put("casino", URLEncoder.encode(OLAConstants.CASINO_NAME, "UTF-8"));
				checkTransactionMap.put("secretkey", URLEncoder.encode(OLAConstants.secretkey, "UTF-8"));
				checkTransactionMap.put("externaltranid", URLEncoder.encode(Long.toString(LmsTransactionId), "UTF-8"));
				InputStream istream = OLAClient.callPlaytech(OLAConstants.CHECK_TRANSACTION_PHP, checkTransactionMap, OLAConstants.CHECK_TRANSACTION_URL);
				if(istream!=null)
				{
				Digester digester = new Digester();
				digester.addObjectCreate("checktransaction", OlaPTResponseBean.class);
				digester.addBeanPropertySetter("checktransaction/status", "status");
				digester.addBeanPropertySetter("checktransaction/id", "imsDepositTransactionId");
				bean = (OlaPTResponseBean) digester.parse(istream);
				return bean;
				}
				else
				{
					bean.setStatus("null");
					return bean;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				bean.setStatus("null");
				return bean;
			}
		}
		public static OlaPTResponseBean callCheckTransactionIdForWithdrawal(OlaPTResponseBean bean,long LmsTransactionId)
		{
			Map<String,String> checkTransactionMap = new HashMap<String, String>();
			
			try{
				checkTransactionMap.put("casino", URLEncoder.encode(OLAConstants.CASINO_NAME, "UTF-8"));
				checkTransactionMap.put("secretkey", URLEncoder.encode(OLAConstants.secretkey, "UTF-8"));
				checkTransactionMap.put("externaltranid", URLEncoder.encode(Long.toString(LmsTransactionId), "UTF-8"));
				InputStream istream = OLAClient.callPlaytech(OLAConstants.CHECK_TRANSACTION_PHP, checkTransactionMap, OLAConstants.CHECK_TRANSACTION_URL);
				if(istream!=null)
				{
				Digester digester = new Digester();
				digester.addObjectCreate("checktransaction", OlaPTResponseBean.class);
				digester.addBeanPropertySetter("checktransaction/status", "status");
				digester.addBeanPropertySetter("checktransaction/id", "imsWithdrawalTransactionId");
				bean = (OlaPTResponseBean) digester.parse(istream);
				return bean;
				}
				else
				{
					bean.setStatus("null");
					return bean;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				bean.setStatus("null");
				return bean;
			}
		}
		public static String affiliatePlrBindingRummy(String depositAnyWhere,
				String plrId,String affiliateId,int affiliateOrgId,int walletId,Connection con)
				throws LMSException {
			//	Connection con =DBConnect.getConnection();
			// first check whether player exists in the player affiliate mapping
			// table or not
		
			try
			{
				//con.setAutoCommit(false);
			String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(
					con, plrId,walletId);
			System.out.println("refAffiliateId" + refAffiliateId);
			boolean PlrExist = false;
			if (refAffiliateId == null) {
				// player in not registered in OLA
				PlrExist = false;
			} else {
				// player is mapped with some affiliate
				PlrExist = true;
			}

			if (PlrExist) {
				if (depositAnyWhere.equalsIgnoreCase("YES")) {
					return "OK";
				} else {
					// check mapping
					boolean isMapped = affiliateId.equalsIgnoreCase(refAffiliateId);
					if (isMapped) {
						return "OK";
					} else {
						return "Player not mapped with this Affiliate in OLA";
					}
				}
			} else {// means player does not exists in OLA System
				// hence Bind player with affiliate in OLA DB
				CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
						affiliateId,affiliateOrgId,walletId);
				//con.commit();
				return "OK";
			
									
			}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return "Some Internal error occured";
			}
		}
		
		public static OlaPlayerDetailsBean newRummyPlayerRegistration(OlaPlayerDetailsBean playerBean) {
			try
			{ 
				//URL url = new URL("https://www.khelplayrummy.com//services/register.php");
				URL url = new URL("http://27.251.125.195:6380//services/register.php");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn
						.getOutputStream());
				String urlStr ="login="+playerBean.getUsername()+"&email="+playerBean.getEmail()+"&passwd="+playerBean.getPassword()+"&link_name= &buddy= &uid= &toc=on &state="+playerBean.getState()+"&type=quick&PF=JSON&confirm_passwd="+playerBean.getPassword(); 
				wr.write(urlStr);
				System.out.println("URL:"+url+"Parameters:"+urlStr+"Request:"+wr.toString());
				wr.flush();
				
				InputStream iStream =conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(iStream ));
				StringBuilder sb =new StringBuilder();
				String line =null;
						while ((line = reader.readLine()) != null) {
								sb.append(line);
								}
						
				  String msg = sb.toString();
				  System.out.println("Player Registration :"+msg);
				  String []msgParse= msg.split(",");
				  int length = msgParse.length;
				  String success_flag = msgParse[0].split(":")[1];
				  
				if(success_flag.equalsIgnoreCase("false")){
					 String error_msg =msgParse[1].split(":")[1].split("}")[0];
					 playerBean.setMsg(error_msg);
					 playerBean.setSuccess(false);
					System.out.println(" Already exits at Khel Play!!");
					return  playerBean;
								
				}
				else {
					System.out.println("success_flag"+msgParse[length-1].split(":")[1].split("}")[0]);
					if(msgParse[length-1].split(":")[1].split("}")[0].equalsIgnoreCase("true")){
						String  verified = msgParse[1].split(":")[1].replace("\"", "");
						String account_id = msgParse[2].split(":")[1].replace("\"","");
						playerBean.setSuccess(true);
						playerBean.setMsg(success_flag);
						playerBean.setAccountId(account_id);
						System.out.println("verified :"+verified+" account_id : "+account_id);
						return  playerBean;
						
						
					}
					else {
						playerBean.setSuccess(false);
						playerBean.setMsg("Some Error");
					}
					
					
				}
				
				return  playerBean;
				
			}
			catch(Exception e)
			{
				playerBean.setSuccess(false);
				playerBean.setMsg("Some Error");
				e.printStackTrace();
			
				
			}
			return  playerBean;
				
		}
		
		public static InputStream  parseCheckEmailAvailabilityApi(String email)
		{
			try
			{ 
				URL url = new URL("http://27.251.125.195:6380/services/check_email.php");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn
						.getOutputStream());
				String urlStr ="email="+email+"&PF=JSON"; 
				wr.write(urlStr);
				wr.flush();
				return conn.getInputStream();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}		
	public static 	OlaPlayerDetailsBean newPMSPlayerRegistration(OlaPlayerDetailsBean playerBean){
		// Call Player Mgmt Api 
		String method = "playerRegistrationAction";
		JSONObject params = new JSONObject();
        params.put("firstName",playerBean.getFirstName());
        params.put("lastName", playerBean.getLastName());
        if(playerBean.getGender().equalsIgnoreCase("M")){
        	 params.put("gender","MALE");
        }else{
        	 params.put("gender","FEMALE");
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-dd-MM");	
		SimpleDateFormat sf1 = new SimpleDateFormat("dd-MM-yyyy");
		try{
			String birthdate=sf1.format(sf.parse(playerBean.getDateOfBirth()));
			  params.put("dateOfBirth",birthdate);
		}catch(Exception e){
			e.printStackTrace();
		}
		
      
        params.put("userName",playerBean.getUsername());
        params.put("emailId",playerBean.getEmail());
        params.put("mobileNum",playerBean.getPhone());
        params.put("address",playerBean.getAddress());
        params.put("city",playerBean.getCity());
        JSONObject responseObj =Utility.sendCallApi(method, params, "7");
        if(responseObj==null){
        	playerBean.setSuccess(false);
    		playerBean.setMsg("Registration Error From Player Lottery");
    		return playerBean;
        }
        boolean isSuccess=false;
        try{
        	isSuccess= responseObj.getBoolean("isSuccess");
		   }catch(Exception e){
        	e.printStackTrace();
        	playerBean.setSuccess(false);
    		playerBean.setMsg("Registration Error From Player Lottery");
    		return playerBean;
	        	
        }
       
       System.out.println("call API Deposit Done");
       if(isSuccess){
    		playerBean.setAccountId(responseObj.getInt("playerId")+"");
    		playerBean.setSuccess(true);
    		playerBean.setMsg("Player Registered At Player Lottery");
    		return playerBean;
    	 											    	
    	   
       }else{
    		playerBean.setSuccess(false);
    		playerBean.setMsg(responseObj.getString("errorMsg"));
       }
	
		return playerBean;
	}
	
	
	
	
public static void setWalletDataMap(){
	Connection con =DBConnect.getConnection();
	PreparedStatement pstmt=null;
	ResultSet rs = null;
	olaWalletMap=new  HashMap<Integer, String>();
	try{
		String walletQuery ="select wallet_id,wallet_display_name from  st_ola_wallet_master where wallet_status='ACTIVE'"; 	
		pstmt = con.prepareStatement(walletQuery);
		rs=pstmt.executeQuery();
		while(rs.next()){
			olaWalletMap.put(rs.getInt("wallet_id"), rs.getString("wallet_display_name"));
			
		}
		
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		try{	
			if(con!=null){
					con.close();
					}
			if(pstmt!=null){
				pstmt.close();
				
			}
			if(rs!=null){
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
	}
}
public static String affiliatePlrBindingKpRummy(String depositAnyWhere,
		String plrId,UserInfoBean userBean,int walletId,Connection con)
		throws LMSException {
	//	Connection con =DBConnect.getConnection();
	// first check whether player exists in the player affiliate mapping
	// table or not

	try
	{
		//con.setAutoCommit(false);
	String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(con, plrId,walletId);
	logger.info("refAffiliateId" + refAffiliateId);
	boolean PlrExist = false;
	if (refAffiliateId == null) {
		// player in not registered in OLA
		PlrExist = false;
	} else {
		// player is mapped with some affiliate
		PlrExist = true;
	}

	if (PlrExist) {
		if (depositAnyWhere.equalsIgnoreCase("YES")) {
			return "OK";
		} else {
			// check mapping
			boolean isMapped = (userBean.getUserName()).equalsIgnoreCase(refAffiliateId);
			if (isMapped) {
				return "OK";
			} else {
				return "Player not mapped with this Affiliate in OLA";
			}
		}
	} else {// means player does not exists in OLA System
		// hence Bind player with affiliate in OLA DB
		boolean isBind =bindPlrAtKpRummy(plrId,userBean.getUserOrgId(),walletId);
		if(!isBind){
			logger.info("Error In Player Binding at KP Rummy");
			return "Error In Player Binding Try After Some Time";
		}
		CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
				userBean,walletId);
		//con.commit();
		return "OK";
	
							
	}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return "Some Internal error occured";
	}
}	
/*public static String affiliatePlrBindingKpRummy(String depositAnyWhere,
		String plrId,String affiliateId,int affiliateOrgId,int walletId,Connection con,String walletName)
		throws LMSException {
	//	Connection con =DBConnect.getConnection();
	// first check whether player exists in the player affiliate mappisng
	// table or not

	try
	{
		//con.setAutoCommit(false);
	String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(
			con, plrId,walletId);
	System.out.println("refAffiliateId" + refAffiliateId);
	boolean PlrExist = false;
	if (refAffiliateId == null) {
		// player in not registered in OLA
		PlrExist = false;
	} else {
		// player is mapped with some affiliate
		PlrExist = true;
	}

	if (PlrExist) {
		if (depositAnyWhere.equalsIgnoreCase("YES")) {
			return "OK";
		} else {
			// check mapping
			boolean isMapped = affiliateId.equalsIgnoreCase(refAffiliateId);
			if (isMapped) {
				return "OK";
			} else {
				return "Player not mapped with this Affiliate in OLA";
			}
		}
	} else {// means player does not exists in OLA System
		// hence Bind player with affiliate in OLA DB
		boolean isBind =bindPlrAtKpRummy(plrId,affiliateOrgId,walletId);
		if(!isBind){
			logger.info("Error In Player Binding at KP Rummy");
			return "Error In Player Binding Try After Some Time";
		}
		CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
				affiliateId,affiliateOrgId,walletId);
		//con.commit();
		return "OK";
	
							
	}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return "Some Internal error occured";
	}
}*/



public static Map<Integer, String> getOlaWalletDataMap(){
	if(olaWalletMap==null){
		setWalletDataMap();
	}
	return olaWalletMap;
	
}




public static String prepareXMLFromData(Map<String,String> argMap){
	
	try{

		DocumentBuilderFactory  docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		// Create Root Element 
		Element root = doc.createElement("weaverServiceRequest");
		doc.appendChild(root);
		// add child nodes 
		for (Map.Entry<String,String> entry : argMap.entrySet()) {
			Element plrUserName =doc.createElement( entry.getKey());
			plrUserName.appendChild(doc.createTextNode(entry.getValue()));
			root.appendChild(plrUserName);
		}
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer transformer=transFact.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter stringWriter = new StringWriter();
		transformer.transform(source,new StreamResult(stringWriter));
		
		return stringWriter.toString();
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
}	
/**
 * This Method Prepare XML only for KP Rummy Registration 
 * @param plrDetails
 * @return
 */
	public static String prepareXMLForKPRegistration(OlaPlayerRegistrationRequestBean plrDetails,String walletId){
		
		try{
			
			
			DocumentBuilderFactory  docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			//Create Rool Element For Request Data
			Element root = doc.createElement("weaverServiceRequest");
			doc.appendChild(root);
			Element element =doc.createElement("requestType");
			element.appendChild(doc.createTextNode("REGISTRATION"));
			root.appendChild(element);
			element =doc.createElement("domainName");
			element.appendChild(doc.createTextNode(OLAUtility.getWalletIntBean(walletId).getTpWalletCode()));
			root.appendChild(element);
			// Create Root Element for Player Reg Data 
			Element subroot = doc.createElement("playerRegistrationData");
			root.appendChild(subroot);
			// add child nodes 
			/*element =doc.createElement("firstName");
			element.appendChild(doc.createTextNode(plrDetails.getFirstName()==null?"":plrDetails.getFirstName()));
			subroot.appendChild(element);
			element =doc.createElement("lastName");
			element.appendChild(doc.createTextNode(plrDetails.getLastName()==null?"":plrDetails.getLastName()));
			subroot.appendChild(element);*/
			if("KhelPlayRummy".equalsIgnoreCase(plrDetails.getWalletName()) || "GroupRummy".equalsIgnoreCase(plrDetails.getWalletName()) ||"ALA_WALLET".equalsIgnoreCase(plrDetails.getWalletName())){
				element =doc.createElement("userName");
				element.appendChild(doc.createTextNode(plrDetails.getUsername()==null?"":plrDetails.getUsername()));
				subroot.appendChild(element);
				if(!"ALA_WALLET".equalsIgnoreCase(plrDetails.getWalletName())){
					element =doc.createElement("emailId");
					element.appendChild(doc.createTextNode(plrDetails.getEmail()==null?"":plrDetails.getEmail()));
					subroot.appendChild(element);
				}
			}
			element =doc.createElement("mobileNo");
			element.appendChild(doc.createTextNode(plrDetails.getPhone()));
			subroot.appendChild(element);
		/*	element =doc.createElement("dob");
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-dd-MM");	
			SimpleDateFormat sf1 = new SimpleDateFormat("dd/MM/yyyy");
			String birthdate ="";
				try{
					 birthdate=sf1.format(sf.parse(plrDetails.getDateOfBirth()));
					
				}catch(Exception e){
					e.printStackTrace();
				}
			element.appendChild(doc.createTextNode(birthdate));
			subroot.appendChild(element);
*/			element =doc.createElement("countryCode");
			element.appendChild(doc.createTextNode(plrDetails.getCountry()));
			subroot.appendChild(element);
			element =doc.createElement("requestIp");
			element.appendChild(doc.createTextNode(plrDetails.getRequestIp()));
			subroot.appendChild(element);
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer transformer=transFact.newTransformer();
			DOMSource source = new DOMSource(doc);
			StringWriter stringWriter = new StringWriter();
			transformer.transform(source,new StreamResult(stringWriter));
			
			return stringWriter.toString();
		}catch(Exception e){
			e.printStackTrace();
		}	
	

	
	
	
	
	return null;
}
@SuppressWarnings("unchecked")
public static Map<String,String> prepareDataFromXml(InputStream xmlData){
	
	try{

		Digester digester = new Digester();
		   digester.setRules(new ExtendedBaseRules());
	        digester.addObjectCreate("weaverServiceResponse", HashMap.class);
	         digester.addRule("*", new Rule() {
	          
				@Override public void body(String nspace, String name, String text) {
	                ((HashMap<String,String>)getDigester().peek()).put(name, text);
	            }
	        });
	         Map<String,String> map = (Map<String,String>) digester.parse(xmlData);
		return map;
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public static boolean bindPlrAtKpRummy(String plrName,int affiliateOrgId,int walletId) {
	try{
		Map<String,String> bindReqMap = new HashMap<String, String>();
		bindReqMap.put("requestType","PLAYER_AFFILIATE_BINDING");
		bindReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpWalletCode());
		bindReqMap.put("userName",plrName);
		bindReqMap.put("affiliateReference",affiliateOrgId+"");
		InputStream bindResponse =OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(bindReqMap),walletId,OLAConstants.depReq);
		Map<String,String> bindRespMap = null;
		bindRespMap=OLAUtility.prepareDataFromXml(bindResponse);
		logger.info(bindRespMap);
		if(bindRespMap==null){
			logger.info("player binding msg:"+bindRespMap);
			return false;
		}else if(bindRespMap.get("errorCode")!=null || bindRespMap.get("errorMsg")!=null){
			logger.info("player binding msg:"+bindRespMap.get("errorMsg")+"Code:"+bindRespMap.get("errorCode"));
			return false;
			
		}else if(bindRespMap.get("respMsg")!=null){
			logger.info("player binding msg:"+bindRespMap.get("respMsg"));
			return true;
		}
	}catch(Exception e){
		e.printStackTrace();
	}
return false;
}

public static OlaPlayerRegistrationRequestBean newKpRummyPlayerRegistration(OlaPlayerRegistrationRequestBean playerBean,int walletId) {
	try
	{ 
		//URL url = new URL("https://www.khelplayrummy.com//services/register.php");
		
		InputStream plrRegResponse =OLAClient.callKhelPlayRummy(prepareXMLForKPRegistration(playerBean,walletId+""),walletId,OLAConstants.depReq);
		Map<String,String> plrRegRespMap = null;
		plrRegRespMap=OLAUtility.prepareDataFromXml(plrRegResponse);
		logger.info("Registration Response"+plrRegRespMap);
		if(plrRegRespMap==null){
		
			playerBean.setMsg("Error In Player Registration Plesae Try After Some Time");
			playerBean.setSuccess(false);
			
		}else if(plrRegRespMap.get("errorCode")!=null||plrRegRespMap.get("errorMsg")!=null){
		
			playerBean.setMsg(plrRegRespMap.get("errorMsg"));
			playerBean.setSuccess(false);
			return  playerBean;
		
			
		}else if(plrRegRespMap.get("respMsg")!=null){
		
			playerBean.setSuccess(true);
			playerBean.setMsg(plrRegRespMap.get("respMsg"));
			playerBean.setAccountId(plrRegRespMap.get("accountId"));
			if(plrRegRespMap.containsKey("password")){
				playerBean.setPassword(plrRegRespMap.get("password"));
			}else{
				playerBean.setPassword("");
			}
			
			
			return  playerBean;
		
		}

	}catch(Exception e){
		playerBean.setSuccess(false);
		playerBean.setMsg("Some Error");
		e.printStackTrace();
	}
	return  playerBean;
		
}
public static OlaRummyNGDepositRepBean prepareBeanDataFromXml(InputStream xmlData){
	
	try{
		//URL rules=new Digester().getClass().getResource("/home/gauravk/neeraj/rules.xml");  
		//Digester digester=DigesterLoader.createDigester(rules); 

			Digester  digester = new Digester();
		 	digester.addObjectCreate("weaverServiceResponse", OlaRummyNGDepositRepBean.class);
		 	digester.addBeanPropertySetter("weaverServiceResponse/requestType","requestType");
			digester.addBeanPropertySetter("weaverServiceResponse/fromDate","fromDate");
			digester.addBeanPropertySetter("weaverServiceResponse/toDate","toDate");
			digester.addBeanPropertySetter("weaverServiceResponse/errorCode","errorCode");
			digester.addBeanPropertySetter("weaverServiceResponse/errorMsg","errorMsg");
		 	digester.addObjectCreate("weaverServiceResponse/netGamingTxnReport", OlaRummyNGTxnRepBean.class);
			digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/domainName","domainName");
			digester.addSetNext("weaverServiceResponse/netGamingTxnReport/","setRummyngTxnList");
		 	digester.addObjectCreate("weaverServiceResponse/netGamingTxnReport/playerTransaction", OlaRummyNGPlrTxnRepBean.class);
		 	digester.addSetNext("weaverServiceResponse/netGamingTxnReport/playerTransaction","setRummyngplrTxnList");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/accountId","accountId");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/userName","userName");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/amount","amount");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/paymentType","paymentType");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/providerName","providerName");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/txnTime","txnTime");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/txnId","txnId");
		 	digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction/gameType","gameType");
			
		//	digester.addSetProperties("weaverServiceResponse/netGamingTxnReport/playerTransaction", "name", "name");
			//digester.addBeanPropertySetter("weaverServiceResponse/netGamingTxnReport/playerTransaction");
			
			/*digester.addObjectCreate("AdvertiserStats", OlaNetGamingXMLReader.class);
			digester.addObjectCreate("AdvertiserStats/row", OlaNetGamingRowList.class);
			digester.addSetNext("AdvertiserStats/row/","setRowList");
			
			digester.addObjectCreate("AdvertiserStats/row/column", OlaNetGamingRetailerData.class);
			digester.addSetNext("AdvertiserStats/row/column","setColumnList");
			
			digester.addSetProperties("AdvertiserStats/row/column/", "name", "name");
			digester.addBeanPropertySetter("AdvertiserStats/row/column/");*/
		 
	         OlaRummyNGDepositRepBean map = (OlaRummyNGDepositRepBean) digester.parse(xmlData);
		return map;
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}


public static OlaPlayerRegistrationRequestBean preparePlayerInfoBeanDataFromXml(InputStream xmlData){
	
	try{
			Digester  digester = new Digester();
		 	digester.addObjectCreate("weaverServiceResponse", OlaPlayerRegistrationRequestBean.class);
		 	digester.addBeanPropertySetter("weaverServiceResponse/mobileNo","phone");
			digester.addBeanPropertySetter("weaverServiceResponse/userName","username");
			digester.addBeanPropertySetter("weaverServiceResponse/accountId","accountId");
			digester.addBeanPropertySetter("weaverServiceResponse/emailId","email");
			digester.addBeanPropertySetter("weaverServiceResponse/registrationDate","plrRegDate");
			digester.addBeanPropertySetter("weaverServiceResponse/errorCode","errorCode");
			digester.addBeanPropertySetter("weaverServiceResponse/errorMsg","errorMsg");
		 	
			//OlaPlayerRegistrationRequestBean playerRegInfoBean = (OlaPlayerRegistrationRequestBean) digester.parse(new StringReader(xmlData));
		 	OlaPlayerRegistrationRequestBean playerRegInfoBean = (OlaPlayerRegistrationRequestBean) digester.parse(xmlData);
		return playerRegInfoBean;
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}


public static String getWalletName(int walletId){
	if(olaWalletMap==null){
		setWalletDataMap();
	}
	return olaWalletMap.get(walletId);
}
/**
 * get Wallet Integration bean from wallet Id or wallet Integration Code
 * @param wallet
 * @return
 */
public static OlaWalletIntegrationBean getWalletIntBean(String wallet){
	
	if(walletIntBeanMap!=null){
		return walletIntBeanMap.get(wallet);
	}
	return null;
}
public static String affiliatePlrBinding(String depositAnyWhere,String plrId,UserInfoBean userBean,int walletId,Connection con)throws LMSException {
	//	Connection con =DBConnect.getConnection();
	// first check whether player exists in the player affiliate mapping
	// table or not

	try
	{
		//con.setAutoCommit(false);
	String refAffiliateId = CommonFunctionsHelper.checkPlrAffiliateMapping(con, plrId,walletId);
	logger.info("refAffiliateId" + refAffiliateId);
	boolean PlrExist = false;
	if (refAffiliateId == null) {
		// player in not registered in OLA
		PlrExist = false;
	} else {
		// player is mapped with some affiliate
		PlrExist = true;
	}

	if (PlrExist) {
		if (depositAnyWhere.equalsIgnoreCase("YES")) {
			return "OK";
		} else {
			// check mapping
			boolean isMapped = (userBean.getUserName()).equalsIgnoreCase(refAffiliateId);
			if (isMapped) {
				return "OK";
			} else {
				return "Player not mapped with this Affiliate in OLA";
			}
		}
	} else {// means player does not exists in OLA System
		// hence Bind player with affiliate in OLA DB
		CommonFunctionsHelper.bindPlrNAffiliate(con, plrId,
				userBean,walletId);
		//con.commit();
		return "OK";
	
							
	}
	}
	catch(Exception e)
	{
		logger.error("Exception ", e);
		return "Some Internal error occured";
	}
}	
public static int getPlrId(Connection con,String plrName){
	PreparedStatement pstmt =null;
	ResultSet rs =null;
	int lms_plr_id= 0;
	try{
		String query ="select lms_plr_id  from st_ola_player_master where username =?";
		pstmt =con.prepareStatement(query);
		pstmt.setString(1,plrName);
		rs = pstmt.executeQuery();
	
		if(rs.next()){
			lms_plr_id = rs.getInt("lms_plr_id");
		}else {
			pstmt =con.prepareStatement(query);
			pstmt.setString(1,"ANO@NYMOUS");
			rs = pstmt.executeQuery();
			if(rs.next()){
				lms_plr_id = rs.getInt("lms_plr_id");
			}
			
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		try{
			if(pstmt!=null){
				pstmt.close();
			}
			if(rs!=null){
				rs.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	return lms_plr_id;
	
}
public static OlaWalletBean getWalletBean(int walletId){
	return olaWalletDataMap.get(walletId);
	
}


}