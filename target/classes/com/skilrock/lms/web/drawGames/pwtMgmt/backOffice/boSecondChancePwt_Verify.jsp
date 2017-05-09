<%@ page contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<table border="1" cellpadding="4" cellspacing="0" width="100%"
			align="center" bordercolor="#cccccc">
			<tr style="background: #eee">
				<th>
					Ticket Number
				</th>
				<th>
					Verification Code
				</th>
				<th>
					Ticket Purchase Time
				</th>
				<th>
					Ticket Status
				</th>
				<th>
					Winning Amount
				</th>
				<th>
					Draw Time
				</th>
				<th>
					Draw Result
				</th>
				<th>
					Retailer Name
				</th>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</body>
</HTML>
<code id="headId" style="visibility: hidden">
	$RCSfile: boSecondChancePwt_Verify.jsp,v $ $Revision: 1.3 $
</code>