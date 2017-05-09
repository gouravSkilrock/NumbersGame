<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Selected ICS Queries Run Successfully.
				</h3>

				<table width="100%" border="1" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC" align="center" valign="top" id="searchTable">
					<tr>
						<th>S. No.</th>
						<th>Query ID</th>
						<th>Query Title</th>
						<th>Expected Result</th>
						<th>Actual Result</th>
						<th>Query Execution Time (ms)</th>
						<th>Is Success</th>
					</tr>
					<s:iterator value="%{dailyQueryStatusList}" status="taskIndex">
						<tr>
							<td><s:property value="#taskIndex.index+1"/></td>
							<td><s:property value="queryId"/></td>
							<td><s:property value="queryTitle"/></td>
							<td><s:property value="expectedResult"/></td>
							<td><s:property value="actualResult"/></td>
							<td><s:property value="queryExecutionTime"/></td>
							<td>
								<s:if test="%{isSuccess eq 'YES'}">
									<span style="color:green;">YES</span>
								</s:if>
								<s:else>
									<span style="color:red;">NO</span>
								</s:else>
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_ics_runICSQueries_Success.jsp,v $ $Revision: 1.1.2.2 $
</code>