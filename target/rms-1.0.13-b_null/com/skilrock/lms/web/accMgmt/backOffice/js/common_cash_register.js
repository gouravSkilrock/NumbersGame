function fetchCashierList() {
	var url = "bo_act_cashier_list.action";
	var option = _id.o("cashierName");
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(",");
		for (var i = 0; i < data.length; i=i+1) {
			var cashierDetailsArr = data[i].split("=");
			var dvalue = cashierDetailsArr[1];
			var gkey = cashierDetailsArr[0];
			var opt = new Option(dvalue, gkey);
			option.options[i + 1] = opt;
		}
	}
}
function fetchDrawerList() {
	var option = _id.o("drawerName");
	var option1 = _id.o("drawerId");
	var url = "bo_act_drawer_list.action";
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(",");
		for (var i = 0; i < data.length; i=i+1) {
			var drawerDetailsArr = data[i].split("=");
			var dvalue = drawerDetailsArr[1];
			var gkey = drawerDetailsArr[0];
			var opt = new Option(dvalue, gkey);
			var opt1 = new Option(dvalue, gkey);
			option.options[i + 1] = opt;
			option1.options[i + 1] = opt1;
		}
	}
}
var flag1 = false;
function checkDrawerAvailability()
{		
	_id.i("assignDiv", "");
	_id.i("drawerName_e", "");
	var drawerId = _id.v("drawerName");
	var url = "bo_act_check_drawer_availablity.action?drawerName="+drawerId;
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(":");
		if(data[0] == 'ASSIGNED')
		{
			_id.i("drawerexist_e", "Drawer is already assigned to the cashier name "+data[1], "e");
			return false;
		}
		else{
			_id.i("drawerexist_e", "");
			return true;
		}
	}
}

function checkDrawerStatusBeforeClear()
{
	_id.i("clearDrawer_e", "");
	_id.i("clearDivSuccess","");
	var drawerId = _id.v("drawerId");
	var url = "bo_act_check_drawer_availablity.action?drawerName="+drawerId;
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(":");
		if(data[0] == 'ASSIGNED')
		{
			_id.i("clearDrawer_e", "Drawer is assigned to the cashier name "+data[1], "e");
			flag1 = true;
			return false;
		}
		else{
			_id.i("clearDrawer_e", "Drawer is empty.");
			return true;
		}
	}
}

function verifyAssignDrawee()
{
	var flag = true;
	flag1 = true;
	
	_id.i("drawerexist_e", "");
	var cashierName = _id.v("cashierName");
	var drawerName = _id.v("drawerName");
	
	if (cashierName == -1) {

		_id.i("cashierName_e", "Please Select the cashier", "e");
		flag = false;
	} else {
		_id.i("cashierName_e", "");
	}
	if (drawerName == -1) {

		_id.i("drawerName_e", "Please Select the Drawer", "e");
		flag = false;
	} else {
		_id.i("drawerName_e", "");
	}
	if(flag)
	{
		flag1 = checkDrawerAvailability();
	}
	if (flag) {
		if (flag1==false) {
			var flag2 = confirm("Do you want to change Drawer owner ? ");
			if (!flag2) {
				_id.i("assignDiv","");
				flag = false;
				_id.i("drawerexist_e", "");
			}
			else{
				_id.i("assignDiv","");
				_id.i("drawerexist_e", "");
			}
		}
	}
	if(flag)
	{
		document.getElementById("form2").reset();
	}
	return flag;
	
}
function isValidUName(val){
	var flag = true;
	 var regex=/^([A-Za-z0-9]){0,32}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}
function verifyCreateDrawer()
{
	var flag = true;
	_id.i("createDrawerName_e", "");
	var createDrawerName = _id.v("createDrawerName");
	if (createDrawerName == "") {

		_id.i("createDrawerName_e", "Please Enter the Drawer Name", "e");
		return false;
	} else {
		_id.i("createDrawerName_e", "");
	}
	if(!isValidUName(createDrawerName)){
		_id.i("createDrawerName_e", "Please Enter the valid Drawer Name", "e");
		flag = false;
	}
	else {
		_id.i("createDrawerName_e", "");
	}
if(flag==true)
{
	var url = "bo_act_check_drawer_existance.action?createDrawerName="+createDrawerName;
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		if(_respData == 'EXIST')
		{
			_id.i("createDivSuccess", "");
			_id.i("createDrawerName_e", "Drawer Name "+createDrawerName+" is already exist", "e");
			flag = false;
		}
		else{
			_id.i("createDrawerName_e", "");
			flag  =  true;
		}
	}
}
if(flag)
{
	document.getElementById("form1").reset();
}
	return flag;
}

var arr = new Array();
function appendDenoTab(mode)
{
	addRecArrayDenomination('');
		for ( var i = 0; i < arr.length; i++) {
			addReceivedDenomination(arr[i]);
		}
}
function addReceivedDenomination(deno){ 
	var div = document.getElementById("table");
	var row = document.createElement("tr");
	var col1=document.createElement("td");
	col1.setAttribute("style","color: navy");
	var col2=document.createElement("td");
	col1.setAttribute("align", "right");
	col1.setAttribute("width", "160");
	col2.setAttribute("width", "100");
	var	tdHtmlData1 = '<div style="background: transparent;border-width: 0px;color: navy;">'+deno+'   x </div>';
	//var	tdHtmlData1= '<input type="text" name="denoType" value="'+deno+'" size="3" style="background: transparent;border-width: 0px;color: navy;" readonly="readonly"  />x';
	var	tdHtmlData2= '<input type="text" name="multiples" id="multiples_'+deno+'" maxlength="10" size="6"  onkeypress="validateComm(event)" onBlur="setRecAmt(\'multiples_' + deno + '\');"></input>';
	col1.innerHTML=tdHtmlData1;
	col2.innerHTML=tdHtmlData2;
	row.appendChild(col1);
	row.appendChild(col2);
	div.appendChild(row);
	 
}
function setRecAmt(buttonId) {
	document.getElementById('totalAmt').value = 0;
	var totAmt = 0;
	for ( var i = 0; i < arr.length; i++) {
		var multiples = document.getElementById('multiples_' + arr[i] + '').value;
		if (multiples == "") {
			multiples = 0;
		}
		totAmt = parseInt(totAmt) + (arr[i] * multiples);
	}
	document.getElementById('totalAmt').value = totAmt;
}
function validateComm(evt) {
	  var theEvent = evt || window.event;
	  var key = theEvent.keyCode || theEvent.which;
	  if(key==13 || key==8 || key==9)
	  {
		  return true;
	  }
	  else{
	  key = String.fromCharCode( key );
	  var regex = /[0-9]|13/;
	  if( !regex.test(key) ) {
	    theEvent.returnValue = false;
	    alert("Please enter valid Entry");
	    if(theEvent.preventDefault) theEvent.preventDefault();
	  }
	  }
	}

function drawerMgmtTypeShow(obj) {
	var value = obj.value;
	document.forms[0].reset();

	if("ASSIGN" == value) {
		fetchCashierList();
		fetchDrawerList();

		_id.non('clearTab');
		_id.blk('asssignTab');
		_id.non('createTab');
		_id.non('clearDivSuccess');
		_id.non('createDivSuccess');
		_id.i("cashierName_e","");
		_id.i("drawerName_e","");
		_id.i("drawerexist_e", "");
		obj.checked = true;
		
	}
	if("CLEAR" == value) {
		fetchDrawerList();
		_id.i("clearDrawer_e","");
		_id.non('asssignTab');
		_id.blk('clearTab');
		_id.non('createTab');
		_id.non('assignDiv');
		_id.non('createDivSuccess');
	
		obj.checked = true;
	}
	if("CREATE" == value) {
		_id.i("createDrawerName_e","");
		_id.non('asssignTab');
		_id.non('clearTab');
		_id.blk('createTab');
		_id.non('assignDiv');
		_id.non('clearDivSuccess');
		
		obj.checked = true;
	}

}
function verifyClearDrawerData()
{
	var drawerId = _id.v("drawerId");
	var flag = true;
	if (drawerId == -1) {

		_id.i("clearDrawer_e", "Please Select the Drawer", "e");
		flag = false;
	} else {
		_id.i("clearDrawer_e", "");
	}
	if(flag)
	{
		document.getElementById("form1").reset();
	}
	return flag;
}
function addRecArrayDenomination(type) {
	arr = new Array();
	iArr = new Array();
	var url = "bo_act_add_RecArray_Denomination.action?type="+type;
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(",");
		for ( var i = 0; i < data.length; i = i + 1) {
			if(type!='EXCHANGE')
			arr.push(data[i]);
			else
			iArr.push(data[i]);	
		}
	}
}
function addExchangeArrayDenomination() {
	eArr = new Array();
	var type = "NOT_EXCHANGE";
	var url = "bo_act_add_RecArray_Denomination.action?type="+type;
	var _resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var data = _respData.split(",");
		for ( var i = 0; i < data.length; i = i + 1) {
			eArr.push(data[i]);	
		}
	}
}
var iArr = new Array();
var eArr = new Array();
function fillTables(){
	addRecArrayDenomination('EXCHANGE');
	addExchangeArrayDenomination();
	if(iArr.length!=0){
		for ( var i = 0; i < iArr.length; i++) {
			fillInitialTable(iArr[i]);
		}
		for ( var i = 0; i < eArr.length; i++) {
			fillExchangeTable(eArr[i]);
		}
	}
	else{
		_id.non('top_form');
		_id.i("errorDiv","Either denominations are not available or Drawer is not assigned");
	}
}
function fillInitialTable(deno){ 
	var div = document.getElementById("eTable");
	var row = document.createElement("tr");
	var col1=document.createElement("td");
	col1.setAttribute("style","color: navy");
	var col2=document.createElement("td");
	col1.setAttribute("align", "right");
	col1.setAttribute("width", "160");
	col2.setAttribute("width", "100");
//	var	tdHtmlData1 = '<div style="background: transparent;border-width: 0px;color: navy;">'+deno+'   x </div>';
	var	tdHtmlData1= '<input type="text" name="iDenoType" value="'+deno+'" size="3" style="background: transparent;border-width: 0px;color: navy;" readonly="readonly"  />x';
	var	tdHtmlData2= '<input type="text" name="iMultiples" id="iMultiples_'+deno+'" maxlength="10" size="6"  onkeypress="validateComm(event)"></input>';
	col1.innerHTML=tdHtmlData1;
	col2.innerHTML=tdHtmlData2;
	row.appendChild(col1);
	row.appendChild(col2);
	div.appendChild(row);
}

function fillExchangeTable(deno){ 
	var div = document.getElementById("exchangeTable");
	var row = document.createElement("tr");
	var col1=document.createElement("td");
	col1.setAttribute("style","color: navy");
	var col2=document.createElement("td");
	col1.setAttribute("align", "right");
	col1.setAttribute("width", "160");
	col2.setAttribute("width", "100");
	var	tdHtmlData1 = '<div style="background: transparent;border-width: 0px;color: navy;">'+deno+'   x </div>';
	//var	tdHtmlData1= '<input type="text" name="eDenoType" value="'+deno+'" size="3" style="background: transparent;border-width: 0px;color: navy;" readonly="readonly"  />x';
	var	tdHtmlData2= '<input type="text" name="eMultiples" id="eMultiples_'+deno+'" maxlength="10" size="6"  onkeypress="validateComm(event)"></input>';
	col1.innerHTML=tdHtmlData1;
	col2.innerHTML=tdHtmlData2;
	row.appendChild(col1);
	row.appendChild(col2);
	div.appendChild(row);
}
function verifyExchangeAmount()
{
	var c = 0;
	var fl = false;
	var fl1 = false;
	var fl2 = false;
	var iAmt = 0;
	var eAmt = 0;
	_id.i("errorDiv","");
	for ( var i = 0; i < iArr.length; i++) {
		iMultiples = document.getElementById('iMultiples_' + iArr[i] + '').value;
		if (iMultiples == "" || iMultiples == 0) {
			fl = false;
		} else {
			iAmt = parseInt(iAmt) + (iArr[i] * iMultiples);
			fl1 = true;
		}
	}
	if (fl1 == true) {
		for ( var i = 0; i < eArr.length; i++) {
			eMultiples = document.getElementById('eMultiples_' + eArr[i] + '').value;
			if (eMultiples == "" || eMultiples == 0) {
				fl = false;
			} else {
				eAmt = parseInt(eAmt) + (eArr[i] * eMultiples);
				fl2 = true;
			}
		}
	}
	if(iAmt!=eAmt)
	{
		alert("Please enter valid multiples !");
		fl2 = false;
		return fl2;
	}
	if(fl2==false)
		_id.i("errorDiv","Please enter valid entries");
	else
		_id.i("errorDiv","");
	return fl2;
}

parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/common_cash_register.js,v $"] = "$Id: common_cash_register.js,v 1.2 2016/10/31 09:46:22 neeraj Exp $";