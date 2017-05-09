
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.NumberFormat"%>
<%
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	calendar.set(calendar.HOUR_OF_DAY, 23);
	calendar.set(calendar.MINUTE, 59);
	calendar.set(calendar.SECOND, 59);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/searchGoodCauseAndVat.js"></script>
	</head>
	<script type="text/javascript">
			function validateDates() {			
				var isInValid = false;
				var startDate = _id.o("start_date").value;
				var endDate =  _id.o("end_date").value;	
				if (startDate == "" || endDate == "") {
					isInValid = true;
					_id.o("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";			           
				} else {
					if (endDate < startDate) {
						isInValid = true;
						_id.o("dates").innerHTML = "<font color = 'red'>end date should be greater then or equals to start date</font>";          
			        }
				}				
				if (isInValid) {				
					return false;
				}
				_id.o("dates").innerHTML = "";
				return true;			
			}
	</script>
	<link type="text/css" rel="stylesheet"
		href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
		media="screen" />
	<script>var projectName="<%=request.getContextPath()%>"</script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>


	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Service Wise
					<s:text name="Good_Cause" />
					And Vat Submit Report
				</h3>


				<s:form id="searchSubmit"
					action="bo_act_gudCauseAndVatSubmit_Search"
					onsubmit="return validateFormEntries();">

					<input type="hidden" style="background-color: yellow"
						name="buttonValue" id="hiddenInput" value="Search" />
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="error1"></div>
							</td>
						</tr>
						<tr>
							<td align="center">
								<s:select name="serviceName" id="serviceName" headerKey="-1"
									headerValue="--Please Select--" label="Select Service"
									list="#session.serviceNameMap" cssClass="option"
									required="true" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="error2"></div>
							</td>
						</tr>
						<tr>
							<td align="center">
								<s:select name="commissionType" id="commissionType" headerKey="-1"
									headerValue="--Please Select--" label="Select Type"
									list="#session.commissionTypeMap" cssClass="option"
									required="true" />
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<align="center"><tr>
							<td colspan="2" align="left">
								<table align="left" width="400px" cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									<tr align="left">
										<td align="center">
											<label class="label">
												Start Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="start_date" id="start_date"
												value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
									</tr>
									<tr>
										<td align="center">
											<label class="label">
												End Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
								</table>
							</td>
						</tr></align>

						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit name='button' id="searchbutoon" value="Search"
										align="right" targets="result" theme="ajax" cssClass="button"
										onclick="setInputHiddenValue(this.value)" />
								</table>
							</td>
						</tr>
					</table>

				</s:form>
				<div id="result">
				</div>

				<div id="result1" style="position: absolute; top: 350px">

				</div>


			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_gudCause_Vat_Submit_Menu.jsp,v $ $Revision:
	1.1.2.1.6.4 $
</code>