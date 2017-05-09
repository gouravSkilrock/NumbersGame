<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/commercialService/productMgmt/backOffice/js/prodReg.js"></script>
	</head>
	<body onload="fetchOptions()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<h3><s:text name="label.prod.regi"/></h3>
			<div id="top_form">
				<s:form name="productReg" action="bo_cs_productReg_submit" onsubmit="return isFormValid()">
					<table cellpadding="2" cellspacing="0" border="0" width="450">
						<tr>
							<td>
								<div id="catIdErr" style="color:red"/>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="label.slct.cat"/>:
							</td>
							<td>
								<div id="catList"></div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="prodCodeErr" style="color:red"/>
							</td>
						</tr>
						
						<s:textfield name="productCode" id="prodCode" key="label.entr.prod.code" maxLength="15" size="10" value=""></s:textfield>
						<tr>
							<td>
								<div id="operatorCodeErr" style="color:red"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="label.slct.ope"/>:
							</td>
							<td>
								<div id="optList"></div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="circleCodeErr" style="color:red"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="label.slct.circle"/>:
							</td>
							<td>
								<div id="cirList"></div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="denoErr" style="color:red"></div>
							</td>
						</tr>
						<s:checkbox id="flexiId" onclick="flexiClick()" key="label.flexible" name="isFlexi" value="false"></s:checkbox>
						<s:textfield name="denomination" id="deno" key="label.entr.denomination" maxLength="10" size="8" value=""></s:textfield>
						<tr>
							<td>
								<div id="talktimeErr" style="color:red"></div>
							</td>
						</tr>
						<s:textfield name="talktime" id="talktime" key="label.entr.talktime" maxLength="10" size="8" value=""></s:textfield>
						<tr>
							<td>
								<div id="validityErr" style="color:red"></div>
							</td>
						</tr>
						<s:textfield name="validity" id="validity" key="label.entr.validity" maxLength="10" size="8" value=""></s:textfield>
						<tr>
							<td>
								<div id="adminFeeErr" style="color:red"></div>
							</td>
						</tr>
						<s:textfield name="adminFee" id="adminFee" key="label.entr.admin.fee" maxLength="10" size="8" value=""></s:textfield>
						<tr>
							<td>
								<div id="serviceTaxErr" style="color:red"></div>
							</td>
						</tr>
						<s:textfield name="serviceTax" id="serviceTax" key="label.entr.service.tax" maxLength="10" size="8" value=""></s:textfield>
						<tr>
							<td>
								<div id="rechargeInstErr" style="color:red"></div>
							</td>
						</tr>
						<s:textfield name="rechargeInst" id="rechargeInst" key="label.entr.recharge.instruction" maxLength="30" size="8" value=""></s:textfield>
						<tr>
							<td>
								<div id="prodDescErr" style="color:red"/>
							</td>
						</tr>
						<s:textarea cssStyle="border:1px dashed #000000;" name="productDesc" id="prodDesc" key="label.dscrptn" rows="50" cols="100"></s:textarea>
						<tr>
							<td>
								<div id="supplierNameErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="supplierName" id="supplierName" key="label.supplier.name"></s:textfield>
						<tr>
							<td>
								<div id="unitPriceErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="unitPrice" id="unitPrice" key="label.unit.price" value=""></s:textfield>
						<tr>
							<td>
								<div id="retailerCommErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="retailerComm" id="retailerComm" label="%{#application.TIER_MAP.RETAILER} commision" value=""></s:textfield>
						<tr>
							<td>
								<div id="agentCommErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="agentComm" id="agentComm" label="%{#application.TIER_MAP.AGENT} commision" value=""></s:textfield>
						<tr>
							<td>
								<div id="jvCommErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="jvComm" id="jvComm" label="JV commision" value=""></s:textfield>
						<tr>
							<td>
								<div id="goodCauseAmtErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="goodCauseAmt" id="goodCauseAmt" label="%{getText('Good_Cause')}" value=""></s:textfield>
						<tr>
							<td>
								<div id="vatAmtErr" style="color:red"/>
							</td>
						</tr>
						<s:textfield name="vatAmt" id="vatAmt" label="%{getText('VAT')}" value=""></s:textfield>
						<tr>
							<td align="right">
								<s:submit name="prodSubmit" key="btn.reg" cssClass="button"></s:submit>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_cs_productReg_main.jsp,v $
$Revision: 1.3 $
</code>
