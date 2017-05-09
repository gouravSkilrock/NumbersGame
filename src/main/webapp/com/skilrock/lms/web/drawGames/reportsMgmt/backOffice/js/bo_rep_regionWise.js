
var i18nObj = parent.frames[0].i18Obj;

$(document)
		.ready(
				function() {
					$('#agtDiv').hide();
					var url = path
							+ "/com/skilrock/lms/web/userMgmt/getCountryListWithCode.action";
					$.get(url, function(data, status) {
						fillCountryList(data, status);
						$('#down').html("");
					}
					
					);
					
					
					$('#country').change(function() {
						$('#down').html("");
						getStateListWithCode($('#country').val());

					}

					);
					$('#state').change(
							
							function() {
								$('#down').html("");
								$('#areaDiv').html("");
								$('#agtDiv').hide();
								getCityListWithCode($('#country').val(), $(
										'#state').val());

							}

					);
					$('#city').change(
							function() {
								$('#down').html("");
								$('#agtDiv').hide();
								$('#cityCode').val(myMap[$('#city').val()]);
								var countryname=$('#country').find(":selected").text();
								var statename=$('#state').find(":selected").text();
								var cityname=$('#city').find(":selected").text();
								
								getAreaListWithCode(countryname,statename,cityname);

							}

					);
					 $('#repForm').on('change', 'input[type=checkbox]', function(e) {
						 	$('#down').html("");	
								var arealist ="";
								$('input[name="area"]:checked').each(function() {
									arealist =arealist+this.value+",";
								});
								var city =$('#city').find(":selected").text();
								var state =$('#state').val();
								//alert(arealist+state+city);
								getAgentList(state,city,arealist);
								
							
					    });
				
					$('#selectAll').click(function(e){
						$('#down').html("");
					    var table= $(e.target).closest('table');
					    $('td input:checkbox',table).prop('checked',this.checked);
					});
					
				}

		);




function validated(){
	$('#down').html("");
	clearErrorFields();
	var isValid =true;
	
	
	if(!isSelected($('#gameId').val())){
		isValid=false;

	$('#gameId_e').html(i18nObj.prop('error.pls.slct.game')+'!!');
	
	}
	
	
	if(!isDateValid($('#startDate').val(),$('#endDate').val())){
		isValid=false;
	
	}
	
	

return isValid ;

}




function isEmpty(value){

if(value.length<1){
return true;

}

return false;

}
function isSelected(value){

	if(value==-1){
		return false;
	}
	return true;
	}

function isDateValid(startDate,endDate){
	var isInValid=false;
	if (startDate == "" || endDate == "") {
		isInValid = true;
		$('#date_e').html("<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>");
				           
	} else {
		if (endDate < startDate) {
			isInValid = true;
			$('#date_e').html("<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>");
		  }
	}				
	if (isInValid) {				
		return false;
	}
	$('#date_e').html("");
	return true;	

}




function clearErrorFields(){
	
	$( "div[id$='_e']" ).html( " " );
	
}











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
		$('#code').val(list[0].split(":")[2]);
		getStateListWithCode(firstOption);
	}
}
function getStateListWithCode(countryCode) {

	$('#state').html("<option value='-1'>--"+i18nObj.prop('label.select.all')+"--</option>");
	if (countryCode == "-1") {

		$('#state').val("-1");
		$('#city').attr('disabled', 'disabled');
		$('#areaDiv').html("");
	} else {
		$('#city').removeAttr('disabled');
		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getStateListWithCode.action?country="
				+ countryCode+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
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
		$('#state').val(-1);
	}
}

function getCityListWithCode(countryCode, stateCode) {

	$('#city').html("<option value='-1'>--"+i18nObj.prop('label.select.all')+"--</option>");
	if (countryCode == "-1" || stateCode == "-1") {
		
		$('#city').val("-1");
		$('#city').attr('disabled', 'disabled');
		$('#areaDiv').html("");
	} else {
		$('#city').removeAttr('disabled');
		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getCityListWithCode.action?country="
				+ countryCode + "&state=" + stateCode+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
		$.get(url, function(data, status) {
			fillCityList(data, status);
		}

		);

	}

}
var myMap =null;
function fillCityList(data, status) {
	myMap= new Object();
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
			myMap[gkey]=listArr[2];
		}
		$('#city').append($(options));
		$('#city').val(list[0].split("|")[0]);
		$('#city').val("-1");
	}
}
function getAreaListWithCode(countryCode, stateCode,cityCode) {

	// $('#area').html("<option value='-1'>--Select All--</option>");
	$('#areaDiv').html("");
	var cityVal=$('#city').val();
	if (countryCode == "-1" || stateCode == "-1" ||cityVal=="-1") {
		
		$('#areaDiv').html("");
		//$('#area').val("-1");
	} else {

		var url = path
				+ "/com/skilrock/lms/web/userMgmt/getAreaListWithCode.action?country="
				+ countryCode + "&state=" + stateCode+ "&city=" + cityCode+"&"+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
		$.get(url, function(data, status) {
			fillAreaList(data, status);
		}

		);

	}

}

function fillAreaList(data, status) {
	
	if (data != "" && status == "success") {
		var name ="area";
		
		
		var list = data.split("|");
		var options = '<table><tr colspan="'+list.length+'>" ';
		$('#areaDiv').html(options);
		options += ' <td>'+i18nObj.prop('label.select.zones')+' :</td></td></tr><tr>';
		// alert("gameArr = "+gameArr.length+" data = "+gameArr);
		for ( var i = 0; i < list.length - 1; i = i + 1) {
			var listArr = list[i].split(":");
			var gkey = listArr[0];
			var dvalue = listArr[1];
			
			options += '<td><input type="checkbox" name="' + name + '" id="id' + i + '" value="' + gkey + '"   ><label for="id' + i + '">' + dvalue  + '</label></td>';
		}
		$('#areaDiv').html(options+"</tr></table>");
		
		
	}
}
function getAgentList(state,city,areas) {
	$('#down').html("");
	$('#agentOrgId').empty().append('<option value="-1">--'+i18nObj.prop('label.ALL')+'label.ALL--</option>');
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/reportsMgmt/getOrgListForArea.action?orgType=AGENT&state="+state+"&city="+city+"&area="+areas);
	$('#agtDiv').show();
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentOrgId");
	for (var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[1], retNameArray[0].toUpperCase());
		retObj.options[i + 1] = opt;
	}
}	


parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/bo_rep_regionWise.js,v $"] = "$Id: bo_rep_regionWise.js,v 1.3 2016/10/31 09:46:33 neeraj Exp $";

