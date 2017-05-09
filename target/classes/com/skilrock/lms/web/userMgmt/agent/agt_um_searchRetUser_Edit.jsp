<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>

		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<script>
function isValidValues(){

 var frmVal  = new parent.frames[0].Validator("userDetails");
 /* frmVal.aV("emailId","req","Please Enter Email Address","email");
  frmVal.aV("emailId","email","Please Enter Correct Email Address","email");

  frmVal.aV("phoneNbr","req","Please Enter Phone Number","phone");
  frmVal.aV("phoneNbr","numeric","Please Enter Correct Value For Phone Number","phone");

  frmVal.aV("mobileNbr","req","Please Enter Mobile Number","Mobile");
  frmVal.aV("mobileNbr","numeric","Please Enter Correct Value For Mobile Number","Mobile");
  frmVal.aV("commentsId","req","Please Enter Comments","comments");
*/  
  
  
  		var terminalCount = $("#terminalCount").val();
var outstandingBal = $("#outstandingBal").val() ;
var comment = $("#commentsId").val() ;
 var status = $("#Status :selected").text() ;
 
if(comment.trim() == "" )
	{
	$("#cmmntErr").html("<font color='red'>Please enter comment.</font> ");
			$("#cmmntErr").show() ;
	return false ;
	}
	else
	{
	$("#cmmntErr").hide() ;
	return true;
	}

			

  //alert(document.error_disp_handler.isValid);
//return document.error_disp_handler.isValid;

}


</script>

<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
			$(document).ready(function() { 	
				if($("#Status").val() == "TERMINATE"){
					$(".hideButton").hide();
					$('form').submit(function(e){ e.preventDefault(); });									
				}
				if($("#toTerminate").val() == "NO" && ($("#terminalCount").val() > '0' || $("#outstandingBal").val() > '0.00'))
				{
				$("#Status option[value='TERMINATE']").prop('disabled', true);
				}
				//alert("EPSA");
				//$("#Status option[value='INACTIVE']").prop('disabled',true);
			});
				
</script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Organization Details
				</h3>


				<s:form action="agt_um_searchRetUser_Save"
					onsubmit="return isValidValues()" name="userDetails">
					<s:actionerror />
					<s:fielderror>
					</s:fielderror>
					<s:set name="creditLimit"
						value="%{#session.USER_SEARCH_RESULTS.orgCreditLimit}"></s:set>
					<%
						NumberFormat format = NumberFormat.getInstance();
							format.setMaximumFractionDigits(2);
							Double dobl = (Double) pageContext.getAttribute("creditLimit");
							String creditLimit = format.format(dobl);
							//creditLimit = creditLimit.replace(",", "");
							session.setAttribute("CREDIT_LIMIT", creditLimit);
					%>
					<table border="0" cellpadding="2" cellspacing="0" width="450">
<s:hidden name="toTerminate" id="toTerminate" value="%{#session.TO_TERMINATE}" />


						<s:textfield label="Organization Name" name="orgName"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgName}" />

						<s:textfield label="Organization Type" name="orgType"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgType}" />
						<s:textfield label="Address line 1" name="orgAddr1"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgAddr1}" />
						<s:textfield label="Address line 2" name="orgAddr2"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.orgAddr2}" />
						<s:textfield label="City" name="orgCity" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgCity}" />
						<s:textfield label="Country" name="orgCountry" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgCountry}" />
						<s:textfield label="State" name="orgState" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgState}" />
							<s:hidden theme="simple" name="orgPin" value="{#session.USER_SEARCH_RESULTS.orgPin}"></s:hidden>
						<!-- <s:textfield label="Pin" name="orgPin" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgPin}" /> -->
						<s:textfield label="Credit Limit" name="orgCreditLimit"
							readonly="true" value="%{#session.CREDIT_LIMIT}" />
						<s:textfield label="Org Status" name="orgStatus" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.orgStatus}" />
							<s:textfield key="label.terminal.count" name="terminalCount" id="terminalCount"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.terminalCount}" />
							<s:textfield key="label.outstanding.bal" name="outstandingBal" id="outstandingBal"
							readonly="true" value="%{#session.USER_SEARCH_RESULTS.outstandingBal}" />
							

						<tr>
							<td>
								<h3>
									<s:text name="label.user.detail" /> <div id="cmmntErr" style="display:none;"></div>
								</h3>
							</td>
						</tr>

						<s:textfield label="First Name" name="firstName" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.firstName}" />
						<s:textfield label="Last Name" name="lastName" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.lastName}" />
						<s:textfield label="Current Status" name="status" readonly="true"
							value="%{#session.USER_SEARCH_RESULTS.status}" />

						<s:select label="Edit Status " id="Status" name="status1" headerKey="-1"
							cssClass="option" list="{'ACTIVE','INACTIVE','BLOCK','TERMINATE'}"
							value="#session.list.{status}" />
						<tr>
							<td align="center">
								<s:div id="email"></s:div>
							</td>
						</tr>
						<s:textfield label="Email Address" name="emailId"
							cssStyle="background-color:  #F9FCCD"
							value="%{#session.USER_SEARCH_RESULTS.emailId}" maxlength="45" />
						<tr>
							<td align="center">
								<s:div id="phone"></s:div>
							</td>
						</tr>

						<s:textfield label="Phone Number" id="phoneId" name="phoneNbr"
							cssStyle="background-color:  #F9FCCD" maxlength="15"
							value="%{#session.USER_SEARCH_RESULTS.phoneNbr}" />

						<tr>
							<td align="center">
								<s:div id="Mobile"></s:div>
							</td>
						</tr>
						<s:textfield label="Mobile Number" id="mobileId" name="mobileNbr" cssStyle="background-color:  #F9FCCD"
							maxlength="15" value="%{#session.USER_SEARCH_RESULTS.mobileNbr}" />
						<tr>
							<td align="center">
								<s:div id="comments"></s:div>
							</td>
						</tr>
						<s:textfield label="Comments" id="commentsId" name="comments" cssStyle="background-color:  #F9FCCD" />

						<tr>
							<td>
							</td>
							<td>
								<table>
									<s:submit value="Update Changes" align="center"
										cssClass="button hideButton" />
								</table>
							</td>
						</tr>

					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_searchRetUser_Edit.jsp,v $
$Revision: 1.3 $
</code>