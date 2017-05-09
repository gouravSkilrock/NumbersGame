<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script>
	function getCityList(state) {
				$("#cityCode").html("");
				var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/drawGames/reportsMgmt/bo_rep_getCityList.action?stateCode=" + state);
				var json = JSON.parse(_resp);
				$('#cityCode').append($('<option></option>').val("ALL").html("--ALL--"));
				$.each(json.cityMap, function(key, value) {
					$('#cityCode').append($('<option></option>').val(value).html(key));
				});
			}

		function validateDate() {
			var isInValid = false;
			var startDate = document.getElementById("start_date").value;
			var endDate =  document.getElementById("end_date").value;
			var archDate = document.getElementById("arch_date").value ;
			var sDtArr = startDate.split('-');
			var sDate = sDtArr[2]+'-'+sDtArr[1]+'-'+sDtArr[0] ;
			var finalArchDate = Date.parse(archDate) ;
			var finalStartDate = Date.parse(sDate) ;

			if (startDate == "" || endDate == "") {
				isInValid = true;
				document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
			} else {
				var stArr = startDate.split('-');
				var endArr = endDate.split('-');
				var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
				var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
				if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
					isInValid = true;
					document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
				}
			}

			if (isInValid) {
				if((document.getElementsByName('totaltime').length != 0)&&(document.getElementsByName('totaltime')[0].value != 'Date Wise')){
					return true;
				}
				return false;
			}

			if(checkField())
				return true;
			else
				return false
		}
	</script>
	
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3><s:text name="label.draw.game.sale.and"/> <s:text name="PWT"/> <s:text name="Report"/></h3>
	
	
  <s:form name="searchgame" action="bo_rep_gameAgentWise_Result" onsubmit="return validateDate()"> 		
	 <table  border="0" cellpadding="2" cellspacing="0" width="400px">
		<tr>
			<td colspan="2"><s:select name="totaltime" key="label.get.sale.rep" list="{'Current Day','Current Week','Current Month','Date Wise'}" cssClass="option" onchange="setInputField(this.value)" /></td>
		</tr>
		
		<tr> 			
			<td colspan="2" align="center">
			<div id="date" style="display: none;">
				<s:set name="stDate" id="stDate" value="#session.presentDate" />
				<div id="dates"></div>
				<s:div theme="ajax" href="rep_common_fetchDate.action">
				</s:div>
			</div>
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
			<td colspan="2">
				<div id="game"></div>
			</td>
		<tr>
			<td colspan="2">
				<div id="list"></div>
			</td>
		</tr>
		<tr>
			<td><s:submit name="search" key="btn.srch" align="right"  targets="down" theme="ajax" cssClass="button"/></td>
		</tr>
		
	 </table>
	</s:form>
	<div id="down" style="text-align: center" ></div>
	 </div></div>
	
 	
 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_rep_gameAgentWise_Menu.jsp,v $
$Revision: 1.3 $
</code>