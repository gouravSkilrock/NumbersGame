<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript">
			path="<%=request.getContextPath()%>" ; </script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/orgNUserReg.js"></script>
		<script>
			function fetchCityList() {
				var country = _id.o('country').value;
				var state = _id.o('plrState').value;
		 		_ajaxCall("getCity.action?country=" + country + "&state=" + state,'charactersCity');
		 if(document.getElementById('plrCity').length >1){
		  document.getElementById('plrCity').selectedIndex=1;
		 }
		 		
		 		 			 if(typeof $('#area').val()	 !=='undefined'){
		 		 fetchAreaList();
		 		 }
			}
			function selectState(){
			 document.getElementById('plrState').selectedIndex=1;
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
		 //$('#area').val(list[0].split(":")[0]);
		 $('#area').val("-1");
		
	}
}






		</script>
	</head>


	<body
		onload="onLoadAjax('getListAjax.action?listType=country','country'), getEmailPrivList('bo_um_registration_FetchEmailPriv.action?privType=AGENT', 'emailPriv'),fetchScrapNOrgLimits('bo_um_agtReg_fetScrapNLimAjx'),selectState(),fetchCity()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<s:hidden id="country_deployed" value="%{#application.COUNTRY_DEPLOYED}"/>	
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.AGENT" />
					<s:text name="label.reg" />
					:
					<s:property value="#application.TIER_MAP.AGENT" />
					<s:text name="label.org.user.reg" />
				</h3>

				<s:form onsubmit="return isST3Filled()" name="na"
					action="bo_um_agentReg_Save">
					<table border="1" width="780px" cellspacing="0" cellpadding="2"
						bordercolor="#CCCCCC">
						<tr>
							<td valign="top">
								<table border="0" width="390px" cellspacing="0" cellpadding="2">
									<tr>
										<th align="left" colspan="2">
											<s:text name="label.org.detail" />
											: -
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
											<s:textfield key="label.org.name" maxlength="25"
												name="orgName" id="org_name" required="true" />
										</td>
									</tr>

									<tr style="display: none;">
										<td nowrap="nowrap">
											<s:hidden theme="simple" name="orgType" value="AGENT" />
											<s:textfield theme="simple" key="label.org.type"
												readonly="true" maxlength="30"
												value="%{#application.TIER_MAP.AGENT.toUpperCase()}"
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
											<s:textfield key="label.adrs.line1" maxlength="30"
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
											<s:textfield key="label.adrs.line2" maxlength="30"
												name="addrLine2" />
										</td>
									</tr>



									<tr>
										<td align="center" colspan="2">
											<div id="orgcountry"></div>
										</td>
									</tr>
									<tr style="display: none;">
										<td>
											<s:select theme="simple" key="label.country" id="country"
												name="country" headerKey="-1"
												headerValue="%{getText('label.please.select')}" list="{}"
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
												<s:text name="label.state" />
												<span class="required">*</span>:
											</label>
										</td>
										<td align="left">
											<span id="characters"> <select class="option"
													id="state" name="state" onchange="return fetchCityList()">
													<option value="<s:text name="label.please.select" />">
														<s:text name="label.please.select" />
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
												<s:text name="label.city" />
												<span class="required">*</span>:
											</label>
										</td>
										<td align="left">
											<span id="charactersCity"> <select class="option"
													name="city" id="city" onchange="return fetchAreaList()">
													<option value="<s:text name="label.please.select" />">
														<s:text name="label.please.select" />
													</option>
												</select> </span>
										</td>
									</tr>

									<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
										<tr>
											<td align="center" colspan="2">
												<div id="area_e" style="color: red; text-align: center"></div>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="label">
													<s:text name="Area" />

												</label>
											</td>
											<td>
												<s:select theme="simple" id="area" name="area"
													headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
													cssClass="option" />
											</td>

										</tr>
										<!-- 
									
									<tr>
										<td align="center" colspan="2">
											<div id="area_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												Area
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="area"
												name="area" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option" onchange=" fetchDivisonList()"  />
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
									-->
									</s:if>
									<tr>
										<td align="center" colspan="2">
											<div id="orgpin"></div>
										</td>
									</tr>
									<tr style="display: none;">
										<td>
											<s:textfield theme="simple" key="label.po.box" maxlength="10"
												name="pin" id="Pin" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orglimit"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield key="label.crdtlmt" maxlength="10"
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
											<s:textfield key="label.secur.dpst" maxlength="9"
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
												<s:textfield key="label.vat.reg.no" value="" name="vatRegNo"
													maxlength="20" />
											</td>
										</tr>
									</s:if>
									<s:else>
										<tr style="display: none;">
											<td>
												<s:textfield theme="simple" key="label.vat.reg.no" value=""
													name="vatRegNo" maxlength="20" readonly="true" />
											</td>
										</tr>
									</s:else>

									<tr style="display: none;">
										<td>
											<s:textfield theme="simple" key="label.org.status"
												readonly="true" name="statusorg" value="ACTIVE" />
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield name="autoScrap" key="label.autoscrap"
												value="YES" cssClass="option" required="true"
												readonly="true" />
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
							<td valign="top">
								<table border="0" width="390px" cellspacing="0" cellpadding="2">
									<tr>
										<th align="left" colspan="2">
											<s:text name="label.personal.detail" />
											: -
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
											<s:textfield key="label.fname" maxlength="30"
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
											<s:textfield key="label.lname" maxlength="30" name="lastName"
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
												<i><s:text name="label.login.name" />:</i><span
													class="required">*</span>:
											</label>

										</td>
										<td align="left">
											<s:textfield theme="simple" key="label.login.name"
												maxlength="30" name="userName" id="user_name" />
											<a href="#"
												style="font-weight: 900; color: orange; margin-left: 3px"
												onclick="checkAvlOrgUser()"><s:text
													name="label.check.avail" /> </a>
										</td>
									</tr>


									<tr>
										<td align="center" colspan="2">
											<div id="Email"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:textfield key="label.email" maxlength="50" name="email"
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
												<i><s:text name="label.land.no" /> </i>&nbsp;
											</label>
										</td>
										<td align="center">
											<s:textfield theme="simple" name="cityCode" id="cityCode"
												cssStyle="width:24px;float:left;" required="true"
												readonly="true" />
											<b>-</b>
											<input type="text" maxlength="12" name="phone" id="phone"
												style="margin-right: 65px;" />
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
												<i><s:text name="label.mob.no" /> </i>&nbsp;
												<span class="required">*</span>:
											</label>
										</td>
										<td align="center">
											<s:textfield theme="simple" name="code" id="code"
												required="true" cssStyle="width:24px;float:left;"
												readonly="true" />
											<b>-</b>
											<input type="text" maxlength="12" name="mobile" id="mobile"
												style="margin-right: 65px;" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2" style="color: red">
											<div id="id_type_error"></div>
										</td>
									</tr>
									<tr>
										<td>
											<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
												<s:select key="label.id.type" name="idType"
													list="#{'IFU CODE':getText('IFU.CODE')}"
													cssClass="option" required="false" />
											</s:if>
											<s:else>
												<s:select key="label.id.type" name="idType"
													list="#{'Passport':getText('label.passport'),'Driving Licence':getText('label.drivng.licence'),'NID':getText('label.nid'),'Pan Card':getText('label.pan.card')}"
													cssClass="option" required="true" />
											</s:else>
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2" style="color: red">
											<div id="id_no_error">
												<s:property value="errorMap.idError" />
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
												<s:textfield key="label.id.no" name="idNo" id="id_no"
													maxlength="13" required="false" />
											</s:if>
											<s:else>
												<s:textfield key="label.id.no" name="idNo" id="id_no"
													maxlength="15" required="true" />
										</s:else>
										</td>
									</tr>


									<!-- ADDED FOR BANK DETAILS -->

									<s:if test="%{agtBnkMapping == true}">
										<tr>
											<td align="center" colspan="2">
												<div id="bankDiv" style="color: red; text-align: center"></div>
											</td>
										</tr>

										<tr>
											<td align="right">
												<label class="label">
													<s:text name="label.slct.bank" />
													<span class="required">*</span>:
												</label>
											</td>
											<td>
												<s:hidden name="bankId" id="bankId"></s:hidden>
												<s:select theme="simple" id="bank" name="bank"
													headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
													cssClass="option" required="true" />
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="branchDiv" style="color: red; text-align: center"></div>
											</td>
										</tr>
										<tr id="branchRow">
											<td align="right">
												<label class="label">
													<s:text name="label.slct.branch" />
													<span class="required">&nbsp;&nbsp;&nbsp;
														<div id="req" style="margin-top: -12px;">
															*:
														</div> </span>
												</label>
											</td>
											<td>
												<s:hidden name="branchId" id="branchId"></s:hidden>
												<s:select theme="simple" id="branch" name="branch"
													headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
													cssClass="option" required="true" />
											</td>
										</tr>

										<tr>

											<td colspan="2">
												<s:textfield name="sortCode" id="sortCode" maxlength="30"
													disabled="true" key="label.brnch.sort.code"></s:textfield>
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="accountDiv" style="color: red; text-align: center"></div>
											</td>
										</tr>
										<tr>

											<td colspan="2">
												<s:textfield name="accountNbr" id="accountNbr"
													maxlength="15" key="label.entr.acc.no" required="true"></s:textfield>
											</td>
										</tr>
									</s:if>
									<!-- END FOR BANK DETAILS -->

									<tr style="display: none;">
										<td>
											<s:textfield theme="simple" key="label.role" readonly="true"
												name="role" required="true" />
										</td>
									</tr>

									<tr style="display: none;">
										<td>
											<table>
												<tr>
													<td>
														<s:text name="%{#application.TIER_MAP.AGENT}" />
														<s:text name="label.status" />
													</td>
													<td>
														<s:textfield theme="simple" readonly="true" name="status"
															value="ACTIVE" required="true" />
													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="sQues"></div>
										</td>
									</tr>
									<tr>
										<td align="center" colspan="2">
											<s:select key="label.secquestion" name="secQues"
												headerKey="-1" headerValue="%{getText('label.please.select')}"
												list="#{'What is Your Pets Name?':getText('whats.ur.pets.name'),'Which is your fav game?':getText('which.is.ur.fav.game'),'What is your college name?':getText('what.is.ur.college.name')}"
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
												key="label.secanswer" required="true" />
										</td>
									</tr>


								</table>
							</td>
						</tr>
					</table>
					<s:div theme="ajax" id="serviceDiv"
						href="bo_um_agentReg_fillService.action"
						cssStyle="visibility:hidden;position:absolute;">
					</s:div>




					<table border="1" width="795px" cellspacing="0" cellpadding="2"
						bordercolor="#CCCCCC" style="visibility: hidden; display: none;">

						<tr>
							<td align="right" width="180">
								<label class="label">
									<s:text name="label.email.privileges" />
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<div id="emailPriv"></div>
							</td>
						</tr>
						<s:hidden name="reconReportType" value="Book Wise Report"></s:hidden>
						<!--	<tr>
							<td>
							  
								<s:select name="reconReportType" label="Reconciliation Report"
									list="{'Book Wise Report' , 'Ticket Wise Report'}"
									cssClass="option" />
								
							</td>
						</tr>-->
					</table>



					<s:div cssStyle="visibility:hidden;position:absolute;">
						<table border="1" width="100%" cellspacing="0" cellpadding="2"
							bordercolor="#CCCCCC">
							<tr>
								<th colspan="4" align="center">
									<s:property value="#application.TIER_MAP.AGENT" />
									<s:text name="label.limit.values" />
								</th>
							</tr>
							<tr>
								<td class="label" align="right">
									<s:text name="label.verify.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="verLimit" id="verLimit" readonly="true"
											value="%{#application.agtVerLimit}" />
									</table>
								</td>
								<td class="label" align="right">
									<s:text name="label.approve.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="appLimit" id="appLimit" readonly="true"
											value="%{#application.agtAppLimit}" />
									</table>
								</td>
							</tr>
							<tr>
								<td class="label" align="right">
									<s:text name="label.payment.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="payLimit" id="payLimit" readonly="true"
											value="%{#application.agtPayLimit}" />
									</table>
								</td>
								<td class="label" align="right">
									<s:text name="label.scrap.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="scrapLimit" id="scrapLimit" readonly="true"
											value="%{#application.agtScrapLimit}" />
									</table>
								</td>

							</tr>
						</table>
					</s:div>
					<table border="0" width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td align="left">
								<table align="center" border="0">
									<s:submit key="btn.reg" align="left" cssClass="button" />
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
	$RCSfile: bo_um_agentReg_Menu.jsp,v $ $Revision:
	1.6.2.1.4.5.2.16.4.5.2.2 $
</code>