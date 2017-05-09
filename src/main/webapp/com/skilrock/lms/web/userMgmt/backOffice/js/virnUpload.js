
function virnValidityCheck() {
   //alert("heeeeeeeellllooooo");
    document.getElementById('loadingDiv').style.display="block";
    var gameName = document.uploadVirn.gameName.value;
	var filePath = document.getElementById("file").value;
   //alert(gameName);
   //alert(filePath);
	filevaliditycheck(gameName, filePath);
}
function validateDates() {

	var isInValid = false;
     //alert("hi");
	
	var startDate = document.getElementById("startDate").value;
	var saleEndDate = document.getElementById("saleendDate").value;
	var pwtEndDate = document.getElementById("pwtendDate").value;
      // alert(startDate);
	if (document.getElementById("game_Name").value == "Please Select") {
		isInValid = true;
		document.getElementById("gamename1").innerHTML = "<font color = 'red'>Please Select Game Name</font>";
         //alert('please select game name');
	} else {
		document.getElementById("gamename1").innerHTML = "";
	}
	if (startDate == "" || saleEndDate == "" || pwtEndDate == "") {
		isInValid = true;
		document.getElementById("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";
       // alert("please enter dates");
       // return false;
	} else {
		if (saleEndDate <= startDate) {
			isInValid = true;
			document.getElementById("dates").innerHTML = "<font color = 'red'>sale end date should be greater then start date</font>";
          
          //alert("sale end date should be greater then start date");
		} else {
			document.getElementById("dates").innerHTML = "";
			
			if (saleEndDate >= pwtEndDate) {
				isInValid = true;
				document.getElementById("dates").innerHTML = "<font color = 'red'>pwt end date should be greater then sale end date</font>";
          
         // alert("pwt end date should be greater then sale end date");
			} else {
			
				document.getElementById("dates").innerHTML = "";
			}
		}
	}
	if (document.getElementById("file").value == "") {
		isInValid = true;
		document.getElementById("file1").innerHTML = "<font color = 'red'>Please Select the Virn file</font>";
         //alert('please select game name');
	} else {
		document.getElementById("file1").innerHTML = "";
	}	
	if (isInValid) {
		return false;
	}
	return true;
}
function gameDatesfromDB() {
    //alert("hiii");
   // var gameType=document.getElementByName("gameType");
	var gameName = document.uploadVirn.gameName.value;
    //alert(gameName);
	var val = 0;
	for (i = 0; i < document.uploadVirn.gameType.length; i++) {
		if (document.uploadVirn.gameType[i].checked == true) {
			val = document.uploadVirn.gameType[i].value;
		}
	}
	
	if (val == "OPEN") {
  		retrieveDate(val, gameName);
	} else {
     // alert("inside else");
		var x = document.getElementById("r").value;
      //alert(x);
		document.getElementById("startDate").value = x;
		document.getElementById("saleendDate").value = x;
		document.getElementById("pwtendDate").value = x;
		
		document.getElementById("startDateDiv").innerHTML="";
		document.getElementById("saleendDateDiv").innerHTML="";
		document.getElementById("endDateDiv").innerHTML="";
		
	}
}

var req;
function retrieveDate(gametype, gamename) {

	gameType = gametype;
	var url = "gameDates.action?gameType=" + gametype + "&gameName=" + gamename;
   _resp  = _ajaxCall(url);
	if(_resp.result){
      			var str = _resp.data;
   
			var array = new Array();
			array = str.split("*");
			var startDate1 = array[0];
			var saleEndDate1 = array[1];
			var petEnddate1 = array[2];
			document.getElementById("startDate").value = (startDate1);
			document.getElementById("saleendDate").value = (saleEndDate1);
			document.getElementById("pwtendDate").value = (petEnddate1);
			
			document.getElementById("startDateHid").value=startDate1;
		document.getElementById("saleendDateHid").value=saleEndDate1;
		document.getElementById("endDateHid").value=petEnddate1;
		
		var a=document.getElementById("startDateHid").value;
				var b=document.getElementById("saleendDateHid").value;
				var c=document.getElementById("endDateHid").value;				
				document.getElementById("startDateDiv").innerHTML="<input type=\"hidden\" value=\""+a+"\" name=\"startDate\" />";
		document.getElementById("saleendDateDiv").innerHTML="<input type=\"hidden\" value=\""+b+"\" name=\"saleendDate\" />";
		document.getElementById("endDateDiv").innerHTML="<input type=\"hidden\" value=\""+c+"\" name=\"pwtendDate\" />";
	
      
      
      
      } else {
        alert("Problem: ");
        }
	return false;
}

function filevaliditycheck(gamename, filepath) {
	var url = "fileValidityCheck.action?gameName=" + gamename + "&filePath=" + filepath;
    _resp  = _ajaxCall(url);
	if(_resp.result){
      			var res = "" +_resp.data;

			if (res.match("false")) {
   				document.getElementById('loadingDiv').style.display="none";
				document.getElementById("file1").innerHTML = "<font color = 'red'>This file has been uploaded previously</font>";
				document.getElementById('virnId').disabled=true;
				
			} else {
    
				document.getElementById('loadingDiv').style.display="none";
				document.getElementById("file1").innerHTML = "";
				document.getElementById('virnId').disabled=false;
  			}
      
      } else {
        alert("Problem: ");
        }	
   return false;
}

function dateToDisplay() {
	//alert("333333");
	//alert(document.getElementById("gtype").value);
	if (document.getElementById("gtype").value == "OPEN") {
		var name = document.getElementsByName("gameStartDate")[0];
		name.disabled = (name.disabled == true) ? false : true;
	}
}

var req;
var mainDiv;
function retrieveURL(url, div) {
	var which;
	mainDiv = div;
	   _resp  = _ajaxCall(url);
	if(_resp.result){
      		document.getElementById(mainDiv).innerHTML = _resp.data;
			var x = document.getElementsByName("gameType");
			for (var i = 0; i < document.forms[0].elements.length; i++) {
				var e = document.forms[0].elements[i];
				if (e.name == "gameType") {
					if (e.checked == true) {
						gameTypeVal = e.value;
						break;
					}
				}
			}
      } else {
        alert("Problem: ");
        }
}
function disableDates(val) {
	if (val == "OPEN") {		
		document.getElementById("startDateButton").disabled =true;
		document.getElementById("saleendDateButton").disabled =true;
		document.getElementById("pwtendDateButton").disabled =true;
		//alert("jfhsj");
	} else {
		document.getElementById("startDateButton").disabled =false;
		document.getElementById("saleendDateButton").disabled =false;
		document.getElementById("pwtendDateButton").disabled =false;
		
		document.getElementById("startDateDiv").innerHTML="";
		document.getElementById("saleendDateDiv").innerHTML="";
		document.getElementById("endDateDiv").innerHTML="";
	}
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/virnUpload.js,v $'] = '$Id: virnUpload.js,v 1.1 2010/04/01 04:22:20 gaurav Exp $';