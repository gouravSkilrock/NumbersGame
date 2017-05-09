<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>


		<s:head theme="ajax" debug="false" />
	</head>
	<body>

		<div id="wrap">
			<h3>
				<center> <s:property value="#application.TIER_MAP.AGENT" />
				<s:text name="label.bal.dist" /></center>
			</h3>
			<%
				double totAmt = 0;
				double totClmAmt = 0;
				double totUnClmAmt = 0;
				double totBal = 0.0;
			%>

			<s:iterator value="#session.BOLimitDistribList">
				<s:if test="%{key==userOrgId}">
					<s:set name="agentBean" value="value"></s:set>
				</s:if>
			</s:iterator>
			<table width="95%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<th>
						<s:property value="#application.TIER_MAP.AGENT" />
						<s:text name="label.name" />
					</th>
					<th>
						<s:text name="label.usable.limit" />
					</th>
				</tr>
				<tr>
					<td width="10%" align="left" nowrap="nowrap">
						<s:property value="%{#agentBean.name}" />
					</td>
					<td width="10%" align="left" nowrap="nowrap">
						<s:property value="getText('{0,number,#,##0.00}',{#agentBean.liveBal})"/>
					</td>
				</tr>
			</table>
			<br />

			<table width="95%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<th colspan="4" align="center">
						<h3>
							<s:property value="#application.TIER_MAP.RETAILER" />
							's
							<s:text name="label.list" />
						</h3>
					</th>
				</tr>
				<tr>
					<th>
						<s:text name="label.sNo" />
					</th>
					<th>
						<s:property value="#application.TIER_MAP.RETAILER" />
						<s:text name="label.name" />
					</th>
					<th>
						<s:text name="label.usable.limit" />
					</th>
				</tr>



				<s:if test="#session.agtBalDistMap.size>0">
					<s:iterator value="#session.agtBalDistMap" status="taskIndex">
						<s:set name="retBean" value="value" />
						<s:set name="availableCredit" value="#retBean.availableCredit" />
						<%
							if ((Double) pageContext.getAttribute("availableCredit") > 0)
										totAmt = totAmt
												+ (Double) pageContext
														.getAttribute("availableCredit");
						%>
						<tr>
							<td width="10%">
								<s:property value="#taskIndex.index+1" />
							</td>
							<td width="45%" align="left" nowrap="nowrap">
								<s:property value="#retBean.orgName" />
							</td>
							<td width="50%" align="right" nowrap="nowrap"><%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("availableCredit"))%></td>
						</tr>
					</s:iterator>
					<tr>
						<td>
							&nbsp;
						</td>
						<td width="10%" align="left" nowrap="nowrap">
							<h2>
								<s:text name="label.total.dist" />
							</h2>
						</td>
						<td width="10%" align="right" nowrap="nowrap">
							<h3><%=FormatNumber.formatNumberForJSP(totAmt)%></h3>
						</td>
					</tr>

				</s:if>
				<s:else>
					<tr>
						<td colspan="9" align="center">
							<s:text name="msg.no.record.process" />
						</td>
					</tr>
				</s:else>

			</table>
			<br />
			<table width="100%">
				<th align="center">
					<h2>
						<s:if test="%{#agentBean.distributableBal>0}">
							<s:text name="label.you.can.dist" />
								<s:property value="getText('{0,number,#,##0.00}',{#agentBean.distributableBal})"/>
							<s:text name="label.amt.only" />
						</s:if>
						<s:else>
							<s:text name="label.you.can.dist.any.amt" />
						</s:else>
					</h2>
				</th>
			</table>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_agtBalDistribution_Result.jsp,v $ $Revision: 1.3 $
</code>