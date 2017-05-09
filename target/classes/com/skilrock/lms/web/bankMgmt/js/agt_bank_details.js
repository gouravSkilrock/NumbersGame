$(document).ready(function() {
	getAgentList();
	var url = projectName + "/com/skilrock/lms/web/userMgmt/getAgentBankList.action?"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		
		fillDropDownList(data, status, "bank");
	}

	);
	
	$('#bank').change(function() {
		clearDiv();
		if($('#bank').val()!=-1){
			var bankType =($('#bank').val()).split(":")[1];
			$('#bankId').val(($('#bank').val()).split(":")[0]);
			if(bankType=="E-ZWICH"){
				$('#branch').attr("disabled", "disabled");
			
			}else{
				$('#branch').removeAttr('disabled');
				getBranchList(($('#bank').val()).split(":")[0]);
			}
			
		}
		

	});
	$('#branch').change(function() {
		
		if($('#branch').val()!=-1){
			$('#branchId').val(($('#branch').val()).split(":")[0]);
			$('#sortCode').val(($('#branch').val()).split(":")[1]);
			
		}
		

	});
	$('#agentOrgName').change(
			function() {

				getAgentDetails($('#agentOrgName').val());

			}

	);
	
	
});

function getBranchList(bankId){

	var url = projectName + "/com/skilrock/lms/web/userMgmt/getAgentBranchList.action?bankId="+bankId+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "branch");
	}
	);
	
}
function fillDropDownList(data, status, elementId) {
	if (data != "" && status == "success") {

		var options = '';
		$('#' + elementId).append($(options));
		var list = data.split("|");
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0]+":"+listArr[1];
			var dvalue = listArr[2];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#' + elementId).append($(options));

	}
}
function clearDiv(){
	$('#branch')[0].options.length = 1;
	$('#sortCode').val('');
	$('#accountNbr').val('');
}
function clearFieldErrors(){
	$('#bankDiv').html(' ');
	$('#branchDiv').html(' ');
	$('#agtDiv').html(' ');
	$('#accountDiv').html(' ');

}


function validate() {
	
	clearFieldErrors();
	var isValidate = true;

	if($('#bank').val() == -1) {
		$('#bankDiv').html('Please Select Bank.');
		isValidate = false;
	}
	var bankType =($('#bank').val()).split(":")[1];
	$('#bankId').val(($('#bank').val()).split(":")[0]);
	if(bankType!="E-ZWICH"){
		
		if($('#branch').val() == -1) {
			$('#branchDiv').html('Please Select Branch.');
			isValidate = false;
		}
	}
	
	if($('#agentOrgName').val() == -1) {
		$('#agtDiv').html('Please Select LMC .');
		isValidate = false;
	}

	var regx = /^[A-Za-z0-9 _.-]+$/;
	var space = /\s/;
	var accnbr = $('#accountNbr').val();
	if(accnbr == '') {
		$('#accountDiv').html('Please Enter Account Number .');
		isValidate = false;
	}else if(!regx.test(accnbr) || space.test(accnbr)){
		$('#accountDiv').html("No Special Char Allowed");
		isValidate = false;
	}
	
	if(isValidate){
		var returnType=confirm("Please Confirm to Update LMC Details");
		
		if(!returnType){
			isValidate=false;
			
			
		}
	}
	
	

	return isValidate;
}

function getAgentList() {
	var text = _ajaxCall("getAgentNameList.action?orgType=AGENT&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000);
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentOrgName");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function getAgentDetails(agentId){
	$('#agtDiv').html('');
	
	
	if(agentId == -1) {
		$('#agtDiv').html('Please Select Agent.');
		isValidate = false;
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getAgentBankDetails.action?agtOrgId="+agentId+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
		$.get(url, function(data, status) {
			if (data != "" && status == "success") {
				var list = data.split("|");
				var branchData ="";
				if(list[0].split(":")[0]!="null"){
					branchData="</br> Branch Name:<strong>"+list[0].split(":")[0]+"</strong>";
				}
				$('#agtDiv').html("LMC Details </br>Bank Name : <strong>"+list[0].split(":")[1]+"</strong>"+branchData+" </br> Account Number:<strong>"+list[0].split(":")[2]+"</strong>");
	
			}
		}

		);

	}
}