<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	
	<s:actionerror/>
	<div id="wrap">

 	<div id="top_form">
  	<h3>Temporary <s:text name="PWT"/> Receipt</h3>
	<s:form name="tempPwtReceiptSuccess">
		<tr>
			<td><%double total =0.0; %>
				<table border="1" width="100%" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
						<tr>
							<s:textfield label="%{#application.TIER_MAP.AGENT} Name" id="agtOrgName" name="agtOrgName" value="%{#session.tmpPwtReceiptSubmit.agtOrgName}" size="24" readonly="true"></s:textfield>
						</tr>
						<tr>
							<s:textfield label="Temporary Receipt No." id="receiptNum" name="receiptNum" value="%{#session.tmpPwtReceiptSubmit.receiptNum}" size="24" readonly="true"></s:textfield>
						</tr>
						<tr>
							<td colspan="2">
							<s:set name="total" value="0"></s:set>
							 <s:iterator id="gameMap" value="#session.tmpPwtReceiptSubmit.tmpReceiptMap" status="gameHashMap">
							 <table cellpadding="3" width="100%" cellspacing="0" border="1" bordercolor="#CCCCCC">
									<tr>
										<td> Game Number :<s:property value="key"/></td>
									</tr>
									<tr>
										<td>
											<table border="1" width="100%" cellpadding="3" cellspacing="0" bordercolor="#6E6E6E">
									
									<s:iterator id="innerMap" value="value" status="innerHashMap">
											<s:if test="#innerHashMap.index==0">
											<tr><td><b>S.No</td><td><b>PRIZE AMT(<s:property  value="%{#application.currencyWord}" />)</td><td><b>NO. OF TICKET</td><td><b>AMOUNT(<s:property  value="%{#application.currencyWord}" />)</td></tr>
											</s:if>
											<tr>
												<td><b><s:property value="#innerHashMap.index+1"/></td>
												<td><b><s:set name="key" value="key"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("key"))%></td>
												<td><b><s:property value="value"/></td>
												<td><b><s:set name="totalInner" value="%{(key*value)}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalInner"))%></td>
												
											</tr>
											<%total = total+(Double)pageContext.getAttribute("totalInner"); %>
									</s:iterator>
									</table>
										</td>
									</tr>
									
								</table>
							 
							 
							 
							 </s:iterator>
							 <table cellpadding="3" width="100%" cellspacing="0" border="1" bordercolor="#CCCCCC">
									<tr>
										<td align="right"><b>Total Amount :&nbsp;&nbsp;&nbsp;<%=FormatNumber.formatNumberForJSP(total) %></b>
										</td>
									</tr>
									
								</table>
							</td>
						</tr>
						
				</table>		
						</td>
					</tr>	
					<tr>
			   <td>
			      <a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action" target="_blank">Show Temp PWT Receipt</a>
			  </td>
			</tr>
		</s:form>
		</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkAcceptance_Success.jsp,v $
$Revision: 1.2 $
</code>