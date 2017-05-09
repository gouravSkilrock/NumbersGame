var gameArr=new Array();
function fetchDet(){
	crDiv();
	_id.non('top_form');
	var A=_id.o('retOrgName'),gameSelBox=_id.o('gameNbr_NameHid');
	var B=_ajaxCall("getGameNameList.action?gameStatus=OPEN");
	if(B.result){
		_respData=B.data;
		
		if(_respData!=""){
			var C=B.data.split(',');
			for(var D=0;D<C.length;D+=1){
				var E=C[D].split("=");
				var F=E[0];
				var G=E[1];
				var H=new Option(G,F);
				gameSelBox.options[D+1]=H;
			}
		}
	}
	_id.non('dyDiv');
	_id.blk('top_form');
}

function crDiv(){var A=document.createElement('div'),divId='ajaxDiv';A.setAttribute('id',"dyDiv");A.innerHTML='Please Wait....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>';document.body.appendChild(A);}var count=0,gmNoLnth=0;totalGame=0;function appendGameTab(A){var B=_id.v('nog');var C=_id.o('gameNbr_NameHid').options.length-1;totalGame=C;if(B>C){gmNoLnth=C;document.getElementById('add_game').style.visibility="hidden";if(gmNoLnth<1)_id.i('top_form','<h3>'+document.getElementsByTagName('h3')[0].innerHTML+'</h3><br/><center>No Game Available for Order</center>');}else {gmNoLnth=B;}for(var D=0;D<gmNoLnth;D++){count=D;addGameToTable(A,count);}}function addGameToTable(A){if(count<totalGame){var B=_id.o('gameTab');var C=B.rows.length;var D=B.insertRow(C);D.setAttribute('id',"row-"+C);var E=D.insertCell(0);var F=document.createElement('div');F.innerHTML=''+C+"&nbsp;";E.appendChild(F);E.style.textAlign="right";var G=D.insertCell(1);var H=document.createElement('div');H.innerHTML='<select name="gameName" id="game_name-'+C+'" class="option" onchange="chkDupGame(this.id)"><option value="-1">--Please Select--</option></select>';G.appendChild(H);G.style.textAlign="center";var I=D.insertCell(2);var J=document.createElement('div');J.innerHTML='<input type="text" maxlength="4" size="4" onkeypress="return isValidEntries(this.id)" name="noOfBooks" id="no_of_books-'+C+'"  />';I.appendChild(J);I.style.textAlign="center";var K=D.insertCell(3);var L=document.createElement('div');L.setAttribute('id',"div-"+C);L.innerHTML='&nbsp;';K.appendChild(L);K.style.textAlign="center";fillSelect('game_name-'+parseInt(C,10));count+=1;}else {document.getElementById('add_game').style.visibility="hidden";}}function fillSelect(A){var B=_id.o('gameNbr_NameHid');var C=_id.o(A);for(i=0,l=B.options.length;i<l;i++){if(i!=0){var D=B[i];var E=new Option(D.text,D.value);C.options[i]=E;}}}function validate(){var A=document.getElementsByName("gameName");var B=true;for(var C=0;C<(A.length)-1;C++){var D=(A[C].id).split("-");for(var E=C+1;E<A.length;E++){if(A[E].selectedIndex!="0"&&A[C].selectedIndex!="0"){if(A[E].value==A[C].value&&A[E].id!=A[C].id){_id.i("div-"+D[1],"You have already selected that Game","e");_id.o(E).selectedIndex=0;B=false;}else{_id.i("div-"+D[1],"&nbsp;");}}}}if(!B)return false;for(var C=0;C<A.length;C++){var D=(A[C].id).split("-");if(A[C].selectedIndex!="0"&&_id.o("no_of_books-"+D[1]).value!=""){if(!isNumericAmt(_id.v("no_of_books-"+D[1]))){B=false;_id.i("div-"+D[1],"Please Enter Valid No Of Books To Order.","e");continue;}else _id.i("div-"+D[1],"&nbsp;");}if((A[C].selectedIndex=="0")&&!(_id.v("no_of_books-"+D[1])=="")){_id.i("div-"+D[1],"Please Select The  Game Name From List.","e");B=false;continue;}else if((A[C].selectedIndex!="0")&&(_id.o("no_of_books-"+D[1]).value=="")){_id.i("div-"+D[1],"Please Enter The No Of Books To Order.","e");B=false;continue;}else _id.i("div-"+D[1],"&nbsp;");if(_id.o("no_of_books-"+D[1]).value!=""&&parseInt(_id.o("no_of_books-"+D[1]).value,10)<=0){_id.i("div-"+D[1],"Please Enter The No Of Books Greater Than Zero.","e");B=false;continue;}else _id.i("div-"+D[1],"&nbsp;");}return B;}function isNumericAmt(A){var B="1234567890";var C=0;var D;var E=true;var F=A;for(i=0;i<F.length;i++){D=F.charAt(i);if(B.indexOf(D)==-1){E=false;break;}}return E;}function chkDupGame(A){var B=document.getElementsByName("gameName");for(var C=0;C<B.length;C++){if(B[C].selectedIndex!="0"&&B[C].value==_id.v(A)&&B[C].id!=A){alert('You have already selected that Game\n Please Re-Enter "The No Of Books" , if you Need More Books To Order for the same Game!! ');_id.o(A).selectedIndex=0;return false;}}return true;}function isValidEntries(A,B){var C=A.split("-");var D=_id.v('game_name-'+C[1]);if(D=="-1"){_id.i("div-"+C[1],"Please Select The Game Name.","e");return false;}else {_id.i("div-"+C[1],"&nbsp;");}var E=event||B;var F=E.which||E.keyCode;if((F>=48&&F<58)){return true;}return false;}
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/orderMgmt/agent/js/agt_orderMgmt_quickOrderReq.js,v $'] = '$Id: agt_orderMgmt_quickOrderReq.js,v 1.2 2010/05/12 10:25:18 gaurav Exp $';
