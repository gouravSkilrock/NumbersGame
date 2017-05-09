
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name="startDate" value="%{start_date}" />
<s:set name="endDate" value="%{end_Date}" />

<div align="center">
	<h3>
		<u><s:property value="#application.TIER_MAP.RETAILER" /> Credit
			Update <s:text name="Report" /> For <s:property value="%{agtName}" />&nbsp;
			From : <s:property value="startDate" /> &nbsp;&nbsp;To&nbsp;:&nbsp;
			<s:property value="endDate" />
		</u>
	</h3>
</div>

<s:if test="#session.retCreditUpdateReportList.size>0">
	<!--<s:div id="excel"
					cssStyle="text-align:center;width: 100%;font-size: 9pt">
					<s:a href="bo_rep_CreditUpdateReport_exportExcel.action?start_date=%{#startDate}&end_Date=%{#endDate}">Export As Excel</s:a>
				</s:div>-->
</s:if>

<table width="100%" border="1" cellpadding="3" cellspacing="0"
	bordercolor="#CCCCCC" align="center">
	<s:if
		test="%{#session.retCreditUpdateReportList!=null && #session.retCreditUpdateReportList.size()>0}">
		<!-- <tr>
		<th align="center" colspan="2" ><s:property value="#session.orgName" /></th>
		<th align="center">Amount in <s:property value="#application.CURRENCY_SYMBOL" /></th>
	</tr>
	<tr>
		<td align="center" colspan="3"><s:property value="#session.orgAdd" /></td>
	</tr> -->
		<tr align="center">
			<th>
				S. No.
			</th>
			<th>
				<s:property value="#application.TIER_MAP.RETAILER" />
				Name
			</th>
			<th>
				Credit
			</th>

			<th>
				DateTime
			</th>

			<!--<th>
				Done By User
			</th>
			<th>
				Done By Organization
			</th>
			<th>
				Done By Org Type
			</th>

		--></tr>
		<s:iterator id="result" value="#session.retCreditUpdateReportList"
			status="index">
			<tr>
				<td nowrap="nowrap" rowspan="<s:property value="%{value.size()}"/>">
					<s:property value="%{#index.index+1}" />
				</td>
				<td nowrap="nowrap" rowspan="<s:property value="%{value.size()}"/>">
					<s:property value="key" />
				</td>
				<s:iterator id="inner" value="value" status="indx">

					<s:set name="innerindx" value="%{#indx.indx+1}" />

					<s:if test="%{#innerindx == 1}">
						<tr>
					</s:if>
					<td align="right">
						<s:property value="orgStatus" />
						<s:if test="%{orgCreditLimit<0}">
							<s:property value="%{orgCreditLimit*-1}" />
						</s:if>
						<s:else>
							<s:property value="%{orgCreditLimit}" />
						</s:else>
					</td>
					<td align="right">
						<s:property value="%{orgCity.substring(0,orgCity.length()-2)}" />
					</td>
					<!--<td align="right">
						<s:property value="orgCountry" />
					</td>
					--><!--<td align="right">
						<s:property value="orgAddr1" />
					</td>
					--><!--<td align="right">
						<s:if test="%{scrapLimit.length() == 17}">
							<s:property value="#application.TIER_MAP.BO" />
						</s:if>
						<s:if test="%{scrapLimit.length() == 20}">
							<s:property value="#application.TIER_MAP.AGENT" />
						</s:if>
					</td>

					--><s:if test="%{#innerindx != 1}">
			</tr>
	</s:if>
	</s:iterator>
	</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<th align="center">
				No Records Found
			</th>
		</tr>
	</s:else>
</table>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retCreditUpdateReport_result.jsp,v $ $Revision:
	1.1.2.2 $
</code>
