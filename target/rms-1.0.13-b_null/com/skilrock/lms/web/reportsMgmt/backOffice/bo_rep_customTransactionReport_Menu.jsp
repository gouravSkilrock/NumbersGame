<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/report.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript">
			function getRetailerUserList(agentOrgId) {
				if(agentOrgId != -1){
				
					var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/getRetailerUserNameList.action?orgId=" + agentOrgId);
					var firstArr = text.data.split(":");
					//alert(firstArr);
					var retObj = _id.o("retOrgId");
					document.getElementById("retOrgId").options.length = 1;
					for (var i = 0; i < firstArr.length - 1; i++) {
						var retNameArray = firstArr[i].split("|");
						var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
						retObj.options[i + 1] = opt;
					}
				} 
			}
			function getAgentList() {
				var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/getAgentNameList.action?orgType=AGENT");
				var firstArr = text.data.split(":");
				var retObj = _id.o("agentOrgId");
				for (var i = 0; i < firstArr.length - 1; i++) {
					var retNameArray = firstArr[i].split("|");
					var opt = new Option(retNameArray[0].toUpperCase(), retNameArray[1]);
					retObj.options[i + 1] = opt;
				}
			}
			
		</script>
		
		<script type="text/javascript">
	
			function getTableData() {
			var info= document.getElementById("reportinfodiv").innerHTML;
			var tblData = info+document.getElementById("tableDataDiv").innerHTML;
			
			
				document.getElementById('tableValue').value = tblData;
				return false;
			}
							
			function verifyField(){
			//alert(_id.o('retOrgId').value);
						if(_id.o('agentOrgId').value==-1){
				alert('<s:text name="error.select.agt.name.fst"/>');
				//_id.o('agentOrgId').focus();
				return false;		
			}
			
			else if(_id.o('retOrgId').value==-1 ||_id.o('retOrgId').value=="" ){
				alert('<s:text name="error.select.ret.name.fst"/>');
				//_id.o('retOrgId').focus();
				return false;		
			}
			return true;
						}
					function clearDiv(){
						_id.i("down","");
					
					_id.i("dates","");
			if(_id.o('agentOrgId').value==-1){
				alert('<s:text name="error.select.agt.name.fst"/>');
			//	_id.o('agentOrgId').focus();
				return false;		
			}
			
			else if(_id.o('retOrgId').value==-1){
				alert('<s:text name="error.select.ret.name.fst"/>');
				//_id.o('retOrgId').focus();
				return false;		
			}
			
			}

		</script>

	</head>

	<body onload="getAgentList();">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>


		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.ret.txn.rep" />
				</h3>

				<s:form action="bo_rep_custom_transaction_report_search"
					onsubmit="return verifyField();">

					<table border="0" cellpadding="2" cellsapacing="0">
						<tr>
							<td>
								<div id="errorId"></div>
							</td>
						</tr>
					</table>
					<table border="0" cellpadding="2" cellsapacing="0">

						<tr>
							<td colspan="2">
								<s:actionerror />
								<s:fielderror />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<span id="list1" onclick="changeValue()"></span>


							</td>
						</tr>

						<tr id="selType">
							<td width="100">
							</td>
							<td>
								<s:select cssClass="option" name="agentOrgId"
									onchange="getRetailerUserList(this.value); " id="agentOrgId"
									list="{}" key="label.agt.name" headerValue="--%{getText('label.please.select')}--"
									headerKey="-1" required="true"></s:select>
							</td>
						</tr>
						<tr id="selType">
							<td width="100">

							</td>
							<td>
								<s:select cssClass="option" key="label.ret.name"
									name="retOrgId" id="retOrgId" headerKey="-1"
									headerValue="%{getText('label.please.select')}" list="{}" />
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="3" cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									<%
										Calendar prevCal = Calendar.getInstance();
											String startDate = CommonMethods
													.convertDateInGlobalFormat(new java.sql.Date(prevCal
															.getTimeInMillis()).toString(), "yyyy-mm-dd",
															"yyyy-mm-dd");
									%>
									<tr>
										<td>
											<label class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="label.start.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="start_date" id="start_date"
												value="<%=startDate%>" readonly size="12">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<%=startDate%>', false, '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="label.end.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_date"
												value="<%=startDate%>" readonly size="12">
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<%=startDate%>',false, '')" />
										</td>
									</tr>
								</table>
							</td>
						</tr>


						<tr>
							<td align="right">


								<s:submit name="search" key="btn.srch" align="right"
									targets="down" theme="ajax" cssClass="button" />


							</td>
						</tr>
					</table>
				</s:form>
				<div id="down"></div>
				<div id="result" style="overflow-x: auto; overflow-y: hidden;"></div>
			</div>
		</div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_customTransactionReport_Menu.jsp,v $ $Revision:
	1.1.2.4.6.1 $
</code>