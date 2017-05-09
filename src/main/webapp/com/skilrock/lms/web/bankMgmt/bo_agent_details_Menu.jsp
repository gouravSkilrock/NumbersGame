<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript">
			path="<%=request.getContextPath()%>";
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/bankMgmt/js/agt_bank_details.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="lmc"></s:text> Bank Details 
				</h3>
				<s:form name="mapBranch" id="mapBranch"
					action="bo_agent_details_Save" onsubmit="return validate();">
					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
						<tr>
							<td align="left" colspan="2">
								<div id="error">
									<s:actionerror />
									<s:actionmessage />
								</div>
							</td>
						</tr>
						<tr>
								<td colspan="2">
									<div id="agtDiv" style="color: red; text-align: center">

									</div>
								</td>
							</tr>
							<tr>
								<td>
									<s:select cssClass="option" name="agtOrgId" id="agentOrgName" list="{}"	label="%{#application.TIER_MAP.AGENT} Organization"
										headerValue="--Please Select--" headerKey="-1"		required="true"></s:select>
								</td>
							</tr>

						<tr>
							<td align="center" colspan="2">
								<div id="bankDiv" style="color: red; text-align: center"></div>
							</td>
						</tr>
						
						<tr>
							<td align="right">
								<label class="label">
									Select Bank
									<span class="required">*</span>:
								</label>
							</td>
							<td>
							<s:hidden name="bankId" id="bankId" ></s:hidden>
								<s:select theme="simple" id="bank" name="bank" headerKey="-1"
									headerValue="-- Please Select --" list="{}" cssClass="option"
									required="true" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="branchDiv" style="color: red; text-align: center"></div>
							</td>
						</tr>
						<tr id="branchRow">
							<td align="right">
								<label class="label">
									Select Branch
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<s:hidden name="branchId" id="branchId" ></s:hidden>
								<s:select theme="simple" id="branch" name="branch"
									headerKey="-1" headerValue="-- Please Select --" list="{}"
									cssClass="option" required="true" />
							</td>
						</tr>
					
						<tr>
							
							<td colspan="2">
								<s:textfield name="sortCode" id="sortCode" maxlength="30" disabled="true" label="Branch Sort Code"></s:textfield>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="accountDiv" style="color: red; text-align: center"></div>
							</td>
						</tr>
						<tr>
						
							<td colspan="2">
								<s:textfield name="accountNbr" id="accountNbr" maxlength="15" label="Enter Account Number" required="true"></s:textfield>
							</td>
						</tr>
						
						
						<tr>
							<td colspan="2">
								<s:submit name="submit" value="Save" align="center"
									cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
				<br />
				<br />
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_agent_details_Menu.jsp,v $ $Revision: 1.3 $
</code>