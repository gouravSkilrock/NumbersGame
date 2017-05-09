<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<form name="afterLog">
Please Wait.................
<input type="hidden" name="data" value="<%=session.getId()%>" />
<%response.sendRedirect(application.getAttribute("HOST")+"://"+request.getServerName()+application.getAttribute("PORT")+request.getContextPath()+"/com/skilrock/lms/web/loginMgmt/adminLoginAuth.action?data="+session.getId()+""); %>
</form>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: adminLogin.jsp,v $
$Revision: 1.2 $
</code>