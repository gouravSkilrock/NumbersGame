function checkAll(boxVal){
var checkBoxes=document.getElementsByName("check");
for(var i=0;i<checkBoxes.length;i=i+1){
	if(!checkBoxes[i].disabled){
			checkBoxes[i].checked=boxVal;
	}

} 
}

function updateChecked(){
var checkBoxes =document.getElementsByName("check");
var errorMsgDiv=document.getElementById("errorMsg");
var taskIdData="" ;
clearDiv();

for(var i=0;i<checkBoxes.length;i=i+1){
	if(checkBoxes[i].checked == true){
		taskIdData=taskIdData+"&taskIds="+checkBoxes[i].value;
	}

}

if (taskIdData.length < 1) {
	errorMsgDiv.innerHTML="<font color=red>Please choose at least one record</font>";
		return;
	}
var s = confirm("Approve Checked Record");
if(s==false){
	return ;
}

_ajaxUnsync("olaWithdrawalApproval_Update.action?", "afterAjax",taskIdData.replace("&", ""));	
function clearDiv(){
	errorMsgDiv.innerHTML="";

	}	

}

function afterAjax() {
//alert("hello");
	var tempRes = asyncResult.split("N@xt");
	for ( var i = 0; i < tempRes.length; i=i+1) {
		var data = tempRes[i].split(",");
		var rowNum = data[0];
		if (data[1] == "DONE") {
			_id.o('check'+rowNum).checked=false;
			_id.o('check'+rowNum).disabled ="disabled";
			_id.i('status'+rowNum, data[1]);
		}
	}
var excelDiv = document.getElementById("exportdata");
excelDiv.style.display ="block";
}
function validate(){
var transferMode= document.getElementById("transferMode").value;
var status=document.getElementById("status").value;
var modeDiv=document.getElementById("modeMsg");
var statusDiv=document.getElementById("statusMsg");
var dateDiv=document.getElementById("dateMsg");
var startDate =document.getElementById("startDate").value;
var endDate =document.getElementById("endDate").value;
var downDiv =document.getElementById("down");
var dt1 = new Date(startDate);
var dt2 = new Date(endDate);
clearDiv();
if(dt1>dt2){
dateDiv.innerHTML="<font color=red>End Date can't be lesser than Start Date</font>";
return false;
}
if(isSelected(transferMode)){
modeDiv.innerHTML="<font color=red>Please Select A Transfer Mode</font>";
return false;
}
if(isSelected(status)){
statusDiv.innerHTML="<font color=red>Please Select A Status</font>";
return false;
}
function clearDiv(){
modeDiv.innerHTML="";
statusDiv.innerHTML="";
dateDiv.innerHTML="";
downDiv.innerHTML="";
}
function isSelected(val){
if(val=="-1"){
return true;
}
return false;

}
return true;
}




parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/js/withdrawal.js,v $'] = '$Id: withdrawal.js,v 1.2 2016/10/31 09:45:44 neeraj Exp $';
