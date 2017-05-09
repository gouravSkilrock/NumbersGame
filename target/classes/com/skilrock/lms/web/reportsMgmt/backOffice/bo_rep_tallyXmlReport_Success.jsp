<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="com.skilrock.lms.common.Utility"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<script>var projectName="<%=request.getContextPath()%>" </script>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
		<div id="top_form">
			<s:if test="%{message neq null and message.length()>0}">
				<center><font style="color:red;"><s:property value="message"/></font></center>
			</s:if>
			<s:else>
				<table>
					<s:iterator value="files">
						<tr>
							<td colspan="2">
								<a href='bo_rep_tallyDataXml_exportAsXML.action?fileName=<s:property/>.xml'><s:property/></a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_tallyXmlReport_Success.jsp,v $ $Revision:
	1.1.4.2 $
</code>