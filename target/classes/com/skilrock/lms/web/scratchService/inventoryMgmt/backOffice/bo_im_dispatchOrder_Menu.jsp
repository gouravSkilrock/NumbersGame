
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Dispatch Order
				</h3>
				<s:form action="bo_im_dispatchOrder_Search">
					<table cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td><s:textfield label="Game Name" name="gameName"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Game Number" name="gameNumber"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="%{#application.TIER_MAP.AGENT} Organization Name" name="agtOrgName"></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Order Id" name="orderNumber" value=""></s:textfield></td>
						</tr>
						<tr>
							<td><s:textfield label="Delivery Challan Id" name="challanId" value=""></s:textfield></td>
						</tr>
						<tr>
							<td height="20" valign="bottom" align="right">
								<s:submit cssClass="button" theme="ajax" targets="bottom" value="Search" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="bottom"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_dispatchOrder_Menu.jsp,v $ $Revision: 1.3 $
</code>