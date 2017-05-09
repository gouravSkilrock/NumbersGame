	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/drawMgmt/js/updateFreeTicket.js" />
	<s:head theme="ajax" debug="false"/>

	
		</head>
		
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<div id="data">
				<h3>
						Update Promo Game(s) Tickets 
						
				</h3>
			<s:form action="bo_dg_updateFreeTicket_Save">
				<s:hidden name="saleGameId" id="saleGameId" value=""/>
				<s:hidden name="promoGameId" value="" id="promoGameId"/>
				<s:hidden name="promoTicekts" value="" id="promoTicekts"/>
				
			  <div id="top_form">
			  	<div id="msg" style="color: red" align="center"></div>
			  	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			      <tr>
			  	     <th>Game Name</th>
			  	     <th>Promo Game Name</th>
			  	     <th> No of Promo Ticket </th>
			  	     <th >Edit Promo Ticket </th>
			  	     <th> ACTION </th>
			  	     <th> Message </th>
			  	   </tr>
			  	     <s:set name="gameMap" value="promoGameBeanMap" ></s:set>
			  	  <s:if test=" #gameMap.size !=0 ">  
			  	
			  	  <s:iterator value="#gameMap">
			  	    <s:set name="promoList" value="value" ></s:set>
			  	    <s:set name="gameId" value="key" ></s:set>
			 
			  	 <s:iterator value="#promoList" >
			  	  	  <tr>
			  		  <td><s:property value="mainGameName"/></td>
			  		   <td><s:property value="promoGameDisplayName"/></td>
			  		   <td><div id="ptDiv<s:property value='promoGameNo'/>"><s:property value='noOfPromoTickets'/></div></td>
			  		   <td><table><tr> <td><table><s:textfield id="promoTickets" id="promoTickets%{promoGameNo}" name="promoTickets" value="%{noOfPromoTickets}" maxlength="2" size="2" onchange="allowSubmit()" onkeyup="return keyPress(this,event)" onkeydown="return keyPress(this,event)" > </s:textfield></table></td><td><input type="button" value="+" onclick="inc('promoTickets<s:property value="promoGameNo"/>')"/></td><td><input type="button" value="-" onclick="dec('promoTickets<s:property value="promoGameNo"/>')"/></td></tr> </table></td>
			  		   
			  		   <td><s:a href="#" onclick="return updateFreeTicket('%{key}','%{promoGameNo}','promoTickets%{promoGameNo}')"><s:property value="firstName"/>Update</s:a></td>
						  <th> <div id="msgDiv<s:property value='promoGameNo'/>">Click On Update To Change Value</div> </th>
						 </tr>
					</s:iterator>
			  	
			  	  </s:iterator>
			  	</s:if>  
			  	<s:else>
			  	   <tr>
			   			<td colspan="5" align="center">No Records to Process</td>
			   		 </tr>
			  	</s:else> 
			  	</table>

				</s:form>
	  </div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_updateFreeTicket_menu.jsp,v $
$Revision: 1.2 $
</code>