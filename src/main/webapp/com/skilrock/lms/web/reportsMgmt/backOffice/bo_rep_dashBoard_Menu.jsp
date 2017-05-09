<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>"</script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/dashBoard.js" />
		<script></script>

	</head>
	<body onload="getAgentList()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="Dashboard" />
					<s:text name="PA" />
				</h3>


				<s:form name="dashBoard" id="dashBoard" method="POST">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">
						<tr>
							<td align="right">
								<i><s:text name="label.select" /> <s:property
										value="#application.TIER_MAP.AGENT" /> <s:text
										name="label.org" /> :</i>
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="agtOrgName" cssClass="option"
									list="{}" name="organization_Name" emptyOption="false"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									onchange="fetchActData(this.value)" />
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
					</table>

					<div id="completeDiv"
						style="position: absolute; background-color: #CCCCCC; opacity: 1; filter: alpha(opacity =       60); display: none; width: 830px">
						<center> <b><font size="3"> <s:text
								name="msg.wait.data.process" />......</font> </b> </center>
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
	$RCSfile: bo_rep_dashBoard_Menu.jsp,v $ $Revision: 1.3 $
</code>