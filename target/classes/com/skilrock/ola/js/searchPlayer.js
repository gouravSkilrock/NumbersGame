$(document).ready(function (){
	//fetchActiveWallets(path);
	getAgentList(path);
	$("#agentOrgId").change(function(){
		//alert(this.value);
		clearDiv();
		getRetList(this.value);
		
	});
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
	$("#agentName_e").html("");
	$("#retOrgId_e").html("");
	$("#alias_e").html("");
	$("#regDate_e").html("");
	$("#reportDiv").html("");
}
function validateForm(){
clearDiv();
var agent =$("#agentOrgId").val();
var ret =$("#retOrgId").val();
var alias =$("#alias").val();
var frmDate =new Date($("#regFromDate").val());
var toDate =new Date($("#regToDate").val());
if(agent=="-1"){
	
	$("#agentName_e").html("Please Select  Agent ");
	return false;
}
if(ret=="-1" || ret==""){
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
function getAgentList() {
	var text = _ajaxCall(path+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var agtObj = _id.o("agentOrgId");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var agtNameArray = firstArr[i].split("|");
		var opt = new Option(agtNameArray[0].toUpperCase(), agtNameArray[1]);
		agtObj.options[i + 1] = opt;
	}
}
function getRetList(agentOrgName) {
	var agent =$("#agentOrgId").val();
	if(agent=="-1"){
		
		$("#agentName_e").html("Please Select  Agent ");
		return false;
	}
	var text = _ajaxCall(path+"/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId="+agentOrgName);
	var firstRetArr = text.data.split(":");
	var retObj = _id.o("retOrgId");
	$('#retOrgId').empty().append($('<option></option>').val("-1").html("--Please Select--"));
	
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
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/ola/js/searchPlayer.js,v $'] = '$Id: searchPlayer.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $';