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
		<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

		<script>
			$(document).ready(function() {
				//$('.gameType').css('display','none');
				$('.gameType').hide();
			});

			var projectName="<%=request.getContextPath()%>"

			function getGameList(serviceName) {
				$("#down").html("");
				_id.o("dates").innerHTML = "";
				$('.gameType').hide();
				$("#gameNo").html("");
				$("#gameNo").append($('<option></option>').val(-1).html("--Please Select--"));
				$("#gameTypeId").html("");
				$("#gameTypeId").append($('<option></option>').val(-1).html("--Please Select--"));
				var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_rep_getServiceGameDetails.action?serviceName="+serviceName);
				var json = JSON.parse(_resp);
				$.each(json.responseData, function(key, value) {
					$('#gameNo').append($('<option></option>').val(key).html(value));
				});
			}

			function getGameTypeList(gameNo) {
				$("#down").html("");
				_id.o("dates").innerHTML = "";
				serviceName = $("#serviceName option:selected").text();

				if(serviceName == 'SLE') {
					$('.gameType').show();

					$("#gameTypeId").html("");
					$("#gameTypeId").append($('<option></option>').val(-1).html("--Please Select--"));
					var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_rep_getServiceGameDetails.action?serviceName="+serviceName+"&gameNo="+gameNo);
					var json = JSON.parse(_resp);
					$.each(json.responseData, function(key, value) {
						$('#gameTypeId').append($('<option></option>').val(key).html(value));
					});
				} else {
					$('.gameType').hide();
				}
			}

		function validateDataAndDate() {
			var isInValid = false;
			var detailType = $('input[name=detailType]:checked').val();;
			var serviceName = _id.o("serviceName").value;
			var gameNo = _id.o("gameNo").value;
			var gameTypeId = _id.o("gameTypeId").value;
			var agentOrgId = _id.o("agentOrgId").value; 
			var retOrgId = _id.o("retOrgId").value;
			var startDate=_id.o("start_date").value;
			var endDate=_id.o("end_date").value;
			
			$("#down").html("");
			_id.o("dates").innerHTML = "";

			if (startDate == "" || endDate == "") {
				isInValid = true;
				_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.dates.empty'/></font>";			           
			}
			if (endDate < startDate) {
				isInValid = true;
				_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.enddate.gt.strtdate'/></font>";          
	            _id.o("down").innerHTML=" ";
			} if(detailType == "LMC" && agentOrgId == -1) {
				isInValid = true;
				_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.select.agt.name.fst'/></font>";
			} 
			if(detailType == "RETAILER") {
				if(agentOrgId == -1) {
					_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.select.agt.name.fst'/></font>";
					return false;
				}
				if(retOrgId == -1) {
					_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.select.ret.name.fst'/></font>";
					return false;
				}
			}
			
			if(serviceName == -1) {
				_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.select.service.name.fst'/></font>";
				return false;
			}
			
			if(gameNo == -1) {
				_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.select.game.name.fst'/></font>";
				return false;
			}
			
			if(serviceName == "SLE" && gameTypeId == -1) {
				_id.o("dates").innerHTML = "<font color = 'red'><s:text name='error.select.game.type.name.fst'/></font>";
				return false;
			}
			
			if (isInValid) {	
				return false;
			}
			_id.o("dates").innerHTML = "";
			return true;			
		}
		
			function getAgentRetList(value){
			$("#down").html("");
				_id.o("dates").innerHTML = "";
				if(value=='LMC'){
					getAgentList();
					$("#agtDiv").show();
					$("#selType").hide();
				}else if(value=='RETAILER'){
					getAgentList();
					$("#agtDiv").show();	
					$("#selType").show();
				}else{
					$("#agtDiv").hide();
					$("#selType").hide();
				
				}
			
			
			}
			function getAgentList() {
			$("#down").html("");
			_id.o("dates").innerHTML = "";
			$("#agentOrgId").empty();
			$("#agentOrgId").append($('<option></option>').val(-1).html("--Please Select--"));
			var state = $('#stateCode').val();
			var city = $('#cityCode').val();
			var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameListStateAndCityWise.action?orgType=AGENT&state=ALL&city=ALL");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
		function getRetailerUserList(agentOrgId) {
			$("#down").html("");
			_id.o("dates").innerHTML = "";
				if(agentOrgId != -1){
				
					var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/getRetailerUserNameList.action?orgId=" + agentOrgId);
					var firstArr = text.data.split(":");
					//alert(firstArr);
					var retObj = _id.o("retOrgId");
					document.getElementById("retOrgId").options.length = 1;
					for (var i = 0; i < firstArr.length - 1; i++) {
						var retNameArray = firstArr[i].split("|");
						var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
						retObj.options[i + 1] = opt;
					}
				} 
			}
		
		function onReortTypeChange() {
			$("#down").html("");
			_id.o("dates").innerHTML = "";
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
					Detailed Winning Payment Report
				</h3>
				<div id="main-heading" style="display: none">
					Detailed Winning Payment Report
				</div>
				<s:form action="bo_rep_detailedWinningReport_Search"
					onsubmit="return validateDataAndDate();" theme="simple">
					<table width="650" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td>
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td>
								Service Name
								:
							</td>
							<td>
								<s:select name="serviceName" id="serviceName" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="serviceMap" listKey="key" listValue="key"
									onchange="getGameList(this.value)" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="label.game.name" />
								:
							</td>
							<td>
								<s:select name="gameNo" id="gameNo" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									onchange="getGameTypeList(this.value)" list="{}" cssClass="option" />
							</td>
						</tr>
						<div>
						<tr class="gameType">
							<td>
								Game type
								:
							</td>
							<td>
								<s:select name="gameTypeId" id="gameTypeId" theme="simple"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="{}" cssClass="option" />
							</td>
						</tr>
						</div>
						<tr>
							<td>
								Report Type:&nbsp;
							</td>
							<td>
								<input type="radio" name="reportType" id="type1" checked="checked" value="ALL" onChange="onReortTypeChange()" /> All
								<input type="radio" name="reportType" id="type2" value="TRANSACTIONAL" onChange="onReortTypeChange()"/> Transactional
							</td>
						</tr>
						<tr>
							<td>
								Detail Type:&nbsp;
							</td>
							<td>
								<input type="radio" name="detailType" id="type3" checked="checked" onchange="getAgentRetList('DETAILED');" value="DETAILED">Detailed 
								<input type="radio" name="detailType" id="type4" onchange="getAgentRetList('LMC');" value="LMC"> LMC Wise 
								<input type="radio" name="detailType" id="type5" onchange="getAgentRetList('RETAILER');" value="RETAILER"> Retailer Wise 
							</td>
						</tr>
						<tr id="agtDiv" style="display: none">
							<td>
								LMC Organization:
							</td>
							<td>
								<s:select cssClass="option" name="agentOrgId" id="agentOrgId"
									list="{}" onchange="getRetailerUserList(this.value);"
									headerValue="%{getText('label.please.select')}" headerKey="-1"
									required="true"></s:select>
							</td>
						</tr>
						<tr id="selType" style="display: none">
							<td>
								Retailer Name:
							</td>
							<td>
								<s:select cssClass="option" name="retOrgId" id="retOrgId"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="{}" />
							</td>
						</tr>
						<tr>
							<%
								Calendar prevCal = Calendar.getInstance();
									String startDate = CommonMethods
											.convertDateInGlobalFormat(new java.sql.Date(prevCal
													.getTimeInMillis()).toString(), "yyyy-mm-dd",
													"yyyy-mm-dd");
									String calStartDate = CommonMethods.convertDateInGlobalFormat(
											(String) application.getAttribute("DEPLOYMENT_DATE"),
											"dd-mm-yyyy", "yyyy-mm-dd");
									String date = Util.getCurrentTimeString();
							%>
							<s:hidden id="cur_date" value="<%=date.split(" ")[0]%>" />
							<s:hidden id="cur_time" value="<%=date.split(" ")[1]%>" />

							<td>
								<s:text name="label.start.date" />
								<span>*</span>:&nbsp;
							</td>
							<td>
								<input type="text" name="startDate" id="start_date"
									value="<%=startDate%>" readonly size="12" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', '<%=calStartDate%>', '<%=startDate%>')" />
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="label.end.date" />
								<span>*</span>:&nbsp;
							</td>
							<td>
								<input type="text" name="endDate" id="end_date"
									value="<%=startDate%>" readonly size="12" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>','<%=calStartDate%>', '<%=startDate%>')" />
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
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_Excel.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Export_PDF.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/jquery.dataTables.js"></script>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_detailedPaymentReport_Menu.jsp,v $ $Revision: 1.3 $
</code>
