<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<style type="text/css">
			.activeTheme {
				background-color: #BCF5A9;
			}
			.inactiveTheme {
				background-color: #F79F81;
			}
			.naTheme {
				background-color: #819FF7;
			}
		</style>		
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		
		
	
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<div>
					      <s:if test="%{masterBean.headerList neq null and masterBean.headerList.size>0}">
					<s:form action="bo_rep_privModification_ExpToExcel"  method="post" enctype="multipart/form-data">
						<s:hidden id="tableValueMain" name="tableValue" />
						<center><s:submit value="Export As Excel" onclick="getData('tableData', 'tableValueMain', 'tabD');"/></center>
					</s:form>
					<table width="100%" cellspacing="0" cellpadding="3" bordercolor="#CCCCCC" style="border:1px solid #e6e6e6; background:#fafafa; padding:5px; margin-bottom:-28px; margin-top:10px" id="tableData">
						<s:hidden id="reportName" value="Priviledge Modification Report"/>
						<tr>
			               <td colspan="2" style="border:1px solid #fafafa !important; background:#e6e6e6">
			               		<h3><center>Privilege Modification Report</center></h3>
			               </td>
              			</tr>
              			<tr>
							<td width="15%"><b>User Name</b></td>
							<td><s:property value="masterBean.username" /></td>
						</tr>
						<tr>
							<td><b>Registration Date</b></td>
							<td><s:property value="masterBean.registerDate" /></td>
						</tr>
						<tr>
							<td><b>Created By</b></td>
							<td><s:property value="masterBean.registerBy" /></td>
						</tr>
						<tr>
							<td><b>Email Id</b></td>
							<td><s:property value="masterBean.emailId" /></td>
						</tr>
					</table>
					<br/>
					<br/>
					
						<s:set name="isHeaderSet" value="%{'false'}" />
						<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" style="border-collapse:collapse" id="tabD">
							<s:iterator value="%{masterBean.serviceMap}">
								<s:if test="#isHeaderSet eq 'false'">
									<s:set name="isHeaderSet" value="false" />
									<tr style="background:#e6e6e6">
										<td><b>Modified By</b></td>
										<s:iterator value="%{masterBean.headerList}">
											<td><s:property value="doneByUser" /></td>
										</s:iterator>
									</tr>
									<tr style="background:#e6e6e6">
										<td><b>Modified From</b></td>
										<s:iterator value="%{masterBean.headerList}">
											<td><s:property value="doneByIP" /></td>
										</s:iterator>
									</tr>
									<tr style="background:#e6e6e6">
										<td><b>Modification Date</b></td>
										<s:iterator value="%{masterBean.headerList}">
											<td><s:property value="changeTime" /></td>
										</s:iterator>
									</tr>
								</s:if>
								<tr>
									<td colspan='<s:property value="masterBean.headerList.size+1" />'>
										&nbsp;
									</td>
								</tr>
								<tr style="background:#e6e6e6">
									<td colspan='<s:property value="masterBean.headerList.size+1" />'>
										<center><b><s:property value="key"/></b></center>
									</td>
								</tr>
								<s:iterator value="value">
									<tr>
										<td><s:property value="key"/></td>
										<s:iterator value="value">
											<td class='<s:if test="%{status eq 'ACTIVE'}">activeTheme</s:if><s:elseif test="%{status eq 'INACTIVE'}">inactiveTheme</s:elseif><s:else>naTheme</s:else>'><s:property value="status"/></td>
										</s:iterator>
									</tr>
								</s:iterator>
							</s:iterator>
						</table>
					</s:if>
					<s:else><center><h1>NO RECORDS TO PROCESS</h1></center></s:else>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_priviledgeModificationReport_Search.jsp,v $ $Revision: 1.3 $
</code>