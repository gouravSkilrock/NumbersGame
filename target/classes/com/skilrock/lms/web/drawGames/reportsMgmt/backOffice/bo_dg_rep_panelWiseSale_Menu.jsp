<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	calendar.set(calendar.HOUR_OF_DAY, 23);
	calendar.set(calendar.MINUTE, 59);
	calendar.set(calendar.SECOND, 59);
%>
<%
	response.setDateHeader("Expires", 0);
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.draw.wise.sale.panel" />
				</h3>
				<s:form name="searchgame" action="PanelWiseSaleReport_Result"
					onsubmit="return validatePanelDates()">
					<table border="0" cellpadding="2" cellspacing="0" width="400">
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td align="left">
								<s:select name="gameNo" id="gameNo" key="label.game.name"
									list="%{gameDetails}" cssClass="option"
									onchange="_id.i('down', '') ,_id.i('dates', '') " required="true" />
							</td>
						</tr>
						<tr>
							<td>
								<s:set name="stDate" id="stDate" value="%{presentDate}" />
								<%
									Calendar prevCal = Calendar.getInstance();
									String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");

									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
									Date archivingDate = dateFormat.parse(CommonMethods.getLastArchDate());
									Calendar archCal = Calendar.getInstance();
									archCal.setTime(archivingDate);
									archCal.add(Calendar.DATE, 1); 
									String lastDate = dateFormat.format(archCal.getTime());
								%>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td align="right">
								<label class="label">
									<s:text name="label.start.date" />
									<font color='red'> <span>*</span></font>:&nbsp;
								</label>
							</td>
							<td align="left">
								<input type="text" name="start_date" id="start_date"
									value="<%=startDate%>" readonly size="12" onchange="_id.i('down', '') ,_id.i('dates', '') ">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(_id.o('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<%=lastDate%>', '<%=startDate%>')" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<div id="endDateDiv">
									<label class="label">
										<s:text name="label.end.date" />
										<font color='red'> <span>*</span></font>:&nbsp;
									</label>
							</td>
							<td align="left">
								<input type="text" name="end_Date" id="end_date"
									value="<%=startDate%>" readonly size="12" onchange="_id.i('down', '') ,_id.i('dates', '') ">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(_id.o('end_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<%=lastDate%>', '<%=startDate%>')" />
									</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button" notifyTopics="calcTotal()" />
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
	$RCSfile: bo_dg_rep_panelWiseSale_Menu.jsp,v $ $Revision: 1.3 $
</code>