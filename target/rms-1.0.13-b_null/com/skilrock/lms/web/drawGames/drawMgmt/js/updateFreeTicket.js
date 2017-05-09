	var projectName="<%=request.getContextPath() %>";
	var  isSubmit =false;
	function inc(id){
		_id.o("msg").innerHTML ="";
	var val =_id.o(id).value;
	if(val<15){
	allowSubmit();
	_id.o(id).value =++val;
		}
	
	}
function dec(id){
	_id.o("msg").innerHTML ="";
	var val =_id.o(id).value;
	if(val>0){
	allowSubmit();
	_id.o(id).value =--val;
	
	}
	
	
}

function updateFreeTicket(mainGameId,promoGameId,promoId){

				_id.o("msg").innerHTML ="";
				_id.o("saleGameId").value=mainGameId;
				_id.o("promoGameId").value=promoGameId;
				_id.o("promoTicekts").value= _id.o(promoId).value;
				//alert("msgDiv"+promoGameId+_id.o(promoId).value);
				if(isSubmit){
					
					if(isNaN(_id.v('promoTicekts'))||_id.v('promoTicekts').trim()==""){
						_id.o("msg").innerHTML ="Please Provide Valid Input !!";
						_id.o(promoId).value="";
						return false ;
					}
				document.getElementById("msgDiv"+promoGameId).innerHTML = "Please Wait.....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
				var url = "bo_dg_updateFreeTicket_Save.action?saleGameId=" + _id.v('saleGameId') + " & promoGameId=" + _id.v('promoGameId')+" & promoTicekts="+_id.v('promoTicekts')+" &"+new Date().getTime();
					var _resp = _ajaxCall(url);
					//alert(_resp.result);
					//alert(_resp.data);
					if (_resp.result && _resp.data != "") {
						var res =_resp.data.split("|")[0];
						var msg =_resp.data.split("|")[1];
							_id.o("msgDiv"+promoGameId).innerHTML =msg;
						if(res=="Success"){
							var  tickets =_resp.data.split("|")[2];
							if(tickets.trim()!=""){
							_id.o("msgDiv"+promoGameId).style.color="Green";
							_id.o("ptDiv"+promoGameId).innerHTML =tickets;
							}
						
						}else{
							_id.o("msgDiv"+promoGameId).style.color="Red";
						}
					}else{
						_id.o("msgDiv"+promoGameId).innerHTML ="Error In Response";
							_id.o("msgDiv"+promoGameId).style.color="Red";
						}
			
					}else{
					_id.o("msg").innerHTML ="Please Change Atleast One Value";
					}
				
	
		}

		
	
		function allowSubmit(){
		
			isSubmit=true;
		}
		
		function keyPress(obj,e){
		
				
			var code = (window.event)? event.keyCode : e.which;
			//alert("hi"+code);
			 if ( (code < 48 || code > 57) && (code < 96 || code > 105)) {
						 obj.value = "";
      					  e.preventDefault();
       					 return false;
    				}
   			 if( obj.value>10 ||  obj.value<0){
   					  obj.value = "";
      					  e.preventDefault();
       					 return false;
   			 }
  		  return true;
		}
		
	
			

parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/drawMgmt/js/updateFreeTicket.js,v $"] = "$Id: updateFreeTicket.js,v 1.3 2016/10/31 09:46:59 neeraj Exp $";

