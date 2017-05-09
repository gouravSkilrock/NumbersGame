<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
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
	    
		function validateDataAndDate() {
		
				var isInValid = false;
				var startDate=_id.o("start_date").value;
				var endDate=_id.o("end_date").value;
						
				if (startDate == "" || endDate == "") {
					isInValid = true;
					_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.dates.empty'/></font>";			           
				} else {
					if (endDate < startDate) {
						isInValid = true;
						_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.enddate.gt.strtdate'/></font>";          
			            _id.o("down").innerHTML=" ";
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
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div class="agt_rep_top">
				<h3>
					<s:text name="label.agent.wise.sale.report"/>
				</h3>
				<div id="main-heading" style="display: none">
					<s:text name="label.agent.wise.sale.report.heading.in.excel"/>
				</div>
				<s:form action="bo_rep_agent_wise_sale_report_result" onsubmit="return validateDataAndDate();">
					<table width="450" border="0" cellpadding="2" cellspacing="0">
	                     <tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						
		                <tr>
							<%
							  Calendar prevCal= Calendar.getInstance(); 
		                      String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", "yyyy-mm-dd");
		                      String calStartDate = CommonMethods.convertDateInGlobalFormat((String)application.getAttribute("DEPLOYMENT_DATE"), "dd-mm-yyyy",  "yyyy-mm-dd") ;
		                      String date = Util.getCurrentTimeString();
						     %> 
							<s:hidden id="cur_date" value="<%=date.split(" ")[0]%>" />
							<s:hidden id="cur_time" value="<%=date.split(" ")[1]%>" />
							
							<td align="center">
								<s:text name="label.start.date"/><span>*</span>:&nbsp;
					    		<input  type="text" name="startDate" id="start_date" value="<%=startDate%>" readonly size="12" />
					    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate %>', '<%=calStartDate %>', '<%=startDate %>')" />
					    	</td>
					    </tr>	
						<tr>
							<td align="center">
								<s:text name="label.end.date"/><span>*</span>:&nbsp;
					    		<input  type="text" name="endDate" id="end_date" value="<%=startDate %>" readonly size="12" />
					    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate %>','<%=calStartDate %>', '<%=startDate %>')" />
					   		</td>
					    </tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.submit" align="right"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			</div>
		</div>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/Export_Excel.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/Export_PDF.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_agent_wise_sale_report_menu.jsp,v $ $Revision: 1.3 $
</code>
