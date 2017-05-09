<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.beans.OrganizationBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
	<head>

	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	
<%ArrayList<String> userActionList=(ArrayList<String>)session.getAttribute("ACTION_LIST"); %>
<s:head theme="ajax" debug="false"/>
	<script type="text/javascript">
			path="<%=request.getContextPath()%>" ; </script>
<script type="text/javascript">
function isValuesValid(){
 var frmVal  = new parent.frames[0].Validator("orgDetails");

  frmVal.aV("orgAddr1","req",'<s:text name="error.addline1.empty"/>',"addr1");
  frmVal.aV("orgAddr1","regexp=[^A-Za-z0-9\\s/,-]",'<s:text name="error.enter.valid.addline1"/>1',"addr1");
  frmVal.aV("orgAddr2","regexp=[^A-Za-z0-9\\s/,-]",'<s:text name="error.enter.valid.addline1"/>2',"addr1");
  //frmVal.aV("ifuCode", "minlen=13",  i18nOb.prop('error.id.empty'), "ifuCode");
  //frmVal.aV("ifuCode", "regexp=[^0-9\\s/,-]", i18nOb.prop('error.id.empty'), "ifuCode");
  frmVal.aV("orgCity","req",'<s:text name="error.blank.city"/>',"orgcity1");
  frmVal.aV("orgCity","regexp=[^A-Za-z\\s\/\-]",'<s:text name="error.blank.city"/>',"orgcity1");
   if(document.getElementById("area") !='undefined' && document.getElementById("area") !=null){
		 frmVal.aV("area", "dontselect=0", '<s:text name="error.blank.zone"/>', "area_e");	  
		 
	 }
	 if(document.getElementById("division")	 !='undefined' && document.getElementById("division")!=null){
		 frmVal.aV("division", "dontselect=0", '<s:text name="error.blank.division"/>', "division_e");	  
		 
	 }
  //frmVal.aV("orgPin","req","Please Enter Pin","pin");
  //frmVal.aV("orgPin","numeric","Please Enter Correct Value For Pin","pin");

  frmVal.aV("orgSecurityDeposit","req",'<s:text name="error.creditlimit.empty"/>',"security");
  frmVal.aV("orgSecurityDeposit","decimal",'<s:text name="error.creditlimit.empty"/>',"security");

  frmVal.aV("orgCreditLimit","req",'<s:text name="error.creditlimit.empty"/>',"limit");
  frmVal.aV("orgCreditLimit","decimal",'<s:text name="error.creditlimit.empty"/>',"limit");
 // frmVal.aV("comment","req",'<s:text name="error.enter.valid.reason"/>',"cmmntErr");
  
  
  var orgCreditLimit = $("#orgCreditLimit").val() ;
  var comment = $("#comment").val() ;
var orgExtendedCreditLimit = $("#orgExtendedCreditLimit").val();
var terminalCount = $("#terminalCount").val();
var outstandingBal = $("#outstandingBal").val() ;
 var status = $("#Status :selected").text() ;
 
if(comment.trim() == "" )
	{
	$("#cmmntErr").html("<font color='red'>Please enter comment.</font> ");
			$("#cmmntErr").show() ;
	return false ;
	}
	else
	{
	$("#cmmntErr").hide() ;
	return true;
	}

			
			
	 
	//return false;
	//return document.error_disp_handler.isValid;
 }
 

  




</script>	
	<script>
function checkValidation()
{
     var flag=true;
     var afterDays=document.getElementById('daysAfterId').value;
     var afterDaysValidity=isNumeric(afterDays);
     
     var extendedAmount=document.getElementById('extendLimit').value;
 //alert(extendedAmount);
     var extendedAmtValidity=isNumericAmt(extendedAmount);
    // alert(extendedAmtValidity);
      if(afterDaysValidity==true || afterDays=="" || afterDays==0)
      {
       document.getElementById('daysAfterId1').innerHTML = "<font color = 'red'><s:text name='error.enter.valid.days'/>(<s:text name='error.zero.not.allow'/>)</font>";
       flag=false;
      }  
      else if(afterDays < 1 || afterDays > 90)
      {
        document.getElementById('daysAfterId1').innerHTML ="<font color = 'red'><s:text name='error.extend.limit.min.one.max.ninty.day'/></font> ";
        flag= false;
      }else
      document.getElementById('daysAfterId1').innerHTML ="";
      
      
      if(extendedAmtValidity==true || extendedAmount=="" || extendedAmount==".")
      {
      //alert("inside true" + extendedAmount);
       document.getElementById('extendLimit1').innerHTML = "<font color = 'red'><s:text name='error.enter.valid.amt'/></font>";
       flag=false;
      }  
     else if(extendedAmount==0)
     {
        document.getElementById('extendLimit1').innerHTML = "<font color = 'red'><s:text name='error.zero.val.amt.invalid'/></font>";
        flag=false;
      }
      
     else
      {    
           var array = new Array();
			array = extendedAmount.split(".");
			//alert(array.length);
			//alert(array[1].length);
			if(array.length >1 )
			 {
			   //alert(array[1].length);
			   if(array[1].length > 2 || array[1].length < 1)
			   {
			      document.getElementById('extendLimit1').innerHTML ="<font color = 'red'><s:text name='error.enter.upto.two.decimal.only'/></font>";
			      flag=false;
			    }
			      else
			       document.getElementById('extendLimit1').innerHTML ="";
			 }else
			 document.getElementById('extendLimit1').innerHTML ="";
         
      }
      

    return flag;
    
}

function makeEmpty(){
	document.getElementsByName('comment')[0].value= '';
	return true;
}

/*function chkOrgStatusNew(){
	var tdHide = document.getElementById('cmntTdId');
	if(document.getElementsByName('statusNew')[0].value != 'TERMINATE'){
		if(document.getElementsByName('orgStatus')[0].value != document.getElementsByName('statusNew')[0].value){
			document.getElementById('cmnt').innerHTML = '<input onclick="return makeEmpty()" type="textfield" name="comment" value="Enter a Valid Reason" />';
			tdHide.style.display = 'block';
		}else{
			document.getElementById('cmnt').innerHTML = '';
			tdHide.style.display = 'none';
		}
	}else{
		tdHide.style.display = 'none';
		document.getElementById('cmnt').innerHTML = '';
	}
	return true;
}*/

 function isNumeric(value) {
 	//alert("called");
 	var strValidChars = "1234567890";
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	
	return flag;
}

function isNumericAmt(value) {

 	var strValidChars = "1234567890.";
 	var dotCount=0;
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if(strChar==".")
		  dotCount=dotCount+1;
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	if(dotCount > 1 )
	 flag=true;
	return flag;
}

function submitExtendLimit(){
if(checkValidation()){
_resp  = _ajaxCall(createExtendUrl());
//alert(_resp.result+":"+_resp.data);
	if(_resp.result){
	_id.i('resDiv',_resp.data)
	}
}
}
function createExtendUrl(){
var newUrl = 'bo_um_searchAgtRet_ExtendCrLimit.action';
newUrl = newUrl+'?orgId='+_id.v("orgId")+'&extendedLimit='+_id.v("extendLimit")+'&daysAfter='+_id.v("daysAfterId");
//alert(newUrl);
return newUrl;
}

function submitResetExtendLimit(){

_resp  = _ajaxCall(createResetExtendUrl());
//alert(_resp.result+":"+_resp.data);
	if(_resp.result){
	_id.i('resDiv',_resp.data)
	document.getElementById('extendLimit').value=0.0;
	document.getElementById('daysAfterId').value=1;
	}
}

function createResetExtendUrl(){
var newUrl = 'bo_um_searchAgtRet_ExtendCrLimit.action';
newUrl = newUrl+'?orgId='+_id.v("orgId")+'&extendedLimit=0.0 & daysAfter=1';
//alert(newUrl);
return newUrl;
}

function fetchAreaList(){
					 if(typeof $('#area').val()	 !=='undefined'){
				getAreaListWithCode(_id.o('country').value, _id.o('plrState').value,_id.o('location').value);
				
		 		
		 		 		//  if(document.getElementById('area').length >1){
		  				//document.getElementById('area').selectedIndex=1;
		 				//}fetchDivisonList();
			}
			
			
		}
		
		function getAreaListWithCode(countryCode, stateCode,cityCode) {

				$('#area').html("<option value='-1'><s:text name='label.please.select'/></option>");
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
		 $('#area').val($('#areaValue').val());
		
	}
}

function fetchDivisonList(){

	 if(typeof $('#division').val()	 !=='undefined'){
		 		getDivisionList(_id.o('country').value, _id.o('plrState').value,_id.o('plrCity').value,_id.o('area').value)
		 		 }

}
function getDivisionList(countryCode,stateCode,cityCode,areaCode) {

			$('#division').html("<option value='-1'><s:text name='label.please.select'/></option>");
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
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
			$(document).ready(function() { 	
				if($("#Status").val() == "TERMINATE"){
					$(".hideButton").hide();
					$('form').submit(function(e){ e.preventDefault(); });									
				}
				if($("#country").val() == "GHANA"){
					$("#Status option[value='BLOCK']").hide();									
				}
				if($("#toTerminate").val() == "NO" && ($("#terminalCount").val() > '0' || $("#outstandingBal").val() > '0.00'))
				{
				$("#Status option[value='TERMINATE']").prop('disabled', true);
				}
			});	
</script>
	</head>

<body onload="fetchAreaList()">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.edit.org.detail"/></h3>
   
<s:form name="orgDetails" action="bo_um_searchAgtRet_Save"  onsubmit="return isValuesValid()">
<table border="1" cellpadding="2" cellspacing="0" width="450" bordercolor="#CCCCCC">
<s:actionerror />
 <s:fielderror >
  </s:fielderror>
   <s:set name="creditLimit" value="%{#session.ORG_SEARCH_RESULTS.orgCreditLimit}" />
  <s:set name="securityDeposit" value="%{#session.ORG_SEARCH_RESULTS.securityDeposit}" />
  <%	String creditLimit = FormatNumber.formatNumber((Double)pageContext.getAttribute("creditLimit"));
	 	session.setAttribute("CREDIT_LIMIT", creditLimit);
	 	String securityDeposit = FormatNumber.formatNumber((Double)pageContext.getAttribute("securityDeposit"));
	 	session.setAttribute("securityDeposit", securityDeposit);
  %>
  
 <s:textfield key="label.cmpny.name" size="30" name="orgName" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgName}"/>
 <s:hidden value="%{orgId}" id="orgId" name="orgId"></s:hidden> 
 <s:hidden theme="simple" name="orgType" value="%{#session.ORG_SEARCH_RESULTS.orgType}" />
 <s:textfield key="label.compny.type" size="30" readonly="true" value="%{#application.TIER_MAP[#session.ORG_SEARCH_RESULTS.orgType].toUpperCase()}" />
  <tr><td><s:div id="addr1"></s:div></td></tr>
 <s:textfield key="label.adrs.line1" size="30" name="orgAddr1" cssStyle="background-color: #F9FCCD" value="%{#session.ORG_SEARCH_RESULTS.orgAddr1}"/>
 <s:textfield key="label.adrs.line2" size="30" name="orgAddr2" cssStyle="background-color:  #F9FCCD" value="%{#session.ORG_SEARCH_RESULTS.orgAddr2}"/>
 
 <s:textfield key="label.country" size="30" id="country" name="orgCountry" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgCountry}"/>
 <s:textfield key="label.state" size="30" id="plrState" name="orgState" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgState}"/>
 <tr><td><s:div id="orgcity1"></s:div></td></tr>
 <s:hidden name="toTerminate" id="toTerminate" value="%{#session.TO_TERMINATE}" />
 <s:select id="location" cssClass="option" 
								list="#session.CITY_LIST" name="orgCity"  key="label.city" headerKey="-1" headerValue="%{getText('label.please.select')}"  value="%{#session.ORG_SEARCH_RESULTS.orgCity}"
								emptyOption="false"  onchange="return fetchAreaList()"/>
<s:hidden id="areaValue" value="%{#session.ORG_SEARCH_RESULTS.area}"		></s:hidden>								
<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
									<tr>
										<td align="center" colspan="2">
											<div id="area_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												<s:text name="label.zone"/>
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="area" name="area" headerKey="-1"
												headerValue="%{getText('label.please.select')}" list="{}" 
												cssClass="option"   />
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
									--></s:if>								
 <!-- <s:textfield label="City" size="30" name="orgCity" cssStyle="background-color:  #F9FCCD" value="%{#session.ORG_SEARCH_RESULTS.orgCity}"/> -->
 <tr><td><s:div id="pin"></s:div></td></tr>
 <s:hidden theme="simple" name="orgPin" value="%{#session.ORG_SEARCH_RESULTS.orgPin}"></s:hidden>
 <!--<s:textfield label="Pin" cssStyle="background-color:  #F9FCCD" name="orgPin" maxlength="10" value="%{#session.ORG_SEARCH_RESULTS.orgPin}"/> -->
 <s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
 	<s:hidden theme="simple" name="prevIfu" value="%{#session.ORG_SEARCH_RESULTS.idNbr}"></s:hidden>
 	<tr><td><s:div id="ifucode"></s:div></td></tr>
	<s:textfield key="label.ifu.code" name="ifuCode" id="ifuCode" cssStyle="background-color: #F9FCCD" value="%{#session.ORG_SEARCH_RESULTS.idNbr}" maxlength="13"/>
</s:if>
 <s:textfield key="label.pwt.scrp.status" name="scrapStatus" value="%{#session.ORG_SEARCH_RESULTS.pwtScrapStatus}" readonly="true"/>     

 <s:textfield key="label.org.status" size="30" name="orgStatus" readonly="true"  value="%{#session.ORG_SEARCH_RESULTS.orgStatus}"/>
   <s:select key="label.edit.stat" name="statusNew" headerKey="-1" id = "Status" cssClass="option" 										
		list="#{'ACTIVE':getText('ACTIVE'),'INACTIVE':getText('INACTIVE'),'BLOCK':getText('BLOCK'),'TERMINATE':getText('TERMINATE')}" value="%{#session.ORG_SEARCH_RESULTS.orgStatus}" onchange=""  />
 <tr><td>
 <s:textfield key="label.comment" name="comment" id="comment" value=""/>
 <div id="cmmntErr" style="display:none;"></div></td></tr>
 
 <tr><td><s:div id="security"></s:div></td></tr> 
 <s:textfield key="label.secur.dpst" readonly="true" name="orgSecurityDeposit" id="orgSecurityDeposit"  maxlength="8" value="%{#session.securityDeposit}"/>
 <s:textfield key="label.crdtlmt" readonly="true" name="orgCreditLimit" id="orgCreditLimit"  maxlength="8" value="%{#session.CREDIT_LIMIT}"/>
 <s:textfield key="label.ext.credit.limit" readonly="true" name="orgExtendedCreditLimit" id="orgExtendedCreditLimit" maxlength="8" value="%{#session.ORG_SEARCH_RESULTS.extendedCredit}"/>
 <s:textfield key="label.terminal.count" readonly="true" name="terminalCount" id="terminalCount" maxlength="8" value="%{#session.ORG_SEARCH_RESULTS.terminalCount}"/>
 <s:textfield key="label.outstanding.bal" readonly="true" name="outstandingBal" id="outstandingBal" value="%{#session.ORG_SEARCH_RESULTS.outstandingBal}"/>
 
 <tr><td><s:div id="limit"></s:div></td></tr> 
   <tr> 
     <td  align="right"><table>
         <s:reset   key="btn.reset"  cssClass="button hideButton" />
        </table>
        
        </td>
     <td>
        <table>
        
        
        
         <s:submit key="btn.update.detail"  cssClass="button hideButton" id="updateButton"  />
        
        </table>
        
     </td> 
   </tr>
 </table>
<!--  
<br />
<table  border="1" cellpadding="2" cellspacing="0" width="450" bordercolor="#CCCCCC"> 
<tr><th colspan="2" align="center">Organization Limit Values</th></tr>
<s:textfield name="verLimit" label="Verification Limit" id="verLimit" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.verLimit}" />
<s:textfield name="appLimit" label="Approval Limit" id="appLimit" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.appLimit}" />
<s:textfield name="payLimit" label="Payment Limit" id="payLimit" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.payLimit}" />
<s:textfield name="scrapLimit" label="Scrap Limit" id="scrapLimit" readonly="true" value="%{#session.ORG_SEARCH_RESULTS.scrapLimit}" />
</table>
-->
 </s:form>
 
 
 
 

</div></div>
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchAgtRet_EditDet.jsp,v $
$Revision: 1.3 $
</code>