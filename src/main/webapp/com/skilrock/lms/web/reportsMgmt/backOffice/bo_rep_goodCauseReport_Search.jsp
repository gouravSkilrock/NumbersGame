<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<div align="center">
					<s:set name="startDate" value="startDate" />
					<s:set name="endDate" value="endDate" />
					<h3><u>Good Cause Report From : <s:property value="%{new com.skilrock.lms.web.drawGames.common.Util().changeFormat('yyyy-MM-dd', 'dd-MMM-yyyy', #startDate)}" /> to : <s:property value="%{new com.skilrock.lms.web.drawGames.common.Util().changeFormat('yyyy-MM-dd', 'dd-MMM-yyyy', #endDate)}" /></u></h3>
				</div>
				<div>
					<s:if test="%{reportList neq null and reportList.size>0}">
						<s:set name="saleTot" value="0.0" />
						<s:set name="winTot" value="0.0" />
						<table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center" valign="top" id="searchTable">
							<tr>
								<th>Service</th>
								<th>Game</th>
								<th>Tax Calculated on Sale</th>
								<th>Tax Calculated on Winning</th>
								<th align="center"><center>Total</center></th>
							</tr>
							<s:iterator value="reportList" status="taskIndex">
								<tr>
									<s:set name="saleAmt" value="%{saleUnapprovedAmount + saleApprovedAmount + saleDoneAmount}" />
									<s:set name="winAmt" value="%{winningUnapprovedAmount + winningApprovedAmount + winningDoneAmount}" />
									<s:set name="rightTot" value="%{saleUnapprovedAmount + saleApprovedAmount + saleDoneAmount + winningUnapprovedAmount + winningApprovedAmount + winningDoneAmount}" />

									<td><s:property value="serviceCode"/></td>
									<td><s:property value="gameName"/></td>
									<td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmt"))%></td>
									<td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winAmt"))%></td>
									<td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("rightTot"))%></td>

									<s:set name="saleTot" value="%{#saleTot + saleUnapprovedAmount + saleApprovedAmount + saleDoneAmount}" />
									<s:set name="winTot" value="%{#winTot + winningUnapprovedAmount + winningApprovedAmount + winningDoneAmount}" />
								</tr>
							</s:iterator>
							<tr>
								<td colspan="2">Total</td>
								<td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleTot"))%></td>
								<td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winTot"))%></td>
								<td>&nbsp;</td>
							</tr>
						</table>
					</s:if>
					<s:else>
						<center><h1>NO RECORDS TO PROCESS</h1></center>
					</s:else>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_goodCauseReport_Search.jsp,v $ $Revision: 1.3 $
</code>