<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script>var path = "<%=request.getContextPath()%>";
			</script>
			<script>var projectName="<%=request.getContextPath()%>"</script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
			<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/slider/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/ola/js/commonOla.js"></script>
		<script src="<%=request.getContextPath() %>/com/skilrock/ola/js/searchPlayer.js"></script>
		
	</head>
	<body >
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.srch.plr"/> 
					
					
				</h3>


				<s:form name="PlrSearch_Result" id="PlrSearch_Result" action="olaBoPlrSearch_Result" onsubmit="return validateForm()">
					<div style="color: red; text-align: left;"><s:actionmessage /></div>
						<s:actionerror />
							<table border="0" width="100%" cellpadding="2" cellspacing="0">
					<tr>
						<td colspan="2" >
						<div style="color:red;margin-left: 100px" id="agentName_e"></div>
						</td>
					</tr>
					<tr>
						<td>
								<s:select label="%{getText('label.agt.name')}" name="agentOrgId" id="agentOrgId" headerKey="-1" headerValue="%{getText('label.please.select')}"
								list="{}" cssClass="option" required="true" />
 						</td>
					</tr>
					<tr>
						<td colspan="2">
						<div style="color:red;margin-left: 100px" id="retOrgId_e"></div>
						</td>
					</tr>
					<tr>
						<td>
								<s:select label="%{getText('Retailer')}" name="retOrgId" id="retOrgId" headerKey="-1" headerValue="%{getText('label.please.select')}"
								list="{}" cssClass="option" required="true"/>
 						</td>
					</tr>
					<tr>
							<td colspan="2">
								<div style="color: red; text-align: center" id="walletName_e"></div>
							</td>
					</tr>
					<tr>
							
							<td>
									<!--<s:select label="Wallet Name" name="walletName" headerKey="-1"
										id="walletName" headerValue="--ALL--" list="{}"
										cssClass="option" />
								--><s:select key="label.wallet.name" id="walletName" name="walletName"  list="walletDetailsMap" listKey="value.walletDevName" listValue="value.walletDisplayName"
										cssClass="option" required="true" headerKey="-1" headerValue="%{getText('label.ALL')}"  onchange="displayPassword(this.value);"></s:select>
							</td>
					</tr>
					<tr>
							<td colspan="2">
								<div style="color: red; text-align: center" id="regType_e"></div>
							</td>
					</tr>
					<tr>
							
							<td>
									<s:select key="label.reg.type" name="regType" headerKey="-1"
										id="regType" headerValue="--ALL--" list="#{'DIRECT':getText('label.direct'),'OLA':getText('label.ola')}"
										cssClass="option" />
								</td>
					</tr>
					<tr>
							<td colspan="2">
								<div style="color: red; text-align: center" id="regDate_e"></div>
							</td>
					</tr>
					<tr>
					<td colspan="2" ><div id="dateDiv"  style="display:block;margin-left: 50px ">
					<table>
						<tr>
							<td >
								<label class="label">
									<s:text name="label.slct.reg.date.from"/>:
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<input type="text" name="regFromDate" id="regFromDate"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('regFromDate'),'yyyy-MM-dd', this, '<s:property value="#session.presentDate"/>','<s:property value="depDate"/>','<s:property value="#session.presentDate"/>')" />
							</td>
						</tr>
							<tr>
							<td align="right">
								<label class="label">
									<s:text name="label.slct.reg.date.to"/>:
									<span class="required">*</span>:
								</label>
							</td>
							<td>
								<input type="text" name="regToDate" id="regToDate"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('regToDate'),'yyyy-MM-dd', this, '<s:property value="#session.presentDate"/>','<s:property value="depDate"/>','<s:property value="#session.presentDate"/>')" />
							</td>
						</tr>
						
					</table>
					</div></td>
					
					</tr>
					<tr>
						<td colspan="2">
						<div style="color:red;margin-left: 100px" id="alias_e"></div>
						</td>
					</tr>
					<tr>
							<td align="right">
								<label class="label">
									<s:text name="label.plyr.alias"/>:
								</label>
							</td>
							<td>
							<input type="text"  name="alias" id="alias" maxlength="30"/>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="search"  key="btn.srch" align="center"
									targets="reportDiv" theme="ajax" cssClass="button" />
								<div id="orgLoading" style="position: absolute;"></div>
							</td>
						</tr>
					</table>
							
							
						
					
					<div id="completeDiv"
						style="position: absolute; background-color: #CCCCCC; opacity: 1; filter: alpha(opacity = 60); display: none; width: 830px">
						<center> <b><font size="3">
							<s:text name="msg.wait.data.process"/>......</font> </b> </center>
					</div>

					<div id="resultDiv" style="display: none">
					</div>
					<br/><br/>
					<br/><br/>
					
					<div id="reportDiv">
					</div>
				</s:form>
			</div>
		</div>

	</body>
	
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_ola_search_plr_menu.jsp,v $ $Revision: 1.3 $
</code>
