<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<s:head theme="ajax" debug="false"/>
<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/confirmAgent.js"></script>
	</head>

	<script>
	function addAnotherGame(){
	isValid = true;
	var tmpRcptArr = document.getElementsByName("gameNbr_Name");
	for(var i=0;i<tmpRcptArr.length;i++){
	if(tmpRcptArr[i].value==-1){
	document.getElementById("row"+tmpRcptArr[i].id).style.color = "red";
	isValid =false;
	break;
	}
	}
	if(isValid){
	var table = document.getElementById("tmpRcptTable");
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	var cellLeft = row.insertCell(0);
	var html="";
	var tdHtmlData = '<table id="tmpRcptGame'+lastRow+'" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC"><tr id="rowgame'+lastRow+'"><td>';
	
	html+='<i>Game Number: &nbsp;</i></td><td><select name="gameNbr_Name" id="game'+lastRow+'" class="option" onchange="onLoadAjaxPwt(this.id,\'divtmpRcptGame'+lastRow+'\')"><option value="-1">--Please Select--</option>';
    for(i=0;i<document.getElementById('game1').options.length;i++) {
		if(i!=0){
   		 html+='<option value="'+document.getElementById('game1')[i].text+'">'+document.getElementById('game1')[i].value+'</option>';
  		 }
  	 }
	html+='</select>'
	tdHtmlData+=html;
	tdHtmlData+='</td></tr><tr><td colspan="2"><input type="hidden" id="gamePrzAmtLength'+lastRow+'" name="gamePrzAmtLength"/><div id="divtmpRcptGame'+lastRow+'"></div></td></tr></table>'
	cellLeft.innerHTML=tdHtmlData;
	}else{
	
	}
}
var requestPwt;
var select;
function onLoadAjaxPwt(id,div) {

select = div;
if(validateGameName(id)){
url = "bo_pwt_bulkAcceptance_GamePrzDet.action?gameName="+(document.getElementById(id).value).substring((document.getElementById(id).value).indexOf('-')+1);
_resp  = _ajaxCall(url);
	if(_resp.result){
      addPrzAmtTable(_resp.data);
      } else {
        alert("Problem: ");
        }
	}

}
	
function addPrzAmtTable(outputResponse){

var table = document.getElementById(select);
var arr = new Array();
arr = outputResponse.split(":");
var html="";
var tdHtmlData ='<table border="1" cellpadding="3" cellspacing="0" bordercolor="#6E6E6E"><tr><td><b>S.No</td><td><b>PRIZE AMT('+parent.frames[0].document.getElementById("currencyWord").innerHTML+')</td><td><b>NO. OF TICKET</td><td><b>AMOUNT('+parent.frames[0].document.getElementById("currencyWord").innerHTML+')</td></tr>';
for(var i=0;i<arr.length-1;i++){
html+='<tr><td>'+parseInt(i+1)+'</td><td>'+arr[i]+'<input type="hidden" name="prizeAmtArr" value="'+arr[i].replace(/,/g,"")+'"/></td><td><input type="text" maxlength="6" name="noOfTick" id="'+select+'nooftick'+parseInt(i+1)+'" size="16" onblur="calculate(\''+select+'amount'+parseInt(i+1)+'\',this.id,this.value*'+arr[i].replace(/,/g,"")+')" onkeypress="return onlyNumbers()"/></td><td><input type="text" name="'+select+'amount'+parseInt(i+1)+'" id="'+select+'amount'+parseInt(i+1)+'" size="16" disabled="true"/></td></tr>';
}
tdHtmlData+=html;
tdHtmlData+='</table>';
table.innerHTML=tdHtmlData;
}	
	function validatePrzAmt(){
	
	var tmpRcptArr = document.getElementsByName("gamePrzAmtLength");
	for(var i=0;i<tmpRcptArr.length;i++){
	var divId="divtmpRcptGame"+(tmpRcptArr[i].id).substring(16,(tmpRcptArr[i].id).length);
	var tickNoCounter = 0;
	var	cn = document.getElementById(divId).getElementsByTagName('*');
		  	for (var j = 0; j < cn.length; j++) 
		  	{
				     if ( cn[j].nodeType==1 && cn[j].name=="noOfTick")
				      {
				      if(cn[j].value!=""){
				      
				      tickNoCounter++;
				      }else{
				      cn[j].value = " ";
				      tickNoCounter++;
				      }
				      }
		  	}
	document.getElementById(tmpRcptArr[i].id).value=tickNoCounter;
	}
	
	return validateData();
	}
	
	function  validateData(){
	isValid = true;
	if(document.getElementById("agtOrgName").value==-1){
	document.getElementById("agtOrgNameId").innerHTML="<font color='red'>Please Select <s:property value="#application.TIER_MAP.AGENT" /> Name</font>";
	isValid=false;
	}else{
	document.getElementById("agtOrgNameId").innerHTML="";
	}
	var tmpRcptArr = document.getElementsByName("gameNbr_Name");
	isGameSelected = false;
	for(var i=0;i<tmpRcptArr.length;i++){
	if(tmpRcptArr[i].value!=-1){
	isGameSelected = true;
	}
	if(isGameSelected){
	var tmpRcptArr1 = document.getElementsByName("noOfTick");
	isEmpty =true;
	for(var i=0;i<tmpRcptArr1.length;i++){
	if(tmpRcptArr1[i].value!=" "){
	isEmpty=false;
	}
	}
	if(isEmpty){
	isValid=false;
	document.getElementById("gameErrDiv").innerHTML="<font color='red'>Please Enter Number of Ticket</font>";
	}else{
	document.getElementById("gameErrDiv").innerHTML="";
	}
	}else{
	document.getElementById("gameErrDiv").innerHTML="<font color='red'>Please Select Game Name</font>";
	isValid=false;
	}
		
	}
	
	return isValid;
	}
	
	
	function validateGameName(gameNameId){
	var tmpRcptArr = document.getElementsByName("gameNbr_Name");
	for(var i=0;i<tmpRcptArr.length;i++){
	//alert(gameNameId+"----------"+tmpRcptArr[i].id);
	if(gameNameId!=tmpRcptArr[i].id){
	//alert(document.getElementById(gameNameId).value+"----------"+tmpRcptArr[i].value);
	if(document.getElementById(gameNameId).value==-1){
	document.getElementById("divtmpRcptGame"+gameNameId.replace("game","")).innerHTML="";
	return false;
	}
	if(document.getElementById(gameNameId).value==tmpRcptArr[i].value&document.getElementById(gameNameId).value!=-1){
	alert("Game Name Already Selected");
	document.getElementById(gameNameId).selectedIndex=0;
	document.getElementById("divtmpRcptGame"+gameNameId.replace("game","")).innerHTML="";
	document.getElementById("row"+gameNameId).style.color = "red";
	return false;
	}
	}
	}
	document.getElementById("row"+gameNameId).style.color = "#777";
	return true;
	}
function onlyNumbers(evt) {
	var e = event || evt;
	var charCode = e.which || e.keyCode;
	if (charCode >= 48 && charCode < 58) {
		return true;
	}
	return false;
}
function calculate(resId,przId,res){
if(isNaN(res)||res<0){
document.getElementById(resId).value="";
document.getElementById(przId).value="";

}else{ 
document.getElementById(resId).value=res;
}
}

	function _subTrans(){
	return ( validatePrzAmt() && _subValid('subTrans'));
	}
	</script>

	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	
	<s:actionerror/>
	<div id="wrap">

 	<div id="top_form">
  	<h3>PWT Acceptance in Bulk</h3>
<s:form name="tempPwtReceipt" action="bo_pwt_bulkAcceptance_Save" onsubmit="return _subTrans()">
		<tr>
			<td>
				<table id="tmpRcptTable" >
						<tr>	
						<td align="left"><div id="agtOrgNameId"></div><div id="gameErrDiv"></div><table>
							<s:select label="%{#application.TIER_MAP.AGENT} Name" id="agtOrgName" name="agtOrgName" headerKey="-1"
										headerValue="--Please Select--"
										list="#session.AGT_ORG_LIST.{orgName}"cssClass="option" onchange="verifyAgent(this.value,'agtOrgName','game1');"/>		
							</table></td>
					</tr>
					<tr>	
						<td>
							<input type="button" value="Add Another Game" onclick="addAnotherGame()" class="button"/>
						</td>
					</tr>
					<tr>
						<td>
								<table id="tmpRcptGame1" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC">
									<tr id="rowgame1">
										<td>
											<s:select label="Game Number" name="gameNbr_Name" id="game1"  headerKey="-1" cssClass="option"
														headerValue="--Please Select--" list="%{#session.ACTIVE_GAME_LIST.{gameNbr_Name}}" onchange="onLoadAjaxPwt(this.id,'divtmpRcptGame1')"/>
										</td>
									</tr>
									<tr>
										<td colspan="2"><input type="hidden" id="gamePrzAmtLength1"  name="gamePrzAmtLength"/>
											<div id="divtmpRcptGame1">
											</div>
										</td>
									</tr>
									
								</table>

							</td>
					</tr>
					
				</table>
			</td>
		</tr>
		<tr align="right">	
						<td>
							<div id="subCheck" style="position:absolute;"></div>
							<s:submit theme="simple" value="Submit" id="subTrans" cssClass="button"/>
						</td>
					</tr>	
		</s:form>
		</div></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkAcceptance_Menu.jsp,v $
$Revision: 1.2 $
</code>