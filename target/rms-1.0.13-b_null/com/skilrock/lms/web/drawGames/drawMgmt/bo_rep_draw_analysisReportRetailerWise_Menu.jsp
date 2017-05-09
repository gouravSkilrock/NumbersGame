<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	calendar.set(calendar.HOUR_OF_DAY, 23);
	calendar.set(calendar.MINUTE, 59);
	calendar.set(calendar.SECOND, 59);
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
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.draw.analys"/> <s:text name="Report"></s:text>
				</h3>


				<s:form name="searchgame" action="bo_rep_drawAnaRetWise_Search" onsubmit=" return validateFormEntriesNumberBetAmount()">

					<table border="0" cellpadding="2" cellspacing="0" width="400" style="width:100%;vertical-align:middle; ">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						
						
						<tr>
									
							<td colspan="2">
								<s:text name="label.game.name" /> <span class="required">*</span>:
								<s:select name="gameNo" id="gameNo" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}"
									 list="#session.DRAWGAME_LIST"
									cssClass="option"
									onchange="fetchDrawNames()" />
							</td>
							
							
						</tr>
						
						
						<tr>
							<td colspan="2">
								<table cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="dateOfDraw" id="dateOfDraw" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String dateOfDraw = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date" />
												<span class="required">*</span>:&nbsp;
											</label>
											<input type="text" name="dateOfDraw" id="dateOfDraw"
												value="<%=dateOfDraw%>" readonly size="12" onchange="fetchDrawNames();">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('dateOfDraw'),'yyyy-mm-dd', this, '<%=dateOfDraw%>', '<s:property value="%{depDate}"/>', '')" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						
						
						<tr>
							<td colspan="2">
								<div id="drawNameDiv" style="display: none;">
									<s:text name="label.draw.name"/>:
									<s:select name="drawName" theme="simple" id="drawName"
										headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}" cssClass="option"
										onchange="_id.i('down','');" required="true" />
								</div>
							</td>
						</tr>
						
						
						
						
						<tr>
							<td colspan="2">
								<div id="drawDateTime" style="display: none;">
									 <s:text name="label.draw.time" />
									<s:select name="drawTime" theme="simple" id="drawTime"
										headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}" cssClass="option"
										onchange="_id.i('down','');" />
										
								</div>
							</td>
						</tr>
						
						
						
						
						
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center"
									targets="down" theme="ajax" cssClass="button"/>
							</td>
						</tr>

					</table>
				</s:form>

				<div id="down" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_draw_analysisReportRetailerWise_Menu.jsp,v $ $Revision: 1.3 $
</code>