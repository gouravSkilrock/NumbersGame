function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getOrganizationList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentOrgId");
	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[1].toUpperCase(), retNameArray[0]);
		retObj.options[i + 1] = opt;
	}
}

function validateDates() {
	var isInValid = false;
	var startDate = _id.o("start_date").value;
	var endDate =  _id.o("end_date").value;	
	if (startDate == "" || endDate == "") {
		isInValid = true;
		_id.o("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";			           
	} else {
		if (endDate < startDate) {
			isInValid = true;
			_id.o("dates").innerHTML = "<font color = 'red'>end date should be greater then or equals to start date</font>";          
        }
	}				
	if (isInValid) {				
		return false;
	}
	_id.o("dates").innerHTML = "";
	return true;			
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/drawPendingReport.js,v $'] = '$Id: drawPendingReport.js,v 1.2 2016/10/31 09:46:26 neeraj Exp $';