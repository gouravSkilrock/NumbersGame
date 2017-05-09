function _commonAjaxFunction(passedUrl) {
	var MAXIMUM_WAITING_TIME = 3 * 60 * 1000;
	var xhReq = new createXMLHttpRequest();
				var _c="?";
				if(passedUrl.indexOf("?")!=-1){_c="&";}
				passedUrl=passedUrl+_c+new Date().getTime()+parent.frames[0].sesId+Math.random()*1000000;
	
	xhReq.open("GET", passedUrl, false);
	var requestTimer = setTimeout(function () {
			xhReq.abort();
			_commonAjaxFunction(passedUrl);
		}, MAXIMUM_WAITING_TIME);
		
	var serverResponse;
	xhReq.onreadystatechange = function () {
			if (xhReq.readyState != 4 || xhReq.status != 200) { return;	}
			clearTimeout(requestTimer);			
			serverResponse = xhReq.responseText;
			return;
	};
	
	xhReq.setRequestHeader("If-Modified-Since", new Date().getTime());
	xhReq.send(null);
	
	 //alert(serverResponse); 
	return serverResponse;
}

function createXMLHttpRequest() {
	try {	return new XMLHttpRequest();	}	catch (e) {	}
	try {	return new ActiveXObject("Msxml2.XMLHTTP");	}	catch (e) {	}
	try {	return new ActiveXObject("Microsoft.XMLHTTP");	}	catch (e) {	}
	alert("XMLHttpRequest not supported");
	return null;
}


function getParent_OrgList(url,divId,tierLevel){
    var url = "fetchOrgListForService.action";
    var resData = _commonAjaxFunction(url);
    alert(resData);
    var resArr = resData.split("=");

   var parent_typeList = resArr[0].split(":");
   var parent_Type;     // contains parenttype of selected tier 
   var flag = true;
   for(var i=0; i<parent_typeList.length ;i++){
        
         if(!flag){
             break;											// this loop gets us what is parent_Type of selected Tier Level
          }   
         var tiercomp = parent_typeList[i].split("-");
         var tiertype = tiercomp[0];
         var tierparent = tiercomp[1];
         if (tiertype == tierLevel){
             parent_Type=tierparent;
             flag =false;
         
         }
            
   }
   
   
   
   
   
   

}