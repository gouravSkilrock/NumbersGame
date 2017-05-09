<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawGameConsolidate.js"></script>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
			<s:if test="%{message neq null && message neq ''}">
			 <s:text name="label.select.st.date.after"/>  <s:property value="message"/>
			</s:if>
			<s:else>
				<div>
					<font size="2"> <center> <b></><s:text
							name="label.draw.game.mtn" /> <s:text name="label.from" /> : <s:property
							value="startDate" /> &nbsp;<s:text name="label.to" />&nbsp;:
						<s:property value="endDate" /> </b> </center></font>
					<s:form action="bo_rep_drawWiseMtn_ExportToExcel" method="post"
						enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:hidden name="reportName"
							value="USSD Draw Wise report" />
						<s:submit key="btn.exportasexcel"
							cssStyle="margin-right:-400px;font-size:11px; text-align:center"
							onclick="createTableData();" />
					</s:form>
					</div>
				<div id="tableDataDiv">
					<table id="dataTable" style="border-collapse: collapse; margin-bottom: 30px;margin-right: 30px;"
						border="1" width="auto" cellpadding="6" cellspacing="0"
						bordercolor="#CCCCCC" align="left">
						<s:if test="mtnDataList neq null && mtnDataList.size>0">
						<tr>
							<th style="background-color: #e6e6e6" rowspan="3">
								<s:text name="Draw Name" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="3">
								<s:text name="Draw DateTime" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="3">
								<s:text name="Draw FreezeTime" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="3">
								<s:text name="Winning Result" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="3">
								<s:text name="Winning Transfer Status" />
							</th>
							<th style="background-color: #f0f0f0" colspan="3" align="center">
								<s:text name="Sale" />
							</th>
							<th style="background-color: #f0f0f0" colspan="9" align="center">
								<s:text name="Winning" />
							</th>
						</tr>
						<tr>
							<th style="background-color: #e6e6e6" rowspan="2" align="center">
								<s:text name="DYA" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="2" align="center">
								<s:text name="Airtime" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="2" align="center">
								<s:text name="Total Sales" />
							</th>
							<th style="background-color: #e6e6e6" rowspan="2" align="center">
								<s:text name="Total Winning" />
							</th>
							<th style="background-color: #ebebeb" colspan="2" align="center">
								<s:text name="DYA" />
							</th>
							<th style="background-color: #ebebeb" colspan="2" align="center">
								<s:text name="Airtime" />
							</th>
							<th style="background-color: #ebebeb" colspan="2" align="center">
								<s:text name="Winlot" />
							</th>
							<th style="background-color: #ebebeb" colspan="2" align="center">
								<s:text name="Total Claimed Winning" />
							</th>
							
						</tr>
						<tr>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Paid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Unpaid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Paid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Unpaid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Paid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Unpaid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Paid" />
							</th>
							<th style="background-color: #e6e6e6" align="center">
								<s:text name="Unpaid" />
							</th>
						</tr>
							<s:set name="dyaSale" value="0" />
							<s:set name="airtimeSale" value="0" />
							<s:set name="totalSale" value="0" />
							<s:set name="airtimeDoneWinning" value="0" />
							<s:set name="airtimePendingWinning" value="0" />
							<s:set name="dyaDoneWinning" value="0" />
							<s:set name="dyaPendingWinning" value="0" />
							<s:set name="totalDoneWinning" value="0" />
							<s:set name="totalPendingWinning" value="0" />
							<s:set name="totalWinning" value="0" />
							<s:set name="winlotDoneWinning" value="0" />
							<s:set name="winlotPendingWinning" value="0" />
							<s:iterator id="beanCart" value="mtnDataList">
								<tr>
									<td align="left">
										<s:property value="drawName" />
									</td>
									<td  style="width: 80px;" align="center" nowrap>
										<s:property value="drawDateTime" />
									</td>
									<td  style="width: 80px;" align="center">
										<s:property value="drawFreezeTime" />
									</td>
									<td  style="width: 80px;" align="center">
										<s:property value="winningResult" />
									</td>
									<td  style="width: 80px;" align="center">
										<s:property value="drawStatus" />
									</td>
									<td>
										<s:set name="dyaSale" value="dyaSale" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dyaSale"))%>
									</td>
									<td>
										<s:set name="airtimeSale" value="airtimeSale" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("airtimeSale"))%>
									</td>
									<td>
										<s:set name="totalSale" value="totalSale" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalSale"))%>
									</td>
									
									<td>
										<s:set name="totalWinning" value="totalWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalWinning"))%>
									</td>
									
									<td>
										<s:set name="dyaDoneWinning" value="dyaDoneWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dyaDoneWinning"))%>
									</td>
									<td>
										<s:set name="dyaPendingWinning" value="dyaPendingWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("dyaPendingWinning"))%>
									</td>
									<td>
										<s:set name="airtimeDoneWinning" value="airtimeDoneWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("airtimeDoneWinning"))%>
									</td>
									<td>
										<s:set name="airtimePendingWinning"
											value="airtimePendingWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("airtimePendingWinning"))%>
									</td>
									<td>
										<s:set name="winlotDoneWinning" value="winlotDoneWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("winlotDoneWinning"))%>
									</td>
									<td>
										<s:set name="winlotPendingWinning" value="winlotPendingWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("winlotPendingWinning"))%>
									</td>

									<td>
										<s:set name="totalDoneWinning" value="totalDoneWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalDoneWinning"))%>
									</td>
									<td>
										<s:set name="totalPendingWinning" value="totalPendingWinning" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalPendingWinning"))%>
									</td>
									


								</tr>
							</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="6" align="center">
									<s:text name="msg.no.record.process" />
								</td>
							</tr>
						</s:else>
					</table>
				</div></s:else>
				</div>
			</div>
	</body>
</html>
