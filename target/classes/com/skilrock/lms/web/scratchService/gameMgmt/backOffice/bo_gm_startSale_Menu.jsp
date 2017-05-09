<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">

  <div id="top_form">
 
   
  <h3><s:text name="label.start.games"/> </h3>
 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
 				
			   <tr><th align="center"><s:text name="btn.srch.res"/></th></tr>
			    
			<s:if test="#session.NEW_GAME_START_LIST==null || #session.NEW_GAME_START_LIST.size()==0">
				<tr><td colspan="4" align="center"> <s:text name="label.no.game.to.start"/></td>
				</tr>
			</s:if>
   			<s:else>
   			  <tr>
			     <th><s:text name="label.game.name"/></th>
			     <th><s:text name="label.game.nbr"/></th>
			     <th><s:text name="label.start.date"/></th>
			     <th><s:text name="label.start"/> </th>			     
			     
			    
			   </tr>
			  <s:iterator id="games" value="#session.NEW_GAME_START_LIST">
			    <tr>
			      
			      <td> <s:property  id="task"  value="%{gameName}"/></td>
			      <td> <s:property value="gameNbr"/></td>
			      <td> <s:property value="startDate"/></td>
			      <td> <s:a href="com/skilrock/lms/admin/bo_gm_startSale_Save.action?gameId=%{gameId}"><s:text name="label.start"/></s:a></td>
			     </tr>
			  </s:iterator> 
			</s:else>
			 </table>
			 
	<s:div id="naviga" >
	    <s:if test=" #session.NEW_GAME_START_LIST1.size >5 ">
           <table align="center">
	
	      	<s:if test="#session.startValueGameSearch!=0"><s:a href="startGameAjax.action?end=first"><s:text name="label.first"/></s:a><s:a   href="startGameAjax.action?end=Previous" > <s:text name="label.pre"/></s:a></s:if><s:else><s:text name="label.first.pre"/></s:else>    
         	<s:if test="#session.startValueGameSearch==((#session.NEW_GAME_START_LIST1.size/5)*5)"><s:text name="label.next.last"/></s:if><s:else><s:a   href="startGameAjax.action?end=Next" ><s:text name="label.next"/></s:a><s:a  href="startGameAjax.action?end=last"><s:text name="label.last"/></s:a></s:else>      
       	  </table>
      </s:if>
  </s:div>  	
		</div></div> 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_startSale_Menu.jsp,v $
$Revision: 1.3 $
</code>