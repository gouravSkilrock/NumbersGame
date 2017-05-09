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
		<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script> --%>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script>

		<script>
	function validateData() {
		var frmDate =new Date($("#start_date").val());
		var toDate =new Date($("#end_date").val());
		if($("input[name='mobileType']:checked").val()=='NUMBER')
		{
			var mobileNbr = document.getElementById("mobileNbr").value;
			var isNumeric=mobileNbr.match(/^\d+$/);
			if(mobileNbr == "" || mobileNbr.length == 0 || mobileNbr == null) {
				_id.i("error", "Enter a mobile number", "e");
				return false;
			}
			if(!isNumeric) {
				_id.i("error", "Mobile Number Can Only Be Numeric", "e");
				return false;
			}
		}
		if(toDate<frmDate){
			$("#error").html(i18nOb.prop('error.enddate.gt.strtdate'));
			return false;
		}
		_id.i("error", "");
		return true;
	}
	function createTableData() {
				tblHTML = $("#tableDataDiv").html();
				

				var tblData = '<div><b>USSD Subscriber Wise Report</b></div><br/>';
				if($("input[name='mobileType']:checked").val()=='NUMBER')
					tblData += '<div><b>Mobile Number : </b>'+$("#mobileNbr").val()+'</div>';
				else
					tblData += '<div><b>Mobile Number : </b>ALL</div>';
					tblData += '<div><b>Start Date : </b>'+$("#start_date").val()+'</div>';
					tblData += '<div><b>End Date : </b>'+$("#end_date").val()+'</div>';
				if($("#gameNo").val()==-1)
					tblData += '<div><b>Game Name : </b>ALL</div><br>';
				else
					tblData += '<div><b>Game Name : </b>'+$('#gameNo option:selected').text()+'</div><br>';
					tblData += '<div><b>Draw Name : </b>'+$("#drawName").val()+'</div><br>';
					tblData += '<div><b>Winning Status : </b>'+$("#winningStatus").val()+'</div><br>';
					tblData += document.getElementById("tableDataDiv").innerHTML;

			    $('#tableValue').val(tblData);
				$("#tableDataDiv").html(tblHTML);

				return false;
			}
			function showMobileTextBox(){
			var value=$("input[name='mobileType']:checked").val();
				if (value == 'ALL') {
					$('#mobileNbr').hide();
					$('#mobileNbr').val("");
				}else if(value=='NUMBER'){
					$('#mobileNbr').show();
					$('#mobileNbr').focus();
					}
				}
			
</script>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.ussd.subscriber.wise.report"></s:text>
				</h3>
				<s:form name="fetchTransactions" action="bo_rep_ussdSubscriberWiseReport_result" onsubmit="return validateData()" theme="simple">
					<table border="0" cellpadding="2" cellspacing="0" width="400">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td class="tdLabel">
								<s:text name="Mobile Number" />
								:
							</td>
							<td>
								<s:radio id="mobileType" name="mobileType"
									list="#{'ALL':'All','NUMBER':'Number'}" value="%{'ALL'}"
									onchange="showMobileTextBox();clearDivs();" />
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<input type="text" id="mobileNbr" name="mobileNbr"
									style="display: none; right-margin: 120px;" maxlength="10" />
							</td>
						</tr>
						<%
							Calendar prevCal = Calendar.getInstance();
								String currentDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd","yyyy-mm-dd");
								String lastDate = CommonMethods.getLastArchDate();
						%>
						<s:set name="stDate" id="stDate" value="#session.presentDate" />

						<tr>
							<td class="tdLabel">
								<label class="label">
									<s:text name="label.start.date" />
									<span>*</span>:&nbsp;
								</label>
							</td>
							<td>
								<input type="text" name="startDate" id="start_date"
									onchange="fetchDrawNames()"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this,'<%=currentDate%>', '', '<%=currentDate%>')" />
							</td>
						</tr>
						<tr>
							<td class="tdLabel">
								<label class="label">
									<s:text name="label.end.date" />
									<span>*</span>:&nbsp;
								</label>
							</td>
							<td>
								<input type="text" name="endDate" id="end_date"
									onchange="fetchDrawNames()"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this,'<%=currentDate%>', '', '<%=currentDate%>')" />
							</td>
						</tr>
						<tr>

							<td class="tdLabel">
								<s:text name="label.game.name" />
								:
							</td>
							<td>
								<s:select name="gameNo" id="gameNo" theme="simple"
									headerKey="-1" headerValue="ALL" list="mtnGameMap"
									listKey="key" listValue="value"
									onchange="fetchDrawNames(); _id.i('down1','',this);"
									cssClass="option" />
							</td>
						</tr>
						<tr>
							<td class="tdLabel">
								<s:text name="label.draw.name" />
								:
							</td>
							<td>
								<s:select name="drawName" theme="simple" id="drawName" headerKey="ALL"
									headerValue="%{getText('label.ALL')}" list="{}"
									cssClass="option" onchange="clearDivs();" />
							</td>
						</tr>

						<tr>
							<td class="tdLabel">
								<s:text name="Winning Status" />
								:
							</td>
							<td>
								<s:select name="winningStatus" id="winningStatus"
									list="#{'PAID':'Paid', 'PENDING':'Pending', 'NON WIN':'Non Win'}"
									headerKey="ALL" headerValue="ALL" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search" value="Search" align="center"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_ussdSubscriberWiseReport_menu.jsp,v $ $Revision:
	1.1.2.3 $
</code>