package com.skilrock.camlot.coreEngine.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class CommonSMSHelper {
	private String mobileNo;
	private String msg;
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private DOMImplementation domImpl;
	private Document xml;
	
	public CommonSMSHelper(String mobileNo, String msg){
		this.mobileNo = mobileNo;
		this.msg = msg;
	}
	
	public String sendSMS(String userId, String passwd){
		String resp = "";
		try{
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
		domImpl = docBuilder.getDOMImplementation();
		
		Element push, userid, pwd, ctype, sender, multisms, detail, to, dlr, alert;
		Node n = null;
		// Document.
		xml = domImpl.createDocument(null, "push", null);
		// Root element.
		push = xml.getDocumentElement();
				
		userid = xml.createElementNS(null, "userid");
		n = xml.createTextNode(userId);
		userid.appendChild(n);
		
		pwd = xml.createElementNS(null, "pwd");
		n = xml.createTextNode(passwd);
		pwd.appendChild(n);
		
		ctype = xml.createElementNS(null, "ctype");
		n = xml.createTextNode("1");
		ctype.appendChild(n);
		
		sender = xml.createElementNS(null, "sender");
		n = xml.createTextNode("s");
		sender.appendChild(n);
		
		push.appendChild(userid);
		push.appendChild(pwd);
		push.appendChild(ctype);
		push.appendChild(sender);
		
		multisms = xml.createElementNS(null, "multisms");
			detail = xml.createElementNS(null,"detail");
			detail.setAttributeNS(null, "msgid", "1111");
			detail.setAttributeNS(null, "msgTxt", msg);
			detail.setAttributeNS(null, "siurl", "www.mnatives.com");	
				to = xml.createElementNS(null, "to");
				to.setAttributeNS(null, "id", "123");
				to.setAttributeNS(null, "pno", mobileNo);
			detail.appendChild(to);
		multisms.appendChild(detail);
		push.appendChild(multisms);
		
		dlr = xml.createElementNS(null, "dlr");
		n = xml.createTextNode("0");
		dlr.appendChild(n);
		push.appendChild(dlr);
		
		alert = xml.createElementNS(null, "alert");
		n = xml.createTextNode("0");
		alert.appendChild(n);
		push.appendChild(alert);
		
		
		TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "no");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(xml);
        trans.transform(source, result);
        String xmlString = sw.toString();

        //print xml
        System.out.println("Here's the xml:\n\n" + xmlString);
		
        
        //sending sms
		
		
        String address = "http://203.122.58.168/prepaidgetbroadcast/prepaidxmlapi";
			//String encodedAsdress = URLEncoder.encode(address, "UTF-8");
			 URL url = new URL(address);
						
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//URLConnection conn = url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(xmlString.toString());
			wr.flush();
			
			if(conn.getResponseCode() == 408 || conn.getResponseCode() == 404){
				return "#ERROR:No Response From Host";
			}
						
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			// Get the response				
			StringBuffer result2 = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null)
			{
				result2.append(line);
			}		
			in.close();
			System.out.println(result2);
			return result2.toString();

		
        
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return resp;
	}
	
	public static void main(String args[]){
		new CommonSMSHelper("919582850003", "Rs 20000000 has been credited to your HDFC Bank Account No 6354XXX232").sendSMS("demoint","demoint2010");
	}
}
