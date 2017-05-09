<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript">
		</script>
		<style type="text/css">
			form {
				  overflow: auto;
			}
		</style>
	</head>
	<body>
		<s:if test="%{agentDetailList != null and agentDetailList.size>0}">
			<table id="dataTable" width="100%" border="1" cellpadding="3"
				cellspacing="0" bordercolor="#CCCCCC">
				<thead>
					<tr>
						<th align="center">
							S.No
						</th>
						<th align="center">
							Select All
						</th>
						<th align="center">
							Agent Name
						</th>
						<th align="center">
							Invoicing Method
						</th>
						<th align="center">
							Method Type
						</th>
						<th align="center">
							Method Value
						</th>
					</tr>
					<tr>
						<th align="center">
							&nbsp;
						</th>
						<th align="center">
							<input type="checkbox"
								onclick="selectAllCB(this, 'selAllOrg');" />
						</th>
						<th align="center">
							&nbsp;
						</th>
						<th align="center">
								
						<s:select id="mainIMN" list="invoiceMap" listKey="key" listValue="value" onchange="selectAllSB(this.id, this, 'methodName'); changeMethodTypeBox(this.value, '0'); changeMethodValueBoxAll(this.value);" theme="simple" cssClass="option"/>
								
						</th> 	
						<th align="center">
							<div id='td_type_0'>
							</div>
						</th>
						<th align="center">
							<s:select id="mainSEL" list="#{'YES':'Yes', 'NO':'No'}"
								onchange="selectAllTB(this, 'methodValue'); validateBoolValues(this.value);"
								theme="simple" cssClass="option" />
							<input type="text" id="mainTXT"
								onchange="selectAllTB(this, 'methodValue');"
								onkeydown="validateTextValues(event);" style="display: none;"
								onpaste="return false;" size="4" maxlength="10" />
						</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="agentDetailList" status="status">
						<tr>
							<td>
								<s:property value="%{#status.index+1}" />
							</td>
							<td align="center">
								<input type="checkbox" class="selAllOrg" />
							</td>
							<td>
								<s:hidden cssClass="orgId" name="" value="%{orgId}" />
								<s:hidden cssClass="methodId" name="" value="%{methodId}" />
								<s:property value="orgName" />
							</td>
							
							<td align="center">
								
								<s:select name="methodName" list="invoiceMap" cssClass="methodName option" listKey="key" listValue="value" onchange='changeMethodTypeBox(this.value, %{orgId}); changeMethodValueBox(this.value, %{orgId}); selectCB(this);' value="%{methodId}" theme="simple" />
							</td>
							<td>
								<div id='td_type_<s:property value="orgId" />'>
									<s:property value="methodType" />
								</div>
							</td>
							<td align="center">
								<div id='td_sel_<s:property value="orgId" />'
									class="boolInput"
									style='<s:if test="%{methodType=='BOOLEAN'}">display:block;</s:if><s:else>display:none;</s:else>'>
									<s:select name="methodValue" value="%{methodValue}"
										onchange="selectCB(this);" list="#{'YES':'Yes', 'NO':'No'}"
										theme="simple" cssClass="boolInputValue option" />
								</div>
								<div id='td_txt_<s:property value="orgId" />'
									class="textInput"
									style='<s:if test="%{methodType!='BOOLEAN'}">display:block;</s:if><s:else>display:none;</s:else>'>
									<s:textfield name="methodValue" value="%{methodValue}"
										onclick="selectCB(this);"
										cssClass="methodValue textInputValue" theme="simple"
										size="4" maxlength="10" />
								</div>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
				<br />
				<table>
					<tr>
						<td>
							<input type="button" value="Update" onclick="updateValues();" />
						</td>
					</tr>
				</table>
		</s:if>
		<s:else>
			<h3>
				<center>No Record to Process</center>
			</h3>
		</s:else>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_updateInvoicingMethod_Search.jsp,v $ $Revision: 1.3 $
</code>