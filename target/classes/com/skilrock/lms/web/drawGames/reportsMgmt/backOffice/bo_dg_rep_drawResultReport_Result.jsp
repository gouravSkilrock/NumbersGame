<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>

		<div id="wrap">
			<div id="top_form">


				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th colspan="6" align="center">
							<s:text name="label.draw.results"/>
						</th>
					</tr>
					<tr>
						<th>
							<s:text name="label.draw.time" />
						</th>
						<th>
							<s:text name="label.draw.status" />
						</th>
						<th>
							<s:text name="label.win.res" />
						</th>
						<th>
							<s:text name="label.rank.res" />
						</th>
					</tr>

					<s:if test="#session.DRAW_DATA_LIST.size>0">
						<s:iterator id="beanCart" value="#session.DRAW_DATA_LIST">
							<tr>
								<td align="left">
									<s:property value="%{drawDateTime.replace('.0','')}" />
								</td>
								<td align="left">
									<s:property value="drawStatus" />
								</td>
								<s:if test="%{winningResult==null}">
									<td align="left">
										N.A.
									</td>
								</s:if>
								<s:else>
									<td align="left"
										style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 155px">
										<s:property value="winningResult.toUpperCase()" />
										&nbsp;
									</td>
								</s:else>
								<s:if test="%{winningResult==null}">
									<td align="left">
										N.A.
									</td>
								</s:if>
								<s:else>
									<td align="left"
										style="font-weight: bold; font-size: 13px; word-break: break-all;; width: 455px">
										<table width="100%" border="1" cellpadding="3" cellspacing="0"
											bordercolor="#CCCCCC" align="center">
											<tr>
												<th align="center">
													<s:text name="label.prize.amt" />
												</th>
												<th align="center">
													<s:text name="label.nbr.winner" />
												</th>
												<th align="center">
													<s:text name="label.nbr.match" />
												</th>
											</tr>
											<s:iterator value="%{drawResultBean}">
												<tr>
													<td align="right">
														<s:property value="prizeAmount" />
													</td>

													<td align="right">
														<s:property value="nbrOfWinners" />
													</td>
													<td>
														<s:property value="nbrOfMatch.replaceAll('with','+')" />
													</td>
												</tr>
											</s:iterator>
										</table>
									</td>
								</s:else>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:text name="msg.no.record.process" />
							</td>
						</tr>
					</s:else>

				</table>


			</div>
		</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_dg_rep_drawResultReport_Result.jsp,v $ $Revision:
	1.1.2.2.2.5 $
</code>