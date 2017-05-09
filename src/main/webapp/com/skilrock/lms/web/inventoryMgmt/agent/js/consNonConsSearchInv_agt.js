
function  _validateForm(){
	_id.i('downResult', "");_id.i('lastDown', "");
	var txtVal = _id.v('count_text');
	if(isNaN(txtVal)){
		_id.i('err_div', "Please Insert Valid Numeric Number For Count", "e");
		return false;
	}
	return true;
}


function changeOwnerType(obj) {
	_id.i('downResult', "");_id.i('lastDown', "");
	var value = obj.value;
	if(value == "RETAILER") {
		//_id.blk('agt_label');
		//_id.blk('agt_org_list');		
		//fillSelBox("-1", "ret_org_list", "ret_list");
		getRetailerUserList();
		//_id.blk('ret_org_list');		
		//_id.blk('ret_label');		
		
	}else {		
		//_id.non('agt_label');
		//_id.non('agt_org_list');
		//_id.non('ret_label');		
		_id.non('retlistDiv');			
	}
}

function invTypeShow(radVal) {
	_id.i('downResult', "");_id.i('lastDown', "");
	if("NON_CONS" == radVal) {
		fillSelBox("-1", "inv_id", "nonCons_list", 'flag');
		_id.blk('brand'); _id.blk('brand_label');
		_id.blk('model_label'); _id.blk('model'); 
		_id.o('brand').options.length=1;
		_id.o('model').options.length=1;
	}else {
		fillSelBox("-1", "inv_id", "cons_list", 'flag');
		_id.non('brand'); _id.non('brand_label');
		_id.blk('model_label'); _id.blk('model');
		_id.o('model').options.length=1;
	}
}


function fillBrandNModel(invVal){
	_id.i('downResult', "");_id.i('lastDown', "");	
	var radArr=_id.n('invType');
	for(var i=0, l=radArr.length;i<l;i+=1){
		if(radArr[i].checked){
			if(radArr[i].value=="NON_CONS") {							
				fillSelBox(invVal, "brand", "brand_list") ;
				_id.o('model').options.length=1;
			}else {				
				fillSelBox(invVal, "model", "model_spec_list") ;
			}
		}
	}	
}


function fillSelBox(invValue, updOptId, fetOptId, flag) {	
	//alert(flag+" == "+invValue);
	var valArr = invValue.split("-");	
	invValue = valArr[0];
	//alert(invValue);
	
	var brandObj = _id.o(updOptId);	
	brandObj.options.length=1;	
	var selObj = _id.o(fetOptId);
	var text, value, count=1;
	var brandValueArr;
	for(var i=0,l= selObj.length; i<l; i=i+1) {
		value = selObj.options[i].value;
		brandValueArr = value.split("-");
		text = selObj.options[i].text;
		//alert(text+" = "+ value);
		if(brandValueArr[1] == invValue || (invValue == "" && flag)) {			
			var opt = new Option(text, brandValueArr[0]);
			brandObj.options[count] = opt;
			count = count +1;
			//alert(text+" = "+ value);
		}
		
	}	
	//alert(text+" = "+ value);
}
function getRetailerUserList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAllOrgNameList.action?orgType=RETAILER");
	var firstArr = text.data.split(":");
	var retObj = _id.o("ret_org_list");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
	document.getElementById("retlistDiv").style.display="block";	
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/agent/js/consNonConsSearchInv_agt.js,v $'] = '$Id: consNonConsSearchInv_agt.js,v 1.3 2016/10/31 09:47:02 neeraj Exp $';