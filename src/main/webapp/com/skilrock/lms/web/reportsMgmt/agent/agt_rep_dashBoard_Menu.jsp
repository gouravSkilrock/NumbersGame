<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/dashBoard.js" />
		<script></script>

	</head>
	<body onload="fetchActData('<s:property value="#session.USER_INFO.userOrgId"/>')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.AGENT" /> <s:text name="Dashboard"/>
				</h3>


				<s:form name="dashBoard" id="dashBoard" method="POST">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">
						<tr>
							<td align="right">
								<i><s:property value="#application.TIER_MAP.AGENT" /> Organization :</i>
							</td>
							<td nowrap="nowrap" colspan="2"><b>
								<s:property value="#session.USER_INFO.orgCode" /></b>
								<div style="display: none;">
									<s:select theme="simple" id="agtOrgName" cssClass="option"
										list="{}" name="organization_Name" emptyOption="false"
										headerKey="%{#session.USER_INFO.userOrgId}"
										headerValue="%{#session.USER_INFO.orgCode}" />
								</div>
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
					</table>

					<div id="completeDiv"
						style="position: absolute; background-color: #CCCCCC; opacity: 1; filter: alpha(opacity =       60); display: none; width: 830px">
						<center> <b><font size="3">Please Wait Data is
							Being Processed......</font> </b> </center>
					</div>

					<div id="resultDiv" style="display: none">
					</div>
					<div id="reportDiv">
					</div>
				</s:form>
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_dashBoard_Menu.jsp,v $
$Revision: 1.2 $
</code>