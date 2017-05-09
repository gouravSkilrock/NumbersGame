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
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>
     var path = "<%=request.getContextPath() %>";
	   function setIsLoadingFirst(){
		isLoadingFirst=true;
		}
   </script>
  
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/ticketBookTrackar.js"></script>
   	
<style type="text/css">
.outerBorderLeft {
          border-left :  3px solid #000000;	 
       }
</style>
 
</head>

<body onload="gameDetails('RETAILER')">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap"><div id="top_form">

  <h3><s:text name="label.pwt.tkt.and.virn.tracker"/></h3>	
	<div id="first">
	  <s:form id="ab" name="tv" action="/com/skilrock/lms/web/scratchService/pwtMgmt/trackPwt.action" onsubmit="return verifyNSave()"> 		
			 <table  border="0" cellpadding="2" cellspacing="0"  width="400px">
			 	<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="gName_e"></div></td>
				</tr>
			 	<tr>				
					<td><s:select name="gameIdNbrName" key="label.game.name" headerKey="-1" list="{}" cssClass="option"
					headerValue="%{getText('label.please.select')}" id="game_name" /></td>
				</tr>
				<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="tktNo_e"><s:property value="pwtErrorMap.tktBean.status" /></div></td>
				</tr>
				<tr>				
					<td><s:textfield name="ticketNbr" id="ticketNbr" key="label.tckt.no" /></td>
				</tr>
				<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="virnNbr_e"><s:property value="pwtErrorMap.pwtBean.message" /></div></td>
				</tr>
				<tr>				
					<td><s:textfield name="virnNbr" id="virnNbr" key="label.virn.nbr" /></td>
				</tr>			
				<tr>
					<td>&nbsp;</td>					
					<td><table><s:submit name="search" formId="ab" key="btn.track" align="right" cssClass="button" theme="ajax" targets="results" onclick="setIsLoadingFirst()"/></table></td>
				</tr>		
			 </table>		 
	  </s:form> 
	</div>
	
	<s:div id="results">
	</s:div>
	
</div></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: trackVirnHome.jsp,v $
$Revision: 1.3 $
</code>