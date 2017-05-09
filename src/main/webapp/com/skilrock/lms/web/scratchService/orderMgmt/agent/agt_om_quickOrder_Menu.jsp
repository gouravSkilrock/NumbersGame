<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>var path = "<%=request.getContextPath() %>";</script>		
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/orderMgmt/agent/js/agt_orderMgmt_quickOrderReq.js"/>
	<s:head theme="ajax" debug="false"/>
</head>


<body onload="fetchDet(),appendGameTab('Virn')">

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<div id="top_form">
	<h3>Quick Order Request To BO</h3>
	
		<s:form name="pwtAgent" action="agt_om_quickOrder_Save"  onsubmit="return validate()">
		<table border="0" cellpadding="3" cellspacing="0" width="100%">
			<tr>
				<td colspan="3"><s:actionerror /><div id="game_name_error"></div></td>			
			</tr>
						
			<tr>
				<td colspan="3">
					<table border="1" cellpadding="3" cellspacing="0" width="100%" id="gameTab" bordercolor="#CCCCCC">
					<tr>
						<th align="center" width="10%">S No.</th>
						<th nowrap="nowrap"  align="center" width="25%">Game Name</th>
						<th align="center" width="15%">No Of Books to Order</th>
						<th align="center" width="50%">Error Message</th>
					</tr>	
					</table>
				</td>
			</tr>
			
			<tr>
				<td align="right">
				<input type="button"  name="addGame" id="add_game" value="Add More Game" class="button" onclick="addGameToTable('Virn');"/>
				<table style="display:none">
					<s:select  name="gameNbr_NameHid" id="gameNbr_NameHid"  headerKey="-1" headerValue="--Please Select--"
							list="{}" />
					<tr><td><input type="hidden" name="nog" id="nog" value="<%=application.getAttribute("NO_OF_GAME_PER_PAGE")%>"/></td></tr>
				</table>				 
			</td>
				<td align="right" colspan="2">
					 <div id="subCheck" style="position:absolute;"></div>
					 <s:submit align="right" theme="simple" value="Generate Order" cssClass="button" id="subTrans" /> 
				</td>
			</tr>
		</table>
	</s:form>
</div>
</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_quickOrder_Menu.jsp,v $
$Revision: 1.3 $
</code>