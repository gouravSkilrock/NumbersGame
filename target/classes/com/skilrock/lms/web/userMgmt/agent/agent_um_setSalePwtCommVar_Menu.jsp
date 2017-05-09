
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/agent/js/agentPwtCommVar.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" /> Sale and Winning Profit Variance
				</h3>

				<s:form id="searchgame" action="agent_um_setSalePwtCommVar_Search"
					onsubmit="return validateFormEntries();">

					<input type="hidden" style="background-color: yellow"
						name="buttonValue" id="hiddenInput" value="Search" />
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="center" colspan="2">
								<div id="agentOrgError"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:select name="serviceName" id="serviceName" headerKey="-1"
									headerValue="--Please Select--" label="Select Service"
									list="#session.serviceNameMap" cssClass="option" 
									onchange="hideGameStatus(this.value)" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<s:select name="orgType" id="orgType" headerKey="-1"
									headerValue="--Please Select--"
									label="Select Organization Type"
									list="#session.organizationTypeMap" cssClass="option"
									onchange="getOrgListing('getOrgList.action?orgType='+ this.value,'agentOrgName');" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<s:select name="agentOrgName" id="agentOrgName" label="Select Organization"
									list="{}" cssClass="option" headerKey="-1"
									headerValue="--Please Select--"
									onchange="hideDiv();" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:select name="gamestatus" id="gameStatus" label="Game Status"
									list="#{'ALL':'--Please Select', 'CLOSE':'CLOSE', 'OPEN':'OPEN', 'SALE_CLOSE':'SALE_CLOSE', 'SALE_HOLD':'SALE_HOLD'}"
									cssClass="option" onchange="hideDiv();" />
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit name='button' id="searchbutoon" value="Search"
										align="right" targets="result" theme="ajax" cssClass="button"
										onclick="setInputHiddenValue(this.value)" />
								</table>
							</td>
						</tr>
					</table>

				</s:form>
				<div id="result"></div>
				<s:div id="updateGameDiv"></s:div>
				<br />
				<br />
			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agent_um_setSalePwtCommVar_Menu.jsp,v $
$Revision: 1.2 $
</code>