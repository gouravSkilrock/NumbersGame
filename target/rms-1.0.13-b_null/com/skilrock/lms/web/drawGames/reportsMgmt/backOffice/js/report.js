var i18nObj = parent.frames[0].i18Obj;
function setInputField(tvalue) {
	document.getElementById("down").innerHTML="";
		if(tvalue=='Date Wise') {
			// alert("inside
			// if"+document.getElementById("date").style.visibility);
			document.getElementById("date").style.display="block";
			_id.i('dates','');
		}
		else {
			// alert(" else");
			document.getElementById("date").style.display="none";
		}
		document.getElementById("result").innerHTML="";
}

var agtObj=null;

function setField(tvalue) {
	document.getElementById("down").innerHTML="";
	if(tvalue=='Game Wise') {
			document.getElementById("list").style.display="none";
			// alert("inside if"+document.getElementById("game").style.display);
			// document.getElementById("date").innerHTML="";
			// document.getElementById("datewise").innerHTML="";
			// document.getElementById("game").innerHTML="<tr><td
			// class='tdLabel'><label for='game' class='label'>Game
			// Status:</label></td> <td> <select name='gameStatus' id='game'
			// class='option'> <option value='ALL'>ALL</option> <option
			// value='OPEN'>OPEN</option> <option value='CLOSE'>CLOSE</option>
			// <option value='TERMINATE'>TERMINATE</option> <option
			// value='SALE_HOLD'>SALE_HOLD</option> <option
			// value='SALE_CLOSE'>SALE_CLOSE</option> </select>" ;
			// document.getElementById("datewise").innerHTML="<tr><td
			// class='tdLabel'><label for='pwtSearchReportBO_totaltime'
			// class='label'>Get Sale Reports :</label></td><td><select
			// name='totaltime' id='pwtSearchReportBO_totaltime'
			// class='option'><option value='Current Day'>Current Day</option>
			// <option value='Current Week'>Current Week</option> <option
			// value='Current Month'>Current Month</option> </select> </td>";
		}
		else {
			// alert(" else");
			document.getElementById("game").innerHTML="";
			// document.getElementById("datewise").innerHTML="<tr><td
			// class='tdLabel'><label for='pwtSearchReportBO_totaltime'
			// class='label'>Get Sale Reports :</label></td><td><select
			// name='totaltime' id='pwtSearchReportBO_totaltime' class='option'
			// onchange='setInputField(this.value)'><option value='Current
			// Day'>Current Day</option> <option value='Current Week'>Current
			// Week</option> <option value='Current Month'>Current
			// Month</option> <option value='Date Wise'>Date
			// Wise</option></select> </td>";
		}
	if(tvalue=='Retailer Wise'){
		document.getElementById("list").style.display="block";
		_resp = _ajaxCall('fetchAjaxAgentList.action');
		_respData = _resp.data;
		resData = _respData.split(":");
	
		agtObj = _id.o("agtId");
		agtObj.options.length = 1;
		
		showAgtOrg();
		
	}
	else{
		document.getElementById("list").style.display="none";
	}
}

function showAgtOrg(seltype){
	
		agtObj.innerHTML ="";
		agtObj.innerHTML="<option value=''>--Please Select--</option>";
		for (var i = 0; i < resData.length ; i=i+1) {
			var retNameArray = resData[i].split("|");
			if(retNameArray[0] != ''){
			var opt1 = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			agtObj.options[i + 1] = opt1;
			}
		
		}
	
document.getElementById("agentlistDiv").style.display="block";	
}

function closeDiv()
{
	
	var startDate = document.getElementById("start_date").value;
	var archDate = document.getElementById("arch_date").value ;
	var sDtArr = startDate.split('-');
	var sDate = sDtArr[2]+'-'+sDtArr[1]+'-'+sDtArr[0] ;
	var finalArchDate = Date.parse(archDate) ;
	var finalStartDate = Date.parse(sDate) ;
	
	if(finalArchDate <= finalStartDate)
	{
		document.getElementById("dates").innerHTML = "" ;
	}


}


function validateDates() {

	var isInValid = false;
     
	var startDate = document.getElementById("start_date").value;
	var endDate =  document.getElementById("end_date").value;
	var archDate = document.getElementById("arch_date").value ;
	var sDtArr = startDate.split('-');
	var sDate = sDtArr[2]+'-'+sDtArr[1]+'-'+sDtArr[0] ;
	var finalArchDate = Date.parse(archDate) ;
	var finalStartDate = Date.parse(sDate) ;
    
	if (startDate == "" || endDate == "") {
		isInValid = true;
		document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
       // alert("please enter dates");
	} else if(finalArchDate > finalStartDate)
	{
		document.getElementById("down").innerHTML = "<font color = 'red'>Please select date after "+ archDate +"</font>";
		//document.getElementById("down").innerHTML = "";
		return false;
	} else {
		var stArr = startDate.split('-');
		var endArr = endDate.split('-');
		var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
		var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
		if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
			isInValid = true;
			document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
          // alert("end date should be lesser then start date");
		}
	}
	// alert(isInValid);
	if (isInValid) {
		// alert("inside isinvalid");
		if((document.getElementsByName('totaltime').length != 0)&&(document.getElementsByName('totaltime')[0].value != 'Date Wise')){
			return true;
		}
		return false;
	}
	// document.getElementById("dates").innerHTML = "";
	if(checkField())
	return true;
	else
	return false

}

function validateDatesWithoutArchive() {

	var isInValid = false;
     
	var startDate = document.getElementById("start_date").value;
	var endDate =  document.getElementById("end_date").value;
	/* var archDate = document.getElementById("arch_date").value ;
	var sDtArr = startDate.split('-');
	var sDate = sDtArr[2]+'-'+sDtArr[1]+'-'+sDtArr[0] ;
	var finalArchDate = Date.parse(archDate) ;
	var finalStartDate = Date.parse(sDate) ;
    */
	if (startDate == "" || endDate == "") {
		isInValid = true;
		document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
       // alert("please enter dates");
	}/* else if(finalArchDate > finalStartDate)
	{
		document.getElementById("dates").innerHTML = "<font color = 'red'>Please select date after "+ archDate +"</font>";
		return false;
	}*/ else {
		var stArr = startDate.split('-');
		var endArr = endDate.split('-');
		var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
		var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
		if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
			isInValid = true;
			document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
          // alert("end date should be lesser then start date");
		}
	}
	// alert(isInValid);
	if (isInValid) {
		// alert("inside isinvalid");
		if((document.getElementsByName('totaltime').length != 0)&&(document.getElementsByName('totaltime')[0].value != 'Date Wise')){
			return true;
		}
		return false;
	}
	// document.getElementById("dates").innerHTML = "";
	if(checkField())
	return true;
	else
	return false

}



function validatePanelDates() {			
	var isInValid = false;
	var startDate = _id.o("start_date").value;
	var endDate =  _id.o("end_date").value;	
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

function checkField(){
if(document.getElementById("agtId") != null){
	val = document.getElementById("agtId").value;
}
if(document.getElementById("AgentId") != null){
	val = document.getElementById("AgentId").value
}
else{
	return true;
}
if(val==-1){
alert(i18nObj.prop('msg.pls.select')+" "+tierMap['AGENT']);
return false;
}
return true;
}


function calcTotal(){
var table = document.getElementById("expandTable");
var rowCount = table.rows.length;
var colCount=table.rows[3].getElementsByTagName('td').length;
var headCount=table.rows[3].getElementsByTagName('th').length;
for(var c=2;c<colCount+headCount;c++){
	var count=0.0;
	for(var r=3;r<rowCount-1;r++){
		if(c<colCount){
			count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('td')[c].innerHTML).replace(/,/g,""));
		}else{
			count=parseFloat(count)+parseFloat((table.rows[r].getElementsByTagName('th')[c-colCount].innerHTML).replace(/,/g,""));
		}
	}
	var x=table.rows[rowCount-1].insertCell(c);
	x.innerHTML=addCommas(count.toFixed(2));
}

}

function getData() {
	var tblData = _id.o("tableDataDiv").innerHTML;
	tblData = tblData.replace(_id.o("sortRow").innerHTML,
			"");
	_id.o('reportData').value = tblData;
	return false;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js,v $'] = '$Id: report.js,v 1.3 2016/10/31 09:46:33 neeraj Exp $';