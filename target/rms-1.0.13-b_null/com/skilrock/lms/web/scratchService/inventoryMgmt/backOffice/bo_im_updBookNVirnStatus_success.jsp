<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>

<body>

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<%!int n=-1,m=-5; %>
	<div id="wrap"><div id="top_form">
  			<h3><s:text name="label.missing.book.status"/> </h3>
   	
		<table width="750" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">
			<tr>
				<th colspan="5" align="center" > <s:text name="label.invalid.book.list"/></th>
			</tr>
			<% n=-1; m=-5; %>
			<s:iterator value="#session.miss_inv_bk">	
			<% n+=1; m+=1;
				if(n%5==0) { %><tr><%} %>
				
				<td align="center" style="color: red"> <s:property /></td>
			
			<%if(m%5==0) { %></tr><%} %>
			</s:iterator>
		</table>
		<br /><br />
		 <table width="750" border="1" cellpadding="2" cellspacing="0"  bordercolor="#CCCCCC">
			<tr>
				<th colspan="5"  align="center"> <s:text name="label.invalid.book.series.list"/></th>
			</tr>
			<% n=-1; m=-5; %>
			<s:iterator value="#session.miss_inv_bkser">	
				<% n+=1; m+=1;
					if(n%5==0) { %><tr><%} %>
					
					<td align="center" style="color: red"> <s:text name="label.from"/> : <s:property value="bookNbrFrom"/> &nbsp;&nbsp;<s:text name="label.to"/> : <s:property value="bookNbrTo"/></td>
				
				<%if(m%5==0) { %></tr><%} %>
			</s:iterator>
		</table>
		<br /><br />
		<table width="750" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">
			<tr>
				<th colspan="5"  align="center"> <s:text name="label.invalid.pack.list"/></th>
			</tr>
			<% n=-1; m=-5; %>
			<s:iterator value="#session.miss_inv_pk">	
				<% n+=1; m+=1;
				if(n%5==0) { %><tr><%} %>
				
				<td align="center" style="color: red"> <s:property /></td>
			
				<%if(m%5==0) { %></tr><%} %>
			</s:iterator>
		</table>
		<br /><br />
		<table width="750" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">
			<tr>
				<th colspan="5"  align="center"> <s:text name="label.invalid.pack.series.list"/></th>
			</tr>
			<% n=-1; m=-5; %>
			<s:iterator value="#session.miss_inv_pkser">	
				<% n+=1; m+=1;
				if(n%5==0) { %><tr><%} %>
				
				<td align="center" style="color: red"> <s:text name="label.from"/> : <s:property value="bookNbrFrom"/> &nbsp;&nbsp;<s:text name="label.to"/> : <s:property value="bookNbrTo"/></td>
			
				<%if(m%5==0) { %></tr><%} %>
			</s:iterator>
		</table>
		<br /><br />
		<table width="750" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">
			<tr>
				<th colspan="5"  align="center"> <s:text name="label.invalid.virn.list"/></th>
			</tr>
			<% n=-1; m=-5; %>
			<s:iterator value="#session.miss_inv_virn">	
				<% n+=1; m+=1;
				if(n%5==0) { %><tr><%} %>
				
				<td align="center" style="color: red"> <s:property/> </td>
			
				<%if(m%5==0) { %></tr><%} %>
			</s:iterator>
		</table>
	</div></div>
</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_updBookNVirnStatus_success.jsp,v $
$Revision: 1.3 $
</code>