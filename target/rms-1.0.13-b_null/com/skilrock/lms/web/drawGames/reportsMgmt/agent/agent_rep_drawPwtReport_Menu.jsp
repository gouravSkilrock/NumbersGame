<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@page import="com.skilrock.lms.common.Utility"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />

		<script>
		  var projectName="<%=request.getContextPath()%>"
		</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/validatesDate.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="PWT"/> <s:text name="Report"/> 
				</h3>
				<s:form action="agent_rep_drawPwtReport_Search" onsubmit="return validateDates()">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<s:hidden name="agentOrgId" value="%{#session.USER_INFO.userOrgId}"></s:hidden>
						<tr>
							<td align="center" colspan="2">
								<s:select name="reportType" label="Reports Type "
									list="#{'Game Wise':'Game Wise','Retailer Wise':#application.TIER_MAP.RETAILER + ' Wise'}"
									cssClass="option"
									onchange="clearDiv()" />
							</td>
						</tr>
						<%
							Calendar prevCal = Calendar.getInstance();
							String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String) session.getAttribute("date_format"));

							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							Calendar lastDateCalendar = Calendar.getInstance();
							int lastMonthSpan = Integer.parseInt(Utility.getPropertyValue("LAST_MONTH_SPAN_REPORT"));
							lastDateCalendar.add(Calendar.MONTH, -lastMonthSpan); 
							String lastDate = dateFormat.format(lastDateCalendar.getTime());
							System.out.println("lastDate - "+lastDate);
						%>
						<tr>
							<td align="right">
								<label class="label" ><s:text name="label.start.date" /><span>*</span>:&nbsp;</label>
							</td>
							<td>
					    		<input  type="text" name="start_date" id="start_date" value="<%=startDate %>" readonly size="12" />
					    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'dd-mm-yyyy', this, '<%=startDate %>', '<%=lastDate %>', '<%=startDate %>')" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<label class="label"><s:text name="label.end.date" /><span>*</span>:&nbsp;</label>
							</td>
							<td>
								<input  type="text" name="end_Date" id="end_date" value="<%=startDate %>" readonly size="12" />
    							<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'dd-mm-yyyy', this, '<%=startDate %>', '<%=lastDate %>', '<%=startDate %>')" />
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button"
									onclick="clearDiv()" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agent_rep_drawPwtReport_Menu.jsp,v $
$Revision: 1.3 $
</code>