<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/agent/js/agt_accMgmt_validation.js" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head debug="false"/>
	</head>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">
<div id="top_form">
<h3>ChequeBounce</h3>
	   
			<s:form action="agt_act_chqBounce_Save" onsubmit="return verifyChequeBounceAmt();">
			<s:token />	   		    
			<table border="1" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC">
			
			  <s:iterator id="Results1" value="#session.ChequeDetailsRetailer">
			    <tr>
			   <td><s:text name="Cheque Number"></s:text></td> <td><s:property value="%{chequeNumber}" /><s:hidden name="chequeNumber" value="%{chequeNumber}"/></td>
		        </tr>
			        <tr>  
			     <td> 
			   <s:text name="Cheque Date" /></td><td><s:property value="%{chequeDate}" /><s:hidden name="chequeDate" value="%{chequeDate}"/></td>
			     
			     </tr>
			     
			       <tr>
      				 <td> <s:text name="Issuing Party Name" /></td> <td>  <s:property value="%{issuePartyname}" /><s:hidden name="issuePartyname" value="%{issuePartyname}"/></td>
			  
			    </tr>
			    
			      <tr>
			    <td> 
			    
 <s:text name="Organization Name" /></td> <td> <s:property value="%{orgName}" /><s:hidden name="orgName" value="%{orgId}"/></td>
			  			    
			    
			     </tr>
			       <tr>
			     <td>
	 <s:text name="Bank Name" />	</td> <td> <s:property value="%{bankName}"/><s:hidden name="bankName" value="%{bankName}"/></td>
			     
			       </tr>
			         <tr>
			      <td>
			      	 <s:text name="Cheque Amount" />	</td> <td> <s:set name="strchequeAmount" value="%{chequeAmount}" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strchequeAmount")) %><s:hidden name="chequeAmount" value="%{chequeAmount}"/></td>
			                   
			       </tr>
			       <div id="chequeBounceErrMsg">
			       	
			       </div>
			       <tr>
			      <td>
			         <s:textfield name="chequeBounceCharges" label="Cheque Bounce Charges" value="0.00" maxlength="6"></s:textfield>
			      </td>
		        </tr>
			       
			  </s:iterator> 
			
			<tr><td height="15">
			   			<div id="subCheck" style="position:absolute;"></div>
			<s:submit align="center" value="Cheque Bounce" cssClass="button" id="subTrans" onclick="return _subValid(this.id);"/>
						
						</td></tr>
			</table>
		</s:form>
		
			</div></div>	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_act_chqBounce_Detail.jsp,v $
$Revision: 1.2 $
</code>