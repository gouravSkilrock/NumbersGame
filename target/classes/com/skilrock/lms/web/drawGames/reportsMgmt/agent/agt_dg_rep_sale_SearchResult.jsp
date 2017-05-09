
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<s:head theme="ajax"/>

<body onload="disable()">

<br/>
  
<div align="center"><h3><u>Draw Game Sale <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	


<div id="excel"  text-align: left;width: 750px;font-size: 9pt"><s:a href="agt_rep_DGSaleReport_ExpExcel.action?orgType=BO">Export As Excel</s:a></div>
  	
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
			<th align="center" colspan="6" ><s:property value="#session.orgName" /></th>
			<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
		</tr>
		<tr>
			<td align="center" colspan="7"><s:property value="#session.orgAdd" /></td>
		</tr>
	 	<tr>
	 		<th colspan="1"></th>
	 		<th colspan="3">MRP Sale Details</th>
	 		<th colspan="3">Deducted Sale Details</th>
	 	</tr>
	 	
		<tr>
			
			<s:if test="%{filter=='Game Wise'}">
			<td width="12%" nowrap="nowrap">Game Name</td>
			</s:if>
			<s:else>
			<td width="12%" nowrap="nowrap"><s:property value="#application.TIER_MAP.RETAILER" /> Name</td>
			</s:else>
			<td width="10%">Sale Amount</td>			
			<td width="10%">Return Amount</td>
			<td width="10%" >Net Sale amount</td>
			<td width="10%">Sale Amount</td>			
			<td width="10%">Return Amount</td>
			<td width="10%" >Net Sale amount</td>	
		</tr>		
		<s:if test="#session.searchResultRet.size>0">
			<s:iterator id="beanCart" value="#session.searchResultRet">
			<tr class="startSortable">
				<s:if test="%{(gamename!=null && gamename != 'Total')||(agentName!=null && agentName != 'Total')||(retName!=null && retName != 'Total')}">				
					<td width="12%"  align="left" nowrap="nowrap"><s:property value="%{gamename}" /><s:property value="%{retName}" /></td>				
					<td width="10%" align="right">&nbsp;<s:set name="saleJsp" value='%{saleMrp}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleJsp")) %></td>
					<td width="10%" align="right"><s:set name="refundJsp" value='%{saleReturnMrp}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("refundJsp")) %></td>
					<th width="10%" align="right"><s:set name="netSaleJsp" value='%{netMrpAmt}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netSaleJsp"))%></th>	
					<td width="10%" align="right">&nbsp;<s:set name="saleJspNet" value='%{saleAmt}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleJspNet")) %></td>
					<td width="10%" align="right"><s:set name="refundJspNet" value='%{returnAmt}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("refundJspNet")) %></td>
					<th width="10%" align="right"><s:set name="netJsp" value='%{netAmt}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netJsp")) %></th>
				</s:if>
				<s:else>
					<s:set name="totalsaleJsp" value='%{saleMrp}'/>
					<s:set name="totalrefundJsp" value='%{saleReturnMrp}'/>
					<s:set name="totalnetSaleJsp" value='%{netMrpAmt}'/>
					<s:set name="totalsaleJspNet" value='%{saleAmt}'/>
					<s:set name="totalrefundJspNet" value='%{returnAmt}'/>
					<s:set name="totalnetJsp" value='%{netAmt}'/>
				</s:else>
			</tr>	
			</s:iterator>
			<tr>
				<th width="12%" align="left" nowrap="nowrap">Total</th>				
				<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalsaleJsp"))%></th>
				<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalrefundJsp"))%></th>
				<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalnetSaleJsp"))%></th>	
				<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalsaleJspNet"))%></th>
				<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalrefundJspNet"))%></th>
				<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalnetJsp"))%></th>
			</tr> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Records to Process</td>
			</tr>
		</s:else>
		
	 </table>
	 </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_dg_rep_sale_SearchResult.jsp,v $
$Revision: 1.2 $
</code>