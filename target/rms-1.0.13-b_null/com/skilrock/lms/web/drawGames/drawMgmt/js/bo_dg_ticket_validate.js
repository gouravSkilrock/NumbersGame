var i18nObj = parent.frames[0].i18Obj;
var path = projectName;

function isFilled()
{
	var returnType = true;
	var ticketNumArr = _id.n("ticketNumArr");
	_id.i('down','',this);
	_id.i('tktErr','',this);
	for (var i = 0; i < ticketNumArr.length; i+=1) 
	{
		var tktnbr = ticketNumArr[i].value.length;
		if(ticketNumArr[i].value == ""){
			returnType = false;
			_id.i('tktErr',i18nObj('error.remove.unused.entry'),this);
			break;
		}else if(tktnbr==18 || tktnbr==20){}
		else{
			returnType = false;
			_id.i('down','',this);
			_id.i('tktErr',i18nObj('error.enter.correct.tkt.nbr'),this);
			}
		}
	return returnType;
	}
	
function checkNum(e)
{
	e = e || window.event;
	if((e.which >=0 && e.which <=31) || (e.which >= 48 && e.which <= 57))
		return true;
	alert("Only digits are allowed");
	return false;
}

function moveFocusOnSubmit(value)
{
	if(value.length == 20){
		_id.o('search').focus();
	}
	return true;
}

function addTickets(tableID)
{
	_id.i('down','',this);
	_id.i('tktErr','',this);
	var table = _id.o(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var colCount = table.rows[0].cells.length;
	for(var i=0; i<colCount; i+=1) {
		var newcell = row.insertCell(i);
		newcell.innerHTML = table.rows[0].cells[i].innerHTML;
		switch(newcell.childNodes[0].type) {
        	case "text":
                newcell.childNodes[0].value = "";
                break;
        	case "checkbox":
                newcell.childNodes[0].checked = false;
                break;
        	case "select-one":
                newcell.childNodes[0].selectedIndex = 0;
                break;
		}
	}
}

function deleteTickets(tableID)
{
	_id.i('down','',this);
	_id.i('tktErr','',this);
	var table = _id.o(tableID);
    var rowCount = table.rows.length;
    var isAnySelected = false;
          for(var i=0; i<rowCount; i += 1) {
              var row = table.rows[i];
              var chkbox = row.cells[0].childNodes[0];
              if(null != chkbox && true == chkbox.checked) {
            	  isAnySelected = true;
                  if(rowCount <= 1) {
                      alert(i18nObj('error.atleast.one.tkt.required'));
                      break;
                  }
                  table.deleteRow(i);
                  rowCount -= 1;
                  i-= 1;
              }
          }
          if(!isAnySelected && rowCount>1){
        	  alert(i18nObj('error.select.atleast.one.tkt'));
          }
}

function getRetailerUserList(agentOrgId) 
{
	if(agentOrgId != -1){
	var text = _ajaxCall("getRetailerUserNameList.action?orgId=" + agentOrgId);
	var firstArr = text.data.split(":");
	var retObj = _id.o("retOrgId");
	_id.o("retOrgId").options.length = 1;
	for (var i = 0; i < firstArr.length - 1; i+=1) 
	{
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
	}   
}

function getAgentList() 
 {
		var text = _ajaxCall(path+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
		var firstArr = text.data.split(":");
		var retObj = _id.o("agentOrgId");
		for (var i = 0; i < firstArr.length - 1; i+=1) 
		{
			var retNameArray = firstArr[i].split("|");
			var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
			retObj.options[i + 1] = opt;
		}
}
		
function validateDataUnBlock()
{
	_id.i("errorDiv", "", this);
	_id.i("down", "", this);
	_id.i("down1", "", this);
	var value = $('input[type=radio]:checked').val();
	var tktnbr = _id.o("ticketNo").value;
	if(_id.o("gameNo").value == '-1')
	{
		_id.i("errorDiv", "*"+i18nObj.prop('error.select.game.name'), this);
		return false;
	}
	if(value == '1'){
		if(tktnbr != "" && (tktnbr.length == 18 || tktnbr.length == 20)){
			return true;
		}else{
			_id.i("errorDiv", "*"+i18nObj.prop('error.enter.tkt.proper'), this);
			return false;
		}
	}
	return true;
}

function toggleAllChecks()
{
	var isChecked = _id.o("checkAll").checked;
	var ticketNumArr = _id.n("ticketNumArr");
	for (var i = 0; i < ticketNumArr.length; i+=1) 
	{
		var retNameArray = ticketNumArr[i].checked = isChecked;
	}
}

function validateUnblockTicket()
{
	var ticketNumArr = _id.n("ticketNumArr");
	var returnValue = true;
	var isDataSelected = false; 
	_id.i("ErrMsgDivChecks", "", this);
	var len = ticketNumArr.length;
		for (var i = 0; i < len; i+=1) 
		{
			var reason = _id.o(i+"reasons").value;
			if(ticketNumArr[i].checked){
				isDataSelected = true;
				if(reason == "-1"){
					_id.i("ErrMsgDivChecks", "*"+i18nObj.prop('error.select.status.fr.selected.tkt'), this);
					returnValue = false;
					break;
				}
			}else if(reason != "-1"){
				_id.i("ErrMsgDivChecks", "*"+i18nObj.prop('error.deselect.unwanted.status'), true);
				returnValue = false;
				break;
		}
	}

	if(!isDataSelected){
		_id.i("ErrMsgDivChecks", "*"+i18nObj.prop('error.select.atleast.one.tkt'), true);
		returnValue = false;
	}
	return returnValue;
}

function onlyIntegers(evt) 
{
	var code = (document.all) ? event.keyCode:evt.which;
	var e = event || evt;
	if ((code >= 48 && code < 58) || (code >= 96 && code < 105) || (code == 46 || code == 8 || code == 37 || code == 39 || code == 45) || ( code == 9 || code == 16 || code == 35 || code == 36 )) {
		return true;
	}
	return false;
}

function ActiveInactDivs()
{
	var value = $('input[type=radio]:checked').val();
	_id.i("errorDiv", "", this);
	_id.i("down", "", this);
	_id.i("down1", "", this);
	_id.o("ticketNo").value = "";
	if(value == '1'){
		_id.o("tktLable").style.display = "block";
		_id.o("tktValue").style.display = "block";
		_id.o("agtLable").style.display = "none";
		_id.o("agtValue").style.display = "none";
		_id.o("retLable").style.display = "none";
		_id.o("retValue").style.display = "none";
		_id.o("criLable").style.display = "none";
		_id.o("criValue").style.display = "none";
	}else{
		_id.o("blockUnblockTicket").reset();
		_id.o("retOrgId").options.length = 1;
		_id.o("tktLable").style.display = 'none';
		_id.o("tktValue").style.display = 'none';
		_id.o("agtLable").style.display = 'block';
		_id.o("agtValue").style.display = 'block';
		_id.o("retLable").style.display = 'block';
		_id.o("retValue").style.display = 'block';
		_id.o("criLable").style.display = "block";
		_id.o("criValue").style.display = "block";
	}
	
}



function getTicketWiseData(id){
	var div  = "down1";
	_id.i(div,'',this);	
	var varArrya = id.split('--');
	var start_date = varArrya[0];
	var drawName = varArrya[1];
	var gameId = _id.o('gameNo')[_id.o('gameNo').selectedIndex].value;
	var orgId = _id.o('agentOrgId')[_id.o('agentOrgId').selectedIndex].value;
	var drawStatus = (_id.o(start_date).innerHTML).trim();
	var param="?mm=mm&gameId="+gameId+"&agentOrgId="+orgId+"&start_date="+start_date+"&drawName="+drawName+"&status="+drawStatus;
	forSubmission(div , param);
}

function forSubmission(divId, appndParam){
	_ajxClDv("bo_rep_drawwise_win_tickets_details.action",appndParam,divId)
}

function _ajxClDv(actionName,param,divId) {
	 $.ajax( {
	        type : "POST",
	        url : actionName,
	        data : param,
	        async : false,
	        success : function(result) {
	        if(result.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
	            window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	        }else{
	        	  $("#"+divId).html(result).hide().show(2000);
	        	  sortables_init();
	        	  _id.o("down2").style.display = "none";
	            }
	        
	        }
	    });
}

function getData() 
{
	var tblData = _id.o("tableDataDiv").innerHTML;
	tblData = tblData.replace(_id.o("sortRow").innerHTML,"");
	_id.o('tableValue').value = tblData;
}

function _validatorMerchantPwtTicket(){
	var isValid = false;
	var ticketNbr=document.getElementById('ticketNumber');
	_id.o('down').innerHTML="";
	_id.o('tktNbrErrMsg').innerHTML ="";
	if(ticketNbr == null || ticketNbr == undefined){
		_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
		_id.o('ticketNumber').focus();
		return false;
	}
	 
	if(ticketNbr.value=="" || ticketNbr.value=="0000000000000000" ){
		_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
		ticketNbr.focus();
		return false;
	}
	if(ticketNbr.value.length != 15 && ticketNbr.value.length != 18 ){
		_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
		ticketNbr.focus();
		return false;
	}else{
		var str = (ticketNbr.value).toString();
		var patt1 = /^[0-9]+$/;	
		if(!(patt1.test(str))){
			_id.o('tktNbrErrMsg').innerHTML = "<font color='red'>"+i18nObj.prop('error.enter.valid.tkt.nbr')+"</font>";
			ticketNbr.focus();
			return false;
		}
		return true;
	}
	return true;
	
}

function moveFocusOnTicketSubmit(value){
	if(value.length == 18 || value.length == 15){
		_id.o('search').focus();
	}
	return true;
}
function removeTickets(tableID){
	var flag=false;
	$($(":checkbox")).each(function(){
		if($(this).prop('checked')==true){
		   flag=true;
		   if($("#"+tableID).find("tr").length<=1){
			   alert("Atleast one ticket is required");
		   }else{
			   $(this).parent().parent().remove();
		   }
		}
	});
	if(flag==false){
		alert("Plase select atleast one ticket");
	}
}
function isTicketSelected(){
	var flag=false;
	var flag1=true;
	var flag2=true;
	var checkBoxArr=$(":checkbox");
	var ticketNumArr=new Array();
	var j=0;
	for(var i=0;i<checkBoxArr.length;i++)
	{
		if($(checkBoxArr[i]).prop('checked')==true){
			flag=true;
			if($(checkBoxArr[i]).parent().next().find(":text").val()==""){
				$("#down").html("");
				$("#tktErr").html("<font color='red'>"+i18nObj.prop('error.remove.unused.entry')+"</font>");
				return false;
			}
			else{
				ticketNumArr[j]=$(checkBoxArr[i]).parent().next().find(":text").val();
				j++;
			}
		}
	}
	if(!flag){
		$("#down").html("");
		$("#tktErr").html("<font color='red'>Plase select atleast one ticket</font>");
		return false;
	}
    for(var i=0;i<ticketNumArr.length;i++){
    	if(ticketNumArr[i].length!=18 && ticketNumArr[i].length!=20){
       	 $("#tktErr").html("<font color='red'>"+i18nObj.prop('error.enter.correct.tkt.nbr')+"</font>");
       	 $("#down").html("");	
       	 return false;
       }
    	$("#tktErr").html("");
     }  
    $("#tktErr").html("");
 	return true;
}

