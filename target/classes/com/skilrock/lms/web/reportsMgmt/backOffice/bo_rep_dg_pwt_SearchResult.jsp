
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax"/>

<body onload="disable()">

<br/>
 <div align="center"><h3><u><s:text name="PWT"/> <s:text name="Report"/> &nbsp;
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
		  		<s:if test="#session.pwtResultBO.size>0">
					<s:div id="excel"  cssStyle="text-decoration: underline; text-align: left;width: 500px;font-size: 9pt">
						<s:a href="#">Export As Excel</s:a>
					</s:div>
				</s:if>
			</td>
		</tr>
		<tr>
		<td align="left">
			 <table width="500" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
				<tr >
						
					<th align="center">Party Name</th>
					<th align="center">Total <s:text name="PWT"/> Amount</th>
											
				</tr>
				
				<s:if test="#session.pwtResultBO.size>0">
					<s:iterator id="beanCart" value="#session.pwtResult">
					<tr class="startSortable">
						<td align="left"><s:property value="partyName" /></td>
						<td align="right">
							<s:if test="%{#session.USER_INFO.userType =='BO' }">
							
								<s:set name="strPwtAmt"
								value="pwtAmt" /><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("strPwtAmt"))%>
							
							</s:if>
							<s:else>
								<s:set name="strPwtAmt"
								value="pwtAmt" /><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("strPwtAmt"))%>
							</s:else>
						</td>
					</tr>	
					</s:iterator> 
				</s:if>
				<s:else>
					<tr>
						<td colspan="7" align="center">No Record Available</td>
					</tr>
				</s:else>
				
			 </table>
		 </tr>
		 
		 <tr>
		 <td>
			<s:if test="#session.pwtResultBO.size>10">		    	
				<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="bo_dg_rep_pwt_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="bo_dg_rep_pwt_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
			    <s:if test="#session.startValueOrderSearch==((#session.pwtResultBO.size/10)*10)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="bo_dg_rep_pwt_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="bo_dg_rep_pwt_Navigate.action?end=last">Last</s:a></s:else>      
		    </s:if>		
	 	</td>
	 </tr>
	 </table>
	 
	 <div id="lastDown" style="text-align: center">
	 </div>
	 

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_dg_pwt_SearchResult.jsp,v $
$Revision: 1.2 $
</code>