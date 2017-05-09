
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<s:head theme="ajax" />
</head>

<body>
			
		<table cellpadding="2" cellspacing="0" border="1"
							bordercolor="#cccccc"><%!int i = 0;%>
											
													<s:iterator value="%{interfaceList}">	
													<%
															i += 1;
																		
														%>																						
													<tr><td ><input type="checkbox" value="<s:property/>"
																	name="orgNameChk" id="orgName<s:property/>" onclick="setOrgName(this)"/>
													<s:property value="%{orgName}" /> </td><td id="orgName<s:property/>td"></td>
													<%
																if (i % 3 == 0) {
															%>
														</tr>

														<%
															}
														%>
														
													</s:iterator>
													
													</table>
	
	
	
	
	
</body>
</html>
<code id="headId" style="visibility: hidden">
	$Id: bo_rm_CreateServiceorgDetail.jsp,v 1.1 2010/04/12 04:41:04 gaurav Exp $
</code>