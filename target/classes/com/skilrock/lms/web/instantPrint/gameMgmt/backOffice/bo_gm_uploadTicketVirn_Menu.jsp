<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/instantPrint/gameMgmt/backOffice/js/invetoryUpload.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body
		onload="retrieveURL('bo_gm_fetchGameList.action?gameType=NEW','game'),disableDates('NEW')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					Upload Game Inventory
				</h3>

				<s:actionerror />
				<s:form name="uploadVirn" method="post"
					action="bo_gm_uploadInventory_Save" enctype="multipart/form-data"
					onsubmit="return validateDates()">
					<table border="0" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC">
						<tr>
							<td colspan="2" nowrap="nowrap" align="left">
								<s:radio id="gtype" label="Select Type" name="gameType"
									value="'NEW'"
									list="#{'NEW':'New Games','OPEN':'Old Open Games'}"
									onclick="retrieveURL('bo_gm_fetchGameList.action?gameType=' + this.value,'game'), disableDates(this.value)" />
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="gamename1"></div>
							</td>
						</tr>
						<tr>
							<td align="left">
								<s:text name="Select Game:"></s:text>
							</td>
							<td>
								<strong><span id="game"> </span> </strong>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates1"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="startDate1" align="center"></div>
							</td>
						</tr>
								<%
									Calendar prevCal = Calendar.getInstance();
										String sDate = CommonMethods
												.convertDateInGlobalFormat(new java.sql.Date(prevCal
														.getTimeInMillis()).toString(), "yyyy-mm-dd",
														(String) session.getAttribute("date_format"));
									session.setAttribute("sDate",sDate);									
								%>
						<s:set name="stDate" id="stDate" value="#session.sDate" />
						<s:hidden theme="simple" id="r" value="%{stDate}"></s:hidden>
						<s:set name="st1Date" value="#application.date_format" />
						<tr>
							<td colspan="2">

								<div id="gameStartDate">
									<label class="label">
										Start Date
										<span class="required">*</span>:
									</label>
									<input type="text" name="startDate" id="startDate"
										value="<s:property value="%{stDate}"/>" readonly size="12">
									<input type="button" id="startDateButton"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('startDate'),'dd-mm-yyyy', this, '<s:property value="%{stDate}"/>', '<s:property value="%{stDate}"/>', false)" />

									<input type="hidden" id="startDateHid"
										value="#session.presentDate" />
									<div id="startDateDiv" style="display: none"></div>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="saleendDate1" align="center"></div>
							</td>
						</tr>


						<tr>
							<td colspan="2">
								<label class="label">
									Sale End Date
									<span class="required">*</span>:
								</label>
								<input type="text" name="saleendDate" id="saleendDate"
									value="<s:property value="%{stDate}"/>" readonly size="12">
								<input type="button" id="saleendDateButton"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('saleendDate'),'dd-mm-yyyy', this, '<s:property value="%{stDate}"/>', '<s:property value="%{stDate}"/>', false)" />

								<input type="hidden" id="saleendDateHid"
									value="#session.presentDate" />
								<div id="saleendDateDiv" style="display: none"></div>
							</td>
						</tr>


						<tr>
							<td colspan="2">
								<div id="pwtendDate1" align="center"></div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<label class="label">
									PWT End Date
									<span class="required">*</span>:
								</label>
								<input type="text" name="pwtendDate" id="pwtendDate"
									value="<s:property value="%{stDate}"/>" readonly size="12">
								<input type="button" id="pwtendDateButton"
									style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('pwtendDate'),'dd-mm-yyyy', this, '<s:property value="%{stDate}"/>', '<s:property value="%{stDate}"/>', false)" />

								<input type="hidden" id="endDateHid"
									value="#session.presentDate" />
								<div id="endDateDiv" style="display: none"></div>

							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="packNumber"></div>
								<table id="addrowtb2" border="1" width="100%" cellpadding="3"
									cellspacing="0" style="bordercolor: #CCCCCC">
									<tr>
										<th colspan="4" align="center" style="width: 45%">
											<s:label theme="simple"
												value="Enter Pack Number Series (From/To)" />
										</th>
									</tr>
									<tr>
										<td style="width: 10%">
											<s:label theme="simple" value="From" />
										</td>
										<td style="width: 15%">
											<s:textfield theme="simple" size="10" name="packNumberFrom"
												id="pnumfrom" onkeypress=" return isValidEntries()"
												onblur="validateFrom(this.id)" onchange="disableSubmit()" />
										</td>
										<td style="width: 5%">
											<s:label theme="simple" value="To" />
										</td>
										<td style="width: 15%">
											<s:textfield theme="simple" size="10" name="packNumberTo"
												id="pnumto" onkeypress=" return isValidEntries()"
												onblur="validateFromTo(this.id)" onchange="disableSubmit()"></s:textfield>
										</td>
									</tr>
								</table>
							</td>
						</tr>



						<tr>
							<td>
								<div id="file1">
								</div>
							</td>
						</tr>
						<tr>
							<td align="left">
								<s:file label="Select File to Upload Virn" id="file"
									name="invFile" cssClass="button" accept="text/*"></s:file>
							</td>
						</tr>

						<tr>
							<td align="right">
								<s:submit value="Upload Inventory" cssClass="button"></s:submit>
							</td>
						</tr>

					</table>
				</s:form>

			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_gm_uploadTicketVirn_Menu.jsp,v $ $Revision: 1.2 $
</code>
