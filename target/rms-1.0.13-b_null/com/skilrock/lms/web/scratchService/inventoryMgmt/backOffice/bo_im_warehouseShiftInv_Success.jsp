	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		</head>
<body>


	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">
	<%
	String dcId = (String)session.getAttribute("dcId") ;
	String autoGeneDCNo = (String)session.getAttribute("autoGeneDCNo") ;
	 %>


  <div id="top_form">
   <h3>Inventory shifted successfully. <br/>
  <!--  DCId = <%= dcId %> --> 
   
   <b> Click to see Delivery Challan </b> : <font color="red" ><a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/bo_rep_showDeliveryChallenWarehouse.action?id=<%= dcId %>&type=DLCHALLAN" target="_blank"><%= autoGeneDCNo %></a></font> </h3>

 
<s:form>
			
			<table border="0" cellpadding="2" cellspacing="0">
			<tr>
			  <td>
			  <a href="<%= request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/bo_im_invWarehouseShift_Menu.action">Click to Shift More Inventory</a>
			  </td>
			</tr>
			
			</table>
			
		</s:form>
		</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_warehouseShiftInv_Success.jsp,v $
$Revision: 1.3 $
</code>