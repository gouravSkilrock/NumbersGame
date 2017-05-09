var _id = {o:function (el_id) {
	return document.getElementById(el_id);
}, n:function (el_id) {
	return document.getElementsByName(el_id);
}, tn:function (el_id) {
	return document.getElementsByTagName(el_id);
}, i:function (el_id, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.o(el_id).innerHTML = msg;
}, v:function (el_id, r) {
	if (r) {
		return this.o(el_id).value == "";
	}
	return this.o(el_id).value;
}, iniFocus:function (el_id) {
	this.o(el_id).focus();
}, blk:function (el_id) {
	this.o(el_id).style.display = "block";
}, non:function (el_id) {
	this.o(el_id).style.display = "none";
}, hid:function (el_id) {
	this.o(el_id).style.visibility = "hidden";
}, vis:function (el_id) {
	this.o(el_id).style.visibility = "visible";
}, fo:function (f, ele) {
	return document.forms[f].elements[ele];
}, fi:function (f, ele, msg, e) {
	if (e) {
		msg = "<font color = 'red'>" + msg + "</font>";
	}
	this.fo(f, ele).innerHTML = msg;
}, fv:function (f, ele, r) {
	if (r) {
		return this.fo(f, ele).value == "";
	}
	return this.fo(f, ele).value;
}, finiFocus:function (f, ele) {
	this.fo(f, ele).focus();
}};

function _ajaxCallText(actionName, param) {
	var _resp;
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		datatype: "application/json",
		async : false,
		
		success : function(result, arguments, text) {
			if (arguments == 'success') {
				_resp = text.responseText;
				var json = JSON.parse(_resp);
				if(json.isSuccess == false) {
					alert(json.errorMsg);
					_resp = false;
				}
			} else {
				_resp = 'error';
			}
		},
	    error: function(xhr, status, text) {
	    	var responseStatus=xhr.status;
	    	//alert(responseStatus);
	    	if(responseStatus == 440){
	    		window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	    	}
	    	
	    	//window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	        //var response = $.parseJSON(xhr.responseText);

	        console.log('Failure!');

	        //if (response) {
	          //  console.log(response.error);
	       // } else {
	            // This would mean an invalid response from the server - maybe the site went down or whatever...
	        //}
	        _resp = false;
	    }
	});
	return _resp;
}


function _ajaxCallJson(actionName, param) {
	var _resp;
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		datatype: "application/json",
		async : false,
		
		success : function(result, arguments, text) {
		    //var responseStatus=xhr.status;
			if (arguments == 'success') {
				var data = text.responseText;
				try{
					_resp = JSON.parse(data);
				}catch (e) {
					window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
					// TODO: handle excepti
				}				
			} else {
				_resp = 'error';
			}
		},
	    error: function(xhr, status, text) {
	    	var responseStatus=xhr.status;
	    	//alert(responseStatus);
	    	if(responseStatus == 440){
	    		window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	    	}
	    	else if (responseStatus == 403) {
				alert("Request cannot be fulfilled ");
			}else if(responseStatus == 0){
				_resp = "NETERROR";
				return _resp;
			}
	    	
	    	//window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	        //var response = $.parseJSON(xhr.responseText);

	        console.log('Failure!');

	        //if (response) {
	          //  console.log(response.error);
	       // } else {
	            // This would mean an invalid response from the server - maybe the site went down or whatever...
	        //}
	        _resp = false;
	    }
	});
	return _resp;
}


function _ajaxCallTextData(actionName, param) {
	var _resp;
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		datatype: "application/json",
		async : false,
		
		success : function(result, arguments, text) {
			if (arguments == 'success') 
				_resp = text.responseText;
			
		},
	    error: function(xhr, status, text) {
	    	var responseStatus=xhr.status;
	    	//alert(responseStatus);
	    	if(responseStatus == 440){
	    		window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	    	}
	    	
	    	//window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	        //var response = $.parseJSON(xhr.responseText);

	        console.log('Failure!');

	        //if (response) {
	          //  console.log(response.error);
	       // } else {
	            // This would mean an invalid response from the server - maybe the site went down or whatever...
	        //}
	        _resp = false;
	    }
	});
	return _resp;
}

function _ajaxCallDiv(actionName,param,divId) {
	//alert('param=='+param);
	$.ajax( {
		type : "POST",
		url : actionName,
		data : param,
		async : false,
		success : function(result) {
		//alert(result);
		if(result.match("/com/skilrock/lms/web/loginMgmt/loggedOut.jsp")){
			window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
		}else
			$("#"+divId).html(result);
		
		}
	});
}

function formSubmit(formId,divId,event){
	//alert("ddd"+formId);
	var form=$('#'+formId);
	//alert(form.attr('action'));
	$.ajax( {
		type : "POST",
		url : form.attr('action'),
		data : form.serialize(),
		/*async : false,*/
		beforeSend: function() {
		 $('.box-1').css({
	    	 'display':'block',
	    	 'background': 'url("images/backOffice/ui/ajax-loader_blue.gif")  no-repeat center center transparent',
	    	 'position':'relative',
			 'overflow':'hidden',
			 'width':'100px',
			 'height':'100px',
			 /*border:1px dashed #900;*/
			 'text-align':'center',
			 'margin':'5px',
			 'margin-left': '400px',
			 'float':'left'
	     });
	     $('.button-div').css({
	     'display':'none'
	     });		    
		  },
		  complete: function(){
				 $('.box-1').css({
			    	 'display':'none'
			    	 
			     });
			     $('.button-div').css({
			     'display':'block'
			     });
			  },
		success : function(result) {
		//alert(result);
		//alert($(result).find('#errorMessage').text());
		//alert($(result).filter("#errorMessage").text());
		//alert($(result).find(".result-heading-new").text());
		if(result.match("ajaxExceptionOccurHandled.action")){
			//alert($(result).filter("#errorMessage").html());
			//alert($(result).filter("#errorMessage").text());
		window.open(projectName+"/com/skilrock/pms/backOffice/commonMethod/Action/applicationExceptionAction.action?errorMessage="+$(result).find("#errorMessage").text(), target="_top");
		}else
			$("#"+divId).html(result);
		}
	});
	return false;
}

function _ajaxCallForReports(url,targetDiv,currElemId){
	 var childElem = document.getElementById(currElemId);
	 var parentElem = childElem.parentNode;
    var noOfChildNode = parentElem.getElementsByTagName("tr").length;
	var _respData = _ajaxCallText(url, "");
   if(_respData == "success") {
   	parentElem.removeChild(childElem);

   }
}
function _ajaxCallRest(actionName,userName,pwd,param){
	var _resp;
	$.ajax
	  ({
	    type: "POST",
	    url: actionName,
	    dataType: 'json',
	    async: false,
	    data: param,
	    headers: {
            'userName': userName,
            'password': pwd
        },
        contentType: 'application/json; charset=utf-8',
	   /* beforeSend: function (xhr) {
	        xhr.setRequestHeader ("userName:"+ userName,"password:"+ pwd);
	    },*/
	    success: function (result, arguments, text){
	    	if (arguments == 'success') {
				var data = text.responseText;
				try{
					_resp = JSON.parse(data);
				}catch (e) {
					window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
					// TODO: handle excepti
				}				
			} else {
				_resp = 'error';
			}
	    },
	    error: function(xhr, status, text) {
	    	var responseStatus=xhr.status;
	    	
	    	if(responseStatus == 440){
	    		window.open(projectName+"/com/skilrock/lms/web/loginMgmt/loggedOut.jsp", target="_top");
	    	}
	       //console.log('Failure!');
	        _resp = false;
	    } 
	});
	return _resp;
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/common/globalJs/ajaxCall.js,v $'] = '$Id: ajaxCall.js,v 1.3 2016/10/31 09:46:50 neeraj Exp $';