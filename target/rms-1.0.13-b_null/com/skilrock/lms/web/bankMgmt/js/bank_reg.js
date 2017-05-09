$(document)
		.ready(
				function() {

					$('#submit').attr("disabled", true);
					var url = path
							+ "/com/skilrock/lms/web/userMgmt/getCountryListWithCode.action";
					$.get(url, function(data, status) {
						fillCountryList(data, status);
						
					}
					
					);
					
					
					$('#country').change(function() {

						getStateListWithCode($('#country').val());

					}

					);
					$('#state').change(
							function() {

								getCityListWithCode($('#country').val(), $(
										'#state').val());

							}

					);
					
				

					$('#createBank').submit(function() {

						return validateInputs();

					}

					);

					$('#bankName').change(function() {
						clearDIV();
						checkBankNameAvail($('#bankName').val());

					}

					);
					fillRoleList();
				}

		);

function fillCountryList(data, status) {
	if (data != "" && status == "success") {

		var options = '';
		$('#country').append($(options));
		var list = data.split("|");
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#country').append($(options));
		var firstOption = list[0].split(":")[0];
		$('#country').val(firstOption);
		getStateListWithCode(firstOption);
	}
}

function getStateListWithCode(countryCode) {

	$('#state').html("<option value='-1'>--Please Select--</option>");
	if (countryCode == "-1") {

		$('#state').val("-1");
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getStateListWithCode.action?country="
				+ countryCode;
		$.get(url, function(data, status) {
			fillStateList(data, status);
		}

		);

	}

}

function fillStateList(data, status) {
	if (data != "" && status == "success") {

		var options = '';
		$('#state').append($(options));
		var list = data.split("|");
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#state').append($(options));
		var firstOption = list[0].split(":")[0];
		$('#state').val(firstOption);
		getCityListWithCode($('#country').val(), firstOption);
	}
}

function getCityListWithCode(countryCode, stateCode) {

	$('#city').html("<option value='-1'>--Please Select--</option>");
	if (countryCode == "-1" || stateCode == "-1") {

		$('#city').val("-1");
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getCityListWithCode.action?country="
				+ countryCode + "&state=" + stateCode;
		$.get(url, function(data, status) {
			fillCityList(data, status);
		}

		);

	}

}

function fillCityList(data, status) {
	if (data != "" && status == "success") {

		var options = '';
		$('#city').append($(options));
		var list = data.split("|");
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			options += '<option value="' + gkey + '">' + dvalue + '</option>';
		}
		$('#city').append($(options));
		$('#city').val(list[0].split("|")[0]);
		$('#city').val("-1");
	}
}

function checkBankNameAvail(bankName) {

	var isValid = validName(bankName);
	if (isValid == false) {
		$('#bankName_e').css("color", "red");
		$('#bankName_e').html("Enter Correct Bank Name: Min Length 2,Special characters not allowed,Should not begin with Number ");
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/bo_bankName_check.action?bankName="+ bankName+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;;
		$.get(url, function(data, status) {

			if (data == "Available") {
				$('#bankName_e').html("Bank Name Available");
				$('#bankName_e').css("color", "green");
				$('#submit').attr("disabled", false);
			} else {
				$('#bankName_e').css("color", "red");
				$('#bankName_e').html("Bank Name Not Available Try Another");

			}
		}

		);

	}
}

$("#submit").click(function() {
	alert('');
});

function validateInputs() {

	clearFieldErrors();
	var isValidate = true;

	if($('#bankFullName').val() == '') {
		$('#bankFullName_e').html('Please Enter Bank Full Name.');
		isValidate = false;
	}
	if($('#bankadd1').val() == '') {
		$('#bankadd1_e').html('Please Enter Bank Address.');
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
	if($('#roleId').val() == -1) {
		$('#roleDiv').html('Please Select Role For Bank');
		isValidate = false;
	}

	return isValidate;
}

function validName(name) {
	if (name == "")
		return false;
	var regex = /^([A-Za-z])([A-Za-z0-9\.]){1,20}$/;
	if (!name.match(regex))
		return false;

	return true;
}

function clearFieldErrors() {
	$('#bankFullName_e').html('');
	$('#bankadd1_e').html('');
	$('#orgstate').html('');
	$('#orgcity').html('');
	$('#roleDiv').html('');
}

function clearDIV() {
	$('#bankName_e').html('');
	clearFieldErrors();
}

function fillRoleList() {

	if( typeof $('#roleId').val()!='undefine'){
		
	
	
	$('#submit').attr("disabled", true);
	var url = path
			+ "/com/skilrock/lms/web/userMgmt/getRoleList.action?"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	$.get(url, function(data, status) {
		
		if (data != "" && status == "success") {

			var options = '';
			$('#roleId').append($(options));
			var list = data.split("|");
			// alert("gameArr = "+gameArr.length+" data = "+gameArr);
			for ( var i = 0; i < list.length - 1; i = i + 1) {
				var listArr = list[i].split(":");
				var gkey = listArr[0];
				var dvalue = listArr[1];
				options += '<option value="' + gkey + '">' + dvalue + '</option>';
			}
			$('#roleId').append($(options));
			$('#roleId').val(list[0].split("|")[0]);
			$('#roleId').val("-1");
		}
		
	});
	
	}	
}