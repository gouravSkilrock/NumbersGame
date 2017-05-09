<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<meta http-equiv="X-UA-Compatible" content="IE=7; IE=9" />
</head>
<style type="text/css"> 
   .table 
{ 
  border-top: solid 1px black; 
  border-bottom: solid 1px black; 
  border-left: solid 1px black; 
  border-right: solid 1px black;
   
}
#topFrame{}
</style>

<frameset framespacing="0" border="0" frameborder="no" rows="60,*"> 
  <frame name="top" id="topFrame" scrolling="no" noresize src="<%=request.getContextPath() %>/com/skilrock/lms/web/loginMgmt/Header2.jsp">
  <frameset cols="100%">
    <frame name="leftbottom" src="#" scrolling="yes" noresize="noresize">
   
     </frame>
    </frameset>
</frameset>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: frame2.jsp,v $
$Revision: 1.3 $
</code>