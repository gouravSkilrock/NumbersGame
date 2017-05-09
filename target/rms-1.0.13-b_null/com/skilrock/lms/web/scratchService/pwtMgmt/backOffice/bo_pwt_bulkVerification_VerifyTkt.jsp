
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
	<script>
	var path = "<%=request.getContextPath() %>";
	function validate(){
	var isValid = true;
	var frmVal  = new parent.frames[0].Validator("tmpPwtTkt");
    frmVal.aV("gameNbr_Name","dontselect=0","Please Select Game Number","gamemessage");
  		var checkDup = checkDuplicate("tmpPwtTkt","ticketNumber");
		if(document.error_disp_handler.isValid&&!checkDup){
	 	 return true;
	  	}
	  	return false;
	  }
	  
	function _subTrans(){
	return ( validate() && _subValid('subTrans'));
	} 
	</script>
	<!--<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtTicketEntry.js"></script>-->
					<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtCommonFn.js"></script>
					
	
	<body onload="crtTab('tkt');">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">

  <div id="top_form">
  <h3>Ticket Validation Before <s:text name="PWT"/> Receipts</h3>
  	<s:actionerror/>
		<s:form action="bo_pwt_bulkVerification_SaveTkt" name="tmpPwtTkt" method="POST" enctype="multipart/form-data">
		
   
	<table><tr><td>		
			<table border="0" width="100%" cellpadding="2" cellspacing="0">
				<tr>
					<td colspan="2">
						<s:textfield label="Receipt No." name="receiptNum" value="%{receiptNum}" size="16" readonly="true"></s:textfield>
					</td>	
				</tr>
				<tr>
					<td colspan="2">
						<s:textfield label="%{#application.TIER_MAP.AGENT} Name" name="agtOrgName" value="%{agtOrgName}" size="25" readonly="true"></s:textfield>
					</td>	
				</tr>
				<tr>
					<td colspan="2"><div id = "gamemessage"></div>
						<s:select label="Game Number" name="gameNbr_Name" id="game"  headerKey="-1" cssClass="option"
						headerValue="--Please Select--" list="%{#session.ACTIVE_GAME_LIST.{gameNbr_Name}}" onchange="searchPwtResult('bo_pwt_bulkVerification_GameNbrFmt.action?gameName='+((this.value).split('-')[1]))" />
			</td>
				</tr>
			</table>
	</td>
	<td rowspan="3" valign="bottom">
		<input type="hidden" name="gameNbrFormat" id="gameNbrFormat"/><div id="loadingDiv"></div> 
	</td>
	</tr>
	</table>		
			<br/>
           <div id="allEmpty"></div><div id="_tktDiv">
                   </div>		
 			<br /><tr><td colspan="2"><font color="red">Entries in file should not exceed 5000 </font></td></tr>
			<br/><s:file name="ticketFile" id="uploadFile" label="Select Ticket File "></s:file>
		
			<table width="90%" cellpadding="2" cellspacing="0" border="0">
				<tr><td align="right" colspan="4"><div id="subCheck" style="position:absolute;"></div>
							<s:submit theme="simple" value="Verify" cssClass="button" id="subTrans"  onclick="return _subTrans()"/>
				</td></tr>
			</table>
		</s:form>
		</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkVerification_VerifyTkt.jsp,v $
$Revision: 1.2 $
</code>