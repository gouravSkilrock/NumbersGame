<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>
     var path = "<%=request.getContextPath() %>";
         
   </script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_gameTktVirnvalidate.js"></script>
   <script>
     function _subTrans(){
			return ( verifyNSave() && _subValid('subTrans'));
		}
   </script>
</head>

<body onload="gameDetails('RETAILER')">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap"><div id="top_form">

  <h3>Winning Ticket And VIRN Verification(Direct Player)</h3>	
	<div id="first">
	  <s:form id="ab" name="tv" action="/com/skilrock/lms/web/scratchService/pwtMgmt/pwt_agtDirectplr_verifyTicketNVirn.action" onsubmit="return _subTrans()"> 		
			 <table  border="0" cellpadding="2" cellspacing="0"  width="400px">
			 	
			 	<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="gName_e"></div></td>
				</tr>
			 	<tr>				
					<td><s:select name="gameIdNbrName" label="Game Name" headerKey="-1" list="{}" cssClass="option"
					headerValue="--Please Select--" id="game_name" /></td>
				</tr>
				<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="tktNo_e"><s:property value="pwtErrorMap.tktBean.status" /></div></td>
				</tr>
				<tr>				
					<td><s:textfield name="ticketNbr" id="ticketNbr" label="Ticket Number" /></td>
				</tr>
				<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="virnNbr_e"><s:property value="pwtErrorMap.pwtBean.message" /></div></td>
				</tr>
				<tr>				
					<td><s:textfield name="virnNbr" id="virnNbr" label="VIRN Number" /></td>
				</tr>			
				<tr>
					<td>&nbsp;</td>							
					<td><div id="subCheck" style="position:absolute;"></div><table><s:submit  formId="ab" id="subTrans" value="Verify And Save" align="right" cssClass="button"/></table></td>
				</tr>		
			 </table>		 
	  </s:form> 
	</div>	
	
</div></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: AgentDirectPlrPWTHome.jsp,v $
$Revision: 1.2 $
</code>