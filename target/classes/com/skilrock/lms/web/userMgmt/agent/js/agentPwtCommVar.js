
function validateFormEntries() {
	var agentOrgNameObj = _id.o("agentOrgName");
	var serviceNameObj = _id.o("serviceName");
	var orgTypeObj = _id.o("orgType");
	if (serviceNameObj.value == "-1") {
		_id.i("agentOrgError", "Please Select Service Name .", "e");
		return false;
	} else {
		if (orgTypeObj.value == "-1") {
			_id.i("agentOrgError", "Please Select Organization Type .", "e");
			return false;
		} else {
			if (agentOrgNameObj.value == "-1") {
		//alert("agentOrgNameObj.value  "+agentOrgNameObj.value);
				_id.i("agentOrgError", "Please Select " + tierMap["RETAILER"] + " Organization Name .", "e");
				return false;
			}
		}
	}
	_id.i("agentOrgError", "");
	return true;
}
function setInputHiddenValue(bValue) {
	//alert("skjhjkhfjkghdfjkghl"+bValue);
	document.getElementById("updateGameDiv").innerHTML = "";
	document.getElementById("hiddenInput").value = bValue;
}
function hideDiv() {
	document.getElementById("updateGameDiv").innerHTML = "";
	document.getElementById("result").innerHTML = "";
}
function enableInputFields(checkId) {
	//alert("inside"+document.getElementById(checkId));
	if (document.getElementById(checkId).checked == true) {
		//alert("inside if");
		document.getElementById("updateGameDiv").innerHTML = "";
		document.getElementById("allSaleCommRate").disabled = false;
		document.getElementById("allSaleCommRate").style.backgroundColor = "yellow";
		document.getElementById("allSaleCommRate").focus();
		document.getElementById("allPwtCommRate").disabled = false;
		document.getElementById("allPwtCommRate").style.backgroundColor = "yellow";
		document.getElementById("submitbutoon").disabled = false;
	} else {
	//alert("inside else");
		document.getElementById("allcheck").checked = false;
		document.getElementById("allSaleCommRate").disabled = true;
		document.getElementById("allSaleCommRate").style.backgroundColor = "white";
		document.getElementById("allPwtCommRate").disabled = true;
		document.getElementById("allPwtCommRate").style.backgroundColor = "white";
		document.getElementById("submitbutoon").disabled = true;
	}
}
function disableAllInputFields() {
	document.getElementById("allcheck").checked == false;
	document.getElementById("allSaleCommRate").disabled = true;
	document.getElementById("allSaleCommRate").style.backgroundColor = "white";
	document.getElementById("allPwtCommRate").disabled = true;
	document.getElementById("allPwtCommRate").style.backgroundColor = "white";
	document.getElementById("submitbutoon").disabled = true;
	document.getElementById("allcheck").checked = false;
}
function updateTableColumn(tableId, type, value) {
	//alert(value+"   : type = "+type +"  "+document.getElementById(tableId));
	var table = document.getElementById(tableId);
	var rows = table.getElementsByTagName("tr");
	if (type == "sale" && value != "") {
		for (var i = 2; i < rows.length; i++) {
			var newCommVar = parseFloat(value);
			var rowCells = rows[i].getElementsByTagName("td");
			var commVar = 0;
			if (rowCells[2].innerText == "0.00") {
				commVar = 0;
			} else {
				commVar = parseFloat(rowCells[2].innerText);
			}
	    //alert(commVar);
			var netCommVar = (newCommVar + commVar) + "";
			rowCells[2].innerText = newCommVar;
			var netCommRate = 0;
			if (rowCells[1].innerText == "0.00") {
				netCommRate = 0;
			} else {
				netCommRate = parseFloat(rowCells[1].innerText);
			}
			rowCells[3].innerText = newCommVar + netCommRate;
 		  //document.getElementById('allSaleCommRate').readOnly=true;
			document.getElementById("allPwtCommRate").focus();
		}
	} else {
		if (type == "pwt" && value != "") {
			for (var i = 2; i < rows.length; i++) {
				var newCommVar = parseFloat(value);
				var rowCells = rows[i].getElementsByTagName("td");
				var commVar = 0;
				if (rowCells[5].innerText == "0.00") {
					commVar = 0;
				} else {
					commVar = parseFloat(rowCells[5].innerText);
				}
	    //alert(commVar);
				var netCommVar = (newCommVar + commVar) + "";
				rowCells[5].innerText = newCommVar;
				var netCommRate = 0;
				if (rowCells[4].innerText == "0.00") {
					netCommRate = 0;
				} else {
					netCommRate = parseFloat(rowCells[4].innerText);
				}
				rowCells[6].innerText = newCommVar + netCommRate;
 		  //document.getElementById('allPwtCommRate').readOnly=true;
			}
		}
	}
}
function updateSpecificRow(type, value) {
	//var gamename=document.getElementById('g').value;
	//alert("gane name : "+gameName);
	var table = document.getElementById("gameDetails");
	var rows = table.getElementsByTagName("tr");
	var row;
	for (var i = 2; i < rows.length; i++) {
		var rowCells = rows[i].getElementsByTagName("td");
		if (rowCells[0].innerText == gameName) {
			row = rows[i];
		}
	}
	var rowCells = row.getElementsByTagName("td");
	if (type == "sale" && value != "") {
		var newCommVar = parseFloat(value);
		var defCommVar = parseFloat(rowCells[1].innerText);
		var netCommRate = newCommVar + defCommVar;
		rowCells[2].innerText = newCommVar;
		rowCells[3].innerText = netCommRate;
	} else {
		if (type == "pwt" && value != "") {
			var newCommVar = parseFloat(value);
			var defCommVar = parseFloat(rowCells[4].innerText);
			var netCommVar = (newCommVar + defCommVar);
			rowCells[5].innerText = newCommVar;
			rowCells[6].innerText = newCommVar + defCommVar;
		}
	}
}
function setDivValue(rowId) {
 	//alert(rowId);
 	//var newRowId="row"+rowId;
	disableAllInputFields();
	var row = document.getElementById(rowId);
 	//alert(" row "+row)
	var rowCells = row.getElementsByTagName("td");  
    //alert("rowCells length : "+rowCells.length +" colunm text = "+rowCells[0].innerText);
	var divObj = document.getElementById("updateGameDiv");    
    //alert(divObj);
	var serviceName = document.getElementById("serviceName").value;
    //alert('**serviceName*****'+serviceName+'****');
	if (serviceName == "CS") {
		document.getElementById("updateGameDiv").innerHTML = createTableForCSCommVar(rowCells);
	} else {
		document.getElementById("updateGameDiv").innerHTML = createTableForVar(rowCells);
	}
}
function setBlank(tid) {
	document.getElementById(tid).value = "";
}
function getOrgListing(url, val) {
	selectBox = val;
	var orgTypeVal = document.getElementById("orgType").value;
	if (orgTypeVal != "-1") {
		_resp = _ajaxCall(url);
		_respData = _resp.data;
		if(_respData != ""){
			var resData = _respData.substring(0,_respData.length-1).split(":");
			var retObj = _id.o("agentOrgName");
			retObj.options.length = 1;
			for (var i = 0; i < resData.length; i = i + 1) {
				var val = resData[i].split("|");
				var opt = new Option(val[0], val[1]);
				retObj.options[i + 1] = opt;
			}
		} else {
			_id.i("agentOrgError", "No Retailers Available", "e");
			return false;
		}
	} else {
		document.getElementById("agentOrgName").value = -1;
		var retObj = _id.o("agentOrgName");
		retObj.options.length = 1;
	}
}
function createTableForCSCommVar(rowCells) {
	disableAllInputFields();
	var str = "<h3>Sale rates to update</h3>";
	var tablestr = "<table align='left' id='updateTable'>";
	var serviceName = document.getElementById("serviceName");
	serviceName = serviceName[serviceName.selectedIndex].text;
	var orgType = document.getElementById("orgType");
	orgType = orgType[orgType.selectedIndex].text;
	//alert("aa");
	var agentSelect = document.getElementById("agentOrgName");
	agentOrgName = agentSelect[agentSelect.selectedIndex].value;
	//alert("bb");
	//alert("agent Org Name "+agentOrgName);
	str = str + "<tr><td class='label'>Organization Type: </td><td><input type='text' name='orgType' id='orgType' readonly='readonly' value='" + orgType + "'></td></tr>";
	str = str + "<tr><td class='label'>Organization Name: </td><td><input type='text' name='agtOrgName' id='agtOrgName' readonly='readonly' value='" + agentOrgName + "'></td></tr>";
	if (BrowserDetect.browser == "Safari" || BrowserDetect.browser == "Firefox" || BrowserDetect.browser == "Chrome") {
		str = str + "<tr><td class='label'>Product Name: </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>Default Sale Profit Rate: </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='" + rowCells[1].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>Sale Profit Variance: </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].textContent + "'  onclick='setBlank(this.id)'></td></tr>";
	} else {
		str = str + "<tr><td class='label'>Product Name: </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>Default Sale Profit Rate: </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='" + rowCells[1].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>Sale Profit Variance: </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].innerText + "'  onclick='setBlank(this.id)'></td></tr>";
	}
	//alert(rowCells[5].innerText);
	str = str + "<tr><td>&nbsp;</td> <td align='left'><input type='button' value='Update'\tid='submitbutoon2'    class='button'  \tname='button'  onclick='submitUpdatedValueCS()'  /></td><tr>";
	tablestr = tablestr + str + "</table>";	
	//alert(tablestr);
	return tablestr;
}
function createTableForVar(rowCells) {
	disableAllInputFields();
	var str = "<h3>Sale and Winning rates to update</h3>";
	var tablestr = "<table align='left' id='updateTable'>";
	var serviceName = document.getElementById("serviceName");
	serviceName = serviceName[serviceName.selectedIndex].text;
	var orgType = document.getElementById("orgType");
	orgType = orgType[orgType.selectedIndex].text;
	//alert("aa");
	var agentSelect = document.getElementById("agentOrgName");
	agentOrgName = agentSelect[agentSelect.selectedIndex].value;
	var agentOrg =   agentSelect.options[agentSelect.selectedIndex].text;	
	//alert("bb");
	//alert("agent Org Name "+agentOrgName);
	str = str + "<tr><td class='label'>Organization Type: </td><td><input type='text' name='orgType' id='orgType' readonly='readonly' value='" + orgType + "'></td></tr>";
	str = str + "<tr><td class='label'>Organization Name: </td><td><input type='text' name='agentOrgnme' id='agentOrgnme' readonly='readonly' value='" + agentOrg + "'></td></tr>";
	str = str + "<tr><td class='label'></td><td><input type='hidden' name='agtOrgName' id='agtOrgName' readonly='readonly' value='" + agentOrgName + "'></td></tr>";
	if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
		str = str + "<tr><td class='label'>Game Name: </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>Default Sale Profit Rate: </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='" + rowCells[1].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>Sale Profit Variance: </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].textContent + "'  onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>Default Winning Profit Rate: </td><td><input type='text'  name='defPwtCommRate' id='defPwtCommRate' readonly='readonly' value='" + rowCells[4].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>Winning Profit Rate: </td><td><input type='text' name='pwtCommVar' id='pwtCommVar' style='background-color: #F9FCCD' value='" + rowCells[5].textContent + "' onclick='setBlank(this.id)'></td></tr>";
	} else {
		str = str + "<tr><td class='label'>Game Name: </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>Default Sale Profit Rate: </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='" + rowCells[1].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>Sale Profit Variance: </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].innerText + "'  onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>Default Winning Profit Rate: </td><td><input type='text'  name='defPwtCommRate' id='defPwtCommRate' readonly='readonly' value='" + rowCells[4].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>Winning Profit Rate: </td><td><input type='text' name='pwtCommVar' id='pwtCommVar' style='background-color: #F9FCCD' value='" + rowCells[5].innerText + "' onclick='setBlank(this.id)'></td></tr>";
	}
	//alert(rowCells[5].innerText);
	str = str + "<tr><td>&nbsp;</td> <td align='left'><input type='button' value='Update'\tid='submitbutoon2'    class='button'  \tname='button'  onclick='submitUpdatedValue()'  /></td><tr>";
	tablestr = tablestr + str + "</table>";	
	//alert(tablestr);
	return tablestr;
}
/*function submitUpdatedAllValue() {
	//alert("111111111");
	var agtOrgName=document.getElementById('agentOrgName').value;
	//alert("2222222222"+agtOrgName);
	agtOrgName=convert(agtOrgName);
	var gameStatus=document.getElementById('gameStatus').value;	
	//alert("33333333333 "+gameStatus);
	var saleCommVar=document.getElementById('allSaleCommRate').value;
	saleCommVar=saleCommVar.replace(" ","");
	var pwtCommVar=document.getElementById('allPwtCommRate').value;
	pwtCommVar=pwtCommVar.replace(" ","");
	var sflag =isDecimal(saleCommVar);
	var pflag =isDecimal(pwtCommVar);
	if(pflag && (pwtCommVar>100 || pwtCommVar<0) ) {		
		alert("Winning Profit Variance must be less than 100% or greater then or  equal to 0%");
		document.getElementById('allPwtCommRate').focus();			
	}
	if(sflag)  {
		alert("Please Enter a Valid Decimal Number in Sale Profit Variance.");
		document.getElementById('allSaleCommRate').focus();		
	}else if(pflag)  {
		alert("Please Enter a Valid Decimal Number Winning Profit Variance.");
		document.getElementById('allPwtCommRate').focus();		
	}
	else {	
	
		var newUrl="submitUpdatedAllValue.action?agtOrgName="+agtOrgName+"&gameStatus="+gameStatus+"&saleCommVar="+saleCommVar+"&pwtCommVar="+pwtCommVar;
		mainDiv="all";
		_resp  = _ajaxCall(newUrl);
		if(_resp.result){
			var resText = _resp.data;
			if(resText=='successfully updated') { 					
					updateTableColumn('gameDetails', 'sale', document.getElementById('allSaleCommRate').value);			
					updateTableColumn('gameDetails', 'pwt', document.getElementById('allPwtCommRate').value);
					document.getElementById(mainDiv).innerHTML = resText;
			}
			else if(resText=='Some Internal Error occurred')
					document.getElementById(mainDiv).innerHTML = resText;
			else if(resText=='You Must Upadate Sale Or Winning Commission Variance Value')	  {
					alert("You Must Upadate Sale Or Winning Profit Variance Value");				
					document.getElementById('allSaleCommRate').focus();
			}else {
					alert(resText);				
					document.getElementById('allSaleCommRate').focus();				
			}

		}else{
			alert("Problem:------");
		}


	}
}*/
function convert(urlX) {
	var exp = /&/g;
	return urlX.replace(exp, "amp");
}
var mainDiv;
var req;
var which;
var gameName;
function submitUpdatedValue() {
	//alert("ssss");
	var serviceName = document.getElementById("serviceName").value;
	var orgType = document.getElementById("orgType").value;
	var agtOrgName = document.getElementById("agtOrgName").value;
	agtOrgName = convert(agtOrgName);
	var gname = document.getElementById("gname").value;
	gameName = gname;
	//alert(gname);
	var defSaleCommRate = document.getElementById("defSaleCommRate").value;
	var saleCommVar = document.getElementById("saleCommVar").value;
	saleCommVar = saleCommVar.replace(" ", "");
	//alert("11111111");
	var defPwtCommRate = document.getElementById("defPwtCommRate").value;
	//alert("1111111111aaaaaaaaaa");
	var pwtCommVar = document.getElementById("pwtCommVar").value;
	//alert("1111111111bbbbbbbbb");
	pwtCommVar = pwtCommVar.replace(" ", "");
	//alert("111111111111111111cccccccccc");
	var sflag = isDecimal(saleCommVar);
	//alert("11111111dddddddddd");
	var pflag = isDecimal(pwtCommVar);
	//alert("222222222222222222");
	var diff = parseFloat(defSaleCommRate) + parseFloat(saleCommVar);
	if (!validateAmount(saleCommVar) || !validateAmount(pwtCommVar)) {
		return false;
	}
	if (diff < 0) {
		alert("Calculated Net Profit Variance is Negetive. Please Enter Another Value in Sale Profit Variance.");
		document.getElementById("saleCommVar").focus();
		return sflag;
	}
	//alert("3333333333");
	var pwtdiff = parseFloat(defPwtCommRate) + parseFloat(pwtCommVar);
	if ((parseFloat(pwtCommVar) > 100 || pwtdiff < 0)) {
		alert("Winning Profit Variance must be less than 100% or greater then or  equal to 0%");
		document.getElementById("pwtCommVar").focus();
		return false;
	}
	if (pflag) {
		alert("Please Enter a Valid Decimal Numbers Winning Profit Variance");
		document.getElementById("pwtCommVar").focus();
		return pflag;
	} else {
		if (sflag) {
			alert("Please Enter a Valid Decimal Numbers Sale Profit Variance");
			document.getElementById("saleCommVar").focus();
			return sflag;
		} else {	
	//alert(" game type value is == "+gameTypeValue);
	//alert("444444444");
			var newUrl = "agent_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
	//alert("new url is == "+newUrl);
			mainDiv = "updateGameDiv";
			var _c = "?";
			if (newUrl.indexOf("?") != -1) {
				_c = "&";
			}
			newUrl = newUrl + _c + new Date().getTime() + parent.frames[0].sesId + Math.random() * 1000000;
			if (window.XMLHttpRequest) { // Non-IE browsers
				req = new XMLHttpRequest();
				req.onreadystatechange = processStateChange;
				try {
					req.open("GET", newUrl, true);
					req.setRequestHeader("If-Modified-Since", new Date().getTime());//added later
				}
				catch (e) {
					alert(e);
				}
				req.send(null);
			} else {
				if (window.ActiveXObject) { // IE
					req = new ActiveXObject("Microsoft.XMLHTTP");
					if (req) {
						req.onreadystatechange = processStateChange;
						req.open("GET", newUrl, true);
						req.setRequestHeader("If-Modified-Since", new Date().getTime());// added later
						req.send();
					}
				}
			}
		}
	}
}
function processStateChange() {
	if (req.readyState == 4) { // Complete
		if (req.status == 200) { // OK response
      //alert("received" +req.statusText);
    	//alert(req.responseText);
			if (req.responseText == "successfully updated") {
				updateSpecificRow("sale", document.getElementById("saleCommVar").value);
    			//alert("last ============ 11111111111111");
				updateSpecificRow("pwt", document.getElementById("pwtCommVar").value);
				//alert("last ============ 2222222222222222");
				document.getElementById("updateGameDiv").innerHTML = req.responseText;
			} else {
				if (req.responseText == "Some Internal Error occurred") {
					document.getElementById(mainDiv).innerHTML = req.responseText;
				} else {
					if (req.responseText == "You Must Upadate Sale Or Winning Commission Variance Value") {
						alert("You Must Upadate Sale Or Winning Profit Variance Value");
						document.getElementById("saleCommVar").focus();
					} else {
				//alert(req.responseText);
						var result = req.responseText;
						var str = result.split(";");
				//alert(str[0]=="SALE");
						if (str[0] == "SALE") {
							alert("pbm:Sale Profit Variance must be less than " + str[1]);
							document.getElementById("saleCommVar").focus();
						} else {
							if (str[0] == "AGENT_DIFF_GREATER") {
								alert("pbm:Maximum Net Sale Profit Variance should be " + str[1] + " and Maximum Net Winning Profit Rate should be " + str[2]);
							} else {
								alert("pbm:Winning Profit Variance must be less than " + str[1]);
								document.getElementById("pwtCommVar").focus();
							}
						}
					}
				}
			}
		} else {
			alert("Problem: " + req.statusText);
		}
	}
}
function isDecimal(sText1) {
	var sText = sText1;
	var validChars = "0123456789.+-";
	var isNumber = false;
	//alert("111111111111111111111111--sText.length : "+sText.length);
	var myChar;
	var count = 0;
	if (sText.length == 0) {
		isNumber = true;
	}
	for (i = 0; i < sText.length; i++) {
		myChar = sText.charAt(i);
		if (myChar == ".") {
			count++;
		}
		if (validChars.indexOf(myChar) == -1 || count > 1) {
			isNumber = true;
			break;
		}
	}
	//alert("22222222222222222222222");
	return isNumber;
}
function hideGameStatus(val) {
	//alert('***'+val+'***');
	if (val == "DG") {
		_id.o("gameStatus").disabled = true;
	} else {
		if (val == "SE") {
			_id.o("gameStatus").disabled = false;
		} else {
			if (val == "CS") {
				_id.o("gameStatus").disabled = true;
			}
		}
	}
}
function validateAmount(amt) {
	var splitAmtArr = amt.split(".");
	if (splitAmtArr.length > 1) {
		if (splitAmtArr[1].length > 2) {
			alert("Please Enter Amount Upto Two Decimal Points Only");
			return false;
		}
	}
	return true;
}
function submitUpdatedValueCS() {
	var serviceName = document.getElementById("serviceName").value;
	var orgType = document.getElementById("orgType").value;
	var agtOrgName = document.getElementById("agtOrgName").value;
	agtOrgName = convert(agtOrgName);
	var gname = document.getElementById("gname").value;
	gameName = gname;
	var defSaleCommRate = document.getElementById("defSaleCommRate").value;
	var saleCommVar = document.getElementById("saleCommVar").value;
	saleCommVar = saleCommVar.replace(" ", "");
	var sflag = isDecimal(saleCommVar);
	var diff = parseFloat(defSaleCommRate) + parseFloat(saleCommVar);
	//alert(diff);
	if (isNaN(saleCommVar)) {
		alert("Please Enter a Valid Decimal Numbers Sale Profit Variance.");
		document.getElementById("saleCommVar").focus();
		return false;
	}
	if (!validateAmount(saleCommVar)) {
		return false;
	}
	if (diff < 0) {
		alert("Calculated Net Profit Variance is Negetive. Please Enter Another Value in Sale Profit Variance.");
		document.getElementById("saleCommVar").focus();
		return sflag;
	}
	if (sflag) {
		alert("Please Enter a Valid Decimal Numbers Sale Profit Variance");
		document.getElementById("saleCommVar").focus();
		return sflag;
	} else {
		var newUrl = "";
		if (orgType == "AGENT") {
			newUrl = "bo_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
		} else {
			if (orgType == "RETAILER") {
					//alert(agtOrgName);
				newUrl = "agent_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
			}
		}
		mainDiv = "updateGameDiv";
		var _c = "?";
		if (newUrl.indexOf("?") != -1) {
			_c = "&";
		}
		newUrl = newUrl + _c + new Date().getTime() + parent.frames[0].sesId + Math.random() * 1000000;
		if (window.XMLHttpRequest) { // Non-IE browsers
				//alert('');
			req = new XMLHttpRequest();
				//alert('**window.req******'+req+'****');
			req.onreadystatechange = processStateChangeCS;
				//alert('**req.onreadystatechange******'+req.onreadystatechange+'****');
			try {
				req.open("GET", newUrl, true);
				req.setRequestHeader("If-Modified-Since", new Date().getTime());//added later
			}
			catch (e) {
				alert(e);
			}
			req.send(null);
		} else {//alert('');
			if (window.ActiveXObject) { // IE
				//alert('**window.ActiveXObject******'+window.ActiveXObject+'****');
				req = new ActiveXObject("Microsoft.XMLHTTP");
					//alert('**window.req******'+req+'****');
				if (req) {
					//alert('');
					req.onreadystatechange = processStateChange;
					req.open("GET", newUrl, true);
					req.setRequestHeader("If-Modified-Since", new Date().getTime());// added later
					req.send();
				}
			}
		}
	}
}
function processStateChangeCS() {
	if (req.readyState == 4) { // Complete
		if (req.status == 200) { // OK response
    //  alert("received" +req.statusText);
    	//alert(req.responseText);
			if (req.responseText == "successfully updated") {
				updateSpecificRow("sale", document.getElementById("saleCommVar").value);
    			//alert("last ============ 11111111111111");
				//alert(req.responseText);
				document.getElementById("updateGameDiv").innerHTML = req.responseText;
			} else {
				if (req.responseText == "Some Internal Error occurred") {
					document.getElementById(mainDiv).innerHTML = req.responseText;
				} else {
					if (req.responseText == "You Must Upadate Sale Commission Variance Value") {
						alert("You Must Upadate Sale Profit Variance Value");
						document.getElementById("saleCommVar").focus();
					} else {
				//alert(req.responseText);
						var result = req.responseText;
						var str = result.split(";");
				//alert(str[0]=="SALE");
						if (str[0] == "SALE") {
							alert("pbm:Sale Profit Variance must be less than " + str[1]);
							document.getElementById("saleCommVar").focus();
						} else {
							if (str[0] == "AGENT_DIFF_GREATER") {
								alert("pbm:Maximum Net Sale Profit Variance should be " + str[1]);
							}
						}
					}
				}
			}
		} else {
			alert("Problem: " + req.statusText);
		}
	}
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/agent/js/agentPwtCommVar.js,v $"] = "$Id: agentPwtCommVar.js,v 1.4 2016/10/31 09:46:58 neeraj Exp $";

