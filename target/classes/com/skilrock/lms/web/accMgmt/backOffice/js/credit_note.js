var i18nObj = parent.frames[0].i18Obj;

function verifyAgent(agtName) {
	var e = document.getElementById("agentName");
	var agtDisplayName = e.options[e.selectedIndex].text;
	//alert(agtDisplayName+"asa");
	document.getElementById("agentNameValue").value =agtDisplayName;
	// pass selected value to display on Details Page	
	 fetchLimits(agtName,agtDisplayName);
  if(document.getElementById('agentName').value!="-1")
  {
  var returnType=confirm(i18nObj.prop('error.u.hav.slect')+tierMap['AGENT']+":: " + agtDisplayName)
  if(returnType)    
    {     
    document.getElementById('amount').focus();
    // return true;
    }
   else
   {
       document.getElementById('agentName').selectedIndex=0;
       document.getElementById('agentName').focus();
       //return false;
   }
  }  
}

function validateData()
{
	var e = document.getElementById("retOrgName");
	var retDisplayName = e.options[e.selectedIndex].text;
	document.getElementById("retNameValue").value =retDisplayName;
  var flag = true;
  
  var amount= document.getElementById('amount').value;
  var agentName= document.getElementById('orgName').value;
  var remarks= document.getElementById('remarks').value;
  var retName= document.getElementById('retOrgName').value;

  if(agentName== '-1')
  {
      
	  document.getElementById('orgName1').innerHTML = '<font color=red>'+i18nObj.prop('error.agent.empty')+" "+ i18nObj.prop('msg.name')+'</font>';
	  flag=false;
 }
  else
  {
    document.getElementById('orgName1').innerHTML = "";  
   }
  if($("#orgType").val()=='RETAILER'){
	  if(retName=='-1'){
	    document.getElementById('retOrgName1').innerHTML = '<font color=red>'+i18nObj.prop('error.retailer.empty')+" "+i18nObj.prop('msg.name')+'</font>';
	    flag=false; 
      }else{
	    document.getElementById('retOrgName1').innerHTML = "";
      }
  }
   if(isDecimal1(amount) && amount!=".")
  {
      if (parseFloat(amount,10) == "0")
       {
				document.getElementById("amount1").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.zero.val.not.allow')+"</font>";
			    flag = false;  
      }
      
      else{
        var amountArr = amount.split('.');
      	if(amountArr.length > 1){
      		if(amountArr[1].length > 2){
      			document.getElementById("amount1").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.amt.upto.two.decimal.point.allow')+"</font>";
			    flag = false;  
      		} else {
      			document.getElementById("amount1").innerHTML = "";
      		}
      	} else {
      		document.getElementById("amount1").innerHTML = "";
      	}
      }
   }   
  else
     {
       document.getElementById('amount1').innerHTML = '<font color=red>'+i18nObj.prop('error.only.dcml.value.req')+'</font>';
        flag = false;
      }
  
   if(remarks=="")
   {
    document.getElementById('remarks1').innerHTML = '<font color=red>'+i18nObj.prop('error.enter.remark')+'</font>';
    flag = false;
   }
   else if(remarks.replace(/ /g,'').length == 0)
   {
       document.getElementById('remarks1').innerHTML = '<font color=red>'+i18nObj.prop('error.enter.some.meaningful.remark')+'</font>';
        flag = false;
   }
   else if(remarks.length > 99)
   {
       document.getElementById('remarks1').innerHTML = '<font color=red>'+i18nObj.prop('error.max.hund.chars.allow.remark')+'</font>';
        flag = false;
   }
  
  else
   {
     document.getElementById('remarks1').innerHTML = "";
   }
  
 	if(flag) {
	 	var isTrue = convertDigitToWord(amount);
	 	if(!isTrue) {
	 		flag = false;
	 		document.getElementById('amount').focus();
	 	}
 	}
 	
 	if('AGAINST_LOOSE_BOOKS_RETURN' == _id.v('reason')){
 		if('-1' == _id.v('game_id')){
 			flag = false;
 		}
 	}
 	
 	
 
    return flag;
  
  
}

function isDecimal1(sText1) {
	var sText = sText1;
	var validChars = "0123456789.";
	var isNumber = true;
	var myChar;
	var count = 0;
	if (sText.length == 0) {
		isNumber = false;
	}
	for (i = 0; i < sText.length; i++) {
		myChar = sText.charAt(i);
		if (myChar == ".") {
			count++;
		}
		if (validChars.indexOf(myChar) == -1 || count > 1) {
			isNumber = false;
			break;
		}
	}
	return isNumber;
}

function showGameList(val){
	//alert('*****'+val+'******');
	if('AGAINST_LOOSE_BOOKS_RETURN' == val){
		_id.blk('gameListDiv1');
		_id.blk('gameListDiv2');
	} else {
		_id.non('gameListDiv1');
		_id.non('gameListDiv2');
	}
}

function gameDetails() {
	//alert('sdfasdfasd');
	getAgentList();
	var url = path+"/com/skilrock/lms/web/scratchService/inventoryMgmt/bo_im_fetchGameDetails_ajax.action";
	var _resp = _ajaxCall(url);
	//alert(_resp.data)
	if(_resp.result && _resp.data!=""){
		
			var _respData = _resp.data;
			var option = _id.o("game_id");
			option.options[0] = new Option("---Please Select---", -1);	
			
		var gameArr = _respData.split("Nx*");
		//alert("gameArr = "+gameArr.length+" data = "+gameArr);
		var gameWiseMap;
		for(var i=0; i<gameArr.length; i++) {
			var gameDetailsArr = gameArr[i].split(":");
			var gkey = gameDetailsArr[0];//+"-"+gameDetailsArr[1]+"-"+gameDetailsArr[2];						
			var dvalue = gameDetailsArr[1]+"-"+gameDetailsArr[2];
			var opt = new Option(dvalue, gkey);
			option.options[i+1] = opt;						
		}
		//alert(gameDetMap);
		//_id.o('game_id')=option;
	}		
}

function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agentName");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function resetData(){

	if($("#orgType").val()=='RETAILER'){
		$("#orgType").val("RETAILER");
	}else{
		$("#orgType").val("AGENT");
		$("#retName").hide();
	}
	document.getElementById("orgName1").innerHTML="";
	document.getElementById("amount1").innerHTML="";
	document.getElementById("amount").value="";
	document.getElementById("orgName").value=-1;
	document.getElementById("retOrgName").value=-1;
	document.getElementById("retOrgName1").innerHTML="";
	document.getElementById("remarks1").innerHTML="";
	document.getElementById("remarks").value="";
	if(document.getElementById("limitDiv"))
	document.getElementById("limitDiv").innerHTML="";
	return false;
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/credit_note.js,v $'] = '$Id: credit_note.js,v 1.3 2016/10/31 09:46:21 neeraj Exp $';