<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
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
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">
				<s:if test="%{message!=null}">
					<h1>
						<b> <center> <s:property value="message" /> </center> </b>
					</h1>
				</s:if>
				<s:else>
					<div><s:if test="%{analist != null && analist.size > 0}">
						<s:form action="bo_rep_drawwise_win_tickets_details_export.action"
							method="post" enctype="multipart/form-data">
							<s:hidden id="tableValue1" name="valueToSend" />
							<s:submit value="Export As Excel"
								cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px"
								onclick="getTableData1();" />
						</s:form>
						</s:if>
					</div>
					<div id="tableDataDiv1">
						<s:if test="%{analist != null && analist.size > 0}">
							<table width="100%" border="1" cellpadding="3" cellspacing="0"
								bordercolor="#CCCCCC" align="center">
								<tr>
									<th colspan="6" align="center">
										<s:property value="%{drawName}" />
										<s:property value="%{start_date}" />
									</th>
								</tr>
								<tr>
									<th align="center">
										Retailer Name
									</th>
									<th align="center">
										Ticket Number
									</th>
									<th align="center">
										Ticket Status
									</th>
									<th align="center">
										Sale Date Time
									</th>
									<th align="center">
										Sale Amount
									</th>
									<th align="center">
										Winning Amount
									</th>
								</tr>
								<s:iterator id="beanCart" value="%{analist}">
									<tr class="startSortable">
										<td align="left">
											<s:property value="partyName" />
										</td>
										<td align="left">
											<s:property value="ticketNumber" />
										</td>
										<td align="left">
											<s:property value="ticketStatus" />
										</td>
										<td align="left">
											<s:property value="tktPurchaseTime" />
										</td>
										<td align="left">
											<s:property value="totalAmount" />
										</td>
										<td align="left">
											<s:property value="Winningamount" />
										</td>
									</tr>
								</s:iterator>
							</table>
						</s:if>
					</div>
					<s:else>
						<tr>
							<td colspan="8" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawwise_win_tickets_details.jsp,v $ $Revision:
	1.1.1.1.1.1 $
</code>