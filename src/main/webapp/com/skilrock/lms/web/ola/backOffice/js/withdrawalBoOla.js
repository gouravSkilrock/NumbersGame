var walletDetMap = new Object();
$(document).ready(function() {
	
        $('#walletName').change(function() {
        	var walletValue =$(this).val();
        	verifyWalletName(walletValue);
        	displayAuthCode(walletValue);
        	        
         }); 
        $('#withDrawalFrm').submit(function() {
        	return verifyWithdrawl();
         }); 
        
 });



		


function displayAuthCode(authValue) {
	
	var walletDetailsArr = authValue.split(":");
	var authType = walletDetailsArr[2];
	
	if (authType == "SMS_CODE") { 
		$("#sms_code").show();
	} else {
		//alert('insode no');
		 $("#sms_code").hide();
		
	}
	
}

function verifyWithdrawl() {

	var isValid = true;
	var userName = _id.v("user_name");
	var amount = _id.v("amount");
	var walletName = _id.v("walletName");
	var smsCode = _id.v("smsCode");
	var verifyAmount = _id.v("verifyAmount");
	if (userName == "") {
		_id.i("uName_e", "Please enter the user Name", "e");
		isValid = false;
	} else {
		_id.i("uName_e", "");
	}

	if (amount == "") {
		_id.i("amount_e", "Please enter amount", "e");
		isValid = false;
	} else {
		_id.i("amount_e", "");
	}
	if(amount <= 0)
	{
		_id.i("amount_e", "amount should be greater then zero", "e");
		isValid = false;
	}
	if (isNaN(amount)) {
		_id.i("amount_e", "Please enter valid amount", "e");
		isValid = false;
	}
	var amountArr = amount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
			_id.i("amount_e", "Please enter valid amount", "e");
			return false;
		}
	}
	if(verifyAmount == "")
	{
		//alert("hello"+verifyAmount);
		_id.i("verifyAmount_e", "Please enter amount for verify");
		isValid = false;
	}
	else
	{
		_id.i("verifyAmount_e", "");
	}
	if(amount!=verifyAmount)
	{
		_id.i("verifyAmount_e", "Please verify the amount correctly", "e");
		isValid= false;
	}
	if (walletName == -1) {

		_id.i("walletName_e", "Please Select The wallet Name", "e");
		isValid = false;
		return false;
	} else {
		_id.i("walletName_e", "");
	}

	if (smsCode == "") {
		_id.i("smsCode_e", "please enter the Authentication code", "e");
		isValid = false;
		return false;
	} else {
		_id.i("smsCode_e", "");
	}
	return isValid;
}






parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/backOffice/js/Attic/withdrawalBoOla.js,v $"] = "$Id: withdrawalBoOla.js,v 1.1.2.1 2013/10/03 12:56:49 yogesh Exp $";