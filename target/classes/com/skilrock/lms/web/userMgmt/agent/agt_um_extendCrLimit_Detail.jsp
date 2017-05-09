<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
			$(document).ready(function() { 	
				if($("#Status").val() == "TERMINATE"){
					$(".hideButton").hide();									
				}
			});
				
</script>
		<s:head theme="ajax" debug="false" />
	


	<script>

function enable()
{
  //alert('hello');
  var id=document.getElementById("extendLimit");
  id.readOnly=false;
  id.style.backgroundColor =  "#F9FCCD";
  document.getElementById("daysAfterId").readOnly=false;
  document.getElementById("daysAfterId").style.backgroundColor = "#F9FCCD";
  document.getElementById('submitButtonId').disabled=false;
 // return false;

}


function checkValidation()
{
	document.getElementById('isResetORExtend').value='';
	document.getElementById('isResetORExtend').value="extend";
     var flag=true;
     var afterDays=document.getElementById('daysAfterId').value;
     var afterDaysValidity=isNumeric(afterDays);
     
     var extendedAmount=document.getElementById('extendLimit').value;
 //alert(extendedAmount);
     var extendedAmtValidity=isNumericAmt(extendedAmount);
     //alert(extendedAmtValidity);
      if(afterDaysValidity==true || afterDays=="" || afterDays==0)
      {
       document.getElementById('daysAfterId1').innerHTML = "<font color = 'red'>Please Enter Valid value for Days(Zero not allowed)</font>";
       flag=false;
      }  
      else if(afterDays < 1 || afterDays > 90)
      {
        document.getElementById('daysAfterId1').innerHTML ="<font color = 'red'>Exetend Limit fot minimum 1 day or maximum 90 days</font> ";
        flag= false;
      }else
      document.getElementById('daysAfterId1').innerHTML ="";
      
      
      if(extendedAmtValidity==true || extendedAmount=="" || extendedAmount==".")
      {
      // alert("inside true" + extendedAmount);
       document.getElementById('extendLimit1').innerHTML = "<font color = 'red'>Please Enter Valid value for Amount</font>";
       flag=false;
      }  
     else if(extendedAmount==0)
     {
        document.getElementById('extendLimit1').innerHTML = "<font color = 'red'>Zero Value for Amount is not valid</font>";
        flag=false;
      }
      
     else
      {    
           var array = new Array();
			array = extendedAmount.split(".");
			//alert(array.length);
			//alert(array[1].length);
			if(array.length >1 )
			 {
			   //alert(array[1].length);
			   if(array[1].length > 2 || array[1].length < 1)
			   {
			      document.getElementById('extendLimit1').innerHTML ="<font color = 'red'>Enter upto two decimal Only</font>";
			      flag=false;
			    }
			      else
			       document.getElementById('extendLimit1').innerHTML ="";
			 }else
			 document.getElementById('extendLimit1').innerHTML ="";
         
      }
   //   alert(_id.o('extendLimit').value);
   	if(flag){
     var  _resp  = _ajaxCall("fetchAgtCrLimit.action?orgId="+_id.o('orgId').value+"&message=XCL&listType="+_id.o('extendLimit').value);
	 var isCrLimit=true;		
		if(_resp.data!='TRUE'){
	 		 alert(_resp.data);
	 		 isCrLimit=false;
	 	}  
	}
    return flag && isCrLimit;
    
}

 function isNumeric(value) {
 	//alert("called");
 	var strValidChars = "1234567890";
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	
	return flag;
}

function isNumericAmt(value) {

 	var strValidChars = "1234567890.";
 	var dotCount=0;
	var strChar;
	var flag = false;
	var strString = value;
	   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if(strChar==".")
		  dotCount=dotCount+1;
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	if(dotCount > 1 )
	 flag=true;
	return flag;
}

function resetExtendedLimit(){
	document.getElementById('isResetORExtend').value='';
	document.getElementById('isResetORExtend').value="reset";
    document.getElementById('extendLimit').value=0;
    document.getElementById('daysAfterId').value=1;
     return true;

}

</script>
</head>
	<body>

<%@include	file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.RETAILER" /> Organization Details
				</h3>

				<s:form action="agt_um_extendCrLimit_Save" name="extend" >
					<s:set name="availCredit"   value="%{#session.ORG_SEARCH_RESULTS.availableCredit}" ></s:set>
					<s:set name="creditlimit"   value="%{#session.ORG_SEARCH_RESULTS.orgCreditLimit}" ></s:set>
					<s:set name="currentCreditAmt"   value="%{#session.ORG_SEARCH_RESULTS.currentCreditAmt}" ></s:set>
					<s:set name="secDeposit"   value="%{#session.ORG_SEARCH_RESULTS.secDeposit}" ></s:set>
					<s:set name="claimableBal"   value="%{#session.ORG_SEARCH_RESULTS.claimableBal}" ></s:set>
				
   <%
   
     NumberFormat format = NumberFormat.getInstance();
	 format.setMaximumFractionDigits(2);
	 double dobl = (Double) pageContext.getAttribute("availCredit");
	 Double dob2 = (Double) pageContext.getAttribute("creditlimit");
	 Double dob3 = (Double) pageContext.getAttribute("currentCreditAmt");
	 double dob4 = (Double) pageContext.getAttribute("claimableBal");
	 double dob5 = dobl-dob4;
	 //String availCredit = format.format(dobl); 
	 String creditLimit = format.format(dob2);	
     String currentCreditAmt = format.format(dob3); 
     //String balance = format.format(dob5);
     String secDeposit = format.format((Double) pageContext.getAttribute("secDeposit")) ;										 
												 	

 %>
<table border="0" bordercolor="#CCCCCC" cellpadding="0" cellspacing="0" width="600">
				<tr>
					<td>

					<table width="100%" border="0" cellpadding="4" cellspacing="0"
						 align="center" >
						<tr>
							<td align="right" class="label">
								Organization Name:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgName}" />
								<s:hidden value="%{orgId}" id="orgId" name="orgId"></s:hidden> 
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization Type:
							</td>
							<td class="data">
								<s:property value="%{#application.TIER_MAP[#session.ORG_SEARCH_RESULTS.orgType].toUpperCase()}" />
 							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization Address Line1:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgAddr1}" />
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization Address Line2:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgAddr2}" />
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization City:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgCity}" />
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization Country:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgCountry}" />
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization State:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgState}" />
							</td>
						</tr>
						<tr>
							<td align="right" class="label" style="display: none;">
								Organization Pin:
							</td>
							<td class="data" style="display: none;">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgPin}" />
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								 Credit Limit:
							</td>
							<td class="data">
								<%=creditLimit %>
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								 Security Deposit:
							</td>
							<td class="data">
								<%=secDeposit %>
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								 Current Credit Amount:
							</td>
							<td class="data">
								<%=currentCreditAmt %>
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Balance:
							</td>
							<td class="data">
								<%=FormatNumber.formatNumberForJSP(dob5) %>
							</td>
						</tr>
						<tr>
							<td align="right" class="label">
								Organization Status:
							</td>
							<td class="data">
								<s:property value="%{#session.ORG_SEARCH_RESULTS.orgStatus}" />
							</td>
						</tr>
						
					<s:set name="extendedLimitStr" value="%{#session.ORG_SEARCH_RESULTS.extendedCredit}"></s:set>
					<s:hidden name="orgId" value="%{#session.ORG_SEARCH_RESULTS.orgId}"></s:hidden>
					<tr>
							
							<td colspan="2">
								<div id="extendLimit1"></div>
								<s:textfield id="extendLimit" name="extendedLimit" label="Extended Credit Limit"
									value="<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("extendedLimitStr")).replaceAll(",","")%>"
									 maxlength="12" readonly="true"></s:textfield>
							</td>
					</tr>
					
					<tr>
							<td align="right" class="label">
								For:
							</td>
							<td>
								<div id="daysAfterId1"></div>
								<table border="0">
									<tr><td>
										<table><s:textfield name="daysAfter" id="daysAfterId" maxlength="3" 
											value="%{#session.ORG_SEARCH_RESULTS.extendsCreditLimitUpto}" 
											readonly="true"/></table>
						   		</td><td> Days</td>
						   		</tr>
						   		</table> 
						   </td>
							
							
					</tr>
						
					<tr>	
							<td>&nbsp;</td>
							<td>
								<input type="button" value="Edit"  class="button hideButton" onclick="enable()"  />
							</td>
					</tr>
						
					<tr>
						<td>&nbsp;</td>
						<td align="left" colspan="2" nowrap="nowrap">
						<table border="0">
							<tr>
								<td nowrap="nowrap">							
									<table>
										<s:submit value="Extend Limit" theme="simple" id="submitButtonId" align="center" cssClass="button hideButton" disabled="true" onclick="return checkValidation()" onkeypress="return checkValidation()"/>
									</table>
								</td>
								<td>
									<table>
										<s:submit value="Reset Extend Limit" theme="simple" id="resetSubmitButtonId" align="center" cssClass="button hideButton" onclick="return resetExtendedLimit()" onkeypress="return resetExtendedLimit()"/>
									</table>
								</td>
							</tr>
							</table>
						</td>
					</tr>	
						
					</table>
				</td></tr>
				
				</table>
				

					<s:hidden value="" id="isResetORExtend" name="isResetORExtend"/>

				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_extendCrLimit_Detail.jsp,v $
$Revision: 1.3 $
</code>