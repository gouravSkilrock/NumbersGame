<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css" / >

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>

	<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap">

  <div id="top_form">
  <h3>Search Order</h3>
  
		<s:form action="doAgentOrderDispatchSearch">
			<table>

				


				<tr>
					<s:textfield label="Game Name" name="gameName"></s:textfield>
				</tr>
				<tr>
					<s:textfield label="Game Number" name="gameNumber"></s:textfield>
				</tr>
				<tr>
					<s:textfield label="%{#application.TIER_MAP.RETAILER} Organization Name" name="retOrgName"></s:textfield>
				</tr>
				<tr>
					<s:textfield label="Order Id" name="orderNumber" ></s:textfield>
				</tr>
				
<s:submit  theme="ajax" targets="bottom" value="Search" cssClass="button"/>

</table>
			
			</s:form>
 <div  id="bottom" >  
	
 </div>
</div></div>
	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agentInvOrderDispatchSearch.jsp,v $
$Revision: 1.2 $
</code>