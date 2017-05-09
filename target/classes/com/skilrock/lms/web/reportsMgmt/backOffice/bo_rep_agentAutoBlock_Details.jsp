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
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>
			function getData() {
				var tblData = document.getElementById("tableDataDiv").innerHTML;
				tblData = tblData.replace(document.getElementById("sortRow").innerHTML, "");
				document.getElementById('tableValue').value = tblData;
				return false;
			}
		</script>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<div align="left">
					<s:form action="bo_rep_agentAutoBlock_ExportExcel" method="post" enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:submit value="Export As Excel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();" />
					</s:form>
				</div>
				<div id='tableDataDiv'>
					<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
						<tr>
							<th align="center">
								<s:text name="label.sNo" />
							</th>
							<th align="center">
								<s:text name="label.agt.name" />
							</th>
							<th align="center">
								<s:text name="label.blockAmount" />
							</th>
							<th align="center">
								<s:text name="label.blockDays" />
							</th>
							<th align="center">
								<s:text name="label.blockAction" />
							</th>
							<th align="center">
								<s:text name="label.close.bal" /> (<s:property value="#application.CURRENCY_SYMBOL" />)
							</th>
							<th align="center">
								<s:text name="label.num.days" />
							</th>
							<th align="center">
								<s:text name="label.cur.stat" />
							</th>
							<th align="center">
								<s:text name="label.status.eligibleForBlock" />
							</th>
							<th align="center">
								<s:text name="label.status.changeAction" />
							</th>
						</tr>
						<s:iterator value="autoBlockMap" status="status">
							<tr class="startSortable">
								<td>
									<s:property value="%{#status.index+1}" />
								</td>
								<td>
									<s:property value="value.orgName" />
								</td>
								<td>
									<s:property value="value.blockAmount" />
								</td>
								<td>
									<s:property value="value.blockDays" />
								</td>
								<td>
									<s:if test="%{value.blockAction eq 'NO_ACTION'}">
										<s:text name="label.status.noAction" />
									</s:if>
									<s:elseif test="%{value.blockAction eq 'SALE_BLOCK'}">
										<s:text name="label.status.saleBlock" />
									</s:elseif>
									<s:elseif test="%{value.blockAction eq 'LOGIN_BLOCK'}">
										<s:text name="label.status.LoginBlock" />
									</s:elseif>
								</td>
								<td>
									<s:set name="closingBalance" value="%{value.firstClosingBalance}" />
									<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("closingBalance"))%>
								</td>
								<td>
									<s:if test="%{value.eligibleBlock == true}">
										<s:if test="%{value.negitiveFromDays >= #application.MAXIMUM_AUTOBLOCK_DAYS}">
											<s:text name="label.more.then" />
										</s:if>
										<s:property value="value.negitiveFromDays" />
									</s:if>
									<s:else>
										<s:property value="value.negitiveFromDays" />
									</s:else>
								</td>
								<td>
									<span id='currentStatus_<s:property value="value.orgId" />'>
										<s:property value="value.orgStatus" />
									</span>
								</td>
								<td>
									<s:if test="%{value.eligibleBlock == true}">
										YES
									</s:if>
									<s:else>
										NO
									</s:else>
								</td>
								<td>
									<s:if test="%{value.eligibleBlock == true or value.eligibleActive == true}">
										<s:if test="%{value.orgStatus eq 'ACTIVE'}">
											<input type="button" id='updateStatus_<s:property value="value.orgId" />' value="INACTIVE" onclick="updateOrgStatus(<s:property value="value.orgId" />);" />
										</s:if>
										<s:elseif test="%{value.orgStatus eq 'INACTIVE'}">
											<input type="button" id='updateStatus_<s:property value="value.orgId" />' value="ACTIVE" onclick='updateOrgStatus(<s:property value="value.orgId" />);' />
										</s:elseif>
										<s:hidden id="currentStatusHide_%{value.orgId}" value="%{value.orgStatus}" />
									</s:if>
								</td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_agentAutoBlock_Details.jsp,v $ $Revision: 1.3 $
</code>