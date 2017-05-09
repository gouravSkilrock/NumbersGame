
var tempDataArr = new Array();
var extraTempDataArr=new Array();
var incentiveTempDataArr = new Array();
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
		alert('Please Enter correct data');
		return;
	}
	//alert(_id.o('row'+(parseFloat(row)+1)).style.background);
	if(_id.o('row'+(parseFloat(row)+1)).style.background =="rgb(248, 168, 88)" ||_id.o('row'+(parseFloat(row)+1)).style.background =="#f8a858" ){
		heightDiv();
		_ajaxUnsync(path + "/com/skilrock/lms/web/accMgmt/bo_act_updateAgentTrainnigExp_Update.action?trainingType=" + trType + "&agtId=" + agtId + "&trVar=" + trVar, "afterAjax");
	}else{
		alert('Please update data');
		return;
	}
	//alert(agtId + " " + trVar + "***" + trType);
	}
function afterAjax() {
	_id.i("resultDiv", asyncResult);
	_id.blk("resultDiv");
	_id.o("completeDiv").style.display = "none";
	fillTempArr();
}
function heightDiv() {
	_id.o("completeDiv").style.height = document.body.clientHeight - 160 + "px";
	_id.o("completeDiv").style.display = "block";
}
/*function fetchActData(agtId) {
	//alert(_id.o("gameId").value);
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
	var _resp = _ajaxUnsync("bo_act_updateAgentTrainnigExp_View.action?trainingType=" + agtId+"&gameNo="+_id.o("gameId").value, "afterAjax");
	//_id.i("resultDiv", _resp.data);
	//_id.blk("resultDiv");
	//filterData();
}*/

function fetchActData() {
	//alert(_id.o("gameId").value);
	_id.o("resultDiv").innerHTML = "";
	_id.o("reportDiv").innerHTML = "";
	var _resp = _ajaxUnsync("bo_act_updateAgentTrainnigExp_View.action?trainingType=" + document.getElementById("trainingType").value + "&gameNo=" + document.getElementById("gameId").value + "&serviceId=" + document.getElementById("serviceId").value, "afterAjax");
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
	var defTar=_id.o('defTranPer').value;
	//alert(defTar);
	var chkBx = _id.n("tempStatus");
	var agtId = _id.n("agtId");
	var trVar = _id.n("trVar");
	var trType = _id.o("trainingType").value;
	var updData = "&trainingType=" + trType;
	var isSelected = false;
	var isUpdate = false;
	var isInvalid=false;
	if(_id.o('defIncentivePer') != undefined){
		var defIncentivePer=_id.o('defIncentivePer').value;
		var incentivePerVar = _id.n("incentiveVar");	
	}
	var defExtraTar = _id.o('defExtraTranPer').value;
	var extraTrVar = _id.n("extraTrVar");
	
	
	
	for (var i = 0; i < chkBx.length; i++) {
		if (chkBx[i].checked == true) {
			isSelected = true;
			var row = (chkBx[i].id).replace("chkBox", "");
			//alert(_id.o('row'+row).style.background+"==_id.o('row'+row).style.background=="+_id.o('row'+row).style.background.match(/rgb\(248, 168, 88\)/g));
				if(_id.o('row'+row).style.background.match(/rgb\(248, 168, 88\)/g) ||_id.o('row'+row).style.background =="#f8a858" ){
				isUpdate = true;
				if(_id.o('trVar'+row).value < -defTar ||_id.o('trVar'+row).value >(100-defTar) || isNaN(_id.o('trVar'+row).value)){
					//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
					_id.o('msg'+row).innerHTML="Please enter correct data";
					isInvalid = true;
					continue;
				}
				if(_id.o('extraTrVar'+row).value < -defExtraTar ||_id.o('extraTrVar'+row).value >(100-defExtraTar) || isNaN(_id.o('extraTrVar'+row).value)){
					//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
					_id.o('msg'+row).innerHTML="Please enter correct data";
					isInvalid = true;
					continue;
				}
				if(_id.o('defIncentivePer') != undefined){
					if(_id.o('incentiveVar'+row).value < -defIncentivePer ||_id.o('incentiveVar'+row).value >(100-defIncentivePer) || isNaN(_id.o('incentiveVar'+row).value)){
						//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
						_id.o('msg'+row).innerHTML="Please enter correct data";
						isInvalid = true;
						continue;
					}
				}
			}
			var tempAgtId = agtId[row - 1].value;
			var tempTrVar = trVar[row - 1].value;
			var extraTempTrVar = extraTrVar[row - 1].value;
			if(_id.o('defIncentivePer') != undefined){
				var incentiveTempVar = incentivePerVar[row - 1].value;
			}
			updData = updData + "&agtId=" + tempAgtId + "&trVar=" + tempTrVar +"&extraTrVar=" + extraTempTrVar;
			if(_id.o('defIncentivePer') != undefined){
				updData = updData +"&incentiveVar=" + incentiveTempVar;
			}
		}
	}

	/*if(trType == 'DAILY'){
		var defExtraTar=_id.o('defExtraTranPer').value;
		var extraTrVar = _id.n("extraTrVar");
		
		for (var i = 0; i < chkBx.length; i++) {
			if (chkBx[i].checked == true) {
				isSelected = true;
				var row = (chkBx[i].id).replace("chkBox", "");
				//alert(_id.o('row'+row).style.background+"==_id.o('row'+row).style.background=="+_id.o('row'+row).style.background.match(/rgb\(248, 168, 88\)/g));
					if(_id.o('row'+row).style.background.match(/rgb\(248, 168, 88\)/g) ||_id.o('row'+row).style.background =="#f8a858" ){
					isUpdate = true;
					if(_id.o('trVar'+row).value < -defTar ||_id.o('trVar'+row).value >(100-defTar) || isNaN(_id.o('trVar'+row).value)){
						//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
						_id.o('msg'+row).innerHTML="Please enter correct data";
						isInvalid = true;
						continue;
					}
					if(_id.o('extraTrVar'+row).value < -defExtraTar ||_id.o('extraTrVar'+row).value >(100-defExtraTar) || isNaN(_id.o('extraTrVar'+row).value)){
						//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
						_id.o('msg'+row).innerHTML="Please enter correct data";
						isInvalid = true;
						continue;
					}
				}
				var tempAgtId = agtId[row - 1].value;
				var tempTrVar = trVar[row - 1].value;
				var extraTempTrVar = extraTrVar[row - 1].value;
				updData = updData + "&agtId=" + tempAgtId + "&trVar=" + tempTrVar +"&extraTrVar=" + extraTempTrVar;
			}
		}
	}else{
			for (var i = 0; i < chkBx.length; i++) {
				if (chkBx[i].checked == true) {
					isSelected = true;
					var row = (chkBx[i].id).replace("chkBox", "");
					
						if(_id.o('row'+row).style.background.match(/rgb\(248, 168, 88\)/g) ||_id.o('row'+row).style.background =="#f8a858" ){
						isUpdate = true;
						if(_id.o('trVar'+row).value < -defTar ||_id.o('trVar'+row).value >(100-defTar) || isNaN(_id.o('trVar'+row).value)){
							//alert('invalid data : '+_id.o('trVar'+row).value+'**'+defTar+'***'+(100-defTar));
							_id.o('msg'+row).innerHTML="Please enter correct data";
							isInvalid = true;
							continue;
						}	
					}
					var tempAgtId = agtId[row - 1].value;
					var tempTrVar = trVar[row - 1].value;
					updData = updData + "&agtId=" + tempAgtId + "&trVar=" + tempTrVar;
				}
			}
		}*/
	if (updData.length < 1 || !(isUpdate && isSelected)) {
		alert('Please update atleast one value');
		return;
	}
	if(isInvalid){
		return;
	}
	heightDiv();
//	alert(updData + "&gameNo=" + document.getElementById("gameId") + "&serviceId=" + document.getElementById("serviceId").value);
	_ajaxUnsync(path + "/com/skilrock/lms/web/accMgmt/bo_act_updateAgentTrainnigExp_Update.action?", "afterAjax", updData+"&gameNo="+document.getElementById("gameId").value +"&serviceId="+document.getElementById("serviceId").value);
}

function updRowColor(rowId) {
	//alert("--rowId--"+rowId);
	var row = _id.o("row" + rowId);
	var trainingValue = _id.n("trVar")[rowId - 1].value;
	//alert("--trainingValue--"+trainingValue);
	
	if (trainingValue == "") {
		_id.n("trVar")[rowId - 1].value = 0;
	}
	if (parseFloat(tempDataArr[rowId], 10) == parseFloat(trainingValue, 10)) {
		row.style.background = "white";
	} else {
		var chkBx = _id.o("chkBox" + rowId);
		//chkBx.checked = true;
		row.style.background = "#F8A858";
		//alert("hello" + row.style.background);
	}
	
	var trType = _id.o("trainingType").value;
	//alert('--trType--'+trType);
	//if(trType == 'DAILY'){
		var extraTrainingValue = _id.n("extraTrVar")[rowId - 1].value;
		if (extraTrainingValue == "") {
			_id.n("extraTrVar")[rowId - 1].value = 0;
		}
		if (parseFloat(extraTempDataArr[rowId], 10) != parseFloat(extraTrainingValue, 10)) {
			row.style.background = "#F8A858";
			//alert("hello" + row.style.background);
		}
		if(_id.o('defIncentivePer') != undefined){
			var incentiveTrainingValue = _id.n("incentiveVar")[rowId - 1].value;
			if (incentiveTrainingValue == "") {
				_id.n("incentiveVar")[rowId - 1].value = 0;
			}
			if (parseFloat(incentiveTempDataArr[rowId], 10) != parseFloat(incentiveTrainingValue, 10)) {
				row.style.background = "#F8A858";
				//alert("hello" + row.style.background);
			}
		}
	//}
}
function fillTempArr() {
	
	var trType = _id.o("trainingType").value;
	//alert('--trType--'+trType);
	//if(trType == 'DAILY'){
	var extraTrainingValue = _id.n("extraTrVar");
	for (var i = 1, len = extraTrainingValue.length; i <= len; i++) {
		extraTempDataArr[i] = extraTrainingValue[i - 1].value;
	}
	//}
	
	var trainingValue = _id.n("trVar");
	for (var i = 1, len = trainingValue.length; i <= len; i++) {
		tempDataArr[i] = trainingValue[i - 1].value;
	}
	
	if(_id.o('defIncentivePer') != undefined){
		var incentiveTrainingValue = _id.n("incentiveVar");
		for (var i = 1, len = incentiveTrainingValue.length; i <= len; i++) {
			incentiveTempDataArr[i] = incentiveTrainingValue[i - 1].value;
		}
	}
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/updateTrainingData.js,v $"] = "$Id: updateTrainingData.js,v 1.3 2016/10/31 09:46:22 neeraj Exp $";
