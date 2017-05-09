<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"	type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/orderMgmt/agent/js/agt_orderMgmt_agentOrder.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
	</head>
	<s:head theme="ajax" />

	<body>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Order Request for <s:property value="#application.TIER_MAP.RETAILER" />
				</h3>
				<s:form name="agentOrderForm" action="agt_om_selfIniOrderRet_Search" onsubmit="return validateSearchData();">
					<table border="0" cellpadding="5" cellspacing="0"
						bordercolor="#CCCCCC">

						<tr>
							<td colspan="2" align="left">
								<span id="characters"> </span>
								<br />
							</td>
						</tr>
					</table>
					<table border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">

						<tr>
							<td >
								<div id="retOrgDiv"></div>
								<s:if test="%{cartEmpty == 'No'}">

									<s:textfield name="retOrgName" label="%{#application.TIER_MAP.RETAILER} Name"
										key="#session.{RET_ORG_NAME}" readonly="true"></s:textfield>

								</s:if>
								<s:else>
									<s:select label="%{#application.TIER_MAP.RETAILER} Name" name="retOrgName" id="retOrgNameId"
										headerKey="-1" headerValue="--Please Select--"
										list="#session.RET_ORG_LIST.{orgName}" value="%{retOrgName}"
										onchange="verifyRetailer(this.value,'retOrgNameId','gameNbrId'),retrieveURL('agt_om_selfIniOrderRet_FetchRetCrDetail.action?retOrgName=','characters')"
										cssClass="option" />

								</s:else>
							</td>
						</tr>
					</table>
					
						<%
							NumberFormat format = NumberFormat.getInstance();
								format.setMaximumFractionDigits(2);

								String totalAmountFinal = null;
								String totalAmount = null;
								double dobl = 0.0;
						%>
						<s:if test="%{cartEmpty == 'No'}">

							<br />
							Credit Limit:<b><s:set name="strCreditLimit" value="%{#session.CREDIT_LIMIT}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditLimit")) %>
							</b>
											
							Current Credit Amount:<b><s:set name="strCreditAmt" value="%{#session.CREDIT_AMT}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditAmt")) %>
							</b>

	<br />
						<br />

							<table border="1" cellpadding="2" cellspacing="0" width="400" bordercolor="#CCCCCC">

								<tr>
									<th colspan="3" align="center">
										Cart Detail
									</th>
								</tr>
								<tr>
									<th>
										Game Name
									</th>
									<th>
										Quantity
									</th>
									<th>
										Amount
									</th>
								</tr>
								<s:set name="total" value="0.0" />
								<s:iterator id="Cart" value="#session.CART">
									<tr>

										<td>
											<s:property value="gameName" />
										</td>
										<td>
											<s:property value="orderedQty" />
										</td>
										<s:set name="total2"
											value="%{ticketPrice * orderedQty * ticketsPerBook}" />
										<td>
											<%
												dobl = (Double) pageContext.getAttribute("total2");
															totalAmount = format.format(dobl);
											%>
											<%=totalAmount%>

										</td>
							
									<!--
										<td>
											<s:property value="ticketPrice * orderedQty * ticketsPerBook" />
										</td>
 										-->
										<s:set name="total2"
											value="%{ticketPrice * orderedQty * ticketsPerBook}" />
										<s:set name="total" value="#total+#total2" />
									</tr>
								</s:iterator>
								<%
									NumberFormat formatTotal = NumberFormat.getInstance();
											formatTotal.setMaximumFractionDigits(2);
											dobl = (Double) pageContext.getAttribute("total");
											totalAmountFinal = formatTotal.format(dobl);
								%>
								<tr>
									<td colspan=2>
										Total Amount
									</td>
									<td><%=totalAmountFinal%></td>
								</tr>
								<tr>
									<td colspan="3" align="center">
										<s:submit theme="simple" action="agt_om_selfIniOrderRet_ProcessCart"
											cssClass="button" value="Process Cart" />

										<s:submit theme="simple" action="agt_om_selfIniOrderRet_CancelCart"
											cssClass="button" value="Cancel" />
									</td>
								</tr>

							</table>

<br />

						</s:if>

						<table border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">

							<tr>
								<td>
									<table cellpadding="2" cellspacing="0" border="0">
										<tr>
											<th align="center">
												Search Game
											</th>
										</tr>

										<tr>
											<td align="left">
												<div id="gameNumDiv"></div>
												Game Number:
												<s:textfield label="Game Number" name="gameNbr" id="gameNbrId"
													theme="simple"></s:textfield>
											</td>

										</tr>

										<tr>
											<td align="left">
												&nbsp;&nbsp;&nbsp;Game Name:
												<s:textfield label="Game Name" name="gameName"
													theme="simple"></s:textfield>
											</td>
										</tr>

										<tr>

											<td colspan="3">
												<div id="ticketPriceDiv"></div>
												&nbsp;&nbsp;&nbsp;&nbsp;Ticket Price:
												<s:select theme="simple" headerKey="1" value="'Equal To'"
													name="priceMatch" label="Price"
													list="{'Less Than Equal To','Greater Than Equal To','Equal To'}"
													cssClass="option"></s:select>
												<s:textfield theme="simple" name="ticketPrice">
												</s:textfield>

											</td>
										</tr>

										<tr>
											<td colspan="3">

												<s:div theme="ajax" href="agt_om_selfIniOrderRet_Date.action" >
												</s:div>
											</td>

										</tr>
										<tr>
											<td>

<s:submit theme="ajax" name="search" value="Search" cssClass="button" targets="resultSearch" />

											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>

					<div id="result">
	
						
			</div>
				</s:form>
			</div>
		</div>
			 				<s:div theme="ajax" id="resultSearch">
				</s:div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_selfIniOrderRet_Menu.jsp,v $
$Revision: 1.2 $
</code>