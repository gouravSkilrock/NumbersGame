<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

	<script>
	var path = "<%=request.getContextPath() %>";
	function validateData(){
		var isValid = true;
		var frmVal  = new parent.frames[0].Validator("tmpPwtBO");
	    frmVal.aV("gameNbr_Name","dontselect=0","Please select the game first","gamemessage");
		var checkDup = checkDuplicate("tmpPwtBO","virnCode");
	  	if(document.error_disp_handler.isValid&&!checkDup){
	 	 return true;
	  	}
		return false;
}
	function _subTrans(){
	return ( validateData() && _subValid('subTrans'));
	}
function gameConfirm(gameName){
if(gameName==1){
return false;
}
var answer = confirm ("You Have Selected "+gameName+"?");
if (answer){
searchPwtResult('bo_pwt_bulkVerification_GameNbrFmt.action?gameName='+gameName);
}
else{
 document.getElementsByName('gameNbr_Name')[0].selectedIndex=0;
}

}	
	
</script>
<!--<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtTicketEntry.js"></script>-->
					<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtCommonFn.js"></script>
					

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
<s:head theme="ajax" debug="false"/>
	</head>
		



	<body onload="crtTab('virn');">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>VIRN Validation for <s:text name="PWT"/> Receipt</h3>
	<s:actionerror/>
		<s:form name="tmpPwtBO" action="bo_pwt_bulkVerification_SaveVIRN" method="POST" enctype="multipart/form-data">
			
			<table border="0" cellpadding="3" cellspacing="0" width="350">
				<tr>
					<td colspan="4">
						<table>
						<tr>
					<td nowrap="nowrap">
						<s:textfield label="Receipt No." name="receiptNum" value="%{receiptNum}" size="16" readonly="true"></s:textfield>
					</td>	
				</tr>
				<tr>
					<td nowrap="nowrap"><div id="agtOrgDiv"></div>
						<s:textfield label="%{#application.TIER_MAP.AGENT} Name" name="agtOrgName" value="%{agtOrgName}" size="25" readonly="true"></s:textfield>
					</td>	
				</tr>

				<tr>
					<td  nowrap="nowrap">
					<div id="gamemessage"></div><input type="hidden" name="gameNbrFormat" id="gameNbrFormat"/><div id="loadingDiv" style="position: absolute;"></div>
					<s:select label="Game Number" name="gameNbr_Name"  headerKey="-1" headerValue="--Please Select--"
					  list="#session.ACTIVE_GAME_LIST.{gameNbr_Name}" onchange="gameConfirm(((this.value).split('-')[1]))"  cssClass="option"/>
					 
					
					 </td>
				</tr>
						</table>
					</td>	
		
				</tr>
			<tr><td colspan="4"><div id="allEmpty"></div><div id="_virnDiv"></div></td></tr>
			<tr><td colspan="4"><font color="red">Entries in file should not exceed 5000 </font></td></tr>									
			<tr><td colspan="4"><table cellpadding="0" cellspacing="0">
			<s:file name="ticketFile" id="uploadFile" label="Select VIRN File "></s:file>
			</table></td></tr>
			<tr> <td align="right" colspan="2"><div id="subCheck" style="position:absolute;"></div><s:submit align="right" theme="simple" value="Verify and Save" cssClass="button" id="subTrans"  onclick="return _subTrans()"/> </td>
					
				</tr>
				</table>


		
		</s:form>
				</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkVerification_VerifyVIRN.jsp,v $
$Revision: 1.2 $
</code>