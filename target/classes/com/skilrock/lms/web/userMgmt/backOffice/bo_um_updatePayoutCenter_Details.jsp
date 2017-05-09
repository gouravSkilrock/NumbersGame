<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<style type="text/css">
			.selectedTheme {
				background-color: #BCF5A9;
			}
			.deselectedTheme {
				background-color: #F79F81;
			}
			.updateTheme {
				background-color: #819FF7;
			}
			.invalidTheme {
				background-color: #FF0000;
			}
		</style>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
	<h1>Default Values</h1>
		<div id="wrap">
			<div id="top_form">
				<s:if test="%{payoutCenterList != null and payoutCenterList.size>0}">
					<div>
						<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
							<tr>
								<s:if test="%{orgType eq 'AGENT'}">
									<td>Claim At Self : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('SELF_CLAIM_AGT')}" /></td>
									<td>Claim At Other : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('OTHER_CLAIM_AGT')}" /></td>
								</s:if>
								<s:elseif test="%{orgType eq 'RETAILER'}">
									<td>Claim At Self : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('SELF_CLAIM_RET')}" /></td>
									<td>Claim At Other : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('OTHER_CLAIM_RET')}" /></td>
								</s:elseif>

								<s:if test="%{orgType eq 'AGENT'}">
									<td>Verification Limit : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('agtVerLimit')}" /></td>
								</s:if>
								<s:elseif test="%{orgType eq 'RETAILER'}">
									<td>Verification Limit : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('retVerLimit')}" /></td>
								</s:elseif>

								<s:if test="%{orgType eq 'AGENT'}">
									<td>Min Claim Amount : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('MIN_CLAIM_PER_TICKET_AGT')}" /></td>
									<td>Max Claim Amount : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('MAX_CLAIM_PER_TICKET_AGT')}" /></td>
								</s:if>
								<s:elseif test="%{orgType eq 'RETAILER'}">
									<td>Min Claim Amount : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('MIN_CLAIM_PER_TICKET_RET')}" /></td>
									<td>Max Claim Amount : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('MAX_CLAIM_PER_TICKET_RET')}" /></td>
								</s:elseif>

								<s:if test="%{orgType eq 'AGENT'}">
									<td>Max Daily Claim Limit : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('MAX_PER_DAY_PAY_LIMIT_FOR_AGENT')}" /></td>
								</s:if>
							</tr>
						</table>
					</div>
					<br/>
						<input type="button" value="Reset Selected to Default" onclick="setToDefault();" />
					<br/>
					<br/>
					<div>
						<table id="dataTable" width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
							<thead>
								<tr>
									<th align="center">S.No</th>
									<th align="center">Select All</th>
									<s:if test="%{orgType eq 'AGENT'}">
										<th align="center"><s:text name="AGENT" /> Name</th>
									</s:if>
									<s:elseif test="%{orgType eq 'RETAILER'}">
										<th align="center"><s:text name="Retailer" /> Name</th>
									</s:elseif>
									<th align="center">Claim At Self</th>
									<th align="center">Claim At Other</th>
									<th align="center">Verification Limit</th>
									<th align="center">Min Claim Amount</th>
									<th align="center">Max Claim Amount</th>
									<s:if test="%{orgType eq 'AGENT'}">
										<th align="center">Max Daily Claim Limit</th>
									</s:if>
								</tr>
								<tr>
									<th align="center">&nbsp;</th>
									<th align="center"><input type="checkbox" onclick="selectAllCB(this, 'selAllOrg');" /></th>
									<th align="center">&nbsp;</th>
									<th align="center"><input type="checkbox" onclick="selectAllCB(this, 'claimAtSelf');" /></th>
									<th align="center"><input type="checkbox" onclick="selectAllCB(this, 'claimAtOther');" /></th>
									<th align="center"><input type="text" onchange="selectAllTB(this, 'verificationLimit');" onkeydown="validateNumValues(event);" onpaste="return false;" size="8" maxlength="10" /></th>
									<th align="center"><input type="text" onchange="selectAllTB(this, 'minClaimAmount');" onkeydown="validateNumValues(event);" onpaste="return false;" size="8" maxlength="10" /></th>
									<th align="center"><input type="text" onchange="selectAllTB(this, 'maxClaimAmount');" onkeydown="validateNumValues(event);" onpaste="return false;" size="8" maxlength="10" /></th>
									<s:if test="%{orgType eq 'AGENT'}">
										<th align="center"><input type="text" onchange="selectAllTB(this, 'claimLimit');" onkeydown="validateNumValues(event);" onpaste="return false;" size="8" maxlength="10" /></th>
									</s:if>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="payoutCenterList" status="status">
									<tr>
										<td><s:property value="%{#status.index+1}" /></td>
										<td align="center"><input type="checkbox" class="selAllOrg" /></td>
										<td>
											<s:hidden cssClass="orgId" name="" value="%{orgId}" />
											<s:property value="orgName" />
										</td>
										<td align="center" class='<s:if test="%{claimAtSelf eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="claimAtSelf_%{orgId}" name="claimAtSelf" value="%{claimAtSelf}" onclick="selectCB(this);" cssClass="claimAtSelf" theme="simple" />
										</td>
										<td align="center" class='<s:if test="%{claimAtOther eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="claimAtOther_%{orgId}" name="claimAtOther" value="%{claimAtOther}" onclick="selectCB(this);" cssClass="claimAtOther" theme="simple" />
										</td>
										<td align="center">
											<s:textfield id="verificationLimit_%{orgId}" name="verificationLimit" value="%{verificationLimit}" cssClass="verificationLimit" onchange="selectCB(this);" onkeydown="validateNumValues(event);" size="8" maxlength="10" theme="simple" />
										</td>
										<td align="center">
											<s:textfield id="minClaimAmount_%{orgId}" name="minClaimAmount" value="%{minClaimAmount}" cssClass="minClaimAmount" onchange="selectCB(this);" onkeydown="validateNumValues(event);" size="8" maxlength="10" theme="simple" />
										</td>
										<td align="center">
											<s:textfield id="maxClaimAmount_%{orgId}" name="maxClaimAmount" value="%{maxClaimAmount}" cssClass="maxClaimAmount" onchange="selectCB(this);" onkeydown="validateNumValues(event);" size="8" maxlength="10" theme="simple" />
										</td>
										<s:if test="%{orgType eq 'AGENT'}">
											<td align="center">
												<s:textfield id="claimLimit_%{orgId}" name="claimLimit" value="%{claimLimit}" cssClass="claimLimit" onchange="selectCB(this);" onkeydown="validateNumValues(event);" size="8" maxlength="10" theme="simple" />
											</td>
										</s:if>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<br/>
						<table>
							<tr>
								<td><input type="button" value="Refresh" onclick="fetchAgentRetailerData();" /></td>
								<td><input type="button" value="Update" onclick="updateValues();" /></td>
							</tr>
						</table>
					</div>
				</s:if>
				<s:else>
					<h3>
						<center>No Record to Process</center>
					</h3>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_updatePayoutCenter_Details.jsp,v $ $Revision: 1.3 $
</code>