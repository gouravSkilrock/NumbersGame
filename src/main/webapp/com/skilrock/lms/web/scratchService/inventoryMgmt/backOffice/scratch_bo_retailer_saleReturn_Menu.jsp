<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/AutoComplete.css"
	media="screen" type="text/css" />
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/AutoComplete.js"></script>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head theme="ajax" debug="false" />


<script>
	var path = "<%=request.getContextPath()%>";
		function getRetailerUserList(agentOrgId) {
			if(agentOrgId != -1){
				var text = _ajaxCall("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/getRetailerUserNameList.action?orgId="
					+ agentOrgId);
			var firstArr = text.data.split(":");
			//alert(firstArr);
			var retObj = _id.o("retOrgName");
			document.getElementById("retOrgName").options.length = 1;
			for ( var i = 0; i < firstArr.length - 1; i++) {
				var retNameArray = firstArr[i].split("|");
				var opt = new Option(retNameArray[0].toUpperCase(),
						retNameArray[1]);
				retObj.options[i + 1] = opt;
			}
		}
	}

	function saleReturnNewAjax(url, val) {
		replaceDiv = val;
		if (val == "gameDiv") {
			document.getElementById("orgLoading").innerHTML = '<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
			disableTable();
			emptyAllFields();
		}
		if (url.match("gameName")) {
			document.getElementById("gameLoading").innerHTML = '<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
			url = url + escape(document.getElementById("retOrgName").value);
			replaceDiv = "gameName";
			disableTable();
			emptyAllFields();
		}
		_resp = _ajaxCall(url);
		if (_resp.result) {
			if (replaceDiv == "gameDiv") {
				document.getElementById("gameDiv").innerHTML = _resp.data;
				document.getElementById("orgLoading").innerHTML = '';
				if (document.getElementById("gameName").options.length < 2) {
					disableTable();
				}
			}
			if (replaceDiv == "gameName") {
				getPacknBookArray(_resp.data);
				document.getElementById("gameLoading").innerHTML = '';
			}
		} else {
			alert("Problem: ");
		}
	}
</script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/saleReturn.js"></script>
</head>

<body onload="disableTable(),getAgentList()">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3><s:text name="label.sale.return"/></h3>

			<s:form name="sale" id="sale"
				action="bo_scratch_boToRetailerDirectSaleRefund_Verify"
				method="POST">

				<div id="errorDiv" style="color: red">&nbsp; &nbsp;&nbsp;</div>
				<s:hidden id="agtName" name="organization_Name" />
				<table border="0" width="100%" cellpadding="2" cellspacing="0">
					<tr>
						<td><s:text name="label.select"/> <s:property value="#application.TIER_MAP.AGENT" />
							<s:text name="label.org"/> :</td>
						<td nowrap="nowrap" colspan="2"><s:select theme="simple"
								id="agtOrgName" cssClass="option" list="{}" name="agentOrgId"
								emptyOption="false" headerKey="-1"
								headerValue="%{getText('label.please.select')}"
								onchange="getRetailerUserList(this.value)" />
							<div id="orgLoading" style="position: absolute;"></div>
						</td>
					</tr>
					<tr>
						<td><s:text name="label.select"/> <s:property value="#application.TIER_MAP.RETAILER" />
							<s:text name="label.org"/> :</td>
						<td nowrap="nowrap" colspan="2"><s:select theme="simple"
								id="retOrgName" cssClass="option" list="{}" name="retOrgName"
								emptyOption="false" headerKey="-1"
								headerValue="%{getText('label.please.select')}"
								onchange="saleReturnAjax('bo_im_saleReturnRetailer_GameList.action?orgId='+ escape(this.value),'gameDiv')" />
							<!-- <div id="orgLoading" style="position: absolute;"></div> -->
						</td>
					</tr>

					<tr>
						<td><s:text name="label.game.name"/> :</td>
						<td>
							<div id="gameDiv">
								<s:select theme="simple" name="gameName" id="gameName"
									headerKey="-1" cssClass="option"
									headerValue="%{getText('label.please.select')}" list="{}" />
							</div></td>
						<td>
							<div id="gameLoading">
						</td>
					</tr>

				</table>

				<div id="completeDiv"
					style="position: absolute; background-color: #CCCCCC ;opacity:1 ;filter:alpha(opacity=30)">
				</div>
				<table id="completeTable" border="1" bordercolor="black"
					width="100%">
					<tr>
						<td><div id="packRow">
								<table id="packTable" width="100%" border="0" cellpadding="2"
									cellspacing="0">
									<tr>
										<td align="left" colspan="2"><input type="button"
											value="<s:text name="btn.add.more.pack"/>" name="addPack" id="addPack"
											class="button" onclick="addPackRow()" /></td>
									</tr>
									<tr>
										<td colspan="3">
											<div id="packErrDiv"></div></td>
									</tr>
									<tr>
										<td width="20%"><s:label theme="simple"
												key="label.pack.nbr" /></td>
										<td width="15%"><input type="text" name="packNumber"
											id="packNbr1" value="" size="16" /></td>
										<td><div id="packNbr1Div">&nbsp;</div></td>
									</tr>
								</table>
							</div></td>
					</tr>
					<tr>
						<td>
							<table id="bookSeriesTable" border="0" width="100%"
								cellpadding="2" cellspacing="0">
								<tr>
									<td align="left" colspan="2"><input type="button"
										value="<s:text name="btn.add.book.series"/>" name="addBookSeries"
										id="addBookSeries" class="button" onclick="addBookSeriesRow()" />
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<div id="bookSeriesErrDiv"></div></td>
								</tr>
								<%
									for (int i = 0; i < 3; i++) {
								%>
								<tr>
									<td width="22%"><s:label theme="simple"
											key="label.book.no.from" />:</td>
									<td width="15%"><input type="text" name="bookSeriesFrom"
										id="bookNbrFmSe<%=i + 1%>" value="" size="16" /></td>
									<td width="8%"><s:label theme="simple" key="label.to" />:</td>
									<td width="15%"><input type="text" name="bookSeriesTo"
										id="bookNbrToSe<%=i + 1%>" value="" size="16" /></td>
									<td><div id="bookNbrSe<%=i + 1%>Div">&nbsp;</div></td>
								</tr>
								<%
									}
								%>
							</table></td>
					</tr>
					<tr>
						<td>
							<table id="bookTable" border="0" width="100%" cellpadding="2"
								cellspacing="0">
								<tr>
									<td align="left" colspan="2"><input type="button"
										value="<s:text name="btn.add.more.book"/>" name="addBook" id="addBook"
										class="button" onclick="addBookRow()" /></td>
								</tr>
								<tr>
									<td colspan="3">
										<div id="bookErrDiv"></div></td>
								</tr>
								<%
									for (int i = 0; i < 5; i++) {
								%>
								<tr>
									<td width="20%"><s:label theme="simple"
											key="label.book.no" />:</td>
									<td width="15%"><input type="text" name="bookNumber"
										id="bookNbr<%=i + 1%>" value="" size="16" /></td>
									<td><div id="bookNbr<%=i + 1%>Div">&nbsp;</div></td>
								</tr>
								<%
									}
								%>
							</table></td>
					</tr>
					<tr>
						<td>
							<table width="100%">
								<tr>
									<td align="center"><s:submit id="verify" theme="simple"
											key="btn.verify&save" onclick="return VerifyBooks('BO')"
											cssClass="button" /></td>
								</tr>
							</table></td>
					</tr>
				</table>
			</s:form>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	scrathc_bo_retailer_saleReturn_Menu.jsp,v $ $Revision: 1.3 $ </code>