<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
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
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/bankMgmt/js/bank_rep.js"></script>
	<script>
	function getData() {
		var tblData = document.getElementById("tableDataDiv").innerHTML;
		tblData = tblData.replace(document.getElementById("sortRow").innerHTML,
				"");
		document.getElementById('tableValue').value = tblData;
		return false;
	}
</script>
</head>
<body >
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
		
				<h3>
					Bank/Branch  Wise Credit Update
					<s:text name="Report"></s:text>
				</h3>
			<s:form  action="bo_rep_creditUpdate_Search">

					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<tr>
											<td align="right" colspan="2">
											<div id="bankName_e" style="color: red; text-align: center"></div>
											</td>
											<td>
										<s:select label="Select Bank" id="bank"  name="branchDetailsBean.bankId" headerKey="-1"
													headerValue="-- ALL --" list="{}" cssClass="option" required="true" />
										</td>
									</tr>
									<tr>
											<td align="right" colspan="2">
											<div id="branch_e" style="color: red; text-align: center"></div>
											</td>
											<td>
										<s:select label="Select Branch" id="branch"  name="branchDetailsBean.branchId" headerKey="-1"
													headerValue="-- ALL --" list="{}" cssClass="option" required="true" />
										</td>
									</tr>
								
									<tr>
										<td colspan="2" align="right">
											<label class="label">
												Start  Date
												<span>*</span>:&nbsp;
											</label>
											
											<input type="text" name="startDate" id="startDate"
												value="<s:property value="currDate" />" readonly size="12"/>
												
															<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('startDate'),'yyyy-MM-dd', this, '<s:property value="currDate"/>',false,'<s:property value="currDate"/>')" />			
											
											</td>
									</tr>
									
									<tr>
										<td colspan="2" align="right">
											<label class="label">
												End  Date
												<span>*</span>:&nbsp;
											</label>
											
											<input type="text" name="endDate" id="endDate"
												value="<s:property value="currDate" />" readonly size="12"/>
												
															<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('endDate'),'yyyy-MM-dd', this, '<s:property value="currDate"/>',false,'<s:property value="currDate"/>')" />			
											
											</td>
									</tr>
									<tr>
											<td colspan="2" align="right">
													<s:submit id="search" name="search" value="Search" align="center"
																targets="down" theme="ajax" cssClass="button" />
											</td>
								</tr>
									
								</table>
							</td>
						</tr>
						

					</table>
				</s:form>

				<div id="down" style="text-align: center"></div>

				<br />
				<br />	
				
			</div>
			</div>	
				

</body>


</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_creditUpdate_Menu.jsp,v $ $Revision:
	1.1.2.4 $
</code>