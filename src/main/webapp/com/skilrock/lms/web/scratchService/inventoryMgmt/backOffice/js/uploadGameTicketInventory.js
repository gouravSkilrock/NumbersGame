	
	// done by Arun Upadhyay for Common ajax call in whole javascript
var i18nObj = parent.frames[0].i18Obj;
function _commonAjaxFunction(passedUrl) {
	var MAXIMUM_WAITING_TIME = 3 * 60 * 1000;
	var xhReq = new createXMLHttpRequest();
				var _c="?";
				if(passedUrl.indexOf("?")!=-1){_c="&";}
				passedUrl=passedUrl+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	
	xhReq.open("GET", passedUrl, false);
	var requestTimer = setTimeout(function () {
			xhReq.abort();
			_commonAjaxFunction(passedUrl);
		}, MAXIMUM_WAITING_TIME);
		
	var serverResponse;
	xhReq.onreadystatechange = function () {
			if (xhReq.readyState != 4 || xhReq.status != 200) { return;	}
			clearTimeout(requestTimer);			
			serverResponse = xhReq.responseText;
			return;
	};
	
	xhReq.setRequestHeader("If-Modified-Since", new Date().getTime());
	xhReq.send(null);
	if (xhReq.readyState != 4 || xhReq.status != 200) {
		return;
	}
	clearTimeout(requestTimer);			
	serverResponse = xhReq.responseText;
	 //alert(serverResponse); 
	return serverResponse;
}

function createXMLHttpRequest() {
	try {	return new XMLHttpRequest();	}	catch (e) {	}
	try {	return new ActiveXObject("Msxml2.XMLHTTP");	}	catch (e) {	}
	try {	return new ActiveXObject("Microsoft.XMLHTTP");	}	catch (e) {	}
	alert(i18nObj.prop('error.xmlhttp.not.support'));
	return null;
}

function getGameListForInventory(url, divId, supplierId) {
	_id.o("supplierName").selectedIndex = 0;
	var resData = _commonAjaxFunction(url);
	var resArr = resData.split("||");
		
		
	// add game list into option from the response data
	var gameNameList = resArr[0].split(",");
	var gameOption = _id.o(divId);
	gameOption.options.length = 1;
	for (var i = 0; i < gameNameList.length; i++) {
		if (gameNameList[i] != "") {
			var key = gameNameList[i];
			var opt = new Option(key, key);
			gameOption.options[i + 1] = opt;
		}
	}
	
	
	// add supplier list into option from the response data
	if (supplierId) {
		var supplierList = resArr[1].split(",");
		var supOption = _id.o(supplierId);
		supOption.options.length = 1;
		for (var i = 0; i < supplierList.length; i++) {
			if (supplierList[i] != "") {
				var sup = supplierList[i].split("=");
				var key = sup[1].replace(" ", "");
				var value = sup[0];
				var opt = new Option(key, value);
				supOption.options[i + 1] = opt;
			}
		}
	}
	disableSubmit();
	reloadOriginal();
}



function disableSubmit() {
	_id.o("subButton").disabled = true;
}

function enableSubmit() {
	_id.o("subButton").disabled = false;
}


function disableVerify() {
	_id.o("verify").disabled = true;
}

function enableVerify() {
	var tableId = _id.o("addrowtb2");
	var iteration = parseInt(tableId.rows.length, 10);
	
	for (var a =0; a<=iteration; a++) {	
		if(_id.o('error-'+a)){
			if(_id.o('error-'+a).innerHTML == "Pack Series Is Valid")
				_id.o('error-'+a).innerHTML ="&nbsp;";
			if(_id.o('error-'+a).innerHTML!="&nbsp;"){
				_id.o("verify").disabled = true;
				return false;
			}
		}	
	}
	_id.o("verify").disabled = false;
}



function addPackToTable(divToTable) {
	var multiplierDiv;
	multiplierDiv = "packSeries";
	var tbl = _id.o(divToTable);
	var lastRow = tbl.rows.length;
	var iteration = parseInt(lastRow, 10);
	var row = tbl.insertRow(lastRow);
		row.setAttribute("id", "row-" + iteration);
	var cellLeft = row.insertCell(0);
	var cellLeft1 = row.insertCell(1);
	var cellLeft2 = row.insertCell(2);
	var cellLeft3 = row.insertCell(3);
	var cellLeft4 = row.insertCell(4);
	var cellLeft5 = row.insertCell(5);
	var cellLeft6 = row.insertCell(6);
	var newdivText = document.createTextNode("From");
	cellLeft.appendChild(newdivText);
	var newdiv = document.createElement("div");
	newdiv.innerHTML = "<input type=\"text\" size = \"10\" name=\"packNumberFrom\" id=\"pnumfrom-" + iteration + "\" onkeypress=\"return isValidEntries()\" onblur=\"validateFrom(this.id)\" onchange=\"disableSubmit()\"></input>";
	cellLeft1.appendChild(newdiv);
	newdivText = document.createTextNode("To");
	cellLeft2.appendChild(newdivText);
	newdiv = document.createElement("div");
	newdiv.innerHTML = "<input type=\"text\" size = \"10\" name=\"packNumberTo\" id=\"pnumto-" + iteration + "\" onkeypress=\"return isValidEntries()\" onblur=\"validateFromTo(this.id)\"  onchange=\"disableSubmit()\"></input>";
	cellLeft3.appendChild(newdiv);
	newdiv = document.createElement("div");
	newdiv.setAttribute("id", "totpk-" + iteration);
	newdiv.innerHTML = "<input type=text size = \"1\" size='3' id=\"totpkVal-" + iteration + "\" name=\"totalpack\" readonly=\"true\"/>";
	cellLeft4.appendChild(newdiv);
	newdiv = document.createElement("div");
	//alert(iteration+'<input type="button" id = "'+ multiplierDiv +'-' + iteration + '" name="remove" value="Remove" onclick = "removeRow(this.id)"/>');
	newdiv.innerHTML = '<input type="button" id = "'+ multiplierDiv +'-' + iteration + '" name="remove" value="Remove" onclick = "removeRow(this.id)"/>';
	//alert(newdiv.innerHTML);
	cellLeft5.appendChild(newdiv);
	newdiv = document.createElement("div");
	newdiv.setAttribute("id", "error-" + iteration);
	newdiv.innerHTML = "&nbsp;";
	cellLeft6.appendChild(newdiv);
	
	_id.o("addPack").disabled = true;
	return true;
}


function removeRow(rowId) {

	var rowNoArr = rowId.split("-");	
	var tbl = _id.o('addrowtb2');
	var lastRow = tbl.rows.length;
	var rowNo = parseInt(rowNoArr[1], 10);
	var deletedRow = _id.o('row-'+rowNo);
	//alert('row-'+rowNo +"     "+deletedRow.rowIndex);
	tbl.deleteRow(deletedRow.rowIndex);
	_id.o("addPack").disabled = false;
}

 
function isValidEntries(evt) {
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if ((charCode >= 48 && charCode < 58) || charCode == 45) {
		return true;
	}
	return false;
}


function getGameNbrArr() {
	var gameName = _id.o('game_Name').value;
	var gameNbrArr = gameName.split("-");	
	return gameNbrArr;
}





function validateFromAndTO(tId) {	
	var gameNbrArr = getGameNbrArr()	
	var insertedValue = _id.o(tId);
	//alert(tId +"inserted value ==="+insertedValue.value+"===");
	var tIdArr = tId.split("-"); 
	var gameNbr = gameNbrArr[0];
	if(gameNbrArr.length <=1) {		
		_id.o('error-'+tIdArr[1]).innerHTML = "<font color='red'>"+i18nObj.prop('error.pls.slct.the.game.name')+"</font>";
		disableVerify();
		insertedValue.value = "";
		_id.o('game_Name').focus();
		return false;
	}else if(!(insertedValue.value).match(gameNbr)) {
		_id.o('error-'+tIdArr[1]).innerHTML = "<font color='red'>"+i18nObj.prop('error.pls.insert.corrct.game.nbr.of.the.game')+"</font>";
		if (insertedValue.value.indexOf("-") == -1) {
			insertedValue.value = insertedValue.value.substring(0, gameNbr.length) + "-" + insertedValue.value.substring(gameNbr.length);
		}
		disableVerify();	
		return false;
	}	
	if (insertedValue.value.indexOf("-") == -1) {
			insertedValue.value = insertedValue.value.substring(0, gameNbr.length) + "-" + insertedValue.value.substring(gameNbr.length);
	}	

	return true;
}

function  validateFrom(id) {	
	validateFromTo(id);	
}


function validateFromTo(id) {
	var tIdArr = id.split("-"); 
		
	// VERIFY THE FROM PACK DETAILS	
	if(_id.o('pnumfrom-'+tIdArr[1]).value==""){
		_id.o('error-'+tIdArr[1]).innerHTML = "<font color='red'>"+i18nObj.prop('error.pls.fill.all.fields')+"</font>";
		return false;
	}else if(!validateFromAndTO('pnumfrom-'+tIdArr[1])) {
		return false;
	}else {		
		_id.o('error-'+tIdArr[1]).innerHTML = "&nbsp;";	 
	}
	
	if(tIdArr[0] == "pnumfrom" && _id.o('pnumto-'+tIdArr[1]).value=="") return true;
	
	
	//  VERIFY THE 'TO' PACK DETAILS 
	if(_id.o('pnumto-'+tIdArr[1]).value=="") {
			_id.o('error-'+tIdArr[1]).innerHTML = "<font color='red'>"+i18nObj.prop('error.pls.fill.all.fields')+"</font>";
			return false;
	}else if(!validateFromAndTO('pnumto-'+tIdArr[1])) {
			return false;				
	}else {		
		_id.o('error-'+tIdArr[1]).innerHTML = "&nbsp;";
		var e=	enableVerify();		 
	}
	 	
	 
	// VERIFY AND CALCULATE THE TOTAL NOUMBER OF PACKS BEING UPLOADED   	
	var gameNbr = getGameNbrArr()[0];
	var gameDigits = gameNbr.length;
	
	if (_id.o('pnumto-'+tIdArr[1]).value != "") {
		var div = "totpk-" + tIdArr[1];
		var fromPackNoValue = _id.o('pnumfrom-'+tIdArr[1]).value;
		var toPackNoValue = _id.o('pnumto-'+tIdArr[1]).value;
		
				
		if (fromPackNoValue.indexOf("-") == -1) {
			fromPackNoValue = fromPackNoValue.substring(0, gameDigits) + "-" + fromPackNoValue.substring(gameDigits);
			_id.o(from).value = fromPackNoValue;
		}
		
		if (toPackNoValue.indexOf("-") == -1) {
			toPackNoValue = toPackNoValue.substring(0, gameDigits) + "-" + toPackNoValue.substring(gameDigits);
			_id.o(to).value = toPackNoValue;
		}
		
		
		var fromVal = fromPackNoValue;
		var toVal = toPackNoValue;
				
		if (fromVal.match("-") != null || toVal.match("-") != null) {
			var noOfPackFrm = fromVal.substring(fromVal.indexOf("-") + 1, fromVal.length);
			var noOfPackTo = toVal.substring(toVal.indexOf("-") + 1, toVal.length);
			//check for valid packs regarding their pack no. length
			if(noOfPackFrm.length != noOfPackTo.length) {
				_id.o(div).innerHTML = "please check Pack entries";
				_id.o("addPack").disabled = true;
				_id.o("verify").disabled = true;
				return false;
			}
			//check for '000' entry not allowed in packs
			if(parseInt(noOfPackTo, 10) == 0 ||  parseInt(noOfPackFrm, 10) == 0) {
				_id.o(div).innerHTML = "'000' entry not allowed in packs";
				_id.o("addPack").disabled = true;
				_id.o("verify").disabled = true;
				return false;
			}
			
			//calculate total pack no. being uploaded 
			if (parseInt(noOfPackTo, 10) < parseInt(noOfPackFrm, 10)) {
				_id.o(div).innerHTML = "From Greater";
				_id.o("addPack").disabled = true;
				_id.o("verify").disabled = true;
			} else {
				var noOfPack = parseInt(noOfPackTo, 10) - parseInt(noOfPackFrm, 10) + 1;
				if (noOfPack > 0) {
					_id.o(div).innerHTML = "<input type=text size=3 readonly name='totalpack' value=" + noOfPack + ">";
					_id.o("addPack").disabled = false;
					enableVerify();		
				} else {
					_id.o(div).innerHTML = "please check entries";
					_id.o("addPack").disabled = true;
					_id.o("verify").disabled = true;
				}
			}	
		} else {
			_id.o(div).innerHTML = "Packs Not Seperated by '-'";
			_id.o("addPack").disabled = true;
			_id.o("verify").disabled = true;
		}
	}	
}




 function verifyInsertedPacks(verifyUrl, errorDiv) {
	if(!isEmpty()) return false;
	
	var gameNameValue = _id.o('game_Name').value;
	
	var packNumberFromArr = new Array();
	var packNumberToArr = new Array();
	var totalPackArr = new Array();
	var messRowDivIdsArr = new Array();
	var fromCount = 0, toCount = 0, tpCount = 0;
	var formElmntsArr = document.forms[0].elements;
	
	for (var i = 0; i<formElmntsArr.length; i++) {
		var pack = formElmntsArr[i];
		if (pack.name.match("packNumberFrom")) {
			packNumberFromArr[fromCount] = pack.value;
			messRowDivIdsArr[fromCount] = pack.id;	
			fromCount++;		
		}		
		if (pack.name.match("packNumberTo")) {		
			packNumberToArr[toCount] = pack.value;			
				toCount++;
		}
		if (pack.name.match("totalpack")) {		
			totalPackArr[tpCount] = pack.value;			
				tpCount++;
		}	
		
	}
	
	var url = verifyUrl + '?gameName='+gameNameValue+'& packNumberTo='+packNumberToArr
			+'& packNumberFrom='+packNumberFromArr+'& totalPackStr='+totalPackArr;
	//alert("url1 is === "+url);
	var resData = _commonAjaxFunction(url);	
	//alert(resData);
	var verifyRes = resData.split(",");
	if(verifyRes.length == 1 && verifyRes[0] == "") {
		alert(i18nObj.prop('error.probIn.tkt.verification.pls.re.varify.the.tkt.inv.data'));
		return false;
	}
	
	var returnFlag = true;	
	for(var i=0; i<verifyRes.length; i++) {
		if(verifyRes[i] == "Pack Series Is Valid") {
			//alert('error-'+messRowDivIdsArr[i].split('-')[1]+" ====="+_id.o('error-'+messRowDivIdsArr[i].split('-')[1]));
			_id.o('error-'+messRowDivIdsArr[i].split('-')[1]).innerHTML = verifyRes[i];
		} else {
			_id.o('error-'+messRowDivIdsArr[i].split('-')[1]).innerHTML = "<font color = 'red'>"+verifyRes[i]+"</font>";
			returnFlag = false;
		}		
	}
	
	if(returnFlag) enableSubmit();
	else disableSubmit();
	
	return  returnFlag;
 }









function isEmpty() {
	var returnFlag = false;
	var gameNameObj = _id.o('game_Name');
	var suppNameObj = _id.o('supplierName');
	var warehouseNameObj = _id.o('warehouseId');
	if(gameNameObj.value == "Please Select") {
		_id.o('game_name_error').innerHTML = i18nObj.prop('error.entr.game.name');
		returnFlag = true;
	}
	if(suppNameObj.value == "Please Select") {
		_id.o('supp_name_error').innerHTML = "<font color='red'>" + i18nObj.prop('error.pls.slct.supplier.name') + "!</font>";
		returnFlag = true;
	}
	if(warehouseNameObj.value == "Please Select") {
		_id.o('warehouse_name_error').innerHTML = "<font color='red'>" + i18nObj.prop('error.pls.slct.warehouse.name') + "!</font>";
		returnFlag = true;
	}
	
	// check that the form elements filled completed or not 
	var formElmntsArr = document.forms[0].elements;
	for (var i = 0; i<formElmntsArr.length; i++) {
		var elmnt = formElmntsArr[i];
		if (elmnt.value == "") {
			_id.o("fill_error").innerHTML = "<font color=\"red\">"+i18nObj.prop('error.pls.fill.all.crrct.valuesIn.the.fields')+"</font>";
			return false;
		}
	}
		
	var count = 0;
	var packArray = new Array();	
	
	var packNumberFromArr, packNumberToArr;
	var packnumberfrom, packnumberTo;
	
	for (var i = 0; i<formElmntsArr.length; i++) {
		var pack = formElmntsArr[i];
		if (pack.name.match("packNumberFrom")) {
			packNumberFromArr = pack.value.split("-");
			packnumberfrom = packNumberFromArr[1];
		}		
		if (pack.name.match("packNumberTo")) {		
			packnumberToArr = pack.value.split("-");
			packnumberTo = packnumberToArr[1];
			for (var j = parseInt(packnumberfrom, 10); j <= parseInt(packnumberTo, 10); j++) {
				packArray[count] = j;
				count++;	
			}
		}	
		
	}
	
	var packArraySize = packArray.length;	
	for (var k = 0; k <= parseInt(packArraySize, 10); k++) {
		for (var D = (parseInt(k, 10) + 1); D <= parseInt(packArraySize, 10); D++) {
			if (packArray[k] == packArray[D]) {		
				_id.o("fill_error").innerHTML = "<font color=\"red\">"+i18nObj.prop('error.pls.remove.duplicate.entries.fr.book.nbr')+"</font>";
				return false;
			}
		}
	}
	
	_id.o("fill_error").innerHTML = "";
	return true;
}
		

		
		
function reloadOriginal(selectedIndexOfGame) {
	
	_id.o("addPack").disabled = true;
	_id.o("verify").disabled = true;
	_id.o("subButton").disabled = true;
	_id.o("error-0").innerHTML ="&nbsp;";
	_id.o("totpk-0").innerHTML = "<input type='text' size='3' readonly name='totalpack' value=''>";
	
	var gtypeValue;
	if(_id.o("gtypeNEW").checked==true)
		gtypeValue =  _id.o("gtypeNEW");
	else if(_id.o("gtypeOPEN").checked==true)
		gtypeValue =  _id.o("gtypeOPEN");
	else  
		gtypeValue =  _id.o("gtypeNEW");
		
	document.forms[0].reset();	
	gtypeValue.checked=true;
	//alert(selectedIndexOfGame);	
	if(selectedIndexOfGame)
		_id.o('game_Name').selectedIndex = selectedIndexOfGame;
	
	var tableId = _id.o("addrowtb2");
	var iteration = parseInt(tableId.rows.length, 10);
	for (var a =2;a<iteration; a++) {
		tableId.deleteRow(a);
	}
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/uploadGameTicketInventory.js,v $'] = '$Id: uploadGameTicketInventory.js,v 1.3 2016/10/31 09:46:20 neeraj Exp $';

