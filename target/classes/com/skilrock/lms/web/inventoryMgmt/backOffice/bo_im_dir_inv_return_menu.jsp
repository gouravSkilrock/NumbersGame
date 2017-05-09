<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
			<script>var path = "<%=request.getContextPath()%>"</script>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/slider/js/jquery-1.3.2.min.js"></script>
			<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/returnInvFromRet.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/inventoryMgmt/agent/js/assignConsNonConsInv.js" />

		<s:head theme="ajax" debug="false" />
	</head>

	<body onload="chageBrandList(document.saveInv.invId.value, 'brand', 'brand_list','<s:property value="%{brandsId}" />');chageBrandList(document.saveInv.brandsId.value, 'model', 'model_list','<s:property value="%{modelId}" />');">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.ret.inv.from" />
					<s:property value="#application.TIER_MAP.RETAILER" />
				</h3>
				<s:form name="returnInventory"
					action="bo_im_dir_inv_return_success"
					onsubmit="return _validateDirInvReturnItems()">
					<div id="err_div" style="text-align: center; width: 400px"></div>
					<div id="err" style="color: red">
						<s:actionerror />
					</div>

					<s:select name="Spec" id="brand_list" theme="simple"
						cssStyle="display:none" list="%{brandMap}" />
					<s:select name="Spec" id="model_list" theme="simple"
						cssStyle="display:none" list="%{modelMap}" />
					<s:select name="Spec" id="inv_List" theme="simple"
						cssStyle="display:none" list="{}" />
					<div id="err_div" style="text-align: center; width: 400px"></div>
					<div id="err" style="color: red">
						<s:actionerror />
					</div>

					<table border="0" cellpadding="2" cellspacing="0" width="360px"
						style="text-align: left">
						<tr>
							<td align="right">
								<div id="ron"></div>
							</td>
						</tr>
						
						<s:select name="agtOrgId" id="agentOrgId"
							label="%{getText('label.select')} %{#application.TIER_MAP.AGENT} %{getText('label.org')}"
							list="{}" cssClass="option" headerKey="-1"
							headerValue="%{getText('label.please.select')}" required="true" />
						
						<tr>
							<td colspan="2">
								<div id="ret_err"></div>
							</td>
						</tr>
					
						<s:select name="retOrgId" id="retOrgId" label="%{getText('label.select')} %{#application.TIER_MAP.RETAILER} %{getText('label.org')}"
							list="{}" cssClass="option" headerKey="-1" headerValue="%{getText('label.please.select')}" required="true"
							onchange="getInvList(this.value);_id.o('brand').options.length=1;_id.o('model').options.length=1;_id.o('invNo').options.length=1; " />
						<tr>
							<td colspan="2">
								<div id="inv_err"></div>
							</td>
						</tr>
						<s:select name="invId" id="invName" key="label.invntry.name" headerKey="-1"
							headerValue="%{getText('label.please.select')}" list="%{invMap}"
							cssClass="option"
							onchange="chageBrandList(this.value, 'brand', 'brand_list','-1'); _id.o('model').options.length=1;_id.o('invNo').options.length=1; " />
						<tr>
							<td colspan="2">
								<div id="brand_err"></div>
							</td>
						</tr>
						<s:select name="brandsId" id="brand" key="label.brand.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="{}" cssClass="option"
							onchange="chageBrandList(this.value, 'model', 'model_list','-1');_id.o('invNo').options.length=1;" />
						<tr>
							<td colspan="2">
								<div id="model_err"></div>
							</td>
						</tr>
						<s:select name="modelId" id="model" key="label.model.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							list="{}" cssClass="option"
							onchange="changeModelList(this.value, 'invNo', 'inv_List','-1');" />
						<tr>
							<td colspan="2">
								<div id="tern_err"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<div id="trn"></div>
							</td>
						</tr>
						<s:select name="invSrNo" id="invNo" key="label.inv.no" list="{}"
							headerKey="-1" headerValue="%{getText('label.please.select')}"
							cssClass="option" />
						<tr>
							<td colspan="2"></td>
						</tr>
						<s:submit key="btn.ret" id="Return" cssStyle="text-align:center;"
							cssClass="button" />
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_dir_inv_return_menu.jsp,v $ $Revision:
	1.1.2.1.2.5.8.1.2.2 $
</code>