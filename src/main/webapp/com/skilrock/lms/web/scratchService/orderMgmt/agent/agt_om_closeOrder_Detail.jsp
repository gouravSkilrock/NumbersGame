<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<head>

	<script>
	function Confirm()
	{
	if(document.StatusClose.orderStatus.value=="Close"){
	var confirmValue = confirm("You are going to Close this Order.Are you sure?");
   	 if( confirmValue ){
   	 return true;
   	 }else{
   	 return false;
   	}
	}
	}
	</script>
</head>
	<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>


<div id="wrap">

  <div id="top_form">
  
	<h3>Change Order Status</h3>
	<s:actionerror/>

<s:form name="StatusClose" id="a" >
						<table width="87%" border="1" align="center" cellpadding="5"
							cellspacing="0" bordercolor="#CCCCCC">
							<tr>
								<td>
									<table width="100%">
										<tr>
											<th>
												Order Number
											</th>
											<td>
												<s:property value="#session.AGT_ORDER_ID" />
												<s:hidden name="orderId" value="%{#session.AGT_ORDER_ID}" />
											</td>
											<th>
												Order Date
											</th>
											<td>
												<s:property value="#session.date" />
											</td>
										</tr>
										<tr>
											<th>
												<s:property value="#application.TIER_MAP.RETAILER" />
												Name
											</th>
											<td>
												<s:property value="#session.RET_ORG_NAME" />
											</td>
											<th>
												Address
											</th>
											<td>
												<s:property value="#session.RET_ORG_ADDR.addrLine1" />
											</td>
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<s:property value="#session.RET_ORG_ADDR.addrLine2" />
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<s:property value="#session.RET_ORG_ADDR.city" />
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<s:property value="#session.RET_ORG_ADDR.state" />
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td>
												<s:property value="#session.RET_ORG_ADDR.country" />
										</tr>
										<tr>
											<th>
												Credit Limit :
											</th>
											<td>
												<s:set name="strCreditLimit"
													value="%{#session.CREDIT_LIMIT}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("strCreditLimit"))%></td>
											<th>
												Current Credit Amount:
											</th>
											<td>
												<s:set name="strCreditAmt" value="%{#session.CREDIT_AMT}" /><%=FormatNumber.formatNumberForJSP(pageContext
						.getAttribute("strCreditAmt"))%></td>
										</tr>
										<tr>
											<td>
												<s:text name="Change Order Status" />
											</td>
											<td>
												<s:select theme="simple" name="orderStatus" list="{'Close'}"
													value="Close" cssClass="option"></s:select>
											</td>
										</tr>
										<tr></tr>
										<tr>
											<td>
												<s:submit theme="simple" value="Change Status"
													action="agt_om_closeOrder_Save" formId="a"
													onclick="return Confirm()" cssClass="button" />
											</td>
											<td>
												<s:submit theme="simple" value="Cancel"
													action="agt_om_closeOrder_Menu" formId="a"
													cssClass="button" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<p>
								&nbsp;
							</p>
						</table>
						<p>&nbsp;</p>
	</s:form></div></div>		 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_closeOrder_Detail.jsp,v $
$Revision: 1.3 $
</code>