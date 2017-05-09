<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.dge.beans.ReportBeanDrawModule"%>
<%@page import="com.skilrock.lms.dge.beans.ReportDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.ReportGameBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);

	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<s:actionerror />
				<s:form action="bo_rep_drawPendingSettlement_ExpToExcel"  method="post" enctype="multipart/form-data">
					<s:hidden id="tableValueSub" name="tableValue" />
					<s:submit value="Export As Excel" onclick="getData('tableDataSub', 'tableValueSub');"/>
				</s:form>
				<div id="tableDataSub">
					<s:hidden id="subReportName" value="Draw Pending Settlement Report - Retailer Wise Unsuccessful Tickets"/>
					<s:hidden id="isDateDisplay" value="YES"/>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th align="center"><s:text name="Retailer" /> Name</th>
							<th align="center">Ticket Number</th>
							<th align="center">Mrp Amount</th>
							<th align="center">Cancel Reason</th>
						</tr>
						<s:if test="%{drawPendingSettlementList.size>0}">
							<s:iterator value="%{drawPendingSettlementList}">
								<tr>
									<td align="center">
										<s:property value="retailerOrgName" />
									</td>
									<td><s:property value="ticketNumber" /></td>
									<td align="right"><s:property value="processingAmount" /></td>
									<td><s:property value="cancelReason" /></td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="7" align="center">
									No Result Found
								</td>
							</tr>
						</s:else>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_drawPendingSettlement_retUnsuccessfulDate.jsp,v $ $Revision:
	1.3.6.3.2.17.4.1 $
</code>