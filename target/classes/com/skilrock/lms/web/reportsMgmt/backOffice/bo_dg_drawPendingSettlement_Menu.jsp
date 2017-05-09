
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
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
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
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/drawPendingReport.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('.dateSel').hide();
			});

			function toggleDateDiv(selection) {
				$('#agentOrgId').prop('selectedIndex', 0);
				$('#interfaceType').prop('selectedIndex', 0);
				$('#down').html('');
				$('#retWiseResult').html('');
				if(selection == 'false') {
					$('.dateSel').hide();
					$('.interfaceSel').show();
				} else {
					$('.dateSel').show();
					$('.interfaceSel').hide();
				}
			}

			function clearSubDiv() {
				$('#retWiseResult').html('');
			}

			function goToLast() {
				$(document).scrollTop($(document).height());
			}

			function getData(tblElem, hidElem) {
					var tblData ="";
				
					tblData +=  "<span><b>Report Name : </b></span>"+ $("#reportName").val() + "</br>";
					
					if($("#isDateDisplay").val() == "YES"){				
				 		tblData += "<span><b>Start Date : </b></span>"+ $("#start_date").val() + "</br>"; 
				 		tblData += "<span><b>Start Date : </b></span>"+ $("#end_date").val() + "</br>";
				 	}
				 	tblData += document.getElementById(tblElem).innerHTML;
					if(document.getElementById("sortRow") != null) {
						tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
					}
					document.getElementById(hidElem).value = tblData;
					return false;
			}
		</script>
	</head>
	<body onload="getAgentList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Draw Pending Settlement Report
				</h3>
				<s:form name="searchgame" action="bo_rep_drawPendingSettlementData"
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
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<tr>
										<td>
											<input type="radio" name="reportType" id="processing" value="processing" checked="checked" onchange="toggleDateDiv('false');" />
											<label for="processing">Processing Tickets</label>
											<input type="radio" name="reportType" id="unsuccessful" value="unsuccessful" onchange="toggleDateDiv('true');" />
											<label for="unsuccessful">Unsuccessful Tickets</label>
										</td>
									</tr>
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
												.convertDateInGlobalFormat(new java.sql.Date(prevCal
													.getTimeInMillis()).toString(), "yyyy-mm-dd",
														"yyyy-mm-dd");
									%>
									<tr>
										<td colspan="2">
											Select <s:text name="AGENT" />
											<s:select cssClass="option" headerKey="-1" required="true"
												headerValue="ALL" theme="simple"
												name="agentOrgId" id="agentOrgId" list="{}">
											</s:select>
										</td>
									</tr>
									<tr class="interfaceSel">
										<td colspan="2">
											Select Interface
											<s:select cssClass="option" required="true" theme="simple"
												name="interfaceType" id="interfaceType" list="#{'web':'Web', 'terminal':'Terminal'}" />
										</td>
									</tr>
									<tr class="dateSel">
										<td>
											<label class="label">
												Start Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="startDate" id="start_date"
												value="<%=startDate%>" readonly size="12" />
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{deploymentDate}"/>', '')" />
										</td>
									</tr>
									<tr class="dateSel">
										<td>
											<label class="label">
												End Date
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="endDate" id="end_date"
												value="<%=startDate%>" readonly size="12" />
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" value="Search" onclick="clearSubDiv();" align="center"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
				<div id="retWiseResult" style="text-align: center"></div>
				<br />
				<br />
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_drawPendingSettlement_Menu.jsp,v $ $Revision: 1.3 $
</code>