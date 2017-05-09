<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<s:head theme="ajax" />

	<body onload="disable()">
		<s:if test="%{#session.isExpand}">
			<div align="center">
				<h3>
					<u><s:text name="msg.detail.pls.choose.st.date"/> <s:property
							value="%{lastDate}" /> </u>
				</h3>
			</div>
		</s:if>
		<s:else>
			<div align="center">
				<h3>
					<u><s:text name="label.cash.chq.rep.of"/> <s:property
							value="#session.username" /> &nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 				.getReportType())) {
 %> From : <%=GetDate.getConvertedDate(((DateBeans) session
							.getAttribute("datebean")).getStartDate())%>
						&nbsp;&nbsp;To&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
							.getAttribute("datebean")).getEndDate())%> <%
 	} else {
 %> <%="for : "
							+ ((DateBeans) session.getAttribute("datebean"))
									.getReportType()%> <%
 	}
 %> </u>
				</h3>
			</div>
			<p>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="center">
							<s:text name="label.party.name"/>
						</th>
						<th align="center">
							<s:text name="label.cash.colln"/>
						</th>
						<th align="center">
							<s:text name="label.chq.colln"/>
						</th>
						<th align="center">
							<s:text name="label.chq.bounce"/>
						</th>
						<th align="center">
							<s:text name="Credit_Note"/>
						</th>
						<th align="center">
							<s:text name="Debit_Note"/>
						</th>
						<th align="center">
							<s:text name="label.bank.deposit"/>
						</th>
						<th align="center">
							<s:text name="label.net.amt"/>
						</th>
					</tr>
					<%
						double totCash = 0.0;
							double totChq = 0.0;
							double totChqBounce = 0.0;
							double totCrNote = 0.0;
							double totDrNote = 0.0;
							double totNet = 0.0;
							double totBankDep = 0.0;
							double net = 0.0;
					%>
					<s:iterator id="beanCart" value="#session.cashChqResultUser">
						<tr class="startSortable">
							<td align="left">
								<s:property value="name" />
							</td>
							<td align="right">
								<s:set name="cash" value="totalCash" />
								<s:property value="totalCash" />
							</td>
							<td align="right">
								<s:set name="chq" value="totalChq" />
								<s:property value="totalChq" />
							</td>
							<td align="right">
								<s:set name="chqBounce" value="cheqBounce" />
								<s:property value="cheqBounce" />
							</td>
							<td align="right">
								<s:set name="crNote" value="credit" />
								<s:property value="credit" />
							</td>
							<td align="right">
								<s:set name="drNote" value="debit" />
								<s:property value="debit" />
							</td>
							<td align="right">
								<s:set name="bankDep" value="bankDeposit" />
								<s:property value="bankDeposit" />
							</td>
							<td align="right">
								<%
									net = Double.parseDouble((String) pageContext
													.getAttribute("cash"))
													+ Double.parseDouble((String) pageContext
															.getAttribute("chq"))
													- Double.parseDouble((String) pageContext
															.getAttribute("chqBounce"))
													+ Double.parseDouble((String) pageContext
															.getAttribute("crNote"))
													+ Double.parseDouble((String) pageContext
															.getAttribute("bankDep"))
													- Double.parseDouble((String) pageContext
															.getAttribute("drNote"));
											totNet = totNet + net;
								%>
								<%=FormatNumber.formatNumber(net)%>
							</td>
						</tr>
					</s:iterator>
					<s:iterator id="total" value="#session.cashChqResultUser">
						<s:set name="cash" value="totalCash" />
						<s:set name="chq" value="totalChq" />
						<s:set name="chqBounce" value="cheqBounce" />
						<s:set name="crNote" value="credit" />
						<s:set name="drNote" value="debit" />
						<s:set name="bankDep" value="bankDeposit" />
						<%
							totCash = totCash
											+ Double.parseDouble((String) pageContext
													.getAttribute("cash"));
									totChq = totChq
											+ Double.parseDouble((String) pageContext
													.getAttribute("chq"));
									totChqBounce = totChqBounce
											+ Double.parseDouble((String) pageContext
													.getAttribute("chqBounce"));
									totCrNote = totCrNote
											+ Double.parseDouble((String) pageContext
													.getAttribute("crNote"));
									totDrNote = totDrNote
											+ Double.parseDouble((String) pageContext
													.getAttribute("drNote"));
									totBankDep = totBankDep
											+ Double.parseDouble((String) pageContext
													.getAttribute("bankDep"));
						%>

					</s:iterator>
					<tr>
						<th>
							<s:text name="label.total"/>
						</th>
						<td align="right"><%=FormatNumber.formatNumber(totCash)%></td>
						<td align="right"><%=FormatNumber.formatNumber(totChq)%></td>
						<td align="right"><%=FormatNumber.formatNumber(totChqBounce)%></td>
						<td align="right"><%=FormatNumber.formatNumber(totCrNote)%></td>
						<td align="right"><%=FormatNumber.formatNumber(totDrNote)%></td>
						<td align="right"><%=FormatNumber.formatNumber(totBankDep)%></td>
						<td align="right"><%=FormatNumber.formatNumber(totNet)%></td>
					</tr>
				</table>
		</s:else>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_cashChq_SearchResUser.jsp,v $ $Revision: 1.3 $
</code>