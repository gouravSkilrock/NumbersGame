<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<head>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>
		var projectName="<%=request.getContextPath() %>";
		var image = "<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif";
	</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_digitToWord.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_cashChequePayment.js" />	
	<s:head theme="ajax" debug="false"/>
</head>

<body onload="getRetailerList();">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap">
  <div id="top_form">
  <h3>Cheque Receipt Detail</h3>
  <div id="im" style="text-align: center;">Please Wait....<img src="<%=request.getContextPath() %>/LMSImages/images/loadingAjax.gif" /></div>
  		<div id="table_error_div" style="text-align: left; color: red;"><s:actionerror/></div>
  		<div id="cart" style="display:none;">
		<s:form name="retailerPaymentForm">
			<s:token />					
			<div id="hiddenValues"></div>			
			<table width="100%" border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" id="chq_dtl_div" style="display: none;"> 		
				<tr>
					<td><div id="chq_detail_tbl"></div></td>	
				</tr>
				<tr>
					<td align="center">						
							<s:submit theme="simple" action="agt_act_chq_Menu" value="Cancel" cssClass="button"/>
							<div id="subCheck" style="position:absolute;"></div>
							<s:submit theme="simple"  value="Submit Receipt" action="agt_act_chq_Save" onclick="return createHidenFields(this.id);" cssClass="button"/>							
					</td>
				</tr>
		  </table>
		</s:form>
		</div>
		
		
		

		
		<div id="x" style="display:none;">
		<s:form name="na" action="agt_act_chq_AddToCart" onsubmit="return validateChequeEntries()">	
			<table border="0" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC"  width="450">				
            	<tr><th align="left" colspan="2">Cheque Receipt</th></tr>
            	
				<tr><td colspan="2"><div id="orgTypeError" style="text-align: center;"></div></td></tr>
		      	<tr>
		      		<td class="label" align="right">Organization Type</td>
		      		<td><table><s:select name="orgType" label="" id="orgType" list="#{'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}" value="%{#session.ORG_TYPE}" cssClass="option" required="true"></s:select></table></td>
		      	</tr>
				
				<tr><td colspan="2" align="center"><div id="orgNameError" style="text-align: center;"></div></td></tr>
		       	<tr>
		
		      		<td colspan="2" align="center">
					
		      		<div id="retlistDiv" style="display: none">
													<table>
								
													<s:select headerKey="-1" headerValue="-- Please Select --"
														label="Organization Name" name="orgName" id="orgName"
														list="{}" cssClass="option"
														onchange="verifyRetailerWithOutstandingBalance(this.value,'orgName','chequeNumber');" required="true"></s:select>
														</table>
								
													</div>
		      		
		      		<!-- table><s:select label="" headerKey="-1" headerValue="-- Please Select --" name="orgName" id="orgName" list="{}" value="{}" cssClass="option" onchange="verifyRetailer(this.value,'orgName','chequeNumber');" required="true"></s:select></table>-->
		      		</td>
				</tr>
			
				
				<tr><th align="left" colspan="2">Cheque Details : -</th></tr>
				
				<tr><td colspan="2"><div id="chequeNumberError" style="text-align: center;"></div></td></tr>
				<tr>
		      		<td class="label" align="right">Cheque Number</td>
		      		<td><table>
				
						<s:textfield id="chequeNumber" label="" name="chequeNumber" size="10" maxlength="10" value="" required="true"></s:textfield>
						</table>
					</td>
				</tr>
				<tr><td colspan="2"><div id="issuePartynameError" style="text-align: center;"></div></td></tr>
				
				<tr>
		      		<td class="label" align="right">Issuing party Name</td>
		      		<td><table>
						<s:textfield  label="" name="issuePartyName" id="issuePartyname" size="20" maxlength="30" required="true" ></s:textfield>
						</table>
					</td>
				</tr>
				<tr><td colspan="2"><div id="bankNameError" style="text-align: center;"></div></td></tr>
				<tr>
		      		<td class="label" align="right">Bank Name</td>
		      		<td><table>
						<s:textfield label="" name="bankName" id="bankName" size="20" maxlength="30" required="true" ></s:textfield>
						</table>
					</td>
				</tr>
				
				<tr><td colspan="2"><div id="chequeAmountError" style="text-align: center;"></div></td></tr>
				<tr>
		      		<td class="label" align="right">Cheque Amount</td>
		      		<td><table>
						<s:textfield id="chequeAmount" label="" name="chequeAmount" size="15" maxlength="12" value="" required="true"></s:textfield>
						</table>
					</td>
				</tr>
				<tr><td colspan="2"><div id="verifyAmntError" style="text-align: center;"></div></td></tr>
				<tr>
		      		<td class="label" align="right">Verify Cheque Amount</td>
		      		<td><table>
						<s:textfield id="verifychequeAmount" label="" name="verifyChequeAmount" size="15" maxlength="12" required="true"></s:textfield>
						</table>
					</td>
				</tr>
				
				<tr><td colspan="2"><div id="chequeDateError" style="text-align: center;"></div></td></tr>
				<s:set name="aa" value="#session.CHEQUE_END_DATE_START"/>
			    <s:set name="BB" value="#session.CHEQUE_START_DATE_START"/>
		      	<s:hidden name="startDate" value="%{BB}"  id="chqStdate"/>
		      	<%	
					String endDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("CHEQUE_END_DATE_START"), "yyyy-mm-dd", "mm/dd/yyyy");
					String startDate = CommonMethods.convertDateInGlobalFormat((String)session.getAttribute("CHEQUE_START_DATE_START"), "yyyy-mm-dd", "mm/dd/yyyy");
				%>							
				<tr>
					
					<td colspan="1" align="right">
						<label class="label">Cheque Date<span class="required">*</span>:</label>
					</td>
    				<td nowrap="nowrap">
    					<table><tr>
    						<td>
	    						<input  type="hidden" name="sd" value="<%=startDate%>" id="chq_st_date" />
	    						<input  type="hidden" name="ed" value="<%=endDate%>" id="chq_end_date" />
	    						<input  type="text" name="chequeDate" id="chequeDate" value="<%=endDate%>" readonly size="12" />
    						</td>
    						<td> <div id="chqSub"></div></td>
    					</tr></table>
    					    					
   					</td>
				</tr>

				
				<tr>
					<td align="left"></td>
					<td align="left">
						<s:reset theme="simple" value="Reset" cssClass="button" onclick="return removeMessages()"/>
						<s:submit theme="simple" value="Submit" targets="bottom" cssClass="button"/>
					</td>
				</tr>
					</table>
							<s:hidden name="paymentType" value="CHEQUE"/>
			</s:form>
			</div>
 

</div></div>
<br /><br /><br />
<br /><br /><br />
</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_chq_Menu.jsp,v $
$Revision: 1.3 $
</code>