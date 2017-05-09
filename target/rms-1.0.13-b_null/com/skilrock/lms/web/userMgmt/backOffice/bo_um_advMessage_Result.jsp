<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_dg_ticketCancellation.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>

		<script>
			function fetchRetailerList(val) {
				clearDiv();
				
				if (val != -1) {
					_resp = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?agentOrgName=" + val);
					_respData = _resp.data;
					if(_respData != ""){
						var resData = _respData.split(",");
						var retObj = _id.o("retOrgName");
						retObj.options.length = 1;
						for (var i = 0; i < resData.length; i = i + 1) {
							var val = resData[i].split("=");
							var opt = new Option(val[1], val[1]);
							retObj.options[i + 1] = opt;
						}
					} else if(_respData == ""){
						_id.o('retOrgName').value = -1;
						_id.o('retOrgName').options.length = 1;
					}
					
				} else {
					_id.o("retOrgName").value = -1;
					var retObj = _id.o("retOrgName");
					retObj.options.length = 1;
				}
			}
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Advertisement Message Entry
				</h3>
				<div id="tktErrMsgDiv"></div>
				<s:form name="ticketCancellation" id="ticketCancellation"
					onsubmit="return validateTicket();">



					<div id="errMsgDiv">

					</div>
					<table id="retTable" border="0" cellpadding="1" cellspacing="0"
						bordercolor="#CCCCCC" align="left" width="40%">
						<s:select list="%{'PLAYER'}" label="Select Message For" id="orgId"
							name="OrgName" cssClass="option" headerKey="-1"
							headerValue="-- Please Select --"></s:select>
						<s:select name="gameNo" label="Game Name" id="gameNo"
							list="%{#session.DRAWGAME_LIST}" cssClass="option" headerKey="-1"
							headerValue="-- Please Select --"></s:select>
						<s:radio
							list="#{'AgentWise':'AgentWise','LocationWise':'Location'}"
							name="search_type" id="search_type" value="'AgentWise'"
							label="Cancel Type" onclick="return checkType()" />
						<tr>
							<div id="result"></div>
						</tr>
					</table>

				</s:form>

				<div id="test"></div>
			</div>
		</div>




	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_advMessage_Result.jsp,v $
$Revision: 1.2 $
</code>