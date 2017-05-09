<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_debitNoteAgt.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_digitToWord.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
	<s:head debug="false"/>
</head>

<body onload="getRetailerUserList();">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
	<div id="top_form">
	<div id="limitDiv" style="align: center;"></div>
	<h3><s:property value="#application.TIER_MAP.RETAILER" /> <s:text name="Debit_Note"/></h3>
	<s:div  id="x">
		<s:form  action="agt_act_debitNote_Entry" onsubmit="return validateData()">
			<table border="0" cellpadding="2" cellspacing="0" >
				<tr>
					<td><table><s:label key="label.party.type" /></table></td>
					<td colspan="2"><table>
					<s:hidden theme="simple" name="partyType" id="partyType" value="RETAILER"/>
					<s:textfield label="" readonly="true"  value="%{#application.TIER_MAP.RETAILER.toUpperCase()}" required="true"></s:textfield></table></td>
				</tr>
				
				<tr><td colspan="3"><s:div id="retName1"></s:div></td></tr>
				<tr>
					 <td colspan="3" align="left">	<div id="retlistDiv" style="display: none">
													<table>
													<s:hidden name="orgNameValue" id="orgNameValue"></s:hidden>
													<s:select headerKey="-1" headerValue="%{getText('label.please.select')}"
														key="label.org.name" name="orgName" id="orgName"
														list="{}" cssClass="option"
														onchange="verifyRetailerWithOutstandingBalance(this.value,'orgName','amount');" required="true"></s:select>
														</table>
								
													</div>
				</td>
							</tr>
				
				<tr><td colspan="3"><s:div id="amount1"></s:div></td></tr>				
				<tr>
					<td><table><s:label key="label.enter.amt" /></table></td>
					<td colspan="2"><table><s:textfield  name="amount" label="" id="amount" size="15" maxlength="12" value="" required="true"></s:textfield></table></td>
				</tr>
				
				<tr><td colspan="3"><s:div id="remarks1"></s:div></td></tr>
				<tr>
					<td valign="top" align="right"><table><s:label key="label.remarks" /></table></td>
					<td colspan="2"><table><s:textarea label="" cssStyle="height: 2cm;width: 6cm; border-color : black"  name="remarks" id="remarks" rows="5" cols="6" value="" required="true"></s:textarea></table></td>
				</tr>
						
				
			<tr>
				<td align="center"><table><s:submit theme="simple" targets="bottom" key="btn.ok" cssClass="button"/></table></td>
				<td align="left" colspan="2"><table><s:reset theme="simple" key="btn.reset" cssClass="button" onclick="return removeMsgs()"/></table></td>
			</tr>
						

			</table>
			
			</s:form>
			 
   
     <s:div theme="ajax" id="bottom" >  
			
		
		 </s:div>
 </s:div></div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_debitNote_Menu.jsp,v $
$Revision: 1.3 $
</code>