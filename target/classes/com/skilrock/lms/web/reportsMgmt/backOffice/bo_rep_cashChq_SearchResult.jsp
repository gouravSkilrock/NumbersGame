<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<s:head theme="ajax" />

	<body onload="disable()">

		<div align="center">
			<h2>
				<u><s:text name="label.this.consolidate.data.for.all.user" /> </u>
			</h2>

			<div align="center">
				<h3>
					<u><s:text name="label.cash.chq" /> <s:text name="Report" />
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
				</h3>
				<s:if test="%{#application.COUNTRY_DEPLOYED!='INDIA'}">
				<s:a
					href="bo_rep_cashChq_ExpExcel_claimed_tickets.action?cashierType=ALL&start_date=%{start_date}&end_Date=%{end_Date}&request_locale=%{#session.WW_TRANS_I18N_LOCALE}"
					onclick=" return checkDetails()">
					<b><u><s:text name="label.export.pwt.as.excel" /> </u> </b>
				</s:a>
				</s:if>
			</div>
			<p>
				<s:set name="dataBeanList" value="list1" />
				<s:if test="#dataBeanList.size>0">
					<s:div id="excel"
						cssStyle="text-align:center;width: 100%;font-size: 9pt">
						<s:a href="bo_rep_cashChq_ExpExcel.action?stateName=%{stateName}&cityName=%{cityName}">
							<s:text name="btn.exportasexcel" />
						</s:a>
					</s:div>
				</s:if>

				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">

					<tr>
					<s:if test="%{reportType=='Agentwise'}">
						<th align="center" colspan="9">
							<s:property value="#session.orgName" />
						</th>
						</s:if>
					<s:elseif test="%{reportType=='Userwise'}">
						<th align="center" colspan="8">
							<s:property value="#session.orgName" />
						</th>
						</s:elseif>
						<s:else><th align="center" colspan="7">
							<s:property value="#session.orgName" />
						</th></s:else>
						<th align="center">
							<s:text name="label.amt.in" />
							<s:property value="#application.CURRENCY_SYMBOL" />
						</th>
					</tr>
					<tr>
					<s:if test="%{reportType=='Userwise'}">
						<td align="center" colspan="11">
							<s:property value="#session.orgAdd" />
						</td>
						</s:if>
						<s:else><td align="center" colspan="10">
							<s:property value="#session.orgAdd" />
						</td></s:else>
					</tr>
					<tr>
						<s:if test="%{reportType=='Agentwise'}">
							<th align="center">
								<s:text name="label.party.name" />
							</th>
							<th align="center">
								<s:text name="label.state" />
							</th>
							<th align="center">
								<s:text name="label.city" />
							</th>
						</s:if>
						<s:elseif test="%{reportType=='Daywise'}">
							<th align="center">
								<s:text name="label.date" />
							</th>
						</s:elseif>
						<s:else>
							<th align="center">
								<s:text name="label.party.name" />
							</th>
						</s:else>

						<th align="center">
							<s:text name="label.cash.colln" />
						</th>
						<th align="center">
							<s:text name="label.chq.colln" />
						</th>
						<th align="center">
							<s:text name="label.chq.bounce" />
						</th>
						<th align="center">
							<s:text name="Credit_Note" />
						</th>
						<th align="center">
							<s:text name="Debit_Note" />
						</th>
						<th align="center">
							<s:text name="label.bank.deposit" />
						</th>
						<s:if test="%{reportType=='Userwise'}">
							<th align="center">
								<s:text name="Claimed Amount" />
							</th>
						</s:if>
						<th align="center">
							<s:text name="label.net.coll" />
							<s:set name="stateCode" value="%{state}" />
							<s:set name="cityCode" value="%{city}" />
						</th>

					</tr>

					<s:if test="#dataBeanList.size>0">
						<s:iterator id="beanCart" value="#dataBeanList">
							<s:if test="%{orgId!=-1}">

								<tr class="startSortable">
									<td align="left">
										<s:if test="%{reportType=='Agentwise'}">
											<s:a
												href="bo_rep_cashChq_SearchDate.action?orgId=%{orgId}&orgName=%{name}"
												theme="ajax" targets="result">
												<s:property value="name" />
											</s:a>
										</s:if>
										<s:elseif test="%{reportType=='Daywise'}">
											<s:a
												href="bo_rep_cashChq_SearchAgentWise.action?start_date=%{start_date}&end_Date=%{name}&reportType=Daywise&state=%{#stateCode}&city=%{#cityCode}"
												theme="ajax" targets="result">
												<s:property value="name" />
											</s:a>
										</s:elseif>
										<s:else>
											<s:a
												href="bo_rep_cashChq_SearchUserWise.action?userName=%{name}&orgId=%{orgId}"
												theme="ajax" targets="result">
												<s:property value="name" />
											</s:a>
										</s:else>

									</td>
									<s:if test="%{reportType=='Agentwise'}">
									<td><s:property value="state" /></td>
									<td><s:property value="city" /></td>
									</s:if>
									<td align="right">

										<s:property value="totalCash" />
									</td>
									<td align="right">

										<s:property value="totalChq" />
									</td>
									<td align="right">

										<s:property value="cheqBounce" />
									</td>
									<td align="right">

										<s:property value="credit" />
									</td>
									<td align="right">

										<s:property value="debit" />
									</td>
									<td align="right">

										<s:property value="bankDeposit" />
									</td>
									<s:if test="%{reportType=='Userwise'}">
							<td align="right">
							<s:if test="%{claimAmt == NULL}">
							0.00
							</s:if>
							<s:else>
										<s:set name="amount" value="ClaimAmt"/>
             							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount")).replaceAll(",", "") %>
             				</s:else>
									</td>
						</s:if>
									<td align="right">
										<s:property value="netAmt" />
									</td>
								</tr>

							</s:if>
							<s:else>
								<tr>
								<s:if test="%{reportType=='Agentwise'}">
									<th colspan="3">
										<s:text name="label.total" />
									</th></s:if><s:else>
									<th>
										<s:text name="label.total" />
									</th>
									</s:else>
									<td align="right">

										<s:property value="totalCash" />
									</td>
									<td align="right">

										<s:property value="totalChq" />
									</td>
									<td align="right">

										<s:property value="cheqBounce" />
									</td>
									<td align="right">

										<s:property value="credit" />
									</td>
									<td align="right">

										<s:property value="debit" />
									</td>
									<td align="right">

										<s:property value="bankDeposit" />
									</td>
									<s:if test="%{reportType=='Userwise'}">
							<td align="right">
										<s:property value="totalClaim" />
									</td>
						</s:if>
									<td align="right">
										<s:property value="netAmt" />
									</td>
								</tr>
							</s:else>
						</s:iterator>


					</s:if>
					<s:else>
						<tr>
							<td colspan="9" align="center">
								<s:text name="msg.no.trsc.record" />
							</td>
						</tr>
					</s:else>

				</table>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_cashChq_SearchResult.jsp,v $ $Revision: 1.3 $
</code>