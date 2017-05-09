<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="org.eclipse.jdt.internal.compiler.ast.ForeachStatement"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%
	System.out.println("***********Live Report*******");
%>
<h3><u>Total Player <s:text name="PWT"/> Claimed at <s:property value="#application.TIER_MAP.AGENT" /> End</u></h3>
<table width="100%" cellspacing="0" cellpadding="3" border="1" bordercolor="#CCCCCC" align="center">
	<tr>
		<th><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
		<th>Net <s:text name="PWT"/> Amount</th>
	</tr>
	<tr>
		<td><s:property value ="agtName" /></td>
		<td><s:property value="#session.agtDirPlrPwt"/></td>
	</tr>
</table>
<br/>
<div align="center"><h3><u>Consolidated Live <s:text name="Report" /> &nbsp;
	<%if("Date Wise".equals((String)session.getAttribute("reportType"))) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((String)session.getAttribute("reportType")));}%>
	</u></h3></div>
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	
	
	<tr>
	<th rowspan="2" >
				Organization Name
			</th>
	<th colspan="3" align="center">Draw Game</th>
	<th colspan="2" align="center">Scratch Game</th>
			<th rowspan="2">
				Net Amount
			</th>
	</tr>
		<tr>
			
			<th>
				Sale Amount
			</th>
			
			<th>
				Cancel Amount
			</th>
			
			<th>
				<s:text name="PWT"/> Amount
			</th>
			<th>
				Sale Amount
			</th>
			<th>
				<s:text name="PWT"/> Amount
			</th>
			
		</tr>
		<s:if test="%{#session.reportMap!=null && #session.reportMap.size>1}">
		<s:iterator id="reportMap" value="#session.reportMap">
		<s:set name="orgValue" value="%{value}"></s:set>
		<s:if test="%{key!='Total' && key!='dirPlrPwt'}">
			
			<tr class="startSortable">
				<td>
					<s:property value="%{key}"/>
				</td>
				<%
					String orgValue = pageContext.getAttribute("orgValue")
									.toString();
							String orgValArr[] = orgValue.split(",");
							for (int i = 0; i < orgValArr.length; i++) {
				%>
				<td>
					<%=orgValArr[i]%>
				</td>
				<%
					}
				%>
			</tr>
		</s:if>
		
		</s:iterator>
		<s:set name="totalVal" value="#session.reportMap.Total"></s:set>
		<tr style="font-weight: bold;">
				<td>
					Total
				</td>
				<%
					String orgValue = pageContext.getAttribute("totalVal")
									.toString();
							String orgValArr[] = orgValue.split(",");
							for (int i = 0; i < orgValArr.length; i++) {
				%>
				<td>
					<%=orgValArr[i]%>
				</td>
				<%
					}
				%>
			</tr>
		
	</s:if>
	<s:else>
		<tr>
			<td colspan="8" align="center">
				No Record Found
			</td>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_consGameReport_SearchResult.jsp,v $
$Revision: 1.2 $
</code>