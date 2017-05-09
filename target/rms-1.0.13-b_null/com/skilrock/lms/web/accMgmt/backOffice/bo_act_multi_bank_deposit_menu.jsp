<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script>var path = "<%=request.getContextPath()%>"</script>
		
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/rightClickDisable.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/ACT_ST5_Validation.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/accMgmt/backOffice/js/digitToWord.js"></script>
		
		<s:head theme="ajax" debug="false" />
		
		<style type="text/css">
			#wrap {
    				background: none repeat scroll 0 0 #FFFFFF;
				    height: 100%;
				    margin: 0 auto;
				    margin-left: 120px;
				    text-align: left;
				    width: 951px;
				  }
				 
				 .wrap2 {
    				background: none repeat scroll 0 0 #FFFFFF;
				    height: 100%;
				    margin: 0 auto;
				    margin-left: 120px !important;
				    text-align: left;
				    width: 1045px !important;
				 }
		</style>
		
		<script type="text/javascript">
			var i = 0;
			var j = 5;
			var chkBoxIds = new Array();
			var confrmIds = new Array();
			var totalDepositAmt = 0.0;
		
			$(document).ready(function(){
				$('#tableBodyData').empty();
				addRows();
				
				 $("#btnAddRow").click(function(){
			 		addRows();
			 	 });
			 	 
			 	 $("#submitPayments").click(function(){
						if($(".myCheckBoxClass:checkbox:checked").length > 0){
       						prepareConfirmData();       						
   						}else {
       						alert("Please select atleast one payment !!");
   						}
   				 });
				
				$('#confirmPayments').click(function(){
					var isValid = validateConfirmPage();
					if(isValid > 0 ){
						alert("Please fill all required entries (higlighted in red) or remove the particular row !!");
					}else{					
						var finalData =	prepareJsonData();
						$('#finalPaymentData').val(finalData);
						$('#confirmForm').submit();
					}
				});		
			});
			
			
			
			function prepareConfirmData(){
				var tmp=0;
				$("input:checkbox[name=selectPayment]:checked").each(function () {
						chkBoxIds[tmp] = $(this).attr("id").split("_")[1];
						tmp += 1;
        		});
        		var dupFound = checkDuplicatePANames();
				if(!dupFound){
					prepareConfirmationPage(chkBoxIds); 
				}        		       		
			}
			
			function prepareJsonData(){
				jsonObj = [];
				for (var p=0; p<confrmIds.length; p++) {
					 item = {}
					 item ["agtName"] = $("#agentNames"+confrmIds[p]+" option:selected").text();
					 item ["agtId"] = $("#agentNames"+confrmIds[p]).val().split("#")[0];
        			 item ["bankName"] = $("#bankNames"+confrmIds[p]+" option:selected").text();;
        			 item ["branchName"] = $("#branchName"+confrmIds[p]).val();
        			 item ["depositAmt"] = $("#depositAmt"+confrmIds[p]).val();
        			 item ["receiptNo"] = $("#receiptNo"+confrmIds[p]).val();
        			 item ["depositDate"] = $("#depositDate"+confrmIds[p]).val();
        			 item ["avlCredit"] = $("#avlCredit"+confrmIds[p]).val();
        			 jsonObj.push(item);
				}
				jsonString = JSON.stringify(jsonObj);
				return jsonString;
			}
			
			function prepareConfirmationPage(chkBoxIds){			
					
					for (var k=0; k<chkBoxIds.length; k++) {
						confrmIds[k] = k;
						$row = $('<tr id="row'+k+'" />')
						$('#tableBodyData2').append($row);	
							$col1 = $('<td/>');
							$col1.append("<select name='agtName' id='agentNames"+k+"' class='option' onchange='displayBal(this)'></select>");
							$row.append($col1);
									getAgentDataDropDown('agentNames'+k,chkBoxIds[k]);
									$('#agentNames'+k).val($('#agtName'+chkBoxIds[k]).val());
							
							
							$col2 = $('<td/>');
							$col2.append("<select name='bankName' id='bankNames"+k+"' class='option' onchange='displayChng(this)'></select>");
								$row.append($col2);	    
									getBankDataDropDown('bankNames'+k);
								    $('#bankNames'+k).val($('#bankName'+chkBoxIds[k]).val());
						
								    
							$('.option').attr("disabled", true);
							
							$col3 = $('<td/>');
							$col3.append("<input type='text' id='branchName"+k+"' name='branachName' onBlur='displayChngText(this)' value='"+$('#branchName'+chkBoxIds[k]).val()+"' readOnly='true'  size='12' maxlength='20'/>");
							$row.append($col3);
							
							$col4 = $('<td/>');
							$col4.append("<input type='text' id='depositAmt"+k+"' name='depositAmt' onBlur='displayChngText(this);changeTotAmt(this)' value='"+$('#depositAmt'+chkBoxIds[k]).val()+"' readOnly='true' size='10' maxlength='7'/>");
							$row.append($col4);
							totalDepositAmt = parseFloat(totalDepositAmt)+parseFloat($('#depositAmt'+chkBoxIds[k]).val());
							
							$col5 = $('<td/>');
							$col5.append("<input type='text' id='receiptNo"+k+"' name='receiptNo' onBlur='displayChngText(this); ' value='"+$('#receiptNo'+chkBoxIds[k]).val()+"' readOnly='true' size='16' maxlength='20'/>");
							$row.append($col5);
							
							$col8 = $('<td/>');
							$col8.append("<input type='text' id='depositDate"+k+"' name='depositDate' value='"+$('#depositDate'+chkBoxIds[k]).val()+"' readonly size='10' maxlength='10'/>");
							$row.append($col8);
							
							$col9 = $('<td/>');
							$col9.append("<input type='button' class='dateIconStyle' onclick='prepareCalendarData(this)' id='cal_"+k+"' disabled/>");
							$row.append($col9);
							
							$col6 = $('<td/>');
							$col6.append("<input type='text' id='avlCredit"+k+"' name='avlCredit' value='"+$('#avlCredit'+chkBoxIds[k]).val()+"' readOnly='true' size='12'/>");
							$row.append($col6);
							
							$col7 = $('<td/>');
							$col7.append("<input type='button' class='button' id='editBtn_"+k+"' name='editBtn' value='Edit' onClick='editRow(this)'>&nbsp;<input type='button' class='button' id='removeBtn_"+k+"' name='removeButton' value='Remove' onClick='removeRow(this)'>");
							$row.append($col7);			
					}
					
					$('#firstInput').empty();
					$('#totDepAmt').html(totalDepositAmt);
					$('#secondInput').show();
					$('#wrap').addClass("wrap2");
			}
			
			function getAgentDataDropDown(tdIdOne,j){
				var agtData = $('#agtData').val();
				var agtMap = jQuery.parseJSON(agtData);
				$('#'+tdIdOne).append($('<option></option>').val("-1").html("--Please Select--"));
				$.each(agtMap, function(key, value) {
					$('#'+tdIdOne).append($('<option></option>').val(key+"#"+j).html(value.toUpperCase()));
				});
				sortDropDown(tdIdOne);
				$('#'+tdIdOne).val(-1);	
			 }
			 
			 /* Sort Drop down box by names*/
			 function sortDropDown(elemId)	{		 
				 $("#"+elemId).html($('#'+elemId+' option').sort(function(x, y) {
	         			return $(x).text() < $(y).text() ? -1 : 1;
	   			 }));
	   		}
			 
			 function getBankDataDropDown(tdIdTwo){
			 	var bankData = $('#bankData').val();
				var bankMap = jQuery.parseJSON(bankData);
				//$('#'+tdIdTwo).append($('<option></option>').val("-1").html("--Please Select--"));
				$.each(bankMap, function(key, value) {
					$('#'+tdIdTwo).append($('<option></option>').val(key).html(value));
				});
				//sortDropDown(tdIdTwo);
				//$('#'+tdIdTwo).val('ZENITH BANK');	
			 }
			 
			 function addRows(){
			 	for(;i<j;i++){
					$('#tableBodyData').append("<tr>");
					$('#tableBodyData').append("<td><select name='agtName' id='agtName"+i+"' class='option' onchange='displayBal(this)'></select></td>");
					getAgentDataDropDown('agtName'+i,i);
					$('#tableBodyData').append("<td><select name='bankName' id='bankName"+i+"' class='option' onchange='displayChng(this)'></select></td>");
					getBankDataDropDown('bankName'+i);
					$('#tableBodyData').append("<td><input type='text' id='branchName"+i+"' name='branachName' onBlur='displayChngText(this)' size='10' maxlength='20'/></td>");
					$('#tableBodyData').append("<td><input type='text' id='depositAmt"+i+"' name='depositAmt' onBlur='displayChngText(this)' size='15' maxlength='7'/></td>");
					$('#tableBodyData').append("<td><input type='text' id='receiptNo"+i+"' name='receiptNo' onBlur='displayChngText(this)' size='17' maxlength='20'/></td>");
					$('#tableBodyData').append("<td><input type='text' id='depositDate"+i+"' name='depositDate' value='"+$('#curDate').val()+"' readonly size='12' maxlength='10'/></td>");
					$('#tableBodyData').append("<td><input type='button' class='dateIconStyle' onclick='prepareCalendarData(this)' id='cal_"+i+"'/></td>");
					$('#tableBodyData').append("<td><input type='text' id='avlCredit"+i+"' name='avlCredit' readOnly='true' size='12'/></td>");
					$('#tableBodyData').append("<td center><input type='checkbox' id='chkBox_"+i+"' name='selectPayment' onClick='validateRow(this)' class='myCheckBoxClass' style='margin-left:20px'></td>");
					$('#tableBodyData').append("</tr>");
				}
				j+=5;
			 }
			 
			 function editRow(obj){
			 	$('#agentNames'+obj.id.split("_")[1]).attr("disabled", false);
			 	$('#bankNames'+obj.id.split("_")[1]).attr("disabled", false);
			 	$('#branchName'+obj.id.split("_")[1]).attr('readonly', false);
			 	$('#depositAmt'+obj.id.split("_")[1]).attr('readonly', false);
			 	$('#receiptNo'+obj.id.split("_")[1]).attr('readonly', false);
			 	$('#cal_'+obj.id.split("_")[1]).attr('disabled', false);
			 }
			 
			 function removeRow(obj){
			 	totalDepositAmt = parseFloat(totalDepositAmt) - parseFloat($("#depositAmt"+obj.id.split("_")[1]).val());
			 	$('#totDepAmt').html(totalDepositAmt);
			 	$("#row"+obj.id.split("_")[1]).remove();
			 	var removeItem = obj.id.split("_")[1];			 	
			 	confrmIds = $.grep(confrmIds, function(value) {
  					return value != removeItem;
				});				
			 	if($('#secondInput tr').length == 4){
			 		$('#secondInput').empty();
			 		location.reload();
			 	}
			 }			 
			 
			 function fetchAvlCredit(agtOrgId, textBoxId) {
					var text = _ajaxCallJson(projectName+"/com/skilrock/lms/web/accMgmt/fetchAvlCredit.action?agentOrgId="+agtOrgId);
					var avlAmt = text.avlBal;
					var bal = avlAmt.split("=");
					$('#'+textBoxId).val((parseFloat(bal[0]) - parseFloat(bal[1])));			 
			 }
			 
			function displayBal(obj){
				$("#"+obj.id).css("border","");
				var rowId = obj.id.substring(7, obj.id.length);
				if($("#"+obj.id).val() == -1){
					$("#avlCredit"+rowId).val("");
				}
				fetchAvlCredit($("#"+obj.id).val().split('#')[0],"avlCredit"+$("#"+obj.id).val().split('#')[1]);			 
			}
			
			function displayChng(obj){
				$("#"+obj.id).css("border","");			 
			}
			
			function displayChngText(obj){
				if($("#"+obj.id).val() != ""){
					$('#totDepAmt').html(totalDepositAmt);
					$("#"+obj.id).css("border","");
				}
			}
			
			function changeTotAmt(obj){
				totalDepositAmt = 0.0;
				for(var q=0; q<confrmIds.length;q++){
					totalDepositAmt = parseFloat(totalDepositAmt) + parseFloat($("#depositAmt"+confrmIds[q]).val());					
				}
				$('#totDepAmt').html(totalDepositAmt);		 
			}
			
			function validateDepositAmt(obj){
				if($("#"+obj.id).val() <= 0){
					$("#depositAmt"+rowId).css("border","1px solid #FF0000");
					$("#depositAmt"+rowId).val("");
					$("#depositAmt"+rowId).attr('placeholder', "Amount > 0");
				}			 
			}
			
			function validateRow(obj){
				var rowId = obj.id.split('_')[1];
				var statusFlag = true;
				
				/* validating Agent name selected */
				if($("#agtName"+rowId).val() == -1){
					$("#agtName"+rowId).css("border","1px solid #FF0000");
					$('#'+obj.id).attr('checked', false);
					statusFlag = false;
				}
				
				/* validating bank name selected */
				if($("#bankName"+rowId).val() == -1){
					$("#bankName"+rowId).css("border","1px solid #FF0000");
					$('#'+obj.id).attr('checked', false);
					statusFlag = false;
				}
				
				/* validating branch name entered */
				/*if($("#branchName"+rowId).val() == "" || $("#branchName"+rowId).val() == 'undefined'){
					$("#branchName"+rowId).css("border","1px solid #FF0000");
					$("#branchName"+rowId).attr('placeholder', "Enter Branch Name");
					$('#'+obj.id).attr('checked', false);
					statusFlag = false;
				}*/
				
				/* validating deposit amount entered */
				if($("#depositAmt"+rowId).val() == "" || $("#depositAmt"+rowId).val() == 'undefined'){
					$("#depositAmt"+rowId).css("border","1px solid #FF0000");
					$("#depositAmt"+rowId).attr('placeholder', "Enter Deposit Amount");
					$('#'+obj.id).attr('checked', false);
					statusFlag = false;
				}
				
				/* validating bank deposit receipt no entered */
				if($("#receiptNo"+rowId).val() == "" || $("#receiptNo"+rowId).val() == 'undefined'){
					$("#receiptNo"+rowId).css("border","1px solid #FF0000");
					$("#receiptNo"+rowId).attr('placeholder', "Enter Receipt No");
					$('#'+obj.id).attr('checked', false);
					statusFlag = false;
				}
				
				/* validating deposit amount entered  > 0*/
				if($("#depositAmt"+rowId).val() <= 0 && $("#depositAmt"+rowId).val() != ""){
					$("#depositAmt"+rowId).css("border","1px solid #FF0000");
					$("#depositAmt"+rowId).val("");
					$("#depositAmt"+rowId).attr('placeholder', "Amount > 0");
					$('#'+obj.id).attr('checked', false);
					statusFlag = false;
				}
				
				/* validating deposit Amount must be a decimal value*/
					 var pattern = new RegExp(/^[0-9]*[.]{0,1}[0-9]*$/);
					 if(!pattern.test($("#depositAmt"+rowId).val())){
					 	$("#depositAmt"+rowId).css("border","1px solid #FF0000");
						$("#depositAmt"+rowId).val("");
						$("#depositAmt"+rowId).attr('placeholder', "valid amount");
						$('#'+obj.id).attr('checked', false);
						statusFlag = false;
					 }
				
				/* Activating table footer if atleast one checkbox is checked*/
				if(statusFlag == true){
					$("#tableFooterData").show();
				}			 
			}
			
			function validateConfirmPage(){
				var statusFlag = true;
				var invalidStatus = 0;
				for(var p=0; p<confrmIds.length; p++){
				
					var rowId = confrmIds[p];
					
					/* validating Agent name selected */
					if($("#agentNames"+rowId).val() == -1){
						$("#agentNames"+rowId).css("border","1px solid #FF0000");
						statusFlag = false;
					}
				
					/* validating bank name selected */
					if($("#bankNames"+rowId).val() == -1){
						$("#bankNames"+rowId).css("border","1px solid #FF0000");
						statusFlag = false;
					}
				
					/* validating branch name entered */
					/*if($("#branchName"+rowId).val() == "" || $("#branchName"+rowId).val() == 'undefined'){
						$("#branchName"+rowId).css("border","1px solid #FF0000");
						$("#branchName"+rowId).attr('placeholder', "Enter Branch Name");
						statusFlag = false;
					}*/
				
					/* validating deposit amount entered */
					if($("#depositAmt"+rowId).val() == "" || $("#depositAmt"+rowId).val() == 'undefined'){
						$("#depositAmt"+rowId).css("border","1px solid #FF0000");
						$("#depositAmt"+rowId).attr('placeholder', "Enter Deposit Amount");
						statusFlag = false;
					}
				
					/* validating bank deposit receipt no entered */
					if($("#receiptNo"+rowId).val() == "" || $("#receiptNo"+rowId).val() == 'undefined'){
						$("#receiptNo"+rowId).css("border","1px solid #FF0000");
						$("#receiptNo"+rowId).attr('placeholder', "Enter Receipt No");
						statusFlag = false;
					}
				
					/* validating deposit amount entered  > 0*/
					if($("#depositAmt"+rowId).val() <= 0 && $("#depositAmt"+rowId).val() != ""){
						$("#depositAmt"+rowId).css("border","1px solid #FF0000");
						$("#depositAmt"+rowId).val("");
						$("#depositAmt"+rowId).attr('placeholder', "Amount > 0");
						statusFlag = false;
					}
					
					/* validating deposit Amount must be a decimal value*/
					 var pattern = new RegExp(/^[0-9]*[.]{0,1}[0-9]*$/);
					 if(!pattern.test($("#depositAmt"+rowId).val())){
					 	$("#depositAmt"+rowId).css("border","1px solid #FF0000");
						$("#depositAmt"+rowId).val("");
						$("#depositAmt"+rowId).attr('placeholder', "valid amount");
						statusFlag = false;
					 }
					 
					 if($("#depositAmt"+rowId).val() == "."){
						$("#depositAmt"+rowId).css("border","1px solid #FF0000");
						$("#depositAmt"+rowId).val("");
						$("#depositAmt"+rowId).attr('placeholder', "valid amount");
						statusFlag = false;
					}
					
					if(statusFlag == false){
						invalidStatus = invalidStatus +1;
					}					
				}	
				return invalidStatus;		 
			}
			
			function resetForm(obj){
				 location.reload();
			}
			
			function checkDuplicatePANames(){
				var arr = [];
				var indexArr = [] ;
				var nameArr = [] ;			
				var finalArr = [] ;	
				for(var k=0; k<chkBoxIds.length; k++){
					arr[k] = $("#agtName"+chkBoxIds[k]).val().split("#")[0];
					indexArr[k] = $("#agtName"+chkBoxIds[k]).val().split("#")[1];
					nameArr[k] = $("#agtName"+chkBoxIds[k]).val().split("#")[0];
				}
				for(var i = 0; i < indexArr.length;i++){
					for(var j = i+1; j < indexArr.length;j++){
						if(nameArr[i] == nameArr[j]){
							finalArr.push(indexArr[i]);
							finalArr.push(indexArr[j]);
						}
					}
				}
								
				var sortedArr = arr.sort();
				var duplicateElemArr = [];
				for (var i = 0; i < arr.length - 1; i++) {
				    if (sortedArr[i + 1] == sortedArr[i]) {
				        duplicateElemArr.push(sortedArr[i]);
				    }
				}
				if(duplicateElemArr.length > 0){
					alert("Duplicate PA found (You can select one PA at the time)");
					for(var i = 0; i < finalArr.length; i++){
						$("#agtName"+finalArr[i]).css("border","1px solid #FF0000");
					}	
					return true;
				}
							
				return false;
			}
			
			function prepareCalendarData(obj){
				var rowId = obj.id.split("_")[1];
				var inputFieldObj = document.getElementById("depositDate"+rowId)
				var dateFormat = 'yyyy-mm-dd';
				var btnObj = obj;
				var currentDate = $('#curDate').val();
				var startDate = '2010-01-01';
				var endDate = false;
				displayCalendar(inputFieldObj, dateFormat, btnObj, currentDate, startDate, endDate);
			}
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div id="limitDiv" style="align: center;"></div>
				<h3>
					Bank Deposit Payment Details
				</h3>
				<div id="divFrm" style="display: block">
					<s:form name="Bank_Deposit_form" id="bankDepositForm">
						<s:actionerror />
						
						<s:hidden id="bankData" name="bankData" value="%{new com.skilrock.lms.common.Utility().convertJSON(bankMap)}" />
						<s:hidden id="agtData" name="agtData" value="%{new com.skilrock.lms.common.Utility().convertJSON(agtMap)}" />
						<s:hidden id="curDate" name="curDate" value="%{currentDate}" />
						<table cellpadding="2" cellspacing="0" border="1" width="auto" style="	border-collapse: collapse" id="firstInput">
							<thead>
								<tr style="background-color:#e6e6e6">
									<th colspan="9" align="right"><input type="button" class="button" id="btnAddRow" name="btnAddRow" value="+Add Rows"/></th>
								</tr>
								<tr style="background-color:#e6e6e6"  align="center">
									<th>PA Name</th>
									<th>Bank Name</th>
									<th>Branch Name</th>
									<th>Deposit Amount</th>
									<th>Bank Deposit Receipt No.</th>
									<th colspan="2">Deposit Date</th>
									<th>Available Balance</th>
									<th>Select to Payment</th>
								</tr>							
							</thead>
							<tbody id="tableBodyData">
							</tbody>
							<tfoot id="tableFooterData" style="display:none">
								<tr align="center" style="background-color:#e6e6e6">
									<td colspan="9">
										<input type="button" class="button" value="Submit Selected Payments" id="submitPayments" name="submitPayments" />
										<input type="reset" class="button" value="Reset" id="resetPayments" name="resetPayments"  onclick="resetForm(this)"/>
									</td>
								</tr>
							</tfoot>
						</table>
						
						<table cellpadding="2" cellspacing="0" border="1" style="border-collapse: collapse; display:none" id="secondInput">
							<thead>
								<tr style="background-color:#e6e6e6">
									<th colspan="9" align="center">Confirm Payment Submission Details</th>
								</tr>
								<tr style="background-color:#e6e6e6"  align="center">
									<th>PA Name</th>
									<th>Bank Name</th>
									<th>Branch Name</th>
									<th>Deposit Amount</th>
									<th>Bank Deposit Receipt No.</th>
									<th colspan="2">Deposit Date</th>
									<th>Available Balance</th>
									<th>Perform Action</th>
								</tr>							
							</thead>
							<tbody id="tableBodyData2">
							</tbody>
							<tfoot id="tableFooterData2">
								<tr>
									<td colspan="9" align="center"><b>Total Deposit Amount : <span id="totDepAmt"></span></b></td>
								</tr>
								<tr align="center" style="background-color:#e6e6e6">
									<td colspan="9">
										<input type="button" class="button" value="Confirm Payments" id="confirmPayments" name="confirmPayments" />
										<input type="button" class="button" value="Cancel" id="cancelPayments" name="cancelPayments"  onclick="resetForm(this)"/>
									</td>
								</tr>
							</tfoot>
						</table>
					</s:form>
					 <s:form id="confirmForm" action="bo_act_multi_bank_deposit_save" method="post" enctype="multipart/form-data" namespace="/com/skilrock/lms/web/accMgmt" cssStyle="display:none">
						<s:token name="token"></s:token>
						<s:hidden id="finalPaymentData" name="finalPaymentData"></s:hidden>
					</s:form>
				</div>

			</div>
		</div>
	</body>

</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_act_multi_bank_deposit_menu.jsp,v $ $Revision: 1.3 $
</code>