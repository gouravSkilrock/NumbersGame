var i18nObj = parent.frames[0].i18Obj;
function onTypeChangeValue(typeValue) {	
	document.getElementById('ret_error').innerHTML="";
	document.getElementById('d2').innerHTML="";
	document.getElementById('agent_error').innerHTML="";		
	if(typeValue=='BO') {
		document.getElementById('agt').style.display="none";
		document.getElementById('agt1').style.display="none";	
		document.getElementById('ret').style.display="none";
		document.getElementById('ret1').style.display="none";
		document.getElementById('game_id').focus();
		
	}else {
	 	document.getElementById('agt').style.display="block";	
	 	document.getElementById('agt1').style.display="block";	
	 	document.getElementById('agent_org_name').options[0].selected = true;			
		document.getElementById('ret').style.display="none";
		document.getElementById('ret1').style.display="none";
		document.getElementById('agent_org_name').focus();
	}
} 

function onAgentChangeValue(agentValue) {
	var value =document.getElementById('agent_org_name').value;
	//alert("11111");
	document.getElementById('d2').innerHTML="";
	//alert("222222222");
	document.getElementById('agent_error').innerHTML="";
	//alert("333333333");
	var type=document.getElementById('org_type').value;
	//alert("44444444"+type);
	if(type=='AGENT') {
		if(value=='-1') {		
			document.getElementById('agent_error').innerHTML=i18nObj.prop('error.pls.slct.an')+" "+tierMap['AGENT'].toUpperCase()+" "+i18nObj.prop('error.cmpny');		
			return false;
		}else return true;
	}else if(agentValue!='form' && type=='RETAILER'){
		//alert("inside else retailer");
		fetchRetOrgList(agentValue)	;
		
	}else {
		//alert("form submit");
		return true;
	} 
	}
	
	

function convert(urlX){
		var exp= /&/g ;
		return urlX.replace(exp,"amp");
	
}

var mainDiv;
var req;
var which;

function executeAjax(newUrl, div) {		
	
	mainDiv = div;
	   _resp  = _ajaxCall(newUrl);
	if(_resp.result){
	    	var resStr = _resp.data;
	    	//alert(resStr); 
			if(resStr=='NO_RET') {
				alert("NO "+tierMap['RETAILER'].toUpperCase()+" "+i18nObj.prop('error.org.exist'));
				document.getElementById('ret').style.display="none";
			}			
			else {
				document.getElementById('ret').style.display="block";
				createRetSelect(resStr);
			}
      } else {
        alert("Problem: ");
        }
}

function removeAllOptions(newSelectbox) {
	for(var i=newSelectbox.options.length-1;i>=0;i--) 
		if(i>1) 	newSelectbox.remove(i);	
}



function createRetSelect(responseData){
	var newSelectbox = document.getElementById("ret_org_name");
	removeAllOptions(newSelectbox);
	var retList = new Array();
	retList=responseData.replace("[","").replace("]","").split(",");
	for(i=0;i<retList.length;i++) {
		if(i!=0) {
			var key=retList[i].replace(" ","");
			var val=retList[i].replace(" ","");
		}else {
			var key=retList[i];
			var val=retList[i];
		}		
		var opt = new Option(key, val);
		newSelectbox.options[i+1] = opt;
	}
}




function getInventoryDetails(url, div) {

	mainDiv = div;
	var type=document.getElementById('org_type').value;
	var agent=document.getElementById('agent_org_name').value;
	var retailer=document.getElementById('ret_org_name').value;
	var gameid=document.getElementById('game_id').value;
	if((type=='AGENT')&& (agent=='-1')) {				
			document.getElementById('agent_error').innerHTML=i18nObj.prop('error.pls.slct.an')+" "+tierMap['AGENT'].toUpperCase()+" "+i18nObj.prop('error.cmpny');		
			return false;	
	} 
	else if((type=='RETAILER') && (retailer=='-1')) {
			document.getElementById('ret_error').innerHTML=i18nObj.prop('error.pls.slct.an')+" "+tierMap['RETAILER'].toUpperCase()+" "+i18nObj.prop('error.cmpny');		
			return false;	
	}
	else {
		//alert("INSIDE else");	
		document.getElementById("loadingDiv").innerHTML='<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
		var newUrl=url+"?type="+type+"& agtId="+escape(agent)+"& gameid="+gameid+"& retId="+retailer;
   _resp  = _ajaxCall(newUrl);
	if(_resp.result){
   	    	var resStr= _resp.data; 		
			if(resStr=='NONE') {
				alert(i18nObj.prop('error.no.books.exist'));
				document.getElementById('d2').innerHTML=i18nObj.prop('error.no.books.exist');
			}			
			else{
				createInventoryDetailsDiv(resStr);
			}	
		document.getElementById("loadingDiv").innerHTML='';	
      } else {
        alert("Problem: ");
        }
	}
}
var currentStart;
var currentEnd;

function showDetails(pId) {
	//alert("id = "+pId);
	
	if(pId.match("plus")) {
		//alert("plus clicked");
		var newId=pId.replace("plus", "");
		//alert("new id = "+newId);
		var length=packList.length;
		for(i=currentStart; i<currentEnd; i++) {
			document.getElementById("v"+i).style.display="none";
			document.getElementById("minus"+i).style.display="none";
			document.getElementById("plus"+i).style.display="block";
		}
		document.getElementById("v"+newId).style.display="block";
		document.getElementById("minus"+newId).style.display="block";
		document.getElementById("plus"+newId).style.display="none";
	}
	else {
		//alert("minus clicked");
		var newId=pId.replace("minus", "");
		//alert("new id = "+newId);
		document.getElementById("v"+newId).style.display="none";
		document.getElementById("minus"+newId).style.display="none";
		document.getElementById("plus"+newId).style.display="block";
	}
	
}

var paginationSelect;
function createInventoryDetailsDiv(response) {
	
	var totalBooks=inventoryDetails(response); 
	/*var str="";
		for(var z=0; z<totalBooks.length; z++) {
			str=str+list[z];
		}	
		//alert("str ========= "+str);
	*/
	//alert(paginationSelect);
	if(document.getElementById("pagination")!=null){		
		document.getElementById("pagination").selectedIndex=0;
	}
	if(packList.length>11){
	createPackDetail('0','10');
	}else{
	createPackDetail('0',packList.length);
	}
	

}


var packList;
var booksInPack;
var totalBooks;

function inventoryDetails(response) {

	totalBooks=new Array();
	packList=new Array();
	booksInPack=new Array();
	
	var divPacks=response.split("pack");
	
	var divPacksLength=divPacks.length;
	for(var i=0; i<divPacksLength; i++) {		
		var packs=divPacks[i].split("book");		
				
		var bookList=0.0;	
		var books=packs[1].split(",");		
		totalBook=new Array();
		
		var booksLength=books.length;
		for(var j=0; j<booksLength; j++) {
			var book=books[j].split("TO");					
			var bookLists=generateSeriesList(book[0], book[1]);	
			
			var totalBookLength=totalBook.length;
			var bookListsLength=bookLists.length;		
			for(var k=0; k<bookListsLength; k++) {
				totalBook[totalBookLength+k]=bookLists[k];
			}					
			//bookList=bookList+booklists.length;				
		}
		//booksInPack[i]=bookList;
		booksInPack[i]=totalBook.length;
		packList[i]=packs[0];
		totalBooks[i]=totalBook;
		//alert("pack = "+packList[i]+"  books length = "+bookList);
		
		
	}	
	
	paginationSelect='<select id="pagination" onchange="createPackDetail(this.value.split(\'to\')[0],this.value.split(\'to\')[1])">'
		var packLen = packList.length;
	    var i=0;
	    		//alert(packLen);
			   while(i<packLen){
			  	 if(i+10<packLen){
			  paginationSelect=paginationSelect+'<option value="'+parseInt(i)+'to'+parseInt(i+10)+'">'+(i+1)+'  to '+(i+10);
			  	 }else{
			  paginationSelect=paginationSelect+'<option value="'+parseInt(i)+'to'+packLen+'">'+parseInt(i+1)+' to '+packLen;
			   }
			   i=i+10; 
			   }
	paginationSelect=paginationSelect+'</select>';
	
	return totalBooks;
}

function generateSeriesList(first, last) {	
	//alert(" first "+first+"  last = "+last);	
		var list=new Array();
		var afirst=first.split("-");
		var length=afirst[1].length;
		var blast=last.split("-");	
		//alert(" afirst "+afirst[1]+"  blast = "+blast[1]);
		var firstNumber=parseFloat(afirst[1]);	
		var lastNumber=parseFloat(blast[1]);	
		//alert(" first No. "+firstNumber+"  lastNumber = "+lastNumber);
		var i=0;
		while(firstNumber<=lastNumber) {
			var book=afirst[0]+"-"+completeBookNbr(firstNumber, length);		
			list[i]=book;
			firstNumber+=1;	
			i++;			
		}	
		
			
		return list;
	}
	
	
function completeBookNbr(number, length) {
		var num=number+"";
		var zeroLength=length-num.length;
		var totalZeros="";
		while(zeroLength>0) {
			totalZeros=totalZeros+"0";
			zeroLength-=1;
		}
		return (totalZeros+num);
	}

function createPackDetail(start,end){

currentStart =parseInt(start,10);
currentEnd = parseInt(end,10);
var selectedInd=0;

if(document.getElementById("pagination")!=null){
	selectedInd=document.getElementById("pagination").selectedIndex;
}
var table="<table border='1' cellpadding='0' cellspacing='0' bordercolor='CCCCCC' style='border-color: green;' width='90%'>"+
				"<tr><th align='center'>Pack Number</th><th align='center'>Number Of Books</th><th align='right'>"+paginationSelect+"</th></tr>";
	//alert(start+" === "+end);
	//alert(parseInt(start,10)<parseInt(end,10));
	for(var i=parseInt(start,10); i<parseInt(end,10); i++) {
		var newRow="<tr>"+
						"<td align='left'>Pack - "+packList[i]+"</td>"+
						"<td align='left'>Number Of Books = "+booksInPack[i]+"</td>"+
						"<td align='right'>"+
						"<div id='plus"+i+"' style='color: blue;font-size: medium;cursor: hand;'><label id='labelplus"+i+"' onclick='showDetails(\"plus"+i+"\")' /><img src='"+path+"/LMSImages/images/Plus.gif'/></label></div>"+
						"<div id='minus"+i+"' style='color: blue;font-size: medium;display: none;cursor: hand;'><label id='labelminus"+i+"' onclick='showDetails(\"minus"+i+"\")' /><img src='"+path+"/LMSImages/images/minus.gif'/></label></div>"+
						"</td>"+
					"</tr>"+
					"<tr><td colspan='3'>"+
							"<div id='v"+i+"' style='color: blue;display: none;'><table width='100%' border='1' cellpadding='3' cellspacing='0' bordercolor='CCCCCC'>";
		var bookDetailDiv="";
		
		var books=totalBooks[i];
		var totalBooksLength=books.length;
		//alert("totalBooksLength = "+totalBooksLength);
		var n=-1,m=-10; 
		for(var k=0; k<totalBooksLength; k++) {			
					
					n+=1; m+=1;
					if(n%10==0) {
						bookDetailDiv=bookDetailDiv+"<tr>";
					}
						bookDetailDiv=bookDetailDiv+"<td align='left'>&nbsp;"+books[k]+"</td>";	
					if(m%10==0)
						bookDetailDiv=bookDetailDiv+"</tr>";		
						
		}
		bookDetailDiv=bookDetailDiv+"</table></div></td></tr>"
		newRow=newRow+bookDetailDiv;
		table=table+newRow;
		//alert("pack = "+packList[i]+"      book length = "+booksInPack[i]+"    totalBooks = "+totalBooks[i].length);
	
	}
	table=table+"</table>";
	//alert(table);
	document.getElementById('d2').innerHTML=table;
	document.getElementById("pagination").selectedIndex=selectedInd;

	
}
function getAgentList() {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
	var firstArr = text.data.split(":");
	var retObj = _id.o("agent_org_name");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
}
function fetchRetOrgList(orgId) {
	var text = _ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/fetchRetOrgList.action?orgId="+orgId);
	var firstArr = text.data.split(":");
	var retObj = _id.o("ret_org_name");

	for ( var i = 0; i < firstArr.length - 1; i=i+1) {
		var retNameArray = firstArr[i].split("|");
		var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
		retObj.options[i + 1] = opt;
	}
	document.getElementById('ret').style.display="block";
	document.getElementById('ret1').style.display="block";
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/inventoryBooksDetails.js,v $'] = '$Id: inventoryBooksDetails.js,v 1.3 2016/10/31 09:46:19 neeraj Exp $';