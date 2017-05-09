

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
		currGovCommRate.value=splitValues[1].split("_")[0];
		document.getElementById('cGovComm').style.display="block";				
	}
}

function changeCommRate(type) {
	var commRate = document.getElementById('game_id').value

	var splitValues=commRate.split(":");
	var index = (type == 'SALE') ? 0 : 1;
	var currGovCommRate=document.getElementById('cGovCommRate');
	currGovCommRate.value=splitValues[1].split("_")[index];
	document.getElementById('cGovComm').style.display="block";				
}

function getGameListing(url) {
	//alert("aa");
	
	var serviceVal=document.getElementById('serviceName').value;
	if(serviceVal!='-1' && (serviceVal=='DG' || serviceVal=='SE' || serviceVal=='IW' || serviceVal=='SLE' || serviceVal=='VS')){
		_resp  = _ajaxCall(url);
		_respData=_resp.data;
		//alert(_respData);
		var resData  = _respData.split("|:");
		var retObj = _id.o("game_id");
		retObj.options.length=1;
		for(var i =0; i<resData.length; i=i+1) {	
			var valArr=resData[i].split("||");
				var opt = new Option(valArr[1], valArr[0]);
				retObj.options[i+1] = opt;
		}
	}else{
		document.getElementById('game_id').value=-1;
		var retObj = _id.o("game_id");
		retObj.options.length=1;
	}

	if(serviceVal=='DG' || serviceVal=='SLE' || serviceVal=='IW' || serviceVal=='VS')
		$("#trType").show();
	else
		$("#trType").hide();
}
function checkFormDetails() {	
	var flag=true;
	var currGovCommRate=document.getElementById('game_error');
	var newGovComm_error=document.getElementById('newGovComm_error');
	var serviceName = document.getElementById('serviceName').value;
	var tValue=document.getElementById('game_id').value;
	var newGovComm=document.getElementById('newGovComm').value;

	if(serviceName=="-1") {		
		document.getElementById('service_error').innerHTML="Please Select Service.";
		flag=false;		
	} else {
		document.getElementById('service_error').innerHTML="";
	}

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
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/updateGovCommRate.js,v $'] = '$Id: updateGovCommRate.js,v 1.4 2016/10/31 09:46:21 neeraj Exp $';