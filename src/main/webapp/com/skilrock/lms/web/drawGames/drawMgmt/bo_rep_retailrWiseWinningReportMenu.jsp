<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

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
	</head>

	<body onload="getAgentList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap_new">
			<div id="top_form_new">
				<h3 id="main-heading">
					<s:text name="label.Retaier.wise.winning.tkts.report" />
				</h3>


				<s:form name="searchgame" action="bo_rep_retailrWiseWinningReportSearch"
					onsubmit="return validateDatesForm()">
					

					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
										String date = Util.getCurrentTimeString();															
									%>
									<s:hidden id="cur_date" value="<%=date.split(" ")[0]%>" />
									<s:hidden id="cur_time" value="<%=date.split(" ")[1]%>" />
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date" /><span>*</span>:&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
											<input type="text" name="start_date" id="start_date"
												value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{depDate}"/>', '<%=startDate%>')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date" /><span>*</span>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '<%=startDate%>')" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="label.game.name"/><span>*</span>:&nbsp;
								<s:select name="gameNo" id="gameNo" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="#session.DRAWGAME_LIST" cssClass="option"
									onchange="_id.i('down1','',this)" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:text name="label.agt.name" />:&nbsp;&nbsp;&nbsp;
								<s:select cssClass="option" name="agentOrgId" id="agentOrgId" theme="simple"
								list="{}" headerValue="%{getText('label.ALL')}" headerKey="-1" required="true"
								onchange="clearDivs();"></s:select>
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

				<div id="down" style="text-align: center">
				</div>
			</div>
		</div>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_Excel.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/Export_PDF.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retailrWiseWinningReportMenu.jsp,v $ $Revision:
	1.3.6.2.2.10.2.3.2.3 $
</code>