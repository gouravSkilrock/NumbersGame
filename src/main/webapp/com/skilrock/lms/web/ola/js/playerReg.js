function isValidated(){

	clearDivs();
	var flag = true;
	var walletDiv = document.getElementById("walletMsg");
	
	var walletVal =document.getElementById('walletName').value;
	if(walletVal=="-1"){
		walletDiv.innerHTML = "<font color=red>Please Select Wallet Name</font>";
			flag = false;
			
		}
	var walletDisplayName = walletVal.split(":");
	var walletDevName = walletDisplayName[1];
		if (walletDevName =="PLAYTECH_CASINO") {
		var password1Val = document.getElementById('password1').value;
		var password2Val = document.getElementById('password2').value;
		var p1Div = document.getElementById("p1Msg");
		var p2Div = document.getElementById("p2Msg");
		if(isEmpty(password1Val)){
		p1Div.innerHTML = "<font color=red>Please enter a password</font>";
		flag = false;
						}
		if(isEmpty(password2Val)){
		p2Div.innerHTML = "<font color=red>Please re-confirm your password</font>";
		flag = false;
		
		if(password1Val!=password2Val){
			p1Div.innerHTML = "<font color=red>Please enter same password</font>";
			p2Div.innerHTML = "<font color=red>Please enter same password</font>";
			flag = false;
				}
			}
		
		}
		if (walletDevName !="KhelPlayRummy") {	
			
			var stateDiv = document.getElementById("stateMsg");
			
			var addressVal = document.getElementById('address').value;
			var stateVal = document.getElementById('plrState').value;
			if(isNotSelected(stateVal)){
				stateDiv.innerHTML = "<font color=red>Please Select the name of the state you live in</font>";
				flag = false;
				return flag;
			}
			var cityVal = document.getElementById('plrCity').value;
			//var countryVal = document.getElementById('country').value;
			//var zipVal = document.getElementById('zip').value;
			var genderVal = _id.n('gender');
			for (var i = 0; i < genderVal.length; i++) {
				if (genderVal[i].checked) {
					var genderVal1 = genderVal[i].value;
				}
			}
			var genderDiv = document.getElementById("genderMsg");
			
			var addDiv = document.getElementById("addressMsg");
			var cityDiv = document.getElementById("cityMsg");
			
			if(genderVal1 == undefined){
				genderDiv.innerHTML = "<font color=red>Please enter Gender</font>";
				flag = false;
			}
			if(isEmpty(addressVal)){
				addDiv.innerHTML = "<font color=red>Please enter your address</font>";
				flag = false;
			}
			if(isNotSelected(cityVal)){
				cityDiv.innerHTML = "<font color=red>Please Select the name of the city you live in</font>";
				flag = false;
			}
		}	
	
	var firstnameVal = document.getElementById('firstName').value;
	var lastnameVal = document.getElementById('lastName').value;
	var usernameVal = document.getElementById('username').value;	
	var emailVal = document.getElementById('email').value;
	var email1Val = document.getElementById('email2').value;
	var phoneVal = document.getElementById('phone').value;
	//var languageVal = document.getElementById('language').value;
	
	var depositVal = document.getElementById('deposit').value;
	
	//alert("genderVal1"+genderVal1);

	var fnameDiv = document.getElementById("fnameMsg");
	var lnameDiv = document.getElementById("lnameMsg");
	
	var unameDiv = document.getElementById("unameMsg");
	
	var emailDiv = document.getElementById("emailMsg");
	var email1Div = document.getElementById("email2Msg");
	var phoneDiv = document.getElementById("phoneMsg");
	//var langDiv = document.getElementById("langMsg");
	
	
	//var countryDiv = document.getElementById("countryMsg");
	//var zipDiv = document.getElementById("zipMsg");
	var depositDiv = document.getElementById("depositMsg");
	
	if(isNotSelected(depositVal)){
	depositDiv.innerHTML = "<font color=red>Please Select Deposit Amount </font>";
		flag = false;
	}
	if(isEmpty(firstnameVal)){
		fnameDiv.innerHTML = "<font color=red>Please enter a first name</font>";
		flag = false;
	}
	if(isEmpty(lastnameVal)){
		lnameDiv.innerHTML = "<font color=red>Please enter a last name</font>";
		flag = false;
	}
	if(isEmpty(usernameVal)){
		unameDiv.innerHTML = "<font color=red>Please enter a username</font>";
		flag = false;
	}
	if(isEmpty(emailVal)){
		emailDiv.innerHTML = "<font color=red>Please enter your email address</font>";
		flag = false;
	}
	if(isEmpty(email1Val)){
		email1Div.innerHTML = "<font color=red>Please re-confirm your email address</font>";
		flag = false;
	}
	if(isEmpty(phoneVal)){
		phoneDiv.innerHTML = "<font color=red>Please enter your phone number</font>";
		flag = false;
	}
	
	
	/* if(isEmpty(countryVal)){
		countryDiv.innerHTML = "<font color=red>Please enter the name of the country you live in</font>";
		flag = false;
	}*/
	/*if(isEmpty(zipVal)){
		zipDiv.innerHTML = "<font color=red>Please enter the ZipCode</font>";
		flag = false;
	}*/
	
	if(emailVal!=email1Val){
		emailDiv.innerHTML = "<font color=red>Please enter same e-mail address</font>";
		email1Div.innerHTML = "<font color=red>Please enter same e-mail address</font>";
		flag = false;
	}
	if(!isValidPhone(phoneVal)){
		phoneDiv.innerHTML = "<font color=red>Please enter correct phone number</font>";
		flag = false;
	}
	if(!isValidEmail(emailVal)){
		emailDiv.innerHTML = "<font color=red>Please enter a valid email address</font>";
		flag = false;
	}

		
		if(!isValidName(firstnameVal)){
			fnameDiv.innerHTML = "<font color=red>Please enter a valid first name</font>";
			flag = false;
		}
		if(!isValidName(lastnameVal)){
			lnameDiv.innerHTML = "<font color=red>Please enter a valid last name</font>";
			flag = false;
		}
		
		if(!isValidUName(usernameVal)){
			unameDiv.innerHTML = "<font color=red>Please enter a valid username</font>";
			flag = false;
		}
	
	
	/*if(!isValidText(cityVal)){
		cityDiv.innerHTML = "<font color=red>Please enter a valid city name</font>";
		flag = false;
	}
	if(!isValidText(stateVal)){
		stateDiv.innerHTML = "<font color=red>Please enter a valid state name</font>";
		flag = false;
	}
	if(!isValidText(countryVal)){
		countryDiv.innerHTML = "<font color=red>Please enter a valid country name</font>";
		flag = false;
	}
	if(!isValidDeposit(depositVal)){
		depositDiv.innerHTML = "<font color=red>Please enter a valid amount</font>";
		flag = false;
	}*/
	
	//alert(flag);
	return flag;
}

function clearDivs(){
	document.getElementById("actionMsg").innerHTML="";
	document.getElementById("walletMsg").innerHTML="";
	document.getElementById("fnameMsg").innerHTML = "";
	document.getElementById("lnameMsg").innerHTML = "";
	document.getElementById("genderMsg").innerHTML = "";
	document.getElementById("unameMsg").innerHTML = "";
	document.getElementById("emailMsg").innerHTML = "";
	document.getElementById("email2Msg").innerHTML = "";
	document.getElementById("phoneMsg").innerHTML = "";
	//document.getElementById("langMsg").innerHTML = "";
	document.getElementById("addressMsg").innerHTML = "";
	document.getElementById("cityMsg").innerHTML = "";
	document.getElementById("stateMsg").innerHTML = "";
	//document.getElementById("countryMsg").innerHTML = "";
	//document.getElementById("zipMsg").innerHTML = "";
	document.getElementById("depositMsg").innerHTML = "";
	var walletVal =document.getElementById('walletName').value;
	var walletDisplayName = walletVal.split(":");
	var walletDevName = walletDisplayName[1];
	if (walletDevName =="PLAYTECH_CASINO"){
	document.getElementById("p1Msg").innerHTML = "";
	document.getElementById("p2Msg").innerHTML = "";
	}
}

function isNotSelected(field){
	if(field =="-1") return true;
	return false;
}

function isEmpty(field){
	if(field == "") return true;
	return false;
}

function isValidPhone(val){
	var flag = true;
	var regex = /^[0-9\+\-\(\)]{10,12}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}

function isValidEmail(val){
	var flag = true;
	var regex = /^([A-Za-z0-9\x27\x2f!#$%&*+=?^_`{|}~-]+(\.[A-Za-z0-9\x27\x2f!#$%&*+=?^_`{|}~-]+)*)@(([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]|[a-zA-Z0-9]{1,63})(\.([a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]|[a-zA-Z0-9]{1,63}))*\.[a-zA-Z0-9]{2,63})$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}

function isValidName(val){
	var flag = true;
	var regex = /^[A-Za-z,\.-]{1,15}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}


function isValidText(val){
	var flag = true;
	var regex = /^[A-Za-z-]{1,50}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}

function isValidUName(val){
	var flag = true;
	//var regex = /^[A-Za-z0-9\.@_#\/'&\$\+<>\(\)\*;!\?,-]{1,32}$/;
	 var regex=/^([A-Za-z])([A-Za-z0-9\.]){4,20}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}

function isValidDeposit(val){
	var flag = true;
	var regex = /^[0-9\.]{1,20}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}
function checkAvlUser() {
	
	if (_id.v("username") == "") {
		_id.i("unameMsg", "Please Enter User Name", "e");
		return false;
	}
var walletValue = document.getElementById("walletName").value;
var sub = document.getElementById("submit");	
// alert(walletValue);
	if(walletValue=="-1"){
		_id.o("walletMsg").innerHTML = "<font color='red'>Please Select a Wallet First</font>";
	return false;
	}	
var walletDisplayName = walletValue.split(":");
var walletDevName = walletDisplayName[1];
		//alert(url);
		//alert(_id.o("unameMsg").innerHTML);
url = "ret_um_playerReg_Availability.action?username="+ _id.v("username")+"&walletName="+walletValue;
_resp = _ajaxCall(url);
_id.o("unameMsg").innerHTML = "Validating User Name.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
	if (walletDevName == "RUMMY"||walletDevName == "KhelPlayRummy") {	
			if (_resp.result && _resp.data != "") {
							var _respData = _resp.data;
							//alert(_respData+" length "+_respData.split(",").length);
							var data = _respData.split(",");
							for (var i = 0; i < data.length; i++) {
								var keyValue = data[i].split("=");
									if (keyValue[0] == "userError") {
											if (keyValue[1] == "Avail") {
												_id.o("unameMsg").innerHTML = "<font color='green'>User Name is Available !!</font>";
												sub.disabled =false;
											} else if(keyValue[1] == "Some Error"){
												sub.disabled =true;
												_id.o("unameMsg").innerHTML = "<font color='red'>Some Error Occured. Please Try After Sometime..</font>";
											}
											else {
												sub.disabled =true;
											_id.o("unameMsg").innerHTML = "<font color='red'>User Name Not  Available</font>";
											}
				
										}
									}
	
	
							}
		}else if(walletDevName="PLAYER_LOTTERY"){	
		
			if (_resp.result && _resp.data != "") {
						var _respData = _resp.data;
							//alert(_respData+" length "+_respData.split(",").length);
						var data = _respData.split(",");
						for (var i = 0; i < data.length; i=i+1) {
							var keyValue = data[i].split("=");
								if (keyValue[0] == "valid") {
										_id.o("unameMsg").innerHTML = "<font color='green'>User Name Available</font>";
										sub.disabled =false;
									} else if(keyValue[0] == "inValid"){
										sub.disabled =true;
										_id.o("unameMsg").innerHTML = "<font color='red'>User Name Not Available</font>";
									}
									else {
									sub.disabled =true;
									_id.o("unameMsg").innerHTML = "<font color='red'>User Name is Invalid</font>";
									}
								
							}
						}
		
		}
}
function checkAvlEmail() {
	
	if (_id.v("email") == "") {
		_id.i("emailMsg", "Please Enter Email", "e");
		return false;
	}
	
	var flag =isValidEmail(_id.v("email"));
	if(!flag){
		_id.i("emailMsg", "Please Enter Valid Email", "e");
		return false;
	}
	var walletValue = document.getElementById("walletName").value;
	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	if (walletDevName == "RUMMY"||walletDevName == "KhelPlayRummy") {	
	_id.o("emailMsg").innerHTML = "Validating User Name.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
	var sub = document.getElementById("submit");	
	url = "ret_um_playerReg_Availability.action?email="+ _id.v("email")+"&walletName="+walletValue;
		//alert(url);
	//	alert(_id.o("emailMsg").innerHTML);
	_resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		//alert(_respData+" length "+_respData.split(",").length);
		var data = _respData.split(",");
		for (var i = 0; i < data.length; i++) {
			var keyValue1 = data[i].split("=");
				if (keyValue1[0] == "EmailError") {
					if (keyValue1[1] == "Avail") {
						_id.o("emailMsg").innerHTML = "<font color='green'>Email is Available !!</font>";
						sub.disabled =false;
					}else if (keyValue1[1] == "Error") {
						_id.o("emailMsg").innerHTML = "<font color='red'>Some Error Occured. Please Try After Sometime..</font>";
						sub.disabled =false;
					} else {
						sub.disabled =true;
						_id.i("emailMsg", keyValue1[1], "e");
					}
				
			}
		}	
		
		
		
	} else {
		_id.o("emailMsg").innerHTML = "<font color='red'>Some Error Occured. Please Try After Sometime..</font>";
	}

}	
	
}
function displayPassword(walletValue) {
	clearDivs();
if(document.getElementById('walletName').value!="-1")
{
	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	var returnType=confirm("You have selected :: " + walletDevName);
//	alert(returnType);
		if(returnType)    
  			{     
						
			if (walletDevName =="PLAYTECH_CASINO") {
			//		alert('3 insode yes');
				document.getElementById("password11").style.display = "block";
				document.getElementById("password22").style.display = "block";
				document.getElementById("checkAvail").style.display = "none";
					} else {
			//alert('4 insode no');
					document.getElementById("password11").style.display = "none";
					document.getElementById("password22").style.display = "none";
					document.getElementById("checkAvail").style.display = "block";
						if(walletDevName =="KhelPlayRummy"){
							document.getElementById("addDiv").style.display = "none";
							document.getElementById("addDiv1").style.display = "none";
							document.getElementById("genDiv1").style.display = "none";
							document.getElementById("genDiv").style.display = "none";
							document.getElementById("stateDiv1").style.display = "none";
							document.getElementById("stateDiv").style.display = "none";
							document.getElementById("cityDiv1").style.display = "none";
							document.getElementById("cityDiv").style.display = "none";
						
							
						}
					
					}
	
  }
 else
 {
     document.getElementById('walletName').selectedIndex=0;
     document.getElementById('walletName').focus();
     //return false;
 }
} else{
	
	
	document.getElementById("addDiv").innerHTML="";
	document.getElementById("genDiv").innerHTML="";
	document.getElementById("stateDiv").innerHTML="";
	document.getElementById("cityDiv").innerHTML="";
	
}



}
function getStates(){
	var walletValue = document.getElementById("walletName").value;
	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	if (walletDevName != "KhelPlayRummy") {	
		_ajaxCall('getState.action?country=' + this.value,'characters');
	}

}
function onLoadData(){
	
	fetchActiveWallets(projectName);
	onLoadAjax('getListAjax.action?listType=country','country');
	selectState();
	
	



}
function fetchCityList() {
	
	var country = _id.o('country').value;
	var state = _id.o('plrState').value;
		//alert(country+":"+state);
		_ajaxCall("getCity.action?country=" + country + "&state=" + state,'charactersCity');
}
function selectState(){
		//alert(document.getElementById('plrState').selectedIndex);
		 document.getElementById('plrState').selectedIndex=1;
			fetchCityList();
}
function checkAvlMobile() {
	
	if (_id.v("phone") == "") {
		_id.i("phoneMsg", "Please Enter Phone", "e");
		return false;
	}
	
	var flag =isValidPhone(_id.v("phone"));
	if(!flag){
		_id.i("phoneMsg", "Please Enter Valid Phone", "e");
		return false;
	}
	var walletValue = document.getElementById("walletName").value;
	if(walletValue=="-1"){
		_id.i("walletMsg", "Please Select Wallet First", "e");
		return false;
	}
	var walletDisplayName = walletValue.split(":");
	var walletDevName = walletDisplayName[1];
	if (walletDevName == "RUMMY"||walletDevName == "KhelPlayRummy") {	
	_id.o("phoneMsg").innerHTML = "Validating User Name.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
	var sub = document.getElementById("submit");	
	url = "ret_um_playerReg_Availability.action?phone="+ _id.v("phone")+"&walletName="+walletValue;
		//alert(url);
	//	alert(_id.o("emailMsg").innerHTML);
	_resp = _ajaxCall(url);
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		//alert(_respData+" length "+_respData.split(",").length);
		var data = _respData.split(",");
		for (var i = 0; i < data.length; i++) {
			var keyValue1 = data[i].split("=");
				if (keyValue1[0] == "PhoneError") {
					if (keyValue1[1] == "Avail") {
						_id.o("phoneMsg").innerHTML = "<font color='green'>Phone Number  is Available !!</font>";
						sub.disabled =false;
					}else if (keyValue1[1] == "Error") {
						_id.o("phoneMsg").innerHTML = "<font color='red'>Some Error Occured. Please Try After Sometime..</font>";
						sub.disabled =false;
					} else {
						sub.disabled =true;
						_id.i("phoneMsg", keyValue1[1], "e");
					}
				
			}
		}	
		
		
		
	} else {
		_id.o("phoneMsg").innerHTML = "<font color='red'>Some Error Occured. Please Try After Sometime..</font>";
	}

}	
	
}