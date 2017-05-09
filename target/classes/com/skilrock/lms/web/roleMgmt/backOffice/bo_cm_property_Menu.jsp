<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>
			function updateRow(row){
				var code = _id.n("code")[row].value;
				var value = escape(_id.n("value")[row].value);
				var status = _id.n("status")[row].value;
				var description = _id.n("description")[row].value;
				var div = _id.n("update")[row];
				var url='bo_cm_property_Save.action?code='+code+'&value='+value+'&status='+status+'&description='+description;
				var _resp = _ajaxCall(url);
				if(_resp.data!=null){
					div.style.display = "block";
				}
				return true;
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Edit Configuration Variables
				</h3>
				<s:form name="config" action="bo_cm_property_Save">
					<table width="100%" border="1" cellpadding="2" cellspacing="0"
						bordercolor="#CCCCCC" align="center" valign="top">
						<tr>
							<th>
								S.No
							</th>
							<th>
								Property Name
							</th>
							<th>
								Status
							</th>
							<th>
								Value
							</th>
							<th>
								Description
							</th>
							<th>
								Update
							</th>
							<th>
								Message
							</th>
						</tr>
						<s:iterator id="task" value="#session.CONFIG_LIST"
							status="taskIndex">
							<tr>

								<td>
									<s:property value="#taskIndex.index+1" />
									<s:hidden name="code" value="%{code}" />
								</td>
								<td>
									<s:property value="name" />
									&nbsp;
								</td>
								<s:if test="%{editable=='NO'}">
									<td>
										<s:property value="%{status}" />
										<s:hidden name="status" value="%{status}" />
									</td>
									<td>
										<s:property value="%{value}" />
										<s:hidden name="value" value="%{value}" />
									</td>
								</s:if>
								<s:else>
									<td>
										<s:select theme="simple" cssClass="option"
											list="{'ACTIVE','INACTIVE'}" name="status" value="%{status}" />
									</td>
									<td>
										<s:textfield theme="simple" name="value" value="%{value}" />
									</td>
								</s:else>

								<td>
									<s:property value="description" />
									<s:hidden name="description" value="%{description}" />
								</td>
								<s:if test="%{editable=='NO'}">
									<td></td>
								</s:if>
								<s:else>
									<td>
										<input type="button"
											onclick="return updateRow('<s:property value="#taskIndex.index" />')"
											value="Update" class="button" />
									</td>
								</s:else>
								<td>
									<div name="update" id="update" style="display:none;color:#FF0000">
										Updated successfully
									</div>
								</td>
							</tr>
						</s:iterator>
						<tr>
							<td colspan="7" align="center">
								<table>
									<s:submit name="submit" value="Update All" cssClass="button" />
								</table>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_cm_property_Menu.jsp,v $
$Revision: 1.2 $
</code>