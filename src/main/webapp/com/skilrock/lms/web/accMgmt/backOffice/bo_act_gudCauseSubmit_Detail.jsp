	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
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
   <h3>Game Details</h3>
   <s:form>
		<table width="400" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">

			 
			  <s:iterator id="task" value="#session.GAME_DETAILS">
			    <tr>
			      <td>Game Name</td><td>  <s:property    value="%{gameName}"/></td>
			     </tr>
			     
			      <tr>
			      <td>Game Number </td><td> <s:property    value="%{gameNbr}"/></td>
			     </tr>
			     
			       <tr>
			      <td>Game Status</td><td> <s:property    value="%{gameStatus}"/></td>
			     </tr>
			      <s:if test="%{#session.serviceCode=='SE'}">
				      <tr>
				      <td>Ticket Price </td><td> 
				      	<s:set name="ticketPrice" value="ticketPrice"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("ticketPrice"))%>			      	
				      </td>
				     </tr>
				      <tr>
				      <td>Game Start Date </td><td> <s:property    value="%{gameStartDate}"/></td>
				     </tr>
				      
				      <tr>
				      <td>Sale End Date </td><td> <s:property    value="%{saleEndDate}"/></td>
				     </tr>
				      
				      <tr>
				      <td><s:text name="PWT"/> End DATE </td><td> <s:property   value="%{pwt_end_date}"/></td>
				     </tr>
			      </s:if>
			  </s:iterator> 			
			
</table>	
<table align="center">
			<tr>
				<td>
					<table><s:submit value="CANCEL" action="bo_act_gudCauseSubmit_Menu" align="center" cssClass="button" /> </table>
				</td>
				<td>
					<table><div id="subCheck" style="position:absolute;"></div><s:submit value="Submit Good Cause" action="bo_act_gudCauseSubmit_Save" align="center" cssClass="button" id="subTrans" onclick="return _subValid(this.id);"/></table>
				</td>
				
			</tr>
</table>
<s:hidden name="serviceCode" value="%{#session.serviceCode}"></s:hidden>
  <!-- 
       <table border="1" align="center">

						<tr>
							<th>
								Prize Amount
							</th>
							<th>
								No of Prizes Remaining
							</th>
						</tr>
						<s:iterator value="prizeStatusList">
							<tr>
								<td>
									<s:property value="%{prizeAmt}" />
								</td>
								<td>
									<s:property value="%{nbrOfPrizeLeft}" />
								</td>
							</tr>
						</s:iterator>
					</table>
         -->

		


</s:form>
		</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseSubmit_Detail.jsp,v $
$Revision: 1.2 $
</code>