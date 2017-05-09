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
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<div>
					<font size="2"> <center> <b></><s:text
							name="Jackpot Detail Report" /> <s:text name="label.from" /> :
						<s:property value="startDate" /> &nbsp;<s:text name="label.to" />&nbsp;:
						<s:property value="endDate" /> </b> </center></font>
					<!-- 	<s:form action="bo_rep_drawWiseMtn_ExportToExcel" method="post"
						enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:hidden name="reportName" value="Draw Wise Mtn Report" />
						<s:submit key="btn.exportasexcel"
							cssStyle="margin-right:-400px;font-size:11px; text-align:center"
							onclick="createTableData();" /> 
					</s:form>   -->
				</div>
				</br>
				<div>
					<s:set name="jackpotBasic" value="0" />
					<s:set name="jackpotPower" value="0" />
					<s:set name="securityDepositAmt" value="0" />
					<s:if test="reportBean neq null">
						<table style="border-collapse: collapse; margin-bottom: 30px;"
							border="1" width="auto" cellpadding="6" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr>
								<th>
									TOTAL AVAILABLE JACKPOT BASIC
								</th>
								<th>
									TOTAL AVAILABLE JACKPOT POWER
								</th>
								<th>
									SECURITY DEPOSIT AMOUNT
								</th>
							</tr>
							<tr>
								<td>
									<s:property value="reportBean.totalAvailableJackpotAmtBasic" />
								</td>
								<td>
									<s:property value="reportBean.totalAvailableJackpotAmtPower" />

								</td>
								<td>
									<s:property value="reportBean.securityDepositAmt" />
								</td>

							</tr>
						</table>
					</s:if>
				</div>
				<br />
				<div id="tableDataDiv">

					<table id="dataTable"
						style="border-collapse: collapse; margin-bottom: 30px;" border="1"
						width="100%" cellpadding="6" cellspacing="0" bordercolor="#CCCCCC"
						align="center">
						<s:if test="reportBean.jackpotDataList.size!=0 ">
							<tr>
								<th style="background-color: #e6e6e6" rowspan="2" align="center">
									<s:text name="Event Id" />
								</th>
								<th style="background-color: #e6e6e6" rowspan="2" align="center">
									<s:text name="Draw DateTime" />
								</th>
								<th style="background-color: #e6e6e6" colspan="5" align="center">
									<s:text name="Basic" />
								</th>
								<th style="background-color: #e6e6e6" colspan="5" align="center">
									<s:text name="Power" />
								</th>
							</tr>
							<tr>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Sale Amt" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Total Prize Fund" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Fixed Prizes Fund" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Jackpot For This Draw" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Total Available Jackpot" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Sale Amt" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Total Prize Fund" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Fixed Prizes Fund" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Jackpot For This Draw" />
								</th>
								<th style="background-color: #e6e6e6" align="center">
									<s:text name="Total Available Jackpot" />
								</th>
							</tr>
							<!--<s:set name="dyaSale" value="0" />
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
							<s:set name="winlotPendingWinning" value="0" />   -->
							<s:iterator id="beanCart" value="reportBean.jackpotDataList">
								<tr align="center" class="startSortable">
									<td align="center">
										<s:property value="eventId" />
									</td>
									<td align="center">
										<s:property value="drawDate" />
									</td>
									<td align="center">
										<s:set name="totalSaleAmtBasic" value="totalSaleAmtBasic" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalSaleAmtBasic"))%>
									</td>
									<td align="center">
										<s:set name="totalPrizeFundBasic" value="totalPrizeFundBasic" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalPrizeFundBasic"))%>
									</td>
									<td align="center">
										<s:set name="fixedPrizesFundBasic"
											value="fixedPrizesFundBasic" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("fixedPrizesFundBasic"))%>
									</td >

									<td align="center">
										<s:set name="jackpotForThisDrawBasic"
											value="jackpotForThisDrawBasic" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("jackpotForThisDrawBasic"))%>
									</td>
									<td align="center">
										<s:set name="totalAvailableJackpotBasic"
											value="totalAvailableJackpotBasic" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalAvailableJackpotBasic"))%>
									</td>
									<td align="center">
										<s:set name="totalSaleAmtPower" value="totalSaleAmtPower" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalSaleAmtPower"))%>
									</td>
									<td align="center">
										<s:set name="totalPrizeFundPower" value="totalPrizeFundPower" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalPrizeFundPower"))%>
									</td>
									<td align="center">
										<s:set name="fixedPrizesFundPower"
											value="fixedPrizesFundPower" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("fixedPrizesFundPower"))%>
									</td>

									<td align="center">
										<s:set name="jackpotForThisDrawPower"
											value="jackpotForThisDrawPower" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("jackpotForThisDrawPower"))%>
									</td>
									<td align="center">
										<s:set name="totalAvailableJackpotPower"
											value="totalAvailableJackpotPower" />
										<%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("totalAvailableJackpotPower"))%>
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

				</div>
			</div>
		</div>
	</body>
</html>
