<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" />
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
			<s:if test="%{message neq null && message neq ''}">
			 <s:text name="label.select.st.date.after"/>  <s:property value="message"/>
			</s:if>
			<s:else>
				<s:if test="%{reconcileList != null and reconcileList.size()>0}">
					<s:form action="bo_dg_rep_ExportToExcel" method="post" enctype="multipart/form-data">
						<s:hidden id="tableValue" name="reportData" />
						<s:hidden name="reportName" value="Draw Game Reconciliation Report" />
						<s:submit key="btn.exportasexcel" cssStyle="margin-right:-400px;font-size:11px; text-align:center" onclick="createTableData();"/>
					</s:form>
					<div id="tableDataDiv">
						<table id="dataTable" width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
							<tr>
								<th align="center">
									S. No.
								</th>
								<th align="center">
									Transaction Type
								</th>
								<th align="center">
									Wallet Type
								</th>
								<th align="center">
									Mobile No
								</th>
								<th align="center">
									Ticket Number
								</th>
								<th align="center">
									Amount
								</th>
								<th align="center">
									Transaction Date
								</th>
								<th align="center">
									WINLOT Status
								</th>
								<th align="center">
									WINLOT Ref Id
								</th>
								<th align="center">
									MTN Status
								</th>
								<th align="center">
									MTN Id
								</th>
								<s:if test="%{status eq 'INITIATED' && transactionType neq 'ALL'}">
									<s:set name="flag" value="true" />
									<th align="center">
										Select
										<input type="checkbox" onclick="selectAllCB(this);" />
									</th>
								</s:if>
								<s:else>
									<s:set name="flag" value="false" />
								</s:else>
							</tr>
							<s:iterator value="reconcileList" status="indx">
							<tr >
									<td align="center">
										<s:property value="#indx.index + 1" />
									</td>
									<td align="center">
										<s:property value="transactionType" />
									</td>
									<td align="center">
										<s:property value="walletName" />
									</td>
									<td align="center">
										<s:property value="mobileNo" />
									</td>
									<td align="center">
										<s:property value="ticketNumber" />
									</td>
									<td align="center">
										<s:set name="amount" value="amount"/>
										<%= FormatNumber.formatNumberForJSP(pageContext.getAttribute("amount")) %>
									</td>
									<td align="center">
										<s:date name="transactionDate" format="yyyy-MM-dd HH:mm:ss" />
									</td>
									<td align="center">
										<s:property value="status" />
									</td>
									<td align="center">
										<s:property value="transactionId" />
									</td>
									<td align="center">
										<s:property value="merchantStatus" />
									</td>
									<td align="center">
										<s:if test="%{merchantTransId == null && #flag == true}">
											<s:if test="%{transactionType eq 'PWT' and walletName eq 'DYA'}">
												NA<s:hidden name="merchantTransId" cssClass="merchantTransId" value="%{'NA'}" />
											</s:if>
											<s:else>
												<input type="text" name="merchantTransId" class="merchantTransId" size="8" maxlength="15" />
											</s:else>
										</s:if>
										<s:else>
											<s:property value="merchantTransId" />
											<s:hidden name="merchantTransId" cssClass="merchantTransId" value="%{merchantTransId}" />
										</s:else>
									</td>
									<s:if test="%{#flag == true}">
										<td align="center">
											<input type="checkbox" name="transId" class="transId" value='<s:property value="transactionId"/>' />
										</td>
									</s:if>
								</tr>
							</s:iterator>
						</table>
					</div>
					<br/>
					<center>
						<s:if test="%{status eq 'INITIATED'}">
							<s:if test="%{transactionType eq 'SALE'}">
								<s:if test="%{walletType eq 'DYA'}">
									<input type="button" value="Process Transaction" onclick="processRequest('DONE');" />
								</s:if>
								<input type="button" value="Failed Transaction" onclick="processRequest('FAIL');" />
							</s:if>
							<s:elseif test="%{transactionType eq 'PWT'}">
								<input type="button" value="Process Transaction" onclick="processRequest('DONE');" />
							</s:elseif>
						</s:if>
					</center>
				</s:if>
				<s:else>
					<center><h3>No Record Exist</h3></center>
				</s:else></s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_ReconciliationSearchMTN.jsp,v $ $Revision: 1.3 $
</code>