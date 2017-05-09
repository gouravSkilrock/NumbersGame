<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>
<body onload="disable()">
<br/>
 <div align="center"><h3><u><s:text name="label.scratch.report.pwt.report"/>&nbsp;
	<%if("".equals(((DateBeans)session.getAttribute("datebean") ).getReportType())) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((DateBeans)session.getAttribute("datebean") ).getReportType());}%>
	</u></h3></div>
<p>	 
	
	<table width="500" align="center">	
	
<%-- 	<tr>
		<td align="left">
			<table width="500" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
				<tr>						
					<th colspan="2" align="center"><s:text name="PWT"/> Claimed at <s:property value="#application.TIER_MAP.AGENT" /> End</th>					
				</tr>
				<tr>						
					<td>Winning Claimed to <s:property value="#application.TIER_MAP.AGENT" /></td>
					<td align="right"><s:set name="strAgtPwtAmt"
							value="#session.agtPwtAmt" /><%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("strAgtPwtAmt"))%></td>					
				</tr>			
		 	</table>
		</td>	
	</tr>
	<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr>
	 --%>
	
	
	
	<tr>
		<td align="left">
		 <table width="500" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
			<tr>					
				<th colspan="2" align="center"><s:text name="label.scratch.winning.paid.to.players"/></th>												
			</tr>
			<tr>					
				<th align="center"><s:text name="label.scratch.party.name"/></th>
				<th align="center"><s:text name="label.total.pwt.amount"/></th>										
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
					<td colspan="7" align="center"><s:text name="label.scratch.no.record.available"/></td>
				</tr>
			</s:else>
			
		 </table>
		 <s:div id="naviga" cssStyle="text-align: center;width: 500px">
			<s:if test=" #session.APP_ORDER_LIST5.size >10 ">			    	
				<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="ret_rep_pwt_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="ret_rep_pwt_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
			    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="ret_rep_pwt_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="ret_rep_pwt_Navigate.action?end=last">Last</s:a></s:else>      
		    </s:if>		
		 </s:div>
	 	</td>
	 </tr>
	 </table>
	 
	 <div id="lastDown" style="text-align: center">
	 </div>
	 
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_pwt_SearchResult.jsp,v $
$Revision: 1.2 $
</code>