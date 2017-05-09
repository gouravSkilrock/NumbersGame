<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.dge.beans.ReportBeanDrawModule"%>
<%@page import="com.skilrock.lms.dge.beans.ReportDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.ReportGameBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);
%>
<%
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
					<s:hidden id="tableValueMain" name="tableValue" />
					<s:submit value="Export As Excel" onclick="getData('tableDataMain', 'tableValueMain');"/>
				</s:form>

				<s:set name="processTicketSum" value="0.0" />
				<s:set name="processAmountSum" value="0.0" />
				<div id="tableDataMain">
					<s:hidden id="reportName" value="Draw Pending Settlement Report - Processing Tickets"/>
					<s:hidden id="isDateDisplay" value="NO"/>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th align="center"><s:text name="AGENT" /> Name</th>
							<th align="center">Processing Tickets</th>
							<th align="center">Amount</th>
						</tr>
						<s:if test="%{drawPendingSettlementList.size>0}">
							<s:iterator value="%{drawPendingSettlementList}">
								<tr class="startSortable">
									<td align="left">
										<s:url action="bo_rep_drawPendingSettlementData" id="retTicketProcess" >
											<s:param name="agentOrgId"><s:property value="agentOrgId" /></s:param>
											<s:param name="reportType">retTicketProcess</s:param>
										</s:url>
										<s:a href="%{retTicketProcess}" cssClass="link" theme="ajax" targets="retWiseResult" onclick="goToLast();"><s:property value="agentOrgName" /></s:a>
									</td>
									<td align="left">
										<s:set name="processTickets" value="%{processingTickets}" />
										<s:property value="processingTickets" />
									</td>
									<td align="right" class="add">
										<s:set name="processAmount" value="%{processingAmount}" />
										<s:property value="processingAmount" />
									</td>
									<s:set name="processTicketSum" value="%{#processTicketSum+#processTickets}" />
									<s:set name="processAmountSum" value="%{#processAmountSum+#processAmount}" />
								</tr>
							</s:iterator>
							<tr>
								<th align="left">Total</th>
								<th align="left"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("processTicketSum"))%></th>
								<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("processAmountSum"))%></th>
							</tr>
						</s:if>
						<s:else>
							<tr>
								<td colspan="3" align="center">
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
	$RCSfile: bo_dg_drawPendingSettlement_processing.jsp,v $ $Revision:
	1.3.6.3.2.17.4.1 $
</code>