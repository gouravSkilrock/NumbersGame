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


<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/warehouseReg.js"></script>
<script>
 var mainDiv;
  var req;
  var which;


function fetchCityList() {
				var country = _id.o('country').value;
				var state = _id.o('plrState').value;
		 		_ajaxCall("getCity.action?country=" + country + "&state=" + state,'charactersCity');
		 if(document.getElementById('plrCity').length >1){
		  document.getElementById('plrCity').selectedIndex=1;
		 }
		 		
		 		 			 if(typeof $('#area').val()	 !=='undefined'){
		 		 fetchAreaList();
		 		 }
			}

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
	


<body onload="onLoadAjax('getListAjax.action?listType=country','country'),selectState(),fetchCity()">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.warehouse.reg"/></h3>


<s:form  name="na" action="bo_um_warehouseReg_Save" onsubmit="return formFilled();">

<s:actionerror />
         <s:fielderror />
 <table border="0" cellpadding="2" cellspacing="0" width="450">

   <tr><td colspan="2" align="center">
   <div id="err"></div>
   </td></tr>
   <tr><td>
 
   <s:textfield key="label.warehouse.name" maxlength="30" id="whName" name="whName" required="true"/>   
    </td></tr>
    
    <tr><td>
 
   <s:select list="#{'FIXED':'FIXED LOCATION', 'MOBILE':'MOBILE'}" id="warehouseType" name="warehouseType" key="label.warehouse.type" required="true"  cssClass="option" required="true" headerValue="--Please Select--"  headerKey="-1"></s:select>   
    </td></tr>
 
    
    <tr><td><s:select list="#session.boUsers" headerKey="-1" id="ownerId" name="ownerId" cssClass="option" required="true" headerValue="Please Select" key="Warehouse User" /></td></tr>
    
    <tr><td colspan="2" align="center">
   <div id="addr1"></div>
   </td></tr>
    
    
     <s:textfield key="label.add.line1" maxlength="30" name="addrLine1"/>
    <tr><td colspan="2" align="center">
   <div id="addr2"></div>
   </td></tr>
	 <s:textfield key="label.add.line2" maxlength="30" name="addrLine2"/>
	<tr><td colspan="2" align="center">
   <div id="City"></div>
   </td></tr>
     
     
     <tr><td colspan="2" align="center">
   <div id="supCountry"></div>
   </td></tr>
     
    <tr style="display: none;">
										<td>
											<s:select theme="simple" key="label.country" id="country"
												name="country" headerKey="-1"
												headerValue="%{getText('label.please.select')}" list="{}"
												onchange="_ajaxCall('getState.action?country=' + this.value,'characters')"
												cssClass="option" required="true" />
										</td>
									</tr>
  
     	<tr>
										<td align="center" colspan="2">
											<div id="orgstate"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												<s:text name="label.state" />
												<span class="required">*</span>:
											</label>
										</td>
										<td align="left">
											<span id="characters"> <select class="option"
													id="state" name="state" onchange="return fetchCityList()">
													<option value="-1">
														<s:text name="label.please.select" />
													</option>
												</select> </span>
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="orgcity"></div>
										</td>
									</tr>
									<tr>
										<td align="right">
											<label class="label">
												<s:text name="label.city" />
												<span class="required">*</span>:
											</label>
										</td>
										<td align="left">
											<span id="charactersCity"> <select class="option"
													name="city" id="city" onchange="return fetchAreaList()">
													<option value="-1">
														<s:text name="label.please.select" />
													</option>
												</select> </span>
										</td>
									</tr>
  
  
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
$RCSfile: bo_im_warehouseReg_Menu.jsp,v $
$Revision: 1.3 $
</code>