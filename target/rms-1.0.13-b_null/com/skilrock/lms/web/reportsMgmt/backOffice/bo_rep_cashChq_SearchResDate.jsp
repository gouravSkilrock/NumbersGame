
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
		<u><s:text name="msg.detail.pls.choose.st.date"/>
			 <s:property value="%{lastDate}" />
			
		</u>
	</h3>
</div>
</s:if>
<s:else>
		<div align="center">
			<h3>
				<s:if test="%{reportType=='Daywise'}">
					<u><s:text name="label.cash.chq.rep.of"/> <s:property value="%{end_Date}" />
					</u>
				</s:if>
				<s:else>
					<u><s:text name="label.cash.chq.rep.of"/> <s:property value="%{orgName}" />
						&nbsp; <%
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
				</s:else>

			</h3>
		</div>
		
		<p>
		<table width="90%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<s:if test="%{reportType=='Daywise'}">
					<th align="center">
						<s:property value="#application.TIER_MAP.AGENT" />
						<s:text name="label.name"/>
					</th>
				</s:if>
				<s:else>
					<th align="center">
						<s:text name="label.date"/>
					</th>
				</s:else>

				<th align="center">
					<s:text name="label.voucher.no"/>.
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
					<s:text name="label.bnk.name"/>
				</th>
				<th align="center">
					<s:text name="label.net.amt"/>
				</th>
			</tr>

			<s:if test="#session.searchCashChqResultDate.size>0">
				<%
					double totCash = 0.0;
						double totChq = 0.0;
						double totChqBounce = 0.0;
						double totCrNote = 0.0;
						double totDrNote = 0.0;
						double totNet = 0.0;
						double net = 0.0;
						double totBankDep = 0.0;
				%>
				<s:iterator id="beanCart" value="#session.searchCashChqResultDate">
					<s:set name="payAmount" value="paymentAmount" />
					<%
						String temp = FormatNumber
										.formatNumber((Double) pageContext
												.getAttribute("payAmount"));
					%>
					<tr>
						<td align="left">
							<s:property value="date" />
						</td>
						<td align="center">
							<s:property value="voucherNo" />
						</td>
						<td align="right">
							<s:if test="%{paymentType=='CASH'}">
								<%
									totCash = totCash
														+ (Double) pageContext
																.getAttribute("payAmount");
								%>
								<%=temp%>
							</s:if>
							<s:else>-</s:else>
						</td>
						<td align="right">
							<s:if test="%{paymentType=='CHEQUE'}">
								<%=temp%>
								<%
									totChq = totChq
														+ (Double) pageContext
																.getAttribute("payAmount");
								%>
							</s:if>
							<s:else>-</s:else>
						</td>
						<td align="right">
							<s:if test="%{paymentType=='CHQ_BOUNCE'}">
								<%=temp%>
								<%
									totChqBounce = totChqBounce
														+ (Double) pageContext
																.getAttribute("payAmount");
												totNet = totNet
														- (Double) pageContext
																.getAttribute("payAmount") * 2;
								%>
							</s:if>
							<s:else>-</s:else>
						</td>

						<td align="right">
							<s:if test="%{paymentType=='CREDIT NOTE'}">
								<%=temp%>
								<%
									totCrNote = totCrNote
														+ (Double) pageContext
																.getAttribute("payAmount");
								%>
							</s:if>
							<s:else>-</s:else>
						</td>
						<td align="right">
							<s:if test="%{paymentType=='DEBIT NOTE'}">
								<%=temp%>
								<%
									totDrNote = totDrNote
														+ (Double) pageContext
																.getAttribute("payAmount");
												totNet = totNet
														- (Double) pageContext
																.getAttribute("payAmount") * 2;
								%>
							</s:if>
							<s:else>-</s:else>
						</td>
						<td align="right">
							<s:if test="%{paymentType=='BANK DEPOSIT'}">
								<%
									totBankDep = totBankDep
														+ (Double) pageContext
																.getAttribute("payAmount");
								%>
								<%=temp%>
							</s:if>
							<s:else>-</s:else>
						</td>
						<td align="center">
							<s:property value="bankName" />
						</td>
						<td align="right">
							<%=temp%>
							<%
								totNet = totNet
												+ (Double) pageContext.getAttribute("payAmount");
							%>
						</td>
					</tr>
				</s:iterator>
				<tr>
					<th>
						<s:text name="label.total"/>
					</th>
					<th>
						&nbsp;
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totCash)%>
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totChq)%>
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totChqBounce)%>
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totCrNote)%>
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totDrNote)%>
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totBankDep)%>
					</th>
					<th align="right">
						-
					</th>
					<th align="right">
						<%=FormatNumber.formatNumber(totNet)%>
					</th>
					
				</tr>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9" align="center">
						<s:text name="msg.no.record"/> 
					</td>
				</tr>
			</s:else>

		</table>
		</s:else>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_cashChq_SearchResDate.jsp,v $
$Revision: 1.3 $
</code>