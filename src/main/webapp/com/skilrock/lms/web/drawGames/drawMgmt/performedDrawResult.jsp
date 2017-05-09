
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<s:head theme="ajax" />
	</head>
	<table width="810" border="1" cellpadding="3" cellspacing="0"
		bordercolor="#CCCCCC" align="center" style="margin-left: 6px">
		<div style="color: red; text-align: left;">
			<s:actionmessage />
		</div>
		<tr>
			<th colspan="8" align="center">
				<s:text name="label.draw.list"/>
			</th>
		</tr>
		<tr>
			<th width="67px" scope="col" align="center">
				<s:text name="label.draw.id"/>
			</th>
			<th width="125px" scope="col" align="center">
				<s:text name="label.draw.time"/>
			</th>
			<th width="126px" scope="col" align="center">
				<s:text name="label.draw"/>
				<s:text name="Freeze" />
				<s:text name="label.time"/>
			</th>
			<th width="88px" scope="col" align="center">
				<s:text name="label.draw.status"/>
			</th>
			<th width="100px" scope="col" align="center" id="drawNameCap">
				<s:text name="DrawName" />
			</th>
			<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
				<th width="112px" scope="col" align="center" id="eventIdCap">
					<s:text name="EventId" />
				</th>
			</s:if>
			<th width="94px" scope="col" align="center">
				<s:text name="label.res"/>
			</th>
			<th scope="col" align="center">
				<s:text name="label.check.ppr"/>
			</th>
		</tr>
	</table>
	<div
		style="height: 125px; overflow-y: scroll; overflow-x: hidden; clear: both"
		id="testDiv">

		<table width="810" border="1" cellpadding="5" cellspacing="0"
			id="drawResTbl" bordercolor="#CCCCCC" align="center"
			style="margin-left: 6px">

			<s:if test="#session.DRAW_SCH_LIST.size>0">
				<s:hidden id="privName" value="%{priv}" />
				<s:iterator value="#session.DRAW_SCH_LIST">
					<tr style="cursor: hand;" id="<s:property value="drawId" />"
						onclick="document.getElementById('r<s:property value="drawId" />').checked=true,checkedDraw('url', 'active', 'ppr')">
						<td width="67px">
							<input type="radio" name="drawIds"
								id="r<s:property value="drawId" />"
								value="<s:property value="drawId" />" />
							&nbsp;
							<s:property value="drawId" />
						</td>
						<td width="125px" align="center" id="<s:property value="drawId" />drawDateTime">
							<s:property value="drawDateTime" />
						</td>
						<td width="126px" align="center"
							id="<s:property value="drawId" />freezeDateTime">
							<s:property value="freezeDateTime" />
						</td>
						<td width="88px" align="center">
							<s:property value="drawStatus" />
						</td>
						<td width="100px" align="center">
							<s:property value="drawName" />
						</td>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
							<td width="112px" align="center">
								<s:property value="eventId" />
							</td>
						</s:if>
						<td width="94px" align="center">
							<s:property value="winningResult" />
						</td>
						<td align="center">
							<s:property value="checkPPR" />
						</td>

					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="4" align="center">
						<s:text name="msg.no.result"/>
					</td>

				</tr>
			</s:else>

		</table>
	</div>
	<div id="curTime" style="visibility: hidden;"><%=new java.sql.Timestamp(new java.util.Date().getTime())
					.toString()%></div>
</html>

