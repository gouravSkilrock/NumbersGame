<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<s:if test="%{#session.PROD_REG_STAT == 'DUP_PROD_NUM'}">
				<h3><s:text name="label.prod.code"/>: <s:property value="productCode"/> <s:text name="label.with.denomination"/>: <s:property value="denomination"/> <s:text name="label.already.exist.pls.try.again"/></h3><br/>
			</s:if>
			<s:else>
				<h3><s:text name="label.your.prod.code"/>: <s:property value="productCode"/><s:text name="label.and.denomination"/>: <s:property value="denomination"/>   <s:text name="label.registered.successfully"/></h3><br/>
			</s:else>
			<a href="bo_cs_productRegistration.action"><s:text name="label.regi.more.prods"/></a>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_cs_productReg_success.jsp,v $
$Revision: 1.3 $
</code>
