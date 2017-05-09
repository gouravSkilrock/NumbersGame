var i18nOb = i18Obj;

$(document)
		.ready(
				function() {

					var url = path
					+ "/com/skilrock/lms/web/userMgmt/getCountryListWithCode.action";
			$.get(url, function(data, status) {
				fillCountryList(data, status);
				
			}
			
			);
			
			
			$('#country').change(function() {

				getStateListWithCode($('#country').val());

			}

			);
					
					
					
					$('#gameId').change(function() {

						fetchDrawNames();

					}

					);
					
					$('#startDate').change(function() {
						
						fetchDrawNames();
				}
					);

				$('#endDate').change(function() {
					fetchDrawNames();

				}

				);
				
				$( "input[name$='repType']" ).change(function() {
					if($("input[type='radio']:checked").val()=='DW'){
						fetchDrawNames();
					}else{
						
						 hideDivs();
					}
				

				}

				);
				
			
					$('#repForm').submit(function() {

						return validateDates();

					}

					);
						
				
				}

		);
	


		
			
		

	

			function fetchDrawNames() {
				
				if(_id.o('drawNameDiv')!=null && _id.o('drawDateTime')!=null && _id.o('down')!=null){
				_id.o('drawNameDiv').style.display = 'none';
				_id.o('drawDateTime').style.display = 'none';
				}
				_id.i('down','');
				_id.i('result','');
				if($("input[type='radio']:checked").val()=='DW'){
				
					var endDateofDraw=null;
					var gameNo = _id.v('gameId');
					var dateOfDraw = _id.o('startDate')==null?_id.v('startDate'):_id.v('startDate');
					if(_id.o("endDate")!=null)
					endDateofDraw= _id.v("endDate");
					var drawName = _id.o('drawName');
					drawName.options.length = 1;
					if(gameNo!=-1){
						
					var _resp = _ajaxCall(projectName+"/com/skilrock/lms/web/drawGames/reportsMgmt/fetchNameOfDraw.action?gameNo=" + gameNo+ "&startDate=" + dateOfDraw + "&endDate=" + endDateofDraw);
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
							
			}

			
	function validateDates() {	
		
		_id.i('down','');
		_id.i('result','');
					var gameNo =_id.o("gameId");
						if(gameNo.value == "-1"){
							_id.i("error", i18nOb.prop('error.select.game.name'), "e");
							return false;
							}
						_id.i("error", "");		
					var isInValid = false;
					var startDate = _id.o("startDate")==null?_id.o("startDate").value:_id.o("startDate").value;
					var endDate =  _id.o("endDate").value;	
					if (startDate == "" || endDate == "") {
						isInValid = true;
						_id.o("dates").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.dates.empty')+"</font>";			           
					} else {
						if (endDate < startDate) {
							isInValid = true;
							_id.o("dates").innerHTML = "<font color = 'red'>"+i18nOb.prop('error.enddate.gt.strtdate')+"</font>";          
				        }
					}				
					if (isInValid) {				
						return false;
					}
					_id.o("dates").innerHTML = "";
					return true;			
				}
	function fillCountryList(data, status) {
		if (data != "" && status == "success") {

			var options = '';
			$('#country').append($(options));
			var list = data.split("|");
			// alert("gameArr = "+gameArr.length+" data = "+gameArr);
			for ( var i = 0; i < list.length - 1; i = i + 1) {
				var listArr = list[i].split(":");
				var gkey = listArr[0];
				var dvalue = listArr[1];
				options += '<option value="' + gkey + '">' + dvalue + '</option>';
			}
			$('#country').append($(options));
			var firstOption = list[0].split(":")[0];
			$('#country').val(firstOption);
			getStateListWithCode(firstOption);
		}
	}

	function getStateListWithCode(countryCode) {

		$('#stateCode').html("<option value='-1'>"+i18nOb.prop(label.ALL)+"</option>");
		if (countryCode == "-1") {

			$('#stateCode').val("-1");
		} else {

			var url = path
					+ "/com/skilrock/lms/web/userMgmt/getStateListWithCode.action?country="
					+ countryCode;
			$.get(url, function(data, status) {
				fillStateList(data, status);
			}

			);

		}

	}

	function fillStateList(data, status) {
		if (data != "" && status == "success") {

			var options = '';
			$('#stateCode').append($(options));
			var list = data.split("|");
			// alert("gameArr = "+gameArr.length+" data = "+gameArr);
			for ( var i = 0; i < list.length - 1; i = i + 1) {
				var listArr = list[i].split(":");
				var gkey = listArr[0];
				var dvalue = listArr[1];
				options += '<option value="' + gkey + '">' + dvalue + '</option>';
			}
			$('#stateCode').append($(options));
			
		
		}
	}	
function hideDivs(){
	_id.o('down').innerHTML ="";
	_id.o('result').innerHTML ="";
	$( "div[id$='drawNameDiv']" ).hide();
	
}
function getData() {
	var tblData = document.getElementById("tableDataDiv").innerHTML;
	tblData = tblData.replace(document.getElementById("sortRow").innerHTML,"");
	var hrefArray =document.getElementsByName("href_m");
	for(var i=0;i<hrefArray.length;i=i+1){
		tblData = tblData.replace(hrefArray[i].innerHTML,"");
		
		}
	document.getElementById('tableValue').value = tblData;
	
}
	
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/bo_rep_stateWise.js,v $'] = '$Id: bo_rep_stateWise.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';

