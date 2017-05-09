<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/userMgmt/backOffice/js/supReg.js"></script>
<script>
 var mainDiv;
  var req;
  var which;


  function retrieveURL(url,div) {
  mainDiv=div;
    _resp  = _ajaxCall(url);
	if(_resp.result){
      _id.i(mainDiv,_resp.data);
      } else {
        alert('<s:text name="error.problem"/>');
        }
   }

function hi(){
alert("hi");
alert(document.na.text1.value);
}





</script>
<s:head theme="ajax" debug="false"/>
	</head>
	


<body onload="onLoadAjax('getListAjax.action?listType=country','country')">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.supp.reg"/></h3>


<s:form  name="na" action="bo_um_tktSupplierReg_Save" onsubmit="return isST3SupValidation()" >

<s:actionerror />
         <s:fielderror />
 <table border="0" cellpadding="2" cellspacing="0" width="450">
   <tr><td colspan="2" align="center">
   <div id="sup"></div>
   </td></tr>
   <tr><td>
 
   <s:textfield key="label.supplier.name"  required="true" maxlength="30" name="supName"/>   
    </td></tr>
    
    <tr><td colspan="2" align="center">
   <div id="addr1"></div>
   </td></tr>
    
    
     <s:textfield key="label.add.line1"  required="true" maxlength="30" name="addrLine1"/>
    <tr><td colspan="2" align="center">
   <div id="addr2"></div>
   </td></tr>
	 <s:textfield key="label.add.line2" maxlength="30" name="addrLine2"/>
	<tr><td colspan="2" align="center">
   <div id="City"></div>
   </td></tr>
     <s:textfield key="label.city"  required="true" maxlength="15" name="city"/>
     
     <tr><td colspan="2" align="center">
   <div id="supCountry"></div>
   </td></tr>
     
    <tr><td colspan="2" align="center">
     <div id = "countryName"></div>
     <s:select key="label.country" id="country" name="country" headerKey="-1" headerValue="%{getText('label.please.select')}" value="-1" list="{}" onchange="retrieveURL('getState.action?country=' + this.value,'characters')" required="true" cssClass="option"/>
     </td></tr>
  
     <tr><td colspan="2" align="center">
   <div id="supState"></div>
   </td></tr>
  
   
      <tr><td>
       <div id = "stateName"></div>
          </td></tr>
      <tr><td align="right" class="label"><s:text name="label.state"/><span class="required">*</span>:</td><td><span id="characters"><select class="option" ><option  value="--Please Select--"><s:text name="label.please.select"/></option></select> </span></td></tr>
   
    <tr><td colspan="2" align="center">
   <div id="Pin"></div>
   </td></tr>
   <tr><td>
      <s:textfield key="label.pin.code"  required="true" maxLength="10" name="pin" id="pin1"  /> 
      </td></tr>
   <tr>
   <td>
   </td>
	   <td>
	   <table>       
	        <s:submit key="btn.reg" align="center" cssClass="button" />
	  </table>
	   </td>
	   
   </tr>
   </table>  
   
 </s:form>
</div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_tktSupplierReg_Menu.jsp,v $
$Revision: 1.3 $
</code>