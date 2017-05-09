
		function setAgtName(){
			if(document.getElementById('agtOrgId').value != -1){
				var obj = document.getElementById('agtOrgId').getElementsByTagName('option');
				var agentName = "";
				for(i=0; i<obj.length; i++){
					if(obj[i].value == document.getElementById('agtOrgId').value){
						agentName = obj[i].innerHTML;
					} 
				}
				_id.i('agtName','<input type="hidden" name="agtName" value="'+ agentName+ '" />');
			}
		}
		function validate(){
			var agtOrgId=_id.o('agtOrgId').value;
			var chk=true;
			_id.o('errorDiv').innerHTML="";
			
			//alert(chk);
			if(!chk){ 
				return chk;
			}
			else{
				chk = validateDates();
			}
			return chk;
		}
		
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
				_id.i('repType','<input type="hidden" name="reportType" value="'+ tvalue +'" />');

		}
		
		function validateDates() {
				var isInValid = false;
				var startDate = document.getElementById("start_date").value;
				var endDate =  document.getElementById("end_date").value;	
    			
    			if (startDate == "" || endDate == "") {
					isInValid = true;
					document.getElementById("dates").innerHTML = "<font color = 'red'>Please Enter all the dates</font>";
				} else {
					var stArr = startDate.split('-');
					var endArr = endDate.split('-');
					var tempStDate = stArr[1] + '-' + stArr[0] + '-' + stArr[2]; 
					var tempEndDate = endArr[1] + '-' + endArr[0] + '-' + endArr[2]; 
					if ((Date.parse(tempEndDate) - Date.parse(tempStDate))<0) {
						isInValid = true;
						document.getElementById("dates").innerHTML = "<font color = 'red'>end date should be greater then or equals to start date</font>";          
          	
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
	function getAgentList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agtOrgId");

			for ( var i = 0; i < firstArr.length - 1; i=i+1) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/bo_CRDR_rep.js,v $'] = '$Id: bo_CRDR_rep.js,v 1.2 2016/10/31 09:46:26 neeraj Exp $';

