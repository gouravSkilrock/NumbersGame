	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>path ="<%=request.getContextPath() %>";</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/retailer/js/ret_invMgmt_bookWiseDtls.js"></script>		
		<s:head theme="ajax" debug="false"/>
	</head>

<body>

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
    <div id="top_form">
 	<h3><s:text name="label.scratch.book.wise.inventory.details"/></h3> 
 		<div style="width:400px;text-align: right"  id="progImgDiv"></div>
 		<div id="ret_error" style="color: red;text-align: center;width:400px"></div>
 		<div id="dtlTbl">
		<table border="1" width="400" bordercolor="CCCCCC" style="text-align: left"  cellpadding="3" cellspacing="0">
			<s:select name="gameid"  label="%{getText('label.scratch.game.name')}" cssClass="option" id="game_id" list="#session.retListGame" onchange="_id.i('d2', '');"/>							
			<tr>
				<td>&nbsp;</td>
			 	<td>
					<input type="button" name="showDetail" id="submit" value="<s:text name="btn.scratch.show.details"/>" class="button" onclick="getInventoryDetails('ret_im_bookWiseInvDetAjx_Detail.action', 'd2')" />
			 	</td>
		 	</tr>
	 	</table>	 	
		</div>
	 <div id="loadingDiv">&nbsp;</div>
	 <div id="d2" style="text-align: left;float: left;width: 900px"></div>	
	 </div></div>
	 
	  
	 
	<br/><br/>
	
	
</body>
</html><code id="headId" style="visibility: hidden">$Id: ret_im_bookWiseInvDet_Menu.jsp,v 1.1 2010/04/02 12:12:11 gaurav Exp $</code>