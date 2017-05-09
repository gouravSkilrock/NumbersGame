<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/inventoryReport.js"></script>
	<s:head theme="ajax" debug="false"/>
</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
     <div id="top_form">
  <h3> <s:text name="label.srch.inv"/></h3>
	
	
  <s:form name="searchgame" action="bo_im_searchInv_Search" onsubmit="return validateGameNumber()"> 		
	 <table  border="0" cellpadding="2" cellspacing="0">
		<tr>
			<td><s:textfield name="gamename" value="" key="label.game.name" size="20" /></td>
		</tr>
		<tr>
			<td><div id="gameNumber1"></div><s:textfield name="gamenumber" value="" key="label.game.nbr" size="20" id="gameNumber"/></td>
		</tr>
		
		<tr>
			<td><s:select name="gamestatus" key="label.game.status" list="{'ALL','OPEN','CLOSE','TERMINATE','SALE_HOLD','SALE_CLOSE'}" cssClass="option"/></td>
		</tr>		 
		<tr>
			<td><s:submit name="search" key="btn.srch" align="right"  targets="down" theme="ajax" cssClass="button"/></td>
		</tr>
		
	 </table>
	</s:form> 
	
	<div id="down" align="left" ></div>
	</div></div>
	
 	
 	 
		
  	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_searchInv_Menu.jsp,v $
$Revision: 1.3 $
</code>