$(document).ready(function (){
	//fetchActiveWallets(path);
	getRetList(path);
	$("#regType").change(function(){
		//alert(this.value);
		if(this.value=="DIRECT"){
			$("#dateDiv").hide();
		}else{
			$("#dateDiv").show();
		}
		
		
	});
	
});
function clearDiv(){
	$("#retOrgId_e").html("");
	$("#alias_e").html("");
	$("#regDate_e").html("");
	$("#reportDiv").html("");
}
function validateForm(){
clearDiv();
var ret =$("#retOrgId").val();
var alias =$("#alias").val();
var frmDate =new Date($("#regFromDate").val());
var toDate =new Date($("#regToDate").val());
if(ret=="-1"){
	$("#retOrgId_e").html("Please Select Retailer ");
	return false;
}
/*if(!($.trim(alias)=="")){
	if( !isValidAlias(alias)){
		$("#alias_e").html("Invalid Alias Format ");
		return false;
	}
}*/
if(toDate<frmDate){
	$("#regDate_e").html("Invalid Date Selection : To Date Should be after From Date ");
	return false;
}
	
	
return true;
}

function getRetList(agentOrgName) {
	var text = _ajaxCall(path+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=Retailer");
	var firstRetArr = text.data.split(":");
	var retObj = _id.o("retOrgId");
	retObj.innerHTML="";
	retObj.innerHTML="<option value='-1'>--Please Select--</option>";
		
	for (var i = 0; i <firstRetArr.length - 1; i=i+1) {
		var retNameArray = firstRetArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function isValidAlias(val){
	var flag = true;
	//var regex = /^[A-Za-z0-9\.@_#\/'&\$\+<>\(\)\*;!\?,-]{1,32}$/;
	 var regex=/^([A-Za-z])([A-Za-z0-9\.]){0,30}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/ola/js/agtSearchPlayer.js,v $'] = '$Id: agtSearchPlayer.js,v 1.3 2016/10/31 09:46:24 neeraj Exp $';
