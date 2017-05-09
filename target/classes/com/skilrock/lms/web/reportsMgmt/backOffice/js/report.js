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
			//alert("inside if"+document.getElementById("game").style.display);
			//document.getElementById("date").innerHTML="";
			//document.getElementById("datewise").innerHTML="";
			document.getElementById("game").innerHTML="<tr><td class='tdLabel'><label for='game' class='label'>"+i18nObj.prop('label.game.status')+":</label></td> <td> <select name='gameStatus' id='game' class='option'>   <option value='ALL'>"+i18nObj.prop('label.ALL')+"</option>  <option value='OPEN'>"+i18nObj.prop('OPEN')+"</option>   <option value='CLOSE'>"+i18nObj.prop('CLOSE')+"</option> <option value='TERMINATE'>"+i18nObj.prop('TERMINATE')+"</option> <option value='SALE_HOLD'>"+i18nObj.prop('SALE_HOLD')+"</option> <option value='SALE_CLOSE'>"+i18nObj.prop('SALE_CLOSE')+"</option>  </select>" ;
			//document.getElementById("datewise").innerHTML="<tr><td class='tdLabel'><label for='pwtSearchReportBO_totaltime' class='label'>Get Sale Reports :</label></td><td><select name='totaltime' id='pwtSearchReportBO_totaltime' class='option'><option value='Current Day'>Current Day</option>   <option value='Current Week'>Current Week</option>   <option value='Current Month'>Current Month</option> </select> </td>";
		}
		else {
			//alert(" else");
			document.getElementById("game").innerHTML="";
			//document.getElementById("datewise").innerHTML="<tr><td class='tdLabel'><label for='pwtSearchReportBO_totaltime' class='label'>Get Sale Reports :</label></td><td><select name='totaltime' id='pwtSearchReportBO_totaltime' class='option' onchange='setInputField(this.value)'><option value='Current Day'>Current Day</option>   <option value='Current Week'>Current Week</option>   <option value='Current Month'>Current Month</option>   <option value='Date Wise'>Date Wise</option></select> </td>";
		}
}






function validateDates() {

	var isInValid = false;
     
	//alert();
	var startDate = document.getElementById("start_date").value;
	var endDate =  document.getElementById("end_date").value;
    
    if(startDate == null || endDate == null){
    	return false;
    }
	if (startDate == "" || endDate == "") {
		isInValid = true;
		document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
       //alert("please enter dates");      
	} else {
		var stArr = startDate.split('-');
		var endArr = endDate.split('-');
		var tempStDate = stArr[1] + '/' + stArr[0] + '/' + stArr[2]; 
		var tempEndDate = endArr[1] + '/' + endArr[0] + '/' + endArr[2]; 
		if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
			isInValid = true;
			document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
			return false;
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
function getUserName()
{
		var option = _id.o("userName");
		var data = "";
		data=(document.getElementById('userData').value).replace("{","").replace("}","").replace(/ /g,"");
		var userArr = data.split(",");
		var UserNameMap;
		for ( var i = 0; i < userArr.length; i++) {
			UserNameMap = new Object();
			var userDetailsArr = userArr[i].split("=");
			UserNameMap['userId'] = userDetailsArr[0];
			UserNameMap['userName'] = userDetailsArr[1];
	
			var gkey = userDetailsArr[0] + ':' + userDetailsArr[1];
			UserNameMap[gkey] = UserNameMap;

			var dvalue = userDetailsArr[1];

			var opt = new Option(dvalue, gkey);
			option.options[i + 1] = opt;

		}

}
function validateUser()
{
	var userName = _id.v("userName");
	if(userName == -1)
	{
		_id.i("userName_e", i18nObj.prop('error.pls.entr.user.name'), "e");
		return false;
	}
	else
	{
		_id.i("userName_e", "");
		return true;
	}
}

function checkPrivIs(vid) {
	
	document.getElementById('privTab').style.display='block';
	document.getElementById('userTab').style.display='none';
	document.getElementById('resultPriv2').style.display='none'
	document.getElementById('service_err').style.display = 'block';
	document.getElementById('priv_err').style.display = 'block';
	document.getElementById('group_err').style.display = 'block';
	document.getElementById('tier_err').innerHTML = "";
	document.getElementById('users_err').innerHTML = "";
	document.getElementById('tier_err').style.display = 'none';
	document.getElementById('dept_err').style.display = 'none';
	document.getElementById('users_err').style.display = 'none';
	document.getElementById('tier').selectedIndex = "0";
	document.getElementById("user").options.length = 1;
	
	
}

function checkUserIs(vid) {
	
	document.getElementById('privTab').style.display='none';
	document.getElementById('userTab').style.display='block';
	document.getElementById('resultPriv').style.display='none';
	document.getElementById('service').selectedIndex = "0";
	document.getElementById('dept_err').style.display = 'block';
	document.getElementById('tier_err').style.display = 'block';
	document.getElementById('users_err').style.display = 'block';
	document.getElementById('service_err').innerHTML = "";
	document.getElementById('priv_err').innerHTML = "";
	document.getElementById('group_err').innerHTML = "";
	document.getElementById('service_err').style.display = 'none';
	document.getElementById('priv_err').style.display = 'none';
	document.getElementById('group_err').style.display = 'none';	
	document.getElementById("priv").options.length = 1;
	document.getElementById("group").options.length = 1;
	
}

function resetValues(){
	document.getElementById("priv").options.length = 1;
	document.getElementById("group").options.length = 1;
	document.getElementById("service").selectedIndex = "0";
	document.getElementById("priv").selectedIndex = "0";
	document.getElementById("group").selectedIndex = "0";
}

function chageGroupList(serviceValue, updOptId, fetOptId, val) {	
	//alert(val);
	//alert(serviceValue+"#"+updOptId+"#"+fetOptId+"#"+val);
	var groupObj = _id.o(updOptId);	
	groupObj.options.length=1;	
	var selObj = _id.o(fetOptId);
	var text, value, count=1;
	var groupArr;
	for(var i=0,l=selObj.length; i<l; i=i+1) {
		value = selObj.options[i].value
		groupArr = (selObj.options[i].text).replace("[","").replace("]","").trim().split(",");
		//alert(text+" = "+ value);
		for(var j = 0; j<groupArr.length; j=j+1)
		{	
			//alert("Value:"+groupArr[j]);
			if(value == serviceValue) {			
				var opt = new Option(groupArr[j],groupArr[j]+"$"+serviceValue);
				groupObj.options[count] = opt;
				
				if(value==val){
					groupObj.options.selected=true;
				}				
				count = count +1;				
			}		
			
		}
			
	}	
}


function chagePrivList(groupValue, updOptId, fetOptId, val) {	
	//alert(groupValue);
	//alert(serviceValue+"#"+updOptId+"#"+fetOptId+"#"+val);
	var privGroupObj = _id.o(updOptId);	
	privGroupObj.options.length=1;	
	var selObj = _id.o(fetOptId);
	var text, value, count=1;
	var privGroupArr;
	for(var i=0,l=selObj.length; i<l; i=i+1) {
		privGroupArr = selObj.options[i].value.split("-");
		//alert(privGroupArr[1]);
		value = selObj.options[i].text+"$"+privGroupArr[1];
		text = privGroupArr[2];
		//alert("value: "+value+"groupValue: ");
		//groupArr = (selObj.options[i].text).replace("[,","").replace("]","").trim().split(",");
		//alert(text+" = "+ value);
			if(privGroupArr[1].trim()+"$"+privGroupArr[0] == groupValue.trim()) {	
				
				var opt = new Option(text, value);
				privGroupObj.options[count] = opt;
				
				if(privGroupArr[1].trim()==val){
					privGroupObj.options.selected=true;
				}
				
				count = count +1;				
			}
	
			
	}	
}

function chageUserTypeList(deptValue, updOptId, fetOptId, val) {
	var privGroupObj = _id.o(updOptId);	
	privGroupObj.options.length=1;	
	var selObj = _id.o(fetOptId);
	var text, value, count=1;
	var privGroupArr;
	for(var i=0,l=selObj.length; i<l; i=i+1) {
		value = deptValue+"-"+selObj.options[i].value;
		text = selObj.options[i].text;
			
		if(deptValue > -1){
			var opt = new Option(text, value);
			privGroupObj.options[count] = opt;
		}
				
		if(deptValue==val){
			privGroupObj.options.selected=true;
		}
		count = count +1;		
	}	
}


function chageUserList(tierValue, updOptId, fetOptId, val) {	
	//alert(val);
	//alert(serviceValue+"#"+updOptId+"#"+fetOptId+"#"+val);	
	var userObj = _id.o(updOptId);	
	userObj.options.length=1;	
	var selObj = _id.o(fetOptId);
	var text, value, count=1;
	var userArr, roleId;
	
	for(var i=0,l=selObj.length; i<l; i=i+1) {
		userArr = (selObj.options[i].text).replace("[","").replace("]","").trim().split(",");
		//alert(text+" = "+ value);
		for(var j = 0; j<userArr.length; j=j+1)
		{		
			roleId = userArr[j].split("-")[1];
				if(tierValue == userArr[j].split("-")[1]+"-"+userArr[j].split("-")[2]){		
					var opt = new Option(userArr[j].split("-")[3],userArr[j].split("-")[0]+"-"+userArr[j].split("-")[1]+"-"+userArr[j].split("-")[2]);
					userObj.options[count] = opt;
					count = count +1;	
				}
				
				if(tierValue.split("-")[1] == "AL"){
					if(tierValue.split("-")[0] == userArr[j].split("-")[1]){
					var opt = new Option(userArr[j].split("-")[3],userArr[j].split("-")[0]+"-"+userArr[j].split("-")[1]+"-"+userArr[j].split("-")[2]);
					userObj.options[count] = opt;
					count = count +1;	}
				}
				
				if(tierValue==val){
					userObj.options.selected=true;
				}				
									
		}
			
	}	
}

function chageValuebyPriv(privValue, updFieldId ) {	
	//alert(privValue);
	//alert(serviceValue+"#"+updOptId+"#"+fetOptId+"#"+val);
	var privIdsObj = _id.o(updFieldId);
	var value = privValue.split("$")[0].replace("[","").replace("]","");
	//alert(value);
	privIdsObj.value = value;
	return true;
}

function changeValuebyRelated(relatedValue, fetchOptId, updFieldId){
	
	//alert(relatedValue.trim());	
	var selObj = _id.o(fetchOptId);
	var privIdsObj = _id.o(updFieldId);
	var reqValArr, reqVal;

	for(var i=0,l=selObj.length; i<l; i=i+1) {
		reqValArr = (selObj.options[i].value).split("-");
		reqVal = reqValArr[1];
		
		if(reqVal.trim() == relatedValue.trim()){
			privIdsObj.value = (selObj.options[i].text).replace("[","").replace("]","");
		}	
	}
}

function changeValuebyService(serviceValue, fetchOptId, updFieldId){
	
	//alert(serviceValue.trim());	
	var selObj = _id.o(fetchOptId);
	var privIdsObj = _id.o(updFieldId);
	var reqVal;

	for(var i=0,l=selObj.length; i<l; i=i+1) {
		reqVal = selObj.options[i].value;		
		//alert(reqVal+"#"+serviceValue);
		if(reqVal.trim() == serviceValue.trim()){
			privIdsObj.value = (selObj.options[i].text).replace("[","").replace("]","");
		}
	
	}
}



function _privServiceValidate() {	
	
	var frmVal  = new parent.frames[0].Validator("editPriv");
	
	if(document.getElementById('privTab').style.display == "block"){
		frmVal.aV("serviceId","dontselect=0",i18nObj.prop('error.pls.select.any.serv.name'),"service_err");		  
		//frmVal.aV("groupId","dontselect=0","Please Select any Group Name","group_err");	
		//frmVal.aV("privId","dontselect=0","Please Select any Priviledge Name","priv_err");
	}else{		  
		frmVal.aV("deptId","dontselect=0",i18nObj.prop('error.pls.select.any.dept'),"dept_err");
		frmVal.aV("tierId","dontselect=0",i18nObj.prop('error.pls.select.any.user.type'),"tier_err");
		frmVal.aV("userId","dontselect=0",i18nObj.prop('error.pls.select.any.user'),"users_err");
		
	}
	
	if(document.error_disp_handler.isValid){
		
		if( _id.o('privWiseReport').checked){
			getPrivUsers();
		}else{
			getUserPrivs();
		}
	}
	else{
			return (document.error_disp_handler.isValid);
	}
}


function getPrivUsers()
{
  //var userName=document.getElementById('userName').value;
  var url='bo_rep_privWiseUserReport_Search.action?serviceId='+_id.o("service").value+'&privIds='+_id.o("privIds").value;
  //alert(url);
    
   document.getElementById('lodingImage').style.display="block";
   _resp  = _ajaxCall(url);
   //alert(_resp);
   
	if(_resp.result){
        document.getElementById('lodingImage').style.display="none";
        _id.o('resultPriv').style.display="block";
        _id.i('resultPriv',_resp.data);
     
      } else {
        alert("Problem: ");
        }
 
return false;
}


function getUserPrivs()
{
	var obj = document.getElementById('user');
	var userName= obj.options[obj.selectedIndex].text;
	var objValue= obj.options[obj.selectedIndex].value;
	var userId = objValue.split("-")[0];	
  //alert(userId);
  var url='bo_rep_fetchUserActivePrivs.action?userId = '+userId;
    
    document.getElementById('lodingImage').style.display="block";
   _resp  = _ajaxCall(url);
  // alert(_resp);
	if(_resp.result){
        document.getElementById('lodingImage').style.display="none";
        _id.o('resultPriv2').style.display="block";
        _id.i('resultPriv2',_resp.data);
     
      } else {
        alert("Problem: ");
        } 
return false;
}


function clearContent(list, divId){	
	
	var obj = _id.o(list);
		document.getElementById(divId).innerHTML = '';
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js,v $'] = '$Id: report.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';