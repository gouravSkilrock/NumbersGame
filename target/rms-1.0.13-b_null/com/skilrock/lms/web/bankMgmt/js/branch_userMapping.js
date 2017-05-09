$(document).ready(function() {
	var url = path + "/com/skilrock/lms/web/userMgmt/getUserList.action?"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "user");
	}
	);
	var url = path + "/com/skilrock/lms/web/userMgmt/getBranchList.action?"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "branch");
	}
	);
	$('#user').change(
			function() {

				getUserDetails($('#user').val());

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

function validateInputs() {

	clearFieldErrors();
	if($('#branch').val() == -1) {
		$('#branchDiv').html('Please Select Branch.');
		return false;
	}
	if($('#user').val() == -1) {
		$('#userDiv').html('Please Select User.');
		return false;
	}
	var e = document.getElementById("user");
	var userName = e.options[e.selectedIndex].text;
	var e = document.getElementById("branch");
	var branch = e.options[e.selectedIndex].text;
	var returnType=confirm("Please Confirm to assign:" +branch+" Branch to  "+userName +"  User");
	if(!returnType){
		
		return false;
		
	}
	return true;
}

function clearFieldErrors() {
	$('#branchDiv').html('');
	$('#userDiv').html('');
	$('#branchDiv').html('');
}
function getUserDetails(userId){
	
	
	clearFieldErrors();
	if(userId == -1) {
		$('#userDiv').html('Please Select User.');
		isValidate = false;
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getUserDetails.action?userId="+userId+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;;
		$.get(url, function(data, status) {
			if (data != "" && status == "success") {
				var list = data.split("|");
				$('#branchDiv').html("This user is already Map with following details </br>Branch Name : <strong>"+list[0].split(":")[1]+"</strong></br> Branch Full Name:<strong>"+list[0].split(":")[2]+"</strong>");
	
			}
		}

		);

	}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/bankMgmt/js/branch_userMapping.js,v $'] = '$Id: branch_userMapping.js,v 1.2 2016/10/31 09:45:38 neeraj Exp $';