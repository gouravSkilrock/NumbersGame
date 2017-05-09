<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

		<script>
		var projectName="<%=request.getContextPath()%>"
	     function getTableData() {
		 var tblData = document.getElementById("tableDataDiv").innerHTML;
		 var tableHeader = document.getElementById("table_header").innerHTML;
		 document.getElementById('tableValue').value = tableHeader + "\n\n "+tblData;
		 return false;
	     }
	
		function validateDataAndDate() {
				
					var gameNo =_id.o("gameId");
					if(gameNo.value == "-1"){
						_id.i("errorDiv", "<font color='red'>Please Select Game Name .</font>", null);
						return false;
						}
					_id.i("errorDiv", "");
		
				var isInValid = false;
				var std=_id.o("start_date").value.split("-");
				var ed=_id.o("end_date").value.split("-");
			
	            var startDate=new Date(std[2],std[1],std[0]);
	            
		
				var endDate=new Date(ed[2],ed[1],ed[0]);
				
				if (startDate == "" || endDate == "") {
					isInValid = true;
					_id.o("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";			           
				} else {
					if (endDate < startDate) {
						isInValid = true;
						_id.o("dates").innerHTML = "<font color = 'red'>end date should be greater then or equals to start date</font>";          
			        }
				}				
				if (isInValid) {				
					return false;
				}
				_id.o("dates").innerHTML = "";
				return true;			
			}
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/bo_rep_BlockTickets.js"></script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					Blocked Tickets
					<s:text name="Report" />
				</h3>
				<s:form action="bo_rep_block_tickets_search" onsubmit="return validateDataAndDate()">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">

								Game Name:
								<s:select name="gameId" id="gameId" theme="simple"
									headerKey="-1" headerValue="--Please Select--"
									list="drawGameList"   cssClass="option" />

							</td>
						</tr>
	                     <tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						
		                <tr>
							<%
							  Calendar prevCal= Calendar.getInstance(); 
		                      String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
		                      String calStartDate = CommonMethods.convertDateInGlobalFormat((String)application.getAttribute("DEPLOYMENT_DATE"), "dd-mm-yyyy", (String)application.getAttribute("date_format")) ;
						     %> 
					    <s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
											
						
							<td align="center">
								<label class="label" >Start Date<span>*</span>:&nbsp;</label>
					    		<input  type="text" name="start_date" id="start_date" value="<%=startDate%>" readonly size="12" />
					    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'dd-mm-yyyy', this, '<%=startDate %>', '<%=calStartDate %>', '<%=startDate %>')" />
					    	</td>
					    </tr>	
						<tr>
							<td align="center">
								<label class="label">End Date<span>*</span>:&nbsp;</label>
					    		<input  type="text" name="end_Date" id="end_date" value="<%=startDate %>" readonly size="12" />
					    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'dd-mm-yyyy', this, '<%=startDate %>','<%=calStartDate %>', '<%=startDate %>')" />
					   		</td>
					    </tr>
					    
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
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
	$RCSfile: bo_rep_block_tickets_menu.jsp,v $ $Revision: 1.3 $
</code>
