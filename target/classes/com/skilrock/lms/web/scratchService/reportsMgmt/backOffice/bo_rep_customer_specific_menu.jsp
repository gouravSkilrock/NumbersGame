<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<s:head theme="ajax" debug="false" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
<script>var projectName="<%=request.getContextPath()%>"</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

<script type="text/javascript">
			
	function getAgentList() {
		var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			    var optFirst = new Option("All", -1);
			    retObj.options[0] = optFirst;
				for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
</script>

<script type="text/javascript">
	
	function verifyField() {
		if (!($("#end_date").val() >= $("#start_date").val())) {
			alert('End Date shuold be greater than Start Date !');
			return false;
		} 
		return true;
	}
	
	function clearDiv() {
		_id.i("down", "");
		_id.i("dates", "");
		var radios = document.getElementsByName('reportType');

		for (var i = 0, length = radios.length; i < length; i++) {
		    if (radios[i].checked) {
		        
		    	if(radios[i].value=="Regional Office Wise"){
					document.getElementById('agtList').style.display = 'none';
					
				}else{
					document.getElementById('agtList').style.display = 'block';
				}
		        break;
		    }
		}
	}
</script>
</head>

<body onload="getAgentList();">

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>


	<div id="wrap">

		<div id="top_form">
			<h3>
				<s:text name="Scratch Sale Winning Collection Report" />
			</h3>

			<s:form action="bo_rep_customer_specific_Search"
				onsubmit="return verifyField();">

				<table border="0" cellpadding="2" cellsapacing="0">
					<tr>
						<td>
							<div id="errorId"></div>
						</td>
					</tr>
				</table>
				<table border="0" cellpadding="2" cellsapacing="0">

					<tr>
						<td colspan="2"><s:actionerror /> <s:fielderror /></td>
					</tr>
					<tr>
						<td colspan="2"><span id="list1" onclick="changeValue()"></span>


						</td>
					</tr>

					<tr>
					<s:if test="%{showRegionalWise=='true'}">

						<td align="center" colspan="2"><s:radio name="reportType"
								label="%{getText('Report')} %{getText('label.type')}"
								value="'Agent Wise'"
								list="#{'Agent Wise':#application.TIER_MAP.AGENT +' '+ getText('label.wise'),'Retailer Wise':#application.TIER_MAP.RETAILER +' '+ getText('label.wise'),'Regional Office Wise':'Regional Office Wise'}"
								onclick="clearDiv()" /></td>
					</s:if>
					<s:else>
						<td align="center" colspan="2"><s:radio name="reportType"
								label="%{getText('Report')} %{getText('label.type')}"
								value="'Agent Wise'"
								list="#{'Agent Wise':#application.TIER_MAP.AGENT +' '+ getText('label.wise'),'Retailer Wise':#application.TIER_MAP.RETAILER +' '+ getText('label.wise')}"
								onclick="clearDiv()" /></td>
					
					</s:else>
					
					</tr>

					<tr id="selType">
						
						<td colspan="2">
					    <div id="agtList">
					    <table>
					   <s:select cssClass="option" name="agentOrgId"
								 id="agentOrgId"
								list="{}" key="label.agt.name"
								required="true" ></s:select> 
								</table>
					</div>
								</td>
					</tr>

					<tr>
						<td colspan="2">
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
							<div id="date"
								style="display: block; text-align: left; width: 60%">
								<s:div id="newDates" theme="ajax"
									href="rep_common_fetchDate.action">
								</s:div>
							</div>
						</td>

					</tr>
					<tr>
						<td align="right"><s:submit name="search" key="btn.srch"
								align="right" targets="down" theme="ajax" cssClass="button" />
						</td>
					</tr>
				</table>
			</s:form>
			<div id="down"></div>
			<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
		</div>
	</div>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
</body>

</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	bo_rep_customer_specific.jsp,v $ $Revision: 1.1.2.4.6.1 $ </code>