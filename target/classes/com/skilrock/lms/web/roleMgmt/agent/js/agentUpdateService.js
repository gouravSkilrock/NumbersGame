
function getTierInterfaceDiv(tierLevel, serviceName) {
	if (tierLevel == "AGENT" || tierLevel == "RETAILER") {
		_id.o("radioDiv" + serviceName).style.display = "block";
		_id.o("tierVal" + serviceName).value = "All";
		_id.o("radioAll" + serviceName).checked = true;
		_id.o("parentOrgDiv" + serviceName).style.display = "none";
		_id.o("childOrgDiv" + serviceName).innerHTML = "";
		_id.o("childOrgDiv" + serviceName).style.display = "none";
		_id.o("tierInterfaceDiv_" + serviceName).style.display = "block";
	} else {
		if (tierLevel == "All") {
			_id.o("radioDiv" + serviceName).style.display = "none";
			_id.o("parentOrgDiv" + serviceName).style.display = "none";
			_id.o("childOrgDiv" + serviceName).innerHTML = "";
			_id.o("childOrgDiv" + serviceName).style.display = "none";
			_id.o("tierInterfaceDiv_" + serviceName).style.display = "block";
		} else {
			_id.o("radioDiv" + serviceName).style.display = "none";
			_id.o("parentOrgDiv" + serviceName).style.display = "none";
			_id.o("childOrgDiv" + serviceName).innerHTML = "";
			_id.o("childOrgDiv" + serviceName).style.display = "none";
			_id.o("tierInterfaceDiv_" + serviceName).style.display = "none";
		}
	}
	var url = projectName + "/com/skilrock/lms/web/roleMgmt/agt_getInterface.action?tierLevel=" + tierLevel + "&serviceId=" + _id.o('serviceId'+serviceName).value;
	//alert("url"+url);
	_resp = _ajaxCall(url);
	//alert(_resp.data);
	_id.i("tierInterfaceDiv_" + serviceName, _resp.data);
}
function getParentOrg(radioBtn, tierLevelId) {
	if (radioBtn.value != "All") {
		var tierLevel = _id.o(tierLevelId).value;
		var url = "agt_fetchParentOrgList.action?tierLevel=" + tierLevel;
		var resData = _ajaxCall(url);
		var data = ((resData.data).replace("{", "")).replace("}", "");
		var parentOrgArr = data.split(","); 
	    //alert('parentOrgList'+tierLevelId.replace('tierLevel',''));
		var selParentOrg = _id.o("parentOrgList" + tierLevelId.replace("tierLevel", ""));
		selParentOrg.options.length = 0;
		selParentOrg.options[0] = new Option("--Please Select--", -1);
		for (var i = 0; i < parentOrgArr.length; i++) {
			var Org = parentOrgArr[i].split("=");
			selParentOrg.options[i + 1] = new Option(Org[1], Org[0]);
		}
		_id.o("tierVal" + tierLevelId.replace("tierLevel", "")).value = "Specific";
		_id.o("parentOrgDiv" + tierLevelId.replace("tierLevel", "")).style.display = "block";
	} else {
		_id.o("tierVal" + tierLevelId.replace("tierLevel", "")).value = "All";
		_id.o("parentOrgDiv" + tierLevelId.replace("tierLevel", "")).style.display = "none";
	}
}
function getChildOrg(id, serviceName, bgColor) {
	//alert(serviceName);
	var url = "agt_fetchChildOrgList.action?parentOrgId=" + _id.o(id).value;
	var resData = _ajaxCall(url);
	var data = ((resData.data).replace("{", "")).replace("}", "");
	var chlidOrgArr = data.split(",");
	var chkTbl = "<table width='100%' cellpadding='0' cellspacing='0' border='0' bordercolor='#cccccc' class='tabBdr' bgcolor='" + bgColor + "'><tr>";
	if (data != "") {
		for (var i = 0; i < chlidOrgArr.length; i++) {
			var Org = chlidOrgArr[i].split("=");
			chkTbl = chkTbl + "<td><input type='checkbox' name='chkChildOrg' value=" + Org[0] + ">" + Org[1] + "</td>";
			if ((i+1) % 5 == 0) {
				chkTbl = chkTbl + "</tr><tr>";
			}
		}
	} else {
		chkTbl = chkTbl + "<td>No organization to proceed</td>";
	}
	chkTbl = chkTbl + "</tr></table>";
	//alert(chkTbl);
	_id.o("childOrgDiv" + serviceName).innerHTML = chkTbl;
	_id.o("childOrgDiv" + serviceName).style.display = "block";
}
function interfaceStatusChange(id) {
	var hidInterface = "";
	var div = _id.o(id);
	var interfaceSel = div.getElementsByTagName("select");
	for (var i = 0; i < interfaceSel.length; i++) {
		if (interfaceSel[i].value != _id.o("intPrev" + interfaceSel[i].id).value) {
			hidInterface = hidInterface + "<input type='text' name='interfaceStatusNew' value='" + interfaceSel[i].value + "' />";
		} else {
			hidInterface = hidInterface + "<input type='text' name='interfaceStatusNew' value='' />";
		}
	}
	_id.i("hiddenInterface" + id.replace("tierInterfaceDiv_", ""), hidInterface);
	return true;
}
function check(id) {
	var chkValid = false;
	var serId = id.replace("form", "");
	var tierId = "tierLevel" + serId;
	//alert(_id.o(tierId).value);
	if (_id.o(tierId).value != "-1") {
		if (_id.o(tierId).value != "All") {
			var tierValId = "tierVal" + serId;
			//alert(_id.o(tierValId).value);
			if (_id.o(tierValId).value != "All") {
				var selParentOrgId = "parentOrgList" + serId;
				//alert(_id.o(selParentOrgId).value);
				if (_id.o(selParentOrgId).value != "-1") {
					var childOrgArr = _id.o("childOrgDiv" + serId).getElementsByTagName("input");
					var isChildChecked = false;
					//alert(childOrgArr.length);
					for (var i = 0; i < childOrgArr.length; i++) {
						if (childOrgArr[i].checked == true) {
							isChildChecked = true;
							break;
						}
					}
					if (!isChildChecked) {
						alert("Please Select Minimum One Organization");
						chkValid = false;
					} else {
						chkValid = true;
					}
				} else {
					alert("Please Select Parent Organization");
					_id.o(selParentOrgId).focus();
					chkValid = false;
				}
			} else {
				chkValid = true;
			}
		} else {
			chkValid = true;
		}
	} else {
		alert("Please Select Tier");
		_id.o(tierId).focus();
		chkValid = false;
	}
	return chkValid;
}
function showHideServiceDiv(id) {
	//alert(id);
	if (_id.o(id).style.display == "none") {
		_id.o(id).style.display = "block";
	} else {
		_id.o(id).style.display = "none";
	}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/roleMgmt/agent/js/Attic/agentUpdateService.js,v $'] = '$Id: agentUpdateService.js,v 1.1.2.2 2010/06/21 05:14:24 gaurav Exp $';