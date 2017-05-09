
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/salePwtCommVar.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h3>
					<s:property value="%{getText('AGENT')}" /> <s:text name="label.sale.and"/> <s:text name="PWT"/> <s:text name="label.profit.var"/>
				</h3>

				<s:form id="searchgame" action="bo_um_setSalePwtCommVar_Search"
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
									headerValue="%{getText('label.please.select')}" key="label.slct.service"
									list="#session.serviceNameMap" cssClass="option"
									onchange="hideGameStatus(this.value);" required="true" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<s:select name="orgType" id="orgType" headerKey="-1"
									headerValue="%{getText('label.please.select')}"
									key="label.select.org.type" list="#{'AGENT':getText('AGENT'),'RETAILER':getText('Retailer')}"
									cssClass="option" onchange="getOrgListing();hideRetailerStatus(this.value)" required="true" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<s:select name="agentOrgName" id="agentOrgName"
									label="%{getText('label.select')} %{getText('AGENT')} %{getText('label.org')}" list="{}" cssClass="option"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									onchange="getRetList(this.value),hideDiv();" required="true" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:select name="retailerOrgName" id="retailerOrgName"
									label="%{getText('label.select')} %{#application.TIER_MAP.RETAILER} %{getText('label.org')}" list="{}"
									cssClass="option" headerKey="-1"
									headerValue="%{getText('label.please.select')}" onchange="hideDiv();"
									required="true" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:select name="gamestatus" id="gameStatus" key="label.game.status"
									list="#{'ALL':getText('label.please.select'), 'CLOSE':getText('CLOSE'), 'OPEN':getText('OPEN'), 'SALE_CLOSE':getText('SALE_CLOSE'), 'SALE_HOLD':getText('SALE_HOLD')}"
									cssClass="option" onchange="hideDiv();" />
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit name='button' id="searchbutoon" key="btn.srch"
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
$RCSfile: bo_um_setSalePwtCommVar_Menu.jsp,v $
$Revision: 1.4 $
</code>