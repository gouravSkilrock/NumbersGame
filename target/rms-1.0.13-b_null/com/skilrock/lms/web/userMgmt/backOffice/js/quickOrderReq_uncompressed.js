
		var gameArr = new Array();
		function fetchDet(){
			crDiv();			
			_id.non('top_form');
			
			var retSelBox = _id.o('retOrgName'),gameSelBox=_id.o('gameNbr_NameHid');
			var _resp  = _ajaxCall("getGameNameList.action?gameStatus=OPEN");
			if(_resp.result){
				_respData=_resp.data;
				var tmpArr = _resp.data.split(',');
				for(var i=0; i<tmpArr.length; i+=1){
					var gameArr=tmpArr[i].split("=");
					var value = gameArr[0];
					var key = gameArr[1];
					//alert(key+"  "+value);
					var opt = new Option(key,value);
					gameSelBox.options[i+1] = opt;
				}
			}
		//alert();
		_id.non('dyDiv');
		_id.blk('top_form');
		}
		
		function crDiv(){
		  var newdiv = document.createElement('div'),divId = 'ajaxDiv';
		  newdiv.setAttribute('id',"dyDiv");
		  newdiv.innerHTML = 'Please Wait....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>';
		  document.body.appendChild(newdiv); 
		}		
		
		
		var count=0,gmNoLnth=0; totalGame=0;
		function appendGameTab(type){
		//alert("numOfGame>gmInSelBx == ");
			// numOfGame == game display length from property file
			var numOfGame = _id.v('nog');
			// gmInSelBx == total no of games in database
			var gmInSelBx = _id.o('gameNbr_NameHid').options.length-1;
			totalGame = gmInSelBx;
			//alert("numOfGame>gmInSelBx == "+numOfGame+" > "+gmInSelBx);
			if(numOfGame>gmInSelBx){
				gmNoLnth = gmInSelBx;
				document.getElementById('add_game').style.visibility="hidden";
				if(gmNoLnth<1)
				_id.i('top_form','<h3>'+document.getElementsByTagName('h3')[0].innerHTML+'</h3><br/><center>No Game Available for Order</center>');
			}
			else {
				gmNoLnth = numOfGame;
			}
			for(var i=0;i<gmNoLnth;i++){
				count=i;
				addGameToTable(type, count);
			}
		}
		
		function addGameToTable(type) {
			//alert(" count<gmNoLnth "+count +" < "+gmNoLnth);
			
			if(count<totalGame){	
				  var tbl =_id.o('gameTab');
				  var iteration = tbl.rows.length;
				  var row = tbl.insertRow(iteration);
				  row.setAttribute('id', "row-"+iteration);
				  	
				  var cellFirst = row.insertCell(0);			 	   	
				  		var newdivsr = document.createElement('div');				  				  
						newdivsr.innerHTML = ''+iteration+"&nbsp;";
				  cellFirst.appendChild(newdivsr);
				  cellFirst.style.textAlign="right";
				  	
				  var cellSec = row.insertCell(1);			 	   	
						var newdiv = document.createElement('div');				  				  
						newdiv.innerHTML = '<select name="gameName" id="game_name-'+iteration+'" class="option" onchange="chkDupGame(this.id)"><option value="-1">--Please Select--</option></select>';
				  cellSec.appendChild(newdiv);
				  cellSec.style.textAlign="center";
				      
				  var cellTh = row.insertCell(2);			 	   	
						var newdivth = document.createElement('div');				  				  
						newdivth.innerHTML = '<input type="text" maxlength="4" size="4" onkeypress="return isValidEntries(this.id)" name="noOfBooks" id="no_of_books-'+iteration+'"  />';
				  cellTh.appendChild(newdivth);			 			    
				  cellTh.style.textAlign="center";
				    
				  var cellForth = row.insertCell(3);			 	   	
						var newdivForth = document.createElement('div');
						newdivForth.setAttribute('id', "div-"+iteration);				  				  
						newdivForth.innerHTML = '&nbsp;';
				 cellForth.appendChild(newdivForth);  
				 cellForth.style.textAlign="center";
	
				 fillSelect('game_name-'+parseInt(iteration,10));
				      count+=1;
				 //altRowColor('gameOutTab'+count,count);
				    
			 }
			 else {		     
			     document.getElementById('add_game').style.visibility="hidden";
			 }
		}
		
		function fillSelect(selectBox){
			var ajaxList=_id.o('gameNbr_NameHid');
			//alert(_id.o(selectBox));
			var newSelectbox = _id.o(selectBox);
			for(i=0,l=ajaxList.options.length;i<l;i++) {
					if(i!=0){
						var obj = ajaxList[i];
						var opt = new Option(obj.text,obj.value);
						newSelectbox.options[i] = opt;
					}
			}
		}
	
	
		function validate() {
			var gameSel = document.getElementsByName("gameName");
			var flag = true;
			for(var i=0;i<(gameSel.length)-1;i++){
				var _idArr = (gameSel[i].id).split("-");
				for(var j=i+1;j<gameSel.length;j++){									
					if(gameSel[j].selectedIndex!="0" && gameSel[i].selectedIndex!="0") {					
						if(gameSel[j].value==gameSel[i].value && gameSel[j].id!=gameSel[i].id){
							_id.i("div-"+_idArr[1], "You have already selected that Game", "e");					
							_id.o(j).selectedIndex=0;					
							flag = false;					
						} else{
							_id.i("div-"+_idArr[1], "&nbsp;");
						}						
					}
				}
			}
			if(!flag)	return false;
		
			for(var i=0;i<gameSel.length;i++){
				var _idArr = (gameSel[i].id).split("-");
				if(gameSel[i].selectedIndex!="0" && _id.o("no_of_books-"+_idArr[1]).value!="") {
					if(!isNumericAmt(_id.v("no_of_books-"+_idArr[1]))){						
						flag = false;
						_id.i("div-"+_idArr[1], "Please Enter Valid No Of Books To Order.", "e");
						continue;
					}else _id.i("div-"+_idArr[1], "&nbsp;");													
				}
				//alert("1");
				if( (gameSel[i].selectedIndex=="0") && !(_id.v("no_of_books-"+_idArr[1])=="") ) {
					_id.i("div-"+_idArr[1], "Please Select The  Game Name From List.", "e");
					flag = false;
					continue;
				}
				else if((gameSel[i].selectedIndex!="0") && (_id.o("no_of_books-"+_idArr[1]).value=="") ){
					_id.i("div-"+_idArr[1], "Please Enter The No Of Books To Order.", "e");
					flag = false;
					continue;
				}else _id.i("div-"+_idArr[1], "&nbsp;");	
				
				//alert("2");
				
				if(_id.o("no_of_books-"+_idArr[1]).value!="" && parseInt(_id.o("no_of_books-"+_idArr[1]).value, 10)<=0){
					_id.i("div-"+_idArr[1], "Please Enter The No Of Books Greater Than Zero.", "e");
					flag = false;
					continue;					
				}else  _id.i("div-"+_idArr[1], "&nbsp;");	
				
				//alert("3");	
			}
		
			return flag;
		}
			
		function isNumericAmt(value) {
		 	var strValidChars = "1234567890";
		 	var dotCount=0;
			var strChar;
			var flag = true;
			var strString = value;
			//  test strString consists of valid characters listed above
			for (i = 0; i < strString.length; i++) {
				strChar = strString.charAt(i);				
				if (strValidChars.indexOf(strChar) == -1) {
					flag = false;
					break;
				}
			}		
			return flag;
		}
		
		
		function chkDupGame(id){	
			var gameSel = document.getElementsByName("gameName");	
			for(var i=0; i<gameSel.length; i++){
				if(gameSel[i].selectedIndex!="0" && gameSel[i].value==_id.v(id) && gameSel[i].id!=id){	
					alert('You have already selected that Game\n Please Re-Enter "The No Of Books" , if you Need More Books To Order for the same Game!! ');
					_id.o(id).selectedIndex=0;				
					return false;					
				}
			}
			return true;
		}
	
		function isValidEntries(textObj, evt) {			
			var idArr = textObj.split("-");			
			var selBoxValue = _id.v('game_name-'+idArr[1]);
			if(selBoxValue=="-1"){ _id.i("div-"+idArr[1], "Please Select The Game Name.", "e"); return false; }
			else 	{_id.i("div-"+idArr[1], "&nbsp;"); }			
						
			var e = event || evt;
			var charCode = e.which || e.keyCode;
			if ((charCode >= 48 && charCode < 58)) {
				return true;
			}
			return false;
		}
		
parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/quickOrderReq_uncompressed.js,v $'] = '$Id: quickOrderReq_uncompressed.js,v 1.1 2010/04/01 04:22:20 gaurav Exp $';
