<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/activityRep.js" />
		<script></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
	<body
		onload="fetchActData('<s:property value="#session.USER_INFO.userOrgId"/>')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" />
					Activity <s:text name="Report"/>
				</h3>


				<s:form name="retActivity" id="retActivity" action="bo_rep_retActivity_History" method="POST">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">
						<tr>
							<td align="right" style="visibility:hidden">
								<i>Select <s:text name="Report"/> Type</i>
							</td>
							<td nowrap="nowrap" colspan="2" style="visibility:hidden">
								<s:select theme="simple" cssClass="option" name="reportType" id="reportType" list="#{'current':'current','history':'history'}" onchange="return fetchActData(document.getElementById('agtOrgName').value)"></s:select>
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
						<!-- <tr>
							<td id="searchButtonId"><s:submit name="search" value="Search" align="right"  targets="histReportDiv" theme="ajax" cssClass="button" /></td>
						</tr>-->
						<tr>
							<td align="right">
								<i><s:property value="#application.TIER_MAP.AGENT" />
									Organization :</i>
							</td>
							<td nowrap="nowrap" colspan="2"><b>
								<s:property value="#session.USER_INFO.orgName" /></b>
								<div style="display: none;">
									<s:select theme="simple" id="agtOrgName" cssClass="option"
										list="{}" name="organization_Name" emptyOption="false"
										headerKey="%{#session.USER_INFO.userOrgId}"
										headerValue="%{#session.USER_INFO.orgName}" />
								</div>
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<i>Location :</i>
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
								<i>Terminal Type :</i>
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="terminal" cssClass="option"
									list="#{'EFT930G':'EFT930G','ICT220':'ICT220','PC':'PC','N.A.':'No Terminal'}" name="terminal" emptyOption="false"
									headerKey="ALL" headerValue="ALL" onchange="filterData()" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<i>Select Status :</i>
							</td>
							<td nowrap="nowrap" colspan="2">

								<input type="checkbox" name="statusName" value="ACTIVE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: green; color: white; padding: 2px;">ACTIVE
									[<span id="activeCnt"></span>]</span>&nbsp;&nbsp;

								<input type="checkbox" name="statusName" value="IDLE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: yellow; color: black; padding: 2px;">IDLE[<span
									id="idleCnt"></span>]</span>&nbsp;&nbsp;

								<input type="checkbox" name="statusName" value="NO SALE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: red; color: white; padding: 2px;">NO
									SALE[<span id="nosaleCnt"></span>]</span>&nbsp;&nbsp;

								<input type="checkbox" name="statusName" value="TERMINATE"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: black; color: white; padding: 2px;">TERMINATE[<span
									id="terminateCnt"></span>]</span>&nbsp;&nbsp;
								
								<input type="checkbox" name="statusName" value="NO TERMINAL"
									id="retActivity_statusName" onclick="filterData()"
									checked="checked" />
								&nbsp;&nbsp;
								<span
									style="font-weight: bold; background: black; color: white; padding: 2px;">NO TERMINAL[<span
									id="noTerminal"></span>]</span>&nbsp;&nbsp;
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
$RCSfile: agt_rep_retActivity_Menu.jsp,v $
$Revision: 1.2 $
</code>