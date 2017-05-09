<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<div id="allowedBooks1"></div>
				<br />
				<div id="right">
					<table width="450" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center">
						<tr>
							<th align="center" colspan="3">
								Search Result
							</th>
						</tr>
						<s:if test="#session.RequestList.size()!=0">
							<tr>
								<th>
									Order No
								</th>
								<th>
									<s:property value="#application.TIER_MAP.AGENT" />
									Name
								</th>
								<th>
									Date
								</th>
							</tr>
							<s:iterator id="SearchOrderResults" value="#session.RequestList">
								<tr>
									<s:url id="es" action="bo_om_processOrdReq_Detail.action" encode="true">
										<s:param name="name" value="%{name}" />
										<s:param name="orderId" value="%{orderId}" />
									</s:url>
									<td>
										<s:a id="link2" theme="ajax" href="%{es}" targets="right">
											<s:property value="orderId" />
										</s:a>
									</td>
									<td>
										<s:property value="name" />
									</td>
									<td>
										<s:property value="date" />
									</td>
								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="3" align="center">
									No Order to Process
								</td>
							</tr>
						</s:else>
					</table>
								<s:div id="naviga">
				<s:if test=" #session.RequestList1.size >5 ">
					<s:if test="#session.startValueRequestSearch!=0">
						<s:a theme="ajax" targets="bottom"
							href="bo_om_processOrdReq_Navigate.action?end=first">First</s:a>
						<s:a theme="ajax" targets="bottom"
							href="bo_om_processOrdReq_Navigate.action?end=Previous"> Previous</s:a>
					</s:if>
					<s:else>First Previous</s:else>
					<s:if
						test="#session.startValueRequestSearch==((#session.RequestList1.size/5)*5)">Next Last</s:if>
					<s:else>
						<s:a theme="ajax" targets="bottom"
							href="bo_om_processOrdReq_Navigate.action?end=Next">Next</s:a>
						<s:a theme="ajax" targets="bottom"
							href="bo_om_processOrdReq_Navigate.action?end=last">Last</s:a>
					</s:else>
				</s:if>
			</s:div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_om_processOrdReq_Search.jsp,v $ $Revision: 1.3 $
</code>