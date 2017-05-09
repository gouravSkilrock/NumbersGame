var i18nObj = parent.frames[0].i18Obj;
function verifyAgent(agtName,agtOrgId,focusId) {
 
  if(document.getElementById(agtOrgId).value!="-1")
  {
  var returnType=confirm(parent.frames[0].i18Obj.prop('error.u.hav.slect') +' '+ tierMap['AGENT']+":: " + agtName)
  if(returnType)    
    {     
    document.getElementById(focusId).focus();
    // return true;
    }
   else
   {
       document.getElementById(agtOrgId).selectedIndex=0;
       document.getElementById(agtOrgId).focus();
       //return false;
   }
  }  
}


function verifyRetailer(retName,retOrgId,focusId) {
	$("#limitDiv").html("");
	var e = document.getElementById("orgName");
	var orgDisplayName = e.options[e.selectedIndex].text;
	//alert(agtDisplayName+"asa");
	document.getElementById("orgNameValue").value =orgDisplayName;
	//alert(document.getElementById("agentNameValue").value+"121as1a");
// pass selected value to display on Details Page	
 fetchLimits(retName,orgDisplayName);
  if(retName !="-1")
  {
  var returnType=confirm(parent.frames[0].i18Obj.prop('error.u.hav.slect') +' '+tierMap['RETAILER']+":: " + orgDisplayName);
  if(returnType)    
    {     
    document.getElementById(focusId).focus();
    // return true;
    }
   else
   {
       document.getElementById(retOrgId).selectedIndex=0;
       document.getElementById(retOrgId).focus();
       //return false;
   }
  }  
}

function verifyRetailerWithOutstandingBalance(retName,retOrgId,focusId){
	var e = document.getElementById("orgName");
	var orgDisplayName = e.options[e.selectedIndex].text;
	//alert(agtDisplayName+"asa");
	document.getElementById("orgNameValue").value =orgDisplayName;
	//alert(document.getElementById("agentNameValue").value+"121as1a");
// pass selected value to display on Details Page	
 fetchLimitsOfRetailer(retName,orgDisplayName);
  if(retName !="-1")
  {
  var returnType=confirm(parent.frames[0].i18Obj.prop('error.u.hav.slect') +' '+tierMap['RETAILER']+":: " + orgDisplayName);
  if(returnType)    
    {  
	  document.getElementById('orgName').value=e.options[e.selectedIndex].value;
    document.getElementById(focusId).focus();
    // return true;
    }
   else
   {
       document.getElementById(retOrgId).selectedIndex=0;
       document.getElementById(retOrgId).focus();
       //return false;
   }
  }  
}

function getRetailerUserList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=RETAILER");
	var firstArr = text.data.split(":");
	var retObj = _id.o("orgName");
	for (var i = 0; i < firstArr.length - 1; i++) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		
		retObj.options[i + 1] = opt;
	}
	
	document.getElementById("retlistDiv").style.display="block";	
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/common/globalJs/confirmAgent.js,v $'] = '$Id: confirmAgent.js,v 1.3 2016/10/31 09:46:50 neeraj Exp $';