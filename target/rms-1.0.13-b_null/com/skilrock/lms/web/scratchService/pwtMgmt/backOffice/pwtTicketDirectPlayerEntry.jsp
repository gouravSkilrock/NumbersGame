<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

			<script type="text/javascript">
function validateTicket()
{
if ( document.getElementById("gameNbr_Name").value == "-1" )
    {
 // alert ( "Please select Printer." );
		document.getElementById('gameNbr_Name1').innerHTML = "<font color = 'red'> Please select Game  </font>";
	 flag12=false;
    }
   else{
    document.getElementById('gameNbr_Name1').innerHTML = "";
    	 flag12=true;
    }
    if(document.getElementById("ticketNumber").value=="")
    {
    
    document.getElementById('ticketNumber1').innerHTML = "<font color = 'red'> Please Enter Correct Ticket Number </font>";
    	 flag12=false;
    }
else{
document.getElementById('ticketNumber1').innerHTML ="";
  	 flag12=true;
    }
    return flag12;
}




</script>
			
		<s:head theme="ajax" debug="false" />
	</head>



		<body>

		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Direct Player <s:text name="PWT"/> Receive
				</h3>

				<s:actionerror />
				<s:form action="verifyTicketsDirectPlayer"
					onsubmit="return validateTicket()">

					<h3>Ticket Validation Before <s:text name="PWT"/> Receipts</h3>
					<table border="0" cellpadding="2" cellspacing="0" width="400" >
						

						<tr>
							<td align="left">
								<div id="gameNbr_Name1"></div>
							</td>
						</tr>
						<tr>
							<td align="left">
								<s:select label="Game Number-Game Name" id="gameNbr_Name"
									name="gameNbr_Name" headerKey="-1"
									headerValue="--Please Select--"
									list="%{#session.ACTIVE_GAME_LIST.{gameNbr_Name}}"
									cssClass="option" />
							</td>

						</tr>

						<tr>
							<td>
								<div id="ticketNumber1"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:textfield label="Ticket Number" name="ticketNumber"
									id="ticketNumber"></s:textfield>
							</td>
						</tr>

						<tr>
							<td>
								<s:submit value="Verify" cssClass="button" />
							</td>

						</tr>

		
					</table>


					<s:iterator value="#session.COUNTER">
						<s:set name="count" value="%{intValue()}" />
					</s:iterator>

					<p align="left">
						<font size="3" color="blue">
						<s:label theme="simple" value="Step %{count} of  3" />
					</font>
					</p>
				</s:form>
				</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: pwtTicketDirectPlayerEntry.jsp,v $
$Revision: 1.2 $
</code>