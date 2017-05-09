<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=UTF-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
			<script type="text/javascript"
			src="<%=request.getContextPath() %>/js/jquery-1.10.2.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/jquery.i18n.properties-min-1.0.9.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/i18nLoader.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/activityRep.js" />
		<script></script>

	</head>
	<body onload="getAgentList()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.sold.book"/> <s:text name="Report"/>
				</h3>


				<s:form name="retActivity" id="retActivity" method="POST">

					<table border="0" width="100%" cellpadding="2" cellspacing="0">
						<tr>
							<td>
								<s:text name="label.select"/> <s:property value="#application.TIER_MAP.AGENT" /> <s:text name="label.org"/> :
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="agtOrgName" cssClass="option"
									list="{}" name="organization_Name" emptyOption="false"
									headerKey="-1" headerValue="%{getText('label.please.select')}"
									onchange="getRetList()" />
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="label.select"/> <s:property value="#application.TIER_MAP.RETAILER" /> <s:text name="label.org"/> :
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="retOrgName" cssClass="option"
									list="{}" name="retOrgName" emptyOption="false"
									headerKey="-1" headerValue="%{getText('label.please.select')}" />
								
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="label.slct.book.type"/> :
							</td>
							<td nowrap="nowrap" colspan="2">

								<s:select theme="simple" id="bookType" cssClass="option"
									list="#{' ':getText('label.complete.sold'),'!':getText('label.remaining')}" emptyOption="false"
									headerKey="-1" headerValue="%{getText('label.please.select')}" />
								
							</td>
						</tr>
						<tr>
							<td>
								&nbsp
							</td>
							<td nowrap="nowrap" colspan="2">

								<input type="button" value="<s:text name="btn.submit"/>" onclick="fetchSoldEntryData()"/>
							</td>
						</tr>
					</table>
				</s:form>
				<div id="resultDiv">
				</div>
				
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_soldBookEntry_Menu.jsp,v $
$Revision: 1.3 $
</code>