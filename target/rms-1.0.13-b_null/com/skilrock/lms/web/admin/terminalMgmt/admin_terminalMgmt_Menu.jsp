<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script>
			function fetchRet(val) {
				//clearDiv();
				//var modelId = _id.o("model").selected;
				var _resp = _ajaxCall(projectName
						+ "/com/skilrock/lms/admin/admin_terminalMgmt_Search.action?search_type="
						+ val);
				_id.i("result", _resp.data);
			}
			function fetchRett(val) {
				//clearDiv();
				var modelId = $("#model  option:selected").text();
				var modelIdVal = $("#model  option:selected").val();
				var brandId = $("#brand  option:selected").text();
				var brandIdVal = $("#brand  option:selected").val();
				var _resp = _ajaxCall(projectName
						+ "/com/skilrock/lms/admin/admin_terminalMgmt_Search.action?search_type="
						+ val+"&modelId="+modelId);
						_id.i("result", _resp.data);
						$("select#brand").val(brandIdVal);
						chageBrandList(brandIdVal, 'model', 'model_list','-1');
						$("select#model").val(modelIdVal);
						
				$("#searchTable").show();
			}
			
			function fillChk(val) {
				var parentChk = _id.o("P" + val).checked;
				var tab = _id.o("tab" + val);
				var chkBx = tab.getElementsByTagName("input");
				for ( var i = 0; i < chkBx.length; i++) {
					if (chkBx[i].type == "checkbox") {
						chkBx[i].checked = parentChk;
					}
				}
			}
			function selAllChkBx() {
				var tab = _id.o("searchTable");
				var chkBx = tab.getElementsByTagName("input");
				var selAll = _id.o("selAll").checked;
				var dis = false;
				if (selAll) {
					dis = true;
				}
				for ( var i = 0; i < chkBx.length; i++) {
					if (chkBx[i].type == "checkbox" && chkBx[i].id != "selAll") {
						chkBx[i].checked = false;
						chkBx[i].disabled = dis;
					}
				}
			}
			
			function chageBrandList(invValue, updOptId, fetOptId, val) {

	var valArr = invValue.split("-");
	invValue = valArr[0];
	// alert(valArr[0]);

	var brandObj = _id.o(updOptId);
	brandObj.options.length = 1;
	var selObj = _id.o(fetOptId);
	var text, value, count = 1;
	var brandValueArr;
	for ( var i = 0, l = selObj.length; i < l; i = i + 1) {
		value = selObj.options[i].value;
		brandValueArr = value.split("-");
		text = selObj.options[i].text;
		// alert(text+" = "+ value);
		if (brandValueArr[1] == invValue || invValue == -1) {
			var opt = new Option(text, value);
			brandObj.options[count] = opt;

			if (value == val) {
				brandObj.options.selected = true;
			}
			count = count + 1;
		}
	}
}
function changeModelList(invValue, updOptId, fetOptId, val) {
				var modelId = _id.o("model").selected;
				var _resp = _ajaxCall(projectName
						+ "/com/skilrock/lms/admin/getVersionByModel.action?model="
						+ modelId.value);
				_id.i("result", _resp.data);
				
}
			
		</script>
		
	</head>

	<body onload="fetchRet('AgentWise')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Terminal Management
				</h3>
				<div id="tktErrMsgDiv"></div>
				<s:form name="terminalMgmt" id="terminalMgmt"
					action="admin_terminalMgmt_Save" cssStyle="width:100%;" >
					<tr>
						<td>
							<table width="100%">
								<tr>
									<td>
										<div id="result" style="width:100%;"></div>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</s:form>

			</div>
		</div>




	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: admin_terminalMgmt_Menu.jsp,v $ $Revision: 1.3 $
</code>