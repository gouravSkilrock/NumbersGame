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

<body >


	<div id="wrap">

  <div id="top_form">
  <br />	
	 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		<tr>
			<th colspan="2" nowrap="nowrap" align="center"><s:text name="label.game.name"/></th>
			<th align="center"><s:text name="label.sale.end.date"/></th>
			<th align="center"><s:text name="label.sold.by.bo"/></th>
			<th align="center"><s:text name="label.returned.to.bo"/></th>
			<th align="center"><s:text name="label.sold.by"/> <s:property value="#application.TIER_MAP.AGENT" /></th>
			<th align="center"><s:text name="label.returned.to"/> <s:property value="#application.TIER_MAP.AGENT" /></th>
						
		</tr>
		
		<s:if test="#session.searchResultBOLink.size>0">
			<s:iterator id="beanCart" value="#session.searchResultBOLink">
		<tr>
				<td align="left" colspan="2" nowrap="nowrap"><s:property value="gamenbr" />-<s:property value="gamename" /></td>
				<td align="right"><s:property value="saleenddate" /></td>
				<td align="right"><s:property value="purchaseFromBo" /></td>				
				<td align="right"><s:property value="returnToBo" /></td>
				<td align="right"><s:property value="soldByAgent" /></td>
				<td align="right"><s:property value="returnToAgent" /></td>				
		</tr>	
			</s:iterator> 
		</s:if>
		<s:else>
		<tr>
				<td colspan="7" align="center"> <s:text name="msg.no.record.process"/></td>
		</tr>
		</s:else>
		
	 </table>
	 
	 </div></div>

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_searchInv_Detail.jsp,v $
$Revision: 1.3 $
</code>