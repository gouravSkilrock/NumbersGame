	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>path ="<%=request.getContextPath() %>";</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/agent/js/agt_invMgmt_bookWiseDtls.js"></script>		
		<s:head theme="ajax" debug="false"/>
	</head>

<body onload="fetchRetOrgList()">

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
    <div id="top_form">
 	<h3>Book Wise Inventory Details</h3> 
 	
 		<div style="width:400px;text-align: right"  id="progImgDiv"></div>
 		<div id="ret_error" style="color: red;text-align: center;width:400px"></div>
 		<div id="dtlTbl">
		<table border="1" width="400" bordercolor="CCCCCC" style="text-align: left"  cellpadding="3" cellspacing="0">
			<s:select list="#{'AGENT':#application.TIER_MAP.AGENT.toUpperCase(), 'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}" name="type" cssClass="option" label="Organization Type" id="org_type" onchange="onTypeChangeValue(this.value)" />
			<s:select  name="retName" label="%{#application.TIER_MAP.RETAILER} Organization Name" cssClass="option" disabled="true" id="ret_org_name" list="{}" headerKey="-1" headerValue="--Please Select--" />
			<s:select name="gameid" label="Game Name" cssClass="option" id="game_id" list="#session.boAgentListGame"/>							
			<tr>
				<td>&nbsp;</td>
			 	<td>
					<input type="button" name="showDetail" id="submit" value="Show Details" class="button" onclick="getInventoryDetails('agt_im_bookWiseInvDetAjx_Detail.action', 'd2')" />
			 	</td>
		 	</tr>
	 	</table>	 	
		</div>
	 <div id="loadingDiv">&nbsp;</div>
	 <div id="d2" style="text-align: left;float: left;width: 900px"></div>	
	 </div></div>
	 
	  
	 
	<br/><br/>
	
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_bookWiseInvDet_Menu.jsp,v $
$Revision: 1.2 $
</code>