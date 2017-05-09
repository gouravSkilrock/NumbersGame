	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	<s:head theme="ajax" debug="false"/>
	
	<script>
		function saveAssignUser(gameId){
			var arr = new Array();
			_id.o("gameId").value=gameId;
			for(var i=0; i<10; i++){
				var obj = _id.o("userId"+(i+1)+"R"+gameId);
				if(obj != null){
					var val = obj.value;
					arr[i] = val;
				}
			}
			
			_id.o("userArrId").value=arr;
			if(validate(gameId)){
				document.forms[0].submit();
			}
		}
	
		function validate(gameId){
			var count = 0;
			var arr = new Array();
			for(var i=0; i<10; i++){
				var obj = _id.o("userId"+(i+1)+"R"+gameId);
				if(obj != null){
					var val = obj.value;
					arr[i] = val;
				}
			}
			
			for(var i=0; i<(arr.length); i++){
				if(arr[i] != -1){
					count = count + 1;
				}
			}
			
			if(count <=1){
				alert('<s:text name="error.min.user.select"/>');
				return false;
			}
			
			for(var i=0; i<(arr.length); i++){
				if(arr[i] != -1){
					for(var j=(i+1); j<arr.length; j++){
						if(arr[i] == arr[j]){
							alert('<s:text name="error.select.diff.user"/>');
							return false;
						}
					}
				}
			}
			
			return true;
		}
			
	</script>
		</head>
		
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<div id="data" style="margin-left: -162px;">
				<h3>
						<s:text name="label.edit.res.submit.user"/>'s
					</h3>
			<s:form action="resultUserAssignEdit">
				<s:hidden name="gameId" id="gameId" value=""/><!--
				<s:hidden name="gameNo" value="" id="gameNo"/>
				--><s:hidden name="userStr" value="" id="userArrId"/>
				
			   <div id="top_form">
			  	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			      <tr>
			  	     <th><s:text name="label.game.name"/></th>
			  	      <s:iterator value="#session.BO_RESULT_ASSIGN_USERS_COUNT" status="ti">
			  	      <th><s:text name="label.user"/><s:property value="#ti.index+1"/></th>
			  	      </s:iterator>
			  	      <th><s:text name="label.edit"/></th>
			  	   </tr>
			  	  <s:if test=" #session.USER_LIST.size !=0 ">  
			  	  <s:iterator value="#session.USER_LIST">
			  	   
			  	  <tr>
			  	  <td><s:property value="gameName"/></td>
			  	  <s:iterator value="#session.BO_RESULT_ASSIGN_USERS_COUNT" status="ti">
			  	   <td><s:select list="#session.BO_USER_LIST" theme="simple" value="%{userIdArr[#ti.index]}" headerKey="-1" headerValue="%{getText('label.please.select')}" id="userId%{#ti.index+1}R%{gameId}" cssClass="option" /></td>
					</s:iterator>
			  	  
			  	    <td><s:a href="#" onclick="saveAssignUser('%{gameId}')"><s:property value="firstName"/><s:text name="btn.save"/></s:a></td>
			  	    	  	  
			  	  </tr>
			  	  
			  	  
			  	  </s:iterator>
			  	</s:if>  
			  	<s:else>
			  	   <tr>
			   			<td colspan="5" align="center"><s:text name="msg.no.record.process"/></td>
			   		 </tr>
			  	</s:else> 
			  	</table>

				</s:form>
	  </div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_result_AssignUser_Fetch.jsp,v $
$Revision: 1.3 $
</code>