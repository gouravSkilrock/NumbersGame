<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
<body>
				<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.close.games.for.sale"/> </h3>



			 <table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr> <th align="center" colspan="6"><s:text name="btn.srch.res"/> </th></tr>
			 
			  <s:if test="#session.SALE_ClOSE_SEARCH_RESULTS.size !=0"> 
			    <tr>
			     <th><s:text name="label.game.nbr"/></th>
			     <th><s:text name="label.name"/></th>
			     <th><s:text name="label.status"/></th>
			     <th><s:text name="label.start.date"/></th>
			     <th><s:text name="label.sale.end.date"/></th>
			     <th><s:text name="label.pwt.end.date"/></th>
			   </tr>
			  <s:iterator id="closeGames" value="#session.SALE_ClOSE_SEARCH_RESULTS">
			    <tr>
			      <td> <s:a href="bo_gm_stopSale_Detail.action?gameId=%{gameId}"><s:property  id="gameId"  value="%{gameNbr}"/></s:a></td>
			      <td> <s:property value="gameName"/></td>
			      <td> <s:property value="gameStatus"/></td>
			      <td> <s:property value="gameStartDate"/></td>	
			      <td> <s:property value="saleEndDate"/></td>		      
			      <td><s:property value="pwt_end_date"/></td>
			    </tr>
			  </s:iterator>
			  </s:if> 
			  <s:else>
					<tr>
								<td colspan="6" align="center"><s:text name="label.no.game.to.process.for.sale.close"/></td>
							</tr>
			  </s:else>
			 </table>
			 <s:div name="navi"> 
<s:if test=" #session.SALE_ClOSE_SEARCH_RESULTS1.size >5 ">    
	
	
	<s:if test="#session.startValueOrgSearch!=0"><s:a href="bo_gm_stopSale_Navigate.action?end=first"><s:text name="label.first"/></s:a><s:a  href="bo_gm_stopSale_Navigate.action?end=Previous" ><s:text name="label.pre"/> </s:a></s:if><s:else><s:text name="label.first.pre"/></s:else>    
    <s:if test="#session.startValueOrgSearch==((#session.SALE_ClOSE_SEARCH_RESULTS1.size/5)*5)"><s:text name="label.next.last"/></s:if><s:else><s:a href="bo_gm_stopSale_Navigate.action?end=Next" ><s:text name="label.next"/></s:a><s:a href="bo_gm_stopSale_Navigate.action?end=last"><s:text name="label.last"/></s:a></s:else>      
    
	</s:if>
	</s:div>  				

		</div></div>
			
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_stopSale_Menu.jsp,v $
$Revision: 1.3 $
</code>