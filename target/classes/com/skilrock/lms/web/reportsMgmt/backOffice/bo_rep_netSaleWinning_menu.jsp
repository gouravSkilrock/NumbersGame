<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap_new">
			<div id="top_form_new">
				<s:hidden id="main-heading"><s:text name=""/></s:hidden>
				<h3>
					<s:text name="label.net.sale.win.rep" />
				</h3>
				<s:form id="mForm" action="bo_rep_netSaleWinning_Search"
					onsubmit="return validateForm();">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv" style="color: red;"></div>
							</td>
						</tr>
						<tr>
							<s:set name="stDate" id="stDate" value="#session.presentDate" />
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
							<td align="right">
								<s:text name="label.start.date" />
								<span>*</span>:&nbsp;
							</td>
							<td>
								<input type="text" name="startDate" id="start_date"
									value="<%=startDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>','<s:property value="%{depDate}" />', '<%=startDate%>')" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="label.end.date" />
								<span>*</span>:&nbsp;
							</td>
							<td>
								<input type="text" name="endDate" id="end_date"
									value="<%=startDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>','<s:property value="%{depDate}" />', '<%=startDate%>')" />
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<s:select key="label.slct.report.type" id="reportType"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									name="reportType"
									list="#{'AGENT':getText('label.agt.wise'),'RETAILER':getText('label.ret.wise')}"
									cssClass="option"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="agt" style="display: none; margin-left: 53px;">
									<table>
										<tr>
											<td>
												<s:select headerKey="-1" key="label.slct.agnt.org"
													headerValue="%{getText('label.ALL')}" name="agentOrgId"
													id="agentOrgId" list="{}" listKey="key" listValue="%{}"
													cssClass="option">
												</s:select>
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<s:submit name="submit" id="submit" key="btn.submit"
									align="right" theme="ajax" targets="down" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
			</div>
		</div>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/netSaleWinRep.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_Excel.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_PDF.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_netSaleWinning_menu.jsp,v $ $Revision: 1.3 $
</code>