<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script
			src="<%=request.getContextPath() %>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/assignConsNonConsInv.js" />
		<s:head theme="ajax" debug="false" />
	</head>

	<body
		onload="createNonConsTbl('nonCons_tbl', 'nonCons'); createReturnInvConsTbl('cons_tbl', 'cons');">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.return.cons.noncons.inv" />
				</h3>


				<s:form name="searchgame" action="bo_im_consNonConsReturnInv_save"
					onsubmit="return _validateForm()">
					<s:select name="noInv" id="nonCons_list" theme="simple"
						cssStyle="display:none" list="#session.INV_NAME_LIST" />
					<s:select name="noInv" id="cons_list" theme="simple"
						cssStyle="display:none" list="#session.CONS_INV_NAME_LIST" />

					<s:select name="noInv" id="brand_list" theme="simple"
						cssStyle="display:none" list="#session.BRAND_NAME_LIST" />
					<s:select name="noInv" id="model_list" theme="simple"
						cssStyle="display:none" list="#session.MODEL_NAME_LIST" />
					<s:select name="noInv" id="model_spec_list" theme="simple"
						cssStyle="display:none" list="#session.MODEL_SPEC_LIST" />
					<div id="err_div" style="text-align: center; width: 400px"></div>
					<div id="err" style="color: red">
						<s:actionerror />
					</div>
					<table border="0" cellpadding="2" cellspacing="0" width="500px">

						<tr>
							<td align="right">
								<label class="label">
									<s:text name="label.return.from" />
									:&nbsp;
								</label>
							</td>
							<td>
								<s:select name="ownerType" id="owner_type" theme="simple"
									value="'ALL'"
									list="#{'AGENT':getText('AGENT')}"
									onchange="changeOwnerType(this)" cssClass="option"
									onfocus="_id.non('assign')" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<label id="agt_label" style="display: block" class="label">
									<s:property value="#application.TIER_MAP.AGENT" />
									<s:text name="label.add.more" />
									:&nbsp;
								</label>
							</td>
							<td>
								<s:select name="agtOrgId" theme="simple" id="agt_org_list"
									cssStyle="display:block" list="{}" headerKey="-1"
									headerValue="%{getText('label.please.select')}" onfocus="_id.non('assign')"
									onchange=" fillSelBox(this.value, 'ret_org_list', 'ret_list');"
									cssClass="option" />
							</td>
						</tr>

						<tr>
							<td align="right">
								<label id="ret_label" style="display: none" class="label">
									<s:property value="#application.TIER_MAP.RETAILER" />
									<s:text name="label.org.name" />
									:&nbsp;
								</label>
							</td>
							<td>
								<s:select name="retOrgId" id="ret_org_list" theme="simple"
									cssStyle="display:none" list="{}" headerKey="-1"
									onfocus="_id.non('assign')" headerValue="%{getText('label.please.select')}"
									cssClass="option" />
							</td>
						</tr>

					</table>

					<div id="hiddFldDiv"></div>
					<div id="nonCons_err_div"></div>
					<table border="1" cellpadding="2" cellspacing="0" width="100%"
						bordercolor="#CCCCCC" id="nonCons_tbl">
						<tr>
							<th align="center">
								<table width="100%">
									<tr>
										<td>
											<s:text name="label.return.nonconsum.invntry" />
										</td>
										<td>
											<input type="button" value="<s:text name="label.add.more" />"
												onclick="createNonConsTbl('nonCons_tbl', 'nonCons')" />
										</td>
									</tr>
								</table>
							</th>
						</tr>

					</table>


					<br />
					<br />
					<div id="cons_err_div"></div>
					<table border="1" cellpadding="2" cellspacing="0" width="660"
						bordercolor="#CCCCCC" id="cons_tbl">
						<tr>
							<th align="center">
								<table width="100%">
									<tr>
										<td>
											<s:text name="label.return.consum.invntry" />
										</td>
										<td>
											<input type="button" value="<s:text name="label.add.more" />"
												onclick="createConsTbl('cons_tbl', 'cons');" />
										</td>
									</tr>
								</table>
							</th>
						</tr>

					</table>



					<br />
					<table border="0" cellpadding="2" cellspacing="0" width="650px">
						<tr>
							<td align="right" width="450px">
								<input type="button" value="<s:text name="btn.verify" />"
									id="verify" style="text-align: center; display: block"
									onclick="verifyReturnInv('bo_im_verifyReturnInv.action')"
									class="button" />
							</td>
							<td align="center">

								<s:submit key="btn.ret" id="Return" theme="simple"
									cssStyle="text-align:center;display:none" cssClass="button" />
							</td>
						</tr>
					</table>

				</s:form>


			</div>
		</div>




	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_consNonConsReturnInv_menu.jsp,v $ $Revision:
	1.3.2.1.4.1.2.5.8.1.2.1 $
</code>