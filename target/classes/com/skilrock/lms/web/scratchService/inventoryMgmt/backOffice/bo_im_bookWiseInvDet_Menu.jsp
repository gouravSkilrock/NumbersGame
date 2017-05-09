
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
		path ="<%=request.getContextPath()%>";
	</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/inventoryBooksDetails.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body onload="getAgentList();">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.book.wise.inv.details" />
				</h3>
				<form>

					<table border="0" width="60%" bordercolor="red"
						style="text-align: left" cellpadding="2" cellspacing="0">
						<tr>
							<td colspan="2">
								<table width="100%">
									<tr>
										<td colspan="1" width="40%">
											<label class="label">
												<s:text name="label.org.type" />
												:
											</label>
										</td>
										<td colspan="1">
											<s:select
												list="#{'BO':'BO','AGENT':#application.TIER_MAP.AGENT.toUpperCase(), 'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}"
												name="type" theme="simple" cssClass="option"
												key="label.org.type" id="org_type"
												onchange="onTypeChangeValue(this.value)"></s:select>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div id="agent_error" style="color: red; text-align: right;"></div>
										</td>
									</tr>
									<tr>
										<td>
											<table style="display: none" id="agt1">
												<tr>
													<td colspan="1" width="40%">
														<label class="label">
															<s:property value="#application.TIER_MAP.AGENT" />
															<s:text name="label.org.name" />
															:
														</label>
													</td>
												</tr>
											</table>
										</td>
										<td colspan="1">
											<table style="display: none" id="agt">
												<tr>

													<td colspan="1" align="left">

														<s:select headerKey="-1"
															headerValue="%{getText('label.please.select')}" list="{}"
															theme="simple" name="agtId" cssClass="option"
															id="agent_org_name"
															onchange="onAgentChangeValue(this.value)"></s:select>

													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<div id="ret_error" style="color: red; text-align: right;"></div>
										</td>
									</tr>
									<tr>
										<td>
											<table style="display: none" id="ret1">
												<tr>
													<td colspan="1" width="40%">
														<label class="label">
															<s:property value="#application.TIER_MAP.RETAILER" />
															<s:text name="label.org.name" />
															:
														</label>
													</td>
												</tr>
											</table>
										</td>
										<td>
											<table style="display: none" id="ret">
												<tr>
													<td colspan="1" align="left">
														<s:select name="retId"
															label="%{#application.TIER_MAP.RETAILER} Organization Name"
															theme="simple" cssClass="option" id="ret_org_name"
															list="{}" headerKey="-1"
															headerValue="%{getText('label.please.select')}" />

													</td>
												</tr>
											</table>
										</td>
									</tr>

									<tr>
										<td width="40%">
											<label class="label">
												<s:text name="label.game.name" />
												:
											</label>
										</td>
										<td align="left">
											<select name="gameid" class="option" id="game_id">
												<s:iterator value="#session.boAgentListGame">
													<option value="<s:property value="key"/>">
														<s:property value="value" />
													</option>
												</s:iterator>
											</select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td></td>
						</tr>
						<tr>
							<td colspan="1" width="97%" align="center">
								<table>
									<s:submit name="showDetail" targets="d2" id="submit"
										key="btn.show.details" cssClass="button" theme="ajax"
										onclick="getInventoryDetails('bo_im_bookWiseInvDet_Detail.action', 'd2')" />
								</table>
							</td>
							<td colspan="1" width="5%">
								<div id="loadingDiv">
									&nbsp;
								</div>
							</td>
						</tr>

					</table>

				</form>

				<div id="d2"></div>
			</div>
		</div>
		<br />
		<br />


	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_bookWiseInvDet_Menu.jsp,v $ $Revision: 1.1.8.4.8.1.2.1
	$
</code>