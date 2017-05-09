<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<body>
	<div align="left">
		<s:form action="exportReportAsExcelWithoutTable.action" method="post"
			enctype="multipart/form-data">
			<s:hidden id="tableValue" name="reportData" />
			<s:hidden name="reportName" value="Retailer Transaction Records" />
			<s:submit key="btn.exporttoexcel"
				cssStyle="margin-right:-400px;font-size:15px"
				onclick="getTableData();" />
		</s:form>
	</div>

	<div align="center" id="reportinfodiv">
		<h4>
			<u><s:text name="Retailer" /> <s:text name="Transaction" /> <s:text name="Report" />
				&nbsp; <%
 	if ("".equals(((DateBeans) session.getAttribute("datebean"))
 			.getReportType())) {
 %> <s:text name="label.from" /> : <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getStartDate())%>
				&nbsp;&nbsp;<s:text name="label.to" />&nbsp;:&nbsp; <%=GetDate.getConvertedDate(((DateBeans) session
						.getAttribute("datebean")).getEndDate())%> <%
 	} else {
 		out.println("Of : "
 				+ ((DateBeans) session.getAttribute("datebean"))
 						.getReportType());
 	}
 %> </u>
		</h4>
	</div>



	<div id=tableDataDiv>
		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center" style="font-size: 12px">

			<s:if test="%{#session.result!=null}">

				<tr align="center">

					<th nowrap="nowrap">
						<s:text name="label.date.time" />
					</th>
					<th nowrap="nowrap">
						<s:text name="label.srv" />
					</th>
					<th nowrap="nowrap">
						<s:text name="label.particular" />
					</th>
					<th nowrap="nowrap">
						<s:text name="label.amount" />
					</th>
					<th nowrap="nowrap">
						<s:text name="label.avail.bal" />
					</th>
					<th nowrap="nowrap">
						<s:text name="label.remarks" />
					</th>

				</tr>



				<s:iterator id="result" value="#session.result">


					<tr>

						<s:set name="orgValue" value="%{value}"></s:set>



						<td align="right" noWrap="nowrap">
							<s:property value="%{#orgValue.date}" />

						</td>
						<td align="right" noWrap="nowrap">
							<s:property value="%{#orgValue.service}" />

						</td>

						<td align="right" noWrap="nowrap">
							<s:property value="%{#orgValue.gameName}" />

						</td>

						<td align="right" noWrap="nowrap">
							<!--<s:property value="%{#orgValue.amount}" />-->
							<s:set name="amount" value="%{#orgValue.amount}" />
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount"))%>
						</td>

						<td align="right">
							<!--<s:property value="%{#orgValue.currentBalance}"/>-->
							<s:set name="currentBal" value="%{#orgValue.currentBalance}" />
							<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("currentBal"))%>
						</td>
						<td align="right">
							<s:property value="%{#orgValue.remarks}" />

						</td>

					</tr>
				</s:iterator>

			</s:if>
			<s:else>
				<tr>
					<th align="center">
						<s:if test="%{message==null}">
				<s:text name="msg.no.record" />
				</s:if>
						<s:else>
							<s:property value="message" />
						</s:else>
					</th>
				</tr>
			</s:else>
		</table>
	</div>
</body>


<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_customTransactionReport_search.jsp,v $ $Revision:
	1.1.2.3.4.1.2.2 $
</code>