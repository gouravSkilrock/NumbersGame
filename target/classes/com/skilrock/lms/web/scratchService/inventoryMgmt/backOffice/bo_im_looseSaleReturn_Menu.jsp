<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<style>
.tabNo {
	text-align: left;
	vertical-align: top;
}
</style>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/looseSale.js"></script>
	</head>
	<body onload="getUserName(),appendGameTab()">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Loose Sale Return Ticket Wise Process
				</h3>

				<s:form name="looseSale" action="bo_im_looseSaleReturn_Save"
					onsubmit="return validateUser()">
					<table border="0" cellpadding="2" cellspacing="0" width="400px">
						<tr>
							<td>
								<s:hidden name="userData" id="userData" value="%{userData}"></s:hidden>
							</td>
						</tr>
						
						<tr>
							<td>
								<s:hidden name="gameData" id="gameData" value="%{gameData}"></s:hidden>
							</td>
						</tr>

						<tr>
							<td>
								<div style="color: red; text-align: right;" id="userName_e"></div>
							</td>
						</tr>
						
						<tr>
							<td align="left">
								<s:select list="{}" headerKey="-1"
									headerValue="-- Please Select--" label="Select Agent Name"
									name="userName" id="userName" cssClass="" onchange="resetFormData();"></s:select>
							</td>
						</tr>
						<tr>
						<tr>
							<td align="left" width="30%">
								<input type="button" name="addGame" value="Add More Game"
									class="button" onclick="addAnotherGame()" />
								<table style="display: none">

									<s:select name="gameNbr_NameHid" id="gameNbr_NameHid"
										headerKey="-1" headerValue="--Please Select--" list="{}" />
									<tr>
										<td>
											<input type="hidden" id="nog" name="nog"
												value="<%=application.getAttribute("NO_OF_GAME_PER_PAGE")%>" />
										</td>
									</tr>

								</table>

							</td>
						</tr>
						<tr>
							<td>
								<div style="color: red; text-align: right;" id="game_e"></div>
							</td>
						</tr>
						<tr>
							<td>
								<div style="color: red; text-align: right;" id="ticket_e"></div>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div id="mainDiv">
									<table border="0" cellpadding="2" cellspacing="2" width="100%"
										id="gameTab">

									</table>
								</div>

							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="search" value="submit" align="center"
									 cssClass="button" cssStyle="font-size: 14px"/>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_looseSaleReturn_Menu.jsp,v $ $Revision: 1.2 $
</code>