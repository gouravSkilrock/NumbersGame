
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
  
<div align="center"><h3><u>Draw Game <s:text name="PWT"/> <s:text name="Report"/> &nbsp;
	<%
	
	System.out.println(session.getAttribute("searchResultRet"));
	if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	
<div id="excel"  text-align: center;width: 750px;font-size: 9pt"><s:a href="agt_rep_DGPwtReport_ExpExcel.action?orgType=BO">Export As Excel</s:a></div>
	<span style="position:relative;left:380px;">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></span>  	
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
				<th align="center" colspan="4" ><s:property value="#session.orgName" /></th>
			</tr>
			<tr>
				<td align="center" colspan="4"><s:property value="#session.orgAdd" /></td>
			</tr>
		<tr>
			<s:if test="%{filter=='Game Wise'}">
			<td width="34%" nowrap="nowrap">Game Name</td>
			</s:if>
			<s:else>
			<td width="34%" nowrap="nowrap"><s:property value="#application.TIER_MAP.RETAILER" /> Name</td>
			</s:else>
			<td width="22%">Mrp Amount</td>			
			<td width="22%">Net Amount</td>
			<td width="22%">Profit amount</td>
		</tr>		
		<s:if test="#session.searchResultRet.size>0">
			<s:iterator id="beanCart" value="#session.searchResultRet">
				<s:if test="%{(gameName!=null && gameName != 'Total')||(retName!=null && retName != 'Total')}">
					<tr class="startSortable">				
						<td width="34%"  align="left" nowrap="nowrap"><s:property value="%{gameName}" /><s:property value="%{retName}" /></td>				
						<td width="22%" align="right">&nbsp;<s:set name="clmMrpJsp" value='%{clmMrp}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmMrpJsp")) %></td>
						<td width="22%" align="right"><s:set name="clmNetJsp" value='%{clmNet}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmNetJsp")) %></td>
						<th width="22%" align="right"><s:set name="clmProfitJsp" value='%{clmProfit}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmProfitJsp"))%></th>	
					</tr>
				</s:if>
				<s:else>
						<s:set name="totalclmMrpJsp" value="%{clmMrp}"/>
						<s:set name="totalclmNetJsp" value="%{clmNet}"/>
						<s:set name="totalclmProfitJsp" value="%{clmProfit}"/>
				</s:else>	
			</s:iterator>
			<th width="12%" align="center" nowrap="nowrap">Total</th>				
			<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalclmMrpJsp"))%></th> 
			<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalclmNetJsp"))%></th> 
			<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalclmProfitJsp"))%></th> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Records to Process</td>
			</tr>
		</s:else>
		
	 </table></br></br>
<s:if test="%{#application.COUNTRY_DEPLOYED != 'ZIMBABWE'}">
	 <div id="dirPlrTable">
	 	<s:if test="%{filter != 'Retailer Wise'}">
	 <div align="center"><h3><u>Winning Direct Player &nbsp;
	<%
	
	if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("for : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	
  	<span style="position:relative;left:380px;">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></span>
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
			<th align="center" colspan="4"><s:property value="#session.orgName" /></th>
		</tr>
		<tr>
			<td align="center" colspan="4"><s:property value="#session.orgAdd" /></td>
		</tr>
	 	<tr>
	 		<s:if test="%{filter=='Game Wise'}">
				<td width="34%" nowrap="nowrap">Game Name</td>
			</s:if>
			<s:else>
				<td width="34%" nowrap="nowrap"><s:property value="#application.TIER_MAP.RETAILER" /> Name</td>
			</s:else>
			<td width="22%">Mrp Amount</td>			
			<td width="22%">Net Amount</td>
			<td width="22%">Profit amount</td>
	 	</tr>
	 	<s:if test="#session.dirPlrPwtRepAgt.size>0">
			<s:iterator id="beanCart" value="#session.dirPlrPwtRepAgt">
				<s:if test="%{(gameName!=null && gameName != 'Total')}">
					<tr>				
						<td width="34%"  align="left" nowrap="nowrap"><s:property value="%{gameName}" /><s:property value="%{retName}" /></td>				
						<td width="22%" align="right">&nbsp;<s:set name="pwtClmJsp" value='%{pwtAmtClm}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtClmJsp")) %></td>
						<td width="22%" align="right"><s:set name="netClmJsp" value='%{netAmtClm}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netClmJsp")) %></td>
						<th width="22%" align="right"><s:set name="clmProfitJsp" value='%{profitClm}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmProfitJsp"))%></th>	
					</tr>
				</s:if>
				<s:else>
					<s:set name="totaldirclmMrpJsp" value="%{pwtAmtClm}"/>
					<s:set name="totaldirclmNetJsp" value="%{netAmtClm}"/>
					<s:set name="totaldirclmProfitJsp" value="%{profitClm}"/>
				</s:else>	
			</s:iterator>
			<th width="12%" align="center" nowrap="nowrap">Total</th>				
			<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totaldirclmMrpJsp"))%></th> 
			<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totaldirclmNetJsp"))%></th> 
			<th width="10%" align="right"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totaldirclmProfitJsp"))%></th>  
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Records to Process</td>
			</tr>
		</s:else>
	 </table>
	 </s:if>
	 </s:if>
	 
	 </body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_dg_rep_pwt_SearchResult.jsp,v $
$Revision: 1.2 $
</code>