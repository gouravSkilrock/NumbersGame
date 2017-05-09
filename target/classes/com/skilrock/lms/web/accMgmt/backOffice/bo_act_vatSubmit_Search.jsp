
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<div id="wrap">

			<div id="top_form">
				<div id="searchDiv">
				<table id="searchTlb" width="684" border="1" cellpadding="3"
					cellspacing="0" bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="center" colspan="5">
							Search Result
						</th>
					</tr>

					<s:if test="#session.TASK_VAT_SEARCH_RESULTS.size !=0">
						<tr>
							<th>
								Month
							</th>
							<th>
								Service
							</th>
							<th>
								Status
							</th>
							<th>
								Amount
							</th>
							<th>
								Details
							</th>


						</tr>
						<s:iterator id="task" value="#session.TASK_VAT_SEARCH_RESULTS"
							status="taskIndex">
							<s:set name="stramount" value="amount" />
							<tr name="searchRow" id="<s:property value="#taskIndex.index"/>">
								<s:set name="AMOUNT" value="amount"></s:set>

								<td>
									<s:property id="task" value="%{month}" />
								</td>
								<td>
									<s:property value="serviceName" />
								</td>
								<td>
									<s:property value="status" />
								</td>
								<td>
									<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("stramount"))%></td>
								<td>
									<s:a targets="result1" theme="ajax"
										href="com/skilrock/lms/account/bo_act_vatSubmit_Save.action?taskId=%{taskId}&transactionType=%{transactionType}&month=%{month}&amt=%{amount}&serviceCode=%{serviceCode}"
										onclick="removeRow('%{#taskIndex.index}');">Submit</s:a>
								</td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="5" align="center">
								No Records to Process for VAT Submission
							</td>
						</tr>
					</s:else>

				</table>
				</div>
				<div id="ans">
				</div>
				<div id="subCheck" style="position: absolute;"></div>

			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_vatSubmit_Search.jsp,v $
$Revision: 1.2 $
</code>