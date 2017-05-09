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
		<script type="text/javascript">
			path="<%=request.getContextPath()%>" ; </script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/bankMgmt/js/bank_reg.js"></script>

	
</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				
				<h3>
					Create Bank 
				</h3>
			<s:form name="createBank" id="createBank"
					action="bo_bank_registration_Save">
			
					<table border="0" cellpadding="2" cellspacing="0" width="400"
						style="width: 100%; vertical-align: middle;">
						<tr>
							<td align="left" colspan="2">
								<div id="error"><s:actionerror /> <s:actionmessage/>  </div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
							<div id="bankName_e"  style="color: red; text-align: center"></div>
							</td>
							<td >
								<s:textfield name="bankDetailsBean.bankName" id="bankName" label="Bank Name" maxlength="15" required="true"></s:textfield>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
							<div id="bankFullName_e"  style="color: red; text-align: center"></div>
							</td>
							<td >
								<s:textfield name="bankDetailsBean.bankFullName" id="bankFullName" label="Bank Full Name" maxlength="50" required="true"></s:textfield>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
							<div id="bankadd1_e"  style="color: red; text-align: center"></div>
							</td>
							<td >
								<s:textfield name="bankDetailsBean.bankAddLine1" id="bankadd1" label="Bank Address Line 1" maxlength="50" required="true"></s:textfield>
							</td>
						</tr>
						<tr> 
							<td colspan="2">
								<s:textfield name="bankDetailsBean.bankAddLine2" id="bankadd2" label="Bank Address Line 2" maxlength="50"></s:textfield>
							</td>
						</tr>
							<tr>
										<td align="center" colspan="2">
											<div id="roleDiv"   style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr >
										<td align="right">
											<label class="label">
												Role 
												<span class="required">*</span>:
											</label>
										</td>
										<td>
											<s:select theme="simple"  id="roleId"
												name="bankDetailsBean.roleId" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option" required="true" />
										</td>
						</tr>
						<tr>
										<td align="center" colspan="2">
											<div id="orgcountry"   style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr style="display: none;">
										<td align="right">
											<label class="label">
												Country
												<span class="required">*</span>:
											</label>
										</td>
										<td>
											<s:select theme="simple" label="Country" id="country"
												name="bankDetailsBean.country" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option" required="true" />
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgstate"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												State
												<span class="required">*</span>:
											</label>
										</td>
										<td >
											<s:select theme="simple"  id="state"
												name="bankDetailsBean.state" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option" required="true" />
										</td>
										
										
										
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgcity"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												City
												<span class="required">*</span>:
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="city"
												name="bankDetailsBean.city" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option" required="true" />
										</td>
										
									</tr>
						
						
						
						<tr>
							<td colspan="2">
								<s:submit name="submit" id="submit" value="submit" align="center"
									 cssClass="button" />
							</td>
						</tr>
						
						

					</table>
				</s:form>

			

				<br />
				<br />	
				
			</div>
			</div>	
				

</body>


</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_bank_registrationMenu.jsp,v $ $Revision: 1.2 $
</code>