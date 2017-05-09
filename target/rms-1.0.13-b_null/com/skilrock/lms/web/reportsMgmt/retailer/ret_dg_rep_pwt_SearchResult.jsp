
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
 <div align="center"><h3><u>Draw Game <s:text name="PWT"/> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>
<p>	 
	
	<table width="500" align="center">	
	
	<tr>
		<td align="left">
			<table width="500" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
				<tr>						
					<th colspan="2" align="center"><s:text name="PWT"/> Claimed at <s:property value="#application.TIER_MAP.AGENT.toUpperCase()" /> End</th>					
				</tr>
				<tr>						
					<td>PWT Claimed to <s:property value="#application.TIER_MAP.AGENT.toUpperCase()" /></td>
					<td align="right"><s:set name="strAgtPwtAmt"
							value="#session.agtPwtAmt" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtPwtAmt"))%></td>					
				</tr>			
		 	</table>
		</td>	
	</tr>
	<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr>
	
	
	
	
	<tr>
		<td align="left">
		 <table width="500" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
			<tr>					
				<th colspan="2" align="center"><s:text name="PWT"/> Collected From Players</th>												
			</tr>
			<tr>					
				<th align="center">Party Name</th>
				<th align="center">Total <s:text name="PWT"/> Amount</th>										
			</tr>
			
			<s:if test="#session.pwtResultRet.size>0">
				<s:iterator id="beanCart" value="#session.pwtResult">
				<tr class="startSortable">
					<td align="left"><s:property value="partyName" /></td>
					<td align="right"><s:set name="strPwtAmt"
							value="pwtAmt" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strPwtAmt"))%></td>					
				</tr>	
				</s:iterator> 
			</s:if>
			<s:else>
				<tr>
					<td colspan="7" align="center">No Record Available</td>
				</tr>
			</s:else>
			
		 </table>		
	 	</td>
	 </tr>
	 </table>
	 
	 <div id="lastDown" style="text-align: center">
	 </div>
	 

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_dg_rep_pwt_SearchResult.jsp,v $
$Revision: 1.2 $
</code>