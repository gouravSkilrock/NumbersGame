<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
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
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

		<script>
			var projectName="<%=request.getContextPath()%>"
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/bo_rep_drawSale.js"></script>
		
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

		<script>
			function getCityList(state) {
						$("#cityCode").html("");
						var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_rep_getCityList.action?stateCode=" + state);
						var json = JSON.parse(_resp);
						$('#cityCode').append($('<option></option>').val("ALL").html("--ALL--"));
						$.each(json.cityMap, function(key, value) {
							$('#cityCode').append($('<option></option>').val(value).html(key));
						});
					}
					function getAgentList() {
			$("#agentOrgId").empty();
			$("#agentOrgId").append($('<option></option>').val(-1).html("--Please Select--"));
			var state = $('#stateCode').val();
			var city = $('#cityCode').val();
			var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameListStateAndCityWise.action?orgType=AGENT&state="+state+"&city="+city);
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
		</script>

	</head>
	<body onload="getAgentList()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.sale"/> <s:text name="Report"/>
				</h3>
				<s:form action="bo_rep_drawSaleReport_Search" onsubmit="return validateData(),validateDates()">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="reportType" id="reportType" label="%{getText('Report')} %{getText('label.type')} "
									list="#{'Game Wise':getText('label.gamewise'),'Agent Wise':getText('label.agt.wise'),'Retailer Wise':getText('Retailer')+' '+ getText('label.wise')}"
									cssClass="option" onchange="clearDiv(),showAgents(this.value)" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="totaltime" key="label.reports.duration"
									list="#{'Current Day':getText('label.cur.day'),'Date Wise':getText('label.datewise')}"
									cssClass="option"
									onchange="setDateField(this.value),clearDiv()" />
							</td>
						</tr>
<tr class="stateDiv">
							<td  colspan="2">
								<!-- <div id="stateDiv" style="display: none"> -->
									<s:select name="stateCode" id="stateCode" label="State Name" list="stateMap" cssClass="option" onchange="getCityList(this.value); getAgentList()"
									headerValue="--ALL--" headerKey="ALL"  cssStyle="margin-left:10px; width:130px"/>
								<!-- </div> -->
							</td>
						</tr>
						<tr class="cityDiv">
							<td align="center" colspan="2">
								<!-- <div id="cityDiv" style="display: none"> -->
									<s:select name="cityCode" id="cityCode" label="City Name" list="{}" cssClass="option" 
									headerValue="--ALL--" headerKey="ALL" onchange="getAgentList()" cssStyle="margin-left:10px; width:130px"/>
								<!-- </div> -->
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="agtDiv" style="display: none">
									<table>
										<s:select cssClass="option" name="agentOrgId"
											id="agentOrgId" list="{}"
											label="%{#application.TIER_MAP.AGENT} %{getText('label.org')}"
											headerValue="%{getText('label.please.select')}" headerKey="-1" required="true"></s:select>
									</table>
								</div>
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
									<s:div id="newDates" theme="ajax"
										href="%{request.getContextPath()}/com/skilrock/lms/web/reportsMgmt/rep_common_fetchDate_specific.action">
									</s:div>
								</div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.srch" align="right"
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
$RCSfile: bo_rep_drawSaleReport_Menu.jsp,v $
$Revision: 1.3 $
</code>