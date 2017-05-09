var i18nObj = parent.frames[0].i18Obj;
function validateFormEntries() {
	var agentOrgNameObj = _id.o("agentOrgName");
	var serviceNameObj = _id.o("serviceName");
	var orgTypeObj = _id.o("orgType");
	var retailerOrgNameObj = _id.o("retailerOrgName");
	if (serviceNameObj.value == "-1") {
		_id.i("agentOrgError", i18nObj.prop('error.select.srv.name'), "e");
		return false;
	} else {
		if (orgTypeObj.value == "-1") {
			_id.i("agentOrgError", i18nObj.prop('error.org.type.empty'), "e");
			return false;
		} else {
			if (agentOrgNameObj.value == "-1") {
				//alert("agentOrgNameObj.value  "+agentOrgNameObj.value);
				_id.i("agentOrgError", i18nObj.prop('msg.pls.select')+" "+tierMap['AGENT']+" "+i18nObj.prop('msg.org.name'), "e");
				return false;
			} else {
				if (orgTypeObj.value == "RETAILER") {
					if (retailerOrgNameObj.value == "-1") {
						_id.i("agentOrgError", i18nObj.prop('msg.pls.select')+" "+tierMap['RETAILER']+" "+i18nObj.prop('msg.org.name'), "e");
						return false;
					}
				}
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
function enableInputFieldsOLA(checkId){

	//alert("inside"+document.getElementById(checkId));
	if (document.getElementById(checkId).checked == true) {
		//alert("inside if");
		document.getElementById("updateGameDiv").innerHTML = "";
		document.getElementById("allWidCommRate").disabled = false;
		document.getElementById("allWidCommRate").style.backgroundColor = "yellow";
		document.getElementById("allWidCommRate").focus();
		document.getElementById("allDepCommRate").disabled = false;
		document.getElementById("allDepCommRate").style.backgroundColor = "yellow";
		document.getElementById("allNetGCommRate").disabled = false;
		document.getElementById("allNetGCommRate").style.backgroundColor = "yellow";
		document.getElementById("submitbutoon").disabled = false;
	} else {
	//alert("inside else");
		document.getElementById("allcheck").checked = false;
		document.getElementById("allWidCommRate").disabled = true;
		document.getElementById("allWidCommRate").style.backgroundColor = "white";
		document.getElementById("allDepCommRate").disabled = true;
		document.getElementById("allDepCommRate").style.backgroundColor = "white";
		document.getElementById("allNetGCommRate").disabled = true;
		document.getElementById("allNetGCommRate").style.backgroundColor = "white";
		document.getElementById("submitbutoon").disabled = true;
	}

}
function disableAllInputFieldsOLA() {
	document.getElementById("allcheck").checked == false;
	document.getElementById("allWidCommRate").disabled = true;
	document.getElementById("allWidCommRate").style.backgroundColor = "white";
	document.getElementById("allDepCommRate").disabled = true;
	document.getElementById("allDepCommRate").style.backgroundColor = "white";
	document.getElementById("allNetGCommRate").disabled = true;
	document.getElementById("allNetGCommRate").style.backgroundColor = "white";
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
		if (rowCells[0].innerText == gameName || rowCells[0].textContent == gameName) {
			row = rows[i];
		}
	}
	var rowCells = row.getElementsByTagName("td");
	if (type == "sale" && value != "") {
		var newCommVar = parseFloat(value);
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			var defCommVar = parseFloat(rowCells[1].textContent);	
		}else{
			var defCommVar = parseFloat(rowCells[1].innerText);
		}
		var netCommRate = newCommVar + defCommVar;
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			rowCells[2].textContent= newCommVar;
			rowCells[3].textContent = netCommRate;
		}else{
			rowCells[2].innerText = newCommVar;
			rowCells[3].innerText = netCommRate;
		}
	} else if(type == "wid" && value != ""){

		var newCommVar = parseFloat(value);
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			var defCommVar = parseFloat(rowCells[1].textContent);	
		}else{
			var defCommVar = parseFloat(rowCells[1].innerText);
		}
		var netCommRate = newCommVar + defCommVar;
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			rowCells[2].textContent= newCommVar;
			rowCells[3].textContent = netCommRate;
		}else{
			rowCells[2].innerText = newCommVar;
			rowCells[3].innerText = netCommRate;
		}
	
		
	} else if(type == "dep" && value != ""){


		var newCommVar = parseFloat(value);
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			var defCommVar = parseFloat(rowCells[4].textContent);	
		}else{
			var defCommVar = parseFloat(rowCells[4].innerText);
		}
		var netCommRate = newCommVar + defCommVar;
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			rowCells[5].textContent= newCommVar;
			rowCells[6].textContent = netCommRate;
		}else{
			rowCells[5].innerText = newCommVar;
			rowCells[6].innerText = netCommRate;
		}
		
	} else if(type == "netG" && value != ""){

		var newCommVar = parseFloat(value);
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			var defCommVar = parseFloat(rowCells[7].textContent);	
		}else{
			var defCommVar = parseFloat(rowCells[7].innerText);
		}
		var netCommRate = newCommVar + defCommVar;
		if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
			rowCells[8].textContent= newCommVar;
			rowCells[9].textContent = netCommRate;
		}else{
			rowCells[8].innerText = newCommVar;
			rowCells[9].innerText = netCommRate;
		}
		
	}else{
		if (type == "pwt" && value != "") {
			var newCommVar = parseFloat(value);
			if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
				var defCommVar = parseFloat(rowCells[4].textContent);
			}
			else
				var defCommVar = parseFloat(rowCells[4].innerText);
				
			var netCommVar = (newCommVar + defCommVar);
			if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
				rowCells[5].textContent = newCommVar;
				rowCells[6].textContent = newCommVar + defCommVar;
			}else{
				rowCells[5].innerText = newCommVar;
				rowCells[6].innerText = newCommVar + defCommVar;
			}
		}
	}
}
function setDivValue(rowId) {
 	//alert(rowId);
 	//var newRowId="row"+rowId;
	var serviceName = document.getElementById('serviceName').value;
	if(serviceName == 'OLA'){
		disableAllInputFieldsOLA();
	}else{
		disableAllInputFields();
	}
	var row = document.getElementById(rowId);
 	//alert(" row "+row)
	var rowCells = row.getElementsByTagName("td");  
    //alert("rowCells length : "+rowCells.length +" colunm text = "+rowCells[0].textContent);
	var divObj = document.getElementById("updateGameDiv");    
    //alert(divObj);
    if(serviceName == 'CS'){
    	document.getElementById('updateGameDiv').innerHTML=createTableForCSCommVar(rowCells);
    }else if(serviceName == 'OLA'){
    	document.getElementById('updateGameDiv').innerHTML=createTableForOLACommVar(rowCells);
    }else{
    	document.getElementById('updateGameDiv').innerHTML=createTableForVar(rowCells);
    }
}
function setBlank(tid) {
	document.getElementById(tid).value = "";
}
function getOrgListing() {
	// selectBox = val;
	var orgTypeVal = document.getElementById("orgType").value;
	if (orgTypeVal != "-1") {
		
					
		_resp = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
		_respData = _resp.data;
		var resData = _respData.split(":");
		var retObj = _id.o("agentOrgName");
		 retObj.options.length = 1;
		for (var i = 0; i < resData.length-1; i = i + 1) {
			var valArray = resData[i].split("|");
			var opt = new Option(valArray[0].toUpperCase(),valArray[1]);
			retObj.options[i + 1] = opt;
		}
		
		
	} else {
		document.getElementById("agentOrgName").value = -1;
		var retObj = _id.o("agentOrgName");
		retObj.options.length = 1;
	}
}
function getRetList(val) {
	//alert(val);
	var orgTypeVal = document.getElementById("orgType").value;
	//alert(orgTypeVal);
	if (orgTypeVal == "RETAILER") {
		_resp = _ajaxCall("fetchRetOrgList.action?orgId="+val);
		// _resp = _ajaxCall("fetchRetOrgList.action?orgId=" + val);
		_respData = _resp.data;
		var resData = _respData.split(":");
		var retObj = _id.o("retailerOrgName");
		retObj.options.length = 1;
		for (var i = 0; i < resData.length; i = i + 1) {
			var val = resData[i].split("|");
			if(val[0] != ''){
				var opt = new Option(val[0].toUpperCase(),val[1]);
				retObj.options[i + 1] = opt;
			}
		}
	
		
	} else {
		document.getElementById("retailerOrgName").value = -1;
		var retObj = _id.o("retailerOrgName");
		retObj.options.length = 1;
	}
}

function createTableForCSCommVar(rowCells) {
	disableAllInputFields();
	var str="<h3>"+i18nObj.prop('label.sales.rate.to.update')+"</h3>";
	var tablestr="<table align='left' id='updateTable'>";
	
	var serviceName=document.getElementById('serviceName');
	serviceName=serviceName[serviceName.selectedIndex].text;
	
	var orgType=document.getElementById('orgType');
	orgType=orgType.value;
	//orgType=orgType[orgType.selectedIndex].text;
	//alert("aa");
	var agentSelect = document.getElementById('agentOrgName');
	
	var retailerSelect = document.getElementById('retailerOrgName');
	var orgId ;
	if(orgType == 'AGENT'){
		agentOrgName=agentSelect[agentSelect.selectedIndex].value;
		orgId = agentSelect[agentSelect.selectedIndex].text;
	}else{
		if(orgType == 'RETAILER'){
			agentOrgName=retailerSelect[retailerSelect.selectedIndex].value;
			orgId = retailerSelect[retailerSelect.selectedIndex].text;
		}
	}
	//alert("bb");
	//alert("agent Org Name "+agentOrgName);
	str=str+"<tr><td class='label'>"+i18nObj.prop('label.org.type')+": </td><td><input type='text' name='orgType' id='orgType' readonly='readonly' value='"+orgType+"'></td></tr>";
	str=str+"<tr><td class='label'>"+i18nObj.prop('msg.org.name')+": </td><td><input type='text' name='agentOrgId' id='agentOrgId' readonly='readonly' value='" + orgId + "'><input type='hidden' name='agtOrgName' id='agtOrgName' readonly='readonly' value='" + agentOrgName + "'></td></tr>";
	if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
		str=str+"<tr><td class='label'>"+i18nObj.prop('label.product.name')+": </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='"+rowCells[0].textContent+"'></td></tr>";
		str=str+"<tr><td class='label'>"+i18nObj.prop('label.def.sale.profit.rate')+": </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='"+rowCells[1].textContent+"'></td></tr>";
		str=str+"<tr><td class='label'>"+i18nObj.prop('label.sale.profit.variance')+": </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='"+rowCells[2].textContent+"'  onclick='setBlank(this.id)'></td></tr>";
	}else{
		str=str+"<tr><td class='label'>"+i18nObj.prop('label.product.name')+": </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='"+rowCells[0].innerText+"'></td></tr>";
		str=str+"<tr><td class='label'>"+i18nObj.prop('label.def.sale.profit.rate')+": </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='"+rowCells[1].innerText+"'></td></tr>";
		str=str+"<tr><td class='label'>"+i18nObj.prop('label.sale.profit.variance')+": </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='"+rowCells[2].innerText+"'  onclick='setBlank(this.id)'></td></tr>";
	}
	//alert(rowCells[5].innerText);
	str=str+"<tr><td>&nbsp;</td> <td align='left'><input type='button' value="+i18nObj.prop('label.update')+"	id='submitbutoon2'    class='button'  	name='button'  onclick='submitUpdatedValueCS()'  /></td><tr>";
	tablestr=tablestr+str+"</table>";	
	//alert(tablestr);
	return tablestr;
}

function createTableForOLACommVar(rowCells){
	disableAllInputFieldsOLA();
	var str = "<h3>"+i18nObj.prop('label.ola.comm.rate.to.update')+"</h3>";
	var tablestr = "<table align='left' id='updateTable'>";
	var serviceName = document.getElementById("serviceName");
	serviceName = serviceName[serviceName.selectedIndex].text;
	var orgType = document.getElementById("orgType");
	orgType = orgType.value;
	//orgType = orgType[orgType.selectedIndex].text;
	//alert("****"+orgType+"******");
	var agentSelect;
	if (orgType == "AGENT") {
		agentSelect = document.getElementById("agentOrgName");
	} else {
		if (orgType == "RETAILER") {
			agentSelect = document.getElementById("retailerOrgName");
		}
	}
	agentOrgName = agentSelect[agentSelect.selectedIndex].value;
	var agentOrgId = agentSelect[agentSelect.selectedIndex].text;

	//alert("bb");
	//alert("agent Org Name "+agentOrgName);
	str = str + "<tr><td class='label'>"+i18nObj.prop('label.org.type')+": </td><td><input type='text' readonly='readonly' value='" + tierMap[orgType].toUpperCase() + "'></td></tr>";
	str = str + "<tr><td class='label'>"+i18nObj.prop('msg.org.name')+": </td><td><input type='text' name='agentOrgId' id='agentOrgId' readonly='readonly' value='" + agentOrgId + "'><input type='hidden' name='agtOrgName' id='agtOrgName' readonly='readonly' value='" + agentOrgName + "'></td></tr>";
	
	if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.wallet.name')+": </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.wdl.profit.rate')+": </td><td><input type='text' name='defWidCommRate' id='defWidCommRate' readonly='readonly' value='" + rowCells[1].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.wdl.profit.variance')+": </td><td><input type='text' name='widCommVar' id='widCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].textContent + "'  onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.dep.profit.rate')+": </td><td><input type='text'  name='defDepCommRate' id='defDepCommRate' readonly='readonly' value='" + rowCells[4].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.dep.profit.variance')+": </td><td><input type='text' name='depCommVar' id='depCommVar' style='background-color: #F9FCCD' value='" + rowCells[5].textContent + "' onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.net.game.profit.rate')+": </td><td><input type='text'  name='defNetGCommRate' id='defNetGCommRate' readonly='readonly' value='" + rowCells[7].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.net.game.profit.variance')+": </td><td><input type='text' name='netGCommVar' id='netGCommVar' style='background-color: #F9FCCD' value='" + rowCells[8].textContent + "' onclick='setBlank(this.id)'></td></tr>";
	}else{
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.wallet.name')+": </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.wdl.profit.rate')+": </td><td><input type='text' name='defWidCommRate' id='defWidCommRate' readonly='readonly' value='" + rowCells[1].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.wdl.profit.variance')+": </td><td><input type='text' name='widCommVar' id='widCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].innerText + "'  onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.dep.profit.rate')+": </td><td><input type='text'  name='defDepCommRate' id='defDepCommRate' readonly='readonly' value='" + rowCells[4].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.dep.profit.rate')+": </td><td><input type='text' name='depCommVar' id='depCommVar' style='background-color: #F9FCCD' value='" + rowCells[5].innerText + "' onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.net.game.profit.rate')+"Default Net Gaming Profit Rate: </td><td><input type='text'  name='defNetGCommRate' id='defNetGCommRate' readonly='readonly' value='" + rowCells[7].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.net.game.profit.rate')+": </td><td><input type='text' name='netGCommVar' id='netGCommVar' style='background-color: #F9FCCD' value='" + rowCells[8].innerText + "' onclick='setBlank(this.id)'></td></tr>";
	}
	//alert(rowCells[5].innerText);
	str = str + "<tr><td>&nbsp;</td> <td align='left'><input type='button' value="+i18nObj.prop('label.update')+"\tid='submitbutoon2'    class='button'  \tname='button'  onclick='submitUpdatedValueOLA()'  /></td><tr>";
	tablestr = tablestr + str + "</table>";	
	//alert(tablestr);
	return tablestr;
}

function createTableForVar(rowCells) {
	disableAllInputFields();
	var str = "<h3>"+i18nObj.prop('label.sale.pwt.rate.to.update')+"</h3>";
	var tablestr = "<table align='left' id='updateTable'>";
	var serviceName = document.getElementById("serviceName");
	serviceName = serviceName[serviceName.selectedIndex].text;
	var orgType = document.getElementById("orgType");
	orgType = orgType.value;
	//orgType = orgType[orgType.selectedIndex].text;
	//alert("****"+orgType+"******");
	var agentSelect;
	if (orgType == "AGENT") {
		agentSelect = document.getElementById("agentOrgName");
	} else {
		if (orgType == "RETAILER") {
			agentSelect = document.getElementById("retailerOrgName");
		}
	}
	agentOrgName = agentSelect[agentSelect.selectedIndex].value;
	var agentOrgId = agentSelect[agentSelect.selectedIndex].text;
	//alert("bb");
	//alert("agent Org Name "+agentOrgName+"agentOrgId"+agentOrgId);
	str = str + "<tr><td class='label'>"+i18nObj.prop('label.org.type')+": </td><td><input type='text' readonly='readonly' value='" + tierMap[orgType].toUpperCase() + "'></td></tr>";
	str = str + "<tr><td class='label'>"+i18nObj.prop('msg.org.name')+": </td><td><input type='text' name='agentOrgId' id='agentOrgId' readonly='readonly' value='" + agentOrgId + "'><input type='hidden' name='agtOrgName' id='agtOrgName' readonly='readonly' value='" + agentOrgName + "'></td></tr>";
	
	if(BrowserDetect.browser=="Safari" || BrowserDetect.browser=="Firefox" || BrowserDetect.browser=="Chrome"){
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.game.name')+": </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.sale.profit.rate')+": </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='" + rowCells[1].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.sale.profit.variance')+": </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].textContent + "'  onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.pwt.profit.rate')+": </td><td><input type='text'  name='defPwtCommRate' id='defPwtCommRate' readonly='readonly' value='" + rowCells[4].textContent + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.pwt.profit.rate')+": </td><td><input type='text' name='pwtCommVar' id='pwtCommVar' style='background-color: #F9FCCD' value='" + rowCells[5].textContent + "' onclick='setBlank(this.id)'></td></tr>";
	}else{
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.game.name')+": </td><td><input type='text' name='gname' readonly='readonly' id='gname' value='" + rowCells[0].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.sale.profit.rate')+": </td><td><input type='text' name='defSaleCommRate' id='defSaleCommRate' readonly='readonly' value='" + rowCells[1].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.sale.profit.variance')+": </td><td><input type='text' name='saleCommVar' id='saleCommVar' style='background-color: #F9FCCD' value='" + rowCells[2].innerText + "'  onclick='setBlank(this.id)'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.def.pwt.profit.rate')+": </td><td><input type='text'  name='defPwtCommRate' id='defPwtCommRate' readonly='readonly' value='" + rowCells[4].innerText + "'></td></tr>";
		str = str + "<tr><td class='label'>"+i18nObj.prop('label.pwt.profit.rate')+": </td><td><input type='text' name='pwtCommVar' id='pwtCommVar' style='background-color: #F9FCCD' value='" + rowCells[5].innerText + "' onclick='setBlank(this.id)'></td></tr>";
	}
	//alert(rowCells[5].innerText);
	str = str + "<tr><td>&nbsp;</td> <td align='left'><input type='button' value="+i18nObj.prop('label.update')+"\tid='submitbutoon2'    class='button'  \tname='button'  onclick='submitUpdatedValue()'  /></td><tr>";
	tablestr = tablestr + str + "</table>";	
	//alert(tablestr);
	return tablestr;
}
function submitUpdatedAllValue() {
	//alert("111111111");
	var agtOrgName = document.getElementById("agentOrgName").value;
	//alert("2222222222"+agtOrgName);
	agtOrgName = convert(agtOrgName);
	var gameStatus = document.getElementById("gameStatus").value;	
	//alert("33333333333 "+gameStatus);
	var saleCommVar = document.getElementById("allSaleCommRate").value;
	saleCommVar = saleCommVar.replace(" ", "");
	var pwtCommVar = document.getElementById("allPwtCommRate").value;
	pwtCommVar = pwtCommVar.replace(" ", "");
	var sflag = isDecimal(saleCommVar);
	var pflag = isDecimal(pwtCommVar);
	if (pflag && (pwtCommVar > 100 || pwtCommVar < 0)) {
		alert(i18nObj.prop('error.pwt.profitVar.must.be.less.than.hundred.per.or.gt.equal.zero')+"%");
		document.getElementById("allPwtCommRate").focus();
	}
	if (sflag) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
		document.getElementById("allSaleCommRate").focus();
	} else {
		if (pflag) {
			alert(i18nObj.prop('error.enter.valid.decimal.nbr.pwt.profitVar'));
			document.getElementById("allPwtCommRate").focus();
		} else {
			var newUrl = "submitUpdatedAllValue.action?agtOrgName=" + agtOrgName + "&gameStatus=" + gameStatus + "&saleCommVar=" + saleCommVar + "&pwtCommVar=" + pwtCommVar;
			mainDiv = "all";
			_resp = _ajaxCall(newUrl);
			if (_resp.result) {
				var resText = _resp.data;
				if (resText == "successfully updated") {
					updateTableColumn("gameDetails", "sale", document.getElementById("allSaleCommRate").value);
					updateTableColumn("gameDetails", "pwt", document.getElementById("allPwtCommRate").value);
					document.getElementById(mainDiv).innerHTML = resText;
				} else {
					if (resText == i18nObj.prop('error.some.internal.error.occured')) {
						document.getElementById(mainDiv).innerHTML = resText;
					} else {
						if (resText == i18nObj.prop('error.you.must.update.sale.pwt.commVar.val')) {
							alert(i18nObj.prop('error.you.must.update.sale.pwt.profitVar.val'));
							document.getElementById("allSaleCommRate").focus();
						} else {
							alert(resText);
							document.getElementById("allSaleCommRate").focus();
						}
					}
				}
			} else {
				alert(i18nObj.prop('error.problem')+"------");
			}
		}
	}
}
function convert(urlX) {
	var exp = /&/g;
	return urlX.replace(exp, "amp");
}
var mainDiv;
var req;
var which;
var gameName;

function submitUpdatedValue() {
	//alert(1);
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
	//alert(defSaleCommRate + "       " + saleCommVar+ "    "+"   "+defPwtCommRate+"  "+pwtCommVar);
	var diff = parseFloat(defSaleCommRate) + parseFloat(saleCommVar);
	//alert(diff);
	if (isNaN(saleCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return false;
	}
	if(!validateAmount(saleCommVar) || !validateAmount(pwtCommVar)){
		
		return false;
	}
	if (diff < 0) {
		alert(i18nObj.prop('error.calc.net.profitVar.negative.enter.another.valIn.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return sflag;
	}
	//alert("3333333333");
	var pwtdiff = parseFloat(defPwtCommRate) + parseFloat(pwtCommVar);
	if ((parseFloat(pwtCommVar) > 100 || pwtdiff < 0)) {
		alert(i18nObj.prop('error.pwt.profitVar.must.be.less.than.hundred.per.or.gt.equal.zero')+"%");
		document.getElementById("pwtCommVar").focus();
		return false;
	}
	if (pflag || isNaN(pwtCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.pwt.profitVar'));
		document.getElementById("pwtCommVar").focus();
		return false;
	} else {
		if (sflag) {
			alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
			document.getElementById("saleCommVar").focus();
			return sflag;
		} else {	
	//alert(" game type value is == "+gameTypeValue);
	//alert("444444444");
			//alert(orgType);
			var newUrl = "";
			if (orgType == "AGENT") {
				newUrl = "bo_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
			} else {
				if (orgType == "RETAILER") {
					newUrl = "agent_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
				}
			}
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
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return false;
	}
	if(!validateAmount(saleCommVar)){
		return false;
	}
	if (diff < 0) {
		alert(i18nObj.prop('error.calc.net.profitVar.negative.enter.another.valIn.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return sflag;
	}
	if (sflag){
			alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
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
				req = new XMLHttpRequest();
				req.onreadystatechange = processStateChangeCS;
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

function submitUpdatedValueOLA(){
	var serviceName = document.getElementById("serviceName").value;
	var orgType = document.getElementById("orgType").value;
	var agtOrgName = document.getElementById("agtOrgName").value;
	agtOrgName = convert(agtOrgName);
	var gname = document.getElementById("gname").value;
	gameName = gname;
	var defWidCommRate = document.getElementById("defWidCommRate").value;
	var widCommVar = document.getElementById("widCommVar").value;
	widCommVar = widCommVar.replace(" ", "");

	var defDepCommRate = document.getElementById("defDepCommRate").value;
	var depCommVar = document.getElementById("depCommVar").value;
	depCommVar = depCommVar.replace(" ", "");
	
	var defNetGCommRate = document.getElementById("defNetGCommRate").value;
	var netGCommVar = document.getElementById("netGCommVar").value;
	netGCommVar = netGCommVar.replace(" ", "");

	var wflag = isDecimal(widCommVar);
	var dflag = isDecimal(depCommVar);
	var nflag = isDecimal(netGCommVar);
	
	var widDiff = parseFloat(defWidCommRate) + parseFloat(widCommVar);
	if ((parseFloat(widCommVar) > 100 || widDiff < 0)) {
		alert(i18nObj.prop('error.wdl.profitVar.must.be.less.than.hundred.per.or.gt.equal.zero')+"%");
		document.getElementById("widCommVar").focus();
		return false;
	}
	if (wflag || isNaN(widCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.wdl.profitVar'));
		document.getElementById("widCommVar").focus();
		return false;
	}
	if(!validateAmount(widCommVar) || !validateAmount(depCommVar) || !validateAmount(netGCommVar)){
		
		return false;
	}
	if (widDiff < 0) {
		alert(i18nObj.prop('error.calc.net.profitVar.negative.enter.another.valIn.wdl.profitVar'));
		document.getElementById("widCommVar").focus();
		return wflag;
	}

	var depDiff = parseFloat(defDepCommRate) + parseFloat(depCommVar);
	if ((parseFloat(depCommVar) > 100 || depDiff < 0)) {
		alert(i18nObj.prop('error.depo.profitVar.must.be.less.than.hundred.per.or.gt.equal.zero')+"%");
		document.getElementById("depCommVar").focus();
		return false;
	}
	if (dflag || isNaN(depCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.depo.profitVar'));
		document.getElementById("depCommVar").focus();
		return false;
	} else {
		if (wflag) {
			alert(i18nObj.prop('error.enter.valid.decimal.nbr.wdl.profitVar'));
			document.getElementById("widCommVar").focus();
			return wflag;
		} else {	
			var newUrl = "";
			if (orgType == "AGENT") {
				newUrl = "bo_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defWidCommRate=" + defWidCommRate + "&widCommVar=" + widCommVar + "&defDepCommRate=" + defDepCommRate + "&depCommVar=" + depCommVar +  "&defNetGCommRate=" + defNetGCommRate + "&netGCommVar=" + netGCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
			} else {
				if (orgType == "RETAILER") {
					newUrl = "agent_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defWidCommRate=" + defWidCommRate + "&widCommVar=" + widCommVar + "&defDepCommRate=" + defDepCommRate + "&depCommVar=" + depCommVar + "&defNetGCommRate=" + defNetGCommRate + "&netGCommVar=" + netGCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
				}
			}
	//alert("new url is == "+newUrl);
			mainDiv = "updateGameDiv";
			var _c = "?";
			if (newUrl.indexOf("?") != -1) {
				_c = "&";
			}
			newUrl = newUrl + _c + new Date().getTime() + parent.frames[0].sesId + Math.random() * 1000000;
			if (window.XMLHttpRequest) { // Non-IE browsers
				req = new XMLHttpRequest();
				req.onreadystatechange = processStateChangeOLA;
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
						req.onreadystatechange = processStateChangeOLA;
						req.open("GET", newUrl, true);
						req.setRequestHeader("If-Modified-Since", new Date().getTime());// added later
						req.send();
					}
				}
			}
		}
	}
}

function submitUpdatedValueIW() {
	// alert(1);
	var serviceName = document.getElementById("serviceName").value;
	var orgType = document.getElementById("orgType").value;
	var agtOrgName = document.getElementById("agtOrgName").value;
	agtOrgName = convert(agtOrgName);
	var gname = document.getElementById("gname").value;
	gameName = gname;
	// alert(gname);
	var defSaleCommRate = document.getElementById("defSaleCommRate").value;
	var saleCommVar = document.getElementById("saleCommVar").value;
	saleCommVar = saleCommVar.replace(" ", "");
	// alert("11111111");
	var defPwtCommRate = document.getElementById("defPwtCommRate").value;
	// alert("1111111111aaaaaaaaaa");
	var pwtCommVar = document.getElementById("pwtCommVar").value;
	// alert("1111111111bbbbbbbbb");
	pwtCommVar = pwtCommVar.replace(" ", "");
	// alert("111111111111111111cccccccccc");
	var sflag = isDecimal(saleCommVar);
	// alert("11111111dddddddddd");
	var pflag = isDecimal(pwtCommVar);
	// alert(defSaleCommRate + " " + saleCommVar+ " "+" "+defPwtCommRate+"
	// "+pwtCommVar);
	var diff = parseFloat(defSaleCommRate) + parseFloat(saleCommVar);
	// alert(diff);
	if (isNaN(saleCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return false;
	}
	if (!validateAmount(saleCommVar) || !validateAmount(pwtCommVar)) {

		return false;
	}
	if (diff < 0) {
		alert(i18nObj
				.prop('error.calc.net.profitVar.negative.enter.another.valIn.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return sflag;
	}
	// alert("3333333333");
	var pwtdiff = parseFloat(defPwtCommRate) + parseFloat(pwtCommVar);
	if ((parseFloat(pwtCommVar) > 100 || pwtdiff < 0)) {
		alert(i18nObj.prop('error.pwt.profitVar.must.be.less.than.hundred.per.or.gt.equal.zero') + "%");
		document.getElementById("pwtCommVar").focus();
		return false;
	}
	if (pflag || isNaN(pwtCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.pwt.profitVar'));
		document.getElementById("pwtCommVar").focus();
		return false;
	} else {
		if (sflag) {
			alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
			document.getElementById("saleCommVar").focus();
			return sflag;
		} else {
			// alert(" game type value is == "+gameTypeValue);
			// alert("444444444");
			// alert(orgType);
			var newUrl = "";
			if (orgType == "AGENT") {
				newUrl = "bo_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
			} else {
				if (orgType == "RETAILER") {
					newUrl = "agent_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
				}
			}
			// alert("new url is == "+newUrl);
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
					req.setRequestHeader("If-Modified-Since", new Date().getTime());// added later
				} catch (e) {
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

function submitUpdatedValueVS() {
	// alert(1);
	var serviceName = document.getElementById("serviceName").value;
	var orgType = document.getElementById("orgType").value;
	var agtOrgName = document.getElementById("agtOrgName").value;
	agtOrgName = convert(agtOrgName);
	var gname = document.getElementById("gname").value;
	gameName = gname;
	// alert(gname);
	var defSaleCommRate = document.getElementById("defSaleCommRate").value;
	var saleCommVar = document.getElementById("saleCommVar").value;
	saleCommVar = saleCommVar.replace(" ", "");
	// alert("11111111");
	var defPwtCommRate = document.getElementById("defPwtCommRate").value;
	// alert("1111111111aaaaaaaaaa");
	var pwtCommVar = document.getElementById("pwtCommVar").value;
	// alert("1111111111bbbbbbbbb");
	pwtCommVar = pwtCommVar.replace(" ", "");
	// alert("111111111111111111cccccccccc");
	var sflag = isDecimal(saleCommVar);
	// alert("11111111dddddddddd");
	var pflag = isDecimal(pwtCommVar);
	// alert(defSaleCommRate + " " + saleCommVar+ " "+" "+defPwtCommRate+"
	// "+pwtCommVar);
	var diff = parseFloat(defSaleCommRate) + parseFloat(saleCommVar);
	// alert(diff);
	if (isNaN(saleCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return false;
	}
	if (!validateAmount(saleCommVar) || !validateAmount(pwtCommVar)) {
		return false;
	}
	if (diff < 0) {
		alert(i18nObj
				.prop('error.calc.net.profitVar.negative.enter.another.valIn.sale.profitVar'));
		document.getElementById("saleCommVar").focus();
		return sflag;
	}
	// alert("3333333333");
	var pwtdiff = parseFloat(defPwtCommRate) + parseFloat(pwtCommVar);
	if ((parseFloat(pwtCommVar) > 100 || pwtdiff < 0)) {
		alert(i18nObj.prop('error.pwt.profitVar.must.be.less.than.hundred.per.or.gt.equal.zero') + "%");
		document.getElementById("pwtCommVar").focus();
		return false;
	}
	if (pflag || isNaN(pwtCommVar)) {
		alert(i18nObj.prop('error.enter.valid.decimal.nbr.pwt.profitVar'));
		document.getElementById("pwtCommVar").focus();
		return false;
	} else {
		if (sflag) {
			alert(i18nObj.prop('error.enter.valid.decimal.nbr.sale.profitVar'));
			document.getElementById("saleCommVar").focus();
			return sflag;
		} else {
			// alert(" game type value is == "+gameTypeValue);
			// alert("444444444");
			// alert(orgType);
			var newUrl = "";
			if (orgType == "AGENT") {
				newUrl = "bo_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
			} else {
				if (orgType == "RETAILER") {
					newUrl = "agent_um_setSalePwtCommVar_Save.action?agtOrgName=" + agtOrgName + "&gname=" + gname + "&defSaleCommRate=" + defSaleCommRate + "&saleCommVar=" + saleCommVar + "&defPwtCommRate=" + defPwtCommRate + "&pwtCommVar=" + pwtCommVar + "&serviceName=" + serviceName + "&orgType=" + orgType;
				}
			}
			// alert("new url is == "+newUrl);
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
					req.setRequestHeader("If-Modified-Since", new Date().getTime());// added later
				} catch (e) {
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
    //  alert("received" +req.statusText);
    	//alert(req.responseText);
			if (req.responseText == "successfully updated") {
				updateSpecificRow("sale", document.getElementById("saleCommVar").value);
    			//alert("last ============ 11111111111111");
				updateSpecificRow("pwt", document.getElementById("pwtCommVar").value);
				//alert(req.responseText);
				document.getElementById("updateGameDiv").innerHTML = req.responseText;
			} else {
				if (req.responseText == i18nObj.prop('error.some.internal.error.occured')) {
					document.getElementById(mainDiv).innerHTML = req.responseText;
				} else {
					if (req.responseText == i18nObj.prop('error.you.must.update.sale.pwt.commVar.val')) {
						alert(i18nObj.prop('error.you.must.update.sale.pwt.profitVar.val'));
						document.getElementById("saleCommVar").focus();
					} else {
				//alert(req.responseText);
						var result = req.responseText;
						var str = result.split(";");
				//alert(str[0]=="SALE");
						if (str[0] == "SALE") {
							alert(i18nObj.prop('error.pbm.sale.profitVar.must.be.lt')+" " + str[1]);
							document.getElementById("saleCommVar").focus();
						} else {
							if (str[0] == "AGENT_DIFF_GREATER") {
								alert(i18nObj.prop('error.pbm.max.net.sale.profitVar.must.be')+" " + str[1] + " "+i18nObj.prop('error.and.max.net.pwt.profit.rate.should.be') + str[2]);
							} else {
								alert(i18nObj.prop('error.pbm.pwt.profitVar.must.be.lt')+" " + str[1]);
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
				if (req.responseText == i18nObj.prop('error.some.internal.error.occured')) {
					document.getElementById(mainDiv).innerHTML = req.responseText;
				} else {
					if (req.responseText == i18nObj.prop('error.you.must.update.sale.commVar.val')) {
						alert(i18nObj.prop('error.you.must.update.sale.profitVar.val'));
						document.getElementById("saleCommVar").focus();
					} else {
				//alert(req.responseText);
						var result = req.responseText;
						var str = result.split(";");
				//alert(str[0]=="SALE");
						if (str[0] == "SALE") {
							alert(i18nObj.prop('error.pbm.sale.profitVar.must.be.lt') +" "+ str[1]);
							document.getElementById("saleCommVar").focus();
						} else {
							if (str[0] == "AGENT_DIFF_GREATER") {
								alert(i18nObj.prop('error.pbm.max.net.sale.profitVar.must.be')+" " + str[1]);
							}
						}
					}
				}
			}
		} else {
			alert(i18nObj.prop('error.problem')+" " + req.statusText);
		}
	}
}

function processStateChangeOLA(){

	if (req.readyState == 4) {
		if (req.status == 200) {
			if (req.responseText == "successfully updated") {
				updateSpecificRow("wid", document.getElementById("widCommVar").value);
				updateSpecificRow("dep", document.getElementById("depCommVar").value);
				updateSpecificRow("netG", document.getElementById("netGCommVar").value);
				document.getElementById("updateGameDiv").innerHTML = req.responseText;
			} else {
				if (req.responseText == i18nObj.prop('error.some.internal.error.occured')) {
					document.getElementById(mainDiv).innerHTML = req.responseText;
				} else {
					if (req.responseText == i18nObj.prop('error.you.must.update.any.one.commVar.val')) {
						alert(i18nObj.prop('error.you.must.update.any.one.profitVar.val'));
						document.getElementById("widCommVar").focus();
					} else {
				//alert(req.responseText);
						var result = req.responseText;
						var str = result.split(";");
				//alert(str[0]=="SALE");
						if (str[0] == "WITHDRAWAL") {
							alert(i18nObj.prop('error.pbm.wdl.profitVar.must.be.lt')+" " + str[1]);
							document.getElementById("widCommVar").focus();
						} else if(str[0] == "DEPOSIT"){
							alert(i18nObj.prop('error.pbm.depo.profitVar.must.be.lt')+" " + str[1]);
							document.getElementById("depCommVar").focus();
						} else if(str[0] == "NET GAMING"){
							alert(i18nObj.prop('error.pbm.net.game.profitVar.must.be.lt')+" " + str[1]);
							document.getElementById("netGCommVar").focus();
						} else{
							if (str[0] == "AGENT_DIFF_GREATER") {
								alert(i18nObj.prop('error.pbm.max.net.wdl.profitVar.should.be')+" " + str[1] 
								      +i18nObj.prop('error.and.max.net.depo.profit.rate.should.be') +" " + str[2]
								      + i18nObj.prop('error.and.max.net.game.profit.rate.should.be')+" " + str[3]);
							}
						}
					}
				}
			}
		} else {
			alert(i18nObj.prop('error.problem')+" " + req.statusText);
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
	if (val == "DG" || val == 'CS' || val == 'OLA') {
		_id.o("gameStatus").disabled = true;
	} else {
		if (val == "SE") {
			_id.o("gameStatus").disabled = false;
		}
	}
}
function hideRetailerStatus(val) {
	if (val == "AGENT") {
		_id.o("retailerOrgName").disabled = true;
	} else {
		if (val == "RETAILER") {
			_id.o("retailerOrgName").disabled = false;
		}
	}
}
function validateAmount(amt){
	var splitAmtArr = amt.split('.');
	if(splitAmtArr.length > 1){
		if(splitAmtArr[1].length > 2){
			alert(i18nObj.prop('error.enter.amt.upto.two.decimal.point.only'));
			return false;
		}
	}
	return true;
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/salePwtCommVar.js,v $"] = "$Id: salePwtCommVar.js,v 1.4 2016/10/31 09:46:23 neeraj Exp $";

