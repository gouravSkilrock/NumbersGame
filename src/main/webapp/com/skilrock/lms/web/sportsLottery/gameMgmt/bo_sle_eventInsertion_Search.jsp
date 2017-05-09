<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript">
			$(document).ready(function() {
				var selectList = document.getElementsByTagName('select');
				var limit = 0;
				for(var x=0; x<selectList.length; x++) {
					if(selectList[x].name.match('Hr') || selectList[x].name.match('Min') || selectList[x].name.match('Sec')) {
						var limit=59;
						if(selectList[x].name.match('Hr'))
							limit=23;
						for(var i=0; i<=limit; i++) {
							//$("#"+selectList[x].name).append(new Option(i, i));
							$("#"+selectList[x].name).append($('<option></option>').val(i).html(i));
						}
					}
				}

				var inputList = document.getElementsByTagName('input');
				for(var x=0; x<inputList.length; x++) {
					if(inputList[x].name.match('optionSet'))
						inputList[x].checked="true";
				}
			});
		</script>
	</head>
	<body>
		<div id="errorDiv" style="color: red;">
			<s:actionmessage />
		</div>
		<div id="wrap">
			<div id="top_form">
				<s:form action="eventInsertionSubmit" onsubmit="return frmSubmit();" theme="simple">
					<s:hidden name="gameId" value="%{gameId}" />
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="left">
								Home Team
							</td>
							<td align="left">
								<s:select id="homeTeam" headerKey="-1" headerValue="--Please Select--"
									name="homeTeam" list="%{teamMasterList}" listKey="teamCode"
									listValue="teamName" cssClass="option"></s:select>
							</td>
							<td align="left">
								Away Team
							</td>
							<td align="left">
								<s:select id="awayTeam" headerKey="-1" headerValue="--Please Select--"
									name="awayTeam" list="%{teamMasterList}" listKey="teamCode"
									listValue="teamName" cssClass="option"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="1">
								Select Events
							</td>
							<td colspan="3">
								<s:checkboxlist name="optionSet" id="optionSet" list="#{'HOME_H':'Home','DRAW_D':'Draw','AWAY_A':'Away','CANCEL_C':'Cancel'}" />
							</td>
						</tr>
						<tr>
							<s:set name="stDate" id="stDate" value="#session.presentDate" />
							<%
								Calendar prevCal = Calendar.getInstance();
								String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
							%>
							<td colspan="1">
								Start Time
							</td>
							<td colspan="3">
								<input type="text" name="eventStartDate" id="eventStartDate"
									value="<%=startDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('eventStartDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
								<s:select list="{}" id="eventStartHr" name="eventStartHr" cssClass="option" />
								<s:select list="{}" id="eventStartMin" name="eventStartMin" cssClass="option" />
								<s:select list="{}" id="eventStartSec" name="eventStartSec" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td colspan="1">
								End Date
							</td>
							<td colspan="3">
								<input type="text" name="eventEndDate" id="eventEndDate"
									value="<%=startDate%>" readonly="readonly" size="12" />
								<input type="button"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('eventEndDate'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
									<s:select list="{}" id="eventEndHr" name="eventEndHr" cssClass="option" />
									<s:select list="{}" id="eventEndMin" name="eventEndMin" cssClass="option" />
									<s:select list="{}" id="eventEndSec" name="eventEndSec" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="submit" value="Submit" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>