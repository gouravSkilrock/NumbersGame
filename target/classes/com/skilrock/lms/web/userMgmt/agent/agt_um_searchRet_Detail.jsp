<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
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
			scrolling="no" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script>
			$(document).ready(function() { 	
				if($("#Status").val() == "TERMINATE"){
					$(".hideButton").hide();									
				}
				//$("#Status option[value='INACTIVE']").prop('disabled',true);
			});	
</script>
<s:head theme="ajax" debug="false" />
		<script>
function isValuesValid(){

  var frmVal  = new parent.frames[0].Validator("orgDetails");

  var formStatus ;
  
  		var terminalCount = $("#terminalCount").val();
var outstandingBal = $("#outstandingBal").val() ;
var comment = $("#commentsId").val() ;
 var status = $("#Status :selected").text() ;
 
if(comment.trim() == "" )
	{
	$("#cmmntErr").html("<font color='red'>Please enter comment.</font> ");
			$("#cmmntErr").show() ;
	formStatus = false ;
	}
	else
	{
	$("#cmmntErr").hide() ;
	formStatus = true;
	}

if(formStatus == true)
{
	fetchAgtCrLimit();
}
else
{
return false ;
}
			

}
function fetchAgtCrLimit()
{
  var  _resp  = _ajaxCall("fetchAgtCrLimit.action?orgId="+_id.o('orgId').value+"&message=CL&listType="+_id.o('orgCreditLimit').value);
 var isCrLimit=true;		
	if(_resp.data!='TRUE'){
 		 alert(_resp.data);
 		 isCrLimit=false;
 	}

}

function stopRKey(evt) {
  var evt = (evt) ? evt : ((event) ? event : null);
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
  if ((evt.keyCode == 13) && (node.type=="text"))  {return false;}
}

document.onkeypress = stopRKey;

function makeEmpty(){
	document.getElementsByName('comment')[0].value= '';
	return true;
}

function chkOrgStatusNew(){
	var tdHide = document.getElementById('cmntTdId');
	if(document.getElementsByName('statusNew')[0].value != 'TERMINATE'){
		if(document.getElementsByName('orgStatus')[0].value != document.getElementsByName('statusNew')[0].value){
			document.getElementById('cmnt').innerHTML = '<input onclick="return makeEmpty()" type="textfield" name="comment" value="Enter a Valid Reason" />';
			tdHide.style.display = 'block';
		}else{
			document.getElementById('cmnt').innerHTML = '';
			tdHide.style.display = 'none';
		}
	}else{
		tdHide.style.display = 'none';
		document.getElementById('cmnt').innerHTML = '';
	}
	return true;
}

</script>
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
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Edit Organization Details
				</h3>

				<s:form name="orgDetails" onsubmit="return isValuesValid();"
					action="agt_um_searchRet_Save">
					<table border="0" cellpadding="2" cellspacing="0" width="450">
						<s:actionerror />
						<s:fielderror>
						</s:fielderror>
						<s:set name="creditLimit"
							value="%{#session.ORG_SEARCH_RESULTS.orgCreditLimit}" />
						<s:set name="securityDeposit"
							value="%{#session.ORG_SEARCH_RESULTS.securityDeposit}" />
						<%
							String creditLimit = FormatNumber
										.formatNumber((Double) pageContext
												.getAttribute("creditLimit"));
								session.setAttribute("CREDIT_LIMIT", creditLimit);
								String securityDeposit = FormatNumber
										.formatNumber((Double) pageContext
												.getAttribute("securityDeposit"));
								session.setAttribute("securityDeposit", securityDeposit);
						%>

						<s:textfield label="Company Name" size="30" name="orgName"
							readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgName}" />
                        <s:hidden value="%{orgId}" id="orgId" name="orgId"></s:hidden> 
						<s:hidden theme="simple" name="orgType" value="%{#session.ORG_SEARCH_RESULTS.orgType}" />
						<s:textfield label="Company Type" size="30"
							readonly="true" value="%{#application.TIER_MAP[#session.ORG_SEARCH_RESULTS.orgType].toUpperCase()}" />
						<tr>
							<td>
								<s:div id="addr1"></s:div>
							</td>
						</tr>
						<s:textfield label="Address Line1" size="30" name="orgAddr1"
							cssStyle="background-color: #F9FCCD"
							value="%{#session.ORG_SEARCH_RESULTS.orgAddr1}" />
						<s:textfield label="Address Line2" size="30" name="orgAddr2"
							cssStyle="background-color:  #F9FCCD"
							value="%{#session.ORG_SEARCH_RESULTS.orgAddr2}" />
						
						
						<s:textfield label="Country" size="30" name="orgCountry"
							readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgCountry}" />
						<s:textfield label="State" size="30" name="orgState"
							readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgState}" />
						
						<tr>
							<td>
								<s:div id="orgcity1"></s:div>
							</td>
						</tr>	
						<s:select id="location" cssClass="option" id="orgCity"
								list="#session.CITY_LIST" name="orgCity" required="true"  label="City" value="%{#session.ORG_SEARCH_RESULTS.orgCity}"
								emptyOption="false" headerKey="-1" headerValue="--Please Select--"/>
						<!-- <s:textfield label="City" size="30" name="orgCity"
							cssStyle="background-color:  #F9FCCD"
							value="%{#session.ORG_SEARCH_RESULTS.orgCity}" /> -->
						<tr>
							<td style="display: none;">
								<s:div id="pin"></s:div>
							</td>
						</tr>
						<s:hidden theme="simple" name="orgPin" value="%{#session.ORG_SEARCH_RESULTS.orgPin}"></s:hidden>
						<s:textfield name="toTerminate" id="toTerminate" value="%{#session.TO_TERMINATE}" />
						<!-- <s:textfield label="Pin" cssStyle="background-color:  #F9FCCD"
							name="orgPin" maxlength="10"
							value="%{#session.ORG_SEARCH_RESULTS.orgPin}" /> -->

						<tr>
							<td>
								<s:div id="security"></s:div>
							</td>
						</tr>
						<s:textfield label="Security Deposit" name="orgSecurityDeposit"
							maxlength="8" value="%{#session.securityDeposit}"
							cssStyle="background-color:#F9FCCD" />
						<tr>
							<td>
								<s:div id="limit"></s:div>
							</td>
						</tr>
						<s:textfield label="Credit Limit" name="orgCreditLimit"
							maxlength="10" value="%{#session.CREDIT_LIMIT}" readonly="true" />
<s:textfield key="label.terminal.count" name="terminalCount" id="terminalCount"
							readonly="true" value="%{#session.ORG_SEARCH_RESULTS.terminalCount}" />
							<s:textfield key="label.outstanding.bal" name="outstandingBal" id="outstandingBal"
							readonly="true" value="%{#session.ORG_SEARCH_RESULTS.outstandingBal}" />
    
						<s:textfield label="CurrentStatus" name="orgStatus"
							readonly="true" value="%{#session.ORG_SEARCH_RESULTS.orgStatus}" />
						<s:select label="Edit Status " id="Status" name="statusNew" headerKey="-1"
							cssClass="option" list="{'ACTIVE','INACTIVE','BLOCK','TERMINATE'}"
							value="%{#session.ORG_SEARCH_RESULTS.orgStatus}"/>
						 <tr><td id="comment"  align="right">Comment</td> <td><s:textfield id="commentsId" name="comments" theme="simple" />&nbsp;<div id="cmmntErr" style="display:none;"></div></td></tr>
						<tr>
							<td></td>
							<td>
								<table>
									<s:submit value="Update Details" cssClass="button hideButton" />
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
$RCSfile: agt_um_searchRet_Detail.jsp,v $
$Revision: 1.3 $
</code>