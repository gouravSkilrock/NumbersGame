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
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />

		<script>
		var projectName="<%=request.getContextPath()%>"
		
		function setAgtName(){
		clearDiv();
			if(document.getElementById('agtOrgId').value != -1){
				var obj = document.getElementById('agtOrgId').getElementsByTagName('option');
				var agentName = "";
				for(i=0; i<obj.length; i++){
					if(obj[i].value == document.getElementById('agtOrgId').value){
						agentName = obj[i].innerHTML;
					} 
				}
				_id.i('agtName','<input type="hidden" name="agtName" value="'+ agentName+ '" />');
			}
		}
		function validate(){
			var agtOrgId=_id.o('agtOrgId').value;
			var chk=true;
			if(agtOrgId==-1){
				_id.o('errorDiv').innerHTML="<p style='color: red'>Please Select Organisation Name</p>";
				chk=false;
			}else{
			_id.o('errorDiv').innerHTML="";
			}
			//alert(chk);
			if(!chk){ 
				return chk;
			}
			else{
				chk = validateDates();
			}
			return chk;
		}
		function setDateField(tvalue) {//alert(tvalue);
		clearDiv();
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
				_id.i('repType','<input type="hidden" name="reportType" value="'+ tvalue +'" />');

		}
				
			function clearDiv(){
			_id.i("down","");
			_id.i("down1","");
			}

		function validateDates() {
				var isInValid = false;
				var startDate = document.getElementById("start_date").value;
				var endDate =  document.getElementById("end_date").value;	
    			
    			if (startDate == "" || endDate == "") {
					isInValid = true;
					document.getElementById("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";
				} else {
					var stArr = startDate.split('-');
					var endArr = endDate.split('-');
					var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
					var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
					if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
						isInValid = true;
						document.getElementById("dates").innerHTML = "<font color = 'red'>end date should be greater then or equals to start date</font>";          
          	
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
		
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					Live Game <s:text name="Report"/> for <s:property value="#application.TIER_MAP.RETAILER" />s
				</h3>
				<s:form action="bo_rep_consLiveGameReport_Search"
					onsubmit="return validate()" >
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="agtOrgId" id="agtOrgId" headerKey="1"
									headerValue=" ALL RETAILERS" label="Select Organization"
									list="#session.orgMap" cssClass="option" required="true"
									onchange="setAgtName();" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<s:select name="totaltime" label="Get %{getPwt('Collection')} %{getPwt('Report')} "
									list="#{'Current Day':'Current Day','Date Wise':'Date Wise'}"
									cssClass="option" onchange="setDateField(this.value)" />
							</td>
						</tr>
						<tr>
							<td><div id="dates"></div></td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<%  
									Calendar prevCal= Calendar.getInstance(); 
									String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
 								%>
								<s:hidden name="curDate" id="curDate" value="<%=startDate%>"></s:hidden>
								<div id="repType"><input type="hidden" name="reportType" value="Current Day" /></div>
								<div id="agtName"></div>
								<div id="date" style="display:none;text-align: left;width: 60%">
									<s:div id="newDates" theme="ajax"
										href="rep_common_fetchDate.action">
									</s:div>
								</div>
							</td>
							
						</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									targets="down" theme="ajax" cssClass="button" onclick="clearDiv()" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="down1"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_consLiveGameReport_Menu.jsp,v $
$Revision: 1.2 $
</code>