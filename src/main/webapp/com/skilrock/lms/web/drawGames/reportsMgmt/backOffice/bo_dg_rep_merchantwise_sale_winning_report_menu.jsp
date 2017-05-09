<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet"	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"	type="text/css" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>var projectName="<%=request.getContextPath()%>"</script>
	<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script>

			function validateDate(){
					$("#result").html('');
				var startDate=$("#start_date").val();
				var endDate=$("#end_Date").val();
				if(Date.parse(startDate)>Date.parse(endDate)){
					$("#date_e").html('End date should be greater than start date');
					return false;
				}
				else{
					$("#date_e").html('');
					return true;
				}				
			}
	
	</script>

</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="USSD Summary Report" />
				</h3>


				<s:form action="bo_dg_rep_merchantwise_sale_winning_report_search">

					<table border="0" cellpadding="2" cellspacing="0" width="400" style="width:100%;vertical-align:middle; ">
						<tr>
								<td align="left" colspan="2">
									<div style="color: red; text-align: center" id="date_e"></div>
								</td>
						</tr>
						<tr>
								<td colspan="2">
									<table cellpadding="3"
										cellspacing="0" border="0">
										<s:set name="stDate" id="stDate" value="#session.presentDate" />
										
										<tr>
												<td>
													<label class="label">
														<s:text name="label.start.date" />
														<span>*</span>:&nbsp;
													</label>
													<input type="text" name="start_date" id="start_date"
														value="<s:property value="#session.presentDate"/>" readonly size="12">
													<input type="button"
														style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
														onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this,'<s:property value="#session.presentDate"/>', false, '')" />
												</td>
										</tr>
										<tr>
												<td>
													<label class="label">
														<s:text name="label.end.date" />
														<span>*</span>:&nbsp;
													</label>
													<input type="text" name="end_Date" id="end_Date"
														value="<s:property value="#session.presentDate"/>" readonly size="12">
													<input type="button"
														style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
														onclick="displayCalendar(document.getElementById('end_Date'),'yyyy-mm-dd', this,'<s:property value="#session.presentDate"/>', false, '')" />
												</td>
										</tr>
									</table>
								</td>
						</tr>
						<tr>
								<td colspan="2">
										<s:submit id="submit" name="search" key="btn.srch" align="center"
											targets="down" theme="ajax" cssClass="button" onclick="return validateDate();"/>
								</td>
						</tr>

					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
				<br />
				<br />
				<div id="result" style="text-align: center"></div>
			</div>
		</div>

</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_merchantwise_sale_winning_report_menu.jsp,v $ $Revision: 1.3 $
</code>
