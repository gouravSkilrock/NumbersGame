<%@ taglib prefix="s" uri="/struts-tags"%>
<html>

<script language="javascript" type="text/javascript" >

function openWin(){

window.open("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
}
</script>
   <body onload="openWin()">
   </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: loggedIn.jsp,v $
$Revision: 1.2 $
</code>