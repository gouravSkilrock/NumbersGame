	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
		
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	
	
	<script >
function backProcess(){
history.back();
//window.history.forward(-1);
}
window.history.forward(-1);

function checkVIRN(divId,textId){
if(document.getElementById(textId).value==""){
document.getElementById(divId).innerHTML="<font color=\"red\">Please Enter Virn Code</font>";
return false;
}
else{
document.getElementById(divId).innerHTML="";
return true;
}
}
</script>
<s:head theme="ajax" debug="false"/>
		</head>
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>Following Tickets Has Been Validated and Processed for Acceptance</h3>
		<s:actionerror />
		<s:if test="#session.statusFlag=='Yes'">
			
			<s:form action="verifyPWTTickets.action">

				<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="30%">
							Ticket Number
						</th>
						<th width="30%">
							Status
						</th>
						<th width="30%">
							Reason
						</th>
					</tr>


					<s:iterator value="#session.VERIFIED_TICKET_LIST">
						<s:set name="status1" value="Valid" />
						<tr>
							<td width="30%">
								<s:property value="%{ticketNumber}" />
							</td>


							<td width="25%">
								<s:text name="Invalid">Valid</s:text>
							</td>


							<td width="45%">
							<font size="3" color="blue"><s:property value="%{status}" />
								</font>

							</td>


						</tr>

					</s:iterator>
				</table>
				<h3>
					VIRN Code Verification
				</h3>
				<s:set name="Game_name_nbr" value="#session.SELECTED_GAMENBR_NAME" />
				<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">

					<tr>
						<td>
							<s:label theme="simple" value="Game Name" />
							<s:textfield theme="simple" label="Game Name" name="gameNbr_Name"
								value="%{Game_name_nbr}" disabled="true"></s:textfield>
						</td>
					</tr>

					<tr>
						<td>
							<s:set name="ticketnumber" value="#session.TICKET_NUMBER" />
							<s:label theme="simple" value="Ticket Number" />
							<s:textfield theme="simple" label="Ticket Number"
								name="ticketNumber" value="%{ticketnumber}" disabled="true"></s:textfield>
						</td>
					</tr>

				</table>

				<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">

					<tr>
						<td><div id="virnDiv"></div>
							<s:label theme="simple" value="Virn Code" />
							<s:textfield theme="simple" label="Virn Code" name="virnCode" id="virnValue"></s:textfield>
						</td>

					</tr>

				</table>


				<table width="300" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
					<tr></tr>
					<tr></tr>
					<tr>
						<td>
							<s:submit theme="simple" value="Verify" cssClass="button" onclick="return checkVIRN('virnDiv','virnValue')"/>
						</td>
						<td>
							<s:submit theme="simple" value="Back"
								onclick="backProcess(); return false;"  cssClass="button"/>
						</td>

					</tr>
				</table>
			</s:form>
			<s:iterator value="#session.COUNTER">
				<s:set name="count" value="%{intValue()}" />
			</s:iterator>

			<p align="left">
			<font size="3" color="blue"><s:label theme="simple"
						value="Step %{count} of  3" />
				</font>
			</p>
		</s:if>
		<s:if test="#session.statusFlag=='No'">
			<h3>There is no Ticket validated for <s:text name="PWT"/> Receive</h3>

			<table width="500" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
				<tr>
					<th width="30%">
						Ticket Number
					</th>
					<th width="30%">
						Status
					</th>
					<th width="30%">
						Reason
					</th>
				</tr>

				<s:iterator value="#session.VERIFIED_TICKET_LIST">
					<s:set name="status1" value="InValid" />
					<tr>
						<td width="30%">
							<s:property value="%{ticketNumber}" />
						</td>
						<td width="25%">
							<s:text name="Invalid">Invalid</s:text>
						</td>

						<td width="45%">
							<font color="red"><s:property value="%{status}" />
							</font>

						</td>

					</tr>

				</s:iterator>
				<tr>
				
				
				</tr>
						<tr>
					<td colspan="3">				
					
						<s:a href="dispalyPwtTicketEntryDirectPlayer.action">Do Another Validation</s:a>
					</td>
				</tr>
			</table>
			<s:iterator value="#session.COUNTER">
				<s:set name="count" value="%{intValue()}" />
			</s:iterator>

			<p align="left">
				<font size="3" color="blue"><s:label theme="simple"
						value="Step %{count} of  3" />
				</font>
			</p>
		</s:if>
		</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: verifiedTicketsDirectPlayerResult.jsp,v $
$Revision: 1.2 $
</code>