// Date specific js By Mukesh Sharma (Removed from jsp due to same in many)
function setDateField(tvalue) {//alert(tvalue);
	document.getElementById("down").innerHTML="";
	if(tvalue=='Date Wise') {
		//alert("inside if"+document.getElementById("date").style.visibility);
		document.getElementById("date").style.display="block";
	}				
	else{ 
		//alert('--in else'+tvalue);
		if(tvalue=='Current Day') {//alert();
		//alert('<s:property value="#session.presentDate"/>');
		_id.o("start_date").value=_id.o("curDate").value;
		_id.o("end_date").value=_id.o("curDate").value;
		document.getElementById("date").style.display="none";
		}
		else {
			document.getElementById("date").style.display="none";
		}
	}
	_id.i('dates','');
}

function convertDateFormat(date){
	var splitDate = date.split("-");
	var newDate = splitDate[1]+"-"+splitDate[0]+"-"+splitDate[2];
	return newDate;
}

function validateDates() {
	var isInValid = false;
	var startDate = new Date(convertDateFormat(_id.o("start_date").value));
	var endDate =  new Date(convertDateFormat(_id.o("end_date").value));	
	if (startDate == "" || endDate == "") {
		isInValid = true;
		_id.o("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";			           
	} else {
		if (endDate < startDate) {
			isInValid = true;
			_id.o("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
	    }
	}				
	if (isInValid) {				
		return false;
	}
	_id.o("dates").innerHTML = "";
	return true;			
}

function clearDiv(){
	_id.i("down","");
	_id.i("result","");
}



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/common/globalJs/validatesDate.js,v $'] = '$Id: validatesDate.js,v 1.2 2017/03/27 14:46:50 MukeshSharma Exp $';
