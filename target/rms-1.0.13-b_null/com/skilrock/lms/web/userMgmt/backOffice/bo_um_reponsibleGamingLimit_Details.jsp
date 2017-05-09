<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%> <%!String isOffline = null;%>

		</title>
		<s:head theme="ajax" debug="false" />

	</head>

	<body>

		<div id="wrap">

			<div id="top_form">

				<table border="0" cellpadding="2" cellspacing="0" width="100%">
					<tr>
						<th>
							<s:text name="label.crtria" />
						</th>
						<th>
							<s:text name="label.crtria.limit" />
						</th>
						<th>
							<s:text name="label.crtria.action" />
						</th>
						<th>
							<s:text name="label.org.type" />
						</th>
						<th>
							<s:text name="label.status" />
						</th>
					</tr>
					<s:iterator value="%{#session.criteriaList}">
						<tr>
							<td>
								<s:hidden name="criteriaId" value="%{criteriaId}"></s:hidden>
								<s:property value="%{criteria}"></s:property>
							</td>
							<td>
								<s:textfield name="criLimit" theme="simple" value="%{criLimit}"></s:textfield>
							</td>
							<td>
								<s:select name="criAction" cssClass="option" theme="simple"
									list="%{#session.criActionList}"
									headerValue="--Please Select--" headerKey="-1"
									value="%{criAction}"></s:select>
							</td>
							<!-- <td><s:select name="orgType" cssClass="option" theme="simple" list="%{#session.orgList}" headerValue="--Please Select--" headerKey="-1" value="%{orgType}" ></s:select></td> -->
							<td>
								<s:hidden theme="simple" name="orgType" value="orgType" />
								<s:textfield theme="simple"
									value="%{#application.TIER_MAP[orgType].toUpperCase()}"
									readonly="true"></s:textfield>
							</td>
							<td>
								<s:select name="status" cssClass="option" theme="simple"
									list="%{#session.criStatusList}"
									headerValue="--Please Select--" headerKey="-1"
									value="%{status}"></s:select>
							</td>
						</tr>
					</s:iterator>
					<tr>
						<td colspan="5" width="80%">
							<s:submit key="label.update.limit" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_reponsibleGamingLimit_Details.jsp,v $ $Revision:
	1.1.2.1.2.1.4.6 $
</code>