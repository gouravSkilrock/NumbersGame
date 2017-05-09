<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div align="center">
	<h3>
	</h3>
</div>


<div id="dataDiv" style="">
	<table width="100%" border="1" cellpadding="2" cellspacing="0"
		bordercolor="#CCCCCC" align="center" valign="top" id="dataTable">
		<tr>
			<th rowspan="2">
				Agent Org Name
			</th>
			<th colspan="3">
				Credit Note
			</th>
			<th colspan="3">
				Debit Note
			</th>
		</tr>
		<tr>
			<th>
				Againts Loose Books Returns
			</th>
			<th>
				Againts Faulty Recharge Vouchers
			</th>
			<th>
				Others
			</th>
			<th>
				Againts Loose Books Returns
			</th>
			<th>
				Againts Faulty Recharge Vouchers
			</th>
			<th>
				Others
			</th>

		</tr>

		<s:if test="#session.CREDIT_DEBIT_NOTE_LIST.size() > 0">

			<s:iterator value="#session.CREDIT_DEBIT_NOTE_LIST" status="customStat">
				<s:if test="#customStat.index != #session.CREDIT_DEBIT_NOTE_LIST.size() -1 ">
					<tr>
						<td>
							<s:property value="%{userOrgName}" />
						</td>
						<td>
							<s:set name="crAgainstLooseBooksReturnAmount" value="%{crAgainstLooseBooksReturn}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("crAgainstLooseBooksReturnAmount")) %>
						</td>
						<td>
						<s:set name="crAgainstFaultyRechargeVouchersAmount" value="%{crAgainstFaultyRechargeVouchers}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("crAgainstFaultyRechargeVouchersAmount")) %>
						</td>
						<td>
							<s:set name="crOthersAmount" value="%{crOthers}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("crOthersAmount")) %>
						
						</td>
						<td>
						<s:set name="drAgainstLooseBooksReturnAmount" value="%{drAgainstLooseBooksReturn}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drAgainstLooseBooksReturnAmount")) %>
						</td>
						<td>
						<s:set name="drAgainstFaultyRechargeVouchersAmount" value="%{drAgainstFaultyRechargeVouchers}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drAgainstFaultyRechargeVouchersAmount")) %>
						</td>
						<td>
						<s:set name="drOthersAmount" value="%{drOthers}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drOthersAmount")) %>
						</td>

					</tr>
				</s:if>
				<s:else>
					<tr style="font-weight: bold;">
						<td>
							<s:property value="'Total'" />
						</td>
						<td>
						<s:set name="crAgainstLooseBooksReturnTotAmount" value="%{crAgainstLooseBooksReturnTot}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("crAgainstLooseBooksReturnTotAmount")) %>
						</td>
						<td>
						<s:set name="crAgainstFaultyRechargeVouchersTotAmount" value="%{crAgainstFaultyRechargeVouchersTot}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("crAgainstFaultyRechargeVouchersTotAmount")) %>
						</td>
						<td>
						<s:set name="crOthersTotAmount" value="%{crOthersTot}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("crOthersTotAmount")) %>
						</td>
						<td>
						<s:set name="drAgainstLooseBooksReturnTotAmount" value="%{drAgainstLooseBooksReturnTot}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drAgainstLooseBooksReturnTotAmount")) %>
						</td>
						<td>
						<s:set name="drAgainstFaultyRechargeVouchersTotAmount" value="%{drAgainstFaultyRechargeVouchersTot}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drAgainstFaultyRechargeVouchersTotAmount")) %>
						</td>
						<td>
						<s:set name="drOthersTotAmount" value="%{drOthersTot}"></s:set>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("drOthersTotAmount")) %>
						</td>

					</tr>
				</s:else>
			</s:iterator>

		</s:if>
		<s:else>
			<tr>
				<td align="center" colspan="7">
					No Records To Process
				</td>
			</tr>
		</s:else>

	</table>
</div>


<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_credit_debit_note_report_Search.jsp,v $ $Revision:
	1.1.2.14 $
</code>