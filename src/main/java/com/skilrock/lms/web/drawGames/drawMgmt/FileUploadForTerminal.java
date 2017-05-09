package com.skilrock.lms.web.drawGames.drawMgmt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.FileUploadForTerminalHelper;

public class FileUploadForTerminal extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String deviceType;
	private String profileName;
	private ArrayList<File> sgnFileUploader = new ArrayList<File>();
	private ArrayList<File> adfFileUploader = new ArrayList<File>();
	private ArrayList<File> agnFileUploader = new ArrayList<File>();

	private ArrayList<String> sgnFileUploaderFileName = new ArrayList<String>();
	private ArrayList<String> adfFileUploaderFileName = new ArrayList<String>();
	private ArrayList<String> agnFileUploaderFileName = new ArrayList<String>();

	private String isMandatory;
	private String status;
	private String version;
	private String itemNames;

	public String fetchProfileNames() {
		HttpSession session = request.getSession();
		FileUploadForTerminalHelper helper = new FileUploadForTerminalHelper();
		session.setAttribute("LIST_OF_DEVICES", helper.getDeviceTypeList());
		session.setAttribute("LIST_OF_PROFILES", helper.getProFileNameList());
		session.setAttribute("LIST_OF_ITEMS", helper.getItemNames());

		return SUCCESS;
	}

	public void uploadNewFilesForTerminal() throws IOException, SQLException {

		String errorMessage = "Uploaded Successfully...";
		
		HttpSession session=request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String outFilePath=(String) ServletActionContext
		.getServletContext().getAttribute("OUT_FILE_PATH_FOR_TERMINAL");
		System.out.println(userBean.getUserId());
		FileUploadForTerminalHelper helper = new FileUploadForTerminalHelper();

		System.out.println(sgnFileUploaderFileName + "  "
				+ adfFileUploaderFileName + "  " + agnFileUploaderFileName);
		if (sgnFileUploaderFileName == null && adfFileUploaderFileName == null
				&& agnFileUploaderFileName == null) {
			errorMessage = "Please Select The File...";
		} else {
			errorMessage = helper.uploadFile(deviceType, profileName,
					itemNames, sgnFileUploader, adfFileUploader,
					agnFileUploader, sgnFileUploaderFileName,
					adfFileUploaderFileName, agnFileUploaderFileName, version,
					userBean.getUserId(), isMandatory, status,outFilePath);
		}

		PrintWriter out = response.getWriter();
		out.write(errorMessage);
	}

	public void getTerminalVersion() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			String message = null;
			FileUploadForTerminalHelper helper = new FileUploadForTerminalHelper();
			message = helper.getTerminalVersion(deviceType, profileName);
			pw.print(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getItemNames() {
		return itemNames;
	}

	public void setItemNames(String itemNames) {
		this.itemNames = itemNames;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
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

	public ArrayList<File> getSgnFileUploader() {
		return sgnFileUploader;
	}

	public void setSgnFileUploader(ArrayList<File> sgnFileUploader) {
		this.sgnFileUploader = sgnFileUploader;
	}

	public ArrayList<File> getAdfFileUploader() {
		return adfFileUploader;
	}

	public void setAdfFileUploader(ArrayList<File> adfFileUploader) {
		this.adfFileUploader = adfFileUploader;
	}

	public ArrayList<String> getSgnFileUploaderFileName() {
		return sgnFileUploaderFileName;
	}

	public void setSgnFileUploaderFileName(
			ArrayList<String> sgnFileUploaderFileName) {
		this.sgnFileUploaderFileName = sgnFileUploaderFileName;
	}

	public ArrayList<String> getAdfFileUploaderFileName() {
		return adfFileUploaderFileName;
	}

	public void setAdfFileUploaderFileName(
			ArrayList<String> adfFileUploaderFileName) {
		this.adfFileUploaderFileName = adfFileUploaderFileName;
	}

	public ArrayList<String> getAgnFileUploaderFileName() {
		return agnFileUploaderFileName;
	}

	public void setAgnFileUploaderFileName(
			ArrayList<String> agnFileUploaderFileName) {
		this.agnFileUploaderFileName = agnFileUploaderFileName;
	}

	public String getVersion() {
		return version;
	}

	public ArrayList<File> getAgnFileUploader() {
		return agnFileUploader;
	}

	public void setAgnFileUploader(ArrayList<File> agnFileUploader) {
		this.agnFileUploader = agnFileUploader;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

}
