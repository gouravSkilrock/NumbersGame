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
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<!--<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>-->
		<script>
			/* $(document).ready(function(){
				$("#stateCode").parents("tr").hide();
				$("#cityCode").parents("tr").hide();
			});*/
		
			function getCityList(state) {
				$("#cityCode").html("");
				var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_rep_getCityList.action?stateCode=" + state);
				var json = JSON.parse(_resp);
				$('#cityCode').append($('<option></option>').val("ALL").html("--ALL--"));
				$.each(json.cityMap, function(key, value) {
					$('#cityCode').append($('<option></option>').val(value).html(key));
				});
			}

		var projectName="<%=request.getContextPath()%>";
		function setDateField(tvalue) {//alert(tvalue);
				document.getElementById("down").innerHTML="";
				if(tvalue=='Date Wise') {
					//alert("inside if"+document.getElementById("date").style.visibility);
					document.getElementById("date").style.display="block";
				}				
				else{ 
					//alert('--in else'+tvalue);
					if(tvalue=='Current Day') {//alert();
					//alert('<s:property value="#session.presentDate"/>');
					_id.o("start_date").value=_id.o("curDate").value;
					_id.o("end_date").value=_id.o("curDate").value;
					document.getElementById("date").style.display="none";
					}
					else {
						document.getElementById("date").style.display="none";
					}
				}
				_id.i('dates','');
		}
		function clearDiv(){
			_id.i("down","");
			_id.i("result","");
			_id.i("dates","");
			_id.i("msg","");
			}
		var reporttypes='';
		function showAgents(val){
		// alert(val+' '+_id.o('agtDiv'));
		 if(val=='Retailer Wise'){
		 	reporttypes='Retailer Wise';
		 	_id.o('agtDiv').style.display='block';
		 }else{
		 	reporttypes='';
		 	_id.o('agtDiv').style.display='none';
		 }
		}	
		function showStateCity(val) {
			if(val == 'Retailer Wise' || val == 'Agent Wise') {
			 	$("#stateCode").parents("tr").show();
			 	$("#cityCode").parents("tr").show();
			 } else {
				 $("#stateCode").parents("tr").hide();
				 $("#cityCode").parents("tr").hide();
			 }
		}
		function getAgentList() {
			$("#agentOrgId").empty();
			$("#agentOrgId").append($('<option></option>').val(-1).html("--Please Select--"));
			var state = $('#stateCode').val();
			var city = $('#cityCode').val();
			var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameListStateAndCityWise.action?orgType=AGENT&state="+state+"&city="+city);
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
		var tierMap = '<%=application.getAttribute("TIER_MAP")%>';
		var i18nObj = parent.frames[0].i18Obj;
		function validateDates() {
				if($("#agentOrgId").val() == -1 && reporttypes=='Retailer Wise'){
				        $("#msg").empty();
				        $("#msg").append(
						i18nObj.prop('msg.pls.select')+" "+tierMap["AGENT"] + " "+i18nObj.prop('msg.org')).css("color","red");
						//reporttypes='';
						return false;
			     }
			     
				var isInValid = false;
				var startDate = document.getElementById("start_date").value;
				var endDate =  document.getElementById("end_date").value;
				var archDate = document.getElementById("arch_date").value ;
	var sDtArr = startDate.split('-');
	var sDate = sDtArr[2]+'-'+sDtArr[1]+'-'+sDtArr[0] ;
	var finalArchDate = Date.parse(archDate) ;
	var finalStartDate = Date.parse(sDate) ;
				if(startDate==null ||endDate==null ){
				return false;
				}
    			if (startDate == "" || endDate == "") {
					isInValid = true;
					document.getElementById("dates").innerHTML = "<font color = 'red'><s:text name='error.dates.empty'/></font>";
				} /* else if(finalArchDate > finalStartDate) {
					document.getElementById("down").innerHTML = "<font color = 'red'>Please select date after "+ archDate +"</font>";
					return false;
				} */ else {
					var stArr = startDate.split('-');
					var endArr = endDate.split('-');
					var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
					var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
					if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
						isInValid = true;
						document.getElementById("dates").innerHTML = "<font color = 'red'><s:text name='error.enddate.gt.strtdate'/></font>";          
          	
					}
				}
				if (isInValid) {
					if(document.getElementsByName('totaltime')[0].value != 'Date Wise'){
						return true;
					}
					return false;
				}
				return true;
		}
		
		
function closeDiv()
{
	
	var startDate = document.getElementById("start_date").value;
	var archDate = document.getElementById("arch_date").value ;
	var sDtArr = startDate.split('-');
	var sDate = sDtArr[2]+'-'+sDtArr[1]+'-'+sDtArr[0] ;
	var finalArchDate = Date.parse(archDate) ;
	var finalStartDate = Date.parse(sDate) ;
	
	if(finalArchDate <= finalStartDate)
	{
		document.getElementById("dates").innerHTML = "" ;
	}


}
		
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
	<body onload="getAgentList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="PWT"/> <s:text name="Report"/> 
				</h3>
				<s:form action="bo_rep_drawPwtReport_Search" onsubmit="return validateDates()">
				<span align="center" colspan="2" id="msg"></span>
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td  colspan="2">
								<s:select name="reportType" label="%{getText('label.report.type')} "
									list="#{'Game Wise':getText('label.gamewise'),'Agent Wise':getText('label.agt.wise'),'Retailer Wise':getText('Retailer') + ' '+getText('label.wise')}"
									cssClass="option"
									onchange="clearDiv(),showAgents(this.value),showStateCity(this.value)"  cssStyle="margin-left:10px; width:130px" />
							</td>
						</tr>
						<tr>
							<td  colspan="2">
								<s:select name="totaltime" label="%{getText('label.report.duration')} "
									list="#{'Current Day':getText('label.cur.day'),'Date Wise':getText('label.datewise')}"
									cssClass="option"
									onchange="setDateField(this.value),clearDiv()" cssStyle="margin-left:10px; width:130px"/>
							</td>
						</tr>
						<tr class="stateDiv">
							<td  colspan="2">
								<!-- <div id="stateDiv" style="display: none"> -->
									<s:select name="stateCode" id="stateCode" label="State Name" list="stateMap" cssClass="option" onchange="getCityList(this.value); getAgentList()"
									headerValue="--ALL--" headerKey="ALL"  cssStyle="margin-left:10px; width:130px"/>
								<!-- </div> -->
							</td>
						</tr>
						<tr class="cityDiv">
							<td align="center" colspan="2">
								<!-- <div id="cityDiv" style="display: none"> -->
									<s:select name="cityCode" id="cityCode" label="City Name" list="{}" cssClass="option" 
									headerValue="--ALL--" headerKey="ALL" onchange="getAgentList()" cssStyle="margin-left:10px; width:130px"/>
								<!-- </div> -->
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="agtDiv" style="display: none">
									<table style="margin-left: 20px;">
										<s:select cssClass="option" name="agentOrgId"
											id="agentOrgId" list="{}"
											label="%{#application.TIER_MAP.AGENT} %{getText('label.org')}"
											headerValue="%{getText('label.please.select')}" headerKey="-1" required="true"></s:select>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div align="center" id="dates"></div>
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
														
														String lastDate = CommonMethods.getLastArchDate();
								%>
								<s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
								<input type="hidden" value="<%=lastDate %>" id="arch_date" />
								<div id="date"
									style="display: none; text-align: left; width: 60%; margin-left: 60px;">
									<s:div id="newDates" theme="ajax" href="%{request.getContextPath()}/com/skilrock/lms/web/reportsMgmt/rep_common_fetchDate_specific.action">
									</s:div>
								</div>
							</td>

						</tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.srch" align="right"
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
$RCSfile: bo_rep_drawPwtReport_Menu.jsp,v $
$Revision: 1.3 $
</code>