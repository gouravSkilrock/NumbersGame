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
			content="text/html; charset=UTF-8" />
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
					<s:text name="label.cashier.wise.trans.rep"/> 
				</h3>
				<s:actionerror/>	<s:actionmessage/>
			<s:form  action="bo_rep_cashierWiseTrn_Search" id="searchForm" name="searchForm">

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
										<s:select key="label.slct.bank" id="bank"  name="branchDetailsBean.bankId" headerKey="-1"
													headerValue="-- ALL --" list="{}" cssClass="option" required="true" />
										</td>
									</tr>
									<tr>
											<td align="right" colspan="2">
											<div id="branch_e" style="color: red; text-align: center"></div>
											</td>
											<td>
										<s:select key="label.slct.branch" id="branch"  name="branchDetailsBean.branchId" headerKey="-1"
													headerValue="-- ALL --" list="{}" cssClass="option" required="true" />
										</td>
									</tr>
									<tr>
											<td align="right" colspan="2">
											<div id="repType_e" style="color: red; text-align: center"></div>
											</td>
											<td>
										<s:radio id="repType" name="repType" key="label.slct.report.type" list="#{'Payment':'Payment','Credit':'Credit','ALL':'ALL'}" value="#{'ALL'}" />
										</td>
									</tr>
									<tr>
											<td align="right" colspan="2">
											<div id="terminalId_e" style="color: red; text-align: center"></div>
											</td>
											<td>
											<s:textfield name="terminalId" label="Terminal Id:" id="terminalId" 		maxLength="8" />
											
										
										</td>
									</tr>
								
									<tr>
										<td colspan="2" align="center">
											<label class="label">
												<s:text name="label.start.date"/>
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
										<td colspan="2" align="center">
											<label class="label">
												<s:text name="label.end.date"/>
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
													<s:submit name="search" id="search" key="btn.srch" align="center"
																targets="down" theme="ajax" cssClass="button"  onclick="return validate()"/>
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
	$RCSfile: bo_rep_cashierWiseTrn_Menu.jsp,v $ $Revision:
	1.1.2.4 $
</code>