<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<s:head theme="ajax"/>
</head>

<body>
  
  
	<div align="center"><h3><u>Cash & Cheque <s:text name="Report"/> &nbsp;
	<%
		if ("".equals(((DateBeans) session.getAttribute("datebean"))
				.getReportType())) {
	%>
  		 From : 
  		<%=GetDate.getConvertedDate(((DateBeans) session
								.getAttribute("datebean")).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans) session
								.getAttribute("datebean")).getEndDate())%>
  		
	<%
  				} else {
  			%>
	<%="Of : "
						+ ((DateBeans) session.getAttribute("datebean"))
								.getReportType()%>
	<%
		}
	%>
	</u></h3></div>
	
	<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr><th align="center" colspan="5">Cash & Cheque Deposit To <s:property value="#application.TIER_MAP.AGENT" /></th></tr>
		
		<tr>			
			<th align="center">Cash Deposit</th>
			<th align="center">Cheque Deposit</th>
			<th align="center">Cheque Bounce</th>
			<th align="center">Debit Note</th>
			<th align="center">Credit Note</th>
			<th align="center">Net Deposit</th>						
		</tr>
		
		<s:if test="#session.retCashChqDet!=null">
		<tr class="startSortable">
					<td align="right">
						<s:set name="strAgtTotalCash"
							value="#session.retCashChqDet.totalCash" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtTotalCash"))%></td>
					<td align="right">
						<s:set name="strAgtTotalChq"
							value="#session.retCashChqDet.totalChq" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtTotalChq"))%></td>
					<td align="right">
						<s:set name="strAgtTotalChqBounce"
							value="#session.retCashChqDet.cheqBounce" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtTotalChqBounce"))%></td>
					<td align="right">
						<s:set name="strAgtTotalDebit"
							value="#session.retCashChqDet.debit" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtTotalDebit"))%></td>
					<td align="right">
						<s:set name="strAgtTotalCredit"
							value="#session.retCashChqDet.credit" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtTotalCredit"))%></td>
					<td align="right">
						<s:set name="strAgtTotalNetAmt"
							value="#session.retCashChqDet.netAmt" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtTotalNetAmt"))%></td>
				</tr>	
		</s:if>
		
		<s:else>
			<tr>
				<td colspan="7" align="center"> No Transaction with <s:property value="#application.TIER_MAP.AGENT" /></td>
			</tr>
		</s:else>	
					
		</table>
		
	<br/>
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_cashChq_SearchResult.jsp,v $
$Revision: 1.3 $
</code>