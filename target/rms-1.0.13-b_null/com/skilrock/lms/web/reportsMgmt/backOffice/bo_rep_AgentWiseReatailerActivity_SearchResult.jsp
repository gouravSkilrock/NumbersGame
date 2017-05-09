<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	

	
</head>
		<body>


			<!--<s:set name="dataBeanMap" value="retActivityMap"/>-->
			<s:set name="gameBeanList" value="gameSaleDataListMain"/>
	
			<s:if test="retActivityMap.size>0">
			<div align="left">
				<s:form action="exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="AgentWiseRetailerActivtiy" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div> 
			<div id="tableDataDiv">
			<table align="center">
			<tr>
			<td><h3><u><s:text name="Agent"/> Wise <s:text name="Retailer"/> Activity Report for Date: <s:property value="startDate"/></u></h3></td>
			</tr>
			</table>
			<s:set name="colSpan" value="%{#gameBeanList.size()}" />
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					
					<tr>
						<th rowspan="2">
							S/No.
						</th>
						<th rowspan="2">
							<s:text name="Agent">
							</s:text>
							Name
						</th>
						<th rowspan="2">
							Location
						</th>
						<th rowspan="2">
							Status
						</th>
						<th rowspan="2">
							Total POS
						</th>
						<th rowspan="2">
							POS With
							<s:text name="Agent">
							</s:text>
						</th>
						<th colspan="4">
							Assigned Pos
						</th>

						<th rowspan="2">
							POS With
							<s:text name="Retailer">
							</s:text>
						</th>

						<th colspan="<s:property value='#colSpan'/>">
						
							Sales
						</th>
						<th colspan="<s:property value='#colSpan'/>">
							ARPT
						</th>

					</tr>
					<tr>
						<th>
							New Login
						</th>
						<th>
							No Sales
						</th>
						<th>
							Active
						</th>
						<th>
							Active %
						</th>

						 <s:iterator id="beanCart" value="%{#gameBeanList}">
							<th>
								(<s:property value="gameName" />)
							</th>
						</s:iterator>
						<s:iterator id="beanCart" value="%{#gameBeanList}">
							<th>
								(<s:property value="gameName" />)
							</th>
						</s:iterator>
					</tr>
					<s:set name="count" value="0" />
					<s:iterator id="result" value="retActivityMap">
						<s:set name="agentWiseRetActivityBean" value="%{value}"></s:set>
						<s:if test="#agentWiseRetActivityBean.agentName!='Tot@l'">

							<tr class="startSortable">
								<td align="right">
									<s:set name="count" value="#count+1" />

									<s:property value="#count" />
								</td>
								<td align="left">
									<s:property value="#agentWiseRetActivityBean.agentName" />
								</td>
								<td align="left">
									<s:property value="#agentWiseRetActivityBean.city" />
								</td>
								<td align="left">
									<s:if test="#agentWiseRetActivityBean.orgStatus=='ACTIVE'">
										<div style="color: green">
											<s:property value="#agentWiseRetActivityBean.orgStatus" />
										</div>
									</s:if>
									<s:elseif test="#agentWiseRetActivityBean.orgStatus=='INACTIVE'">
										<div style="color: red">
											<s:property value="#agentWiseRetActivityBean.orgStatus" />
										</div>
									</s:elseif>
									<s:else>
										<div style="color: purple">
											<s:property value="#agentWiseRetActivityBean.orgStatus" />
										</div>
									</s:else>
								</td>
								<td align="right">
									<s:set name="totalPos"
										value="%{#agentWiseRetActivityBean.assignedTotal+#agentWiseRetActivityBean.notAssignedTotal}" />
									<s:property value="#totalPos" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.notAssignedTotal" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.newLoginRet" />
								</td>
								<td align="right">
									<s:set name="nosale"
										value="%{#agentWiseRetActivityBean.assignedTotal-#agentWiseRetActivityBean.activeRet-#agentWiseRetActivityBean.newLoginRet}" />
									<s:property value="#nosale" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.activeRet" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.activeRetPercentage" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.assignedTotal" />
								</td>

								<s:set name="gameBeanList" value="#agentWiseRetActivityBean.gameSaleDataList" />
								<s:iterator id="beanCart" value="%{#gameBeanList}">
									<th align="right">
										<s:property value="totalSale" />
									</th>
								</s:iterator>
								<s:iterator id="beanCart" value="%{#gameBeanList}">
									<th align="right">
										<s:property value="%{avgSalePerTerminal}" />
									</th>
								</s:iterator>

							</tr>
						</s:if>
						<s:else>
						<tr>
								<td colspan="4" align="center">
									Total
								</td>
								<td align="right">
									<s:set name="totalPos"
										value="%{#agentWiseRetActivityBean.assignedTotal+#agentWiseRetActivityBean.notAssignedTotal}" />
									<s:property value="#totalPos" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.notAssignedTotal" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.newLoginRet" />
								</td>
								<td align="right">
									<s:set name="nosale"
										value="%{#agentWiseRetActivityBean.assignedTotal-#agentWiseRetActivityBean.activeRet-#agentWiseRetActivityBean.newLoginRet}" />
									<s:property value="#nosale" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.activeRet" />
								</td>
								<td>
									<s:property value="#agentWiseRetActivityBean.activeRetPercentage" />
								</td>
								<td align="right">
									<s:property value="#agentWiseRetActivityBean.assignedTotal" />
								</td>

								<s:set name="gameBeanList" value="#agentWiseRetActivityBean.gameSaleDataList" />
								<s:iterator id="beanCart" value="%{#gameBeanList}">
									<th align="right">
										<s:set name="totalSale" value="totalSale" /><%=FormatNumber.formatNumberForJSP(pageContext
															.getAttribute("totalSale"))%></th>
								</s:iterator>
								<s:iterator id="beanCart" value="%{#gameBeanList}">
									<th align="right">
										<s:set name="totalARTP" value="%{avgSalePerTerminal/#count}" />
										<%=FormatNumber.formatNumberForJSP(pageContext
															.getAttribute("totalARTP"))%>

									</th>
								</s:iterator>
							</tr>
						</s:else>
					</s:iterator>

				</table>
					</div>	
		 </s:if>
			<s:else>
						<table  align="center"><tr><td>
								No Records to Process
								</td></tr>
						</table>
			</s:else>
	
		</body>
</html>		