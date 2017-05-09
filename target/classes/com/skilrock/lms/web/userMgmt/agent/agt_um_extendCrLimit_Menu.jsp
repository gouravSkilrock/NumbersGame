<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


<s:head theme="ajax" debug="false"/>
	</head>
<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

				<div id="wrap">
<div id="top_form">
	<h3> Search <s:property value="#application.TIER_MAP.RETAILER" /> to Extend Credit Limit </h3>
	<s:div theme="ajax" id="x">
		<s:form action="agt_um_extendCrLimit_Search" method="post">
			<table border="0" cellpadding="3" cellspacing="0" width="450">

				<tr>
					<s:textfield label="Organization Name" name="orgName"></s:textfield>
				</tr>
				<tr>
					<s:select theme="simple" cssStyle="visibility:hidden;"  label="Organization Type" name="orgType" list="#{'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}" cssClass="option"></s:select>
				</tr>

   				 <tr>
					<s:select headerKey="1" headerValue="--- Please Select ---" label="Status" name="orgStatus" list="{'ACTIVE','INACTIVE','BLOCK','TERMINATE'}"cssClass="option"></s:select>
				</tr>
				<tr><td></td>
				<td align="left">
					<table>

				<s:submit  theme="ajax" targets="aa11" value="Search" cssClass="button"/>
					</table>
				</td>
				</tr>
			


			</table>
		
			</s:form>
			 
    </s:div>
     
     
   <s:div theme="ajax" id="aa11" >   
			
		
	</s:div></div></div>
		
   	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_extendCrLimit_Menu.jsp,v $
$Revision: 1.3 $
</code>