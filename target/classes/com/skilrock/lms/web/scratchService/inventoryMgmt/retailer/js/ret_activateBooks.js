
function selectGameBook(chkName,id){
	var tableName=_id.o(chkName);
	var allChkBox = tableName.getElementsByTagName("input");
	var checkValue=_id.o(id).checked;
	for(var i=0;i<allChkBox.length;i+=1){
		allChkBox[i].checked=checkValue;
	}
}

function selectAllBooks(id){
	var allChkBox = _id.tn("input");
	var checkValue=_id.o(id).checked;
	for(var i=0;i<allChkBox.length;i+=1){
		allChkBox[i].checked=checkValue;
	}
}

function validate(){
	var allChkBox = _id.n("bookNumber");
	var chk=false;
	for(var i=0;i<allChkBox.length;i+=1){
		if(allChkBox[i].checked==true){
			chk=true;
			break;
		}
	}
	if(!chk){
		alert('Please Select Atleast One Book');
	}
	return chk;
}