<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<s:head theme="ajax" debug="false"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet"	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"	type="text/css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<script>var projectName="<%=request.getContextPath()%>"</script>
<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/slider/js/jquery-1.3.2.min.js"></script>
<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawGameConsolidate.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js"></script>
<script>var projectName="<%=request.getContextPath()%>"</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js"></script>		

<script>
function createTableData() {
				tblHTML = $("#tableDataDiv").html();

				var tblData = '<div><b>USSD Draw Wise report</b></div><br/>';
					tblData += '<div><b>Game Name : </b>'+$("#gameNo :selected").text()+'</div></br>';
					tblData += '<div><b>Start Date : </b>'+$("#start_date").val()+'</div></br>';
					tblData += '<div><b>End Date : </b>'+$("#end_date").val()+'</div></br>';
					tblData += '<div><b>Draw Name : </b>'+$("#drawName").val()+'</div></br>';
					tblData += document.getElementById("tableDataDiv").innerHTML;

			    $('#tableValue').val(tblData);
				$("#tableDataDiv").html(tblHTML);

				return false;
			}
</script>

</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.draw.game.mtn" />
				</h3>


				<s:form name="searchConsolidateData" action="bo_rep_draw_wise_mtn_report_result" onsubmit="return validate()">

					<table border="0" cellpadding="2" cellspacing="0" width="400" style="width:100%;vertical-align:middle; ">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
							<div style="color: red; text-align: center" id="gameNo_e"></div>
								
							</td>
						</tr>
						
						<tr>
									
							<td colspan="2">
								<s:text name="label.game.name" />:
								<s:select name="gameNo" id="gameNo" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}"
									 list="mtnGameMap" listKey="key" listValue="value" onchange="fetchDrawNames(); _id.i('down1','',this);" cssClass="option"/>
							</td>
							
							
						</tr>
						<tr>
							<td align="left" colspan="2">
							<div style="color: red; text-align: center" id="date_e"></div>
								
							</td>
						</tr>
						
						<tr><%
									Calendar prevCal = Calendar.getInstance();
									String currentDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
									String lastDate = CommonMethods.getLastArchDate();
								%>
							<td colspan="2">
								<table cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="startDate" id="start_date"
												onchange="fetchDrawNames()"  value="<s:property value="#session.presentDate"/>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												  onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this,'<%=currentDate%>', '', '<%=currentDate%>')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="endDate" id="end_date"
												onchange="fetchDrawNames()"  value="<s:property value="#session.presentDate"/>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this,'<%=currentDate%>', '', '<%=currentDate%>')"/>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div id="drawNameDiv" style="display: none">
												<s:text name="label.draw.name"/>:
												<s:select cssStyle="width:150px; margin-left:12px" name="drawName" theme="simple" id="drawName"
													headerKey="ALL" headerValue="%{getText('label.ALL')}" list="{}"
													cssClass="option" onchange="clearDivs();" />
											</div>
										</td>
									</tr>
									
									<tr>
										<td colspan="2">
												Winning Transfer Status:
												<s:select cssStyle="width:150px; margin-left:12px" name="drawStatus" theme="simple" id="drawStatus"
													headerKey="ALL" headerValue="%{getText('label.ALL')}" list="#{'APPROVED':'Approved','PENDING':'Pending'}" 
													cssClass="option" onchange="clearDivs();" />
										</td>
									</tr>
									
									
								</table>
							</td>
						</tr>
							
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center"
									targets="down" theme="ajax" cssClass="button"/>
							</td>
						</tr>

					</table>
				</s:form>

				<div id="down" style="text-align: center; width: 95%;"></div>

			</div>
				
		</div>

</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawWiseMtnReport_menu.jsp,v $ $Revision: 1.3 $
</code>
