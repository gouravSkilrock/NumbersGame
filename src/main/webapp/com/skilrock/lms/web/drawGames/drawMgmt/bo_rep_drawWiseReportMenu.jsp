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
				var critData = "<table>";
				critData += "<tr><td><b>Game Name</b></td><td>"+$("#gameNo option:selected").text()+"</td></tr>";
				critData += "<tr><td><b>Start Date</b></td><td align=left>"+$("#dateOfDraw").val()+"</td></tr>";
				critData += "<tr><td><b>End Date</b></td><td align=left>"+$("#end_date").val()+"</td></tr>";
				critData += "<tr><td><b>State Name</b></td><td>"+$("#stateCode option:selected").text()+"</td></tr>";
				critData += "<tr><td><b>City Name</b></td><td>"+$("#cityCode option:selected").text()+"</td></tr>";
				critData += "<tr><td><b>Distributor Name</b></td><td>"+$("#agentOrgId option:selected").text()+"</td></tr>";
				critData += "</table><br/>";
				var tblData = document.getElementById("tableDataDiv").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
				document.getElementById('tableValue').value = critData+tblData;
				return false;
			}

			function getTableData1() {
				var tblData = document.getElementById("tableDataDiv1").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
						"");
			    
				document.getElementById('tableValue1').value = tblData;
				return false;
			}
			function getAgentList() {
				$("#agentOrgId").empty();
				$("#agentOrgId").append($('<option></option>').val(-1).html("ALL"));
				var state = $('#stateCode').val();
				var city = $('#cityCode').val();
				var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameListStateAndCityWise.action?orgType=AGENT&state="
							+ state + "&city=" + city);
				var firstArr = text.data.split(":");
				var retObj = _id.o("agentOrgId");
				for ( var i = 0; i < firstArr.length - 1; i++) {
					var retNameArray = firstArr[i].split("|");
					var opt = new Option(retNameArray[0].toUpperCase(),
							retNameArray[1]);
					retObj.options[i + 1] = opt;
				}
			
			}
			function getCityList(state) {
				$("#cityCode").html("");
				var _resp = _ajaxCallText(projectName
						+ "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_rep_getCityList.action?stateCode="
						+ state);
				var json = JSON.parse(_resp);
				$('#cityCode').append(
							$('<option></option>').val("ALL").html("ALL"));
				$.each(json.cityMap, function(key, value) {
					$('#cityCode').append(
							$('<option></option>').val(value).html(key));
				});
				
			}
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.draw.game" /> <s:text name="Report" />
				</h3>


				<s:form name="searchgame" action="fetchDrawDataSave"
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
								<s:text name="label.game.name" /> <span class="required">*</span>:
								<s:select name="gameNo" id="gameNo" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="#session.DRAWGAME_LIST" cssClass="option"
									onchange="fetchDrawNames(); _id.i('down1','',this);" cssStyle="margin-left:10px; width:150px"/>
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
												<s:text name="label.start.date" />:&nbsp;
											</label>
											<input type="text" name="dateOfDraw" id="dateOfDraw"
												value="<%=startDate%>" readonly size="12"
												onchange="fetchDrawNames()" style="margin-left:13px; width:145px">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('dateOfDraw'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{depDate}"/>', '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date" />:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12"
												onchange="fetchDrawNames()" style="margin-left:17px; width:145px">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div id="drawNameDiv" style="display: none;">
												<s:text name="label.draw.name"/>:
												<s:select cssStyle="width:150px; margin-left:12px" name="drawName" theme="simple" id="drawName"
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
								<s:text name="label.state" /> Name:
								<s:select cssStyle="margin-left:16px; width:150px" name="stateCode" id="stateCode" theme="simple" list="stateMap" cssClass="option" onchange="getCityList(this.value); getAgentList();"
								headerValue="ALL" headerKey="ALL" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:text name="label.city" /> Name:
								<s:select cssStyle="margin-left:22px; width:150px"name="cityCode" id="cityCode" theme="simple" list="{}" cssClass="option" 
								headerValue="ALL" headerKey="ALL" onchange="getAgentList()"/>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="agtDiv">
									<table>
										<s:select cssStyle="margin-left:5px; width:150px" cssClass="option" name="agentOrgId" id="agentOrgId"
											list="{}" label="%{getText('label.agt.name')}"
											headerValue="%{getText('label.ALL')}" headerKey="-1" 
											onchange="clearDivs();"></s:select>
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
	$RCSfile: bo_rep_drawWiseReportMenu.jsp,v $ $Revision:
	1.3.6.2.2.10.2.3.2.3 $
</code>