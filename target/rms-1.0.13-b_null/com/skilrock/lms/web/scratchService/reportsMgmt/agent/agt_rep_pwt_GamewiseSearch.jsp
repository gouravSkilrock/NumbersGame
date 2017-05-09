
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>

<body onload="disable()">

<br/>
 <div align="center"><h3><u>Game Wise <s:text name="PWT"/> <s:text name="Report"/> &nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>
<p>	 
	
	
  		<s:if test="#session.pwtResult.size>0">
			<div id="excel"  style="text-decoration: underline; text-align: left;width: 90%;font-size: 9pt"><s:a href="#"></s:a></div>
		</s:if>
	 <table width="90%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr >
			
			<th>Game No.</th>	
			<th>Game Name</th>
			<th><s:property value="#application.TIER_MAP.AGENT" />s Claimed</th>
			<th>Players Claimed</th>
			<th><s:property value="#application.TIER_MAP.RETAILER" />s Claimed</th>
			<th>Total <s:text name="PWT"/> Amount</th>
									
		</tr>
		
		<s:if test="#session.pwtResultBO.size>0">
			<s:iterator id="beanCart" value="#session.pwtResultBO">
			<tr class="startSortable">
				<td align="left"><s:property value="gameNbr" /></td>
				<td align="left"><s:property value="gameName" /></td>
				<td align="right"><s:property value="agentPWT" /></td>
				<td align="right"><s:property value="playerPWT" /></td>
				<td align="right"><s:property value="retPWT" /></td>
				<td align="right"><s:property value="totalPWT" /></td>
								
			</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
			<tr>
				<td colspan="6" align="center">No Record Available</td>
			</tr>
		</s:else>
		
	 </table>
	 <div id="naviga" style="text-align: center;width: 90%">
		<s:if test=" #session.APP_ORDER_LIST5.size >10 ">
    	
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="agt_rep_pwt_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="agt_rep_pwt_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="agt_rep_pwt_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="agt_rep_pwt_Navigate.action?end=last">Last</s:a></s:else>      
    
		</s:if>
	
	 </div>
	 
	
	 

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_pwt_GamewiseSearch.jsp,v $
$Revision: 1.2 $
</code>