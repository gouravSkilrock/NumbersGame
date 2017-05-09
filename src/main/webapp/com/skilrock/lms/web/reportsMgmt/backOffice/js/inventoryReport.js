function validateGameNumber() {
	var num=document.getElementById("gameNumber").value;
	if(isNaN(num)){			
		document.getElementById("gameNumber1").innerHTML = "<font color=red>Please enter a numeric value</font>";
		return false;
		}else {
		
		if(num.indexOf(".")!=-1){
		document.getElementById("gameNumber1").innerHTML = "<font color=red>Please enter a numeric value</font>";
		return false;
		}else{
		document.getElementById("gameNumber1").innerHTML = "";
		return true;}
		}
}


function setDivBlankOnload() {
	document.getElementById('down2').innerHTML="";
	document.getElementById('down3').innerHTML="";
} 


function getBOBooksDetailAgentWise() {
	var agtOrgName=document.getElementById('agent_org_name').value;
	//alert(agtOrgName);
	agtOrgName=convert(agtOrgName);
	//alert(agtOrgName);
	var gameId=document.getElementById('game_id').value;
	//alert(gameId);
	var url ="boTotalBooksWithAgent.action?gameid="+gameId+"& agentName="+agtOrgName+"& type=link";
	executeAjax(url, 'down3');
}


function convert(urlX){
		var exp= /&/g ;
		return urlX.replace(exp,"amp");
	
}

var mainDiv;
var req;
var which;

function executeAjax(newUrl, div) {		
	
	mainDiv = div;
	//alert(newUrl +"      div == "+mainDiv);
	   _resp  = _ajaxCall(newUrl);
	if(_resp.result){
     
        var resStr= _resp.data; 
    	var first=resStr.split(")");
    	alert("length of first ==="+first.length);
    	for(var k=0; first.length; k++) {
    		var second=first[k].split(";");
    		var agent=second[0];
    		var books=second[1].split(",");
    		
    		for(var l=0; books.length; l++) {
    			var book=books[l];
    		
    		}
    	}
      } else {
        alert("Problem: ");
        }
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/inventoryReport.js,v $'] = '$Id: inventoryReport.js,v 1.1 2010/04/01 04:23:08 gaurav Exp $';