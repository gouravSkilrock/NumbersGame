<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.List" %>
<%response.setDateHeader("Expires",1000);%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
</head>
<body>
 
 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   <tr>
			   <td colspan="6" align="center"><b>Search Results</b></td>
			   
			   <td>
			   <%int resLength=0;
			   if(session.getAttribute("SEARCH_RESULTS1")!=null){
			   
			   resLength =((List)session.getAttribute("SEARCH_RESULTS1")).size();  
			   
			   }%>
			   
			   <select  id="resultList" name="resultList" class="option" onchange="searchPwtResult('bo_pwt_bulkFinalRcpt_Navigate.action')">
			   <%int i=0;while(i<resLength){
			  	 if(i+5<resLength){
			  	 %>
			  	 <option value="<%=i+1%> to <%=i+5%>"><%=i+1 %> to <%=i+5 %>
			  	 <%
			  	 }else{
			  	 %>
			  		<option value="<%=i+1%> to <%=resLength%>"><%=i+1 %> to <%=resLength %>
			   <%
			   }
			   i=i+5; 
			   }
			   %>
			   </select></td>
			   </tr>
			  
			   <s:if test="%{searchResultsAvailable == 'Yes'}">
			    <tr>
			     <th>Receipt Id</th>
			     <th><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
			     <th>Received Date</th>
			     <th>Tickets Received</th>
			       <th>Verified VIRN</th>
			       <th>Verified Ticket No.</th>
			     <th>Status</th>
			       
			   </tr>
			  <s:iterator id="SearchPwtResults" value="#session.SEARCH_RESULTS">
			    <tr>
			      <td><s:a href='#' onclick="searchPwtResult('bo_pwt_bulkFinalRcpt_Detail.action?receiptNum=%{#SearchPwtResults.receiptId}&status=%{#SearchPwtResults.status}&agtOrgName=%{(#SearchPwtResults.agtOrgName)}')"><s:property value="receiptId"/></s:a></td>
			      <td> <s:property value="agtOrgName"/></td>
			      <td><s:property value="recievedDate"/></td>
			      <td> <s:property value="tickReceived"/></td>
			      <td> <s:property value="verifiedVIRN"/></td>
			       <td> <s:property value="verifiedTickNum"/></td>
			       <td> <s:property value="status"/></td>
			            
			      
			     </tr>
			  </s:iterator> 
			  </s:if>			 
			<s:else>
				<tr>
			   			<td colspan="7" align="center">No Records to Process</td>
			   	</tr>
			</s:else>
		</table> 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkFinalRcpt_SearchResult.jsp,v $
$Revision: 1.2 $
</code>