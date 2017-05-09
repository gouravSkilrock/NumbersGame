<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>
<style type="text/css"> 
   .table 
{ 
  border-top: solid 1px black; 
  border-bottom: solid 1px black; 
  border-left: solid 1px black; 
  border-right: solid 1px black;
   
}</style>

<frameset framespacing="0" border="0" frameborder="no" rows="90,*"> 
  <frame name="top" id="topFrame" scrolling="no" noresize src="<%=request.getContextPath() %>/com/skilrock/lms/web/loginMgmt/Header.jsp">
  <frameset cols="100%">
    <frame name="leftbottom" src="#" scrolling="yes" noresize="noresize">
    
    </frameset>
</frameset>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: frame.jsp,v $
$Revision: 1.4 $
</code>