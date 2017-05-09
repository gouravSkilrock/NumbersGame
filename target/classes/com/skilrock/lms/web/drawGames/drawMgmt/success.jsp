
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<s:head theme="ajax" />
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
			<s:if test="#session.RESULT_LIST.isSuccess()">
				<table width="800" border="1" cellpadding="4" cellspacing="0"
					id="drawResTbl" bordercolor="#CCCCCC" align="center">
					<tr>
						<th colspan="5" align="center" style="color: red">
							<s:property value="#session.RESULT_LIST.responseMessage" />
							<s:text name="label.list"/>
						</th>
					</tr>
					<tr>

						<th width="15%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="25%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th width="25%" scope="col" align="center">
							<s:text name="label.draw"/> <s:text name="Freeze" /> <s:text name="label.time"/>
						</th>
						<th width="25%" scope="col" align="center">
							<s:text name="label.status"/>
						</th>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
							<th width="25%" scope="col" align="center">
								<s:text name="EventId" />
							</th>
						</s:if>
					</tr>
					<s:iterator value="#session.RESULT_LIST.drawScheduleList">
						<tr id="<s:property value="draw_id" />">
							<td>
								&nbsp;
								<s:property value="draw_id" />
							</td>
							<td>
								<s:property value="draw_time" />
							</td>
							<td>
								<s:property value="updatedFreezeTime" />
							</td>
							<td>
								<s:property value="updateMsg" />
							</td>
							<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
								<td>
									<s:property value="eventId" />
								</td>
							</s:if>
						</tr>
					</s:iterator>
					<tr>
						<td colspan="5" align="right">
						<a
					href="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/drawMgmt/drawMenu.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"><s:text name="msg.bak.to.srch.res"/> </a>
						</td>
					</tr>
				</table>
			</s:if>
			<s:else>
				<table>
					<tr>
						<th style="color: red">
							<s:property value="#session.RESULT_LIST.responseMessage" />
						</th>
					</tr>
				</table>

			</s:else>
		




	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: success.jsp,v $
$Revision: 1.4 $
</code>