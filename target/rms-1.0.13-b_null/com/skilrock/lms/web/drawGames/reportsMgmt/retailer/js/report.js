 var i18nObj = parent.frames[0].i18Obj;

function setInputField(tvalue) {
	document.getElementById("down").innerHTML="";
		if(tvalue=='Date Wise') {
			//alert("inside if"+document.getElementById("date").style.visibility);
			document.getElementById("date").style.display="block";
			_id.i('dates','');
		}
		else {
			//alert(" else");
			document.getElementById("date").style.display="none";
		}
}



function setField(tvalue) {
	document.getElementById("down").innerHTML="";
	if(tvalue=='Game Wise') {
			document.getElementById("list").style.display="none";
			//alert("inside if"+document.getElementById("game").style.display);
			//document.getElementById("date").innerHTML="";
			//document.getElementById("datewise").innerHTML="";
			document.getElementById("game").innerHTML="<tr><td class='tdLabel'><label for='game' class='label'>Game Status:</label></td> <td> <select name='gameStatus' id='game' class='option'>   <option value='ALL'>ALL</option>  <option value='OPEN'>OPEN</option>   <option value='CLOSE'>CLOSE</option>    <option value='TERMINATE'>TERMINATE</option>    <option value='SALE_HOLD'>SALE_HOLD</option>     <option value='SALE_CLOSE'>SALE_CLOSE</option>  </select>" ;
			//document.getElementById("datewise").innerHTML="<tr><td class='tdLabel'><label for='pwtSearchReportBO_totaltime' class='label'>Get Sale Reports :</label></td><td><select name='totaltime' id='pwtSearchReportBO_totaltime' class='option'><option value='Current Day'>Current Day</option>   <option value='Current Week'>Current Week</option>   <option value='Current Month'>Current Month</option> </select> </td>";
		}
		else {
			//alert(" else");
			document.getElementById("game").innerHTML="";
			//document.getElementById("datewise").innerHTML="<tr><td class='tdLabel'><label for='pwtSearchReportBO_totaltime' class='label'>Get Sale Reports :</label></td><td><select name='totaltime' id='pwtSearchReportBO_totaltime' class='option' onchange='setInputField(this.value)'><option value='Current Day'>Current Day</option>   <option value='Current Week'>Current Week</option>   <option value='Current Month'>Current Month</option>   <option value='Date Wise'>Date Wise</option></select> </td>";
		}
	if(tvalue=='Agent Wise'){
		document.getElementById("list").style.display="none";
		_resp = _ajaxCall('fetchAjaxAgentList.action');
		document.getElementById("list").innerHTML=_resp.data;
	}
	else{
		
	}
	if(tvalue=='Retailer Wise'){
	
	}
	else{
		
	}
	
}






function validateDates() {

	var isInValid = false;
     
	//alert();
	var startDate = document.getElementById("start_date").value;
	var endDate =  document.getElementById("end_date").value;	
    // alert(startDate+"  "+endDate);
    
	if (startDate == "" || endDate == "") {
		isInValid = true;
		document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
       //alert("please enter dates");      
	} else {
		var stArr = startDate.split('-');
		var endArr = endDate.split('-');
		var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
		var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
		if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
			isInValid = true;
			document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
          //alert("end date should be lesser then start date");	
		}
	}
	//alert(isInValid);
	if (isInValid) {
	//alert("inside isinvalid");
		if(document.getElementsByName('totaltime')[0].value != 'Date Wise'){
			return true;
		}
		return false;
	}
	document.getElementById("dates").innerHTML = "";
	return true;

}



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/reportsMgmt/retailer/js/report.js,v $'] = '$Id: report.js,v 1.3 2016/10/31 09:46:50 neeraj Exp $';