<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.skilrock.lms.beans.ReconReportBean"%>

<%@page import="com.skilrock.lms.beans.ReportBean"%>
<%@page import="com.skilrock.lms.beans.SalePurchaseRetBean"%>
<%@page import="com.skilrock.lms.beans.PWTPaymentsBean"%>
<%@page import="com.skilrock.lms.beans.CashChqPmntBean"%>
<%@page import="com.skilrock.lms.beans.StockMrpDiscountBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.beans.SalePurchaseBean"%>
<%@page import="com.skilrock.lms.beans.ConsolidatedBean"%>
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
			
</head>
<body>
<div id="wrap">
     <div id="top_form">
		<%
		System.out.println("*******************8");
		
			List reconcilationList = (List)session.getAttribute("RECON_LIST");
			List reconAftrPmntList = (List)session.getAttribute("RECON_AFTRLST_PMNT");
			System.out.println("*******************22222222222 "+reconcilationList.size());
			ReportBean reportBean=(ReportBean)reconcilationList.get(0);
			List<SalePurchaseBean> saleList=(List<SalePurchaseBean>)reportBean.getSaleList();
			List<PWTPaymentsBean> pwtList=reportBean.getPwtList();
			List<CashChqPmntBean> paymentList=reportBean.getPaymentList();
			Map<String, StockMrpDiscountBean> stockMrpDiscMap=reportBean.getStockMrpDiscMap();
			Double totalSale = 0.0;
			Double totalPurch = 0.0;
			Double totalMrp = 0.0;
			Double totalProfit = 0.0;
			Double totalPwtAmt = 0.0;
			Double totalPwtComm = 0.0;
			Double totalNetPwt = 0.0;
			Double totalPayments = 0.0;
			int totalBooksStk = 0;
			Double totalMrpStk = 0.0;
			Double totalDiscPriceStk = 0.0;
		
		 %>
		 <table width="800" border="0" cellspacing="0" cellpadding="0" align="center"><tr><td>
	 <table width="800" border="1" cellspacing="0" cellpadding="4" align="center" bordercolor="#B1B1B1"><tr><td>

		<table width="100%" border="0" cellspacing="0" cellpadding="4" bgcolor="#f7f7f7">
		  <tr>
			<td width="40%" class="bdr_btm"><b>Reconciliation <s:text name="Report"/></b></td>
			<td width="20%" align="right" class="bdr_btm">From: </td>
			<td width="10%" class="bdr_btm" bgcolor="#eeeeee"><%=reconcilationList.get(4)%></td>
			<td width="20%" align="right" class="bdr_btm">To: </td>
			<td width="10%" class="bdr_btm" bgcolor="#eeeeee"><%=reconcilationList.get(5)%></td>
		  </tr>
		</table><br />
		 <table width="100%" border="1" cellspacing="0" cellpadding="6" bgcolor="#F2F2FF" bordercolor="#eeeeee">
  			<tr>
   		<td width="25%" bgcolor="#FFE8FF" align="right"><b>Opening Ledger Balance</b></td>
		<td width="25%" bgcolor="#FFE8FF" class="data"><%=FormatNumber.formatNumberForJSP(reconcilationList.get(2))%></td>
		<td width="25%" align="right"><b>Reconciliation Opening Balance: </b></td>
		<td width="25%" class="data"><%=FormatNumber.formatNumberForJSP(session.getAttribute("PREVIOUS_BAL"))%></td>
		  	</tr>
		  </table><br />
		 		
		<table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF">
  		<tr><td align="center" colspan="4"><h4>RECONCILIATION <s:text name="Report"/> For <s:property value="retailerName"/> </h4></td></tr>
        <tr><td align="center" colspan="4" bgcolor="#FFF0E1"><b>SALE AND PURCHASE DETAILS</b></td></tr>
        <tr>
        <th align="left">Game Name</th>
        <th align="left">Net Sale to Player</th>
        <th align="left"><s:property value="#application.TIER_MAP.RETAILER" />'s Net Profit</th>
        <th align="left">Payable to <s:property value="#application.TIER_MAP.AGENT" /></th>        </tr>
        <%
		 		Iterator saleItr = saleList.listIterator();
		 		while (saleItr.hasNext()) {
				SalePurchaseBean salePurchaseBean=(SalePurchaseBean)saleItr.next();
				totalMrp= totalMrp+	salePurchaseBean.getMrp();
				totalSale=totalSale+salePurchaseBean.getSalePrice();
				totalPurch=	totalPurch+	salePurchaseBean.getPurchprice();
				totalProfit=totalProfit+salePurchaseBean.getComm();	 
			 %>
				<tr><td class="label"><%=salePurchaseBean.getGameName()%></td>
                <td class="data"><%=FormatNumber.formatNumberForJSP(salePurchaseBean.getMrp())%></td>
                <td class="data"><%=FormatNumber.formatNumberForJSP(salePurchaseBean.getComm())%></td>
                <td class="data"><%=FormatNumber.formatNumberForJSP((salePurchaseBean.getPurchprice()))%></td>
                </tr>
			 <%}
			 		%>
		 
		 	<%
		 		if(saleList.isEmpty()) {
				%>
        
       <tr><td class="label"><%="NA"%></td>
        <td class="data"><%=0.0%></td>
          <td class="data"><%=0.0%></td>
         <td class="data"><%=0.0%></td></tr>
			 <%
			 }%>
             <tr>
        <td align="right"><b>Total</b></td>
       <td class="data"><b><%=FormatNumber.formatNumberForJSP(totalSale)%></b></td>
       <td class="data"><b><%=FormatNumber.formatNumberForJSP(totalProfit)%></b></td>
       <td class="data" style="color:#009900"><b><%=FormatNumber.formatNumberForJSP(totalPurch)%></b></td>
        </tr></table><br />
			
		
        <table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF">
  		<tr><td align="center" colspan="4" bgcolor="#FFF0E1"><b>PRIZE WINNING TICKETS (PWT)</b></td></tr>
        <tr>
        <th width="19%" align="left">Game Name</th>
        <th width="56%" align="left">Winning Amount</th>
        <th width="25%" align="left"><s:property value="#application.TIER_MAP.RETAILER" />'s Net Receivable PWT</th>
         </tr>
         	
		 		<%
		 		if(pwtList.isEmpty()) {
				%>
        <tr>
      <td class="label"><%="NA"%></td>
     <td class="data"><%=0.0%></td>
     <td class="data"><%=0.0%></td>
       </tr>
                 
   <%
			 }%>
		 		<%
		 		
		 		Iterator pwtItr = pwtList.iterator();
				while (pwtItr.hasNext()) {
				PWTPaymentsBean pwtBean=(PWTPaymentsBean)pwtItr.next();
				
				totalPwtAmt = totalPwtAmt+ pwtBean.getNetPWT();
				totalPwtComm=totalPwtComm+pwtBean.getNetCommAmt();
		        totalNetPwt=totalNetPwt+pwtBean.getTotalAmt();
		        System.out.println("######### Winning ");
			 %>
			 <tr><td class="label"><%=pwtBean.getGameName()%></td>
             <td class="data"><%=FormatNumber.formatNumberForJSP(pwtBean.getNetPWT())%></td>
            <td class="data"><%=FormatNumber.formatNumberForJSP(pwtBean.getTotalAmt())%></td>
              </tr>
			 <%}%>
      <tr>
    				
    <td align="right"><b>Total </b></td>
    <td class="data"><b><%=FormatNumber.formatNumberForJSP(totalPwtAmt)%></b></td>
   <td class="data"><b style="color:#FF0000"><%=FormatNumber.formatNumberForJSP(totalNetPwt)%></b></td>
    </tr>
                </table><br />
		 
		 		 <table width="100%" border="1" cellspacing="0" cellpadding="4" bgcolor="#FFF8F0" bordercolor="#FFF0DF">
  		<tr><td align="center" colspan="4" bgcolor="#FFF0E1"><b>PAYMENTS</b></td></tr>
        <tr>
        <th align="left" colspan="3">Payment Type</th>
        <th align="left" colspan="1">Amount</th>
           </tr>
           		<%
		 		if(paymentList.isEmpty()) {
				%>
            <tr>
            <td class="label" colspan="3"><%="NA"%></td>
            <td class="data"><%=0.0%></td>
            </tr>
              <%
			 }%>
		 		<%
		 		
		 		Iterator paymentsItr = paymentList.iterator();
				while (paymentsItr.hasNext()) {
				CashChqPmntBean chqPmntBean=(CashChqPmntBean)paymentsItr.next();
				if(chqPmntBean.getPaymentType().equalsIgnoreCase("Cash")||chqPmntBean.getPaymentType().equalsIgnoreCase("Cheque")){
					totalPayments = totalPayments+ chqPmntBean.getPaymentAmount();
				}else{
					totalPayments = totalPayments-chqPmntBean.getPaymentAmount();
				}
				System.out.println("######### before Last ");
			 %>
            
            <tr>
            <td class="label" colspan="3"><%=chqPmntBean.getPaymentType()%></td>
            <td class="data"><%=FormatNumber.formatNumberForJSP(chqPmntBean.getPaymentAmount())%></td>
            </tr>
            <%}%>
            <tr>
        <td bgcolor="#FFF0E1" colspan="2">&nbsp;</td>
        <td width="31%" align="left" bgcolor="#FFF0E1"><b>Receivable From Total Net Payment</b></td>
        <td width="23%" align="left" bgcolor="#FFF0E1"><b style="color:red;"><%=FormatNumber.formatNumberForJSP(totalPayments)%></b></td>
        </tr></table><br />
		 	</td>
		 </tr>
		 </table><br />
		 <% double weeklyClosingBal=(Double)session.getAttribute("PREVIOUS_BAL")+totalPurch-totalNetPwt-totalPayments;%>
		 <table width="100%" border="1" cellspacing="0" cellpadding="6" bgcolor="#FFE8FF" bordercolor="#eeeeee"><tr>
    	<td width="25%"><b>Closing Ledger Balance</b></td>
    	<td width="21%" class="data"><%=FormatNumber.formatNumberForJSP(reconcilationList.get(3)) %></td>
    	<td width="29%" bgcolor="#F2F2FF"><b>Reconciliation Closing Balance</b></td>
    	<td width="25%" bgcolor="#F2F2FF" class="data"><%=FormatNumber.formatNumberForJSP(weeklyClosingBal) %></td>
        </tr></table><br />
		 
		 
		 <table width="100%" border="1" cellspacing="0" cellpadding="3" bordercolor="#CCCCCC">
  			 <tr><td colspan="5" align="center"><b> AVAILABLE STOCK </b></td></tr>
  			<tr>
  	  			<th width="25%">Game Name</th>
  	  			<th width="20%">No. Of Tickets</th>
    			<th width="25%">Total MRP</th>
    			<th width="10%">Percent</th>
    			<th width="20%">Purchase Price</th>
 			 </tr>
  			
  			<%
		 		if(stockMrpDiscMap.isEmpty()) {
				%>
				 <tr><td class="label"><%="NA"%></td><td class="data"><%="NA"%></td><td class="data"><%=0%></td><td class="data"><%=0.0%></td><td class="data"><%=0.0%></td></tr>
			 <%
			 }%>
  			
  			<%
		 		Iterator invItr = stockMrpDiscMap.entrySet().iterator();
				while (invItr.hasNext()) {
				Map.Entry<Integer,StockMrpDiscountBean> stockItrpair = (Map.Entry<Integer,StockMrpDiscountBean>)invItr.next();
				StockMrpDiscountBean stockMrpDiscountBean = stockItrpair.getValue();
					System.out.println("######### Last "+stockMrpDiscountBean);
					totalBooksStk=totalBooksStk+stockMrpDiscountBean.getStCountTickets();
				    totalMrpStk=totalMrpStk+stockMrpDiscountBean.getStMrp();
				    totalDiscPriceStk=totalDiscPriceStk+stockMrpDiscountBean.getStDiscPrice();
				   System.out.println("######### Last ");
			 %>
			 <tr><td class="label"><%=stockMrpDiscountBean.getStGameName()%></td><td class="data"><%=FormatNumber.formatNumber(stockMrpDiscountBean.getStCountTickets())%></td><td class="data"><%=FormatNumber.formatNumberForJSP(stockMrpDiscountBean.getStMrp())%></td><td class="data"><%=FormatNumber.formatNumber(stockMrpDiscountBean.getStPercent())%></td><td class="data"><%=FormatNumber.formatNumberForJSP(stockMrpDiscountBean.getStDiscPrice())%></td></tr>
			 <%}%>
			 <tr>
    				<td width="20%" colspan="1"><b>Total</b></td>
    				<td width="20%" class="data"><%=totalBooksStk%></td>
    				<td width="20%" class="data"><%=FormatNumber.formatNumberForJSP(totalMrpStk)%></td>
    				<td width="20%" class="data">&nbsp;</td>
    				<td width="20%" class="data"><%=FormatNumber.formatNumberForJSP(totalDiscPriceStk)%></td>
    			</tr>
			 </table>
	   <table  border="2" cellpadding="4" cellspacing="0" bordercolor="#eeeeee" align="center">
       <tr><td align="left" ></td><b> Reconciliation Details After Last Payment </b><td align="center" colspan="1" bgcolor="#FFF0E1"></td>
       <td align="center" colspan="1" bgcolor="#FFF0E1"></td></tr>
        <tr>
       
        <th align="center">Reconciliation Opening Balance</th>
        <th align="center">Payable Against Net Sales</th>
        <th align="center">PWT</th>
        <th align="center">Reconciliation Closing Balance</th>
     	</tr>
        <%
		 	if(reconAftrPmntList!=null){
	 		ConsolidatedBean consolidatedBean=(ConsolidatedBean)reconAftrPmntList.get(0);
	 		 		
			 
			 %>
				<tr>
		        <td class="data" bgcolor="#f8f8f8" align="right"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getWeeklyOpeningBal()))%></td>
                <td class="data" bgcolor="#f8f8f8" align="right"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getNetPayable()))%></td>
                <td class="data" bgcolor="#f8f8f8" align="right"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getNetPWT()))%></td>
                <td class="data" bgcolor="#f8f8f8" align="right"><%=FormatNumber.formatNumberForJSP((consolidatedBean.getWeeklyClosingBal()))%></td>
                </tr>
                
			  <tr>
                <td colspan="6" height="8"><br /><b> Last Payment Details</b></td>
                </tr>
                <tr>
       
        <th align="center" colspan="2">Transaction Date</th>
        <th align="center">Transaction Type</th>
        <th align="center">Amount</th>
        </tr>
        <tr>
        <td class="data" bgcolor="#f8f8f8" align="center" colspan="2"><%=reconAftrPmntList.get(4)%></td>
          <td class="data" bgcolor="#f8f8f8" align="center"><%=(String)reconAftrPmntList.get(5)%></td>
          <td class="data" bgcolor="#f8f8f8" align="center"><%=FormatNumber.formatNumberForJSP((reconAftrPmntList.get(3)))%></td>
     
     </tr>
		 
		 	<%
		 	}
		 		else {
				%>
        
          <tr><td colspan="4"> No Payments Were Made During The Selected Period</td></tr>
			 <%
			 }%>
			 </table>
		 

		 </td></tr>		 	
		 </table>
		 
		 
	</div></div>
	<br />
	<input type="button" class="button" value="Print Detailed" onclick="printTable('agent/agt_rep_recon_detailed_print.jsp')" />
	<br />
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_reconciliation_Detail.jsp,v $
$Revision: 1.3 $
</code>