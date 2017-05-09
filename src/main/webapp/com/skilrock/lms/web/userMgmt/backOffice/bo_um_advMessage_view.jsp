<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/bo_um_advMessage.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

		<!-- bootstrap 3.0.2 -->
		<link
			href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=request.getContextPath()%>/css/bootstrap/font-awesome.min.css"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=request.getContextPath()%>/css/bootstrap/ionicons.min.css"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=request.getContextPath()%>/css/bootstrap/minimal/blue.css"
			rel="stylesheet" type="text/css" />
		<link href="<%=request.getContextPath()%>/css/bootstrap/AdminLTE.css"
			rel="stylesheet" type="text/css" />
		<script
			src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js"
			type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/bootstrap/icheck.js"
			type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/js/bootstrap/app.js"
			type="text/javascript"></script>

		<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 10]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
		<![endif]-->


		<style type="text/css">
.chromestyle ul li a {
	border-right: 1px solid #DADADA;
	color: #494949;
	font-size: 12px;
	font-weight: bold;
	margin: 0;
	padding: 4px 7px;
	text-decoration: none;
	width: 220px;
}

.showData {
	cursor: pointer;
}
</style>

		<script type="text/javascript"><!--
			$(document).ready(function() {
				/*
				//iCheck for checkbox and radio inputs
				$('input[type="checkbox"]').iCheck({
					checkboxClass: 'icheckbox_minimal-blue',
					radioClass: 'iradio_minimal-blue'
				});
				//When unchecking the checkbox
				$("#check-all").on('ifUnchecked', function(event) {
					//Uncheck all checkboxes
					$("input[type='checkbox']", ".table-mailbox").iCheck("uncheck");
				});
				//When checking the checkbox
				$("#check-all").on('ifChecked', function(event) {
					//Check all checkboxes
					$("input[type='checkbox']", ".table-mailbox").iCheck("check");
				});
				*/

				/*
				//Handle starring for glyphicon and font awesome
				$(".fa-star, .fa-star-o, .glyphicon-star, .glyphicon-star-empty").click(function(e) {
					e.preventDefault();
					var glyph = $(this).hasClass("glyphicon");
					var fa = $(this).hasClass("fa");

					if (glyph) {
						$(this).toggleClass("glyphicon-star");
						$(this).toggleClass("glyphicon-star-empty");
					}

					if (fa) {
						$(this).toggleClass("fa-star");
						$(this).toggleClass("fa-star-o");
					}
				});
				*/

				var json;

				$("#subBox").hide();
				$("#subAction").hide();

				checkMessageCount = function() {
					if($('#messageTable tr').length == 0) {
						$('#messageTable').append('<tr class="subRows"><td colspan=6><center><s:text name="msg.no.newMsg"/>.</center></td></tr>');
					}
				};

				$(document).on('click','.showData', {} ,function(e) {
					var dataArr=$(this).parent().attr('data_message').split('-');
					fetchWebMessageDetail(dataArr[0],dataArr[1]);
				});

				createMessageTable = function(creteria, messageNumber) {
					$("#mainBox").show();
					$("#subBox").hide();
					$("#mainAction").hide();
					$("#subAction").hide();
					$("#creteria").val(creteria);
					$('.pull-right').show();
					$('#noOfMessages').html("");

					$('.subRows').remove();
					if(creteria == 'ALL') {
						var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_fetchWebMessages.action?messages=[{\"status\":\"READ\"}, {\"status\":\"UNREAD\"}]&messageNumber="+messageNumber);
						json = (JSON.parse(_resp)).responseData;
						$.each(json.messageList, function(key, value) {
							if(value.status == 'READ' || value.status == 'UNREAD') {
								$('#messageTable').append('<tr class="subRows" data_message="'+value.messageId+'-'+value.status+'" id="tr_'+value.messageId+'"><td class="small-col"><input type="checkbox" name="messageIds" value='+value.messageId+' class="child" /><\/td><td class="small-col"><i id="i_imp_'+value.messageId+'" class="fa"><\/i><\/td><td class="name showData" >'+value.creatorUserName+'<\/td><td class="subject showData">'+value.messageSubject+' - <i>'+value.messageContent+'<\/i><\/td><td class="time showData">'+value.messageDate+'<\/td></tr>');
								$('#tr_'+value.messageId).addClass(value.status.toLowerCase());
								if(value.mandatoryStatus == 'YES') {
									$('#i_imp_'+value.messageId).addClass("fa-star");
								} else if(value.mandatoryStatus == 'NO') {
									$('#i_imp_'+value.messageId).addClass("fa-star-o");
								}
							}
						});
						$("#mainAction").show();
					} else if(creteria == 'READ' || creteria == 'UNREAD') {
						var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_fetchWebMessages.action?messages=[{\"status\":\""+creteria+"\"}]&messageNumber="+messageNumber);
						json = (JSON.parse(_resp)).responseData;
						$.each(json.messageList, function(key, value) {
							if(value.status == creteria) {
								$('#messageTable').append('<tr class="subRows" data_message="'+value.messageId+'-'+value.status+'"  id="tr_'+value.messageId+'"><td class="small-col"><\/td><td class="small-col"><i id="i_imp_'+value.messageId+'" class="fa"><\/i><\/td><td class="name showData">'+value.creatorUserName+'<\/td><td class="subject showData">'+value.messageSubject+' - <i>'+value.messageContent+'<\/i><\/td><td class="time showData">'+value.messageDate+'<\/td></tr>');
								$('#tr_'+value.messageId).addClass(creteria.toLowerCase());
								if(value.mandatoryStatus == 'YES') {
									$('#i_imp_'+value.messageId).addClass("fa-star");
								} else if(value.mandatoryStatus == 'NO') {
									$('#i_imp_'+value.messageId).addClass("fa-star-o");
								}
							}
						});
					} else if(creteria == 'IMPORTANT') {
						var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_fetchWebMessages.action?messages=[{\"status\":\"IMPORTANT\"}]&messageNumber="+messageNumber);
						json = (JSON.parse(_resp)).responseData;
						$.each(json.messageList, function(key, value) {
							if(value.mandatoryStatus == 'YES' && value.status != 'DELETE' && value.status != 'REMOVE') {
								$('#messageTable').append('<tr class="subRows" data_message="'+value.messageId+'-'+value.status+'"  id="tr_'+value.messageId+'"><td class="small-col"><\/td><td class="small-col"><i id="i_imp_'+value.messageId+'" class="fa"><\/i><\/td><td class="name showData">'+value.creatorUserName+'<\/td><td class="subject showData">'+value.messageSubject+' - <i>'+value.messageContent+'<\/i><\/td><td class="time">'+value.messageDate+'<\/td></tr>');
								$('#tr_'+value.messageId).addClass(value.status.toLowerCase());
								$('#i_imp_'+value.messageId).addClass("fa-star");
							}
						});
					} else if(creteria == 'DELETE') {
						var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_fetchWebMessages.action?messages=[{\"status\":\"DELETE\"}]&messageNumber="+messageNumber);
						json = (JSON.parse(_resp)).responseData;
						$.each(json.messageList, function(key, value) {
							if(value.status == creteria) {
								$('#messageTable').append('<tr class="subRows" data_message="'+value.messageId+'-'+value.status+'"  id="tr_'+value.messageId+'"><td class="small-col"><\/td><td class="small-col"><i id="i_imp_'+value.messageId+'" class="fa"><\/i><\/td><td class="name showData">'+value.creatorUserName+'<\/td><td class="subject showData">'+value.messageSubject+' - <i>'+value.messageContent+'<\/i><\/td><td class="time">'+value.messageDate+'<\/td></tr>');
								if(value.mandatoryStatus == 'YES') {
									$('#i_imp_'+value.messageId).addClass("fa-star");
								} else if(value.mandatoryStatus == 'NO') {
									$('#i_imp_'+value.messageId).addClass("fa-star-o");
								}
							}
						});
					}

					if($('#noOfMessages').html() == '') {
						if(json.noOfMessages == 0) {
							$('#noOfMessages').html("Showing 0-0/0");
						} else {
							var a = parseInt($('#pageNumber').val())+1;
							var b = parseInt(json.messageList.length)+parseInt($('#pageNumber').val());
							if(b > parseInt(json.noOfMessages)) {
								b = json.noOfMessages;
							}
							var c = json.noOfMessages;
							$('#noOfMessages').html("Showing "+a+"-"+b+"/"+c);
						}
					}

					checkMessageCount();
				};

				$('.nav li a').click(function(e) {
					$('#pageNumber').val("0");

					$('.nav li.active').removeClass('active');
					var obj = $(this).parent();
					if (!obj.hasClass('active')) {
						obj.addClass('active');
					}
					e.preventDefault();
				});

				fetchWebMessageDetail = function (messageId, status) {
					var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_fetchWebMessageDetail.action?messageId="+messageId+"&status="+status);
					var jsonStatus = (JSON.parse(_resp)).isSuccess;
					if(jsonStatus == true) {
						var jsonData = (JSON.parse(_resp)).responseData;

						$("#mainBox").hide();
						$("#subBox").show();
						$("#mainAction").hide();
						$("#subAction").show();
						$(".clearfix").hide();

						if($("#creteria").val() == 'DELETE') {
							$(".deleteButton").hide();
						} else {
							$(".deleteButton").show();
						}

						$("#messageId").val(jsonData.messageId);
						$("#subject").html(jsonData.messageSubject);
						$("#content").html(jsonData.messageContent);
						$("#sendDate").html(jsonData.messageDate);
						//$("#importent").html(jsonDetail.mandatoryStatus);
						$("#senderName").html(jsonData.creatorUserName);

						$('#tr_'+jsonData.messageId).removeClass("unread");
						$('#tr_'+jsonData.messageId).addClass("read");
					}
				};

				searchWebMessages = function () {
					var searchText = $("#search").val();

					$("#mainBox").show();
					$("#subBox").hide();
					$("#mainAction").show();
					$("#subAction").hide();
					$('.nav li.active').removeClass('active');
					$('.pull-right').hide();

					$('.subRows').remove();
					var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_searchWebMessages.action?messages="+searchText);
					json = (JSON.parse(_resp)).responseData;
					$.each(json, function(key, value) {
						$('#messageTable').append('<tr class="subRows" id="tr_'+value.messageId+'"><td class="small-col"><input type="checkbox" name="messageIds" value='+value.messageId+' class="child" /><\/td><td class="small-col"><i id="i_imp_'+value.messageId+'" class="fa"><\/i><\/td><td class="name">'+value.creatorUserName+'<\/td><td class="subject"><a href="#" onClick="fetchWebMessageDetail(\''+value.messageId+'\',\''+value.status+'\');">'+value.messageSubject+' - <i>'+value.messageContent+'<\/i></a><\/td><td class="time">'+value.messageDate+'<\/td></tr>');
						$('#tr_'+value.messageId).addClass(value.status.toLowerCase());
						if(value.mandatoryStatus == 'YES') {
							$('#i_imp_'+value.messageId).addClass("fa-star");
						} else if(value.mandatoryStatus == 'NO') {
							$('#i_imp_'+value.messageId).addClass("fa-star-o");
						}
					});

					checkMessageCount();
				};

				changeMultipleMessageStatus = function(action) {
					if($("input[name=messageIds]:checkbox:checked").length == 0) {
						alert("Please Select Atleast One Message.");
					} else {
						var creteria = $("#creteria").val();
						var data = $('#messageTable tbody tr').map(function() {
							var $row = $(this);
							if ($row.find('.child').prop('checked')) {
								return {
									messageId : $row.find('.child').val()
								};
							}
						}).get();
						var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_changeMessageStatus.action?messages="+JSON.stringify(data)+"&status="+action);

						$("#selectAll").prop("checked", false);
						createMessageTable(creteria, 0);
					}
				};

				returnBack = function() {
					var creteria = $('#creteria').val();
					var pageNumber = $('#pageNumber').val();

					$("#mainBox").show();
					$("#subBox").hide();
					$(".clearfix").show();
					if(creteria == 'ALL') {
						$("#mainAction").show();
					} else {
						$("#mainAction").hide();
					}
					$("#subAction").hide();

					createMessageTable(creteria, pageNumber);
				};

				deleteMessage = function() {
					var messageId = $("#messageId").val();
					var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_deleteWebMessage.action?messageId="+messageId);
					$('#tr_'+messageId).remove();
					checkMessageCount();

					$("#mainBox").show();
					$("#subBox").hide();
					if($("#creteria").val() == 'ALL') {
						$("#mainAction").show();
					} else {
						$("#mainAction").hide();
					}
					$("#subAction").hide();
					//createMessageTable($("#creteria").val(), 0);
				};

				$('.selectAll').on('click', function() {
					var check = this.checked;
					$('.child').each(function() {
						this.checked = check;               
					});
				});
				$(document.body).on('click', '.child', function(){
					var allSize = $(this).closest('#messageTable').find('.child').length;
					var checkedSize = $(this).closest('#messageTable').find('.child:checked').length;
					if (allSize == checkedSize) {
						$(".selectAll").prop('checked', true);
					} else {
						$(".selectAll").prop('checked', false);
					}
				});

				fetchPaginationResult = function(direction) {
					if(direction == 'NEXT') {
						if(parseInt($('#pageNumber').val())+parseInt(json.messageCount) >= json.noOfMessages) {
							return;
						} else {
							$('#pageNumber').val(parseInt($('#pageNumber').val())+parseInt(json.messageCount));
						}
					} else if(direction == 'PREVIOUS') {
						if($('#pageNumber').val() == 0) {
							return;
						} else {
							$('#pageNumber').val(parseInt($('#pageNumber').val())-parseInt(json.messageCount));
						}
					}
					//$('#noOfMessages').html("Showing "+$('#pageNumber').val()+"-"+json.messageList.length+"/"+json.noOfMessages);
					var a = parseInt($('#pageNumber').val())+1;
					var b = parseInt(json.messageList.length)+parseInt($('#pageNumber').val());
					if(b > parseInt(json.noOfMessages)) {
						b = json.noOfMessages;
					}
					var c = json.noOfMessages;
					$('#noOfMessages').html("Showing "+a+"-"+b+"/"+c);

					$(".table-responsive").hide().fadeIn(500);
					createMessageTable($("#creteria").val(), $('#pageNumber').val());
				};
			});
		--></script>
	</head>

	<body onload="createMessageTable('ALL', 0);">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<!-- MAILBOX BEGIN -->
		<div class="mailbox row">
			<div class="col-xs-offset-1 col-xs-10">
				<br />
				<div class="box box-solid">
					<div class="box-body">
						<div class="row">
							<div class="col-md-3 col-sm-3">
								<div class="box-header">
									<i class="fa fa-inbox"></i>
									<h3 class="box-title">
										<s:text name="label.msg.inbox" />
									</h3>
								</div>
								<div style="margin-top: 15px;">
									<ul class="nav nav-pills nav-stacked">
										<li class="header">
											<s:text name="label.folders" />
										</li>
										<li class="active">
											<a href="#" onclick="createMessageTable('ALL', 0);"><i
												class="fa fa-inbox"></i> <s:text name="label.inbox" /> </a>
										</li>
										<li>
											<a href="#" onclick="createMessageTable('READ', 0);"><i
												class="fa fa-pencil-square-o"></i> <s:text name="label.read" />
											</a>
										</li>
										<li>
											<a href="#" onclick="createMessageTable('UNREAD', 0);"><i
												class="fa fa-mail-forward"></i> <s:text name="label.unread" />
											</a>
										</li>
										<li>
											<a href="#" onclick="createMessageTable('IMPORTANT', 0);"><i
												class="fa fa-star"></i> <s:text name="label.imp" /> </a>
										</li>
										<li>
											<a href="#" onclick="createMessageTable('DELETE', 0);"><i
												class="fa fa-folder"></i> <s:text name="label.del" /> </a>
										</li>
									</ul>
								</div>
							</div>
							<div class="col-md-9 col-sm-9">
								<div class="row pad">
									<div class="col-sm-6">
										<div class="btn-group">
											<div id="mainAction">
												<label style="margin-right: 10px;">
													<input type='checkbox' class='selectAll' id='selectAll' />
												</label>
												<button type="button"
													class="btn btn-default btn-sm btn-flat dropdown-toggle"
													data-toggle="dropdown">
													<s:text name="label.action" />
													<span class="caret"></span>
												</button>
												<ul class="dropdown-menu" role="menu">
													<li>
														<a href="#" onclick="changeMultipleMessageStatus('READ');"><s:text
																name="label.mark.read" /> </a>
													</li>
													<!--
													<li>
														<a href="#" onclick="changeMultipleMessageStatus('UNREAD');">Mark as unread</a>
													</li>
													-->
													<li class="divider"></li>
													<li>
														<a href="#"
															onclick="changeMultipleMessageStatus('DELETE');"><s:text
																name="label.del.msg" /> </a>
													</li>
												</ul>
											</div>
											<div id="subAction">
												<input type="button"
													class="btn btn-default btn-sm btn-flat backButton"
													value="Back" onclick="returnBack();" />
												<input type="button"
													class="btn btn-default btn-sm btn-flat deleteButton"
													value="Delete" onclick="deleteMessage();" />
											</div>
										</div>
									</div>
									<div class="col-sm-6 search-form">
										<div class="input-group">
											<input type="text" class="form-control input-sm"
												placeholder="Search" id="search">
												<div class="input-group-btn">
													<button type="submit" name="q"
														class="btn btn-sm btn-primary"
														onclick="searchWebMessages();">
														<i class="fa fa-search"></i>
													</button>
												</div>
										</div>
									</div>
								</div>
								<div class="table-responsive">
									<div id="mainBox">
										<s:hidden id="creteria" />
										<table class="table table-mailbox" id="messageTable">
										</table>
									</div>
									<div id="subBox" style="background-color: #f6f6f6;">
										<s:hidden name="messageId" id="messageId" value="" />
										<p style="font-weight: bold; font-size: 18px;" id="subject"></p>
										<br />
										<p id="senderName" style="font-weight: bold; float: left;"></p>
										<p id="sendDate" style="float: right;"></p>
										<br />
										<br />
										<p id="content"></p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box-footer clearfix">
						<div class="pull-right">
							<small id="noOfMessages"></small>
							<s:hidden name="pageNumber" id="pageNumber" value="0" />
							<button class="btn btn-xs btn-primary"
								onclick="fetchPaginationResult('PREVIOUS');">
								<i class="fa fa-caret-left"></i>
							</button>
							<button class="btn btn-xs btn-primary"
								onclick=fetchPaginationResult('NEXT');;
>
								<i class="fa fa-caret-right"></i>
							</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-1"></div>
		</div>
		<!-- MAILBOX END -->
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_advMessage_view.jsp,v $ $Revision: 1.3 $
</code>