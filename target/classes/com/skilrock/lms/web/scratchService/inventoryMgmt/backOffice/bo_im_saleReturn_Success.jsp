<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.sales.return"/></h3>
	
		
		<table border="0" width="45%" cellpadding="2" cellspacing="0">
		<h3><s:text name="label.following.packs.books.accepted"/></h3>
			<tr>
				<td width="50%">
					<s:text name="%{#application.TIER_MAP.AGENT} Organization" />
				</td>
				<td width="50%">
					<s:property value="#session.ORG_NAME" />
				</td>
			</tr>

			<tr>
				<td width="50%">
					<s:text name="Game" />
				</td>
				<td width="50%">
					<s:property value="#session.GAME_NAME" />
				</td>
			</tr>
	
	<tr><td bgcolor="black" height="1" colspan="2"></td></tr>
		</table>

		<br />
		<table width="45%" border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td width="50%"></td>
			</tr>
		</table>
		<s:if test="#session.VALID_PACK_LIST.size>0">
		<table border="1" width="45%" cellpadding="2" cellspacing="0">

			<tr>
				<td width="50%">
					<s:text name="Pack Number" />
				</td>
				<td width="50%">
					<s:text name="Validity Status" />
				</td>
			</tr>
			<s:iterator value="#session.VALID_PACK_LIST">
				<tr>
					<td width="50%">
						<s:property value="%{packNumber}" />
					</td>
					
					<td width="50%">
						<font color="red"><s:property value="%{status}" />
						</font>

					</td>


				</tr>

			</s:iterator>
		</table>
 </s:if>
		<table width="45%" border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td width="50%"></td>
			</tr>
		</table>
<s:if test="#session.VALID_BOOK_LIST.size>0">
		<table border="1" width="45%" cellpadding="2" cellspacing="0">
			<tr>
				<td>
					<s:text name="Book Number" />
				</td>
				<td>
					<s:text name="Validity Status" />
				</td>
			</tr>

			<s:iterator value="#session.VALID_BOOK_LIST">
				<tr>
					<td width="50%">
						<s:property value="%{bookNumber}" />
					</td>
					<td width="50%">
						<font color="red"><s:property value="%{status}" />
						</font>

					</td>


				</tr>

			</s:iterator>
			</table>
		</s:if>
		<table width="45%"  border="0" cellpadding="2" cellspacing="0">
			<tr>
				
				<s:url id="saleReturn" action="bo_im_saleReturn_Menu" namespace="/com/skilrock/lms/inventory"/>
				
				<td >
					<s:a href="bo_scratch_boToRetailerDirectSaleRefund_Menu.action"><s:text name="label.more.sale.return"/></s:a>
				</td>
			<s:if test="%{#session.showCreditNote ==true}">
			 		<td align="right"><a theme="simple" href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action" target="_blank"><s:text name="label.show.credit.note"/></a></td>
			</s:if> 
			  <td align="right"><a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/bo_rep_showDeliveryChallen.action?id=<%=session.getAttribute("DEL_CHALLAN_ID") %>&type=DSRCHALLAN" target="_blank"><s:text name="label.show.sale.return.note"/></a></td>
			</tr>
		</table>

	</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_saleReturn_Success.jsp,v $
$Revision: 1.3 $
</code>