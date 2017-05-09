<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	calendar.setTimeInMillis(System.currentTimeMillis());
	calendar.set(calendar.HOUR_OF_DAY, 23);
	calendar.set(calendar.MINUTE, 59);
	calendar.set(calendar.SECOND, 59);
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
		<script>
		
		  
	  	
	 function getRetailerUserList(agentOrgId) {
		if(agentOrgId != -1){
		var text = _ajaxCall("getRetailerUserNameList.action?orgId=" + agentOrgId);
		var firstArr = text.data.split(":");
		var retObj = _id.o("retOrgId");
		for (var i = 0; i < firstArr.length - 1; i++) 
		{
			var retNameArray = firstArr[i].split("|");
			var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			retObj.options[i + 1] = opt;
		}
		}   
		}

	 function getAgentList() {
			var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			for (var i = 0; i < firstArr.length - 1; i++) 
			{
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
			}
			
			
    function showHide(id2) {
	
		if(id2=="ret1")
		{
		_id.o("errorDiv").innerHTML="";
		_id.o("ret1").checked=true;
		_id.o("ret2").checked=false;
		_id.o("agtDiv").style.display='block';
		_id.o("retOrgDiv").style.display='block';
		_id.o("retailerId").style.display='none';
		_id.o("retId").value="";
		_id.o("transNo").value="";
		}
		else if(id2=="ret2")
		{
		_id.o("errorDiv").innerHTML="";
		_id.o("retOrgId").selectedIndex=0;
		_id.o("agentOrgId").selectedIndex=0;
		_id.o("ret1").checked=false;
		_id.o("ret2").checked=true;
		_id.o("agtDiv").style.display='none';
		_id.o("retOrgDiv").style.display='none';
		_id.o("retailerId").style.display='block';
		_id.o("transNo").value="";
		}
		}
	
	function validateData(){
	
	var returnFlag=true;
	var idFlag = false;
	var nameFlag = false;
	var stringToBeDisplayed="";
	if(_id.o("ret1").checked)
	nameFlag=true;
	else if(_id.o("ret2").checked)
	idFlag=true;
	
	if(_id.o("gameNo").selectedIndex==0)
	{
	returnFlag=false;
	stringToBeDisplayed+='*Please Select The Game Name <br>';
	}
	
	if(_id.o("transNo").value=="" || _id.o("transNo").value.length < 3 || _id.o("transNo").value.length > 4 )
	{
	returnFlag=false;
	stringToBeDisplayed+='*Please Enter The Transaction No Properly <br>';
	}
	else if(_id.o("transNo"))
	{
	
	}
	
	
	
	if(nameFlag)
	{
	if(_id.o("retOrgId").selectedIndex==0 || _id.o("agentOrgId").selectedIndex==0){
	returnFlag=false;
	stringToBeDisplayed+='*Please Select Organization Names <br>';
	}
	}
	else if(idFlag)
	{
	if(_id.o("retId").value!="" && (_id.o("retId").value.length==4 || _id.o("retId").value.length==5))
	{
	}
	else
	{
	returnFlag=false;
	stringToBeDisplayed+='*Please Enter Retailer User Id Properly <br>';
	}
	}

	if(!returnFlag)
	_id.o("errorDiv").innerHTML="<font color='red'>"+stringToBeDisplayed+"</font>";
	else
	_id.o("errorDiv").innerHTML="";
	
	return returnFlag;
	}
	function onlyIntegers(evt) {
	var code = (document.all) ? event.keyCode:evt.which;
	var e = event || evt;
	if ((code >= 48 && code < 58) || (code >= 96 && code < 105) || (code == 46 || code == 8 || code == 37 || code == 39 || code == 45) || ( code == 9 || code == 16 || code == 35 || code == 36 )) {
		return true;
	}
	return false;
	}
	
	function isAlphanumeric(elem, helperMsg){
	var alphaExp = /^[0-9a-zA-Z]+$/;
	if(elem.value.match(alphaExp) && !trimAll(elem.value)==""){
	doReset(elem);
		return true;
	}else{
		//alert(helperMsg);
		var elem1=elem.name+"1";
		//document.getElementById('gameName1').innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
		elem.focus();
		return false;
	}
}
	
	
	</script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	</head>

	<body onload="getAgentList();">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Track Transaction Ticket
				</h3>
				<s:form name="form" name="trackTransTkt" theme="simple">
					<table width="400" cellpadding="2" cellspacing="0" border="0">
					<tr>
					<td>
					<div id="errorDiv"></div>
					</td>
					</tr>
						<tr>
							<td>
								<input type="checkbox" id="ret1" name="checkgroup"
									onclick="showHide(this.id); " checked />
								Retailer Name  &nbsp;&nbsp;&nbsp;&nbsp;
								<!--<input type="checkbox" id="ret2" name="checkgroup"
									onclick="showHide(this.id); " />
								Retailer Id
							--></td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="agtDiv">
									<table>
										Agent Name <span class="required">*</span>:<s:select name="agentOrgId"
											onchange="getRetailerUserList(this.value); return verifyRetailer(this.options[this.selectedIndex].text);"
											id="agentOrgId" list="{}" 
											headerValue="-Please Select Agent Name-" headerKey="-1"></s:select>
									</table>
								</div>
							</td>
						</tr>

						<tr>
							<td>
								<div id="retOrgDiv">
									<table>
										Retailer Name <span class="required">*</span>:<s:select name="retOrgId" id="retOrgId"
											headerKey="-1" headerValue="--Please Select--" list="{}" />
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="retailerId" style="display: none;">
									<table>
										<s:textfield label="Retailer User Id" name="retId" id="retId"
											maxlength="5" size="6"
											onkeydown="return onlyIntegers(event);"></s:textfield>
									</table>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								Game Name <span class="required">*</span>:
								<s:select name="gameNo" id="gameNo" theme="simple"
									headerKey="-1" headerValue="--Please Select--"
									list="#session.DRAWGAME_LIST" cssClass="option" />
							</td>
						</tr>
						<tr>
							<td>
								Transaction No <span class="required">*</span> :
								<input type="text" name="transNo" id="transNo"
									onpaste="return false;" maxlength="4" size="5"
									onkeydown="return onlyIntegers(event);" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
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
												Sale Date
												<span class="required">*</span>:&nbsp;
											</label>
											<input type="text" name="sale_date" id="start_date"
												value="<%=startDate%>" readonly size="12">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(_id.o('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false,'')" />
										</td>
									</tr>

								</table>
							</td>
						</tr>
					</table>
					<tr>
						<td>
							<s:submit name="search" value="Search" id="search" align="left"
								targets="down" theme="ajax" action="fetchDrawTransTktStatus"
								cssClass="button" onclick="return validateData()" />
						</td>
					</tr>
				</s:form>

				<div id="down" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_trackTransactionTicketMenu.jsp,v $ $Revision:
	1.1.2.2.4.3 $
</code>