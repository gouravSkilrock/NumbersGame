<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body><br><br>

<s:form action="/com/skilrock/igs/rolemgmt/createRoleHeadDB.action" method="post" >

<h3>Create Role Head </h3>

 
 User Name<SELECT name="userName"  onchange="alert(this.value)">
  <s:iterator value="userList">
 <option><s:property value="%{userName}"/>
 </s:iterator>
  </SELECT>

<br>
Select Role<SELECT name="roleId" onchange="alert(this.value)">
  <s:iterator value="roleList">
 <option value="<s:property value="%{roleId}"/>"/><s:property value="%{roleName}"/>
 </s:iterator>
  </SELECT>


<s:submit cssClass="button" value="ok" align="center"/>
</s:form>

<body>
</html><code id="headId" style="visibility: hidden">$Id: createRoleHead.jsp,v 1.1 2010/04/01 04:23:07 gaurav Exp $</code>