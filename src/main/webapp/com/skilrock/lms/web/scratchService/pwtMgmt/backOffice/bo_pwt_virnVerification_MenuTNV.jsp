<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

	<script>
	var path = "<%=request.getContextPath() %>";
	</script>

<!--<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtTicketEntry.js"></script>-->
					<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtCommonFnTNV.js"></script>
					

<script >
function verifyAgent(agtName)
{
  return verifyOrg('agtOrgName',agtName,'You have selected '+tierMap['AGENT']+':: ');
}
function validateData(){
		var frmVal  = new parent.frames[0].Validator("pwtBO");
	    frmVal.aV("gameNbr_Name","dontselect=0","Please select the game first","gamemessage");
	    frmVal.aV("agtOrgName","dontselect=0","Please select a valid "+tierMap['AGENT'],"agtOrgDiv");
		//var checkDup = checkDuplicate("pwtBO","virnCode");
		var checkDup = checkDuplicateNew();
		//alert("check dup is "+ checkDup);
		if(document.error_disp_handler.isValid&&!checkDup && checkTktVirnBinding()){
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		
<s:head theme="ajax" debug="false"/>
	</head>
		



	<body onload="crtTab('virn');">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>VIRN Validation for <s:text name="PWT"/> Receipt TNV</h3>
	<s:actionerror/>
		<s:form name="pwtBO" action="bo_pwt_virnVerification_Save_TNV" method="POST" enctype="multipart/form-data">
			<table border="0" cellpadding="3" cellspacing="0" width="350">
				<tr>
					<td colspan="4">
						<table>
			
				<tr>
					<td  nowrap="nowrap">
					<div id="gamemessage"></div><input type="hidden" name="gameNbrFormat" id="gameNbrFormat"/><div id="loadingDiv" style="position: absolute;"></div>
					<s:select label="Game Number" name="gameNbr_Name"  headerKey="-1" headerValue="--Please Select--"
					  list="#session.ACTIVE_GAME_LIST.{gameNbr_Name}" onchange="gameConfirm(((this.value).split('-')[1]))"  cssClass="option"/>
					 
					
					 </td>
				</tr>
			
				<tr>
					<td nowrap="nowrap"><div id="agtOrgDiv"></div>
											 <s:select label="%{#application.TIER_MAP.AGENT} Name" name="agtOrgName" id="agtOrgName"  headerKey="-1" headerValue="--Please Select--"
					  list="#session.AGT_ORG_LIST.{orgName}" cssClass="option" onchange="return verifyAgent(this.value);" />
					</td>	
				</tr>

						</table>
					</td>	
		
				</tr>

			<tr><td colspan="4"><div id="allEmpty"></div><div id="_virnDiv"></div></td></tr>	
			<!-- 
			<tr><td colspan="4"><font color="red">Entries in file should not exceed 5000 </font></td></tr>									
			<tr><td colspan="4"><table cellpadding="0" cellspacing="0">
			<s:file name="virnFile" id="uploadFile" label="Select VIRN File "></s:file>
			</table></td></tr>
			 -->
			<tr> <td align="right" colspan="2"><div id="subCheck" style="position:absolute;"></div><s:submit align="right" theme="simple" value="Verify and Save" cssClass="button" id="subTrans" onclick="return _subTrans()"/> </td>
					
				</tr>
				</table>
			
			
			
			
			
		
		</s:form>
				</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_virnVerification_MenuTNV.jsp,v $
$Revision: 1.2 $
</code>