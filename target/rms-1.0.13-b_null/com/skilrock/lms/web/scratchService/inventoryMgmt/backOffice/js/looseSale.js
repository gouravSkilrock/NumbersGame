var _vf=false;
function getUserName() {
	var option = _id.o("userName");
	var gameSelBox = _id.o('gameNbr_NameHid');
	var data = "";
	var data1 = "";
	data = (document.getElementById('userData').value).replace("{", "")
			.replace("}", "").replace(/ /g, "");
	data1 = document.getElementById('gameData').value;
	var userArr = data.split(",");
	var gameArr = data1.split('Nx*');
	var UserNameMap;
	var gameNameMap;
	for ( var i = 0; i < userArr.length; i++) {
		UserNameMap = new Object();
		var userDetailsArr = userArr[i].split("=");
		UserNameMap['userId'] = userDetailsArr[0];
		UserNameMap['userName'] = userDetailsArr[1];

		var gkey = userDetailsArr[0] + ':' + userDetailsArr[1];
		UserNameMap[gkey] = UserNameMap;

		var dvalue = userDetailsArr[1];

		var opt = new Option(dvalue, gkey);
		option.options[i + 1] = opt;

	}
	for ( var j = 0; j < gameArr.length - 1; j++) {
		gameNameMap = new Object();
		var gameDetailsArr = gameArr[j].split(":");
		gameNameMap['gameId'] = gameDetailsArr[0];
		gameNameMap['gameName'] = gameDetailsArr[1];

		var gkey1 = gameDetailsArr[0] + ':' + gameDetailsArr[1];
		gameNameMap[gkey1] = gameNameMap;

		var dvalue1 = gameDetailsArr[1];

		var opt1 = new Option(dvalue1, gkey1);
		gameSelBox.options[j + 1] = opt1;

	}

}
function validateUser() {
	var _resp = false;
	// alert(count);
	var userName = _id.v("userName");

	if (userName == -1) {
		_id.i("userName_e", "Please enter the user Name", "e");
		return false;
	} else {
		_id.i("userName_e", "");
		_resp = true;
	}
	var coun = 0;
	var gameCellNumber = new Array();
	for ( var i = 0; i < count; i++) {
		var gameName = document.getElementById('gameSel_' + i + '').value;
		if (gameName.split('-')[1] == 1)
		coun++;
		else
			gameCellNumber.push(i);
		document.getElementById('totAmount_' + i + '').focus();
		var numberOfTickets = document.getElementById('NumTickets_' + i + '').value;
		var ticketComm = document.getElementById('ticketComm_' + i + '').value;
		var ticketAmt = document.getElementById('ticketAmt_' + i + '').value;
		var totAmt = numberOfTickets * ticketAmt * (1-(ticketComm/ 100));
		document.getElementById('totAmount_' + i + '').value = totAmt;
	}
	if (coun == count) {
		_id.i("game_e", "Please Select atleast one Game", "e");
		_resp = false;
	}
	else
	{
		_id.i("game_e","");
	}
	var numOfTickets = 0;
	var ticketCommission = 0;
	if (coun < count) {
		_id.i("game_e", "");
		for ( var k = 0; k < gameCellNumber.length; k++) {
			numOfTickets = document.getElementById('NumTickets_' + gameCellNumber[k] + '').value;
			ticketCommission = document.getElementById('ticketComm_' + gameCellNumber[k] + '').value;
			if (numOfTickets == 0) {
				_id.i("game_e", "Please Enter Tickets", "e");
				return false;
			} else {
				_id.i("game_e", "");
				_resp = true;
			}
			if (isNaN(ticketCommission)) {
				_id.i("game_e", "Please enter valid Commission amount", "e");
				return false;
			} else {
				_id.i("game_e", "");
				_resp = true;
			}
	}
		
	}
	
	if(_resp)
	{
	var flag = confirm("Do you want to proceed ? ");
	if(!flag)
	{
		_resp = false;
	}
	}
	return _resp;
}
var count = 0, gmNoLnth = 0;
gmInSelBx = 0;
function appendGameTab() {
	var numOfGame = _id.v('nog');
	gmInSelBx = _id.o('gameNbr_NameHid').options.length - 1;
	if (numOfGame > gmInSelBx) {
		gmNoLnth = gmInSelBx;
		document.getElementsByName('addGame')[0].style.visibility = "hidden";
		if (gmNoLnth < 1)
			_id
					.i(
							'top_form',
							'<h3>' + document.getElementsByTagName('h3')[0].innerHTML + '</h3><br/><center>No Game Available for Sale</center>');
	} else {
		gmNoLnth = numOfGame;
	}
	for ( var i = 0; i < gmNoLnth; i++) {
		count = i;
		addAnotherGame();
	}
}
function addAnotherGame() {
	if (count < gmInSelBx) {
		var table = _id.o("gameTab");
		var lastRow = table.rows.length;
		var row = table.insertRow(lastRow);
		var cellLeft = row.insertCell(0);
		var html = "";
		var tdHtmlData = '<tr><td><div id="gamemessage'
				+ count
				+ '"></div><table id="gameBookTable'
				+ count
				+ '" cellpadding=0" cellspacing="0" border="0" bordercolor="#CCCCCC" width="800"><tr><td colspan="0"><i>Game Number:</i><select name="gameName" id="gameSel_'
				+ count
				+ '" class="option"  onChange="fillComm(this.id)" onBlur="chkDupGame(this.id)"><option value="-1">--Please Select--</option></td>';
		tdHtmlData += '<td><table id="book_' + count + '" border="0" align="left"></table></td></tr></table></td></tr></br>'
		cellLeft.innerHTML = tdHtmlData;
		addBookRowStatic('book_' + count);
		fillSelect('gameSel_' + count);
		//altRowColor('gameBookTable' + count, count);
		count++;
	}
}
var arr = new Array();
function addBookRowStatic(buttonId) {

	arr = buttonId.split("_");
	var table = _id.o(buttonId);
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	var cellLeft = row.insertCell(0);
	var html = 'Number Of Tickets: <input type="text" name="NumTickets" id="NumTickets_'+ arr[1]
			+ '" size="7" autocomplete="off" onChange="setFocus(\'NumTickets_'
			+ arr[1]
			+ '\');" maxlength="6" onkeypress="validate(event,id)" onpaste="return false;"/>Ticket Amount: <input type="text" name="ticketAmt" id="ticketAmt_'
			+ arr[1]
			+ '" readonly="readonly" size="6"/>Ticket Commission: <input type="text" name="ticketComm" id="ticketComm_' + arr[1]
			+ '"  size="3" onBlur="setFocusComm(\'ticketComm_' + arr[1]
			+ '\');" maxlength="5" onkeypress="validateComm(event)" onpaste="return false;"/>Total Amount: <input type="text" name="totAmount" id="totAmount_' + arr[1] + '"  size="7" onChange="setFocus(\'totAmount_'
			+ arr[1]
					+ '\');" readonly="readonly"/>';
	cellLeft.innerHTML = html;

}

function chkDupGame(id) {

	var gameSel = document.getElementsByName("gameName");

	for ( var i = 0; i < gameSel.length; i++) {
		if (gameSel[i].selectedIndex != "0") {
			if (gameSel[i].value == _id.v(id) && gameSel[i].id != id) {
				//hidSubmit();
				alert('You have already selected the Game');
				gameSel[id.split('_')[1]].focus();
				_id.o(id).selectedIndex = 0;
				document.getElementById('ticketAmt_' + id.split('_')[1] + '').value = 0;
				document.getElementById('ticketComm_' + id.split('_')[1] + '').value = 0;
			}
		}
	}
}
var _vf = false;
function hidSubmit() {
	_vf = false;
	_id.hid('subTrans');
}
function fillSelect(selectBox) {
	var ajaxList = _id.o('gameNbr_NameHid');
	var newSelectbox = _id.o(selectBox);
	for (i = 0, l = ajaxList.options.length; i < l; i++) {
		if (i != 0) {
			var obj = ajaxList[i];
			var opt = new Option(obj.text, obj.value);
			newSelectbox.options[i] = opt;
		}
	}
}
function altRowColor(id, count) {
	var table = _id.o(id);
	if (count % 2 == 0) {
		table.className = "light_border_green";
		table.style.backgroundColor = "#F0FFF8";
	} else {
		table.className = "light_border_red";
		table.style.backgroundColor = "##FF0000";
	}
}
function setFocus(buttonId) {
	var arr = new Array();
	arr = buttonId.split("_");

	document.getElementById('ticketComm_' + arr[1] + '').focus();
	var numberOfTickets = document.getElementById('NumTickets_' + arr[1] + '').value;
	var ticketComm = document.getElementById('ticketComm_' + arr[1] + '').value;
	var ticketAmt = document.getElementById('ticketAmt_' + arr[1] + '').value;
	var totAmt = numberOfTickets * ticketAmt * (1-(ticketComm/ 100));
	document.getElementById('totAmount_' + arr[1] + '').value = totAmt;
}
function setFocusComm(buttonId) {
	var arr = new Array();
	arr = buttonId.split("_");
	document.getElementById('gameSel_' + (parseInt(arr[1])+1) + '').focus();
	var numberOfTickets = document.getElementById('NumTickets_' + arr[1] + '').value;
	var ticketComm = document.getElementById('ticketComm_' + arr[1] + '').value;
	var ticketAmt = document.getElementById('ticketAmt_' + arr[1] + '').value;
	var totAmt = numberOfTickets * ticketAmt * (1-(ticketComm/ 100));
	document.getElementById('totAmount_' + arr[1] + '').value = totAmt;
}
function validate(evt,id) {
	var gameId = document.getElementById('gameSel_' + id.split("_")[1] + '').value;
	  var theEvent = evt || window.event;
	  var key = theEvent.keyCode || theEvent.which;
	  key = String.fromCharCode( key );
	  if(gameId==-1)
	  {
		  _id.i("game_e", "Please Select Game", "e");
		  theEvent.returnValue = false;
   	  if(theEvent.preventDefault) theEvent.preventDefault();
	  }
	  else
	  {
		_id.i("game_e", "");
	  }
	  var regex = /[0-9]/;
	  if( !regex.test(key) ) {
	    theEvent.returnValue = false;
	    if(theEvent.preventDefault) theEvent.preventDefault();
	  }
	}
function validateComm(evt) {
	  var theEvent = evt || window.event;
	  var key = theEvent.keyCode || theEvent.which;
	  key = String.fromCharCode( key );
	  var regex = /[0-9]|\./;
	  if( !regex.test(key) ) {
	    theEvent.returnValue = false;
	    if(theEvent.preventDefault) theEvent.preventDefault();
	  }
	}
function resetFormData()
{
	for(var i=0;i<count;i++)
	{
	   document.getElementsByName('gameName')[i].value = -1;  
	   document.getElementsByName('NumTickets')[i].value = '';  
	   document.getElementsByName('ticketAmt')[i].value = '';  
	   document.getElementsByName('ticketComm')[i].value = '';  
	   document.getElementsByName('totAmount')[i].value = '';
	}  
}
function fillComm(id)
{
	var userName = _id.v("userName");
	if (userName == -1) {
		document.getElementById(id).value=-1;
		_id.i("userName_e", "Please enter the user Name", "e");
		return false;
	} else {
		_id.i("userName_e", "");
	}
	var gameNumber = document.getElementById(id).value.split(':');
	var data1 = "";
	var gameNameMap;
	data1 = document.getElementById('gameData').value;
	var gameArr = data1.split('Nx*');
	for ( var j = 0; j < gameArr.length - 1; j++) {
		gameNameMap = new Object();
		var gameDetailsArr = gameArr[j].split(":");
		if (gameDetailsArr[0] == gameNumber[0]) {
			url = "bo_im_looseSale_getComm.action?agtOrgId="+userName.split(":")[0]+"&gameId="+gameNumber[0];
			var _resp  = _ajaxCall(url);	
			if (_resp.result && _resp.data != "") {
		
			document.getElementById('ticketAmt_' + id.split('_')[1] + '').value = gameDetailsArr[2];
			document.getElementById('ticketComm_' + id.split('_')[1] + '').value = _resp.data;
		}
		}
	}

}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/looseSale.js,v $'] = '$Id: looseSale.js,v 1.2 2016/10/31 11:40:03 neeraj Exp $';