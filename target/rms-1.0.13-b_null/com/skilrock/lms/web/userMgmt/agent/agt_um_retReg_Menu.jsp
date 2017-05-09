<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.css"
			media="screen" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%> <%!String isOffline = null;%>

		</title>
		<s:head theme="ajax" debug="false" />
<script type="text/javascript">
			path="<%=request.getContextPath()%>" ; </script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.js"></script>

		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/orgNUserReg.js"></script>
		<script>
		var terminalData='';
var invRspDatasim1='';
var invRspDatasim2='';
var invRspDatasim3='';
		function checkTerminal(modelName){
			_id.o('terminalId').value = '';
			AutoComplete_Create('terminalId', '');
			if(modelName != ''){
				_resp  = _ajaxCall('fetchTerminalSerialNos.action?modelName=' + modelName);
				terminalData = _resp.data;
				var data = _resp.data.split(':');
				AutoComplete_Create('terminalId', data);
			}
		}
		function fillInvIds(modelName,id){
			_id.o(id).value = '';
			AutoComplete_Create(id, '');
			if(modelName != ''){
					_resp  = _ajaxCall('fetchTerminalSerialNos.action?modelName=' + modelName);
				 invRspData = _resp.data;
				 
				 if(id=='sim1'){
					invRspDatasim1=_resp.data;
				}else if(id=='sim2'){
					invRspDatasim2=_resp.data;
				}
				else{
					invRspDatasim3=_resp.data;
			
				}
				var invdata = _resp.data.split(':');
					AutoComplete_Create(id, invdata);
					}
				}		
		function fetchCityList() {
			var country = _id.o('country').value;
			var state = _id.o('plrState').value;
	 		_ajaxCall("getCity.action?country=" + country + "&state=" + state,'charactersCity');
	 		 document.getElementById('plrCity').selectedIndex=1;
	 		 if(typeof $('#area').val()	 !=='undefined'){
		 		 fetchAreaList();
		 		 }
		}
		
		function selectState(){
			 _id.o('plrState').selectedIndex=1;
			 fetchCityList();
		}
		
		function fetchCity(){
		_id.o('plrCity').selectedIndex=1;
		fetchCityCode();
		}
		function fetchCityCode(){
		var city = _id.o('plrCity').value;
		var _res=_ajaxCall('getCityCode.action?city=' +city);
		_id.o('cityCode').value=_res.data;
		}
		
		function fetchTerminalModelNames(){
			if(_id.o('terminalId')){
				_resp  = _ajaxCall('fetchTerminalModelNames.action');
				var dataArr = _resp.data.split(':');
				var modelId = document.getElementById("modelId");
				modelId.options.length=1;
					if(dataArr[0].trim()!=""){
				for(var i = 0; i < dataArr.length ; i++){
					modelId.add(new Option(dataArr[i],dataArr[i],null) );
				}
				}
			}
			
			if (document.getElementById("sim1")) {
			_resp  = _ajaxCall('fetchInvModelNames.action');
			var dataArr = _resp.data.split(':');
			var simModelName =document.getElementsByName("simModelName");
			if(dataArr[0].trim()!=""){
			
			for(var j=0;j<simModelName.length;j++){
				
				simModelName[j].options.length=1;
				
				for(var i = 0; i < dataArr.length ; i++){
					simModelName[j].add(new Option(dataArr[i],dataArr[i],null) );
				}
			}
			
			
			
		}
		
		
	}
			
			
		}
		function fetchAreaList(){
			 if(typeof $('#area').val()	 !=='undefined'){
			getAreaListWithCode(_id.o('country').value, _id.o('plrState').value,_id.o('plrCity').value);
			 if(document.getElementById('area').length >1){
		  				document.getElementById('area').selectedIndex=1;
		 				}
		 		
		
		 		 		fetchDivisonList();
			}
		}
		
		function getAreaListWithCode(countryCode, stateCode,cityCode) {

				$('#area').html("<option value='-1'>--Please Select--</option>");
				if (countryCode == "-1" || stateCode == "-1") {

							$('#area').val("-1");
				} else {

				var url = path+ "/com/skilrock/lms/web/userMgmt/getAreaListWithCode.action?country="+ countryCode + "&state=" + stateCode+ "&city=" + cityCode;
		$.get(url, function(data, status) {
			fillAreaList(data, status);
		}

		);

	}

}

function fillAreaList(data, status) {
	
	if (data != "" && status == "success") {

		var options = '';
		$('#area').append($(options));
		var list = data.split("|");
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#area').append($(options));
		$('#area').val(list[0].split("|")[0]);
		$('#area').val("-1");
		
	}
}
function fetchDivisonList(){

	 if(typeof $('#division').val()	 !=='undefined'){
		 		getDivisionList(_id.o('country').value, _id.o('plrState').value,_id.o('plrCity').value,_id.o('area').value)
		 		 }

}
function getDivisionList(countryCode,stateCode,cityCode,areaCode) {

			$('#division').html("<option value='-1'>--Please Select--</option>");
				if (countryCode == "-1" || stateCode == "-1" || cityCode=="-1" ||areaCode =="-1" ) {

							$('#division').val("-1");
				} else {
	var url = path+ "/com/skilrock/lms/web/userMgmt/getDivisionListWithCode.action?country="+ countryCode + "&state=" + stateCode+ "&city=" + cityCode+"&area="+areaCode;
		$.get(url, function(data, status) {
		
	if (data != "" && status == "success") {

		var options = '';
		$('#division').append($(options));
		var list = data.split("|");
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#division').append($(options));
		$('#division').val(list[0].split("|")[0]);
		$('#division').val("-1");
		
	}
	
				
		}
		);
		
		}
}
		</script>
	</head>

	<body
		onload="_id.finiFocus('na','orgName'),fetchTerminalModelNames(),onLoadAjax('getListAjax.action?listType=country','country'),fetchScrapNOrgLimits('agt_um_retReg_fetScrapNLimAjx.action'),selectState(),fetchCity();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" />
					Registration:
					<s:property value="#application.TIER_MAP.RETAILER" />
					Organization and User Registration
				</h3>
				<s:hidden id="srap_sts" name="scrapsts"
					value="%{#session.USER_INFO.pwtSacrap}" />

				<%--<s:form  name="na"  onsubmit="return isST3Filled();" action="saveRetailerOrg" >--%>

				<s:form name="na" onsubmit="return isST3Filled()"
					action="agt_um_retReg_Save">

					<table border="1" width="780px" cellspacing="0" cellpadding="5"
						bordercolor="#CCCCCC">
						<tr>
							<td valign="top">
								<table border="0" width="390px" cellspacing="0" cellpadding="2">
									<tr>
										<th align="left" colspan="2">
											Organization Details : -
										</th>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="org" style="color: red">
												<s:property value="errorMap.orgError" />
											</div>
										</td>
									</tr>
									<tr>
										<td nowrap="nowrap">
											<s:textfield label="Organization Name" maxlength="30"
												name="orgName" id="org_name" required="true" />
										</td>
									</tr>

									<tr style="display: none;">
										<td nowrap="nowrap">
											<s:hidden theme="simple" name="orgType" value="RETAILER" />
											<s:textfield theme="simple" label="Organization Type"
												readonly="true" maxlength="30"
												value="%{#application.TIER_MAP.RETAILER.toUpperCase()}"
												required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="addr1"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="Address Line1" maxlength="30"
												name="addrLine1" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="addr2"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="Address Line2" maxlength="30"
												name="addrLine2" />
										</td>
									</tr>



									<s:hidden id="country_deployed" value="%{#application.COUNTRY_DEPLOYED}" />
									<tr>
										<td align="center" colspan="2">
											<div id="orgcountry"></div>
										</td>
									</tr>
									<tr style="display: none;">
										<td>
											<s:select theme="simple" label="Country" id="country"
												name="country" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												onchange="_ajaxCall('getState.action?country=' + this.value,'characters')"
												cssClass="option" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgstate"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												State
												<span class="required">*</span>:
											</label>
										</td>
										<td align="left">
											<span id="characters"> <select class="option"
													id="state" name="state" onchange="return fetchCityList()">
													<option value="----Please Select----">
														----Please Select----
													</option>
												</select> </span>
										</td>
									</tr>
									<tr>
										<td align="center" colspan="2">
											<div id="orgcity"></div>
										</td>
									</tr>

									<tr>
										<td align="right">
											<label class="label">
												City
												<span class="required">*</span>:
											</label>
										</td>
										<td align="left">
											<span id="charactersCity"> <select class="option"
													name="city" id="city">
													<option value="----Please Select----">
														----Please Select----
													</option>
												</select> </span>
										</td>
									</tr>
									
									<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
									<tr>
										<td align="center" colspan="2">
											<div id="area_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												Zone
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="area"
												name="area" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  />
										</td>
										
									</tr>
									
										<!--<tr>
										<td align="right">
											<label class="label">
												Area
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="area"
												name="area" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  onchange ="fetchDivisonList()"/>
										</td>
										
									</tr>
									<tr>
										<td align="center" colspan="2">
											<div id="division_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									
									<tr>
										<td align="right">
											<label class="label">
												Divison 
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="division"
												name="division" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  />
										</td>
										
									</tr>
									--></s:if>
									<tr>
										<td align="center" colspan="2">
											<div id="orgpin"></div>
										</td>
									</tr>
									<tr style="display: none;">
										<td>
											<s:textfield theme="simple" label="PO Box No." maxlength="10"
												name="pin" value="123456" id="Pin" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orglimit"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="Credit Limit" maxlength="10"
												name="creditLimit" id="Limit" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgsecurity"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label=" Security Deposit" maxlength="9"
												name="security" id="Security" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="vatReg">
											</div>
										</td>
									</tr>
									<s:if test="%{vat_applicable=='yes'}">
										<tr>
											<td>
												<s:textfield label="Vat Registration No." value=""
													name="vatRegNo" maxlength="20" />
											</td>
										</tr>
									</s:if>
									<s:else>
										<tr style="display: none;">
											<td>
												<s:textfield theme="simple" label="Vat Registration No."
													value="" name="vatRegNo" maxlength="20" />
											</td>
										</tr>
									</s:else>
									<tr>
										<td>
											<s:textfield name="autoScrap" label="Auto Scrap" value="YES"
												cssClass="option" required="true" readonly="true" />
										</td>
									</tr>
									<s:hidden name="reconReportType" value="Book Wise Report"></s:hidden>
									<!--
								 <tr>
										<td>
											<s:select name="reconReportType"
												label="Reconciliation Report" list="{'Ticket Wise Report'}"
												cssClass="option" />
										</td>
									</tr> -->

									<tr style="display: none;">
										<td>
											<s:textfield theme="simple" label="Organization Status"
												readonly="true" name="statusorg" value="ACTIVE"
												required="true" />
										</td>
									</tr>


								</table>
							</td>

							<%--		    
		    <tr>
		    	<td></td>
		    	<td align="left">
		         	<table><s:submit name="sub" value="Next" align="right" cssClass="button"/></table>
		        </td>
			</tr>
			This code added because of registering retailers on a single page 
--%>
							<td>
								<table border="0" width="390px" cellspacing="0" cellpadding="2">
									<tr>
										<th align="left" colspan="2">
											Personal Details : -
										</th>
									</tr>

									<s:actionerror />
									<s:fielderror />
									<tr>
										<td align="center" colspan="2">
											<div id="fname"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="First Name" maxlength="30"
												name="firstName" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="lname"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="Last Name" maxlength="30" name="lastName"
												required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="user" style="color: red">
												<s:property value="errorMap.userError" />
											</div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												<i>Login Name</i>&nbsp;
												<span class="required">*</span>:
											</label>
										</td>
										<td align="center">
											<s:textfield label="Login Name" theme="simple" maxlength="30"
												name="userName" cssStyle="float:left" id="user_name" />
											<a href="#"
												style="font-weight: 900; color: orange; margin-left: 3px"
												onclick="checkAvlOrgUser()">Check Availability</a>
										</td>
									</tr>


									<tr>
										<td align="center" colspan="2">
											<div id="Email"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="Email" maxlength="50" name="email"
												required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="Phone"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												<i>LandLine Number</i>&nbsp;
											</label>
										</td>
										<td align="center">
											<s:textfield theme="simple" name="cityCode" id="cityCode"
												cssStyle="width:24px;float:left;" required="true"
												readonly="true" /><b>-</b>
											<input type="text" maxlength="12" name="phone" id="phone"
												style="margin-right: 100px;" />
										</td>
									</tr>
									<tr>
										<td align="center" colspan="2">
											<div id="Mobile"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												<i>Mobile Number</i>&nbsp;
												<span class="required">*</span>:
											</label>
										</td>
										<td align="center">
											<s:textfield theme="simple" name="code" id="code"
												required="true" cssStyle="width:24px;float:left;"
												readonly="true" /><b>-</b>
											<input type="text" maxlength="12" name="mobile" id="mobile"
												style="margin-right: 100px;" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="id_type_error"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:select label="Id Type" name="idType"
												list="{'Passport','Driving Licence','NID','Pan Card'}"
												cssClass="option" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="id_no_error" style="color: red">
												<s:property value="errorMap.idError" />
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield label="Id Number" name="idNo" id="id_no"
												maxlength="15" required="true" />
										</td>
									</tr>

									<tr style="display: none;">
										<td>
											<s:textfield theme="simple" label="Role " readonly="true"
												name="role" required="true" />
										</td>
									</tr>

									<tr style="display: none;">
										<td>
											<s:textfield theme="simple"
												label="%{#application.TIER_MAP.RETAILER} Status"
												readonly="true" name="status" value="ACTIVE" />
										</td>
									</tr>
									<s:if test="%{#application.RET_OFFLINE=='YES'}">
										<tr>
											<td>
												<s:select name="isOffLine" label="Off Line Mode"
													list="{'NO','YES'}" cssClass="option" required="true" />
											</td>
										</tr>
									</s:if>
									<s:if test="%{#application.LOGIN_BINDING=='YES'}">
											
											<tr>
												<td align="center" colspan="2">
													<div id="termDiv"></div>
												</td>
											</tr>
											<tr>
												<td colspan="2">
												<table>
												<tr>
												<td align=right>
													<label class="label">
														<i>Terminal Model <span class="required">*</span>:</i>
													</label>
												</td>

												<td align="left">
													<select id="modelId" name="modelName" class="option"
														onchange="checkTerminal(this.value)">
														<option value="-1">
															---Please Select---
														</option>
													</select>
												</td>
												<td></td>
												<td align="right" class="label">
													<label class="label">
														Terminal Id
														<span class="required">*</span>:
													</label>	
												</td>
												<td align="left">
													<input type="text" id="terminalId" name="terminalId"
														size="20" />
												</td>
												</tr>
												</table>
												</td>
												
											</tr>
										<!-- 	<tr>
												<td align="center" colspan="2">
													<div id="termDiv"></div>
												</td>
											</tr> 

											<tr>
												
												<td align="left">
												</td>
											</tr>-->
										</s:if>
										<s:if test="%{#application.sim_binding=='YES'}">
											
											<tr>
												<td align="center" colspan="2">
													<div id="simDiv"></div>
												</td>
											</tr>
											<tr>
												<td colspan="2">
												<table>
												<tr>
												<td align=right>
													<label class="label">
														<i>Sim Model <span class="required">*</span>:</i>
													</label>
												</td>

												<td align="left">
												
												<s:select theme="simple"  id="modelId1"
												name="simModelName" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  onchange="fillInvIds(this.value,'sim1')"/>
													<%-- <select id="modelId1" name="simModelName" class="option"
														onchange="fillInvIds(this.value,'sim1')">
														<option value="-1">
															---Please Select---
														</option>
													</select> --%>
												</td>
												<td></td>
												<td align="right" class="label">
													<label class="label">
														Sim Id
														<span class="required">*</span>:
													</label>	
												</td>
												<td align="left">
													<input type="text" id="sim1" name="sim"
														size="20" />
												</td>
												</tr>
												</table>
												</td>
												
											</tr>
											<tr>
												<td colspan="2">
												<table>
												<tr>
												<td align=right>
													<label class="label">
														<i> Sim Model <span class="required">*</span>:</i>
													</label>
												</td>

												<td align="left">
													<s:select theme="simple"  id="modelId1"
												name="simModelName" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  onchange="fillInvIds(this.value,'sim2')"/>
												<%-- 	<select id="modelId2" name="simModelName" class="option"
														onchange="fillInvIds(this.value,'sim2')">
														<option value="-1">
															---Please Select---
														</option>
													</select> --%>
												</td>
												<td></td>
												<td align="right" class="label">
													<label class="label">
														Sim Id
														<span class="required">*</span>:
													</label>	
												</td>
												<td align="left">
													<input type="text" id="sim2" name="sim"
														size="20" />
												</td>
												</tr>
												</table>
												</td>
												
											</tr>
											<tr>
												<td colspan="2">
												<table>
												<tr>
												<td align=right>
													<label class="label">
														<i>Sim Model <span class="required">*</span>:</i>
													</label>
												</td>

												<td align="left">
													<s:select theme="simple"  id="modelId1"
												name="simModelName" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  onchange="fillInvIds(this.value,'sim3')"/>
													<%-- <select id="modelId3" name="simModelName" class="option"
														onchange="fillInvIds(this.value,'sim3')">
														<option value="-1">
															---Please Select---
														</option>
													</select> --%>
												</td>
												<td></td>
												<td align="right" class="label">
													<label class="label">
														Sim Id
														<span class="required">*</span>:
													</label>	
												</td>
												<td align="left">
													<input type="text" id="sim3" name="sim"
														size="20" />
												</td>
												</tr>
												</table>
												</td>
												
											</tr>
										<!-- 	<tr>
												<td align="center" colspan="2">
													<div id="termDiv"></div>
												</td>
											</tr> 

											<tr>
												
												<td align="left">
												</td>
											</tr>-->
										</s:if>
									<tr>
										<td align="center" colspan="2">
											<div id="sQues"></div>
										</td>
									</tr>
									<tr>
										<td align="center" colspan="2">
											<s:select label="Secret Question " name="secQues"
												headerKey="-1" headerValue="-- Please Select --"
												list="{'What is Your Pets Name?','Which is your fav game?','What is your college name?'}"
												value="gameType" required="true" cssClass="option" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="sAns"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield name="secAns" maxlength="30"
												label="Secret Answer" required="true" />
										</td>
									</tr>

								</table>
							</td>
						</tr>
					</table>



					<!-- <br /> -->
					<s:div theme="ajax" id="serviceDiv"
						href="agt_um_retReg_fillService.action"
						cssStyle="visibility:hidden;position:absolute;">

					</s:div>

					<!-- <br /> -->

					<table border="1" width="100%" cellspacing="0" cellpadding="2"
						style="visibility: hidden; display: none;" bordercolor="#CCCCCC">
						<tr>
							<th colspan="4" align="center">
								<s:property value="#application.TIER_MAP.RETAILER" />
								Limit Values
							</th>
						</tr>
						<tr>
							<td class="label" align="right">
								Verification Limit&nbsp;:
							</td>
							<td>
								<table>
									<s:textfield name="verLimit" id="verLimit" readonly="true"
										value="%{#application.retVerLimit}" />
								</table>
							</td>
							<td class="label" align="right">
								Approval Limit&nbsp;:
							</td>
							<td>
								<table>
									<s:textfield name="appLimit" id="appLimit" readonly="true"
										value="%{#application.retAppLimit}" />
								</table>
							</td>
						</tr>
						<tr>
							<td class="label" align="right">
								Payment Limit&nbsp;:
							</td>
							<td>
								<table>
									<s:textfield name="payLimit" id="payLimit" readonly="true"
										value="%{#application.retPayLimit}" />
								</table>
							</td>
							<td class="label" align="right">
								Scrap Limit&nbsp;:
							</td>
							<td>
								<table>
									<s:textfield name="scrapLimit" id="scrapLimit" readonly="true"
										value="%{#application.retScrapLimit}" />
								</table>
							</td>
							<td class="label" align="right">
								Ola Deposit Limit&nbsp;:
							</td>
							<td>
								<table>
									<s:textfield name="olaDepositLimit" id="olaDepositLimit"
										readonly="true" value="%{#application.retDepositLimit}" />
								</table>
							</td>
							<td class="label" align="right">
								Ola Withdrawal Limit&nbsp;:
							</td>
							<td>
								<table>
									<s:textfield name="olaWithdrawalLimit" id="olaWithdrawalLimit"
										readonly="true" value="%{#application.retWithdrawalLimit}" />
								</table>
							</td>

						</tr>
					</table>

					<table border="0" width="720px" cellspacing="0" cellpadding="0">
						<tr>
							<td style="width: 400px"></td>
							<td align="left" style="width: 390px">
								<table>
									<s:submit value="Register" align="right" cssClass="button" />
								</table>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: agt_um_retReg_Menu.jsp,v $ $Revision: 1.7 $
</code>