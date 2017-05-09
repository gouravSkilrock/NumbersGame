
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>

<body onload="disable()">
  
	<div align="center"><h3><u>Cash & Cheque <s:text name="Report"/> &nbsp;
	<%if( "".equals(  ((DateBeans)session.getAttribute("datebean") ).getReportType()   )) { %>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<% } else {%>
	<%="Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType()%>
	<%}%>
	</u></h3></div>
	<p>	
	<s:if test="#session.searchCashChqResult.size>0">
			<s:div id="excel" cssStyle="text-align: left;width: 100%;font-size: 9pt"><s:a href="agt_rep_cashChq_ExpExcel_Ghana.action">Export As Excel</s:a></s:div>
	</s:if>
	
	<s:if test="#session.agentcashchq!=null">
	  
			<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
				<tr>
					<th align="center" colspan="5">Cash & Cheque Deposit To BO</th>
				</tr>
				<tr>			
					<th align="center">Cash Deposit</th>
					<th align="center">Cheque Deposit</th>
					<th align="center">Cheque Bounce</th>
					
					<th align="center">Credit Note</th>
					<th align="center">Debit Note</th>
					<th align="center">Bank Deposit</th>
					<th align="center">Net Deposit</th>				
				</tr>
					<s:if test="#session.agentbocashchqdetail!=null">
					<tr class="startSortable">
							<td align="right"><s:set name="strBoTotalCash" value="#session.agentbocashchqdetail.totalCash" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalCash")) %></td>
							<td align="right"><s:set name="strBoTotalChq" value="#session.agentbocashchqdetail.totalChq" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalChq")) %></td>
							<td align="right"><s:set name="strBoTotalChqBounce" value="#session.agentbocashchqdetail.cheqBounce" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalChqBounce")) %></td>
							<td align="right"><s:set name="strBoTotalCredit" value="#session.agentbocashchqdetail.credit" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalCredit")) %></td>
							<td align="right"><s:set name="strBoTotalDebit" value="#session.agentbocashchqdetail.debit" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalDebit")) %></td>
							<td align="right"><s:set name="strBoTotalBank" value="#session.agentbocashchqdetail.bankDeposit" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalBank")) %></td>
							<td align="right"><s:set name="strBoTotalNetAmt" value="#session.agentbocashchqdetail.netAmt" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strBoTotalNetAmt")) %></td>
					</tr>
						
					</s:if>
					<s:else>
						<tr>
							<td colspan="7" align="center"> No Transaction with BO</td>
						</tr>
					</s:else>				
			 </table>
			 <br/>
		</s:if>
	
	
  		
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr >
				
			<th align="center">Party Name</th>
			<th align="center">Cash Collection</th>
			<th align="center">Cheque Collection</th>
			<th align="center">Cheque Bounce</th>
			<th align="center">Credit Note</th>
			<th align="center">Debit Note</th>
			<th align="center">Net Collection</th>
			<th align="center">Live Balance</th>
						
		</tr>
		
		<s:if test="#session.cashchqDetail.size>0">
			<s:iterator id="beanCart" value="#session.cashchqDetail">
			<tr>
				<td align="left"><s:property value="name" /></td>
				<td align="right"><s:property value="totalCash" /></td>
				<td align="right"><s:property value="totalChq" /></td>
				<td align="right"><s:property value="cheqBounce" /></td>
				<td align="right"><s:property value="credit" /></td>
				<td align="right"><s:property value="debit" /></td>
				<td align="right"><s:property value="netAmt" /></td>
				<td align="right"><s:property value="currentBal" /></td>
				
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="7" align="center"> No Transaction's Record Found</td>
			</tr>
		</s:else>
		
	 </table>
	 <s:div id="naviga" cssStyle="text-align: center;width: 100%">
		<s:if test=" #session.APP_ORDER_LIST5.size >10 ">
    	
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="agt_rep_cashChq_Navigate_Ghana.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="agt_rep_cashChq_Navigate_Ghana.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="agt_rep_cashChq_Navigate_Ghana.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="agt_rep_cashChq_Navigate_Ghana.action?end=last">Last</s:a></s:else>      
    
		</s:if>
		<!-- 
		<s:if test="#session.searchCashChqResult.size>0">
			<s:div id="excel" cssStyle="text-align: left;font-size: 9pt"><s:a href="exportCashChqExcelForBO.action">Export As Excel</s:a></s:div>
		</s:if>
		 -->
	 </s:div>
	 
	 

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_cashChq_SearchResultGhana.jsp,v $
$Revision: 1.3 $
</code>