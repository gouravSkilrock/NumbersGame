<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<s:head theme="ajax" debug="false" />
<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/instantWin/js/pwtDirPlayerBO.js" ></script>
<script>
	function myFunction() {
		if(document.getElementById("ticketNbr").value.length == 0) {
			alert("Please Enter Ticket Number");
			return false;
		}
		document.getElementById('subTransDraw').style.display = "none";
		return true;
	}
	
	function checkEvent(e) {
		if (e.keyCode == 13) {
			return false;
		} else {
			return true;
		}
	}
</script>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3>
				<s:text name="label.win.tkt.verify" />
			</h3>
			<div id="first">
				<div id="tktNbrErrMsg"></div>
				<s:form id="form" name="form1" action="bo_fetchVerifyTicketData">
					<table id="drawTable">
						<s:actionerror />
						<tr>
							<td align="right"><s:textfield name="ticketNbr"
									id="ticketNbr" key="label.tckt.no" maxlength="20"
									onkeyup="return moveFocusOnSubmit(this.value);" onkeypress="return checkEvent(event);"/></td>
						</tr>
						<tr>
							<td><s:submit formId="form" id="subTransDraw"
									key="btn.verify" align="right" cssClass="button"
									targets="result" theme="ajax" onclick = "return myFunction();"/></td>
						</tr>
					</table>
					<div id="loading"></div>
				</s:form>
			</div>
			<div id="result" style="text-align: center"></div>
			<div id="regPlayer" style="text-align: center"></div>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	instantWinVerifyTicket.jsp,v $ $Revision: 1.3 $ </code>