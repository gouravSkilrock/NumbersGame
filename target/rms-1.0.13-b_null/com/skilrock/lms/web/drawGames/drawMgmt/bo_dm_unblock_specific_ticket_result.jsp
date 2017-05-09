<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">

				<s:if test="%{message!=null}">
					<s:property value="message" />
				</s:if>
				<s:else>
					<s:if
						test="%{blockTicketUserBeanList != null && blockTicketUserBeanList.size > 0}">
						<table width="50%" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">

							<s:iterator id="beanCart" value="%{blockTicketUserBeanList}"
								status="taskIndex">
								<tr class="startSortable">
									<td>
										<s:property value="ticketNumber" />
									</td>
									<td>
										<s:property value="responseMessage" />
									</td>
								</tr>
							</s:iterator>
						</table>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:text name="msg.no.record.process"/>
							</td>
						</tr>
					</s:else>
				</s:else>
			</div>
		</div>



	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dm_unblock_specific_ticket_result.jsp,v $ $Revision:
	1.1.1.1.1.1 $
</code>
