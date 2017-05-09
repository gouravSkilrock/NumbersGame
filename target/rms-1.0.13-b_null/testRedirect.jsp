<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="org.apache.struts2.ServletActionContext" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
System.out.println(application.getAttribute("WRAPPER_LOGOUT"));
String wrapperURL = (String) application.getAttribute("WRAPPER_LOGOUT") ;

 %>
<script>

function redirectPage(){
var aaa = '<%= wrapperURL %>';
window.top.location.href = aaa ;
}

</script>

</head>
<body onload="redirectPage()">
</body>
</html>