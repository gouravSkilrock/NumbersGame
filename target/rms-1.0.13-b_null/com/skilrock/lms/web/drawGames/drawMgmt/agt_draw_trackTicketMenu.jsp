<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
		function isFilled()
		{
			_id.i('down','');
			var frmVal  = new parent.frames[0].Validator("trackTkt");
			
			frmVal.aV("tkt_no","req","Please Enter Ticket Number","tktErr");
		  	frmVal.aV("tkt_no","numeric","Please Enter Correct Ticket Number","tktErr");
		  	frmVal.aV("tkt_no","minlen=16","Please Enter the Ticket Number","tktErr");
			return document.error_disp_handler.isValid;
		}
		
		function moveFocusOnSubmit(value){
			if(value.length == 20){
				_id.o('search').focus();
			}
			return true;
		}
		
	</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h3>
					Track Ticket
				</h3>


				<s:form name="form" name="trackTkt" onsubmit="return isFilled();">
					<table border="0" cellpadding="2" cellspacing="0" width="400">
						<tr>
							<td>
								<div id="tktErr"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:textfield name="ticketNum" maxlength="20" id="tkt_no" label="Enter Ticket Number" onkeyup="return moveFocusOnSubmit(this.value);"/>	
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" id="search" align="right"
									targets="down" theme="ajax" action="agtTrackTicketResult" cssClass="button" />
							</td>
						</tr>
					</table>

				</s:form>

				<div id="down" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_draw_trackTicketMenu.jsp,v $
$Revision: 1.2 $
</code>