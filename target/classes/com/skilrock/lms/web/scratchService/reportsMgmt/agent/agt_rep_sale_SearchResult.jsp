
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax" />

	<body onload="disable()">

		<br />


		<s:if test="#session.saletype!=null">
			<div align="center">
				<h3>
					<u>Sale <s:text name="Report" /> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %> From : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
						&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%> <%
						} else {
							out.println("Of : "
									+ ((DateBeans) session.getAttribute("datebean"))
											.getReportType());
						}
					%> </u>
				</h3>
			</div>
			<s:if
				test="#session.searchResultBO.size>0 || #session.purchased!=null">
				<s:div id="excel"
					cssStyle="text-align: left;width: 100%;font-size: 9pt">
					<s:a href="agt_rep_sale_ExpExcel.action ">Export As Excel</s:a>
				</s:div>
			</s:if>

			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<th colspan="6" align="center">
						<b>Purchased Details From BO</b>
					</th>
				</tr>
				<tr>
					<th colspan="3">
						MRP Sale Details
					</th>
					<th colspan="3">
						Deducted Sale Details
					</th>
				</tr>
				<tr>
					<th>
						Purchased Amount
					</th>
					<th>
						Purchased Return Amount
					</th>
					<th>
						Net Purchased Amount
					</th>
					<th>
						Purchased Amount
					</th>
					<th>
						Purchased Return Amount
					</th>
					<th>
						Net Purchased Amount
					</th>

				</tr>

				<s:if test="#session.purchased!=null">
					<tr class="startSortable">
						<td align="right">
							<s:property value="#session.purchased.saleMrp" />
						</td>
						<td align="right">
							<s:property value="#session.purchased.saleReturnMrp" />
						</td>
						<td align="right">
							<s:property value="#session.purchased.netMrpAmt" />
						</td>
						<td align="right">
							<s:property value="#session.purchased.saleAmt" />
						</td>
						<td align="right">
							<s:property value="#session.purchased.returnAmt" />
						</td>
						<td align="right">
							<s:property value="#session.purchased.netAmt" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td colspan="7" align="center">
							No Transaction With BO
						</td>
					</tr>
				</s:else>
			</table>

			<br>
			<br>
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<th colspan="7" align="center">
						<b>Sale Details With <s:property
								value="#application.TIER_MAP.RETAILER" /> </b>
					</th>

				</tr>
				<tr>
					<th>
						&nbsp;
					</th>
					<th colspan="3">
						MRP Sale Details
					</th>
					<th colspan="3">
						Deducted Sale Details
					</th>
				</tr>
				<tr>
					<th>
						Party Name
					</th>
					<th>
						Sale Amount
					</th>
					<th>
						Sale Return Amount
					</th>
					<th>
						Net Sale Amount
					</th>
					<th>
						Sale Amount
					</th>
					<th>
						Sale Return Amount
					</th>
					<th>
						Net Sale Amount
					</th>

				</tr>

				<s:if test="#session.searchResultBO.size>0">
					<s:iterator id="beanCart" value="#session.searchResultBO">
						<tr class="startSortable">
							<td align="left">
								<s:property value="name" />
							</td>
							<td align="right">
								<s:property value="saleMrp" />
							</td>
							<td align="right">
								<s:property value="saleReturnMrp" />
							</td>
							<td align="right">
								<s:property value="netMrpAmt" />
							</td>
							<td align="right">
								<s:property value="saleAmt" />
							</td>
							<td align="right">
								<s:property value="returnAmt" />
							</td>
							<td align="right">
								<s:property value="netAmt" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="7" align="center">
							No Transaction With
							<s:property value="#application.TIER_MAP.RETAILER" />
						</td>
					</tr>
				</s:else>

			</table>
			<s:div id="naviga" cssStyle="text-align: center;width: 100%">
				<s:if test=" #session.APP_ORDER_LIST5.size >5 ">

					<s:if test="#session.startValueOrderSearch!=0">
						<s:a theme="ajax" targets="down"
							href="agt_rep_sale_Navigate.action?end=first">First</s:a>
						<s:a theme="ajax" targets="down"
							href="agt_rep_sale_Navigate.action?end=Previous"> Previous</s:a>
					</s:if>
					<s:else>First Previous</s:else>
					<s:if
						test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if>
					<s:else>
						<s:a theme="ajax" targets="down"
							href="agt_rep_sale_Navigate.action?end=Next">Next</s:a>
						<s:a theme="ajax" targets="down"
							href="agt_rep_sale_Navigate.action?end=last">Last</s:a>
					</s:else>

				</s:if>
				<!-- 
		<s:if test="#session.searchResultBO.size>0">
			<s:div id="excel" cssStyle="text-align: left;font-size: 9pt"><s:a href="exportExcelForBO.action">Export As Excel</s:a></s:div>
		</s:if>
		 -->
			</s:div>
		</s:if>

		<s:else>

			<s:if test="%{#session.isExpand}">
				<div align="center">
					<h3>
						<u>For Details Please Choose start date after <s:property
								value="%{lastDate}" /> </u>
					</h3>
				</div>
			</s:if>
			<s:else>
				<div align="center">
					<h3>
						<u>Sale <s:text name="Report" /> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %> From : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
							&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%> <%
						} else {
							out.println("Of : "
									+ ((DateBeans) session.getAttribute("datebean"))
											.getReportType());
						}
					%> </u>
					</h3>
				</div>
				<s:if test="#session.searchResultBO.size>0">
					<s:div id="excel"
						cssStyle="text-align: left;width: 100%;font-size: 9pt">
						<s:a href="agt_rep_sale_ExpExcel.action">Export As Excel</s:a>
					</s:div>
				</s:if>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>

						<th width="12%" rowspan="2" nowrap="nowrap">
							Game Name
						</th>
						<th width="6%" rowspan="2" nowrap="nowrap">
							Cost/Ticket
						</th>
						<th width="8%" rowspan="2">
							Tickets/Book
						</th>
						<th width="8%" rowspan="2">
							Existing Books
						</th>
						<th width="20%" colspan="2">
							From BO
						</th>
						<th width="20%" colspan="2">
							To
							<s:property value="#application.TIER_MAP.RETAILER" />
						</th>
						<th width="10%" rowspan="2">
							Books Remaining
						</th>
						
						<th width="20%" colspan="2">
							From BO
						</th>
						<th width="20%" colspan="2">
							To
							<s:property value="#application.TIER_MAP.RETAILER" />
						</th>
					</tr>
					<tr>
						<th width="10%">
							Books Purchased
						</th>
						<th width="10%">
							Books Returned
						</th>
						<th width="10%">
							Books Sold
						</th>
						<th width="10%">
							Books Returned
						</th>
						
						<th width="10%">
							Tickets Purchased
						</th>
						<th width="10%">
							Tickets Returned
						</th>
						<th width="10%">
							Tickets Sold
						</th>
						<th width="10%">
							Tickets Returned
						</th>
						
						
						
					</tr>
					
					
					

					<s:if test="#session.searchResultBO.size>0">
						<s:iterator id="beanCart" value="#session.searchResultBO">
							<tr>
								<td width="12%" align="left" nowrap="nowrap">
									<s:property value="gamename" />
								</td>
								<td width="6%" align="right">
									<s:property value="ticketCost" />
								</td>
								<td width="8%" align="right">
									<s:property value="ticketsPerBook" />
								</td>
								<td width="8%" align="right">
									<s:property value="existingBooks" />
								</td>
								<td width="10%" align="right">
									<s:property value="sale" />
								</td>
								<td width="10%" align="right">
									<s:property value="salereturn" />
								</td>
								<td width="10%" align="right">
									<s:property value="saleToRetailer" />
								</td>
								<td width="10%" align="right">
									<s:property value="saleReturnByRetailer" />
								</td>
								<th width="10%" align="right">
									<s:property value="agentsRemBooks" />
								</th>
								<td width="10%" align="right">
									<s:property value="looseSale" />
								</td>
								<td width="10%" align="right">
									<s:property value="looseSaleReturn" />
								</td>
								<td width="10%" align="right">
									<s:property value="looseSaleToRetailer" />
								</td>
								<td width="10%" align="right">
									<s:property value="looseSaleReturnByRetailer" />
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="9" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>

				</table>
				<s:div id="naviga" cssStyle="text-align: center;width: 100%">
					<s:if test=" #session.APP_ORDER_LIST5.size >5 ">

						<s:if test="#session.startValueOrderSearch!=0">
							<s:a theme="ajax" targets="down"
								href="agt_rep_sale_Navigate.action?end=first">First</s:a>
							<s:a theme="ajax" targets="down"
								href="agt_rep_sale_Navigate.action?end=Previous"> Previous</s:a>
						</s:if>
						<s:else>First Previous</s:else>
						<s:if
							test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/10)*10)">Next Last</s:if>
						<s:else>
							<s:a theme="ajax" targets="down"
								href="agt_rep_sale_Navigate.action?end=Next">Next</s:a>
							<s:a theme="ajax" targets="down"
								href="agt_rep_sale_Navigate.action?end=last">Last</s:a>
						</s:else>

					</s:if>
					<!-- 
		<s:if test="#session.searchResultBO.size>0">
			<s:div id="excel" cssStyle="text-align: left;font-size: 9pt"><s:a href="exportExcelForBO.action">Export As Excel</s:a></s:div>
		</s:if>
		 -->
				</s:div>
			</s:else>
		</s:else>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_rep_sale_SearchResult.jsp,v $ $Revision: 1.2 $
</code>