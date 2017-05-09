
function getEmailPrivList(url, divId) {
	_resp  = _ajaxCall(url);
 	if(_resp.result){
 	_respData=_resp.data;
 	var emailPrivList = _respData.split(","),table = '<table><tr>',idAndTitle="";
	for(var i=0,len=emailPrivList.length; i<len; i++) {
		idAndTitle = emailPrivList[i].split("=");
		var tr = '<td><input type="checkbox" name="emailPrivId" value="'+idAndTitle[0].replace(" ", "")+'"/>'+
				'<label class="checkboxLabel">'+idAndTitle[1]+'</label></td>';
		table = table + tr ;		
	}
	table = table + '</tr></table>';
	_id.i(divId,table);
	}else{
	_id.i(divId,'Error');
	}
 }


// done by Arun Upadhyay for Common ajax call in whole javascript
 function _commonAjaxFunction(passedUrl) {
	var MAXIMUM_WAITING_TIME = 3*60*1000;
	 var xhReq = new createXMLHttpRequest();
		var _c="?";
		if(passedUrl.indexOf("?")!=-1){_c="&";}
		passedUrl=passedUrl+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	 
	 xhReq.open("GET", passedUrl, false);
	 var requestTimer = setTimeout(function() {
       xhReq.abort();
       _commonAjaxFunction(passedUrl);
     }, MAXIMUM_WAITING_TIME);
     var serverResponse;
   	 xhReq.onreadystatechange = function() {
									if (xhReq.readyState != 4)  { return; }
									   clearTimeout(requestTimer); 
									    if (xhReq.status != 200)  {										      
									       return;
									    }
									    serverResponse = xhReq.responseText;
									   	return ;  
								 };

		xhReq.setRequestHeader("If-Modified-Since", new Date().getTime());
		xhReq.send(null);
	
		 //alert(serverResponse); 
	 return serverResponse;
 }

 function createXMLHttpRequest() {
   	try { return new XMLHttpRequest(); } catch(e) {}
   	try { return new ActiveXObject("Msxml2.XMLHTTP"); } catch (e) {}
   	try { return new ActiveXObject("Microsoft.XMLHTTP"); } catch (e) {}
   	alert("XMLHttpRequest not supported");
   return null;
 }







function initialFocusRet() {
	document.newuser.firstName.focus();
}
function isST3Filled() {
	var alphaExp = /[a-zA-Z]/;
	var alphaNumExp = /[a-zA-Z0-9]/;
	var isInValid = false;
	if (document.newuser.firstName.value == "") {
		isInValid = true;
		document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter First Name</font>";
	} else {
		if (document.newuser.firstName.value.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = document.newuser.firstName.value;
			var flg = false;
   //  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);
				if (strValidChars.indexOf(strChar) != -1) {
					document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
					isInValid = true;
					flg = true;
					break;
				}
			}
			if (!flg) {
				document.getElementById("fname").innerHTML = "";
			}
		} else {
			document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
			isInValid = true;
		}
	}
	if (document.newuser.lastName.value == "") {
		document.getElementById("lname").innerHTML = "<font color = 'red'>Please Enter Last Name</font>";
		isInValid = true;
	} else {
		if (document.newuser.lastName.value.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = document.newuser.lastName.value;
			var flg = false;
   //  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);
				if (strValidChars.indexOf(strChar) != -1) {
					document.getElementById("lname").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
					isInValid = true;
					flg = true;
					break;
				}
			}
			if (!flg) {
				document.getElementById("lname").innerHTML = "";
			}
		} else {
			document.getElementById("lname").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
			isInValid = true;
		}
	}
	if (document.newuser.email.value == "") {
		document.getElementById("Email").innerHTML = "<font color = 'red'>Please Enter Email Address</font>";
		isInValid = true;
	} else {
		var valid = ValidateEMail(document.newuser.email.value);
		if (valid) {
			document.getElementById("Email").innerHTML = "";
		} else {
			document.getElementById("Email").innerHTML = "<font color = 'red'>Please Enter Correct Email Address</font>";
			isInValid = true;
		}
	}
	if (document.newuser.phone.value == "") {
		document.getElementById("Phone").innerHTML = "<font color = 'red'>Please Enter Phone Number</font>";
		isInValid = true;
	} else {
		if (isNumeric(document.newuser.phone.value)) {
			document.getElementById("Phone").innerHTML = "<font color = 'red'>Please Enter Correct Value For Phone Number</font>";
			isInValid = true;
		} else {
			document.getElementById("Phone").innerHTML = "";
		}
	}
	if (document.newuser.userName.value == "") {
		document.getElementById("agent").innerHTML = "<font color = 'red'>Please Enter "+tierMap['AGENT']+" Name</font>";
		isInValid = true;
	} else {
		if (document.newuser.userName.value.match(alphaNumExp)) {
			if (document.newuser.userName.value.match(" ")) {
				document.getElementById("agent").innerHTML = "<font color = 'red'>Please Enter Valid Value (No Spaces)</font>";
			} else {
				document.getElementById("agent").innerHTML = "";
			}
		} else {
			document.getElementById("agent").innerHTML = "<font color = 'red'>Please Enter Valid Value</font>";
			isInValid = true;
		}
	}
	if (document.newuser.secQues.value == -1) {
		document.getElementById("sQues").innerHTML = "<font color = 'red'>Please Select Secret Question</font>";
		isInValid = true;
	} else {
		document.getElementById("sQues").innerHTML = "";
	}
	if (document.newuser.secAns.value == "") {
		document.getElementById("sAns").innerHTML = "<font color = 'red'>Please Enter Secret Answer</font>";
		isInValid = true;
	} else {
		if (document.newuser.secAns.value.match(alphaNumExp)) {
			document.getElementById("sAns").innerHTML = "";
		} else {
			document.getElementById("sAns").innerHTML = "<font color = 'red'>Please Enter Valid Value</font>";
			isInValid = true;
		}
	}
	if (isInValid) {
		return false;
	}
	return true;
}
function isST3BOUserValidation() {
	var isInValid = false;
	if (document.newuser.firstName.value == "") {
		isInValid = true;
		document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter First Name</font>";
	} else {
		var alphaExp = /[a-zA-Z]/;
		if (document.newuser.firstName.value.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = document.newuser.firstName.value;
			var flg = false;
   //  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);
				if (strValidChars.indexOf(strChar) != -1) {
					document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
					isInValid = true;
					flg = true;
					break;
				}
			}
			if (!flg) {
				document.getElementById("fname").innerHTML = "";
			}
		} else {
			document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
			isInValid = true;
		}
	}
	if (document.newuser.lastName.value == "") {
		document.getElementById("lname").innerHTML = "<font color = 'red'>Please Enter Last Name</font>";
		isInValid = true;
	} else {
		var alphaExp = /[a-zA-Z]/;
		if (document.newuser.lastName.value.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = document.newuser.lastName.value;
			var flg = false;
   //  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);
				if (strValidChars.indexOf(strChar) != -1) {
					document.getElementById("lname").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
					isInValid = true;
					flg = true;
					break;
				}
			}
			if (!flg) {
				document.getElementById("lname").innerHTML = "";
			}
		} else {
			document.getElementById("lname").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
			isInValid = true;
		}
	}
	if (document.newuser.email.value == "") {
		document.getElementById("Email").innerHTML = "<font color = 'red'>Please Enter Email Address</font>";
		isInValid = true;
	} else {
		var valid = ValidateEMail(document.newuser.email.value);
		if (valid) {
			document.getElementById("Email").innerHTML = "";
		} else {
			document.getElementById("Email").innerHTML = "<font color = 'red'>Please Enter Correct Email Address</font>";
			isInValid = true;
		}
	}
	if (document.newuser.phone.value == "") {
		document.getElementById("Phone").innerHTML = "<font color = 'red'>Please Enter Phone Number</font>";
		isInValid = true;
	} else {
		if (isNumeric(document.newuser.phone.value)) {
			document.getElementById("Phone").innerHTML = "<font color = 'red'>Please Enter Correct Phone Number</font>";
			isInValid = true;
		} else {
			document.getElementById("Phone").innerHTML = "";
		}
	}
	
	if (document.newuser.orgName.value == -1) {
		document.getElementById("orgError").innerHTML = "<font color = 'red'>Please Select Organization</font>";
		isInValid = true;
	} else {
		document.getElementById("orgError").innerHTML = "";
	}
	
	
	
	if (document.newuser.userName.value == "") {
		document.getElementById("user").innerHTML = "<font color = 'red'>Please Enter User Name</font>";
		isInValid = true;
	} else {
		document.getElementById("user").innerHTML = "";
	}
	if (document.newuser.role.value == -1) {
		document.getElementById("Role").innerHTML = "<font color = 'red'>Please Select Role </font>";
		isInValid = true;
	} else {
		document.getElementById("Role").innerHTML = "";
	}
	if (document.newuser.secQues.value == -1) {
		document.getElementById("sQues").innerHTML = "<font color = 'red'>Please Select Secret Question</font>";
		isInValid = true;
	} else {
		document.getElementById("sQues").innerHTML = "";
	}
	if (document.newuser.secAns.value == "") {
		document.getElementById("sAns").innerHTML = "<font color = 'red'>Please Enter Secret Answer</font>";
		isInValid = true;
	} else {
		document.getElementById("sAns").innerHTML = "";
	}
	if (isInValid) {
		return false;
	}
	return true;
}
function isValidValues() {
	var isInValid = false;
	if (document.userDetails.emailId.value == "") {
		document.getElementById("email").innerHTML = "<font color = 'red'>Please Enter Email Address</font>";
		isInValid = true;
	} else {
		var valid = ValidateEMail(document.userDetails.emailId.value);
		if (valid) {
			document.getElementById("email").innerHTML = "";
		} else {
			document.getElementById("email").innerHTML = "<font color = 'red'>Please Enter Correct Email Address</font>";
			isInValid = true;
		}
	}
	if (document.userDetails.phoneNbr.value == "") {
		document.getElementById("phone").innerHTML = "<font color = 'red'>Please Enter Phone Number</font>";
		isInValid = true;
	} else {
		if (isNumeric(document.userDetails.phoneNbr.value)) {
			document.getElementById("phone").innerHTML = "<font color = 'red'>Please Enter Correct Phone Number</font>";
			isInValid = true;
		} else {
			document.getElementById("Phone").innerHTML = "";
		}
	}
	if (isInValid) {
		return false;
	}
	return true;
}
function ValidateEMail(emailStr) {
var ldot = emailStr.indexOf(".");
	var emailPat = /^(.+)@(.+)$/;
	var specialChars = "\\(\\)<>@,;:\\\\\\\"\\.\\[\\]";
	var validChars = "[^\\s" + specialChars + "]";
	var quotedUser = "(\"[^\"]*\")";
	var ipDomainPat = /^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/;
	var atom = validChars + "+";
	var word = "(" + atom + "|" + quotedUser + ")";
	var userPat = new RegExp("^" + word + "(\\." + word + ")*$");
	var domainPat = new RegExp("^" + atom + "(\\." + atom + ")*$");
	var matchArray = emailStr.match(emailPat);
	if (matchArray == null) {
		return false;
	}
	var user = matchArray[1];
	var domain = matchArray[2];
	//alert("user "+user);
	//alert("domain "+domain);
	if (user.match(userPat) == null) {
		return false;
	}
	var IPArray = domain.match(ipDomainPat);
	if (IPArray != null) {
		for (var i = 1; i <= 4; i++) {
			if (IPArray[i] > 255) {
				return false;
			}
		}
		return true;
	}
	var domainArray = domain.match(domainPat);
	if (domainArray == null) {
		return false;
	}
	var atomPat = new RegExp(atom, "g");
	var domArr = domain.match(atomPat);
	//alert("domArr "+domArr);
	var len = domArr.length;
	if (domArr[domArr.length - 1].length < 2 || domArr[domArr.length - 1].length > 4) {
		return false;
	}
	if (len < 2) {
		var errStr = "This address is missing a hostname!";
		return false;
	}
	if (isNumcont(emailStr.substring(ldot + 1))) {		
		return false;
	}
	return true;
}
function ValidateEMail1(emailStr) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3}[a-zA-z])+$/.test(emailStr)) {
		return true;
	}
	alert("Invalid E-mail Address! Please re-enter.");
	return false;
}

function isNumeric(value) {
	var strValidChars = "0123456789";
	var strChar;
	var flag = false;
	var strString = value;
	
   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			flag = true;
			break;
		}
	}
	return flag;
}
function isNumcont(value) {
	var strValidChars = "0123456789";
	var strChar;
	var strString = value;
	
   //  test strString consists of valid characters listed above
	for (i = 0; i < strString.length; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) != -1) {
			return true;
		}
	}
	return false;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/userReg.js,v $'] = '$Id: userReg.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';