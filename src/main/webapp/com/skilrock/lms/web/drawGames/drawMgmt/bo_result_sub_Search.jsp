
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
	<table width="823" border="1" cellpadding="3" cellspacing="0"
		bordercolor="#CCCCCC" align="left" style="margin-left: 6px">
		<tr>
			<th colspan="7" align="center">
				<s:text name="label.draw.list"/>
			</th>
		</tr>

	</table>
	<div
		style="height: 160px; overflow-y: scroll; overflow-x: hidden; clear: both"
		id="testDiv">
		<s:hidden name="gameNo" value="%{gameNo}"></s:hidden>
		<s:hidden name="gameName" value="%{gameName}"></s:hidden>

		<table width="800" border="1" cellpadding="5" cellspacing="0"
			id="drawResTbl" bordercolor="#CCCCCC" align="center">
			<tr>
				<th scope="col" align="center">
					<s:text name="label.draw.id"/>
				</th>
				<th scope="col" align="center">
					<s:text name="label.draw.date.time"/>
				</th>
				<th scope="col" align="center" id="drawNameCap">
					<s:text name="DrawName" />
				</th>
				<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
				<th scope="col" align="center" id="eventIdCap">
					<s:text name="EventId" />
				</th>
				</s:if>
				<th scope="col" align="center">
					<s:text name="label.user1.name"/>
				</th>
				<th scope="col" align="center">
					<s:text name="label.user1.res"/>
				</th>
				<s:if test="%{gameName=='KenoFive' || gameName=='KenoFour'}">
					<th scope="col" align="center">
						<s:text name="label.user1.mc.res"/>
					</th>
				</s:if>
				<th scope="col" align="center">
					<s:text name="label.user1.time"/>
				</th>
				<th scope="col" align="center">
					<s:text name="label.user2.name"/>
				</th>
				<th scope="col" align="center">
					<s:text name="label.user2.res"/>
				</th>
				<s:if test="%{gameName=='KenoFive' || gameName=='KenoFour'}">
					<th scope="col" align="center">
						<s:text name="label.user2.mc.res"/>
					</th>
				</s:if>
				<th scope="col" align="center">
					<s:text name="label.user2.time"/>
				</th>
			</tr>

			<s:if test="%{gameName=='KenoFive' || gameName=='KenoFour'}">
				<s:if test="#session.DRAW_SCH_LIST.size>0">
					<s:iterator value="#session.DRAW_SCH_LIST">
						<tr style="cursor: hand;" id="<s:property value="drawId" />"
							onclick="document.getElementById('r<s:property value="drawId" />').checked=true, resSubmitManual('url', 'active', 'resSubmit')">
							<td>
								<input type="radio" name="drawIds"
									id="r<s:property value="drawId" />"
									value="<s:property value="drawId" />" />
								&nbsp;
								<s:property value="drawId" />
							</td>
							<td>
								<s:property value="drawDateTime" />
								<s:hidden name="drawId" value="%{drawId}"></s:hidden>
							</td>
							<td align="center" id="<s:property value="drawname" />">
								<s:property value="drawName" />
							</td>
							<td align="center" id="<s:property value="userId" />">
								<s:property value="userName" />
							</td>
							<td align="center" id="winone<s:property value="drawId" />">
								<s:property value="winResult" />
							</td>
							<td align="center" id="macone<s:property value="drawId" />">
								<s:property value="firstMacResult" />
							</td>
							<td align="center">
								<s:property value="updateTime" />
							</td>
							<td align="center" id="<s:property value="userId2"/>">
								<s:property value="userName2" />
							</td>
							<td align="center" id="wintwo<s:property value="drawId" />">
								<s:property value="winResult2" />
							</td>
							<td align="center" id="mactwo<s:property value="drawId" />">
								<s:property value="secondMacResult" />
							</td>
							<td align="center">
								<s:property value="updateTime2" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="7" align="center">
							<s:text name="msg.no.result"/>
						</td>

					</tr>
				</s:else>
			</s:if>
			<s:else>
				<s:if test="#session.DRAW_SCH_LIST.size>0">
				
					<s:iterator value="#session.DRAW_SCH_LIST">
						<tr style="cursor: hand;" id="<s:property value="drawId" />"
							onclick="document.getElementById('r<s:property value="drawId" />').checked=true, resSubmitManual('url', 'active', 'resSubmit')">
							<td>
								<input type="radio" name="drawIds"
									id="r<s:property value="drawId" />"
									value="<s:property value="drawId" />" />
								&nbsp;
								<s:property value="drawId" />
							</td>
							<td>
								<s:property value="drawDateTime" />
								<s:hidden name="drawId" value="%{drawId}"></s:hidden>
							</td>
							<td align="center">
								<s:property value="drawName" />
							</td>
							<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
							<td  align="center">
								<s:property value="eventId" />
							</td>
							</s:if>
							<td align="center" id="<s:property value="userId" />">
								<s:property value="userName" />
							</td>
							<td align="center" id="winone<s:property value="drawId" />">
								<s:property value="winResult" />
							</td>
							<td align="center">
								<s:property value="updateTime" />
							</td>
							<td align="center" id="<s:property value="userId2"/>">
								<s:property value="userName2" />
							</td>
							<td align="center" id="wintwo<s:property value="drawId" />">
								<s:property value="winResult2" />
							</td>
							<td align="center">
								<s:property value="updateTime2" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="7" align="center">
							<s:text name="msg.no.result"/>
						</td>

					</tr>
				</s:else>
			</s:else>
		</table>
	</div>
	<div id="curTime" style="visibility: hidden;"><%=new java.sql.Timestamp(new java.util.Date().getTime())
					.toString()%></div>
</html>

