var i18nObj = parent.frames[0].i18Obj;
function checkDenyPwtStatus() {
		var fields = document.getElementsByName('denyPwtStatus');
		//alert(" length = "+fields[0].checked +""+fields[1].checked) ;
		
		if(fields[0].checked || fields[1].checked) {
			return true;
		}
		else {
			alert(i18nObj.prop('error.select.atlease.one.opt'));
			return false;
		}
}


function showDenyFields() {
		var value = confirm(i18nObj.prop('error.u.sure.to.deny.pwt.proc'));
		if(value) {
			var Fields = document.getElementById('denyfields');			
			Fields.style.display = "block";
			Fields.focus();
		}
		return false;
}    

			  	
	  
	  	//get the agent list from database
	  	function retURL(url, targetDiv, orgType) {
	  		document.getElementById("retList").style.display="none";
	  		document.getElementById("retDiv").style.display="none";
	  		document.getElementById("ret_org_name").selectedIndex=0;
	  		document.getElementById("agent_org_name").selectedIndex=0;
			
			//_id.i("down", "");	
			//alert(orgType);   			  		
	  		if(orgType=='AGENT' || orgType=='RETAILER') {
	  			getAgentList();	
	  			document.getElementById(targetDiv).style.display="block";
	  			document.getElementById('agentDiv').style.display="block";
			 }
			 else {
					document.getElementById(targetDiv).style.display="none";
			  		document.getElementById('agentDiv').style.display="none";
				 
			 }			 
	  	}
	  	function agtURL(url, targetDiv, orgType) {
	  	
	  		document.getElementById("agent_org_name").selectedIndex=0;
			
			//_id.i("down", "");	
			//alert(orgType);   			  		
	  		if(orgType=='AGENT' ) {
	  			getAgentList();	
	  			document.getElementById(targetDiv).style.display="block";
	  			document.getElementById('agentDiv').style.display="block";
			 }
			 else {
					document.getElementById(targetDiv).style.display="none";
			  		document.getElementById('agentDiv').style.display="none";
				 
			 }			 
	  	}
	  	function getRetailerList(url, targetDiv, agentOrgId) {	  		
	  		var changedUrl = url+'?agentOrgId='+agentOrgId;
	  		var orgType = _id.o('Org_type').value;
	  		//alert(changedUrl +"    Org_type = "+orgType);
	  		if(orgType!='RETAILER') {
	  			_id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 
			 	 return false;
	  		}
	  		//alert("retailer ");
	  		if(agentOrgId!='') {
				var ajaxCall = _ajaxCall(changedUrl);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("retDiv",tierMap['RETAILER']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 	 
			 }
	  	}
	  	
	  	function getRetailerListForAgent(url, targetDiv, orgType) {
	  		  //alert();	
	  		if(orgType=='RETAILER') {
				var ajaxCall = _ajaxCall(url);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("retDiv",tierMap['RETAILER']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 	 
			 }
	  	}

    function getAgentsList(url, targetDiv, orgType){			
			//alert(orgType);   			  		
	  		if(orgType=='AGENT') {
				var ajaxCall = _ajaxCall(url);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("agentDiv",tierMap['AGENT']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("agentDiv", "");				 	 	 
			 }		
    }



    function validateFormData(){	 
		 
		 var frmVal  = new parent.frames[0].Validator("playerDetails");
		 
		  var radButton = _id.n('paymentType');
			for(var a=0; a<radButton.length; a++){
				 	if(radButton[a].value !='cheque' && radButton[a].checked){
			 		 return true;
			 	}
			}
			  
			  frmVal.aV("chqNbr","req",i18nObj.prop('error.enter.chq.nbr'),"chequeNbrError");	  
		  	  frmVal.aV("chqNbr","regexp=[^0-9]",i18nObj.prop('error.enter.valid.chq.nbr'),"chequeNbrError");
		  	  
		  	   frmVal.aV("draweeBank","req",i18nObj.prop('error.enter.drawee.bnk'),"draweeBankError");	  
		  	   frmVal.aV("draweeBank","regexp=[^A-Za-z\\s/,.]",i18nObj.prop('error.enter.correct.drawee.bnk'),"draweeBankError");
		  	  
		  	  
			  return document.error_disp_handler.isValid;
			   
	 }
	 
	 
	 
	 function calculateNetAmt(){	 
		 var frmVal  = new parent.frames[0].Validator("playerDetails");
		  var taxAmount=_id.n('taxAmt')[0].value;
		  var pwtAmount=_id.n('pwtAmount')[0].value;
		  //alert("dd" + taxAmount);
		  
		  frmVal.aV("taxAmt","req",i18nObj.prop('error.enter.tax.val'),"taxError");
		  if(!document.error_disp_handler.isValid){
		     _id.i('taxError',i18nObj.prop('error.enter.correct.tax.val'));
		    	  return false;
		  }		     
		     
		  if(isNaN(taxAmount)){
		         _id.i('taxError',i18nObj.prop('error.enter.correct.tax.val'));
		    	  return false;
		   }else if(parseFloat(taxAmount) >= parseFloat(pwtAmount)){
		           _id.i('taxError',i18nObj.prop('error.tax.shouldnt.gt.win.amt'));
		    		return false;
		   }else if(parseFloat(taxAmount) < 0){
		      		_id.i('taxError',i18nObj.prop('error.tax.cannt.be.neg.val'));
		    		return false;
		   }
		  _id.i('taxError','');	 	 
		  var netAmount= parseFloat(pwtAmount) - parseFloat(taxAmount);
		  _id.o('netAmt').value=netAmount;
		  
		  if(validateFormData())
		  	  return true;
		  else
		     return false;	  
	 }
	

	
	   function changeSubmitButton(gameType){
    alert(gameType);

     if(gameType=="Draw")
     {        
        document.getElementById('scratchSubmit').style.display="none";
        document.getElementById('drawSubmit').style.display="block";
     }else
     {
       document.getElementById('scratchSubmit').style.display="block";
        document.getElementById('drawSubmit').style.display="none";
     }
  
    }
   function getAgentList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agent_org_name");

			for ( var i = 0; i < firstArr.length - 1; i=i+1) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
   
   function onAgentChangeValue(agentValue) {
		var value =document.getElementById('agent_org_name').value;
		var type=document.getElementById('Org_type').value;

		if(type=='AGENT') {
			if(value=='-1') {		
				document.getElementById('agent_error').innerHTML=i18.prop('error.pls.slct.an')+" "+tierMap['AGENT'].toUpperCase()+" "+i18nObj.prop('error.cmpny');		
				return false;
			}else return true;
		}else if(agentValue!='form' && type=='RETAILER'){
			//alert("inside else retailer");
			fetchRetOrgList(agentValue)	;
			
		}else {
			//alert("form submit");
			return true;
		} 
		}
   
   function fetchRetOrgList(orgId) {
		var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId="+orgId);
		var firstArr = text.data.split(":");
		var retObj = _id.o("ret_org_name");

		for ( var i = 0; i < firstArr.length - 1; i=i+1) {
			var retNameArray = firstArr[i].split("|");
			var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			retObj.options[i + 1] = opt;
		}
		document.getElementById('retDiv').style.display="block";
		document.getElementById('retList').style.display="block";
	}

	
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/pwtBO.js,v $'] = '$Id: pwtBO.js,v 1.3 2016/10/31 09:46:58 neeraj Exp $';