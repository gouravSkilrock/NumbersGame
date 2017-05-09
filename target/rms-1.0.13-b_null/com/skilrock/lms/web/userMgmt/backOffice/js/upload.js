
   function validate(){
 var isInValid = false;
for (var i=0;i < document.forms[0].elements.length;i++)
	{
		var e = document.forms[0].elements[i];
		if (e.name == "rankupload")
		{
			if(e.value==""){
			document.getElementById('rank').innerHTML = "<font color = 'red'>Please Select File</font>";
			isInValid=true;
			}
			else{
			document.getElementById('rank').innerHTML = "";
			}
		}
		if (e.name == "details")
		{
			if(e.value==""){
			document.getElementById('det').innerHTML = "<font color = 'red'>Please Select File</font>";
			isInValid=true;
			}
			else{
			document.getElementById('det').innerHTML = "";
			}
		}
}
if(isInValid){
  return false;
  }
  return true;
  }
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/upload.js,v $'] = '$Id: upload.js,v 1.1 2010/04/01 04:22:21 gaurav Exp $';