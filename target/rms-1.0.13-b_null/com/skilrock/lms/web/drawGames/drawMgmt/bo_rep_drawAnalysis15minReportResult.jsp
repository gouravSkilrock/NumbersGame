<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@page import="com.skilrock.lms.dge.beans.AnalysisReportDrawBean"%>



<%
	response.setDateHeader("Expires", 0);
%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
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

					<s:if
						test="#session.DRAW_DATA_LIST !=null && #session.DRAW_DATA_LIST.size>0">
						<tr>
							<th colspan="10" align="center">
								<s:text name="label.draw.data.last.fift.min"/>
						
							</th>
						</tr>
						<tr>
							<th nowrap="nowrap">
								<s:text name="label.tckt.no"/> 
						
							</th>
							<th>
								<s:text name="label.purchase.time"/> 
						 
							</th>
							<th>
								<s:text name="label.play.type"/> 
						 
							</th>

							<th>
								<s:text name="label.pick.record"/> 
						
							</th>
							<th>
								<s:text name="label.amount"/> 
						
							</th>
							<th>
								<s:text name="label.win.amt"/> 
						
							</th>


						</tr>

						<s:set name="repBean" id="repBean" value="#session.DRAW_DATA_LIST" />
						<s:iterator id="beanCart" value="%{#repBean}">
							<tr class="startSortable">
								<td align="left" nowrap="nowrap">
									<s:property value="ticketNumber" />


								</td>

								<td>
									<s:property value="purchaseTime" />


								</td>

								<td>
									<s:property value="playType" />


								</td>


								<td>
									<s:property value="playerPickData" />
								</td>
								<td>
									<s:property value="totalAmount" />

								</td>
								<td>
									<s:property value="Winningamount" />
								</td>

							</tr>
						</s:iterator>

					</s:if>
					<s:else>
						<tr>
							<td colspan="6" align="center">
								<s:if test="%{message==null}">
									<s:text name="msg.no.record.process"/>
				
								</s:if>
								<s:else>
									<s:property value="message" />
								</s:else>

							</td>
						</tr>
					</s:else>

				</table>

			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawAnalysis15minReportResult.jsp,v $ $Revision:
	1.1.2.3.4.2.2.1 $
</code>