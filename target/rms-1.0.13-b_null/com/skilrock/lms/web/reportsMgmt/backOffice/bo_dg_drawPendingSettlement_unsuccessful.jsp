<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.dge.beans.ReportBeanDrawModule"%>
<%@page import="com.skilrock.lms.dge.beans.ReportDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.ReportGameBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires",0);
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
				<s:if test="%{drawPendingSettlementList.size>0}">
					<s:actionerror />
					<s:form action="bo_rep_drawPendingSettlement_ExpToExcel"  method="post" enctype="multipart/form-data">
						<s:hidden id="tableValueMain" name="tableValue" />
						<s:submit value="Export As Excel" onclick="getData('tableDataMain', 'tableValueMain');"/>
					</s:form>
					<s:set name="delayTicketSum" value="0.0" />
					<s:set name="delayAmountSum" value="0.0" />
					<s:set name="unNotifiedTicketSum" value="0.0" />
					<s:set name="unNotifiedAmountSum" value="0.0" />
					<s:set name="serverTicketSum" value="0.0" />
					<s:set name="serverAmountSum" value="0.0" />
					<s:set name="totalTicketSum" value="0.0" />
					<s:set name="totalAmountSum" value="0.0" />
					<div id="tableDataMain">
						<s:hidden id="reportName" value="Draw Pending Settlement Report - Unsuccessful Tickets"/>
						<s:hidden id="isDateDisplay" value="YES"/>
						<table width="100%" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr>
								<th align="center" rowspan="2"><s:text name="AGENT" /> Name</th>
								<th align="center" colspan="2">Expired Tickets</th>
								<th align="center" colspan="2">UnNotified Tickets</th>
								<th align="center" colspan="2">Non Refunded</th>
								<th align="center" rowspan="2">Total Tickets</th>
								<th align="center" rowspan="2">Total Amount</th>
							</tr>
							<tr>
								<th>No of Expired Ticket</th>
								<th>Amount Expired Ticket</th>
								<th>No of UnNotified Ticket</th>
								<th>Amount UnNotified Ticket</th>
								<th>No of Non Refunded Ticket</th>
								<th>Amount Non Refunded Ticket</th>
							</tr>
							
								<s:iterator value="%{drawPendingSettlementList}">
									<tr class="startSortable">
										<td>
											<s:url action="bo_rep_drawPendingSettlementData" id="retTicketProcess" >
												<s:param name="agentOrgId"><s:property value="agentOrgId" /></s:param>
												<s:param name="reportType">retUnsuccessfulData</s:param>
												<s:param name="startDate"><s:property value="startDate" /></s:param>
												<s:param name="endDate"><s:property value="endDate" /></s:param>
											</s:url>
											<s:a href="%{retTicketProcess}" theme="ajax" targets="retWiseResult" onclick="goToLast();"><s:property value="agentOrgName" /></s:a>
										</td>
										<td>
											<s:set name="delayTicket" value="%{delayExpTicket}" />
											<s:property value="delayExpTicket" />
										</td>
										<td align="right">
											<s:set name="delayAmount" value="%{delayExpAmount}" />
											<s:property value="delayExpAmount" />
										</td>
										<td>
											<s:set name="unNotifiedTicket" value="%{unNotifyExpTicket}" />
											<s:property value="unNotifyExpTicket" />
										</td>
										<td align="right">
											<s:set name="unNotifiedAmount" value="%{unNotifyExpAmount}" />
											<s:property value="unNotifyExpAmount" />
										</td>
										<td>
											<s:set name="serverTicket" value="%{serverErrorExpTicket}" />
											<s:property value="serverErrorExpTicket" />
										</td>
										<td align="right">
											<s:set name="serverAmount" value="%{serverErrorExpAmount}" />
											<s:property value="serverErrorExpAmount" />
										</td>
										<td>
											<s:set name="totalTicket" value="%{totalExpTicket}" />
											<s:property value="totalExpTicket" />
										</td>
										<td align="right">
											<s:set name="totalAmount" value="%{totalExpAmount}" />
											<s:property value="totalExpAmount" />
										</td>
										<s:set name="delayTicketSum" value="%{#delayTicketSum+#delayTicket}" />
										<s:set name="delayAmountSum" value="%{#delayAmountSum+#delayAmount}" />
										<s:set name="unNotifiedTicketSum" value="%{#unNotifiedTicketSum+#unNotifiedTicket}" />
										<s:set name="unNotifiedAmountSum" value="%{#unNotifiedAmountSum+#unNotifiedAmount}" />
										<s:set name="serverTicketSum" value="%{#serverTicketSum+#serverTicket}" />
										<s:set name="serverAmountSum" value="%{#serverAmountSum+#serverAmount}" />
										<s:set name="totalTicketSum" value="%{#totalTicketSum+#totalTicket}" />
										<s:set name="totalAmountSum" value="%{#totalAmountSum+#totalAmount}" />
									</tr>
								</s:iterator>
								<tr>
									<th align="left">Total</th>
									<th align="left"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("delayTicketSum"))%></th>
									<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("delayAmountSum"))%></th>
									<th align="left"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("unNotifiedTicketSum"))%></th>
									<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("unNotifiedAmountSum"))%></th>
									<th align="left"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("serverTicketSum"))%></th>
									<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("serverAmountSum"))%></th>
									<th align="left"><%=FormatNumber.formatNumberForJSPWithoutZero(pageContext.getAttribute("totalTicketSum"))%></th>
									<th align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalAmountSum"))%></th>
								</tr>
						</table>
					</div>
				</s:if>
				<s:else>
					<center>No Result Found</center>
				</s:else>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_drawPendingSettlement_unsuccessful.jsp,v $ $Revision:
	1.3.6.3.2.17.4.1 $
</code>