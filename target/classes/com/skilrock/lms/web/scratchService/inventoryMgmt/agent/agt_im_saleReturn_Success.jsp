<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css" />

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>

<body>

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap">

  <div id="top_form">
		<h3>Sales Return</h3>
		<h4>
			Following Packs / Books Has Been Accepted
		</h4>
		<table border="1" width="75%">
			<tr>
				<td width="50%">
					<s:text name="%{#application.TIER_MAP.RETAILER} Organization" />
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
		</table>

		<table width="75%">
			<tr>
				<td width="50%"></td>
			</tr>
		</table>

        <s:if test="#session.VALID_PACK_LIST.size>0">
		<table border="1" width="75%">
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
						<font color="red"><s:property value="%{status}" /> </font>
					</td>
				</tr>
			</s:iterator>
		</table>
      </s:if>
      
		<table width="75%">
			<tr>
				<td width="50%"></td>
			</tr>
		</table>

       <s:if test="#session.VALID_BOOK_LIST.size>0">
		<table border="1" width="75%">
			<tr>
				<td width="50%">
					<s:text name="Book Number" />
				</td>
				<td width="50%">
					<s:text name="Validity Status" />
				</td>
			</tr>
			<s:iterator value="#session.VALID_BOOK_LIST">
				<tr>
					<td width="50%">
						<s:property value="%{bookNumber}" />
					</td>
					
					<td width="50%">
						<font color="red"><s:property value="%{status}" /> </font>
					</td>
				</tr>
			</s:iterator>
		</table>
     </s:if>
		<table width="75%">
			<tr>
				<td width="50%"></td>
			</tr>
		</table>

		<table width="75%">
			<tr>
				
				<td width="30%" align="left">
					<a href="agt_im_saleReturn_Menu.action">More Sale Return</a>
				</td>
				<s:if test="%{#session.showCreditNote ==true}">
					  <td width="30%" align="right"><a theme="simple" target="_blank" href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action">Show Credit Note</a></td>
			</s:if> 
					  <td align="right"><a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/agt_rep_showDeliveryChallen.action?id=<%=session.getAttribute("DEL_CHALLAN_ID") %>&type=DSRCHALLAN" target="_blank">Show Sale Return Note</a></td>
			</tr>
		</table>
</div></div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_saleReturn_Success.jsp,v $
$Revision: 1.2 $
</code>