
		function clearDiv(){
			
			_id.o('down').innerHTML="";
		}
		function fillOptions() {
	var alltag = document.getElementsByTagName("select");
	for (var i = 0; i < alltag.length; i++) {
		var k = 0;
		var tagType = alltag[i].id;
		if (tagType.match("hour")) {
			for (var j = 0; j < 24; j++) {
				if (j < 10) {
					j = "0" + j;
				}
				alltag[i].options[k] = new Option(j, k);
				if (tagType.match("to") && j == 23) {
					alltag[i].options[k].selected = true;
				}
				k++;
			}
		} else {
			if (tagType.match("min") || tagType.match("sec")) {
				for (var m = 0; m <= 59; m++) {
					if (m < 10) {
						m = "0" + m;
					}
					alltag[i].options[k] = new Option(m, k);
					if (tagType.match("to") && m == 59) {
						alltag[i].options[k].selected = true;
					}
					k++;
				}
			}
		}
	}
	getAgentList();
}
		
		/*function setDateField(tvalue) {//alert(tvalue);
		fillOptions();
				document.getElementById("down").innerHTML="";
				if(tvalue=='Today') {
					//alert("inside if"+document.getElementById("date").style.visibility);
					
					//alert("sumit");
					document.getElementById("start_date").value="03-11-2012";
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
		*/
		function getRetailerList(agtId){
			clearDiv();
			if(agtId.value!=-1){
				var _resp=_ajaxCall('bo_rep_quickLedger_RetailerList.action?orgId='+agtId.value);
				//alert(_resp.data);
				var _respData = _resp.data;
						if(_respData != ""){
							var resData = _respData.split(":");
							var retObj = _id.o("retOrgId");
							retObj.options.length = 1;
							for (var i = 0; i < resData.length-1; i = i + 1) {
								var val = resData[i].split("|");
								var opt = new Option(val[0].toUpperCase(), val[1]);
								retObj.options[i + 1] = opt;
							}
						} else if(_respData == ""){
							_id.o('retOrgId').value = -1;
							_id.o('retOrgId').options.length = 1;
						}
				
			}
		}
		
		function getAgentList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			for ( var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
		
		
		function compareTime()
		{
			var agentOrgId = $("#agentOrgId").val();
			if(agentOrgId == -1) {
				$("#agtReport").show() ;
				$("#down").hide() ;
				$("#agtReport").html('<font color="red">Please Select Agent</font>') ;
				return false;
			} else {
				$("#agtReport").hide() ;
			}

			var retOrgId = $("#retOrgId").val();
			if(retOrgId == -1) {
				$("#retReport").show() ;
				$("#down").hide() ;
				$("#retReport").html('<font color="red">Please Select Retailer</font>') ;
				return false;
			} else {
				$("#retReport").hide() ;
			}
			
			var status =  validateReportType();
			if(status == false)
			{
				$("#report").show() ;
				$("#down").hide() ;
				$("#report").html('<font color="red">Please Select Report Type'  +'</font>') ;
			}
			else
			{
				$("#report").hide() ;
		var start_time =$("#start_date").val().replace(/-/g, " ") + " " + $("#from_hour").val()+":"+$("#from_min").val()+":"+$("#from_sec").val();
		var end_time = $("#start_date").val().replace(/-/g, " ") + " " +  $("#to_hour").val()+":"+$("#to_min").val()+":"+$("#to_sec").val() ;
		
		var st = Date.parse(start_time);
		var et = Date.parse(end_time);
		
		if(st > et)
		{
			$("#errorDiv").html('<font color="red">Start Time cannot be greater than End Time</font>') ;
			return false;
		}
		else
		{
			$("#errorDiv").hide() ;
			return compareDateWithArchiveDate() ;
		}}
		//return  validateReportType();
		}
		

		function closeDiv()
		{
			
			var start_date = Date.parse($("#start_date").val()) ;
			var arch_date = Date.parse($("#arch_date").val()) ;
			
			if(arch_date < start_date)
			{
				$("#errorDiv").hide();
			}
		}

		
		function compareDateWithArchiveDate()
		{
			var start_date = Date.parse($("#start_date").val()) ;
			var arch_date = Date.parse($("#arch_date").val()) ;
			
			
			if(start_date <= arch_date)
			{
				$("#down").html('<font color="red">Please select date after '+ $("#arch_date").val() +'</font>') ;
				//$("#errorDiv").show();
				return false ;
			}
			else
			{
				return true ;
			}
			
			
		}
		
		function validateReportType()
		{
			if($("#reportType").val() == '-1')
			{
				
				return false;
			}
			else
			{
				return true;
			}
		}
		
		parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/quickLedgerRep.js,v $'] = '$Id: quickLedgerRep.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';
	
		
