<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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


	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3 style="text-align: center">
					Advertisement Message Result
				</h3>
				<table border="1" cellpadding="1" cellspacing="0"
					bordercolor="#CCCCCC" align="center" width="100%">
					<tr>
						<td>
							Select Message For
						</td>
						<td>
							<s:property value="%{orgType}" />
						</td>
					</tr>
					<tr>
						<td>
							Message Type (Mode)
						</td>
						<td>
							<s:property value="%{mode}" />
						</td>
					</tr>
					<tr>
						<td>
							Message Subject
						</td>
						<td>
							<s:property value="%{messageSubject}" />
						</td>
					</tr>
					<tr>
						<td>
							Message Content
						</td>
						<td>
							<s:property value="%{messageContent}" />
						</td>
					</tr>
					<tr>
						<td>
							Expiry Period
						</td>
						<td>
							<s:property value="%{expiryDate}" /> <s:property value="expHr" />:<s:property value="expMin" />:<s:property value="expSec" />
						</td>
					</tr>
					<s:if test="%{mode neq 'FLASH'}">
						<tr>
							<td>
								Popup
							</td>
							<td>
								<s:if test="%{isPopup == true}">
									YES
								</s:if>
								<s:else>
									NO
								</s:else>
							</td>
						</tr>
						<tr>
							<td>
								Mandatory
							</td>
							<td>
								<s:if test="%{isMandatory == true}">
									YES
								</s:if>
								<s:else>
									NO
								</s:else>
							</td>
						</tr>
					</s:if>
					<tr>
						<td>
							Interface
						</td>
						<td>
							<s:property value="%{interfaceType}" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table border="1" cellpadding="1" cellspacing="0" width="100%">
								<tr align="center">
									<td colspan="5">
										<b>Organization's Affected</b>
									</td>
								</tr>
								
								<tr>
									<s:iterator value="%{agtName}" status="agtIndex">
										<td>
											<s:if test="%{toString()=='-1'}">ALL Agent Organization's</s:if>
											<s:else>
												<s:property />
											</s:else>

										</td>
										<s:if test="(#agtIndex.index+1)%5==0">
								</tr>
								<tr>
									</s:if>
									</s:iterator>
								</tr>
								
								<tr>
									<s:iterator value="%{retName}" status="retIndex">
										<td>
											<s:if test="%{toString()=='-1'}">ALL Retailer Organization's</s:if>
											<s:else>
												<s:property />
											</s:else>

										</td>
										<s:if test="(#retIndex.index+1)%5==0">
								</tr>
								<tr>
									</s:if>
									</s:iterator>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<s:a href="bo_um_advMessage_Menu.action">Add More Messages</s:a>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_advMessage_ret_Submit.jsp,v $
$Revision: 1.2 $
</code>