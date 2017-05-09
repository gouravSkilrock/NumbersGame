var walletDetMap = new Object();
$(document).ready(function() {
	
        
        $('#walletName').change(function() {
        	var walletValue =$(this).val();
        	verifyWalletName(walletValue);
        	 $("#user_name").val("");
        	 $("#walletName_e").html("");
        	 $("#uName_e").html("");

        	var walletDisplayName = walletValue.split(":");
        	var walletDevName = walletDisplayName[1];
        	//alert(":"+walletDevName);
        	if (walletDevName == "RUMMY") {
        		  $("#user_Phone").css("style.display","block");
        	
        	} else {
        		 $("#user_Phone").css("style.display","none");
        		
        	}
         
         }); 
        $('#depositFrm').submit(function() {
        	return verifyDeposit();
         }); 
        $('#user_name').change(function() {
        	disableSubmit();
         });  
        
 });


function verifyDeposit() {
	
	var isValid = true;
	var userName = $('#user_name').val();
	var amount = $('#amount').val();
	var walletName =  $('#walletName').val();
	
	var verifyAmount =$('#verifyAmount').val(); 
	var phone = $('#verifyAmount').val().trim();	
	//var password = _id.v("password");
	if (userName == "") {
		$('#uName_e').html("Please enter the user Name");
		isValid = false;

	} else {
		$('#uName_e').html(" ");
	}
	

	
	

	
/*	if(password == "")
	{
		_id.i("password_e", "Please enter the password", "");
	}else{
		_id.i("password_e", "");
	}*/
	if (amount == "") {
		$('#amount_e').html("Please enter amount");
		isValid = false;
	} else {
		$('#amount_e').html(" ");
	
	}
	if(amount < 25)
	{
		$('#amount_e').html(" Minimum Deposit Amount is 25.Enter amount greater than or equal to 25");
		isValid = false;
	}
	if (isNaN(amount)) {
		$('#amount_e').html(" Please enter valid amount");
		isValid = false;
	}
	var amountArr = amount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
			$('#amount_e').html(" Please enter valid amount");
			isValid = false;
		}
	}
	var regex = /^[0-9\.]{1,20}$/;
	if(!amount.match(regex))
	{
		$('#amount_e').html(" Please enter valid amount");
		isValid = false;
	}
	if(verifyAmount == "")
	{
		//alert("hello"+verifyAmount);
		$('#verifyAmount_e').html(" Please enter amount for verify");
		isValid = false;
	}
	else
	{
		$('#verifyAmount_e').html(" ");
	}
	if(amount!=verifyAmount)
	{
		$('#verifyAmount_e').html("Please verify the amount correctly");
		isValid= false;
	}
	
	if (walletName == -1) {
		$('#walletName_e').html("Please Select The wallet Name");
		isValid = false;
		return isValid;
	} else {
		$('#walletName_e').html("");
		
	}
	var walletDisplayName = walletName.split(":");
	var walletDevName = walletDisplayName[1];
	// alert("walletDevName"+walletDevName);
	if(walletDevName=="RUMMY"){
			if(phone!==""){
	var regex1 = /^[0-9\+\-\(\)]{3,20}$/;
	if (!phone.match(regex1)) {
		$('#userPhone_e').html("Please Enter Correct Phone Number");
		isValid = false;
	} else {
		$('#userPhone_e').html(" ");
	}
}else{
	$('#userPhone_e').html("Please Enter Correct Phone Number");

	isValid = false;
	
}
	}
	if(isValid ==true)
	{	
		//alert('all are ok :' + amount);
		var amountFlag=localConvertDigitToWord(amount,userName,phone);
		if(!amountFlag){
			isValid=false;
		}
				
	}
	
	return isValid;
}



function validateUserName(){
	
var userName = $("#user_name").val();
if (userName == "") {
		$("#uName_e").html("Please enter the user Name");
		isValid = false;
		return isValid;

	} else {
		$("#uName_e").html(" ");
	}
var walletValue = $("#walletName").val();
//alert(walletValue);$('#submit').attr("disabled",true);
if(walletValue=="-1"){
	$("#walletName_e").html("<font color='red'>Please Select a Wallet First</font>");
}
else{
	$("#uName_e").html("Validating User Name.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>");
	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	url = "bo_um_playerReg_Availability.action?userName="+userName+"&walletName="+walletValue;
  $.get(url, function(_respData,status) {
	  
	  		if (walletDevName == "RUMMY") {
	  				if (_respData !="" && status== "success") {
				
	  					var data = _respData.split(",");
	  						for (var i = 0; i < data.length; i=i+1) {
	  					var keyValue = data[i].split("=");
							if (keyValue[0] == "userError") {
								if (keyValue[1] == "Avail") {
									$('#submit').attr("disabled",false);
									$('#uName_e').html("<font color='green'>User Name is Valid !!</font>");
									
							
									
								} else if(keyValue[1] == "Some Error"){
									$('#submit').attr("disabled",true);
									$('#uName_e').html("<font color='red'>User Name is Invalid !!</font>");
									
								}
								else {
									$('#submit').attr("disabled",true);
									$('#uName_e').html("<font color='red'>User Name is Invalid</font>");
								
								}
							
						}
					}
			}
			}
			else if(walletDevName=="PLAYER_LOTTERY"){
				if (_respData !="" && status== "success") {
					
					var data = _respData.split(",");
					for (var i = 0; i < data.length; i=i+1) {
						var keyValue = data[i].split("=");
							if (keyValue[0] == "valid") {
									$('#uName_e').html("<font color='green'>"+keyValue[1]+"</font>");
									$('#submit').attr("disabled",false);
								} else if(keyValue[0] == "inValid"){
									$('#submit').attr("disabled",true);
									$('#uName_e').html("<font color='red'>"+keyValue[1]+"</font>");
						
								}
								else {
									$('#submit').attr("disabled",true);
									$('#uName_e').html("<font color='red'>User Name is Invalid</font>");
								
								}
							
						}
					}
			}
	           }

	        );

	
	}

}
function disableSubmit(){
$('#submit').attr("disabled",true);
validateUserName()

}


parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/backOffice/js/Attic/depositBoOla.js,v $"] = "$Id: depositBoOla.js,v 1.1.2.1 2013/10/03 12:56:49 yogesh Exp $";