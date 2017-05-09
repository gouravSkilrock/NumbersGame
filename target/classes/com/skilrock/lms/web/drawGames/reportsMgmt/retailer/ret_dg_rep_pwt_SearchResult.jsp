
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>

<body onload="disable()">

<br/>
  
<div align="center"><h3><u>Draw Game <s:text name="PWT"/> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	
  	
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	    <tr>
			<td width="12%" nowrap="nowrap">Game Name</td>
			<td width="10%">PWT Amount</td>			
			<td width="10%">Net Amount</td>
			<td width="10%" >Profit amount</td>
		</tr>		
		<s:if test="#session.searchResultRet.size>0">
			<s:iterator id="beanCart" value="#session.searchResultRet">
			<tr class="startSortable">				
				<td width="12%"  align="left" nowrap="nowrap"><s:property value="%{gameName}" /></td>				
				<td width="10%" align="right">&nbsp;<s:set name="clmMrpJsp" value='%{clmMrp}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmMrpJsp")) %></td>
				<td width="10%" align="right"><s:set name="clmNetJsp" value='%{clmNet}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmNetJsp")) %></td>
				<th width="10%" align="right"><s:set name="clmProfitJsp" value='%{clmProfit}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("clmProfitJsp"))%></th>
			</tr>	
			</s:iterator> 
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
$RCSfile: ret_dg_rep_pwt_SearchResult.jsp,v $
$Revision: 1.3 $
</code>