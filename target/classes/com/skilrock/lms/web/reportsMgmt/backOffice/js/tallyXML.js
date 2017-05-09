



var mainDiv;
var req;
var which;
function retrieveURL(url, div) {
	mainDiv = div;
	
//alert(url);

	if (window.XMLHttpRequest) { // Non-IE browsers
		req = new XMLHttpRequest();
		req.onreadystatechange = processStateChange;
		try {
			req.open("GET", url, true);
			req.setRequestHeader("If-Modified-Since", new Date().getTime());
		}
		catch (e) {
			alert(e);
		}
		req.send(null);
	} else {
		if (window.ActiveXObject) { // IE
			req = new ActiveXObject("Microsoft.XMLHTTP");
			//alert(req);
			if (req) {
				req.onreadystatechange = processStateChange;
				req.open("GET", url, true);
				req.setRequestHeader("If-Modified-Since", new Date().getTime());
				req.send();
			}
		}
	}
}
function processStateChange() {
	if (req.readyState == 4) { // Complete
		if (req.status == 200) { // OK response
      alert("in process received" +req.statusText);
    alert(req.responseText);
			document.getElementById("accountDiv").innerHTML = req.responseText;
			if((req.responseText)!=""){
			alert("not null");
			AddOptionsTally();
			accountRow.style.display="block";
			}else{
			accountRow.style.display="none";
			}
			document.getElementById("acctDiv").innerHTML = "";
			document.getElementById("frmdate").innerHTML = "";
			document.getElementById("todate1").innerHTML = "";
			
		} else {
			alert("Problem: " + req.statusText);
		}
	}
}

function AddOptionsTally()
{
	var ajaxList = document.getElementById('tallyAccountSel');
 	var newSelectbox = document.getElementById('tallyAccount');
	newSelectbox.options.length=1;
		for(i=0,l=ajaxList.options.length;i<l;i++) {
			if(i!=0){
				var obj = ajaxList[i];
				var opt = new Option(obj.text,obj.value);
				newSelectbox.options[i] = opt;
			}
		}
}

function validate() {  	
alert("In validate 1");
	var isInValid = false;
	for (var i = 0; i < document.forms[0].elements.length; i++) {
	
		var e = document.forms[0].elements[i];
		//alert("In For");
		if (e.name == "tallyAccount") {
		alert(e.name+" 1 "+e.value +" ## "+e.id);
			if (e.value == "-1" && accountRow.style.display!="none") {
		//	alert(document.getElementById("acctDiv")+"  1");
				document.getElementById("acctDiv").innerHTML = "<font color = 'red'>Please Select Account222 Type</font>";
				isInValid = true;
			} else {
				document.getElementById("acctDiv").innerHTML = "";
			}
		}
		if (e.name == "xmlType") {
		//alert(e.name+"  2 "+e.value+"---"+e.checked);
			if (e.value == "All Accounts") {
		//	alert(document.getElementById("acctDiv")+"  2");
				document.getElementById("acctDiv").innerHTML = "";
				//document.getElementById("acctDiv").innerHTML = "";
				
				
			} 
		}
		
			if (e.name == "compName") {
		//alert(e.name+"  2 "+e.value);
			if (e.value == "") {
		 	//alert("  2");
				document.getElementById("compDiv").innerHTML = "<font color = 'red'>Please Enter Company Name</font>";
				isInValid = true;
			}else {
				document.getElementById("compDiv").innerHTML = "";
			} 
		}
		
		
		
		
	}
	//alert("jklljkljlkj");
	var fromDt = document.getElementById("fromDate");
	//alert(fromDt);
	var fromDtValue = fromDt.value;
	//alert(fromDtValue);
	if (fromDtValue == "") {
		document.getElementById("frmdate").innerHTML = "<font color = 'red'>Please Enter From Date</font>";
		//alert("in from");
		isInValid = true;
	} else {
		document.getElementById("frmdate").innerHTML = "";
	}
	//alert(document.getElementById("toDate"));
	var to = document.getElementById("toDate");
	var toDtValue = to.value;
  //alert(toDtValue);
	if (toDtValue != "") {
		if (fromDtValue > toDtValue) {
		//alert("toooo");
			document.getElementById("todate1").innerHTML = "<font color = 'red'>Please Select To date Greater than From Date</font>";
  //alert("Please Select to date Greater than From Date");
			isInValid = true;
		} else {
			document.getElementById("todate1").innerHTML = "";
		}
	}
 // alert("isInValid :"+isInValid);
	if (isInValid) {
	//alert(isInValid);
		return false;
	}
	return true;
}
function onload1() {


	 //alert("gdfg");
	//if (document.tallyXML.type.value == "bo") {
	//alert("2222 boXMLAjax.action?xmlType=Accountwise");
		retrieveURL("bo_rep_genTallyXML_FetchAccList.action?xmlType=Accountwise", "accountDiv");
	///} else {
		//retrieveURL("agentXMLAjax.action?xmlType=Accountwise", "accountDiv");
	//}
	
}



		
 
 

 
 	parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/tallyXML.js,v $'] = '$Id: tallyXML.js,v 1.1 2010/04/01 04:23:08 gaurav Exp $';	
