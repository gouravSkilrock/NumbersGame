
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
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/commercialService/reportsMgmt/csMgmt.js"></script>

		<s:head theme="ajax" debug="false" />



	</head>
	<body>

		<div id="wrap">
			<div id="top_form">
				<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
				<s:if test="#session.IS_REPRINT == 'true'">
					<s:form action="reprintLastTxnId">
						<s:label value="Your Last Sale was not Printed. Please Reprint Last Sale First" />
						<s:submit value="Reprint"/>
					</s:form>		
				</s:if> 
				<s:else>
					<h3>
						The request could not be processed. Please try again.
					</h3>
				</s:else>
				<!--  
				<h2>
					<s:property value="#session.FINAL_DATA_ERROR"/>
				</h2>
				-->
			</div>
		</div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: ret_cs_sale_Error.jsp,v $ $Revision: 1.1.2.3 $
</code>
