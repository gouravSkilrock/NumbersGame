
function showDenyFields() {
//alert();
		var value = confirm("Are you sure to deny PWT Process ?");
		if(value) {
			var Fields = document.getElementById('denyfields');
			
			Fields.style.display = "block";
			Fields.focus();
		}
		return false;
}


function checkDenyPwtStatus() {
		var fields = document.getElementsByName('denyPwtStatus');
		//alert(" length = "+fields[0].checked +""+fields[1].checked) ;
		
		if(fields[0].checked || fields[1].checked) {
			return true;
		}
		else {
			alert("Please Select Atleast One Option");
			return false;
		}
}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/denyPwt.js,v $'] = '$Id: denyPwt.js,v 1.1 2010/04/02 12:12:10 gaurav Exp $';