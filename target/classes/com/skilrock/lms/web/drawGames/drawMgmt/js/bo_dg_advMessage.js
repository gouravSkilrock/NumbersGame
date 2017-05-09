function fetchAdvRet(val) {
				//clearDiv();
	var _resp = _ajaxCall(projectName + "/com/skilrock/lms/web/drawGames/drawMgmt/bo_dg_advMessage_Search.action?search_type=" + val);
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
function chklimit(obj){
	var val = document.getElementById('message').value;
	var orgVal = document.getElementById('orgId').value;
	//alert(orgVal);
	if(orgVal == 'RETAILER'){
		document.getElementById('msgCharLeft').innerHTML = '('+(160 - parseInt(val.length)) + '/160)';
	} else {
		document.getElementById('msgCharLeft').innerHTML = '('+(50 - parseInt(val.length)) + '/50)';
	}
	
	return true;
}
function validate() {
	var errMsg = "";
	var orgValue = _id.o("orgId").value;
	
	if(orgValue == -1){
		errMsg = errMsg + "Please Select Message For<br/>";
		//alert(errMsg);
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
					errMsg = errMsg + "Please Select Atleast One Game<br/>";
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
					errMsg = errMsg + "Please Select Atleast One Category<br/>";
				}
			}
		} else {
			errMsg = errMsg + "Please Select Service<br/>";
		}
		
		if (_id.o("msgLoc").value == "-1") {
			errMsg = errMsg + "Please Select Message Location<br/>";
		}
	}
	
	var msg = _id.o("message").value;
	if (msg == "") {
		errMsg = errMsg + "Please enter Message<br/>";
	} else {
		if (msg.replace(/ /g, "").length == 0) {
			errMsg = errMsg + "Please Enter some meaning full Message<br/>";
		} else {
			if (msg.length > 1199) {
				errMsg = errMsg + "Maximun 2000 characters are allowed for Message<br/>";
			}
		}
	}
	
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
	errMsg = errMsg + "Please Select Atleast One Organization<br/>";
	}
	
	_id.i("errMsgDiv", errMsg, "e");
	if (errMsg.length == 0) {
		return true;
	}
	return false;
}

	function showMsgDiv(val){
		var advMsg = document.getElementById('message');
		advMsg.value = '';
		_id.o("serviceId").selectedIndex=0;
		if(val == 'RETAILER'){
			document.getElementById('modeDiv').style.display = 'block';
			document.getElementById('serDiv').style.display = 'none';
			//alert(advMsg.maxLength);
			//alert(advMsg.size);
			advMsg.maxLength = 160;
			document.getElementById('msgCharLeft').innerHTML = '(160/160)';
			
			document.getElementById('gamesDiv').style.display = 'none';
			document.getElementById('categoryDiv').style.display = 'none';
			document.getElementById('msgLocDiv').style.display = 'none';
			
			document.getElementById('activityDiv').style.display = 'none';
			document.getElementById('activityDiv1').style.display = 'block';
			document.getElementById('activityDiv2').style.display = 'none';
			
		} else {
			document.getElementById('modeDiv').style.display = 'none';
			document.getElementById('serDiv').style.display = 'block';
			//alert(advMsg.maxLength);
			//alert(advMsg.size);
			advMsg.maxLength = 50;
			document.getElementById('msgCharLeft').innerHTML = '(50/50)';
			
			//document.getElementById('gamesDiv').style.display = 'block';
			document.getElementById('msgLocDiv').style.display = 'block';
			
			document.getElementById('activityDiv').style.display = 'block';
			document.getElementById('activityDiv1').style.display = 'none';
			document.getElementById('activityDiv2').style.display = 'none';
		}
	}
	
	function onServiceChng(val){
		document.getElementById('gamesDiv').style.display = 'none';
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
			} 
		}
	}
	
	
	
	