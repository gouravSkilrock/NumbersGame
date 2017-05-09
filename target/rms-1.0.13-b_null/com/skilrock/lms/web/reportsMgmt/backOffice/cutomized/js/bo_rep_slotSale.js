
$(document)
		.ready(
				function() {

				 //	getAgentList();
				
				
				
				}

		);




function validated(){
	$('#down').html("");
	clearErrorFields();
	var isValid =true;
	
	
	
	
	
	if(!isDateValid($('#startDate').val(),$('#endDate').val())){
		isValid=false;
	
	}
	
	

return isValid ;

}





function isDateValid(startDate,endDate){
	var isInValid=false;
	if (startDate == "" || endDate == "") {
		isInValid = true;
		$('#date_e').html("<font color = 'red'>Please Enter all the dates</font>");
				           
	} else {
		if (endDate < startDate) {
			isInValid = true;
			$('#date_e').html("<font color = 'red'>End date should be greater then or equals to start date</font>");
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





function getAgentList() {
	$('#down').html("");
	$('#agentOrgId').empty().append('<option value="-1">--ALL--</option>');
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/reportsMgmt/getOrgListForArea.action?orgType=AGENT");
	$('#agtDiv').show();
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentOrgId");
	for (var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[1], retNameArray[0].toUpperCase());
		retObj.options[i + 1] = opt;
	}
}	


parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/cutomized/js/Attic/bo_rep_slotSale.js,v $"] = "$Id: bo_rep_slotSale.js,v 1.1.2.2 2014/02/04 08:55:04 yogesh Exp $";

