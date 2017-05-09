<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.beans.OrganizationBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>

		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />



		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			scrolling="no" type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<%
			ArrayList<String> userActionList = (ArrayList<String>) session
					.getAttribute("ACTION_LIST");
		%>
		<s:head theme="ajax" debug="false" />

		<script>
function isValuesValid(){

 var frmVal  = new parent.frames[0].Validator("orgDetails");

   
  frmVal.aV("orgAddr1","req",'<s:text name="error.addline1.empty"/>',"addr1");
  frmVal.aV("orgAddr1","regexp=[^A-Za-z0-9\\s/,-]",'<s:text name="error.enter.valid.addline"/>1',"addr1");
  
  frmVal.aV("orgAddr2","regexp=[^A-Za-z0-9\\s/,-]",'<s:text name="error.enter.valid.addline"/>2',"addr1");
  
  frmVal.aV("orgCity","req",'<s:text name="error.blank.city"/>',"orgcity1");
  frmVal.aV("orgCity","regexp=[^A-Za-z\\s]",'<s:text name="error.blank.city"/>',"orgcity1");
  
   
  frmVal.aV("orgPin","req",'<s:text name="error.blank.pin"/>',"pin");
  frmVal.aV("orgPin","numeric",'<s:text name="error.blank.pin"/>',"pin");

  frmVal.aV("orgSecurityDeposit","req",'<s:text name="error.creditlimit.empty"/>',"security");
  frmVal.aV("orgSecurityDeposit","decimal",'<s:text name="error.creditlimit.empty"/>',"security");

  frmVal.aV("orgCreditLimit","req",'<s:text name="error.creditlimit.empty"/>',"limit");
  frmVal.aV("orgCreditLimit","decimal",'<s:text name="error.creditlimit.empty"/>',"limit");
  
  //alert(document.error_disp_handler.isValid);

return document.error_disp_handler.isValid;

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
document.getElementById('resDiv').innerHTML ="";
document.getElementById('crResult').innerHTML ="";
document.getElementById('creditLimit1').innerHTML ="";
_resp  = _ajaxCall(createExtendUrl());
//alert(_resp.result+":"+_resp.data);
	if(_resp.result){
	_id.i('resDiv',_resp.data)
	}
}
}
function createExtendUrl(){
var newUrl = 'bo_um_extendCrLimit_Save.action';
newUrl = newUrl+'?orgId='+_id.v("orgId")+'&extendedLimit='+_id.v("extendLimit")+'&daysAfter='+_id.v("daysAfterId")+'&isResetORExtend=extend';
//alert(newUrl);
return newUrl;
}

function submitResetExtendLimit(){
document.getElementById('resDiv').innerHTML ="";
document.getElementById('crResult').innerHTML ="";
document.getElementById('creditLimit1').innerHTML ="";
_resp  = _ajaxCall(createResetExtendUrl());
//alert(_resp.data);
	if(_resp.result){
	_id.i('resDiv',_resp.data)
	//document.getElementById('extendLimit').value=0.0;
	//document.getElementById('daysAfterId').value=1;
	}
}

function createResetExtendUrl(){
var newUrl = 'bo_um_extendCrLimit_Save.action';
newUrl = newUrl+'?orgId='+_id.v("orgId")+'&extendedLimit=0.0&daysAfter=0&isResetORExtend=reset';
//alert(newUrl);
return newUrl;
}

function checkCrLmtValidation(){
	var creditAmtChk=isNumericAmt(_id.v('orgCreditLimit'));
	var securityAmtChk=isNumericAmt(_id.v('orgSecurityDeposit'));
	//alert(securityAmtChk+"  "+creditAmtChk);
	if(creditAmtChk || securityAmtChk){
		document.getElementById('creditLimit1').innerHTML = "<font color = 'red'><s:text name='error.enter.valid.amt'/></font>";
		return false;
	}else{
		document.getElementById('creditLimit1').innerHTML='';
	}	
	return true;
}
function submitCreditLimit(){
if(checkCrLmtValidation()){
document.getElementById('crResult').innerHTML ="";
document.getElementById('resDiv').innerHTML ="";
document.getElementById('creditLimit1').innerHTML ="";
var newUrl = 'bo_um_updateCrLimit_Save.action';
newUrl = newUrl+'?orgId='+_id.v("orgId")+'&orgCreditLimit='+_id.v("orgCreditLimit")+'&orgSecurityDeposit='+_id.v("orgSecurityDeposit");
//alert(newUrl);
_resp  = _ajaxCall(newUrl);
//alert(_resp.result+":"+_resp.data);
	if(_resp.result){
	_id.i('crResult',_resp.data)
	}
}
}
</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.edit.xcl.detail.of"/> 
					<s:property value="#application.TIER_MAP[#session.ORG_SEARCH_RESULTS.orgType]" />
					: <s:property value="%{#session.ORG_SEARCH_RESULTS.orgName}"/>
				</h3>

				<s:form name="orgDetails" onsubmit="return isValuesValid();"
					action="bo_um_searchAgtRet_Save">
					<table border="1" cellpadding="2" cellspacing="0" width="450"
						bordercolor="#CCCCCC">
						<s:actionerror />
						<s:fielderror>
						</s:fielderror>
						<s:set name="creditLimit"
							value="%{#session.ORG_SEARCH_RESULTS.orgCreditLimit}" />
						<s:set name="securityDeposit"
							value="%{#session.ORG_SEARCH_RESULTS.securityDeposit}" />
						<%
							String creditLimit = FormatNumber
										.formatNumber((Double) pageContext
												.getAttribute("creditLimit"));
								session.setAttribute("CREDIT_LIMIT", creditLimit);
								String securityDeposit = FormatNumber
										.formatNumber((Double) pageContext
												.getAttribute("securityDeposit"));
								session.setAttribute("securityDeposit", securityDeposit);
						%>

						<s:hidden value="%{orgId}" id="orgId" name="orgId"></s:hidden>
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

				<s:form name="creditLimitForm" onsubmit="return checkCrLmtValidation()">
					<table border="1" cellpadding="2" cellspacing="0" width="450"
						bordercolor="#CCCCCC">
						<tr>
							<td colspan="2">
								<div id="creditLimit1"></div>
								<div id="crResult"></div>
							</td>
						</tr>
						<s:textfield key="label.secur.dpst" name="orgSecurityDeposit" id="orgSecurityDeposit"
							cssStyle="background-color: #F9FCCD" maxlength="8"
							value="%{#session.securityDeposit}" />
						<s:textfield key="label.crdtlmt" name="orgCreditLimit" id="orgCreditLimit"
							cssStyle="background-color: #F9FCCD" maxlength="10"
							value="%{#session.CREDIT_LIMIT}" />
						<tr>
							<td align="right">
								<table>
									<s:reset key="btn.reset" cssClass="button" />
								</table>
							</td>
							<td>
								<input type="button" value="<s:text name="btn.update.crd.limit"/>"
									id="submitButtonId" class="button" onclick="submitCreditLimit()" />
							</td>
						</tr>
					</table>
				</s:form>

				<s:form name="extendLimitForm" onsubmit="return checkValidation()">
					<table border="1" cellpadding="2" cellspacing="0" width="450"
						bordercolor="#CCCCCC">
						<tr>
							<td colspan="2">
								<div id="extendLimit1"></div>
								<div id="resDiv"></div>
							</td>
						</tr>

						<s:textfield key="label.extnd.crdtlmt" size="30"
							id="extendLimit" name="extendedLimit"
							cssStyle="background-color: #F9FCCD" maxlength="10"
							value="%{#session.ORG_SEARCH_RESULTS.extendedCredit}" />

						<tr>
							<td align="right" class="label">
								<s:text name="label.for"/>:
							</td>
							<td>
								<div id="daysAfterId1"></div>
								<s:textfield theme="simple" name="daysAfter" id="daysAfterId"
									cssStyle="background-color: #F9FCCD" maxlength="3"
									value="%{#session.ORG_SEARCH_RESULTS.extendsCreditLimitUpto}" />
								<s:text name="label.days"/>

							</td>
						</tr>
						<tr>
							<td align="right">
								<table>
									<s:reset key="btn.reset" cssClass="button" />
								</table>
							</td>
							<td>
								<table>
									<input type="button" value="<s:text name="btn.extend.limit"/>" id="submitButtonId"
										class="button" onclick="submitExtendLimit()" />
									<input type="button" value="<s:text name="btn.reset.extend.limit"/>"
										id="resetSubmitButtonId" class="button"
										onclick="submitResetExtendLimit()" />
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
$RCSfile: bo_um_extendCrLimit_Edit.jsp,v $
$Revision: 1.3 $
</code>