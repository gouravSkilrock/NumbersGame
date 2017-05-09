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
      
      <table border="1" cellpadding="2" cellspacing="0" width="400px">
        <tr style="background: #eee">
          <th>Request Id</th>
          <th>Status</th>
          <th>First Name</th>
        </tr>
	    <s:iterator value="%{pwtReqDetailsList}">
	       <tr>
	          <td><s:a theme="ajax" targets="serchResults" onclick="showDetails()"><s:property value="%{request_id}"/></s:a></td>
	          <td><s:property value="%{req_status}"/></td>
	          <td><s:property value="%{first_name}"/></td>
	       </tr>
	    </s:iterator>
     </table>
    
</body>



</HTML><code id="headId" style="visibility: hidden">$Id: retailerPwtForApproveDetails.jsp,v 1.1 2010/04/02 12:12:09 gaurav Exp $</code>