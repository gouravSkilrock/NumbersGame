var i18nObj = parent.frames[0].i18Obj;

function getAgentList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("agentOrgId");
			for (var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
			}
		
			function fillDrawName(val, divId){
				_id.o('drawNameDiv').style.display = 'none';
				_id.i('down','');
				if(val != -1){
					_resp = _ajaxCall("fetchDrawName.action?gameNo="+val);
					_respData=_resp.data;
					// alert(_id.o(divId));
					var drawNameObj = _id.o(divId);
					drawNameObj.options.length=1;
					var data=_respData.split(",");
					var len = data.length;
					// alert(len);
					if(len > 0){
						for(var i=0; i<data.length; i++){
							var val1 = data[i];
							var opt = new Option(val1, val1);
							drawNameObj.options[i+1] = opt;
						}
						if(data[0] != ''){
							_id.o('drawNameDiv').style.display = 'block';
						}
					}
				}
			}
			
			function validateDates() {
				
					var gameNo =_id.o("gameNo");
					if(gameNo.value == "-1"){
						_id.i("error", i18nObj.prop('error.select.game.name')+".", "e");
						return false;
						}
					_id.i("error", "");
		
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
			

			function validateDraw(){
				
					var drawName =$("#drawName").val();
					var gameNo =$("#gameNo").val();
					if(gameNo == "-1"){
						_id.i("error", "Please Select Game.", "e");
						return false;
					}
					if(drawName == "-1"){
						_id.i("error", "Please Select Draw Name.", "e");
						return false;
					}
					_id.i("error", "");
				return true;			
			}
			
			
			
			function clearDivs()
			{
				_id.i('down','');
				
				if(_id.o('down1')){
					_id.i('down1','');
				}
			}

			// bo_dg_rep_numberBetAmount_Menu.jsp

			function fetchDrawNames() {
				
				if(_id.o('drawNameDiv')!=null && _id.o('drawDateTime')!=null && _id.o('down')!=null){
				_id.o('drawNameDiv').style.display = 'none';
				_id.o('drawDateTime').style.display = 'none';
				}

				clearDivs();
							var endDateofDraw=null;
							var gameNo = _id.v('gameNo');
							var dateOfDraw = _id.o('dateOfDraw')==null?_id.v('start_date'):_id.v('dateOfDraw');
							if(_id.o("end_date")!=null)
							endDateofDraw= _id.v("end_date");
							var drawName = _id.o('drawName');
							drawName.options.length = 1;
							if(gameNo!=-1){
							var _resp = _ajaxCall("fetchNameOfDraw.action?gameNo=" + gameNo
									+ "&startDate=" + dateOfDraw + "&endDate=" + endDateofDraw);
							if (_resp.result && _resp.data != "") {
								var dataArr = _resp.data.split(",");
								for ( var i = 0; i < dataArr.length; i++) {
									var opt = new Option(dataArr[i], dataArr[i]);
									drawName.options[i + 1] = opt;
								}
							}
							
							if(_id.o('drawNameDiv')!=null && _resp.data != ""){
								_id.o('drawNameDiv').style.display = 'block';
							}
							if(_id.o('drawNameCol').value!=null && _resp.data != ""){
								_id.o('drawNameCol').value = _id.o('drawName').value;
							}
					
						}
			}

			function validateFormEntriesNumberBetAmount() {
				
				var gameNo =_id.o("gameNo");
						if(gameNo.value == "-1"){
							_id.i("error", i18nObj.prop('error.select.game.name'), "e");
							return false;
							}
				var drawName = _id.o("drawName");
						if (drawName.value == "-1") {
									_id.i("error", i18nObj.prop('error.select.draw.name'), "e");
					return false;
									} 

			_id.i("error", "");
			return true;
			}

			// bo_rep_draw_analysisReportRetailerWise_Menu.jsp
			function fillDrawName(val, divId , divId1){
				
				
				_id.o('drawNameDiv').style.display = 'none';
				_id.o('drawDateTime').style.display = 'none';
				_id.i('down','');
				if(val != -1){
					_resp = _ajaxCall("fetchDrawName.action?gameNo="+val);
					_respData=_resp.data;
					var drawNameObj = _id.o(divId);
					drawNameObj.options.length=1;
					var data=_respData.split(",");
					var len = data.length;
					if(len > 0){
						for(var i=0; i<data.length; i++){
							var val1 = data[i];
							var opt = new Option(val1, val1);
							drawNameObj.options[i+1] = opt;
						}
						if(data[0] != ''){
							_id.o('drawNameDiv').style.display = 'block';
						}
						else
						{
						// alert('hello');
						fillDrawTime1(val, divId1)
						}
					}
				}
			}




			function fillDrawTime1(val, divId1)
			{
			_id.i('down','');
			_resp = _ajaxCall("fetchDrawTime.action?gameNo="+val);
			_respData=_resp.data;
			var drawNameObj = _id.o(divId1);
			drawNameObj.options.length=1;
			var data=_respData.split(",");
			var len = data.length;
			if(len > 0){
					for(var i=0; i<data.length; i++){
							var val1 = data[i];
							var opt = new Option(val1, val1);
							drawNameObj.options[i+1] = opt;
					}
				if(data[0] != ''){
							_id.o('drawDateTime').style.display = 'block';
				}
				}
			}


			function validateFormEntriesAnalysisReportRetailerWise() {

			var gameNo =_id.o("gameNo");
			if(gameNo.value == "-1"){
			_id.i("error", i18nObj.prop('error.select.game.name'), "e");
			return false;
			}
			var drawTime = _id.o("drawTime");
			var drawName = _id.o("drawName");
			if (drawTime.value == "-1" && drawName.value == "-1") {
			_id.i("error", i18nObj.prop('error.select.draw.time.or.id'), "e");
			return false;
			} 

			_id.i("error", "");
			return true;
			}


			// bo_rep_drawAnalysis15minReportMenu.jsp

			/*
			 * function getAgentList() { var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			 * var firstArr = text.data.split(":"); var retObj =
			 * _id.o("agentOrgId"); for (var i = 0; i < firstArr.length - 1;
			 * i++) { var retNameArray = firstArr[i].split("|"); var opt = new
			 * Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			 * retObj.options[i + 1] = opt; } }
			 */

				function fillDrawName(val, divId){
					_id.o('drawNameDiv').style.display = 'none';
					_id.i('down','');
					if(val != -1){
						_resp = _ajaxCall("fetchDrawName.action?gameNo="+val);
						_respData=_resp.data;
						// alert(_id.o(divId));
						var drawNameObj = _id.o(divId);
						drawNameObj.options.length=1;
						var data=_respData.split(",");
						var len = data.length;
						// alert(len);
						if(len > 0){
							for(var i=0; i<data.length; i++){
								var val1 = data[i];
								var opt = new Option(val1, val1);
								drawNameObj.options[i+1] = opt;
							}
							if(data[0] != ''){
								_id.o('drawNameDiv').style.display = 'block';
							}
						}
					}
				}
				
				function validateDates() {
					if(_id.n('gameNo')[0].value == -1){
						alert('Please Select Game');			
						return false;
					}
					var isInValid = false;
					var startDate = _id.o("dateOfDraw").value;
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
			function fetchDrawName(val,divId,val1){
				 	alert(val+divId+val1);
					var gameNo = val1;
				
					var dateOfDraw = val;
					var drawName = _id.o(divId);
					drawName.options.length = 1;
					var _resp = _ajaxCall("fetchNameOfDraw.action?gameNo=" + gameNo + "&dateOfDraw=" + dateOfDraw);
					if (_resp.result && _resp.data != "") {
						var dataArr = _resp.data.split(",");
						for (var i = 0; i < dataArr.length; i++) {
							var opt = new Option(dataArr[i], dataArr[i]);
							drawName.options[i + 1] = opt;
						}
					}
					
				}	
				function validateFormEntriesAnalysis15minReport() {

					var gameNo =_id.o("gameNo");
						if(gameNo.value == "-1"){
							_id.i("error", i18nObj.prop('error.select.game.name'), "e");
							return false;
							}

				_id.i("error", "");
				return true;
			}
				
				
				
				// bo_dg_rep_drawMachineResultReport_Menu.jsp
				
				
				function fillDrawName(val, divId){
					_id.o('drawNameDiv').style.display = 'none';
					_id.o('drawNameCol').value = null;
					_id.i('down','');
					if(val != -1){
						_resp = _ajaxCall("fetchDrawName.action?gameNo="+val);
						_respData=_resp.data;
						// alert(_id.o(divId));
						var drawNameObj = _id.o(divId);
						drawNameObj.options.length=1;
						var data=_respData.split(",");
						var len = data.length;
						// alert(len);
						if(len > 0){
							for(var i=0; i<data.length; i++){
								var val1 = data[i];
								var opt = new Option(val1, val1);
								drawNameObj.options[i+1] = opt;
							}
							if(data[0] != ''){
								_id.o('drawNameDiv').style.display = 'block';
								_id.o('drawNameCol').value = _id.o('drawName').value;
							}
						}
					}
				}
			
				function validateDates() {
					clearDivs();
					var gameNo =_id.o("gameNo");
						if(gameNo.value == "-1"){
							_id.i("error", i18nObj.prop('error.select.game.name'), "e");
							return false;
							}
						_id.i("error", "");		
					var isInValid = false;
					var startDate = _id.o("dateOfDraw")==null?_id.o("start_date").value:_id.o("dateOfDraw").value;
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
				
				function validateDatesForm() {
					clearDivs();	
					var isInValid = false;
					var startDate = _id.o("start_date")==null?_id.o("start_date").value:_id.o("start_date").value;
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
					var gameNo =_id.o("gameNo");
					if(gameNo.value == "-1"){
						_id.i("error", i18nObj.prop('error.select.game.name'), "e");
						return false;
						}
					_id.i("error", "");	
					return true;			
				}
								
				parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawWise.js,v $'] = '$Id: bo_rep_drawWise.js,v 1.3 2016/10/31 09:46:59 neeraj Exp $';

