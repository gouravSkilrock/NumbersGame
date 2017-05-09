<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<s:head theme="ajax" debug="false"/>	
</head>
  
<body>
      
      <table border="1" cellpadding="2" cellspacing="0" width="100%" bordercolor="#cccccc" align="center">
        <tr style="background: #eee">
          <th>Request Id</th>
          <th>Requested By</th>
          <th>Status</th>
          <th>Name</th>
           <th>Remarks</th>
        </tr>
	    <s:if test="%{pwtPayDetailsList.size()>0}">
	    <s:iterator value="%{pwtPayDetailsList}">
	    
	       <tr>
	          <td><s:a href="showPendingPwtDetails.action?taskId=%{task_id}&partyType=%{partyType}"><s:property value="%{request_id}"/></s:a></td>
	          <td><s:property value="%{retailer_name}"/></td>
	          <td><s:property value="%{req_status}"/></td>
	          <td><s:property value="%{partyName}"/></td>
	           <td><s:property value="%{remarks}"/></td>
	       </tr>
	    </s:iterator>
	    </s:if>
	    <s:else>
	     <tr><td colspan="5" align="center">No Records Found</td></tr>
	    </s:else>
     </table>
    
</body>



</HTML><code id="headId" style="visibility: hidden">$Id: agentPwtPaySearchResult.jsp,v 1.1 2010/04/02 12:12:09 gaurav Exp $</code>