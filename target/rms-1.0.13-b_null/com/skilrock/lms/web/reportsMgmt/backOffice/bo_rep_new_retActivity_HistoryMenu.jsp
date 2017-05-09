<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
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
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
		
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>";
	
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="slider/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript">
		    var jq = jQuery.noConflict();
		</script>
			<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/newRetHistory.js" >
		</script>
			<script type="text/javascript" src="backOffice/js/jquery.fixedtable.js"></script>
			
			
		<script type="text/javascript" src="backOffice/js/sh_main.min.js"></script>
		<script type="text/javascript" src="backOffice/js/sh_javascript.js"></script>
		
		
	</head>
	<body onload="filterHistoryData('3')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" />
					<s:text name="Activity" />
					<s:text name="Report" />
				</h3>

				<s:form name="retActivity" id="retActivity" action="bo_rep_new_retActivity_History_result" method="POST">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">

						<tr>
							<td>
								<table width="400px" cellpadding="3"
									cellspacing="0" border="0">
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
												<s:text name="label.start.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="start_date" id="start_date"
												value="<%=startDate%>" readonly size="12" onchange="filterHistoryData('cd')">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12" onchange="filterHistoryData('cd')">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table>
									<tr>
										<td>
											<s:radio
												list="#{'1':getText('label.device&version'),'2':getText('label.connectivity.mode'),'3':getText('label.retailer.activity.count'),'4':getText('label.transaction.volume'),'5':getText('label.location')}"
												key="label.slct.type" id="selectType" name="selectType"
												value="3" onchange="filterHistoryData(this.value)"></s:radio>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<!--<tr>
							<td>
								<s:submit name="search" align="center" value="Search"
									targets="histReportDiv" theme="ajax" cssClass="button"/>
							</td>
						</tr>
						-->
						<tr>
							<td>
								<table style="display: block;" id="statusDiv" border="0">
								<tr>
										<td>
										<table>
									<s:radio list="serviceList" key="label.slct.service"
												id="selectService" name="selectService"
											value="serviceList[0]" onchange="filterStateData('')"/>
										</table>
										</td>
									</tr>
									<tr>
										<td>
										<table>
										
									<s:radio
									list="stateList"
									key="label.slct.state" id="selectState" name="selectState" 
									onchange="filterStateData(this.value)"></s:radio>
										</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
						<td>
						<div id="mydata" style="display: none;"></div>
						</td>
						</tr>
						<!--
							<s:hidden id="cList1" value="%{cityCodeList}"></s:hidden>
						<tr>
							<td>
								<table style="display: none;" id="cbDiv" border="0">
									<tr>
										<td>
										<s:checkboxlist list="city" name="cList" id="cList" label="Select City" onchange="changeMe()" value="city[0]"/>
										
											<table  border="1" cellpadding="2" cellspacing="0" id="searchTable">
												<tr>
													<td>
														<b><input type="checkbox" id="selAll" name="cityName" value="-1" onclick="selAllChkBx();"/>Select All City's </b>
													</td>
												</tr>
												<tr>
													<td>
														<table id="tab0">
															<tr>
																<s:iterator value="cityList" status="retIndex">
																	<td>
																		<input type="checkbox" name="cList" value="<s:property />" onchange="buildTable('')" />
																		<s:property />
																	</td>
																	<s:if test="(#retIndex.index+1)%7==0">
															</tr>
															<tr>
																</s:if>
																</s:iterator>
															</tr>
														</table>
													</td>
												</tr>
											</table>	
										</td>
									</tr>
								</table>
							</td>
						</tr>						
					--></table>
				</s:form>
				<div id="histReportDiv"></div>
				<div id="histDiv" style="display: none;"></div>
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_new_retActivity_HistoryMenu.jsp,v $ $Revision: 1.1.2.8
	$
</code>