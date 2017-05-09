<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script type="text/javascript">
	var isSE = '<s:property value="#application.IS_SCRATCH"/>';
	var isDG = '<s:property value="#application.IS_DRAW"/>';
	var isOLA = '<s:property value="#application.IS_OLA"/>';
	var isCS = '<s:property value="#application.IS_CS"/>';</script>
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/backOffice/js/receipt.js" />
	<s:head theme="ajax" debug="false"/>
	<!-- <script>
		function voucherValidateForm(){
			var frmVal  = new parent.frames[0].Validator("searchgame");		
		  	frmVal.aV("recieptnumber","alphanumeric","Please Enter Valid Receipt Number","recieptnumber1");	
		  	if(!voucherValidateDates()) return false;
		   	return document.error_disp_handler.isValid;
	  	}
	  	
	  	function voucherValidateDates() {
			var isInValid = false;
			var VStartDate = _id.v("vst");
			var VEndDate =  _id.v("ved");    
			var TStartDate = _id.v("tst");
			var TEndDate =  _id.v("ted");  
			if ((VStartDate == "" || VEndDate == "") && (TStartDate == "" || TEndDate == "")) {
				isInValid = true;
				_id.i("dates", "Please Enter all the dates", "e");       
			}else {
				if ((VEndDate < VStartDate) || (TEndDate < TStartDate)) {
					isInValid = true;
					_id.i("dates", "End date should be greater then or equals to start date", "e"); 
				}
			}
			if (isInValid) {	
				return false;
			}
			_id.i("dates", "");
			return true;
		
		}
	</script> -->
</head>

	<body onload="getRetailerList('agt_rep_searchReceipt_FetchRetailer.action','retlist', 'RETAILER'); changeReceiptTypeForAgent('RETAILER',isDG,isSE,isOLA,isCS); ">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">
     <div id="top_form">
  <h3>Search Voucher</h3>
	
	
  <s:form name="searchgame" action="agt_rep_searchReceipt_Search" onsubmit="return voucherValidateForm();"> 		
	  <table  border="0" cellpadding="2" cellspacing="0" width="500">
		<tr><td colspan="2"><div id="recieptnumber1"></div></td></tr>
		<tr>
			<td colspan="1" align="right"><label class="label">Voucher Number<span class="required">*</span>:</label></td>
			<td align="left"><table><s:textfield name="recieptnumber" value="" size="11" id="recieptnumber" /></table></td>
		</tr>
		<tr><td colspan="2"><div id="dates"></div></td></tr>
		<tr>
			<td colspan="1" align="right"><label class="label">Voucher Date<span class="required">*</span>:</label></td>	
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
							<label class="label">&nbsp;From<span class="required">*</span>:</label>
				    		<input  type="text" name="VStartDate" id="vst" readonly size="12" onchange="document.getElementById('tst').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('vst'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>', '<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				    	</td>
				    	<td>
							<label class="label">To<span class="required">*</span>:</label>
				    		<input  type="text" name="VEndDate" id="ved" readonly size="12" onchange="document.getElementById('ted').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('ved'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>','<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				   		</td>
				  </tr>
				  
				</table>			
			</td>
		</tr>
		<tr>
			<td colspan="1" align="right"><label class="label">Transaction Date<span class="required">*</span>:</label></td>	
			<td colspan="1" align="left">									
				<table>
				<s:set name="stDate"  value="#application.DEPLOYMENT_DATE" />
				<s:set name="endDate" value="#session.presentDate" />
					<tr>
						<td>
							<label class="label">&nbsp;From<span class="required">*</span>:</label>
				    		<input  type="text" name="TStartDate" id="tst" readonly size="12" onchange="document.getElementById('vst').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('tst'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>', '<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				    	</td>
				    	<td>
							<label class="label">To<span class="required">*</span>:</label>
				    		<input  type="text" name="TEndDate" id="ted" readonly size="12" onchange="document.getElementById('ved').value=''"/>
				    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('ted'),'yyyy-mm-dd', this, '<s:property value="%{endDate}"/>','<%=startDate%>', '<s:property value="%{endDate}"/>')" />
				   		</td>
				  </tr>
						
				</table>			
			</td>
		</tr>
		<tr>
			<td colspan="1" align="right"><label class="label">Voucher To<span class="required">*</span>:</label></td>
			<td align="left"><table><s:select name="usertype" id="user_type" list="#{'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase(), 'GOVT':'GOVT', 'PLAYER':'PLAYER'}" onchange="changeReceiptTypeForAgent(this.value,isDG,isSE,isOLA,isCS); getRetailerList('agt_rep_searchReceipt_FetchRetailer.action','retlist', this.value);" cssClass="option"/></table></td>
		</tr>
		

		<tr><td  id="retDiv" align="right"></td><td><div style="text-align: left;" id="retlist">
		
		<div id="retlistDiv" style="display: none">
								<table>
								<s:select name="retOrgId" id="retOrgId"
									label="Select %{#application.TIER_MAP.RETAILER} Organization" list="{}"
									cssClass="option" headerKey="-1"
									headerValue="--Please Select--" 
									required="true" />
								</table>
								
								</div>
		</div></td></tr>
		
		<tr>
			<td colspan="1" align="right"><label class="label">Voucher Type<span class="required">*</span>:</label></td>
			<td align="left"><table><s:select name="receiptstatus" id="rec_type" headerKey="ALL" headerValue="--Please Select--" list="#{'VAT':'VAT_RECEIPT'}" cssClass="option"/></table></td>
		</tr>
		
		<tr>
			<td><s:submit name="search" value="Search" align="right"  targets="down" theme="ajax" cssClass="button"/></td>
		</tr>
		
	 </table>
	 
	</s:form> 
	<div id="down" style="text-align: left;width: 100%" ></div>
	
	</div></div>
	
 	
 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_searchReceipt_Menu.jsp,v $
$Revision: 1.2 $
</code>