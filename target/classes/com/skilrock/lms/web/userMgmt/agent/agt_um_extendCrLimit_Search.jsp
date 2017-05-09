<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
<body>
 
 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   <tr>
			   <td colspan="7" align="center"><b>Search Results</b></td>
			   </tr>
			  
			   <s:if test=" #session.ORG_SEARCH_RESULTS1.size !=0 ">
			    <tr>
			     <th align="center">Organization Name</th>
			     <!-- <th>Organization Type</th> -->
			     <th align="center">Organization Status</th>
			    <!--  <th>Parent Company</th> -->
			       <th align="center">City</th>
			     <th align="center">State </th>
			     <th align="center">Country</th>
			    
			   </tr>
			  <s:iterator id="SearchOrgResults" value="#session.ORG_SEARCH_RESULTS">
			    <tr>
			      <td align="center"><s:a id="link2" targets="top" href="agt_um_extendCrLimit_Detail.action?orgId=%{orgId}"> <s:property value="orgName"/></s:a> </td>
			      <!-- <td> <s:property id="gn" value="orgType"/></td> -->
			      <td align="center"> <s:property value="orgStatus"/></td>
			      <!-- <td><s:property value="parentOrgName"/></td> -->
			      <td align="center"> <s:property value="orgCity"/></td>
			      <td align="center"> <s:property value="orgState"/></td>
			       <td align="center"> <s:property value="orgCountry"/></td>
			            
			      
			     </tr>
			  </s:iterator> 
			  </s:if>			 
			<s:else>
				<tr>
			   			<td colspan="7" align="center">No Records to Process</td>
			   	</tr>
			</s:else>
		</table> 
	<s:div id="naviga" >
	<s:if test=" #session.ORG_SEARCH_RESULTS1.size >5 ">
     <table align="center">
	
	<s:if test="#session.startValueOrgSearch!=0"><s:a theme="ajax" targets="aa11" href="agt_um_extendCrLimit_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="aa11" href="agt_um_extendCrLimit_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrgSearch==((#session.ORG_SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="aa11" href="agt_um_extendCrLimit_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="aa11" href="agt_um_extendCrLimit_Navigate.action?end=last">Last</s:a></s:else>      
    
	
	</table></s:if>
	</s:div>  		 
			 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_extendCrLimit_Search.jsp,v $
$Revision: 1.2 $
</code>