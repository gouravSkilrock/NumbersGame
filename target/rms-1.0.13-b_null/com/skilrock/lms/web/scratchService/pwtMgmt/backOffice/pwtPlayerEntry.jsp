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
<s:actionerror/>
		<s:form action="verifyPWTTickets">
			 <h3>PWT Entry Form</h3>
			 <s:set name="Game_name_nbr" value="#session.SELECTED_GAMENBR_NAME"/>
			<table>

				<tr>
			    <td> 
			    <s:label theme="simple" value="Game Name"/> <s:textfield theme="simple" label="Game Name" name="gameNbr_Name"  value="%{Game_name_nbr}" disabled="true"></s:textfield>
			    </td>
			    
			</tr>     

			</table>
			
                <table border="1">
              
			<tr>
			    <td> 
			    <s:label theme="simple" value="Virn Code"/> <s:textfield theme="simple" label="Virn Code" name="virnCode" ></s:textfield>
			    </td>
			    
			</tr>     
			
             
                </table>
 		
				
                <table>
				<tr></tr>
				<tr></tr>
				<tr> <td><s:submit cssClass="button" theme = "simple" value="Verify and Save"/> </td>
					
				</tr>
				</table>


		
		</s:form>
</div></div>
	</body>
</html><code id="headId" style="visibility: hidden">$Id: pwtPlayerEntry.jsp,v 1.1 2010/04/02 12:12:06 gaurav Exp $</code>
