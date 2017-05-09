function validateFormEntries() {
	
	var serviceNameObj =_id.o('serviceName');

	if(serviceNameObj.value=='-1'){
			_id.i("error1", "Please Select Service Name .", "e");	
			return false;
	}
	 if(serviceNameObj.value != 'DG' && serviceNameObj.value != 'SE' && serviceNameObj.value != 'IW'  && serviceNameObj.value != 'VS'){
			_id.i("error1", "Invalid Service .", "e");
				document.getElementById('result').innerHTML="";
	 	  		document.getElementById('result1').innerHTML="";	
				return false;
	} 
	_id.i("error1", "");
	return true;
}

function setInputHiddenValue(bValue) {
	document.getElementById('hiddenInput').value=bValue;
 	 document.getElementById('result').innerHTML="";
 	  document.getElementById('result1').innerHTML="";
 	  
 }
 var rowCount=0;
 function removeRow(id){
 var rows=(_id.o('searchTlb').getElementsByTagName('tr').length)-rowCount;
if(rows>3){
	_id.o(id).style.display='none';
	rowCount++;
}
else{
	_id.o('searchDiv').innerHTML='<table id="searchTlb" width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center"><tr><th align="center" colspan="5">Search Result</th></tr><tr><td colspan="5" align="center">No Records to Process for VAT Submission</td></tr></table>';
}
}
 parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/accMgmt/backOffice/js/SearchVat.js,v $'] = '$Id: SearchVat.js,v 1.4 2016/10/31 09:46:22 neeraj Exp $';