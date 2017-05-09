package com.skilrock.lms.coreEngine.ola.common;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;


public class OLAClient {
	private static Log logger =LogFactory.getLog(OLAClient.class);
	public static InputStream callPlaytech(String phpName,
			Map<String, String> paramMap, String addr) {
		try {

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
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization",
					"Basic dXNlcjpwYXNzd29yZA==");
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();
			return conn.getInputStream();

		} catch (Exception e) {
			System.out.println("inside exception ");
			e.printStackTrace();
			return null;
		}

	}
	public static InputStream callPlaytechForChangePlayer(String phpName,
			Map<String, String> paramMap, String addr, String rootPath) {
		try {

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
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 File keyFile = new File(rootPath+"casino.sugal-prod.1700.khelplay.p12");
		      ((HttpsURLConnection) conn).setSSLSocketFactory(OLAUtility.getFactory(keyFile, "6Q0xZR5n"));
			conn.setRequestProperty("Authorization",
					"Basic dXNlcjpwYXNzd29yZA==");
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();
			return conn.getInputStream();

		} catch (Exception e) {
			System.out.println("inside exception ");
			e.printStackTrace();
			return null;
		}

	}
	public static InputStream callPlaytechForNewAffiliate(String phpName,
			Map<String, String> paramMap, String addr, String rootPath) {
		try {

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
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 File keyFile = new File(rootPath+"advapi.sugal-prod.4601.p12");
		      ((HttpsURLConnection) conn).setSSLSocketFactory(OLAUtility.getFactory(keyFile, "TZefDhG1Cv"));
			conn.setRequestProperty("Authorization",
					"Basic dXNlcjpwYXNzd29yZA==");
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(urlStr.toString());
			wr.flush();
			return conn.getInputStream();

		} catch (Exception e) {
			System.out.println("inside exception ");
			e.printStackTrace();
			return null;
		}

	}
public static InputStream callKhelPlayRummy(String reqeustData,int walletId,String reqType) throws LMSException{
	// Uncomment the below code to bypass SSL
	try{
		/*SSLContext ssl_ctx = SSLContext.getInstance("TLS");
	     TrustManager[ ] trust_mgr = get_trust_mgr();
	     ssl_ctx.init(null,                // key manager
	                  trust_mgr,           // trust manager
	                  new SecureRandom()); // random number generator
	     HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
	     HostnameVerifier allHostsValid = new HostnameVerifier() {   
	         public boolean verify(String hostname, SSLSession session) {  
	             return true;   
	         }   
	     };
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);  */ 
		
		 URL url =null; 
		 URLConnection conn =null;
		
		if(OLAConstants.isHttp){
			 url = new URL("http://"+OLAUtility.getWalletIntBean(walletId+"").getTpIp()+reqType ); 
			 logger.info("ola URL http:// "+url.getHost()+url.getPath());
			  	conn =  (HttpURLConnection) url.openConnection();  
			 
			    ((HttpURLConnection) conn).setRequestMethod("POST");
			
		}else{
			 url = new URL("https://"+OLAUtility.getWalletIntBean(walletId+"").getTpIp()+reqType); 
			 logger.info("ola URL https:// "+url.getHost()+url.getPath());
				conn =  (HttpsURLConnection) url.openConnection();  
			 
		    ((HttpsURLConnection) conn).setRequestMethod("POST");
		}
		  logger.info("Req URL : "+url+"User Name: "+OLAUtility.getWalletIntBean(walletId+"").getTpUserName());
		conn.setDoInput(true);  
		conn.setDoOutput(true);  
		conn.setRequestProperty("serviceUserName",OLAUtility.getWalletIntBean(walletId+"").getTpUserName());
		conn.setRequestProperty("servicePassword", OLAUtility.getWalletIntBean(walletId+"").getTpPassword());
		OutputStreamWriter  wr = new OutputStreamWriter(conn.getOutputStream());
	    String param1 = "reqData="+reqeustData+"";
	    logger.info("reqData="+param1);
	    wr.write(param1);
	    wr.flush();
	    wr.close();
	return  conn.getInputStream(); 
	}catch(Exception e){
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		
	}
	
}	
/*private static TrustManager[ ] get_trust_mgr() {
    TrustManager[ ] certs = new TrustManager[ ] {
       new X509TrustManager() {
          public X509Certificate[ ] getAcceptedIssuers() { return null; }
     
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}
        }
     };
     return certs;
 }	*/
	
}
