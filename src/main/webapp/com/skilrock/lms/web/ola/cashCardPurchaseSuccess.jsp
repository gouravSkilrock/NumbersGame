<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.beans.CashCardPurchaseDataBean"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/common.js"></script>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>



	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		

		
	<h1>
			You have Successfully Purchased Following Cards 
		</h1>
<br></br>
		<s:if test="#session.cashCardList.size>0">
			<s:set name="cashCardBean" id="cashCardBean"
				value="#session.cashCardList" />
			<s:iterator id="beanCart" value="%{#cashCardBean}">
				<table align="center" border="2" width="400px">
					<tr >
					<td align="left" nowrap="nowrap">
							<h2>
							Serial Number :
								<s:property value="serialNumber" />
							</h2>

						</td>
						<td align="left" nowrap="nowrap">
							<h2>
								Pin ID :
								<s:property value="pinNbr" />
							</h2>

						</td>

						<td>

							<h2>
								Amount :
								<s:property value="amount" />
							</h2>
						


						</td>


					</tr>
				</table>
			</s:iterator>

		</s:if>
		<s:else>
<H3>No Cash Cards Purchased</H3>
</s:else>
		


	</body>
	<script>
	updBalIframe();
</script>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: cashCardPurchaseSuccess.jsp,v $ $Revision: 1.2 $
</code>