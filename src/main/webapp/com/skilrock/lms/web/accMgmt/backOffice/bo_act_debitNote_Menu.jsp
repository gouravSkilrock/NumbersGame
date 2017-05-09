<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/backOffice/js/debitNote.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/backOffice/js/digitToWord.js"></script>
	<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js"></script>
	<script>var path="<%=request.getContextPath() %>"</script>
	<s:head debug="false"/>
</head>

	<body onload='return gameDetails();'>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<div id="top_form">
<div id="limitDiv" style="align: center;"></div>
<h3><s:text name="Debit_Note"/></h3>
	<s:div  id="x">
		<s:form  action="bo_act_debitNote_Entry" onsubmit="return validateData()">
			<table border="0" cellpadding="2" cellspacing="0" class="credit_note" > 		        
				<s:if test="%{#application.IS_DIRECT_DEBIT_NOTE_ENABLE_FOR_RETAILER=='YES'}">
						<tr>
							<td>
								<s:select cssClass="option" required="true"
									key="label.org.type" id="orgType" name="orgType"
									list="#{'RETAILER':#application.TIER_MAP.RETAILER}" onchange="hideAndShow();">
								</s:select>
							</td>
						</tr>
				</s:if>

				<s:else>
						<tr>
							<td>
								<s:select cssClass="option" required="true"
									key="label.org.type" id="orgType" name="orgType"
									list="#{'AGENT':#application.TIER_MAP.AGENT}"
									onchange="hideAndShow();"></s:select>
							</td>
						</tr>
				</s:else>
				
				<tr><td colspan="3"><s:div id="orgName1"></s:div></td></tr>				
				<tr>
					<td><table class="credit_note1"><s:label key="label.agt.name" /></table></td>
					<td colspan="2"><table class="credit_note2">
					<s:hidden name="agentNameValue" id="agentNameValue"></s:hidden>
					<s:select label="" cssClass="option" name="agentName" id="orgName" headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}" onchange="getRetList();verifyAgent(this.value)" required="true"></s:select></table></td>
				</tr>
				
				<tr>
					<td>
						<div id="retOrgName1">
						</div>
					</td>
				</tr>
				
				<s:if test="%{#application.IS_DIRECT_DEBIT_NOTE_ENABLE_FOR_RETAILER=='YES'}">
						<tr id="retName" align="left">
							<td>
								<s:text name="label.ret.name"></s:text>
								
							</td>
							<td>
								<table cellpadding="0" cellspacing="0" width="100%" class="credit_note3">
									<tbody>
										<s:hidden name="retNameValue" id="retNameValue"></s:hidden>
										<tr>
											<td><span class="required newreq">*<strong>:</strong></span>
												<s:select id="retOrgName" 
													cssClass="option" list="{}" name="retOrgName"
													headerKey="-1"
													headerValue="%{getText('label.please.select')}"
													onchange="varifyRetailer(this.value)" />
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
				</s:if>
				<s:else>
						<tr id="retName" align="left" style="display: none;">
								<td style="text-align: right;">
									<s:text name="label.ret.name"></s:text>
									<span class="required">*</span>:
								</td>
								<td>
									<table cellpadding="0" cellspacing="0" width="100%" class="credit_note4">
										<tbody>
											<s:hidden name="retNameValue" id="retNameValue"></s:hidden>
											<tr>
												<td>
													<s:select id="retOrgName" required="true"
														cssClass="option" list="{}" name="retOrgName"
														headerKey="-1"
														headerValue="%{getText('label.please.select')}"
														onchange="varifyRetailer(this.value)" />
												</td>
											</tr>
										</tbody>
									</table>
								</td>
						</tr>
				</s:else>
				
				
				<tr><td colspan="3"><s:div id="amount1"></s:div></td></tr>		        
				<tr>
					<td><table class="credit_note1"><s:label key="label.enter.amt" /></table></td>
					<td colspan="2"><table class="credit_note2"><s:textfield  name="amount" label="" id="amount" size="25" maxlength="09" value="" required="true"></s:textfield></table></td>
				</tr>
				
				<tr>
					<td>
						<table class="credit_note1">
							<s:label key="label.reason"></s:label>
						</table>
					</td>
					<td colspan="2">
						<table class="credit_note5">
							<s:select id="reason" name="reason" list="#session.REASON_LIST" onchange="showGameList(this.value);" cssClass="option"></s:select>
						</table>
					</td>
				</tr>
				
				<tr>
					<td>
						<div id="gameListDiv1" style="display: none;">
							<table class="credit_note8">
								<s:label key="label.game.name"></s:label>
							</table>
						</div>
					</td>
					<td colspan="2">
						<div id="gameListDiv2" style="display: none;">
							<table class="credit_note9">
								<s:select id="game_id" name="gameName" list="{}" cssClass="option"></s:select>
							</table>
						</div>
					</td>
				</tr>
				
				<tr><td colspan="3"><s:div id="remarks1"></s:div></td></tr>				
				<tr>
					<td valign="top" align="right"><table  class="credit_note1"><s:label key="label.remarks" /></table></td>
					<td colspan="2"><table class="credit_note2"><s:textarea label="" cssStyle="height: 2cm;width: 6cm; border-color : black"  name="remarks" id="remarks"  value="" required="true"></s:textarea></table></td>
				</tr>
			
				<tr>
				<td colspan="2">
					<table border="0" width="100%" class="credit_note6">
					<tr>
						<td class="crd_ri"><s:submit theme="simple" targets="bottom" key="btn.submit" cssClass="button"/></td>
						<td align="left" colspan="2"><s:reset theme="simple" key="btn.reset" cssClass="button" onclick="resetData();"/></td>
					</tr>
					</table>
				</td>
				</tr>
			</table>
		</s:form>
		
		<s:div theme="ajax" id="bottom" > </s:div>
     
 </s:div></div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_debitNote_Menu.jsp,v $
$Revision: 1.3 $
</code>