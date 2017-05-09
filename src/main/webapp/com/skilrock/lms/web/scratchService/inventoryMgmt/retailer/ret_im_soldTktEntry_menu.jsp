<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>var path = "<%=request.getContextPath() %>";</script>
	<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/agent/js/agt_pwtMgmt_pwtCommonFnAgt.js"></script>
	<script>	
		function fetchBookList(url) {
			//alert(url);
			var _resp  = _ajaxCall(url);//alert(_resp.data);
			if(_resp.result){
				var _respData=_resp.data;
				//alert(_respData);
				var tmpArr = _respData.split(',');
				//alert(tmpArr.length+"======"+tmpArr);
				var innTbl = '<table border="0" cellpadding="3" cellspacing="2" width="100%" id="gameTab">';
				var colArr = ['#f3f7fd','#fafdee','#fefade','#eefdd1','#g5e9f7', '#fcefef']; 
				var col = 0;
				for(var i=0; i<tmpArr.length; i+=1){
					
					var gameArr = tmpArr[i].split(";");	
					
					innTbl = innTbl+'<tr><td><div style="text-align:center; color:red; " id="'+(gameArr[0].split("-"))[0]+'"></div><div style="text-align:center; color:red; " id="tktErr'+(gameArr[0].split("-"))[0]+'"></div></td></tr>';
					innTbl = innTbl+'<tr><td><table border="1" cellpadding="3" style="background-color: '+colArr[col]+'" cellspacing="0" width="650" id="bookPan" bordercolor="#C0D8FC">';
					if(col>3) col=0;
					else col = col+1;
					
					innTbl = innTbl+'<tr><th align="center" colspan="6">'+gameArr[0]+'<input type="hidden" name="gameName" value="'+gameArr[0]+'"></th></tr>';
					innTbl = innTbl+'<tr><th align="center">Book Nbr</th><th align="center">Current Rem. Tickets</th><th align="center">Update Rem. Tickets</th>';
					var rowspan = gameArr.length/2;
					//innTbl = innTbl+'<th rowspan="'+rowspan+'">&nbsp;</th>';
					innTbl = innTbl+'<th align="center" nowrap="nowrap">Book Nbr</th><th align="center">Current Rem. Tickets</th><th align="center">Update Rem. Tickets</th></tr>';
					var z = 0, y = 0;
					for(var j=1; j<gameArr.length; j+=1){
						var newGameArr = gameArr[j].split("=");	
						if(z==0){
							innTbl = innTbl+'<tr>';
							z = 1;
						}else {
							z = 0;
						}
						
						innTbl = innTbl+'<td nowrap="nowrap">'+newGameArr[0]+'<input type="hidden" name="bookNbr" value="'+newGameArr[0]+'" /></td>';
						innTbl = innTbl+'<td align="center"><input type="text" name="currRem"  size="6" value="'+newGameArr[1]+'" readonly="readonly" size="6"></td>';
						innTbl = innTbl+'<input type="hidden" name="tktInBook"  size="6" value="'+newGameArr[2]+'" ></td>';
						innTbl = innTbl+'<td  align="center"><input type="text" name="updCurrRem" value="" size="6" limit="5" style="background-color: #fcefef"></td>';	
						
						if(y==1){
							innTbl = innTbl+'</tr>';
							y = 0;
						}else {
							y = 1;
						}
					}
					
					innTbl = innTbl+'</table></td></tr>';				
				}
				innTbl = innTbl+'<tr><td align="right"><input class="button" type="button" value="submit" onclick="verificationOfEntries()"></td></tr></table>';
			//	alert(innTbl);
				
				_id.o('gameDivTab').innerHTML =  innTbl;
				
			}else {
				alert("Problem Occured ......");
			}
		}
		
		
		function validateData(){
			var bookEle = _id.n('bookNbr');
			var currRemEle = _id.n('currRem');
			var updCurrRemEle = _id.n('updCurrRem');
			var isUpd = false;
			for(var i=0; i<updCurrRemEle.length; i++){//alert(updCurrRemEle[i].value);
				//alert('tktErr'+((bookEle[i].value).split("-"))[0]);
				if(!(updCurrRemEle[i].value=="")){
					if(isNaN(updCurrRemEle[i].value) || (updCurrRemEle[i].value.indexOf('.')!=-1)){
						alert("Please Enter Integer Value");
						return false;
					}									
					if(parseInt(updCurrRemEle[i].value,10)<parseInt(currRemEle[i].value,10) ) {						
						isUpd = true;
						if(parseInt(updCurrRemEle[i].value,10)<0){
							var tktErr='tktErr'+((bookEle[i].value).split("-"))[0];
							_id.i(tktErr, "Please Insert Positive Value","e");
							updCurrRemEle[i].style.backgroundColor = "red";
							return false;
						}
						updCurrRemEle[i].value=parseInt(updCurrRemEle[i].value,10);
					}else{
						var gmNbr = ((bookEle[i].value).split("-"))[0];
						_id.i(gmNbr, "Please Insert Updated Remaining Ticket Less Then Current Remaining Tickets","e");
						updCurrRemEle[i].style.backgroundColor = "red";
						return false;
					}
				}
			}
			if(!isUpd){
				alert("Please Fill Some Sold Ticket Entries !!");
			}
			return isUpd;
		}
		
		function verificationOfEntries(){
			var showNxt=validateData();//alert(showNxt);
		if(showNxt){
			var bookEle = _id.n('bookNbr');
			var updCurrRemEle = _id.n('updCurrRem');
			var currRem=_id.n('currRem');
			var tktInBook=_id.n('tktInBook');
			var innTbl = '<table border="1" cellpadding="3" style="background-color:#f3f7fd" cellspacing="0" width="650" id="bookPan" bordercolor="#C0D8FC">';
			innTbl = innTbl+'<tr><th align="center">Book Nbr</th><th align="center">Current Rem. Tickets</th><th align="center">Update Rem. Tickets</th>';
			for(var i=0;i<updCurrRemEle.length;i++){
			if(updCurrRemEle[i].value!=""){
				innTbl = innTbl+'<tr><td nowrap="nowrap">'+bookEle[i].value+'<input type="hidden" name="bookNbr" value="'+bookEle[i].value+'" /></td>';
				innTbl = innTbl+'<td nowrap="nowrap">'+currRem[i].value+'<input type="hidden" name="currRem" value="'+currRem[i].value+'" /><input type="hidden" name="tktInBook" value="'+tktInBook[i].value+'" /></td>';
				innTbl = innTbl+'<td nowrap="nowrap">'+updCurrRemEle[i].value+'<input type="hidden" name="updCurrRem" value="'+updCurrRemEle[i].value+'" /></td></tr>';
			}
			}			
				
			innTbl = innTbl+'</table>';
			//	alert(innTbl);
			_id.o('gameDivTab').innerHTML =  innTbl;
			
			_id.o('subTrans').style.display="block";
			}
		}
		
		
		function _subTrans(){
			return ( _subValid('subTrans'));
		}
	</script>
	<s:head theme="ajax" debug="false"/>
</head>


<body onload="fetchBookList('ret_im_soldTktEntry_fetchBooksAjax.action')">

	
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
	<div id="top_form">
		<h3>Sold Ticket Entry</h3>
		
			<s:form name="pwtAgent" action="ret_im_soldTktEntry_save" onsubmit="return _subTrans()">
		
			<table border="0" cellpadding="3" cellspacing="0" >				
				<tr>
					<td>
						<div id="gameDivTab">
							
						</div>
					<br /></td>
				</tr>
				
				
				<tr>
					<td align="right" >&nbsp;
						<div id="subCheck" style="position:absolute;"></div>
						<s:submit align="right" theme="simple" value="Update" cssClass="button" id="subTrans" cssStyle="display:none"/> 
					</td>
				</tr>
			</table>
		</s:form>
	</div>
	</div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: ret_im_soldTktEntry_menu.jsp,v $
$Revision: 1.2 $
</code>