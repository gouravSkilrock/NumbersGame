<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body><br><br>

<s:form action="/com/skilrock/igs/rolemgmt/assignRoletoGenUserinDB.action" method="post" >

<h3>Assign  Priviledges to General Users </h3>

 
 User Name<SELECT name="userName" class="option" onchange="alert(this.value)">
  <s:iterator value="headsUserList">
 <option><s:property value="%{headsUserList.get(0)}"/>
 </s:iterator>
  </SELECT>
 
 
 
  <table>
  <s:iterator id="ss"  value="headsPrivList">
  <tr>
  <td>
  <input type=checkbox value="<s:property value="pid"/>"  name="rolePriv"" /> 
  </td>
  
  
  <td>
  <s:property value="privTitle" />
  </td>
  </tr>
  </s:iterator>
</table>

<s:submit value="ok" cssClass="button" align="center"/>
</s:form>

<body>
</html><code id="headId" style="visibility: hidden">$Id: assignRole.jsp,v 1.2 2010/04/12 11:05:30 rakesh Exp $</code>