$(document).ready(function() {

	var url = projectName + "/com/skilrock/lms/web/userMgmt/getBankList.action?"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "bank");
		var banksList = data.split("|");
		if(banksList.length==2){
			$('#bank').val(banksList[0].split(":")[0]);
			getBranchList($('#bank').val());
		}
	}

	);
	
	$('#bank').change(function() {
		clearDiv();
		if($('#bank').val()!=-1){
			getBranchList($('#bank').val());
		}
		

	});
	$('[name="repType"]').change(function() {
		
		$('#down').html('');

	});
	$('#search').click(function() {
		
		$('#down').html('');

	});
	

	
	
	
});

function getBranchList(bankId){
	
	var url = projectName + "/com/skilrock/lms/web/userMgmt/getBranchList.action?bankId="+bankId+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		fillDropDownList(data, status, "branch");
		var branchList = data.split("|");
		if(branchList.length==2){
			$('#branch').val(branchList[0].split(":")[0]);
			
		}
		
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
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#' + elementId).append($(options));
		

	}
}
function clearDiv(){
	$('#branch')[0].options.length = 1;
	$('#terminalId_e').html('');
}

function validate() {

	$('#terminalId_e').html('');
	var isValidate = true;

	if($('#terminalId').val()!= '') {
		var regex = /^[0-9]{8}$/;
		if(!$('#terminalId').val().match(regex)){
			isValidate = false;
			$('#terminalId_e').html('Please Valid Terminal Id.');
			
		}
			
	}
	

	return isValidate;
}


