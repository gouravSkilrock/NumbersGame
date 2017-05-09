<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>
	var path = "<%=request.getContextPath() %>";
	</script>	

	<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtCommonFnAgt.js"></script>
	
	<script >
function verifyRetailer(retName)
{
  return verifyOrg('retOrgName',retName,'You have selected '+tierMap['RETAILER']);
}

function validateData(){
		var isInValid = false;
		var checkDup = checkDuplicate('Virn');
		if(frmValidtr()&&!checkDup.dup&&!checkDup.fill){
	 	 return true;
	  	}
	  	if(checkDup.fill){
		_id.i('retOrgDiv',_id.o('retOrgDiv').innerHTML+'<br/> Please Fill one Entry','e');
		}
		alert("Please check error messages on page");
		return false;
}
function frmValidtr(){
	var frmVal  = new parent.frames[0].Validator("pwtAgent");
    frmVal.aV("retOrgName","dontselect=0","Please select a valid "+tierMap['RETAILER'],"retOrgDiv");
	return document.error_disp_handler.isValid&_un();
}
	
function _subTrans(){
	return ( validateData() && _subValid('subTrans'));
}
</script>
<s:head theme="ajax" debug="false"/>
	</head>
<body onload="fetchDet(),appendGameTab('Virn')">

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<div id="top_form">
	<h3>VIRN Validation for Winning Receipt</h3>
	
		<s:form name="pwtAgent" action="agt_pwt_virnVerification_Save" onsubmit="return _subTrans()">
		<table border="0" cellpadding="3" cellspacing="0" width="350">
			<tr>
				<td>
					<div id="retOrgDiv" style="text-align: left;"></div>
							<table><s:select label="%{#application.TIER_MAP.RETAILER} Name" name="retOrgName" id="retOrgName" headerKey="-1"
								headerValue="--Please Select--"
								list="{}" cssClass="option" onchange="return verifyRetailer(this.options[this.selectedIndex].text);"/>
					 		 </table>
				 </td>
				 <td align="right">
					 <input type="button"  name="addGame" value="Add More Game" class="button" onclick="addGameToTable('Virn');"/>
					 <table style="display:none">
					 <s:select  name="gameNbr_NameHid" id="gameNbr_NameHid"  headerKey="-1" headerValue="--Please Select--"
											  list="{}" /><tr><td><input type="hidden" id="nog" name="nog" value="<%=application.getAttribute("NO_OF_GAME_PER_PAGE")%>"/></td></tr>
					
					</table>
											 
				 </td>
			</tr>
			<tr>
				<td colspan="3">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" id="gameTab">
						
					</table>
				</td>
			</tr>
			<tr>
						 <td align="right" colspan="2">
							 <div id="subCheck" style="position:absolute;"></div>
							 <s:submit align="right" theme="simple" value="Verify and Save" cssClass="button" id="subTrans" onclick="fillCount('Virn')"/> 
						 </td>
			</tr>
		</table>
	</s:form>
</div>
</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_pwt_virnVerification_Menu.jsp,v $
$Revision: 1.3 $
</code>