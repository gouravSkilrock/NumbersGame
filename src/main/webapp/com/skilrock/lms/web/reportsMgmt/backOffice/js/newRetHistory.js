
var tableName=null;
var text=null;
var fl = false;
function filterHistoryData(selectType){
    var st1 = document.getElementsByName("selectState");
	   for (var i = 0; i < st1.length; i++) {       
	       if (st1[i].checked) {
	        var selectState = st1[i].value;
	    	if(selectType!="cd"){
	        st1[i].checked = false;
	    	}
	          break;
	        }
	    }
   var newSelType= selectType;
	if(selectType=="cd"){
		var st = document.getElementsByName("selectType");
	    for (var i = 0; i < st.length; i++) {       
	        if (st[i].checked) {
	        	selectType = st[i].value;
	            break;
	        }
	    }
	}
	var startDate = document.getElementById('start_date').value;
	var endDate = document.getElementById('end_date').value;
	var radios = document.getElementsByName("selectService");
    for (var i = 0; i < radios.length; i++) {       
        if (radios[i].checked) {
           selectService = radios[i].value;
            break;
        }
    }
	if(selectType=='3'){
		if(newSelType=="cd"){
			filterStateData('');
		} else{
			document.getElementById('statusDiv').style.display = 'block';	
			document.getElementById("histReportDiv").innerHTML = "";
		}	
	}
 else {
	document.getElementById('mydata').style.display = 'none';
	document.getElementById('statusDiv').style.display = 'none';
	document.getElementById("histReportDiv").innerHTML = "";
	text = _ajaxCall(path + "/com/skilrock/lms/web/reportsMgmt/bo_rep_new_retActivity_History_result.action?selectType="+selectType+"&start_date="+startDate+"&end_Date="+endDate+"&selectService="+selectService);
	document.getElementById("histReportDiv").innerHTML = text.data;
	}
}

function filterStateData(selectState){
	var st = document.getElementsByName("selectType");
	   for (var i = 0; i < st.length; i++) {       
	       if (st[i].checked) {
	        var selectType = st[i].value;
	          break;
	        }
	    }
	   if(selectState==""){
	   var st2 = document.getElementsByName("selectState");
	   for (var i = 0; i < st2.length; i++) {       
	       if (st2[i].checked) {
	        var selectState = st2[i].value;
	          break;
	        }
	    }
	   }
	var startDate = document.getElementById('start_date').value;
	var endDate = document.getElementById('end_date').value;
	var radios = document.getElementsByName("selectService");
    for (var i = 0; i < radios.length; i++) {       
        if (radios[i].checked) {
           var selectService = radios[i].value;
            break;
        }
    }
	text = _ajaxCall(path + "/com/skilrock/lms/web/reportsMgmt/bo_rep_new_retActivity_History_result.action?selectType="+selectType+"&start_date="+startDate+"&end_Date="+endDate+"&selectService="+selectService+"&selectState="+selectState);
	document.getElementById("histReportDiv").innerHTML = text.data
	document.getElementById("mydata").innerHTML = document.getElementById("myt").innerHTML;
	document.getElementById('mydata').style.display = 'block';
	tableName = document.getElementById("Open_Text_General");
	if(fl==false)
	buildTable('');
	else
	selAllChkBx();
}
 function makeTable(){
	   sh_highlightDocument();

       $("#tableDiv_General").each(function() {
           var Id = $(this).get(0).id;
           var maintbheight = 300;
           var maintbwidth = 820;

           jq("#" + Id + " .FixedTables").fixedTable({
               width: maintbwidth,
               height: maintbheight,
               fixedColumns: 1,
               classHeader: "fixedHead",
               classColumn: "fixedColumn",
               fixedColumnWidth: 150,
               outerId: Id
         
           });
       });
   } 
 var c = 0;
var cityName = new Array();
function buildTable(data){	
	call();
	if(data!="all")
	cityName = new Array();
	var cityCheck = document.getElementsByName("cList");
		for (var i = 0; i < cityCheck.length; i++) {       
			if (cityCheck[i].checked) {
				cityName.push(cityCheck[i].value.split('(')[1].split(')')[0]); 
			}
		} 
	var table =  tableName;
	var rows = table.getElementsByTagName('tr');
	var thcol = document.getElementById('cList1').value;
	var thcolnew = thcol.replace("[","").replace("]","");
	var tempArr = new Array();
	for(var i=thcolnew.split(',').length;i>0;i--){
		tempArr.push(i);
	}
	for(var i=thcolnew.split(',').length;i>0;i--){
			if(contains(cityName,thcolnew.split(',')[i-1].trim())){
				var tempArrTemp=new Array();
				for(var k=0;k<i;k++){
					tempArrTemp.push(tempArr.pop());
				}
				tempArrTemp.pop();
				var tempLen=tempArrTemp.length;
				if(tempArrTemp.length!=0)
				for(var k1=0;k1<tempLen;k1++){
					tempArr.push(tempArrTemp.pop());
				}
			}
			else{
				//alert(rows[0].cells.length + "  "+i + "  "+thcolnew.split(',')[i-1].trim());
				if((rows[0].cells.length)-1>cityName.length)
				rows[0].deleteCell(i);
			}
		}
	
	//alert("temp "+tempArr);
	for (var row=1; row<rows.length-1;row++) {
		var col = rows[row].getElementsByTagName('td');
		for(var k=0;k<tempArr.length;k++){
			col[tempArr[k]].style.display="none";
		}
	}
	makeTable();
	var subTab = document.getElementById('subTab');
	var subRows = subTab.getElementsByTagName('tr');
	if(subRows[0].cells.length==0){
		$(".fixedColumn").hide();
	}
}
function call(){
	document.getElementById("histReportDiv").innerHTML=text.data;
	tableName = document.getElementById("Open_Text_General");
}
function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) {
            return true;
        }
    }
    return false;
}
function selAllChkBx() {
	cityName = new Array();
	var tab = _id.o("searchTable");
	var chkBx = tab.getElementsByTagName("input");
	var selAll = _id.o("selAll").checked;
	var dis = false;
	if (selAll) {
		dis = true;
		fl = true;
	} else{
		fl = false;
	}
	for (var i = 0; i < chkBx.length; i++) {
		if(dis==true){
			if (chkBx[i].type == "checkbox" && chkBx[i].id != "selAll") {
				cityName.push(chkBx[i].value.split('(')[1].split(')')[0]);
				chkBx[i].checked = true;
				//chkBx[i].disabled = dis;
			}
		} else{
			if(chkBx[i].value!="-1")
			cityName.pop(chkBx[i].value.split('(')[1].split(')')[0]);
			chkBx[i].checked = false;
			//chkBx[i].disabled = dis;
		}
	}
	buildTable("all");
} 
function showdetails(startDate,selectDevice){
var text1 = _ajaxCall(path + "/com/skilrock/lms/web/reportsMgmt/bo_rep_retActivity_pos_version_History.action?start_date="+startDate+"&selectDevice="+selectDevice);
document.getElementById("posVersionDiv").innerHTML = text1.data;
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/backOffice/js/newRetHistory.js,v $'] = '$Id: newRetHistory.js,v 1.3 2016/10/31 09:46:26 neeraj Exp $';