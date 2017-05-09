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
<br></br>
<h4>Pending <s:text name="PWT"/> Details</h4>
  <div id="top_form">
  
          <s:form>
          
             <table border="1" align="center" cellspacing="0" cellpadding="2" borderColor = "#CCCCCC"  width="600">
	               <tr>
	                 <th align="center">Game Name</th>
	                 <th align="center">Game Number</th>
	                 <th align="center">Ticket Number</th>
	                 <th align="center">Prize Amount</th>
	                 <th align="center">Details</th>
	              </tr>
              	<s:if test = "gamePlayerBeanList.size>0">
                 <s:iterator value="gamePlayerBeanList">
                 <s:set name="pwtAmtStr" value="%{pwtAmt}"></s:set>
                 <tr>
	                  <td> <s:property value="%{gameName}"/></td>
	                  <td> <s:property value="%{gameNbr}"/></td>
	                  <td> <s:property value="%{ticketNbr}"/></td>
	                  <td> <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmtStr")) %></td>
				      <td>
				 		<s:url id="showDetails" action="getPwtDetails" namespace="/com/skilrock/lms/admin">
				                 <s:param name="plrId" value="%{plrId}"></s:param>
				                 <s:param name="gameId" value="%{gameId}"></s:param>
				                 <s:param name="virnCode" value="%{virnCode}"></s:param>
					             <s:param name="pwtAmt" value="%{pwtAmt}"></s:param>
					             <s:param name="ticketNbr" value="%{ticketNbr}"></s:param>
					             <s:param name="playerReceiptId" value="%{playerReceiptId}"></s:param>
               			</s:url>
				 		<s:a href="%{showDetails}">Details</s:a>
				 	</td>
                 </tr>                 
                 </s:iterator>
                </s:if>
             	<s:else>
             		<tr>
	                 <td colspan="5" align="center">No <s:text name="PWT"/> Details Found</td>
	                 
	              </tr>
             	</s:else>
             
             
             </table>
          
          
          
          
          </s:form>
  
  
  
  
  </div></div>
	</body>
	
<code id="headId" style="visibility: hidden">
$RCSfile: approvePwt.jsp,v $
$Revision: 1.2 $
</code>