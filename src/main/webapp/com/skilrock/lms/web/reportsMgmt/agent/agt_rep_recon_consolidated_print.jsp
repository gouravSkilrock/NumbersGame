<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.skilrock.lms.beans.ReconReportBean"%>

<%@page import="com.skilrock.lms.beans.ReportBean"%>`
<%@page import="com.skilrock.lms.beans.SalePurchaseRetBean"%>
<%@page import="com.skilrock.lms.beans.PWTPaymentsBean"%>
<%@page import="com.skilrock.lms.beans.CashChqPmntBean"%>
<%@page import="com.skilrock.lms.beans.StockMrpDiscountBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.beans.SalePurchaseBean"%>
<%@page import="com.skilrock.lms.beans.ConsolidatedBean"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
			
<style type="text/css">

body {
	font-family: Verdana, Tahoma, Helvetica, sans-serif;
	font-size: 0.7em;
	color: #666666; 
}
table,td,th { font-size: 11px;}
</style>			
				<script type="text/javascript">
			
			function printpage(){									
					window.print();		
					window.close();	
			}
			</script>
</head>
<body onload="printpage()">
<div id="wrap">
     <div id="top_form">
		<%
			System.out.println("**Consolidated *******8");
		
			List reconcilationList = (List)session.getAttribute("RECON_LIST");
			System.out.println("*******************22222222222 "+reconcilationList.size());
			
		
		
		 %>
		 <table width="800" border="0" cellspacing="0" cellpadding="0" align="center"><tr><td>
	 <table width="800" border="1" cellspacing="0" cellpadding="4" align="center" bordercolor="#f8f8f8"><tr><td>

		<table width="100%" border="0" cellspacing="0" cellpadding="4" bgcolor="#f7f7f7">
		  <tr>
			<td width="40%" class="bdr_btm"><b>Reconciliation <s:text name="Report"/></b></td>
			<td width="20%" align="right" class="bdr_btm">From: </td>
			<td width="10%" class="bdr_btm" bgcolor="#eeeeee"><%=reconcilationList.get(reconcilationList.size()-2)%></td>
			<td width="20%" align="right" class="bdr_btm">To: </td>
			<td width="10%" class="bdr_btm" bgcolor="#eeeeee"><%=reconcilationList.get(reconcilationList.size()-1)%></td>
		  </tr>
		</table><br />
		 	 		
		<table width="960" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF" #session.usertype>
  		<tr><td align="center" colspan="4"><h4 style="padding-top: 11px"> Reconciliation <s:text name="Report"/> for <s:property value="#application.TIER_MAP.AGENT" /> : <s:property value="#session.USER_INFO.getOrgName()"/></h4></td></tr>
       <table  border="1" cellpadding="4" cellspacing="0" bordercolor="#eeeeee" align="center">
       <tr><td rowspan="2"><b><s:property value="#application.TIER_MAP.RETAILER" /> Name</b> </td><td align="center" colspan="2" bgcolor="#FFF0E1"><b>Ledger Details</b></td>
       <td align="center" colspan="7" bgcolor="#FFF0E1"><b>Reconciliation Details</b></td></tr>
        <tr>
       
        <th align="center">Opening Ledger Balance</th>
        <th align="center">Closing Ledger Balance</th>
        <th align="center">Reconciliation Opening Balance</th>
        <th align="center">Payable Against Net Sales</th>
        <th align="center">PWT</th>
        <th align="center">Payments</th>
     	<th align="center">Reconciliation Closing Balance</th>
     	<th align="center">Purchase Price Of Available Stock</th>         </tr>
        <%    
        	   boolean empty=true;
		 		for (int i=0;i<((List)session.getAttribute("RECON_LIST")).size()-2;i++) {
	 		ConsolidatedBean consolidatedBean=(ConsolidatedBean)((List)session.getAttribute("RECON_LIST")).get(i);
	 		System.out.println(consolidatedBean.getUserName());
	 		System.out.println(consolidatedBean.getOpeningLedBal());
	 		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
	 		SimpleDateFormat dateFormat2= new SimpleDateFormat("dd-MM-yyyy");
	 		empty=false;
	 		
			 
			 %>
				<tr>
				<td class="data" style="width: 30px">
				<%=consolidatedBean.getUserName()%> 
				</td>
				<td class="data" bgcolor="#f1f1f1" align="right"><%=FormatNumber.formatNumberForJSP(consolidatedBean.getOpeningLedBal())%></td>
                <td class="data" bgcolor="#f1f1f1" align="right"><%=FormatNumber.formatNumberForJSP(consolidatedBean.getClosingLedBal())%></td>
                <td class="data" align="right" bgcolor="#f8f8f8"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getWeeklyOpeningBal()))%></td>
                <td class="data" align="right" bgcolor="#f8f8f8"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getNetPayable()))%></td>
                <td class="data" align="right" bgcolor="#f8f8f8"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getNetPWT()))%></td>
                <td class="data" align="right" bgcolor="#f8f8f8"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getNetPayments()))%></td>
                <td class="data" align="right" bgcolor="#f8f8f8"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getWeeklyClosingBal()))%></td>
                <td class="data" align="right" bgcolor="#f8f8f8"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getPurchPrcRemStk()))%></td>
                </tr>
			 <%}
			 		%>
		 
		 	<%
		 		if((reconcilationList.isEmpty())||(empty==true)) {
				%>
        
       <tr><td class="label"><%="NA"%></td>
          <td class="data"><%=0.0%></td>
          <td class="data"><%=0.0%></td>
          <td class="data"><%=0.0%></td>
          <td class="data"><%=0.0%></td>
          <td class="data"><%=0.0%></td>
          <td class="data"><%=0.0%></td>
           <td class="data"><%=0.0%></td></tr>
			 <%
			 }%>
			 </table>
           </table><br />
					 
		 	</td>
		 </tr>
		 </table><br />
		 
		 
		 

		 </td></tr>		 	
		 </table>
		 
		 
	</div></div>
	
		
	<div id="detailrep"></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_recon_consolidated_print.jsp,v $
$Revision: 1.3 $
</code>