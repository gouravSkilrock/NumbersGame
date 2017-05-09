<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.skilrock.lms.beans.FileUploadBean"%>
<%@page import="java.util.Iterator"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<s:head theme="ajax" debug="false" />

	</head>
	<body>

		<h3>
			Offline File Upload Details
		</h3>
		<%
			Map<String, List<FileUploadBean>> retFileMap = (Map<String, List<FileUploadBean>>) session
					.getAttribute("retFileMap");
			Iterator<Map.Entry<String, List<FileUploadBean>>> iter = retFileMap
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, List<FileUploadBean>> pair = (Map.Entry<String, List<FileUploadBean>>) iter
						.next();
				String key = pair.getKey();
				List<FileUploadBean> fileList = pair.getValue();
				String gameArr[] = key.split("-");
		%>
		<table style="border: 1px solid;" cellpadding="2" cellspacing="0"
			width="450">
			<tr>
				<th>
					Game No
				</th>
				<td><%=gameArr[0]%></td>
				<th>
					Game Name
				</th>
				<td><%=gameArr[1]%></td>
			</tr>
			<tr>
				<td colspan="4">
					<table style="border: 1px solid;" width="100%">
						<tr>
							<th>
								File Name
							</th>
							<th>
								Upload Time
							</th>
							<th>
								Status
							</th>
						</tr>
						<%
							if (fileList.size() != 0) {
									for (int i = 0; i < fileList.size(); i++) {
										FileUploadBean fileBean = fileList.get(i);
						%>
						<tr>
							<td><%=fileBean.getFileName()%></td>
							<td><%=fileBean.getUploadTime()%></td>
							<td><%=fileBean.getStatus()%></td>
							<td>
							<div id="<%=fileBean.getRefNo()%>">
								<a href="#" onclick="mergeFile('<%=fileBean.getRefNo()%>','<%=fileBean.getStatus()%>',<%=fileBean.getRetailerUserId()%>,<%=fileBean.getGameNo()%>)">APPROVE</a>
								<a href="#" onclick="declineFile('<%=fileBean.getRefNo()%>',<%=fileBean.getGameNo()%>)">DECLINE</a>
							</div>
							</td>
						</tr>
						<%
							}
								} else {
						%>
						<tr>
							<td colspan="4">
								No file to Process for this Game
							</td>
						</tr>
						<%
							}
						%>
					</table>
				</td>
			</tr>
		</table>

		<%
			}
		%>

	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_dm_searchOfflineRetUser_FileUpload.jsp,v $
$Revision: 1.2 $
</code>