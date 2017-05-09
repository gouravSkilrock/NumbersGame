<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax"/>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

<script>
function retailerListAjax(){
	document.getElementById("list").style.display="block";
		_resp = _ajaxCall('retListForAgent.action');
		document.getElementById("list").innerHTML=_resp.data;
		return true;
}


function retValidate(){
	if(document.getElementById("retOrgId").value== -1){
		document.getElementById("err").innerHTML = '<font color = "red">Please select atleast one '+tierMap["RETAILER"]+'</font>'
		return false;
	}else{
		return true;
	}
}

function clearResult(){
	document.getElementById('err').innerHTML = '';
	document.getElementById('downApp').innerHTML = '';
}
</script>
	</head>

	<body onload="return retailerListAjax()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Quick Ledger
				</h3>
				<s:form name="searchgame" action="agt_rep_quickLedger_Result"
					onsubmit="return retValidate();">
					<table border="0" cellpadding="2" cellspacing="0" width="400px">
						<tr>
							<td colspan="2">
								<s:select name="type" label="Get Ledger For " list="{'Current Day','Last Day'}" cssClass="option" onchange="clearResult()" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="err"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								select <s:property value="#application.TIER_MAP.RETAILER" />
							</td>
							<td align="left">
								<span id="list"></span>
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="search" value="Search" align="right" targets="downApp" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="downApp" style="text-align: center"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_quickLedger_Menu.jsp,v $
$Revision: 1.2 $
</code>