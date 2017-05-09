<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.GetDate"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires",  0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<style type="text/css"></style>
		<script type="text/javascript"
			src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script>
			var methodsMap;
			$(document).ready(function() {
				var resp = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/bo_im_methodIdMap.action");
				methodsMap = JSON.parse(resp.data);

			});

			function getInvoiceList(gameId) {
				if(gameId==-1)
					$("#resultDiv").html("");
				else {
					$("#resultDiv").html("<center><img src='<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif' alt='loading' /></center>");
					var resp = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/bo_im_updateInvoicingMethod_Search.action?gameId="+gameId);
					$("#resultDiv").html(resp.data);
				}
				$("#td_type_0").html("BOOLEAN");
			}

			function validateBoolValues(element) {
				$(".boolInputValue").val(element);
			}

			function validateTextValues(event) {
				if((event.keyCode<48 || event.keyCode>57) && (event.keyCode<96 || event.keyCode>105)) {
					if(event.keyCode!=8 && event.keyCode!=37 && event.keyCode!=39 && event.keyCode!=46) {
						event.preventDefault();
						return;
					}
				}
			}

			function selectAllCB(element, onActivity) {
				$('.'+onActivity).each(function() {
					this.checked = $(element).is(":checked");
				});

				if(onActivity!='selAllOrg') {
					$(element).parent().addClass("updateTheme");
					$('.selAllOrg').each(function() {
						this.checked = true;
					});
				}
			}

			function selectAllSB(elementId, element, onActivity) {
				var value = $('#'+elementId+' option:selected').html();
				if(confirm("Are you want to Update Value to "+value+" ?")) {
					changeMethodTypeBoxAll(element.value);	
					$('.'+onActivity).each(function() {
						$(this).val($(element).val());
					});

					$(element).parent().addClass("updateTheme");
					$('.selAllOrg').each(function() {
						this.checked = true;
					});
				}
			}

			function selectAllTB(element, onActivity) {
				if(confirm("Are you want to Update Value to "+$(element).val()+" ?")) {
					$('.'+onActivity).each(function() {
						if($(element).val() == '') {
							$(this).val('0.00');
						} else {
							$(this).val($(element).val());
						}
					});

					$(element).parent().addClass("updateTheme");
					$('.selAllOrg').each(function() {
						this.checked = true;
					});
				}
			}
			
			function changeMethodTypeBoxAll(id){
				var type;
				$.each(methodsMap.arg0, function(key, value) {
					if(key==id)
						type=value;
				});
				var div = $("#top_form");
				$(div).find('div').each(function() {
					if($(this).attr("id") != undefined) {
						if($(this).attr("id").indexOf("td_type_")!=-1) {
							$(this).html(type);
				        }
			        }
				});
			
			}

			function selectCB(element) {
				var className = $(element).parent().attr('class');
				if(className!='updateTheme') {
					$(element).parent().removeClass("selectedTheme");
					$(element).parent().removeClass("deselectedTheme");
					$(element).parent().addClass("updateTheme");
				} else {
					if($(element).is(":checked")) {
						$(element).parent().removeClass("updateTheme");
						$(element).parent().addClass("selectedTheme");
					} else {
						$(element).parent().removeClass("updateTheme");
						$(element).parent().addClass("deselectedTheme");
					}
				}

				if($(element).parent().parent().find('.selAllOrg').is(":checked")==false) {
					$(element).parent().parent().find('.selAllOrg').prop('checked', true);
				}
			}

			function changeMethodValueBoxAll(element) {
				var type;
				$.each(methodsMap.arg0, function(key, value) {
					if(key==element)
						type=value;
				});
				$("#mainSEL").css("display", "none");
				$("#mainTXT").css("display", "none");

				if(type=='BOOLEAN')
					$("#mainSEL").css("display", "block");
				else
					$("#mainTXT").css("display", "block");

				var div = $("#top_form");
				$(div).find('div').each(function() {
					if($(this).attr("id") != undefined) {
						if(type=='BOOLEAN') {
			            	if($(this).attr("id").indexOf("td_sel_")!=-1) {
			            		$(this).css("display", "block");
			            		$(this).children(".boolInputValue").val("YES");
			            	} else if($(this).attr("id").indexOf("td_txt_")!=-1) {
			            		$(this).css("display", "none");
			            	}
			            } else {
			            
			            	if($(this).attr("id").indexOf("td_sel_")!=-1) {
			            		$(this).css("display", "none");
			            	} else if($(this).attr("id").indexOf("td_txt_")!=-1) {
			            		$(this).css("display", "block");
			            		$(this).children(".textInputValue").val("");
			            	}
			            }
		            }
		        });
			}

			function changeMethodTypeBox(methodId,orgId) {
				var type;
				$.each(methodsMap.arg0, function(key, value) {
					if(key==methodId)
						type=value;
				});
				$("#td_type_"+orgId).html(type);
			}

			function changeMethodValueBox(methodId, orgId) {
			var type;
				$.each(methodsMap.arg0, function(key, value) {
					if(key==methodId)
						type=value;
				});
				if(type=='BOOLEAN') {
					$("#td_sel_"+orgId).css("display", "block");
					$("#td_txt_"+orgId).css("display", "none");

					$("#td_sel_"+orgId).children(".boolInput").val("YES");
				} else {
					$("#td_sel_"+orgId).css("display", "none");
					$("#td_txt_"+orgId).css("display", "block");

					$("#td_txt_"+orgId).children(".textInputValue").val("");
				}
			}

			function updateValues() {
				var isNotSelect = true;
				var data = $('#dataTable tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('.selAllOrg').prop('checked')) {
						isNotSelect = false;

						var methodValuePage = "";
						if($row.find('.boolInput').css("display") == "block")
							methodValuePage = $row.find('.boolInputValue').val();
						else if($row.find('.textInput').css("display") == "block")
							methodValuePage = $row.find('.textInputValue').val();

						return {
							orgId : $row.find('.orgId').val(),
							methodId : $row.find('.methodName').val(),
							//methodValue : $row.find('.methodValue').val()
							methodValue : methodValuePage
						};
					}
				}).get();

				if(isNotSelect) {
					alert('Please Select Any Organization.');
				} else {
					if (confirm("Are you sure, you want to update values ?")) {
						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/bo_im_updateInvoicingMethod_Update.action", "fetchAgentRetailerData", "gameId="+$("#gameId").val()+"&jsonParamData="+JSON.stringify(data));
						alert("Values Updated Successfully.");						
						$("html,body").scrollTop(55);
					}
				}
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<div align="left">
					<h3>
						Update Agent Invoicing Method
					</h3>
				</div>
				<s:select list="gameMap" id="gameId" name="gameId" label="Please Select Game" headerKey="-1" headerValue="--Please Select--" onchange="getInvoiceList(this.value);" cssClass="option" />
				<br /><br />
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_updateInvoicingMethod_Menu.jsp,v $ $Revision: 1.3 $
</code>