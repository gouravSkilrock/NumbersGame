function checkAll(boxVal){
var checkBoxes=document.getElementsByName("check");
for(var i=0;i<checkBoxes.length;i=i+1){
	if(!checkBoxes[i].disabled){
			checkBoxes[i].checked=boxVal;
	}

} 
}
function sendSMS(id){
var s = confirm("Send SMS To Player");
var taskIdData="taskId="+id.split("submit")[1] ;
if(s==true){_ajaxUnsync("sendOlaDepositSMS.action?","afterAjax",taskIdData);}
	
}
function afterAjax() {
//alert("hello");
	var tempRes = asyncResult;
	var data =tempRes.split("N@xt");
	//for ( var i = 0; i < tempRes.length; i=i+1) {
		//var data = tempRes[i].split(",");
		//var rowNum = data[0];
		//if (data[1] == "APPROVED") {
			//_id.o('check'+rowNum).checked=false;
			//_id.o('check'+rowNum).disabled ="disabled";
			//_id.i('status'+rowNum, data[1]);
		//}
	//}
var btnDiv = document.getElementById("smsBtn"+data[1]);
btnDiv.style.display ="none";
var resultDiv =document.getElementById("result"+data[1]);
resultDiv.innerHTML =data[0];
}
function validate(){
var pinType= document.getElementById("pinType").value;
var amount=document.getElementById("amount").value;
var typeDiv=document.getElementById("typeMsg");
var amountDiv=document.getElementById("amountMsg");
clearDiv();

if (amount == "") {
		amountDiv.innerHTML="<font color=red>Please Enter Amount </font>";
return false;
	} else {
		amountDiv.innerHTML="";
	}
	if(amount <= 0)
	{
		amountDiv.innerHTML="<font color=red>Please Enter  amount greater then zero</font>";
		return false;
	}
	if (isNaN(amount)) {
	amountDiv.innerHTML="<font color=red>Please enter valid amount</font>";
		return false;
	}
	var amountArr = amount.split('.');
	if (amountArr.length > 1) {
		if (amountArr[1].length > 2) {
		amountDiv.innerHTML="<font color=red>Please enter valid amount</font>";
			return false;
		}
	}

if(isSelected(pinType)){
typeDiv.innerHTML="<font color=red>Please Select Pin Type</font>";
return false;
}
function clearDiv(){
typeDiv.innerHTML="";
amountDiv.innerHTML="";
}
function isSelected(val){
if(val=="-1"){
return true;
}
return false;

}

return true;
}
function fetchRetOrgList(path){

var url = path + "/com/skilrock/lms/web/ola/OLA_MGT/retOrgList.action";
	//alert(url);
	var _resp = _ajaxCall(url);
	//alert(_resp.data)
	if (_resp.result && _resp.data != "") {
		var _respData = _resp.data;
		var option = _id.o("retailer");
		var orgList = _respData.split("N@xt");
		for ( var i = 0; i < orgList.length; i++) {
			var gkey = orgList[i];
			var dvalue = orgList[i];
			var opt = new Option(dvalue, gkey);
			option.options[i + 1] = opt;

		}

		
	}




}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/js/sendSMS.js,v $'] = '$Id: sendSMS.js,v 1.2 2016/10/31 09:45:44 neeraj Exp $';
