var i18nObj = parent.frames[0].i18Obj;

$(document).ready(function (){
	getAgentList(path);
	$("#agentOrgId").change(function(){
		//alert(this.value);
		clearDiv();
		getRetList(this.value);
		
	});
});
function clearDiv(){
	$("#ron").html("");
	$("#ret_err").html("");
//	$("#alias_e").html("");
//	$("#regDate_e").html("");
}

function getAgentList() {
	var text = _ajaxCall(path+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var agtObj = _id.o("agentOrgId");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var agtNameArray = firstArr[i].split("|");
		var opt = new Option(agtNameArray[0].toUpperCase(), agtNameArray[1]);
		agtObj.options[i + 1] = opt;
	}
}
function getRetList(agentOrgName) {
	var text = _ajaxCall(path+"/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId="+agentOrgName);
	var firstRetArr = text.data.split(":");
	var retObj = _id.o("retOrgId");
	retObj.innerHTML="";
	retObj.innerHTML="<option value='-1'>--"+i18nObj.prop('msg.pls.select')+"--</option>";
	
	
	for (var i = 0; i <firstRetArr.length - 1; i=i+1) {
		var retNameArray = firstRetArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/inventoryMgmt/backOffice/js/returnInvFromRet.js,v $'] = '$Id: returnInvFromRet.js,v 1.3 2016/10/31 09:47:02 neeraj Exp $';