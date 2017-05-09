<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" /><link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_dg_ticket_validate.js"></script>
	</head>

	<body onload="getAgentList();ActiveInactDivs();">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.block.unblk.tkt"/>
				</h3>
				<s:form name = "blockUnblockTicket" id  = "blockUnblockTicket" action="bo_dm_unblock_ticket_display_search" onsubmit="return validateDataUnBlock();">
					<table width="400" cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td colspan="2" align="left">
								<div id="errorDiv"></div>
							</td>
						</tr>
						<tr>
							<td align="left" nowrap>
								<s:text name="label.srch.criteria"/> <font color = "red">*</font>
							</td>
							<td align="left">
								<s:radio list="#{'2':'All','1':'Enter Ticket No.'}"
									name="criteria" id="criteria" theme="simple"
									onchange="ActiveInactDivs();" value = "2"/>
							</td>
						</tr>
						<tr>
							<td align="left" nowrap>
								<s:text name="label.game.name"/> <font color = "red">*</font>
							</td>
							<td align="left">

								<s:select name="gameNo" id="gameNo" theme="simple" 
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									list="%{drawGameInfo}" cssClass="option" required="true" onchange="_id.i('errorDiv', '', this);_id.i('down', '', this);_id.i('down1', '', this);"/>
							</td>
						</tr>
						<tr>
						<td>
								<div id="criLable">
									<s:text name="label.status"/>
								</div>
							</td>
						<td>
								<div id="criValue">
									<s:select id="reason" name="reason"
										headerKey="ALL" headerValue="%{getText('label.all')}" theme="simple"
										list="{'TEMPORARY_BLOCK','UNBLOCKED'}"
										cssClass="option" onchange="_id.i('errorDiv', '', this);_id.i('down', '', this);_id.i('down1', '', this);"/>
								</div>
							</td>
						</tr>
						<tr>
							<td align="left" nowrap>
								<div id="agtLable">
									 <s:text name="label.agt.name"/>
								</div>
							</td>

							<td align="left">
							<div id="agtValue">
								<s:select name="agentOrgId"
									onchange="getRetailerUserList(this.value); _id.i('errorDiv', '', this);_id.i('down', '', this);_id.i('down1', '', this); return verifyRetailer(this.options[this.selectedIndex].text);"
									id="agentOrgId" list="{}"
									headerValue="%{getText('label.please.select')}" headerKey="-1" theme="simple" cssClass="option"></s:select>
							</div>		
							</td>
						</tr>
					
						<tr>
							<td align="left" nowrap>
								<div id="retLable">
									<s:text name="Retailer" /> <s:text name="label.name"/>
								</div>
							</td>
							<td align="left">
								<div id="retValue">
									<s:select name="retOrgId" id="retOrgId" headerKey="-1"
										headerValue="%{getText('label.please.select')}" list="{}" theme="simple"
										cssClass="option"
										onchange="_id.i('errorDiv', '', this);_id.i('down', '', this);_id.i('down1', '', this);" />
								</div>
							</td>
						</tr>
						<tr>

							<td align="left" nowrap>
								<div id="tktLable" style="display: none;">
									<s:text name="label.entr.tckt.no"/>.<font color = "red">*</font>
								</div>
							</td>

							<td align="left">
							<div id="tktValue" style="display: none;">
								<input type="text" name="ticketNo" id = "ticketNo" onkeypress="return checkNum(event);" size="20" maxlength="20" style="margin-top: -20px;"/>
							</div>		
							</td>
						</tr>
					</table>
					<tr>
						<td colspan="2">
							<s:submit name="search" key="btn.srch" id="search" align="center"
								targets="down" theme="ajax" cssClass="button"/>
						</td>
					</tr>
				</s:form>

				<div id="down" style="text-align: center"></div>
				<div id="down1" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>



	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dm_unblock_specific_ticket_menu.jsp,v $ $Revision:
	1.1.1.1.1.1 $
</code>