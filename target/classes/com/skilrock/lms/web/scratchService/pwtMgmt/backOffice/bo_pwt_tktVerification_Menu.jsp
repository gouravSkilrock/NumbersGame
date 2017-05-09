<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
<script>
	var path = "<%=request.getContextPath() %>";
	var projectName=path;
	
	function validate(){
	var frmVal  = new parent.frames[0].Validator("pwtBOTkt");
    frmVal.aV("gameNbr_Name","dontselect=0","Please Select Game Number","gamemessage");
   	frmVal.aV("partyOrgId","dontselect=0","Please Select "+tierMap['AGENT']+" Name","partymessage");
  		var checkDup = checkDuplicate("pwtBOTkt","ticketNumber");
	  	if(document.error_disp_handler.isValid&&!checkDup){
	 	 return true;
	  	}
	  	return false;
	  }
	</script>
<!--<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtTicketEntry.js"></script>-->
					<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtCommonFn.js"></script>
					

	
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>

	<body onload="crtTab('tkt');">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
		<div id="wrap">

  <div id="top_form">
  <h3>Ticket Validation Before <s:text name="PWT"/> Receipts</h3>
  <s:actionerror/>
		<s:form name="pwtBOTkt" action="bo_pwt_tktVerification_Verify" onsubmit="return validate();" method="POST" enctype="multipart/form-data">
			<table><tr><td>		
			<table border="0" width="100%" cellpadding="2" cellspacing="0">
				<tr>
					<td colspan="2"><div id = "gamemessage"></div>
						<s:select label="Game Number" name="gameNbr_Name" id="game"  headerKey="-1" cssClass="option"
						headerValue="--Please Select--" list="%{#session.ACTIVE_GAME_LIST.{gameNbr_Name}}" onchange="searchPwtResult('bo_pwt_bulkVerification_GameNbrFmt.action?gameName='+((this.value).split('-')[1]))" />
					</td>
				</tr>
				<tr>
					<td colspan="2"><div id = "partymessage"></div>
						<s:select label="%{#application.TIER_MAP.AGENT} Name" name="partyOrgId" id="party_org_id"  headerKey="-1" cssClass="option"
						headerValue="--Please Select--" list="#session.AGENT_LIST_MAP"  onchange="return verifyRetailer(this.options[this.selectedIndex].text);" />
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
			<br><s:file name="ticketDetails" id="uploadFile" label="Select Ticket File "></s:file>
			<table width="305" cellpadding="2" cellspacing="0" border="0">
			<tr>
					<td align="right">
						<s:submit theme="simple" value="Verify" cssClass="button" />
					</td>

				</tr>
			</table>
		</s:form>
		</div></div>
		</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_tktVerification_Menu.jsp,v $
$Revision: 1.4 $
</code>