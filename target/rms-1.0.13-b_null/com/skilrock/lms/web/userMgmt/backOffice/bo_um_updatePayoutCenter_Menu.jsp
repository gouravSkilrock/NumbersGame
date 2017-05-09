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
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		<script>
			$(document).ready(function() {
				$("#agentListTr").hide();

				$("input[name='selAgtRet']").change(function() {
					$("#agentOrgId").val(-1);
					$("#resultDiv").html("");
					$("#errorDiv").html("");

					if($(this).val()=='AGENT') {
						$("#agentListTr").hide();
					} else if($(this).val()=='RETAILER') {
						$("#agentListTr").show();
					}
				});
			});

			function validateNumValues(event) {
				//alert(event.keyCode);

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

			function fetchAgentRetailerData() {
				var text;
				if($("input[name='selAgtRet']:checked").val()=='AGENT') {
					$("#resultDiv").html("<img src='<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif' alt='loading' />");
					text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_updatePayoutCenter_Details.action?orgType=AGENT&agentOrgId=0");
					$("#resultDiv").html(text.data);
				} else if($("input[name='selAgtRet']:checked").val()=='RETAILER') {
					if($("#agentOrgId").val() == -1) {
						$("#errorDiv").html(i18nOb.prop('error.agent.empty'));
						$("#resultDiv").html("");
					} else {
						$("#errorDiv").html("");
						$("#resultDiv").html("<img src='<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif' alt='loading' />");
						text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_updatePayoutCenter_Details.action?orgType=RETAILER&agentOrgId="+$("#agentOrgId").val());
						$("#resultDiv").html(text.data);
					}
				}

				return false;
			}

			function setToDefault() {
				var isNotSelect = true;
				var data = $('#dataTable tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('.selAllOrg').prop('checked')) {
						isNotSelect = false;
						return {
							orgId : $row.find('.orgId').val()
						};
					}
				}).get();

				if(isNotSelect) {
					alert('Please Select Any Organization.');
				} else {
					if (confirm("Do you really want to update values to default for the selected organizations ?")) {
						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_updatePayoutCenterSetToDefault.action", "fetchAgentRetailerData", "orgType="+$("input[name='selAgtRet']:checked").val()+"&jsonParamData="+JSON.stringify(data));
						alert("Value Updated Successfully.");
					}
				}
			}

			function updateValues() {
				var isNotSelect = true;
				var limit=true;
				var blank=true;
				var data = $('#dataTable tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('.selAllOrg').prop('checked')) {
						isNotSelect = false;
						$row.find('.minClaimAmount').parent().removeClass("invalidTheme");
						$row.find('.maxClaimAmount').parent().removeClass("invalidTheme");
						$row.find('.maxClaimAmount').parent().removeClass("deselectedTheme");
						$row.find('.minClaimAmount').parent().removeClass("deselectedTheme");
						$row.find('.maxClaimAmount').parent().removeClass("selectedTheme");
						$row.find('.minClaimAmount').parent().removeClass("selectedTheme");

						if(parseFloat($row.find('.minClaimAmount').val()) > parseFloat($row.find('.maxClaimAmount').val())){
							limit=false;
							$row.find('.minClaimAmount').parent().addClass("invalidTheme");
							$row.find('.maxClaimAmount').parent().addClass("invalidTheme");
						}
						
						if(!($row.find('.verificationLimit').val()!='' && $row.find('.claimLimit').val()!='' && $row.find('.minClaimAmount').val()!='' && $row.find('.maxClaimAmount').val()!='')){
							blank=false;
						}
						
						return {
							orgId : $row.find('.orgId').val(),
							claimAtSelf : $row.find('.claimAtSelf').is(":checked"),
							claimAtOther : $row.find('.claimAtOther').is(":checked"),
							verificationLimit : $row.find('.verificationLimit').val(),
							claimLimit : $row.find('.claimLimit').val(),
							minClaimAmount : $row.find('.minClaimAmount').val(),
							maxClaimAmount : $row.find('.maxClaimAmount').val()
						};
					}
				}).get();

				if(!limit){
					alert("Max Claim Amount should be greater than Min Claim Amount")
				}

				if(!blank){
					alert("Field can't be empty!");
				}

				if(isNotSelect) {
					alert('Please Select Any Organization.');
				} else if(limit && blank){
					if (confirm("Are you sure, you want to update values ?")) {
						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_updatePayoutCenterUpdate.action", "fetchAgentRetailerData", "orgType="+$("input[name='selAgtRet']:checked").val()+"&jsonParamData="+JSON.stringify(data));
						alert("Values Updated Successfully.");
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
					<h3>Update Payout Center</h3>
				</div>
					<s:form action="#" onsubmit="return fetchAgentRetailerData();" theme="simple">
						<table>
							<tr>
								<td colspan="2">
									<div id="errorDiv" style="color:red;"></div>
								</td>
							</tr>
							<tr>
								<td>Please Select</td>
								<td><s:radio id="selAgtRet" name="selAgtRet" list="#{'AGENT':getText('AGENT'), 'RETAILER':getText('Retailer')}" value="%{'AGENT'}" /></td>
							</tr>
							<tr id="agentListTr">
								<td>Select <s:text name="AGENT" /></td>
								<td><s:select name="agentOrgId" id="agentOrgId" list="agentList" listKey="orgId" listValue="orgName" headerKey="-1" headerValue="--Please Select--" cssClass="option" /></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><s:submit key="Search" align="right" cssClass="button" /></td>
							</tr>
						</table>
					</s:form>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_updatePayoutCenter_Menu.jsp,v $ $Revision: 1.3 $
</code>