	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>var path = "<%=request.getContextPath() %>";</script>	
	<!--<script  src="js/pwtTicketEntry.js"></script>-->
				<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtCommonFn.js"></script>
	<s:head theme="ajax" debug="false"/>
		</head>
		<script>
	function validate(){
	var isValid = true;
	var frmVal  = new parent.frames[0].Validator("pwtTktAgtRet");
    frmVal.aV("gameNbr_Name","dontselect=0","Please Select Game Number","gamemessage");
  	var checkDup = checkDuplicate("pwtTktAgtRet","ticketNumber");
	  	if(document.error_disp_handler.isValid&&!checkDup){
	 	 return true;
	  	}
	  	return false;
	  }
	</script>
	<body>
	
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">

  <div id="top_form">
   <h3>Change Winning Ticket Status</h3>
	<s:actionerror/>
		<s:form name="pwtTktAgtRet" action="agt_pwt_returnPwtTkt_Verify" onsubmit="return validate();">
			
			<table border="0" width="46%" cellpadding="2" cellspacing="0">
				<tr>
					<td colspan="2"><div id ="gamemessage"></div>
						<s:select label="Game Number" name="gameNbr_Name" headerKey="-1" cssClass="option"
							headerValue="--Please Select--" list="%{#session.ACTIVE_GAME_LIST.{gameNbr_Name}}" />
					</td>
				</tr>
			</table>

			<table rder="0" width="46%" cellpadding="2" cellspacing="0">
				<tr>
					<td width="46%"></td>
				</tr>
			</table>

			<table border="0" width="46%" cellpadding="2" cellspacing="0">

<tr><td colspan=2><div id="errorDiv"></div></td></tr>
				<s:iterator value="#session.TICKET_LIST">
					<tr>
						<td width="50%">
							<s:label theme="simple" value="Ticket Number   :" />
						</td>
						<td align="right">
							<s:textfield theme="simple" label="Ticket Number"
								name="ticketNumber" value="%{ticketNumber}"></s:textfield>
						</td>
					</tr>
				</s:iterator>
			</table>


			<table width="46%">
				<tr></tr>
				<tr></tr>
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
$RCSfile: agt_pwt_returnPwtTkt_Menu.jsp,v $
$Revision: 1.2 $
</code>