function getRetailerUserList(agentOrgId) {
	if (agentOrgId != -1) {

		var text = _ajaxCall(projectName+"/com/skilrock/lms/web/drawGames/drawMgmt/getRetailerUserNameList.action?orgId="
				+ agentOrgId);
		var firstArr = text.data.split(":");
		//alert(firstArr);
		var retObj = _id.o("retOrgId");
		document.getElementById("retOrgId").options.length = 1;
		for ( var i = 0; i < firstArr.length - 1; i++) {
			var retNameArray = firstArr[i].split("|");
			var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			retObj.options[i + 1] = opt;
		}
	}
}
function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentOrgId");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

function verifyField() {
	//alert(_id.o('retOrgId').value);
	var startDate = _id.o('start_date').value;
	var archDate = _id.o('arch_date').value;
	
	if (_id.o('agentOrgId').value == -1) {
		alert('Please Select Agent Name First');
		//_id.o('agentOrgId').focus();
		return false;
	}

	else if (_id.o('retOrgId').value == -1 || _id.o('retOrgId').value == "") {
		alert('Please Select Retailer Name First');
		//_id.o('retOrgId').focus();
		return false;
	}
	
	else if (startDate <= archDate)
	{
		$("#down").html("<font color='red'>Please select date after "+archDate+"</font>");
		return false;
	}
	else
	{
		return true;
	}
	
}

function clearDiv() {
	_id.i("down", "");

	//_id.i("dates","");
	if (_id.o('agentOrgId').value == -1) {
		alert('Please Select Agent Name First');
		_id.o('agentOrgId').focus();
		return false;
	}

	else if (_id.o('retOrgId').value == -1) {
		alert('Please Select Retailer Name First');
		_id.o('retOrgId').focus();
		return false;
	}

}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/5by90TrnRep.js,v $'] = '$Id: 5by90TrnRep.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';
