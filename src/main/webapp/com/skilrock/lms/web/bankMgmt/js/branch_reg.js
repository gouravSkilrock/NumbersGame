$(document).ready(function() {

	var url = path + "/com/skilrock/lms/web/userMgmt/getBankList.action?"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "bank");
	}

	);
});

function fillDropDownList(data, status, elementId) {
	if (data != "" && status == "success") {

		var options = '';
		$('#' + elementId).append($(options));
		var list = data.split("|");
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#' + elementId).append($(options));

	}
}

function validate() {

	clearFieldErrors();
	var isValidate = true;

	if($('#bank').val() == -1) {
		$('#bankName_e').html('Please Select Bank.');
		isValidate = false;
	}
	if($('#branchName').val() == '') {
		$('#branchName_e').html('Please Enter Branch.');
		isValidate = false;
	}else{
		var isValidate = validName($('#branchName').val());
		if(!isValidate){
			$('#branchName_e').html("Enter Correct Branch Name: Min Length 2,Special characters not allowed,Should not begin with Number ");
		}
	}
	
		
		
	

	if($('#branchFullName').val() == '') {
		$('#branchFullName_e').html('Please Enter Branch Full Name.');
		isValidate = false;
	}
	if($('#branchadd1').val() == '') {
		$('#branchadd1_e').html('Please Enter Branch Address.');
		isValidate = false;
	}
	if($('#state').val() == -1) {
		$('#orgstate').html('Please Select State.');
		isValidate = false;
	}
	if($('#city').val() == -1) {
		$('#orgcity').html('Please Select City.');
		isValidate = false;
	}

	return isValidate;
}

function clearFieldErrors() {
	$('#bankName_e').html('');
	$('#branchName_e').html('');
	$('#branchFullName_e').html('');
	$('#branchadd1_e').html('');
	$('#orgstate').html('');
	$('#orgcity').html('');
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/bankMgmt/js/branch_reg.js,v $'] = '$Id: branch_reg.js,v 1.2 2016/10/31 09:45:38 neeraj Exp $';