
function setAppData(buyData) {
	//alert("yogesh" + buyData);
	//alert('jsklfjasl');
	//alert(document.applets[0]);
	document.applets[0].showStatus(buyData);
	//document.applets[0].repaint();
}
function cancelTktAuto(tktNum, totAmt, printStatus) {
	_id.o("csSaleAppDiv").style.display = "block";
	//alert('printStatus:' + printStatus);
	if ("SUCCESS" == printStatus) {
		_id.o("csSaleAppDiv").style.display = "block";
		_ajaxCall("updateLastTxnId.action");
		return true;
	} else {
		if ("FAIL" == printStatus) {
			_id.o("csSaleAppDiv").style.display = "none";
			alert("Printer is not connected. Please OK to Reprint");
	    //alert("before");
		//var _resp = _ajaxCall('reprintLastTxnId.action');
		//alert("after" + _resp.data);
		//alert("again....");
		//setAppData(_resp.data);
		//('hello again');
		}
	}
	return false;
}
var isCircleGlobal = 'NO';
function isShowCircle(isCircle) {
	isCircleGlobal = isCircle;
	if("YES" == isCircleGlobal){
		_id.blk('circleDiv');
	} else if("NO" == isCircleGlobal){
		_id.non('circleDiv');
	}
}
function fillCircle(oprVal) {
	
	var circle = _id.o("state");
	circle.options.length = 1;
	_id.o("voucherType").options.length = 1;
	_id.o("denomination").options.length = 1;
	_id.o("amt").value = "0";
	_id.o("confirmAmt").value = "0";
	_id.o("mobileNo").value = "";
	_id.non("denomDiv");
	_id.non("amtDiv1");
	_id.non("amtDiv2");
	_id.non("confirmAmtDiv1");
	_id.non("confirmAmtDiv2");
	_id.non("mobileDiv1");
	_id.non("mobileDiv2");
	if (oprVal != -1) {
		var _resp = _ajaxCall("ret_cs_sale_fetch_circle_map.action?operatorCode=" + oprVal);
		var dataArr = _resp.data.replace("{", "").replace("}", "").replace(", ", ",").split(",");
		var circleArr = "";
		var opt = "";
		for (var i = 0; i < dataArr.length; i++) {
			circleArr = dataArr[i].split("=");
			opt = new Option(circleArr[1], circleArr[0]);
			circle.options[i + 1] = opt;
		}
	}
	
	if("NO" == isCircleGlobal){
		fillCategory('*');
	}
}
function fillCategory(circleVal) {
	var category = _id.o("voucherType");
	category.options.length = 1;
	_id.o("denomination").options.length = 1;
	_id.o("amt").value = "0";
	_id.o("confirmAmt").value = "0";
	_id.o("mobileNo").value = "";
	_id.non("denomDiv");
	_id.non("amtDiv1");
	_id.non("amtDiv2");
	_id.non("confirmAmtDiv1");
	_id.non("confirmAmtDiv2");
	_id.non("mobileDiv1");
	_id.non("mobileDiv2");
	var oprVal = _id.o("operatorCode").value;
	if (oprVal != -1 && circleVal != -1) {
		var _resp = _ajaxCall("ret_cs_sale_fetch_category_map.action?operatorCode=" + oprVal + "&state=" + circleVal);
		var dataArr = _resp.data.replace("{", "").replace("}", "").replace(", ", ",").split(",");
		var categoryArr = "";
		var opt = "";
		for (var i = 0; i < dataArr.length; i++) {
			categoryArr = dataArr[i].split("=");
			opt = new Option(categoryArr[1], categoryArr[0]);
			category.options[i + 1] = opt;
		}
	}
}
function fillDenominations(categoryVal) {

	var denomination = _id.o("denomination");
	denomination.options.length = 1;
	_id.o("amt").value = "0";
	_id.o("confirmAmt").value = "0";
	_id.o("mobileNo").value = "";
	_id.non("denomDiv");
	_id.non("amtDiv1");
	_id.non("amtDiv2");
	_id.non("confirmAmtDiv1");
	_id.non("confirmAmtDiv2");
	_id.non("mobileDiv1");
	_id.non("mobileDiv2");
	var oprVal = _id.o("operatorCode").value;
	var circleVal = _id.o("state").value;
	//alert(oprVal + ',,,' + circleVal + ',,,,' + categoryVal);
	if("NO" == isCircleGlobal){
		circleVal = '*';
	}
	if (oprVal != -1 && circleVal != -1 && categoryVal != -1) {
		if ("Pin" == categoryVal) {
		//alert('');
			var _resp = _ajaxCall("ret_cs_sale_fetch_denominations_map.action?operatorCode=" + oprVal + "&state=" + circleVal + "&voucherType=" + categoryVal);
			var dataArr = _resp.data.replace("{", "").replace("}", "").replace(", ", ",").split(",");
			var denominationArr = "";
			var opt = "";
			for (var i = 0; i < dataArr.length; i++) {
				denominationArr = dataArr[i].split("=");
				opt = new Option(denominationArr[1], denominationArr[0]);
				denomination.options[i + 1] = opt;
			}
			_id.blk("denomDiv");
		} else {
			if ("flexi" == categoryVal) {
				_id.blk("amtDiv1");
				_id.blk("amtDiv2");
				_id.blk("confirmAmtDiv1");
				_id.blk("confirmAmtDiv2");
				_id.blk("mobileDiv1");
				_id.blk("mobileDiv2");
			}
		}
	}
}
function updBalIframe() {
	parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
}
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/commercialService/playMgmt/js/Attic/csMgmt.js,v $"] = "$Id: csMgmt.js,v 1.1.2.11 2011/12/23 09:16:39 umesh Exp $";

