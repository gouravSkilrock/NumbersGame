<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.skilrock.lms.beans.DateBeans"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>
		<s:if test="%{lastPurgDate!=null}">
			<div align="center">
				<h2>
					<u><s:text name="label.select.st.date.after"/> <s:property value="%{lastPurgDate}" />
					</u>
				</h2>
			</div>
		</s:if>
		<s:else>
<div align="center">
<s:if test="%{drawPanelWiseSaleList.size()>0}">
<s:form action="exportReportAsExcel"  method="post" enctype="multipart/form-data">
<s:hidden name="gameNo"></s:hidden>
<s:hidden id="reportName" name="reportName" value="PanelWiseSale_Report"/>
<s:hidden id="reportData" name="reportData" />
<s:submit key="btn.exportasexcel" cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px" onclick="getData();"/>
</s:form>
</s:if>
</div>
			<div id="tableDataDiv">
				<div align="center">
					<h3>
						<u>
						<s:text name="Report" /> <s:text name="label.for.draw.wise.sale.panel"/> <s:text name="label.from"/> : <s:property
							value="%{displaySd}" /> <s:text name="label.to"/> <s:property value="%{displayEd}" /></u>
					</h3>
				</div>

					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC" align="center" id="expandTable">
				
					<s:set name="countVal" value="0"></s:set>
					
						<s:iterator id="result" value="%{drawPanelWiseSaleList}"
							status="taskIndex">
							<s:if test="#taskIndex.first == true">
								<s:iterator id="result" value="%{panelSaleMap}"
									status="taskIndex">
									<s:set name="countVal" value="#countVal+1"></s:set>
								</s:iterator>
								<s:set name="countVal" value="#countVal+2"></s:set>
							</s:if>
						</s:iterator>
					
					<tr>
						<th align="center" colspan="<s:property value='#countVal'/>">
							<s:property value="%{orgName}" />
						</th>
					</tr>
					<tr>
						<th align="center" colspan="<s:property value='#countVal'/>">
							<s:property value="%{orgAddress}" />
						</th>
					</tr>
					<s:if test="%{drawPanelWiseSaleList.size()>0}">
						<s:iterator id="result11" value="%{drawPanelWiseSaleList}"
							status="taskIndex">

							<s:if test="#taskIndex.first == true">
								<tr align="center">
									<th nowrap="nowrap">
										<s:text name="label.draw.id"/>
									</th>
									<th nowrap="nowrap">
										<s:text name="label.draw.date.time"/>
									</th>
									<s:iterator id="result" value="%{panelSaleMap}"
										status="taskIndex">
										<th nowrap="nowrap">
											<s:property value="key" />
										</th>
									</s:iterator>
								</tr>
							</s:if>
							<tr class="startSortable" style="white-space: nowrap;">
								<td nowrap="nowrap" align="left">
									<s:property value="drawId" />
								</td>
								<td nowrap="nowrap" align="left">
									<s:property value="drawDateTime" />
								</td>
								<s:iterator id="result111" value="%{panelSaleMap}"
									status="taskIndex">
									<td nowrap="nowrap" align="right">
										<s:property value="value" />
									</td>
								</s:iterator>
							</tr>
						</s:iterator>
						<tr id="totalRow" style="font-weight: bold" align="right">
			<th align="right" style="border-right: 0px;" >
				<s:text name="label.total"/>
			</th>
			<th align="center" style="border-left: 0px;">
				
			</th>
			</tr>
					</s:if>
					<s:else>
						<tr>
							<th align="center">
								<s:text name="msg.no.record"/>
							</th>
						</tr>
					</s:else>

				</table>
			</div>
		</s:else>



	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_panelWiseSale_Result.jsp,v $ $Revision:
	1.1.4.31.2.5 $
</code>