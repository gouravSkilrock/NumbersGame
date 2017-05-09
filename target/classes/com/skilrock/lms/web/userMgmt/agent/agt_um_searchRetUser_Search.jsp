<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head theme="ajax" debug="false"/>
</head>
<body>

<div id="bottom_table">
	<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr>
			   <td colspan="9" align="center"><b>Search Results</b></td>
			   </tr>
			
			   <s:if test=" #session.USER_SEARCH_RESULTS.size !=0 ">
			      <tr>
			  
			     <th align="center" width="92">User Id</th>
			     <th align="center" width="140">User Name</th>
			     <th align="center" width="220">With Organization</th>
			     <th align="center" width="110">Status</th>
			      <!-- <th width="124">Role Name</th> -->  
			     <!--<th align="center" width="206">Registration Date</th>
			   
			    
			   --></tr>
			  <s:iterator id="SearchUserResults1" value="#session.USER_SEARCH_RESULTS">
			    <s:url id="123">
			    <param name="ff" value="=%{userId}">
			    </s:url>
			    <tr>
			      <td align="center"><s:a id="link2" targets="top" href="agt_um_searchRetUser_Detail.action?userId=%{userId}"><s:property id="gn" value="userId"/></s:a></td>
			      <td align="center"> <s:property value="userName"/></td>
			      <td align="center"><s:property value="userOrgName"/></td>
			      <td align="center"> <s:property value="userStatus"/></td>
			        <!-- <td> <s:property value="userRoleName"/></td> -->
			            <!--<td align="center"> <s:property value="registerDate"/></td>-->
			            
			      
		        </tr>
			  </s:iterator>
			  	</s:if>			 
				<s:else>
				<tr>
			   		<td colspan="9" align="center">No records to Process</td>
			   </tr>
				</s:else>  
   			</table>
   </div>
			 
	 <s:div id="naviga" cssStyle="text-align: center">
	<s:if test=" #session.USER_SEARCH_RESULTS1.size >5 ">
     
	<s:if test="#session.startValueUserSearch!=0"><s:a theme="ajax" targets="bottom" href="agt_um_searchRetUser_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="bottom" href="agt_um_searchRetUser_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueUserSearch==((#session.USER_SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="bottom" href="agt_um_searchRetUser_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="bottom" href="agt_um_searchRetUser_Navigate.action?end=last">Last</s:a></s:else>      
    
	</s:if>
	</s:div>
 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_searchRetUser_Search.jsp,v $
$Revision: 1.3 $
</code>