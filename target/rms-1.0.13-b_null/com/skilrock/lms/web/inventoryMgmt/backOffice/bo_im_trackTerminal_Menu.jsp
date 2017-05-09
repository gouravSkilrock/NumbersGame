
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			scrolling="no" type="text/css" />
		<style type="text/css">
.outerBorderLeft {
	border-left: 3px solid #000000;
}
</style>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/validate_nonconsInvUpload.js"></script>
		<script>
			  var mainDiv;
			  var req;
			  var which;
			
			  function retrieveURL(url,div) {
			  //alert("hi");
			  mainDiv=div;
			   _resp  = _ajaxCall(url);
				if(_resp.result){
			      _id.i(mainDiv,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name='label.select.game'/>:  " +_resp.data);
			      } else {
			        alert("Problem: ");
			        }
			  }
			  
			  function checkValidation(){ 
				  
				 	 var frmVal  = new parent.frames[0].Validator("trackInv");	 
					// validate the retailer organization  Entries		  
					frmVal.aV("invId","dontselect=0",'<s:text name="error.select.inv.name"/>',"inv_err");		  
					frmVal.aV("brandsId","dontselect=0",'<s:text name="error.select.brand.name"/>',"brand_err");	
					frmVal.aV("modelId","dontselect=0",'<s:text name="error.select.model.name"/>',"model_err");
							  
					var patt1 = /^\w*$/;	
					var invFlag = true;	
				  	if(document.getElementById("termNumberId").value.toString().trim()==""){
				  		document.getElementById("termNumberId1").innerHTML="<font color=\"red\"><s:text name='error.enter.any.val'/></font>";
				  		invFlag=false;
				  	}else if (!patt1.test(document.getElementById("termNumberId").value.toString().trim())){
				  		document.getElementById("termNumberId1").innerHTML="<font color=\"red\"><s:text name='error.invalid.serial.no'/>. !!</font>";
				  		invFlag=false;
				  	}
				 	return (invFlag && document.error_disp_handler.isValid);
			  }
			  
			  function clearDiv(id)
			  {
			  document.getElementById(id+"1").innerHTML="";
			  }
			  function isNumeric(id) {
				var strValidChars = "0123456789-";
				var strChar;
				
				var dashCount=0;
				var strString=	document.getElementById(id).value;
				if (strString.charAt(0)=="-") {
						return false;
				}
				if (strString.charAt(strString.length-1)=="-") {
						return false;
				}
			
			   //  test strString consists of valid characters listed above
				for (i = 0; i < strString.length ; i++) {
					strChar = strString.charAt(i);		
					if(strChar=="-"){dashCount++;}		
					if (strValidChars.indexOf(strChar) == -1) {
					
						document.getElementById(id+"1").innerHTML = "<font color = 'red'><s:text name='error.invalid.val'/></font>";			
						
						return false;
						
					}
				}
				
				if(dashCount>1){
				
						document.getElementById(id+"1").innerHTML = "<font color = 'red'><s:text name='error.invalid.val'/></font>";			
						return false;
				}
				
				
				return true;
			}
		</script>

		<s:head theme="ajax" debug="false" />
	</head>
	<body onload="resetValues();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">

				<h3>
					<s:text name="label.track.inv" />
				</h3>
				<s:form action="bo_im_track_terminal_Detail"
					onsubmit="return checkValidation()" name="trackInv">

					<s:select name="Spec" id="brand_list" theme="simple"
						cssStyle="display:none" list="%{brandMap}" />
					<s:select name="Spec" id="model_list" theme="simple"
						cssStyle="display:none" list="%{modelMap}" />
					<s:select name="Spec" id="bind_list" theme="simple"
						cssStyle="display:none" list="%{bindingLengthMap}" />

					<table>
						<tr>
							<td colspan="2">
								<div id="inv_err"></div>
							</td>
						</tr>
						<s:select name="invId" id="inv" key="label.invntry.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}" list="%{invMap}"
							cssClass="option"
							onchange="clearContent('inv', 'bottom'); clearContent('inv', 'bottom'); chageBrandList(this.value, 'brand', 'brand_list','-1'); _id.o(\'model').options.length=1;" />
						<tr>
							<td colspan="2">
								<div id="brand_err"></div>
							</td>
						</tr>
						<s:select name="brandsId" id="brand" key="label.brand.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
							cssClass="option"
							onchange="clearContent('brand', 'bottom'); chageBrandList(this.value, 'model', 'model_list','-1');" />
						<tr>
							<td colspan="2">
								<div id="model_err"></div>
							</td>
						</tr>
						<s:select name="modelId" id="model" key="label.model.name"
							headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}"
							cssClass="option"
							onchange="clearContent('model', 'bottom'); changeModelList(this.value, 'bind', 'bind_list','-1');" />

						<s:select name="bindId" id="bind" headerKey="-1"
							headerValue="%{getText('label.please.select')}" list="{}" cssClass="option"
							cssStyle="display:none" />
						<s:textfield name="bind_len" id="bindLen" value="" maxlength="09"
							cssStyle="display:none" />
						<tr>
							<td>
								<div id="termNumberId1"></div>
								<s:textfield name="termNumber" id="termNumberId"
									key="label.enter.sr"
									onfocus="clearDiv(this.id); clearContentText('termNumberId', 'bottom');"></s:textfield>
							</td>
						</tr>
						<tr>
							<s:submit key="btn.show" targets="bottom" cssClass="button"
								theme="ajax"></s:submit>
						</tr>
					</table>
				</s:form>

				<div id="bottom">

				</div>

			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_trackTerminal_Menu.jsp,v $ $Revision: 1.3 $
</code>