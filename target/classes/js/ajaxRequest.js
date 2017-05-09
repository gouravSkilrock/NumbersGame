var i18nObj = parent.frames[0].i18Obj;

var _id = {o:function (el_id) {
	return document.getElementById(el_id);
}, n:function (el_id) {
	return document.getElementsByName(el_id);
}, tn:function (el_id) {
	return document.getElementsByTagName(el_id);
}, i:function (el_id, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.o(el_id).innerHTML = msg;
}, v:function (el_id, r) {
	if (r) {
		return this.o(el_id).value == "";
	}
	return this.o(el_id).value;
}, iniFocus:function (el_id) {
	this.o(el_id).focus();
}, blk:function (el_id) {
	this.o(el_id).style.display = "block";
}, non:function (el_id) {
	this.o(el_id).style.display = "none";
}, hid:function (el_id) {
	this.o(el_id).style.visibility = "hidden";
}, vis:function (el_id) {
	this.o(el_id).style.visibility = "visible";
}, fo:function (f, ele) {
	return document.forms[f].elements[ele];
}, fi:function (f, ele, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.fo(f, ele).innerHTML = msg;
}, fv:function (f, ele, r) {
	if (r) {
		return this.fo(f, ele).value == "";
	}
	return this.fo(f, ele).value;
}, finiFocus:function (f, ele) {
	this.fo(f, ele).focus();
}};
var request;
var selectBox;
var tierMap = parent.frames[0].tierArr;
function onLoadAjax(url, val) {
// alert(url);
	selectBox = val;
	_resp = _ajaxCall(url);
// alert(_resp.result+":"+_resp.data);
	if (_resp.result) {
		_respData = _resp.data;
		if (selectBox == "avlCredit") {
			if (_respData == "N") {
			} else {
					// 1 = Avl; 2 = ClmBal; 3 = UnClm
					// alert(" _respData "+_respData);
				var resData = _respData.split("=");
					// alert(resData.length+" "+resData);
				if (BrowserDetect.browser == "Firefox") {
					window.parent.top.document.getElementById("avlCredit").innerValue = resData[0];
					window.parent.top.document.getElementById("avlCreditClm").innerValue = resData[1];
					window.parent.top.document.getElementById("avlCreditUNClm").innerValue = resData[2];
				} else {
					parent.frames[0].document.getElementById("avlCredit").innerText = resData[0];
					parent.frames[0].document.getElementById("avlCreditClm").innerText = resData[1];
					parent.frames[0].document.getElementById("avlCreditUNClm").innerText = resData[2];
				}
			}
		} else {
			var newdiv = document.createElement("div"); // This creates a
														// dynamic new div for
														// holding data from
														// ajax response
			var divId = "ajaxDiv";
			newdiv.setAttribute("id", divId);
			newdiv.style.visibility = "hidden";
			newdiv.innerHTML = _respData;
			document.body.appendChild(newdiv);
			if (selectBox == "game") {
				selectBox = "supplierName";
				newdiv.innerHTML = (_respData).substring((_respData).indexOf("</select") + 9);
				uploadInventory(_respData);
				load();
			} else {
				if (selectBox == "country") {  // Vaibhav Change this on
												// 24-09-2010 for Lagos
					AddOptions(_id.n("ajaxList")[0]);
					_id.o(selectBox).value = _id.n("ajaxList")[0][1].text;
					_ajaxCall("getState.action?country=" + _id.n("ajaxList")[0][1].text, "characters");
					_id.o("code").value=_ajaxCall("getCountryPhnCode.action?country=" + _id.n("ajaxList")[0][1].text).data;
					
				} else {
					AddOptions(document.getElementsByName("ajaxList")[0]);
				}
			}
		}
	}
}
function AddOptions(ajaxList) {
	var newSelectbox = document.getElementById(selectBox);
	newSelectbox.options.length = 1;
	for (i = 0, l = ajaxList.options.length; i < l; i++) {
		if (i != 0) {
			var obj = ajaxList[i];
			var opt = new Option(obj.text, obj.value);
			newSelectbox.options[i] = opt;
		}
	}
}
function uploadInventory(responseText) {
	document.getElementById("game").innerHTML = (responseText).substring(0, (responseText).indexOf("</select>")) + "</select>";
	AddOptions(document.getElementById("ajaxList"));
}
var eCode = "", logJsErr = projectName + "/com/skilrock/lms/web/loginMgmt/logJsError.action?message=";
var orgName = "", brNmVer = "", refreshDivLabl = "", frm0 = parent.frames[0], frm1 = parent.frames[1], _bd;
var e1 = "Error on Page (Error Code : ", e2 = ")\n Please contact Vendor.";
function checkCache() {
	var jspPage = "";
	if (BrowserDetect) {
		_bd = BrowserDetect;
		brNmVer = "Browser--" + _bd.browser + " Ver --" + _bd.version + " OS--" + _bd.OS;
		logJsErr = logJsErr + "--" + brNmVer;
	}
	if (frm0.document.getElementById("currentLoggedUser")) {
		orgName = "&agentOrgName=" + frm0.document.getElementById("currentLoggedUser").childNodes[0].nodeValue;
	}
	if (frm0.document.getElementById("refreshDivLabel")) {
		refreshDivLabl = frm0.document.getElementById("refreshDivLabel");
	}
		// alert(frm0.cachedMap.length+"headId---"+document.getElementById("headId").childNodes[0].nodeValue
		// +"*"+frm0.Version.length);
	if (document.getElementById("headId")) {
		var obj = document.getElementById("headId").childNodes[0].nodeValue.replace(/\n/g,"");
		jspPage = obj.split(",")[0].replace("$RCSfile: ", "").replace(/ /g, "");
		var version = (obj.split(",")[1]);
		version = version.substring(version.indexOf("Revision:")+9,version.lastIndexOf("$")).replace(/ /g, "");
		//alert("Name-" + jspPage + "***Ver on Ser-" + frm0.cachedMap[jspPage] + "**Ver**" + version + "**n**" + obj);
		if (typeof jspPage == "undefined") {
			eCode = "V10002";
				// alert(e1+eCode+e2);
			repRef(jspPage, logJsErr + e1 + eCode + e2 + "File Name-" + jspPage + " Jsp Ver-" + version);
			return;
		} else {
			if (typeof frm0.cachedMap[jspPage] != "undefined") {
				if (version != frm0.cachedMap[jspPage]) {
					eCode = "V10002";
					// alert(e1+eCode+e2);
					repRef(jspPage, logJsErr + e1 + eCode + e2 + "FileName- " + jspPage + "Ver " + version + " Ver in Map  " + frm0.cachedMap[jspPage]);
					return;
				}
			}
		}
		checkCacheJS(jspPage);
	} else {
		frm0.uncachedRefresh = frm0.uncachedRefresh + 1;
		if (frm0.uncachedRefresh < 2) {
			frm1.location.reload(true);
			refreshDivLabl.style.display = "block";
			return;
		} else {
			eCode = "V10004";
			// alert(e1+eCode+e2);
			frm0.ajaxReq(logJsErr + e1 + eCode + e2 + "**" + orgName, "jsErr");
			frm0.uncachedRefresh = 0;
			frm1.location.replace(projectName + "/com/skilrock/lms/web/loginMgmt/internalErr.action");
				// alert(jsErrMsg);
			return;
		}
	}
}
function repRef(jspPage, errMsg) {
	var matched = false, refPgsLen = frm0.refJspPgs.length, cntRef = 0;
	if (frm0.refJspPgs[jspPage]) {
		matched = true;
		cntRef = frm0.refJspPgs[jspPage];
	}
	if (matched) {
		if (cntRef < 1) {
			incRefCount(jspPage);
			return;
		} else {
			frm0.refJspPgs[jspPage] = "" + parseInt(parseInt(frm0.refJspPgs[jspPage]) + 1);
					// alert(errMsg+orgName+" Accessed
					// "+frm0.refJspPgs[jspPage]+" times after Error");
			frm0.ajaxReq(errMsg + orgName + " Accessed " + frm0.refJspPgs[jspPage] + " times after Error", "jsErr");
		}
	} else {
		incRefCount(jspPage);
	}
}
function incRefCount(jspPage) {
	frm0.refJspPgs[jspPage] = "1";
	frm1.location.reload(true);
	refreshDivLabl.style.display = "block";
}
function checkCacheJS(jspPage) {
	var keyInMap = "", dataToMatch = "", dataInServ = "";
	var curPageJSArr = new Array();
	for (var i in document.getElementsByTagName("script")) {
		var jsFileName = document.getElementsByTagName("script")[i].src;
		if (jsFileName && !jsFileName.match("struts") && !jsFileName.match("ajaxRequest") && !jsFileName.match("ajaxDojo") && !jsFileName.match("jsSorting")) {
			if (jsFileName.indexOf(projectName) != -1) {
				curPageJSArr[jsFileName] = jsFileName.substring(jsFileName.lastIndexOf("/") + 1, jsFileName.length);
			} else {
				curPageJSArr[jsFileName] = jsFileName.substring(jsFileName.lastIndexOf("/") + 1, jsFileName.length);
			}
		}
	}
	for (var j in curPageJSArr) {
		var inVersion = false, verOnPg = "";
		for (var i in frm0.Version) {
			if (i.match(curPageJSArr[j])) {
				inVersion = true;
				keyInMap = i.substring(i.indexOf("com") - 1, i.indexOf(".js,v"));
				var ver = frm0.Version[i];
				verOnPg = ver.substring(ver.indexOf(".js,v") + 5, ver.indexOf("/") - 4).replace(/ /g, "");
				break;
			}
		}
			// alert("JS on serv-"+keyInMap+"n ver on serv-*"+frm0.cachedMap[keyInMap]+"*nJS on page-"+j+"n Ver on page-*"+verOnPg+"*");
		if (!inVersion) {
			eCode = "V10005";
			refRes = repRef(jspPage, logJsErr + e1 + eCode + e2 + "File Not in Ver-- FileName on Page  " + curPageJSArr[j]);
			return;
			break;
		} else {
			dataInServ = frm0.cachedMap[keyInMap];
			if (typeof dataInServ == "undefined") {
				eCode = "V10003";
									// alert(e1+eCode+e2);
				repRef(jspPage, logJsErr + e1 + eCode + e2 + "FileName on page " + keyInMap + " Version on Server Not Present- Ver on Page  " + verOnPg);
				return;
				break;
			}
			if (verOnPg != dataInServ) {
				eCode = "V10003";
							// alert(e1+eCode+e2);
				repRef(jspPage, logJsErr + e1 + eCode + e2 + "JS on serv-" + keyInMap + " ver on serv-" + dataInServ + "JS on page-" + j + " Ver on page-" + verOnPg);
				return;
				break;
			}
		}
	}
}
function _ajaxCall(url, div) {
	var _req;
	var _resData;
	var _result = false;
	var _c = "?";
	if (url.indexOf("?") != -1) {
		_c = "&";
	}
	url = url + _c + new Date().getTime() + parent.frames[0].sesId + Math.random() * 1000000;
	if (typeof div != "undefined") {
		_id.i(div, parent.frames[0].i18Obj.prop('msg.loading')+".....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>");
		_id.o(div).style.display = "block";
	}
	// alert('Ajax Req n'+url);
	_req = _XMLRequest();
	_req.onreadystatechange = function () {
		if (_req.readyState == 4) {
			if (_req.status == 200) {
				if(_req.responseText.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
					window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
				}									// alert('Ajax Req
												// n'+_req.responseText);
				_resData = _req.responseText;
				_result = true;
				if (typeof div != "undefined") {
					_id.i(div, _resData);
				}
			}
		}
	};
	_req.open("GET", url, false);
	_req.setRequestHeader("If-Modified-Since", new Date().getTime());
	_req.send(null);
	if(_req.status == 200){
		if(_req.responseText.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
			window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
		}
		_resData = _req.responseText;
		_result = true;
		if (typeof div != "undefined") {
			_id.i(div, _resData);
		}
	}
	return {result:_result, data:_req.responseText};
}
function _XMLRequest() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	}
	return new ActiveXObject("Microsoft.XMLHTTP");
}
var asyncReq = null;
var asyncDiv = null;
var asyncResult = null;
function _ajaxUnsync(url, fnToCall, param) {
	var _resData;
	var _result = false;
	asyncDiv = fnToCall;
	var _c = "?";
	if (url.indexOf("?") != -1) {
		_c = "&";
	}
	url = url + _c + new Date().getTime() + parent.frames[0].sesId + Math.random() * 1000000;

	// alert('Ajax Req n'+url);
	asyncReq = _XMLRequest();
	asyncReq.onreadystatechange = reqChange;
	if (param == undefined) {
		asyncReq.open("GET", url, false);
		asyncReq.setRequestHeader("If-Modified-Since", new Date().getTime());
		asyncReq.send(null);
		if(BrowserDetect.browser == 'Firefox'){
			if(asyncReq.responseText.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
				window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
			}
			asyncResult = asyncReq.responseText;
			eval(asyncDiv + "()");
		}
	} else {
		asyncReq.open("POST", url.substring(0, url.indexOf("?")), false);
		asyncReq.setRequestHeader("If-Modified-Since", new Date().getTime());
		asyncReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		asyncReq.setRequestHeader("Content-length", param.length);
		asyncReq.setRequestHeader("Connection", "close");
		asyncReq.send(param);
	}
}
function reqChange() {
	if (asyncReq.readyState == 4) {
		if (asyncReq.status == 200) {
			if(asyncReq.responseText.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
				window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
			}
			asyncResult = asyncReq.responseText;
			eval(asyncDiv + "()");
		} else {
			alert(i18nObj.prop('error.problem'));
		}
	}
}
function _subValid(subBtnId) {
	var divDisp = document.getElementById("subCheck");
	if (typeof subBtnId != "undefined") {
		document.getElementById(subBtnId).style.visibility = "hidden";
	}
	if (typeof divDisp != "undefined") {
		divDisp.innerHTML = i18nObj.prop('label.wait')+"....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
	}
	var returnType = confirm(i18nObj.prop('error.want.to.proceed'));
	if (returnType) {
		return true;
	} else {
		if (typeof subBtnId != "undefined") {
			document.getElementById(subBtnId).style.visibility = "visible";
		}
		divDisp.innerHTML = "";
		return false;
	}
}
function _subValidAnchor() {
	var arrAnchorElem = document.getElementsByTagName("a");
	var anchorLen = arrAnchorElem.length;
	for (var i = 0; i < anchorLen; i++) {
		var ele = arrAnchorElem[i];
		if ((ele.onclick != null) && (typeof ele.onclick != "undefined")) {
			var isSubVal = (ele.onclick).toString();
			if ((isSubVal.indexOf("_subValidAnchor")) != -1) {
				ele.innerHTML = "";
			}
		}
	}
	document.getElementById("subCheck").innerHTML = i18nObj.prop('label.wait')+"....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
	return true;
}
// Changed for Org_code Implementation
function fetchLimits(orgName,agtDisplayName) {
	var div = "limitDiv";
	if ((typeof _id.o("limitDiv") == "undefined") || _id.o("limitDiv") == null) {
		_id.i("top_form", "<div id = \"limitDiv\" style=\"align:center;\"></div>" + _id.o("top_form").innerHTML);
	}
	if (orgName != -1) {
		var resp = _ajaxCallTextData("fetchLimits.action?agentOrgName=" + encodeURIComponent(orgName));
		var outBal = _ajaxCallTextData("fetchOutBal.action?agentOrgName=" + encodeURIComponent(orgName));
		var bal = resp.split("=");
		_id.i(div, "<br/><font color='#4284B0'>Balance of <b>" + agtDisplayName + "</b> is <b>" + (parseFloat(bal[0]) - parseFloat(bal[1])) + "</b>" +
				" <br/> " +
				"Outstanding Balance for "+ "<b>" + agtDisplayName + "</b> "+ i18nObj.prop('label.is') + " <b>" + outBal + "</b></font>" , "e");
	} else {
		_id.i(div, "");
	}
}
function fetchLimitsOfRetailer(orgName,agtDisplayName) {
	var div = "limitDiv";
	if ((typeof _id.o("limitDiv") == "undefined") || _id.o("limitDiv") == null) {
		_id.i("top_form", "<div id = \"limitDiv\" style=\"align:center;\"></div>" + _id.o("top_form").innerHTML);
	}
	if (orgName != -1) {
		var resp = _ajaxCallTextData("fetchLimits.action?agentOrgName=" + encodeURIComponent(orgName));
		var outBal = _ajaxCallTextData("fetchRetailerOutBal.action?retOrgName=" + encodeURIComponent(orgName));
		var bal = resp.split("=");
		_id.i(div, "Balance of <b>" + agtDisplayName + "</b> is <b>" + (parseFloat(bal[0]) - parseFloat(bal[1])).toFixed(2) + "</b>"+
				" <br/> " +
				"Outstanding Balance for "+ "<b>" + agtDisplayName + "</b> is <b>" + outBal + "</b></font>" , "e");
	} else {
		_id.i(div, "");
	}
}
function addCommas(nStr) {
	nStr += "";
	x = nStr.split(".");
	x1 = x[0];
	x2 = x.length > 1 ? "." + x[1] : "";
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, "$1" + "," + "$2");
	}
	return x1 + x2;
}
function onlyNum(evt) {
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if (charCode >= 48 && charCode < 58) {
		return true;
	}
	return false;
}
function onlyDecimal(evt) {
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if ((charCode >= 48 && charCode < 58) || charCode == 46) {
		if (charCode == 46) {
			var temp = e.srcElement.value;
			if ((temp.split(".")).length > 1) {
				return false;
			}
		}
		return true;
	}
	return false;
}
function onlyDecimalSigned(evt) {
	var e = evt || event;
	var charCode = e.which || e.keyCode;
	if ((charCode >= 48 && charCode < 58) || charCode == 46 || charCode == 45 || charCode == 8 || charCode == 37 || charCode == 39) {
		if (charCode == 46) {
			var temp = e.srcElement.value;
			if ((temp.split(".")).length > 1) {
				return false;
			}
		}
		return true;
	}
	return false;
}

function fetchDivByName(doc,name){
var retDivArr = new Array();	
var divArr = doc.getElementsByTagName("div");
for(var i=0;i<divArr.length;i++){
	if(divArr[i].getAttribute("name")==name||divArr[i].getAttribute("id")==name){
		retDivArr.push(divArr[i]);
	}
}
return retDivArr;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/js/ajaxRequest.js,v $'] = '$Id: ajaxRequest.js,v 1.6 2016/10/31 09:45:30 neeraj Exp $';
