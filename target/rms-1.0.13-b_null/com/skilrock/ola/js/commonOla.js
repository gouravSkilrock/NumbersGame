var i18nOb = top.frames[0].i18Obj;
$(document).ready(function(){
	$('#refCode').on('change',function(){
		var sub = document.getElementById("submit");	
		sub.disabled =true;
		validateUserName();
	});
	
	$('#walletName').on('change',function(){
		 clearWithdrawalForm();
		_id.i("uName_e", "");
		
	});
});

function clearDepositForm(){
	$('#amount').val('');
	$('#verifyAmount').val('');
	$('#refCode').val('');
	$('#refCode').focus();
}

function clearWithdrawalForm(){
	$('#amount').val('');
	$('#verifyAmount').val('');
	$('#refCode').val('');
	$('#smsCode').val('');
	$('#refCode').focus();
}



var walletDetMap = new Object();

function disableSubmit(){
	var sub = document.getElementById("submit");	
	sub.disabled =true;
	validateUserName();

	}

function fetchActiveWallets(path) {

	//alert("fsdgfsd");

	var url = path + "/com/skilrock/lms/web/ola/OLA_MGT/olaWalletDetails.action";
	//alert(url);
	var _resp = _ajaxCall(url);
	//alert(_resp.data)
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var option = _id.o("walletName");

		var walletArr = _respData.split("Nxt");
		//alert("gameArr = "+gameArr.length+" data = "+gameArr);
		var walletWiseMap;
		for ( var i = 0; i < walletArr.length; i++) {
			// creating map of all details of game
			walletWiseMap = new Object();

			var walletDetailsArr = walletArr[i].split(":");
			walletWiseMap['walletId'] = walletDetailsArr[0];
			walletWiseMap['walletDisplayName'] = walletDetailsArr[1];
			walletWiseMap['aunthticationType'] = walletDetailsArr[2];
			var gkey = walletDetailsArr[0] + ':' + walletDetailsArr[1] + ":"
					+ walletDetailsArr[2];
			walletDetMap[gkey] = walletWiseMap;
			//alert(gkey+" gkey   === "+(gameDetMap[gkey])['gameId']);
			// create game option
			var dvalue = walletDetailsArr[1];
			//var aunthType = walletDetailsArr[2];
			//alert(aunthType);

			var opt = new Option(dvalue, gkey);
			//alert("dvalue"+dvalue);
			//alert("gkey"+gkey);
			option.options[i + 1] = opt;

		}

		//alert(gameDetMap);
	}
}
function displayAuthCode(authValue) {
	verifyWalletName(authValue);
	var walletDetailsArr = authValue.split(":");
	var authType = walletDetailsArr[2];
	
	if (authType == "SMS_CODE") {
		document.getElementById('sms_code').style.display = "block";
	} else {
		//alert('insode no');
		document.getElementById('sms_code').style.display = "none";
	}
	
}

function verifyWithdrawl() {

	var isValid = true;
	var userName = _id.v("refCode");
	var amount = _id.v("amount");
	var walletName = _id.v("walletName");
	var smsCode = _id.v("smsCode");
	var verifyAmount = _id.v("verifyAmount");
	if (userName == "") {
		_id.i("uName_e", i18nOb.prop('error.pls.entr.user.name'), "e");
		isValid = false;
	} else {
		_id.i("uName_e", "");
	}

	if (amount == "") {
		_id.i("amount_e", i18nOb.prop('error.amount.empty'), "e");
		isValid = false;
	} else {
		_id.i("amount_e", "");
	}
	if(amount <= 0)
	{
		_id.i("amount_e", i18nOb.prop('error.amount.gt.zero'), "e");
		isValid = false;
	}
	if (isNaN(amount)) {
		_id.i("amount_e", i18nOb.prop('error.amount.invalid'), "e");
		isValid = false;
	}
	var amountArr = amount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
			_id.i("amount_e", i18nOb.prop('error.amount.invalid'), "e");
			return false;
		}
	}
	if(verifyAmount == "")
	{
		//alert("hello"+verifyAmount);
		_id.i("verifyAmount_e", i18nOb.prop('error.amount.notverified'));
		isValid = false;
	}
	else
	{
		_id.i("verifyAmount_e", "");
	}
	if(amount!=verifyAmount)
	{
		_id.i("verifyAmount_e", i18nOb.prop('error.amount.notverified.correct'), "e");
		isValid= false;
	}
	if (walletName == -1) {

		_id.i("walletName_e", i18nOb.prop('error.pls.slct.wallet.name'), "e");
		isValid = false;
		return false;
	} else {
		_id.i("walletName_e", "");
	}

	if (smsCode == "") {
		_id.i("smsCode_e", i18nOb.prop('error.pls.entr.auth.code'), "e");
		isValid = false;
		return false;
	} else {
		_id.i("smsCode_e", "");
	}
	return isValid;
}

function verifyDeposit() {
	
	var isValid = true;
	var userName = $('#refCode').val();
	var amount =$('#amount').val();
	var walletName =$('#walletName').val();
	
	var verifyAmount =$('#verifyAmount').val();
	
	//var password = _id.v("password");
	if (userName == "") {
		_id.i("uName_e", i18nOb.prop('error.pls.entr.user.name'), "e");
		isValid = false;

	} else {
		_id.i("uName_e", "");
	}

	if (amount == "") {
		_id.i("amount_e", i18nOb.prop('error.amount.empty'), "e");
		isValid = false;
	} else {
		_id.i("amount_e", "");
	}
	/*if(amount < 25)
	{
		_id.i("amount_e", " Minimum Deposit Amount is 25.Enter amount greater than or equal to 25 ", "e");
		isValid = false;
	}*/
	if (isNaN(amount)) {
		_id.i("amount_e", i18nOb.prop('error.amount.invalid'), "e");
		isValid = false;
	}
	var amountArr = amount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
			_id.i("amount_e", i18nOb.prop('error.amount.invalid'), "e");
			isValid = false;
		}
	}
	var regex = /^[0-9\.]{1,20}$/;
	if(!amount.match(regex))
	{
		_id.i("amount_e", i18nOb.prop('error.amount.invalid'), "e");
		isValid = false;
	}
	if(verifyAmount == "")
	{
		//alert("hello"+verifyAmount);
		_id.i("verifyAmount_e", i18nOb.prop('error.amount.notverified'));
		isValid = false;
	}
	else
	{
		_id.i("verifyAmount_e", "");
	}
	if(amount!=verifyAmount)
	{
		_id.i("verifyAmount_e", i18nOb.prop('error.amount.notverified.correct'), "e");
		isValid= false;
	}
	
	if (walletName == -1) {

		_id.i("walletName_e", i18nOb.prop('error.pls.slct.wallet.name'), "e");
		isValid = false;
		return isValid;
	} else {
		_id.i("walletName_e", "");
	}

	
	if(isValid ==true)
	{	
		//alert('all are ok :' + amount);
		var amountFlag=localConvertDigitToWord(amount,userName,refCode);
		if(!amountFlag){
			isValid=false;
		}
				
	}
	
	return isValid;
}

function verifyCheckbox(){
				var isValid = true;
				var checkBoxIdList = _id.n("withdrawalCodeArr");
				
				for (var i = 0; i < checkBoxIdList.length; i++) {
				if(checkBoxIdList[i].checked == false)
				{
				_id.i("checkboxName_e", i18nOb.prop('error.pls.slct.withdrawl.req.code'), "e");
					isValid = false;
				}
				else
				{
					isValid = true;
					break;
				}
				}
				if(isValid == true)
				{
					var flag = confirm(i18nOb.prop('msg.do.u.want.to.proceed'));
					if(!flag)
					{
						isValid = false;
					}
				}
				return isValid;
			
			}
function localConvertDigitToWord(input,userName,phone) {
	
	var splitInput = input.split(".");
	var message = "Amount Entered By You is : - ";
	//alert(message);
	var decNumName;
	//
	if(splitInput[0]) {
		decNumName = convert(splitInput[0]);
		//alert(""+decNumName);
	}
	else decNumName = " Zero ";
	//alert("dfgdfg");
	message = message + decNumName;
	
	if(splitInput.length>1) {
		var numName = convert(splitInput[1]);
		message = message+" (  ."+splitInput[1]+" )";
	}
	//alert("message " + message);
	var curr = parent.frames[0].document.getElementById("currencyWord").innerHTML.replace(/(^\s+|\s+$)/g, "");
	//alert(curr);
	
	
	var walletValue = document.getElementById("walletName").value;

	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	if(walletDevName=="RUMMY"){
	message = message+" "+curr+" Only  \n Player Alias: "+userName+"      Phone Number:"+phone;
	}else{
	message = message+" "+curr+" Only  \n Player Alias: "+userName+"";
	}
	var flag = confirm(message);
	return flag;
}


function convert(input) {

	var inputlength = input.length;	
	var x = 0;
	var teen1 = "";
	var teen2 = "";
	var teen3 = "";
	var numName = "";
	var invalidNum = "";
	// for insertion of million, thousand, hundred 
	var a1 = "";
	var a2 = "";
	var a3 = "";
	var a4 = "";
	var a5 = "";
	// stores output
	digit = new Array(inputlength); 
	for (i = 0; i < inputlength; i++)  {
	// puts digits into array
	digit[inputlength - i] = input.charAt(i)};
	store = new Array(9); // store output
	for (i = 0; i < inputlength; i++) {
	x= inputlength - i;
	// assign text to each digit
	//alert(x);
	switch (x) { 
	
		case 9: d1(digit[x]); store[x] = n; break;
		case 8: if (digit[x] == "1") {teen3 = "yes";}
				  else {teen3 = "";}; d2(digit[x]); store[x] = n; break;
		case 7: if (teen3 == "yes") {teen3 = ""; d3(digit[x]);}
				  else {d1(digit[x]);}; store[x] = n; break;
		case 6: d1(digit[x]); store[x] = n; break;
		
		case 5: if (digit[x] == "1") { teen2 = "yes";}
				  else {teen2 = "";}; d2(digit[x]); store[x] = n; break;
		
		case 4: if (teen2 == "yes") {teen2 = ""; d3(digit[x]);}    
				  else {d1(digit[x]);}; store[x] = n; break;
		case 3: d1(digit[x]); store[x] = n; break;
		case 2: if (digit[x] == "1") {teen1 = "yes";}
				  else {teen1 = ""}; d2(digit[x]); store[x] = n; break;
		case 1: if (teen1 == "yes") {teen1 = "";d3(digit[x]);}     
				  else {d1(digit[x]);}; store[x] = n; break;
	}
//alert('dfdfgdfgdfgdfgdfgdgdfg');
	
	if (store[x] == "Not a Number"){invalidNum = "yes"};
		switch (inputlength){
			case 1:   store[2] = ""; 
			case 2:   store[3] = ""; 
			case 3:   store[4] = ""; 
			case 4:   store[5] = "";
			case 5:   store[6] = "";
			case 6:   store[7] = "";
			case 7:   store[8] = "";
			case 8:   store[9] = "";
		}
	if (store[9] != "") { a1 =" Hundred, "} else {a1 = ""};
	if ((store[9] != "")||(store[8] != "")||(store[7] != ""))
	{ a2 =" Million, "} else {a2 = ""};
	if (store[6] != "") { a3 =" Hundred "} else {a3 = ""};
	if ((store[6] != "")||(store[5] != "")||(store[4] != ""))
	{ a4 =" Thousand, "} else {a4 = ""};
	if (store[3] != "") { a5 =" Hundred "} else {a5 = ""};
	}
	// add up text, cancel if invalid input found
	if (invalidNum == "yes"){numName = "Invalid Input"}
	else {
	numName =  store[9] + a1 + store[8] + store[7] 
	+ a2 + store[6] + a3 + store[5] + store[4] 
	+ a4 + store[3] + a5 + store[2] + store[1];
	}
	store[1] = ""; store[2] = ""; store[3] = ""; 
	store[4] = ""; store[5] = ""; store[6] = "";
	store[7] = ""; store[8] = ""; store[9] = "";
	if (numName == "")	numName = "Zero";
	//alert(numName);
	return numName;

}
//single digit terms
function d1(x) { 
	var y = x+'';
//alert("d1 = "+y);
	switch(y) {
		case '0': n= ""; break;
		case '1': n= " One "; break;
		case '2': n= " Two "; break;
		case '3': n= " Three "; break;
		case '4': n= " Four "; break;
		case '5': n= " Five "; break;
		case '6': n= " Six "; break;
		case '7': n= " Seven "; break;
		case '8': n= " Eight "; break;
		case '9': n= " Nine "; break;
		default: n = "Not a Number";
	}
	return n;
}


// 10x digit terms
function d2(x) { 
	var y = x+'';
	//alert("d2 = "+y);
	switch(y) {
		case '0': n= ""; break;
		case '1': n= ""; break;
		case '2': n= " Twenty "; break;
		case '3': n= " Thirty "; break;
		case '4': n= " Forty "; break;
		case '5': n= " Fifty "; break;
		case '6': n= " Sixty "; break;
		case '7': n= " Seventy "; break;
		case '8': n= " Eighty "; break;
		case '9': n= " Ninety "; break;
		default: n = "Not a Number";
	}
	return n;
}



function d3(x) { // teen digit terms
	var y = x+'';
	//alert("d3 = "+y);
	switch(y) {
		case '0': n= " Ten "; break;
		case '1': n= " Eleven "; break;
		case '2': n= " Twelve "; break;
		case '3': n= " Thirteen "; break;
		case '4': n= " Fourteen "; break;
		case '5': n= " Fifteen "; break;
		case '6': n= " Sixteen "; break;
		case '7': n= " Seventeen "; break;
		case '8': n= " Eighteen "; break;
		case '9': n= " Nineteen "; break;
		default: n=  "Not a Number";
	}
	return n;
}




function verifyReport()
{	
	var walletName = _id.v("walletName");
	if (walletName == -1) {
		_id.i("walletName_e", i18nOb.prop('error.pls.slct.wallet.name'), "e");
		return false;
	} else {
		_id.i("walletName_e", "");
		return true;
	}
	//return isValid;
}


function verifyWalletNameForReport(walletName) {
	_id.o('down').innerHTML="";
  if(document.getElementById('walletName').value!="-1")
  {
	  var walletDisplayName = walletName.split(":");
		var walletDevName = walletDisplayName[1];
  var returnType=confirm("You have selected :: " + walletDevName);
  
  if(!returnType)    
    {     
	    document.getElementById('walletName').selectedIndex=0;
       document.getElementById('walletName').focus();
       //return false;
   }
  }  
}
function verifyWalletName(walletName) {
	
if(document.getElementById('walletName').value!="-1")
{
	var walletDisplayName = walletName.split(":");
	var walletDevName = walletDisplayName[1];
var returnType=confirm("You have selected :: " + walletDevName);

if(returnType)    
  {     
	
  document.getElementById('amount').focus();
  // return true;
  }
 else
 {
     document.getElementById('walletName').selectedIndex=0;
     document.getElementById('walletName').focus();
     //return false;
 }
}  
} 
function displayemail(walletValue) {
//alert(walletValue);
	verifyWalletName(walletValue);
	  document.getElementById('user_name').value="";
	  _id.o("walletName_e").innerHTML="";
	  _id.o("uName_e").innerHTML="";
	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	//alert(":"+walletDevName);
	if (walletDevName == "RUMMY") {
	//alert('insode yes');
		document.getElementById("user_Phone").style.display = "block";
	} else {
	//	alert('insode no');
		document.getElementById("user_Phone").style.display = "none";
	}
}		

// start fetch ola commision update type 
function fetchCommUpdateType(path1) {
	var url = path1 + "/com/skilrock/lms/web/ola/OLA_MGT/commUpdateType.action";
	//alert(url);
	var _resp = _ajaxCall(url);
//	alert("in");
	if (_resp.result && _resp.data != "") {
// alert(_resp.data);
		var _respData = _resp.data;
		var option = _id.o("netGamingExpType");
		var option1 = document.getElementById("netGamingExpType");
		var opt = new Option(_respData, _respData);
		option1.options[1] = opt;
	}


// end by neeraj
}
function verifyCashCard() {
	var isValid = true;
	var userName = _id.v("userName");
//	if (userName == "") {
	//	_id.i("userName_e", "Please enter the user Name", "e");
		//isValid = false;
	//} else {
		//_id.i("userName_e", "");
	//}
	var cardType = _id.v("cardType");
	if (cardType == "-1") {
		_id.i("cardType_e", i18nOb.prop('error.pls.slct.card.type'), "e");
		isValid = false;
	} else {
		_id.i("cardType_e", "");
	}
	var denoAmountA = document.getElementsByName("denoType");
	var numberofCardsA = document.getElementsByName("numberofCards");
	var k;
	for (k = 0; k < denoAmountA.length; k++) {
		if (k == 0) {
			if (numberofCardsA[0].value == "-1" && denoAmountA[0].value == "-1") {
				_id.i("numberofCards_e", i18nOb.prop('error.pls.slct.nbr.of.cards'), "e");
				_id.i("denoType_e", i18nOb.prop('error.pls.slct.denomination.amt'), "e");
				isValid = false;
				break;
			} else {
				if (denoAmountA[0].value == "-1") {
					_id.i("denoType_e", i18nOb.prop('error.pls.slct.denomination.amt'), "e");
					isValid = false;
					break;
				} else {
					if (numberofCardsA[0].value == "-1") {
						_id.i("numberofCards_e", i18nOb.prop('error.pls.slct.nbr.of.cards'), "e");
						isValid = false;
						break;
					} else {
						_id.i("denoType_e", "");
						_id.i("numberofCards_e", "");
					}
				}
			}
		} else {
			if (denoAmountA[k].value == "-1") {
				if (numberofCardsA[k].value == "-1") {
					_id.i("denoType_e", "");
					_id.i("numberofCards_e", "");
					isValid = true;
				} else {
					_id.i("denoType_e", i18nOb.prop('error.pls.slct.denomination.amt'), "e");
					isValid = false;
				}
			} else {
				if (numberofCardsA[k].value == "-1") {
					_id.i("numberofCards_e", i18nOb.prop('error.pls.slct.nbr.of.cards'), "e");
					isValid = false;
				} else {
					_id.i("numberofCards_e", "");
				}
			}
		}
	}
	return isValid;
}
function verifyPinGenerator() {
	var isValid = true;
	var pinDist = _id.v("distributorType");
	var commRate = _id.v("commRate");
	if (commRate == "") {
		_id.i("commRate_e", i18nOb.prop('error.pls.entr.comm.rate'), "e");
		isValid = false;
	} else {
		_id.i("commRate_e", "");
	}
	if(commRate<= 0)
	{
		_id.i("commRate_e", i18nOb.prop('error.comm.rate.gt.then.zero'), "e");
		isValid = false;
	}
	if (isNaN(commRate)) {
		_id.i("commRate_e", i18nOb.prop('error.pls.entr.valid.comm.rate'), "e");
		isValid = false;
	}
	var commRateArr =commRate.split('.');
	if (commRateArr.length > 1) {
		if (commRateArr[1].length > 2) {
			_id.i("commRate_e", i18nOb.prop('error.pls.entr.valid.comm.rate'), "e");
			isValid = false;
		}
	}
	var regex = /^[0-9\.]{1,20}$/;
	if(!commRate.match(regex))
	{
		_id.i("commRate_e", i18nOb.prop('error.pls.entr.valid.comm.rate'), "e");
		isValid = false;
	}
	if (pinDist == "-1") {
		isValid = false;
		_id.i("distributorType_e", i18nOb.prop('error.pls.slct.distributor'), "e");
	} else {
		_id.i("distributorType_e", "");
	}
	var denoTyp = _id.v("denoType");
	if (denoTyp == "-1") {
		isValid = false;
		_id.i("denoType_e", i18nOb.prop('error.pls.slct.denomination'), "e");
	} else {
		_id.i("denoType_e", "");
	}
	var pinQuantity = _id.v("pinQuantity");
	if (pinQuantity == "") {
		_id.i("pinQuantity_e", i18nOb.prop('error.pls.entr.quantity'), "e");
		isValid = false;
	} else {
		if (pinQuantity <= 0) {
			_id.i("pinQuantity_e", i18nOb.prop('error.quantity.gt.zero'), "e");
			isValid = false;
		} else {
			if (isNaN(pinQuantity)) {0.0
				_id.i("pinQuantity_e", i18nOb.prop('error.pls.entr.valid.quantity'), "e");
				isValid = false;
			} else {
				_id.i("pinQuantity_e", "");
			}
		}
	}
	
	var pinQuantArr = pinQuantity.split('.');
	if (pinQuantArr.length > 1) {
			_id.i("pinQuantity_e", i18nOb.prop('error.pls.entr.valid.quantity'), "e");
			isValid = false;
	}
	var regex = /^[0-9]{1,8}$/;
	if(!pinQuantity.match(regex))
	{
		_id.i("pinQuantity_e", i18nOb.prop('error.pls.entr.valid.quantity'), "e");
		isValid = false;
	}
	
	
	var partyType = _id.v("partyType");
	if (partyType == "-1") {
		isValid = false;
		_id.i("partyType_e", i18nOb.prop('error.pls.slct.party.type'), "e");
	} else {
		_id.i("partyType_e", "");
	}
	return isValid;
}

function validateUserName(){
var mobilNumLimit=document.getElementById("mobileNumberlimit").value;
var userName = $('#refCode').val();
var regex = /^\d+$/;

	if (userName == "") {
		_id.i("uName_e", i18nOb.prop('error.ref.code.empty'), "e");
		isValid = false;
		return isValid;

	} else if(!userName.match(regex)){
		isValid=false;
		_id.i("uName_e", i18nOb.prop('error.mobno.empty'), "e");
		return isValid;
	}else if(userName.length!=parseInt(mobilNumLimit)){
		isValid=false;
		_id.i("uName_e", i18nOb.prop('error.mobno.empty'), "e");
		return isValid;
	}else {
		_id.i("uName_e", "");
	}
var walletValue = $('#walletName').val();
var sub = document.getElementById("submit");	
//alert(walletValue);
if(walletValue=="-1"){
	_id.o("walletName_e").innerHTML = "<font color='red'>"+i18nOb.prop('error.pls.slct.wallet.first')+"</font>";
}else{
	_id.o("uName_e").innerHTML = "Validating User Name.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
	url = projectName+"/com/skilrock/ola/commonMethods/action/verifyRefCode.action?refCode="+userName+"&walletName="+walletValue;
	_resp = _ajaxCallJson(url);
	//alert(_resp.isSuccess);
					if (_resp.isSuccess) {
						$('#uName_e').html("<font color='green'>"+_resp.message+"</font>");
							
							sub.disabled =false;
						} else{
							sub.disabled =true;
							$('#uName_e').html("<font color='red'>"+_resp.message+"</font>");
						}
						
				
			}
	}
	





parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/ola/js/commonOla.js,v $"] = "$Id: commonOla.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $";