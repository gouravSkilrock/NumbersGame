<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'bo_rep_actInactInvStatus_reportSearch.jsp'
			starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/ActInactReportjs.js"></script>
	</head>
	<body>
	
	<div align="left">
			<s:form action="com/skilrock/lms/web/reportsMgmt/exportToExcel.action" method="post"
				enctype="multipart/form-data">
				<s:hidden id="tableValue" name="valueToSend" />
				<s:submit value="Export As Excel"
					cssStyle="margin-right:-400px;background:#77b3d7;font-size:15px"
					onclick="getData();" />
			</s:form>
	</div>
	<div id="tableDataDiv">
		<div id="wrap">
			<div id="top_form">

				<table width="1000px" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center" id="tableDataDiv">


					<tr>
						<s:if test="%{reportType==4}">
							<td colspan="8">
						</s:if>
						<s:elseif test="%{reportType==2}">
							<td colspan="6">
						</s:elseif>
						<s:else>
							<td colspan="7">
						</s:else>
						<s:if test="%{reportType==1}">
							<center>
								<h3>
									<s:text name="label.all"/>
									<s:if test="%{creteria eq 'GT' or creteria eq 'EQ'}">
										<s:text name="label.active"/>
									</s:if>
									<s:else>
										<s:text name="label.inact"/>
									</s:else>
									<s:text name="label.terminal.for.duration"/>
									<br />
									<s:text name="%{startDate}" />
									<s:text name="label.to"/>
									<s:text name="%{endDate}" />
								</h3>
							</center>
						</s:if>
						<s:if test="%{reportType==3}">
							<center>
								<h3>
									<s:text name="%{cityName}" />
									- <s:text name="label.all"/>
									<s:if test="%{creteria eq 'GT' or creteria eq 'EQ'}">
										<s:text name="label.active"/>
									</s:if>
									<s:else>
										<s:text name="label.inact"/>
									</s:else>
									<s:text name="label.terminal.for.duration"/>
									<br />
									<s:text name="%{startDate}" />
									<s:text name="label.to"/>
									<s:text name="%{endDate}" />
								</h3>
							</center>
						</s:if>
						<s:if test="%{reportType==2}">
							<center>
								<h3>
									<s:text name="%{agentCode}" />
									-
									<s:if test="%{creteria eq 'GT' or creteria eq 'EQ'}">
										<s:text name="label.active"/>
									</s:if>
									<s:else>
										<s:text name="label.inact"/>
									</s:else>
									<s:text name="label.terminal.for.duration"/>
									<br />
									<s:text name="%{startDate}" />
									<s:text name="label.to"/>
									<s:text name="%{endDate}" />
								</h3>
							</center>
						</s:if>
						<s:if test="%{reportType==4}">
							<center>
								<h3>
									<s:text name="label.regional.breakdown"/>
									<s:if test="%{creteria eq 'GT' or creteria eq 'EQ'}">
										<s:text name="label.active"/>
									</s:if>
									<s:else>
										<s:text name="label.inact"/>
									</s:else>
									<s:text name="label.terminal.for.duration"/>
									<br />
									<s:text name="%{startDate}" />
								</h3>
							</center>

						</s:if>
						</td>
					</tr>

					<tr>
						<th align="center">
							<s:text name="label.sNo"/>
						</th>
						<th align="center">
							<s:if test="%{reportType==4}">
								<s:text name="Agent" />&nbsp;<s:text name="label.name"/>/<s:text name="Agent" />&nbsp;<s:text name="label.id"/>
						</s:if>
							<s:else>
								<s:text name="Retailer" />&nbsp;&nbsp;<s:text name="label.id"/>
						</s:else>
						</th>
						<th align="center">
							<s:text name="label.region"/>
						</th>

						<s:if test="%{reportType==1 || reportType==3}">
							<th align="center">
								<s:text name="Agent" />
								&nbsp;<s:text name="label.name"/>/
								<s:text name="Agent" />
								&nbsp;<s:text name="label.id"/>
							</th>
						</s:if>

						<s:if test="%{reportType==4}">
							<th align="center">
								<s:text name="label.no.of.pos"/>
							</th>
						</s:if>

						<th align="center">
							<s:if test="%{reportType==4}">
							<s:text name="label.active"/>
						</s:if>
							<s:else>
							<s:text name="label.idle.time"/>
						</s:else>
						</th>
						<s:if test="%{reportType==4}">
							<th align="center">
								<s:text name="label.inact"/>
							</th>
						</s:if>
						<th> 
						Sale Amount
						</th>
						<th>
						Zone
						</th>

					</tr>

					<s:if test="%{reportType==4}">
						<s:iterator id="beanCart" value="%{actInactInvStatusList}"
							status="taskIndex">

							<tr class="startSortable">
								<td align="left">
									<s:property value="#taskIndex.index+1" />
								</td>
								<td align="left">
									<s:property value="agentId" />
								</td>
								<td align="left">
									<s:property value="region" />
								</td>
								<td align="left">
									<s:property value="totalPos" />
								</td>
								<td align="left">
									<s:property value="activePos" />
								</td>
								<td align="left">
									<s:property value="inActivePos" />
								</td>
								<td align="left">
								<s:property value="saleAmount"/>
								</td>
								<td align="left">
								<s:property value="zone"/>
								</td>
							</tr>
							<s:else>
								<tr>

								</tr>
							</s:else>
						</s:iterator>
					</s:if>
					<s:elseif test="%{reportType==1 || reportType==3}">
						<s:iterator id="beanCart" value="%{actInactInvStatusList}"
							status="taskIndex">

							<tr class="startSortable">
								<td align="left">
									<s:property value="#taskIndex.index+1" />
								</td>
								<td align="left">
									<s:property value="retId" />
								</td>
								<td align="left">
									<s:property value="region" />
								</td>
								<td align="left">
									<s:property value="agentId" />
								</td>
								<td align="left">
									<s:property value="idleTime" />
								</td>
								<td align="left">
								<s:property value="saleAmount"/>
								</td>
								<td align="left">
								<s:property value="zone"/>
								</td>
							</tr>
							<s:else>
								<tr>
								</tr>
							</s:else>
						</s:iterator>
					</s:elseif>
					<s:elseif test="%{reportType==2}">
						<s:iterator id="beanCart" value="%{actInactInvStatusList}"
							status="taskIndex">

							<tr class="startSortable">
								<td align="left">
									<s:property value="#taskIndex.index+1" />
								</td>
								<td align="left">
									<s:property value="retId" />
								</td>
								<td align="left">
									<s:property value="region" />
								</td>
								<td align="left">
									<s:property value="idleTime" />
								</td>
								<td align="left">
								<s:property value="saleAmount"/>
								</td>
								<td align="left">
								<s:property value="zone"/>
								</td>
							</tr>
							<s:else>
								<tr>
								</tr>
							</s:else>
						</s:iterator>
					</s:elseif>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:text name="msg.no.record.process"/>
							</td>
						</tr>
					</s:else>

				</table>
			</div>
		</div>
		</div>
</body>

	
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_actInactInvStatus_reportSearch.jsp,v $ $Revision:
	1.1.1.1$
</code>