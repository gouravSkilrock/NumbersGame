package com.skilrock.lms.web.reportsMgmt.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.common.TallyXmlReportHelper;

public class TallyXmlReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public TallyXmlReportAction() {
		super(TallyXmlReportAction.class);
	}

	private String startDate;
	private String endDate;
	private String fileType;
	private int gameId;
	private String[] files;
	private String message;
	private String fileName;

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String[] getFiles() {
		return files;
	}

	public void setFiles(String[] files) {
		this.files = files;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String reportMenu() {
		try {
			TallyXmlReportHelper helper = new TallyXmlReportHelper();
			if(!"SALE_CONSOLIDATED".equals(fileType)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date archivingDate = dateFormat.parse(CommonMethods.getLastArchDate());
				Calendar archCal = Calendar.getInstance();
				archCal.setTime(archivingDate);
				archCal.add(Calendar.DATE, 1);
				Date stDate = dateFormat.parse(startDate);
				if(stDate.before(archCal.getTime())) {
					message = "Select Date After Archiving Date.";
					return SUCCESS;
				}
			}
			files = helper.fetchFiles(startDate, endDate, fileType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void exportAsXML() throws Exception {
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment;filename="+fileName);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/home/stpl/upload/"+fileName), "UTF-8"));
		String line = bufferedReader.readLine();
		String resp = "";
		while (line != null) {
			resp += line + "\n";
			line = bufferedReader.readLine();
		}
		logger.info(resp.toString());

		out.print(resp);
		out.close();
	}
}