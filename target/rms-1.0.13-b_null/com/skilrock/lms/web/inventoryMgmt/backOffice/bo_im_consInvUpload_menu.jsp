<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			function validate() {
				var frmVal  = new parent.frames[0].Validator("saveInv");
				  // validate the retailer organization  Entries		  
				  frmVal.aV("invId","dontselect=0",'<s:text name="error.select.inv.name"/>',"inv_err");		  
				  frmVal.aV("modelId","dontselect=0",'<s:text name="error.selectInvSpecName"/>',"inv_spec_err");		  
				  frmVal.aV("quantity","req",'<s:text name="error.enter.total.qty"/>',"qty_err");
				  frmVal.aV("quantity","numeric",'<s:text name="error.enter.correct.total.qty"/>',"qty_err");
				  frmVal.aV("quantity","non_zero",'<s:text name="error.total.qty.mustbe.gt.zero"/>',"qty_err");		  
				// alert(document.error_disp_handler.isValid);
				return document.error_disp_handler.isValid;
			}
			
			function chageBrandList(invValue, updOptId, fetOptId) {			
				var brandObj = _id.o(updOptId);	
				brandObj.options.length=1;	
				var selObj = _id.o(fetOptId);
				var text, value, count=1;
				var brandValueArr;
				for(var i=0,l= selObj.length; i<l; i=i+1) {
					value = selObj.options[i].value;
					brandValueArr = value.split("-");
					text = selObj.options[i].text;				
					if(brandValueArr[1] == invValue || invValue == -1) {			
						var opt = new Option(text, value);
						brandObj.options[count] = opt;
						count = count +1;					
					}				
				}	
			}	
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.upload.cons.inv" />
				</h3>


				<s:form name="saveInv" action="bo_im_consInvUpload_save"
					onsubmit="return validate()">

					<s:select name="Spec" id="inv_spec_list" theme="simple"
						cssStyle="display:none" list="#session.CONS_INV_SPEC_NAME_LIST" />

					<table border="0" cellpadding="2" cellspacing="0" width="400px">
						<tr>
							<td colspan="2">
								<div id="inv_err"></div>
							</td>
						</tr>
						<s:select name="invId" key="label.invntry.name" headerKey="-1"
							headerValue="--Please Select--"
							list="#session.CONS_INV_NAME_LIST" cssClass="option"
							onchange="chageBrandList(this.value, 'model', 'inv_spec_list')" />
						<tr>
							<td colspan="2">
								<div id="inv_spec_err"></div>
							</td>
						</tr>
						<s:select name="modelId" id="model" key="label.inv.spcfctn"
							headerKey="-1" headerValue="--Please Select--" list="{}"
							cssClass="option" />
						<tr>
							<td colspan="2">
								<div id="qty_err"></div>
							</td>
						</tr>
						<s:textfield name="quantity" key="label.total.quality"
							maxlength="10" id="quantity" />
						<s:submit key="btn.submit" align="center" cssClass="button" />
					</table>
				</s:form>

				<div id="down" style="text-align: center"></div>
			</div>
		</div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_consInvUpload_menu.jsp,v $ $Revision: 1.3 $
</code>