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
							<s:property value="%{orgType}" />
						</td>
					</tr>
					<tr>
						<s:if test="gameNo[0] != 'false'">
							<td>
								<s:text name="label.game.name" />
							</td>
							<td>
								<s:property value="%{gameName}" />
							</td>
						</s:if>
						<s:if test="sleGameNo[0] != 'false'">
							<td>
								<s:text name="label.game.name" />
							</td>
							<td>
								<s:property value="%{gameName}" />
							</td>
						</s:if>
						<s:if test="iwGameNo[0] != 'false'">
							<td>
								<s:text name="label.game.name" />
							</td>
							<td>
								<s:property value="%{gameName}" />
							</td>
						</s:if>
						<s:if test="vsGameNo[0] != 'false'">
							<td>
								<s:text name="label.game.name" />
							</td>
							<td>
								<s:property value="%{gameName}" />
							</td>
						</s:if>
						<s:if test="categoryName[0] != 'false'">
							<td>
								<s:text name="label.cat.name" />
							</td>
							<td>
								<s:property value="%{categoryName}" />
							</td>
						</s:if>

						<s:if test="walletName[0] != 'false'">
							<td>
								<s:text name="label.wallet.name" />
							</td>
							<td>
								<s:property value="%{walletName}" />
							</td>
						</s:if>
					</tr>
					<tr>
						<td>
							<s:text name="label.msg" />
						</td>
						<td>
							<s:property value="%{message}" />
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="label.msg.location" />
						</td>
						<td>
							<s:property value="%{msgLocation}" />
							<s:text name="label.of.the" />
							<s:if test="walletName[0] != 'false'">
								<s:text name="label.slip" />
							</s:if>
							<s:if test="gameNo[0] != 'false' || iwGameNo[0] != 'false'  || vsGameNo[0] != 'false'">
								<s:text name="label.tkt" />
							</s:if>
							<s:if test="categoryName[0] != 'false'">
								<s:text name="Voucher" />
							</s:if>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table border="1" cellpadding="1" cellspacing="0" width="100%">
								<tr align="center">
									<td colspan="5">
										<b><s:text name="label.org.affected" /> </b>
									</td>
								</tr>

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
												<s:text name="label.all.ret.org" />
											</s:if>
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
				<s:a href="bo_um_advMessage_Menu.action">
					<s:text name="label.add.more.msg" />
				</s:a>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_advMessage_Submit.jsp,v $ $Revision: 1.3 $
</code>