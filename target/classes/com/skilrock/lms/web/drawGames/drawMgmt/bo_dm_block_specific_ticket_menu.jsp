<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_dg_ticket_validate.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.block.tkt" />
				</h3>
				<s:form name="form" name="BlockTicket" onsubmit="return isTicketSelected();">
					<table cellspacing="0" cellpadding="3" border="0"
						bordercolor="#CCCCCC" theme="simple" align="center">
						<tr>
							<td colspan="2" align="left">
								<div id="tktErr"></div>
							</td>
						</tr>
		
						<tr>
							<td align="left">
								<input type="button" value="<s:text name="btn.add.tkt"/>"
									onclick="addTickets('innerTable')"></input>
								<input type="button" value="<s:text name="btn.remove.tkt"/>"
									onclick="removeTickets('innerTable')"></input>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="left">
								<table border="0" cellpadding="2" cellspacing="0"
									id="innerTable">
									<tr>
										<td>
											<input type="checkbox" name="chk"></input>
										</td>
										<td>
											<s:text name="label.tckt.no" />
											<input type="text" name="ticketNumArr"
												onkeypress="return checkNum(event);" size="20" maxlength="20" />
										</td>
										<td>
											<s:text name="label.reason" />
											<select name="ticketStatusArr" class="option">
												<option value="FRAUD">
													<s:text name="label.fraud" />
												</option>
												<option value="OTHERS">
													<s:text name="label.others" />
												</option>
											</select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="block" key="btn.block.tkts" id="block"
									align="left" targets="down" theme="ajax"
									action="bo_dm_block_specific_ticket_result" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: left"></div>
				<br />
				<br />
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dm_block_specific_ticket_menu.jsp,v $ $Revision:
	1.1.1.1.1.1 $
</code>