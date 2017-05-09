<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" />
	</head>

	<body>
		<h3>
			<s:text name="label.search.ret.non.cons.inv" />
		</h3>

		<div id="err_div"></div>

		<table nowrap="nowrap" id='id_of_table' width="100%" border="1"
			cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">

			<tr>
				<td nowrap="nowrap">
					<s:text name="label.org.name" />
				</td>

				<s:if test="#session.MODEL_NAME_MAP.size>0">

					<s:iterator id="MODEL_NAME_MAP" value="#session.MODEL_NAME_MAP">

						<td>
							<s:property value="%{value}" />
						</td>
					</s:iterator>
				</s:if>
			</tr>


			<s:if test="#session.INV_DET_MAP.size>0">

				<s:iterator value="#session.INV_DET_MAP">


					<tr>
						<td nowrap="nowrap" colspan="1">

							<s:property value="%{Key}" />

						</td>
						<s:iterator value="%{value}">
							<s:set name="modelCount" value="%{value}"></s:set>
							<td>
								<s:if test="%{#modelCount==null}">N.A</s:if>
								<s:else>

									<s:property value="%{value}" />
								</s:else>
							</td>
						</s:iterator>

					</tr>
				</s:iterator>

			</s:if>

			<s:else>
				<tr>
					<td nowrap="nowrap" colspan="9" align="center">
						<s:text name="msg.no.result" />
					</td>
				</tr>
			</s:else>
		</table>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_consNonConsSearchRetailer_searchResult.jsp,v $
	$Revision: 1.3 $
</code>