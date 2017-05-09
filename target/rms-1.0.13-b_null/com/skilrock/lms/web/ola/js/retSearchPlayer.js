$(document).ready(function (){
	fetchActiveWallets(path);
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
	$("#alias_e").html("");
	$("#regDate_e").html("");
}
function validateForm(){
clearDiv();
var alias =$("#alias").val();
var frmDate =new Date($("#regFromDate").val());
var toDate =new Date($("#regToDate").val());
if(!($.trim(alias)=="")){
	if( !isValidAlias(alias)){
		$("#alias_e").html("Invalid Alias Format ");
		return false;
	}
}
if(toDate<frmDate){
	$("#regDate_e").html("Invalid Date Selection : To Date Should be after From Date ");
	return false;
}
	
return true;
}



function isValidAlias(val){
	var flag = true;
	//var regex = /^[A-Za-z0-9\.@_#\/'&\$\+<>\(\)\*;!\?,-]{1,32}$/;
	 var regex=/^([A-Za-z])([A-Za-z0-9\.]){0,30}$/;
	if(!val.match(regex))
		flag=false;
	return flag;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/js/retSearchPlayer.js,v $'] = '$Id: retSearchPlayer.js,v 1.2 2016/10/31 09:45:44 neeraj Exp $';