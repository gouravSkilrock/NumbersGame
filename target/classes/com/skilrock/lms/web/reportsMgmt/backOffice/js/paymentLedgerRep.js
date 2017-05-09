var i18nObj = parent.frames[0].i18Obj;
		
		function getAgentList() {
			var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
			var firstArr = text.data.split(":");
			var retObj = _id.o("orgName");
			for ( var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
	
			function setDateField(tvalue) {//alert(tvalue);
					document.getElementById("down").innerHTML="";
					if(tvalue=='Date Wise') {
						//alert("inside if"+document.getElementById("date").style.visibility);
						document.getElementById("startId").style.display="block";
						document.getElementById("endId").style.display="block";
					}				
					else{ 
						//alert('--in else'+tvalue);
						if(tvalue=='Current Day') {//alert();
						//alert('<s:property value="#session.presentDate"/>');
						_id.o("start_date").value=_id.o("curDate").value;
						_id.o("end_date").value=_id.o("curDate").value;
						document.getElementById("startId").style.display="none";
						document.getElementById("endId").style.display="none";
						}
						else {
							document.getElementById("startId").style.display="none";
							document.getElementById("endId").style.display="none";
						}
					}
			}
			function clearDiv(){
				_id.i("down","");
				_id.i("result","");
				return verifyAgent();
				}
				
				
			function calcTotal(){
		var table = document.getElementById("expandTable");
		var rowCount = table.rows.length;
		var colCount=table.rows[3].getElementsByTagName('td').length;
		var headCount=table.rows[3].getElementsByTagName('th').length;
		for(var c=1;c<colCount+headCount;c++){
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

	function verifyAgent(){
		if(_id.o('orgName').value==-1){
			alert(i18nObj.prop('error.pls.select.company.first'));
			_id.o('orgName').focus();
			return false;		
		}
		return true;
	}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/paymentLedgerRep.js,v $'] = '$Id: paymentLedgerRep.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';
		
