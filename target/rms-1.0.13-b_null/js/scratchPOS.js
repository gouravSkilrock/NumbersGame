var i18nObj = parent.frames[0].i18Obj;	
	var bookNbr2 = [];
	var upCurrRem1 = [];
	var gameName1 =[];
	var tktInBook1=[];
	var currRem1=[];
	var gameSelectedArr=[];
	var userActionListArray=["ret_im_activate_books_Menu","ticketAndVirnNumberVerify","ret_im_soldTktEntry_Menu","ret_im_sale_ticket","ret_om_bookRecieveRegistration_Menu","ret_om_quickOrder_Menu","ret_om_processOrdReq_Menu"];
	
$(document).ready(function(){
	privilegeList();
	setSideMenuFunctionality();
	setSideMenu();
	prepareCalender();
});



/**
 * on keyUp on input fields
 */

$(document).on("keyup","#saleTktNbr",function(e){
	if(e.which != 37 && e.which != 39  ){
		this.value = this.value.replace(/[^0-9\-]/g,'');
	}
});

$(document).on("keyup","#bookNumber,#winVirnNbr,#winTktNbr",function(e){
	
	  e.preventDefault();
	
	if(e.which != 37 && e.which != 39 ){
	this.value = this.value.replace(/[^0-9\-]/g,'');
	}
});

/**
 * on keyDown on input fields
 */

$(document).on("keydown",".updBooks",function(){
	var id = $(this).siblings().attr('id');
	if($("#"+id).html()!=""){
	$("#"+id).html("");
     }
});

$(document).on("keydown","#bookNumber",function(){
	if($("#bookActivationSpan").html()!=""){
		$("#bookActivationSpan").html("");
	}
});
$(document).on("keydown","#winVirnNbr",function(){
	if($("#winVirnErrSpan").html()!=""){
		$("#winVirnErrSpan").html("");
	}
});

$(document).on("keydown","#winTktNbr",function(){
	if($("#winTktErrSpan").html()!=""){
		$("#winTktErrSpan").html("");
	}
});
$(document).on("keydown","#challanId",function(){
	if($("#chlErrMsg").html()!=""){
		$("#chlErrMsg").html("");
	}
});
$(document).on("keydown","#saleTktNbr",function(){
	if($("#saleTktMsg").html()!=""){
		$("#saleTktMsg").html("");
	}
});

$(document).on("keydown",".noOfBooks",function(e){
	if($(this).parent().parent().parent().children().eq(1).children().children().val()==-1){
		e.preventDefault();
		$(this).val('');
	}else{
		$(this).parent().parent().parent().children().eq(3).children().html('');
	}
});


/**
 * Sell ticket submit starts
 */
$(document).on("click","#saleTktSubmit",function(){
	var tktNbr=$.trim($("#saleTktNbr").val());
	if(tktNbr==""){
		$("#saleTktMsg").html(i18nObj.prop('error.msg.enter.ticket.no'));	
		return false;
	}
	var reqData=JSON.stringify({
			"ticketNbr": tktNbr.split('-').join('')
	});
	var actionURL=projectName+'/com/skilrock/lms/web/scratchService/inventoryMgmt/ret_im_sale_ticket.action';
	var respData = _ajaxCallJson(actionURL,"json="+reqData);
	if(respData.responseCode==0){
		clearDiv();
		var s='';
		 s+="<div class='detail-con'><span>"+i18nObj.prop('label.scratch.sell.status')+" </span><span >"+respData.responseMsg+"</span></div>" ;
		 $('#winSuccessData').append(s);
		 $('#winSuccessBox').css('display','block');
		 $('#winSuccessImg').css('display','block');
		 $('#winSuccessMsg').css('display','block');
		 $('#saleTicketForm').css('display','none');
	}else{
		$("#erroMsg").html(respData.responseMsg);
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	}
	
});


/**
 * Quick order starts
 */

$(document).on("click","#qOrder",function(){
	$("#quickOrderDataDiv").html('');
	var resp  = _ajaxCallTextData(projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/getGameNameListForRet.action?gameStatus=OPEN");
	var data=resp.split(',');
	var selectDiv='';
	var optionDiv='<option value="-1">'+i18nObj.prop('label.scratch.option.select.game')+'</option>';
	$.each(data,function(){
		var key=this.split("=")[0];
		var value=this.split("=")[1];
		optionDiv+='<option value='+key+'>'+value+'</option>';
	});
	var i=1;
	selectDiv+='<ul>';
	selectDiv+='<li class="clearFix quickOrderHead">';
	selectDiv+='<div class="srNo"><span>'+i18nObj.prop('label.scratch.serial.no')+'</span></div>';
	selectDiv+='<div class="selectGame">';
	selectDiv+='<div class="date-input">';
	selectDiv+=i18nObj.prop('label.scratch.game.name');
	selectDiv+='</div>';
	selectDiv+='</div>';
	
	selectDiv+='<div class="bookOrder">';
	selectDiv+=i18nObj.prop('label.scratch.no.of.books.to.order');
	selectDiv+='</div>';
	
	selectDiv+='<div class="remarksMsg">';
	selectDiv+='<span>'+i18nObj.prop('label.scratch.remarks')+'</span>';
	selectDiv+='</div>';
	selectDiv+='</li>';
	$.each(data,function(){
		selectDiv+='<li class="clearFix quickOrderbody">';
		selectDiv+='<div class="srNo"><span>'+i+++'</span></div>';
		selectDiv+='<div class="selectGame">';
		selectDiv+='<div class="date-input">';
		selectDiv+='<select class="drop-dowon qOrder">';
		selectDiv+=optionDiv;
		selectDiv+='</select>';
		selectDiv+='</div></div>';
		selectDiv+='<div class="bookOrder">';
		selectDiv+='<div class="date-input">';
		selectDiv+='<input type="text" class="date-box noOfBooks" maxlength=4>';
		selectDiv+='</div></div>';
		selectDiv+='<div class="remarksMsgError">';
		selectDiv+='<span></span>';
		selectDiv+='</div>';
		selectDiv+='</li>'; 
	});
	selectDiv+='</ul>';
	selectDiv+='<div class="submit-btn">';
	selectDiv+='<input type="button" id="qOrderBtn" value="'+i18nObj.prop('btn.scratch.generate.order')+'">';
	selectDiv+='</div>';
	$("#quickOrderDataDiv").append(selectDiv);
});


/**
 * on Sumbit of Quick Order
 */

$(document).on("click","#qOrderBtn",function(e){
	gameSelectedArr=[];
	var gameName=[];
	var noOfBooks=[];
	var flag=true;
	var positiveBook = true;
	$(".quickOrderbody").each(function(){
		if($(this).children().eq(1).children().children().val() !=-1 && $.trim($(this).children().eq(2).children().children().val())!=""){
			$(this).children().eq(3).children().html('');
			gameName.push($(this).children().eq(1).children().children().val());
			if($.trim($(this).children().eq(2).children().children().val())<0){
				positiveBook=false;
			}
			noOfBooks.push($.trim($(this).children().eq(2).children().children().val()));
		}else if($(this).children().eq(1).children().children().val() !=-1){
			flag=false;
			$(this).children().eq(3).children().html(i18nObj.prop('error.msg.enter.no.of.books.to.order'));
		}
	});
	if(flag  && gameName.length > 0 && positiveBook){
		var resp  = _ajaxCallJson(projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/ret_om_quickOrder_Save.action?gameName="+gameName+"&noOfBooks="+noOfBooks+"");
		if(resp.responseCode==0){
			var s='';
			 s+="<div class='detail-con'><span>"+i18nObj.prop('error.msg.select.atleast.one.game')+"</span><span >"+resp.responseMsg+"</span></div>" ;
			 $('#winSuccessData').append(s);
			 $('#winSuccessBox').css('display','block');
			 $('#winSuccessImg').css('display','block');
			 $('#winSuccessMsg').css('display','block');
			 $('#quickOrderForm').css('display','none');
		}else{
			$("#erroMsg").html(resp.responseMsg);
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
		}
	
	}else{
		if(flag && positiveBook){
			$("#erroMsg").html(i18nObj.prop('error.msg.select.atleast.one.game'));
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
		}if(!positiveBook){
			alert(i18nObj.prop('error.msg.enter.positive.value'));
			clearDiv();
		}
	}	
});


$(document).on('change','.qOrder',function(){
	if(this.value!=-1){
		if ($.inArray(this.value,gameSelectedArr) == -1) {
			gameSelectedArr.push(this.value);
		} else {
			$(this).val(-1);
			alert(i18nObj.prop('error.msg.game.already.selected'));
			return;
		}
	}
	
});


/**
 * qucik order ends
 */


/**
 * Book Recieve Start
 */

function getChallanDetails(challanId) {
	//$("#challanBookData").html('');
	//var challanId=$.trim($("#challanId").val());
	var gameCount=0;
	var bookCount=0;
	var div='';
	if(challanId==""){
		$("#chlErrMsg").html(i18nObj.prop('error.msg.enter.challan.id'));
		return false;
	}
	var resp  = _ajaxCallJson(projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/ret_om_bookRecieveRegistration_getBooks.action?challanId="+challanId+"");
	if(resp.responseCode==0){
		$.each($.parseJSON(resp.gameBookData),function(key,value){
			gameCount++;
			bookCount=bookCount+value.length;	
		});
				
		div+='<table class="Challan-table table-data">';
		div+='<thead>';
		div+='<tr><th>';
		div+='<input type="checkbox" id="AllBookCheckbox" class="chkAll">';
		div+='<label for="AllBookCheckbox"><strong>Select All</strong></label>';
		div+='</th>';
		div+='<th colspan="5">';
		div+='<span class="nm-gm">'+i18nObj.prop('label.scratch.no.of.games')+'<b>'+gameCount+'</b></span>';
		div+='<span class="nm-gm">'+i18nObj.prop('label.scratch.no.of.books')+'<b>'+bookCount+'</b></span></th>';
		div+='</tr>';
		div+='</thead>';
		
		div+'<tbody>';
	    var index=0;
		var indexIn=0;
		$.each($.parseJSON(resp.gameBookData),function(key,value){
			div+='<tr>';
			div+='<td scope="row" style="width:150px;">';
			div+='<input type="checkbox" id="AllGameBookCheckbox'+index+'" class="chkAll AllGameBookCheckbox'+index+'" onClick="checkGameWise(this.id)" index='+index+'>';
			div+='<label for="AllGameBookCheckbox'+index+'"><strong>'+key+'</strong></label>';
			div+='</td>';
			div+='<td>';
			div+='<ul>';
			$.each(value,function(key,value){
				div+='<li>';
				div+='<input type="checkbox" id="AllBookCheckbox'+indexIn+'" class="chkAll AllGameBookCheckbox'+index+' selectBook" onClick="checkGameBookWise(this.id)" index='+index+' bookNbr='+value+' >';
				div+='<label for="AllBookCheckbox'+indexIn+'">'+value+'</label>';
				div+='</li>';
				indexIn++;
			});
			div+='</ul>';
			div+='</td>';
			
			div+='</tr>';
			index++;
		});
	 
	     
		div+='</tbody>';
		div+='</table>';
		div+='<div class="submit-btn">';
		div+='<input type="button" id="bookRcv" value="'+i18nObj.prop('btn.scratch.mark.as.received')+'">';
		div+='</div>';
		
		
	}else{
		$("#erroMsg").html(resp.responseMsg);
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	}
	
	$("#dlChallanTable").css("display","block");
	$("#dlChallanTable").html(div);

};

$(document).on("click","#AllBookCheckbox",function(){
  var isAllBookCheckbox=$(this). prop("checked");
  $(".chkAll").prop("checked",isAllBookCheckbox);
});

$(document).on("click",".AllGameBookCheckbox",function(){
	  var isAllBookCheckbox=$(this).prop("checked");
	  $(".chkAll").prop("checked",isAllBookCheckbox);
});

/**
 * On Click of Book Receive
 */
$(document).on("click","#bookRcv",function(){
	var bookArray = $('input:checkbox:checked.selectBook').map(function () {
		  return $(this).attr("bookNbr");
		}).get();
	if(bookArray.length>0){
		var resp  = _ajaxCallJson(projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/ret_om_bookRecieveRegistration_Success.action?bookNumber="+bookArray+"");
		if(resp.responseCode==0){
			var s='';
			 s+="<div class='detail-con'><span>"+i18nObj.prop('label.scratch.book.receive.status')+"</span><span >"+i18nObj.prop('label.scratch.book.received.succesfully')+"</span></div>" ;
			 $('#winSuccessData').append(s);
			 $('#winSuccessBox').css('display','block');
			 $('#winSuccessImg').css('display','block');
			 $('#winSuccessMsg').css('display','block');
			 $('#dlChallanTable').css('display','none');
			 $('#bookReceiveForm').css('display','none');
		}else{
			$("#erroMsg").html(resp.responseMsg);
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
		}
	}else{
		$("#erroMsg").html(i18nObj.prop('error.msg.select.any.book'));
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	}
	
});

/**
 * Book Recieve ends
 */


/**
 *Track Order starts
 */

$(document).on("click","#trackOrderBtn",function(){
	$("#searchOrderDiv").empty();
	$("#orderDetailDiv").empty();
	var gameName=$.trim($("#trackGameName").val());
	var gameNumber=$.trim($("#trackGameNo").val());
	var orderStatus=$.trim($("#trackOrderStatus").val());
	var orderNumber=$.trim($("#trackOrderId").val());
	var startDate=$.trim($("#selectedDateTimePicker").val());
	var endDate=$.trim($("#selectedDateTimePicker1").val());
	
	if(new Date(getCompataibleDate2(startDate)).getTime() > new Date(getCompataibleDate2(endDate)).getTime()){
		$("#erroMsg").html(i18nObj.prop('error.msg.select.end.date.after.start.date'));
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
		return false;
	}
	var resp  = _ajaxCallJson(projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/ret_om_processOrdReq_Search.action?gameName="+gameName+"&gameNumber="+gameNumber+"&orderStatus="+orderStatus+"&orderNumber="+orderNumber+"&startDate="+startDate+"&endDate="+endDate+"");
	
	if(resp.responseCode==0){
		var div='';
		div+='<h3 class="Search-heading">'+i18nObj.prop('label.scratch.search.results')+'</h3>';
		if($.parseJSON(resp.responseData).length>0){
			div+='<table class="table-data">';
			div+='<thead>';
			div+='<tr>';
			div+='<th>'+i18nObj.prop('label.scratch.order.number')+'</th>';
			div+='<th>'+i18nObj.prop('label.scratch.order.date')+'</th>';
			div+='<th>'+i18nObj.prop('label.scratch.order.status')+'</th>';
			div+='</tr>';
			div+='</thead>';
			div+='<tbody>';
			$.each($.parseJSON(resp.responseData),function(){
				div+='<tr>';
				div+='<th scope="row"><a class="table-click-a" href="" onclick="fetchOrderDetail(this.text); return false;">'+this.orderId+'</a></th>';
				div+='<td>'+this.date+'</td>';
				div+='<td>'+this.name+'</td>';
				div+='</tr>';
			});
			div+='</tbody>';
		div+='</table>';
		}else{
			div+='<div style=" text-align: center;">'+i18nObj.prop('label.scratch.no.records.found')+'</div>';
		}
		$("#searchOrderDiv").html(div);
	}else{
		$("#erroMsg").html(resp.responseMsg);
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	}
	
});

function fetchOrderDetail(orderId){
	$("#orderDetailDiv").empty();
	var resp  = _ajaxCallJson(projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/ret_om_processOrdReq_details.action?orderId="+orderId+"");
	if(resp.responseCode==0){
		var responseData=$.parseJSON(resp.responseData);
		var div='';
		div+='<h3 class="Search-heading">'+i18nObj.prop('label.scratch.order.details')+'</h3>';
		div+='<table class="table-data">';
			div+='<tbody>';
			div+='<tr>';
			div+='<th scope="row" colspan="2">'+i18nObj.prop('label.scratch.order.id')+'</th>';
			div+='<td colspan="2">'+responseData.orderId+'</td>';
			div+='</tr>';
			div+='<tr>';
			div+='<th scope="row" colspan="2">'+i18nObj.prop('label.scratch.order.date')+'</th>';
			div+='<td colspan="2">'+responseData.orderDate+'</td>';
			div+='</tr>';
			div+='<tr>';
			div+='<th scope="row" colspan="2">'+i18nObj.prop('label.scratch.order.status')+'</th>';
			div+='<td colspan="2">'+responseData.orderStatus+'</td>';
			div+='</tr>';
			div+='<tr>';
			div+='<th scope="row" colspan="2">'+i18nObj.prop('label.scratch.dl.number')+'</th>';
			div+='<td colspan="2">'+responseData.dlNbr+'</td>';
			div+='</tr>';
			$.each(responseData.gameDataMap,function(key,value){
				div+='<tr class="try">';
				div+='<th width="25%" rowspan="2" scope="row" >'+key+'</th>';
				div+="<td width='25%'>"+i18nObj.prop('label.scratch.no.of.books.ordered')+"</td>";
				div+="<td width='25%'>"+i18nObj.prop('label.scratch.approved.books')+"</td>";
				div+="<td width='25%'>"+i18nObj.prop('label.scratch.no.of.books.delivered')+"</td>";
				div+='</tr>';
				div+='<tr class="try">';
				div+='<td>'+value.noOfBookOrder+'</td>';
				div+='<td>'+value.approvedBook+'</td>';
				div+='<td>'+value.deliveredBooks+'</td>';
				div+='</tr>';
			});      
		div+='</tbody>';
		div+='</table>';
		$("#orderDetailDiv").html(div);
	}else{
		$("#erroMsg").html(resp.responseMsg);
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	}

	return false;
	
}



/**
 *Track Order Ends
 */




//when side menu REPORT clicked
$(document).on("click","#report",function(){
	parent.frames[1].location.replace(projectName+"/com/skilrock/lms/web/drawGames/reportsMgmt/retailer/mainPageReports.jsp");
});

$(document).on("click","#close-popup-button",function(){
	$("#timeErrorDiv").css("display","none");
});

/**
 * Book Activation starts
 */
$(document).on("click","#activateBtn",function(){
	var bookNbr1 = $("#bookNumber").val();
	if(bookNbr1!=""){
		var url = projectName+"/com/skilrock/lms/web/scratchService/inventoryMgmt/ret_im_activate_books_Save.action?bookNbr1="+bookNbr1;
		var resp = _ajaxCallJson(url,'');
		if(resp.isSuccess){
			clearDiv();
			var s='';
   		 	 s+="<div class='detail-con'>"+i18nObj.prop('label.scratch.book.status')+"</span><span >"+resp.responseMsg+"</span></div>" ;
			 $('#winSuccessData').append(s);
    		 $('#winSuccessBox').css('display','block');
    		 $('#winSuccessImg').css('display','block');
    		 $('#winSuccessMsg').css('display','block');
    		 $('#bookActivateForm').css('display','none');
		}else{
			$("#erroMsg").html(resp.responseMsg);
			$("#timeErrorDiv").css("display","block");
			$("#timeErrorDiv").delay(2000).fadeOut("slow");
		}
	}else{clearDiv();
		$("#bookActivationSpan").append(i18nObj.prop('error.msg.enter.book.number'));
	}
});

/**
 * Book Activation ends
 */

/**
 * Winning Claim starts
 */
$(document).on("click","#winningBtn",function(){
	var ticketNbr = $("#winTktNbr").val();
	var virnNbr = $("#winVirnNbr").val();
	if (!(ticketNbr=="" || (virnNbr=="" || virnNbr.length<10))) {
		var url = projectName+"/com/skilrock/lms/web/scratchService/pwtMgmt/ticketAndVirnNumberVerify.action?ticketNbr="+ticketNbr+"&virnNbr="+virnNbr;
		var resp = _ajaxCallJson(url,'');
		if(resp.isSuccess) {
			var isPay = false;
			if(resp.pwtDetailMap.tktBean.isValid){
				if(resp.pwtDetailMap.pwtBean.isValid){
					if(!(resp.pwtDetailMap.pwtBean.isHighLevel)){
						clearDiv();
						if(resp.pwtDetailMap.pwtBean.pwtAmount>0){
							isPay = confirm("Winning Amount is " + resp.pwtDetailMap.pwtBean.pwtAmount + "\nDo you want to pay ?");
	    					if (!isPay) {
	    						return false;  
	    					}
    						var payPwtResp = _ajaxCallJson(projectName+"/com/skilrock/lms/web/scratchService/pwtMgmt/pwt_ret_verifyAndSaveTicketNVirn.action?ticketNbr="+ticketNbr+"&virnNbr="+virnNbr,'');
    						clearDiv();
    						if (payPwtResp.isSuccess){
    							var s='';
    			        		s+="<div class='detail-con'><span>"+i18nObj.prop('label.scratch.winning.amount.is')+"</span><span >"+payPwtResp.pwtDetailMap.pwtBean.pwtAmount+"</span></div>" ;
    			        		$('#winSuccessData').append(s);
    			        		$('#winSuccessBox').css('display','block');
    			        		$('#winSuccessImg').css('display','block');
    			        		$('#winSuccessMsg').css('display','block');
    			        		$('#winningClaimForm').css('display','none');
    						}
    						else{
    					    	$("#erroMsg").html(payPwtResp.responseMsg);
    				    		$("#timeErrorDiv").css("display","block");
    				    		$("#timeErrorDiv").delay(2000).fadeOut("slow");
    				    		$("#winTktErrSpan").html('');
    					    	//$("#winTktErrSpan").append("Please enter valid ticket number");
    					    }
						}else{
			        		alert(i18nObj.prop('error.msg.non.win.ticket'));
			        	}
					}else{
	    				alert(i18nObj.prop('error.msg.high.prize.ticket'));
	    			}
				}else{
	    			alert(resp.pwtDetailMap.pwtBean.message);
	    		}
			}else{
	    		$("#erroMsg").html(resp.pwtDetailMap.tktBean.status);
	    		$("#timeErrorDiv").css("display","block");
	    		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	    	}
		}else{
	    	$("#erroMsg").html(resp.responseMsg);
    		$("#timeErrorDiv").css("display","block");
    		$("#timeErrorDiv").delay(2000).fadeOut("slow");
    		$("#winTktErrSpan").html('');
	    	$("#winTktErrSpan").append(i18nObj.prop('error.msg.enter.valid.ticket.no'));
	    }
	}
	if(virnNbr=="" || virnNbr.length<10){
		$("#winVirnErrSpan").html('');
		$("#winVirnErrSpan").append(i18nObj.prop('error.msg.enter.correct.virn'));
	}if(ticketNbr==""){
		$("#winTktErrSpan").html('');
		$("#winTktErrSpan").append(i18nObj.prop('error.msg.enter.ticket.no'));
	}
});


/**
 * Winning Claim ends
 */

/**
 * Sold Tkt Entry starts
 */
/**
 * Creating UI dynamiclly of Sold Ticket Entries 
 */
function createSoldTktEntryFrom(){
	var innTbl="";
	var url = projectName+"/com/skilrock/lms/web/scratchService/inventoryMgmt/ret_im_soldTktEntry_fetchBooksAjax.action";
	var _respData = _ajaxCallTextData(url);
	if(_respData!=''){
		var tmpArr = _respData.split(',');
		for(var i=0; i<tmpArr.length; i+=1){
		    innTbl+="<table class='table-data' id='gameTab'>";
		    innTbl+="<h3 class='name-heading'>"+((tmpArr[i]).split(";"))[0]+"</h3>";
			innTbl+="<thead><tr><th>"+i18nObj.prop('label.scratch.table.header.book.number')+"</th><th>"+i18nObj.prop('label.scratch.table.header.current.remaining.tickets')+"</th><th >"+i18nObj.prop('label.scratch.table.header.update.remaining.tickets')+"</th></tr></thead>";
			var gameArr = tmpArr[i].split(";");	
			var z = 0, y = 0;
			for(var j=1; j<gameArr.length; j+=1){
				var newGameArr = gameArr[j].split("=");
				if(z==0){
					innTbl = innTbl+'<tbody>';
					z = 1;
				}else {
					z = 0;
				}	
				innTbl+='<input type="hidden" name="gameName" value='+((tmpArr[i]).split(";"))[0]+'>';
				innTbl+='<tr><td>'+newGameArr[0]+'<input type="hidden" name="bookNbr" value='+newGameArr[0]+'></td>';
				innTbl+="<td><input type='text' name='currRem' value="+newGameArr[1]+" class='input-txt' readonly></td>";
				innTbl+='<input type="hidden" name="tktInBook"  size="6" value='+newGameArr[2]+' ></td>';
				innTbl+="<td><span><input type='text' name='updCurrRem' value='' class='input-txt updBooks'><span class='table-error'  id = "+newGameArr[0]+"></span></span></td></tr>";
				if(y==1){
					innTbl = innTbl+'</tbody>';
					y = 0;
				}else {
					y = 1;
				}
			}				
		}
		innTbl+="</table>";
		innTbl+="<div class='submit-btn'><input type='button' value='"+i18nObj.prop('btn.scratch.submit')+"' id='soldTktSubmit'></div>";
		$("#soldTktDataEntry").append(innTbl);
	}
	else{
		$("#soldTktDataEntry").append(i18nObj.prop('error.msg.no.data.found'));
	}
}

$(document).on("click","#soldTkt",function(){
	$("#soldTktDataEntry").html('');
	createSoldTktEntryFrom();
});

/**
 * Sold Ticket Entry Submit Action Starts
 */
$(document).on("click","#soldTktSubmit",function(){
	var IsValid = verificationOfEntries(); 
	if(IsValid){
		var url = projectName+"/com/skilrock/lms/web/scratchService/inventoryMgmt/ret_im_soldTktEntry_save.action?bookNbr="+bookNbr2+"&updCurrRem="+upCurrRem1+"&gameName="+gameName1+"&tktInBook="+tktInBook1+"&currRem="+currRem1;
		var resp = _ajaxCallJson(url,'');
		if(resp.isSuccess){
			$("#soldTktDataEntry").html('');
			var s='';
			s+="<div class='detail-con'><span>Sold Ticket Entry Status :</span><span >"+resp.responseMsg+"</span></div>" ;
			$('#winSuccessData').append(s);
			$('#winSuccessBox').css('display','block');
			$('#winSuccessImg').css('display','block');
			$('#winSuccessMsg').css('display','block');
			$('#soldTktEntryForm').css('display','none');
		}else{
			$("#erroMsg").html(resp.responseMsg);
    		$("#timeErrorDiv").css("display","block");
    		$("#timeErrorDiv").delay(2000).fadeOut("slow");
		}
	}
});
function getDLChallanList(){
	var userOrgId = $("#userOrgId").val();
	var url = projectName+"/com/skilrock/lms/web/scratchService/orderMgmt/ret_om_bookRecieveRegistration_Menu.action?userOrgId="+userOrgId;
	var resp = _ajaxCallJson(url,'');
	console.log(resp);
	if (resp.responseCode == 0) {
		var table = $("<table class='table-data'/>");
		table.append($("<tr><th>"+i18nObj.prop('label.scratch.table.header.serial.number')+"</th><th>"+i18nObj.prop('label.scratch.table.header.delivery.challan.number')+"</th><th>"+i18nObj.prop('label.scratch.table.header.delivery.challan.date')+"</th></tr>"));
		$.each(resp.responseData, function(rowIndex, r) {
	        var row = $("<tr/>");
	        $.each(r, function(colIndex, c) { 
	        	if (colIndex === "dlChallanNumber") {
	        		var temp = "<td class='dl-challan-link' id='"+c+"'><a href='#'>" + c + "</a></td>";
	        		row.append(temp);
	        	}
	        	else {
	        		row.append($("<td/>").text(c));
	        	}
	        });
	        table.append(row);
	    });
		$("#bookReceiveForm").append(table);
	}
	else{
		/*$("#erroMsg").html(resp.responseMsg);
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");*/
		$("#bookReceiveForm").append($("<span>"+resp.responseMsg+"</span>"));
	}
}
$(document).on("click","#receiveReg",function(){
	$("#bookReceiveForm").html('');
	getDLChallanList();
});
$(document).on("click",".dl-challan-link",function(){
	var dlSerialId = $(this).attr('id');
	getChallanDetails(dlSerialId);
	
});
function verificationOfEntries(){
    var bookEle =[];
	var currRemEle=[]; 
	var updCurrRemEle=[];
	var tktInBook=[];
	var gameName =[];
	$("input[name='bookNbr']").each(function(){
		bookEle.push($(this).val());
	});
	$("input[name='currRem']").each(function(){
		currRemEle.push($(this).val());
	});
	$("input[name='updCurrRem']").each(function(){
		updCurrRemEle.push($(this).val());
	});
	$("input[name='tktInBook']").each(function(){
		tktInBook.push($(this).val());
	});
	$("input[name='gameName']").each(function(){
		gameName.push($(this).val());
	});
	var isUpd = false;
	var isVacant = true;
	for(var i=0; i<updCurrRemEle.length; i++){
		if(!(updCurrRemEle[i]=="")){
			if(isNaN(parseInt(updCurrRemEle[i],10))){
				alert(i18nObj.prop('error.msg.enter.integer.value'));
				return false;
			}									
			if(parseInt(updCurrRemEle[i],10)<parseInt(currRemEle[i],10)  ) {					
				isUpd = true;
				bookNbr2.push(bookEle[i]);
				upCurrRem1.push(updCurrRemEle[i]);
				tktInBook1.push(tktInBook[i]);
				currRem1.push(currRemEle[i]);
				gameName1.push(gameName[i]);
				if(parseInt(updCurrRemEle[i],10)<0){
					$("#"+bookEle[i]+"").html("");
					isUpd = false;
					isVacant = false;
					$("#"+bookEle[i]+"").append(i18nObj.prop('error.msg.enter.positive.value'));
					if( i<updCurrRemEle.length){
						continue;
					}
					else{
						return false;
					}
				}
			}else{
				$("#"+bookEle[i]+"").html("");
				isUpd = false;
				isVacant = false;
				$("#"+bookEle[i]+"").append(i18nObj.prop('error.msg.insert.sold.ticket.entries'));
				if( i<updCurrRemEle.length){
					continue;
				}
				else{
					return false;
				}
			}
		}
	}
	if(!isUpd && isVacant ){
		$("#erroMsg").html(i18nObj.prop('error.msg.fill.sold.ticket.entries'));
		$("#timeErrorDiv").css("display","block");
		$("#timeErrorDiv").delay(2000).fadeOut("slow");
	}
	return isUpd;
}

/**
 * Sold Tkt Entry ends
 */


$(document).on("click",".scratch",function(){
	clearScratchForms();
	clearDiv();
	if($('#winClaim').children().hasClass('active')){
		$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.winningclaim'));
		$("#winningClaimForm").css("display","block");
	}else if($('#activate').children().hasClass('active')){
		$("#bookActivateForm").css("display","block");
		$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.activateBook'));
	}else if($('#receiveReg').children().hasClass('active')){
		$("#bookReceiveForm").css("display","block");
		$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.bookReceiveRegister'));
	}else if($('#sellTkt').children().hasClass('active')){
		$("#saleTicketForm").css("display","block");
		$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.saleticket'));
	}else if($('#soldTkt').children().hasClass('active')){
		$("#soldTktEntryForm").css("display","block");
		$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.soldTicketEntry'));
	}else if($('#qOrder').children().hasClass('active')){
		$("#quickOrderForm").css("display","block");
		if ("SAFARIBET" == countryDeployed) {
			$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.quickOrder.toRegionalOffice'));
		} else {
			$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.quickOrder.toAgent'));
		}
	}else if($('#trackOrder').children().hasClass('active')){
		$("#trackOrderForm").css("display","block");
		prepareCalender();
		$(".page-title").html(i18nObj.prop('label.scratch.pagetitle.trackOrder'));
	}
});

function checkGameWise(id){
	  $("#AllBookCheckbox").prop("checked",false);
	  var isAllBookCheckbox=$("#"+id). prop("checked");
	  $("."+id).prop("checked",isAllBookCheckbox);
}

function checkGameBookWise(id){
	 var isAllBookCheckbox=$("#"+id). prop("checked");
	 if(!isAllBookCheckbox){
		 $("#AllBookCheckbox").prop("checked",false);
		 var gameBookIndex=$("#"+id).attr("index");
		 $("#AllGameBookCheckbox"+gameBookIndex).prop("checked",false);
	 }
}


function privilegeList(){
	var userActionList = _ajaxCallJson(projectName+'/com/skilrock/lms/web/drawGames/playMgmt/fetchAllowedUserAction.action', '');
	userActionList.indexOf(userActionListArray[0])!=-1?$("#activate").css("display","block"):$("#activate").css("display","none");
	userActionList.indexOf(userActionListArray[1])!=-1?$("#winClaim").css("display","block"):$("#winClaim").css("display","none");
	userActionList.indexOf(userActionListArray[2])!=-1?$("#soldTkt").css("display","block"):$("#soldTkt").css("display","none");
	userActionList.indexOf(userActionListArray[3])!=-1?$("#sellTkt").css("display","block"):$("#sellTkt").css("display","none");
	userActionList.indexOf(userActionListArray[4])!=-1?$("#receiveReg").css("display","block"):$("#receiveReg").css("display","none");
	userActionList.indexOf(userActionListArray[5])!=-1?$("#qOrder").css("display","block"):$("#qOrder").css("display","none");
	userActionList.indexOf(userActionListArray[6])!=-1?$("#trackOrder").css("display","block"):$("#trackOrder").css("display","none");
}

function clearDiv(){
	$("#winTktErrSpan").html('');
	$("#bookActivationSpan").html('');
	$("#winVirnErrSpan").html('');
	$("#winTktNbr").val('');
	$("#winVirnNbr").val('');
	$("#bookNumber").val('');
	$("#saleTktNbr").val('');
	$("#saleTktMsg").empty();
	$("#challanId").val('');
	$("#chlErrMsg").empty();
	$("#challanBookData").html('');
	$("#searchOrderDiv").empty();
	$("#orderDetailDiv").empty();
	$('#winSuccessData').html('');
	$('#dlChallanTable').html('');
}

function clearScratchForms(){
	$("#winningClaimForm").css("display","none");
	$("#quickOrderForm").css("display","none");
	$("#bookActivateForm").css("display","none");
	$("#bookReceiveForm").css("display","none");
	$("#saleTicketForm").css("display","none");
	$("#soldTktEntryForm").css("display","none");
	$("#quickOrderForm").css("display","none");
	$("#trackOrderForm").css("display","none");
	$('#winSuccessBox').css('display','none');
	$('#winSuccessImg').css('display','none');
	$('#winSuccessMsg').css('display','none');
}

function setSideMenu(){
	var flag=true;
	$("#accordian>ul>li").each(function(){
		if(flag){
			if ($(this).is(":visible")) {
				$(this).children().addClass("active");
				$(".scratch").trigger("click");
				flag=false;
				return;
			}
		}		
	});
}
function setSideMenuFunctionality(){
	$("#accordian>ul>li>a").click(function(){
		//slide up all the link lists
		$("#accordian ul ul").slideUp();
		$("#accordian>ul>li>a").removeClass("active");
		//slide down the link list below the h3 clicked - only if its closed
		if(!$(this).next().is(":visible")){
			$(this).next().slideDown();
			$(this).toggleClass("active");
		}
	});
}
function prepareCalender(){
	 $('#selectedDateTimePicker,#selectedDateTimePicker1').datetimepicker( {
		 mask : '9999/19/39',
		 format : 'Y-m-d',
		 formatDate : 'Y-m-d',
		 timepicker : false,
		 maxDate: '0'
	 });
	 $('#selectedDateTimePicker').val($("#currentServerTime").val().split(" ")[0]);
	 $('#selectedDateTimePicker1').val($("#currentServerTime").val().split(" ")[0]);
	 $('#selectedDateTimePicker').attr("readonly",true);
	 $('#selectedDateTimePicker').attr("readonly",true);
	 
	$('#selectedDateTimePicker,#selectedDateTimePicker1').click(function(){
		$(this).blur();
	});
	

	$('#selectedDateTimePickerDiv').click(function(){
		$('#selectedDateTimePicker').datetimepicker('show');
		$('#selectedDateTimePicker').blur();
	});
	$('#selectedDateTimePickerDiv1').click(function(){
		$('#selectedDateTimePicker1').datetimepicker('show');
		$('#selectedDateTimePicker1').blur();
	});
	
}

//if date comes in yyyy-MM-dd format
function getCompataibleDate2(date){
	var dArr = date.split("-");
	var finalDate = dArr[1]+"/"+dArr[2]+"/"+dArr[0];
	return finalDate;
}
