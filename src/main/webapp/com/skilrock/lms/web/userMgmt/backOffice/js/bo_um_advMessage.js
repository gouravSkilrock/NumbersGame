var i18nObj = parent.frames[0].i18Obj;
function fetchAdvRet(val) {
	var orgType = val;
	if(val == 'RETAILER' || val == 'PLAYER')
		val = 'AgentWise';
	// clearDiv();
	// $('#limitDiv').html('');
	var _resp = _ajaxCall(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_advMessage_Search.action?search_type=" + val + "&orgType=" + orgType);
	_id.i("result", _resp.data);
}

function fillChk(val) {
	var parentChk = _id.o("P" + val).checked;
	var tab = _id.o("tab" + val);
	var chkBx = tab.getElementsByTagName("input");
	for (var i = 0; i < chkBx.length; i++) {
		if (chkBx[i].type == "checkbox") {
			chkBx[i].checked = parentChk;
		}
	}
}

function fillHrMinSec() {
	var expHr = document.getElementById('expHr');
	var expMin = document.getElementById('expMin');
	var expSec = document.getElementById('expSec');
	expHr.innerHTML = "";
	expMin.innerHTML = "";
	expSec.innerHTML = "";
	var x=0;
	for(var i=0; i<=59; i++) {
		x = (i<10)?"0"+i:i;
		if(i<=23) {
			// expHr.innerHTML = expHr.innerHTML+"<option
			// value="+x+">"+x+"</option>";
			$('#expHr').append($('<option></option>').val(x).html(x));
		} if(i%5==0) {
			// expMin.innerHTML = expMin.innerHTML+"<option
			// value="+x+">"+x+"</option>";
			$('#expMin').append($('<option></option>').val(x).html(x));
		} if(i%15==0) {
			// expSec.innerHTML = expSec.innerHTML+"<option
			// value="+x+">"+x+"</option>";
			$('#expSec').append($('<option></option>').val(x).html(x));
		}
	}
	$('#expHr').val('23');
	$('#expMin').val('55');
	$('#expSec').val('45');
}

function selAllChkBx() {
	var tab = _id.o("searchTable");
	var chkBx = tab.getElementsByTagName("input");
	var selAll = _id.o("selAll").checked;
	var dis = false;
	if (selAll) {
		dis = true;
	}
	for (var i = 0; i < chkBx.length; i++) {
		if (chkBx[i].type == "checkbox" && chkBx[i].id != "selAll") {
			chkBx[i].checked = false;
			chkBx[i].disabled = dis;
		}
	}
}

function checkContentLimit() {
	var content = document.getElementById('content').value;
	var interfaceType = document.getElementById('interfaceType').value;
	var contentLength;
	if(interfaceType == 'TERMINAL') {
		contentLength = 250 - parseInt(content.length);
		if(contentLength < 0) {
			document.getElementById('contentCharLeft').innerHTML = '0/250';
			 document.getElementById('content').value = content.substring(0, 250);
		}
		document.getElementById('contentCharLeft').innerHTML = '('+(contentLength) + '/250)';
	}
	if(interfaceType == 'WEB') {
		contentLength = 250 - parseInt(content.length);
		if(contentLength < 0) {
			document.getElementById('contentCharLeft').innerHTML = '0/250';
			 document.getElementById('content').value = content.substring(0, 250);
		}
		document.getElementById('contentCharLeft').innerHTML = '('+(contentLength) + '/250)';
	}

	return true;
}

function chklimit(obj){
	var val = document.getElementById('message').value;
	var orgVal = document.getElementById('orgId').value;
	 //alert(orgVal);
	if(orgVal == 'RETAILER'){
		 document.getElementById('msgCharLeft').innerHTML = '('+(150 -
		 parseInt(val.length)) + '/150)';
	} else {
		 document.getElementById('msgCharLeft').innerHTML = '('+(150 -
		 parseInt(val.length)) + '/150)';
	}
	
	return true;
}
function chklimitSubject(obj){
	var val = document.getElementById('subject').value;
	var orgVal = document.getElementById('orgId').value;
	 //alert(orgVal);
	if(orgVal == 'RETAILER'){
		 document.getElementById('msgCharLeft').innerHTML = '('+(100 -
		 parseInt(val.length)) + '/100)';
	} else {
		 document.getElementById('msgCharLeft').innerHTML = '('+(100 -
		 parseInt(val.length)) + '/100)';
	}
	
	return true;
}
function validate() {
	var errMsg = "";
	var orgValue = _id.o("orgId").value;
	if(orgValue == -1){
		errMsg = errMsg + i18nObj.prop('error.select.msg.fr')+"<br/>";
		// alert(errMsg);
	} else if(orgValue == 'PLAYER'){
		var servId = _id.o('serviceId').value;
		if(servId != -1){
			_id.o('serviceType').value = servId;
			
			if("DG" == servId){
				var isGame = false;
				var gameNo = _id.n("gameNo");
				for (var i = 0; i < gameNo.length; i++) {
					if (gameNo[i].checked == true) {
						isGame = true;
					}
				}
				if (!isGame) {
					errMsg = errMsg + i18nObj.prop('error.select.atleast.one.game')+"<br/>";
				}
			}
			
			if("CS" == servId){
				var isCategory = false;
				var categoryNo = _id.n("categoryNo");
				for (var i = 0; i < categoryNo.length; i++) {
					if (categoryNo[i].checked == true) {
						isCategory = true;
					}
				}
				if (!isCategory) {
					errMsg = errMsg + i18nObj.prop('error.select.atleast.one.cat')+ "<br/>";
				}
			}

			if("SLE" == servId){
				var isGame = false;
				var gameNo = _id.n("sleGameNo");
				for (var i = 0; i < gameNo.length; i++) {
					if (gameNo[i].checked == true) {
						isGame = true;
					}
				}
				if (!isGame) {
					errMsg = errMsg + i18nObj.prop('error.select.atleast.one.game')+"<br/>";
				}
			}
			if("IW" == servId){
				var isGame = false;
				var gameNo = _id.n("iwGameNo");
				for (var i = 0; i < gameNo.length; i++) {
					if (gameNo[i].checked == true) {
						isGame = true;
					}
				}
				if (!isGame) {
					errMsg = errMsg + i18nObj.prop('error.select.atleast.one.game')+"<br/>";
				}
			}
			if("VS" == servId){
				var isGame = false;
				var gameNo = _id.n("vsGameNo");
				for (var i = 0; i < gameNo.length; i++) {
					if (gameNo[i].checked == true) {
						isGame = true;
					}
				}
				if (!isGame) {
					errMsg = errMsg + i18nObj.prop('error.select.atleast.one.game')+"<br/>";
				}
			}
		} else {
			errMsg = errMsg + i18nObj.prop('error.service.noselect')+ "<br/>";
		}
		
		if (_id.o("msgLoc").value == "-1") {
			errMsg = errMsg + i18nObj.prop('error.msglocation.noselect')+"<br/>";
		}

		var msg = _id.o("message").value;
		if (msg == "") {
			errMsg = errMsg + i18nObj.prop('error.advertmsg.empty')+"<br/>";
		} else {
			if (msg.replace(/ /g, "").length == 0) {
				errMsg = errMsg + i18nObj.prop('error.advertmsg.not.meaningful')+ "<br/>";
			} else {
				if (msg.length > 1199) {
					errMsg = errMsg + i18nObj.prop('error.msg.exceed.max')+"<br/>";
				}
			}
		}
	} else if(orgValue == 'BO' || orgValue == 'AGENT' || orgValue == 'RETAILER') {
		var msg = _id.o("subject").value;
		if (msg.trim() == "") {
			errMsg = errMsg + i18nObj.prop('error.subject.empty')+"<br/>";
		}

		var msg = _id.o("content").value;
		if (msg.trim() == "") {
			errMsg = errMsg + i18nObj.prop('error.content.empty')+"<br/>";
		}

		if(orgValue == 'RETAILER' && _id.o("interfaceType").value == 'TERMINAL') {
			var msg = _id.o("subject").value;
			if (msg.indexOf("~") != -1 || msg.indexOf("#") != -1 || msg.indexOf("|") != -1 || msg.indexOf("%") != -1) {
				errMsg = errMsg + i18nObj.prop('error.subject.char')+"<br/>";
			}

			var msg = _id.o("content").value;
			if (msg.indexOf("~") != -1 || msg.indexOf("#") != -1 || msg.indexOf("|") != -1 || msg.indexOf("%") != -1) {
				errMsg = errMsg +i18nObj.prop('error.content.char')+ "<br/>";
			}
		}
	}

	if(_id.o("mode").value != 'REGISTRATION' && $("input:radio[name=userSelection]:checked").val()!='ALL') {
		var tab = _id.o("searchTable");
		var chkBx = tab.getElementsByTagName("input");
		var checked = false;
		for (var i = 0; i < chkBx.length; i++) {
			if (chkBx[i].type == "checkbox" && chkBx[i].checked==true) {
				checked = true;
				break;
			}
		}
		if(!checked){
		errMsg = errMsg + i18nObj.prop('error.org.noselect')+ "<br/>";
		}
	}
	
	_id.i("errMsgDiv", errMsg, "e");
	if (errMsg.length == 0) {
		return true;
	}
	return false;
}

	function resetAll() {
		$('#message').val('');
		$('#subject').val('');
		$('#content').val('');
		$('#isPopup').prop('checked', false);
		$('#isMandatory').prop('checked', false);
		$('#errMsgDiv').html('');
		$('#msgCharLeft').html('');
		$('#contentCharLeft').html('');
		$('#serviceId').prop('selectedIndex', 0);
		$('#mode').prop('selectedIndex', 0);
		$('#interfaceType').prop('selectedIndex', 0);
		$('#expHr').val('23');
		$('#expMin').val('55');
		$('#expSec').val('45');
		$('#search_type').prop('selectedIndex', 0);
		$('#msgLoc').prop('selectedIndex', 0);
		$('#activity').prop('selectedIndex', 0);
		$('#activity1').prop('selectedIndex', 0);
		$('#activity2').prop('selectedIndex', 0);
		$('#activity3').prop('selectedIndex', 0);
	}

	function clearAllDivs() {
		document.getElementById('serviceDiv').style.display = 'none';
		// document.getElementById('modeDiv').style.display = 'none';
		document.getElementById('msgDiv1').style.display = 'none';
		document.getElementById('msgDiv2').style.display = 'none';
		document.getElementById('chkDiv').style.display = 'none';
		document.getElementById('msgCharLeft').style.display = 'none';
		document.getElementById('contentCharLeft').style.display = 'none';
		document.getElementById('gamesDiv').style.display = 'none';
		document.getElementById('sleGameDiv').style.display = 'none';
		document.getElementById('iwGameDiv').style.display = 'none';
		document.getElementById('vsGameDiv').style.display = 'none';
		document.getElementById('walletDiv').style.display = 'none';
		document.getElementById('categoryDiv').style.display = 'none';
		document.getElementById('searchTypeDiv').style.display = 'none';
		document.getElementById('msgLocDiv').style.display = 'none';
		document.getElementById('activityDiv').style.display = 'none';
		document.getElementById('activityDiv1').style.display = 'none';
		document.getElementById('activityDiv2').style.display = 'none';
		document.getElementById('activityDiv3').style.display = 'none';
	}

	function setInterfaceList(val) {
		var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_advMessage_getInterfaceList.action?orgType="+val);
		var interfaceName = _resp.split(',');
		var interfaceType = document.getElementById("interfaceType");
		interfaceType.innerHTML = "";
		for(i=0; i<interfaceName.length; i++) {
			var opt = document.createElement('option');
		    opt.value = interfaceName[i];
		    opt.innerHTML = interfaceName[i];
		    interfaceType.appendChild(opt);
		}
	}

	function showMsgDiv(val){
		var advMsg = document.getElementById('message');
		advMsg.value = '';
		_id.o("serviceId").selectedIndex=0;
		clearAllDivs();
		resetAll();
		if(val == '-1') {
			document.getElementById('result').innerHTML = '';
		} else if(val == 'BO') {
			document.getElementById('modeDiv').style.display = 'block';
			document.getElementById('msgDiv2').style.display = 'block';
			document.getElementById('contentCharLeft').style.display = 'block';
			document.getElementById('contentCharLeft').innerHTML = '(500/500)';
			setInterfaceList(val);
			$("#subject").attr('maxlength','100');
			$("#content").attr('maxlength','1000');
		} else if(val == 'AGENT') {
			document.getElementById('modeDiv').style.display = 'block';
			document.getElementById('msgDiv2').style.display = 'block';
			document.getElementById('contentCharLeft').style.display = 'block';
			document.getElementById('contentCharLeft').innerHTML = '(250/250)';
			setInterfaceList(val);
			$("#subject").attr('maxlength','20');
			$("#content").attr('maxlength','250');
		} else if(val == 'RETAILER') {
			document.getElementById('modeDiv').style.display = 'block';
			document.getElementById('msgDiv2').style.display = 'block';
			document.getElementById('searchTypeDiv').style.display = 'block';
			document.getElementById('contentCharLeft').style.display = 'block';
			 document.getElementById('contentCharLeft').innerHTML = '(150/150)';
			setInterfaceList(val);
			$("#subject").attr('maxlength','20');
			$("#content").attr('maxlength','250');
		} else if (val == 'PLAYER') {
			document.getElementById('modeDiv').style.display = 'none';
			document.getElementById('serviceDiv').style.display = 'block';
			advMsg.maxLength = 150;
			document.getElementById('msgCharLeft').innerHTML = '(150/150)';
			document.getElementById('msgLocDiv').style.display = 'block';
			document.getElementById('activityDiv').style.display = 'block';
			document.getElementById('msgDiv1').style.display = 'block';
			document.getElementById('msgCharLeft').style.display = 'block';
			document.getElementById('searchTypeDiv').style.display = 'block';
		}
	}

	function onServiceChng(val){
		document.getElementById('gamesDiv').style.display = 'none';
		document.getElementById('sleGameDiv').style.display = 'none';
		document.getElementById('iwGameDiv').style.display = 'none';
		document.getElementById('vsGameDiv').style.display = 'none';
		document.getElementById('categoryDiv').style.display = 'none';
		document.getElementById('walletDiv').style.display = 'none';
		document.getElementById('activityDiv').style.display = 'none';
		document.getElementById('activityDiv2').style.display = 'none';
		document.getElementById('activityDiv3').style.display = 'none';
		if(val != -1){
			if("DG" == val){
				document.getElementById('gamesDiv').style.display = 'block';
				document.getElementById('activityDiv').style.display = 'block';
			} else if("CS" == val){
				document.getElementById('categoryDiv').style.display = 'block';
				document.getElementById('activityDiv2').style.display = 'block';
			} else if("OLA" == val){
				document.getElementById('walletDiv').style.display = 'block';
				document.getElementById('activityDiv3').style.display = 'block';
			} else if("SLE" == val){
				document.getElementById('sleGameDiv').style.display = 'block';
				document.getElementById('activityDiv').style.display = 'block';
			} else if("IW" == val){
				document.getElementById('iwGameDiv').style.display = 'block';
				document.getElementById('activityDiv').style.display = 'block';
			} else if("VS" == val){
				document.getElementById('vsGameDiv').style.display = 'block';
				document.getElementById('activityDiv').style.display = 'block';
			} 
		}
	}
	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/bo_um_advMessage.js,v $'] = '$Id: bo_um_advMessage.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $';