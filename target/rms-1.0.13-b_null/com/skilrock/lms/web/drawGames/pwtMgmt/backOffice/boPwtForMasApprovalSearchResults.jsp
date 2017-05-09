<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<!--<s:head theme="ajax" debug="false"/>	
--></head>
  
<body>
      
      <table border="1" cellpadding="4" cellspacing="0" width="100%" align="center" bordercolor="#cccccc">
        <tr style="background: #eee">
          <th><s:text name="label.rqst.id"/></th>
          <th><s:text name="label.req.by"/></th>
          <th><s:text name="label.status"/></th>
          <th><s:text name="label.name"/></th>
           <th><s:text name="label.remarks"/></th>
        </tr>
	    <s:if test="%{pwtReqDetailsList.size()>0}">
	    <s:iterator value="%{pwtReqDetailsList}">
	       <tr>
	          <td><s:a href="draw_showPendingReqDetailsForMasApprovalBO.action?taskId=%{task_id}&partyType=%{partyType}" > <s:property value="%{request_id}"/> </s:a></td>
	          <td><s:property value="%{requestedByOrgName}"/></td>
	          <td><s:property value="%{req_status}"/></td>
	          <td><s:property value="%{partyName}"/></td>
	          <td><s:property value="%{remarks}"/></td>
	       </tr>
	    </s:iterator>
	    </s:if>
	    <s:else>
	     <tr><td colspan="5" align="center"><s:text name="msg.no.record"/></td></tr>
	    </s:else>
     </table>
    
</body>



</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: boPwtForMasApprovalSearchResults.jsp,v $
$Revision: 1.3 $
</code>