<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);
%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<style>
			.retCount{
				font-size: 12px;
				font-weight: bold;
			}
		</style>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
			<h3>
				Weekly Sale and PWT
			</h3>
			<br/>
			<table width="500px" border="0" cellpadding="3" cellspacing="0" align="center">
				<tr>
					<td width="30%" align="left">
						<span class="retCount">Number of <s:property value="#application.TIER_MAP.RETAILER" />s: &nbsp</span>
					</td>
					<td width="70%" align="left">
						<span class="retCount"><s:property value="#session.RETAILER_COUNT" /></span>
					</td>
				</tr>
			</table>
			<br/>
				<table width="500px" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th colspan="8" align="center">
							Weekly Draw <s:text name="Report"/>
						</th>
					</tr>
					<tr>
						<th>
							Date
						</th>
						<th>
							Sale
						</th>
						<th>
							<s:text name="PWT"/>
						</th>
					</tr>

					<s:if test="#session.WEEK_REPORT.size>0">

						<s:iterator id="dailyBean" value="%{#session.WEEK_REPORT}">
							<tr class="startSortable">
								<td align="left">
									<s:property value="date" />
								</td>
								<td align="left">
									<s:set name="netsaleJsp" value="%{netsale}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netsaleJsp")) %>
								</td>
								<td align="right">
									<s:set name="netPwtJsp" value="%{netPwt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netPwtJsp")) %>
								</td>

							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>

				</table>




			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_weeklyReport.jsp,v $
$Revision: 1.2 $
</code>