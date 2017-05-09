$(document).ready(function() {
	$('#branchDiv').hide();
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
		
		if($('[name=delType]:checked').val()=="Branch"){
			
			$('#branchDiv').show();
			
			if($('#bank').val()!=-1){
					getBranchList($('#bank').val());
				}
		}

	});
	$('[name="delType"]').change(function() {
		
		$('#down').html('');
		$('#bank').val("-1");
		if($('[name=delType]:checked').val()=="Bank"){
			$('#branchDiv').hide();
			$('#branch').val("-1");
		}
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
	

}

function validate() {



	
    
	//alert();
	var startDate = $('#regStartDate').val();
	var endDate =  $('#regEndDate').val();
    
    if(startDate == null || endDate == null){
    	return false;
    }
	if (startDate == "" || endDate == "") {
		
		 $('#error').html("<font color = 'red'>Please Enter all the dates</font>");
		 return  false;
	
       //alert("please enter dates");      
	} else {
		var stArr = startDate.split('-');
		var endArr = endDate.split('-');
		var tempStDate = stArr[1] + '/' + stArr[2] + '/' + stArr[0]; 
		var tempEndDate = endArr[1] + '/' + endArr[2] + '/' + endArr[0]; 
	
		if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
			
			 $('#error').html("<font color = 'red'>End date should be greater then or equals to start date</font>");
			 return  false;
          //alert("end date should be lesser then start date");	
		}
	}
	
	
	$('#error').html("");
	
	return true;


}


