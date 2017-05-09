<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_dg_ticketCancellation.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<h3 style="text-align: center">
					<s:text name="label.tkt.cancel.res"/>
				</h3>
				<s:if test="%{#request.CANCEL_BEAN!=null}">
					<s:set name="cancelData" value="#request.CANCEL_BEAN"></s:set>
					<table border="1" cellpadding="1" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<td align="center">
								<s:text name="label.tckt.no"/>
							</td>
							<td>
								<s:property value="#cancelData.ticketNo" />
							</td>
						</tr>
						<tr>
							<td align="center">
								<s:text name="label.ref.amt"/>
							</td>
							<td>
								<s:property value="#cancelData.refundAmount" />
							</td>
						</tr>
						<tr>
							<td align="center">
								<s:text name="label.status"/>
							</td>
							<td>
								<s:property value="#cancelData.errMsg" />
							</td>
						</tr>
					</table>
				</s:if>
				<s:elseif test="%{#request.canTktList!=null}">
					<table border="1" cellpadding="1" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<th align="center">
							<s:text name="label.tckt.no"/>
						</th>
						<th align="center">
							<s:text name="label.ref.amt"/>
						</th>
						<th align="center">
							<s:text name="label.status"/>
						</th>
						<s:iterator value="#request.canTktList" id="cancelData">
							<tr>
								<td align="center">
									<s:property value="#cancelData.ticketNo" />
								</td>
								<td align="center">
									<s:property value="#cancelData.refundAmount" />
								</td>

								<td align="center">
									<s:property value="#cancelData.errMsg" />
								</td>
							</tr>
						</s:iterator>
					</table>
				</s:elseif>
				<s:else>
					<table border="1" cellpadding="1" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<td>
								<s:text name="msg.error.try.again" />
							</td>
						</tr>
					</table>
				</s:else>
				<br />
				<br />
				<a href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/bo_dg_ticketCancellation_Menu.action"><s:text name="msg.want.cancel.more.tkt"/>?</a>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_ticketCancellation_Submit.jsp,v $ $Revision:
	1.1.2.4.8.1 $
</code>