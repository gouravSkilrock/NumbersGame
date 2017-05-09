
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<s:head theme="ajax" />
	<body onload="disable()">

		<br />
		<div align="center">
			<h3>
				<u><s:property value="#application.TIER_MAP.AGENT" />  Winning <s:text name="Report" />
				<%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %> From : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
					&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%> <%
 	} else {
 		out.println("Of : "
 				+ ((DateBeans) session.getAttribute("datebean"))
 						.getReportType());
 	}
 %> </u>
			</h3>
		</div>

	<!--  	<div id="excel" text-align:left;width: 750px;font-size: 9pt">
			<s:a href="bo_rep_DGFullReport_ExpExcel.action?orgType=BO">Export As Excel</s:a>
		</div>  -->

		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<th align="center" colspan="5" />
					
					<s:property value="#session.orgName" />
				</th>
				<th align="center">
					Amount in
					<s:property value="#application.CURRENCY_SYMBOL" />
				</th>
			</tr>
			<tr>
				<td align="center" colspan="6" />
					
					<s:property value="#session.orgAdd" />
				</td>
			</tr>
			<tr>
				<th rowspan="2" width="10%" nowrap="nowrap">
					<s:property value="#application.TIER_MAP.AGENT" />
					Name
				</th>
				<th>
					Total <s:text name="PWT" /> Amount
				</th>
				<th>
					Claimed Amount
				</th>
				<th>
					Claimed At BO Amount
				</th>
				<th>
					<s:text name="Unclaimed" /> After 7 days
				</th>
				<th>
					Receivable from <s:property value="#application.TIER_MAP.AGENT" />
				</th>
			</tr>
			<tr>
				<s:if test="#session.AGENT_PWT_LIST.size>0">
					<s:iterator id="beanCart" value="#session.AGENT_PWT_LIST">
						<s:set name="data" value="%{value}"></s:set>
						<tr class="startSortable">
							<td>
							<!--  	<s:a
							href="bo_dg_WinningExpand_retWise.action?agtOrgId=%{agtOrgId}"
							theme="ajax" targets="result"> -->
							<s:property value="name" />
								</s:a>
								
							</td>
							<td align="right">
								<s:set name="totWinAmt" value="totalWinAmt"></s:set>
								<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("totWinAmt"))%>
							</td>
							<td align="right">
								<s:set name="claimedAmt" value="claimedAmt"></s:set>
								<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("claimedAmt"))%>
							</td>
							<td align="right">
								<s:set name="claimedAtBoAmt" value="claimedAtBoAmt"></s:set>
								<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("claimedAtBoAmt"))%>
							</td>
							<td align="right">
							<s:set name="unclaimedAmtAftr7Days" value="unclaimedAmtAftr7Days"></s:set>
								<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("unclaimedAmtAftr7Days"))%>
							</td>
							<td align="right">
								<s:set name="finalAmt" value="%{#totWinAmt - #claimedAmt}"></s:set>
								<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("finalAmt"))%>
							</td>
						</tr>
					</s:iterator>

				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="center">
							No Records to Process
						</td>
					</tr>
				</s:else>
		</table>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_agtWinningList_Result.jsp,v $ $Revision: 1.3 $
</code>