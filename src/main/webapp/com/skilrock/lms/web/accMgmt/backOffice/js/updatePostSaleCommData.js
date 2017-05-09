var i18nObj = parent.frames[0].i18Obj;
var tempDataArr = new Array();

function changeTrainType(){
	//alert('change game Name');
	document.getElementById("trainingType").selectedIndex = 0;
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
}

function updateData(row) {
	
	var rowId = _id.o(row);
	var agtId = _id.n("agtId")[row].value;
	var trVar = _id.n("trVar")[row].value;
	var trType = _id.o("trainingType").value;
	if(trVar < -5 ||trVar > 95 || isNaN(trVar)){
		alert(i18nObj.prop('error.enter.correct.data'));
		return;
	}
	//alert(_id.o('row'+(parseFloat(row)+1)).style.background);
	if(_id.o('row'+(parseFloat(row)+1)).style.background =="rgb(248, 168, 88)" ||_id.o('row'+(parseFloat(row)+1)).style.background =="#f8a858" ){
		heightDiv();
		_ajaxUnsync(path + "/com/skilrock/lms/web/accMgmt/bo_act_updateAgentTrainnigExp_Update.action?trainingType=" + trType + "&agtId=" + agtId + "&trVar=" + trVar, "afterAjax");
	}
	else{
		alert(i18nObj.prop('error.update.data'));
		return;
	}
	//alert(agtId + " " + trVar + "***" + trType);
	}
function afterAjax() {
	_id.o("mainDiv").innerHTML="";
	_id.i("mainDiv", asyncResult);
	_id.blk("mainDiv");
	//_id.o("mainDiv").style.display = "none";
	fillTempArr();
}
function heightDiv() {
	_id.o("completeDiv").style.height = document.body.clientHeight - 160 + "px";
	_id.o("completeDiv").style.display = "block";
}
function fetchActData(agtId) {
	//alert(_id.o("gameId").value);
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
	var _resp = _ajaxUnsync("bo_act_updateAgentTrainnigExp_View.action?trainingType=" + agtId+"&gameNo="+_id.o("gameId").value);
	//_id.i("resultDiv", _resp.data);
	//_id.blk("resultDiv");
	//filterData();
}
function selAll(val) {
	var chkBx = _id.n("tempStatus");
	for (var i = 0; i < chkBx.length; i++) {
		chkBx[i].checked = val;
	}
}
function updateSelected() {
	var defTar=_id.o('defDepositPer').value;
	//alert(defTar);
	var chkBx = _id.n("tempStatus");
	var agtId = _id.n("agtId");
	var depositVar = _id.n("depositVar");
	//var trType = _id.o("trainingType").value;
	//var updData = "&trainingType=" + trType;
	var updData ="";
	var isSelected = false;
	var isUpdate = false;
	var isInvalid=false;
	for (var i = 0; i < chkBx.length; i++) {
		if (chkBx[i].checked == true) {
			isSelected = true;
			var row = (chkBx[i].id).replace("chkBox", "");
			//alert(_id.o('row'+row).style.backgroundColor);
				if(_id.o('row'+row).style.background != 'white' ||_id.o('row'+row).style.background =="#f8a858" ){
				isUpdate = true;                       
				if(_id.o('depositVar'+row).value < -defTar ||_id.o('depositVar'+row).value >(100-defTar) || isNaN(_id.o('depositVar'+row).value)){
					//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
					_id.o('msg'+row).innerHTML=i18nObj.prop('error.enter.correct.data');
					isInvalid = true;
					continue;
				}	
			}
			var tempAgtId = agtId[row - 1].value;
			var tempTrVar = depositVar[row - 1].value;
			updData = updData + "&orgId=" + tempAgtId + "&depositVar=" + tempTrVar;
		}
	}
	if (updData.length < 1 || !(isUpdate && isSelected)) {
		alert(i18nObj.prop('error.select.atleast.one.val'));
		return;
	}
	if(isInvalid){
		return;
	}
	//heightDiv();
	//alert(updData+"&gameNo="+_id.o("gameId").value);
	_ajaxUnsync(path + "/com/skilrock/lms/web/accMgmt/bo_act_updatePostSaleVar_Update.action?","afterAjax", updData);
}

function updateRetailerSelected() {
	var defTar=_id.o('defDepositPer').value;
	//alert(defTar);
	var chkBx = _id.n("tempStatus");
	var agtId = _id.n("agtId");
	var depositVar = _id.n("depositVar");
	//var trType = _id.o("trainingType").value;
	//var updData = "&trainingType=" + trType;
	var updData ="";
	var isSelected = false;
	var isUpdate = false;
	var isInvalid=false;
	for (var i = 0; i < chkBx.length; i++) {
		if (chkBx[i].checked == true) {
			isSelected = true;
			var row = (chkBx[i].id).replace("chkBox", "");
			//alert(_id.o('row'+row).style.backgroundColor);
				if(_id.o('row'+row).style.background != 'white' ||_id.o('row'+row).style.background =="#f8a858" ){
				isUpdate = true;                       
				if(_id.o('depositVar'+row).value < -defTar ||_id.o('depositVar'+row).value >(100-defTar) || isNaN(_id.o('depositVar'+row).value)){
					//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
					_id.o('msg'+row).innerHTML=i18nObj.prop('error.enter.correct.data');
					isInvalid = true;
					continue;
				}	
			}
			var tempAgtId = agtId[row - 1].value;
			var tempTrVar = depositVar[row - 1].value;
			updData = updData + "&orgId=" + tempAgtId + "&depositVar=" + tempTrVar;
		}
	}
	if (updData.length < 1 || !(isUpdate && isSelected)) {
		alert(i18nObj.prop('error.select.atleast.one.val'));
		return;
	}
	if(isInvalid){
		return;
	}
	//heightDiv();
	//alert(updData+"&gameNo="+_id.o("gameId").value);
	_ajaxUnsync(path + "/com/skilrock/lms/web/accMgmt/agt_act_updatePostSaleVar_Update.action?","afterAjax", updData);
} 

function updRowColor(rowId) {
	var row = _id.o("row" + rowId);
	var trainingValue = _id.n("depositVar")[rowId - 1].value;
	if (trainingValue == "") {
		_id.n("depositVar")[rowId - 1].value = 0;
	}
	if (parseFloat(tempDataArr[rowId], 10) == parseFloat(trainingValue, 10)) {
		row.style.background = "white";
	} else {
		var chkBx = _id.o("chkBox" + rowId);
		//chkBx.checked = true;
		row.style.background = "#F8A858";
		//alert("hello" + row.style.background);
	}
}
function fillTempArr() {
	var trainingValue = _id.n("trVar");
	for (var i = 1, len = trainingValue.length; i <= len; i++) {
		tempDataArr[i] = trainingValue[i - 1].value;
	}
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/updatePostSaleCommData.js,v $"] = "$Id: updatePostSaleCommData.js,v 1.3 2016/10/31 09:46:21 neeraj Exp $";

