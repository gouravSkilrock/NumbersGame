<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">


				<table width="1000px" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<s:if test="#session.DRAW_ANALYSIS_LIST_RETAILERWISE !=null && #session.DRAW_ANALYSIS_LIST_RETAILERWISE.size>0">
					<tr>
						<th colspan="7" align="center">
							<s:text name="label.ret.wise.data.last.draw" />
						</th>
					</tr>
					<tr>
						<th colspan="1"></th>
						<th colspan="2" align="center">
							<s:text name="SALE" />
						</th>
						<th colspan="2" align="center">
							<s:text name="WINNING" />
						</th>
					</tr>
					<tr>
						<th>
							<s:text name="label.ret.name" />
						</th>
						<th>
							<s:text name="label.sale.amt" />
						</th>
						<th>
							<s:text name="label.sold.tkt.count" />
						</th>
						<th>
							<s:text name="label.win.amt" />
						</th>
						<th>
							<s:text name="label.win.tkt.count" />
						</th>
						<th>
							<s:text name="label.prize.payout.ratio" />
						</th>
					</tr>

					
						<s:iterator id="beanCart"
							value="#session.DRAW_ANALYSIS_LIST_RETAILERWISE">
							<s:if test="%{(retailerName != 'Total')}">
								<tr class="startSortable">
									<td align="left">
										<s:property value="retailerName" />
									</td>
									<td align="left">
									<s:set name="saleSet" value="saleAmount"></s:set>
									<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleSet"))%>
									</td>
									<td align="left">
										<s:property value="saleCount" />
									</td>
									<td align="left">
									<s:set name="pwtSet" value="pwtAmount"></s:set>
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtSet"))%>
									</td>
									<td align="left">
										<s:property value="pwtCount" />
									</td>
									<td align="left">
									<s:set name="prizePayoutSet" value="prizePayoutRatio"></s:set>
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayoutSet"))%>
									</td>
								</tr>
							</s:if>

							<s:else>
								<tr>
									<th align="left">
										<s:property value="retailerName" />
									</th>
									<th align="left">
									<s:set name="saleTotSet" value="totalSaleAmount"></s:set>
									<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleTotSet"))%>
									</th>
									<th align="left">
										<s:property value="totalSaleCount" />
									</th>
									<th align="left">
									<s:set name="pwtTotSet" value="totalPwtAmount"></s:set>
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtTotSet"))%>
									</th>
									<th align="left">
										<s:property value="totalPwtCount" />
									</th>
									<th align="left">
									<s:set name="prizePayoutTotSet" value="totalPprAmount"></s:set>
										<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("prizePayoutTotSet"))%>
									</th>
								</tr>
							</s:else>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:if test="%{message==null}">
				<s:text name="msg.no.record.process" />
				</s:if><s:else>
				<s:property value="message"/>
				</s:else>
							</td>
						</tr>
					</s:else>
				</table>
			</div>
		</div>



	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_draw_analysisReportRetailerWise_Result.jsp,v $
	$Revision: 1.3 $
</code>