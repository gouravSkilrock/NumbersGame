<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
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
	
	</head>
	<body onkeypress=submitForm(event);; onload=" getAgentList()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.tkt.cancel" />
				</h3>
				<div id="tktErrMsgDiv"></div>
				<s:form name="ticketCancellation" id="ticketCancellation"
					onsubmit="return validateTicket();">
					<s:token name="token" />
					<table>
						<s:radio
							list="#{'Enter_Ticket':getText('label.entr.tckt.no'),'Last_Ten':getText('label.last.10ticket')}"
							name="search_type" id="search_type" value="'Enter_Ticket'"
							key="label.cancel.type" onclick="return checkType()" />
					</table>
					<div id="errMsgDiv" >
						<br/>
					</div>
					<table id="retTable" style="display: none" border="0"
						cellpadding="1" cellspacing="0" bordercolor="#CCCCCC" align="left"
						width="80%">
						<s:select list="{}"
							label="%{getText('label.select')} %{#application.TIER_MAP.AGENT}" id="agtId"
							name="agentOrgName" cssClass="option" headerKey="-1"
							headerValue="%{getText('label.please.select')}"
							onchange="getRetailerUserList(this.value)"></s:select>
						<s:select list="{}"
							label="%{getText('label.select')} %{#application.TIER_MAP.RETAILER}" id="retOrgId"
							name="retOrgId" cssClass="option" headerKey="-1"
							headerValue="%{getText('label.please.select')}" onchange="checkRetNGame()"></s:select>
						<s:select name="gameNo" key="label.game.name" id="gameNo"
							list="%{#session.DRAWGAME_LIST}" cssClass="option" headerKey="-1"
							headerValue="%{getText('label.please.select')}" onchange="checkRetNGame();"></s:select>
						<tr>
							<td colspan="2" align="center">
								<input type="button"
									onclick="verifyNShowTkts();"
									value="<s:text name="btn.show.tkts" />" class="button" />
							</td>
						</tr>
					<tr>
					<td colspan="2" align="center">	<div id="lastTenDiv" style="display: none;">
						</div>
					</td>	
						</tr>
					</table>
					<div id="enterTktDiv">
						<s:text name="label.entr.tckt.no" /> <span class="required">*</span><s:textfield id="ticketNo"
							maxlength="20" name="ticketNo" onkeydown="return checkNum(this);"
							onkeyup="return checkNum(this);"
							onkeypress="return checkLen(this);"></s:textfield> 
						<s:text name="label.slct.rsn.for.cancltn" /> <span class="required">*</span>:<s:select name="reason" id="reason"
							 cssClass="option"
							headerKey="-1" headerValue="%{getText('label.pls.select.reason')}"
							list="{'WRONG_NUMBER_PICKED','WRONG_AMOUNT_PLAYED','TICKET_NOT_PRINTED', 'OTHER'}" />
						<s:submit formId="ticketCancellation" id="subButton" cssClass="button" key="btn.submit"
							align="center" action="bo_dg_ticketCancellation_Submit"></s:submit>
					</div>
					
				</s:form>
				<div id="result"></div>
				<div id="test"></div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_ticketCancellation_Menu.jsp,v $ $Revision: 1.1.2.9.8.1
	$
</code>