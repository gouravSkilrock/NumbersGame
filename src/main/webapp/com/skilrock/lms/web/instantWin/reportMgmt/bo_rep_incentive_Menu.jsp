<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath()%>";</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<s:head theme="ajax" debug="false" />
	<script type="text/javascript">
		function getData() {
			var tblData = document.getElementById("tableDataDiv").innerHTML;
			tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
			document.getElementById('tableValue').value = tblData;
			return false;
		}
		
		function getReatialerData() {
			var tblData = document.getElementById("tableDataDivForRetailer").innerHTML;
			tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
			document.getElementById('retailerTableValue').value = tblData;
			return false;
		}
		
	   function clearDiv(){
		_id.i("down","");
		_id.i("result","");
		}
		
		function setDateField(currVal){
			$("#daily_start_date").hide();
			$("#daily_end_date").hide();
			$("#weekly_date").hide();
			$("#error").html("");
			$("#down").html("");
			$("#result").html("");
			if("Daily" == currVal){
				$("#daily_start_date").show();
				$("#daily_end_date").show();
			}else if("Weekly" == currVal){
				$("#weekly_date").show();
			}
		} 
		
		function validate(){
			if($("#selectReportType").val() == -1){
				$("#error").html("Please select type!!");
				return false;
			}else{
				$("#error").html("");
				return true;
			}
		}
	</script>
  </head>
  
   <body>
	<%@ include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3>
				Incentive Report 
			</h3>
			<s:form action="bo_rep_incentive_report_search" onsubmit="return validate();">
				<table>	
				<tr><td id="error" style="color: red;"></td></tr>
					<tr>
						<td>
							<table>
								<tr><td>
									<s:select name="reportType" key="label.slct.type" headerKey="-1" headerValue="--Please Select--"
										list="#{'Daily':getText('label.daily'),'Weekly':getText('label.weekly')}"
										cssClass="option" onchange="setDateField(this.value)" id="selectReportType" />
								</td></tr>
							</table>
						</td>
					</tr>
					<tr id="daily_start_date" style="display:none;">
						<td>
							<s:set name="stDate" id="stDate" value="#session.presentDate" />
							<%
								Calendar prevCal = Calendar.getInstance();
								prevCal.set(5, prevCal.get(5)-1);
								String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd","yyyy/mm/dd");
							%>
							<label class="label" style="margin: 3px;"><s:text name="label.start.date" /><span style="color:red;">*</span>:&nbsp;</label>
							<input type="text" name="fromDate" id="fromDate" value="<%=startDate%>" readonly size="12" />
							<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('fromDate'),'yyyy/mm/dd', this, '<%=startDate%>', false, '<%=startDate %>')" />
						</td>
					</tr>
					<tr id="daily_end_date" style="display:none;">
						<td>
							<s:set name="endDate" id="endDate" value="#session.presentDate" />
							<%
								Calendar Cal = Calendar.getInstance();
								Cal.set(5, Cal.get(5)-1);
								String endDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(Cal.getTimeInMillis()).toString(), "yyyy-mm-dd","yyyy/mm/dd");
							%>
							<label class="label" style="margin: 5px;"><s:text name="label.end.date" /><span style="color:red;">*</span>:&nbsp;</label>
							<input type="text" name="toDate" id="toDate" value="<%=endDate%>" readonly size="12" />
							<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('toDate'),'yyyy/mm/dd', this, '<%=endDate%>', false, '<%=startDate %>')" />
						</td>
					</tr>
					<tr id="weekly_date" style="display:none;">
						<td>
							<s:set name="weekDate" id="weekDate" value="#session.presentDate" />
							<%
								Calendar date = Calendar.getInstance();
								date.set(5, date.get(5)-1);
								String weekDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(date.getTimeInMillis()).toString(), "yyyy-mm-dd","yyyy/mm/dd");
							%>
							<label class="label"><s:text name="label.slct.date" /><span style="color:red;">*</span>:&nbsp;</label>
							<input type="text" name="weekDate" id="weekDate" value="<%=weekDate%>" readonly size="12" />
							<input type="button" style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;" onclick="displayCalendar(document.getElementById('weekDate'),'yyyy/mm/dd', this, '<%=weekDate%>', false, '<%=weekDate %>')" />
						</td>
					</tr>
					<tr>
						<td align="right">									
							<s:submit name="search" key="btn.srch" align="center" targets="down" theme="ajax" cssClass="button" onclick="clearDiv()"/>
						</td>
				   </tr>					
			    </table>
			</s:form>
			<div id="down"></div>
			<div id="result" style="overflow-x:auto;overflow-y:hidden;"></div>
		</div>
	</div>
 </body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_incentive_Menu.jsp,v $ $Revision:
	1.1.2.4 $
</code>