var i18nObj = parent.frames[0].i18Obj;
function fetchOptions(){
	_ajaxCall('bo_cs_fetchProdCat.action','catList'); //fetch product categories
	_ajaxCall('bo_cs_fetchOperators.action','optList'); //fetch operators
	_ajaxCall('bo_cs_fetchCircles.action','cirList'); //fetch circles
}

function isFormValid(){
	var frmVal = new parent.frames[0].Validator("productReg");

	 frmVal.aV("catId","dontselect=0",i18nObj.prop('error.category.empty'),"catIdErr");
	
	 //frmVal.aV("productNumber,"regexp=[^0-9]","Please Enter Valid Product Number","prodNumErr");
	 
	 frmVal.aV("productCode","req",i18nObj.prop('error.product.code.empty'),"prodCodeErr");
	 frmVal.aV("productCode","regexp=[^A-Za-z0-9-]",i18nObj.prop('error.product.code.invalid'),"prodCodeErr");
	 
	 frmVal.aV("operatorCode","dontselect=0",i18nObj.prop('error.oper.empty'),"operatorCodeErr");
	 
	 frmVal.aV("circleCode","dontselect=0",i18nObj.prop('error.circle.empty'),"circleCodeErr");
	 
	 frmVal.aV("denomination","req",i18nObj.prop('error.denomntn.empty'),"denoErr");
	 frmVal.aV("denomination","decimal",i18nObj.prop('error.denomtn.invalid'),"denoErr");
	 
	 frmVal.aV("talktime","req",i18nObj.prop('error.talktime.empty'),"talktimeErr");
	 frmVal.aV("talktime","regexp=[^0-9\\s_.]",i18nObj.prop('error.talktime.invalid'),"talktimeErr");
	 
	 frmVal.aV("validity","req",i18nObj.prop('error.validity.empty'),"validityErr");
	 frmVal.aV("validity","regexp=[^A-Za-z0-9\\s_.]",i18nObj.prop('error.pls.entr.valid.validity'),"validityErr");
	 
	 frmVal.aV("adminFee","req",i18nObj.prop('error.admin.fee.empty'),"adminFeeErr");
	 frmVal.aV("adminFee","regexp=[^0-9\\s_.]",i18nObj.prop('error.admin.fee.invalid'),"adminFeeErr");
	 
	 frmVal.aV("serviceTax","req",i18nObj.prop('error.srvctax.empty'),"serviceTaxErr");
	 frmVal.aV("serviceTax","regexp=[^0-9\\s_.]",i18nObj.prop('error.srvctax.invalid'),"serviceTaxErr");
	 
	 frmVal.aV("rechargeInst","req",i18nObj.prop('error.recharge.instr.empty'),"rechargeInstErr");
	 frmVal.aV("rechargeInst","regexp=[^A-Za-z0-9\*!@#\$%\^\+&\(\)<>\?\.\\s_.]{1,30}",i18nObj.prop('error.pls.entr.valid.recharge.instruction'),"rechargeInstErr");
	 
	 frmVal.aV("productDesc","req",i18nObj.prop('error.product.disc.empty'),"prodDescErr");
	 frmVal.aV("productDesc","regexp=[^A-Za-z0-9\\s_.]",i18nObj.prop('error.pls.entr.valid.product.desc'),"prodDescErr");
	 
	 frmVal.aV("supplierName","req",i18nObj.prop('error.suuplier.name.empty'),"supplierNameErr");
	 frmVal.aV("supplierName","regexp=[^A-Za-z\\s_.]",i18nObj.prop('error.supplier.name.invalid'),"supplierNameErr");
	 
	 frmVal.aV("unitPrice","req",i18nObj.prop('error.unit.price.empty'),"unitPriceErr");
	 frmVal.aV("unitPrice","decimal","Please Enter Valid unitPrice","unitPriceErr");
	 
	 frmVal.aV("retailerComm","req",i18nObj.prop('error.retailer.comm.empty'),"retailerCommErr");
	 frmVal.aV("retailerComm","decimal",i18nObj.prop('error.retailer.comm.invalid'),"retailerCommErr");
	 
	 frmVal.aV("agentComm","req",i18nObj.prop('error.agnt.comm.empty'),"agentCommErr");
	 frmVal.aV("agentComm","decimal",i18nObj.prop('error.agnt.comm.invalid'),"agentCommErr");
	 
	 frmVal.aV("jvComm","req",i18nObj.prop('error.jv.comm.empty'),"jvCommErr");
	 frmVal.aV("jvComm","decimal",i18nObj.prop('error.jv.comm.invalid'),"jvCommErr");
	 
	 frmVal.aV("goodCauseAmt","req",i18nObj.prop('error.goodcause.empty'),"goodCauseAmtErr");
	 frmVal.aV("goodCauseAmt","decimal",i18nObj.prop('error.goodcause.invalid'),"goodCauseAmtErr");
	 
	 frmVal.aV("vatAmt","req",i18nObj.prop('error.vat.empty'),"vatAmtErr");
	 frmVal.aV("vatAmt","decimal",i18nObj.prop('error.vat.invalid'),"vatAmtErr");
	 

	 //alert(document.error_disp_handler.isValid);
	  
	 return document.error_disp_handler.isValid;
	
}

function flexiClick(){
	var obj = document.getElementById('flexiId'); 
	if(obj.checked == true){
		document.getElementById('deno').value='0.0';
		document.getElementById('talktime').value='0.0';
		document.getElementById('unitPrice').value='0.0';
		obj.value = true;
		document.getElementById('deno').disabled = true;
		document.getElementById('talktime').disabled = true;
		document.getElementById('unitPrice').disabled = true;
	}else{
		document.getElementById('deno').disabled = false;
		document.getElementById('talktime').disabled = false;
		document.getElementById('unitPrice').disabled = false;
		document.getElementById('deno').focus();
		obj.value = false;
	}
}

parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/commercialService/productMgmt/backOffice/js/prodReg.js,v $"] = "$Id: prodReg.js,v 1.3 2016/10/31 09:47:03 neeraj Exp $";