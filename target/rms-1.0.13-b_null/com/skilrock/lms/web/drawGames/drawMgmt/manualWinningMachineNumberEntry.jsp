
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.Timestamp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/drawGameMachineNbrEntry.js"></script>

		<s:head theme="ajax" />
	</head>
	<body onload="fillWinningDivs()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="success">
			<h2>
				<s:text name="label.submision.of.mc.nbr"/>
			</h2>
			<s:form name="manualEntryForm" action="performManualWinningForMachineEntry"
				onsubmit="return validateManualEntry('%{gameName}')">
				<s:hidden name="gameNo" id="gameNo" value="%{gameNo}"></s:hidden>
				<s:hidden name="gameName" id="gameName" value="%{gameName}"></s:hidden>
				<s:hidden id="winNumSize" name="winNumSize" value=""></s:hidden>
				<s:if
					test="%{gameName.equalsIgnoreCase('Zimlottotwo') || gameName.equalsIgnoreCase('Zimlotto') || gameName.equalsIgnoreCase('Lotto') || gameName.equalsIgnoreCase('Fastlotto')}">
					<h3 align="left">
						*<s:text name="msg.enter.win.res.ascending.ord"/>.
					</h3>
					<br></br>
				</s:if>
				<table width="800" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="800" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="25%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th width="25%" scope="col" align="center">
							<s:text name="label.mc.nbr"/>
						</th>
						<th width="25%" scope="col" align="center">
							<s:text name="label.msg"/>
						</th>
						<th width="40%" scope="col" align="center">
							<s:text name="label.draw.type"/>
						</th>
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%">
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="22%" align="center">
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
			</s:form>
		</div>
		<div id="manual_validate_err"></div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: manualWinningMachineNumberEntry.jsp,v $
$Revision: 1.3 $
</code>