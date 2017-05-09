
<%@page import="java.io.File"%>
<%@page import="com.skilrock.lms.common.utility.LMSUtility"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.skilrock.lms.common.utility.MD5Encoder"%>
<%@page import="com.skilrock.lms.common.LSControllerImpl"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>LS Validation</title>
	</head>
	<body>
		<%
			String finalCheckSumEncoded = LSControllerImpl.getInstance().getFinalChecksum();
			String systemId = LSControllerImpl.getInstance().calculateSystemId();
		%>
		<table width="50%">
			<tr>
				<td>Checksum Calculated</td><td><%= finalCheckSumEncoded %></td>
			</tr>
			<tr>
				<td>System ID</td><td><%= systemId %></td>
			</tr>
		</table>
	</body>
</html>