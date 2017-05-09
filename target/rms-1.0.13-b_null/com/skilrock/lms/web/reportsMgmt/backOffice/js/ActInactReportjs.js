var _respCity ;
var _respCityZones;
var i18nObj = parent.frames[0].i18Obj;
function clearDiv(){
				_id.o("errorDiv").innerHTML="";
				_id.o("down").innerHTML="";
				if(_id.o("GraphTable")!=null)
					_id.o("GraphTable").style.display = 'none';
			}	


function checkChartType(){
	clearDiv();
	if($('input[type=radio]:checked').val()=='pie'){
		cleanListValues();
	}else{
		if(_id.o("checkType").value=="pie")
			_id.o("reportType").selectedIndex=0;
	}
	_id.o("checkType").value=$('input[type=radio]:checked').val();
	checkReportItem();
	
}

function cleanListValues(){
	clearDiv();
	var cityName = _id.o("cityName");
	for(var i=cityName.options.length-1;i>=0;i=i-1)
	   {
	    	if(cityName.options[i].value!=-1)
	   		cityName.remove(i);
    }
	var cityName = _id.o("regionName");
	for(var i=cityName.options.length-1;i>=0;i=i-1)
	   {
	    	if(cityName.options[i].value!=-1)
	   		cityName.remove(i);
    }
	removeElements("zones");
	_id.o("regionNameDiv").style.display='none';
	_id.o("regionNameLabelDiv").style.display='none';
	_id.o("cityNameLabelDiv").style.display='none';
	_id.o("cityNameDiv").style.display='none';
}

function validateData()
		{
		clearDiv();
		var isChecked = false;
		var reportType = _id.o("reportType").options[ _id.o("reportType").selectedIndex].value;
		if(reportType==-1){
			_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.rep.type')+"</font></center>";
			return false;
		}
		if(reportType==2){
			if(_id.o("agentName").options[_id.o("agentName").selectedIndex].value==-1){
				_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.org.name.empty')+" </font></center>";
				return false;
			}
		}
		if(reportType==3 || reportType==4){
			if(_id.o("cityName").options[_id.o("cityName").selectedIndex].value==-1){
				_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.region')+"</font></center>";
				return false;
			}
			else{
				if(_id.o("cityName").options[_id.o("cityName").selectedIndex].value=='ALL'){
					_id.o("dates").innerHTML = "";
					return true;
				}
				var arrayCheck=_id.n("cityNamesArray");
				var len= arrayCheck.length;
				for(var i=0;i<len;i=i+1){
					if(arrayCheck[i].checked){
					isChecked=true;
					break;
					}
				}
				
				if(!isChecked){
					_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.sub.region.city')+" </font></center>";
					return false;
				}
			}
		}
		return true;
		}

function showHideData()
		{
			
			clearDiv();
			_id.o("dates").innerHTML = "";
			var parentElement = _id.o("city");
				while (parentElement.hasChildNodes()) {
					parentElement.removeChild(parentElement.lastChild);
				}
			
			var _resp ;
			var agentName = _id.o('agentName');
			var cityName = _id.o("cityName")
			var reportType = _id.o("reportType").options[ _id.o("reportType").selectedIndex].value;
			var endDate = _id.o("endDate").value;

			if(reportType==4)
				_id.o("endDateDiv").style.display='none';
			else
				_id.o("endDateDiv").style.display='block';
		
			if(reportType==-1){
				clearDiv();
				_id.o("dates").innerHTML = "";
				_id.o("cityNameDiv").style.display='none';
				_id.o("agentNameDiv").style.display='none';
			}else if(reportType==1)
				{
				clearDiv();
				_id.o("dates").innerHTML = "";
				_id.o("cityNameDiv").style.display='none';
				_id.o("agentNameDiv").style.display='none';
			}
			else if(reportType==2)
			{
				clearDiv();
				_id.o("dates").innerHTML = "";
			    for(var i=agentName.options.length-1;i>=0;i=i-1)
				    {
				    	if(agentName.options[i].value!=-1)
				    	agentName.remove(i);
				}
				
				_id.o("cityNameDiv").style.display='none';
				_id.o("agentNameDiv").style.display='none';
				_resp = _ajaxCall("getAgentMapToDisplay.action?endDate=" + endDate);
					if (_resp.result && _resp.data != "") 
					{
						var dataArr = _resp.data.split(":");
							for ( var i = 0; i < dataArr.length-1; i=i+1) {
								var opt = new Option(dataArr[i].split("|")[1], dataArr[i].split("|")[0]);
								agentName.options[i + 1] = opt;
							}
					}
					_id.o("agentNameDiv").style.display='block';
			}
			else if(reportType==3)
			{
				clearDiv();
				_id.o("dates").innerHTML = "";
				_id.o("agentNameDiv").style.display='none';
				_id.o("cityNameDiv").style.display='none';
				for(var i=cityName.options.length-1;i>=0;i=i-1)
			    {
			    	if(cityName.options[i].value!=-1)
			    		cityName.remove(i);
			    }
				_respCity = _ajaxCall("getCityListToDisplay.action");
					if (_respCity.result && _respCity.data != "") 
					{
						var dataArr = _respCity.data.split("|");
							for ( var i = 0; i < dataArr.length; i=i+1) {
								var opt = new Option(dataArr[i].split("=")[0], dataArr[i].split("=")[0]);
								cityName.options[i + 1] = opt;
							}
					}
					cityName.selectedIndex=0;
					_id.o("cityNameDiv").style.display='block';
			}
			else if(reportType==4)
			{
				clearDiv();
				_id.o("dates").innerHTML = "";
				_id.o("agentNameDiv").style.display='none';
				_id.o("cityNameDiv").style.display='none';
				var opt = new Option('ALL' , 'ALL');
				cityName.options[1] = opt;
			
				_respCity = _ajaxCall("getCityListToDisplay.action");
					if (_respCity.result && _respCity.data != "") 
					{
						var dataArr = _respCity.data.split("|");
							for ( var i = 0; i < dataArr.length; i=i+1) {
								var opt = new Option(dataArr[i].split("=")[0], dataArr[i].split("=")[0]);
								cityName.options[i + 2] = opt;
							}
					} 
					_id.o("cityNameDiv").style.display='block';
					cityName.selectedIndex=0;
				}
		}

function createRegions(object)
		{
			clearDiv();
			_id.o("dates").innerHTML = "";
			var dataArr = _respCity.data.split("|");
			var cityArr;
			for ( var i = 0; i < dataArr.length; i=i+1) {
				if(dataArr[i].split("=")[0]==object)
				{
				cityArr=dataArr[i].split("=")[1].split(",")
				break;
				}
			}
			var parentElement = _id.o("city");
			while (parentElement.hasChildNodes()) {
				parentElement.removeChild(parentElement.lastChild);
			}
			for(var count=0; count<cityArr.length;count=count+1)
			{
				if(cityArr[count].split("-")[0] != ""){
			    var newCheckBox = document.createElement('input');
			    newCheckBox.type = 'checkbox';
			    newCheckBox.id = 'cityNamesArray'
			    newCheckBox.name = 'cityNamesArray'
			    newCheckBox.value = cityArr[count].split("-")[0];
			    var text = document.createTextNode( cityArr[count].split("-")[0]);
			    parentElement.appendChild(newCheckBox);
			    parentElement.appendChild(text);
				}
			}
		}
			

function getData() 
	{
		var tblData = _id.o("tableDataDiv").innerHTML;
		tblData = tblData.replace(_id.o("sortRow").innerHTML,"");
		_id.o('tableValue').value = tblData;
		return false;
	}
		
function validateDates() 
		{
			if(_id.o("reportType").value==4)
				return true;
		
			var isInValid = false;
			var startDate = document.getElementById("startDate").value;
			var endDate =  document.getElementById("endDate").value;
			if(startDate == null || endDate == null)
			   	return false;
			
			if (startDate == "" || endDate == "") 
			{
				isInValid = true;
				document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
			}
			else
			{
					var tempStDate;
					var tempEndDate
					var stArr = startDate.split('-');
					var endArr = endDate.split('-');

					if(BrowserDetect.browser=='Chrome')
					{
						tempStDate = stArr[1] + '-' + stArr[2] + '-' + stArr[0]; 
						tempEndDate = endArr[1] + '-' + endArr[2] + '-' + endArr[0];	
					}
					else
					{
					tempStDate = stArr[1] + '/' + stArr[2] + '/' + stArr[0]; 
					tempEndDate = endArr[1] + '/' + endArr[2] + '/' + endArr[0]; 
					}
					
					if ((Date.parse(tempEndDate) < Date.parse(tempStDate))) {
						isInValid = true;
						document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
					}
			}
			if (isInValid) 
			{
					return false;
			}
			return true;
		}
			
function onAgentChange()
		{
			_id.o("agentCode").value=_id.o("agentName").options[_id.o("agentName").selectedIndex].innerHTML;
				
		}

function populateCityList()
		{
	
			clearDiv();
			removeElements("zones");
			_id.o("regionNameDiv").style.display='none';
			_id.o("regionNameLabelDiv").style.display='none';
			_id.o("cityNameDiv").style.display='none';
			_id.o("cityNameLabelDiv").style.display='none';
			if(_id.o("reportType").value==1)
			{
				var reagionName = _id.o("regionName");
				_id.o("dates").innerHTML = "";
				var opt = new Option('ALL' , 'ALL');
				reagionName.options[1] = opt;
				_respCityZones = _ajaxCall("getCityListToDisplay.action");
				if (_respCityZones.result && _respCityZones.data != "") 
				{
					var dataArr = _respCityZones.data.split("|");
						for ( var i = 0; i < dataArr.length-1; i=i+1) {
							var opt = new Option(dataArr[i].split("=")[0], dataArr[i].split("=")[0]);
							reagionName.options[i + 2] = opt;
						}
				} 
				reagionName.selectedIndex=0;
				_id.o("regionNameDiv").style.display='block';
				_id.o("regionNameLabelDiv").style.display='block';
			}
			checkChartType();
		}
			
function createCities(object)
		{
	
			removeElements("zones");
			_id.o("cityNameDiv").style.display='none';
			_id.o("cityNameLabelDiv").style.display='none';
			clearDiv();
			var cityName = _id.o("cityName");
			_id.o("dates").innerHTML = "";
			
			 for(var i=cityName.options.length-1;i>=0;i=i-1)
			    {
			    	if(cityName.options[i].value!=-1)
			    		cityName.remove(i);
		    }
			if(object=='ALL' || object=='-1'){
				checkReportItem();
				return true;
			}
			
			if (_respCityZones.result && _respCityZones.data != "") 
			{
				var dataArr = _respCityZones.data.split("|");
					for ( var i = 0; i < dataArr.length; i=i+1) {
						if(object==dataArr[i].split("=")[0])
						{
							var city = dataArr[i].split("=")[1].split(",");
							var opt = new Option('ALL' , 'ALL');
							cityName.options[1] = opt;
								for ( var j = 0; j < city.length; j=j+1) {
									var cityAndCode = city[j].split("-");
									var opt = new Option(cityAndCode[0], cityAndCode[1]);
									cityName.options[j + 2] = opt;		
								}
								break;
						}
					}
			} 
			_id.o("cityNameDiv").style.display='block';
			_id.o("cityNameLabelDiv").style.display='block';
			cityName.selectedIndex=0;
			checkReportItem();
		}
			
			
function createZones(object)
		{
			clearDiv();
			_id.o("dates").innerHTML = "";
			removeElements("zones");
			if(object=='ALL' || object=='-1'){
				checkReportItem();
				return true;
			}
			
			var _respZones = _ajaxCall("getZoneListToDisplay.action?cityName="+object);
			var dataArr = _respZones.data.split("|");
			var parentElement = _id.o("zones");
			for(var count=0; count<dataArr.length;count=count+1)
			{
			    var newCheckBox = document.createElement('input');
			    newCheckBox.type = 'checkbox';
			    newCheckBox.id = 'zoneNamesArray'
			    newCheckBox.name = 'zoneNamesArray'
			    newCheckBox.value = dataArr[count].split("=")[1];
			    var text = document.createTextNode( dataArr[count].split("=")[0] );
			    parentElement.appendChild(newCheckBox);
			    parentElement.appendChild(text);
			}
			checkReportItem();
		}
		
function removeElements(objName)
		{
			var parentElement = _id.o(objName);
			while (parentElement.hasChildNodes()) {
				parentElement.removeChild(parentElement.lastChild);
			}
		}
		
function validateDataGraphicalData()
		{
			
		
			clearDiv();
			var isChecked = false;
			var reportItem;
			var reportType = _id.o("reportType").options[ _id.o("reportType").selectedIndex].value;
		
			if($('input[type=radio]:checked').size() < 1){
				_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.graph.type')+"</font></center>";
				return false;
			}
			if(reportType==-1){
				_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.rep.type')+"</font></center>";
				return false;
			}
			if(_id.o("regionName").options[_id.o("regionName").selectedIndex].value==-1){
				if($('input[type=radio]:checked').val()!='pie'){
				_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.region')+" </font></center>";
				return false;
				}else{
					return true;
				}
			}else if(_id.o("regionName").options[_id.o("regionName").selectedIndex].value!='ALL' && _id.o("cityName").options[_id.o("cityName").selectedIndex].value==-1){
				_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.city')+"</font></center>";
				return false;
			}else if(_id.o("cityName").options[_id.o("cityName").selectedIndex].value!=-1 && _id.o("cityName").options[_id.o("cityName").selectedIndex].value!='ALL'){
				var arrayCheck=_id.n("zoneNamesArray");
				for(var i=0;i<arrayCheck.length;i+=1){
					if(arrayCheck[i].checked){
					isChecked=true;
				}
			}
				if(isChecked)
					_id.o("errorDiv").innerHTML="";
				else{
					_id.o("errorDiv").innerHTML="<center><font color='red'>*"+i18nObj.prop('error.select.atleast.one.zone')+"</font></center>";
					return false;
				}
			}
		return true;
		}

function checkReportItem()
		{
			var object = _id.o("reportItem");
			var reportItem="";
			if(_id.o("reportType").options[_id.o("reportType").selectedIndex].value==1 && $('input[type=radio]:checked').val()=='pie'){reportItem="REGION";} 
			else if(_id.o("regionName").options[_id.o("regionName").selectedIndex].value!='ALL' && _id.o("cityName").options[_id.o("cityName").selectedIndex].value==-1){}
			else if(_id.o("regionName").options[_id.o("regionName").selectedIndex].value=='ALL'){reportItem="REGION";}
			else if(_id.o("cityName").options[_id.o("cityName").selectedIndex].value=='ALL'){reportItem="CITY";}
			else{reportItem="ZONE";}
			object.value = reportItem;
}


function forSubmission(divId){
	var appndParam = appndP();
	_ajxClDv("bo_rep_graphRepOf_actInactInvStatus_reportSearch.action",appndParam,divId)
}


function appndP(){
	var param="?sk=sk";
	var arrInpElem = document.getElementsByTagName('input');
	var arrSelectElem = document.getElementsByTagName('select');
	if(formId!=""){
	arrInpElem = document.getElementById(formId).getElementsByTagName('input');
	arrSelectElem = document.getElementById(formId).getElementsByTagName('select');
	}
		for(var i=0,l=arrInpElem.length;i<l;i++){
			if(arrInpElem[i]!='undefined'&&arrInpElem[i]!=null&&arrInpElem[i].name!='undefined'&&arrInpElem[i].name!=null&&arrInpElem[i].name!=""&&!arrInpElem[i].name.match('action:')){
				if(arrInpElem[i].type=="checkbox"||arrInpElem[i].type=="radio"){
					if(arrInpElem[i].checked==true){
					param=param+"&"+arrInpElem[i].name+"="+arrInpElem[i].value;}
				}else{
				param=param+"&"+arrInpElem[i].name+"="+arrInpElem[i].value;
				}
			}
		}
		for(var i=0,l=arrSelectElem.length;i<l;i++){
		if(arrSelectElem[i]!='undefined'&&arrSelectElem[i]!=null&&arrSelectElem[i].name!='undefined'&&arrSelectElem[i].name!=null){
					param=param+"&"+arrSelectElem[i].name+"="+arrSelectElem[i].value;
				}
			}
	return param;
	}

function _ajxClDv(actionName,param,divId) {
if(validateDataGraphicalData() &&  validateDates()){
	 $.ajax( {
	        type : "POST",
	        url : actionName,
	        data : param,
	        async : false,
	        success : function(result) {
	        // alert(result);
	        if(result.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
	            window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	        }else{
	        	// alert(result);
	        	$("#GraphTable").show();
	        	  $("#"+divId).html(result).hide().show(2000);
	            }
	        
	        }
	    });
	}
   
}
