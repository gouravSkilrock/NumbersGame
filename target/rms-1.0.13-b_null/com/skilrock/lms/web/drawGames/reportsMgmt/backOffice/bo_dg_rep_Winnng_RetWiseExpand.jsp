
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
			
		</div>

	<!--  	<div id="excel" text-align:left;width: 750px;font-size: 9pt">
			<s:a href="bo_rep_DGFullReport_ExpExcel.action?orgType=BO">Export As Excel</s:a>
		</div>  -->

		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			
			<tr>
				<th rowspan="2" width="10%" nowrap="nowrap">
					<s:property value="#application.TIER_MAP.RETAILER" />
					Name
				</th>
				<th>
					Total Winning Amount
				</th>
				<th>
					Claimed Amount
				</th>
				<th>
					Unclaimed After 7 days
				</th>
			</tr>
			<tr>
				<s:if test="#session.RET_PWT_LIST.size>0">
					<s:iterator id="beanCart" value="#session.RET_PWT_LIST">
						<s:set name="data" value="value"></s:set>
						<tr class="startSortable">
							<td>
							<s:property value="name" />
							</td>
							<td align="right">
								<s:property value="totalWinAmt" />
							</td>
							<td align="right">
								<s:property value="claimedAmt" />
							</td>
							<td align="right">
								<s:property value="unclaimedAmtAftr7Days" />
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
	$RCSfile: bo_dg_rep_Winnng_RetWiseExpand.jsp,v $ $Revision: 1.2 $
</code>