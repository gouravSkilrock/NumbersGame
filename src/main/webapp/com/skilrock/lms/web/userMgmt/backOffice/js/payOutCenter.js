
		
		var isLimitChanged=false;
	$(document).ready(function() {
			$('.check_one').click(function() {
					var isChecked = this.checked;
					var limitName = $(this).val();
						if (limitName == 'verificationLimit') {
							if (isChecked) {
								$('td:nth-child(3),th:nth-child(3)').show();
								$('td:nth-child(4),th:nth-child(4)').show();
							} else {
								$('td:nth-child(3),th:nth-child(3)').hide();
								$('td:nth-child(4),th:nth-child(4)').hide();
							}
						} else if (limitName == 'approvalLimit') {
							if (isChecked) {
								$('td:nth-child(5),th:nth-child(5)').show();
								$('td:nth-child(6),th:nth-child(6)').show();
							} else {
								$('td:nth-child(5),th:nth-child(5)').hide();
								$('td:nth-child(6),th:nth-child(6)').hide();
							}
						} else if (limitName == 'payLimit') {
							if (isChecked) {
								$('td:nth-child(7),th:nth-child(7)').show();
								$('td:nth-child(8),th:nth-child(8)').show();
							} else {
								$('td:nth-child(7),th:nth-child(7)').hide();
								$('td:nth-child(8),th:nth-child(8)').hide();
							}
						} else if (limitName == 'scrapLimit') {
							if (isChecked) {
								$('td:nth-child(9),th:nth-child(9)').show();
								$('td:nth-child(10),th:nth-child(10)').show();
							} else {
								$('td:nth-child(9),th:nth-child(9)').hide();
								$('td:nth-child(10),th:nth-child(10)').hide();
							}
						} else if (limitName == 'olaDepositLimit') {
							if (isChecked) {
								$('td:nth-child(11),th:nth-child(11)').show();
								$('td:nth-child(12),th:nth-child(12)').show();
							} else {
								$('td:nth-child(11),th:nth-child(11)').hide();
								$('td:nth-child(12),th:nth-child(12)').hide();
							}
						} else if (limitName == 'olaWithdrawlLimit') {
							if (isChecked) {
								$('td:nth-child(13),th:nth-child(13)').show();
								$('td:nth-child(14),th:nth-child(14)').show();
							} else {
								$('td:nth-child(13),th:nth-child(13)').hide();
								$('td:nth-child(14),th:nth-child(14)').hide();
							}
						} else if (limitName == 'dailyClaimLimit') {
							if (isChecked) {
								$('td:nth-child(15),th:nth-child(15)').show();
								$('td:nth-child(16),th:nth-child(16)').show();
							} else {
								$('td:nth-child(15),th:nth-child(15)').hide();
								$('td:nth-child(16),th:nth-child(16)').hide();
							}
						}
						else if (limitName == 'claimAnyTicket') {
							if (isChecked) {
								$('td:nth-child(17),th:nth-child(17)').show();
								$('td:nth-child(18),th:nth-child(18)').show();
								
							} else {
								$('td:nth-child(17),th:nth-child(17)').hide();
								$('td:nth-child(18),th:nth-child(18)').hide();
								
							}
						}
						else if (limitName == 'boForPwt') {
							if (isChecked) {
								$('td:nth-child(19),th:nth-child(19)').show();
								$('td:nth-child(20),th:nth-child(20)').show();
							} else {
								$('td:nth-child(19),th:nth-child(19)').hide();
								$('td:nth-child(20),th:nth-child(20)').hide();
							}
						}
						else if (limitName == 'blockAmt') {
							if (isChecked) {
								$('td:nth-child(21),th:nth-child(21)').show();
								$('td:nth-child(22),th:nth-child(22)').show();
							} else {
								$('td:nth-child(21),th:nth-child(21)').hide();
								$('td:nth-child(22),th:nth-child(22)').hide();
							}
						}
						else if (limitName == 'blockDays') {
							if (isChecked) {
								$('td:nth-child(23),th:nth-child(23)').show();
								$('td:nth-child(24),th:nth-child(24)').show();
							} else {
								$('td:nth-child(23),th:nth-child(23)').hide();
								$('td:nth-child(24),th:nth-child(24)').hide();
							}
						}
						else if (limitName == 'blockAction') {
							if (isChecked) {
								$('td:nth-child(25),th:nth-child(25)').show();
								$('td:nth-child(26),th:nth-child(26)').show();
							} else {
								$('td:nth-child(25),th:nth-child(25)').hide();
								$('td:nth-child(26),th:nth-child(26)').hide();
							}
						}
					});

				$('.updateButton').click(function() {
						// alert($(this).parent().parent().find('.update:checked').length);
						// alert($(this).parent().parent().find('.update').prop('checked'));
						var didConfirm = confirm("Are you sure You Want to Change Limit?");
						if (didConfirm == true) {
							   
						var tdDataObj = $(this).parent().parent();
						if (tdDataObj.find('.update_one').prop('checked')) {
							// alert('==row
							// value=='+tdDataObj.find('td:nth-child(2)').text());
							// alert('==row value==' + tdDataObj.find(
									// 'td:nth-child(2)').css('display'));
									
						var rowData=getRowData(tdDataObj);
						// alert(JSON.stringify(rowData));
					
						if(isLimitChanged){
							isLimitChanged=false;
							return false;
						}
						var rowList=[];
						rowList.push(rowData);		
						_ajaxCallDiv(projectName+"/com/skilrock/lms/web/userMgmt/bo_um_agentPayOutCenter_submit.action","orgPwtLimitResponse="+JSON.stringify(rowList),'pay_out_center');		
					
						} else {
						alert("Please Select At least one Limit");
							return false;
						}
						}else{
							return false;
						}
						return true;
					});
				
				$('#update_all').on("click",function() {	
						var didConfirm = confirm("Are you sure You Want to Change Limit?");
						if (didConfirm == true) {
							   var isDataChecked=true;
						  var t = $('.data_row').map(function() {
							   // $(this) is used more than once; cache it for
								// performance.
						        var $row = $(this);
								// alert($row.find('.check_one').prop('checked'));
								if ($row.find('.update_one').prop('checked')) {
								isDataChecked=false;
								
								var rowData=getRowData($row);
								// alert(JSON.stringify(rowData));
								
								return rowData;
								}
						  }).get();
						
						  if(isDataChecked)	{
							alert("Please Select At least one Limit");
							return false;
						   }
						   
						   if(isLimitChanged){
							   isLimitChanged=false;
							   return false;
						   }
							
							// alert(JSON.stringify(t));
							// _ajaxCallDiv(projectName+"/com/skilrock/pms/web/backOffice/accMgmt/Action/boPlayerWithdrawlApproveCancel.action","withdrawlApprovedCancelData="+JSON.stringify(t)+"&playerId="+$('#playerId').val()+"&status=PENDING",'pendingWithdrawl');
							_ajaxCallDiv(projectName+"/com/skilrock/lms/web/userMgmt/bo_um_agentPayOutCenter_submit.action","orgPwtLimitResponse="+JSON.stringify(t),'pay_out_center');		
									
						}else{
							return false;
						}
						
					});
				
				// for selecting all the checkbox
			
					$('#selectAll').click(function() {
							// alert('Update button');
							var isChecked = this.checked;
							// alert(isChecked);
							if (isChecked == true) {
								$('.update_one').prop("checked", true);
							}
							else{
							$('.update_one').prop("checked", false);
							}
						});

					});

				function OrgPwtLimitBean(organizationId, blockAmt, blockDays, blockAction) {
					this.organizationId = organizationId;
					this.blockAmt = blockAmt;
					this.blockDays = blockDays;
					this.blockAction = blockAction;				
				}
	
				function getRowData(tdDataObj){
							var isRowChange=true;
							tdDataObj.find('.errorMsg').html('');
							var blockAmt1=$.trim(tdDataObj.find('td:nth-child(21)').text());
							var blockAmt2=$.trim(tdDataObj.find('#blockAmt').val());
							var blockDays1=$.trim(tdDataObj.find('td:nth-child(23)').text());
							var blockDays2=$.trim(tdDataObj.find('#blockDays').val());
							var blockAction1=$.trim(tdDataObj.find('td:nth-child(25)').text());
							var blockAction2=$.trim(tdDataObj.find('#blockAction').val());

							if(blockAmt1 != blockAmt2) {
								isRowChange=false;
								var blockAmt =blockAmt2;
							} else
								var blockAmt =blockAmt1;

							if(blockDays1 != blockDays2) {
								isRowChange=false;
								var blockDays =blockDays2;
							} else
								var blockDays =blockDays1;
							
							if(blockAction1 != blockAction2) {
								isRowChange=false;
								var blockAction =blockAction2;
							} else
								var blockAction =blockAction1;

							if(isRowChange) {
								isLimitChanged=true;
								tdDataObj.find('.errorMsg').html('Please Update Value');
							}

							var rowData = new OrgPwtLimitBean($.trim(tdDataObj.find('#orgId').val()), blockAmt, blockDays, blockAction);
							return rowData;
					}
				function isValidAmount(amount) {
					// alert(mobileNum);
				    var pattern = new RegExp(/^[0-9]*[.]{0,1}[0-9]{0,2}$/);
				    // alert( pattern.test(amount) );
				    return pattern.test(amount);
				}
				
				function isValidLimit() {
					for (var i = 0, j = arguments.length; i < j; i++) {
						
						if(!isValidAmount(arguments[i])){
							return false;
							}
				      
				    }
					return true;
				}
				
parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/userMgmt/backOffice/js/payOutCenter.js,v $"] = "$Id: payOutCenter.js,v 1.3 2016/10/31 09:46:23 neeraj Exp $";
