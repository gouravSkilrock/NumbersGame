<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					Search Offline <s:property value="#application.TIER_MAP.RETAILER" />'s User
				</h3>
				<s:form action="bo_um_searchOfflineRetUser_Search">

					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<tr>

						</tr>
						<tr>
							<s:textfield label="User Name" name="userName"></s:textfield>
						</tr>
						<tr>
							<td>
								<s:textfield label="Organization Name" name="compName"></s:textfield>
							</td>
							<td>
								<s:textfield label="Parent Organization Name"
									name="parentCompName"></s:textfield>
							</td>
						</tr>
						<tr>
							<s:select headerKey="1" headerValue="-- Please Select --"
								label="User Type" name="userType"
								list="{'ONLINE','OFFLINE'}" cssClass="option"></s:select>
						</tr>
						<tr>
							<s:select headerKey="1" headerValue="-- Please Select --"
								label="Offline Status" name="userStatus"
								list="{'ACTIVE','INACTIVE','BLOCK','TERMINATE'}" cssClass="option"></s:select>
						</tr>
						<tr>
							<td>
							</td>
							<td align="left">
								<table>
									<s:submit align="left" theme="ajax" targets="bottom"
										value="Search" cssClass="button" />
								</table>
							</td>
						</tr>
					</table>
				</s:form>
				<div id="bottom">
				</div>
			</div>

		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_searchOfflineRetUser_Menu.jsp,v $
$Revision: 1.3 $
</code>