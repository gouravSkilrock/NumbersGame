var dayArr = new Array();
dayArr['mon'] = 'Monday';
dayArr['tue'] = 'Tuesday';
dayArr['wed'] = 'Wednesday';
dayArr['thu'] = 'Thursday';
dayArr['fri'] = 'Friday';
dayArr['sat'] = 'Saturday';
dayArr['sun'] = 'Sunday';

function resetErrorDiv() {
	_id.o("errOrgTypeSingle").style.display = 'none';
	_id.o("errRetListSingle").style.display = 'none';
	_id.o("errOrgTypeMultiple").style.display = 'none';
	_id.o("errRetListMultiple").style.display = 'none';
}

function resetDiv() {
	resetErrorDiv();
	_id.o("orgTypeSingle").selectedIndex = 0;
	_id.o("orgTypeMultiple").selectedIndex = 0;
	_id.o("resultSU").innerHTML = '';
	_id.o("resultMU").innerHTML = '';
	_id.o('singleUser').style.display = 'none';
	_id.o('multipleUser').style.display = 'none';

	_id.o('cbRst').checked = false;
	toggleRestriction(false);

	_id.o('tblDiv').style.display = 'none';
	fillDates();
	dispTiming('mon');
	dispTiming('tue');
	dispTiming('wed');
	dispTiming('thu');
	dispTiming('fri');
	dispTiming('sat');
	dispTiming('sun');
}

function toggleUserSel(val) {
	resetDiv();
	if (val == 'SU')
		_id.o('singleUser').style.display = 'block';
	else if (val == 'MU')
		_id.o('multipleUser').style.display = 'block';
}

function getOrgListing(type, criteria) {
	resetErrorDiv();
	if(type == 'RETAILER') {
		fetchAdvRet('AgentWise');
		_id.o("submit").style.display = 'none';
		_id.o('tblDiv').style.display = 'block';
	}
	else {
		_id.o("resultSU").innerHTML = '';
		_id.o("resultMU").innerHTML = '';

		var orgType = 'orgTypeMultiple';
		if (criteria == 'SU')
			orgType = 'orgTypeSingle';

		var orgTypeVal = document.getElementById(orgType).value;
		if (orgTypeVal != "-1") {
			_resp = _ajaxCall('getBoAgtList.action?orgType='+type);
			_respData = _resp.data;
			var resData = _respData.split(":");
			var orgNameList = '';

			if (criteria == 'SU') {
				orgNameList = 'Select User/Org <select name="retName" id="retName" class="option" onchange="getUserTimeLimit(this.value);" >';
				orgNameList += '<option value="-1">--Please Select--</option>';
				for (var i = 0; i < resData.length-1; i = i + 1) {
					var valArray = resData[i].split("|");
					orgNameList += '<option value="'+valArray[0]+'">'+valArray[1].toUpperCase()+'</option>';
				}
				orgNameList += '</select>';
				_id.o('resultSU').innerHTML = orgNameList;
			}
			else if (criteria == 'MU') {
				_id.o('tblDiv').style.display = 'block';
				orgNameList = '<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center"><tr>';
				orgNameList += '<tr><td colspan="5" align="left"><input type="checkbox" onchange="selAllChkBxAgt(this.checked);" /> <b>Select All Organization</b></td></tr>';
				for (var i = 0; i < resData.length-1; i = i + 1) {
					var valArray = resData[i].split("|");
					if(i!=0 && i%5 == 0)
						orgNameList += '</tr><tr>';
					orgNameList += '<td><input type="checkbox" name="retName" value="'+valArray[0]+'" id="cb_'+valArray[0]+'" /><label for="cb_'+valArray[0]+'" id="lbl_'+valArray[0]+'" style="font-size:0.8em; font-family:Arial; ">'+valArray[1].toUpperCase()+'</label></td>';
				}
				orgNameList += '</table>';
				_id.o('resultMU').innerHTML = orgNameList;
			}
		}
	}
}

function getUserTimeLimit(userId) {
	_id.o('tblDiv').style.display = 'block';

	var _resp = _ajaxCall('bo_um_get_ipTimeLimit.action?userId='+userId);
	var respData = _resp.data;
	var userDetails = respData.split("~");

	function setDataInTbl(day, indx) {
		_id.o(day+'StartHr').selectedIndex = parseInt(userDetails[indx].split(":")[0]);
		_id.o(day+'StartMin').selectedIndex = parseInt(userDetails[indx].split(":")[1]);
		_id.o(day+'EndHr').selectedIndex = parseInt(userDetails[indx+1].split(":")[0]);
		_id.o(day+'EndMin').selectedIndex = parseInt(userDetails[indx+1].split(":")[1]);
		dispTiming(day);
	}

	setDataInTbl('mon', 2);
	setDataInTbl('tue', 4);
	setDataInTbl('wed', 6);
	setDataInTbl('thu', 8);
	setDataInTbl('fri', 10);
	setDataInTbl('sat', 12);
	setDataInTbl('sun', 14);
}

function selAllChkBxAgt(chkStatus) {
	var retName = document.getElementsByName("retName");
		for(var i=0; i<retName.length; i++)
			retName[i].checked = chkStatus;
}

function fillDates() {
	var selectList = document.getElementsByTagName('select');
	for(var x=0; x<selectList.length; x++) {
		if(selectList[x].name.match('Hr') || selectList[x].name.match('Min')) {
			var limit = 24;
			if(selectList[x].name.match('Min'))
				limit = 60;
			for(var i=0; i<limit; i++)
				selectList[x].options[i] = new Option((i<10)?'0'+i:i, (i<10)?'0'+i:i);
			if(selectList[x].name.match('End'))
				selectList[x].selectedIndex = limit-1;
		}
	}
}

function fetchAdvRet(val) {
	var _resp = _ajaxCall(projectName + "/com/skilrock/lms/web/drawGames/drawMgmt/bo_dg_advMessage_Search.action?search_type=" + val);
	_id.i("resultMU", _resp.data);
}

function fillChk(val) {
	var parentChk = _id.o("P" + val).checked;
	var tab = _id.o("tab" + val);
	var chkBx = tab.getElementsByTagName("input");
	for (var i = 0; i < chkBx.length; i++)
		if (chkBx[i].type == "checkbox")
			chkBx[i].checked = parentChk;
}

function selAllChkBx() {
	var retName = document.getElementsByName("retName");
	var length = retName.length;
	for(var i=0; i<length; i++) {
		retName[i].checked = _id.o("selAll").checked;
	}
}

function setData() {
	if(validateOrg()) {
		if(_id.o('cbRst').checked) {
			_id.o('timing').value = '00:00_23:59_00:00_23:59_00:00_23:59_00:00_23:59_00:00_23:59_00:00_23:59_00:00_23:59';
			return true;
		}
		else {
			validateTiming('mon');validateTiming('tue');validateTiming('wed');validateTiming('thu');validateTiming('fri');validateTiming('sat');validateTiming('sun');
			if(validateTiming('mon') && validateTiming('tue') && validateTiming('wed') && validateTiming('thu') && validateTiming('fri') && validateTiming('sat') && validateTiming('sun')) {
				var timing = '';
				timing += _id.o("monStartHr").options[_id.o("monStartHr").selectedIndex].text+':'+_id.o("monStartMin").options[_id.o("monStartMin").selectedIndex].text+'_'+_id.o("monEndHr").options[_id.o("monEndHr").selectedIndex].text+':'+_id.o("monEndMin").options[_id.o("monEndMin").selectedIndex].text+'_';
				timing += _id.o("tueStartHr").options[_id.o("tueStartHr").selectedIndex].text+':'+_id.o("tueStartMin").options[_id.o("tueStartMin").selectedIndex].text+'_'+_id.o("tueEndHr").options[_id.o("tueEndHr").selectedIndex].text+':'+_id.o("tueEndMin").options[_id.o("tueEndMin").selectedIndex].text+'_';
				timing += _id.o("wedStartHr").options[_id.o("wedStartHr").selectedIndex].text+':'+_id.o("wedStartMin").options[_id.o("wedStartMin").selectedIndex].text+'_'+_id.o("wedEndHr").options[_id.o("wedEndHr").selectedIndex].text+':'+_id.o("wedEndMin").options[_id.o("wedEndMin").selectedIndex].text+'_';
				timing += _id.o("thuStartHr").options[_id.o("thuStartHr").selectedIndex].text+':'+_id.o("thuStartMin").options[_id.o("thuStartMin").selectedIndex].text+'_'+_id.o("thuEndHr").options[_id.o("thuEndHr").selectedIndex].text+':'+_id.o("thuEndMin").options[_id.o("thuEndMin").selectedIndex].text+'_';
				timing += _id.o("friStartHr").options[_id.o("friStartHr").selectedIndex].text+':'+_id.o("friStartMin").options[_id.o("friStartMin").selectedIndex].text+'_'+_id.o("friEndHr").options[_id.o("friEndHr").selectedIndex].text+':'+_id.o("friEndMin").options[_id.o("friEndMin").selectedIndex].text+'_';
				timing += _id.o("satStartHr").options[_id.o("satStartHr").selectedIndex].text+':'+_id.o("satStartMin").options[_id.o("satStartMin").selectedIndex].text+'_'+_id.o("satEndHr").options[_id.o("satEndHr").selectedIndex].text+':'+_id.o("satEndMin").options[_id.o("satEndMin").selectedIndex].text+'_';
				timing += _id.o("sunStartHr").options[_id.o("sunStartHr").selectedIndex].text+':'+_id.o("sunStartMin").options[_id.o("sunStartMin").selectedIndex].text+'_'+_id.o("sunEndHr").options[_id.o("sunEndHr").selectedIndex].text+':'+_id.o("sunEndMin").options[_id.o("sunEndMin").selectedIndex].text;
				_id.o("timing").value = timing;
				return true;
			}
			else
				return false;
		}
	}
	return false;
}
function validateOrg() {
	if (_id.o('selCriteriaSU').checked) {
		_id.o("errOrgTypeSingle").style.display = 'none';
		_id.o("errRetListSingle").style.display = 'none';

		if(_id.o("orgTypeSingle").selectedIndex < 1) {
			_id.o("errOrgTypeSingle").style.display = 'block';
			return false;
		}
		else if(_id.o("retName").selectedIndex < 1) {
			_id.o("errRetListSingle").style.display = 'block';
			return false;
		}
		return true;
	}
	else if (_id.o('selCriteriaMU').checked) {
		_id.o("errOrgTypeMultiple").style.display = 'none';
		_id.o("errRetListMultiple").style.display = 'none';

		if(_id.o("orgTypeMultiple").selectedIndex < 1) {
			_id.o("errOrgTypeMultiple").style.display = 'block';
			return false;
		}
		else {
			var retName = document.getElementsByName("retName");
			for(var i=0; i<retName.length; i++) {
				if(retName[i].checked == true)
					return true;
			}
		}
		_id.o("errRetListMultiple").style.display = 'block';
		return false;
	}
}

function validateTiming(day) {
	var startHr = parseInt(_id.o(day+'StartHr').options[_id.o(day+'StartHr').selectedIndex].text);
	var startMin = parseInt(_id.o(day+'StartMin').options[_id.o(day+'StartMin').selectedIndex].text);
	var endHr = parseInt(_id.o(day+'EndHr').options[_id.o(day+'EndHr').selectedIndex].text);
	var endMin = parseInt(_id.o(day+'EndMin').options[_id.o(day+'EndMin').selectedIndex].text);

	var flag = true;

	if(startHr==0 && startMin==0 && endHr==0 && endMin==0) {
		_id.o(day+'DispTiming').innerHTML = '<label id="'+day+'StartDispTiming"><font color="blue">User will be Blocked for '+dayArr[day]+'.</font></label><label id="'+day+'EndDispTiming"></label>';
		return true;
	}
		
	if(endHr<startHr)
		flag = false;
	if(endHr == startHr)
		if(endMin<=startMin)
			flag = false;

	if(!flag)
		_id.o(day+'DispTiming').innerHTML = '<label id="'+day+'StartDispTiming"><font color="red">Invalid Timing</font></label><label id="'+day+'EndDispTiming"></label>';
	else {
		_id.o(day+'StartDispTiming').innerHTML = _id.o(day+'StartHr').options[_id.o(day+'StartHr').selectedIndex].text+':'+_id.o(day+'StartMin').options[_id.o(day+'StartMin').selectedIndex].text+' To ';
		_id.o(day+'EndDispTiming').innerHTML = _id.o(day+'EndHr').options[_id.o(day+'EndHr').selectedIndex].text+':'+_id.o(day+'EndMin').options[_id.o(day+'EndMin').selectedIndex].text;
	}

	return flag;
}

function dispTiming(day) {
	var startHr = parseInt(_id.o(day+'StartHr').options[_id.o(day+'StartHr').selectedIndex].text);
	var startMin = parseInt(_id.o(day+'StartMin').options[_id.o(day+'StartMin').selectedIndex].text);
	var endHr = parseInt(_id.o(day+'EndHr').options[_id.o(day+'EndHr').selectedIndex].text);
	var endMin = parseInt(_id.o(day+'EndMin').options[_id.o(day+'EndMin').selectedIndex].text);

	if(startHr==0 && startMin==0 && endHr==0 && endMin==0)
		_id.o(day+'DispTiming').innerHTML = '<label id="'+day+'StartDispTiming"><font color="blue">User will be Blocked for '+dayArr[day]+'.</font></label><label id="'+day+'EndDispTiming"></label>';
	else {
		_id.o(day+'StartDispTiming').innerHTML = _id.o(day+'StartHr').options[_id.o(day+'StartHr').selectedIndex].text+':'+_id.o(day+'StartMin').options[_id.o(day+'StartMin').selectedIndex].text+' To ';
		_id.o(day+'EndDispTiming').innerHTML = _id.o(day+'EndHr').options[_id.o(day+'EndHr').selectedIndex].text+':'+_id.o(day+'EndMin').options[_id.o(day+'EndMin').selectedIndex].text;
	}
}

function toggleRestriction(status) {
	if(status == true)
		_id.o('restMsgDiv').style.display = 'block';
	else
		_id.o('restMsgDiv').style.display = 'none';

	var selectList = document.getElementsByTagName('select');
	for(var x=0; x<selectList.length; x++) {
		if(selectList[x].name.match('Hr') || selectList[x].name.match('Min'))
			selectList[x].disabled = status;
	}
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/ipTimeLimit.js,v $'] = '$Id: ipTimeLimit.js,v 1.2 2016/10/31 11:40:07 neeraj Exp $';
