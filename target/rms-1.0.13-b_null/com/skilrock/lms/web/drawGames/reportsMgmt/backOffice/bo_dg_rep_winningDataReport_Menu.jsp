<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false"/>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				validateForm = function() {
					$(".errorSpan").html("");
					var errorMessage = "";
					if($("#gameId :selected").index() == 0) {
						errorMessage += "Please Select Game.<br/>";
					}

					if(errorMessage == "") {
						$("#errorMessageDiv").html("");
						return true;
					} else {
						$("#errorMessageDiv").html(errorMessage);
						return false;
					}
				}
			});
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Winning Data Report
				</h3>
				<s:div id="errorMessageDiv" cssStyle="color:red; "></s:div>
				<s:form action="bo_dg_rep_winningDataReport_Export" onsubmit="return validateForm();" theme="simple">
					<%
						Calendar prevCal = Calendar.getInstance();
						String drawDate = null;
						String currentDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
						if(request.getParameter("drawDate")==null) {
							drawDate = currentDate;
						} else {
							drawDate = request.getParameter("drawDate");
						}
					%>
					<table>
						<tr>
							<td>Select Game</td>
							<td>
								<s:select name="gameId" id="gameId" list="gameMap" headerKey="-1" headerValue="--Please Select--"
									listKey="key" listValue="value.gameName" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td><label class="label">Start Date<span>*</span>:&nbsp;</label></td>
							<td>
								<input type="text" name="drawDate" id="drawDate"
									value="<%=drawDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('drawDate'),'yyyy-mm-dd', this, '<%=drawDate%>', '<s:property value="%{archDate}"/>', '<%=currentDate%>')" />
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><s:submit value="Export as Excel" /></td>
						</tr>
					</table>
					<br/>
					<center>
						<s:if test="hasActionErrors()">
							<s:iterator value="actionErrors">
								<span style="color:red; " class="errorSpan"><s:property escape="false" /></span>
							</s:iterator>
						</s:if>
					</center>
					<center>
						<s:actionmessage/>
					</center>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_winningDataReport_Menu.jsp,v $ $Revision: 1.3 $
</code>