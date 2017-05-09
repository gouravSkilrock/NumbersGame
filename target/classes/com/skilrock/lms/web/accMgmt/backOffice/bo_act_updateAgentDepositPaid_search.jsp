<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<s:if test="postSaleDepositAgentDateMap.size()>0">
	<table width="100%" border="1" cellpadding="2" cellspacing="0"
		bordercolor="#CCCCCC" align="center" valign="top">
		<tr>
			<!--<th width="10%"> 
			Date
		</th>
		-->
			<th width="15%">
				<s:text name="label.depo.amt" />
			</th>
			<th width="10%">
				<s:text name="label.depo.comsn.rate" />
			</th>
			<th width="10%">
				<s:text name="label.depo.comsn.amt" />
			</th>
			<th width="15%">
				<s:text name="label.tax.amt" />
			</th>
			<th width="10%">
				<s:text name="label.gprs.chrgs" />
			</th>
			<th width="15%">
				<s:text name="label.net.amt.pay" />
			</th>
			<th width="15%">
				<s:text name="label.status" />
			</th>
		</tr>
		<s:iterator value="%{postSaleDepositAgentDateMap}">

			<tr>
				<!--<td>
				<s:property value="%{value.date}" />
				
			</td>
	        -->
				<td align="right">
					<s:set name="depAmount" value="%{value.depAmount}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("depAmount")) %>
				</td>
				<td align="right">
					<s:set name="depCommRate" value="%{value.depCommRate}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("depCommRate")) %>
				</td>
				<td align="right">
					<s:set name="commAmount" value="%{value.commAmount}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("commAmount")) %>
				</td>
				<td align="right">
					<s:set name="taxCharges" value="%{value.taxCharges}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("taxCharges")) %>
				</td>
				<td align="right">
					<s:set name="charges_1" value="%{value.charges_1}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("charges_1")) %>
				</td>
				<td align="right">
					<s:set name="netAmount" value="%{value.netAmount}" />
					<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("netAmount")) %>
				</td>
				<td>
					<s:set name="status" value="%{value.status}" />
					<s:if test="%{value.status == 'APPROVED'}">
						<s:text name="label.pnd.pay" />
					</s:if>
					<s:else>
						<s:text name="label.paid" />
					</s:else>

				</td>
			</tr>
		</s:iterator>
		<div id="butDiv">
			<s:if test="#status == 'APPROVED'">

				<tr>
					<td colspan="7" align="center">
						<table>
							<input type="button" onclick="updateAgentComm()"
								value="<s:text name="btn.update.selected" />" class="button" />
							<s:hidden name="stDate" id="stDate" value="%{startDate}"></s:hidden>
							<s:hidden name="enDate" id="enDate" value="%{endDate}"></s:hidden>

							<!--<s:a href="com/skilrock/lms/web/accMgmt/bo_act_update_postagent_deposit_submit.action?startDate=%{startDate}&endDate=%{endDate}&agentOrgId=%{agentOrgId}">Submit</s:a>
				
			-->
						</table>
					</td>
				</tr>

			</s:if>
		</div>
		</s:if>
		<s:else>
			<s:text name="msg.no.record" />
		</s:else>



	</table>
	<div id="subDiv">
		<s:property value="%{resStatus}" />
	</div>
	<code id="headId" style="visibility: hidden">
		$RCSfile: bo_act_updateAgentDepositPaid_search.jsp,v $ $Revision:
		1.1.2.1 $
	</code>