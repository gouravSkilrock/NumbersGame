<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
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
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>
		<table width="500px" border="1" cellpadding="3" cellspacing="0" 
			bordercolor="#CCCCCC" align="center">
       <s:if test="#session.numberMap !=null && #session.numberMap.size()>0">
			<tr>
				<th colspan="3" align="center">
					<s:text name="label.nbr.wise.bet.amt" /> <s:text name="Report" /> 
				</th>
			</tr>

			<tr>
				<th>
					<s:text name="label.nbr" />
				</th>
				<th>
					<s:text name="label.amt.play.for" />
				</th>
				<th>
					<s:text name="label.nbr.panel.line" />
				</th>
			</tr>

			
				<s:iterator value="#session.numberMap" status="stat">

					<tr class="startSortable">
						<td>
							<s:property value="%{key}" />
						</td>
						<s:iterator value="%{value}">
						<td>
							<s:property />
						</td>
						</s:iterator>
					</tr>
				</s:iterator>

			</s:if>
			<s:else>
				<tr>
					<td align="center">
						<s:if test="%{message==null}">
				<s:text name="msg.no.record.process" />
				</s:if><s:else>
				<s:property value="message"/>
				</s:else>
					</td>
				</tr>
			</s:else>
		</table>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_numberBetAmount_Result.jsp,v $ $Revision: 1.3 $
</code>
