var i18nObj = parent.frames[0].i18Obj;
function getRetailerUserList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=RETAILER");
			var firstArr = text.data.split(":");
			var retObj = _id.o("retOrgId");
			for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
}

function setDateField(tvalue) {//alert(tvalue);
	document.getElementById("down").innerHTML="";
	if(tvalue=='Date Wise') {
		//alert("inside if"+document.getElementById("date").style.visibility);
		document.getElementById("date").style.display="block";
	}				
	else{ 
		//alert('--in else'+tvalue);
		if(tvalue=='Current Day') {//alert();
		//alert('<s:property value="#session.presentDate"/>');
		_id.o("start_date").value=_id.o("curDate").value;
		_id.o("end_date").value=_id.o("curDate").value;
		document.getElementById("date").style.display="none";
		}
		else {
			document.getElementById("date").style.display="none";
		}
	}
	_id.i('dates','');
}
		
function clearDiv(){
	_id.i("down","");

	_id.i("dates","");
	var retOrgId = _id.o('retOrgId').value ;
	//alert(_id.o('agentOrgId').value);
	//var agtOrgId = _id.o('agentOrgId').value;
	/*if(agtOrgId == -1){
		alert(i18nObj.prop('error.select.agt.name.first'));
		_id.o('agentOrgId').focus();
		return false;		
	}*/ 
	if(retOrgId == -1){
		alert(i18nObj.prop('error.select.ret.name.first'));
		_id.o('retOrgId').focus();
		return false;
	}
	return validateDates();
}