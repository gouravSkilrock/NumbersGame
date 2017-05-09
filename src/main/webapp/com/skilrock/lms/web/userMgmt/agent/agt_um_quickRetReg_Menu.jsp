<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<s:head theme="ajax" debug="false"/>
	<script>
		function isST3Filled(){
			 var frmVal  = new parent.frames[0].Validator("na");
			  // validate the retailer organization  Entries
			  //frmVal.aV("orgName","req","Please Enter Organization Name","org");
			  //frmVal.aV("orgName","regexp=[^A-Za-z0-9\\s&_.]","Please Enter Valid Organization Name","org");			  			
			  			  
			  // validate the retailer user  Entries
			  frmVal.aV("firstName","req","Please Enter First Name","fname");
			  frmVal.aV("firstName","alpha","Please Enter Valid Name","fname");
			  
			  frmVal.aV("lastName","req","Please Enter Last Name","lname");
			  frmVal.aV("lastName","alpha","Please Enter Valid Name (Special Characters and Numbers not Allowed)","lname");
			  
			  //frmVal.aV("idType","dontselect=0","Please Select idType","id_type_error");
			  
			  frmVal.aV("idNo","req","Please Enter Id Number","id_no_error");
			  frmVal.aV("idNo","regexp=[^A-Za-z0-9\\s/,-]","Please Enter Valid Id No.","id_no_error");
			  
			  frmVal.aV("phone","req","Please Enter Phone Number","Phone");
			  frmVal.aV("phone","numeric","Please Enter Correct Value For Phone Number","Phone");
			  
			  frmVal.aV("creditLimit","req","Please Enter Limit","orglimit");
			  frmVal.aV("creditLimit","decimal","Please Enter Correct Value For Limit","orglimit");
						  			  
			return document.error_disp_handler.isValid;
	}
	</script>	
</head>

<body onload="_id.finiFocus('na','firstName')">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap">

  <div id="top_form">
 <h3>Quick Retailer Registration</h3>

	
	<s:form  name="na" onsubmit="return isST3Filled()" action="agt_um_quickRetReg_Save">
		<table  border="0" width="390px" cellspacing="0" cellpadding="2"> 
			<tr><td align="center" colspan="2"><div id="fname"><s:property value="errorMap.orgError" /></div></td></tr>   			
    		<tr><td><s:textfield label="First Name" maxlength="14" name="firstName" required="true" /></td></tr>
    
    		<tr><td align="center" colspan="2"><div id="lname"></div></td></tr>    
     		<tr><td><s:textfield label="Last Name" maxlength="14" name="lastName" required="true" /></td></tr>
     		
     		<tr><td align="center" colspan="2"><div id="id_type_error"></div></td></tr>
          	<tr><td><s:select label="Id Type" name="idType"   list="{'Passport','Driving Licence','NID'}" required="true" cssClass="option"/></td></tr>
          	
          	<tr><td align="center" colspan="2"><div id="id_no_error" style="color: red"><s:property value="errorMap.idError" /></div></td></tr>
     		<tr><td><s:textfield label="Id Number" name="idNo"  id="id_no" required="true" maxlength="15" /></td></tr>
			   
			<tr><td align="center" colspan="2"><div id="orglimit"></div></td></tr>			    
			<tr><td><s:textfield label="Credit Limit" maxlength="10" name="creditLimit" id="Limit" value="0.0"/></td></tr>
			
			<tr><td align="center" colspan="2"><div id="Phone"></div></td></tr>    
		    <tr><td><s:textfield label="Phone Number"  maxlength="15" name="phone" id="phone"  /></td></tr>
		    
		    	
			<tr><td><s:select name="isOffLine" label="Off Line Mode" list="{'NO','YES'}" cssClass="option" /></td>
			</tr>
			<tr><td><s:textfield label="Terminal Id" maxlength="20"  name="terminalId" id="termId" /></td>					
				</tr>
     	  	
     	    
			<tr>
				<td style="width: 400px"></td>		       
				<td align="left" style="width: 390px">
		     	<table><s:submit value="Register"  align="right" cssClass="button"/></table>
				</td>
			</tr>
		
		<tr><th><div style="visibility: hidden;">
     	  	<table>
			<tr><th colspan="2" align="center">Retailer Limit Values</th></tr>
			<tr>
				<td><s:textfield name="verLimit" label="Verification Limit" cssStyle="visibility: hidden;" id="verLimit" readonly="true" value="%{#application.retVerLimit}" /></td>
			</tr>
			<tr>
				<td><s:textfield name="appLimit" label="Approval Limit" cssStyle="visibility: hidden;" id="appLimit" readonly="true" value="%{#application.retAppLimit}" /></td>
			</tr>
			<tr>
				<td><s:textfield name="payLimit" label="Payment Limit" cssStyle="visibility: hidden;" id="payLimit" readonly="true" value="%{#application.retPayLimit}" /></td>
			</tr>
			<tr>
				<td><s:textfield name="scrapLimit" label="Scrap Limit" cssStyle="visibility: hidden;" id="scrapLimit" readonly="true" value="%{#application.retScrapLimit}" /></td>
			</tr>
			<tr>
				<td><s:textfield name="olaDepositLimit" label="Ola Deposit Limit" cssStyle="visibility: hidden;" id="olaDepositLimit" readonly="true" value="%{#application.olaDepositLimit}" /></td>
			</tr>
			<tr>
				<td><s:textfield name="olaWithdrawalLimit" label="Ola Withdrawal Limit" cssStyle="visibility: hidden;" id="olaWithdrawalLimit" readonly="true" value="%{#application.olaWithdrawalLimit}" /></td>
			</tr>
			</table>	
     	    </div></th></tr>
     	    
		
		</table>
 </s:form>
</div></div>
</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_quickRetReg_Menu.jsp,v $
$Revision: 1.3 $
</code>