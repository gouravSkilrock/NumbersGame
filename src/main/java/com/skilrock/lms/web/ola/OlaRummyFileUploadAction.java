package com.skilrock.lms.web.ola;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaExPinBean;
import com.skilrock.lms.beans.OlaExPinSummaryBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.NetGamingForRummyHelper;

public class OlaRummyFileUploadAction extends ActionSupport implements ServletResponseAware,ServletRequestAware{
/**
	 * 
	 */
private static final long serialVersionUID = 1L;
private File fileUpload	;
private String fileUploadFileName;
private HttpServletRequest request;
private HttpServletResponse response;
private File[] filesUpload;// Files For Deposit and wagering
private String[] filesUploadFileName;//Files name For Deposit and wagering
private String fromDate; 
private String netGamingDistributionModel;
private HashMap<String, OlaExPinSummaryBean> pinSummaryBeanMap;
public String uploadFile(){
	OlaRummyFileUploadHelper helper = new OlaRummyFileUploadHelper();	
	String uploadStatus =helper.readExcel(fileUpload,fromDate,fromDate);;
	if(uploadStatus.equalsIgnoreCase("true")){
		return SUCCESS;
	}
	else{
		addActionMessage(uploadStatus);
		return ERROR;
	}

	
}


public String olaUploadDepositWageringAction(){
	ServletContext sc=ServletActionContext.getServletContext();
	 netGamingDistributionModel=(String) sc.getAttribute("OLA_NETGAMING_DISTRIBUTION_MODEL");
	return SUCCESS;
}

public String getNetGamingDistributionModel() {
	return netGamingDistributionModel;
}

public void setNetGamingDistributionModel(String netGamingDistributionModel) {
	this.netGamingDistributionModel = netGamingDistributionModel;
}

/**
 * Method to Upload Deposit and wagering Files
 * 
 * @return
 * @throws ParseException 
 */
public String uploadDepositWagering(){
	System.out.println(fromDate);
	ServletContext sc=ServletActionContext.getServletContext();
	String olaNetGamingDistributionModel=(String) sc.getAttribute("OLA_NETGAMING_DISTRIBUTION_MODEL");
	System.out.println("Net Gaming Model:"+olaNetGamingDistributionModel);
	NetGamingForRummyHelper helper=new NetGamingForRummyHelper();
	try {
	if("DEPOSITRATIO".equalsIgnoreCase(olaNetGamingDistributionModel)){
		
			helper.insertDepositWageredWinningData(filesUpload, fromDate,olaNetGamingDistributionModel);
		
	}else if("CUMMULATIVE".equalsIgnoreCase(olaNetGamingDistributionModel)){
		
		helper.insertDepositWageredData(filesUpload,fromDate,olaNetGamingDistributionModel);
	}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		addActionError(e.getMessage());
		return ERROR;
	} catch (LMSException e) {
		addActionError(e.getMessage());
		e.getMessage();
		return ERROR;
	}

	return SUCCESS;
}

public String expiredPinFileUpload(){

	OlaRummyFileUploadHelper helper = new OlaRummyFileUploadHelper();
	ArrayList<OlaExPinBean> olaExPinBeanList = new ArrayList<OlaExPinBean>();
	ArrayList<Object> list =helper.readTxtFile(fileUpload);
	pinSummaryBeanMap =(HashMap<String, OlaExPinSummaryBean>)list.get(0);
	olaExPinBeanList=(ArrayList<OlaExPinBean>)list.get(1);
		if(pinSummaryBeanMap.size()>0){
			HttpSession session = getRequest().getSession();
			session.setAttribute("exPinList",olaExPinBeanList);
			return SUCCESS;
		}
		else{
			addActionMessage("Some Error In File Upload or File Is Empty");
			return ERROR;
		}

		
		
	}

public String expirePins(){
		HttpSession session = getRequest().getSession();
		ArrayList<OlaExPinBean> olaExPinBeanList = (ArrayList<OlaExPinBean>) session.getAttribute("exPinList");
		ServletContext sc = ServletActionContext.getServletContext();
		String desKey = (String) sc.getAttribute("OLA_PIN_DES_KEY");
		String propKey=	(String) sc.getAttribute("OLA_PIN_AES_KEY");
		OlaRummyFileUploadHelper helper = new OlaRummyFileUploadHelper();	
		boolean isSuccess =helper.expirePins(olaExPinBeanList,desKey,propKey);
		if(isSuccess){
			return SUCCESS;
		}
		else{
			addActionMessage("Some Error In File Upload");
			return ERROR;
		}

		
		
	}	

public File getFileUpload() {
	return fileUpload;
}
public void setFileUpload(File fileUpload) {
	this.fileUpload = fileUpload;
}
public String getFileUploadFileName() {
	return fileUploadFileName;
}

public void setFileUploadFileName(String fileUploadFileName) {
	this.fileUploadFileName = fileUploadFileName;
}

public void setServletResponse(HttpServletResponse response) {
	this.response =response;
	}
public void setServletRequest(HttpServletRequest request) {
	this.request=request;
		
	}
public HttpServletRequest getRequest() {
		return request;
	}
public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
public HttpServletResponse getResponse() {
		return response;
	}
public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

public File[] getFilesUpload() {
	return filesUpload;
}

public void setFilesUpload(File[] filesUpload) {
	this.filesUpload = filesUpload;
}


public String getFromDate() {
	return fromDate;
}
public void setFromDate(String fromDate) {
	this.fromDate = fromDate;
}
public String[] getFilesUploadFileName() {
	return filesUploadFileName;
}
public void setFilesUploadFileName(String[] filesUploadFileName) {
	this.filesUploadFileName = filesUploadFileName;
}
public HashMap<String, OlaExPinSummaryBean> getPinSummaryBeanMap() {
	return pinSummaryBeanMap;
}
public void setPinSummaryBeanMap(
		HashMap<String, OlaExPinSummaryBean> pinSummaryBeanMap) {
	this.pinSummaryBeanMap = pinSummaryBeanMap;
}



}
