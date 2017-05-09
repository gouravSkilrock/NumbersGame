
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
	<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/agent/js/agt_rep_paymentLedger.js"></script>

	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	
	</head>

	<body onload="getRetailerUserList()">
	
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

			 
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.payment.ledger"/>
				</h3>

          <s:form action="agt_rep_payment_ledger_Search" onsubmit="return validateDates()">

					<table border="0" cellpadding="2" cellsapacing="0">
						<tr>
							<td>
								<div id="errorId"></div>
							</td>
						</tr>
					</table>
					<table border="0" cellpadding="2" cellsapacing="0">

						<tr>
							<td colspan="2">
								<s:actionerror />
								<s:fielderror />
							</td>
						</tr>
								<tr>
									<td colspan="2">
										<span id="list1" onclick="changeValue()"></span>


									</td>
								</tr>
						
							
				<tr id="selType">
								<td width="100">
			
								</td>
								<td>
									<s:select cssClass="option" key="label.ret.name" name="retOrgId" id="retOrgId"
										headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}" 
										
										/>
								</td>
							</tr>

		<tr>
							<td align="center" colspan="2">
								<s:select name="totaltime" label="%{getText('Report')} %{getText('label.duration')} "
									list="#{'Current Day':getText('label.cur.day'),'Date Wise':getText('label.datewise')}"
									cssClass="option"
									onchange="setDateField(this.value),clearDiv()" />
							</td>
						</tr>
						<tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<%
									Calendar prevCal = Calendar.getInstance();
										String startDate = CommonMethods
												.convertDateInGlobalFormat(new java.sql.Date(prevCal
														.getTimeInMillis()).toString(), "yyyy-mm-dd",
														(String) session.getAttribute("date_format"));
								%>
								<s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
								<div id="date"
									style="display: none; text-align: left; width: 60%">
									<div id="dates"></div>
									<s:div id="newDates" theme="ajax"
										href="rep_common_fetchDateNew.action">
									</s:div>
								</div>
							</td>

						</tr>
						
						<tr>
						<td align="right">
						
											
							<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button"
									onclick="return clearDiv();" />
						
					
							</td></tr>					</table>
				</s:form>
						<div id="down"></div>		
				<div id="result" style="overflow-x:auto;overflow-y:hidden;"></div>
			</div>
		</div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_paymentLedger_Report_Menu.jsp,v $
$Revision: 1.3 $
</code>