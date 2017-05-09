<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/activityRep.js" />
		<script></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
	<body onload="getAgentList(),fetchRetActivityHistory()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" />
					<s:text name="label.actvty.history"/> <s:text name="Report"/>
				</h3>


				<s:form name="retActivity" id="retActivity" action="bo_rep_retActivity_History" method="POST">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">
					
						<tr>
							<td align="right">
								<i><s:text name="label.select"/> <s:text name="Report"/> <s:text name="label.type"/></i>
							</td>
							<td nowrap="nowrap" colspan="2">
								<s:select theme="simple" cssClass="option" name="reportType" id="reportType" list="#{'history':'history'}" onchange="return fetchActData(document.getElementById('agtOrgName').value)"></s:select>
							</td>
						</tr>
						<tr>				
						<td colspan="1"><div style="color: red; text-align: center" id="serviceName_e"></div></td>
						</tr>
						<tr>
							<td align="right">
								<s:select name="serviceName" id="serviceName" headerKey="-1"
									headerValue="--Please Select--" key="label.slct.service"
									list="#session.serviceNameMap" cssClass="option" required="true" />
							</td>
						</tr>
						<tr><td colspan="2"><div id="dates"></div></td></tr>
						<tr> 			
							<td colspan="2" align="center">
								<div id="date" style="display: none;">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />				
									<s:div theme="ajax" href="rep_common_fetchDate.action">
									</s:div>
								</div>
							</td>
						</tr>
						<tr>
							<td id="searchButtonId"><s:submit name="search" key="btn.srch" align="center"  targets="histReportDiv" theme="ajax" cssClass="button" onclick="return verifyServiceName()"/></td>
						</tr> 
						<tr>
							<td align="right">
								<i><s:text name="label.select" /> <s:property value="#application.TIER_MAP.AGENT" />
									<s:text name="label.org" /> :</i>
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="agtOrgName" cssClass="option"
									list="{}" name="organization_Name" emptyOption="false"
									headerKey="-1" headerValue="ALL"
									onchange="fetchActData(this.value)" />
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<i><s:text name="label.location" /> :</i>
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="location" cssClass="option"
									list="#session.city" name="locationName" emptyOption="false"
									headerKey="ALL" headerValue="ALL" onchange="filterData()" />
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<i><s:text name="label.slct.status" /> :</i>
							</td>
							<td nowrap="nowrap" colspan="2">

								<input type="checkbox" name="statusName" value="LIVE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: green; color: white; padding: 2px;"><s:text name="LIVE"/>
									[<span id="activeCnt"></span>]</span>&nbsp;&nbsp;

								<input type="checkbox" name="statusName" value="IDLE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: yellow; color: black; padding: 2px;"><s:text name="IDLE"/>[<span
									id="idleCnt"></span>]</span>&nbsp;&nbsp;

								<input type="checkbox" name="statusName" value="NO SALE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: red; color: white; padding: 2px;"><s:text name="NO_SALE"/>[<span
									id="nosaleCnt"></span>]</span>&nbsp;&nbsp;

								<input type="checkbox" name="statusName" value="TERMINATE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: black; color: white; padding: 2px;"><s:text name="TERMINATE"/>[<span
									id="terminateCnt"></span>]</span>&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</s:form>
				<div id="resultDiv" style="display: none">
				</div>
				<div id="reportDiv">
				</div>
				<div id="histReportDiv"></div>
			</div>
		</div>

	</body>
	<script>
	var timeoutPeriod = 15*60*1000;  //hour*min*sec*millisec
	function autoRefresh(){
	setTimeout("location.reload(true)",timeoutPeriod);
	}
	autoRefresh();
	</script>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_retActivity_HistoryMenu.jsp,v $
$Revision: 1.3 $
</code>