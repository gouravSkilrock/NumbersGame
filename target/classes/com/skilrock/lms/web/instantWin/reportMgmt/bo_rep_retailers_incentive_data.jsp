<%@ page import="org.apache.struts2.components.Form"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center">
	<h2>Retailer Wise Incentive Report</h2>
</div>
<s:if
	test="retailerMap != null && retailerMap.size() > 0">
	<div align="center">
		<s:form action="bo_rep_retailers_Incentive_ExpExcel" method="post"
			enctype="multipart/form-data">
			<s:hidden id="retailerTableValue" name="reportData" />
			<s:hidden id="userType" name="userType" value="Retailer" />
			<s:submit value="Export As Excel" align="center"
				onclick="getReatialerData();" />
		</s:form>
	</div>
</s:if>
<div id="tableDataDivForRetailer">
	<div id="agtName" style="margin-bottom: 7px;">
		 <b>Agent Name</b> -<s:property value="%{orgName.toUpperCase()}" /> 

	</div>
	<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" style="font-size: 12px">
		<s:if test="retailerMap != null && retailerMap.size() > 0">
			<tr>
				<th>Agent</th>
				<th>Retailer Name</th>
				<th>Sale Amount</th>
				<th>Wining Amount</th>
				<th>Non Winning Amount</th>
				<th>Incentive Amount</th>
			</tr>
			<s:set name="saleSum" value="0.0" />
			<s:set name="winSum" value="0.0" />
			<s:set name="nonWinSum" value="0.0" />
			<s:set name="incentiveSum" value="0.0" />
			<s:iterator value="retailerMap.values()">
				<tr class="startSortable">
					<td>
						<s:property value="parentOrgName" />
					</td>
					<td>
						<s:property value="organizationName" />
					</td>
					<td>
						<s:property value="sale" /> 
						<s:set name="saleTot" value="sale" />
					</td>
					<td>
						<s:property value="winning" />
						<s:set name="winTot" value="winning" />
					</td>
					<td>
						<s:property value="%{sale - winning}" />
						<s:set name="nonWinTot" value="%{sale - winning}" />
					</td>
					<td>
						<s:property value="incentiveAmount" />
						<s:set name="incentiveTot" value="incentiveAmount" />
					</td>
				</tr>
				<s:set name="saleSum" value="#saleSum+#saleTot" />
				<s:set name="winSum" value="#winSum+#winTot" />
				<s:set name="nonWinSum" value="#nonWinSum+#nonWinTot" />
				<s:set name="incentiveSum" value="#incentiveSum+#incentiveTot" />
			</s:iterator>
			<tr>
				<th>Total</th>
				<th></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleSum"))%></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winSum"))%></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("nonWinSum"))%></th>
				<th><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("incentiveSum"))%></th>
			</tr>
		</s:if>
		<s:else>
			<center>No Records Found.</center>
		</s:else>
	</table>
</div>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_retailers_incentive_data.jsp,v $ $Revision: 1.3 $ </code>