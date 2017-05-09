<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.text.NumberFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/orderMgmt/agent/js/agt_orderMgmt_order.js"></script>

		<s:head theme="ajax" debug="false" />
		

	</head>

	<body>
	<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					Order Request
				</h3>
				
<%NumberFormat format = NumberFormat.getInstance();
														format.setMaximumFractionDigits(2);%>
				<s:form name="gameDetailsForm" action="agt_om_odrReqToBO_AddToCart" >
					<table border="0" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC">
						
						<tr align="left">
							<th>
								Credit Limit
							</th>
							<s:set name="strCreditLimit" value="%{#session.CREDIT_LIMIT}" />
							<td>
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditLimit")) %>
							</td>
						</tr>
						<tr align="left">
							<th>
								Current Credit Amount
							</th>
							<s:set name="strCreditAmount" value="%{#session.CREDIT_AMT}" />
							<td>
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strCreditAmount")) %>
							</td>
						</tr>
					</table>
<br/>
					<s:if test="%{cartEmpty == 'No'}">

						
						<table border="1" cellpadding="2" cellspacing="0"
							bordercolor="#CCCCCC">

							<tr>
								<td colspan="3" align="center">
									Cart Details
								</td>
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
							<%
								String totalAmountFinal = null;
										String totalAmount = null;
										double dobl = 0.0;
							%>
							<s:set name="total" value="0.0" />
							<s:iterator id="Cart" value="#session.CART">
								<s:set name="total2"
									value="%{ticketPrice * orderedQty * ticketsPerBook}" />
								<tr>

									<td>
										<s:property value="gameName" />
									</td>
									<td>
										<s:property value="orderedQty" />
									</td>
									<td>
										<%
														dobl = (Double) pageContext.getAttribute("total2");
														totalAmount = format.format(dobl);
														System.out.println("totalAmount--" + totalAmount);
										%>
										<%=totalAmount%>
									</td>
									<s:set name="total" value="#total+#total2" />
								</tr>
							</s:iterator>
							<%
										NumberFormat formatTotal = NumberFormat.getInstance();
										formatTotal.setMaximumFractionDigits(2);
										dobl = (Double) pageContext.getAttribute("total");
										totalAmountFinal = formatTotal.format(dobl);
										System.out.println("totalAmountFinal--" + totalAmountFinal);
							%>
							<tr>
								<td colspan="2">
									Total Amount
								</td>
								<td><%=totalAmountFinal%></td>
							</tr>
						</table>



					</s:if>

					<table width="100%" border="0" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC"><tr><td>
					<table width="100%" border="1" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC">

						<tr>
							<th colspan="4" align="center">
								Game Details
							</th>
						</tr>

						<tr>
							<th>
								Game Number
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.gameNbr" />
							</td>
							<th>
								Game Name
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.gameName" />
							</td>

						</tr>

						<tr>
							<th>
								Ticket Price
							</th>
							<s:set name="strticketPrice" value="#session.GAME_DETAILS_BEAN.ticketPrice" />
							<td>
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strticketPrice")) %>
							</td>
							<th>
								Game Start Date
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.startDate" />
							</td>
						</tr>
						<tr>
							<th>
								Sale End Date
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.saleEndDate" />
							</td>
							<th>
								Pwt End Date
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.pwtEndDate" />
							</td>
						</tr>

						<tr>
							<th>
								Tickets Per Book
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.ticketsPerBook" />
							</td>
							<th>
								Books Per Pack
							</th>
							<td>
								<s:property value="#session.GAME_DETAILS_BEAN.booksPerPack" />
							</td>
						</tr>

						<tr>
							<td colspan="4">
								<div id="booksDiv"></div>
							</td>
						</tr>

						<tr>
							<th colspan="2">
								Number of Books To Order
							</th>
							<td colspan="2">
								<s:textfield theme="simple" label="Number of Books To Order"
									name="quantity" maxlength="9" value="%{gameQty}"></s:textfield>
							</td>
						</tr>
						<s:hidden name="gameId" key="#session.GAME_DETAILS_BEAN.gameId" />
						</table>
						</td></tr>
						<tr>
							
							<td>
							
							<s:if test = "%{#session.USER_INFO_BEAN.userType !='AGENT'}">
							<table width="80%" border="1" cellpadding="2" cellspacing="0"
								bordercolor="#CCCCCC">

								<tr>
									<th>
										Prize Amount
									</th>
									<th>
										No of Prizes Remaining
									</th>
								</tr>
								<s:iterator value="prizeStatusList">
									<tr>
										<td>
											<s:property value="%{prizeAmt}" />
										</td>
										<td>
											<s:property value="%{nbrOfPrizeLeft}" />
										</td>
									</tr>
								</s:iterator>
							</table>
							</s:if>
							</td>
						</tr>


						<tr>
							<td align="center">
								<s:submit theme="simple" value="Add to Cart"
									 cssClass="button" onclick="return validateData()" onkeypress="return validateData()"/>
							
								<s:submit theme="simple" value="Cancel"
									action="agt_om_odrReqToBO_CancelReq" cssClass="button" />
							</td>
						</tr>
					</table>
					<p>
						&nbsp;
					</p>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_odrReqToBO_Detail.jsp,v $
$Revision: 1.3 $
</code>