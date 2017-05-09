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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script> --%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
<script>var projectName="<%=request.getContextPath()%>"</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<link rel="stylesheet" 
			href="<%=request.getContextPath()%>/LMSImages/css/jquery-ui-themes-1.10.4/themes/ui-lightness/jquery-ui.css" type="text/css" />
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.2.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-ui.js"></script>

<script>
var jq = $.noConflict();
	function validateData() {
		var mobileNbr = document.getElementById("mobileNbr").value;
		if(mobileNbr == "" || mobileNbr.length == 0 || mobileNbr == null) {
			alert("Enter a Mobile Number");
			return false;
		}
		var isNumeric=mobileNbr.match(/^\d+$/);
		if(isNumeric) {
			return true;
		} else {
			alert("Mobile Number Can Only Be Numeric");
			return false;
		}
		return false;
	}
	function createTableData() {
				tblHTML = $("#tableDataDiv").html();
				$('th:nth-child(11)').remove();
				$('td:nth-child(11)').remove();

				var tblData = '<div><b>MTN Transaction Report</b></div><br/>';
					tblData += '<div><b>Mobile Number : </b>'+$("#mobileNbr").val()+'</div>';
					tblData += '<div><b>Start Date : </b>'+$("#startDate").val()+'</div>';
					tblData += '<div><b>End Date : </b>'+$("#endDate").val()+'</div>';
					tblData += '<div><b>Draw Name : </b>'+$("#drawName").val()+'</div><br>';
					tblData += document.getElementById("tableDataDiv").innerHTML;

			    $('#tableValue').val(tblData);
				$("#tableDataDiv").html(tblHTML);

				return false;
			}
			
 		jq(function() {
                jq( "#dailog" ).dialog({
                   autoOpen: false, 
                   hide: "blind",
					height: 250,
					width:1100,
					left:97,
					show : "scale",
					modal: true,
					
                });
         });
         function myfunction(gameTicketNo){
         	var len = gameTicketNo.split("-").length;
        	var ticketNo = gameTicketNo.split("-")[len-1].trim();
        	var _resp= _ajaxCallDiv(projectName +"/com/skilrock/lms/web/drawGames/drawMgmt/Action/fetchTPTktDetails.action","ticketNumber="+ticketNo,'dailog');
			jq( "#dailog" ).dialog( "open" );
         }
</script>

</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3>
				<s:text name="label.mtn.customer.center.report"></s:text>
			</h3>
			<%-- <s:form name="searchgame" action="bo_dg_rep_mtn_txn_status_result" onsubmit="return validateDates()"> --%>
			<s:form name="fetchTransactions" action="bo_dg_rep_mtn_txn_status_result" onsubmit="return validateData()">
				<table border="0" cellpadding="2" cellspacing="0" width="400">
					<tr>
						<td align="left" colspan="2">
							<div id="error"></div></td>
					</tr>
					<tr>
						<td colspan="2"><s:textfield id="mobileNbr" name="mobileNbr"
								maxlength="10" label="Mobile Number"></s:textfield></td>
					</tr>
					<tr>
						<td colspan="2"><div id="dates"></div></td>
					</tr>
					<tr>
						<td colspan="2">
							<table width="400px" cellpadding="3" cellspacing="0" border="0">
								<s:set name="stDate" id="stDate" value="#session.presentDate" />
								<%
									Calendar prevCal = Calendar.getInstance();
									String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
								%>
								<tr>
									<td><label class="label" style="margin-left: 75px;">
											Start Date <span>*</span>:&nbsp; </label> <input type="text"
										name="startDate" id="startDate" value="<%=startDate%>"
										readonly size="12" onchange="fetchDrawNames()"> <input
											type="button"
											style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
											onclick="displayCalendar(document.getElementById('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{depDate}"/>', '<%=startDate%>')" />
									</td>
								</tr>
								<tr>
									<td><label class="label" style="margin-left: 74px;">
											End Date <span>*</span>:&nbsp; </label> <input type="text"
										name="endDate" id="endDate" value="<%=startDate%>" readonly
										size="12" style="margin-left: 7px;"
										onchange="fetchDrawNames()"> <input type="button"
											style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
											onclick="displayCalendar(document.getElementById('endDate'),'yyyy-mm-dd', this, '<%=startDate%>', '<s:property value="%{depDate}"/>', '<%=startDate%>')" />
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<div id="drawNameDiv" style="display: none;margin-left: 74px;">
											Draw Name:
											<s:select name="drawName" theme="simple" id="drawName"
												headerKey="ALL" headerValue="ALL" list="{}"
												cssClass="option" onchange="_id.i('down','')" />
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2"><s:submit name="search" value="Search"
								align="right" targets="down" theme="ajax" cssClass="button" />
						</td>
					</tr>
				</table>
			</s:form>
			<div id="down" style="text-align: center"></div>
			<div id="dailog" style="background-color: white" title="Ticket Information"></div>				
		</div>
	</div>
</body>
</html>

<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_dg_rep_mtnTxnList_Menu.jsp,v $ $Revision: 1.3 $
</code>