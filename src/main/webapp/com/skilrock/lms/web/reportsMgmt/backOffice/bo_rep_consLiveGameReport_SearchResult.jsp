<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="org.eclipse.jdt.internal.compiler.ast.ForeachStatement"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%
	System.out.println("***********Live Report*******");
%>
<s:if test="%{#application.COUNTRY_DEPLOYED != 'ZIMBABWE'}">
	<div align="center"><h3><u>Total Player <s:text name="PWT"/> Claimed at <s:property value="#application.TIER_MAP.AGENT" /> End</u></h3></div>
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
</s:if>
<br/>
<br/>
<div align="center"><h3><u>Consolidated Live <s:text name="Report"/> &nbsp;
	<%if("Date Wise".equals((String)session.getAttribute("reportType"))) {%>
  		 From : 
  		<%=GetDate.getConvertedDate(( (DateBeans)session.getAttribute("datebean") ).getStartDate())%>
  		&nbsp;&nbsp;To&nbsp;:&nbsp;
  		<%=GetDate.getConvertedDate(((DateBeans)session.getAttribute("datebean") ).getEndDate())%>
  		
	<%} else {out.println("Of : "+((String)session.getAttribute("reportType")));}%>
	</u></h3></div>
<div id="excel" align="center" text-align: center;width: 750px;font-size: 9pt"><s:a href="bo_rep_ConsLiveGame_ExpExcel.action?orgType=BO">Export As Excel</s:a></div>
<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	<tr>
		<th align="center" colspan="13" ><s:property value="#session.orgName" /></th>
		<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
	</tr>
	<tr>
		<td align="center" colspan="14"><s:property value="#session.orgAdd" /></td>
	</tr>
	<tr>
	<th rowspan="2" >
				<s:property value="#application.TIER_MAP.RETAILER" /> Name
			</th>
	<th colspan="3" align="center">Draw Game</th>
	<th colspan="2" align="center"><s:a
					href="bo_rep_liveGameReport_SearchExpand.action?start_date=%{start_date}&end_Date=%{end_Date}&agtOrgId=%{agtOrgId}"
					theme="ajax" targets="down1">Scratch Game</s:a></th>
	<th colspan="2" align="center">OLA Report</th>
	<th rowspan="2">CS Sale</th>
	<th colspan="3" align="center"><s:text name="label.sport.lot"/></th>
	<th rowspan="2">
				Cash Submitted
			</th>
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

			<th>
				Deposit Amount
			</th>
			<th>
				Withdrawal Amount
			</th>
			<th>
				Sale Amount
			</th>
			
			<th>
				Cancel Amount
			</th>
			
			<th>
				<s:text name="PWT"/> Amount
			</th>
		</tr>
		<s:if test="%{#session.reportMap!=null && #session.reportMap.size>1}">
		<s:iterator id="reportMap" value="#session.reportMap">
		<s:set name="orgValue" value="%{value}"></s:set>
		<s:if test="%{key!='Total' && key!='dirPlrPwt' && key != 'agtName'}">
			
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
				<td align="right">
					<%=FormatNumber.formatNumber(orgValArr[i])%>
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
				<td align="right">
					<%=FormatNumber.formatNumberForJSP(orgValArr[i])%>
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
$RCSfile: bo_rep_consLiveGameReport_SearchResult.jsp,v $
$Revision: 1.3 $
</code>