<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<s:head theme="ajax" />
	<body>
		<div id="wrap">
			<div id="top_form">
				<s:if test="%{message neq null}">
					<h3 style="color:red; ">
						<center><s:property value="message" /></center>
					</h3>
				</s:if>
				<s:elseif test="%{depositList != null and depositList.size>0}">
					<s:form action="bo_act_processBankDepositRequest_Export" method="post" enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:submit value="Export As Excel" cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
					</s:form>
					<div id="tableDataDiv">
						<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" id="dataTable">
							<tr>
								<th align="center">S.No</th>
								<th align="center">Retailer Name</th>
								<th align="center">Receipt No</th>
								<th align="center">Bank Name</th>
								<th align="center">Branch Name</th>
								<th align="center">Amount</th>
								<th align="center">Request Date</th>
								<th align="center">Process Date</th>
								<th align="center">Status</th>
								<s:if test="%{status eq 'PENDING'}">
									<th align="center">Select All <input type="checkbox" onclick="selectAll(this, 'selectId');" /></th>
								</s:if>
							</tr>
							<s:iterator value="depositList" status="status">
								<tr class="startSortable">
									<td><s:property value="%{#status.index+1}" /></td>
									<td>
										<s:hidden cssClass="id" name="" value="%{id}" />
										<s:property value="userName" />
									</td>
									<td>
										<s:property value="receiptNo" />
									</td>
									<td>
										<s:property value="bankName" />
									</td>
									<td>
										<s:property value="branchName" />
									</td>
									<td align="right">
										<s:set name="amount" value="amount" />
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount")) %>
									</td>
									<td>
										<s:property value="requestDate" />
									</td>
									<td>
										<s:property value="processDate" />
									</td>
									<td>
										<s:property value="status" />
									</td>
									<s:if test="%{status eq 'PENDING'}">
										<td><center><input type="checkbox" class="selectId" /></center></td>
									</s:if>
								</tr>
							</s:iterator>
						</table>
						<s:if test="%{status eq 'PENDING'}">
							<br/>
							<table>
								<tr>
									<td><input type="button" value="Approve" onclick="processRequest('APPROVED');" /></td>
									<td><input type="button" value="Cancel" onclick="processRequest('CANCELLED');" /></td>
								</tr>
							</table>
						</s:if>
					</div>
				</s:elseif>
				<s:else>
					<h3>
						<center>No Record to Process</center>
					</h3>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_processBankDepositRequest_Search.jsp,v $ $Revision: 1.3 $
</code>