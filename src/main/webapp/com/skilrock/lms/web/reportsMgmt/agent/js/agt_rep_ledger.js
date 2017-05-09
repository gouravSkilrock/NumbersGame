
var mainDiv;
var req;
var which;
function retrieveURL(url, div) {
for (var i=0;i < document.getElementsByName("ledgerType").length;i++)
	{
	var e=document.getElementsByName("ledgerType")[i];
	if(e.checked){
		if(e.value=="consolidated"){
		document.getElementById("GenerateLedger").style.display="none";
		document.getElementById("GenerateLedgerNew").style.display="block";
		}else{
		document.getElementById("GenerateLedger").style.display="block";
		document.getElementById("GenerateLedgerNew").style.display="none";
		}
	
	}

}
	mainDiv = div;
	if (document.ledger.Agt.value == "2") {
		url = url + "&agentName =" + encodeURIComponent(document.ledger.agentName.value);
		//alert(url);
	}
	if (document.ledger.Agt.value == "1") {
		document.ledger.Agt.value = "2";
	}
	if (document.ledger.AgtLedger.value == "yes") {
		if (document.ledger.Agt.value == "0") {
			document.ledger.Agt.value = "1";
		}
	}
   _resp  = _ajaxCall(url);
	if(_resp.result){
      		document.getElementById(mainDiv).innerHTML = _resp.data;
			document.getElementById("accountDiv").innerHTML = "";
			document.getElementById("frmdate").innerHTML = "";
			document.getElementById("todate1").innerHTML = "";
			if (document.ledger.AgtLedger.value == "yes") {
				if (document.ledger.Agt.value == "1") {
					retrieveURL("agt_rep_ledger_FetchAccList.action?ledgerType=Agentwise", "list1");
				}
			}
      } else {
        alert("Problem: ");
        }
}



function validate() {  	
  	//alert("In validate");
	var isInValid = false;
	for (var i = 0; i < document.forms[0].elements.length; i++) {
	//alert("In For");
		var e = document.forms[0].elements[i];
		if (e.name == "accountType") {//alert("In accountType");
			if (e.value == "-1") {
				document.getElementById("accountDiv").innerHTML = "<font color = 'red'>Please Select Account Type</font>";
				isInValid = true;
			} else {
				document.getElementById("accountDiv").innerHTML = "";
			}
		}
		if (e.name == "agentName") {//alert("In agentName");
			if (e.value == "-1") {
				document.getElementById("accountDiv").innerHTML = "<font color = 'red'>Please Select "+tierMap['AGENT']+"</font>";
				isInValid = true;
			} else {
				document.getElementById("accountDiv").innerHTML = "";
			}
		}
		if (e.name == "retailerName") {
		//alert("In retailerName");
			if (e.value == "-1") {
				document.getElementById("accountDiv").innerHTML = "<font color = 'red'>Please Select "+tierMap['RETAILER']+"</font>";
				isInValid = true;
			} else {
				document.getElementById("accountDiv").innerHTML = "";
			}
		}
	}
	var fromDt = document.getElementById("fromDate");
	var fromDtValue = fromDt.value;
	if (fromDtValue == "") {
		document.getElementById("frmdate").innerHTML = "<font color = 'red'>Please Enter From Date</font>";
		isInValid = true;
	} else {
		document.getElementById("frmdate").innerHTML = "";
	}
	var to = document.getElementById("toDate");
	var toDtValue = to.value;
  //alert(toDtValue);
	if (toDtValue != "") {
		
			var toDtArr = toDtValue.split("-");
			var fromDtArr = fromDtValue.split("-");
			//alert((fromDtArr[2]+fromDtArr[1]+fromDtArr[0])+"   "+(toDtArr[2]+toDtArr[1]+toDtArr[0]))
		if ((fromDtArr[2]+fromDtArr[1]+fromDtArr[0]) >(toDtArr[2]+toDtArr[1]+toDtArr[0])) {
			document.getElementById("todate1").innerHTML = "<font color = 'red'>Please Select To date Greater than From Date</font>";
  //alert("Please Select to date Greater than From Date");
			isInValid = true;
		} else {
			document.getElementById("todate1").innerHTML = "";
		}
	}
 // alert("isInValid :"+isInValid);
	if (isInValid) {
		return false;
	}
	return true;
}

function showDayWise(){
	//alert("showDayWise()");
	document.getElementById("conDet").style.display="none";
	document.getElementById("conDayWise").style.display="block";
	document.getElementById("conAllDet").style.display="none";
}

function showAllDet(){
document.getElementById("conDet").style.display="none";
document.getElementById("conDayWise").style.display="none";
document.getElementById("conAllDet").style.display="block";
}
function onload() {

	document.ledger.Agt.value = "0";
	if (document.ledger.type.value == "agent") {
		retrieveURL("agt_rep_ledger_FetchAccList.action?ledgerType=Retailerwise", "list");
	} else {
		retrieveURL("agt_rep_ledger_FetchAccList.action?ledgerType=Retailerwise", "list");
	}
	
}
function changeValue() {
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		var e = document.forms[0].elements[i];
		if (e.name == "ledgerType") {
			e.checked = true;
			retrieveURL("agt_rep_ledger_FetchAccList.action?ledgerType=Retailerwise", "list");
			break;
		}
	}
}

function readScrp1(){
	var valid = validate();
	if(!valid){
	return false;
	}
	var isInstalled = false;
	var version = null;
	if (window.ActiveXObject) {
    var control = null;
    try {
        // AcroPDF.PDF is used by version 7 and later
        control = new ActiveXObject('AcroPDF.PDF');
    } catch (e) {
        
    }
    if (!control) {
        try {
            // PDF.PdfCtrl is used by version 6 and earlier
            control = new ActiveXObject('PDF.PdfCtrl');
        } catch (e) { 
        //alert("Install PDF2");
         document.getElementById("errorId").innerHTML='Your Registry was read and you do not have the Adobe Acrobat   installed. click to install :  <a href="javascript:poptastic();"> click </a> ' ;
            return false;
        }
    }
    if (control) {
        isInstalled = true;
        version = control.GetVersions().split(',');
        version = version[0].split('=');
        version = parseFloat(version[1]);
       // alert(version);
    }
} 

//else {
    // Check navigator.plugins for "Adobe Acrobat" or "Adobe PDF Plug-in"*
    // alert("Check navigator.plugins for Adobe Acroba or Adobe PDF Plug-in");
   //  document.getElementById("errorId").innerHTML="Check navigator.plugins for Adobe Acroba or Adobe PDF Plug-in: " ;
   //  return false;
//}

	return true;
		}
		
 	function readScrp(){
 

 var a = document.all['A'];
 //document.ge    ('<object id="A" classid="CLSID:CA8A9780-280D-11CF-A24D-444553540000"></object>');
 document.getElementById("objectAdobe").innerHTML="<object id='A' classid='CLSID:CA8A9780-280D-11CF-A24D-444553540000'></object>";
 
 if (a && a.readyState != 0) {
 
 alert('Your Registry was read and you have the Adobe Acrobat ActiveX control installed.:');
 return true;
 
 
 } else {
 document.getElementById("errorId").innerHTML='<font color="red">Your Registry was read and you do not have the Adobe Acrobat   installed. click to install :  <a href="javascript:poptastic()"> click </a> </font>' ;
 //alert('Your Registry was read and you do not have the Adobe Acrobat   installed. click to install :");
 //http://www.rgagnon.com/javadetails/java-0426.html
 return false;
 }
 
 
 
 
 }
 
function poptastic()
{  //alert("jdjj");
var newwindow;
	newwindow=window.open('http://www.adobe.com/devnet/acrobat','name','height=400,width=400' );
	if (window.focus) {newwindow.focus()}
}
 
 		
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/agent/js/agt_rep_ledger.js,v $'] = '$Id: agt_rep_ledger.js,v 1.2 2016/10/31 09:46:53 neeraj Exp $';