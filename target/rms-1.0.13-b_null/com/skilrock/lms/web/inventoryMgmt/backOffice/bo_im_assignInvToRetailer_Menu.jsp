<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.css"
			media="screen" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>var path = "<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/slider/js/jquery-1.3.2.min.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/assignInvToRet.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>
	<body onload="chageBrandList(document.saveInv.invId.value, 'brand', 'brand_list','<s:property value="%{brandsId}" />');chageBrandList(document.saveInv.brandsId.value, 'model', 'model_list','<s:property value="%{modelId}" />');">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.assign.inv.to" />
					<s:property value="#application.TIER_MAP.RETAILER" />
				</h3>

				<s:form name="saveInv" action="bo_im_assignInvToRetailer_Save" onsubmit="return validateForm()">
					<s:select name="Spec" id="brand_list" theme="simple"
						cssStyle="display:none" list="%{brandMap}" />
					<s:select name="Spec" id="model_list" theme="simple"
						cssStyle="display:none" list="%{modelMap}" />
					<s:select name="Spec" id="terminal_list" theme="simple"
						cssStyle="display:none" list="#session.TEMINAL_LIST" />
					<div id="err_div" style="text-align: center; width: 400px"></div>
					<div id="err" style="color: red">
						<s:actionmessage/>
						<s:actionerror/>
					</div>

					<table border="0" cellpadding="2" cellspacing="0" width="360px"
						style="text-align: left">
						<tr>
							<td align="right">
								<div id="ron"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div style="color: red; margin-left: 100px" id="agentName_e"></div>
							</td>
						</tr>
						<s:select name="agtOrgId" id="agentOrgId"
							label="%{getText('label.select')} %{#application.TIER_MAP.AGENT} %{getText('label.org')}"
							list="{}" cssClass="option" headerKey="-1"
							headerValue="%{getText('label.please.select')}" required="true" />
						<tr>
							<td colspan="2">
								<div style="color: red; margin-left: 100px" id="retOrgId_e"></div>
							</td>
						</tr>
						<s:select name="retOrgId" id="retOrgId"
							label="%{getText('label.select')} %{#application.TIER_MAP.RETAILER} %{getText('label.org')}"
							list="{}" cssClass="option" headerKey="-1"
							headerValue="%{getText('label.please.select')}" required="true" />

						<tr>
							<td colspan="2">
								<div id="inv_err" style="color: red; margin-left: 100px"></div>
							</td>
						</tr>
						<s:select name="invId" id="inv" key="label.invntry.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="%{invMap}" cssClass="option" />
						<tr>
							<td colspan="2">
								<div id="brand_err" style="color: red; margin-left: 100px"></div>
							</td>
						</tr>
						<s:select name="brandsId" id="brand" key="label.brand.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="{}" cssClass="option" />
						<tr>
							<td colspan="2">
								<div id="model_err" style="color: red; margin-left: 100px"></div>
							</td>
						</tr>
						<s:select name="modelId" id="model" key="label.model.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="{}" cssClass="option" />
						<tr>
							<td colspan="2">
								<div id="tid_err" style="color: red; margin-left: 100px"></div>
							</td>
						</tr>
						<s:textfield key="label.inv.no" id="terminalId" name="invSrNo" size="20" required="true" cssClass="option" />
						<!--<tr>
							<td align="right" class="label">
								<label class="label">
									<s:text name="label.trmnl.id" />
									<span class="required">*</span>:
								</label>
							</td>
							<td align="left">
								<s:textfield key="label.inv.no" id="terminalId" name="invSrNo" size="20" required="true"  cssClass="option" />
							</td>
						</tr>
						-->
						<tr>
							<td colspan="2"></td>
						</tr>
						<s:submit key="btn.assign" id="assign"
							cssStyle="text-align:center;" cssClass="button" />

					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_assignInvToRetailer_Menu.jsp,v $ $Revision:
	1.1.2.1.4.1.2.4.8.1 $
</code>