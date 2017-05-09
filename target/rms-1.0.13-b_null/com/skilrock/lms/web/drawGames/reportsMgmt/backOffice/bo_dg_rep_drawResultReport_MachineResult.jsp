<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">


				<table width="90%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th colspan="7" align="center">
							Draw Results
						</th>
					</tr>

					<tr>
						<th colspan="7" align="center"
							style="font-weight: bold; font-size: 15px; word-break: break-all;">

							<s:if test="#session.DRAW_NAME != null">
								<s:property value="#session.DRAW_NAME" />
							</s:if>
							<s:else>
								<s:property value="#session.GAME_NAME" />
							</s:else>
						</th>
					</tr>

					<tr>
						<th align="center">
							Ev.
						</th>
						<th align="center">
							Date
						</th>
						<!-- <th>
							Draw Status
						</th> -->
						<th align="center">
							Draw Name
						</th>
						<th align="center">
							<s:property value="start_date.split('-')[0]"/>-<s:property value="end_Date.split('-')[0]"/>
						</th>

						<th align="center">
							Total
						</th>
						<!-- <th>
							Rank Result
						</th> -->
						<th align="center">
							Machine
						</th>
						<th align="center">
							Total
						</th>
					</tr>

					<s:if test="#session.DRAW_DATA_LIST.size>0">
						<s:iterator id="beanCart" value="#session.DRAW_DATA_LIST"
							status="customStat">
							<tr class="startSortable">
								<td align="center">
									<s:property value="#customStat.index+1" />
								</td>
								<td align="center">
									<s:property
										value="%{drawDateTime}" />
								</td>
								<td align="center">
									<s:if test="#session.DRAW_NAME_COLUMN != NULL">
										<s:property value="%{drawName}" />
									</s:if>
									<s:else>
										N.A.
									</s:else>
								</td>
								<!-- <td align="left">
									<s:property value="drawStatus" />
								</td> -->
								<s:if test="%{winningResult==null}">
									<td align="center">
										N.A.
									</td>
								</s:if>
								<s:else>
									<td align="center"
										style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 100px">
										<s:property
											value="winningResult.replaceAll(',','-').toUpperCase()" />
										&nbsp;
									</td>
								</s:else>

								<s:if test="%{winningResultSum == 0}">
									<td align="center">
										N.A.
									</td>
								</s:if>
								<s:else>
									<td align="center"
										style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 100px">

										<s:property value="winningResultSum" />
										&nbsp;
									</td>
								</s:else>

								<!-- <s:if test="%{winningResult==null}">
									<td align="left">
										N.A.
									</td>
								</s:if>
								<s:else>
									<td align="left"
										style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 255px">
										<table width="100%" border="1" cellpadding="3" cellspacing="0"
											bordercolor="#CCCCCC" align="center">
											<tr>
												<th align="center">
													Prize Amount
												</th>
												<th align="center">
													Number of Winners
												</th>
											</tr>
											<s:iterator id="beanMap" value="%{przMap}">
												<tr>
													<td align="right">
														<s:property value="key" />
													</td>
													<td align="right">
														<s:property value="%{(value.toString()).replace('.0','')}" />
													</td>
												</tr>
											</s:iterator>
										</table>
									</td>
								</s:else> -->
								<s:if test="%{machineResult==null}">
									<td align="center">
										N.A.
									</td>
								</s:if>
								<s:else>
								<td align="center"
									style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 100px">
									<s:property
										value="machineResult.replaceAll(',','-').toUpperCase()" />
								</td>
								</s:else>

								<s:if test="%{machineResultSum == 0}">
									<td align="center">
										N.A.
									</td>
								</s:if>
								<s:else>
									<td align="center"
										style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 100px">

										<s:property value="machineResultSum" />
										&nbsp;
									</td>
								</s:else>

							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="7" align="center">
								No Records to Process
							</td>
						</tr>
					</s:else>

				</table>




			</div>
		</div>



	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_drawResultReport_MachineResult.jsp,v $ $Revision:
	1.1.2.2.2.5 $
</code>