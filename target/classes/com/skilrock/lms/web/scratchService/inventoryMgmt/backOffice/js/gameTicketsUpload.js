
function retrieveURL(urlY, div) {		
			//alert(div);
	mainDiv = document.getElementById(div);
		 	//alert(mainDiv);	     
	var url = urlY;
	_resp = _ajaxCall(url);
	if (_resp.result) {
	} else {
		alert("Problem: ");
	}
}


function addPack() {
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		var e = document.forms[0].elements[i];
		if (e.name == "packNumber") {
			if (e.value == "") {
				pack = 1;
				return false;
			}
		}
	}
	return true;
}


function addPackToTable(divToTable) {
			  //alert("inside the add row function");
			  //alert(divToTable);
	var multiplierDiv;
	multiplierDiv = "packSeries";
	var tbl = document.getElementById(divToTable);
			  //alert(tbl);
	var lastRow = tbl.rows.length;
			  //alert("lastRow "+lastRow);
	var iteration = parseInt(lastRow);
			  //alert("Row added at no.  "+iteration);
	var row = tbl.insertRow(lastRow);
	var cellLeft = row.insertCell(0);
	var newdiv = document.createElement("div");
	newdiv.setAttribute("id", multiplierDiv + "" + iteration);
	newdiv.innerHTML = "<table border=\"0\" width=\"100%\"><tr><td width=\"10%\"><i>From</i></td><td width=\"15%\"><input type=\"text\" size = \"10\" name=\"packNumberFrom\" id=\"pnumfrom" + iteration + "\" onkeypress=\"return isValidEntries()\" onblur=\"validateFromTo(this.id)\" onchange=\"disableSubmit()\"></input></td><td width=\"5%\"><i>To</i></td><td width=\"15%\"><input type=\"text\" size = \"10\" name=\"packNumberTo\" id=\"pnumto" + iteration + "\" onkeypress=\"return isValidEntries()\" onblur=\"validateFromTo(this.id)\"  onchange=\"disableSubmit()\"></input></td><td width=\"10%\"><div id=\"totpk" + iteration + "\"><input type=text size = \"1\" id=\"totpkVal" + iteration + "\" name=\"totpkVal\" readonly=\"true\"/></div></td><td align=\"center\" width=\"15%\"><input type=button id = \"" + multiplierDiv + "" + iteration + "\" name=\"remove\" value=\"Remove\" onclick = \"removeRowFromTable(this.id)\"/></td><td width=\"30%\"><div id=\"error" + iteration + "\">&nbsp;</div></td></tr></table>";
			  //alert("333");
	cellLeft.appendChild(newdiv);
	document.getElementById("addPack").disabled = true;  
			  //alert(newdiv.innerHTML);
			  //alert("exit form add row function");
	return true;
}


function removeRowFromTable(id) {
	var countError = 0;
		
			//alert( document.getElementById(id));
	          //alert(id);	
	if (id.match("error0")) {
	          //alert("error0");
		flag = true;
		document.getElementById(id).innerHTML = "";
	}
	document.getElementById(id).innerHTML = "";
	var multiplierDiv;
	multiplierDiv = "err";
	var tableId = document.getElementById("addrowtb1");
		//alert("table id"+tableId);
	var lastRow = tableId.rows.length;
	var iteration = parseInt(lastRow);
	 //alert("Row added at no.  "+iteration);
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		var j = i + 1;
		var e = document.forms[0].elements[i];
		if (e.name == "err") {
				//alert("error id " +e.id);
			document.getElementById("againVerify").innerHTML = "<font color='red'>Please Verify Again</font>";
				//var errorId=document.getElementById("error"+j);
				//alert("ERROR ID "+errorId);
				
				//errorId.innerHTML="";
			var yyy = e.value;
			//alert("value of yyy"+yyy);
			if (yyy.match("Pack Series Is Invalid") || !(yyy.match("Pack Series Is Valid"))) {
				countError++;
				//alert("Pack Series Is Invalid");
				break;
			} else {
				 
					//alert("Pack Series Is valid");
			}
		}
	}
	document.getElementById("subButton").disabled = true;
}


function isValidEntries(evt) {
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if ((charCode >= 48 && charCode < 58) || charCode == 45) {
		return true;
	}
	return false;
}


function check() {
	var tbl;
	tbl = document.getElementById("ad");
	if (document.forms[0].gameName.value == "Please Select") {
			
				//document.forms[0].gameName.focus();
		tbl.disabled = true;
		return false;
	} else {
		if (document.forms[0].supplier_Name.value == "Please Select") {
			
				//document.forms[0].supplier_Name.focus();
			tbl.disabled = true;
			return false;
		}
	}
	tbl.disabled = false;
}	

  	
		//added by hanu
function change() {
		
		//alert();
		//document.forms[0].gameName.value="Please Select"
}


function load() {
	var count = 0;
	document.forms[0].gameName.focus();
	tbl = document.getElementById("ad");
	if (document.forms[0].gameName.value != "Please Select") {
		if (document.forms[0].supplier_Name.value != "Please Select") {
			tbl.disabled = false;
			document.getElementById("addPack").disabled = false;
			document.getElementById("verify").disabled = false;
			document.getElementById("subButton").disabled = false;
		} else {
			tbl.disabled = true;
			document.getElementById("addPack").disabled = true;
			document.getElementById("verify").disabled = true;
			document.getElementById("subButton").disabled = true;
		}
	} else {
		document.forms[0].gameName.focus();
		tbl.disabled = true;
		document.getElementById("addPack").disabled = true;
		document.getElementById("verify").disabled = true;
		document.getElementById("subButton").disabled = true;
	}
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		flag = true;
		var e = document.forms[0].elements[i];
		if (e.name == "gameType") {
			if (e.checked) {
		//alert(e.value);	
				retrieveURL("TicketsUploadGameNameAjax.action?gameType=" + e.value, "game");
			}
		}
		if (e.name == "err") {
			var errorValue = e.value;
	//alert(e.value);
			if (e.value == "true") {
		//alert();
				var errId = e.id;
				var errVal = errId.substring(errId.indexOf("err") + 3, errId.length);
				var totpkval = "totpkVal" + errVal;
				var from = "pnumfrom" + errVal;
				var to = "pnumto" + errVal;
				var div = "totpk" + errVal;
				var fromVal = document.getElementById(from).value;
				var toVal = document.getElementById(to).value;
				var noOfPackFrm = fromVal.substring(fromVal.indexOf("-") + 1, fromVal.length);
				var noOfPackTo = toVal.substring(toVal.indexOf("-") + 1, toVal.length);
				var noOfPack = parseInt(noOfPackTo, 10) - parseInt(noOfPackFrm, 10) + 1;
				document.getElementById(totpkval).value = noOfPack;
				document.forms[0].grandTotpk.value = parseInt(document.forms[0].grandTotpk.value, 10) + parseInt(noOfPack, 10);
			} else {
				document.getElementById("subButton").disabled = true;
			}
			if (errorValue.match("Pack Series Is Invalid") || !errorValue.match("Pack Series Is Valid")) {
					// alert("valid series");
				count++;
			}
		}
	}
	if (!parseInt(count) > 0) {
		// alert("valid flag");
		document.getElementById("subButton").disabled = false;
	}
}


function validateFromTo(id) {
	//alert(id+""+id.length);
	var val;
	if (id.length > 8) {
		val = id.substring(id.indexOf("pnumfrom") + 8, id.length);
	} else {
		val = id.substring(id.indexOf("pnumto") + 6, id.length);
	}
	var from = "pnumfrom" + val;
	var to = "pnumto" + val;
	var div = "totpk" + val;
	if (document.getElementById(from).value != "") {
		if (document.getElementById(to).value != "") {
			var fromVal = document.getElementById(from).value;
			var toVal = document.getElementById(to).value;
			if (fromVal.match("-") != null) {
				if (toVal.match("-") != null) {
					var noOfPackFrm = fromVal.substring(fromVal.indexOf("-") + 1, fromVal.length);
					var noOfPackTo = toVal.substring(toVal.indexOf("-") + 1, toVal.length);
					if (parseInt(noOfPackTo, 10) < parseInt(noOfPackFrm, 10)) {
						document.getElementById(div).innerHTML = "From Greater";
						document.getElementById("addPack").disabled = true;
						document.getElementById("verify").disabled = true;
					} else {
						var noOfPack = parseInt(noOfPackTo, 10) - parseInt(noOfPackFrm, 10) + 1;
						if (noOfPack > 0) {
							document.getElementById(div).innerHTML = "<input type=text size=1 name='totalpack' value=" + noOfPack + ">";
							document.getElementById("addPack").disabled = false;
							document.getElementById("verify").disabled = false;
						} else {
							document.getElementById(div).innerHTML = "please check entries";
							document.getElementById("addPack").disabled = true;
							document.getElementById("verify").disabled = true;
						}
					}
				}
			} else {
				document.getElementById(div).innerHTML = "Wrong";
				document.getElementById("addPack").disabled = true;
				document.getElementById("verify").disabled = true;
			}
		} else {
			document.getElementById(div).innerHTML = "Wrong";
			document.getElementById("addPack").disabled = true;
			document.getElementById("verify").disabled = true;
		}
	} else {
		document.getElementById(div).innerHTML = "Wrong";
		document.getElementById("addPack").disabled = true;
		document.getElementById("verify").disabled = true;
	}
}


function ab() {
		//alert(document.getElementById("err0").value);
}


function disableSubmit() {
	document.getElementById("subButton").disabled = true;
}


function isEmpty() {
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		var e = document.forms[0].elements[i];
		if (e.value == "") {
			document.getElementById("fill").innerHTML = "<font color=\"red\">Please fill all the fields</font>";
			return false;
		} else {
			document.getElementById("fill").innerHTML = "";
		}
	}
	var count = 0;
	var packArray = new Array();
	var packArray2 = new Array();
	var packnumberfrom;
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		var pack = document.forms[0].elements[i];
		if (pack.name.match("packNumberFrom")) {
			packArray2 = pack.value.split("-");
			packnumberfrom = packArray2[1];
			  //alert("inside from" + packnumberfrom);
		}
		if (pack.name.match("packNumberTo")) {
			   //alert("inside to");
			packArray2 = pack.value.split("-");
			var packnumberTo = packArray2[1];
			   //alert("inside to"+ packnumberTo);
			   //alert("from is "+ packnumberfrom);
			for (var j = parseInt(packnumberfrom, 10); j <= parseInt(packnumberTo, 10); j++) {
			    
			    //alert("----"+j);
				packArray[count] = j;
				count++;
			    //alert("array is " + packArray);
			}
		}
	}
	var packArraySize = packArray.length;
			//alert(packArraySize)
	for (var k = 0; k <= parseInt(packArraySize); k++) {
		for (var D = (parseInt(k) + 1); D <= parseInt(packArraySize); D++) {
			if (packArray[k] == packArray[D]) {
			    // alert("duplicate entries");
				document.getElementById("fill").innerHTML = "<font color=\"red\">Please Remove the duplicate Entries for Book Number</font>";
				return false;
			}
		}
		document.getElementById("fill").innerHTML = "";
	}
	return true;
}
		
	
		//yogesh
function reloadOriginal() {
		//alert("hello");
	document.getElementById("addPack").disabled = true;
	document.getElementById("verify").disabled = true;
	document.getElementById("subButton").disabled = true;
	document.getElementById("pnumfrom0").value = "";
	document.getElementById("pnumto0").value = "";
	document.getElementById("totpk0").innerHTML = "";
	var tableId = document.getElementById("addrowtb1");
	var lastRow = tableId.rows.length;
		//alert("total rows"+lastRow);
	var packRowId;
	var multiplierDiv;
	multiplierDiv = "packSeries";
			  //alert("lastRow "+lastRow);
	var iteration = parseInt(lastRow);
	for (var a = iteration; a > 0; a--) {
		//alert("removing div"+multiplierDiv+""+a);
		if (document.getElementById(multiplierDiv + "" + a) != null) {
			document.getElementById(multiplierDiv + "" + a).innerHTML = "";
		}
	}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/gameTicketsUpload.js,v $'] = "$Id: gameTicketsUpload.js,v 1.1 2010/04/02 12:12:07 gaurav Exp $";

