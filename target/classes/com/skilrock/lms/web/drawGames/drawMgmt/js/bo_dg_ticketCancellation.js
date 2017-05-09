var i18nObj = parent.frames[0].i18Obj; 

function checkType() {
	_id.i("errMsgDiv", "", false);
	var searchType = _id.n("search_type");
	for (var i = 0; i < searchType.length; i++) {
		if (searchType[i].checked) {
			searchVal = searchType[i].value;
		}
	}
	if ("Enter_Ticket" == searchVal) {
		_id.blk("enterTktDiv");
		_id.non("retTable");
		clearDiv();
		_id.o("ticketCancellation").reset();
	} else {
		if ("Last_Ten" == searchVal) {
			_id.blk("retTable");
			_id.non("enterTktDiv");
			clearDiv();
		}
	}
}
function validateTicket() {
	_id.i("errMsgDiv", "", false);
	var searchType = _id.n("search_type");
	for (var i = 0; i < searchType.length; i++) {
		if (searchType[i].checked) {
			searchVal = searchType[i].value;
		}
	}
	if (searchVal == "Enter_Ticket") {
		_id.non("result");
		var tktFldVal = _id.o("ticketNo").value;
		if (tktFldVal == "") {
			//alert("Please Enter the Ticket No.");
			_id.i("errMsgDiv", i18nObj.prop('error.tckt.no.empty'), true);
			return false;
		}
		else {
			if (tktFldVal.length == 16 || tktFldVal.length == 18 || tktFldVal.length == 20) {
				
			}else{
				//alert("Please Enter the Correct Ticket No.");
				_id.i("errMsgDiv", i18nObj.prop('error.tckt.no.invalid'), true);
				return false;
			}
		}
		if (document.getElementById("reason").selectedIndex == 0)
		{
			_id.i("errMsgDiv", i18nObj.prop('error.rsn.tckt.cancl.empty'), true);
			return false;
		}
	} else {
		if (searchVal == "Last_Ten") {
			/*
			var obj = document.getElementsByName("ticketNumArr");
			var flag = false;
			for (var i = 0; i < obj.length; i++) {
				if (obj[i].checked) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				_id.i("errMsgDiv", "Select Atleast.", true);
				return false;
			}
			*/
			_id.i("errMsgDiv", "", false);
			var arrInput =document.getElementsByName('ticketNumArr');
			var count = 0;
			for ( var i =0; i<arrInput.length; i = i+1) {
				
				if(/*(arrInput[i].name != undefined) && (arrInput[i].type == 'checkbox') &&*/ (arrInput[i].checked))
				{
					count+=1;
					if (document.getElementById('reason_' + arrInput[i].value).selectedIndex == 0) {
						_id.i("errMsgDiv", i18nObj.prop('error.enter.reason.fr.tkt.no')+" - "+arrInput[i].value, true);
						return false;
					}
				}
		/*		alert(arrInput[i].name);
				alert(arrInput[i].type == 'checkbox');
				alert(arrInput[i].checked);
				alert(arrInput[i].name != undefined && arrInput[i].name.match('ticketNumArr') && arrInput[i].checked);*/
				
			}
			if(count == 0)
			{
				_id.i("errMsgDiv", i18nObj.prop('error.select.atleast.one.tkt'), true);
				return false;
			}

			return true;
		}
	}
	return true;
}
function checkNum(obj) {
	var e = event || evt;
	var _cC = e.which || e.keyCode;
	obj.style.color = "";
	if ((_cC >= 48 && _cC < 58) || _cC == 45 || _cC == 40 || _cC == 38 || (_cC >= 96 && _cC < 106 || _cC == 46 || _cC == 8 || _cC == 9 || _cC == 37 || _cC == 39)) {
		return true;
	}
	return false;
}
function checkLen(obj) {
	if (obj.value.length == 20) {
		_id.o("subButton").focus();
	}
	return true;
}
function checkRetNGame() {
	clearDiv();
	return true;
}
function submitForm() {
	var e = event || evt;
	var _cC = e.which || e.keyCode;//alert(_cC);
	if (_cC == 13) {
		_id.o("subButton").click();
		return true;
	}
	return false;
}
function verifyNShowTkts() {
	var agtValue = _id.o("agtId").value;
	var retOrgId = _id.o("retOrgId").value;
	var gameNo = _id.o("gameNo").value;
	if (agtValue == -1) {
		_id.i("errMsgDiv", "*"+i18nObj.prop('error.agent.empty'), true);
	} else {
		if (retOrgId == -1) {
			_id.i("errMsgDiv", "*"+i18nObj.prop('error.retailer.empty'), true);
		} else {
			if (gameNo == -1) {
				_id.i("errMsgDiv", i18nObj.prop('error.game.empty'), true);
			} else {
				var resp = _ajaxCall("bo_dg_ticketCancellation_ShowTicket.action?gameNo=" + gameNo + "&retOrgId=" + retOrgId);
				if (resp.result) {
					clearDiv();	
					_id.o("lastTenDiv").innerHTML = resp.data;
					_id.blk("lastTenDiv");
					_id.i("errMsgDiv", "", false);
					initDojo.startDojo(event);
				} else {
					_id.i("errMsgDiv", i18nObj.prop('error.tr.again'), true);
					return false;
				}
			}
		}
	}
}
function clearDiv() {
	_id.i("lastTenDiv", "");
	_id.i("result", "");
}
function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agtId");
	for ( var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}

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
/*function fetchRetailerList(val) {
	clearDiv();
	if (val != -1) {
		_resp = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?agentOrgName="+ val);
_respData = _resp.data;
if (_respData != "") {
	var resData = _respData.split(",");
	var retObj = _id.o("retOrgId");
	retObj.options.length = 1;
	for ( var i = 0; i < resData.length; i = i + 1) {
		var val = resData[i].split("=");
		var opt = new Option(val[1], val[1]);
		retObj.options[i + 1] = opt;
	}
} else if (_respData == "") {
	_id.o('retOrgId').value = -1;
	_id.o('retOrgId').options.length = 1;
}
} else {
_id.o("retOrgId").value = -1;
var retObj = _id.o("retOrgId");
retObj.options.length = 1;
}
}*/

function validateReason() {
_id.i("errDiv", "", false);
var arrInput = document.getElementsByTagName('input');
var count = 0;
for ( var i in arrInput) {
if(arrInput[i].name != undefined && arrInput[i].name.match('ticketNumArr') && arrInput[i].checked)
{
	count++;
	if (document.getElementById('reason_' + arrInput[i].value).selectedIndex == 0) {
		_id.i("errDiv", i18nObj.prop('error.enter.reason.fr.tkt.no')+" - "+arrInput[i].value, true);
		return false;
	}
}
}
if(count == 0)
{
_id.i("errDiv", i18nObj.prop('error.select.atleast.one.tkt'), true);
return false;
}

return true;
}