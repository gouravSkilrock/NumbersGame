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
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/bankMgmt/js/branch_userMapping.js"></script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Assign/Reassign Branch 
				</h3>
				<s:form name="mapBranch" id="mapBranch"
					action="bo_user_branch_mapping_Save" onsubmit="return validateInputs();">
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
							<td align="center" colspan="2">
								<div id="branchDiv" style="color: red; text-align: center"></div>
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="userDiv" style="color: red; text-align: center"></div>
							</td>
						</tr>
						
						<tr>
							<td align="right">
								<label class="label">
									Select User
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<s:select theme="simple" id="user" name="userId" headerKey="-1"
									headerValue="-- Please Select --" list="{}" cssClass="option"
									required="true" />
							</td>
						</tr>
						<tr>
							<td align="center" colspan="2">
								<div id="branchDiv" style="color: red; text-align: center"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<label class="label">
									Select Branch
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<s:select theme="simple" id="branch" name="branchId"
									headerKey="-1" headerValue="-- Please Select --" list="{}"
									cssClass="option" required="true" />
							</td>
						</tr>
						
						
						<tr>
							<td colspan="2">
								<s:submit name="submit" value="Assign" align="center"
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
	$RCSfile: bo_user_branch_mappingMenu.jsp,v $ $Revision: 1.2 $
</code>