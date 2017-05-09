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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_dg_ticket_validate.js"></script>
		<script>
			var projectName="<%=request.getContextPath()%>"
			function getTableData() {
				var tblData = document.getElementById("tableDataDiv").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
						"");
			    
				document.getElementById('tableValue').value = tblData;
				return false;
			}
			function getTableData1() {
				var tblData = document.getElementById("tableDataDiv1").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
						"");
			    
				document.getElementById('tableValue1').value = tblData;
				return false;
			}
	
		</script>
	</head>

	<!-- <body onload="getAgentList();"> -->
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.promo.draw.game.rep" />
				</h3>


				<s:form name="searchgame" action="fetchPromoDrawDataSearch"
					onsubmit="return validateDates()">

					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:text name="label.game.name" />:
								<s:select name="gameNo" id="gameNo" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="#session.DRAWGAME_LIST" cssClass="option"
									onchange="fetchDrawNames(); _id.i('down1','',this);" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date" /><span>*</span>:&nbsp;
											</label>
											<input type="text" name="dateOfDraw" id="dateOfDraw"
												value="<%=startDate%>" readonly size="12"
												onchange="fetchDrawNames()">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('dateOfDraw'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{depDate}"/>', '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date" /><span>*</span>:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12"
												onchange="fetchDrawNames()">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div id="drawNameDiv" style="display: none;">
												<s:text name="label.draw.name"/>:
												<s:select name="drawName" theme="simple" id="drawName"
													headerKey="ALL" headerValue="%{getText('label.ALL')}" list="{}"
													cssClass="option" onchange="clearDivs();" />
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="agtDiv">
									<table>
										<%-- <s:select cssClass="option" name="agentOrgId" id="agentOrgId"
											list="{}" label="%{getText('label.agt.name')}"
											headerValue="%{getText('label.ALL')}" headerKey="-1" required="true"
											onchange="clearDivs();"></s:select> --%>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>

					</table>
				</s:form>

				<div id="down" style="text-align: center"></div>
				<div id="down1" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawWisePromoReportMenu.jsp,v $ $Revision:
	1.3.6.2.2.10.2.3.2.3 $
</code>