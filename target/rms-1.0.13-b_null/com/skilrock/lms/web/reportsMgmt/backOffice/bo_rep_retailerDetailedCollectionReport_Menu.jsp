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
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>
			var projectName="<%=request.getContextPath()%>"

			$(document).ready(function() {
				$("#loadImage").hide();

				loadWait = function() {
					//$("#exportBtn").hide();
					//$("#loadImage").show();
				};
			});

			function clearDiv() {
				_id.i("down","");
				_id.i("result","");
			}

			function calcTotal() {
				var table = document.getElementById("expandTable");
				var rowCount = table.rows.length;
				var colCount=table.rows[3].getElementsByTagName('td').length;
				var headCount=table.rows[3].getElementsByTagName('th').length;
				for(var c=1;c<colCount+headCount;c++) {
					var count=0.0;
					for(var r=3;r<rowCount-1;r++) {
						if(c<colCount) {
							count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('td')[c].innerHTML).replace(/,/g,""));
						} else {
							count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('th')[c-colCount].innerHTML).replace(/,/g,""));
						}
					}
					var x=table.rows[rowCount-1].insertCell(c);
					x.innerHTML=addCommas(count.toFixed(2));
				}
			}
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="Retailer" />
					Detailed
					<s:text name="Collection" />
					<s:text name="Report" />
				</h3>
				<s:form id="myForm" action="bo_rep_retDetailedCollectionReport_Export" onsubmit="loadWait();" method="post" enctype="multipart/form-data">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
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
								<div id="dates"></div>
								<s:div id="newDates" theme="ajax"
									href="rep_common_fetchDate.action">
								</s:div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit id="exportBtn" name="export" value="Export" align="right" />
							</td>
						</tr>
					</table>
					<s:actionmessage/>
				</s:form>
				<center><img id="loadImage" src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" alt="Loading ..." /></center>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retailerDetailedCollectionReport_Menu.jsp,v $
	$Revision: 1.3 $
</code>