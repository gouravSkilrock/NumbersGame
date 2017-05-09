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
		</style>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
	<h1>Default Values</h1>
		<div id="wrap">
			<div id="top_form">
				<s:if test="%{retCriteriaList != null and retCriteriaList.size>0}">
					<div>
						<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
							<tr>
								<td>Self <s:text name="Retailer" /> : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('CLAIM_AT_SELF_RET')}" /></td>
								<td>Self <s:text name="AGENT" /> : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('CLAIM_AT_SELF_AGT')}" /></td>
								<td>Other <s:text name="Retailer" /> of Same <s:text name="AGENT" /> : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('CLAIM_AT_OTHER_RET_SAME_AGT')}" /></td>
								<td>Other <s:text name="Retailer" /> : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('CLAIM_AT_OTHER_RET')}" /></td>
								<td>Other <s:text name="AGENT" /> : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('CLAIM_AT_OTHER_AGT')}" /></td>
								<td><s:text name="BOUser" /> : <s:property value="%{new com.skilrock.lms.common.Utility().getPropertyValue('CLAIM_AT_BO')}" /></td>
							</tr>
						</table>
					</div>
					<br/>
						<input type="button" value="Reset Selected to Default" onclick="setToDefault();" />
					<br/>
					<br/>
					<div>
						<table id="dataTable" width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" style="table-layout:fixed;word-wrap:break-word">
							<thead>
								<tr style="w">
									<th align="center">S.No</th>
									<th align="center">Select All</th>
									<th align="center"><s:text name="Retailer" /> Name</th>
									<th align="center">Self <s:text name="Retailer" /></th>
									<th align="center">Self <s:text name="AGENT" /></th>
									<th align="center">Other <s:text name="Retailer" /> of Same <s:text name="AGENT" /></th>
									<th align="center">Other <s:text name="Retailer" /></th>
									<th align="center">Other <s:text name="AGENT" /></th>
									<th align="center"><s:text name="BOUser" /></th>
								</tr>
								<tr>
									<th align="center">&nbsp;</th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'selRetailer');" /></th>
									<th align="center">&nbsp;</th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'selfRetailer');" /></th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'selfAgent');" /></th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'otherRetailerSameAgent');" /></th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'otherRetailer');" /></th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'otherAgent');" /></th>
									<th align="center"><input type="checkbox" onclick="selectAll(this, 'atBO');" /></th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="retCriteriaList" status="status">
									<tr>
										<td><s:property value="%{#status.index+1}" /></td>
										<td align="center"><input type="checkbox" class="selRetailer" /></td>
										<td>
											<s:hidden cssClass="retOrgId" name="" value="%{retOrgId}" />
											<s:property value="retOrgName" />
										</td>
										<td align="center" class='<s:if test="%{selfRetailer eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="selfRetailer_%{retOrgId}" name="selfRetailer" value="%{selfRetailer}" onclick="selectCB(this);" cssClass="selfRetailer" theme="simple" />
										</td>
										<td align="center" class='<s:if test="%{selfAgent eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="selfAgent_%{retOrgId}" name="selfAgent" value="%{selfAgent}" onclick="selectCB(this);" cssClass="selfAgent" theme="simple" />
										</td>
										<td align="center" class='<s:if test="%{otherRetailerSameAgent eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="otherRetailerSameAgent_%{retOrgId}" name="otherRetailerSameAgent" value="%{otherRetailerSameAgent}" onclick="selectCB(this);" cssClass="otherRetailerSameAgent" theme="simple" />
										</td>
										<td align="center" class='<s:if test="%{otherRetailer eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="otherRetailer_%{retOrgId}" name="otherRetailer" value="%{otherRetailer}" onclick="selectCB(this);" cssClass="otherRetailer" theme="simple" />
										</td>
										<td align="center" class='<s:if test="%{otherAgent eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="otherAgent_%{retOrgId}" name="otherAgent" value="%{otherAgent}" onclick="selectCB(this);" cssClass="otherAgent" theme="simple" />
										</td>
										<td align="center" class='<s:if test="%{atBO eq true}">selectedTheme</s:if><s:else>deselectedTheme</s:else>'>
											<s:checkbox id="atBO_%{retOrgId}" name="atBO" value="%{atBO}" onclick="selectCB(this);" cssClass="atBO" theme="simple" />
										</td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<br/>
						<table>
							<tr>
								<td><input type="button" value="Refresh" onclick="fetchRetailerData();" /></td>
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
	$RCSfile: bo_um_updateSoldTicketClaimCriteria_Details.jsp,v $ $Revision: 1.3 $
</code>