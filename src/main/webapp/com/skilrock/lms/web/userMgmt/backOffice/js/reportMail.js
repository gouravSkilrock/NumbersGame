
function onTypeChangeValue(typeValue) {	
	//alert("1111111111111");
	document.getElementById('d2').innerHTML="";	
	document.getElementById('d3').style.display="none";			
	//alert("2222222222222");
	document.getElementById('user_type_row').style.display='none';
	//alert("333333333333");
	document.getElementById('isrolehead_row').style.display='none';
	document.getElementById('user_name_row').style.display='none';	
	document.getElementById('agent_list_row').style.display='none';
	document.getElementById('new_user_form_row').style.display='none';
	document.getElementById('new_Form_div').innerHTML="";
	document.getElementById('new_Form_div_button').innerHTML="";
	
	
	
	//alert("4444444444");
	if(typeValue=='BO') {
		document.getElementById('new').style.display='block';	 	
	 	document.getElementById('user_new').checked=false;
		document.getElementById('user_existing').checked=false;						
	} 
	else {
	
		document.getElementById('new').style.display='none';
		document.getElementById('user_existing').checked=true;
		if(typeValue=='RETAILER') {
			//alert(typeValue=='RETAILER')
			//alert("RETAILER == bo_um_assignEmail_FetchAgtEmail.action");
			executeAjax('bo_um_assignEmail_FetchAgtEmail.action', 'agent_list_row', 'agent_user_select');
			document.getElementById('search_button').style.display='block';
		}
		document.getElementById('search_button').style.display='block';		
	}
} 



function checkUserIs(vid) {
	var orgType=document.getElementById('org_type').value;
	
	document.getElementById('user_name_row').style.display='none';
	document.getElementById('new_user_form_row').style.display='none';
	document.getElementById('d2').innerHTML="";
	document.getElementById('new_email_id').readOnly=false;
	document.getElementById('new_Form_div').innerHTML="";
	document.getElementById('new_Form_div_button').innerHTML="";
	document.getElementById('d3').style.display="none";
	
	//alert(document.getElementById('new_email_id').readOnly);
	//alert(orgType);
	if(vid=='user_existing' && orgType=='BO') {
		document.getElementById('user_type_row').style.display='block';
	 	document.getElementById('isrolehead_row').style.display='block';
	 	document.getElementById('search_button').style.display='block';
	 	document.getElementById('isRoleHead_rolehead').checked=true;
	}
	else if(vid=='user_new' && orgType=='BO') {
		document.getElementById('user_type_row').style.display='none';
	 	document.getElementById('isrolehead_row').style.display='none';
		document.getElementById('search_button').style.display='none';
		document.getElementById('new_Form_div').innerHTML="";
		document.getElementById('new_Form_div_button').innerHTML="";
		document.getElementById('new_first').value="";
		document.getElementById('new_last').value="";
		document.getElementById('new_mob').value="";
		document.getElementById('new_email_id').value="";
		document.getElementById('new_user_form_row').style.display='block';
		
		
	}
	else {
		document.getElementById('user_type_row').style.display='none';
	 	document.getElementById('isrolehead_row').style.display='none';
	}
	if(_id.v('isRoleHeadUser')=='N'){
		_id.non('isrolehead_row');
	}
}


function getUserListForReportMail () {
	//alert("djkjgkdhdfghfgjkfhjk");
	document.getElementById('d2').innerHTML="";
	if(!document.getElementById('user_existing').checked) {
		alert("Please Select User Type");
		return false; 
	}
	if(_id.v('isRoleHeadUser')=='N'){
		_id.o('isNotRoleHead_rolehead').checked=true;
	}
	var url;
	var orgType=document.getElementById('org_type').value;	
	
	var userType=document.getElementById('user_type').value;
	
	var userExisting=document.getElementById('user_existing').value;
	var isRoleHeadALL=document.getElementById('isRoleHead_ALL').value;
	
	
	var isNotRoleHeadALL=document.getElementById('isNotRoleHead_rolehead').value;
	var isRoleHeadRolehead=document.getElementById('isRoleHead_rolehead').value;
	
	var isRoleHead;
	
		if(document.getElementById('isRoleHead_rolehead').checked) {
			isRoleHead=isRoleHeadRolehead;
		}
		else if(document.getElementById('isRoleHead_ALL').checked) {
		//alert(isRoleHeadALL);
			isRoleHead=isRoleHeadALL;
		}else {
			isRoleHead=isNotRoleHeadALL;
		}
	
		
		if(orgType=='BO') {
			if(userType=='-1') {
				alert("Please select user type ");
				document.getElementById('user_type').focus();
				return false;
			}
			url="bo_um_assignEmail_FetchUserRepMail.action?orgType="+orgType+"& userType="+userType+"& isRoleHead="+isRoleHead;
			//alert(url);
		}
		else if(orgType=='AGENT' ) {
			url = "bo_um_assignEmail_FetchUserRepMail.action?orgType="+orgType;
			//alert(url);
		}else if(orgType=='RETAILER') {
			var ownerId=document.getElementById('agent_user_select').value
			url = "bo_um_assignEmail_FetchUserRepMail.action?orgType="+orgType+"& ownerId="+ownerId;
		}
		
		// call the ajax to get the user name list
		executeAjax(url, 'user_name_row', "user_name_select");
		
	
}


function checkDetails() {
	//alert(document.getElementById('user_existing').checked);
	if(document.getElementById('user_existing').checked)
	return true;
}


var mainDiv;
var selectDiv;
function executeAjax(url, div, select) {		
	mainDiv = div;
	selectDiv=select;
	_resp  = _ajaxCall(url);
	if(_resp.result){
		var resStr = _resp.data;
		if(resStr=='NO') {
				alert("NO Record Found");				
				var newSelectbox = document.getElementById(selectDiv);	
				newSelectbox.options.length=1;
				document.getElementById(mainDiv).style.display="none";
			}			
			else {				
				createUserNameSelect(resStr, selectDiv);				
				document.getElementById(mainDiv).style.display="block";				
			}
	}else{
		alert("Problem:------");
	}
}


function createUserNameSelect(responseData, selectDiv) {
	var newSelectbox = document.getElementById(selectDiv);
	//alert(selectDiv+"  "+newSelectbox+"  "+responseData);	
	newSelectbox.options.length=1;
	var retList = new Array();
	userList=responseData.split(",");			
	for(i=0;i<userList.length;i++) {
		var list=userList[i].split("=");
		var key=list[1].replace(" ","");
		var val=list[0].replace(" ","");
		
		var splitval = val.split("-");
		//alert(val +"   "+key+"       " +splitval[2]);
		var opt = new Option(key, val);		
		newSelectbox.options[i+1] = opt;
		if(splitval[2] == "UN_REG_USER") {
			newSelectbox.options[i+1].style.color = "red";			
		}		
	}
}

function validateName(value, idv) {
	if (value == "") {
	document.getElementById("fname").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
		return false;	
	} 
	else {
		var alphaExp = /[a-zA-Z]/;
		if (value.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = value;
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
}



function verifyEmailAndGetPrivList(){

	var email=document.getElementById('new_email_id').value;
	//alert(email);
	if (email == "") {		
		document.getElementById("email_error").innerHTML = "<font color = 'red'>Please Enter Email Address</font>";
		return false;
	} else {		
		var valid = ValidateEMail(email);		
		if (valid) {
			document.getElementById("email_error").innerHTML = "";
		} else {
			document.getElementById("email_error").innerHTML = "<font color = 'red'>Please Enter Correct Email Address</font>";
			return false;
		}
	}
	
	var newUrl="bo_um_assignEmail_VerifyAssignEmail.action?email="+escape(email);
	//alert(newUrl);
	_resp  = _ajaxCall(newUrl);
	if(_resp.result){
		var resStr = _resp.data;
		if(resStr=='NO') {
				//alert("NO Record Found");
				document.getElementById('new_Form_div').innerHTML="";
				document.getElementById('new_Form_div_button').innerHTML="";
				document.getElementById('email_error').innerHTML="This EMAIL ID already registered For Another User";
			}			
			else {
				document.getElementById('email_error').innerHTML="";								
				createPrivCheckBoxesForNew(resStr);
				document.getElementById('new_email_id').readOnly=true;
				
			}
	}else{
		alert("Problem:------");
	}
}


function createPrivCheckBoxesForNew(responseData) {

	var title;
	var id;
	var status;
	
	var retList = new Array();
	userList=responseData.split(",");
	
	globalLength=userList.length;
	
		// if user already registered 
	var list=userList[0].split("=");
	title=list[1].replace(" ","");
	id=list[0].replace(" ","");
	status=list[2].replace(" ","");
	var table2='<table cellpadding="4" cellspacing="0" border="0" width="600">';
	var table='<table cellpadding="4" cellspacing="0" border="0" width="600">';
	var n=-1,m=-3; 
	var tr="";
	//alert(userList.length);
	for(k=0;k<userList.length;k++) {
		var list=userList[k].split("=");
		
		title=list[1].replace(" ","");
		id=list[0].replace(" ","");
		status=list[2].replace(" ","");	
			
				//tr=tr+"";
				n+=1; m+=1;
					if(n%3==0) {
						tr=tr+"<tr>";
					}
					tr=tr+'<td align="left"><input type="checkbox" name="priv" id="priv'+k+'" value="'+id+'" ';
						if(status=='ACTIVE') 
							tr=tr+' checked="checked" ';
					tr=tr+('/>&nbsp;'+title+"</td>");	
					if(m%3==0) {
						tr=tr+"</tr>";
					}		
						//alert("sfhdjkdfhjkdghjk0 "+tr);
		}	
		//tr=tr+"<tr><td align='right' colspan='3'><input type='button' value ='Submit' onclick='createNewUser(document.update_form.priv)' /> ";	 					 				
		 table=table+tr+'</table>';
		 table2=table2+"<tr><td width='132'>&nbsp;</td><td align='left' ><input type='button' value ='Submit' class='button' onclick='createNewUser(document.update_form.priv)' /></td></tr></table>";
		//alert(table);
	document.getElementById('new_Form_div').innerHTML=table;
	document.getElementById('new_Form_div_button').innerHTML=table2;		
}


function createNewUser(checkList) {

	var first=document.getElementById('new_first');
	var last=document.getElementById('new_last');
	var mob=document.getElementById('new_mob');
	var email=document.getElementById('new_email_id');
	
	var flag = isST3Filled(first.value, last.value, mob.value, email.value);	
	if(!flag) return false;
	
	var checkedValues="";
	var uncheckedValues="";
	checkList=document.getElementsByName('priv'); 
	for (i = 0; i < checkList.length; i++) {
		if(checkList[i].checked) {
			checkedValues = checkedValues + checkList[i].value +",";
		}else {
			uncheckedValues=uncheckedValues+checkList[i].value +",";
		}
	}
	
	var orgType=document.getElementById('org_type').value;
	
	
	var newUrl="bo_um_assignEmail_NewUserForEmail.action?orgType="+orgType+"&firstName="+first.value+"& lastName="+last.value+"& mobile="+mob.value+"& email="+escape(email.value)+"& activePriv="+checkedValues+"& inactivePriv="+uncheckedValues;
	//alert(newUrl);
	
	createAndUpdate(newUrl);
	first.value="";
	last.value="";
	email.value="";
	mob.value="";
	document.getElementById('new_user_form_row').style.display='none';
	document.getElementById('new_Form_div').innerHTML="";
	document.getElementById('new_Form_div_button').innerHTML="";
	
}










function isST3Filled(first, last, mob, email) {
	var alphaExp = /[a-zA-Z]/;
	var alphaNumExp = /[a-zA-Z0-9]/;
	var isInValid = false;

	if (first == "") {
		isInValid = true;
		document.getElementById("new_first_error").innerHTML = "<font color = 'red'>Please Enter First Name</font>";
	} else {
		if (first.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = first;
			var flg = false;
   //  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);
				if (strValidChars.indexOf(strChar) != -1) {
					document.getElementById("new_first_error").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
					isInValid = true;
					flg = true;
					break;
				}
			}
			if (!flg) {
				document.getElementById("new_first_error").innerHTML = "";
			}
		} else {
			document.getElementById("new_first_error").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
			isInValid = true;
		}
	}
	if (last == "") {
		document.getElementById("new_last_error").innerHTML = "<font color = 'red'>Please Enter Last Name</font>";
		isInValid = true;
	} else {
		if (last.match(alphaExp)) {
			var strValidChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?1234567890";
			var strChar;
			var strString = last;
			var flg = false;
   //  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);
				if (strValidChars.indexOf(strChar) != -1) {
					document.getElementById("new_last_error").innerHTML = "<font color = 'red'>Please Enter Valid Name (Special Characters and Numbers not Allowed)</font>";
					isInValid = true;
					flg = true;
					break;
				}
			}
			if (!flg) {
				document.getElementById("new_last_error").innerHTML = "";
			}
		} else {
			document.getElementById("new_last_error").innerHTML = "<font color = 'red'>Please Enter Valid Name</font>";
			isInValid = true;
		}
	}
	
	if (email == "") {
		document.getElementById("email_error").innerHTML = "<font color = 'red'>Please Enter Email Address</font>";
		isInValid = true;
	} else {
	
		var valid = ValidateEMail(email);

		if (valid) {
			document.getElementById("email_error").innerHTML = "";
		} else {
			document.getElementById("email_error").innerHTML = "<font color = 'red'>Please Enter Correct Email Address</font>";
			isInValid = true;
		}
	}
	if (mob == "") {

		document.getElementById("new_mob_error").innerHTML = "<font color = 'red'>Please Enter Phone Number</font>";
		isInValid = true;
	} else {

		if (isNumeric(mob)) {
		
			document.getElementById("new_mob_error").innerHTML = "<font color = 'red'>Please Enter Correct Value For Phone Number</font>";
			isInValid = true;
		} else {
	
			document.getElementById("new_mob_error").innerHTML = "";
		}
	}
	
	if (isInValid) {
		return false;
	}
	return true;
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




function  getPriviledgesList(tValue) {
	//alert(tValue)
	document.getElementById('d2').innerHTML="";		
	var inputValue = tValue.split("-");
	
	if(inputValue[2]=='REG_USER' || inputValue[2]=='NEW_USER') {
		var conf=confirm("This User Already Registered For Email Report. Do You Want to Update ?");
		if(!conf) {
			return false;
		}
	} 
	var orgType=document.getElementById('org_type').value;
	var url ="bo_um_assignEmail_FetchEmailPriv.action?user="+ inputValue[2]+"& userId="+inputValue[0]+"& orgType="+orgType+"& email="+escape(inputValue[1]); 
	getEmailPriviledges(url, "", "");
}




function getEmailPriviledges(newUrl, div, type) {		
	
	mainDiv = div;
	//alert(newUrl +"      div == "+mainDiv);
	_resp  = _ajaxCall(newUrl);
	if(_resp.result){
		var resStr = _resp.data;	
			if(resStr=='NO') {
				alert("NO Record Found");
				document.getElementById('d2').style.display="none";
				document.getElementById('d3').style.display="none";
			}			
			else {
				document.getElementById('d2').style.display="block";	
				document.getElementById('d3').style.display="block";			
				createPrivCheckBoxes(resStr);
				//document.getElementById('form_update').style.display="block";
			}	
	}else{
		alert("Problem:------");
	}
}


function createPrivCheckBoxes(responseData) {

	var title;
	var id;
	var status;
	
	var retList = new Array();
	userList=responseData.split(",");
	
	globalLength=userList.length;
	
		// if user already registered 
	var list=userList[0].split("=");
	title=list[1].replace(" ","");
	id=list[0].replace(" ","");
	status=list[2].replace(" ","");
	if(title=='REG') {	
		var conf=confirm("This User Already Registered For Email Report. Do You Want to Update ?");
		if(!conf) {
			document.getElementById('d2').innerHTML="";
			return false;
		}
	}
	var table='<table cellpadding="4" cellspacing="0" border="0" width="600">';
	var table2='';	
	var n=-1,m=-3; 
	var tr="";
	//alert(userList.length);
	for(k=1;k<userList.length;k++) {
		var list=userList[k].split("=");
		
		title=list[1].replace(" ","");
		id=list[0].replace(" ","");
		status=list[2].replace(" ","");	
			
				//tr=tr+"";
				n+=1; m+=1;
					if(n%3==0) {
						tr=tr+"<tr>";
					}
					tr=tr+'<td align="left"><input type="checkbox" name="priv" id="priv'+k+'" value="'+id+'" ';
						if(status=='ACTIVE') 
							tr=tr+' checked="checked" ';
					tr=tr+('/>&nbsp;'+title+"</td>");	
					if(m%3==0) {
						tr=tr+"</tr>";
					}		
						//alert("sfhdjkdfhjkdghjk0 "+tr);
		}	
		
		//tr=tr+"<tr><td>&nbsp;</td><td align='left' ><input type='button' class='button' value ='Update' onclick='updatePrivlegeList(document.update_form.priv)' /> ";	 					 				
		 table=table+tr+'</table>';
		 table2=" <table cellpadding='4' cellspacing='0' border='0' width='600'><tr><td width='132'>&nbsp;</td><td align='left' ><input type='button' class='button' value ='Update' onclick='updatePrivlegeList(document.update_form.priv)' /></td></tr></table>";
		//alert(table);
	document.getElementById('d2').innerHTML=table;
	document.getElementById('d3').innerHTML=table2;
	
	
}

var globalLength;

function updatePrivlegeList(checkList) {
	
	var checkedValues="";
	var uncheckedValues="";
	var privList = document.getElementsByName('priv'); 
	//alert("checkList "+privList)
	//alert("checkList length = "+privList.length);
	for (i = 0; i < privList.length; i++) {
		if(privList[i].checked) {
			checkedValues = checkedValues + privList[i].value +",";
		}else {
			uncheckedValues=uncheckedValues+privList[i].value +",";
		}
	}
	//alert(checkList.length+"  "+checkedValues);
	var orgType=document.getElementById('org_type').value;
	
	var userName=document.getElementById('user_name_select').value;		
	var inputValue = userName.split("-");
	
	
	var newUrl="bo_um_assignEmail_UpdatePriv.action?orgType="+orgType+"& user="+ inputValue[2]+"& userId="+inputValue[0]+"& email="+escape(inputValue[1])+"& activePriv="+checkedValues+"& inactivePriv="+uncheckedValues;
	
	createAndUpdate(newUrl );
	
}

function createAndUpdate(url) {
	_resp  = _ajaxCall(url);
	if(_resp.result){
		var resStr = _resp.data;
		if(resStr=='NO') {				
				document.getElementById('d2').innerHTML="NO Record Found";
				document.getElementById('d3').style.display="none";	
			}			
			else {
				document.getElementById('d2').innerHTML="<table><tr><td class='data'>"+resStr+"</td></tr></table>";
					
				document.getElementById('d3').style.display="none";				
			}
	}else{
		alert("Problem:------");
	}
}




parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/reportMail.js,v $'] = '$Id: reportMail.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';