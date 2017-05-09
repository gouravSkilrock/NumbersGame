<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>
<html>
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<body>

		<s:if test=" #session.ORG_SEARCH_RESULTS1.size !=0 ">
			<s:div id="excel"
				cssStyle="text-align: left;width: 100%;font-size: 9pt">
				<s:a href="bo_um_orgDtl_ExpExcel.action?orgType=%{orgType}">
					<s:text name="btn.exportasexcel" />
				</s:a>
			</s:div>
		</s:if>
		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<s:if test="orgType == 'RETAILER'">
					<td colspan="13" align="center" nowrap="nowrap">
						<b><s:text name="btn.srch.res" /> </b>
					</td>
				</s:if>
				<s:else>
					<td colspan="12" align="center" nowrap="nowrap">
						<b><s:text name="btn.srch.res" /> </b>
					</td>
				</s:else>
			</tr>

			<s:if test=" #session.ORG_SEARCH_RESULTS1.size !=0 ">
				<tr>
					<th align="center">
						<s:text name="label.org.name" />
					</th>
					<th align="center">
						<s:text name="label.type" />
					</th>
					<th align="center">
						<s:text name="label.status" />
					</th>
					<th align="center">
						<s:text name="label.prnt.org" />
					</th>
					<th align="center">
						<s:text name="label.pwt.scrp" />
					</th>
					<s:if test="orgType == 'RETAILER'">
						<th align="center">
							<s:text name="label.terminal.status"></s:text>
						</th>
					</s:if>
					<th align="center">
						<s:text name="CL" />
					</th>
					<th align="center">
						<s:text name="XCL" />
					</th>
					<th align="center">
						<s:text name="label.rem.day" />
					</th>
					<s:if test="#application.COUNTRY_DEPLOYED == 'KENYA'">
						<th align="center">
							<s:text name="label.ledger.bal" />
						</th>
					</s:if>
					<th align="center">
						<s:text name="label.blnce" />
					</th>
					<th align="center">
						<s:text name="label.claim.bal" />
					</th>
					<th align="center">
						<s:text name="label.unclaim.bal" />
					</th>
					<th align="center">
						<s:text name="label.secur.dpst" />
					</th>
				</tr>
				<s:iterator id="SearchOrgResults"
					value="#session.ORG_SEARCH_RESULTS" status="rowIndex">
					<tr id="<s:property value="#rowIndex.index"/>">
						<s:set name="trId" value="#rowIndex.index"></s:set>
						<s:set name="orgbean" />
						<s:set name="creditLimit" value="orgCreditLimit" />
						<s:set name="availCredit" value="availableCredit" />
						<s:set name="securityDeposit" value="securityDeposit" />
						<s:set name="ledgerBalance" value="ledgerBalance" />
						<s:set name="extendedCredit" value="extendedCredit" />
						<s:set name="claimBal" value="claimableBal" />
						<s:set name="unclaimBal" value="unclaimableBal" />
						<s:set name="terminalId" value="serialNo"/>
						<%
							NumberFormat format = NumberFormat.getInstance();
									format.setMaximumFractionDigits(2);
									Double dobl = (Double) pageContext
											.getAttribute("creditLimit");
									String creditLimit = format.format(dobl);
									Double dobl1 = (Double) pageContext
											.getAttribute("availCredit");
									Double clmBal = (Double) pageContext
											.getAttribute("claimBal");
									String availCredit = format.format(dobl1 - clmBal);
									Double dobSec = (Double) pageContext
											.getAttribute("securityDeposit");
									String securityDeposit = format.format(dobSec);

									Double ledgerBalance = (Double) pageContext
											.getAttribute("ledgerBalance");
									String strledgerBalance = format.format(ledgerBalance);

									Double extendedCredit = (Double) pageContext
											.getAttribute("extendedCredit");
									String strextendedCredit = format.format(extendedCredit);
									Double unclmBal = (Double) pageContext
											.getAttribute("unclaimBal");
									String strUnclmBal = format.format(unclmBal);
						%>
						<td>
							<s:a id="link2" targets="top"
								href="bo_um_searchAgtRet_Detail.action?orgId=%{orgId}&orgType=%{orgType}">
								<s:property value="orgName" />
							</s:a>
						</td>
						<td>
							<s:property id="gn"
								value="#application.TIER_MAP[orgType].toUpperCase()" />
						</td>
						<td>
							<s:property value="orgStatus" />
						</td>
						<td>
							<s:property value="parentOrgName" />
						</td>
						<td>
							<s:property value="pwtScrapStatus" />
						</td>
						<s:if test="userOrgType == 'RETAILER'">
							<td>
								<s:property value="terminalId" />
							</td>
						</s:if>
						<td align="right" nowrap="nowrap"><%=creditLimit%></td>
						<td align="right" nowrap="nowrap"><%=strextendedCredit%></td>
						<td align="right" nowrap="nowrap">
							<s:property value="extendsCreditLimitUpto" />
						</td>
						<s:if test="#application.COUNTRY_DEPLOYED == 'KENYA'">
							<s:if test="%{ledgerBalance<0}">
								<td align="right" nowrap="nowrap" style="color: red">
									<s:a href="#"
										onclick="updatedLedgerBalance('com/skilrock/lms/admin/bo_um_searchAgtRet_UpdLedgerBal.action?orgId=%{orgId}& parentOrgId=%{parentOrgId}& orgType=%{orgType}','div-%{trId}')">
										<s:div id="div-%{trId}"><%=strledgerBalance%></s:div>
										<div id="load-<s:property value="#rowIndex.index"/>"
											style="text-align: right"></div>
									</s:a>
								</td>
							</s:if>
							<s:else>
								<td align="right" nowrap="nowrap">
									<s:a href="#"
										onclick="updatedLedgerBalance('com/skilrock/lms/admin/bo_um_searchAgtRet_UpdLedgerBal.action?orgId=%{orgId}& parentOrgId=%{parentOrgId}& orgType=%{orgType}','div-%{trId}')">
										<s:div id="div-%{trId}"><%=strledgerBalance%></s:div>
										<div id="load-<s:property value="#rowIndex.index"/>"
											style="text-align: right"></div>
									</s:a>
								</td>
							</s:else>
						</s:if>
						<s:if test="%{availCredit<0}">
							<td align="right" nowrap="nowrap" style="color: red">
								<%=availCredit%></td>
						</s:if>
						<s:else>
							<td align="right" nowrap="nowrap">
								<%=availCredit%></td>
						</s:else>
						<td align="right" nowrap="nowrap">
							<%=clmBal%></td>
						<td align="right" nowrap="nowrap">
							<%=strUnclmBal%></td>
						<td align="right" nowrap="nowrap">
							<%=securityDeposit%></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="11" align="center">
						<s:text name="msg.no.record.process" />
					</td>
				</tr>
			</s:else>
		</table>
		<s:div id="naviga" cssStyle="text-align: center">
			<s:if test=" #session.ORG_SEARCH_RESULTS1.size >5 ">
				<s:if test="#session.startValueOrgSearch!=0">
					<s:a theme="ajax" targets="bottom"
						href="bo_um_searchAgtRet_Navigate.action?end=first&orgType=%{orgType}">
						<s:text name="label.first" />
					</s:a>
					<s:a theme="ajax" targets="bottom"
						href="bo_um_searchAgtRet_Navigate.action?end=Previous&orgType=%{orgType}">
						<s:text name="label.pre" />
					</s:a>
				</s:if>
				<s:else>
					<s:text name="label.first.pre" />
				</s:else>
				<s:if
					test="#session.startValueOrgSearch==((#session.ORG_SEARCH_RESULTS1.size/5)*5)">
					<s:text name="label.next.last" />
				</s:if>
				<s:else>
					<s:a theme="ajax" targets="bottom"
						href="bo_um_searchAgtRet_Navigate.action?end=Next&orgType=%{orgType}">
						<s:text name="label.next" />
					</s:a>
					<s:a theme="ajax" targets="bottom"
						href="bo_um_searchAgtRet_Navigate.action?end=last&orgType=%{orgType}">
						<s:text name="label.last" />
					</s:a>
				</s:else>

			</s:if>
		</s:div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchAgtRet_SearchResult.jsp,v $ $Revision:
	1.2.2.1.4.1.2.4 $
</code>