
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
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
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/commercialService/playMgmt/js/csMgmt.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js"></script>
		<s:head theme="ajax" debug="false" />



	</head>
	<body>

		<div id="wrap">
			<div id="top_form">
				<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
				
				<h3>
					CS Sale Result
				</h3>
				<%
					StringBuffer codebaseBuffer = new StringBuffer();
					codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
					codebaseBuffer.append(request.getServerName());
					if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
						codebaseBuffer.append(':');
						codebaseBuffer.append(request.getServerPort());
					}
					codebaseBuffer.append(request.getContextPath());
					codebaseBuffer.append('/');
				%>
				<div id="csSaleAppDiv" style="display: none">

				<%
					if ("SIGNED".equalsIgnoreCase((String) application
							.getAttribute("APPLET_SIGNED"))) {
				%>
				<applet code="TicketApplet"
					codebase="<%=codebaseBuffer.toString()%>"
					jnlp_href="applets/App.jnlp" width="200" height="500"
					name="TicketApp" mayscript>
				<param name="data" value="108172000002746000" />
				<div style="font-size: 12px; height: 300px; line-height: center;">
					<table>
						<tr>
							<td height="300px;" align="center">
								No Java Runtime Environment v 1.5.2 found!!
								<br />
								<a style="color: red"
									href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/jre-1_5_0_12-windows-i586-p.exe">Click
									to install</a>
							</td>
						</tr>
					</table>
				</div>
				</applet>
				<%
					} else if ("UNSIGNED".equalsIgnoreCase((String) application
							.getAttribute("APPLET_SIGNED"))) {
				%>
				<applet codebase="<%=request.getContextPath()%>/java1.5/"
					code="TicketApplet.class" width="200" height="500" name="TicketApp"
					mayscript>
				<param name="data" value="108172000002746000" />
				</applet>
				<%
					}
				%>
				</div>
				<script>setAppData('<%=(String) session.getAttribute("FINAL_DATA")%>');</script>
				<script>updBalIframe();</script>
			</div>
		</div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: ret_cs_sale_Submit.jsp,v $ $Revision: 1.1.2.5 $
</code>
