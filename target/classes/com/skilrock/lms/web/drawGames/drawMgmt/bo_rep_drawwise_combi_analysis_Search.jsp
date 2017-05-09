<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%
	response.setDateHeader("Expires",0);
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
		<div id="wrap">
			<div id="top_form">
				<s:if test="%{message!=null}">
					<s:property value="message" />
				</s:if>
				<s:else>
					<s:set name="gameDevName" value="gameDevName"></s:set>
					<s:set name="topCombiBean" value="topCombiBean"></s:set>
					<s:if test="%{topCombiBean != null && topCombiBean.size > 0}">
					<div align="left">
						<s:form action="bo_rep_drawwise_combi_analysis_export.action"
							method="post" enctype="multipart/form-data">
							<s:hidden id="gameDevName" name="gameDevName" value="%{gameDevName}"></s:hidden>
							<s:hidden id="tableValue" name="valueToSend" />
							<s:hidden id="reportTitle" name="Combination Analysis Report" />
							<s:submit value="Export As Excel"
								cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px"
								onclick="getData();" />
						</s:form>
					</div>
					</s:if>
					
					<div id="tableDataDiv">
						<s:if test="%{topCombiBean != null && topCombiBean.size > 0}">
							<s:if test="gameDevName != 'TwelveByTwentyFour'">
								<table width="100%" border="0" cellpadding="3" cellspacing="0"
									bordercolor="#CCCCCC" align="left" style="margin-left: -100px">
							</s:if>
							<s:else>
								<table width="100%" border="0" cellpadding="3" cellspacing="0"
									bordercolor="#CCCCCC" align="center" style="margin-left: -100px">
							</s:else>
								<tr>
									<td>
										<table width="500px" border="1" cellpadding="3"
											cellspacing="0" bordercolor="#CCCCCC" align="left">
											<tr>
												<th colspan="3" align="center">
													<s:text name="label.combi.wise.analys.of" /> <s:text name="%{drawName}" />
												</th>
											</tr>
											<tr>
												<s:if test="gameDevName == 'TwelveByTwentyFour'">
													<th align="center">
														<s:text name="label.top.d12.combi" />
													</th>
												</s:if>
												<s:else>
													<th align="center">
														<s:text name="label.top.p2.dcp2.d2.dcd2.banker.combi" />
													</th>
												</s:else>
												<th align="center">
													<s:text name="label.sale.amt" />
												</th>
												<th align="center">
													<s:text name="label.combi.count" />
												</th>
											</tr>
											<s:iterator id="beanCart" value="%{topCombiBean}">

												<tr class="startSortable">
													<td align="left">
														<s:property value="combinition" />
													</td>
													<td align="left">
														<s:set name="saleSet" value="totalSale"></s:set>
														<%=FormatNumber.formatNumberForJSP(pageContext
								.getAttribute("saleSet"))%>
													</td>
													<td align="left">
														<s:property value="combiCount" />
													</td>
												</tr>
											</s:iterator>
										</table>
									</td>
									<s:if test="gameDevName != 'TwelveByTwentyFour'">
										<td>
											<table width="500px" border="1" cellpadding="3"
												cellspacing="0" bordercolor="#CCCCCC" align="left">
												<tr>
													<th colspan="3" align="center">
														<s:text name="label.nbr.wise.analys.of" />
														<s:text name="%{drawName}" />
													</th>
												</tr>
												<tr>
													<th align="center">
														<s:text name="label.top.instr.of" />
														<br />
														<s:text name="label.plr" />
													</th>
	
													<th align="center">
														<s:text name="label.sale.amt" />
													</th>
													<th align="center">
														<s:text name="label.instr.count" />
													</th>
												</tr>
												<s:iterator id="beanCart" value="%{topNumbBean}">
													<tr class="startSortable">
														<td align="left">
															<s:property value="combinition" />
														</td>
	
														<td align="left">
															<s:set name="saleSet" value="totalSale"></s:set>
															<%=FormatNumber.formatNumberForJSP(pageContext
									.getAttribute("saleSet"))%>
														</td>
														<td align="left">
															<s:property value="combiCount" />
														</td>
													</tr>
												</s:iterator>
										</table>
									</td>
									</s:if>
								</tr>
								<s:if test="gameDevName != 'TwelveByTwentyFour'">
									<tr>
										<td colspan="2">
											<table width="1000px" border="1" cellpadding="3"
												cellspacing="0" bordercolor="#CCCCCC" align="center">
												<s:set name="countVal" value="0"/>
												<s:iterator id="beanCart" value="%{topNumbDetailList}"
													status="taskIndex">
													<s:if test="#taskIndex.first == true">
														<s:iterator id="beanCart" value="%{playerInterestMap}"
															status="taskIndex1">
															<s:set name="countVal" value="%{#countVal+2}"></s:set>
														</s:iterator>
													</s:if>
	
												</s:iterator>
												
												<tr>
													<th rowspan="3" align="center">
														<s:text name="label.top.instr.of" />
														<br />
														<s:text name="label.plr" />
													</th>
													<th align="center" colspan='<%=pageContext.getAttribute("countVal")%>'>
														<s:text name="label.bet.types" />
													</th>
													<th rowspan="2" colspan="2" align="center">
														<s:text name="label.total" />
													</th>
												</tr>
	
	
												<tr>
													<s:iterator id="beanCart" value="%{topNumbDetailList}"
														status="taskIndex">
														<s:if test="#taskIndex.first == true">
															<s:iterator id="beanCart" value="%{playerInterestMap}"
																status="taskIndex1">
																<th nowrap="nowrap" colspan="2" align="center">
																	<s:property value="key" />
																</th>
															</s:iterator>
														</s:if>
	
													</s:iterator>
												</tr>
												<tr>
													<s:iterator id="beanCart" value="%{topNumbDetailList}"
														status="taskIndex">
														<s:if test="#taskIndex.first == true">
															<s:iterator id="beanCart" value="%{playerInterestMap}"
																status="taskIndex1">
																<th nowrap="nowrap" align="center">
																	<s:text name="label.count" />
																</th>
																<th nowrap="nowrap" align="center">
																	<s:text name="label.sale" />
																</th>
															</s:iterator>
															<th nowrap="nowrap" align="center">
																<s:text name="label.count" />
															</th>
															<th nowrap="nowrap" align="center">
																<s:text name="label.sale" />
															</th>
														</s:if>
	
													</s:iterator>
												</tr>
	
												<s:iterator id="beanCart" value="%{topNumbDetailList}"
													status="taskIndex">
													<tr class="startSortable">
														<td>
															<s:property value="combinition" />
														</td>
														<s:set name="countTotal" value="0" />
														<s:set name="saleTotal" value="0.0" />
														<s:iterator id="beanCart1" value="%{playerInterestMap}"
															status="taskIndex1">
	
															<s:iterator id="beanCart2" value="%{value}"
																status="taskIndex2">
																<td>
																	<s:set name="countValue" value="%{key}"></s:set>
																	<s:property value="key" />
																	<s:set name="countTotal"
																		value="%{#countTotal+#countValue}" />
																</td>
																<td>
																	<s:set name="saleValue" value="%{value}"></s:set>
																	<s:property value="value" />
																	<s:set name="saleTotal" value="%{#saleTotal+#saleValue}" />
	
																</td>
															</s:iterator>
														</s:iterator>
														<th align="left" noWrap="nowrap">
															<%=pageContext.getAttribute("countTotal")%>
														</th>
														<th align="left" noWrap="nowrap">
															<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleTotal"))%>
														</th>
													</tr>
												</s:iterator>
	
	
											</table>
										</td>
									</tr>
								</s:if>
							</table>
						</s:if>
					</div>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:text name="msg.no.record.process" />
							</td>
						</tr>
					</s:else>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawwise_combi_analysis_Search.jsp,v $ $Revision:
	1.1.1.1.1.1 $
</code>