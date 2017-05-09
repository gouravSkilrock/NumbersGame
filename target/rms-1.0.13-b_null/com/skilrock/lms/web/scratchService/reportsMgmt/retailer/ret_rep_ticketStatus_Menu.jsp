<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/AutoComplete.css" media="screen" type="text/css"/>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/AutoComplete.js"></script>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false"/>
	<script>
	var path = "<%=request.getContextPath() %>";
	
	function getTicketDetailStatus(book_no, isRemain){
		var url = "ret_rep_TicketDetailStatus_Search.action";
		var param = "book_no=" + book_no + "&is_remain=" + isRemain;
		_ajaxCallDiv(url, param, "ticketResult");
		sortables_init();
	}

	
	</script>
		<script type="text/javascript"  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/saleReturn.js"></script>

	</head>


	<body >
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	
	<div id="wrap">

  <div id="top_form">
   <h3><s:property value="#application.TIER_MAP.RETAILER" /> Ticket Status</h3>
		

		<s:form name="tikcetStatusRet" id="tikcetStatusRet" action="ret_rep_TicketStatus_Search" method="POST">
		<div id="errorDiv" style="color: red" >&nbsp; &nbsp;&nbsp;
					</div>
		
			<table border="0" width="75%" cellpadding="2" cellspacing="0">
				<tr>
					<td>
						Game Name :
					</td>
					<td>
						<div id="gameDiv">
						<s:select theme="simple" name="gameId" id="gameId"  headerKey="-1"  cssClass="option" headerValue="ALL" list="#session.boAgentListGame"/>
						</div>
						
					</td>
					<td>
					</td>
				</tr>
<tr colspan="3">
			<td><s:submit name="search" value="Search" align="center"  targets="result" theme="ajax" onclick="$('#ticketResult').html('');" cssClass="button"/></td>
		</tr>
			</table>
					</s:form>
	<div id="down"></div>
			<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			<div id="ticketResult" style="overflow-x: auto; overflow-y: hidden;"></div>


</div>

	 </div>



	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_rep_ticketStatus_Menu.jsp,v $
$Revision: 1.2 $
</code>