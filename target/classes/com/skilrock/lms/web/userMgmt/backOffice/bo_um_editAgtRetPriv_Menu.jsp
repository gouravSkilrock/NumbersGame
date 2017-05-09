<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/bo_um_editAgtRetPriv.js"></script>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.edit" />
					<s:property value="#application.TIER_MAP.AGENT" />
					/
					<s:property value="#application.TIER_MAP.RETAILER" />
					<s:text name="label.priv" />
				</h3>
				<s:actionerror />
				<s:actionmessage />
				<s:form name="editOrgPriv" action="bo_om_editOrgPriv_Save.action"
					method="POST" onsubmit="return validate()">
					<table width="400" cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td colspan="2">
								<div id="orgType_e" style="color: red"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:select cssClass="option" name="organizationType"
									id="Org_type" key="label.org.type"
									list="#{'AGENT':#application.TIER_MAP.AGENT.toUpperCase(),'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}"
									onchange="retURL('agentlist',this.value)"
									headerValue="%{getText('label.please.select')}" headerKey="-1" required="true"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="agtName_e" style="color: red"></div>
							</td>
						</tr>

						<tr>
							<td id="agentDiv" align="right"></td>
							<td align="left">
								<div id="agentlistDiv" style="margin-left: 20px; display: none">

									<table style="margin-left: 10px">
										<tr>
											<td>
												<s:text name="label.select" />
												<s:text name="%{#application.TIER_MAP.AGENT}" />
												<s:text name="label.org" />*
											</td>
											<td>
												<s:select name="agentOrgId" id="agentOrgId" list="{}"
													cssClass="option" headerKey=""
													headerValue="%{getText('label.please.select')}"
													onchange="getData(this.value);" required="true" />
											</td>
										</tr>

									</table>

								</div>
							</td>
						</tr>

						<tr>
							<td id="retDiv"></td>
							<td>
								<div id="retlistDiv" style="display: none">

									<table>
										<tr>
											<td>
												<s:text name="label.select" />
												<s:text name="%{#application.TIER_MAP.RETAILER}" />
												<s:text name="label.org" />*
											</td>
											<td>
												<s:select name="retOrgId" id="retOrgId" list="{}"
													cssClass="option" headerKey="-1"
													headerValue="%{getText('label.please.select')}"
													onchange="getOrgPriveledges(this.value);" required="true" />
											</td>
										</tr>

									</table>
								</div>
							</td>
						</tr>


						<tr>
							<td id="lodingImage" style="display: none; color: red;">
								Wait...
								<img
									src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" />
							</td>
						</tr>
					</table>

					<table width="100%" cellpadding="2" cellspacing="0" border="0"
						align="center">
						<tr>
							<td>
								<div id="result"></div>
							</td>
						</tr>
					</table>

				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_editAgtRetPriv_Menu.jsp,v $ $Revision: 1.7 $
</code>