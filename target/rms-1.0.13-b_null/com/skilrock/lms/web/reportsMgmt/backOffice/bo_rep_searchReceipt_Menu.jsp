<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script type="text/javascript">
	var isSE = '<s:property value="#application.IS_SCRATCH"/>';
	var isDG = '<s:property value="#application.IS_DRAW"/>';
	var isOLA = '<s:property value="#application.IS_OLA"/>';
	var isCS = '<s:property value="#application.IS_CS"/>';
	var isSLE = '<s:property value="#application.IS_SLE"/>';
	</script>
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/receipt.js" />
	<s:head theme="ajax" debug="false"/>

</head>

	<body onload="retURL('bo_rep_searchReceipt_FetchAgent.action','agentlist','AGENT'); changeReceiptType('AGENT',isDG,isSE,isOLA,isCS,isSLE);">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3><s:text name="menu.srch.voucher"/></h3>	
	
  <s:form name="searchgame" action="bo_rep_searchReceipt_Search" onsubmit="return voucherValidateForm();"> 		
	 <table  border="0" cellpadding="2" cellspacing="0" width="500">
		<tr><td colspan="2"><div id="recieptnumber1"></div></td></tr>
		<tr>
			<td colspan="1" align="right"><label class="label"><s:text name="label.voucher.nbr"/>:</label></td>
			<td align="left"><table><s:textfield name="recieptnumber" value="" size="11" id="recieptnumber" /></table></td>
		</tr>
		<tr><td colspan="2"><div id="dates"></div></td></tr>
		<tr>
			<td colspan="1" align="right"><label class="label"><s:text name="label.voucher.date"/><span class="required">*</span>:</label></td>	
			<td colspan="1" align="left">									
				<table>
				<s:set name="stDate"  value="#application.DEPLOYMENT_DATE" />
				<s:set name="endDate" value="#session.presentDate" />
				<%	
					//String startDate = CommonMethods.convertDateInGlobalFormat((String)application.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)application.getAttribute("date_format"));
					String startDate = CommonMethods.convertDateInGlobalFormat((String)application.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)application.getAttribute("date_format"));
				 %>
					<tr>
						<td>
							<label class="label">&nbsp;<s:text name="label.from"/><span class="required">*</span>:</label>
				    		<input  type="text" name="VStartDate" id="vst" readonly size="12" onchange="document.getElementById('tst').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('vst'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>', '<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				    	</td>
				    	<td>
							<label class="label"><s:text name="label.to"/><span class="required">*</span>:</label>
				    		<input  type="text" name="VEndDate" id="ved" readonly size="12" onchange="document.getElementById('ted').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('ved'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>','<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				   		</td>
				  </tr>
				  
				</table>			
			</td>
		</tr>
		<tr>
			<td colspan="1" align="right"><label class="label"><s:text name="label.trsc.date"/><span class="required">*</span>:</label></td>	
			<td colspan="1" align="left">									
				<table>
				<s:set name="stDate"  value="#application.DEPLOYMENT_DATE" />
				<s:set name="endDate" value="#session.presentDate" />
					<tr>
						<td>
							<label class="label">&nbsp;<s:text name="label.from"/><span class="required">*</span>:</label>
				    		<input  type="text" name="TStartDate" id="tst" readonly size="12" onchange="document.getElementById('vst').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('tst'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>', '<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				    	</td>
				    	<td>
							<label class="label"><s:text name="label.to"/><span class="required">*</span>:</label>
				    		<input  type="text" name="TEndDate" id="ted" readonly size="12" onchange="document.getElementById('ved').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('ted'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>','<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				   		</td>
				  </tr>
				  
				</table>			
			</td>
		</tr>
		<tr>
			<td colspan="1" align="right"><label class="label"><s:text name="label.voucher.to"/><span class="required">*</span>:</label></td>
			<td align="left"><table><s:select name="usertype" id="user_type"  list="#{'AGENT':#application.TIER_MAP.AGENT.toUpperCase(), 'GOVT':'GOVT', 'PLAYER':'PLAYER', 'RETAILER':
#application.TIER_MAP.RETAILER.toUpperCase()}" onchange="changeReceiptType(this.value,isDG,isSE,isOLA,isCS,isSLE); retURL('bo_rep_searchReceipt_FetchAgent.action','agentlist',this.value); " cssClass="option"/></table></td>
		</tr>
		<tr>
							
							<td colspan="2">
								
								<div id="agentlistDiv" style="margin-left:20px;display: none">
								<table style="margin-left:10px">
									<s:select name="agentOrgId" id="agentOrgId"
									label="%{getText('label.select')} %{#application.TIER_MAP.AGENT} %{getText('label.org')}" list="{}" cssClass="option"
									headerKey="" headerValue="%{getText('label.please.select')}"
									onchange="getData(this.value);" required="true" />
									</table>
								</div>
									
							</td>
						</tr>

						<tr>
								
						<td colspan="2">
								<div id="retlistDiv" style="display: none">
								<table>
								<s:select name="retOrgId" id="retOrgId"
									label="%{getText('label.select')} %{#application.TIER_MAP.RETAILER} %{getText('label.org')}" list="{}"
									cssClass="option" headerKey="-1"
									headerValue="%{getText('label.please.select')}" 
									required="true" />
								</table>
								
								</div>
							</td>
						</tr><!--
		<tr><td  id="agentDiv" align="right"></td><td><div style="text-align: left;" id="agentlist"></div></td></tr>
		<tr><td  id="retDiv" align="right"></td><td><div style="text-align: left;" id="retlist"></div></td></tr>
		
		--><tr>
			<td colspan="1" align="right"><label class="label"><s:text name="label.voucher.type"/><span class="required">*</span>:</label></td>
			<td align="left"><table><s:select name="receiptstatus" id="rec_type" headerKey="ALL" headerValue="%{getText('label.please.select')}" list="#{'VAT':getText('label.vat.rec')}" cssClass="option"/></table></td>
		</tr>
		
		<tr>
			<td><s:submit name="search" key="btn.srch" align="right"  targets="down" theme="ajax" cssClass="button"/></td>
		</tr>
		
	 </table>
	 
	</s:form> 
	<div id="down" style="text-align: left;width: 100%" ></div>
	
	</div></div>
	
 	
 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_searchReceipt_Menu.jsp,v $
$Revision: 1.3 $
</code>