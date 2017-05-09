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
   <h3>Inventory Assign: Search Order</h3>
	
		<s:form action="bo_im_assignOrder_Search">
			<table border="0" cellpadding="2" cellspacing="0" >
			<tr>
				<th>
					Search Order
				</th>
			</tr>

				<tr>
					<s:textfield label="Game Name" name="gameName"></s:textfield>
				</tr>
				<tr>
					<s:textfield label="Game Number" name="gameNumber"></s:textfield>
				</tr>
				<tr>
					<s:textfield label="%{#application.TIER_MAP.AGENT} Organization Name" name="agtOrgName"></s:textfield>
				</tr>
				<tr>
					<s:textfield label="Order Id" name="orderNumber" value=""></s:textfield>
				</tr>
				
				<s:submit  theme="ajax" targets="bottom" value="Search" cssClass="button"/>

</table>
			
			</s:form>
					
 <div  id="bottom" >  
	
 </div>
</div></div>
	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_assignOrder_Menu.jsp,v $
$Revision: 1.3 $
</code>