
	  	function getRetailerListForAgent(url, targetDiv, orgType) {
	  		  //alert();	
	  		if(orgType=='RETAILER') {
				var ajaxCall = _ajaxCall(url);
				 //alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("retDiv",tierMap['RETAILER']+" Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("retDiv", "");	 	 
			 }
	  	}
	  	function showdetails(reqId,reqStatus,partyName,pwt_amt,ret_name,task_id,requested_by_id,ticket_nbr,virn_nbr,game_id,game_nbr)
	  {
	   _id.blk('serchResultsDetails');
	   _id.i('req_id',reqId);
	   _id.i('req_status',reqStatus);
	    _id.i('first_name',partyName);
	    //_id.i('last_name',lastName);
	    _id.i('ticket_nbr',ticket_nbr);
	      _id.i('virn_nbr',virn_nbr);
	    _id.i('pwt_amt',pwt_amt);
	    _id.i('ret_name',ret_name);
	    _id.o('pwtAmount').value=pwt_amt;
	     _id.o('taskId').value=task_id;
	      _id.o('requestedById').value=requested_by_id;
	      _id.o('ticketNbr').value=ticket_nbr;
	      _id.o('virnNbr').value=virn_nbr;
	      _id.o('gameId').value=game_id;
	      _id.o('gameNbr').value=game_nbr;
	  
	  
	  }
	  
	  function approveRequest()
	  {       
	    if(confirm('Are You Sure To Approve!!')){
	     _id.o('subTrans').style.visibility="hidden";
	     _id.o('subTransDeny').style.visibility="hidden";
	     var url;
	     var taskId=_id.o('taskId').value;
	      var pwtAmount=_id.o('pwtAmount').value;
	     var requestedbyid=_id.o('requestedById').value;
	     var ticketnbr=_id.o('ticketNbr').value;
	     var virnnbr=_id.o('virnNbr').value;
	     var gameId=_id.o('gameId').value;
	     var gamenbr=_id.o('gameNbr').value;
	     url='approvePendingPwtByAgt.action?taskId='+taskId+'&ticketNbr='+ticketnbr+'&virnNbr='+encodeURIComponent(virnnbr)+'&gameNbr='+gamenbr+'&gameId='+gameId+'&pwtAmount='+pwtAmount+'&requestedById='+requestedbyid;
	    // alert(url);
	     _resp= _ajaxCall(url);
	     
	if(_resp.result){                        
                  //alert(_resp.data);
                  if(_resp.data !="null" || _resp.data == ""){
                     _id.i('serchResults','Request has been Approved Successfully so '+_resp.data);
                    //_id.o('serchResults').innerHtml="Request Approved";
                    _id.non('serchResultsDetails');
                    }
                    else
                    {
                     _id.non('serchResultsDetails');
                      _id.i('serchResults','Some Error has ocurred Please try After some time');
                    }
                                  
      } else {
        alert("Problem: ");
        }
	    _id.o('subTrans').style.visibility="visible";
	     _id.o('subTransDeny').style.visibility="visible";
	   
	}
	  }	  
	   function denyRequest()
	  {	     
	    if(confirm('Are You Sure To Deny!!')){
	    _id.o('subTrans').style.visibility="hidden";
	     _id.o('subTransDeny').style.visibility="hidden";
	     var url;
	     var taskId=_id.o('taskId').value;
	      var pwtAmount=_id.o('pwtAmount').value;
	     var requestedbyid=_id.o('requestedById').value;
	     var ticketnbr=_id.o('ticketNbr').value;
	     var virnnbr=_id.o('virnNbr').value;
	     var gameId=_id.o('gameId').value;
	     var gamenbr=_id.o('gameNbr').value;
	     url='denyPendingPwtsAjax.action?taskId='+taskId+'&ticketNbr='+ticketnbr+'&virnNbr='+encodeURIComponent(virnnbr)+'&denyPwtStatus=Temporary Cancellation&gameId='+gameId+'&gameNbr='+gamenbr;
	    // alert(url);
	     _resp= _ajaxCall(url);
	     
	if(_resp.result){
	              //alert(_resp.data);
	              if(_resp.data=="true"){
                   _id.i('serchResults','Request Cancelled');
                    _id.non('serchResultsDetails');
                    }  
                    else
                    {
                      _id.non('serchResultsDetails');
                      _id.i('serchResults','Some Error has ocurred Please try After some time');
                    }                      
                                     
      } else {
        alert("Problem: ");
        }
         _id.o('subTrans').style.visibility="visible";
	     _id.o('subTransDeny').style.visibility="visible";
	 } 
	  }
  	

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtAgt.js,v $'] = '$Id: agt_pwtMgmt_pwtAgt.js,v 1.2 2016/10/31 11:40:11 neeraj Exp $';