 var i18nObj = parent.frames[0].i18Obj; 
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
		function clearDiv(){
			_id.i("down","");
			_id.i("result","");
			_id.i("dates","");
		}
		
		function showAgents(val){
		// alert(val+' '+_id.o('agtDiv'));
		 if(val=='Retailer Wise'){
		 	_id.o('agtDiv').style.display='block';
		 }else{
		 	_id.o('agtDiv').style.display='none';
		 }
		}	
		function getAgentList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			retObj.options[1] = new Option('All', -2);
			for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 2] = opt;
			}
		}	
		
		function validateData(){
		if(_id.o('agtDiv').style.display=='block' && _id.o('agentOrgId').value==-1)
		{
		alert('Please select Agent');
		return false;
		}else
		return true;
				}
		function validateDates() {
				var isInValid = false;
				var startDate = document.getElementById("start_date").value;
				var endDate =  document.getElementById("end_date").value;
				if(startDate==null ||endDate==null ){
				return false;
				}
				if(startDate == null || endDate == null){
					return false;
				}
    			if (startDate == "" || endDate == "") {
					isInValid = true;
					document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.dates.empty')+"</font>";
				} else {
					var stArr = startDate.split('-');
					var endArr = endDate.split('-');
					var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
					var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
					if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
						isInValid = true;
						document.getElementById("dates").innerHTML = "<font color = 'red'>"+i18nObj.prop('error.enddate.gt.strtdate')+"</font>";          
          				return false;
					}
				}
				if (isInValid) {
					if(document.getElementsByName('totaltime')[0].value != 'Date Wise'){
						return true;
					}
					return false;
				}
				return true;
		}			
		