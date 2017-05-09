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
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">

  <div id="top_form">
 
   
  <h3>Start Games </h3>
 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
 				
			   <tr><th align="center">Search Result</th></tr>
			    
			<s:if test="#session.NEW_GAME_START_LIST==null || #session.NEW_GAME_START_LIST.size()==0">
				<tr><td colspan="4" align="center"> No Game to Start</td>
				</tr>
			</s:if>
   			<s:else>
   			  <tr>
			     <th>Game Name</th>
			     <th>Game Number</th>
			     <th>Start Date</th>
			     <th> Start</th>			     
			     
			    
			   </tr>
			  <s:iterator id="games" value="#session.NEW_GAME_START_LIST">
			    <tr>
			      
			      <td> <s:property  id="task"  value="%{gameName}"/></td>
			      <td> <s:property value="gameNo"/></td>
			      <td> <s:property value="startDate"/></td>
			      <td> <s:a href="com/skilrock/lms/admin/bo_gm_startSale_Save.action?gameId=%{gameId}">Start</s:a></td>
			     </tr>
			  </s:iterator> 
			</s:else>
			 </table>
			 
	<s:div id="naviga" >
	    <s:if test=" #session.NEW_GAME_START_LIST1.size >5 ">
           <table>	
	      	<s:if test="#session.startValueGameSearch!=0"><s:a href="startGameAjax.action?end=first">First</s:a><s:a   href="startGameAjax.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
         	<s:if test="#session.startValueGameSearch==((#session.NEW_GAME_START_LIST1.size/5)*5)">Next Last</s:if><s:else><s:a   href="startGameAjax.action?end=Next" >Next</s:a><s:a  href="startGameAjax.action?end=last">Last</s:a></s:else>      
       	  </table>
      </s:if>
  </s:div>  	
		</div></div> 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_startSale_Menu.jsp,v $
$Revision: 1.2 $
</code>