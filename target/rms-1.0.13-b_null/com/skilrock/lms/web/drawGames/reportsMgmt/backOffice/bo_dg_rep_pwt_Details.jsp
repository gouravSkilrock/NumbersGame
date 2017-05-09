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

<body >


	<div id="wrap">

  <div id="top_form">
  <br />	
	 <table width="600" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr>
			<th align="center">Sr.No.</th>
			<s:if test="%{filter != 'Game Wise'}">
				<th align="center">Name</th>	
			</s:if>
			<th align="center">Game Name</th>
			<th align="center">Prize</th>
			<th align="center">No Of Tickets</th>
			<th align="center">Amount</th>
			
						
		</tr>
		
		<s:if test="#session.searchResult.size>0">
			<s:iterator id="beanCart" value="#session.searchResult" status="itr">
		<tr class="startSortable">
				<td align="right"><s:property value="#itr.index+1" /></td>
				<s:if test="%{filter != 'Game Wise'}">
					<td><s:property value="playerName" /></td>
				</s:if>
				
				<td><s:property value="gameName" /></td>
				<td align="right"><s:property value="prize" /></td>				
				<td align="right"><s:property value="noOfTkt" /></td>
				<td align="right"><s:property value="amount" /></td>
							
		</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
		<tr>
				<td colspan="6" align="center"> No Records to Process</td>
		</tr>
		</s:else>
		
	 </table>
	 
	 </div></div>

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_rep_pwt_Details.jsp,v $
$Revision: 1.2 $
</code>