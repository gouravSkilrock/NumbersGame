<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
					<s:text name="label.adv.msg.result" />
				</h3>
				<table border="1" cellpadding="1" cellspacing="0"
					bordercolor="#CCCCCC" align="center" width="100%">
					<tr>
						<td>
							<s:text name="label.slct.msg.for" />
						</td>
						<td>
							<s:if test="%{orgType eq 'BO'}">
								<s:text name="BOUser" />
							</s:if>
							<s:elseif test="%{orgType eq 'AGENT'}">
								<s:text name="AGENT" />
							</s:elseif>
							<s:elseif test="%{orgType eq 'RETAILER'}">
								<s:text name="RETAILER" />
							</s:elseif>
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="label.msg.type" />
							(
							<s:text name="label.mode" />
							)
						</td>
						<td>
							<s:property value="%{messageType}" />
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="label.intrfc" />
						</td>
						<td>
							<s:property value="%{interfaceType}" />
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="label.msg.sub" />
						</td>
						<td>
							<s:property value="%{messageSubject}" />
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="label.msg.content" />
						</td>
						<td>
							<s:property value="%{messageContent}" />
						</td>
					</tr>
					<s:if test="%{messageType eq 'FLASH' or orgType eq 'RETAILER'}">
						<tr>
							<td>
								<s:text name="label.slct.msg.for" />
								<s:text name="label.expry.prd" />
							</td>
							<td>
								<s:property value="%{expiryDate}" />
								<s:property value="expHr" />
								:
								<s:property value="expMin" />
								:
								<s:property value="expSec" />
							</td>
						</tr>
					</s:if>
					<s:if
						test="%{(messageType eq 'INBOX' or messageType eq 'REGISTRATION') and interfaceType eq 'TERMINAL'}">
						<tr>
							<td>
								<s:text name="label.popup" />
							</td>
							<td>
								<s:if test="%{isPopup == true}">
									<s:text name="label.yes" />
								</s:if>
								<s:else>
									<s:text name="label.no" />
								</s:else>
							</td>
						</tr>
					</s:if>
					<s:if test="%{messageType neq 'FLASH'}">
						<tr>
							<td>
								<s:text name="label.mndtry" />
							</td>
							<td>
								<s:if test="%{isMandatory == true}">
									<s:text name="label.yes" />
								</s:if>
								<s:else>
									<s:text name="label.no" />
								</s:else>
							</td>
						</tr>
					</s:if>
					<s:if test="%{isForNewUser neq null and isForNewUser neq '-1'}">
						<tr>
							<td>
								<s:text name="label.msg.for.new.reg.user" />
							</td>
							<td>
								<s:text name="label.yes" />
							</td>
						</tr>
					</s:if>
					<s:if test="%{messageType neq 'REGISTRATION'}">
						<tr>
							<td colspan="2">
								<table border="1" cellpadding="1" cellspacing="0" width="100%">
									<tr align="center">
										<td colspan="5">
											<b><s:text name="label.org.affected" /> </b>
										</td>
									</tr>
									<s:if test="%{userSelection == 'ALL'}">
										<tr>
											<td>
												<s:text name="label.all" />
												<s:property value="%{orgType}" />
												<s:text name="label.orgs" />
											</td>
										</tr>
									</s:if>
									<s:else>
										<tr>
											<s:iterator value="%{agtName}" status="agtIndex">
												<td>
													<s:if test="%{toString()=='-1'}">
														<s:text name="label.all.agt.org" />
													</s:if>
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
													<s:if test="%{toString()=='-1'}">
														<s:text name="label.all" />
														<s:property value="%{orgType}" />
														<s:text name="label.org" />'s</s:if>
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
									</s:else>
								</table>
							</td>
						</tr>
					</s:if>
				</table>
				<br />
				<br />
				<s:a href="bo_um_advMessage_Menu.action">
					<s:text name="label.add.more.msg" />
				</s:a>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_advMessage_bar_Submit.jsp,v $ $Revision: 1.3 $
</code>