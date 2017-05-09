	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
	<head>

	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	
	<s:head theme="ajax" debug="false"/>
		</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">

  <div id="top_form">
   <h3> Cheque Search Result </h3>  
   
			 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr>
			   <td colspan="5" align="center"><b>Search Results</b></td>
			   </tr>
			   
			<s:if test="#session.CHEQUE_SEARCH_RESULTS==null">
				<tr><td colspan="5" align="center">Cheque No:<s:property value="%{chequeNumber}" /> not found</td>
				</tr>
			</s:if>
   			<s:else>
   			<tr>
			   	   
			     <th>Cheque Number</th>
			     <th>Cheque Date</th>
			     <th>Issuing Party</th>
			      <th>Bank</th>
			       <th>Organization Name</th>
			     </tr>
			<s:iterator id="SearchGameResults1" value="#session.CHEQUE_SEARCH_RESULTS">
			    <tr>
			      <td><s:a id="link2" href="bo_act_chqBounce_Detail.action?chequeNumber=%{chequeNumber}&transactionId=%{transactionId}"><s:property id="gn" value="chequeNumber"/></s:a></td>
			     <td> <s:property value="chequeDate"/></td>
			      <td> <s:property value="issuePartyname"/></td>
			      <td> <s:property value="bankName"/></td>
			      <td> <s:property value="orgName"/></td>
				 </tr>
			  </s:iterator> 
			</s:else>
			 </table> 
			</div></div> 	
			
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_chqBounce_SearchResult.jsp,v $
$Revision: 1.2 $
</code>