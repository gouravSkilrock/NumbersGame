	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>	
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<s:head theme="ajax" debug="false"/>
	</head>
	<body>
	

	<div id="wrap">
  	<div id="top_form">
  		<h3>
					Payment Detail
				</h3>
	
		<s:div theme="ajax" id="x">
			<s:form name="chequeForm" action="bo_act_chq_AddToCart"
				onsubmit="return ST5_Validation_Cheque()">
				
				<table>
					<s:hidden name="paymentType" value="CHEQUE" />
					
					<th colspan="2">
						Payment By Cheque
					</th>
					<tr>
						<td>
							<div id="orgType11" align="center"></div>
						</td>
					</tr>
					
						<tr>
						<s:hidden theme="simple" name="orgType" id="orgType" value="AGENT" />
					   <s:textfield label="Organization Type" value="%{#application.TIER_MAP.AGENT.toUpperCase()}" readonly="true"></s:textfield>
						</tr>
					
					<tr>
						<td colspan="2" align="center">
							<div id="orgName11" align="center"></div>
						</td>
					</tr>
					<s:if test="%{#session.CHEQUE_PAYMENT_LIST.size>0}">
						<tr>
								
							<s:hidden name="orgName" id="orgName" value="%{#session.ORG_NAME}" ></s:hidden>
							<s:textfield label="Organization Name" name="agentValue" value="%{#session.AGT_NAME_VALUE}" size="30" readonly="true"></s:textfield>
							<!--  
							<s:select cssClass="option" headerKey="-1" headerValue="-- Please Select --"
								label="Company Name" name="orgName" id="orgName"
								list="%{orgNameList}" disabled="true"
								value="%{#session.ORG_NAME}"></s:select>
								-->
						</tr>
					</s:if>
					<s:else>
						<tr>
							<s:hidden name="agentNameValue" id="agentNameValue"></s:hidden>
							<s:select cssClass="option" headerKey="-1" headerValue="-- Please Select --"
								label="Company Name" name="orgName" id="orgName" required="true"
								list="%{orgNameMap}"  onchange="verifyAgent(this.value)"></s:select>
																
						</tr>
					</s:else>
					<tr><td colspan="2" align="center">
						<s:label name="Cheque Details" value="Cheque Details:-" required="true"></s:label></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="chequeNumber1" align="center"></div>
						</td>
					</tr>
					<tr>
						<s:textfield label="Cheque Number" id="chequeNumber"
							name="chequeNumber" size="10" maxlength="10" value="" required="true"></s:textfield>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="issuePartyname1" align="center"></div>
						</td>
					</tr>
					<tr>
						<s:textfield label="Issuing party Name" name="issuePartyname"
							id="issuePartyname" size="20" maxlength="30" required="true"></s:textfield>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="bankName1" align="center">
							</div>
						</td>
					</tr>
					<tr>
						<s:textfield label="Bank Name" name="bankName" id="bankName"
							size="20" maxlength="30" required="true"></s:textfield>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="chequeAmount1" align="center"></div>
						</td>
					</tr>
					<tr>
						<s:textfield  label="Cheque Amount"
							id="chequeAmount" name="chequeAmount" size="15" maxlength="12"
							value="" required="true"></s:textfield>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="verifyAmnt" align="center"></div>
						</td>
					</tr>
					<tr>
						<s:textfield label="Verify Cheque Amount"
							id="verifychequeAmount" name="verifychequeAmount" size="15"
							maxlength="12" required="true"></s:textfield>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="chequeDate1" align="center" style="margin-left:50px;"></div>
						</td>
					</tr>
					<tr>
						<s:set name="aa" value="#session.CHEQUE_END_DATE" />
						<s:set name="BB" value="#session.CHEQUE_START_DATE" />
											
						<!--  
						<s:set name="format" value="#session.DATE_FORMAT" />
						-->
						<s:hidden name="startDate" value="%{BB}" id="chqStdate1" />
						
						<td colspan="2">
							<%	
								String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("CHEQUE_END_DATE"), "yyyy-mm-dd", "mm/dd/yyyy");
								String startDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("CHEQUE_START_DATE"), "yyyy-mm-dd", "mm/dd/yyyy");
							%>
							<input  type="hidden" name="sd" value="<%=startDate%>" id="chqStdate" />
						<table border="0" width="100%"><tr> <td width="43%" align="right">	<label class="label">Cheque Date<span class="required">*</span>:</label> </td> 
    					 <td valign="middle">	<input  type="text" name="chequeDate" id="chequeDate" value="<%=endDate%>" readonly size="12" /> 
    						<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('chequeDate'),'mm/dd/yyyy', this, '<%=endDate%>', '<%=startDate%>', '<%=endDate%>')" /></td> </table>
   						</td>
   						</tr>
						
						</table>
					<table align="center" width="100%">
						<tr >
						<td  width="26%" align="right" >
							<s:submit theme="simple" value="Submit" targets="bottom"  cssClass="button"/>
							<s:reset theme="simple" value="Reset" cssClass="button" onclick="return removeMsgs()" />
						</td>
						<td></td>
						</tr>
					</table>
			</s:form>
		
		</s:div></div></div>
		<s:div id="bottom" theme="ajax">
		</s:div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_chq_Display.jsp,v $
$Revision: 1.2 $
</code>