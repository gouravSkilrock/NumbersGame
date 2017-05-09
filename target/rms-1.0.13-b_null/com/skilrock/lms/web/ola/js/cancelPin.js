
function validate(){

var walletName= document.getElementById("walletName").value;
var amount=document.getElementById("amount").value;
var typeDiv=document.getElementById("typeMsg");
var amountDiv=document.getElementById("amountMsg");
var serialVal = document.getElementById('serial').value;
var serialDiv = document.getElementById("serialMsg");
var pinVal = document.getElementById('pin').value;
var pinDiv = document.getElementById("pinMsg");
var plrnameVal = document.getElementById('plrname').value;
var plrnameDiv = document.getElementById("plrnameMsg");
clearDiv();
var flag = true;
if (amount == "") {
		amountDiv.innerHTML="<font color=red>Please Enter Amount </font>";
		flag= false;
	} else {
		amountDiv.innerHTML="";
	}
	if(amount <= 0)
	{
		amountDiv.innerHTML="<font color=red>Please Enter  amount greater then zero</font>";
		flag= false;
	}
	if (isNaN(amount)) {
	amountDiv.innerHTML="<font color=red>Please enter valid amount</font>";
		flag= false;
	}
	var amountArr = amount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
		amountDiv.innerHTML="<font color=red>Please enter valid amount</font>";
			flag= false;
		}
	}

if(isSelected(walletName)){
typeDiv.innerHTML="<font color=red>Please Select Wallet </font>";
flag= false;
}
function clearDiv(){
typeDiv.innerHTML="";
amountDiv.innerHTML="";
pinDiv.innerHTML="";
serialDiv.innerHTML="";
plrnameDiv.innerHTML="";
}
function isSelected(val){
if(val=="-1"){
return true;
}
return false;

}
if(isEmpty(serialVal)){
	serialDiv.innerHTML = "<font color=red>Please Enter Serial Number</font>";
	flag = false;
}
if(!isValidSerial(serialVal)){
	serialDiv.innerHTML = "<font color=red>Please Enter Correct Serial Number</font>";
	flag = false;
}
if(isEmpty(pinVal)){
	pinDiv.innerHTML = "<font color=red>Please Enter Pin Number</font>";
	flag = false;
}
if(!isValidPin(pinVal)){
	pinDiv.innerHTML = "<font color=red>Please Enter Correct Pin Number</font>";
	flag = false;
}

if(isEmpty(plrnameVal)){
	plrnameDiv.innerHTML = "<font color=red>Please Enter a Player Name</font>";
	flag = false;
}
if(!isValidPlrName(plrnameVal)){
	plrnameDiv.innerHTML = "<font color=red>Please Enter a Valid Player Name</font>";
	flag = false;
}
if(flag==true){
	var s = confirm("You Want To Cancel This Pin? ");	
	if(s==false){
		return false;
	}
}
return flag;
}

function isEmpty(field){
	if(field == "") return true;
	return false;
}
function isValidSerial(val){
	var regex=/^[0-9]{4,16}$/;
	// var regex = /^([1-9]{1})+([0-9]{11})+$/;
	if(!val.match(regex))
		return false;
	return true;
}
function isValidPin(val){
	var regex=/^[0-9]{4,16}$/;
// 	var regex = /^([1-9]{1})+([0-9]{3})+$/;
	if(!val.match(regex))
		return false;
	return true;
}

function isValidPlrName(val){
	//var regex = /^[A-Za-z0-9\.@_#\/'&\$\+<>\(\)\*;!\?,-]{1,32}$/;
	 var regex=/^([A-Za-z])([A-Za-z0-9\.]){4,12}$/;
	if(!val.match(regex))
		return false;
	return true;
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/js/cancelPin.js,v $'] = '$Id: cancelPin.js,v 1.2 2016/10/31 09:45:44 neeraj Exp $';