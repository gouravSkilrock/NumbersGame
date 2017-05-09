<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body><br>
<s:form action="/com/skilrock/lms/rolemgmt/createnrole.action" method="POST">


<h2 align="center" >select roles </h2>
<input type="checkbox" name="arr" value="user_creation">user creation</checkbox>
<input type="checkbox" name="arr" value="edit_ user_info">Edit user info</checkbox>


<s:submit value="ok" align="center"/>

</s:form>
<body>
</html><code id="headId" style="visibility: hidden">$Id: role.jsp,v 1.1 2010/04/01 04:23:07 gaurav Exp $</code>