<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/validate_nonconsInvUpload.js"></script>
</head>
	
 	
 	
<body onload="createTbl();chageBrandList(document.saveInv.invId.value, 'brand', 'brand_list','<s:property value="%{brandsId}" />');chageBrandList(document.saveInv.brandsId.value, 'model', 'model_list','<s:property value="%{modelId}" />'); changeModelList(document.saveInv.bindId.value, 'bind', 'bind_list','<s:property value="%{bindlId}" />');resetValues();">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3>Upload Non Consumable Inventory</h3>

  <s:form name="saveInv" action="admin_im_nonConsInvUpload_save" onsubmit="return _nonConsValidate()" method="post" enctype="multipart/form-data"> 	
  	<s:actionerror/>

	<s:select name="Spec" id="brand_list" theme="simple" cssStyle="display:none" list="%{brandMap}" />
	  	<s:select name="Spec" id="model_list" theme="simple" cssStyle="display:none" list="%{modelMap}" />
	  		<s:select name="Spec" id="bind_list" theme="simple" cssStyle="display:none" list="%{bindingLengthMap}" />
	  		  	<s:select name="Spec" id="invCode_list" theme="simple" cssStyle="display:none" list="%{invCodeMap}" />
	 
	 <table  border="0" cellpadding="2" cellspacing="0" width="360px" style="text-align: left">
		<tr><td colspan="2"><div id="inv_err"></div></td></tr>
		<s:select name="invId" id="inv" label="Inventory Name" headerKey="-1" headerValue="--Please Select--" list="%{invMap}" cssClass="option" onchange="chageBrandList(this.value, 'brand', 'brand_list','-1'); _id.o('model').options.length=1; _id.o('bind').options.length=1;"/>		
		<tr><td colspan="2"><div id="brand_err"></div></td></tr>
		<s:select name="brandsId" id="brand" label="Brand Name" headerKey="-1" headerValue="--Please Select--" list="{}" cssClass="option" onchange="chageBrandList(this.value, 'model', 'model_list','-1');" />		
		<tr><td colspan="2"><div id="model_err"></div></td></tr>
		<s:select name="modelId" id="model" label="Model Name" headerKey="-1" headerValue="--Please Select--" list="{}" cssClass="option" onchange="changeModelList(this.value, 'bind', 'bind_list','-1');"/>
		<s:select name="bindId" id="bind" headerKey="-1" headerValue="--Please Select--" list="{}" cssClass="option" cssStyle="display:none" />
		<tr><td colspan="2"><div id="cost_err"></div></td></tr>				
		<s:textfield name="cost" label="Cost Per Unit"  maxlength="09" id="costPerUnit" value=""/>
		<s:textfield name="bind_len" id ="bindLen" value=""  maxlength="09"  cssStyle="display:none"/>
		<s:radio name="isNew" label="Is New Inv" value="'Y'" list="#{'Y':'YES', 'N':'NO'}" />
	</table>
		
		<br/>
		<table  border="0" cellpadding="2" cellspacing="0" >
		<tr><td colspan="2"><div id="sr_err"></div></td></tr>
		<tr><td colspan="2">
			<div id="dyn_tbl"></div>
		</td></tr>
		</table>
		
	<br/>	
	<table  border="0" cellpadding="5" cellspacing="0" width="360px">	
		<s:file name="serNoFileName" id="serNoFile" value="" label="Upload Serial No. File"/>
		<s:submit value="submit" align="right" cssClass="button"/>		
	</table>
	</s:form>
	
	<div id="down" style="text-align: center" ></div>
	 </div></div>
		
	

 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: admin_im_nonconsInvUpload_menu.jsp,v $
$Revision: 1.3 $
</code>