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

		<script type="text/javascript">
		
		function validateInputs(){
			divValues("gameError","");
			divValues("agentError","");
			var isChecked=true;
			var arrayCheck=_id.n("[id^=gameList]");
			var len= arrayCheck.length;
			for(var i=0;i<len;i++){
			if(arrayCheck[i].checked){
			isChecked=true;
			divValues("gameError","");
			break;
			}else{
			isChecked=false;
			divValues("gameError","*Please select the game ");
			}
			}

			var select_id = _id.o("agentList");
			if(typeof select_id.options[select_id.selectedIndex]!='undefined'){
			divValues("agentError"," ");
			}
			else {
			isChecked=false;
            divValues("agentError","*Please select from the Agent List ");
            }
			return isChecked;
		}
			function divValues(div,value){
					_id.o(div).innerHTML ="<font color='red'>"+ value+ "</font>";
			}
		function getData() {
		var tblData = _id.o("tableDataDiv").innerHTML;
		tblData = tblData.replace(_id.o("sortRow").innerHTML,
				"");
		_id.o('tableValue').value = tblData;
		return false;
	}
	
	function setSelectAgents(){
			var listOfAgents='';
			var list = _id.o("agentList")
			if(typeof list.options[list.selectedIndex]!='undefined'){
			for(var i=0;i<list.length;i++){
			if(list[i].selected)
			listOfAgents=listOfAgents+list[i].value+',';
			}
			_id.o("list").value=listOfAgents.substring(0,listOfAgents.length-1);
			divValues("agentError"," ");
			}
			else {
			isChecked=false;
            divValues("agentError","*Please select from the Agent List ");
            }
	}
	
	function calcTotal(){
	var table = document.getElementById("expandTable");
	var rowCount = table.rows.length;
	var colCount=table.rows[3].getElementsByTagName('td').length;
	var headCount=table.rows[3].getElementsByTagName('th').length;
	for(var c=4;c<colCount+headCount;c++){
		var count=0.0;
		for(var r=3;r<rowCount-1;r++){
			if(c<colCount){
				count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('td')[c].innerHTML).replace(/,/g,""));
			}else{
				count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('th')[c-colCount].innerHTML).replace(/,/g,""));
			}
		}
		var x=table.rows[rowCount-1].insertCell(c);
		x.innerHTML=addCommas(count.toFixed(2));
	}
	
}	
			</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

			

				<s:form name="IncentiveReport"
					action="bo_rep_incentiveScheme_analysisReportSearch"
					onsubmit="return validateInputs();">
					<br />
					<br />
						<s:hidden name="list" id="list"/>
					<table align="center" border="1"
						style="border-color: black; border: 1px solid #999999;">
						<tr>

							<td colspan="2">
								<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Incentive Scheme
									Analysis Report&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
							</td>
						</tr>
					</table>
					<br />


					<center> <s:iterator id="result" value="%{gameMap}"
						status="index">
						<s:checkbox name="gameList_%{key}" fieldValue="%{key}" label="%{value} Sales" />
					</s:iterator>
					<s:checkbox name="gameList" fieldValue="-1" label="Total Sales" />
					 <br />
					
					<div id="gameError"></div>
					<div id="agentError"></div>
					</center>
					<br />
					<table align="center" cellpadding="3" cellspacing="0" border="0">
						<s:set name="startDate" id="startDate" value="#session.presentDate" />
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
									Start Date
									<span>*</span>:&nbsp;
								</label>
								<input type="text" name="startDate" id="startDate"
									value="<%=startDate%>" readonly size="12">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(_id.o('startDate'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
							</td>

							<td>
								<label class="label">
									End Date
									<span>*</span>:&nbsp;
								</label>
								<input type="text" name="endDate" id="endDate"
									value="<%=startDate%>" readonly size="12">
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(_id.o('endDate'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
							</td>
						</tr>
					</table>
					<table align="left" border="1"
						style="border-color: black; border: 1px solid #999999; margin-left: 225px; margin-top: 13px;">
						<tr>

							<td colspan="2">
								<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="Agent" />(Features)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
							</td>
						</tr>
					</table><br/><br/>
					<table align="left" cellspacing="10px">
						<tr>
							<select name="agentList" id="agentList" multiple="true" size="5">
									<s:iterator id="agentMap" value="%{agentMap}"
										status="taskIndex">
										<s:if test="#taskIndex.index==0">
											<option value="-1" />All AGENTS</option>
											<option value="<s:text name="%{value}" />">
												<s:text name="%{key}" /></option>
										</s:if>
										<s:else>
											<option value="<s:text name="%{value}" />">
												<s:text name="%{key}" />
											</option>
										</s:else>
									</s:iterator>
								</select>
						</tr>
					</table>

					<br />
					<br />
					<table align="left" border="1"
						style="border-color: black; border: 1px solid #999999; margin-left: 225px; margin-top: 20px;">
						<tr>

							<td colspan="3">
								<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MRP Sales of <s:text
										name="Retailer" />(Features)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>
							</td>
						</tr>
					</table>
					<table  border="1"
						style="border-color: black; border: 1px solid #999999; margin-left: 225px; margin-top: 80px;align:left;">
						<tr>
							<td>
								Greater than(>)&nbsp;&nbsp;&nbsp;
								<s:textfield name="grtrThnAmt" id="grtrThnAmt" theme="simple" size="12" maxlength="10" onkeypress="return onlyDecimal(this)"/>
							</td>
							<td>
								Less than(<)&nbsp;&nbsp;&nbsp;
								<s:textfield name="lssThnAmt" id="lssThnAmt" theme="simple" size="12" maxlength="10" onkeypress="return onlyDecimal(this)"/>
							</td>
						</tr>
					</table>


					<s:submit value="SUBMIT" align="center" cssStyle="margin-top:20px;" cssClass="button"
						targets="down" theme="ajax" onclick="setSelectAgents();" notifyTopics="calcTotal()"/>

				</s:form>
				<div id="down" style="text-align: center"></div>
			</div>
		</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_incentiveScheme_analysisReportMenu.jsp,v $ $Revision:
	1.3.6.2.2.10.2.2 $
</code>