<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.css"
			media="screen" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%> <%!String isOffline = null;%>

		</title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript">path="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/orgNUserReg.js"></script>
	</head>
	<body
		onload="getAgentList(),onLoadAjax('getListAjax.action?listType=country','country'),selectState(),fetchCity();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		
		<s:hidden id="country_deployed" value="%{#application.COUNTRY_DEPLOYED}"/>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" />
					<s:text name="label.reg" />
				</h3>
				<div id="retRegDetail">
					<s:form name="na" onsubmit="return isST3Filled()"
						action="bo_um_retReg_Save">
						<table border="0" cellpadding="2" cellspacing="0" width="45%">
							<tr>
								<td colspan="2">
									<div id="agtDiv">

									</div>
								</td>
							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td>
												 <s:text name="label.agent.org" />*
											</td>
											<td>
												<s:select cssClass="option" name="agentOrgName"
													id="agentOrgName" list="{}" headerValue="%{getText('label.please.select')}"
													headerKey="-1"
													onchange="getRetRegForm(this.id),fillModelNames(this.id)"
													required="true"></s:select>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>


						<s:hidden name="agtOrgId" id="agtOrgId"></s:hidden>
						<table border="1" width="780px" cellspacing="0" cellpadding="0"
							bordercolor="#CCCCCC">
							<tr>
								<td valign="top">
									<table border="0" width="390px" cellspacing="0" cellpadding="2">
										<tr>
											<th align="left" colspan="2">
												<s:text name="label.org.detail" />
												: -
											</th>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="org" style="color: red">
													<s:property value="errorMap.orgError" />
												</div>
											</td>
										</tr>
										<tr>
											<td nowrap="nowrap">
												<s:textfield key="label.org.name" maxlength="25"
													name="orgName" id="org_name" required="true" />
											</td>
										</tr>

										<tr style="display: none;">
											<td>
												<s:hidden theme="simple" name="orgType" value="RETAILER" />
												<s:textfield theme="simple" key="label.org.type"
													readonly="true" maxlength="30"
													value="%{#application.TIER_MAP.RETAILER.toUpperCase()}"
													required="true" />

											</td>

										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="addr1"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.adrs.line1" maxlength="30"
													name="addrLine1" required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="addr2"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.adrs.line2" maxlength="30"
													name="addrLine2" />
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="orgcountry"></div>
											</td>
										</tr>
										<tr style="display: none;">
											<td>
												<s:select theme="simple" key="label.country" id="country"
													name="country" headerKey="-1"
													headerValue="-- Please Select --" list="{}"
													onchange="_ajaxCall('getState.action?country=' + this.value,'characters')"
													cssClass="option" required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="orgstate"></div>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="label">
													<s:text name="label.state" />
													<span class="required">*</span>:
												</label>
											</td>
											<td align="left">
												<span id="characters"> <select class="option"
														id="state" name="state">
														<option value="<s:text name="label.please.select" />">
															<s:text name="label.please.select" />
														</option>
													</select> </span>
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="orgcity"></div>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="label">
													<s:text name="label.city" />
													<span class="required">*</span>:
												</label>
											</td>
											<td align="left">
												<span id="charactersCity"> <select class="option"
														id="city" name="city" onchange="selectCity()">
														<option value="<s:text name="label.please.select" />">
															<s:text name="label.please.select" />
														</option>
													</select> </span>
											</td>
										</tr>

										<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
											<tr>


												<td align="center" colspan="2">
													<div id="area_e" style="color: red; text-align: center"></div>
												</td>
											</tr>
											<tr>
												<td align="right">
													<label class="label">
														<s:text name="ZONE" />

													</label>
												</td>
												<td>
													<s:select theme="simple" id="area" name="area"
														headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
														cssClass="option" />
												</td>

											</tr>

											<!--
									
									<tr>
										<td align="right">
											<label class="label">
												Zone
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="area"
												name="area" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  onchange="fetchDivisonList()"/>
										</td>
										
									</tr>
									
									 <tr>
										
										
										<td align="center" colspan="2">
											<div id="division_e"  style="color: red; text-align: center"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												Divison 
											
											</label>
										</td>		
										<td >
											<s:select theme="simple"  id="division"
												name="division" headerKey="-1"
												headerValue="-- Please Select --" list="{}"
												cssClass="option"  />
										</td>
										
									</tr>
									
									
									
									
									-->
										</s:if>
										<tr>
											<td align="center" colspan="2">
												<div id="orgpin"></div>
											</td>
										</tr>
										<tr style="display: none;">
											<td>
												<s:textfield theme="simple" key="label.po.box"
													maxlength="10" name="pin" id="Pin" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="orglimit"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.crdtlmt" maxlength="10"
													name="creditLimit" id="Limit" required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="orgsecurity"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.secur.dpst" maxlength="9"
													name="security" id="Security" required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="vatReg">
												</div>
											</td>
										</tr>
										<s:if test="%{vat_applicable=='yes'}">
											<tr>
												<td>
													<s:textfield key="label.vat.reg.no" value=""
														name="vatRegNo" maxlength="20" />
												</td>
											</tr>
										</s:if>
										<s:else>
											<tr style="display: none;">
												<td>
													<s:textfield theme="simple" key="label.vat.reg.no" value=""
														name="vatRegNo" maxlength="20" readonly="true"
														required="true" />
												</td>
											</tr>
										</s:else>
										<tr>
											<td>
												<s:textfield name="autoScrap" key="label.autoscrap"
													value="YES" cssClass="option" required="true"
													readonly="true" />
											</td>
										</tr>
										<s:hidden name="reconReportType" value="Book Wise Report"></s:hidden>
										<!--
								 <tr>
										<td>
											<s:select name="reconReportType"
												label="Reconciliation Report" list="{'Ticket Wise Report'}"
												cssClass="option" />
										</td>
									</tr> -->

										<tr style="display: none;">
											<td>
												<s:textfield theme="simple" key="label.org.status"
													readonly="true" name="statusorg" value="ACTIVE" />
											</td>
										</tr>

									</table>
								</td>

								<%--		    
		    <tr>
		    	<td></td>
		    	<td align="left">
		         	<table><s:submit name="sub" value="Next" align="right" cssClass="button"/></table>
		        </td>
			</tr>
			This code added because of registering retailers on a single page 
--%>
								<td>
									<table border="0" width="390px" cellspacing="0" cellpadding="2">
										<tr>
											<th align="left" colspan="2">
												<s:text name="label.personal.detail" />
												: -
											</th>
										</tr>

										<s:actionerror />
										<s:fielderror />
										<tr>
											<td align="center" colspan="2">
												<div id="fname"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.fname" maxlength="30"
													name="firstName" required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="lname"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.lname" maxlength="30"
													name="lastName" required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="user" style="color: red">
													<s:property value="errorMap.userError" />
												</div>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="label">
													<i><s:text name="label.login.name" /> </i>
													<span class="required">*</span>:
												</label>
											</td>
											<td align="center">
												<s:textfield key="label.login.name" theme="simple"
													maxlength="30" name="userName" cssStyle="float:left"
													id="user_name" required="true" />
												<a href="#"
													style="font-weight: 900; color: orange; margin-left: 3px"
													onclick=checkAvlOrgUser();><s:text
														name="label.check.avail" /> </a>
											</td>
										</tr>


										<tr>
											<td align="center" colspan="2">
												<div id="Email"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.email" maxlength="50" name="email"
													required="true" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="Phone"></div>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="label">
													<i><s:text name="label.land.no" /> </i>&nbsp;
												</label>
											</td>
											<td align="center">
												<s:textfield theme="simple" name="cityCode" id="cityCode"
													cssStyle="width:24px;float:left;" required="true"
													readonly="true" />
												<b>-</b>
												<input type="text" maxlength="12" name="phone" id="phone"
													style="margin-right: 65px;" />
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="Mobile"></div>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="label">
													<i><s:text name="label.mob.no"/></i>
													<span class="required">*</span>:
												</label>
											</td>
											<td align="center">
												<s:textfield theme="simple" name="code" id="code"
													required="true" cssStyle="width:24px;float:left;"
													readonly="true" />
												<b>-</b>
												<input type="text" maxlength="12" name="mobile" id="mobile"
													style="margin-right: 65px;" />
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<div id="id_type_error"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
													<s:select key="label.id.type" name="idType" 
														list="#{'IFU CODE':getText('IFU.CODE')}"
														cssClass="option" required="false" />
												</s:if>
												<s:else>
													<s:select key="label.id.type" name="idType" 
														list="#{'Passport':getText('label.passport'),'Driving Licence':getText('label.drivng.licence'),'NID':getText('label.nid'),'Pan Card':getText('label.pan.card')}"
														cssClass="option" required="true" />
												</s:else>
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="id_no_error" style="color: red">
													<s:property value="errorMap.idError" />
												</div>
											</td>
										</tr>
										<tr>
											<td>
												<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
													<s:textfield key="label.id.no" name="idNo" id="id_no"
													maxlength="13" required="false" />
												</s:if>
												<s:else>
													<s:textfield key="label.id.no" name="idNo" id="id_no"
													maxlength="15" required="true" />
												</s:else>
											</td>
										</tr>

										<tr style="display: none;">
											<td>
												<s:textfield theme="simple" key="label.role "
													readonly="true" name="role" required="true" />
											</td>
										</tr>

										<tr style="display: none;">
											<td>
												<table>
													<tr>
														<td>
															<s:text name="%{#application.TIER_MAP.RETAILER}" />
															<s:text name="label.status" />
														</td>
														<td>
															<s:textfield theme="simple" readonly="true" name="status"
																value="ACTIVE" required="true" />
														</td>
													</tr>
												</table>

											</td>
										</tr>
										<s:if test="%{#application.RET_OFFLINE=='YES'}">
											<tr>
												<td>
													<s:select name="isOffLine" key="label.offline.mode"
														list="{'NO','YES'}" cssClass="option" required="true" />
												</td>
											</tr>
										</s:if>
										<s:if test="%{#application.LOGIN_BINDING=='YES'}">

											<tr>
												<td align="center" colspan="2">
													<div id="termDiv"></div>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<table>
														<tr>
															<td align=right>
																<label class="label">
																	<i><s:text name="label.terminal.model" /> <span
																		class="required">*</span>:</i>
																</label>
															</td>

															<td align="left">

																<s:select theme="simple" id="modelId" name="modelName"
																	headerKey="-1" headerValue="%{getText('label.please.select')}"
																	list="{}" cssClass="option"
																	onchange="fillTerminalIds(this.value,'terminalId')" />
																<%-- <select id="modelId" name="modelName" class="option"
														onchange="fillTerminalIds(this.value,'terminalId')">
														<option value="-1" >
															---Please Select---
														</option>
													</select> --%>
															</td>
															<td></td>
															<td align="right" class="label">
																<label class="label">
																	<s:text name="label.trmnl.id" />
																	<span class="required">*</span>:
																</label>
															</td>
															<td align="left">
																<input type="text" id="terminalId" name="terminalId"
																	size="20" />
															</td>
														</tr>
													</table>
												</td>

											</tr>
											<!-- 	<tr>
												<td align="center" colspan="2">
													<div id="termDiv"></div>
												</td>
											</tr> 

											<tr>
												
												<td align="left">
												</td>
											</tr>-->
										</s:if>
										<s:if test="%{#application.sim_binding=='YES'}">

											<tr>
												<td align="center" colspan="2">
													<div id="simDiv"></div>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<table>
														<tr>
															<td align=right>
																<label class="label">
																	<i><s:text name="label.sim.model" /><span
																		class="required">*</span>:</i>
																</label>
															</td>

															<td align="left">

																<s:select theme="simple" id="modelId1"
																	name="simModelName" headerKey="-1"
																	headerValue="%{getText('label.please.select')}" list="{}"
																	cssClass="option"
																	onchange="fillInvIds(this.value,'sim1')" />
																<%-- <select id="modelId1" name="simModelName" class="option"
														onchange="fillInvIds(this.value,'sim1')">
														<option value="-1">
															---Please Select---
														</option>
													</select> --%>
															</td>
															<td></td>
															<td align="right" class="label">
																<label class="label">
																	<s:text name="label.sim.id" />
																	<span class="required">*</span>:
																</label>
															</td>
															<td align="left">
																<input type="text" id="sim1" name="sim" size="20" />
															</td>
														</tr>
													</table>
												</td>

											</tr>
											<tr>
												<td colspan="2">
													<table>
														<tr>
															<td align=right>
																<label class="label">
																	<i><s:text name="label.sim.model" /><span
																		class="required">*</span>:</i>
																</label>
															</td>

															<td align="left">

																<s:select theme="simple" id="modelId2"
																	name="simModelName" headerKey="-1"
																	headerValue="%{getText('label.please.select')}" list="{}"
																	cssClass="option"
																	onchange="fillInvIds(this.value,'sim2')" />
																<%-- 	<select id="modelId2" name="simModelName" class="option"
														onchange="fillInvIds(this.value,'sim2')">
														<option value="-1">
															---Please Select---
														</option>
													</select> --%>
															</td>
															<td></td>
															<td align="right" class="label">
																<label class="label">
																	<s:text name="label.sim.id" />
																	<span class="required">*</span>:
																</label>
															</td>
															<td align="left">
																<input type="text" id="sim2" name="sim" size="20" />
															</td>
														</tr>
													</table>
												</td>

											</tr>
											<tr>
												<td colspan="2">
													<table>
														<tr>
															<td align=right>
																<label class="label">
																	<i><s:text name="label.sim.model" /> <span
																		class="required">*</span>:</i>
																</label>
															</td>

															<td align="left">

																<s:select theme="simple" id="modelId3"
																	name="simModelName" headerKey="-1"
																	headerValue="-- Please Select --" list="{}"
																	cssClass="option"
																	onchange="fillInvIds(this.value,'sim3')" />
																<%-- 	<select id="modelId3" name="simModelName" class="option"
														onchange="fillInvIds(this.value,'sim3')">
														<option value="-1">
															---Please Select---
														</option>
													</select> --%>
															</td>
															<td></td>
															<td align="right" class="label">
																<label class="label">
																	<s:text name="label.sim.id" />
																	<span class="required">*</span>:
																</label>
															</td>
															<td align="left">
																<input type="text" id="sim3" name="sim" size="20" />
															</td>
														</tr>
													</table>
												</td>

											</tr>
											<!-- 	<tr>
												<td align="center" colspan="2">
													<div id="termDiv"></div>
												</td>
											</tr> 

											<tr>
												
												<td align="left">
												</td>
											</tr>-->
										</s:if>
										<tr>
											<td align="center" colspan="2">
												<div id="sQues"></div>
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<s:select key="label.secquestion" name="secQues"
													headerKey="-1" headerValue="%{getText('label.please.select')}"
													list="#{'What is Your Pets Name?':getText('whats.ur.pets.name'),'Which is your fav game?':getText('which.is.ur.fav.game'),'What is your college name?':getText('what.is.ur.college.name')}"
													value="gameType" required="true" cssClass="option" />
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<div id="sAns"></div>
											</td>
										</tr>
										<tr>
											<td>
												<s:textfield key="label.secanswer" name="secAns"
													maxlength="30" required="true" />
											</td>
										</tr>

									</table>
								</td>
							</tr>
						</table>



						<s:div theme="ajax" id="serviceDiv"
							href="agt_um_retReg_fillService.action"
							cssStyle="visibility:hidden;position:absolute;">

						</s:div>



						<table border="1" width="100%" cellspacing="0" cellpadding="0"
							style="visibility: hidden; display: none;" bordercolor="#CCCCCC">
							<tr>
								<th colspan="4" align="center">
									<s:property value="#application.TIER_MAP.RETAILER" />
									<s:text name="label.limit.values" />
								</th>
							</tr>
							<tr>
								<td class="label" align="right">
									<s:text name="label.verify.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="verLimit" id="verLimit" readonly="true"
											value="%{#application.retVerLimit}" />
									</table>
								</td>
								<td class="label" align="right">
									<s:text name="label.approve.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="appLimit" id="appLimit" readonly="true"
											value="%{#application.retAppLimit}" />
									</table>
								</td>
							</tr>
							<tr>
								<td class="label" align="right">
									<s:text name="label.payment.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="payLimit" id="payLimit" readonly="true"
											value="%{#application.retPayLimit}" />
									</table>
								</td>
								<td class="label" align="right">
									<s:text name="label.scrap.limit" />
									&nbsp;:
								</td>
								<td>
									<table>
										<s:textfield name="scrapLimit" id="scrapLimit" readonly="true"
											value="%{#application.retScrapLimit}" />
									</table>
								</td>

							</tr>
						</table>

						<table border="0" width="700px" cellspacing="0" cellpadding="0">
							<tr>
								<td style="width: 400px"></td>
								<td align="left" style="width: 390px">
									<table>
										<s:submit key="btn.reg" align="right" cssClass="button" />
									</table>
								</td>
							</tr>
						</table>

					</s:form>
				</div>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_retReg_Menu.jsp,v $ $Revision: 1.3 $
</code>
