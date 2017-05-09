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
			});

			function selectAll(element, onActivity) {
				$('.'+onActivity).each(function() {
					this.checked = $(element).is(":checked");
				});

				if(onActivity!='selRetailer') {
					$(element).parent().addClass("updateTheme");
					$('.selRetailer').each(function() {
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

				if($(element).parent().parent().find('.selRetailer').is(":checked")==false) {
					$(element).parent().parent().find('.selRetailer').prop('checked', true);
				}
			}

			function fetchRetailerData() {
				if($("#agentOrgId").val() == -1) {
					$("#errorDiv").html(i18nOb.prop('error.agent.empty'));
					$("#resultDiv").html("");
				} else {
					$("#errorDiv").html("");
					$("#resultDiv").html("<img src='<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif' alt='loading' />");
					var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_updateSoldTicketClaimCriteria_Details.action?agentOrgId="+$("#agentOrgId").val());
					$("#resultDiv").html(text.data);
				}
				return false;
			}

			function setToDefault() {
				var isNotSelect = true;
				var data = $('#dataTable tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('.selRetailer').prop('checked')) {
						isNotSelect = false;
						return {
							retOrgId : $row.find('.retOrgId').val()
						};
					}
				}).get();

				if(isNotSelect) {
					alert('Please Select Any Retailer.');
				} else {
					if (confirm("Do you really want to update values to default for the selected organizations ?")) {
						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_retailerClaimCriteriaSetToDefault.action", "fetchRetailerData", "jsonParamData="+JSON.stringify(data));
						alert("Value Updated Successfully.");
					}
				}
			}

			function updateValues() {
				var isNotSelect = true;
				var data = $('#dataTable tbody tr').map(function() {
					var $row = $(this);
					if ($row.find('.selRetailer').prop('checked')) {
						isNotSelect = false;
						return {
							retOrgId : $row.find('.retOrgId').val(),
							selfRetailer : $row.find('.selfRetailer').is(":checked"),
							selfAgent : $row.find('.selfAgent').is(":checked"),
							otherRetailerSameAgent : $row.find('.otherRetailerSameAgent').is(":checked"),
							otherRetailer : $row.find('.otherRetailer').is(":checked"),
							otherAgent : $row.find('.otherAgent').is(":checked"),
							atBO : $row.find('.atBO').is(":checked")
						};
					}
				}).get();

				if(isNotSelect) {
					alert('Please Select Any Retailer.');
				} else {
					if (confirm("Are you sure, you want to update values ?")) {
						_ajaxUnsync("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_retailerClaimCriteriaUpdate.action", "fetchRetailerData", "jsonParamData="+JSON.stringify(data));
						alert("Value Updated Successfully.");
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
					<h3>Update Sold Ticket Claim Criteria</h3>
				</div>
				<s:form action="#" onsubmit="return fetchRetailerData();">
					<div id="errorDiv" style="color:red;"></div>
					<s:select name="agentOrgId" id="agentOrgId" key="AGENT" list="agentList" listKey="orgId" listValue="orgName" headerKey="-1" headerValue="--Please Select--" cssClass="option" />
					<s:submit key="Search" align="right" cssClass="button" />
				</s:form>
				<div id="resultDiv"></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_updateSoldTicketClaimCriteria_Menu.jsp,v $ $Revision: 1.3 $
</code>