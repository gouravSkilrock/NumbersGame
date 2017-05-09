

function getCurrentGovRate(tValue) {	
	document.getElementById('newGovComm_error').innerHTML="";
	document.getElementById('game_error').innerHTML="";	
	if(tValue=="-1") {
		var currGovCommRate=document.getElementById('cGovCommRate');
		currGovCommRate.value="";
		document.getElementById('cGovComm').style.display="none"; 	
	}
	else {
		var splitValues=tValue.split(":");		
		var currGovCommRate=document.getElementById('cGovCommRate');
		currGovCommRate.value=splitValues[1];
		document.getElementById('cGovComm').style.display="block";				
	}
}


function checkFormDetails() {	
	var flag=true;
	var currGovCommRate=document.getElementById('game_error');
	var newGovComm_error=document.getElementById('newGovComm_error');
	var tValue=document.getElementById('game_id').value;
	var newGovComm=document.getElementById('newGovComm').value;
	
	if(tValue=="-1") {		
		currGovCommRate.innerHTML="Please Select the Game to Change Good Cause Rate.";
		flag=false;		
	} else {
		currGovCommRate.innerHTML="";
	}
	
	if(isDecimal(newGovComm)) {
		newGovComm_error.innerHTML="Please Enter Valid Entry for New Good Cause Rate";
		flag=false;	
	}
	else if(parseFloat(newGovComm)>100) {
		newGovComm_error.innerHTML="New Good Cause Rate must be less than 100";
		flag=false;
	}
	else  {
		newGovComm_error.innerHTML="";
	}
	
	return flag;
}

function isDecimal(sText1) {
	var sText = sText1;
	var validChars = "0123456789.";
	var isNumber = false;
	var myChar;
	var count = 0;
	if (sText.length == 0) {
		isNumber = true;
	}
	for (i = 0; i < sText.length; i++) {
		myChar = sText.charAt(i);
		if (myChar == ".") {
			count++;
		}
		if (validChars.indexOf(myChar) == -1 || count > 1) {
			isNumber = true;
			break;
		}
	}	
	return isNumber;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/updateGovCommRate.js,v $'] = '$Id: updateGovCommRate.js,v 1.1 2010/04/01 04:22:22 gaurav Exp $';