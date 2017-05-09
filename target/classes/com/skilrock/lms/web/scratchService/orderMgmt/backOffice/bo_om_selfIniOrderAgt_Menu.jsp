<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@page import="java.text.NumberFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">


	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>


		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
		<s:head theme="ajax" debug="false" />

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/orderMgmt/backOffice/js/boOrder.js"></script>
		<script>
				
	function verifyAgent(agtName)
{
  
  if(document.getElementById('agtOrgName').value!="-1")
  {
  var returnType=confirm("You have selected "+tierMap['AGENT']+":: " + agtName)
  if(returnType)    
    {     
     return true;
    }
   else
   {
       document.getElementById('agtOrgName').selectedIndex=0
       document.getElementById('agtOrgName').focus();
       return false;
   }
  }  
} 	
		
		</script>

	</head>
	<body
		<s:if test="%{cartEmpty != 'No'}">
			onload="onLoadAjax('getListAjax.action?listType=agent','agtOrgName')" </s:if>>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					Order Request for <s:property value="#application.TIER_MAP.AGENT" />
				</h3>


				<s:form name="boOrderForm" action="bo_om_selfIniOrderAgt_Search"
					onsubmit="return validateSearchData();">
					<table border="0" cellpadding="2" cellspacing="0">

						<tr>
							<td colspan="2" align="center">
								<span id="characters"> </span>
								<br />
							</td>
						</tr>
					</table>

					<table border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td>
								<div id="agtOrgDiv"></div>

								<s:if test="%{cartEmpty == 'No'}">

									<s:textfield name="agtOrgName" label="%{#application.TIER_MAP.AGENT} Name"
										key="#session.{AGENT_ORG_NAME}" readonly="true"></s:textfield>

								</s:if>
								<s:else>
									<s:select label="%{#application.TIER_MAP.AGENT} Name" id="agtOrgName" name="agtOrgName"
										headerKey="-1" headerValue="--Please Select--"
										list="#session.AGT_ORG_LIST.{orgName}"
										onchange="return verifyAgent(this.value),retrieveURL('bo_om_selfIniOrderAgt_FetchAgtCrDetail.action?agtOrgName=','characters')"
										cssClass="option" />
								</s:else>
							</td>
						</tr>
					</table>

					<s:if test="%{cartEmpty == 'No'}">

						<s:set name="creditLimitStr" value="%{#session.CREDIT_LIMIT}"></s:set>
						<s:set name="creditAmtStr" value="%{#session.CREDIT_AMT}"></s:set>

						<br />
							Credit Limit:<b> <%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("creditLimitStr"))%> </b>
											
							Current Credit Amount:<b><%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("creditAmtStr"))%> </b>


						<br />
						<br />
						<table border="1" cellpadding="2" cellspacing="0" width="400"
							bordercolor="#CCCCCC">

							<tr>
								<td colspan="7" align="center">
									<b>Cart Detail</b>
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
							<s:set name="total" value="0.0" />
							<%
								String totalAmountFinal = null;
										String totalAmount = null;
										double dobl = 0.0;
							%>
							<s:iterator id="Cart" value="#session.CART">
								<tr>
									<s:set name="total2"
										value="%{ticketPrice * orderedQty * ticketsPerBook}" />
									<td>
										<s:property value="gameName" />
									</td>
									<td>
										<s:property value="orderedQty" />
									</td>
									<%
										NumberFormat format = NumberFormat.getInstance();
													format.setMaximumFractionDigits(2);
													dobl = (Double) pageContext.getAttribute("total2");
													totalAmount = format.format(dobl);
									%>
									<td>
										<%=totalAmount%></td>

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
									<s:submit theme="simple"
										action="bo_om_selfIniOrderAgt_ProcessCart"
										value="Process Cart" cssClass="button" />
									<s:submit theme="simple"
										action="bo_om_selfIniOrderAgt_CancelCart" value="Cancel"
										cssClass="button" />
								</td>

							</tr>

						</table>
						<br />


					</s:if>

					<table border="1" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC">
						<tr>
							<td>
								<table border="0" cellpadding="2" cellspacing="0"
									bordercolor="#CCCCCC">
									<tr>
										<th>
											Search Game
										</th>
									</tr>
									<tr>
										<td align="left">
											<div id="gameNumDiv"></div>
											Game Number:
											<s:textfield label="Game Number" name="gameNbr"
												theme="simple"></s:textfield>
										</td>
									</tr>




									<tr>
										<td align="left">
											&nbsp;&nbsp;&nbsp;Game Name:
											<s:textfield label="Game Name" name="gameName" theme="simple"></s:textfield>
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
										<td>
											<s:div theme="ajax" href="bo_om_selfIniOrderAgt_Date.action">
											</s:div>
										</td>
									</tr>
									<tr>
										<td align="center">
											<s:submit theme="ajax" name="search" value="Search"
												targets="resultSearch" cssClass="button" />
										</td>
									</tr>

								</table>
							</td>
						</tr>
					</table>

				</s:form>
			</div>
		</div>
		<s:div theme="ajax" id="resultSearch">
		</s:div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_om_selfIniOrderAgt_Menu.jsp,v $
$Revision: 1.2 $
</code>