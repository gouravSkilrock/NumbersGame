
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

       <script>
function isValidData(){

 var frmVal  = new parent.frames[0].Validator("bounceForm");
  frmVal.aV("chequeBounceCharges","req","Please Enter cheque bounce charges","chqBounceAmtError");
  frmVal.aV("chequeBounceCharges","decimal","Please Enter Correct Value For cheque bounce charges","chqBounceAmtError");

return document.error_disp_handler.isValid;

}
	
	function _subTrans(){
	   return (isValidData() && _subValid('subTrans'));
	
	}
</script>

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			 type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>

	<body>
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					Cheque Details
				</h3>
				<s:form action="bo_act_chqBounce_Save"  name="bounceForm" onsubmit="return _subTrans();">
					<s:token />

					<table border="0" cellpadding="2" cellspacing="0" width="450">

						<s:iterator id="Results1" value="#session.ChequeDetails">
							<tr>
								<td>
									<s:text name="Cheque Number"></s:text>
								</td>
								<td>
									<s:property value="%{chequeNumber}" />
									<s:hidden name="chequeNumber" value="%{chequeNumber}:%{transactionId}" />
								</td>
							</tr>
							<tr>
								<td>
									<s:text name="Cheque Date" />
								</td>
								<td>
									<s:property value="%{chequeDate}" />
									<s:hidden name="chequeDate" value="%{chequeDate}" />
								</td>

							</tr>

							<tr>
								<td>
									<s:text name="Issuing Party Name" />
								</td>
								<td>
									<s:property value="%{issuePartyname}" />
									<s:hidden name="issuePartyname" value="%{issuePartyname}" />
								</td>

							</tr>

							<tr>
								<td>

									<s:text name="Organization Name" />
								</td>
								<td>
									<s:property value="%{orgName}" />
									<s:hidden name="orgName" value="%{orgId}" />
								</td>


							</tr>
							<tr>
								<td>
									<s:text name="Bank Name" />
								</td>
								<td>
									<s:property value="%{bankName}" />
									<s:hidden name="bankName" value="%{bankName}" />
								</td>

							</tr>
							<tr>
								<td>
									<s:text name="Cheque Amount" />
								</td>
								<td>
									<s:set name="strchequeAmount" value="%{chequeAmount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strchequeAmount")) %>
									<s:hidden name="chequeAmount" value="%{chequeAmount}" />
								</td>

							</tr>
							<tr>
							  <td >
                                  <div id="chqBounceAmtError"></div>
                               </td>
                           </tr>
                           
							<tr>
							  <td>
									<s:textfield label="Enter Cheque Bounce Charges" value="0.0" name="chequeBounceCharges" maxlength="6"></s:textfield>
							 </td>

							</tr>
							
						</s:iterator>

			   <tr><td height="15">
			   			<div id="subCheck" style="position:absolute;"></div>
						<s:submit align="center" cssClass="button" value="Cheque Bounce" id="subTrans" />
						
						</td></tr>
					</table>
				</s:form>
			</div>
		</div>


	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_chqBounce_Detail.jsp,v $
$Revision: 1.2 $
</code>