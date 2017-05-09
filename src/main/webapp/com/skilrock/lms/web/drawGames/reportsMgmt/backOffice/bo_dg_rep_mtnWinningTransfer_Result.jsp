<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head><s:head theme="ajax" debug="false"/>

	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
			<s:if test="%{message neq null && message neq ''}">
			 <s:text name="label.select.st.date.after"/>  <s:property value="message"/>
			</s:if>
			<s:else>
				<s:if test="%{pendingData != null and pendingData.size()>0}">
				<h3><center>
					<s:text name="label.ussd.winning.approval" />
				</center></h3>
					
					<div id="tableDataDiv">
					<s:form action="bo_dg_rep_pushPendingWinning" name="pushPending" id="xyz123" onsubmit="return validateForm();">
						<table id="dataTable" width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
							<tr>
								<th align="center" rowspan="2">
									S. No.
								</th>
								<th align="center" rowspan="2">Choose Draw </th>
								<th align="center" rowspan="2">
									Game Name
								</th>
								<th align="center" rowspan="2">
									Draw Name
								</th>
								<th align="center" rowspan="2">
									Draw Date/Time
								</th>
								<th align="center" colspan="4">
									Amount(NGN)
								</th>
								
							</tr>
							<tr><th align="center">Total Winning Amount(On Tickets Played Through USSD)</th><th align="center">To be paid through WINLOT</th><th align="center">To be paid in to MTN (AT)</th><th align="center">To be paid in to DYA</th></tr>
							
							<s:iterator value="pendingData" status="indx">
								<tr>
									<td align="center">
										<s:property value="#indx.index + 1" />
									</td>
									<td align="center">
									
										<s:radio list="{''}" name="drawId" theme="simple"  id="drawId" onchange="enableButton()">
										<s:hidden value="%{drawId}" name="drawId" /><s:hidden value="%{gameId}" name="gameNo" /></s:radio>
									</td>
									
									<td style="display:none;" id="drawNo"><s:property value="%{drawId}"/></td>
									<td style="display:none;" id="gameId"><s:property value="%{gameId}"/></td>
									
									<td align="center">
										<s:property value="gameName" />
									</td>
									<td align="center">
										<s:property value="drawName" />
									</td>
									<td align="center">
										<s:property value='drawDateTime.replace(".0","")' />
									</td>
									<s:set name="totalAmt" value="0.0" />
									<s:iterator value="winnintAmtMap">
									<s:set name="totalAmt" value="#totalAmt + value"/>
									<s:if test="key == 'WINLOT'"><s:set name="winlotAmt" value="value"/></s:if>
									<s:if test="key == 'Airtime'"><s:set name="airtimeAmt" value="value"/></s:if>
									<s:if test="key == 'DYA'"><s:set name="dyaAmt" value="value"/></s:if>
									</s:iterator>
									<td align="center" id="totalAmt"><%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalAmt")) %></td>
									<td align="center" id="winlotAmt"><%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("winlotAmt")) %></td>
									<td align="center" id="airtimeAmt"><%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("airtimeAmt")) %></td>
									<td align="center" id="dyaAmt"><%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("dyaAmt")) %></td>
									
									
								</tr>
							</s:iterator>
							<tr><td colspan="9" align="center"><input type="button" name="submit" value="Transfer Winning to MTN" onclick="validateForm();" class="button"/></td></tr> 
						</table>
						</s:form>
					</div>
					<br/>
					
				</s:if>
				<s:else>
					<center><h3>No Record Exist</h3></center>
				</s:else>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_mtnWinningTransfer_Result.jsp,v $ $Revision: 1.3 $
</code>