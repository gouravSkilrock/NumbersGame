
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>

<body onload="disable()">

<br/>
  
<div align="center"><h3><u><s:text name="Jackpot"/> View <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>	



  	
	 <table width="95%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
	 		<th rowspan="2">Draw_Id</th>
	 		<th rowspan="2">Draw Date</th>
	 		
	 		<th rowspan="2">Sale Amt</th>
	 		<th rowspan="2">Prize Fund</th>
	 		
	 		<th rowspan="2">Fixed Prizes Fund</th>
	 		<th rowspan="2">Remaining Prize Fund</th>
	 		
	 		<th rowspan="1" colspan="4">RSR</th>
	 		
	 		
	 		<th colspan="3"><s:text name="Jackpot"/></th>
	 		
	 		
	
		</tr>	
		<tr>
		   <th colspan="1">RSR For This Draw</th>
	 		<th colspan="1"><s:text name="Total_Available_RSR"/></th>
	 		<th colspan="1">RSR Utilized in this Draw</th>
	 			<th colspan="1">Carried Over RSR</th>
	 		
	 		<th colspan="1"><s:text name="Jackpot"/> for This Draw</th>
	 		<th colspan="1">Total Available <s:text name="Jackpot"/></th>
	 		<th colspan="1">Carried Over <s:text name="Jackpot"/> Amt</th>
		</tr>
		
		<s:if test="#session.jackpotViewList.size>0">
			<s:iterator id="beanCart" value="#session.jackpotViewList">
			<tr>				
				
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="%{drawId}" /></td>	
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="%{drawDate}" /></td>
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="%{totalSaleAmt}" /></td>
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="%{totalPrizeFund}" /></td>		
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="%{fixedPrizesFund}" /></td>					
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="%{remaningPoolFund}" /></td>				
				 
				 
				  
				 
				   		 
				     <td width="10%" align="right" nowrap="nowrap">&nbsp;<s:property value="%{RSRForThisDraw}" /></td>
			         <td width="10%" align="right" nowrap="nowrap"><s:set name="totalAvailableRSR" value='%{totalAvailableRSR}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalAvailableRSR")) %></td>
			         <td width="10%" align="right" nowrap="nowrap">&nbsp;<s:set name="RSRUtilized" value='%{RSRUtilized}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("RSRUtilized")) %></td>
			          <td width="10%"  align="left" nowrap="nowrap"><s:property value="%{carriedOverRSR}" /></td>	
			
			      
				     <td width="10%" align="right">&nbsp;<s:set name="jackpotForThisDraw" value='%{jackpotForThisDraw}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("jackpotForThisDraw")) %></td>
			         <td width="10%" align="right"><s:set name="totalAvailableJackpotAmt" value='%{totalAvailableJackpotAmt}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalAvailableJackpotAmt")) %></td>
			            <td width="10%"  align="left" nowrap="nowrap"><s:set name="carriedOverJackPtAmt" value='%{carriedOverJackpot}'/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("carriedOverJackPtAmt")) %></td>
			   
			         
			
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
$RCSfile: bo_dg_rep_jackpot_result.jsp,v $
$Revision: 1.2 $
</code>